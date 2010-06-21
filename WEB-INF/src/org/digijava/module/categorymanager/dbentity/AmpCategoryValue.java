package org.digijava.module.categorymanager.dbentity;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.util.HierarchyListable;
import org.digijava.module.aim.util.Identifiable;
import org.digijava.module.categorymanager.util.CategoryConstants;
/**
 * Represents one of the possible values for a certain category
 * @author Alex Gartner
 *
 */
public class AmpCategoryValue implements Serializable, Identifiable, Comparable<AmpCategoryValue>, HierarchyListable {
	private Long id;
	private AmpCategoryClass ampCategoryClass;
	private String value;
	private Integer index;
	private Set<AmpActivity> activities;
	//private Long fieldType;
	
	private Set<AmpCategoryValue> usedValues;
	private Set<AmpCategoryValue> usedByValues;

	/*use for only category with category key "implementation_location" 
         to show which field is used for country  */

	//Created because of an error with some strings with french simbols as ID.
	public String getEncodedValue(){
		String value = "";
		for(int i=0;i<this.value.length();i++) {
			if(this.value.charAt(i)>='A' && this.value.charAt(i) <= 'z'){
				value = value + this.value.charAt(i);
			}
		}
		//value = URLEncoder.encode(this.value,"");
		return value;
	}

	public Set getActivities() {
		return activities;
	}
	public void setActivities(Set activities) {
		this.activities = activities;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public AmpCategoryClass getAmpCategoryClass() {
		return ampCategoryClass;
	}
	public void setAmpCategoryClass(AmpCategoryClass ampCategoryClass) {
		this.ampCategoryClass = ampCategoryClass;
	}

	public int getIndex()
	{
		return index;
	}

	public void setIndex(int index)
	{
		this.index	= index;
		// not used, calculated value, see getIndex() method
	}

	public String toString() {
		return value;
	}
	public Object getIdentifier() {
		return this.getId();
	}
	public int compareTo(AmpCategoryValue a) {
		// TODO Auto-generated method stub
		return this.getId().compareTo(a.getId());
	}

	public boolean equals (Object o) {
		AmpCategoryValue a = (AmpCategoryValue) o;
		if ( a == null )
			return false;
		return this.getId().equals( a.getId() );
	}

	public Set<AmpCategoryValue> getUsedValues() {
		return usedValues;
	}

	public void setUsedValues(Set<AmpCategoryValue> usedValues) {
		this.usedValues = usedValues;
	}

	public Set<AmpCategoryValue> getUsedByValues() {
		return usedByValues;
	}

	public void setUsedByValues(Set<AmpCategoryValue> usedByValues) {
		this.usedByValues = usedByValues;
	}

	@Override
	public Collection<? extends HierarchyListable> getChildren() {
		return null;
	}
	
	@Override
	public int getCountDescendants() {
		return 1;
	}
	
	@Override
	public String getLabel() {
		return this.value;
	}
	
	@Override
	public String getUniqueId() {
		return id+"";
	}
}
