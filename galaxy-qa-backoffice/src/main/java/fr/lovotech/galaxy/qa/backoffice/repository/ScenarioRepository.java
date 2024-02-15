package fr.lovotech.galaxy.qa.backoffice.repository;

import fr.lovotech.galaxy.qa.backoffice.domain.Application;
import fr.lovotech.galaxy.qa.backoffice.domain.Scenario;
import fr.lovotech.galaxy.qa.backoffice.repository.utils.ResourceRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScenarioRepository extends ResourceRepository<Scenario,String> {
}
