package org.digijava.module.categorymanager.form;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.helper.KeyValue;
import org.digijava.module.categorymanager.dbentity.AmpCategoryClass;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.LabelCategory;
import org.digijava.module.categorymanager.util.PossibleValue;

/**
 * 
 * @author Alex Gartner
 *
 */
public class CategoryManagerForm extends ActionForm {
    
    private Collection<AmpCategoryClass>    categories  = null;
    
    /**
     * Begin - The properties below are used for adding a new category
     */
    private Boolean submitPressed       = null;
    private String categoryName         = null;
    private String keyName              = null;
    private String description          = null;
    private String [] possibleValues    = null;
    private Integer numOfPossibleValues = null;
    private boolean isMultiselect       = false;
    private boolean isOrdered           = false;
    private boolean advancedMode        = false;
    private List<PossibleValue> possibleVals        = new ArrayList<PossibleValue>();
    
    private Set<KeyValue> availableCategories           = null;
    private List<AmpCategoryClass> usedCategories       = null;
    private Long usedCategoryId                         = null;
    private Long delUsedCategoryId                      = null;
    private String useAction                            = null;
    
    private int numOfAdditionalFields   = 0;
    
    private Boolean usedCatIsSingleSelect;
    public Boolean getUsedCatIsSingleSelect() {
        return usedCatIsSingleSelect;
    }

    public void setUsedCatIsSingleSelect(Boolean usedCatIsSingleSelect) {
        this.usedCatIsSingleSelect = usedCatIsSingleSelect;
    }

    /**
     * End - The properties below are used for adding a new category
     */
    private Long editedCategoryId   = null;
    
    private Set<AmpCategoryValue> allCategoryValues     = null;
        
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
    
    

    public Boolean getSubmitPressed() {
        return submitPressed;
    }

    public void setSubmitPressed(Boolean submitPressed) {
        this.submitPressed = submitPressed;
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
    public Collection<AmpCategoryClass> getCategories() {
        return categories;
    }
    public void setCategories(Collection<AmpCategoryClass> categories) {
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
        this.isMultiselect  = false;
        this.isOrdered      = false;
        this.usedCatIsSingleSelect = false;
        
        /* This is for removing all labels in a category for a certain category value. 
         * If no checkbox is selected the code below is needed */
        Integer possibleValueIndex  = null;
        Integer labelCategoryIndex  = null;
        try {
            possibleValueIndex      = Integer.parseInt( request.getParameter("possibleValueIndex") );
            labelCategoryIndex      = Integer.parseInt( request.getParameter("labelCategoryIndex") );
        }
        catch (RuntimeException e) {;}
        if ( possibleValueIndex != null && labelCategoryIndex != null ){
            PossibleValue pv    = (PossibleValue)possibleVals.get(possibleValueIndex);
            LabelCategory lc    = (LabelCategory)pv.getLabelCategories().get(labelCategoryIndex);
            lc.setLabelsId(new Long[0]);
        }
        /* END */
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

    public boolean isAdvancedMode() {
        return advancedMode;
    }

    public void setAdvancedMode(boolean advancedMode) {
        this.advancedMode = advancedMode;
    }

    public List<AmpCategoryClass> getUsedCategories() {
        return usedCategories;
    }

    public void setUsedCategories(List<AmpCategoryClass> usedCategories) {
        this.usedCategories = usedCategories;
    }

    public Long getUsedCategoryId() {
        return usedCategoryId;
    }

    public void setUsedCategoryId(Long usedCategoryId) {
        this.usedCategoryId = usedCategoryId;
    }

    public String getUseAction() {
        return useAction;
    }

    public void setUseAction(String useAction) {
        this.useAction = useAction;
    }

    public Set<KeyValue> getAvailableCategories() {
        return availableCategories;
    }

    public void setAvailableCategories(Set<KeyValue> availableCategories) {
        this.availableCategories = availableCategories;
    }

    public Long getDelUsedCategoryId() {
        return delUsedCategoryId;
    }

    public void setDelUsedCategoryId(Long delUsedCategoryId) {
        this.delUsedCategoryId = delUsedCategoryId;
    }

    public Set<AmpCategoryValue> getAllCategoryValues() {
        return allCategoryValues;
    }

    public void setAllCategoryValues(Set<AmpCategoryValue> allCategoryValues) {
        this.allCategoryValues = allCategoryValues;
    }
    
}
