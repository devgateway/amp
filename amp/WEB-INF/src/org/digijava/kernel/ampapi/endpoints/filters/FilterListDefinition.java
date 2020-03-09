package org.digijava.kernel.ampapi.endpoints.filters;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class FilterListDefinition {
    
    @ApiModelProperty(value = "The id of the filter definition if there are more than one", example = "1")
    private Long id;
    
    @ApiModelProperty(value = "The name of the filter", example = "Primary Program")
    private String name;
    
    @ApiModelProperty(value = "The translated name of the filter", example = "Primary Program")
    private String displayName;
    
    @ApiModelProperty(value = "What filterId should be associated to the each tree item on each level")
    private List<String> filterIds;
    
    @ApiModelProperty(value = "If the tree should be built dynamically. ")
    private boolean filtered;
    
    @ApiModelProperty(value = "The name of the object from which the values should be fetched")
    private String items;
    
    @ApiModelProperty(value = "Under which tab should be shown the filter tree")
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
