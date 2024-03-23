package org.digijava.module.aim.dbentity;

import org.apache.log4j.Logger;

import java.io.Serializable;

/**
 * Persistent class for Structure Images
 * @author mmoras
 */
public class AmpStructureImg implements Serializable, Cloneable, Comparable<AmpStructureImg> {
    private static Logger logger = Logger.getLogger(AmpStructureImg.class);
    
    
    private Long id;
    
    private byte[] imgFile;

    private String imgFileName;
    
    private String contentType;

    private Long creationTime;
    
    private AmpStructure structure;
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public AmpStructure getStructure() {
        return structure;
    }
    public void setStructure(AmpStructure structure) {
        this.structure = structure;
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
    public int compareTo(AmpStructureImg o) {
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
