package fr.lovotech.galaxy.qa.backoffice.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class TestReportDetails  extends AbstractEntity implements Serializable {
    private ReferenceUID externalUid;
    private String startTime;
    private String endTime;
    private String testTimeTaken;
    private Boolean isPassed;
}
