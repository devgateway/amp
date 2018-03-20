package org.digijava.kernel.ampapi.endpoints.filters;

import org.digijava.kernel.translator.TranslatorWorker;

public interface FilterListManager {
    
    String ITEMS_NAME = "values";
    
    Long UNDEFINED_ID = 999999999L;
    String UNDEFINED_NAME = "Undefined";
    
    FilterListTreeNode UNDEFINED_OPTION = new FilterListTreeNode(UNDEFINED_ID, UNDEFINED_NAME, 
            TranslatorWorker.translateText(UNDEFINED_NAME));
    
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
