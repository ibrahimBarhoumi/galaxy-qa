package fr.lovotech.galaxy.qa.backoffice.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TestCaseInformation implements Serializable {

    private static final long serialVersionUID = 1L;

    private Date time;
    private boolean lock;
    private boolean now;
    private TestCase afterTestCase;
    private TestCase testCase;
    private Boolean dependsOnPrevious;
    private Boolean asynchrone;
    private TestCase testCaseToControl;
}
