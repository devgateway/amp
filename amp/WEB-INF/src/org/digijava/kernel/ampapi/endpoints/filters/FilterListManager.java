package org.digijava.kernel.ampapi.endpoints.filters;

public interface FilterListManager {
    
    default FilterList getFilterList() {
        return new FilterList();
    }
    
    default FilterList getFilterList(String filterId) {
        return getFilterList();
    }
    
    default boolean isVisible() {
        return true;
    }
    
}
