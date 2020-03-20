package org.digijava.kernel.ampapi.endpoints.scorecard.model;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Octavian Ciubotaru
 */
public class SettingsBean {

    public static class CategoryValue {

        @ApiModelProperty(example = "1")
        private Long id;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
    }

    @ApiModelProperty(example = "5")
    private Integer validationTime;

    @ApiModelProperty(example = "true")
    private Boolean validationPeriod;

    @ApiModelProperty(example = "10")
    private Double percentageThreshold;

    private List<CategoryValue> categoryValues;

    public Integer getValidationTime() {
        return validationTime;
    }

    public void setValidationTime(Integer validationTime) {
        this.validationTime = validationTime;
    }

    public Boolean getValidationPeriod() {
        return validationPeriod;
    }

    public void setValidationPeriod(Boolean validationPeriod) {
        this.validationPeriod = validationPeriod;
    }

    public Double getPercentageThreshold() {
        return percentageThreshold;
    }

    public void setPercentageThreshold(Double percentageThreshold) {
        this.percentageThreshold = percentageThreshold;
    }

    public List<CategoryValue> getCategoryValues() {
        return categoryValues;
    }

    public void setCategoryValues(List<CategoryValue> categoryValues) {
        this.categoryValues = categoryValues;
    }
}
