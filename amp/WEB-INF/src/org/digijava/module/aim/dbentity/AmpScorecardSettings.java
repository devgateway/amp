package org.digijava.module.aim.dbentity;

import java.io.Serializable;

public class AmpScorecardSettings implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2696192720129725674L;
	private Long ampScorecardSettingsId;
	private Boolean validationPeriod;
	private Integer validationTime;
	
	public Long getAmpScorecardSettingsId() {
		return ampScorecardSettingsId;
	}
	public void setAmpScorecardSettingsId(Long ampScorecardSettingsId) {
		this.ampScorecardSettingsId = ampScorecardSettingsId;
	}
	public Boolean getValidationPeriod() {
		return validationPeriod;
	}
	public void setValidationPeriod(Boolean validationPeriod) {
		this.validationPeriod = validationPeriod;
	}
	public Integer getValidationTime() {
		return validationTime;
	}
	public void setValidationTime(Integer validationTime) {
		this.validationTime = validationTime;
	}
	


}
