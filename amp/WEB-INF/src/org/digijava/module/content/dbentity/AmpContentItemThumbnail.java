package org.digijava.module.content.dbentity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.dgfoundation.amp.ar.cell.AmountCell;

public class AmpContentItemThumbnail implements Comparable {

    private Long ampContentItemThumbnailId;

    private AmpContentItem contentItem;

    private byte[] thumbnail;

    private int placeholder;

    private byte[] optionalFile;

    private String optionalFileName;

    private String optionalFileContentType;

    private String thumbnailContentType;

    private String thumbnailLabel;

    public void setContentItem(AmpContentItem contentItem) {
        this.contentItem = contentItem;
    }
    public AmpContentItem getContentItem() {
        return contentItem;
    }

    /**
     * @return the thumbnailId
     */
    public Long getAmpContentItemThumbnailId() {
        return ampContentItemThumbnailId;
    }

    /**
     * @param thumbnailId
     *            the thumbnailId to set
     */
    public void setAmpContentItemThumbnailId(Long ampContentItemThumbnailId) {
        this.ampContentItemThumbnailId = ampContentItemThumbnailId;
    }

    /**
     * @return the thumbnail
     */
    public byte[] getThumbnail() {
        return thumbnail;
    }

    /**
     * @param thumbnail
     *            the thumbnail to set
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
     * @param placeholder
     *            the placeholder to set
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
     * @param optionalFile
     *            the optionalFile to set
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
     * @param optionalFileName
     *            the optionalFileName to set
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
     * @param optionalFileContentType
     *            the optionalFileContentType to set
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
     * @param thumbnailContentType
     *            the thumbnailContentType to set
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
     * @param thumbnailLabel
     *            the thumbnailLabel to set
     */
    public void setThumbnailLabel(String thumbnailLabel) {
        this.thumbnailLabel = thumbnailLabel;
    }
    @Override
    public int compareTo(Object o) {
        AmpContentItemThumbnail acit = (AmpContentItemThumbnail) o;
        
        return Integer.valueOf(this.getPlaceholder()).compareTo(Integer.valueOf(acit.getPlaceholder()));
    }

}
