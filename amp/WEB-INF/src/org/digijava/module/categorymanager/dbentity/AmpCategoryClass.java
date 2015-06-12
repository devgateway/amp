/**
 * 
 */
package org.digijava.module.categorymanager.dbentity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.digijava.module.aim.annotations.interchange.Interchangeable;

/**
 * @author Alex Gartner
 *
 */
public class AmpCategoryClass implements Serializable, Comparable<AmpCategoryClass> {
	@Interchangeable(fieldTitle="ID")
	private Long id;
	@Interchangeable(fieldTitle="Name")
	private String name;
	@Interchangeable(fieldTitle="Description")
	private String description;
	@Interchangeable(fieldTitle="Key Name")
	private String keyName;
	private boolean isMultiselect	= false;
	private boolean isOrdered		= false;
	
	@Interchangeable(fieldTitle="Possible Values", recursive=true)
	private List<AmpCategoryValue> possibleValues;
	
	@Interchangeable(fieldTitle="Used Categories", recursive = true)
	private List<AmpCategoryClass> usedCategories;
	@Interchangeable(fieldTitle="Used By Categories", recursive=true)
	private Set<AmpCategoryClass> usedByCategories;
	
	@Interchangeable(fieldTitle="Used By Category Single Select")
	private Boolean usedByCategorySingleSelect; //how this category class is linked with current usedByCategory
	
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
	
	public List<AmpCategoryValue> getPossibleValues() {
		return possibleValues;
	}
	public void setPossibleValues(List<AmpCategoryValue> possibleValues) {
		if (this.possibleValues == null)
			this.possibleValues = possibleValues;
		else {
			this.possibleValues.clear();
			this.possibleValues.addAll(possibleValues);
		}
	}

	public String getKeyName() {
		return keyName;
	}
	public void setKeyName(String key) {
		this.keyName = key;
	}
	public List<AmpCategoryClass> getUsedCategories() {
		return usedCategories;
	}
	public void setUsedCategories(List<AmpCategoryClass> usedCategories) {
		this.usedCategories = usedCategories;
	}
	
	public Set<AmpCategoryClass> getUsedByCategories() {
		return usedByCategories;
	}
	public void setUsedByCategories(Set<AmpCategoryClass> usedByCategories) {
		this.usedByCategories = usedByCategories;
	}
	public int compareTo(AmpCategoryClass o) {
		return keyName.compareTo( o.getKeyName() );
	}
	
	@Override 
	public boolean equals(Object o) {
		if(o instanceof AmpCategoryClass)
		return compareTo( (AmpCategoryClass)o ) == 0;
		else return false;
	}
	
	public Boolean getUsedByCategorySingleSelect() {
		return usedByCategorySingleSelect;
	}
	public void setUsedByCategorySingleSelect(Boolean usedByCategorySingleSelect) {
		this.usedByCategorySingleSelect = usedByCategorySingleSelect;
	} 
		
}
