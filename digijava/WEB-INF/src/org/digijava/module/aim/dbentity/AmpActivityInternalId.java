package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class AmpActivityInternalId implements Serializable {
	private Long ampActivityId;
	private Long ampOrgId;
	private String internalId;


	public boolean equals(Object other) {
		boolean actFlag = false;
		boolean orgFlag = false;
		boolean intIdFlag = false;
		
		if (other == null) return false;
		
		if (! (other instanceof AmpActivityInternalId))
			return false;

		AmpActivityInternalId intId = (AmpActivityInternalId) other;
		if (ampActivityId == null && intId.getAmpActivityId() == null) {
			actFlag = true;
		} else if (ampActivityId == null || intId.getAmpActivityId() == null) {
			actFlag = false;
		} else {
			actFlag = (ampActivityId.equals(intId.getAmpActivityId())) ? true : false;
		}

		if (ampOrgId == null && intId.getAmpOrgId() == null) {
			orgFlag = true;
		} else if (ampOrgId == null || intId.getAmpOrgId() == null) {
			orgFlag = false;
		} else {
			orgFlag = (ampOrgId.equals(intId.getAmpOrgId())) ? true : false;
		}
		
		if (internalId == null && intId.getInternalId() == null) {
			intIdFlag = true;
		} else if (internalId == null || intId.getInternalId() == null) {
			intIdFlag = false;
		} else {
			intIdFlag = (internalId.equals(intId.getInternalId())) ? true : false;
		}		

		return (actFlag & orgFlag & intIdFlag);
	}	

	public int hashCode() {
		if (ampActivityId != null && ampOrgId != null) {
			return new HashCodeBuilder().append(
					this.getAmpActivityId().toString()).append(
					this.getAmpOrgId().toString()).toHashCode();
		} else {
			return new HashCodeBuilder().toHashCode();
		}
	}

	/**
	 * @return
	 */
	public Long getAmpActivityId() {
		return ampActivityId;
	}

	/**
	 * @return
	 */
	public Long getAmpOrgId() {
		return ampOrgId;
	}

	/**
	 * @return
	 */
	public String getInternalId() {
		return internalId;
	}

	/**
	 * @param long1
	 */
	public void setAmpActivityId(Long long1) {
		ampActivityId = long1;
	}

	/**
	 * @param long1
	 */
	public void setAmpOrgId(Long long1) {
		ampOrgId = long1;
	}

	/**
	 * @param string
	 */
	public void setInternalId(String string) {
		internalId = string;
	}

}

