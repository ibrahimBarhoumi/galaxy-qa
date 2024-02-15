package fr.lovotech.galaxy.qa.backoffice.core;

import java.util.List;


import fr.lovotech.galaxy.qa.backoffice.domain.ApplicationVersion;
import fr.lovotech.galaxy.qa.backoffice.domain.ReferenceUID;
import fr.lovotech.galaxy.qa.backoffice.domain.Test;
import fr.lovotech.galaxy.qa.backoffice.domain.TestParameter;
import fr.lovotech.galaxy.qa.backoffice.domain.TestParameterDetail;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestParameterRequest {
    private ReferenceUID externalUid;
    private String code ;
    private Test test;
    private ApplicationVersion applicationVersion;
    private List<TestParameterDetail> testParameterDetails;

    public TestParameter buildTestParameter(){
        return TestParameter.builder()
        .code(this.code)
        .applicationVersion(this.applicationVersion)
        .test(this.test)
        .testParameterDetails(this.testParameterDetails)
        .build();
    }
}
