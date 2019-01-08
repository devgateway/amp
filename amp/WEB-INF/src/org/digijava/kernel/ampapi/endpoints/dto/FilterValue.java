package org.digijava.kernel.ampapi.endpoints.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 
 * @author jdeanquin elfleco
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FilterValue {
    private Object id;
    
    private String code;
    
    private String name;
    
    private String displayName;
    
    private String filterId;
    
    private String type;
    
    private List<FilterValue> children;

    public FilterValue() {
    }

    public FilterValue(Object id, String name, String code, Long type) {
        this(id, name, code);
        this.type = type.toString();
    }

    public FilterValue(Object id, String name, String code, String displayName) {
        this(id, name, code);
        this.displayName = displayName;
    }

    public FilterValue(Object id, String name, String code) {
        this(id, name);
        this.code = code;
    }

    public FilterValue(Object id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("%sid: %s, code: %s, name: %s", this.children == null ? "with children " : "", id, code, name);
    }

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<FilterValue> getChildren() {
        return children;
    }

    public void setChildren(List<FilterValue> children) {
        this.children = children;
    }

    public String getFilterId() {
        return filterId;
    }

    public void setFilterId(String filterId) {
        this.filterId = filterId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
