/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 */
package org.dgfoundation.amp.onepager.helper;

import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.digijava.module.contentrepository.helper.ObjectReferringDocument;

import java.io.Serializable;
import java.util.Calendar;
/**
 * 
 * @author aartimon@dginternational.org
 * @since Apr 14, 2011
 */
public class TemporaryDocument implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String title="Document Title";
    private FileUpload file;
    private boolean existing;
    private double fileSize;
    private String contentType;
    private Calendar date;
    protected ObjectReferringDocument existingDocument;
    private String webLink;
    private String fileName;
    private String newTemporaryDocumentId;
    
    public TemporaryDocument() {
        existing = false;
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public FileUpload getFile() {
        return file;
    }

    public void setFile(FileUpload file) {
        this.file = file;
    }

    public boolean isExisting() {
        return existing;
    }

    public void setExisting(boolean existing) {
        this.existing = existing;
    }
    
    public double getFileSize() {
        return fileSize;
    }

    public void setFileSize(double fileSize) {
        this.fileSize = fileSize;
    }
    
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentType() {
        return contentType;
    }
    
    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public ObjectReferringDocument getExistingDocument() {
        return existingDocument;
    }

    public void setExistingDocument(ObjectReferringDocument existingDocument) {
        this.existingDocument = existingDocument;
    }

    public String getWebLink() {
        return webLink;
    }

    public void setWebLink(String webLink) {
        this.webLink = webLink;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getNewTemporaryDocumentId() {
        return newTemporaryDocumentId;
    }

    public void setNewTemporaryDocumentId(String newTemporaryDocumentId) {
        this.newTemporaryDocumentId = newTemporaryDocumentId;
    }
}
