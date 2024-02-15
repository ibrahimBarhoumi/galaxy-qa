package fr.lovotech.galaxy.qa.backoffice.rest;

import fr.lovotech.galaxy.qa.backoffice.core.EnvironmentRequest;
import fr.lovotech.galaxy.qa.backoffice.domain.Environment;
import fr.lovotech.galaxy.qa.backoffice.rest.utils.HeaderUtil;
import fr.lovotech.galaxy.qa.backoffice.service.EnvironmentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/environment")
public class EnvironmentResource {
    private static final String ENTITY_NAME = "gxQAEnvironment";
    @Value("${spring.application.name}")
    private String applicationName;

    private final EnvironmentService environmentService;

    public EnvironmentResource(EnvironmentService environmentService) {
        this.environmentService = environmentService;
    }

    @PostMapping("")
    public ResponseEntity<Environment> createEnvironment(@RequestBody EnvironmentRequest environmentRequest) throws URISyntaxException, IOException {
        Environment result = environmentService.createEnvironment(environmentRequest);
        return ResponseEntity.created(new URI("environment/create" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                .body(result);
    }

    @GetMapping("")
    public ResponseEntity<Page<Environment>> getAllEnvironments(@RequestParam(required = false, defaultValue = "0") int page,
                                                            @RequestParam(required = false, defaultValue = "10") int size,
                                                            @RequestParam Map<String, Object> inputs) {
        Page<Environment> environments = environmentService.getAllEnvironments(page, size, inputs);
        return new ResponseEntity<Page<Environment>>(environments, HttpStatus.OK);
    }

    @PutMapping("/{environmentId}")
    public ResponseEntity<Environment> updateEnvironment(@PathVariable() String environmentId,
                                                     @RequestBody EnvironmentRequest environmentRequest) {
        Environment environmentUpdated = environmentService.updateEnvironment(environmentId, environmentRequest);
        return new ResponseEntity<>(environmentUpdated, HttpStatus.OK);

    }

    @DeleteMapping("/{environmentId}")
    public ResponseEntity deleteEnvironment(@PathVariable() String environmentId) {
        environmentService.deleteEnvironment(environmentId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/{environmentId}")
    public ResponseEntity<Environment> getEnvironmentById(@PathVariable(value = "environmentId") String environmentId) {
        return new ResponseEntity<>(environmentService.getEnvironmentById(environmentId), HttpStatus.OK);
    }
}
