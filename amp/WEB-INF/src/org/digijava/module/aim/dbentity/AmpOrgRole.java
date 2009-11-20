package org.digijava.module.aim.dbentity ;

import java.io.Serializable;


public class AmpOrgRole implements Serializable
{
    private Long ampOrgRoleId;
    private AmpActivity activity;
	private AmpOrganisation organisation;
	private AmpRole role;
	private Double 	percentage;
	private String additionalInfo;
	
    public Double getPercentage() {
		return percentage;
	}
	public void setPercentage(Double percentage) {
		this.percentage = percentage;
	}
	/**
     * @return Returns the activity.
     */
    public AmpActivity getActivity() {
        return activity;
    }
    /**
     * @param activity The activity to set.
     */
    public void setActivity(AmpActivity activity) {
        this.activity = activity;
    }
    /**
     * @return Returns the ampOrgRoleId.
     */
    public Long getAmpOrgRoleId() {
        return ampOrgRoleId;
    }
    /**
     * @param ampOrgRoleId The ampOrgRoleId to set.
     */
    public void setAmpOrgRoleId(Long ampOrgRoleId) {
        this.ampOrgRoleId = ampOrgRoleId;
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
    /**
     * @return Returns the role.
     */
    public AmpRole getRole() {
        return role;
    }
    /**
     * @param role The role to set.
     */
    public void setRole(AmpRole role) {
        this.role = role;
    }
    
        
	/**
	 * @return the additionalInfo
	 */
	public String getAdditionalInfo() {
		return additionalInfo;
	}
	/**
	 * @param additionalInfo the additionalInfo to set
	 */
	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}
	public boolean equals(Object obj) {
		if (obj == null)
			throw new NullPointerException();
		
		if (!(obj instanceof AmpOrgRole))
			throw new ClassCastException();
		AmpOrgRole orgRole = (AmpOrgRole) obj;
		if (activity==null){
			//this is a new role added to an unsaved activity
			return (orgRole.getAmpOrgRoleId().equals(this.ampOrgRoleId));
		}else{
		return (orgRole.getActivity().getAmpActivityId().equals(activity.getAmpActivityId()) &&orgRole.getOrganisation().getAmpOrgId().equals(organisation.getAmpOrgId()) && orgRole.getRole().getAmpRoleId().equals(role.getAmpRoleId()));
		}
		}
}	
