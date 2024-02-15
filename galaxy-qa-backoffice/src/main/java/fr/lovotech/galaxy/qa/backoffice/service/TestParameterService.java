package fr.lovotech.galaxy.qa.backoffice.service;

import java.util.List;
import java.util.Map;

import fr.lovotech.galaxy.qa.backoffice.core.TestParameterRequest;
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

import fr.lovotech.galaxy.qa.backoffice.repository.TestParameterRepository;

@Service
public class TestParameterService {
    private final TestParameterRepository testParameterRepository;
    public TestParameterService(TestParameterRepository testParameterRepository){
        this.testParameterRepository = testParameterRepository;
    }

    public TestParameter createTestParameter(TestParameterRequest testParameterRequest){
        TestParameter testParameter = testParameterRequest.buildTestParameter();
        return this.testParameterRepository.save(testParameter);
    }

    public List<TestParameterDetail> getTestParameterByTestIdAndApplicationVersionId(String testId,String applicationVersionId){
        Criteria criteria = new Criteria();
        Query query = new Query(criteria);
        query.addCriteria(Criteria.where("test.$id").is(new ObjectId(testId)));
        query.addCriteria(Criteria.where("applicationVersion.$id").is(new ObjectId(applicationVersionId)));
        List<TestParameter> testParameterList = testParameterRepository.findAll(query);
        return testParameterList.get(0).getTestParameterDetails();
    }
    public PageResponse<TestParameter> getAllTestParameter(Pageable paging, Map<String, Object> inputs) {
        Query query = prepareQuerySearch(inputs);
        return new PageResponse<TestParameter>(testParameterRepository.findAll(query, paging));
    }

    private Query prepareQuerySearch(Map<String, Object> inputs) {
        Query query = new Query();
        query.addCriteria(Criteria.where("isActive").is(true));
        for (Map.Entry<String, Object> input : inputs.entrySet()) {
            switch (input.getKey()) {
                case "code":
                    query.addCriteria(
                            Criteria.where("code").regex(containsTextRegEx(input.getValue().toString()), "i"));
                    break;
                
                case "test":
                    query.addCriteria(
                            Criteria.where("type").regex(containsTextRegEx(input.getValue().toString()), "i"));
                    break;
                case "applicationVersion":
                    query.addCriteria(Criteria.where("application.$id").is(new ObjectId(input.getValue().toString())));
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
}
