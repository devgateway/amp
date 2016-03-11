package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class AmpDataFreezeSettings implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 2203566029781790548L;
	private Long ampDataFreezeSettingsId;
	/**
	 * if true feature enabled
	 */
	private Boolean enabled = Boolean.FALSE;
	private Integer gracePeriod;

	public Long getAmpDataFreezeSettingsId() {
		return ampDataFreezeSettingsId;
	}
	public void setAmpDataFreezeSettingsId(Long ampDataFreezeSettingsId) {
		this.ampDataFreezeSettingsId = ampDataFreezeSettingsId;
	}
	public Boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	public Integer getGracePeriod() {
		return gracePeriod;
	}
	public void setGracePeriod(Integer gracePeriod) {
		this.gracePeriod = gracePeriod;
	}
	
}
