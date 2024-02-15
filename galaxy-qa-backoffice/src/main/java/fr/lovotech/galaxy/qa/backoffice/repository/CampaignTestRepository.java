package fr.lovotech.galaxy.qa.backoffice.repository;

import org.springframework.stereotype.Repository;

import fr.lovotech.galaxy.qa.backoffice.domain.CampaignTest;
import fr.lovotech.galaxy.qa.backoffice.repository.utils.ResourceRepository;

import java.util.List;

@Repository
public interface CampaignTestRepository extends ResourceRepository<CampaignTest,String> {
    @org.springframework.data.mongodb.repository.Query(value = "{ 'scenarioList': { $elemMatch: { '_id' : ?0 } }}")
    List<CampaignTest> findByScenarioListId(String id);
    List<CampaignTest> findAll();

}
