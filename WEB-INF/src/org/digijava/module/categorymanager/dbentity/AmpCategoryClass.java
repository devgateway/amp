/**
 * 
 */
package org.digijava.module.categorymanager.dbentity;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * @author Alex Gartner
 *
 */
public class AmpCategoryClass implements Serializable {
	private Long id;
	private String name;
	private String description;
	private String keyName;
	private boolean isMultiselect	= false;
	private boolean isOrdered		= false;
	private List possibleValues;
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public boolean getisMultiselect() {
		return isMultiselect;
	}
	public boolean isMultiselect() {
		return isMultiselect;
	}
	public void setIsMultiselect(boolean isMultiselect) {
		this.isMultiselect = isMultiselect;
	}
	public boolean getIsOrdered() {
		return isOrdered;
	}
	public boolean isOrdered() {
		return isOrdered;
	}
	public void setIsOrdered(boolean isOrdered) {
		this.isOrdered = isOrdered;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List getPossibleValues() {
		return possibleValues;
	}
	public void setPossibleValues(List possibleValues) {
		this.possibleValues = possibleValues;
	}
	public String getKeyName() {
		return keyName;
	}
	public void setKeyName(String key) {
		this.keyName = key;
	}
		
	
}
