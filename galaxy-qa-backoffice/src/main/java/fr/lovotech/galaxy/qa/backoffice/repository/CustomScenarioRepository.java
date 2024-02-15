package fr.lovotech.galaxy.qa.backoffice.repository;

import fr.lovotech.galaxy.qa.backoffice.domain.Scenario;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface CustomScenarioRepository {
    Page<Scenario> findScenariosByCriterias(int page, int size, Map<String, Object> inputs);
}
