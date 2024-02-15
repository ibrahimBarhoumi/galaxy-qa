package fr.lovotech.galaxy.qa.backoffice.core;

import fr.lovotech.galaxy.qa.backoffice.domain.ReferenceUID;
import fr.lovotech.galaxy.qa.backoffice.domain.Scenario;
import fr.lovotech.galaxy.qa.backoffice.domain.ScenarioTest;
import fr.lovotech.galaxy.qa.backoffice.domain.Test;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScenarioTestRequest {
    private ReferenceUID externalUid;
    private int order;
    private Test beforeTest;
    private Test test;
    private Scenario scenario;
    private Date endDate;

    public ScenarioTest buildScenarioTest(){
        return ScenarioTest.builder()
                .externalUid(this.externalUid)
                .order(this.order)
                .beforeTest(this.beforeTest)
                .test(this.test)
                .scenario(this.scenario)
                .endDate(this.endDate)
                .build();
    }
}

