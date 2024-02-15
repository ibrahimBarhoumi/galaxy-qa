package fr.lovotech.galaxy.qa.backoffice.domain;


import fr.lovotech.galaxy.qa.backoffice.enums.AttachmentTypeEnum;
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

    @Builder.Default
    private AttachmentTypeEnum attachmentType = AttachmentTypeEnum.DEFAULT;
    private Long size;
    @Builder.Default
    private Instant uploadDate = Instant.now();
    private String url;
}
