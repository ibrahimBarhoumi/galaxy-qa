package fr.lovotech.galaxy.qa.fileService.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

/**
 * A FileInfo.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class FileInfo implements Serializable {

    private String id;

    private String name;

    private String type;

    private String attachmentType;

    private Long size;

    @Builder.Default
    private String uploadDate = Instant.now().toString();

}
