package fr.lovotech.galaxy.qa.backoffice.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "testVersion")
public class TestVersion extends AbstractEntity implements Serializable {


    private ReferenceUID externalUid;
    @DBRef
    private ApplicationVersion applicationVersion ;
    @DBRef
    private Test test;
    private Date endDate;
    private Date creationDate;
}