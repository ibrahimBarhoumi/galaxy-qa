package fr.lovotech.galaxy.qa.backoffice.core;

import fr.lovotech.galaxy.qa.backoffice.domain.ApplicationVersion;
import fr.lovotech.galaxy.qa.backoffice.domain.Test;
import fr.lovotech.galaxy.qa.backoffice.domain.TestVersion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestVersionRequest {
  
    private Date creationDate;
    private Date endDate;
    private ApplicationVersion applicationVersion;
    private Test test;

    public TestVersion buildTestVersion() {
        return TestVersion.builder()
                .creationDate(this.creationDate)
                .endDate(this.endDate)
                .applicationVersion(this.applicationVersion)
                .test(this.test)
                .build();
    }
}
