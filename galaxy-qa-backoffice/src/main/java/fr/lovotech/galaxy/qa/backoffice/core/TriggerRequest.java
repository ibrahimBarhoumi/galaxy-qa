package fr.lovotech.galaxy.qa.backoffice.core;

import fr.lovotech.galaxy.qa.backoffice.domain.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TriggerRequest {
    protected ReferenceUID externalUid;
    protected String type;
    protected String name;
    protected Boolean deleted;
    protected Date creationDate;
    protected UserAction creator;
    protected TriggerFunctionalType triggerFunctionalType;
    protected TriggerTechnicalType triggerTechnicalType;
    
}
