package org.digijava.kernel.ampapi.endpoints.errors;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.kernel.ampapi.endpoints.util.ObjectMapperUtils;

import java.util.Collection;
import java.util.Map;

public class ApiErrorResponse {

    @JsonProperty(EPConstants.ERROR)
    @ApiModelProperty(dataType = "org.digijava.kernel.ampapi.swagger.types.ErrorOrWarningPH")
    private Map<String, Collection<Object>> errors;

    public ApiErrorResponse(Map<String, Collection<Object>> errors) {
        this.errors = errors;
    }

    public Map<String, Collection<Object>> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, Collection<Object>> errors) {
        this.errors = errors;
    }
    
    /**
     * renders the object as a String
     */
    public String asJsonString() {
        return ObjectMapperUtils.valueToString(this);
    }

    @Override
    public String toString() {
        return "ApiErrorResponse [" + asJsonString() + "]";
    }
}
