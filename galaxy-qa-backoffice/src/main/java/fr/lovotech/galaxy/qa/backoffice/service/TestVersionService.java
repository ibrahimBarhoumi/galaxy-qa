package fr.lovotech.galaxy.qa.backoffice.service;


import fr.lovotech.galaxy.qa.backoffice.domain.Test;
import fr.lovotech.galaxy.qa.backoffice.domain.TestVersion;
import fr.lovotech.galaxy.qa.backoffice.repository.TestRepository;
import fr.lovotech.galaxy.qa.backoffice.repository.TestVersionRepository;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class TestVersionService {

    private final TestVersionRepository  testVersionRepository ;
    private final DataTestCaseService dataTestCaseService ;
    private final TestRepository testRepository;

    public TestVersionService(TestVersionRepository testVersionRepository, DataTestCaseService dataTestCaseService, TestRepository testRepository) {
        this.testVersionRepository = testVersionRepository;
        this.dataTestCaseService = dataTestCaseService;
        this.testRepository = testRepository;
    }

    public Page<Test> getTestVersionByVersionId(List<String> stringIdList , Pageable paging){
        List<ObjectId> objectIdList = new ArrayList<>();
        stringIdList.forEach(s -> {
            objectIdList.add(new ObjectId(s));
        });
        Query query = new Query();
        query.addCriteria(Criteria.where("isActive").is(true));
        query.addCriteria(Criteria.where("applicationVersion.$id").in(objectIdList));
        List<TestVersion> testVersion = testVersionRepository.findAll(query);
        List<Test> testList = new ArrayList<>();
        if( objectIdList.size() <= 1) {
            testVersion.forEach(testVersion1 -> {
                if (dataTestCaseService.verifyIfDataTestCaseWithTestId(testVersion1.getTest().getId())) {
                    testList.add(testVersion1.getTest());
                }

            });
            Page<Test> page = new PageImpl<>(testList);
            return  page;
        }
        else {
            testVersion.forEach(testVersion1 -> {
                if (dataTestCaseService.verifyIfDataTestCaseWithTestId(testVersion1.getTest().getId())) {
                    testList.add(testVersion1.getTest());
                }
            });
            List<Test> testList1 = new ArrayList<>();
            testList1 = testList ;
           //testList1 = testList1.stream().filter(i-> Collections.frequency(testList, i) > 1).collect(Collectors.toList());
            //testList1 = testList.stream().filter(distinctByKey(p -> p.getId())).collect(Collectors.toList());
            List<String> listId = testList.stream().map(Test::getId).collect(Collectors.toList());
            Set<String> testSet = findDuplicateBySetAdd(listId);
            listId = testSet.stream().collect(Collectors.toList());
            Iterable<Test> testIterable = testRepository.findAllById(listId);
            List<Test> testList2 = new ArrayList<>();
            testIterable.forEach(testList2::add);
            Page<Test> page = new PageImpl<>(testList2);
            return  page;
        }
    }
    public List<TestVersion> getTestVersionByTestId(String id){
        Criteria criteria = new Criteria();
        Query query = new Query(criteria);
        query.addCriteria(Criteria.where("isActive").is(true));
        query.addCriteria(Criteria.where("test.$id").is(new ObjectId(id)));
        return testVersionRepository.findAll(query);
    }
    public static <T> Set<T> findDuplicateBySetAdd(List<T> list) {
        Set<T> items = new HashSet<>();
        return list.stream()
                .filter(n -> !items.add(n)) // Set.add() returns false if the element was already in the set.
                .collect(Collectors.toSet());
    }
}

