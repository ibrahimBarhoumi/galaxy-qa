package fr.lovotech.galaxy.qa.backoffice.service;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

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

import fr.lovotech.galaxy.qa.backoffice.core.EnvironmentRequest;
import fr.lovotech.galaxy.qa.backoffice.domain.Environment;
import fr.lovotech.galaxy.qa.backoffice.domain.ReferenceUID;
import fr.lovotech.galaxy.qa.backoffice.exception.BusinessException;
import fr.lovotech.galaxy.qa.backoffice.exception.DomainErrorCodes;
import fr.lovotech.galaxy.qa.backoffice.repository.CustomEnvironmentRepository;
import fr.lovotech.galaxy.qa.backoffice.repository.EnvironmentRepository;
import fr.lovotech.galaxy.qa.backoffice.service.utlis.CommonService;

@Service
public class EnvironmentService extends CommonService {
    private final Logger log = LoggerFactory.getLogger(EnvironmentService.class);

    private final fr.lovotech.galaxy.qa.backoffice.repository.EnvironmentRepository EnvironmentRepository;
    private final CustomEnvironmentRepository customEnvironmentRepository;
    private final MongoTemplate mongoTemplate;

    public EnvironmentService(EnvironmentRepository EnvironmentRepository, CustomEnvironmentRepository customEnvironmentRepository, MongoTemplate mongoTemplate) {
        this.EnvironmentRepository = EnvironmentRepository;
        this.customEnvironmentRepository = customEnvironmentRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public Environment createEnvironment(EnvironmentRequest EnvironmentRequest) {
        Environment Environment = EnvironmentRequest.buildEnvironment();
        Environment.setExternalUid(ReferenceUID.builder().build());
        return saveOrUpdate(Environment);
    }

    public Page<Environment> getAllEnvironments(int page, int size, Map<String, Object> inputs) {
        Page<Environment> Environments = customEnvironmentRepository.findEnvironmentsByCriterias(page, size, inputs);
        long count = Environments.getTotalElements();
        Pageable pageable = PageRequest.of(page, size);
        return PageableExecutionUtils.getPage(new ArrayList<>(Environments.getContent()), pageable, () -> count);
    }

    public Environment updateEnvironment(String id, EnvironmentRequest EnvironmentRequest) {
        Environment Environment = getEnvironmentById(id);
        Update update = Environment.updateFrom(EnvironmentRequest);
        if (update.getUpdateObject().isEmpty()) {
            return Environment;
        }
        Query query = new Query().addCriteria(Criteria.where("id").is(id));
        mongoTemplate.updateFirst(query, update, Environment.class);
        Environment = this.getEnvironmentById(id);
        return Environment;

    }

    public Environment saveOrUpdate(Environment Environment) {
        Environment = EnvironmentRepository.save(Environment);
        return Environment;
    }

    public Environment getEnvironmentById(String id) {
        Optional<Environment> Environment = EnvironmentRepository.findById(id);
        if (Environment.isEmpty()) {
            log.error("[ERROR] Environment with id {} not found: ", id);
            throw new BusinessException(DomainErrorCodes.ENVIRONMENT_NOT_FOUND);
        }
        return Environment.get();
    }

    public void deleteEnvironment(String id) {
        log.debug("Request to delete Environment : {}", id);
        Environment Environment = getEnvironmentById(id);
        if (Environment != null) {
            EnvironmentRepository.deleteById(Environment.getId());
        }
    }
}