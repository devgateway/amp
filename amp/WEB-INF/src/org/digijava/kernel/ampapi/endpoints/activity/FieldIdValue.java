package org.digijava.kernel.ampapi.endpoints.activity;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FieldIdValue {

    private Long id;

    private String value;

    @JsonProperty("translated-value")
    private Map<String, String> translatedValue;

    @JsonProperty("extra_info")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object extraInfo;

    public FieldIdValue(Long id) {
        this.id = id;
    }

    public FieldIdValue(Long id, String value, Map<String, String> translatedValue, Object extraInfo) {
        this.id = id;
        this.value = value;
        this.translatedValue = translatedValue;
        this.extraInfo = extraInfo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Map<String, String> getTranslatedValue() {
        return translatedValue;
    }

    public void setTranslatedValue(Map<String, String> translatedValue) {
        this.translatedValue = translatedValue;
    }

    public Object getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(Object extraInfo) {
        this.extraInfo = extraInfo;
    }
}
