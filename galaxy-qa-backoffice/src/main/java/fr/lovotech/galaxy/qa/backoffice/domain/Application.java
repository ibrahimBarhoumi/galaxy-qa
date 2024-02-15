package fr.lovotech.galaxy.qa.backoffice.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import fr.lovotech.galaxy.qa.backoffice.enums.ApplicationStatus;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Update;

import fr.lovotech.galaxy.qa.backoffice.core.ApplicationRequest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
@Document(collection = "application")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Application extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private ReferenceUID externalUid;
    private Date creationDate;
    private String code;
    private ApplicationStatus applicationStatus;
    private String applicationPhase;
    @Builder.Default
    private List<ConfigurationML> configurationMLS = new ArrayList<>();
    private Date validDate;
    private Date lastUpdate;
    private Date endDate;

    public Update updateFrom(ApplicationRequest applicationRequest){
        Update applicationUpdate = new Update();
        if (applicationRequest.getCreationDate() != null) {
            applicationUpdate.set("creationDate", applicationRequest.getCreationDate());
        }
        if (applicationRequest.getCode() != null) {
            applicationUpdate.set("code", applicationRequest.getCode());
        }
        if (applicationRequest.getApplicationStatus() != null) {
            applicationUpdate.set("status", applicationRequest.getApplicationStatus());
        }
        if (applicationRequest.getApplicationPhase() != null) {
            applicationUpdate.set("applicationPhase", applicationRequest.getApplicationPhase());
        }
        if (applicationRequest.getConfigurationMLS() != null) {
            applicationUpdate.set("configurationMLS", applicationRequest.getConfigurationMLS());
        }
        if (applicationRequest.getValidDate() != null) {
            applicationUpdate.set("validDate", applicationRequest.getValidDate());
        }
        if (applicationRequest.getLastUpdate() != null) {
            applicationUpdate.set("lastUpdate", applicationRequest.getLastUpdate());
        }
        if (applicationRequest.getEndDate() != null) {
            applicationUpdate.set("endDate", applicationRequest.getEndDate());
        } 
        return applicationUpdate;
    }
}
