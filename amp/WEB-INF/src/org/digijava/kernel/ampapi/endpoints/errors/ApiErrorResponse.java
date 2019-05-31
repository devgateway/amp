package org.digijava.kernel.ampapi.endpoints.errors;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.dgfoundation.amp.algo.AlgoUtils;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;

import java.util.Collection;
import java.util.Map;

public class ApiErrorResponse {

    @JsonProperty(EPConstants.ERROR)
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
        try {
            return new ObjectMapper().writer().writeValueAsString(this);
        } catch (Exception e) {
            throw AlgoUtils.translateException(e);
        }
    }
    
    @Override
    public String toString() {
        return "ApiErrorResponse [" + asJsonString() + "]";
    }
}
