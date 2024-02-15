package fr.lovotech.galaxy.qa.backoffice.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@Builder(toBuilder = true)
@Document(collection = "testParameter")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TestParameter  extends AbstractEntity implements Serializable{
    private ReferenceUID externalUid;
    private String code ;
    @DBRef
    private Test test;
    @DBRef
    private ApplicationVersion applicationVersion;
    @Builder.Default
    private List<TestParameterDetail> testParameterDetails = new ArrayList<>();
}
