/*
* AMP GLOBAL SETTINGS 
*/
package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "AMP_LUCENE_INDEX")
public class AmpLuceneIndexStamp implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AMP_LUCENE_INDEX_seq")
    @SequenceGenerator(name = "AMP_LUCENE_INDEX_seq", sequenceName = "AMP_LUCENE_INDEX_seq", allocationSize = 1)
    @Column(name = "id")
    private Long idxId;

    @Column(name = "idxName")
    private String idxName;

    @Column(name = "stamp")
    private Long stamp;


    public String getIdxName() {
        return idxName;
    }
    public void setIdxName(String idxName) {
        this.idxName = idxName;
    }
    public Long getStamp() {
        return stamp;
    }
    public void setStamp(Long stamp) {
        this.stamp = stamp;
    }
    public Long getIdxId() {
        return idxId;
    }
    public void setIdxId(Long idxId) {
        this.idxId = idxId;
    }
    
    
}
