package fr.lovotech.galaxy.qa.backoffice.core;

import fr.lovotech.galaxy.qa.backoffice.domain.*;
import fr.lovotech.galaxy.qa.backoffice.enums.ApplicationStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.*;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationVersionRequest {
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
    private List<ConfigurationML> configurationMLS = new ArrayList<>();
    private Date endDate;
    private Application application;

    public ApplicationVersion buildApplicationVersion(){
       return ApplicationVersion.builder()
        .creationDate(this.creationDate)
        .status(this.status)
        .code(this.code)
        .host(this.host)
        .url(this.url)
        .port(this.port)
        .username(this.username)
        .password(this.password)
        .dbHost(this.dbHost)
        .dbPort(this.dbPort)
        .dbUsername(this.dbUsername)
        .dbPassword(this.dbPassword)
        .configurationMLS(this.configurationMLS)
        .endDate(this.endDate)
        .application(this.application)
        .build();
    }
}
