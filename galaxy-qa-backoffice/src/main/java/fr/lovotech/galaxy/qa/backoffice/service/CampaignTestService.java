package fr.lovotech.galaxy.qa.backoffice.service;

import fr.lovotech.galaxy.qa.backoffice.core.CampaignTestRequest;
import fr.lovotech.galaxy.qa.backoffice.domain.*;
import fr.lovotech.galaxy.qa.backoffice.exception.BusinessException;
import fr.lovotech.galaxy.qa.backoffice.exception.DomainErrorCodes;
import fr.lovotech.galaxy.qa.backoffice.repository.CampaignTestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CampaignTestService {
    private final Logger log = LoggerFactory.getLogger(CampaignTestService.class);

    private final fr.lovotech.galaxy.qa.backoffice.repository.CampaignTestRepository campaignTestRepository;


    public CampaignTestService(CampaignTestRepository campaignTestRepository) {
        this.campaignTestRepository = campaignTestRepository;

    }

    public CampaignTest createCampaignTest(CampaignTestRequest campaignTestRequest) {
        CampaignTest existingCamp = verifyIfCampaignWithSameCodeExistOrDeleted(campaignTestRequest.getCode());
        if (existingCamp == null) {
            CampaignTest campaignTest = campaignTestRequest.buildCampaignTest();
            campaignTest.setExternalUid(ReferenceUID.builder().build());
            return saveOrUpdate(campaignTest);
        } else if(existingCamp.getIsActive()) {
            log.info("[INFO] Campaign with code=[{}] has already an existing active campaign with id=[{}]",existingCamp.getCode(),existingCamp.getId());
            throw new BusinessException(DomainErrorCodes.CAMPAIGN_TEST_CODE_EXISTS);
        }
        else{
            log.info("[INFO] Campaign with code=[{}] has already an existing deleted campaign with id=[{}]",existingCamp.getCode(),existingCamp.getId());
            throw new BusinessException(DomainErrorCodes.CAMPAIGN_TEST_CODE_DELETED);
        }
    }

    public PageResponse<CampaignTest> getAllCampaignTests(Pageable paging, Map<String, Object> inputs) {
        Query query = prepareQuerySearch(inputs);
        //query.collation(Collation.of("fr").strength(Collation.ComparisonLevel.secondary()));
        return new PageResponse<CampaignTest>(campaignTestRepository.findAll(query,paging));
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
                case "campaignType":
                    query.addCriteria(Criteria.where("campaignType").regex(containsTextRegEx(input.getValue().toString()), "i"));
                    break;
                case "scenarioList":
                    query.addCriteria(Criteria.where("scenarioList").regex(containsTextRegEx(input.getValue().toString()), "i"));
                    break;
                case "campaignStatus":
                    query.addCriteria(Criteria.where("campaignStatus").regex(containsTextRegEx(input.getValue().toString()), "i"));
                    break;
                case "validDate":
                    query.addCriteria(Criteria.where("validDate").regex(containsTextRegEx(input.getValue().toString()), "i"));
                    break;
                case "lastUpdate":
                    query.addCriteria(Criteria.where("lastUpdate").regex(containsTextRegEx(input.getValue().toString()), "i"));
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
        return query;
    }

    private String containsTextRegEx(String value) {
        return MongoRegexCreator.INSTANCE.toRegularExpression(value, MongoRegexCreator.MatchMode.CONTAINING);
    }

    public CampaignTest updateCampaignTest(String id, CampaignTestRequest newCampaignTestRequest) {
        CampaignTest existingCampWithSameCode = verifyIfCampaignWithSameCodeExistOrDeleted(newCampaignTestRequest.getCode());
        CampaignTest oldCampaignTest = getCampaignTestById(id);
        CampaignTest newCampaignTest = newCampaignTestRequest.buildCampaignTest();
        if (existingCampWithSameCode == null || oldCampaignTest.getCode().equals(newCampaignTestRequest.getCode())) {

            oldCampaignTest.setId(null);
            oldCampaignTest.setEndDate(new Date());
            oldCampaignTest.setIsActive(false);
            saveOrUpdate(oldCampaignTest);
            newCampaignTest.setId(id);
            newCampaignTest.setExternalUid(ReferenceUID.builder().build());
            newCampaignTest.setCreationDate(new Date());
            return saveOrUpdate(newCampaignTest);
        }
        else if(existingCampWithSameCode.getIsActive()) {
            log.info("[INFO] Campaign with code=[{}] has already an existing active campaign with id=[{}]",existingCampWithSameCode.getCode(),existingCampWithSameCode.getId());
            throw new BusinessException(DomainErrorCodes.CAMPAIGN_TEST_CODE_EXISTS);
        }
        else{
            log.info("[INFO] Campaign with code=[{}] has already an existing deleted campaign with id=[{}]",existingCampWithSameCode.getCode(),existingCampWithSameCode.getId());
            throw new BusinessException(DomainErrorCodes.CAMPAIGN_TEST_CODE_DELETED);
        }
    }

    public CampaignTest saveOrUpdate(CampaignTest campaignTest) {
        campaignTest = campaignTestRepository.save(campaignTest);
        return campaignTest;
    }

    public CampaignTest getCampaignTestById(String id) {
        Optional<CampaignTest> campaignTest = campaignTestRepository.findById(id);
        if (campaignTest.isEmpty()) {
            log.error("[ERROR] CampaignTest with id {} not found: ", id);
            throw new BusinessException(DomainErrorCodes.CAMPAIGN_TEST_NOT_FOUND);
        }
        return campaignTest.get();
    }

    public void deleteCampaignTest(String id) {
        log.debug("Request to delete CampaignTest : {}", id);
        CampaignTest campaignTest = getCampaignTestById(id);
        if (campaignTest != null) {
            campaignTest.setIsDeleted(true);
            campaignTest.setIsActive(false);
            campaignTest.setEndDate(new Date());
            campaignTestRepository.save(campaignTest);
        }
    }

    private CampaignTest verifyIfCampaignWithSameCodeExistOrDeleted(String code) {
        Criteria criteria = new Criteria();
        criteria.orOperator(Criteria.where("isActive").is(true), Criteria.where("isDeleted").is(true));
        Query query = new Query(criteria);
        query.addCriteria(Criteria.where("code").regex(exactText(code),"i"));
        return campaignTestRepository.findAll(query).isEmpty() ? null : campaignTestRepository.findAll(query).get(0);
    }

    private String exactText(String value) {
        return MongoRegexCreator.INSTANCE.toRegularExpression(value, MongoRegexCreator.MatchMode.EXACT);
    }
    
    public CampaignTest deleteConfigurationML(String id, String codeLanguage ,CampaignTestRequest campaignTestRequest ){
        CampaignTest campaignTest = getCampaignTestById(id);
        campaignTestRequest.setConfigurationMLS( campaignTest.getConfigurationMLS().stream()
          .filter(config->!config.getLanguageCode().equals(codeLanguage)).collect(Collectors.toList()));
        campaignTest.setIsActive(false);
        campaignTest.setEndDate(new Date());
        saveOrUpdate(campaignTest);
        return createCampaignTest(campaignTestRequest);
    }

    public List<CampaignTest> findByScenarioId(String id){
        return campaignTestRepository.findByScenarioListId(id);
    }

   
}

