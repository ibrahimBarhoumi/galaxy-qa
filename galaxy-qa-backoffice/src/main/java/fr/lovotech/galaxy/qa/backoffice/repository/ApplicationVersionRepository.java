package fr.lovotech.galaxy.qa.backoffice.repository;

import org.springframework.stereotype.Repository;

import fr.lovotech.galaxy.qa.backoffice.domain.ApplicationVersion;
import fr.lovotech.galaxy.qa.backoffice.repository.utils.ResourceRepository;

@Repository
public interface ApplicationVersionRepository extends ResourceRepository<ApplicationVersion,String>{}
