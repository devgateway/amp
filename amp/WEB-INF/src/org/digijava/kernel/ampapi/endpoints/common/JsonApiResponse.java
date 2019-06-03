package org.digijava.kernel.ampapi.endpoints.common;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorResponse;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * A generic EP Response that can be used to store commonly reported errors and warnings in the same format by all
 * AMP EPs, as well as any additional generic details or concrete class with response content.
 *
 * Note that details/content will be reported unwrapped for convenience.
 *
 * @author Nadejda Mandrescu
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@ApiModel(description = "Reports any errors and warnings found, with posible other extra details like original input")
public class JsonApiResponse<T> extends ApiErrorResponse {

    @JsonProperty(EPConstants.WARNINGS)
    @ApiModelProperty(dataType = "org.digijava.kernel.ampapi.swagger.types.ErrorOrWarningPH")
    private Map<String, Collection<Object>> warnings;

    @JsonUnwrapped
    private T content;

    /**
     * The generic details that you cannot build in a DTO
     * Note: will be reported unwrapped through @JsonAnyGetter
     */
    private Map<String, Object> details;

    public JsonApiResponse(ApiErrorResponse apiErrorResponse) {
        this(apiErrorResponse.getErrors(), null, null, null);
    }

    public JsonApiResponse(Map<String, Collection<Object>> errors, Map<String, Collection<Object>> warnings,
            Map<String, Object> details) {
        this(errors, warnings, details, null);
    }

    public JsonApiResponse(Map<String, Collection<Object>> errors, Map<String, Collection<Object>> warnings,
            T content) {
        this(errors, warnings, null, content);
    }

    public JsonApiResponse(Map<String, Collection<Object>> errors, Map<String, Collection<Object>> warnings,
            Map<String, Object> details, T content) {
        super(errors);
        this.warnings = warnings;
        this.details = details;
        this.content = content;
    }

    public Map<String, Collection<Object>> getWarnings() {
        return warnings;
    }

    public void setWarnings(Map<String, Collection<Object>> warnings) {
        this.warnings = warnings;
    }

    /**
     * The content that will be reported unwrapped. Put inside details if a wrapping is needed.
     */
    public T getContent() {
        return this.content;
    }

    /**
     * The content that will be reported unwrapped. Put inside details if a wrapping is needed.
     */
    public void setContent(T content) {
        this.content = content;
    }

    /**
     * @return a generic Map that you cannot build in a concrete class
     */
    @JsonAnyGetter
    public Map<String, Object> getDetails() {
        return details;
    }

    /**
     * Sets a generic Map with details of the response, that you cannot build in a concrete class.
     * If you can have a concrete class, then please define it and store in content instead.
     * @param details
     */
    public void setDetails(Map<String, Object> details) {
        this.details = details;
    }

    private Map<String, Object> getAndInitDetails() {
        if (details == null) {
            details = new LinkedHashMap<>();
        }
        return details;
    }

    public JsonApiResponse<T> addDetail(String key, Object value) {
        getAndInitDetails().put(key, value);
        return this;
    }

    @Override
    public String toString() {
        return "JsonApiResponse [" + asJsonString() + "]";
    }

}
