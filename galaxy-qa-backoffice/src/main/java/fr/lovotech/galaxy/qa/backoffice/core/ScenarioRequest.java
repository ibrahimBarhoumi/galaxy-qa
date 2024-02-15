package fr.lovotech.galaxy.qa.backoffice.core;

import com.google.common.collect.Lists;
import fr.lovotech.galaxy.qa.backoffice.domain.*;
import fr.lovotech.galaxy.qa.backoffice.enums.ScenarioStatus;
import fr.lovotech.galaxy.qa.backoffice.enums.CampaignType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScenarioRequest {

    private ReferenceUID externalUid;
    private String code;
    private ScenarioStatus scenarioStatus;
    private CampaignType scenarioType;
    private Date creationDate;
    private List<ConfigurationML> configurationMLS = Lists.newArrayList();
    private Date endDate ;
    private List<ScenarioTest> scenarioTests ;
    private Application application ;
    private List<ScenarioVersion> scenarioVersions ;
    public Scenario buildScenario() {
        return Scenario.builder()
                .externalUid(this.externalUid)
                .creationDate(this.creationDate)
                .code(this.code)
                .scenarioStatus(scenarioStatus)
                .scenarioType(scenarioType)
                .configurationMLS(this.configurationMLS)
                .endDate(this.endDate)
                .build();
    }
}
