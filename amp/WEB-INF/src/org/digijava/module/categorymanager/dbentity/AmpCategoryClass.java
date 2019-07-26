package org.digijava.module.categorymanager.dbentity;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * @author Alex Gartner
 *
 */
public class AmpCategoryClass implements Serializable, Comparable<AmpCategoryClass> {
    private Long id;
    private String name;
    private String description;
    private String keyName;
    private boolean isMultiselect   = false;
    private boolean isOrdered       = false;
    
    private List<AmpCategoryValue> possibleValues;
    
    private List<AmpCategoryClass> usedCategories;
    private Set<AmpCategoryClass> usedByCategories;
    
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
