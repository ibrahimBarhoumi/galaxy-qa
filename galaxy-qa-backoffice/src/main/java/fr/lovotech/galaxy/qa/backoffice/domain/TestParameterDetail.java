package fr.lovotech.galaxy.qa.backoffice.domain;

import java.io.Serializable;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TestParameterDetail  extends AbstractEntity implements Serializable{
    private Integer order;
    private String designation;
    private String selector;
}
