package org.digijava.kernel.ampapi.endpoints.indicator;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class IndicatorImporterResult {
    
    @JsonProperty(IndicatorEPConstants.VALUES)
    private List<LocationIndicatorValueResult> values;
    
    public IndicatorImporterResult(List<LocationIndicatorValueResult> values) {
        this.values = values;
    }
    
    public List<LocationIndicatorValueResult> getValues() {
        return values;
    }
    
    public void setValues(List<LocationIndicatorValueResult> values) {
        this.values = values;
    }
}
