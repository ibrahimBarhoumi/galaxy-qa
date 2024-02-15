package fr.lovotech.galaxy.qa.backoffice.domain;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Document(collection = "testParams")
public class TestParam extends AbstractEntity implements Serializable {
    private ReferenceUID externalUid;
    private String label;
    private String value;
    private String designation;
    private Integer order;
    @DBRef
    private DataTestCase dataTestCase;
    private Date endDate;
}
