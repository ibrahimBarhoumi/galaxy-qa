package fr.lovotech.galaxy.qa.backoffice.domain;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class TestResult extends AbstractEntity implements Serializable {
    private ReferenceUID externalUid;
    private List<FileInfo> screenshots = new ArrayList<>();
    private LocalDateTime testDateTime = LocalDateTime.now();
    private FileInfo report;
    private TestReportDetails testReportDetails = new TestReportDetails();
}
