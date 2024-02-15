package fr.lovotech.galaxy.qa.backoffice.rest;
import fr.lovotech.galaxy.qa.backoffice.core.ApplicationVersionRequest;
import fr.lovotech.galaxy.qa.backoffice.domain.ApplicationVersion;
import fr.lovotech.galaxy.qa.backoffice.domain.PageResponse;
import fr.lovotech.galaxy.qa.backoffice.rest.utils.HeaderUtil;
import fr.lovotech.galaxy.qa.backoffice.service.ApplicationVersionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/applicationVersion")
public class ApplicationVersionResource {
    private static final String ENTITY_NAME = "gxQAApplicationVersion";
    @Value("${spring.application.name}")
    private String applicationName;

    private final ApplicationVersionService applicationVersionService;

    public ApplicationVersionResource(ApplicationVersionService applicationVersionService) {
        this.applicationVersionService = applicationVersionService;
    }

    @PostMapping("")
    public ResponseEntity<ApplicationVersion> createApplicationVersion(@RequestBody ApplicationVersionRequest applicationVersionRequest) throws URISyntaxException, IOException {
        ApplicationVersion result = applicationVersionService.createApplicationVersion(applicationVersionRequest);
        return ResponseEntity.created(new URI("applicationVersion/create" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                .body(result);
    }

    @GetMapping("")
    public ResponseEntity<PageResponse<ApplicationVersion>> getAllApplicationVersions(@RequestParam(required = false, defaultValue = "0") int page,
                                                            @RequestParam(required = false, defaultValue = "10") int size,
                                                            @RequestParam Map<String, Object> inputs) {
                                                                Pageable paging = PageRequest.of(page, size);
        PageResponse<ApplicationVersion> applicationVersions = applicationVersionService.getAllApplicationVersions(paging, inputs);
        return new ResponseEntity<PageResponse<ApplicationVersion>>(applicationVersions, HttpStatus.OK);
    }

    @PutMapping("/{applicationVersionId}")
    public ResponseEntity<ApplicationVersion> updateApplicationVersion(@PathVariable() String applicationVersionId,
                                                     @RequestBody ApplicationVersionRequest applicationVersionRequest) {
        ApplicationVersion applicationVersionUpdated = applicationVersionService.updateApplicationVersion(applicationVersionId, applicationVersionRequest);
        return new ResponseEntity<>(applicationVersionUpdated, HttpStatus.OK);

    }

    @DeleteMapping("/{applicationVersionId}")
    public ResponseEntity deleteApplicationVersion(@PathVariable() String applicationVersionId) {
        applicationVersionService.deleteApplicationVersion(applicationVersionId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/{applicationVersionId}")
    public ResponseEntity<ApplicationVersion> getApplicationVersionById(@PathVariable(value = "applicationVersionId") String applicationVersionId) {
        return new ResponseEntity<>(applicationVersionService.getApplicationVersionById(applicationVersionId), HttpStatus.OK);
    }

    @PutMapping("/{applicationId}/{languageCode}")
    public ResponseEntity<ApplicationVersion> updateCampaignTestMl(@PathVariable() String applicationId, @PathVariable() String languageCode , @RequestBody ApplicationVersionRequest applicationVersionRequest) {
        ApplicationVersion applicationVersionMlUpdated = applicationVersionService.deleteConfigurationML(applicationId,languageCode,applicationVersionRequest);
        return new ResponseEntity<>(applicationVersionMlUpdated, HttpStatus.OK);

    }

    @GetMapping("application/{applicationId}")
    public ResponseEntity<PageResponse<ApplicationVersion>> getAllApplicationVersionsById(@PathVariable() String applicationId,@RequestParam(required = false, defaultValue = "0") int page,
                                                                                      @RequestParam(required = false, defaultValue = "10") int size) {
        Pageable paging = PageRequest.of(page, size);
        PageResponse<ApplicationVersion> applicationVersions = applicationVersionService.findVersionsByApplicationId(paging, applicationId);
        return new ResponseEntity<PageResponse<ApplicationVersion>>(applicationVersions, HttpStatus.OK);
    }
}
