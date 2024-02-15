package fr.lovotech.galaxy.qa.fileService.service;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import fr.lovotech.galaxy.qa.fileService.domain.FileInfo;
import fr.lovotech.galaxy.qa.fileService.domain.MetaDataFile;
import fr.lovotech.galaxy.qa.fileService.exception.BusinessException;
import fr.lovotech.galaxy.qa.fileService.exception.DomainErrorCodes;
import fr.lovotech.galaxy.qa.fileService.exception.TechnicalException;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class GridFsService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final GridFsTemplateSelector gridFsTemplateSelector;
    private GridFsOperations gridFsOperations;

    public GridFsService(GridFsTemplateSelector gridFsTemplateSelector,
                         GridFsOperations gridFsOperations) {
        this.gridFsTemplateSelector = gridFsTemplateSelector;

        this.gridFsOperations = gridFsOperations;
    }

    public FileInfo saveAttachmentsFilesFromBytesArray(byte[] initialArray, Map<String, String> metadata, String fileName,
                                                       String fileType) throws IOException {
        InputStream inputStream = new ByteArrayInputStream(initialArray);
        DBObject dbObject = convertToMetadataObject(metadata);
        ObjectId objectId = gridFsTemplateSelector.getGridFsTemplate().store(inputStream, fileName, fileType, dbObject);
        return getFileInfo(metadata, fileName, fileType, inputStream.available(), objectId);
    }

    private FileInfo getFileInfo(Map<String, String> metadata, String fileName, String fileType, long fileSize, ObjectId objectId) {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setId(objectId.toString());
        fileInfo.setName(fileName);
        fileInfo.setUploadDate(Instant.now().toString());
        fileInfo.setSize(fileSize);
        fileInfo.setType(fileType);
        if (StringUtils.isNotEmpty(metadata.get("attachmentType"))) {
            fileInfo.setAttachmentType(metadata.get("attachmentType"));
        }
        return fileInfo;
    }

    public FileInfo saveAttachmentsFiles(MultipartFile multipartFile, Map<String, String> metadata, String fileName,
                                         String fileType) throws IOException {
        InputStream inputStream = multipartFile.getInputStream();
        String originalFileName = multipartFile.getOriginalFilename(); // TODO if empty
        String contentType = multipartFile.getContentType();
        DBObject dbObject = convertToMetadataObject(metadata);

        ObjectId objectId = gridFsTemplateSelector.getGridFsTemplate().store(inputStream, originalFileName, contentType, dbObject);

        return getFileInfo(metadata, fileName, fileType, multipartFile.getSize(), objectId);
    }

    private DBObject convertToMetadataObject(Map<String, String> metadataMap) {
        if (MapUtils.isNotEmpty(metadataMap)) {
            DBObject metaData = new BasicDBObject();
            metaData.putAll(metadataMap);
            return metaData;
        }
        return null;
    }

    public GridFsResource findById(String fileId) {
        GridFSFile file = getFileById(fileId);
        return gridFsTemplateSelector.getGridFsTemplate().getResource(file);
    }

    private GridFSFile getFileById(String fileId) {
        GridFSFile file = gridFsTemplateSelector.getGridFsTemplate().findOne(query(where("_id").is(fileId)));
        if (file == null) {
            log.error("[ERROR] file with id [{}] not found", fileId);
            throw new TechnicalException(DomainErrorCodes.FILE_NOT_FOUND);
        }
        return file;
    }

    public MetaDataFile findFileById(String id) {
        GridFSFile gridFSFile = gridFsTemplateSelector.getGridFsTemplate().findOne(query(where("_id").is(id)));

        MetaDataFile metaDataFile = new MetaDataFile();
        metaDataFile.setUploadDate(gridFSFile.getUploadDate().toInstant().toString());
        metaDataFile.setFileName(gridFSFile.getFilename());
        return metaDataFile;
    }

    public void delete(String id) {
        Query query = new Query(where("_id").is(id));
        GridFSFile gridFSFile = gridFsTemplateSelector.getGridFsTemplate().findOne(query);
        if (gridFSFile.getMetadata().get("removable") != null && StringUtils.equals(gridFSFile.getMetadata().get("removable").toString(), "NOT_REMOVABLE")) {
            throw new BusinessException(DomainErrorCodes.DELETING_FILE_UNAUTHORIZED);
        }
        gridFsTemplateSelector.getGridFsTemplate().delete(query);
    }

    public void deleteFiles(List<String> filesId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").in(filesId));
        query.addCriteria(new Criteria("metadata.removable").is("NOT_REMOVABLE"));
        List<GridFSFile> gridFSFiles = new ArrayList<GridFSFile>();
        gridFsTemplateSelector.getGridFsTemplate().find(query).into(gridFSFiles);
        if (gridFSFiles.size() > 0) {
            throw new BusinessException(DomainErrorCodes.EXISTS_FILE_UNAUTHORIZED_TO_DELETE);
        }
        gridFsTemplateSelector.getGridFsTemplate().delete(new Query(where("_id").in(filesId)));
    }

    public FileInfo duplicateFile(String fileId, Map<String, String> metaData, String fileName, String fileType) {
        GridFSFile file = getFileById(fileId);


        DBObject dbObject = convertToMetadataObject(metaData);
        ObjectId objectId = null;
        try {

            GridFsResource gridFsResource = gridFsTemplateSelector.getGridFsTemplate().getResource(file);
            objectId = gridFsTemplateSelector.getGridFsTemplate().store(gridFsResource.getInputStream(), file.getFilename(), gridFsResource.getContentType(), dbObject);

        } catch (IOException e) {
            log.error("[ERROR] can not duplicate file with id [{}], {}", fileId, e.getMessage());
            throw new TechnicalException(DomainErrorCodes.CAN_NOT_DUPLICATE_FILE);
        }
        MetaDataFile metaDataFile = new MetaDataFile();
        metaDataFile.setUploadDate(file.getUploadDate().toInstant().toString());
        metaDataFile.setFileName(file.getFilename());


        return getFileInfo(metaData, fileName, fileType, file.getMetadata().size(), objectId);
    }

    public GridFsResource findByBusinessKey(String businessKey) {
        GridFSFile file = gridFsTemplateSelector.getGridFsTemplate().findOne(query(where("metadata.businessKey").is(businessKey)));
        if (file == null) {
            log.error("[ERROR] file with businessKey [{}] not found", businessKey);
            throw new TechnicalException(DomainErrorCodes.FILE_NOT_FOUND);
        }
        return gridFsTemplateSelector.getGridFsTemplate().getResource(file);
    }


    public FileInfo getFileInfo(MultipartFile file, String fileName, String fileType, String organization, String attachmentType, String origin, String businessKey) throws IOException {
        Map<String, String> metaData = new HashMap<>();
        metaData.put("fileName", fileName);
        metaData.put("fileType", fileType);
        metaData.put("organization", organization);
        metaData.put("businessKey", businessKey);
        metaData.put("uploadDate", Instant.now().toString());

        metaData.put("origin", StringUtils.isNotBlank(origin) ? origin : null);
        metaData.put("attachmentType", StringUtils.isNotBlank(attachmentType) ? attachmentType : null);
        return saveAttachmentsFiles(file, metaData, fileName, fileType);
    }
}
