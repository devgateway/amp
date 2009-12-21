package org.digijava.module.aim.dbentity;

import java.util.Set;

public class AmpActivityGroup {

	private Long ampActivityGroupId;
	private AmpActivity ampActivityLastVersion;
	private Set<AmpActivity> activities;

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

	public Set<AmpActivity> getActivities() {
		return activities;
	}

	public void setActivities(Set<AmpActivity> activities) {
		this.activities = activities;
	}
}