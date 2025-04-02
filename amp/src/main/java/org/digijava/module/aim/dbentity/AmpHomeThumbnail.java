package org.digijava.module.aim.dbentity;

public class AmpHomeThumbnail {

    private Long thumbnailId;
    
    private byte[] thumbnail;

    private int placeholder;
    
    private byte[] optionalFile;

    private String optionalFileName;

    private String optionalFileContentType;

    private String thumbnailContentType;

    private String thumbnailLabel;

    /**
     * @return the thumbnailId
     */
    public Long getThumbnailId() {
        return thumbnailId;
    }

    /**
     * @param thumbnailId the thumbnailId to set
     */
    public void setThumbnailId(Long thumbnailId) {
        this.thumbnailId = thumbnailId;
    }

    /**
     * @return the thumbnail
     */
    public byte[] getThumbnail() {
        return thumbnail;
    }

    /**
     * @param thumbnail the thumbnail to set
     */
    public void setThumbnail(byte[] thumbnail) {
        this.thumbnail = thumbnail;
    }

    /**
     * @return the placeholder
     */
    public int getPlaceholder() {
        return placeholder;
    }

    /**
     * @param placeholder the placeholder to set
     */
    public void setPlaceholder(int placeholder) {
        this.placeholder = placeholder;
    }

    /**
     * @return the optionalFile
     */
    public byte[] getOptionalFile() {
        return optionalFile;
    }

    /**
     * @param optionalFile the optionalFile to set
     */
    public void setOptionalFile(byte[] optionalFile) {
        this.optionalFile = optionalFile;
    }

    /**
     * @return the optionalFileName
     */
    public String getOptionalFileName() {
        return optionalFileName;
    }

    /**
     * @param optionalFileName the optionalFileName to set
     */
    public void setOptionalFileName(String optionalFileName) {
        this.optionalFileName = optionalFileName;
    }

    /**
     * @return the optionalFileContentType
     */
    public String getOptionalFileContentType() {
        return optionalFileContentType;
    }

    /**
     * @param optionalFileContentType the optionalFileContentType to set
     */
    public void setOptionalFileContentType(String optionalFileContentType) {
        this.optionalFileContentType = optionalFileContentType;
    }

    /**
     * @return the thumbnailContentType
     */
    public String getThumbnailContentType() {
        return thumbnailContentType;
    }

    /**
     * @param thumbnailContentType the thumbnailContentType to set
     */
    public void setThumbnailContentType(String thumbnailContentType) {
        this.thumbnailContentType = thumbnailContentType;
    }
    
    /**
     * @return the thumbnailLabel
     */
    public String getThumbnailLabel() {
        return thumbnailLabel;
    }

    /**
     * @param thumbnailLabel the thumbnailLabel to set
     */
    public void setThumbnailLabel(String thumbnailLabel) {
        this.thumbnailLabel = thumbnailLabel;
    }
    
}
