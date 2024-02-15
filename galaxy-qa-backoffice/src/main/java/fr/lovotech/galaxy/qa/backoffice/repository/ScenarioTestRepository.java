package fr.lovotech.galaxy.qa.backoffice.repository;

import fr.lovotech.galaxy.qa.backoffice.domain.ScenarioTest;
import fr.lovotech.galaxy.qa.backoffice.repository.utils.ResourceRepository;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScenarioTestRepository extends ResourceRepository<ScenarioTest,String> {

    @Query(value="{ 'scenario.$id' : ?0 }")
    public List<ScenarioTest> findByScenarioId(String id);
}
