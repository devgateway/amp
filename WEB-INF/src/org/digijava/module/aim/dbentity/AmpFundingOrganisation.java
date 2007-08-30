package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Set;

import org.digijava.kernel.dbentity.Country;
import org.digijava.module.aim.util.Identifiable;

public class AmpFundingOrganisation {

	private Long ampFundOrgId;
	private Long ampOrgId;
	private Long ampActivityId;
	
	
	private Boolean active;
	private Boolean delegatedCooperation;
	private Boolean delegatedPartner;
	
	
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	public Long getAmpFundOrgId() {
		return ampFundOrgId;
	}
	public void setAmpFundOrgId(Long ampFundOrgId) {
		this.ampFundOrgId = ampFundOrgId;
	}
	public Long getAmpOrgId() {
		return ampOrgId;
	}
	public void setAmpOrgId(Long ampOrgId) {
		this.ampOrgId = ampOrgId;
	}
	public Boolean getDelegatedCooperation() {
		return delegatedCooperation;
	}
	public void setDelegatedCooperation(Boolean delegatedCooperation) {
		this.delegatedCooperation = delegatedCooperation;
	}
	public Boolean getDelegatedPartner() {
		return delegatedPartner;
	}
	public void setDelegatedPartner(Boolean delegatedPartner) {
		this.delegatedPartner = delegatedPartner;
	}
	public Long getAmpActivityId() {
		return ampActivityId;
	}
	public void setAmpActivityId(Long ampActivityId) {
		this.ampActivityId = ampActivityId;
	}
	
	
}	
