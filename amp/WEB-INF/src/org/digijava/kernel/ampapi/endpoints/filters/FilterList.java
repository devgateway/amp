package org.digijava.kernel.ampapi.endpoints.filters;

import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FilterList {
    
    @ApiModelProperty(value = "Filter list definitions")
    private List<FilterListDefinition> listDefinitions = new ArrayList<>();
    
    @ApiModelProperty(value = "Items needed for building the filter tree")
    private Map<String, List<FilterListTreeNode>> items;
    
    public FilterList() { }

    public FilterList(List<FilterListDefinition> listDefinitions, Map<String, List<FilterListTreeNode>> items) {
        this.listDefinitions = listDefinitions;
        this.items = items;
    }
    
    public FilterList(FilterListDefinition listDefinition, Map<String, List<FilterListTreeNode>> items) {
        listDefinitions.add(listDefinition);
        this.items = items;
    }

    public List<FilterListDefinition> getListDefinitions() {
        return listDefinitions;
    }

    public void setListDefinitions(List<FilterListDefinition> listDefinitions) {
        this.listDefinitions = listDefinitions;
    }

    public Map<String, List<FilterListTreeNode>> getItems() {
        return items;
    }

    public void setItems(Map<String, List<FilterListTreeNode>> items) {
        this.items = items;
    } 
    
}
