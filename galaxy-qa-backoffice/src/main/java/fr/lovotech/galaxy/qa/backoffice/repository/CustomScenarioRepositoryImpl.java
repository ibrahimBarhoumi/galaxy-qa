package fr.lovotech.galaxy.qa.backoffice.repository;

import com.nimbusds.oauth2.sdk.util.CollectionUtils;
import fr.lovotech.galaxy.qa.backoffice.domain.Scenario;
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
public class CustomScenarioRepositoryImpl implements CustomScenarioRepository {

    private final MongoTemplate mongoTemplate;

    public CustomScenarioRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Page<Scenario> findScenariosByCriterias(int page, int size, Map<String, Object> inputs) {
        List<Sort.Order> orders = new ArrayList<>();
        Pageable pageable = null;
        Query query = prepareQuerySearch(inputs);
        int count = (int) mongoTemplate.count(query, Scenario.class);
        if (CollectionUtils.isNotEmpty(orders)) {
            pageable = PageRequest.of(page, size, Sort.by(orders));
        } else {
            pageable = PageRequest.of(page, size);
        }
        query.collation(Collation.of("fr").strength(Collation.ComparisonLevel.secondary()));
        query.with(pageable);
        List<Scenario> ScenarioList = mongoTemplate.find(query, Scenario.class);
        return PageableExecutionUtils.getPage(ScenarioList, pageable, () -> count);
    }

    private Query prepareQuerySearch(Map<String, Object> inputs){
        Query query = new Query();
        for (Map.Entry<String, Object> input : inputs.entrySet()) {
            switch (input.getKey()) {
                case "code":
                    query.addCriteria(Criteria.where("code").regex(containsTextRegEx(input.getValue().toString()), "i"));
                    break;
                case "scenarioStatus":
                    query.addCriteria(Criteria.where("scenarioStatus").regex(containsTextRegEx(input.getValue().toString()), "i"));
                    break;
                case "scenarioType":
                    query.addCriteria(Criteria.where("scenarioType").regex(containsTextRegEx(input.getValue().toString()), "i"));
                    break;
                case "isDeleted":
                    query.addCriteria(Criteria.where("isDeleted").is(input.getValue()));
                    break;
                case "isActive":
                    query.addCriteria(Criteria.where("isActive").is(input.getValue()));
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
