package fr.lovotech.galaxy.qa.backoffice.service;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import fr.lovotech.galaxy.qa.backoffice.core.TestRequest;
import fr.lovotech.galaxy.qa.backoffice.domain.*;
import fr.lovotech.galaxy.qa.backoffice.exception.BusinessException;
import fr.lovotech.galaxy.qa.backoffice.exception.DomainErrorCodes;
import fr.lovotech.galaxy.qa.backoffice.repository.ScenarioTestRepository;
import fr.lovotech.galaxy.qa.backoffice.repository.ScenarioVersionRepository;
import fr.lovotech.galaxy.qa.backoffice.repository.TestRepository;
import fr.lovotech.galaxy.qa.backoffice.repository.TestVersionRepository;

import fr.lovotech.galaxy.qa.backoffice.service.utlis.TestHelper;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.MongoRegexCreator;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.lang.model.util.Elements;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;



@Service
public class TestService extends TestHelper {
    private final Logger log = LoggerFactory.getLogger(TestService.class);

    private final TestRepository testRepository;
    private final TestVersionRepository testVersionRepository;
    private final ScenarioTestRepository scenarioTestRepository;
    private final ScenarioVersionRepository scenarioVersionRepository;

    private final ScenarioTestService scenarioTestService;
    private final TestVersionService testVersionService;
    private final FileService fileService;
    private final TestParamService testParamService;
    private final DataTestCaseVersionService dataTestCaseVersionService;

    public TestService(TestRepository testRepository, TestVersionRepository testVersionRepository, ScenarioTestRepository scenarioTestRepository, ScenarioVersionRepository scenarioVersionRepository , ScenarioTestService scenarioTestService, TestVersionService testVersionService, FileService fileService, TestParamService testParamService,DataTestCaseVersionService dataTestCaseVersionService) {
        this.testRepository = testRepository;
        this.testVersionRepository = testVersionRepository;
        this.scenarioTestRepository = scenarioTestRepository;
        this.scenarioVersionRepository = scenarioVersionRepository;

        this.scenarioTestService = scenarioTestService;
        this.testVersionService = testVersionService;
        this.fileService = fileService;
        this.testParamService = testParamService;
        this.dataTestCaseVersionService = dataTestCaseVersionService;
    }


    @Transactional
    public Test createTest(TestRequest testRequest) {
        Test testExist = verifyIfCodeTestExist(testRequest.getCode());
        if (testExist == null) {

            Test test = testRequest.buildTest();
            test.setExternalUid(ReferenceUID.builder().build());
            test.setCreationDate(new Date());
            List<TestVersion> testVersions = testRequest.getTestVersions();
            testRepository.save(test);
            if (testVersions != null) {
                testVersions.forEach(testVersion -> {
                    testVersion.setId(null);
                    testVersion.setExternalUid(ReferenceUID.builder().build());
                    testVersion.setTest(test);
                });
                testVersionRepository.saveAll(testVersions);
            }
            return test;

        } else if (testExist.getIsActive()) {
            log.info("[INFO] Test with code=[{}] has already an existing active test with id=[{}]", testExist.getCode(),
                    testExist.getId());
            throw new BusinessException(DomainErrorCodes.TEST_CODE_EXISTS);
        } else {
            log.info("[INFO] Test with code=[{}] has already an existing deleted test with id=[{}]",
                    testExist.getCode(), testExist.getId());
            throw new BusinessException(DomainErrorCodes.TEST_CODE_DELETED);
        }
    }

    public Test verifyIfCodeTestExist(String code) {

        Criteria criteria = new Criteria();
        criteria.orOperator(Criteria.where("isActive").is(true), Criteria.where("isDeleted").is(true));
        Query query = new Query(criteria);
        query.addCriteria(Criteria.where("code").is(code));
        return testRepository.findAll(query).isEmpty() ? null : testRepository.findAll(query).get(0);
    }

    public PageResponse<Test> getAllTests(Pageable paging, Map<String, Object> inputs) {
        Query query = prepareQuerySearch(inputs);
        return new PageResponse<Test>(testRepository.findAll(query, paging));
    }

    private Query prepareQuerySearch(Map<String, Object> inputs) {
        Query query = new Query();
        query.addCriteria(Criteria.where("isActive").is(true));
        for (Map.Entry<String, Object> input : inputs.entrySet()) {
            switch (input.getKey()) {
                case "creationDate":
                    query.addCriteria(
                            Criteria.where("creationDate").regex(containsTextRegEx(input.getValue().toString()), "i"));
                    break;
                case "code":
                    query.addCriteria(
                            Criteria.where("code").regex(containsTextRegEx(input.getValue().toString()), "i"));
                    break;
                case "status":
                    query.addCriteria(
                            Criteria.where("status").regex(containsTextRegEx(input.getValue().toString()), "i"));
                    break;
                case "type":
                    query.addCriteria(
                            Criteria.where("type").regex(containsTextRegEx(input.getValue().toString()), "i"));
                    break;
                case "application":
                    query.addCriteria(Criteria.where("application.$id").is(new ObjectId(input.getValue().toString())));
                    break;
                case "comment":
                    query.addCriteria(Criteria.where("configurationMLS.comment")
                            .regex(containsTextRegEx(input.getValue().toString()), "i"));
                    break;
                case "label":
                    query.addCriteria(Criteria.where("configurationMLS.label")
                            .regex(containsTextRegEx(input.getValue().toString()), "i"));
                    break;
                case "languageCode":
                    query.addCriteria(Criteria.where("configurationMLS.languageCode")
                            .regex(containsTextRegEx(input.getValue().toString()), "i"));
                    break;
                default:
                    break;
            }
        }
        return query;
    }

    private String containsTextRegEx(String value) {
        return MongoRegexCreator.INSTANCE.toRegularExpression(value, MongoRegexCreator.MatchMode.CONTAINING);
    }

    @Transactional
    public Test updateTest(String id, TestRequest newTestRequest) {
        Test existingTestWithSameCode = verifyIfTestWithSameCodeExist(newTestRequest.getCode());
        Test oldTest = getTestById(id);
        List<TestVersion> oldTestVersions = getTestVersionsByTestId(id);
        List<TestVersion> requiredTestVersions = new ArrayList<>();
        Test newTest = newTestRequest.buildTest();
        if (existingTestWithSameCode == null || oldTest.getCode().equals(newTestRequest.getCode())) {
            oldTestVersions.forEach(testVersion -> {
                testVersion.setEndDate(new Date());
                testVersion.setIsActive(false);
                if (checkIfTestExistScenarioWithSameVersion(oldTest.getId(), testVersion.getApplicationVersion().getId())) {
                    requiredTestVersions.add(testVersion);
                }
            });
            oldTest.setId(null);
            oldTest.setEndDate(new Date());
            oldTest.setIsActive(false);
            testRepository.save(oldTest);
            newTest.setId(id);
            newTest.setExternalUid(ReferenceUID.builder().build());
            newTest.setCreationDate(new Date());
            List<TestVersion> newtestVersions = newTestRequest.getTestVersions();
            if (!requiredTestVersions.isEmpty() && !requiredTestVersions.stream()
                    .anyMatch(element -> newtestVersions.stream().filter(
                            p -> p.getApplicationVersion().getCode().equals(element.getApplicationVersion().getCode()))
                            .count() > 0)) {
                String result = requiredTestVersions.stream().map(n -> n.getApplicationVersion().getCode())
                        .collect(Collectors.joining(",", "[", "]"));
                throw new BusinessException("Cannot update version with dependencies :" + result);
            }
            testRepository.save(newTest);
            if (newtestVersions != null) {
                newtestVersions.forEach(testVersion -> {
                    testVersion.setId(null);
                    testVersion.setExternalUid(ReferenceUID.builder().build());
                    testVersion.setTest(newTest);
                });
                testVersionRepository.saveAll(oldTestVersions);
                testVersionRepository.saveAll(newtestVersions);
            }
            return newTest;
        } else if (existingTestWithSameCode.getIsActive()) {
            log.info("[INFO] Test with code=[{}] has already an existing active test with id=[{}]", existingTestWithSameCode.getCode(),
                    existingTestWithSameCode.getId());
            throw new BusinessException(DomainErrorCodes.TEST_CODE_EXISTS);
        } else {
            log.info("[INFO] Test with code=[{}] has already an existing deleted test with id=[{}]",
                    existingTestWithSameCode.getCode(), existingTestWithSameCode.getId());
            throw new BusinessException(DomainErrorCodes.TEST_CODE_DELETED);
        }

    }

    public Test saveOrUpdate(Test test) {
        test = testRepository.save(test);
        return test;
    }

    public Test getTestById(String id) {
        Optional<Test> test = testRepository.findById(id);
        if (test.isEmpty()) {
            log.error("[ERROR] Test with id {} not found: ", id);
            throw new BusinessException(DomainErrorCodes.TEST_NOT_FOUND);
        }
        return test.get();
    }

    public void deleteTest(String id) {
        Test test = getTestById(id);
        List<ScenarioTest> scenarioTestList = scenarioTestService.getScenarioTestByTestId(id);
        List<TestVersion> testVersionList = testVersionService.getTestVersionByTestId(id);
        if (scenarioTestList.isEmpty()) {
            test.setEndDate(new Date());
            test.setIsActive(false);
            test.setIsDeleted(true);
            testRepository.save(test);
            testVersionList.forEach(testVersion -> {
                testVersion.setEndDate(new Date());
                testVersion.setIsActive(false);
                testVersion.setIsDeleted(true);
            });
            testVersionRepository.saveAll(testVersionList);
        } else {
            throw new BusinessException(DomainErrorCodes.TEST_OWNED_BY_SCENARIO);
        }
    }

    public Test deleteConfigurationML(String id, String codeLanguage, TestRequest testRequest) {
        Test test = getTestById(id);
        testRequest.setConfigurationMLS(test.getConfigurationMLS().stream()
                .filter(config -> !config.getLanguageCode().equals(codeLanguage)).collect(Collectors.toList()));
        test.setIsActive(false);
        test.setEndDate(new Date());
        saveOrUpdate(test);
        return createTest(testRequest);
    }

    public PageResponse<Test> getTestByApplicationId(Pageable paging, String id) {
        Criteria criteria = new Criteria();
        Query query = new Query(criteria);
        query.addCriteria(Criteria.where("application.$id").is(new ObjectId(id)));
        return new PageResponse<Test>(testRepository.findAll(query, paging));
    }

    public List<TestVersion> getTestVersionsByTestId(String testId) {

        Criteria criteria = new Criteria();
        criteria.orOperator(Criteria.where("isActive").is(true), Criteria.where("isDeleted").is(true));
        Query query = new Query(criteria);
        query.addCriteria(Criteria.where("test.$id").is(new ObjectId(testId)));
        List<TestVersion> testVersions = testVersionRepository.findAll(query);
        return testVersions;
    }
    public Page<ApplicationVersion> getPageOfListTestVersionsByTestId(String testId ,Pageable paging ){
        List<ApplicationVersion> applicationVersionList = new ArrayList<>();
        List<TestVersion> testVersionList = getTestVersionsByTestId(testId);
        testVersionList.forEach(testVersion -> {
            applicationVersionList.add(testVersion.getApplicationVersion());
        });
        Page<ApplicationVersion> page = new PageImpl(applicationVersionList);
        return page ;
    }
    public boolean checkIfTestExistScenarioWithSameVersion(String testId, String versionId) {

        Criteria criteria1 = new Criteria();
        // check deleted and active ?
        Query query1 = new Query(criteria1);
        query1.addCriteria(Criteria.where("test.$id").is(new ObjectId(testId)));
        List<ScenarioTest> scenarioTests = scenarioTestRepository.findAll(query1);
        int testNbExist = 0;
        for (ScenarioTest scenarioTest : scenarioTests) {
            Criteria criteria2 = new Criteria();
            // check deleted and active ?
            Query query2 = new Query(criteria2);
            query2.addCriteria(Criteria.where("scenario.$id").is(new ObjectId(scenarioTest.getScenario().getId())));
            query2.addCriteria(Criteria.where("applicationVersion.$id").is(new ObjectId(versionId)));

            List<ScenarioVersion> scenarioVersions = scenarioVersionRepository.findAll(query2);
            testNbExist += scenarioVersions.size();
        }
        return testNbExist > 0;
    }

    private Test verifyIfTestWithSameCodeExist(String code) {
        Criteria criteria = new Criteria();
        criteria.orOperator(Criteria.where("isActive").is(true), Criteria.where("isDeleted").is(true));
        Query query = new Query(criteria);
        query.addCriteria(Criteria.where("code").regex(exactText(code),"i"));
        return testRepository.findAll(query).isEmpty() ? null : testRepository.findAll(query).get(0);
    }

    private String exactText(String value) {
        return MongoRegexCreator.INSTANCE.toRegularExpression(value, MongoRegexCreator.MatchMode.EXACT);
    }

    public String launchTest(Test test, DataTestCaseVersion dataTestCaseVersion) throws IOException, InterruptedException {

        //get testParams
        List<TestParam> testParams = testParamService.getTestParamsByDataTestCaseId(dataTestCaseVersion.getDataTestCase().getId());
        Map<String,String> mapTestParams = new HashMap<>();
        testParams.forEach(param->{
            mapTestParams.put(param.getLabel(), param.getValue());
        });
        // get file test from dataTestCase
        InputStream inputStream = fileService.readFile(dataTestCaseVersion.getFileInfo().getId());
        String seleniumScriptCode = new String(inputStream.readAllBytes());
        log.info(seleniumScriptCode);
        String[] lines = seleniumScriptCode.split(";");


        setupThread("chrome");
        File report = new File(".//src/rapport.html");

        ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(".//src/rapport.html");
        ExtentReports extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
        ExtentTest test1 = extent.createTest("CRA login", "this is a test to validate the login");

        FileInputStream input = new FileInputStream(report);
        MultipartFile file = new MockMultipartFile("file", "rapport.html", "text/html",
                IOUtils.toByteArray(input));
        FileInfo fileInfo = dataTestCaseVersionService.getFilesInfos(file,file.getName(), "text/", "lovotech","","","Key");
        test.getTestResult().setReport(fileInfo);
        testRepository.save(test);

        File rapport = new File("/src/rapport.html");



        WebDriver webdriver = getDriver();
       test1.log(Status.INFO, "Starting test Case");
        webdriver.get(dataTestCaseVersion.getApplicationVersion().getUrl());
        webdriver.manage().window().maximize();
        System.out.println("Started session");
       // String currentUrl = webdriver.getCurrentUrl();
       test.getTestResult().getTestReportDetails().setIsPassed(true);
        test.getTestResult().setTestDateTime(LocalDateTime.now());
        Arrays.stream(lines).forEach(s -> {
            try {
                if (s.contains("findElement")) {
                    String inputParam = s.substring(s.indexOf('"', s.indexOf("(") + 1) +1, s.indexOf('"'+")"));
                    
                    if (s.contains("By.id")) {
                        if (s.contains("sendKeys")) {
                            String valueParam = mapTestParams.get(inputParam);
                            if (!s.contains(".click")) {
                                try {
                                    webdriver.findElement(By.id(inputParam)).sendKeys(valueParam);
                                    test.getTestResult().getScreenshots().add(this.takeSnapShot(webdriver));
                                } catch (Exception e) {
                                    test.getTestResult().getTestReportDetails().setIsPassed(false);
                                    test.getTestResult().getScreenshots().add(this.takeSnapShot(webdriver));
                                }
                            }
                        } else {
                            try {
                                webdriver.findElement(By.id(inputParam)).click();
                                dataTestCaseVersion.getDataTestCase().getTest().getTestResult().getScreenshots().add(this.takeSnapShot(webdriver));
                                this.testRepository.save(dataTestCaseVersion.getDataTestCase().getTest());
                                Thread.sleep(2000);
                            } catch (Exception e1) {
                                test.getTestResult().getTestReportDetails().setIsPassed(false);
                            }

                        }
                    } else if (s.contains("By.cssSelector")) {
                        if (s.contains("sendKeys")) {
                            String valueParam = mapTestParams.get(inputParam);
                            if (!s.contains(".click")) {
                                try {
                                    webdriver.findElement(By.cssSelector(inputParam)).sendKeys(valueParam);
                                    test.getTestResult().getScreenshots().add(this.takeSnapShot(webdriver));
                                } catch (Exception e) {
                                    test.getTestResult().getTestReportDetails().setIsPassed(false);
                                    test.getTestResult().getScreenshots().add(this.takeSnapShot(webdriver));
                                }
                               
                            }
                        } else {
                            try{
                            webdriver.findElement(By.cssSelector(inputParam)).click();
                            test.getTestResult().getScreenshots().add(this.takeSnapShot(webdriver));
                            Thread.sleep(2000);
                            }
                            catch(Exception e2){
                                test.getTestResult().getTestReportDetails().setIsPassed(false);
                                test.getTestResult().getScreenshots().add(this.takeSnapShot(webdriver));
                            }
                        }
                    } else if (s.contains("By.xpath")) {
                        if (s.contains("sendKeys")) {
                            String valueParam = mapTestParams.get(inputParam);
                            if (!s.contains(".click")) {
                                try {
                                    webdriver.findElement(By.xpath(inputParam)).sendKeys(valueParam);
                                    test.getTestResult().getScreenshots().add(this.takeSnapShot(webdriver));
                            
                                } catch (Exception e) {
                                    test.getTestResult().getTestReportDetails().setIsPassed(false);
                                    test.getTestResult().getScreenshots().add(this.takeSnapShot(webdriver));
                                }

                            }
                        } else {
                            try {
                                webdriver.findElement(By.xpath(inputParam)).click();
                            Thread.sleep(2000);
                            test.getTestResult().getScreenshots().add(this.takeSnapShot(webdriver));
                            } catch (Exception e1) {
                                test.getTestResult().getTestReportDetails().setIsPassed(false);
                                test.getTestResult().getScreenshots().add(this.takeSnapShot(webdriver));
                            }
            
                        }
                    } else if (s.contains("By.className")) {
                        if (s.contains("sendKeys")) {
                            String valueParam = mapTestParams.get(inputParam);
                            if (!s.contains(".click")) {
                                try {
                                    webdriver.findElement(By.className(inputParam)).sendKeys(valueParam);
                                    test.getTestResult().getScreenshots().add(this.takeSnapShot(webdriver));
                                } catch (Exception e) {
                                    test.getTestResult().getTestReportDetails().setIsPassed(false);
                                    test.getTestResult().getScreenshots().add(this.takeSnapShot(webdriver));
                                }
                            }
                        } else {
                            try {
                                webdriver.findElement(By.className(inputParam)).click();
                                Thread.sleep(2000);
                                test.getTestResult().getScreenshots().add(this.takeSnapShot(webdriver));
                            } catch (Exception e) {
                                test.getTestResult().getTestReportDetails().setIsPassed(false);
                                test.getTestResult().getScreenshots().add(this.takeSnapShot(webdriver));
                            }
                        }
                    }else if (s.contains("By.linkText")) {
                        if (s.contains("sendKeys")) {
                            String valueParam = mapTestParams.get(inputParam);
                            if (!s.contains(".click")) {
                                try {
                                    webdriver.findElement(By.linkText(inputParam)).sendKeys(valueParam);
                                    test.getTestResult().getScreenshots().add(this.takeSnapShot(webdriver));
                                } catch (Exception e) {
                                    test.getTestResult().getTestReportDetails().setIsPassed(false);
                                    test.getTestResult().getScreenshots().add(this.takeSnapShot(webdriver));
                                }
                            }
                        } else {
                            try {
                                webdriver.findElement(By.linkText(inputParam)).click();
                                test.getTestResult().getScreenshots().add(this.takeSnapShot(webdriver));
                                Thread.sleep(2000);
                            } catch (Exception e) {
                                test.getTestResult().getTestReportDetails().setIsPassed(false);
                                test.getTestResult().getScreenshots().add(this.takeSnapShot(webdriver));
                            }

                        }
                    }else if (s.contains("By.name")) {
                        if (s.contains("sendKeys")) {
                            String valueParam = mapTestParams.get(inputParam);
                            if (!s.contains(".click")) {
                                try {
                                    webdriver.findElement(By.name(inputParam)).sendKeys(valueParam);
                                test.getTestResult().getScreenshots().add(this.takeSnapShot(webdriver));
                                } catch (Exception e) {
                                    test.getTestResult().getTestReportDetails().setIsPassed(false);
                                test.getTestResult().getScreenshots().add(this.takeSnapShot(webdriver));
                                }
                                
                            }
                        } else {
                            try {
                                webdriver.findElement(By.name(inputParam)).click();
                                Thread.sleep(2000);
                                test.getTestResult().getScreenshots().add(this.takeSnapShot(webdriver));
                            } catch (Exception e) {
                                test.getTestResult().getTestReportDetails().setIsPassed(false);
                                test.getTestResult().getScreenshots().add(this.takeSnapShot(webdriver));
                            }
                           

                        }
                    }else if (s.contains("By.partialLinkText")) {
                        if (s.contains("sendKeys")) {
                            String valueParam = mapTestParams.get(inputParam);
                            if (!s.contains(".click")) {
                                try {
                                    webdriver.findElement(By.partialLinkText(inputParam)).sendKeys(valueParam);
                                    test.getTestResult().getScreenshots().add(this.takeSnapShot(webdriver));
                                } catch (Exception e) {
                                    test.getTestResult().getTestReportDetails().setIsPassed(false);
                                    test.getTestResult().getScreenshots().add(this.takeSnapShot(webdriver));
                                }
                    
                            }
                        } else {
                            try {
                                webdriver.findElement(By.partialLinkText(inputParam)).click();
                                Thread.sleep(2000);
                                test.getTestResult().getScreenshots().add(this.takeSnapShot(webdriver));
                            } catch (Exception e) {
                                test.getTestResult().getTestReportDetails().setIsPassed(false);
                                test.getTestResult().getScreenshots().add(this.takeSnapShot(webdriver));
                            }


                        }
                    }else if (s.contains("By.tagName")) {
                        if (s.contains("sendKeys")) {
                            String valueParam = mapTestParams.get(inputParam);
                            if (!s.contains(".click")) {
                                try {
                                    webdriver.findElement(By.tagName(inputParam)).sendKeys(valueParam);
                                    test.getTestResult().getScreenshots().add(this.takeSnapShot(webdriver));
                                } catch (Exception e) {
                                    test.getTestResult().getTestReportDetails().setIsPassed(false);
                                    test.getTestResult().getScreenshots().add(this.takeSnapShot(webdriver));
                                }
                            }
                        } else {
                            try {
                            webdriver.findElement(By.tagName(inputParam)).click();
                            Thread.sleep(2000);
                            test.getTestResult().getScreenshots().add(this.takeSnapShot(webdriver)); 
                            } catch (Exception e) {
                                test.getTestResult().getTestReportDetails().setIsPassed(false);
                                test.getTestResult().getScreenshots().add(this.takeSnapShot(webdriver));
                            }
                            
                        }
                    }
                }
             } catch (Exception e) {
                System.out.print(e.getMessage());
            }
        }
        );
        Thread.sleep(5000);
        System.out.println("test ok");
        extent.flush();
        webdriver.close();
        if (getDriver() != null) {
            tearDownDriver();
        }
        Document doc = null ;
        try {
            doc = Jsoup.parse(new File("./src/rapport.html"), "ISO-8859-1");
        } catch (IOException e) {
            e.printStackTrace();
        } // right
        Element element  = doc.getElementsByClass("test-time-info").first();
        Element startTimeElement = element.getElementsByClass("label start-time").first();
        String startTime = startTimeElement.childNode(0).toString();
        Element endTimeElement = element.getElementsByClass("label end-time").first();
        String endTime = endTimeElement.childNode(0).toString();
        Element timeTakenElement = element.getElementsByClass("label time-taken grey lighten-1 white-text").first();
        String timeTaken = timeTakenElement.childNode(0).toString();
        test.getTestResult().getTestReportDetails().setEndTime(endTime);
        test.getTestResult().getTestReportDetails().setStartTime(startTime);
        test.getTestResult().getTestReportDetails().setTestTimeTaken(timeTaken);
        testRepository.save(test);
        return "test done! ";
        
    }

        public  FileInfo takeSnapShot(WebDriver webdriver) throws Exception {

        //Convert web driver object to TakeScreenshot
        TakesScreenshot scrShot = ((TakesScreenshot) webdriver);

        //Call getScreenshotAs method to create image file
        File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);

        //Move image file to new destination
        File dest= new File("./src/screenshots/"+timestamp()+".png");
        FileUtils.copyFile(SrcFile, dest);
        FileInputStream input = new FileInputStream(dest);
        MultipartFile file = new MockMultipartFile("file", timestamp()+".png", "image/png",
                IOUtils.toByteArray(input));
        //Copy file at destination
        FileUtils.copyFile(SrcFile, dest);
        FileInfo fileInfo = dataTestCaseVersionService.getFilesInfos(file,file.getName(), "image/", "lovotech","","","Key");
        return fileInfo;
    }

    public static String timestamp() {
        return new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date());
    }

}
