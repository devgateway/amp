package org.digijava.module.aim.dbentity;

import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.io.Serializable;
import java.util.Set;
import java.util.HashSet;

public class AmpIndicatorDisaggregationValue implements Serializable {
    private Long id;

    private AmpCategoryValue parentCategory;
    private AmpCategoryValue childCategory;

    private AmpIndicatorGlobalValue baseValue;
    private AmpIndicatorGlobalValue targetValue;
    // changed from List to Set and will be mapped via hbm
    private Set<AmpIndicatorGlobalValue> actualValues; // @OneToMany like mapping in hbm (inverse)

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
