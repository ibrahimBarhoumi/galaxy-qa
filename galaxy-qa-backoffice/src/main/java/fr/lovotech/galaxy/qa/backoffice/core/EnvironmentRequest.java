package fr.lovotech.galaxy.qa.backoffice.core;

import fr.lovotech.galaxy.qa.backoffice.domain.Environment;
import fr.lovotech.galaxy.qa.backoffice.domain.ReferenceUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnvironmentRequest {
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

    public Environment buildEnvironment(){
        return Environment.builder()
                .environmentName(this.environmentName)
                .environmentDescription(this.environmentDescription)
                .databaseHost(this.databaseHost)
                .databaseLogin(this.databaseLogin)
                .databasePassword(this.databasePassword)
                .databaseSchema(this.databaseSchema)
                .databaseService(this.databaseService)
                .databasePort(this.databasePort)
                .appHost(this.appHost)
                .appLogin(this.appLogin)
                .appPassword(this.appPassword)
                .appLoginUrl(this.appLoginUrl)
                .build();
    }
}
