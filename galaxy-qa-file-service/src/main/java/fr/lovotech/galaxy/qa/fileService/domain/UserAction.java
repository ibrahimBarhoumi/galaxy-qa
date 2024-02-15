package fr.lovotech.galaxy.qa.fileService.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class UserAction implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    private UserWrapper user;

    @Builder.Default
    @NotNull
    private Instant actionDate = Instant.now();

}
