package org.digijava.kernel.ampapi.endpoints.integration.dto;

public class FileUploadedResponseDTO {

    private String path;

    private String fileName;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
