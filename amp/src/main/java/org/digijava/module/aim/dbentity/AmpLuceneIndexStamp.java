/*
* AMP GLOBAL SETTINGS 
*/
package org.digijava.module.aim.dbentity;

import java.io.Serializable;

public class AmpLuceneIndexStamp implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private Long idxId;
    private String idxName;
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
