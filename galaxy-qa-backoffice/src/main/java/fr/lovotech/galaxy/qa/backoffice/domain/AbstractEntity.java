package fr.lovotech.galaxy.qa.backoffice.domain;

import fr.lovotech.galaxy.qa.backoffice.config.security.UserUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractEntity {
    @Id
    private String id;

    private Boolean isActive = true;

    private Boolean isDeleted = false;

    private UserAction createdBy = UserAction.builder()
        .actionDate(Instant.now())
        .user(UserWrapper.builder()
            .reference(UserUtils.getLogin())
            .fullName(UserUtils.getFullName())
            .build())
        .build();
}
