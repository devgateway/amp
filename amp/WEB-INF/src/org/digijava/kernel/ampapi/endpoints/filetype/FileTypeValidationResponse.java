package org.digijava.kernel.ampapi.endpoints.filetype;

/**
 *  This class represents a response obtained from the file type validation
 * 
 * @author Viorel Chihai
 *
 */
public class FileTypeValidationResponse {

    FileTypeValidationStatus status;
    
    String contentName;
    String description;
    String extension;
    
    public FileTypeValidationResponse(FileTypeValidationStatus status) {
        this.status = status;
    }
    
    public FileTypeValidationResponse(FileTypeValidationStatus status, String contentName, String description) {
        this.status = status;
        this.contentName = contentName;
        this.description = description;
    }
    
    public FileTypeValidationResponse(FileTypeValidationStatus status, String contentName, String description, String extension) {
        super();
        this.status = status;
        this.contentName = contentName;
        this.description = description;
        this.extension = extension;
    }

    public FileTypeValidationStatus getStatus() {
        return status;
    }

    public void setStatus(FileTypeValidationStatus status) {
        this.status = status;
    }

    public String getContentName() {
        return contentName;
    }

    public void setContentName(String conentName) {
        this.contentName = conentName;
    }
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }
    
}
