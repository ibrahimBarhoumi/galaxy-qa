package fr.lovotech.galaxy.qa.backoffice.service;

import fr.lovotech.galaxy.qa.backoffice.core.ScenarioTestRequest;
import fr.lovotech.galaxy.qa.backoffice.domain.PageResponse;
import fr.lovotech.galaxy.qa.backoffice.domain.ReferenceUID;
import fr.lovotech.galaxy.qa.backoffice.domain.ScenarioTest;
import fr.lovotech.galaxy.qa.backoffice.domain.ScenarioVersion;
import fr.lovotech.galaxy.qa.backoffice.exception.BusinessException;
import fr.lovotech.galaxy.qa.backoffice.exception.DomainErrorCodes;
import fr.lovotech.galaxy.qa.backoffice.repository.ScenarioTestRepository;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
public class ScenarioTestService {
    private final Logger log = LoggerFactory.getLogger(ScenarioVersionService.class);
    private final ScenarioTestRepository scenarioTestRepository ;

    public ScenarioTestService(ScenarioTestRepository scenarioTestRepository) {
        this.scenarioTestRepository = scenarioTestRepository;
    }

    public ScenarioTest createScenarioTest(ScenarioTestRequest scenarioTestRequest){
        ScenarioTest scenarioTest = scenarioTestRequest.buildScenarioTest() ;
        scenarioTest.setExternalUid(ReferenceUID.builder().build());
        return scenarioTestRepository.save(scenarioTest);
    }
    public PageResponse<ScenarioTest> getScenarioTestByScenarioId(String id , Pageable paging){
        Query query = new Query();
        query.addCriteria(Criteria.where("isActive").is(true));
        query.addCriteria(Criteria.where("scenario.$id").is(new ObjectId(id)));
        return new PageResponse<ScenarioTest>(scenarioTestRepository.findAll(query,  paging));
    }
    public PageResponse<ScenarioTest> getAllScenarioTest(Pageable pageable){
        Query query = new Query();
        query.addCriteria(Criteria.where("isActive").is(true));
        return new PageResponse<ScenarioTest>(scenarioTestRepository.findAll(query,pageable));
    }
    public PageResponse<ScenarioTest> findScenariosByTestId (Pageable paging, String id){
        Criteria criteria = new Criteria();
        Query query = new Query(criteria);
        query.addCriteria(Criteria.where("isActive").is(true));
        query.addCriteria(Criteria.where("applicationVersion.$id").is(new ObjectId(id)));
        return new PageResponse<ScenarioTest>(scenarioTestRepository.findAll(query,paging));
    }
    public ScenarioTest getScenarioTestById(String id) {
        Optional<ScenarioTest> ScenarioTest = scenarioTestRepository.findById(id);
        if (ScenarioTest.isEmpty()) {
            log.error("[ERROR] ScenarioTest with id {} not found: ", id);
            throw new BusinessException(DomainErrorCodes.SCENARIO_TEST_NOT_FOUND);
        }
        return ScenarioTest.get();
    }

    public List<ScenarioTest> findScenariosTestByScenarioId (String id){
        Criteria criteria = new Criteria();
        Query query = new Query(criteria);
        query.addCriteria(Criteria.where("isActive").is(true));
        query.addCriteria(Criteria.where("scenario.$id").is(new ObjectId(id)));
        
        return scenarioTestRepository.findAll(query);
    }

    
    public List<ScenarioTest> getScenarioTestByTestId(String id){
        Criteria criteria = new Criteria();
        Query query = new Query(criteria);
        query.addCriteria(Criteria.where("isActive").is(true));
        query.addCriteria(Criteria.where("test.$id").is(new ObjectId(id)));
        return scenarioTestRepository.findAll(query);
    }

}
