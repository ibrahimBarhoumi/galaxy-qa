package fr.lovotech.galaxy.qa.backoffice.rest;

import fr.lovotech.galaxy.qa.backoffice.core.ScenarioVersionRequest;
import fr.lovotech.galaxy.qa.backoffice.domain.Application;
import fr.lovotech.galaxy.qa.backoffice.domain.ApplicationVersion;
import fr.lovotech.galaxy.qa.backoffice.domain.PageResponse;
import fr.lovotech.galaxy.qa.backoffice.domain.ScenarioVersion;
import fr.lovotech.galaxy.qa.backoffice.rest.utils.HeaderUtil;
import fr.lovotech.galaxy.qa.backoffice.service.ScenarioVersionService;
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
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/scenarioVersion")
public class ScenarioVersionResource {
    private static final String ENTITY_NAME = "gxQAScenarioVersion";
    @Value("${spring.application.name}")
    private String applicationName;
    private final ScenarioVersionService scenarioVersionService;

    public ScenarioVersionResource(ScenarioVersionService scenarioVersionService) {
        this.scenarioVersionService = scenarioVersionService;
    }

    @PostMapping("")
    public ResponseEntity<ScenarioVersion> createScenarioVersion(@RequestBody ScenarioVersionRequest scenarioVersionRequest) throws URISyntaxException, IOException {
        ScenarioVersion result = scenarioVersionService.createScenarioVersion(scenarioVersionRequest);
        return ResponseEntity.created(new URI("scenarioVersion/create" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                .body(result);
    }
    @GetMapping("/{versionId}")
    public ResponseEntity<PageResponse<ScenarioVersion>> getScenarioVersionByScenarioId(@PathVariable(value = "versionId") String versionId,
                                                                                  @RequestParam(required = false, defaultValue = "0") int page,
                                                                                  @RequestParam(required = false, defaultValue = "10") int size) {
        Pageable paging = PageRequest.of(page, size);
        PageResponse <ScenarioVersion> scenarioVersionPageResponse = scenarioVersionService.findScenariosByVersionId(paging,versionId);
        return new ResponseEntity<PageResponse<ScenarioVersion>>(scenarioVersionPageResponse, HttpStatus.OK);
    }
    @GetMapping("")
    public ResponseEntity<PageResponse<ScenarioVersion>> getAllScenarioVersion(@RequestParam(required = false, defaultValue = "0") int page,
                                                                         @RequestParam(required = false, defaultValue = "10") int size
    )  {
        Pageable paging = PageRequest.of(page, size);
        PageResponse<ScenarioVersion> applications = scenarioVersionService.getAllScenarioVersions(paging);
        return new ResponseEntity<PageResponse<ScenarioVersion>>(applications, HttpStatus.OK);
    }

    @GetMapping("/application/{scenarioId}")
    public Application getApplicationByScenarioId(@PathVariable(value = "scenarioId") String scenarioId) {
        Application application = scenarioVersionService.getApplicationByScenarioId(scenarioId);
        return application;
    }
    @GetMapping("/version/{scenarioId}")
    public Page<ApplicationVersion> getVersionsByScenarioId(@PathVariable(value = "scenarioId") String scenarioId) {
        Page<ApplicationVersion> applicationVersionList = scenarioVersionService.getVersionsByScenarioId(scenarioId);
        return applicationVersionList;
    }
}
