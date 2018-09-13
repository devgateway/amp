package org.digijava.kernel.ampapi.endpoints.activity;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

public class FieldIdValue {

    private Long id;

    private String value;
    
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    @JsonProperty("ancestor-values")
    private List<String> ancestorValues;

    public FieldIdValue(Long id, String value, List<String> ancestorPath) {
        this.id = id;
        this.value = value;
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

}
