package fr.lovotech.galaxy.qa.backoffice.repository;

import fr.lovotech.galaxy.qa.backoffice.domain.Trigger;
import fr.lovotech.galaxy.qa.backoffice.domain.UITest;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface CustomUITestRepository {
    Page<UITest> findUITestsByCriterias(int page, int size, Map<String, Object> inputs);
}
