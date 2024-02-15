package fr.lovotech.galaxy.qa.backoffice.rest.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import fr.lovotech.galaxy.qa.backoffice.domain.DataTestCaseVersion;
import fr.lovotech.galaxy.qa.backoffice.domain.Scenario;
import fr.lovotech.galaxy.qa.backoffice.domain.Test;
import lombok.*;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LaunchScenarioDTO {
    private Scenario scenario;
    private List<DataTestCaseVersion> dataTestCaseVersions;
}
