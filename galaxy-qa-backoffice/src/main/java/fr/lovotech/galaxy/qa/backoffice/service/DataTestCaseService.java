package fr.lovotech.galaxy.qa.backoffice.service;

import fr.lovotech.galaxy.qa.backoffice.core.DataTestCaseRequest;
import fr.lovotech.galaxy.qa.backoffice.core.TestRequest;
import fr.lovotech.galaxy.qa.backoffice.domain.*;
import fr.lovotech.galaxy.qa.backoffice.domain.DataTestCase;
import fr.lovotech.galaxy.qa.backoffice.exception.BusinessException;
import fr.lovotech.galaxy.qa.backoffice.exception.DomainErrorCodes;
import fr.lovotech.galaxy.qa.backoffice.exception.TechnicalException;
import fr.lovotech.galaxy.qa.backoffice.repository.DataTestCaseRepository;
import fr.lovotech.galaxy.qa.backoffice.repository.DataTestCaseVersionRepository;
import fr.lovotech.galaxy.qa.backoffice.repository.TestParamRepository;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.MongoRegexCreator;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class DataTestCaseService {

    private final Logger log = LoggerFactory.getLogger(DataTestCaseVersion.class);
    private final DataTestCaseRepository dataTestCaseRepository;
    private final DataTestCaseVersionRepository dataTestCaseVersionRepository;
    private final TestParamRepository testParamRepository;

    public DataTestCaseService(DataTestCaseRepository dataTestCaseRepository, DataTestCaseVersionRepository dataTestCaseVersionRepository, TestParamRepository testParamRepository) {
        this.dataTestCaseRepository = dataTestCaseRepository;
        this.dataTestCaseVersionRepository = dataTestCaseVersionRepository;
        this.testParamRepository = testParamRepository;
    }

    private DataTestCase verifyIfDataTestCaseWithSameCodeExist(String code) {
        Criteria criteria = new Criteria();
        criteria.orOperator(Criteria.where("isActive").is(true), Criteria.where("isDeleted").is(true));
        Query query = new Query(criteria);
        query.addCriteria(Criteria.where("code").regex(exactText(code),"i"));
        return dataTestCaseRepository.findAll(query).isEmpty() ? null : dataTestCaseRepository.findAll(query).get(0);
    }

    private String exactText(String value) {
        return MongoRegexCreator.INSTANCE.toRegularExpression(value, MongoRegexCreator.MatchMode.EXACT);
    }


    @Transactional
    public DataTestCase createDataTestCase(DataTestCaseRequest dataTestCaseRequest) {
        DataTestCase dataTestCaseExist = verifyIfDataTestCaseWithSameCodeExist(dataTestCaseRequest.getCode());
        if (dataTestCaseExist == null) {
            DataTestCase dataTestCase = dataTestCaseRequest.buildDataTestCase();

            dataTestCase.setExternalUid(ReferenceUID.builder().build());
            dataTestCase.setCreationDate(new Date());
            List<DataTestCaseVersion> dataTestCaseVersions = dataTestCaseRequest.getApplicationVersionList();
            List<TestParam> testParams  = dataTestCaseRequest.getValues();
            dataTestCaseRepository.save(dataTestCase);
            if (dataTestCaseRequest.isDefault() ){
                setDefault(dataTestCase.getId());
            }
            if (testParams.size()>0){
                testParams.forEach(param->{
                    TestParam testParam = new TestParam();
                    testParam.setDataTestCase(dataTestCase);
                    testParam.setLabel(param.getLabel());
                    testParam.setValue(param.getValue());
                    testParam.setExternalUid(ReferenceUID.builder().build());
                    testParamRepository.save(testParam);
                });
            }
            if (!(dataTestCaseVersions.size() <= 0)) {
                dataTestCaseVersions.forEach(version -> {
                    DataTestCaseVersion dataTestCaseVersion = new DataTestCaseVersion();
                    dataTestCaseVersion.setApplicationVersion(version.getApplicationVersion());
                    dataTestCaseVersion.setDataTestCase(dataTestCase);
                    dataTestCaseVersion.setExternalUid(ReferenceUID.builder().build());
                    dataTestCaseVersion.setFileInfo(version.getFileInfo());
                    dataTestCaseVersionRepository.save(dataTestCaseVersion);
                });

            }

            return dataTestCase;

        } else if (dataTestCaseExist.getIsActive()) {
            log.info("[INFO] DataTestCase with code=[{}] has already an existing active dataTestCase with id=[{}]", dataTestCaseExist.getCode(),
                    dataTestCaseExist.getId());
            throw new BusinessException(DomainErrorCodes.TEST_CODE_EXISTS);
        } else {
            log.info("[INFO] DataTestCase with code=[{}] has already an existing deleted dataTestCase with id=[{}]",
                    dataTestCaseExist.getCode(), dataTestCaseExist.getId());
            throw new BusinessException(DomainErrorCodes.TEST_CODE_DELETED);
        }
    }

    public PageResponse<DataTestCase> getAllDtatTtestCases(Pageable paging, Map<String, Object> inputs) {
        Query query = prepareQuerySearch(inputs);
        return new PageResponse<DataTestCase>(dataTestCaseRepository.findAll(query, paging));
    }
    public boolean verifyIfDataTestCaseWithTestId(String id){
        Query query = new Query();
        query.addCriteria(Criteria.where("isActive").is(true));
        query.addCriteria(Criteria.where("isDefault").is(true));
        query.addCriteria(Criteria.where("test.$id").in(new ObjectId(id)));
        List<DataTestCase> dataTestCases = dataTestCaseRepository.findAll(query);
        if (dataTestCases.isEmpty()){
            return false ;
        }else
            return true ;
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

    public void setDefault (String dataTestCaseId){
        DataTestCase dataTestCase = dataTestCaseRepository.findById(dataTestCaseId).get();
        Query query = new Query();
        query.addCriteria(Criteria.where("isActive").is(true));
        query.addCriteria(Criteria.where("test.$id").in(new ObjectId(dataTestCase.getTest().getId())));
        query.addCriteria(Criteria.where("isDefault").is(true));
        List<DataTestCase> dataTestCases = dataTestCaseRepository.findAll(query);
        if(!dataTestCases.isEmpty()){
            if(!dataTestCases.get(0).getId().equals(dataTestCaseId)){
                dataTestCases.get(0).setDefault(false);
                dataTestCaseRepository.save(dataTestCases.get(0));
                dataTestCase.setDefault(true);
                dataTestCaseRepository.save(dataTestCase);
            }

        }
        else{
            dataTestCase.setDefault(true);
            dataTestCaseRepository.save(dataTestCase);
        }
    }

    public DataTestCase getById(String dataTestCaseId){
        return this.dataTestCaseRepository.findById(dataTestCaseId).get();
    }
    public DataTestCase getByCode(String code){
        return this.dataTestCaseRepository.findByCode(code);
    }

    public List<DataTestCase>  getDefaultDataTestCaseByTestId (String testId){
        Query query = new Query();
        query.addCriteria(Criteria.where("isActive").is(true));
        query.addCriteria(Criteria.where("test.$id").in(new ObjectId(testId)));
        query.addCriteria(Criteria.where("isDefault").is(true));
        List<DataTestCase> dataTestCases = dataTestCaseRepository.findAll(query);
        return dataTestCases;
    }
    public DataTestCase updateDataTestCase(String id , DataTestCaseRequest newDataTestCaseRequest){
        DataTestCase existingDataTestWithSameCode = verifyIfDataTestCaseWithSameCodeExist(newDataTestCaseRequest.getCode());
        DataTestCase oldDataTestCase = getById(id);
        DataTestCase newDataTestCase = newDataTestCaseRequest.buildDataTestCase();
        List<DataTestCaseVersion> oldDataTestCaseVersions = getDataTestVersionsByDataTestCase(id);
        List<TestParam> oldTestParams  = getTestParmsByDataTestCase(id);
        if (existingDataTestWithSameCode == null || oldDataTestCase.getCode().equals(newDataTestCase.getCode())) {
            oldDataTestCase.setId(null);
            oldDataTestCase.setEndDate(new Date());
            oldDataTestCase.setIsActive(false);
            dataTestCaseRepository.save(oldDataTestCase);
            oldTestParams.forEach(testParam -> {
                testParam.setIsActive(false);
                testParam.setIsDeleted(true);
                testParam.setEndDate(new Date());
            });
            oldDataTestCaseVersions.forEach(dataTestCaseVersion -> {
                dataTestCaseVersion.setIsActive(false);
                dataTestCaseVersion.setIsDeleted(true);
                dataTestCaseVersion.setEndDate(new Date());
            });
            dataTestCaseVersionRepository.saveAll(oldDataTestCaseVersions);
            testParamRepository.saveAll(oldTestParams);
            newDataTestCase.setId(id);
            newDataTestCase.setExternalUid(ReferenceUID.builder().build());
            newDataTestCase.setCreationDate(new Date());
            dataTestCaseRepository.save(newDataTestCase);
            List<DataTestCaseVersion> newDataTestCaseVersions = newDataTestCaseRequest.getApplicationVersionList();
            List<TestParam> newTestParam = newDataTestCaseRequest.getValues();
            if(newDataTestCaseVersions != null){
                newDataTestCaseVersions.forEach(dataTestCase->{
                    DataTestCaseVersion dataTestCaseVersion = new DataTestCaseVersion();
                    dataTestCaseVersion.setApplicationVersion(dataTestCase.getApplicationVersion());
                    dataTestCaseVersion.setDataTestCase(newDataTestCase);
                    dataTestCaseVersion.setExternalUid(ReferenceUID.builder().build());
                    dataTestCaseVersion.setFileInfo(dataTestCase.getFileInfo());
                    dataTestCaseVersionRepository.save(dataTestCaseVersion);
                });
                if(newTestParam != null){
                    newTestParam.forEach(testParam -> {
                        TestParam testParams = new TestParam();
                        testParams.setDataTestCase(newDataTestCase);
                        testParams.setLabel(testParam.getLabel());
                        testParams.setValue(testParam.getValue());
                        testParams.setExternalUid(ReferenceUID.builder().build());
                        testParamRepository.save(testParams);
                    });
                }
            }

            return newDataTestCase ;
        }
        else if(existingDataTestWithSameCode.getIsActive()) {
            log.info("[INFO] Campaign with code=[{}] has already an existing active dataTestCase with id=[{}]",existingDataTestWithSameCode.getCode(),existingDataTestWithSameCode.getId());
            throw new BusinessException(DomainErrorCodes.DATA_TEST_CASE_EXISTS);
        }
        else{
            log.info("[INFO] Campaign with code=[{}] has already an existing deleted dataTestCase with id=[{}]",existingDataTestWithSameCode.getCode(),existingDataTestWithSameCode.getId());
            throw new BusinessException(DomainErrorCodes.DATA_TEST_CASE_DELETED);
        }
    }

    public List<TestParam> getTestParmsByDataTestCase(String id){
        Criteria criteria = new Criteria();
        Query query = new Query(criteria);
        query.addCriteria(Criteria.where("isActive").is(true));
        query.addCriteria(Criteria.where("dataTestCase.$id").is(new ObjectId(id)));
        return testParamRepository.findAll(query);
    }
    public List<DataTestCaseVersion> getDataTestVersionsByDataTestCase(String id){
        Criteria criteria = new Criteria();
        Query query = new Query(criteria);
        query.addCriteria(Criteria.where("isActive").is(true));
        query.addCriteria(Criteria.where("dataTestCase.$id").is(new ObjectId(id)));
        return dataTestCaseVersionRepository.findAll(query);
    }
}
