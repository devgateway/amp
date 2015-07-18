package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.helper.donorReport.OrgProfileValue;
import org.digijava.module.aim.helper.donorReport.ValueTranslatabePair;


public class AmpOrgRecipient implements Serializable,OrgProfileValue {
    private static final long serialVersionUID = 1L;
    //IATI check: not to be ignored
    @Interchangeable(fieldTitle="AMP Organization Recipient ID")
    private Long ampOrgRecipientId;
    @Interchangeable(fieldTitle="Organization", pickIdOnly=true, importable=true)
    private AmpOrganisation organization;
    @Interchangeable(fieldTitle="Parent Organization", pickIdOnly=true, importable=true)
    private AmpOrganisation parentOrganization;
    @Interchangeable(fieldTitle="Description", importable=true)
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getAmpOrgRecipientId() {
        return ampOrgRecipientId;
    }

    public void setAmpOrgRecipientId(Long id) {
        this.ampOrgRecipientId = id;
    }

    public AmpOrganisation getOrganization() {
        return organization;
    }

    public void setOrganization(AmpOrganisation organization) {
        this.organization = organization;
    }
 

    public AmpOrganisation getParentOrganization() {
        return parentOrganization;
    }

    public void setParentOrganization(AmpOrganisation parentOrganization) {
        this.parentOrganization = parentOrganization;
    }
    @Override
    public List<ValueTranslatabePair> getValuesForOrgReport(){
    	List<ValueTranslatabePair> values=new ArrayList<ValueTranslatabePair>();
    	values.add(new ValueTranslatabePair(Arrays.asList(new String[]{getOrganization().getName()}),false));
    	values.add(new ValueTranslatabePair(Arrays.asList(new String[]{(getDescription()==null)?"":getDescription()}),false));
    	return values;
    }

	@Override
	public String[] getSubHeaders() {
		String[] subHeaders = { "Name", "Description" };
		return subHeaders;
	}

}
