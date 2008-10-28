package org.digijava.module.autopatcher.dbentity;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import org.dgfoundation.amp.PropertyListable;

/**
* AmpPatch.java
* TODO description here
* @author mihai
* @package org.digijava.module.autopatcher.dbentity
* @since 12.08.2007
 */
public class AmpPatch extends PropertyListable implements Serializable {
	private static final long serialVersionUID = 5759615519655548333L;
	
	private Long id;
	
	private String abstractLocation;
	private String name;
	private String featureAdded;
	private Timestamp discovered;
	private Timestamp lastInvoked;
	private String md5;
	private boolean pendingExec;
	
	private List logs;
	
	public Boolean isSuccessful() {
		if(logs==null || logs.size()==0) return null;
		AmpPatchLog last = (AmpPatchLog) logs.get(logs.size()-1);
		if(last.getSuccessful()==true) return new Boolean(true);
		return false;
	}
	
	public AmpPatch() {
		this.pendingExec=true;
	}
	
	

	public String getAbstractLocation() {
		return abstractLocation;
	}

	public void setAbstractLocation(String abstractFileName) {
		this.abstractLocation = abstractFileName;
	}

	public Timestamp getDiscovered() {
		return discovered;
	}

	public void setDiscovered(Timestamp discovered) {
		this.discovered = discovered;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Timestamp getLastInvoked() {
		return lastInvoked;
	}

	public void setLastInvoked(Timestamp lastInvoked) {
		this.lastInvoked = lastInvoked;
	}

	@PropertyListableIgnore
	public List getLogs() {
		return logs;
	}

	public void setLogs(List logs) {
		this.logs = logs;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String lastMD5) {
		this.md5 = lastMD5;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isPendingExec() {
		return pendingExec;
	}

	public void setPendingExec(boolean pendingExec) {
		this.pendingExec = pendingExec;
	}

	public String getFeatureAdded() {
		return featureAdded;
	}

	public void setFeatureAdded(String featureAdded) {
		this.featureAdded = featureAdded;
	}

	@Override
	public String getBeanName() {
	   return name;
	}
	
	
	
}
