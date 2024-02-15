package fr.lovotech.galaxy.qa.backoffice.core;

import fr.lovotech.galaxy.qa.backoffice.domain.Application;
import fr.lovotech.galaxy.qa.backoffice.domain.ApplicationVersion;
import fr.lovotech.galaxy.qa.backoffice.domain.ConfigurationML;
import fr.lovotech.galaxy.qa.backoffice.domain.ReferenceUID;
import fr.lovotech.galaxy.qa.backoffice.domain.Test;
import fr.lovotech.galaxy.qa.backoffice.enums.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationRequest {
    private ReferenceUID externalUid;
    private Date creationDate;
    private String code;
    private ApplicationStatus applicationStatus;
    private String applicationPhase;
    private List<ConfigurationML> configurationMLS = new ArrayList<>();
    private Date validDate;
    private Date lastUpdate;
    private Date endDate;
    private List<ApplicationVersion> applicationVersions = new ArrayList<>();

    public Application buildApplication() {
        return Application.builder()
                .code(this.code)
                .externalUid(this.externalUid)
                .creationDate(this.creationDate)
                .applicationStatus(this.applicationStatus)
                .applicationPhase(this.applicationPhase)
                .configurationMLS(this.configurationMLS)
                .validDate(this.validDate)
                .lastUpdate(this.lastUpdate)
                .endDate(this.endDate)
                .build();
    }
}
