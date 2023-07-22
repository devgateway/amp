
package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.digijava.module.aim.helper.donorReport.OrgProfileValue;
import org.digijava.module.aim.helper.donorReport.ValueTranslatabePair;

import javax.persistence.*;

@Entity
@Table(name = "AMP_ORG_LOCATION")
@Cacheable
public class AmpOrgLocation implements Serializable,OrgProfileValue {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AMP_ORG_LOCATION_seq")
    @SequenceGenerator(name = "AMP_ORG_LOCATION_seq", sequenceName = "AMP_ORG_LOCATION_seq", allocationSize = 1)    @Column(name = "amp_org_loc_id")
    private Long ampOrgLocId;

    @Column(name = "percent")
    private Double percent;

    @ManyToOne
    @JoinColumn(name = "amp_org_id")
    private AmpOrganisation organization;

    @ManyToOne
    @JoinColumn(name = "id")
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
