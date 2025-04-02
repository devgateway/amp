
package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.digijava.module.aim.helper.donorReport.OrgProfileValue;
import org.digijava.module.aim.helper.donorReport.ValueTranslatabePair;


public class AmpOrgLocation implements Serializable,OrgProfileValue {
    private static final long serialVersionUID = 1L;
    //IATI-check: to be ignored
//    @Interchangeable(fieldTitle="AMP Organization Location ID")
    private Long ampOrgLocId;
//    @Interchangeable(fieldTitle="Percent")
    private Double percent;
//    @Interchangeable(fieldTitle="Organization")
    private AmpOrganisation organization;
//    @Interchangeable(fieldTitle="Location"/*, descend = true*/)
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

    @Override
    public List<ValueTranslatabePair> getValuesForOrgReport() {
        List<ValueTranslatabePair> values = new ArrayList<ValueTranslatabePair>();
        values.add(new ValueTranslatabePair(Arrays.asList(new String[]{getLocation().getName()}), false));
        values.add(new ValueTranslatabePair(Arrays.asList(new String[]{((getPercent() == null) ? "0%" : getPercent() + "%")}), false));
        return values;
    }

    @Override
    public String[] getSubHeaders() {
        String[] subHeaders = { "Name", "Percent" };
        return subHeaders;
    }

}
