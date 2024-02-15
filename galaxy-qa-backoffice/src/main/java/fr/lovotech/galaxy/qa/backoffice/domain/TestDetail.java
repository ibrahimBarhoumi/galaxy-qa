package fr.lovotech.galaxy.qa.backoffice.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import fr.lovotech.galaxy.qa.backoffice.enums.CampaignType;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TestDetail extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private ReferenceUID externalUid;
    private Date startDate;
    private Date endDate;
    // TODO to be virifyed later
    //  private EnvironmentMode environmentMode;
    private CampaignType testType;
    private UserAction launcher;
}
