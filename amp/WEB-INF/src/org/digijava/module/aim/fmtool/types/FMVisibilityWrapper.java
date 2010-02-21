package org.digijava.module.aim.fmtool.types;

import java.io.Serializable;

import org.digijava.module.aim.fmtool.util.FMToolConstants;

public class FMVisibilityWrapper implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id = null;
	private String name = null;	
	private Boolean hasLevel = null;
	private Long parentId = null;
	private String description = null;
	private String type = null;
	
	
	public FMVisibilityWrapper(){
	}
	
	public String getTableName(){
		return null;
	}

	public void setValues(Object[] values){
		this.setValues((Long)values[0], (String)values[1], (Boolean)values[2], (Long)values[3], (String)values[4], null);
	}

	public void setValues(Object[] values, String type){
		this.setValues((Long)values[0], (String)values[1], (Boolean)values[2], (Long)values[3], (String)values[4], type);
	}
	
	public void setValues(Long id, String name, Boolean hasLavel, Long parentId, String description, String type){
		this.setType(type);
		this.setId(id);
		this.setName(name);
		this.setHasLevel(hasLavel);
		this.setParentId(parentId);
		this.setDescription(description);
	}		

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Boolean getHasLevel() {
		return hasLevel;
	}
	public void setHasLevel(Boolean hasLevel) {
		this.hasLevel = hasLevel;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	
}
