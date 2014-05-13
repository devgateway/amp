package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Set;

public class AmpActivityGroupCached implements Serializable {

	private AmpActivityVersion ampActivityLastVersion;
	private AmpActivityGroup ampActivityGroup;
	private boolean autoClosedOnExpiration = false;
	
	public AmpActivityVersion getAmpActivityLastVersion() {
		return ampActivityLastVersion;
	}
	public void setAmpActivityLastVersion(AmpActivityVersion ampActivityLastVersion) {
		this.ampActivityLastVersion = ampActivityLastVersion;
	}
	public AmpActivityGroup getAmpActivityGroup() {
		return ampActivityGroup;
	}
	public void setAmpActivityGroup(AmpActivityGroup ampActivityGroup) {
		this.ampActivityGroup = ampActivityGroup;
	}
	
	@Override
	public boolean equals(Object obj) {
		AmpActivityGroupCached c=(AmpActivityGroupCached) obj;
		if(this.getAmpActivityGroup().getAmpActivityGroupId().equals(c.getAmpActivityGroup().getAmpActivityGroupId()) && 
				this.getAmpActivityLastVersion().getAmpActivityId().equals(c.getAmpActivityLastVersion().getAmpActivityId())) return true;
		return false;		
	}
	
	@Override
	public int hashCode() {
		return 29*this.getAmpActivityGroup().hashCode()+this.getAmpActivityLastVersion().hashCode();
	}
	
	public void setAutoClosedOnExpiration(boolean autoClosedOnExpiration)
	{
		this.autoClosedOnExpiration = autoClosedOnExpiration;
	}
	
	public boolean getAutoClosedOnExpiration()
	{
		return autoClosedOnExpiration;
	}
	
}