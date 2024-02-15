package fr.lovotech.galaxy.qa.backoffice.rest;

import fr.lovotech.galaxy.qa.backoffice.core.ApplicationRequest;
import fr.lovotech.galaxy.qa.backoffice.core.ScenarioRequest;
import fr.lovotech.galaxy.qa.backoffice.domain.Application;
import fr.lovotech.galaxy.qa.backoffice.domain.PageResponse;
import fr.lovotech.galaxy.qa.backoffice.domain.Scenario;
import fr.lovotech.galaxy.qa.backoffice.rest.utils.HeaderUtil;
import fr.lovotech.galaxy.qa.backoffice.service.ScenarioService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/scenario", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8")
public class ScenarioResource {

    private static final String ENTITY_NAME = "gxQAScenario";
    @Value("${spring.application.name}")
    private String applicationName;

    private final ScenarioService ScenarioService;

    public ScenarioResource(ScenarioService scenarioService) {
        this.ScenarioService = scenarioService;
    }

    @PostMapping("")
    public ResponseEntity<Scenario> createScenario(@RequestBody ScenarioRequest scenarioRequest) throws URISyntaxException {
        Scenario result = ScenarioService.createScenario(scenarioRequest);
        return ResponseEntity.created(new URI("Scenario/create" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                .body(result);
    }

    @GetMapping("")
    public ResponseEntity<PageResponse<Scenario>> getAllScenarios(@RequestParam(required = false, defaultValue = "0") int page,
                                                              @RequestParam(required = false, defaultValue = "10") int size,
                                                          @RequestParam Map<String, Object> inputs) {
        Pageable paging = PageRequest.of(page, size);
        PageResponse<Scenario> scenarios = ScenarioService.getAllScenarios(paging, inputs);
        return new ResponseEntity<PageResponse<Scenario>>(scenarios, HttpStatus.OK);
    }

    @PutMapping("/{scenarioId}")
    public ResponseEntity<Scenario> updateScenario(@PathVariable() String scenarioId,
                                                       @RequestBody  ScenarioRequest scenarioRequest) {
        Scenario scenarioUpdated = ScenarioService.updateScenario(scenarioId, scenarioRequest);
        return new ResponseEntity<>(scenarioUpdated, HttpStatus.OK);

    }
    @DeleteMapping("/{scenarioId}")
    public ResponseEntity deleteScenario(@PathVariable() String scenarioId) {
        ScenarioService.deleteScenario(scenarioId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/{ScenarioId}")
    public ResponseEntity<Scenario> getScenarioById(@PathVariable(value = "ScenarioId") String scenarioId){
        return new ResponseEntity<>(ScenarioService.getScenarioById(scenarioId), HttpStatus.OK);
    }

    @PutMapping("/{scenarioId}/{languageCode}")
    public ResponseEntity<Scenario> updateScenarioTestMl(@PathVariable() String scenarioId, @PathVariable() String languageCode , @RequestBody ScenarioRequest scenarioRequest) {
        Scenario scenarioMlUpdated = ScenarioService.deleteConfigurationML(scenarioId,languageCode,scenarioRequest);
        return new ResponseEntity<>(scenarioMlUpdated, HttpStatus.OK);

    }
    @PostMapping("/launch")
    public String launchTest(@RequestBody Scenario scenario) throws IOException, InterruptedException {
        return ScenarioService.launchScenario(scenario);
    }

}
