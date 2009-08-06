
package org.digijava.module.aim.dbentity;

import java.io.Serializable;


public class AmpOrgLocation implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long ampOrgLocId;
    private Double percent;
    private AmpOrganisation organization;
    private AmpCategoryValueLocations location;

    public Long getAmpOrgLocId() {
        return ampOrgLocId;
    }

    public void setAmpOrgLocId(Long ampOrgLocId) {
        this.ampOrgLocId = ampOrgLocId;
    }

    public AmpCategoryValueLocations getLocation() {
        return location;
    }

    public void setLocation(AmpCategoryValueLocations location) {
        this.location = location;
    }

    public AmpOrganisation getOrganization() {
        return organization;
    }

    public void setOrganization(AmpOrganisation organization) {
        this.organization = organization;
    }

    public Double getPercent() {
        return percent;
    }

    public void setPercent(Double percent) {
        this.percent = percent;
    }

}
