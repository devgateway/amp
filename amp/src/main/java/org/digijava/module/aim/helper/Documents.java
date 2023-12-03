/***
 * Documents.java
 * @author Priyajith 
 */
  
package org.digijava.module.aim.helper;

public class Documents {
    private Long docId;
    private String title;
    private String docDescription;
    private String date;
    private String fileName;
    private boolean isFile;
    private String url;
    private Long activityId;
    private String activityName;
    private String uuid;
    
    private String docType;
    
    private String docComment;
    
    private String docLanguage;

    public Documents() {}
    
    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }
    
    public Documents(Long id) {
        docId = id;
    }
    /**
     * @return Returns the docId.
     */
    public Long getDocId() {
        return docId;
    }
    /**
     * @param docId The docId to set.
     */
    public void setDocId(Long docId) {
        this.docId = docId;
    }
    /**
     * @return Returns the title.
     */
    public String getTitle() {
        return title;
    }
    /**
     * @param title The title to set.
     */
    public void setTitle(String title) {
        this.title = title;
    }
    /**
     * @return Returns the fileName.
     */
    public String getFileName() {
        return fileName;
    }
    /**
     * @param fileName The fileName to set.
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    /**
     * @return Returns the activityId.
     */
    public Long getActivityId() {
        return activityId;
    }
    /**
     * @param activityId The activityId to set.
     */
    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }
    /**
     * @return Returns the activityName.
     */
    public String getActivityName() {
        return activityName;
    }
    /**
     * @param activityName The activityName to set.
     */
    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }
    /**
     * @return Returns the url.
     */
    public String getUrl() {
        return url;
    }
    /**
     * @param url The url to set.
     */
    public void setUrl(String url) {
        this.url = url;
    }
    /**
     * @return Returns the docDescription.
     */
    public String getDocDescription() {
        return docDescription;
    }
    /**
     * @param docDescription The docDescription to set.
     */
    public void setDocDescription(String docDescription) {
        this.docDescription = docDescription;
    }
    /**
     * @return Returns the isFile.
     */
    public boolean getIsFile() {
        return isFile;
    }
    /**
     * @param isFile The isFile to set.
     */
    public void setIsFile(boolean isFile) {
        this.isFile = isFile;
    }
    
    public boolean equals(Object obj) {
        if (obj == null) throw new NullPointerException();
        if (!(obj instanceof Documents)) throw new ClassCastException();
        
        Documents docs = (Documents) obj;
        return docId.equals(docs.getDocId());
        
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setFile(boolean isFile) {
        this.isFile = isFile;
    }

    public String getDocComment() {
        return docComment;
    }

    public void setDocComment(String docComment) {
        this.docComment = docComment;
    }

    public String getDocLanguage() {
        return docLanguage;
    }

    public void setDocLanguage(String docLanguage) {
        this.docLanguage = docLanguage;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }
    
    
}
