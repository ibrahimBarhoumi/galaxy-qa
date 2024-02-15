package fr.lovotech.galaxy.qa.backoffice.rest;

import fr.lovotech.galaxy.qa.backoffice.domain.PageResponse;
import fr.lovotech.galaxy.qa.backoffice.domain.ScenarioTest;
import fr.lovotech.galaxy.qa.backoffice.domain.Test;
import fr.lovotech.galaxy.qa.backoffice.domain.TestVersion;
import fr.lovotech.galaxy.qa.backoffice.service.TestVersionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/testVersion", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8")
public class TestVersionResource {
    private static final String ENTITY_NAME = "gxQATestVersion";
    @Value("${spring.application.name}")
    private String applicationName;

    private final TestVersionService testVersionService ;

    public TestVersionResource(TestVersionService testVersionService) {
        this.testVersionService = testVersionService;
    }
    @PostMapping("/version")
    public ResponseEntity<Page<Test>> getVersionTestByVersionId(@RequestBody List<String> versionId,
                                                @RequestParam(required = false, defaultValue = "0") int page,
                                                @RequestParam(required = false, defaultValue = "10") int size) {
        Pageable paging = PageRequest.of(page, size);
        Page<Test> versionTestPageResponse = testVersionService.getTestVersionByVersionId(versionId, paging);
        return new ResponseEntity<Page<Test>>(versionTestPageResponse, HttpStatus.OK);
    }
}
