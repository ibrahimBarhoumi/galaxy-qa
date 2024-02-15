package fr.lovotech.galaxy.qa.backoffice.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Document(collection = "scenarioTest")
public class ScenarioTest extends AbstractEntity implements Serializable {

    private ReferenceUID externalUid;
    private int order;
    @DBRef
    private Test beforeTest;
    @DBRef
    private Test test;
    @DBRef
    @JsonIgnore
    private Scenario scenario;
    private Date endDate;
}
