package fr.lovotech.galaxy.qa.backoffice.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "dataTestCaseVersion")
@Builder(toBuilder = true)
public class DataTestCaseVersion extends AbstractEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private ReferenceUID externalUid;
    private Date creationDate;
    @DBRef
    private DataTestCase dataTestCase;
    @DBRef
    private ApplicationVersion applicationVersion;
    private FileInfo fileInfo;
    private Date endDate;
}
