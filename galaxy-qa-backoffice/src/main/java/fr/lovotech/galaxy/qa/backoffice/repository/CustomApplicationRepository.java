package fr.lovotech.galaxy.qa.backoffice.repository;

import java.util.Map;

import org.springframework.data.domain.Page;

import fr.lovotech.galaxy.qa.backoffice.domain.Application;

public interface CustomApplicationRepository {
    Page<Application> findApplicationsByCriterias(int page, int size, Map<String, Object> inputs);
}
