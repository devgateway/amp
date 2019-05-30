package org.digijava.kernel.ampapi.endpoints.common;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorResponse;

/**
 * A generic EP Response that can be used to store commonly reported errors and warnings in the same format by all
 * AMP EPs, as well as any additional details.
 *
 * @author Nadejda Mandrescu
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class JsonApiResponse extends ApiErrorResponse {

    public JsonApiResponse(ApiErrorResponse apiErrorResponse) {
        this(apiErrorResponse.getErrors(), null, null);
    }

    public JsonApiResponse(Map<String, Collection<Object>> errors, Map<String, Collection<Object>> warnings,
            Map<String, Object> details) {
        super(errors);
        this.warnings = warnings;
        this.details = details;
    }

    @JsonProperty(EPConstants.WARNINGS)
    private Map<String, Collection<Object>> warnings;

    private Map<String, Object> details;

    public Map<String, Collection<Object>> getWarnings() {
        return warnings;
    }

    public void setWarnings(Map<String, Collection<Object>> warnings) {
        this.warnings = warnings;
    }

    @JsonAnyGetter
    public Map<String, Object> getDetails() {
        return details;
    }

    public void setDetails(Map<String, Object> details) {
        this.details = details;
    }

    private Map<String, Object> getAndInitDetails() {
        if (details == null) {
            details = new LinkedHashMap<>();
        }
        return details;
    }

    public JsonApiResponse addDetail(String key, Object value) {
        getAndInitDetails().put(key, value);
        return this;
    }

    @Override
    public String toString() {
        return "JsonApiResponse [" + asJsonString() + "]";
    }

}
