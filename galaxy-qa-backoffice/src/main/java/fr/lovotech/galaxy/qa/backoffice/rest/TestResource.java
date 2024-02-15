package fr.lovotech.galaxy.qa.backoffice.rest;

import fr.lovotech.galaxy.qa.backoffice.core.TestRequest;
import fr.lovotech.galaxy.qa.backoffice.domain.ApplicationVersion;
import fr.lovotech.galaxy.qa.backoffice.domain.DataTestCase;
import fr.lovotech.galaxy.qa.backoffice.domain.PageResponse;
import fr.lovotech.galaxy.qa.backoffice.domain.Test;
import fr.lovotech.galaxy.qa.backoffice.domain.TestVersion;
import fr.lovotech.galaxy.qa.backoffice.domain.*;
import fr.lovotech.galaxy.qa.backoffice.rest.dto.LaunchTestDTO;
import fr.lovotech.galaxy.qa.backoffice.rest.utils.HeaderUtil;
import fr.lovotech.galaxy.qa.backoffice.service.TestService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/test", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8")
public class TestResource {
    private static final String ENTITY_NAME = "gxQATest";
    @Value("${spring.application.name}")
    private String applicationName;

    private final TestService testService;

    public TestResource(TestService testService) {
        this.testService = testService;
    }

    @PostMapping("")
    public ResponseEntity<Test> createTest(@RequestBody TestRequest testRequest) throws URISyntaxException {
        Test result = testService.createTest(testRequest);
        return ResponseEntity.created(new URI("test/create" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                .body(result);
    }

    @GetMapping("")
    public ResponseEntity<PageResponse<Test>> getAllTests(@RequestParam(required = false, defaultValue = "0") int page,
                                                        @RequestParam(required = false, defaultValue = "10") int size,
                                                        @RequestParam Map<String, Object> inputs) {
        Pageable paging = PageRequest.of(page, size);
        PageResponse<Test> tests = testService.getAllTests(paging, inputs);
        return new ResponseEntity<PageResponse<Test>>(tests, HttpStatus.OK);
    }

    @PutMapping("/{testId}")
    public ResponseEntity<Test> updateTest(@PathVariable() String testId,
                                                 @RequestBody  TestRequest testRequest) {
        Test testUpdated = testService.updateTest(testId, testRequest);
        return new ResponseEntity<>(testUpdated, HttpStatus.OK);

    }
    @DeleteMapping("/{testId}")
    public ResponseEntity deleteTest(@PathVariable() String testId) {
        testService.deleteTest(testId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/{testId}")
    public ResponseEntity<Test> getTestById(@PathVariable(value = "testId") String testId){
        return new ResponseEntity<>(testService.getTestById(testId), HttpStatus.OK);
    }


    @PutMapping("/{applicationId}/{languageCode}")
    public ResponseEntity<Test> updateCampaignTestMl(@PathVariable() String applicationId, @PathVariable() String languageCode , @RequestBody TestRequest testRequest) {
        Test testMlUpdated = testService.deleteConfigurationML(applicationId,languageCode,testRequest);
        return new ResponseEntity<>(testMlUpdated, HttpStatus.OK);

    }

    @GetMapping("application/{applicationId}")
    public ResponseEntity<PageResponse<Test>> getTestByApplicationId(@PathVariable() String applicationId, @RequestParam(required = false, defaultValue = "0") int page,
                                                                                          @RequestParam(required = false, defaultValue = "10") int size) {
        Pageable paging = PageRequest.of(page, size);
        PageResponse<Test> testByApplicationId = testService.getTestByApplicationId(paging,applicationId);
        return new ResponseEntity<PageResponse<Test>>(testByApplicationId, HttpStatus.OK);
    }

    @GetMapping("{testId}/versions")
    public List<TestVersion> getTestVersionsByTestId(@PathVariable() String testId) {
        return testService.getTestVersionsByTestId(testId);
    }

    @PostMapping("/launch")
    public String launchTest(@RequestBody LaunchTestDTO launchTestDTO) throws IOException, InterruptedException {
        return testService.launchTest(launchTestDTO.getTest(), launchTestDTO.getDataTestCaseVersion());
    }
    @GetMapping("/versions/{testId}")
    public Page<ApplicationVersion> getVersionsPageByTestId(@PathVariable(value = "testId") String testId,
                                                                 @RequestParam(required = false, defaultValue = "0") int page,
                                                                 @RequestParam(required = false, defaultValue = "10") int size) {
        Pageable paging = PageRequest.of(page, size);
        Page <ApplicationVersion> applicationVersionList = testService.getPageOfListTestVersionsByTestId(testId , paging);
        return applicationVersionList;
    }

}
