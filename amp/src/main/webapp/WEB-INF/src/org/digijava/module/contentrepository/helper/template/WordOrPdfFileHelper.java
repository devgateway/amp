package org.digijava.module.contentrepository.helper.template;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * class used to build word/pdf Document, which will be stoder in jcr
 * @author Dare
 *
 */
public abstract class WordOrPdfFileHelper {
    
    private String docTitle;
    private InputStream content;
    private String contentType;
    private int fileSize;
    private Long documentType;
    
    public WordOrPdfFileHelper(){ 
        
    }
    
    public WordOrPdfFileHelper(String docTitle,String contentType, Long documentType, byte[] body){
        this.docTitle=docTitle;
        this.contentType=contentType;
        this.content=new ByteArrayInputStream(body);
        this.fileSize=body.length;
        this.documentType=documentType;
    }
    
    /**
     * what kind of document is this
     */
    public abstract String getFileType();
    
    public String getDocTitle() {
        return docTitle;
    }
    public void setDocTitle(String docTitle) {
        this.docTitle = docTitle;
    }
    public InputStream getContent() {
        return content;
    }
    public void setContent(InputStream content) {
        this.content = content;
    }
    public String getContentType() {
        return contentType;
    }
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
    public int getFileSize() {
        return fileSize;
    }
    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public Long getDocumentType() {
        return documentType;
    }

    public void setDocumentType(Long documentType) {
        this.documentType = documentType;
    }

    
    
}
