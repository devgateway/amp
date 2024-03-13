package org.digijava.kernel.ampapi.endpoints.resource;

import org.apache.commons.io.FileUtils;
import org.digijava.module.contentrepository.helper.TemporaryDocumentData;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.struts.upload.FormFile;

/**
 * Adapts jersey file & metadata to {@link FormFile} that is consumed by {@link TemporaryDocumentData}.
 *
 * @author Octavian Ciubotaru
 */
class JerseyFileAdapter implements FormFile {

    private FormDataContentDisposition fileDetail;
    private File file;

    JerseyFileAdapter(FormDataContentDisposition fileDetail, File file) {
        this.fileDetail = fileDetail;
        this.file = file;
    }

    @Override
    public String getContentType() {
        return fileDetail.getType();
    }

    @Override
    public void setContentType(String contentType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getFileSize() {
        return (int) file.length();
    }

    @Override
    public void setFileSize(int fileSize) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getFileName() {
        return fileDetail.getFileName();
    }

    @Override
    public void setFileName(String fileName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public byte[] getFileData() throws IOException {
        return FileUtils.readFileToByteArray(file);
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(file);
    }

    @Override
    public void destroy() {
    }
}
