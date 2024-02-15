package fr.lovotech.galaxy.qa.backoffice.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import fr.lovotech.galaxy.qa.backoffice.core.TestRequest;
import fr.lovotech.galaxy.qa.backoffice.enums.TestStatus;
import fr.lovotech.galaxy.qa.backoffice.enums.TestType;
import lombok.*;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Update;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Document(collection = "test")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Test extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private ReferenceUID externalUid;
    private String code;
    private Date creationDate;
    private Date endDate;
    private TestStatus status;
    private TestType type;
    @DBRef
    private Application application;
    @Builder.Default
    private List<ConfigurationML> configurationMLS = new ArrayList<>();
    private TestResult testResult = new TestResult();

    public Update updateFrom(TestRequest testRequest) {
        Update testUpdate = new Update();
        if (testRequest.getCode() != null) {
            testUpdate.set("code", testRequest.getCode());
        }
        if (testRequest.getCreationDate() != null) {
            testUpdate.set("creationDate", testRequest.getCreationDate());
        }
        if (testRequest.getEndDate() != null) {
            testUpdate.set("endDate", testRequest.getEndDate());
        }
        if (testRequest.getStatus() != null) {
            testUpdate.set("status", testRequest.getStatus());
        }
        if (testRequest.getType() != null) {
            testUpdate.set("type", testRequest.getType());
        }
        if (testRequest.getConfigurationMLS() != null) {
            testUpdate.set("configurationMLS", testRequest.getConfigurationMLS());
        }
        if (testRequest.getApplication() != null) {
            testUpdate.set("application", testRequest.getApplication());
        }
        if (testRequest.getTestResult() != null) {
            testUpdate.set("testResult", testRequest.getTestResult());
        }
        return testUpdate;
    }
}