package fr.lovotech.galaxy.qa.backoffice.repository;


import fr.lovotech.galaxy.qa.backoffice.domain.DataTestCase;
import fr.lovotech.galaxy.qa.backoffice.repository.utils.ResourceRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataTestCaseRepository extends ResourceRepository<DataTestCase,String> {
    DataTestCase findByCode(String code);
}