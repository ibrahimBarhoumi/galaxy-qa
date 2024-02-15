package fr.lovotech.galaxy.qa.backoffice.rest;

import fr.lovotech.galaxy.qa.backoffice.core.TestCaseRequest;
import fr.lovotech.galaxy.qa.backoffice.domain.TestCase;
import fr.lovotech.galaxy.qa.backoffice.rest.utils.HeaderUtil;
import fr.lovotech.galaxy.qa.backoffice.service.TestCaseService;
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
@RequestMapping(value = "/api/testcase", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8")
public class TestCaseResource {
    private static final String ENTITY_NAME = "gxQATestCase";
    @Value("${spring.application.name}")
    private String applicationName;

    private final TestCaseService testCaseService;

    public TestCaseResource(TestCaseService testCaseService) {
        this.testCaseService = testCaseService;
    }

    @PostMapping("")
    public ResponseEntity<TestCase> createTestCase(@RequestBody TestCaseRequest testCaseRequest) throws URISyntaxException {
        TestCase result = testCaseService.createTestCase(testCaseRequest);
        return ResponseEntity.created(new URI("testcase/create" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                .body(result);
    }

    @GetMapping("")
    public ResponseEntity<Page<TestCase>> getAllTestCases(@RequestParam(required = false, defaultValue = "0") int page,
                                                            @RequestParam(required = false, defaultValue = "10") int size,
                                                            @RequestParam Map<String, Object> inputs) {
        Page<TestCase> testCases = testCaseService.getAllTestCases(page, size, inputs);
        return new ResponseEntity<Page<TestCase>>(testCases, HttpStatus.OK);
    }

    @PutMapping("/{testcaseId}")
    public ResponseEntity<TestCase> updateTestCase(@PathVariable() String testcaseId,
                                                     @RequestBody  TestCaseRequest testCaseRequest) {
        TestCase testCaseUpdated = testCaseService.updateTestCase(testcaseId, testCaseRequest);
        return new ResponseEntity<>(testCaseUpdated, HttpStatus.OK);

    }
    @DeleteMapping("/{testcaseId}")
    public ResponseEntity deleteTestCase(@PathVariable() String testcaseId) {
        testCaseService.deleteTestCase(testcaseId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/{testcaseId}")
    public ResponseEntity<TestCase> getTestCaseById(@PathVariable(value = "testcaseId") String testCaseId){
        return new ResponseEntity<>(testCaseService.getTestCaseById(testCaseId), HttpStatus.OK);
    }
}
