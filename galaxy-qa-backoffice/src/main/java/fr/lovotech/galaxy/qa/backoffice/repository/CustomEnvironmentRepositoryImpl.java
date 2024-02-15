package fr.lovotech.galaxy.qa.backoffice.repository;

import com.nimbusds.oauth2.sdk.util.CollectionUtils;
import fr.lovotech.galaxy.qa.backoffice.domain.Environment;
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
public class CustomEnvironmentRepositoryImpl implements CustomEnvironmentRepository {
    private final MongoTemplate mongoTemplate;

    public CustomEnvironmentRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Page<Environment> findEnvironmentsByCriterias(int page, int size, Map<String, Object> inputs) {
        List<Sort.Order> orders = new ArrayList<>();
        Pageable pageable = null;
        Query query = prepareQuerySearch(inputs);
        int count = (int) mongoTemplate.count(query, Environment.class);
        if (CollectionUtils.isNotEmpty(orders)) {
            pageable = PageRequest.of(page, size, Sort.by(orders));
        } else {
            pageable = PageRequest.of(page, size);
        }
        query.collation(Collation.of("fr").strength(Collation.ComparisonLevel.secondary()));
        query.with(pageable);
        List<Environment> procedureList = mongoTemplate.find(query, Environment.class);
        return PageableExecutionUtils.getPage(procedureList, pageable, () -> count);
    }

    private Query prepareQuerySearch(Map<String, Object> inputs){
        Query query = new Query();
        for (Map.Entry<String, Object> input : inputs.entrySet()) {
            switch (input.getKey()) {
                case "environmentName":
                    query.addCriteria(Criteria.where("environmentName").regex(containsTextRegEx(input.getValue().toString()), "i"));
                    break;
                case "environmentDescription":
                    query.addCriteria(Criteria.where("environmentDescription").regex(containsTextRegEx(input.getValue().toString()), "i"));
                    break;
                case "databaseHost":
                    query.addCriteria(Criteria.where("databaseHost").regex(containsTextRegEx(input.getValue().toString()), "i"));
                    break;
                case "databaseLogin":
                    query.addCriteria(Criteria.where("databaseLogin").regex(containsTextRegEx(input.getValue().toString()), "i"));
                    break;
                case "databasePassword":
                    query.addCriteria(Criteria.where("databasePassword").regex(containsTextRegEx(input.getValue().toString()), "i"));
                    break;
                case "databaseService":
                    query.addCriteria(Criteria.where("databaseService").regex(containsTextRegEx(input.getValue().toString()), "i"));
                    break;
                case "databaseSchema":
                    query.addCriteria(Criteria.where("databaseSchema").regex(containsTextRegEx(input.getValue().toString()), "i"));
                    break;
                case "databasePort":
                    query.addCriteria(Criteria.where("databasePort").regex(containsTextRegEx(input.getValue().toString()), "i"));
                    break;
                case "appHost":
                    query.addCriteria(Criteria.where("appHost").regex(containsTextRegEx(input.getValue().toString()), "i"));
                    break;
                case "appLogin":
                    query.addCriteria(Criteria.where("appLogin").regex(containsTextRegEx(input.getValue().toString()), "i"));
                    break;
                case "appPassword":
                    query.addCriteria(Criteria.where("appPassword").regex(containsTextRegEx(input.getValue().toString()), "i"));
                    break;
                case "appLoginUrl":
                    query.addCriteria(Criteria.where("appLoginUrl").regex(containsTextRegEx(input.getValue().toString()), "i"));
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