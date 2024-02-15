package fr.lovotech.galaxy.qa.backoffice.core;


import fr.lovotech.galaxy.qa.backoffice.domain.Environment;
import fr.lovotech.galaxy.qa.backoffice.domain.UITest;
import fr.lovotech.galaxy.qa.backoffice.enums.UITestType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UITestRequest extends TriggerRequest {

    private String description;
    private UITestType uiTestType;
    private Environment environment;

    public UITest buildUITest() {
        return UITest.builder()
                .creationDate(this.creationDate)
                .type(this.type)
                .name(this.name)
                .deleted(this.deleted)
                .triggerFunctionalType(this.triggerFunctionalType)
                .triggerTechnicalType(this.triggerTechnicalType)
                .description(this.description)
                .uiTestType(this.uiTestType)
                .environment(this.environment)
                .build();
    }
}
