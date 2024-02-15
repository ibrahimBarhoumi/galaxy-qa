package fr.lovotech.galaxy.qa.fileService.rest;

import com.google.common.collect.Lists;
import fr.lovotech.galaxy.qa.fileService.domain.FileInfo;
import fr.lovotech.galaxy.qa.fileService.domain.MetaDataFile;
import fr.lovotech.galaxy.qa.fileService.exception.TechnicalException;
import fr.lovotech.galaxy.qa.fileService.service.GridFsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api")
public class UploadFileResource {

    private GridFsService gridFsService;


    public UploadFileResource(GridFsService gridFsService) {
        this.gridFsService = gridFsService;
    }


    @RequestMapping(value = "/files/upload", method = RequestMethod.POST)
    public ResponseEntity<List<FileInfo>> uploadPublicListFile(@RequestPart(value = "files") List<MultipartFile> files,
                                                               @RequestParam String organization,
                                                               @RequestParam(required = false) String attachmentType,
                                                               @RequestParam(required = false) String origin,
                                                               @RequestParam String businessKey) throws IOException {
        List<FileInfo> fileInfoList = Lists.newArrayList();
        for (MultipartFile file : files) {
            if (!FileUtils.isContentTypeAllowed(file.getContentType())) {
                throw new TechnicalException("WRONG_FILE_TYPE");
            }
            FileInfo fileInfo = gridFsService.getFileInfo(file, file.getOriginalFilename(), file.getContentType(), organization, attachmentType, origin, businessKey);
            fileInfoList.add(fileInfo);
        }
        return ResponseEntity.ok().body(fileInfoList);
    }

    /**
     * uploader un fichier dans la réponse par le client publique
     *
     * @param file
     * @return
     */
    @RequestMapping(value = "/file/upload", method = RequestMethod.POST)
    public ResponseEntity<FileInfo> uploadPublicFile(
            @RequestParam MultipartFile file,
            @RequestParam String fileName,
            @RequestParam String fileType,
            @RequestParam String organization,
            @RequestParam(required = false) String attachmentType,
            @RequestParam(required = false) String origin,
            @RequestParam String businessKey
    ) throws IOException {

        if (!FileUtils.isContentTypeAllowed(file.getContentType())) {
            throw new TechnicalException("WRONG_FILE_TYPE");
        }
        FileInfo fileInfo = gridFsService.getFileInfo(file, fileName, fileType, organization, attachmentType, origin, businessKey);
        return ResponseEntity.ok().body(fileInfo);
    }


    @RequestMapping(value = "/files/{fileId}", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> loadFile(@PathVariable(required = true) String fileId) throws IOException {
        GridFsResource gridFSFile = gridFsService.findById(fileId);

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(gridFSFile.getContentType()))
                .contentLength(gridFSFile.contentLength())
                .body(gridFSFile);
    }

    @RequestMapping(value = "/file/{businessKey}/load", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> loadFileByBusinessKey(@PathVariable(required = true) String businessKey) throws IOException {
        GridFsResource gridFSFile = gridFsService.findByBusinessKey(businessKey);

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(gridFSFile.getContentType()))
                .contentLength(gridFSFile.contentLength())
                .body(gridFSFile);
    }


    @RequestMapping(value = "/files/info/{fileId}", method = RequestMethod.GET)
    public MetaDataFile loadFileWithInfo(@PathVariable(required = true) String fileId) throws IOException {
        MetaDataFile metaDataFile = gridFsService.findFileById(fileId);
        return metaDataFile;
    }

    /**
     * remove un fichier
     *
     * @param fileId)
     * @return
     */
    @DeleteMapping(value = "/file/remove/{fileId}")
    public ResponseEntity<String> deleteFile(@PathVariable String fileId) {
        gridFsService.delete(fileId);
        return ResponseEntity.ok().body("File removed");
    }

    /**
     * remove list files
     *
     * @param filesId)
     * @return
     */
    @DeleteMapping(value = "/files/remove")
    public void deleteFiles(@RequestParam("filesId") List<String> filesId) {
        gridFsService.deleteFiles(filesId);
    }

    @PostMapping(value = "/file/{fileId}/duplicate")
    public ResponseEntity<FileInfo> duplicateFile(@PathVariable String fileId,
                                                  @RequestParam String fileName,
                                                  @RequestParam String fileType,
                                                  @RequestParam String organization,
                                                  @RequestParam(required = false) String businessKey) throws IOException {
        Map<String, String> metaData = new HashMap<>();
        metaData.put("fileName", fileName);
        metaData.put("fileType", fileType);
        metaData.put("organization", organization);
        if (StringUtils.isNoneBlank(businessKey)) {
            metaData.put("businessKey", businessKey);
        }
        metaData.put("uploadDate", Instant.now().toString());

        FileInfo fileInfo = gridFsService.duplicateFile(fileId, metaData, fileName, fileType);
        return ResponseEntity.ok().body(fileInfo);
    }

}
