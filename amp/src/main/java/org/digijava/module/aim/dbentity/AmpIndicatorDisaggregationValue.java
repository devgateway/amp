package org.digijava.module.aim.dbentity;

import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.InterchangeableBackReference;
import org.digijava.module.aim.annotations.interchange.InterchangeableId;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class AmpIndicatorDisaggregationValue implements Serializable {
    @InterchangeableId
    @Interchangeable(fieldTitle = "Id")
    private Long id;

    @Interchangeable(fieldTitle = "Parent Category", importable = true, pickIdOnly = true,
            fmPath = "/Activity Form/M&E/ME Item/Disaggregation Values/Parent Category")
    private AmpCategoryValue parentCategory;

    @Interchangeable(fieldTitle = "Child Category", importable = true, pickIdOnly = true,
            fmPath = "/Activity Form/M&E/ME Item/Disaggregation Values/Child Category")
    private AmpCategoryValue childCategory;

    @Interchangeable(fieldTitle = "Base Value", importable = true,
            fmPath = "/Activity Form/M&E/ME Item/Disaggregation Values/Base Value")
    private AmpIndicatorGlobalValue baseValue;

    @Interchangeable(fieldTitle = "Target Value", importable = true,
            fmPath = "/Activity Form/M&E/ME Item/Disaggregation Values/Target Value")
    private AmpIndicatorGlobalValue targetValue;

    // changed from List to Set and will be mapped via hbm
    @Interchangeable(fieldTitle = "Actual Values", importable = true,
            fmPath = "/Activity Form/M&E/ME Item/Disaggregation Values/Actual Values")
    private Set<AmpIndicatorGlobalValue> actualValues; // @OneToMany like mapping in hbm (inverse)

    @InterchangeableBackReference
    private AmpIndicator indicator;

    public AmpIndicatorGlobalValue getBaseValue() {
        return baseValue;
    }

    public void setBaseValue(AmpIndicatorGlobalValue baseValue) {
        this.baseValue = baseValue;
    }

    public AmpIndicatorGlobalValue getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(AmpIndicatorGlobalValue targetValue) {
        this.targetValue = targetValue;
    }

    public Set<AmpIndicatorGlobalValue> getActualValues() {
        if(actualValues == null) actualValues = new HashSet<>();
        return actualValues;
    }

    public void setActualValues(Set<AmpIndicatorGlobalValue> actualValues) {
        this.actualValues = actualValues;
    }

    public AmpCategoryValue getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(AmpCategoryValue parentCategory) {
        this.parentCategory = parentCategory;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AmpCategoryValue getChildCategory() {
        return childCategory;
    }

    public void setChildCategory(AmpCategoryValue childCategory) {
        this.childCategory = childCategory;
    }

    public AmpIndicator getIndicator() {
        return indicator;
    }

    public void setIndicator(AmpIndicator indicator) {
        this.indicator = indicator;
    }
}
