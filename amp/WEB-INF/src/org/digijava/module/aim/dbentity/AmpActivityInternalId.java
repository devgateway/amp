package org.digijava.module.aim.dbentity;

import java.io.Serializable;

public class AmpActivityInternalId implements Serializable {
    
    private AmpOrganisation organisation;
    private String internalId;
    private Long ampActivityId;
    private Long ampOrgId;

   
    
    public Long getAmpOrgId() {
		return ampOrgId;
	}

	public void setAmpOrgId(Long ampOrgId) {
		this.ampOrgId = ampOrgId;
	}

	public Long getAmpActivityId() {
        return ampActivityId;
    }

    public void setAmpActivityId(Long internalId) {
        this.ampActivityId = internalId;
    }
    
    /**
     * @return Returns the internalId.
     */
    public String getInternalId() {
        return internalId;
    }
    /**
     * @param internalId The internalId to set.
     */
    public void setInternalId(String internalId) {
        this.internalId = internalId;
    }
    /**
     * @return Returns the organisation.
     */
    public AmpOrganisation getOrganisation() {
        return organisation;
    }
    /**
     * @param organisation The organisation to set.
     */
    public void setOrganisation(AmpOrganisation organisation) {
        this.organisation = organisation;
    }
}

