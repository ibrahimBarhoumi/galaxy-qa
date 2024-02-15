package fr.lovotech.galaxy.qa.backoffice.service;

import fr.lovotech.galaxy.qa.backoffice.domain.ScenarioVersion;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Collation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.MongoRegexCreator;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import fr.lovotech.galaxy.qa.backoffice.core.ApplicationVersionRequest;
import fr.lovotech.galaxy.qa.backoffice.domain.ApplicationVersion;
import fr.lovotech.galaxy.qa.backoffice.domain.PageResponse;
import fr.lovotech.galaxy.qa.backoffice.domain.ReferenceUID;
import fr.lovotech.galaxy.qa.backoffice.exception.BusinessException;
import fr.lovotech.galaxy.qa.backoffice.exception.DomainErrorCodes;
import fr.lovotech.galaxy.qa.backoffice.repository.ApplicationVersionRepository;
@Service
public class ApplicationVersionService {
    private final Logger log = LoggerFactory.getLogger(ApplicationVersionService.class);

    private final fr.lovotech.galaxy.qa.backoffice.repository.ApplicationVersionRepository applicationVersionRepository;
    private final MongoTemplate mongoTemplate;
    private final ScenarioVersionService scenarioVersionService;

    public ApplicationVersionService(ApplicationVersionRepository applicationVersionRepository, MongoTemplate mongoTemplate, ScenarioVersionService scenarioVersionService) {
        this.applicationVersionRepository = applicationVersionRepository;
        this.mongoTemplate = mongoTemplate;
        this.scenarioVersionService = scenarioVersionService;
    }

    public ApplicationVersion createApplicationVersion(ApplicationVersionRequest applicationVersionRequest) {


        ApplicationVersion existingAV = verifyIfApplicationVersionWithSameCodeExist(applicationVersionRequest.getCode());

        if (existingAV == null) {
            ApplicationVersion applicationVersion = applicationVersionRequest.buildApplicationVersion();
            applicationVersion.setExternalUid(ReferenceUID.builder().build());
            return saveOrUpdate(applicationVersion);
        } else if(existingAV.getIsActive()) {
            log.info("[INFO] ApplicationVersion with code=[{}] has already an existing as active with id=[{}]",existingAV.getCode(),existingAV.getId());
            throw new BusinessException(DomainErrorCodes.APPLICATIONVERSION_EXIST);
        }
        else{
            log.info("[INFO] ApplicationVersion with code=[{}] has already an existing as deleted with id=[{}]",existingAV.getCode(),existingAV.getId());
            throw new BusinessException(DomainErrorCodes.APPLICATIONVERSION_EXIST_DELETED);
        }
    }

    public PageResponse<ApplicationVersion> getAllApplicationVersions(Pageable paging, Map<String, Object> inputs) {
        Query query = prepareQuerySearch(inputs);
        return new PageResponse<ApplicationVersion>(applicationVersionRepository.findAll(query,paging));
    }

    private Query prepareQuerySearch(Map<String, Object> inputs){
        Query query = new Query();
        query.addCriteria(Criteria.where("isActive").is(true));
        for (Map.Entry<String, Object> input : inputs.entrySet()) {
            switch (input.getKey()) {
                case "creationDate":
                    query.addCriteria(Criteria.where("creationDate").regex(containsTextRegEx(input.getValue().toString()), "i"));
                    break;
                case "code":
                    query.addCriteria(Criteria.where("code").regex(containsTextRegEx(input.getValue().toString()), "i"));
                    break;
                case "application":
                    query.addCriteria(Criteria.where("application.$id").is(new ObjectId(input.getValue().toString())));
                    break;
                case "status":
                    query.addCriteria(Criteria.where("status").regex(containsTextRegEx(input.getValue().toString()), "i"));
                    break;
                case "host":
                    query.addCriteria(Criteria.where("host").regex(containsTextRegEx(input.getValue().toString()), "i"));
                    break;
                case "url":
                    query.addCriteria(Criteria.where("url").regex(containsTextRegEx(input.getValue().toString()), "i"));
                    break;
                case "port":
                    query.addCriteria(Criteria.where("port").regex(containsTextRegEx(input.getValue().toString()), "i"));
                    break;
                case "username":
                    query.addCriteria(Criteria.where("username").regex(containsTextRegEx(input.getValue().toString()), "i"));
                    break;
                case "password":
                    query.addCriteria(Criteria.where("password").regex(containsTextRegEx(input.getValue().toString()), "i"));
                    break;
                case "dbHost":
                    query.addCriteria(Criteria.where("dbHost").regex(containsTextRegEx(input.getValue().toString()), "i"));
                    break;
                case "dbPort":
                    query.addCriteria(Criteria.where("dbPort").regex(containsTextRegEx(input.getValue().toString()), "i"));
                    break;
                case "dbUsername":
                    query.addCriteria(Criteria.where("dbUsername").regex(containsTextRegEx(input.getValue().toString()), "i"));
                    break;
                case "dbPassword":
                    query.addCriteria(Criteria.where("dbPassword").regex(containsTextRegEx(input.getValue().toString()), "i"));
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


    public ApplicationVersion updateApplicationVersion(String id, ApplicationVersionRequest newApplicationTestRequest) {

        ApplicationVersion existingAppTestWithSameCode = verifyIfApplicationVersionWithSameCodeExist(newApplicationTestRequest.getCode());
        ApplicationVersion oldApplicationTest = getApplicationVersionById(id);
        ApplicationVersion newApplicationTest = newApplicationTestRequest.buildApplicationVersion();
        if (existingAppTestWithSameCode == null || oldApplicationTest.getCode().equals(newApplicationTestRequest.getCode())) {

            oldApplicationTest.setId(null);
            oldApplicationTest.setEndDate(new Date());
            oldApplicationTest.setIsActive(false);
            saveOrUpdate(oldApplicationTest);
            newApplicationTest.setId(id);
            newApplicationTest.setExternalUid(ReferenceUID.builder().build());
            newApplicationTest.setCreationDate(new Date());
            return saveOrUpdate(newApplicationTest);
        }
        else if(existingAppTestWithSameCode.getIsActive()) {
            log.info("[INFO] ApplicationVersion with code=[{}] has already an existing as active with id=[{}]",existingAppTestWithSameCode.getCode(),existingAppTestWithSameCode.getId());
            throw new BusinessException(DomainErrorCodes.APPLICATIONVERSION_EXIST);
        }
        else {
            log.info("[INFO] ApplicationVersion with code=[{}] has already an existing as deleted with id=[{}]",existingAppTestWithSameCode.getCode(),existingAppTestWithSameCode.getId());
            throw new BusinessException(DomainErrorCodes.APPLICATIONVERSION_EXIST_DELETED);
        }
    }

    public ApplicationVersion saveOrUpdate(ApplicationVersion applicationVersion) {
        applicationVersion = applicationVersionRepository.save(applicationVersion);
        return applicationVersion;
    }

    public ApplicationVersion getApplicationVersionById(String id) {
        Optional<ApplicationVersion> applicationVersion = applicationVersionRepository.findById(id);
        if (applicationVersion.isEmpty()) {
            log.error("[ERROR] ApplicationVersion with id {} not found: ", id);
            throw new BusinessException(DomainErrorCodes.APPLICATION_VERSION_NOT_FOUND);
        }
        return applicationVersion.get();
    }

    public void deleteApplicationVersion(String id) {
        log.debug("Request to delete ApplicationVersion : {}", id);
        Pageable pageable  = PageRequest.of(0,10);
        PageResponse<ScenarioVersion> scenarioVersions = this.scenarioVersionService.findScenariosByVersionId(pageable, id);
        if(!scenarioVersions.getContent().isEmpty())
            throw new BusinessException(DomainErrorCodes.VERSION_HAS_SCENARIOS);
        ApplicationVersion applicationVersion = getApplicationVersionById(id);
        if (applicationVersion != null) {
            applicationVersion.setIsDeleted(true);
            applicationVersion.setIsActive(false);
            applicationVersion.setEndDate(new Date());
            applicationVersionRepository.save(applicationVersion);
        }
    }

    private ApplicationVersion verifyIfApplicationVersionWithSameCodeExist(String code) {
        Criteria criteria = new Criteria();
        criteria.orOperator(Criteria.where("isActive").is(true), Criteria.where("isDeleted").is(true));
        Query query = new Query(criteria);
        query.addCriteria(Criteria.where("code").regex(exactText(code),"i"));
        return applicationVersionRepository.findAll(query).isEmpty() ? null : applicationVersionRepository.findAll(query).get(0);
    }

    private String exactText(String value) {
        return MongoRegexCreator.INSTANCE.toRegularExpression(value, MongoRegexCreator.MatchMode.EXACT);
    }

    public ApplicationVersion deleteConfigurationML(String id, String codeLanguage ,ApplicationVersionRequest applicationVersionRequest ){
        ApplicationVersion applicationVersion = getApplicationVersionById(id);
        applicationVersionRequest.setConfigurationMLS( applicationVersion.getConfigurationMLS().stream()
          .filter(config->!config.getLanguageCode().equals(codeLanguage)).collect(Collectors.toList()));
          applicationVersion.setIsActive(false);
          applicationVersion.setEndDate(new Date());
        saveOrUpdate(applicationVersion);
        return createApplicationVersion(applicationVersionRequest);
    }
    public PageResponse<ApplicationVersion> findVersionsByApplicationId (Pageable paging,String id){
        Criteria criteria = new Criteria();
        Query query = new Query(criteria);
        query.addCriteria(Criteria.where("isActive").is(true));
        query.addCriteria(Criteria.where("application.$id").is(new ObjectId(id)));
        return new PageResponse<ApplicationVersion>(applicationVersionRepository.findAll(query,paging));
    }
}
