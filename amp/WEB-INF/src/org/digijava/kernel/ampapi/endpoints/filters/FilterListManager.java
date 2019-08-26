package org.digijava.kernel.ampapi.endpoints.filters;

import org.dgfoundation.amp.nireports.runtime.ColumnReportData;
import org.digijava.kernel.translator.TranslatorWorker;

public interface FilterListManager {
    
    String ITEMS_NAME = "values";
    
    default FilterListTreeNode getUndefinedOption() {
        return new FilterListTreeNode(ColumnReportData.UNALLOCATED_ID,
                FiltersConstants.UNDEFINED_NAME,
                TranslatorWorker.translateText(FiltersConstants.UNDEFINED_NAME));
    }
    
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
