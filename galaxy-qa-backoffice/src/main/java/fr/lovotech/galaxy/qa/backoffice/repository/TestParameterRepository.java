package fr.lovotech.galaxy.qa.backoffice.repository;

import org.springframework.stereotype.Repository;

import fr.lovotech.galaxy.qa.backoffice.domain.TestParameter;
import fr.lovotech.galaxy.qa.backoffice.repository.utils.ResourceRepository;

@Repository
public interface TestParameterRepository extends ResourceRepository<TestParameter, String>{}