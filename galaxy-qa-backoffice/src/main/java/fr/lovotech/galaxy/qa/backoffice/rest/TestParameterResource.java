package fr.lovotech.galaxy.qa.backoffice.rest;

import java.util.List;
import java.util.Map;

import org.apache.regexp.recompile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import feign.Body;
import fr.lovotech.galaxy.qa.backoffice.core.TestParameterRequest;
import fr.lovotech.galaxy.qa.backoffice.domain.PageResponse;
import fr.lovotech.galaxy.qa.backoffice.domain.TestParameter;
import fr.lovotech.galaxy.qa.backoffice.domain.TestParameterDetail;
import fr.lovotech.galaxy.qa.backoffice.service.TestParameterService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/testParameter")
public class TestParameterResource {

    private TestParameterService testParameterService; 

    public TestParameterResource (TestParameterService testParameterService){
        this.testParameterService = testParameterService;
    }

    @PostMapping
    public ResponseEntity<TestParameter> createTestParameter(@RequestBody TestParameterRequest testParameterRequest){
        return new ResponseEntity<TestParameter>(testParameterService.createTestParameter(testParameterRequest),HttpStatus.OK);
    }
    @GetMapping("")
    public ResponseEntity<PageResponse<TestParameter>> getAllTestParameter(@RequestParam(required = false, defaultValue = "0") int page,
                                                                          @RequestParam(required = false, defaultValue = "10") int size,
                                                                          @RequestParam Map<String, Object> inputs) {
    Pageable paging = PageRequest.of(page, size);

        PageResponse<TestParameter> testParameter = testParameterService.getAllTestParameter(paging, inputs);
        return new ResponseEntity<PageResponse<TestParameter>>(testParameter, HttpStatus.OK);
    }
    @GetMapping("/{testId}/{applicationVersionId}")
    public ResponseEntity<List<TestParameterDetail>> getTestParameterDetailsByTestIdAndVersionId(@PathVariable(value = "testId") String testId, @PathVariable(value = "applicationVersionId") String applicationVersionId){
        return new ResponseEntity<>(testParameterService.getTestParameterByTestIdAndApplicationVersionId(testId, applicationVersionId),HttpStatus.OK);
    }
}
