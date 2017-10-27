package org.digijava.kernel.ampapi.endpoints.filters;

import java.util.List;

public class FilterList {
    
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
