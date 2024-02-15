package fr.lovotech.galaxy.qa.backoffice.core;

import fr.lovotech.galaxy.qa.backoffice.domain.CampaignTest;
import fr.lovotech.galaxy.qa.backoffice.domain.ConfigurationML;
import fr.lovotech.galaxy.qa.backoffice.domain.ReferenceUID;
import fr.lovotech.galaxy.qa.backoffice.domain.Scenario;
import fr.lovotech.galaxy.qa.backoffice.enums.CampaignStatus;
import fr.lovotech.galaxy.qa.backoffice.enums.ExecStatus;
import fr.lovotech.galaxy.qa.backoffice.enums.CampaignType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CampaignTestRequest {
    private static final long serialVersionUID = 1L;
    private ReferenceUID externalUid;
    private Date creationDate;
    private String code;
    private CampaignType campaignType;
    private List<Scenario> scenarioList = new ArrayList<>();
    private List<ConfigurationML> configurationMLS = new ArrayList<>();
    private CampaignStatus campaignStatus;
    private ExecStatus execStatus;
    private Date lastExecDate;
    private Date endDate;

    public CampaignTest buildCampaignTest() {
        return CampaignTest.builder()
                .creationDate(this.creationDate)
                .code(this.code)
                .campaignType(this.campaignType)
                .campaignStatus(this.campaignStatus)
                .scenarioList(this.scenarioList)
                .execStatus(this.execStatus)
                .lastExecDate(this.lastExecDate)
                .configurationMLS(this.configurationMLS)
                .endDate(this.endDate)
                .build();
    }
}
