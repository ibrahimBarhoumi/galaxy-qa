This file should contain jHipster Registry .war files that will be deployed directly to the FileService.

to deploy, we could use this command directly:

    java -jar wars/jhipster-registry-4.1.1.war --spring.profiles.active=dev --spring.security.user.password=admin --jhipster.security.authentication.jwt.secret=admin --spring.cloud.config.server.composite.0.type=git --spring.cloud.config.server.composite.0.uri=https://github.com/jhipster/jhipster-registry-sample-config

**we need to change the war version when making updates!!!!

