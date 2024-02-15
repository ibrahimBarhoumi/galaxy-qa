package fr.lovotech.galaxy.qa.backoffice.remote.file;

import feign.Headers;
import feign.Response;
import fr.lovotech.galaxy.qa.backoffice.domain.FileInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FeignClient(name = "FileClient", url = "${service.external.gxqa.file.baseUrl}")
public interface FileClient {

    @RequestMapping(value = "/file/upload", method = RequestMethod.POST,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    FileInfo uploadPublicFile(@RequestPart("file") MultipartFile file,
                              @RequestParam String fileName,
                              @RequestParam String fileType,
                              @RequestParam String organization,
                              @RequestParam(required = false) String attachmentType,
                              @RequestParam(required = false) String origin,
                              @RequestParam String businessKey);

    @RequestMapping(value = "/files/remove", method = RequestMethod.DELETE)
    @Headers("Content-Type: application/json")
    ResponseEntity<String> deleteFiles(@RequestParam("filesId") List<String> filesId);

        @RequestMapping(value = "/files/{fileId}", method = RequestMethod.GET)
    Response loadFile(@PathVariable("fileId") String fileId);


}

