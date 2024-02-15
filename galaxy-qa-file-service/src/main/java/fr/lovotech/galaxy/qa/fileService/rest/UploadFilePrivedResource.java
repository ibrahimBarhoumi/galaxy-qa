package fr.lovotech.galaxy.qa.fileService.rest;

import fr.lovotech.galaxy.qa.fileService.domain.FileInfo;
import fr.lovotech.galaxy.qa.fileService.service.GridFsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UploadFilePrivedResource {

    private GridFsService gridFsService;


    public UploadFilePrivedResource(GridFsService gridFsService) {
        this.gridFsService = gridFsService;
    }

    @RequestMapping(value = "/private/upload", method = RequestMethod.POST)
    public ResponseEntity<FileInfo> uploadPrivateFile(
            @RequestBody byte[] inputArray,
            @RequestParam String fileName,
            @RequestParam String fileType,
            @RequestParam String organization,
            @RequestParam String attachmentType
    ) throws IOException {
        Map<String, String> metaData = new HashMap<>();
        metaData.put("fileName", fileName);
        metaData.put("fileType", fileType);
        metaData.put("organization", organization);
        metaData.put("attachmentType", attachmentType);
        metaData.put("uploadDate", Instant.now().toString());

        FileInfo fileInfo = gridFsService.saveAttachmentsFilesFromBytesArray(inputArray, metaData, fileName, fileType);
        return ResponseEntity.ok().body(fileInfo);
    }
}
