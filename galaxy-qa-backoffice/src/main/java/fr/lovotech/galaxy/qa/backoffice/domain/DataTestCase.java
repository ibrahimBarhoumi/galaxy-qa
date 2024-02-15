package fr.lovotech.galaxy.qa.backoffice.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;


import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "dataTestCases")
@Builder(toBuilder = true)
public class DataTestCase extends AbstractEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private ReferenceUID externalUid;
    private Date creationDate;
    private Date endDate;
    private String code;
    @DBRef
    private Test test;
    private boolean isDefault;

}