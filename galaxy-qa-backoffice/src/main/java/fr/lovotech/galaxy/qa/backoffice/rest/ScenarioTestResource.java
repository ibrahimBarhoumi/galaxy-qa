package fr.lovotech.galaxy.qa.backoffice.rest;


import fr.lovotech.galaxy.qa.backoffice.core.ScenarioTestRequest;
import fr.lovotech.galaxy.qa.backoffice.domain.PageResponse;
import fr.lovotech.galaxy.qa.backoffice.domain.ScenarioTest;
import fr.lovotech.galaxy.qa.backoffice.rest.utils.HeaderUtil;
import fr.lovotech.galaxy.qa.backoffice.service.ScenarioTestService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/scenarioTest")
public class ScenarioTestResource {
    private static final String ENTITY_NAME = "gxQAScenarioTest";
    @Value("${spring.application.name}")
    private String applicationName;

    private final ScenarioTestService scenarioTestService;

    public ScenarioTestResource(ScenarioTestService scenarioTestService) {
        this.scenarioTestService = scenarioTestService;
    }

    @PostMapping("")
    public ResponseEntity<ScenarioTest> createScenarioTest(@RequestBody ScenarioTestRequest scenarioTestRequest) throws URISyntaxException, IOException {
        ScenarioTest result = scenarioTestService.createScenarioTest(scenarioTestRequest);
        return ResponseEntity.created(new URI("application/create" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                .body(result);
    }
    @GetMapping("/{scenarioId}")
    public ResponseEntity<PageResponse<ScenarioTest>> getScenarioTestByScenarioId(@PathVariable(value = "scenarioId") String scenarioId,
                                                          @RequestParam(required = false, defaultValue = "0") int page,
                                                          @RequestParam(required = false, defaultValue = "10") int size) {
        Pageable paging = PageRequest.of(page, size);
        PageResponse <ScenarioTest> scenarioTestPageResponse = scenarioTestService.getScenarioTestByScenarioId(scenarioId,paging);
        return new ResponseEntity<PageResponse<ScenarioTest>>(scenarioTestPageResponse, HttpStatus.OK);
    }
    @GetMapping("")
    public ResponseEntity<PageResponse<ScenarioTest>> getAllScenarioTest(@RequestParam(required = false, defaultValue = "0") int page,
                                                                        @RequestParam(required = false, defaultValue = "10") int size
                                                                       )  {
        Pageable paging = PageRequest.of(page, size);
        PageResponse<ScenarioTest> applications = scenarioTestService.getAllScenarioTest(paging);
        return new ResponseEntity<PageResponse<ScenarioTest>>(applications, HttpStatus.OK);
    }
}
