package org.digijava.kernel.ampapi.endpoints.indicator.manager.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.digijava.module.aim.dbentity.AmpIndicatorGlobalValue;

public class AmpIndicatorDisaggregationValueDto {
    private Long id;
    private Long parentCategoryId;
    private Long childCategoryId;
    @JsonProperty("base")
    private AmpIndicatorGlobalValue baseValue;
    @JsonProperty("target")
    private AmpIndicatorGlobalValue targetValue;
    public Long getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(Long parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChildCategoryId() {
        return childCategoryId;
    }

    public void setChildCategoryId(Long childCategoryId) {
        this.childCategoryId = childCategoryId;
    }

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


}
