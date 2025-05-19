package org.digijava.module.aim.dbentity;

import java.io.Serializable;

public class AmpProjectThumbnail implements Serializable, Cloneable, Comparable<AmpProjectThumbnail> {


    private Long id;

    private byte[] imgFile;

    private String imgFileName;

    private String contentType;

    private Long creationTime;


    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getImgFile() {
        return imgFile;
    }
    public void setImgFile(byte[] imgFile) {
        this.imgFile = imgFile;
    }
    public String getImgFileName() {
        return imgFileName;
    }
    public void setImgFileName(String imgFileName) {
        this.imgFileName = imgFileName;
    }
    public String getContentType() {
        return contentType;
    }
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
    public Long getCreationTime() {
        return creationTime;
    }
    public void setCreationTime(Long creationTime) {
        this.creationTime = creationTime;
    }
    @Override
    public int compareTo(AmpProjectThumbnail o) {
        if(this.creationTime != null && o.creationTime != null){
            return o.creationTime.compareTo(this.creationTime);
        }else{
            return this.imgFileName.compareTo(o.imgFileName);
        }
    }
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

