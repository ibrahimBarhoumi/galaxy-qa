package fr.lovotech.galaxy.qa.backoffice.repository;

import fr.lovotech.galaxy.qa.backoffice.repository.utils.ResourceRepository;


import org.springframework.stereotype.Repository;

import fr.lovotech.galaxy.qa.backoffice.domain.Application;

import javax.management.Query;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends ResourceRepository<Application,String> {

}
