package org.digijava.kernel.ampapi.endpoints.indicator.AmpDashboard;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CoreIndicatorValueDTO {
    @JsonProperty("core-indicator-type")
    private CoreIndicatorTypeDTO coreIndicatorType;
    @JsonProperty("target-value")
    private Double targetValue;
    @JsonProperty("actual-value")
    private Double actualValue;

    public CoreIndicatorTypeDTO getCoreIndicatorType() {
        return coreIndicatorType;
    }

    public void setCoreIndicatorType(CoreIndicatorTypeDTO coreIndicatorType) {
        this.coreIndicatorType = coreIndicatorType;
    }

    public Double getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(Double targetValue) {
        this.targetValue = targetValue;
    }

    public Double getActualValue() {
        return actualValue;
    }

    public void setActualValue(Double actualValue) {
        this.actualValue = actualValue;
    }
}