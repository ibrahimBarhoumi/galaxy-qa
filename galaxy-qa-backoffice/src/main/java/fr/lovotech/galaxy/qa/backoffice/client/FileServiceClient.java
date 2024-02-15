package fr.lovotech.galaxy.qa.backoffice.client;

import feign.Response;
import fr.lovotech.galaxy.qa.backoffice.domain.FileInfo;
import fr.lovotech.galaxy.qa.backoffice.domain.MetaDataFile;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@FeignClient(url = "${internal.file.endpoint.url}", name = "fileClient")
public interface FileServiceClient {
    @RequestMapping(value = "/file/upload", method = RequestMethod.POST,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    FileInfo uploadPublicFile(@RequestPart("file") MultipartFile file,
                              @RequestParam String fileName,
                              @RequestParam String fileType,
                              @RequestParam String organization,
                              @RequestParam(required = false) String attachmentType,
                              @RequestParam(required = false) String origin,
                              @RequestParam String businessKey);

    @RequestMapping(value = "/files/{fileId}", method = RequestMethod.GET )
    byte[] loadFile(@PathVariable("fileId") String fileId);

    @RequestMapping(value = "/files/{fileId}", method = RequestMethod.GET )
    ResponseEntity<byte[]> loadFiles(@PathVariable("fileId") String fileId);
}
