package org.digijava.kernel.ampapi.endpoints.indicator;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IndicatorOperationResult {
    
    @JsonProperty(IndicatorEPConstants.RESULT)
    private ResultOptions result;
    
    public IndicatorOperationResult() {
    }
    
    public IndicatorOperationResult(ResultOptions result) {
        this.result = result;
    }
    
    public enum ResultOptions {
        INSERTED, SAVED, DELETED
    };
    
    public ResultOptions getResult() {
        return result;
    }
    
    public void setResult(ResultOptions result) {
        this.result = result;
    }
}
