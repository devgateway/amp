

package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;


public class AmpOrgStaffInformation implements Serializable{
    private static final long serialVersionUID = 1L;
    private Long id;
    private Long year;
    private Long staffNumber;
    private AmpCategoryValue type;
    private AmpOrganisation organization;
    // helper field, not for saving in db..
    private boolean newlyCreated;

    public boolean isNewlyCreated() {
        return newlyCreated;
    }

    public void setNewlyCreated(boolean newlyCreated) {
        this.newlyCreated = newlyCreated;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AmpOrganisation getOrganization() {
        return organization;
    }

    public void setOrganization(AmpOrganisation organization) {
        this.organization = organization;
    }

    public Long getStaffNumber() {
        return staffNumber;
    }

    public void setStaffNumber(Long staffNumber) {
        this.staffNumber = staffNumber;
    }

    public AmpCategoryValue getType() {
        return type;
    }

    public void setType(AmpCategoryValue type) {
        this.type = type;
    }

    public Long getYear() {
        return year;
    }

    public void setYear(Long year) {
        this.year = year;
    }
}
