package org.digijava.kernel.ampapi.endpoints.activity;

import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

public class FieldIdValue {

    private Long id;

    private String value;

    @JsonProperty("translated-value")
    private Map<String, String> translatedValue;
    
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    @JsonProperty("ancestor-values")
    private List<String> ancestorValues;
    
    public FieldIdValue(Long id) {
        this.id = id;
    }

    public FieldIdValue(Long id, String value, Map<String, String> translatedValue, List<String> ancestorPath) {
        this.id = id;
        this.value = value;
        this.translatedValue = translatedValue;
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

    public List<String> getAncestorValues() {
        return ancestorValues;
    }

    public void setAncestorValues(List<String> ancestorPath) {
        this.ancestorValues = ancestorPath;
    }

    public Map<String, String> getTranslatedValue() {
        return translatedValue;
    }

    public void setTranslatedValue(Map<String, String> translatedValue) {
        this.translatedValue = translatedValue;
    }
}
