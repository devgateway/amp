package org.digijava.module.categorymanager.form;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.categorymanager.util.PossibleValue;

/**
 * 
 * @author Alex Gartner
 *
 */
public class CategoryManagerForm extends ActionForm {
	private Collection 	categories;
	
	/**
	 * Begin - The properties below are used for adding a new category
	 */
	private Boolean addNewCategory	= null;
	private String categoryName		= null;
	private String keyName		= null;
	private String description		= null;
	private String [] possibleValues	= null;
	private Integer numOfPossibleValues	= null;
	private boolean isMultiselect	= false;
	private boolean isOrdered		= false;
    private List<PossibleValue> possibleVals	= new ArrayList<PossibleValue>();
    
    private int numOfAdditionalFields	= 0;
	/**
	 * End - The properties below are used for adding a new category
	 */
	private Long editedCategoryId	= null;
        
        public List<PossibleValue> getPossibleVals() {
            return possibleVals;
        }

        public void setPossibleVals(List<PossibleValue> possibleVals) {
            this.possibleVals = possibleVals;
        }
	
	public Integer getNumOfPossibleValues() {
		return numOfPossibleValues;
	}
	public void setNumOfPossibleValues(Integer numOfPossibleValues) {
		this.numOfPossibleValues = numOfPossibleValues;
	}
	public Boolean getAddNewCategory() {
		return addNewCategory;
	}
	public void setAddNewCategory(Boolean addNewCategory) {
		this.addNewCategory = addNewCategory;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	/*public String getPossibleValue(int key) {
		return (String)possibleValue.get(key);
	}
	public void setPossibleValue(int key, String value) {
		possibleValue.add(key, value);
	}
	*/
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String[] getPossibleValues() {
		return possibleValues;
	}
	public void setPossibleValues(String[] possibleValues) {
		this.possibleValues = possibleValues;
	}
	public Collection getCategories() {
		return categories;
	}
	public void setCategories(Collection categories) {
		this.categories = categories;
	}
	public Long getEditedCategoryId() {
		return editedCategoryId;
	}
	public void setEditedCategoryId(Long editedCategoryId) {
		this.editedCategoryId = editedCategoryId;
	}
	public boolean getIsMultiselect() {
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
	
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		this.isMultiselect	= false;
		this.isOrdered		= false;
	}
	public String getKeyName() {
		return keyName;
	}
	public void setKeyName(String key) {
		this.keyName = key;
	}

	public int getNumOfAdditionalFields() {
		return numOfAdditionalFields;
	}

	public void setNumOfAdditionalFields(int numOfAdditionalFields) {
		this.numOfAdditionalFields = numOfAdditionalFields;
	}
	
}
