package fr.lovotech.galaxy.qa.backoffice.repository;

import fr.lovotech.galaxy.qa.backoffice.domain.Environment;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface CustomEnvironmentRepository {
    Page<Environment> findEnvironmentsByCriterias(int page, int size, Map<String, Object> inputs);
}