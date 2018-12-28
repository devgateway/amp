package org.digijava.kernel.ampapi.endpoints.dto;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import org.digijava.kernel.ampapi.endpoints.gpi.GPIEPConstants;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;

/**
 * @author Octavian Ciubotaru
 */
public class SaveResult<T> {

    @ApiModelProperty("saved object")
    private T data;

    @ApiModelProperty(value = "result string that indicates if the save was successful or not",
            allowableValues = GPIEPConstants.SAVED + ", " + GPIEPConstants.SAVE_FAILED)
    private String result;

    @ApiModelProperty("an array of error objects for all the errors that occurred while saving")
    private List<JsonBean> errors;

    public SaveResult(T data) {
        this.data = data;
        this.result = GPIEPConstants.SAVED;
    }

    public SaveResult(T data, List<JsonBean> errors) {
        this.data = data;
        this.result = GPIEPConstants.SAVE_FAILED;
        this.errors = errors;
    }

    public T getData() {
        return data;
    }

    public String getResult() {
        return result;
    }

    public List<JsonBean> getErrors() {
        return errors;
    }
}
