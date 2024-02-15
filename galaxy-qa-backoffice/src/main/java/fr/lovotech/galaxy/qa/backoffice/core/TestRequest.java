package fr.lovotech.galaxy.qa.backoffice.core;

import fr.lovotech.galaxy.qa.backoffice.domain.*;
import fr.lovotech.galaxy.qa.backoffice.enums.TestStatus;
import fr.lovotech.galaxy.qa.backoffice.enums.TestType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestRequest {
    private ReferenceUID externalUid;
    private String code;
    private Date creationDate;
    private Date endDate;
    private TestStatus status;
    private TestType type;
    private Application application;
    private List<TestVersion> testVersions = new ArrayList<>();
    private List<ConfigurationML> configurationMLS = new ArrayList<>();
    private TestResult testResult;
    public Test buildTest() {
        return Test.builder()
                .externalUid(this.externalUid)
                .code(this.code)
                .creationDate(this.creationDate)
                .endDate(this.endDate)
                .type(this.type)
                .configurationMLS(this.configurationMLS)
                .application(this.application)
                .status(this.status)
                .testResult(testResult)
                .build();
    }
}
