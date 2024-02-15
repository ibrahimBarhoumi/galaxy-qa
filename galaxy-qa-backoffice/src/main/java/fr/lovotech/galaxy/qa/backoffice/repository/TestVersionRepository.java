package fr.lovotech.galaxy.qa.backoffice.repository;

import fr.lovotech.galaxy.qa.backoffice.domain.TestVersion;
import fr.lovotech.galaxy.qa.backoffice.repository.utils.ResourceRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface TestVersionRepository extends ResourceRepository<TestVersion,String>{}
