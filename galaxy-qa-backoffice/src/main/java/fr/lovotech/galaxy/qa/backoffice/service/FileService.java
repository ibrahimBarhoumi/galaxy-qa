package fr.lovotech.galaxy.qa.backoffice.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


import fr.lovotech.galaxy.qa.backoffice.client.FileServiceClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import fr.lovotech.galaxy.qa.backoffice.domain.FileInfo;
import fr.lovotech.galaxy.qa.backoffice.remote.file.FileClient;

@Service
public class FileService {

    private final FileClient fileClient;
    private final FileServiceClient fileServiceClient ;

    public FileService(FileClient fileClient, FileServiceClient fileServiceClient) {
        this.fileClient = fileClient;
        this.fileServiceClient = fileServiceClient;
    }

    public FileInfo uploadPublicFile(MultipartFile file, String fileName, String fileType,
            String organization, String attachmentType, String origin, String businessKey) {
        return fileClient.uploadPublicFile(file, fileName, fileType, organization, attachmentType, origin,
                businessKey);
    }


    public void deleteFiles(List<String> fileId) {
        fileClient.deleteFiles(fileId);
    }

    public InputStream readFile(String fileId) throws IOException {
        return fileClient.loadFile(fileId).body().asInputStream();
    }
    public List<byte[]> getImages(List<String> idFiles){
        List<byte[]> list = new ArrayList<byte[]>();
        idFiles.forEach(e->{
           list.add(fileServiceClient.loadFile(e)) ;
        });
        return list ;
    }
}
