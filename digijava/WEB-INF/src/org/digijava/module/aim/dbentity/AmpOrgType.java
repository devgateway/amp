
package org.digijava.module.aim.dbentity;

import java.io.Serializable;

public class AmpOrgType implements Serializable{
	
	private Long ampOrgTypeId;
	private String orgType;
	private String orgTypeCode;
	//private Set organizations;
	
	/**
	 * @return Returns the ampOrgTypeId.
	 */
	public Long getAmpOrgTypeId() {
		return ampOrgTypeId;
	}
	/**
	 * @param ampOrgTypeId The ampOrgTypeId to set.
	 */
	public void setAmpOrgTypeId(Long ampOrgTypeId) {
		this.ampOrgTypeId = ampOrgTypeId;
	}
	/**
	 * @return Returns the orgType.
	 */
	public String getOrgType() {
		return orgType;
	}
	/**
	 * @param orgType The orgType to set.
	 */
	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}
	/**
	 * @return Returns the orgTypeCode.
	 */
	public String getOrgTypeCode() {
		return orgTypeCode;
	}
	/**
	 * @param orgTypeCode The orgTypeCode to set.
	 */
	public void setOrgTypeCode(String orgTypeCode) {
		this.orgTypeCode = orgTypeCode;
	}

}
