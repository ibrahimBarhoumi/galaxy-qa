package fr.lovotech.galaxy.qa.backoffice.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import fr.lovotech.galaxy.qa.backoffice.core.CampaignTestRequest;
import fr.lovotech.galaxy.qa.backoffice.enums.CampaignStatus;
import fr.lovotech.galaxy.qa.backoffice.enums.ExecStatus;
import fr.lovotech.galaxy.qa.backoffice.enums.CampaignType;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Update;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
@Document(collection = "campaignTest")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CampaignTest extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private ReferenceUID externalUid;
    private Date creationDate;
    private String code;
    private CampaignType campaignType;
    private CampaignStatus campaignStatus;
    private ExecStatus execStatus;
    @Builder.Default
    private List<Scenario> scenarioList = new ArrayList<>();
    @Builder.Default
    private List<ConfigurationML> configurationMLS = new ArrayList<>();
    private Date lastExecDate;
    private Date endDate;

    public Update updateFrom(CampaignTestRequest campaignTestRequest) {
        Update campaignTestUpdate = new Update();
        if (campaignTestRequest.getCreationDate() != null) {
            campaignTestUpdate.set("creationDate", campaignTestRequest.getCreationDate());
        }
        if (campaignTestRequest.getCode() != null) {
            campaignTestUpdate.set("code", campaignTestRequest.getCode());
        }
        if (campaignTestRequest.getCampaignType() != null) {
            campaignTestUpdate.set("campaignType", campaignTestRequest.getCampaignType());
        }
        if (campaignTestRequest.getCampaignStatus() != null) {
            campaignTestUpdate.set("campaignStatus", campaignTestRequest.getCampaignStatus());
        }
        if (campaignTestRequest.getExecStatus() != null) {
            campaignTestUpdate.set("execStatus", campaignTestRequest.getExecStatus());
        }
        if (campaignTestRequest.getLastExecDate() != null) {
            campaignTestUpdate.set("lastExecDate", campaignTestRequest.getLastExecDate());
        }
        if (campaignTestRequest.getConfigurationMLS() != null) {
            campaignTestUpdate.set("configurationMLS", campaignTestRequest.getConfigurationMLS());
        }
        if (campaignTestRequest.getEndDate() != null) {
            campaignTestUpdate.set("endDate", campaignTestRequest.getEndDate());
        }
        return campaignTestUpdate;
    }
}
