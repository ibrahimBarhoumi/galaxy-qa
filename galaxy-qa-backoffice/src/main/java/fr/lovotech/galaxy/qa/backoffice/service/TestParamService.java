package fr.lovotech.galaxy.qa.backoffice.service;

import fr.lovotech.galaxy.qa.backoffice.domain.DataTestCase;
import fr.lovotech.galaxy.qa.backoffice.domain.Test;
import fr.lovotech.galaxy.qa.backoffice.domain.TestParam;
import fr.lovotech.galaxy.qa.backoffice.repository.TestParamRepository;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestParamService {
    private final TestParamRepository testParamRepository;

    public TestParamService(TestParamRepository testParamRepository) {
        this.testParamRepository = testParamRepository;
    }

    public List<TestParam>getTestParamsByDataTestCaseId(String id){
        Query query = new Query();
        query.addCriteria(Criteria.where("isActive").is(true));
        query.addCriteria(Criteria.where("dataTestCase.$id").in(new ObjectId(id)));
        List<TestParam> testParams = testParamRepository.findAll(query);
        return testParams;
    }
}
