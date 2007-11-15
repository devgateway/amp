/*
 * RelOrganization.java
 */

package org.digijava.module.aim.helper;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrgType;

public class RelOrganization {
	private String orgName;
	private String role;
	private AmpOrgType orgTypeId;
	private AmpOrgGroup orgGrpId;
	private String acronym;
	private String orgCode;
	private Long orgId;

	
	public Long getOrgId() {
	    return orgId;
	}

	public void setOrgId(Long orgId) {
	    this.orgId = orgId;
	}

	public RelOrganization() {}
	
	/**
	 * @param orgName
	 * @param role
	 */
	public RelOrganization(String orgName, String role) {
		this.orgName = orgName;
		this.role = role;
	}
	/**
	 * @return Returns the orgName.
	 */
	public String getOrgName() {
		return orgName;
	}
	/**
	 * @param orgName The orgName to set.
	 */
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	/**
	 * @return Returns the role.
	 */
	public String getRole() {
		return role;
	}
	/**
	 * @param role The role to set.
	 */
	public void setRole(String role) {
		this.role = role;
	}
	
	public boolean equals(Object obj) {
		if (obj == null) throw new NullPointerException();
		if (!(obj instanceof RelOrganization)) throw new NullPointerException();
		
		RelOrganization relOrg = (RelOrganization) obj;
		return (relOrg.getOrgName().equals(orgName) && relOrg.getRole().equals(role));
	}

	public String getAcronym() {
		return acronym;
	}

	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public AmpOrgGroup getOrgGrpId() {
		return orgGrpId;
	}

	public void setOrgGrpId(AmpOrgGroup orgGrpId) {
		this.orgGrpId = orgGrpId;
	}

	public AmpOrgType getOrgTypeId() {
		return orgTypeId;
	}

	public void setOrgTypeId(AmpOrgType orgTypeId) {
		this.orgTypeId = orgTypeId;
	}
}