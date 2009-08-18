package org.digijava.module.aim.fmtool.types;

import java.io.Serializable;

import org.digijava.module.aim.fmtool.util.FMToolConstants;

public class FMSourceWrapper  implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long id = null;
	private String type = null;
	private String name = null;
	private Long featureId = null;
	
	public FMSourceWrapper(){
		
	}
	
	public FMSourceWrapper(Object[] values){
		this.setId((Long)values[0]);
		
		String dbType = ((String)values[1]).trim(); 
		if (dbType.equalsIgnoreCase(FMToolConstants.FEATURE_TYPE_MODULE) 
				|| dbType.equalsIgnoreCase(FMToolConstants.FEATURE_TYPE_FEATURE)
				||dbType.equalsIgnoreCase(FMToolConstants.FEATURE_TYPE_FIELD)){
			this.setType(dbType);
		} else {
			throw new RuntimeException("Unknown Source type");
		}
		
		this.setName((String)values[2]);
		this.setFeatureId((Long)values[3]);
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getFeatureId() {
		return featureId;
	}
	public void setFeatureId(Long featureId) {
		this.featureId = featureId;
	}

}
