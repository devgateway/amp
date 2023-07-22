package org.digijava.module.aim.dbentity;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Persistent class for Structure Images
 * @author mmoras
 */
import javax.persistence.*;

@Entity
@Table(name = "AMP_STRUCTURE_IMG")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)

public class AmpStructureImg implements Serializable, Cloneable, Comparable<AmpStructureImg> {
    private static Logger logger = Logger.getLogger(AmpStructureImg.class);


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AMP_STRUCTURE_IMG_seq")
    @SequenceGenerator(name = "AMP_STRUCTURE_IMG_seq", sequenceName = "AMP_STRUCTURE_IMG_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "img_file_name")
    private String imgFileName;

    @Column(name = "content_type")
    private String contentType;

    @Lob
    @Column(name = "img_file")
    private byte[] imgFile;

    @Column(name = "creation_time")
    private Long creationTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "amp_structure_id", nullable = false)
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
