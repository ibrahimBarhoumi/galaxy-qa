package fr.lovotech.galaxy.qa.backoffice.service;

import fr.lovotech.galaxy.qa.backoffice.core.ScenarioVersionRequest;
import fr.lovotech.galaxy.qa.backoffice.domain.*;
import fr.lovotech.galaxy.qa.backoffice.exception.BusinessException;
import fr.lovotech.galaxy.qa.backoffice.exception.DomainErrorCodes;
import fr.lovotech.galaxy.qa.backoffice.repository.ScenarioVersionRepository;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ScenarioVersionService {
    private final Logger log = LoggerFactory.getLogger(ScenarioVersionService.class);

    private final ScenarioVersionRepository scenarioVersionRepository;
    private final MongoTemplate mongoTemplate;

    public ScenarioVersionService(ScenarioVersionRepository scenarioVersionRepository, MongoTemplate mongoTemplate) {
        this.scenarioVersionRepository = scenarioVersionRepository;
        this.mongoTemplate = mongoTemplate;

    }

    public ScenarioVersion createScenarioVersion(ScenarioVersionRequest scenarioVersionRequest) {
        ScenarioVersion scenarioVersion = scenarioVersionRequest.buildScenarioVersion() ;
        scenarioVersion.setExternalUid(ReferenceUID.builder().build());
        return scenarioVersionRepository.save(scenarioVersion);
    }

    public PageResponse<ScenarioVersion> getAllScenarioVersions(Pageable paging) {
        Query query = new Query();
        query.addCriteria(Criteria.where("isActive").is(true));
        return new PageResponse<ScenarioVersion>(scenarioVersionRepository.findAll(query,paging));
    }

    public ScenarioVersion getScenarioVersionById(String id) {
        Optional<ScenarioVersion> ScenarioVersion = scenarioVersionRepository.findById(id);
        if (ScenarioVersion.isEmpty()) {
            log.error("[ERROR] ScenarioVersion with id {} not found: ", id);
            throw new BusinessException(DomainErrorCodes.SCENARIO_VERSION_NOT_FOUND);
        }
        return ScenarioVersion.get();
    }

    public PageResponse<ScenarioVersion> findScenariosByVersionId (Pageable paging, String id){
        Criteria criteria = new Criteria();
        Query query = new Query(criteria);
        query.addCriteria(Criteria.where("isActive").is(true));
        query.addCriteria(Criteria.where("applicationVersion.$id").is(new ObjectId(id)));
        return new PageResponse<ScenarioVersion>(scenarioVersionRepository.findAll(query,paging));
    }

    public Application getApplicationByScenarioId(String id){
        Criteria criteria = new Criteria();
        Query query = new Query(criteria);
        query.addCriteria(Criteria.where("isActive").is(true));
        query.addCriteria(Criteria.where("scenario.$id").is(new ObjectId(id)));
        List<Application> applicationList = new ArrayList<>();
        List<ScenarioVersion> scenarioVersionList = scenarioVersionRepository.findAll(query);
        scenarioVersionList.forEach(element ->{
            applicationList.add(element.getApplicationVersion().getApplication());
        });
        return applicationList.get(0) ;
    }
    public Page<ApplicationVersion> getVersionsByScenarioId(String id){
        Criteria criteria = new Criteria();
        Query query = new Query(criteria);
        query.addCriteria(Criteria.where("isActive").is(true));
        query.addCriteria(Criteria.where("scenario.$id").is(new ObjectId(id)));
        List<ApplicationVersion> applicationVersionList = new ArrayList<>();
        List<ScenarioVersion> scenarioVersionList = scenarioVersionRepository.findAll(query);
        scenarioVersionList.forEach(element->{
            applicationVersionList.add(element.getApplicationVersion());
        });
        Page page = new PageImpl(applicationVersionList);
        return page ;
    }

    public List<ScenarioVersion> findScenariosVersionByScenarioId (String id){
        Criteria criteria = new Criteria();
        Query query = new Query(criteria);
        query.addCriteria(Criteria.where("isActive").is(true));
        query.addCriteria(Criteria.where("scenario.$id").is(new ObjectId(id)));
        return scenarioVersionRepository.findAll(query);
    }
}
