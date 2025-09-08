package org.digijava.module.aim.dbentity;

import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

public class AmpIndicatorDisaggregationValue {
    private Long id;

    private AmpCategoryValue parentCategory;
    private AmpCategoryValue childCategory;

    private AmpIndicatorGlobalValue baseValue;
    private AmpIndicatorGlobalValue targetValue;

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
