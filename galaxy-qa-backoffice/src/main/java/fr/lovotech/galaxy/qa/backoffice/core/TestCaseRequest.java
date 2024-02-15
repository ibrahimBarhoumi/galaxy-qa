package fr.lovotech.galaxy.qa.backoffice.core;

import com.google.common.collect.Lists;
import fr.lovotech.galaxy.qa.backoffice.domain.*;
import fr.lovotech.galaxy.qa.backoffice.enums.ControlKeyType;
import fr.lovotech.galaxy.qa.backoffice.enums.TestCaseMode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestCaseRequest {
    private ReferenceUID externalUid;
    private String name;
    private Date creationDate;
    private TestCaseMode mode;
    private String controlKey;
    private ControlKeyType controlKeyType;
    private Boolean deleted;
    private Trigger trigger;

    private List<TestCaseDetail> testCaseDetails = Lists.newArrayList();
    private List<TestCaseInformation> testCaseInformations = Lists.newArrayList();

    public TestCase buildTestCase(){
        return TestCase.builder()
                .externalUid(this.externalUid)
                .controlKey(this.controlKey)
                .controlKeyType(this.controlKeyType)
                .creationDate(this.creationDate)
                .deleted(this.deleted)
                .name(this.name)
                .mode(this.mode)
                .trigger(this.trigger)
                .testCaseDetails(this.testCaseDetails)
                .testCaseInformations(this.testCaseInformations)
                .build();
    }
}
