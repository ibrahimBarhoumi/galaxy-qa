package fr.lovotech.galaxy.qa.backoffice.rest;
import fr.lovotech.galaxy.qa.backoffice.core.CampaignTestRequest;
import fr.lovotech.galaxy.qa.backoffice.domain.CampaignTest;
import fr.lovotech.galaxy.qa.backoffice.domain.PageResponse;
import fr.lovotech.galaxy.qa.backoffice.rest.utils.HeaderUtil;
import fr.lovotech.galaxy.qa.backoffice.service.CampaignTestService;
import fr.lovotech.galaxy.qa.backoffice.service.ScenarioService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/campaigntest")
public class CampaignTestResource {
    private static final String ENTITY_NAME = "gxQACampaignTest";
    @Value("${spring.application.name}")
    private String applicationName;

    private final CampaignTestService campaigntestService;
    private final ScenarioService scenarioService;

    public CampaignTestResource(CampaignTestService campaigntestService, ScenarioService scenarioService) {
        this.campaigntestService = campaigntestService;
        this.scenarioService = scenarioService;
    }

    @PostMapping("")
    public ResponseEntity<CampaignTest> createCampaignTest(@RequestBody CampaignTestRequest campaigntestRequest) throws URISyntaxException, IOException {
        CampaignTest result = campaigntestService.createCampaignTest(campaigntestRequest);
        return ResponseEntity.created(new URI("campaigntest/create" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                .body(result);
    }

    @GetMapping("")
    public ResponseEntity<PageResponse<CampaignTest>> getAllCampaignTests(@RequestParam(required = false, defaultValue = "0") int page,
                                                            @RequestParam(required = false, defaultValue = "10") int size,
                                                            @RequestParam Map<String, Object> inputs) {
                                                                Pageable paging = PageRequest.of(page, size);
        PageResponse<CampaignTest> campaigntests = campaigntestService.getAllCampaignTests(paging, inputs);
        return new ResponseEntity<PageResponse<CampaignTest>>(campaigntests, HttpStatus.OK);
    }

    @PutMapping("/{campaigntestId}")
    public ResponseEntity<CampaignTest> updateCampaignTest(@PathVariable() String campaigntestId,
                                                     @RequestBody CampaignTestRequest campaigntestRequest) {
        CampaignTest campaigntestUpdated = campaigntestService.updateCampaignTest(campaigntestId, campaigntestRequest);
        return new ResponseEntity<>(campaigntestUpdated, HttpStatus.OK);

    }

    @DeleteMapping("/{campaigntestId}")
    public ResponseEntity deleteCampaignTest(@PathVariable() String campaigntestId) {
        campaigntestService.deleteCampaignTest(campaigntestId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/{campaigntestId}")
    public ResponseEntity<CampaignTest> getCampaignTestById(@PathVariable(value = "campaigntestId") String campaigntestId) {
        return new ResponseEntity<>(campaigntestService.getCampaignTestById(campaigntestId), HttpStatus.OK);
    }

    @PutMapping("/{campaigntestId}/{languageCode}")
    public ResponseEntity<CampaignTest> updateCampaignTestMl(@PathVariable() String campaigntestId,@PathVariable() String languageCode , @RequestBody CampaignTestRequest campaigntestRequest) {
        CampaignTest campaigntestMlUpdated = campaigntestService.deleteConfigurationML(campaigntestId,languageCode,campaigntestRequest);
        return new ResponseEntity<>(campaigntestMlUpdated, HttpStatus.OK);

    }
    @GetMapping("/scenario/{scenarioId}")
    public List<CampaignTest> getCampaignTestByScenarioId(@PathVariable(value = "scenarioId") String scenarioId) {
        return campaigntestService.findByScenarioId(scenarioId);
    }
    @GetMapping("/lunch/{campaignId}")
    public String lunchCampaign(@PathVariable(value = "campaignId") String campaignId){
        return scenarioService.lunchCampaign(campaignId);
    }
}
