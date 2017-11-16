package org.digijava.kernel.ampapi.endpoints.filters;

import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class FilterListDefinition {

    private Long id;
    
    private String name;
    
    private String displayName;
    
    private List<String> filterIds;
    
    private boolean filtered;
    
    private String items;
    
    private String tab = EPConstants.TAB_UNASSIGNED;
    
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

    public boolean isFiltered() {
        return filtered;
    }

    public void setFiltered(boolean filtered) {
        this.filtered = filtered;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public String getTab() {
        return tab;
    }

    public void setTab(String tab) {
        this.tab = tab;
    }
}
