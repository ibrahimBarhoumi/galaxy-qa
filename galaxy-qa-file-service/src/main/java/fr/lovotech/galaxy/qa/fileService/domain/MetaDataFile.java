package fr.lovotech.galaxy.qa.fileService.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class MetaDataFile {

    private String fileName;
    private String fileType;
    private String folderId;
    private String uploadDate;
    private String origin;
    private String organization;

}
