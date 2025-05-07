package org.digijava.kernel.ampapi.endpoints.integration.service;

import com.sun.jersey.core.header.FormDataContentDisposition;
import org.digijava.kernel.ampapi.endpoints.integration.dto.FileUploadedResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;

public interface FileUploaderService {

    void uploadFile(MultipartFile file) throws IOException;

    FileUploadedResponseDTO uploadFile(InputStream fileInputStream, FormDataContentDisposition fileMetaData) throws IOException;
}
