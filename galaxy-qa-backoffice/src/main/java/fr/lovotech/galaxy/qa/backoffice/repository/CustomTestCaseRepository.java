package fr.lovotech.galaxy.qa.backoffice.repository;

import fr.lovotech.galaxy.qa.backoffice.domain.TestCase;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface CustomTestCaseRepository {
    Page<TestCase> findTestCasesByCriterias(int page, int size, Map<String, Object> inputs);
}
