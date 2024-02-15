package fr.lovotech.galaxy.qa.backoffice.rest;

import fr.lovotech.galaxy.qa.backoffice.client.FileServiceClient;
import fr.lovotech.galaxy.qa.backoffice.domain.ApplicationVersion;
import fr.lovotech.galaxy.qa.backoffice.domain.DataTestCaseVersion;
import fr.lovotech.galaxy.qa.backoffice.domain.FileInfo;
import fr.lovotech.galaxy.qa.backoffice.repository.DataTestCaseVersionRepository;
import fr.lovotech.galaxy.qa.backoffice.service.DataTestCaseVersionService;

import fr.lovotech.galaxy.qa.backoffice.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/datatestcaseversion")
public class DataTestCaseVersionResource {
    private static final String ENTITY_NAME = "gxQADataTestCaseVersion";
    @Value("${spring.application.name}")
    private String applicationName;
    private final DataTestCaseVersionService dataTestCaseVersionService ;
    private final DataTestCaseVersionRepository dataTestCaseVersionRepository;
    private final FileServiceClient fileServiceClient ;
    private final FileService fileService ;

    public DataTestCaseVersionResource(DataTestCaseVersionService dataTestCaseVersionService, DataTestCaseVersionRepository dataTestCaseVersionRepository, FileServiceClient fileServiceClient, FileService fileService) {
        this.dataTestCaseVersionService = dataTestCaseVersionService;

        this.dataTestCaseVersionRepository = dataTestCaseVersionRepository;
        this.fileServiceClient = fileServiceClient;
        this.fileService = fileService;
    }

    @GetMapping("/{dataTestCaseId}")
    public Page<ApplicationVersion> getVersionsByDataTestCasesId(@PathVariable(value = "dataTestCaseId") String dataTestCaseId,
                                                                 @RequestParam(required = false, defaultValue = "0") int page,
                                                                 @RequestParam(required = false, defaultValue = "10") int size) {
        Pageable paging = PageRequest.of(page, size);
        Page <ApplicationVersion> applicationVersionList = dataTestCaseVersionService.getVersionsByDataTestCaseId(dataTestCaseId,paging);
        return applicationVersionList;
    }

    @RequestMapping(value = "/file/upload", method = RequestMethod.POST)
    public FileInfo getFilesInfoList(@RequestParam MultipartFile file,
                                     @RequestParam String fileName,
                                     @RequestParam String fileType,
                                     @RequestParam String organization,
                                     @RequestParam(required = false) String attachmentType,
                                     @RequestParam(required = false) String origin,
                                     @RequestParam String businessKey) throws IOException{
        return dataTestCaseVersionService.getFilesInfos(file,fileName,fileType,organization, attachmentType, origin, businessKey);
    }

    @GetMapping("/version/{testId}")
    public List<ApplicationVersion> getVersionsByTestId(@PathVariable(value = "testId") String testId){
        List <ApplicationVersion> dataTestCaseVersions = dataTestCaseVersionService.getVersionsByTestId(testId);
        return dataTestCaseVersions;
    }

    @GetMapping("/test/{testId}/{versionId}")
    public List<DataTestCaseVersion> getVersionsByTestId(@PathVariable(value = "testId") String testId,@PathVariable(value = "versionId") String versionId){
        List<DataTestCaseVersion> dataTestCaseVersion = dataTestCaseVersionService.getDataTestCaseVersionByTestIdAndVersionId(testId,versionId);
        return dataTestCaseVersion;
    }
    @GetMapping("/id/{id}")
    public DataTestCaseVersion getById (@PathVariable(value = "id") String id){
       return this.dataTestCaseVersionService.getDataTestCaseVerisonById(id);
    }
    @PostMapping("/file")
    public ResponseEntity<List<byte[]>> getFile (@RequestBody List<String> fileId) throws IOException {
        List<byte[]> list = fileService.getImages(fileId);
        return new ResponseEntity<List<byte[]>>(list, HttpStatus.OK);
    }

    @GetMapping("/file/{fileId}")
    public ResponseEntity<byte[]> getFile (@PathVariable(value = "fileId") String fileId) throws IOException {
        return fileServiceClient.loadFiles(fileId);
    }
    @GetMapping("/datatestcase/{datatestcaseId}")
    public DataTestCaseVersion getDataTestCaseVersionsByDataTestCaseId(@PathVariable(value = "datatestcaseId") String datatestcaseId){
         DataTestCaseVersion dataTestCaseVersion = dataTestCaseVersionService.getDataTestCaseVersionByDataTestCaseId(datatestcaseId);
        return dataTestCaseVersion;
    }
}
