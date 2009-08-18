package org.digijava.module.aim.fmtool.dbentity;

import java.io.Serializable;

public class AmpFeatureSource implements  Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long ampFeatureSourceId = null;
	private String name = null;

	public AmpFeatureSource(){
		
	}

	public AmpFeatureSource(String name){
		this.name = name;
	}
	
	public Long getAmpFeatureSourceId() {
		return ampFeatureSourceId;
	}

	public void setAmpFeatureSourceId(Long ampFeatureSourceId) {
		this.ampFeatureSourceId = ampFeatureSourceId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	
}
