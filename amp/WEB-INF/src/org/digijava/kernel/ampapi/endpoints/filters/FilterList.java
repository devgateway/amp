package org.digijava.kernel.ampapi.endpoints.filters;

import java.util.List;
import java.util.Map;

public class FilterList {
    
    private List<FilterListDefinition> listDefinitions; 
    
    private Map<String, List<FilterListTreeNode>> items;

    public FilterList(List<FilterListDefinition> listDefinitions, Map<String, List<FilterListTreeNode>> items) {
        this.listDefinitions = listDefinitions;
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
