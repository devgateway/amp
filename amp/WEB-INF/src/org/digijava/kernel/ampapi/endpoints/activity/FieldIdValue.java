package org.digijava.kernel.ampapi.endpoints.activity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public class FieldIdValue {

    private Long id;

    private String value;

    @JsonProperty("translated-value")
    private Map<String, String> translatedValue;

    @JsonProperty("extra_info")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object extraInfo;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("ancestor-values")
    private List<String> ancestorValues;

    public FieldIdValue(Long id) {
        this.id = id;
    }

    public FieldIdValue(Long id, String value, Map<String, String> translatedValue, Object extraInfo,
                        List<String> ancestorPath) {
        this.id = id;
        this.value = value;
        this.translatedValue = translatedValue;
        this.extraInfo = extraInfo;
        this.ancestorValues = ancestorPath;
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

    public List<String> getAncestorValues() {
        return ancestorValues;
    }

    public void setAncestorValues(List<String> ancestorPath) {
        this.ancestorValues = ancestorPath;
    }
}
