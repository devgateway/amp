package org.digijava.module.aim.fmtool.types;

import java.io.Serializable;

import org.digijava.module.aim.fmtool.util.FMToolConstants;

public class FMDuplicatesWrapper implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String name = null;
	
	private Long count = null;
	
	private String type = null;	
	
	public FMDuplicatesWrapper(){
		
	}

	public FMDuplicatesWrapper(String name, Long count, String type){
		
		String dbType = type.trim(); 
		if (dbType.equalsIgnoreCase(FMToolConstants.FEATURE_TYPE_MODULE) 
				|| dbType.equalsIgnoreCase(FMToolConstants.FEATURE_TYPE_FEATURE)
				||dbType.equalsIgnoreCase(FMToolConstants.FEATURE_TYPE_FIELD)){
			this.setType(dbType);
		} else {
			throw new RuntimeException("Unknown duplicates type");
		}
		
		this.setName(name);
		this.setCount(count);
	}
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
	

}
