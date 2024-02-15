package fr.lovotech.galaxy.qa.backoffice.service;

import fr.lovotech.galaxy.qa.backoffice.core.ScenarioRequest;

import fr.lovotech.galaxy.qa.backoffice.domain.*;
import fr.lovotech.galaxy.qa.backoffice.exception.BusinessException;
import fr.lovotech.galaxy.qa.backoffice.exception.DomainErrorCodes;
import fr.lovotech.galaxy.qa.backoffice.exception.TechnicalException;
import fr.lovotech.galaxy.qa.backoffice.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.MongoRegexCreator;
import org.springframework.data.mongodb.core.query.Query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.valueextraction.Unwrapping.Skip;

@Service
public class ScenarioService {
    private final Logger log = LoggerFactory.getLogger(ScenarioService.class);

    private final ScenarioRepository scenarioRepository;
    private final ScenarioVersionRepository scenarioVersionRepository;
    private final ScenarioTestRepository scenarioTestRepository;
    private final ScenarioVersionService scenarioVersionService;
    private final CampaignTestService campaignTestService;
    private final ScenarioTestService scenarioTestService;
    private final CampaignTestRepository campaignTestRepository;
    private final TestService testService;
    private final DataTestCaseService dataTestCaseService ;
    private final DataTestCaseVersionService dataTestCaseVersionService ;

    public ScenarioService(ScenarioRepository scenarioRepository, ScenarioVersionRepository scenarioVersionRepository, ScenarioTestRepository scenarioTestRepository, ScenarioVersionService scenarioVersionService, CampaignTestService campaignTestService, ScenarioTestService scenarioTestService, CampaignTestRepository campaignTestRepository,TestService testService,DataTestCaseService dataTestCaseService , DataTestCaseVersionService dataTestCaseVersionService) {
        this.scenarioRepository = scenarioRepository;
        this.scenarioVersionRepository = scenarioVersionRepository;
        this.scenarioTestRepository = scenarioTestRepository;
        this.scenarioVersionService = scenarioVersionService;
        this.campaignTestService = campaignTestService;
        this.scenarioTestService = scenarioTestService;
        this.campaignTestRepository = campaignTestRepository;
        this.testService = testService;
        this.dataTestCaseService = dataTestCaseService;
        this.dataTestCaseVersionService = dataTestCaseVersionService; 
    }   

    @Transactional
    public Scenario createScenario(ScenarioRequest scenarioRequest) {
        Scenario existingScenario = verifyIfScenarioWithSameCodeExistOrDeleted(scenarioRequest.getCode());
        if (existingScenario == null) {
            Scenario scenario = scenarioRequest.buildScenario();
            scenario.setExternalUid(ReferenceUID.builder().build());
            scenario.setCreationDate(new Date());
            List<ScenarioTest> scenarioTestList = scenarioRequest.getScenarioTests();
            List<ScenarioVersion> scenarioVersions = scenarioRequest.getScenarioVersions();
            scenarioRepository.save(scenario);
            if (scenarioVersions != null) {
                scenarioVersions.forEach(scenarioVersion -> {
                    scenarioVersion.setExternalUid(ReferenceUID.builder().build());
                    scenarioVersion.setScenario(scenario);
                });
                scenarioVersionRepository.saveAll(scenarioVersions);
            }
            scenarioTestList.forEach(scenarioTest -> {
                scenarioTest.setExternalUid(ReferenceUID.builder().build());
                scenarioTest.setScenario(scenario);
            });
            scenarioTestRepository.saveAll(scenarioTestList);
            return scenario ;
        }
        else if(existingScenario.getIsActive()) {
            log.info("[INFO] Campaign with code=[{}] has already an existing active campaign with id=[{}]",existingScenario.getCode(),existingScenario.getId());
            throw new BusinessException(DomainErrorCodes.SCENARIO_TEST_CODE_EXISTS);
        }
        else{
            log.info("[INFO] Campaign with code=[{}] has already an existing deleted campaign with id=[{}]",existingScenario.getCode(),existingScenario.getId());
            throw new BusinessException(DomainErrorCodes.SCENARIO_TEST_CODE_DELETED);
        }
    }

    public PageResponse<Scenario> getAllScenarios(Pageable paging, Map<String, Object> inputs) {
        Query query = prepareQuerySearch(inputs);
        return new PageResponse<Scenario>(scenarioRepository.findAll(query, paging));
    }

    private Query prepareQuerySearch(Map<String, Object> inputs) {
        Query query = new Query();
        query.addCriteria(Criteria.where("isActive").is(true));
        for (Map.Entry<String, Object> input : inputs.entrySet()) {
            switch (input.getKey()) {
                case "code":
                    query.addCriteria(Criteria.where("code").regex(containsTextRegEx(input.getValue().toString()), "i"));
                    break;
                case "scenarioStatus":
                    query.addCriteria(Criteria.where("scenarioStatus").regex(containsTextRegEx(input.getValue().toString()), "i"));
                    break;
                case "scenarioType":
                    query.addCriteria(Criteria.where("scenarioType").regex(containsTextRegEx(input.getValue().toString()), "i"));
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


    public Scenario updateScenario(String id, ScenarioRequest scenarioRequest) {
        Scenario existingScenarioWithSameCode = verifyIfScenarioWithSameCodeExistOrDeleted(scenarioRequest.getCode());
        Scenario oldScenario = getScenarioById(id);
        Scenario newScenario = scenarioRequest.buildScenario();
        List <ScenarioTest> oldScenarioTestList = scenarioTestService.findScenariosTestByScenarioId(id);
        List<ScenarioVersion> oldScenarioVersions = scenarioVersionService.findScenariosVersionByScenarioId(id);
        if (existingScenarioWithSameCode == null || oldScenario.getCode().equals(scenarioRequest.getCode())) {
                oldScenario.setId(null);
                oldScenario.setEndDate(new Date());
                oldScenario.setIsActive(false);
                saveOrUpdate(oldScenario);
            oldScenarioTestList.forEach(scenarioTest -> {
                scenarioTest.setEndDate(new Date());
                scenarioTest.setIsActive(false);
                scenarioTest.setIsDeleted(true);
            });
            oldScenarioVersions.forEach(scenarioVersion -> {
                scenarioVersion.setEndDate(new Date());
                scenarioVersion.setIsActive(false);
                scenarioVersion.setIsDeleted(true);
            });
            scenarioVersionRepository.saveAll(oldScenarioVersions);
            scenarioTestRepository.saveAll(oldScenarioTestList);

            newScenario.setId(id);
            newScenario.setExternalUid(ReferenceUID.builder().build());
            newScenario.setCreationDate(new Date());
            scenarioRepository.save(newScenario);
            List<ScenarioTest> newScenarioTestList= scenarioRequest.getScenarioTests();
            List<ScenarioVersion> newScenarioVersions= scenarioRequest.getScenarioVersions();
            if(newScenarioVersions != null) {
                newScenarioVersions.forEach(scenarioVersion -> {
                    scenarioVersion.setExternalUid(ReferenceUID.builder().build());
                    scenarioVersion.setScenario(newScenario);
                });
                scenarioVersionRepository.saveAll(newScenarioVersions);
            }
            if(newScenarioTestList != null) {
                newScenarioTestList.forEach(scenarioTest -> {
                    scenarioTest.setExternalUid(ReferenceUID.builder().build());
                    scenarioTest.setScenario(newScenario);
                });
                scenarioTestRepository.saveAll(newScenarioTestList);
            }
            return newScenario;
            }
        else if(existingScenarioWithSameCode.getIsActive()) {
            log.info("[INFO] Campaign with code=[{}] has already an existing active campaign with id=[{}]",existingScenarioWithSameCode.getCode(),existingScenarioWithSameCode.getId());
            throw new BusinessException(DomainErrorCodes.SCENARIO_TEST_CODE_EXISTS);
        }
        else{
            log.info("[INFO] Campaign with code=[{}] has already an existing deleted campaign with id=[{}]",existingScenarioWithSameCode.getCode(),existingScenarioWithSameCode.getId());
            throw new BusinessException(DomainErrorCodes.SCENARIO_TEST_CODE_DELETED);
        }
    }

    public Scenario saveOrUpdate(Scenario scenario) {
        scenario = scenarioRepository.save(scenario);
        return scenario;
    }

    public Scenario getScenarioById(String id) {
        Optional<Scenario> scenario = scenarioRepository.findById(id);
        if (scenario.isEmpty()) {
            log.error("[ERROR] Scenario with id {} not found: ", id);
            throw new BusinessException(DomainErrorCodes.SCENARIO_NOT_FOUND);
        }
        return scenario.get();
    }

    public void deleteScenario(String id) {
        log.debug("Request to delete Scenario : {}", id);
        Pageable pageable = PageRequest.of(0, 10);
        Scenario scenario = getScenarioById(id);
        List<CampaignTest> campagnetests = campaignTestService.findByScenarioId(id);
        if (campagnetests.isEmpty()) {
            scenario.setIsActive(false);
            scenario.setEndDate(new Date());
            scenario.setIsDeleted(true);
            scenarioRepository.save(scenario);
            campagnetests.forEach(CampaignTest -> {
                CampaignTest.setEndDate(new Date());
                CampaignTest.setIsActive(false);
                CampaignTest.setIsDeleted(true);
            });
            campaignTestRepository.saveAll(campagnetests);
        }else{
            throw new BusinessException(DomainErrorCodes.CAMPAIGN_HAS_SCENARIO);
        }
        List<ScenarioVersion> scenarioVersions = scenarioVersionService.findScenariosVersionByScenarioId(id);
        scenarioVersions.forEach(e -> {
            e.setIsActive(false);
            e.setEndDate(new Date());
            e.setIsDeleted(true);
            scenarioVersionRepository.save(e);
        });
        List<ScenarioTest> scenarioTests = scenarioTestService.findScenariosTestByScenarioId(id);
        scenarioTests.forEach(e -> {
            e.setIsActive(false);
            e.setEndDate(new Date());
            e.setIsDeleted(true);
            scenarioTestRepository.save(e);
        });
    }

    public Scenario deleteConfigurationML(String id, String codeLanguage, ScenarioRequest scenarioRequest) {
        Scenario scenario = getScenarioById(id);
        scenarioRequest.setConfigurationMLS(scenario.getConfigurationMLS().stream()
                .filter(config -> !config.getLanguageCode().equals(codeLanguage)).collect(Collectors.toList()));
        scenario.setIsActive(false);
        scenario.setEndDate(new Date());
        saveOrUpdate(scenario);
        return createScenario(scenarioRequest);
    }
    private Scenario verifyIfScenarioWithSameCodeExistOrDeleted(String code) {
        Criteria criteria = new Criteria();
        criteria.orOperator(Criteria.where("isActive").is(true), Criteria.where("isDeleted").is(true));
        Query query = new Query(criteria);
        query.addCriteria(Criteria.where("code").regex(exactText(code),"i"));
        return scenarioRepository.findAll(query).isEmpty() ? null : scenarioRepository.findAll(query).get(0);
    }
    private String exactText(String value) {
        return MongoRegexCreator.INSTANCE.toRegularExpression(value, MongoRegexCreator.MatchMode.EXACT);
    }

    public String launchScenario(Scenario scenario) throws IOException , InterruptedException {
        List<ScenarioTest> scenarioTests = scenarioTestService.findScenariosTestByScenarioId(scenario.getId());
        List<DataTestCase> dataTestCasesList = new ArrayList<>();
        
        List<String> dataTestCaseId = new ArrayList<>();
        List<String> defaultDataTestCaseId = new ArrayList<>();
        
        scenarioTests.sort(Comparator.comparing(ScenarioTest::getOrder, Comparator.nullsFirst(Comparator.reverseOrder())));
        List<String> testIdList = new ArrayList<>();

        scenarioTests.forEach(scenarioTest->{
            testIdList.add(scenarioTest.getTest().getId());
        });
        testIdList.forEach(testId->{
                List<DataTestCase> defaults = dataTestCaseService.getDefaultDataTestCaseByTestId(testId);
                if(!defaults.isEmpty() && defaults.size() == 1){
                    defaultDataTestCaseId.add(defaults.get(0).getId());
                }
                else{
                    testIdList.remove(testId);
                }
        });
        final List<DataTestCaseVersion> dataTestCaseVersions  = dataTestCaseVersionService.getDataTestCaseVersion(defaultDataTestCaseId);
        testIdList.forEach((testId)->{
            
            try {
                DataTestCaseVersion dataTestCaseVersion = dataTestCaseVersions.get(testIdList.indexOf(testId));
                String passed = testService.launchTest(testService.getTestById(testId),dataTestCaseVersion);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
        });
        return "scenario done!";
    }

    public String lunchCampaign (String campaignTestId ){
        Optional<CampaignTest> campaignTest = campaignTestRepository.findById(campaignTestId);
        List<Scenario> scenarioList = campaignTest.get().getScenarioList();
        scenarioList.forEach(scenario -> {
            try {
                launchScenario(scenario);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        return "Campaign done" ;
    }
}
