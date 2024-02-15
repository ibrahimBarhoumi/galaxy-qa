package fr.lovotech.galaxy.qa.backoffice.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import fr.lovotech.galaxy.qa.backoffice.domain.DataTestCaseVersion;
import fr.lovotech.galaxy.qa.backoffice.domain.Test;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LaunchTestDTO {
    private Test test;
    private DataTestCaseVersion dataTestCaseVersion;
}
