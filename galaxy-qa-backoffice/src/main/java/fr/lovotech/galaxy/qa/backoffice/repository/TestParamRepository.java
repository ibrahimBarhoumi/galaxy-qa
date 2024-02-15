package fr.lovotech.galaxy.qa.backoffice.repository;

import fr.lovotech.galaxy.qa.backoffice.domain.TestParam;
import fr.lovotech.galaxy.qa.backoffice.repository.utils.ResourceRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface TestParamRepository extends ResourceRepository<TestParam, String> {
}