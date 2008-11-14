package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Set;

import org.digijava.module.aim.util.Identifiable;
import org.digijava.module.categorymanager.util.CategoryConstants;
/**
 * Represents one of the possible values for a certain category
 * @author Alex Gartner
 *
 */
public class AmpCategoryValue implements Serializable, Identifiable, Comparable {
	private Long id;
	private AmpCategoryClass ampCategoryClass;
	private String value;
	private int index;
        private Set activities;
        private long fieldType;

        public long getFieldType() {
            return fieldType;
        }

        public void setFieldType(long fieldType) {
            this.fieldType = fieldType;
        }
        
        /*use for only category with category key "implementation_location" 
         to show which field is used for country  */
        
       public boolean isCountry() {
        boolean isCoutry = false;
        if (fieldType == CategoryConstants.COUNTRY_TYPE) {
            isCoutry = true;
        }
        return isCoutry;

    }

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
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		AmpCategoryValue a = (AmpCategoryValue) o;
		return this.getId().compareTo(a.getId());
	}
	
	public boolean equals (Object o) {
		AmpCategoryValue a = (AmpCategoryValue) o;
		return this.getId().equals( a.getId() );
	}
		
}
