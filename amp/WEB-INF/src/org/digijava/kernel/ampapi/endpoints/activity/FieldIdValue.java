package org.digijava.kernel.ampapi.endpoints.activity;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

public class FieldIdValue {

    private Long id;

    private String value;
    
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    @JsonProperty("ancestor-path")
    private String ancestorPath;

    public FieldIdValue(Long id, String value, String ancestorPath) {
        this.id = id;
        this.value = value;
        this.ancestorPath = ancestorPath;
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

    public String getAncestorPath() {
        return ancestorPath;
    }

    public void setAncestorPath(String ancestorPath) {
        this.ancestorPath = ancestorPath;
    }

}
