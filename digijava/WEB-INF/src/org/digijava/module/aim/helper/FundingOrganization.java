package org.digijava.module.aim.helper;

import java.io.Serializable;
import java.util.Collection;

import org.digijava.module.aim.dbentity.AmpOrganisation;

/**
 * @author jose
 */
public class FundingOrganization implements Serializable 
{
	
	private Long ampOrgId;
	private String orgName ;
	//Collection of Funding objects
	private Collection fundings;
	private String currentOrganization; 
	
	public Long getAmpOrgId() {
		return ampOrgId;
	}
	
	public void setAmpOrgId(Long ampOrgId) {
		this.ampOrgId = ampOrgId;
	}
	
	public String getOrgName() {
		return orgName;
	}
	
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	
	public Collection getFundings() {
		return fundings;
	}
	
	public void setFundings(Collection fundings) {
		this.fundings = fundings;
	}
    public String getCurrentOrganization() {
        return currentOrganization;
    }
    public void setCurrentOrganization(String currentOrganization) {
        this.currentOrganization = currentOrganization;
    }
}
