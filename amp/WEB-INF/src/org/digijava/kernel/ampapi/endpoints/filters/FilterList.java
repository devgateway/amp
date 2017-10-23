package org.digijava.kernel.ampapi.endpoints.filters;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class FilterList {
    
    @JsonProperty(FiltersConstants.LIST_TREE_DEFINITIONS)
    private List<FilterTreeDefinition> treeDefinitions; 
    
    private List<FilterTreeNode> items;

    public FilterList(List<FilterTreeDefinition> treeDefinitions, List<FilterTreeNode> items) {
        this.treeDefinitions = treeDefinitions;
        this.items = items;
    }

    public List<FilterTreeDefinition> getTreeDefinitions() {
        return treeDefinitions;
    }

    public void setTreeDefinitions(List<FilterTreeDefinition> treeDefinitions) {
        this.treeDefinitions = treeDefinitions;
    }

    public List<FilterTreeNode> getItems() {
        return items;
    }

    public void setItems(List<FilterTreeNode> items) {
        this.items = items;
    } 
    
}
