package org.digijava.kernel.ampapi.endpoints.filters;

import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class FilterTreeDefinition {

    private Long id;
    
    private String name;
    
    private String displayName;
    
    private List<String> filterIds;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public List<String> getFilterIds() {
        return filterIds;
    }

    public void setFilterIds(List<String> filterIds) {
        this.filterIds = filterIds;
    }

}
