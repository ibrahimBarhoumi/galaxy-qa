package fr.lovotech.galaxy.qa.backoffice.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import fr.lovotech.galaxy.qa.backoffice.core.UITestRequest;
import fr.lovotech.galaxy.qa.backoffice.enums.UITestType;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Document(collection = "environment")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UITest extends Trigger {

    private String description;
    private UITestType uiTestType;
    private Environment environment;

    @Builder
    public UITest(String type, String name, Boolean deleted, Date creationDate, TriggerFunctionalType triggerFunctionalType, TriggerTechnicalType triggerTechnicalType, String description, UITestType uiTestType, Environment environment) {
        super(type, name, deleted, creationDate, triggerFunctionalType, triggerTechnicalType);
        this.description = description;
        this.uiTestType = uiTestType;
        this.environment = environment; 
    }

    public Update updateFrom(UITestRequest uiTestRequest) {
        Update uiTestUpdate = new Update();
        if (uiTestRequest.getName() != null) {
            uiTestUpdate.set("name", uiTestRequest.getName());
        }
        if (uiTestRequest.getType() != null) {
            uiTestUpdate.set("type", uiTestRequest.getType());
        }
        if (uiTestRequest.getDeleted() != null) {
            uiTestUpdate.set("deleted", uiTestRequest.getDeleted());
        }
        if (uiTestRequest.getCreator() != null) {
            uiTestUpdate.set("creationDate", uiTestRequest.getCreator());
        }
        if (uiTestRequest.getTriggerFunctionalType() != null) {
            uiTestUpdate.set("triggerFunctionalType", uiTestRequest.getTriggerFunctionalType());
        }
        if (uiTestRequest.getTriggerTechnicalType() != null) {
            uiTestUpdate.set("triggerTechnicalType", uiTestRequest.getTriggerTechnicalType());
        }
        if (uiTestRequest.getDescription() != null) {
            uiTestUpdate.set("description", uiTestRequest.getDescription());
        }
        if (uiTestRequest.getUiTestType() != null) {
            uiTestUpdate.set("uiTestType", uiTestRequest.getUiTestType());
        }
        if (uiTestRequest.getEnvironment() != null) {
            uiTestUpdate.set("environment", uiTestRequest.getEnvironment());
        }
        return uiTestUpdate;

    }
}
