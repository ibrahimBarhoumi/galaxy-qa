package fr.lovotech.galaxy.qa.backoffice.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Update;

import fr.lovotech.galaxy.qa.backoffice.core.EnvironmentRequest;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
@Document(collection = "environment")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Environment extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private ReferenceUID externalUid;
    private String environmentName;
    private String environmentDescription;

    private String databaseHost;
    private String databaseLogin;
    private String databasePassword;
    private String databaseSchema;
    private String databaseService;
    private String databasePort;
    private String appHost;
    private String appLogin;
    private String appPassword;
    private String appLoginUrl;


    public Update updateFrom(EnvironmentRequest environmentRequest){
        Update environmentUpdate = new Update();
        if (environmentRequest.getEnvironmentName() != null) {
            environmentUpdate.set("environmentName", environmentRequest.getEnvironmentName());
        }
        if (environmentRequest.getEnvironmentDescription() != null) {
            environmentUpdate.set("environmentDescription", environmentRequest.getEnvironmentDescription());
        }
        if (environmentRequest.getDatabaseHost() != null) {
            environmentUpdate.set("databaseHost", environmentRequest.getDatabaseHost());
        }
        if (environmentRequest.getDatabaseLogin() != null) {
            environmentUpdate.set("databaseLogin", environmentRequest.getDatabaseHost());
        }
        if (environmentRequest.getDatabasePassword() != null) {
            environmentUpdate.set("databasePassword", environmentRequest.getDatabasePassword());
        }
        if (environmentRequest.getDatabaseSchema() != null) {
            environmentUpdate.set("databaseSchema", environmentRequest.getDatabaseSchema());
        }
        if (environmentRequest.getDatabaseService() != null) {
            environmentUpdate.set("databaseService", environmentRequest.getDatabaseService());
        }
        if (environmentRequest.getDatabasePort() != null) {
            environmentUpdate.set("databasePort", environmentRequest.getDatabasePort());
        }
        if (environmentRequest.getAppHost() != null) {
            environmentUpdate.set("appHost", environmentRequest.getAppHost());
        }
        if (environmentRequest.getAppLogin() != null) {
            environmentUpdate.set("appLogin", environmentRequest.getAppLogin());
        }
        if (environmentRequest.getAppPassword() != null) {
            environmentUpdate.set("appPassword", environmentRequest.getAppPassword());
        }
        if (environmentRequest.getAppLoginUrl() != null) {
            environmentUpdate.set("appLoginUrl", environmentRequest.getAppLoginUrl());
        }
        return environmentUpdate;
    }
}
