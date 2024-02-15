package fr.lovotech.galaxy.qa.backoffice.rest;
import fr.lovotech.galaxy.qa.backoffice.core.ApplicationRequest;
import fr.lovotech.galaxy.qa.backoffice.core.CampaignTestRequest;
import fr.lovotech.galaxy.qa.backoffice.domain.Application;
import fr.lovotech.galaxy.qa.backoffice.domain.CampaignTest;
import fr.lovotech.galaxy.qa.backoffice.domain.PageResponse;
import fr.lovotech.galaxy.qa.backoffice.rest.utils.HeaderUtil;
import fr.lovotech.galaxy.qa.backoffice.service.ApplicationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/application")
public class ApplicationResource {
    private static final String ENTITY_NAME = "gxQAApplication";
    @Value("${spring.application.name}")
    private String applicationName;

    private final ApplicationService applicationService;

    public ApplicationResource(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping("")
    public ResponseEntity<Application> createApplication(@RequestBody ApplicationRequest applicationRequest) throws URISyntaxException, IOException {
        Application result = applicationService.createApplication(applicationRequest);
        return ResponseEntity.created(new URI("application/create" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                .body(result);
    }

    @GetMapping("")
    public ResponseEntity<PageResponse<Application>> getAllApplications(@RequestParam(required = false, defaultValue = "0") int page,
                                                                        @RequestParam(required = false, defaultValue = "10") int size,
                                                                        @RequestParam Map<String, Object> inputs) throws ParseException {
        Pageable paging = PageRequest.of(page, size);
        PageResponse<Application> applications = applicationService.getAllApplications(paging, inputs);
        return new ResponseEntity<PageResponse<Application>>(applications, HttpStatus.OK);
    }

    @PutMapping("/{applicationId}")
    public ResponseEntity<Application> updateApplication(@PathVariable() String applicationId,
                                                     @RequestBody ApplicationRequest applicationRequest) {
        Application applicationUpdated = applicationService.updateApplication(applicationId, applicationRequest);
        return new ResponseEntity<>(applicationUpdated, HttpStatus.OK);

    }

    @DeleteMapping("/{applicationId}")
    public ResponseEntity deleteApplication(@PathVariable() String applicationId) {
        applicationService.deleteApplication(applicationId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/{applicationId}")
    public ResponseEntity<Application> getApplicationById(@PathVariable(value = "applicationId") String applicationId) {
        return new ResponseEntity<>(applicationService.getApplicationById(applicationId), HttpStatus.OK);
    }

    @PutMapping("/{applicationId}/{languageCode}")
    public ResponseEntity<Application> updateCampaignTestMl(@PathVariable() String applicationId, @PathVariable() String languageCode , @RequestBody ApplicationRequest applicationRequest) {
        Application applicationMlUpdated = applicationService.deleteConfigurationML(applicationId,languageCode,applicationRequest);
        return new ResponseEntity<>(applicationMlUpdated, HttpStatus.OK);

    }
}
