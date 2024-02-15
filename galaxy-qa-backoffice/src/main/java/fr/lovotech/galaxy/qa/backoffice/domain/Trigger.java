package fr.lovotech.galaxy.qa.backoffice.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import fr.lovotech.galaxy.qa.backoffice.core.TriggerRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.query.Update;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Trigger extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    protected ReferenceUID externalUid;
    protected String type;
    protected String name;
    protected Boolean deleted;
    protected Date creationDate;
    protected TriggerFunctionalType triggerFunctionalType;
    protected TriggerTechnicalType triggerTechnicalType;

    public Trigger(String type, String name, Boolean deleted, Date creationDate, TriggerFunctionalType triggerFunctionalType, TriggerTechnicalType triggerTechnicalType) {
        this.type = type;
        this.name = name;
        this.deleted = deleted;
        this.creationDate = creationDate;
        this.triggerFunctionalType = triggerFunctionalType;
        this.triggerTechnicalType = triggerTechnicalType;
    }

}
