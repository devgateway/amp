package org.digijava.kernel.services.sync.model;

import java.util.Date;

/**
 * @author Octavian Ciubotaru
 */
public class ActivityChange {

    private String ampId;
    private Date modifiedDate;
    private Boolean deleted;

    public String getAmpId() {
        return ampId;
    }

    public void setAmpId(String ampId) {
        this.ampId = ampId;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
}
