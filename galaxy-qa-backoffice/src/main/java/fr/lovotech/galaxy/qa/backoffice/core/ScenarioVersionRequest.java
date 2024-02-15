package fr.lovotech.galaxy.qa.backoffice.core;

import fr.lovotech.galaxy.qa.backoffice.domain.ApplicationVersion;
import fr.lovotech.galaxy.qa.backoffice.domain.ReferenceUID;
import fr.lovotech.galaxy.qa.backoffice.domain.Scenario;
import fr.lovotech.galaxy.qa.backoffice.domain.ScenarioVersion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScenarioVersionRequest {
    private ReferenceUID externalUid;
    private ApplicationVersion applicationVersion ;
    private Scenario scenario;
    private Date endDate;
    private Date creationDate;

    public ScenarioVersion buildScenarioVersion(){
        return ScenarioVersion.builder()
                .creationDate(this.creationDate)
                .endDate(this.endDate)
                .scenario(this.scenario)
                .applicationVersion(this.applicationVersion)
                .build();
    }
}
