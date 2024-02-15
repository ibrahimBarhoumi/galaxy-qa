package fr.lovotech.galaxy.qa.backoffice.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.Lists;
import fr.lovotech.galaxy.qa.backoffice.core.ScenarioRequest;
import fr.lovotech.galaxy.qa.backoffice.enums.ScenarioStatus;
import fr.lovotech.galaxy.qa.backoffice.enums.CampaignType;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Update;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Document(collection = "scenario")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Scenario extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private ReferenceUID externalUid;
    private String code;
    private ScenarioStatus scenarioStatus;
    private CampaignType scenarioType;
    private Date creationDate;
    @Builder.Default
    private List<ConfigurationML> configurationMLS = Lists.newArrayList();
    private Date endDate;

    public Update updateFrom(ScenarioRequest scenarioRequest) {
        Update scenarioUpdate = new Update();
        if (scenarioRequest.getCode() != null) {
            scenarioUpdate.set("code", scenarioRequest.getCode());
        }
        if (scenarioRequest.getScenarioStatus() != null) {
            scenarioUpdate.set("scenarioStatus", scenarioRequest.getScenarioStatus());
        }
        if (scenarioRequest.getScenarioType() != null) {
            scenarioUpdate.set("scenarioType", scenarioRequest.getScenarioType());
        }
        if (scenarioRequest.getCreationDate() != null) {
            scenarioUpdate.set("creationDate", scenarioRequest.getCreationDate());
        }
        if (scenarioRequest.getConfigurationMLS() != null) {
            scenarioUpdate.set("configurationMLS", scenarioRequest.getConfigurationMLS());
        }
        if (scenarioRequest.getEndDate() != null) {
            scenarioUpdate.set("endDate", scenarioRequest.getEndDate());
        }
        return scenarioUpdate;
    }
}
