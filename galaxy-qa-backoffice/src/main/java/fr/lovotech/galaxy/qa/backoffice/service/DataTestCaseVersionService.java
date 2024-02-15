package fr.lovotech.galaxy.qa.backoffice.service;

import feign.Response;
import fr.lovotech.galaxy.qa.backoffice.client.FileServiceClient;
import fr.lovotech.galaxy.qa.backoffice.domain.ApplicationVersion;
import fr.lovotech.galaxy.qa.backoffice.domain.DataTestCase;
import fr.lovotech.galaxy.qa.backoffice.domain.DataTestCaseVersion;
import fr.lovotech.galaxy.qa.backoffice.domain.FileInfo;
import fr.lovotech.galaxy.qa.backoffice.repository.DataTestCaseRepository;
import fr.lovotech.galaxy.qa.backoffice.repository.DataTestCaseVersionRepository;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DataTestCaseVersionService {

    private final DataTestCaseVersionRepository dataTestCaseVersionRepository ;
    private final FileServiceClient fileServiceClient;
    private final DataTestCaseRepository dataTestCaseRepository;
    private final FileService fileService ;

    public DataTestCaseVersionService(DataTestCaseVersionRepository dataTestCaseVersionRepository, FileServiceClient fileServiceClient, DataTestCaseRepository dataTestCaseRepository, FileService fileService) {
        this.dataTestCaseVersionRepository = dataTestCaseVersionRepository;
        this.fileServiceClient = fileServiceClient;
        this.dataTestCaseRepository = dataTestCaseRepository;
        this.fileService = fileService;
    }

    public Page<ApplicationVersion> getVersionsByDataTestCaseId (String id , Pageable paging){
        Query query = new Query();
        query.addCriteria(Criteria.where("isActive").is(true));
        query.addCriteria(Criteria.where("dataTestCase.$id").in(new ObjectId(id)));
        List<DataTestCaseVersion> dataTestCaseVersions = dataTestCaseVersionRepository.findAll(query);
        List<ApplicationVersion> applicationVersionList = new ArrayList<>();
        dataTestCaseVersions.forEach(element->{
            applicationVersionList.add(element.getApplicationVersion());
        });
        Page<ApplicationVersion> page = new PageImpl(applicationVersionList);
        return page ;
    }

    public FileInfo     getFilesInfos(MultipartFile file,String fileName,String fileType, String organization, String attachmentType, String origin, String businessKey) {
        return fileServiceClient.uploadPublicFile( file,fileName,fileType,organization, attachmentType, origin, businessKey);
    }

    public List<ApplicationVersion> getVersionsByTestId(String testId){
        Query query = new Query();
        query.addCriteria(Criteria.where("isActive").is(true));
        query.addCriteria(Criteria.where("test.$id").in(new ObjectId(testId)));
        List<DataTestCase> dataTestCases = dataTestCaseRepository.findAll(query);
        List<DataTestCaseVersion> dataTestCaseVersions = new ArrayList<>();
        List<ApplicationVersion> applicationVersionList = new ArrayList<>();
        dataTestCases.forEach(dataTestCase->{
            Query query1 = new Query();
            query1.addCriteria(Criteria.where("isActive").is(true));
            query1.addCriteria(Criteria.where("dataTestCase.$id").in(new ObjectId(dataTestCase.getId())));
            List<DataTestCaseVersion> dataTestCaseVersions1 = dataTestCaseVersionRepository.findAll(query1);
            dataTestCaseVersions1.forEach(data->{
                dataTestCaseVersions.add(data);
            });
        });
        dataTestCaseVersions.forEach(data->{
            applicationVersionList.add(data.getApplicationVersion());
        });
        List<ApplicationVersion> applicationVersions = applicationVersionList;
        applicationVersions = applicationVersionList.stream().filter(i -> Collections.frequency(applicationVersionList, i) > 1).collect(Collectors.toList());
        applicationVersions= applicationVersionList.stream().distinct().collect(Collectors.toList());
        return applicationVersions;
    }

    public List<DataTestCaseVersion> getDataTestCaseVersionByTestIdAndVersionId(String testId,String versionId){
        Query query = new Query();
        query.addCriteria(Criteria.where("isActive").is(true));
        query.addCriteria(Criteria.where("test.$id").in(new ObjectId(testId)));
        List<DataTestCase> dataTestCases = dataTestCaseRepository.findAll(query);
        List<DataTestCaseVersion> dataTestCaseVersionList = new ArrayList<>();
        dataTestCases.forEach(datatTestCase->{
            Query query1 = new Query();
            query1.addCriteria(Criteria.where("isActive").is(true));
            query1.addCriteria(Criteria.where("dataTestCase.$id").in(new ObjectId(datatTestCase.getId())));
            query1.addCriteria(Criteria.where("applicationVersion.$id").in(new ObjectId(versionId)));
            List<DataTestCaseVersion> dataTestCaseVersion = dataTestCaseVersionRepository.findAll(query1);
            dataTestCaseVersion.forEach(data->{
                dataTestCaseVersionList.add(data);
            });
        });
        return dataTestCaseVersionList;
    }
    public DataTestCaseVersion getDataTestCaseVerisonById (String id){
        return this.dataTestCaseVersionRepository.findById(id).get();
    }

    public List<DataTestCaseVersion> getDataTestCaseVersion(List<String> dataTestCaseId){
        List<ObjectId> objectIdList = new ArrayList<>();
        dataTestCaseId.forEach(s -> {
            objectIdList.add(new ObjectId(s));
        });
        Query query1 = new Query();
        query1.addCriteria(Criteria.where("isActive").is(true));
        query1.addCriteria(Criteria.where("dataTestCase.$id").in(objectIdList));
        List<DataTestCaseVersion> dataTestCaseVersions = dataTestCaseVersionRepository.findAll(query1);
        dataTestCaseVersions.stream().distinct();
        return dataTestCaseVersions ;
    }

   public DataTestCaseVersion getDataTestCaseVersionByDataTestCaseId(String id){
       Query query1 = new Query();
       query1.addCriteria(Criteria.where("isActive").is(true));
       query1.addCriteria(Criteria.where("dataTestCase.$id").in(new ObjectId( id)));
       List<DataTestCaseVersion> dataTestCaseVersionList = dataTestCaseVersionRepository.findAll(query1);
       if(!dataTestCaseVersionList.isEmpty()){
           return  dataTestCaseVersionList.get(0);
       }
        else{
            return null ;
       }
   }
}
