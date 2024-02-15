package fr.lovotech.galaxy.qa.backoffice.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.Lists;
import fr.lovotech.galaxy.qa.backoffice.enums.EnvironmentVersion;
import fr.lovotech.galaxy.qa.backoffice.enums.TestCaseStatus;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode()
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TestCaseDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    private TestCase testCase;
    private String refControlKeyValue;
    private String testControlKeyValue;
    private Date endDate;
    private TestCaseStatus status;
    private TestDetail testDetail;
    private Boolean containsResource;
    private Boolean asynchroneValue;
    private String asynchroneTempFile;
    private EnvironmentVersion environmentVersion;
    @Builder.Default
    private List<TestCaseResult> testCaseResults = Lists.newArrayList();
    @Builder.Default
    private List<UITestScreenShot> screenShots = Lists.newArrayList();
}
