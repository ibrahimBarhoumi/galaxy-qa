package fr.lovotech.galaxy.qa.backoffice.service;

import fr.lovotech.galaxy.qa.backoffice.core.TestCaseRequest;
import fr.lovotech.galaxy.qa.backoffice.domain.ReferenceUID;
import fr.lovotech.galaxy.qa.backoffice.domain.TestCase;
import fr.lovotech.galaxy.qa.backoffice.exception.BusinessException;
import fr.lovotech.galaxy.qa.backoffice.exception.DomainErrorCodes;
import fr.lovotech.galaxy.qa.backoffice.repository.CustomTestCaseRepository;
import fr.lovotech.galaxy.qa.backoffice.repository.TestCaseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

@Service
public class TestCaseService {
    private final Logger log = LoggerFactory.getLogger(TestCaseService.class);

    private final fr.lovotech.galaxy.qa.backoffice.repository.TestCaseRepository TestCaseRepository;
    private final CustomTestCaseRepository customTestCaseRepository;
    private final MongoTemplate mongoTemplate;

    public TestCaseService(TestCaseRepository TestCaseRepository, CustomTestCaseRepository customTestCaseRepository, MongoTemplate mongoTemplate) {
        this.TestCaseRepository = TestCaseRepository;
        this.customTestCaseRepository = customTestCaseRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public TestCase createTestCase(TestCaseRequest TestCaseRequest) {
        TestCase TestCase = TestCaseRequest.buildTestCase();
        TestCase.setExternalUid(ReferenceUID.builder().build());
        return saveOrUpdate(TestCase);
    }

    public Page<TestCase> getAllTestCases(int page, int size, Map<String, Object> inputs) {
        Page<TestCase> TestCases = customTestCaseRepository.findTestCasesByCriterias(page, size, inputs);
        long count = TestCases.getTotalElements();
        Pageable pageable = PageRequest.of(page, size);
        return PageableExecutionUtils.getPage(new ArrayList<>(TestCases.getContent()), pageable, () -> count);
    }

    public TestCase updateTestCase(String id, TestCaseRequest TestCaseRequest) {
        TestCase TestCase = getTestCaseById(id);
        Update update = TestCase.updateFrom(TestCaseRequest);
        if (update.getUpdateObject().isEmpty()) {
            return TestCase;
        }
        Query query = new Query().addCriteria(Criteria.where("id").is(id));
        mongoTemplate.updateFirst(query, update, TestCase.class);
        TestCase = this.getTestCaseById(id);
        return TestCase;

    }

    public TestCase saveOrUpdate(TestCase TestCase) {
        TestCase = TestCaseRepository.save(TestCase);
        return TestCase;
    }

    public TestCase getTestCaseById(String id) {
        Optional<TestCase> TestCase = TestCaseRepository.findById(id);
        if (TestCase.isEmpty()) {
            log.error("[ERROR] TestCase with id {} not found: ", id);
            throw new BusinessException(DomainErrorCodes.TESTCASE_NOT_FOUND);
        }
        return TestCase.get();
    }

    public void deleteTestCase(String id) {
        log.debug("Request to delete TestCase : {}", id);
        TestCase TestCase = getTestCaseById(id);
        if (TestCase != null) {
            TestCaseRepository.deleteById(TestCase.getId());
        }
    }
}
