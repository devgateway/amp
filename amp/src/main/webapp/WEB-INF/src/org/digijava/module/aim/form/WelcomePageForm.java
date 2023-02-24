package org.digijava.module.aim.form;

import java.util.Collection;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;
import org.digijava.module.aim.dbentity.AmpHomeThumbnail;

public class WelcomePageForm extends ActionForm{
    
    private FormFile thumbnail = null;
    
    private FormFile optionalFile = null;
    
    private String optionalFileName = null;
    
    private String thumbnailLabel = null;
    
    private int placeholder;
    
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
     * @return the thumbnail
     */
    public FormFile getThumbnail() {
        return thumbnail;
    }

    /**
     * @param thumbnail the thumbnail to set
     */
    public void setThumbnail(FormFile thumbnail) {
        this.thumbnail = thumbnail;
    }

    /**
     * @return the optionalFile
     */
    public FormFile getOptionalFile() {
        return optionalFile;
    }

    /**
     * @param optionalFile the optionalFile to set
     */
    public void setOptionalFile(FormFile optionalFile) {
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

    private AmpHomeThumbnail thumbnailPlace1;
    
    private AmpHomeThumbnail thumbnailPlace2;

    /**
     * @return the thumbnailPlace1
     */
    public AmpHomeThumbnail getThumbnailPlace1() {
        return thumbnailPlace1;
    }

    /**
     * @param thumbnailPlace1 the thumbnailPlace1 to set
     */
    public void setThumbnailPlace1(AmpHomeThumbnail thumbnailPlace1) {
        this.thumbnailPlace1 = thumbnailPlace1;
    }

    /**
     * @return the thumbnailPlace2
     */
    public AmpHomeThumbnail getThumbnailPlace2() {
        return thumbnailPlace2;
    }

    /**
     * @param thumbnailPlace2 the thumbnailPlace2 to set
     */
    public void setThumbnailPlace2(AmpHomeThumbnail thumbnailPlace2) {
        this.thumbnailPlace2 = thumbnailPlace2;
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
