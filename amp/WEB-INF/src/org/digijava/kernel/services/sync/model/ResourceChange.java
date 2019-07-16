package org.digijava.kernel.services.sync.model;

import java.util.Date;

/**
 * @author Viorel Chihai
 */
public class ResourceChange {

    private String uuid;
    private Date addingDate;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Date getAddingDate() {
        return addingDate;
    }

    public void setAddingDate(Date addingDate) {
        this.addingDate = addingDate;
    }

}
