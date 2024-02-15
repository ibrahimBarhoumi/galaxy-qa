package fr.lovotech.galaxy.qa.backoffice.repository;

import org.springframework.stereotype.Repository;

import fr.lovotech.galaxy.qa.backoffice.domain.TestParameterDetail;
import fr.lovotech.galaxy.qa.backoffice.repository.utils.ResourceRepository;

@Repository
public interface TestParameterDetailRepository extends ResourceRepository<TestParameterDetail, String>{}
