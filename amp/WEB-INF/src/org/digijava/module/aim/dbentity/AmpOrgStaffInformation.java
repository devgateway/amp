

package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.digijava.module.aim.helper.donorReport.OrgProfileValue;
import org.digijava.module.aim.helper.donorReport.ValueTranslatabePair;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import javax.persistence.*;

@Entity
@Table(name = "AMP_STAFF_INFO")
@Cacheable
public class AmpOrgStaffInformation implements Serializable,OrgProfileValue{
    private static final long serialVersionUID = 1L;
    //IATI-check: to be ignored

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AMP_STAFF_INFO_seq")
    @SequenceGenerator(name = "AMP_STAFF_INFO_seq", sequenceName = "AMP_STAFF_INFO_seq", allocationSize = 1)
    @Column(name = "amp_staff_info_id")
    private Long id;

    @Column(name = "year")
    private Long year;

    @ManyToOne
    @JoinColumn(name = "staff_type")
    private AmpCategoryValue type;

    @Column(name = "number_of_staff")
    private Long staffNumber;

    @ManyToOne
    @JoinColumn(name = "amp_org_id")
    private AmpOrganisation organization;

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
    @Override
    public List<ValueTranslatabePair> getValuesForOrgReport(){
        List<ValueTranslatabePair> values=new ArrayList<ValueTranslatabePair>();
        values.add(new ValueTranslatabePair(Arrays.asList(new String[]{getYear().toString()}),false));
        values.add(new ValueTranslatabePair(Arrays.asList(new String[]{getType().getValue()}),true));
        values.add(new ValueTranslatabePair(Arrays.asList(new String[]{getStaffNumber().toString()}),false));
        return values;
    }

    @Override
    public String[] getSubHeaders() {
        String[] subHeaders={"Year","Type of staff","Number of Staff"};
        return subHeaders;
    }
}
