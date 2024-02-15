package fr.lovotech.galaxy.qa.backoffice.rest;

import fr.lovotech.galaxy.qa.backoffice.core.UITestRequest;
import fr.lovotech.galaxy.qa.backoffice.domain.UITest;
import fr.lovotech.galaxy.qa.backoffice.rest.utils.HeaderUtil;
import fr.lovotech.galaxy.qa.backoffice.service.UITestService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/UITest", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8")
public class UITestResource {
    private static final String ENTITY_NAME = "gxQAUITest";
    @Value("${spring.application.name}")
    private String applicationName;

    private final UITestService uiTestService;

    public UITestResource(UITestService uiTestService) {
        this.uiTestService = uiTestService;
    }

    @PostMapping("")
    public ResponseEntity<UITest> createUITest(@RequestBody UITestRequest uITestRequest) throws URISyntaxException {
        UITest result = uiTestService.createUITest(uITestRequest);
        return ResponseEntity.created(new URI("UITest/create" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                .body(result);
    }

    @GetMapping("")
    public ResponseEntity<Page<UITest>> getAllUITests(@RequestParam(required = false, defaultValue = "0") int page,
                                                            @RequestParam(required = false, defaultValue = "10") int size,
                                                            @RequestParam Map<String, Object> inputs) {
        Page<UITest> uITests = uiTestService.getAllUITests(page, size, inputs);
        return new ResponseEntity<Page<UITest>>(uITests, HttpStatus.OK);
    }

    @PutMapping("/{uITestId}")
    public ResponseEntity<UITest> updateUITest(@PathVariable() String uITestId,
                                                     @RequestBody  UITestRequest uITestRequest) {
        UITest uITestUpdated = uiTestService.updateUITest(uITestId, uITestRequest);
        return new ResponseEntity<>(uITestUpdated, HttpStatus.OK);

    }
    @DeleteMapping("/{uITestId}")
    public ResponseEntity deleteUITest(@PathVariable() String uITestId) {
        uiTestService.deleteUITest(uITestId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/{UITestId}")
    public ResponseEntity<UITest> getUITestById(@PathVariable(value = "uITestId") String uITestId){
        return new ResponseEntity<>(uiTestService.getUITestById(uITestId), HttpStatus.OK);
    }
}
