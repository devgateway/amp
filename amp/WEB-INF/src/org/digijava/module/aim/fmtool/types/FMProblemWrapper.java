package org.digijava.module.aim.fmtool.types;

import java.io.Serializable;

public class FMProblemWrapper implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String name = null;
	private String type = null;
	private String problemName = null;
	private Long featureId = null;
	
	public FMProblemWrapper(){
		
	}
	
	public FMProblemWrapper(Long featureId, String name, String type, String problemName){
		this.name = name;
		this.type = type;
		this.problemName = problemName;
		this.featureId = featureId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getProblemName() {
		return problemName;
	}

	public void setProblemName(String problemName) {
		this.problemName = problemName;
	}

	public Long getFeatureId() {
		return featureId;
	}

	public void setFeatureId(Long featureId) {
		this.featureId = featureId;
	}
	
	
}
