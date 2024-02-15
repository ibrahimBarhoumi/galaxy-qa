package fr.lovotech.galaxy.qa.backoffice.repository;

import fr.lovotech.galaxy.qa.backoffice.domain.Test;
import fr.lovotech.galaxy.qa.backoffice.repository.utils.ResourceRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface TestRepository extends ResourceRepository<Test,String>{}
