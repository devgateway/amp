package org.digijava.kernel.ampapi.endpoints.datafreeze;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;

public class DataFreezeEventResult {
    
    @ApiModelProperty("data freeze object")
    private DataFreezeEvent data;
    
    @ApiModelProperty(value = "indicates if the save was successful or not. Can be SAVE_SUCCESSFUL or SAVE_FAILED",
            example = "SAVE_SUCCESSFUL")
    private String result;
    
    @ApiModelProperty("list of errors that occurred while saving")
    private List<JsonBean> errors;
    
    public DataFreezeEvent getData() {
        return data;
    }
    
    public void setData(DataFreezeEvent data) {
        this.data = data;
    }
    
    public String getResult() {
        return result;
    }
    
    public void setResult(String result) {
        this.result = result;
    }
    
    public List<JsonBean> getErrors() {
        return errors;
    }
    
    public void setErrors(List<JsonBean> errors) {
        this.errors = errors;
    }
}
