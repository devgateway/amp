package org.digijava.kernel.ampapi.endpoints.integration.service;

import com.sun.jersey.core.header.FormDataContentDisposition;
import org.digijava.kernel.ampapi.endpoints.integration.IntegraionUploadsDirectoryConfig;
import org.digijava.kernel.ampapi.endpoints.integration.dto.FileUploadedResponseDTO;
import org.digijava.kernel.entity.integration.FileUploaded;
import org.digijava.kernel.persistence.PersistenceManager;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileUploaderServiceImpl implements FileUploaderService {

    private static FileUploaderService fileUploaderService;

    private FileUploaderServiceImpl() {
    }

    public static FileUploaderService getInstance() {
        if (fileUploaderService == null) {
            fileUploaderService = new FileUploaderServiceImpl();
        }
        return fileUploaderService;
    }

    @Override
    public void uploadFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String path = IntegraionUploadsDirectoryConfig.getInstance().getUploadsDir();
        Path filePath = Paths.get(path + fileName);
        file.transferTo(filePath.toFile());

        FileUploaded entity = new FileUploaded(path, fileName);

        Session session = PersistenceManager.getSession();
        session.save(entity);

    }

    @Override
    public FileUploadedResponseDTO uploadFile(InputStream fileInputStream, FormDataContentDisposition fileMetaData) throws IOException {
        String fileName = fileMetaData.getFileName();
        String path = IntegraionUploadsDirectoryConfig.getInstance().getUploadsDir();
        Files.copy(fileInputStream, Paths.get(path + fileName));

        FileUploaded entity = new FileUploaded(path, fileName);

        Session session = PersistenceManager.getSession();
        session.save(entity);

        FileUploadedResponseDTO dto = new FileUploadedResponseDTO();
        dto.setFileName(fileName);
        dto.setPath(path);

        return dto;

    }

}
