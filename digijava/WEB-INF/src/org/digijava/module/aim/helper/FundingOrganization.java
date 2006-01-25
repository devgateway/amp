package org.digijava.module.aim.helper;

import java.util.Collection;

/**
 * @author jose
 */
public class FundingOrganization {
	
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
    
    public boolean equals(Object e) {
    	if (e instanceof FundingOrganization) {
    		FundingOrganization forg = (FundingOrganization) e;
    		return ampOrgId.longValue() == forg.getAmpOrgId().longValue();
    	}
    	throw new ClassCastException();
    }
}
