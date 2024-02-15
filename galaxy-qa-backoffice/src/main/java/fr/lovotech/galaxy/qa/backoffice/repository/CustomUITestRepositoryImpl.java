package fr.lovotech.galaxy.qa.backoffice.repository;

import com.nimbusds.oauth2.sdk.util.CollectionUtils;
import fr.lovotech.galaxy.qa.backoffice.domain.Trigger;
import fr.lovotech.galaxy.qa.backoffice.domain.UITest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Collation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.MongoRegexCreator;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class CustomUITestRepositoryImpl implements CustomUITestRepository {
    private final MongoTemplate mongoTemplate;

    public CustomUITestRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
    @Override
    public Page<UITest> findUITestsByCriterias(int page, int size, Map<String, Object> inputs) {
        List<Sort.Order> orders = new ArrayList<>();
        Pageable pageable = null;
        Query query = prepareQuerySearch(inputs);
        int count = (int) mongoTemplate.count(query, Trigger.class);
        if (CollectionUtils.isNotEmpty(orders)) {
            pageable = PageRequest.of(page, size, Sort.by(orders));
        } else {
            pageable = PageRequest.of(page, size);
        }
        query.collation(Collation.of("fr").strength(Collation.ComparisonLevel.secondary()));
        query.with(pageable);
        List<UITest> uiTests = mongoTemplate.find(query, UITest.class);
        return PageableExecutionUtils.getPage(uiTests, pageable, () -> count);
    }

    private Query prepareQuerySearch(Map<String, Object> inputs){
        Query query = new Query();
        for (Map.Entry<String, Object> input : inputs.entrySet()) {
            switch (input.getKey()) {
                case "name":
                    query.addCriteria(Criteria.where("name").regex(containsTextRegEx(input.getValue().toString()), "i"));
                    break;
                case "deleted":
                    query.addCriteria(Criteria.where("deleted").is(Boolean.valueOf((String) input.getValue())));
                    break;
                case "type":
                    query.addCriteria(Criteria.where("type").regex(containsTextRegEx(input.getValue().toString()), "i"));
                    break;
                case "triggerFunctionalType":
                    query.addCriteria(Criteria.where("triggerFunctionalType").regex(containsTextRegEx(input.getValue().toString()), "i"));
                    break;
                case "triggerTechnicalType":
                    query.addCriteria(Criteria.where("triggerTechnicalType").regex(containsTextRegEx(input.getValue().toString()), "i"));
                    break;
                case "description":
                    query.addCriteria(Criteria.where("description").regex(containsTextRegEx(input.getValue().toString()), "i"));
                    break;
                case "uiTestType":
                    query.addCriteria(Criteria.where("uiTestType").regex(containsTextRegEx(input.getValue().toString()), "i"));
                    break;

                default:
                    break;
            }
        }
        return  query;
    }
    private String containsTextRegEx(String value) {
        return MongoRegexCreator.INSTANCE.toRegularExpression(value, MongoRegexCreator.MatchMode.CONTAINING);
    }
}
