package fr.lovotech.galaxy.qa.backoffice.repository;

import fr.lovotech.galaxy.qa.backoffice.domain.DataTestCaseVersion;
import fr.lovotech.galaxy.qa.backoffice.repository.utils.ResourceRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataTestCaseVersionRepository extends ResourceRepository<DataTestCaseVersion, String> {
}