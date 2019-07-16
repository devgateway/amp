

package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.digijava.module.aim.helper.donorReport.OrgProfileValue;
import org.digijava.module.aim.helper.donorReport.ValueTranslatabePair;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;


public class AmpOrgStaffInformation implements Serializable,OrgProfileValue{
    private static final long serialVersionUID = 1L;
    //IATI-check: to be ignored
    
//    @Interchangeable(fieldTitle="ID")
    private Long id;
//    @Interchangeable(fieldTitle="Year")
    private Long year;
//    @Interchangeable(fieldTitle="Staff Number")
    private Long staffNumber;
//    @Interchangeable(fieldTitle="Type")
    private AmpCategoryValue type;
//    @Interchangeable(fieldTitle="Organization")
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
