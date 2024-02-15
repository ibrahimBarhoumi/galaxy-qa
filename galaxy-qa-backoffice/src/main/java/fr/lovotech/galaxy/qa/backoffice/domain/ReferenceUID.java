package fr.lovotech.galaxy.qa.backoffice.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ReferenceUID implements Serializable {

    @Builder.Default
    private String id = UUID.randomUUID().toString();

    private boolean expired = false;

}
