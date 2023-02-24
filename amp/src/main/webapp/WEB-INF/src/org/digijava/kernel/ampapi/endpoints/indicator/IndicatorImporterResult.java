package org.digijava.kernel.ampapi.endpoints.indicator;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

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
