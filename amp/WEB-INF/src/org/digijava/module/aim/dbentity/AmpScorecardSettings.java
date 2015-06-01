package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Set;

public class AmpScorecardSettings implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2696192720129725674L;
	private Long ampScorecardSettingsId;
	private Boolean validationPeriod;
	private Integer validationTime;
	private Double percentageThreshold;
	private Set<AmpScorecardSettingsCategoryValue> closedStatuses;
	
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
	public Set<AmpScorecardSettingsCategoryValue> getClosedStatuses() {
		return closedStatuses;
	}
	public void setClosedStatuses(Set<AmpScorecardSettingsCategoryValue> closedStatuses) {
		this.closedStatuses = closedStatuses;
	}
	public Double getPercentageThreshold() {
		return percentageThreshold;
	}
	public void setPercentageThreshold(Double percentageThreshold) {
		this.percentageThreshold = percentageThreshold;
	}

}
