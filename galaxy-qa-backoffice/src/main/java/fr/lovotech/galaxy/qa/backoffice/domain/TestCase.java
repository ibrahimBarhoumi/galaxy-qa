package fr.lovotech.galaxy.qa.backoffice.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.Lists;
import fr.lovotech.galaxy.qa.backoffice.core.TestCaseRequest;
import fr.lovotech.galaxy.qa.backoffice.enums.ControlKeyType;
import fr.lovotech.galaxy.qa.backoffice.enums.TestCaseMode;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Update;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Document(collection = "testCase")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TestCase extends AbstractEntity implements Serializable{

    private static final long serialVersionUID = 1L;

    private ReferenceUID externalUid;
    private String name;
    private Date creationDate;
    private TestCaseMode mode;
    private String controlKey;
    private ControlKeyType controlKeyType;
    private Boolean deleted;
    private Trigger trigger;
    @Builder.Default
    private List<TestCaseDetail> testCaseDetails = Lists.newArrayList();
    @Builder.Default
    private List<TestCaseInformation> testCaseInformations = Lists.newArrayList();

    public Update updateFrom(TestCaseRequest testCaseRequest){
        Update TestCaseUpdate = new Update();
        if(testCaseRequest.getName() != null){
            TestCaseUpdate.set("name", testCaseRequest.getName());
        }
        if(testCaseRequest.getControlKey() != null){
            TestCaseUpdate.set("controlKey", testCaseRequest.getControlKey());
        }
        if(testCaseRequest.getCreationDate() != null){
            TestCaseUpdate.set("creationDate", testCaseRequest.getCreationDate());
        }
        if(testCaseRequest.getMode() != null){
            TestCaseUpdate.set("mode", testCaseRequest.getMode());
        }
        if(testCaseRequest.getControlKeyType() != null){
            TestCaseUpdate.set("controlKeyType", testCaseRequest.getControlKeyType());
        }
        if(testCaseRequest.getDeleted() != null){
            TestCaseUpdate.set("deleted", testCaseRequest.getDeleted());
        }
        if(testCaseRequest.getTrigger() != null){
            TestCaseUpdate.set("trigger", testCaseRequest.getTrigger());
        }
        if(testCaseRequest.getTestCaseDetails() != null){
            TestCaseUpdate.set("testCaseDetails", testCaseRequest.getTestCaseDetails());
        }
        if(testCaseRequest.getTestCaseInformations() != null){
            TestCaseUpdate.set("testCaseInformations", testCaseRequest.getTestCaseInformations());
        }
        return TestCaseUpdate;
    }

}
