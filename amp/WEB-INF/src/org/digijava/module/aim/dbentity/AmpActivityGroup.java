package org.digijava.module.aim.dbentity;

import java.util.Set;

public class AmpActivityGroup {

	private Long ampActivityGroupId;
	private AmpActivity ampActivityLastVersion;
	private Set<AmpActivityVersion> activities;

	public Long getAmpActivityGroupId() {
		return ampActivityGroupId;
	}

	public void setAmpActivityGroupId(Long ampActivityGroupId) {
		this.ampActivityGroupId = ampActivityGroupId;
	}

	public AmpActivity getAmpActivityLastVersion() {
		return ampActivityLastVersion;
	}

	public void setAmpActivityLastVersion(AmpActivity ampActivityLastVersion) {
		this.ampActivityLastVersion = ampActivityLastVersion;
	}

	public Set<AmpActivityVersion> getActivities() {
		return activities;
	}

	public void setActivities(Set<AmpActivityVersion> activities) {
		this.activities = activities;
	}	
}