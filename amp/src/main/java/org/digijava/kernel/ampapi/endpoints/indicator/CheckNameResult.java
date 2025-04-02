package org.digijava.kernel.ampapi.endpoints.indicator;

import io.swagger.annotations.ApiParam;

public class CheckNameResult {
    
    @ApiParam("if the name exists or not")
    private Boolean result;
    
    public CheckNameResult(Boolean result) {
        this.result = result;
    }
    
    public Boolean getResult() {
        return result;
    }
    
    public void setResult(Boolean result) {
        this.result = result;
    }
}
