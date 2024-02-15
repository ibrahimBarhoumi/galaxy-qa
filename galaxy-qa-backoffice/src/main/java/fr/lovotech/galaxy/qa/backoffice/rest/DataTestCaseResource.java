package fr.lovotech.galaxy.qa.backoffice.rest;

import fr.lovotech.galaxy.qa.backoffice.core.DataTestCaseRequest;
import fr.lovotech.galaxy.qa.backoffice.core.ScenarioRequest;
import fr.lovotech.galaxy.qa.backoffice.domain.*;
import fr.lovotech.galaxy.qa.backoffice.rest.utils.HeaderUtil;
import fr.lovotech.galaxy.qa.backoffice.service.DataTestCaseService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/datatestcase", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8")
public class DataTestCaseResource {
    private static final String ENTITY_NAME = "gxQADataTestCase";
    @Value("${spring.application.name}")
    private String applicationName;
    private final DataTestCaseService dataTestCaseService;

    public DataTestCaseResource(DataTestCaseService dataTestCaseService) {
        this.dataTestCaseService = dataTestCaseService;
    }

    @PostMapping("")
    public ResponseEntity<DataTestCase> createDataTestCase(@RequestBody DataTestCaseRequest dataTestCaseRequest) throws URISyntaxException {
        DataTestCase result = dataTestCaseService.createDataTestCase(dataTestCaseRequest);
        return ResponseEntity.created(new URI("DataTestCase/create" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                .body(result);
    }

    @GetMapping("")
    public ResponseEntity<PageResponse<DataTestCase>> getAllDataTestCases(@RequestParam(required = false, defaultValue = "0") int page,
                                                                          @RequestParam(required = false, defaultValue = "10") int size,
                                                                          @RequestParam Map<String, Object> inputs) {
        Pageable paging = PageRequest.of(page, size);
        PageResponse<DataTestCase> dataTestCases = dataTestCaseService.getAllDtatTtestCases(paging, inputs);
        return new ResponseEntity<PageResponse<DataTestCase>>(dataTestCases, HttpStatus.OK);
    }

    @PutMapping("{dataTestCaseId}")
    public ResponseEntity<DataTestCase> setDefault(@PathVariable() String dataTestCaseId) {
        this.dataTestCaseService.setDefault(dataTestCaseId);
        return new ResponseEntity<>(this.dataTestCaseService.getById(dataTestCaseId),HttpStatus.OK);
    }

    @GetMapping("code/{code}")
    public ResponseEntity<DataTestCase> getByCode(@PathVariable() String code) {
        return new ResponseEntity<>(this.dataTestCaseService.getByCode(code),HttpStatus.OK);
    }

    @PutMapping("/update/{dataTestCaseId}")
    public ResponseEntity<DataTestCase> updateDataTestCase(@PathVariable() String dataTestCaseId,
                                                           @RequestBody DataTestCaseRequest dataTestCaseRequest) {
        DataTestCase updateDataTestCase = dataTestCaseService.updateDataTestCase(dataTestCaseId,dataTestCaseRequest);
        return new ResponseEntity<>(updateDataTestCase,HttpStatus.OK);
    }
    @GetMapping("/testParam/{dataTestCaseId}")
    public List<TestParam> getTestParmByDataTestCaseId(@PathVariable() String dataTestCaseId){
        return dataTestCaseService.getTestParmsByDataTestCase(dataTestCaseId);
    }
}
