package fr.lovotech.galaxy.qa.backoffice.service;

import fr.lovotech.galaxy.qa.backoffice.core.UITestRequest;
import fr.lovotech.galaxy.qa.backoffice.domain.UITest;
import fr.lovotech.galaxy.qa.backoffice.domain.ReferenceUID;
import fr.lovotech.galaxy.qa.backoffice.domain.UITest;
import fr.lovotech.galaxy.qa.backoffice.exception.BusinessException;
import fr.lovotech.galaxy.qa.backoffice.exception.DomainErrorCodes;
import fr.lovotech.galaxy.qa.backoffice.repository.CustomUITestRepository;
import fr.lovotech.galaxy.qa.backoffice.repository.UITestRepository;
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
public class UITestService {
    private final Logger log = LoggerFactory.getLogger(UITestService.class);

    private final UITestRepository UITestRepository;
    private final CustomUITestRepository customUITestRepository;
    private final MongoTemplate mongoTemplate;

    public UITestService(UITestRepository UITestRepository, CustomUITestRepository customUITestRepository, MongoTemplate mongoTemplate) {
        this.UITestRepository = UITestRepository;
        this.customUITestRepository = customUITestRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public UITest createUITest(UITestRequest uITestRequest) {
        UITest uiTest = uITestRequest.buildUITest();
        uiTest.setExternalUid(ReferenceUID.builder().build());
        return saveOrUpdate(uiTest);
    }

    public Page<UITest> getAllUITests(int page, int size, Map<String, Object> inputs) {
        Page<UITest> uiTests = customUITestRepository.findUITestsByCriterias(page, size, inputs);
        long count = uiTests.getTotalElements();
        Pageable pageable = PageRequest.of(page, size);
        return PageableExecutionUtils.getPage(new ArrayList<>(uiTests.getContent()), pageable, () -> count);
    }

    public UITest updateUITest(String id, UITestRequest uITestRequest) {
        UITest uiTest = getUITestById(id);
        Update update = uiTest.updateFrom(uITestRequest);
        if (update.getUpdateObject().isEmpty()) {
            return uiTest;
        }
        Query query = new Query().addCriteria(Criteria.where("id").is(id));
        mongoTemplate.updateFirst(query, update, UITest.class);
        uiTest = this.getUITestById(id);
        return uiTest;

    }

    public UITest saveOrUpdate(UITest uiTest) {
        uiTest = UITestRepository.save(uiTest);
        return uiTest;
    }

    public UITest getUITestById(String id) {
        Optional<UITest> uiTest = UITestRepository.findById(id);
        if (uiTest.isEmpty()) {
            log.error("[ERROR] UITest with id {} not found: ", id);
            throw new BusinessException(DomainErrorCodes.UITEST_NOT_FOUND);
        }
        return uiTest.get();
    }

    public void deleteUITest(String id) {
        log.debug("Request to delete UITest : {}", id);
        UITest uiTest = getUITestById(id);
        if (uiTest != null) {
            UITestRepository.deleteById(uiTest.getId());
        }
    }
}
