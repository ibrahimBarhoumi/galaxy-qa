package fr.lovotech.galaxy.qa.fileService.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class UserWrapper implements Serializable {

    private String reference;
    private String fullName;

}
