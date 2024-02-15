package fr.lovotech.galaxy.qa.backoffice.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

import fr.lovotech.galaxy.qa.backoffice.core.ApplicationVersionRequest;
import fr.lovotech.galaxy.qa.backoffice.enums.ApplicationStatus;
import lombok.*;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Update;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
@Document(collection = "applicationVersion")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApplicationVersion extends AbstractEntity implements Serializable {

    private ReferenceUID externalUid;
    private Date creationDate;
    private ApplicationStatus status;
    private String code;
    private String host;
    private String url;
    private Integer port;
    private String username;
    private String password;
    private String dbHost;
    private Integer dbPort;
    private String dbUsername;
    private String dbPassword;
    @Builder.Default
    private List<ConfigurationML> configurationMLS = new ArrayList<>();
    private Date endDate;
    @DBRef
    private Application application;


    public Update updateFrom (ApplicationVersionRequest applicationVersionRequest){
        Update applicationVersionUpdate = new Update();
        if (applicationVersionRequest.getCreationDate() != null) {
            applicationVersionUpdate.set("creationDate", applicationVersionRequest.getCreationDate());
        }
        if (applicationVersionRequest.getStatus() != null) {
            applicationVersionUpdate.set("status", applicationVersionRequest.getStatus());
        }
        if (applicationVersionRequest.getCode() != null) {
            applicationVersionUpdate.set("code", applicationVersionRequest.getCode());
        }
        if (applicationVersionRequest.getHost() != null) {
            applicationVersionUpdate.set("host", applicationVersionRequest.getHost());
        }
        if (applicationVersionRequest.getUrl() != null) {
            applicationVersionUpdate.set("url", applicationVersionRequest.getUrl());
        }
        if (applicationVersionRequest.getPort() != null) {
            applicationVersionUpdate.set("port", applicationVersionRequest.getPort());
        }
        if (applicationVersionRequest.getUsername() != null) {
            applicationVersionUpdate.set("username", applicationVersionRequest.getUsername());
        }
        if (applicationVersionRequest.getPassword() != null) {
            applicationVersionUpdate.set("password", applicationVersionRequest.getPassword());
        }
        if (applicationVersionRequest.getDbHost() != null) {
            applicationVersionUpdate.set("dbHost", applicationVersionRequest.getDbHost());
        }
        if (applicationVersionRequest.getDbPort() != null) {
            applicationVersionUpdate.set("dbPort", applicationVersionRequest.getDbPort());
        }
        if (applicationVersionRequest.getDbUsername() != null) {
            applicationVersionUpdate.set("dbUsername", applicationVersionRequest.getDbUsername());
        }
        if (applicationVersionRequest.getDbPassword() != null) {
            applicationVersionUpdate.set("dbPassword", applicationVersionRequest.getDbPassword());
        }
        if (applicationVersionRequest.getConfigurationMLS() != null) {
            applicationVersionUpdate.set("configurationMLS", applicationVersionRequest.getConfigurationMLS());
        }
        if (applicationVersionRequest.getEndDate() != null) {
            applicationVersionUpdate.set("endDate", applicationVersionRequest.getEndDate());
        }
        if (applicationVersionRequest.getApplication() != null) {
            applicationVersionUpdate.set("application", applicationVersionRequest.getApplication());
        }
        return applicationVersionUpdate;
    }
}
