package fr.lovotech.galaxy.qa.backoffice.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import com.mongodb.BasicDBObject;
import fr.lovotech.galaxy.qa.backoffice.core.CampaignTestRequest;
import fr.lovotech.galaxy.qa.backoffice.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import fr.lovotech.galaxy.qa.backoffice.core.ApplicationRequest;
import fr.lovotech.galaxy.qa.backoffice.exception.BusinessException;
import fr.lovotech.galaxy.qa.backoffice.exception.DomainErrorCodes;
import  fr.lovotech.galaxy.qa.backoffice.repository.ApplicationRepository;
import fr.lovotech.galaxy.qa.backoffice.repository.CustomApplicationRepository;

@Service
public class ApplicationService {
    private final Logger log = LoggerFactory.getLogger(ApplicationService.class);

    private final ApplicationRepository applicationRepository;
    private final CustomApplicationRepository customApplicationRepository;
    private final MongoTemplate mongoTemplate;
    private final ApplicationVersionService applicationVersionService;

    public ApplicationService(ApplicationRepository ApplicationRepository, CustomApplicationRepository customApplicationRepository, MongoTemplate mongoTemplate, ApplicationVersionService applicationVersionService) {
        this.applicationRepository = ApplicationRepository;
        this.customApplicationRepository = customApplicationRepository;
        this.mongoTemplate = mongoTemplate;
        this.applicationVersionService = applicationVersionService;
    }

    public Application createApplication(ApplicationRequest applicationRequest) {
        Application applicationExist = verifyIfCodeApplicationExist(applicationRequest.getCode());
        if (applicationExist == null){
            Application application = applicationRequest.buildApplication();
            application.setExternalUid(ReferenceUID.builder().build());
            application.setCreationDate(new Date());
            return saveOrUpdate(application);
        }
        else if(applicationExist.getIsActive()){
            log.info("[INFO] Application with code=[{}] has already an existing active application with id=[{}]",
                    applicationExist.getCode(), applicationExist.getId());
            throw new BusinessException(DomainErrorCodes.APPLICATION_CODE_EXISTS);
        }else {
            log.info("[INFO] Application with code=[{}] has already an existing deleted application with id=[{}]",
                    applicationExist.getCode(), applicationExist.getId());
            throw new BusinessException(DomainErrorCodes.APPLICATION_CODE_DELETED);
        }
    }

    public PageResponse<Application> getAllApplications(Pageable paging, Map<String, Object> inputs) throws ParseException {
        Query query = prepareQuerySearch(inputs);
        return new PageResponse<Application>(applicationRepository.findAll(query,paging));
    }
    private Query prepareQuerySearch(Map<String, Object> inputs) throws ParseException {
        Query query = new Query();
        query.addCriteria(Criteria.where("isActive").is(true));
        for (Map.Entry<String, Object> input : inputs.entrySet()) {
            switch (input.getKey()) {
                case "creationDate":
                    String dateFromInputString = input.getValue().toString();
                    Date parsedDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateFromInputString);
                    Date endDate =new Date( parsedDate.getTime()+ (1000 * 60 * 60 * 24));
                    query.addCriteria(Criteria.where("creationDate").gte(parsedDate).lt(endDate));
                    break;
                case "code":
                    query.addCriteria(Criteria.where("code").regex(containsTextRegEx(input.getValue().toString()), "i"));
                    break;
                case "applicationStatus":
                    query.addCriteria(Criteria.where("applicationStatus").regex(containsTextRegEx(input.getValue().toString()), "i"));
                    break;
                case "applicationPhase":
                    query.addCriteria(Criteria.where("applicationPhase").regex(containsTextRegEx(input.getValue().toString()), "i"));
                    break;
                case "fullName":
                    query.addCriteria(Criteria.where("createdBy.user.fullName").regex(containsTextRegEx(input.getValue().toString()), "i"));
                    break;
                case "comment":
                    query.addCriteria(Criteria.where("configurationMLS.comment").regex(containsTextRegEx(input.getValue().toString()), "i"));
                    break;
                case "label":
                    query.addCriteria(Criteria.where("configurationMLS.label").regex(containsTextRegEx(input.getValue().toString()), "i"));
                    break;
                case "languageCode":
                    query.addCriteria(Criteria.where("configurationMLS.languageCode").regex(containsTextRegEx(input.getValue().toString()), "i"));
                    break;
                default:
                    break;
            }
        }
        return  query;
    }
    private String containsTextRegEx(String value) {
        return MongoRegexCreator.INSTANCE.toRegularExpression(value, MongoRegexCreator.MatchMode.CONTAINING);
    }

    public Application updateApplication(String id, ApplicationRequest newApplicationRequest) {

        Application existingAppWithSameCode = verifyIfCodeApplicationExist(newApplicationRequest.getCode());
        Application oldApplication = getApplicationById(id);
        Application newApplication = newApplicationRequest.buildApplication();
        if (existingAppWithSameCode == null || oldApplication.getCode().equals(newApplicationRequest.getCode())) {
            oldApplication.setId(null);
            oldApplication.setEndDate(new Date());
            oldApplication.setIsActive(false);
            saveOrUpdate(oldApplication);
            newApplication.setId(id);
            newApplication.setExternalUid(ReferenceUID.builder().build());
            newApplication.setCreationDate(new Date());
            return saveOrUpdate(newApplication);
        }
        else if(existingAppWithSameCode.getIsActive()){
            log.info("[INFO] Application with code=[{}] has already an existing active application with id=[{}]",
                    existingAppWithSameCode.getCode(), existingAppWithSameCode.getId());
            throw new BusinessException(DomainErrorCodes.APPLICATION_CODE_EXISTS);
        }else {
            log.info("[INFO] Application with code=[{}] has already an existing deleted application with id=[{}]",
                    existingAppWithSameCode.getCode(), existingAppWithSameCode.getId());
            throw new BusinessException(DomainErrorCodes.APPLICATION_CODE_DELETED);
        }
    }

    public Application saveOrUpdate(Application Application) {
        Application = applicationRepository.save(Application);
        return Application;
    }

    public Application getApplicationById(String id) {
        Optional<Application> Application = applicationRepository.findById(id);
        if (Application.isEmpty()) {
            log.error("[ERROR] Application with id {} not found: ", id);
            throw new BusinessException(DomainErrorCodes.APPLICATION_NOT_FOUND);
        }
        return Application.get();
    }

    public void deleteApplication(String id) {
        log.debug("Request to delete Application : {}", id);
        Pageable pageable  = PageRequest.of(0,10);
        PageResponse<ApplicationVersion> applications = this.applicationVersionService.findVersionsByApplicationId(pageable, id);
        if(!applications.getContent().isEmpty())
            throw new BusinessException(DomainErrorCodes.APPLICATION_HAS_VERSIONS);
        Application application = getApplicationById(id);
        if (application != null) {
            application.setIsActive(false);
            application.setEndDate(new Date());
            application.setIsDeleted(true);
            applicationRepository.save(application);
        }
    }

    public Application verifyIfCodeApplicationExist (String code){
        Criteria criteria = new Criteria();
        criteria.orOperator(Criteria.where("isActive").is(true), Criteria.where("isDeleted").is(true));
        Query query = new Query(criteria);
        query.addCriteria(Criteria.where("code").regex(exactText(code),"i"));
        return applicationRepository.findAll(query).isEmpty() ? null : applicationRepository.findAll(query).get(0);
    }

    private String exactText(String value) {
        return MongoRegexCreator.INSTANCE.toRegularExpression(value, MongoRegexCreator.MatchMode.EXACT);
    }


    public Application deleteConfigurationML(String id, String codeLanguage , ApplicationRequest applicationRequest ){
        Application application = getApplicationById(id);
        applicationRequest.setConfigurationMLS( application.getConfigurationMLS().stream()
                .filter(config->!config.getLanguageCode().equals(codeLanguage)).collect(Collectors.toList()));
        application.setIsActive(false);
        application.setEndDate(new Date());
        saveOrUpdate(application);
        return createApplication(applicationRequest);
    }
}
