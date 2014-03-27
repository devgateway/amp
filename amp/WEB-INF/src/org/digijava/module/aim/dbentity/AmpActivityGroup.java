package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Set;

import org.digijava.module.aim.util.Identifiable;

public class AmpActivityGroup implements Serializable, Identifiable{
	private static final long serialVersionUID = 1L;

	private Long ampActivityGroupId;
	private AmpActivityVersion ampActivityLastVersion;

	private boolean autoClosedOnExpiration = false;
	
	private Set<AmpActivityVersion> activities;

	public Long getAmpActivityGroupId() {
		return ampActivityGroupId;
	}

	public void setAmpActivityGroupId(Long ampActivityGroupId) {
		this.ampActivityGroupId = ampActivityGroupId;
	}

	public AmpActivityVersion getAmpActivityLastVersion() {
		return ampActivityLastVersion;
	}

	public void setAmpActivityLastVersion(AmpActivityVersion ampActivityLastVersion) {
		this.ampActivityLastVersion = ampActivityLastVersion;
	}

	public Set<AmpActivityVersion> getActivities() {
		return activities;
	}

	public void setActivities(Set<AmpActivityVersion> activities) {
		this.activities = activities;
	}
	
	public void setAutoClosedOnExpiration(boolean autoClosedOnExpiration)
	{
		this.autoClosedOnExpiration = autoClosedOnExpiration;
	}
	
	public boolean getAutoClosedOnExpiration()
	{
		return autoClosedOnExpiration;
	}
	
	@Override public Object getIdentifier(){
		return this.getAmpActivityGroupId();
	}
}