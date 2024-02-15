package fr.lovotech.galaxy.qa.backoffice.repository;

import com.nimbusds.oauth2.sdk.util.CollectionUtils;

import fr.lovotech.galaxy.qa.backoffice.domain.Application;
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
public class CustomAplicationRepositoryImpl implements CustomApplicationRepository{
    private final MongoTemplate mongoTemplate;

    public  CustomAplicationRepositoryImpl (MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Page<Application> findApplicationsByCriterias(int page, int size, Map<String, Object> inputs) {
        List<Sort.Order> orders = new ArrayList<>();
        Pageable pageable = null;
        Query query = prepareQuerySearch(inputs);
        int count = (int) mongoTemplate.count(query, Application.class);
        if (CollectionUtils.isNotEmpty(orders)) {
            pageable = PageRequest.of(page, size, Sort.by(orders));
        } else {
            pageable = PageRequest.of(page, size);
        }
        query.collation(Collation.of("fr").strength(Collation.ComparisonLevel.secondary()));
        query.with(pageable);
        List<Application> ApplicationList = mongoTemplate.find(query, Application.class);
        return PageableExecutionUtils.getPage(ApplicationList, pageable, () -> count);
    }

    private Query prepareQuerySearch(Map<String, Object> inputs){
        Query query = new Query();
        for (Map.Entry<String, Object> input : inputs.entrySet()) {
            switch (input.getKey()) {
                case "creationDate":
                    query.addCriteria(Criteria.where("creationDate").regex(containsTextRegEx(input.getValue().toString()), "i"));
                    break;
                case "code":
                    query.addCriteria(Criteria.where("code").regex(containsTextRegEx(input.getValue().toString()), "i"));
                    break;
                    case "resume":
                    query.addCriteria(Criteria.where("resume").regex(containsTextRegEx(input.getValue().toString()), "i"));
                    break;
                    case "configurationMLS":
                    query.addCriteria(Criteria.where("configurationMLS").regex(containsTextRegEx(input.getValue().toString()), "i"));
                    break;
                    case "covalidDatede":
                    query.addCriteria(Criteria.where("validDate").regex(containsTextRegEx(input.getValue().toString()), "i"));
                    break;
                    case "lastUpdate":
                    query.addCriteria(Criteria.where("lastUpdate").regex(containsTextRegEx(input.getValue().toString()), "i"));
                    break;
                    case "comment":
                    query.addCriteria(Criteria.where("comment").regex(containsTextRegEx(input.getValue().toString()), "i"));
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
