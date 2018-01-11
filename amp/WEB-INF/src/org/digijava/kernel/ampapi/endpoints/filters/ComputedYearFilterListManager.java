package org.digijava.kernel.ampapi.endpoints.filters;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.digijava.kernel.translator.TranslatorWorker;

/**
 * This class generates the filter list object for computed year
 * 
 * @author Viorel Chihai
 *
 */
public final class ComputedYearFilterListManager implements FilterListManager {
    
    private static final String ITEMS_NAME = "values";
    private static final int NUM_OF_YEARS = 10;
    private static ComputedYearFilterListManager computedYearFilterListManager;
    
    public static ComputedYearFilterListManager getInstance() {
        if (computedYearFilterListManager == null) {
            computedYearFilterListManager = new ComputedYearFilterListManager();
        }

        return computedYearFilterListManager;
    }
    
    private ComputedYearFilterListManager() { }
    
    @Override
    public FilterList getFilterList() {
        List<FilterListDefinition> filterDefinition = getFilterDefinition();
        Map<String, List<FilterListTreeNode>> filterItems = getFilterListItems();
        
        return new FilterList(filterDefinition, filterItems);
    }
    
    private List<FilterListDefinition> getFilterDefinition() {
        List<FilterListDefinition> listDefinitions = new ArrayList<>();
        FilterListDefinition listDefinition = new FilterListDefinition();
        
        List<String> filterIds = new ArrayList<>();
        filterIds.add(FiltersConstants.COMPUTED_YEAR);
        
        listDefinition.setId(null);
        listDefinition.setName(ColumnConstants.COMPUTED_YEAR);
        listDefinition.setDisplayName(TranslatorWorker.translateText(ColumnConstants.COMPUTED_YEAR));
        listDefinition.setFilterIds(filterIds);
        listDefinition.setFiltered(true);
        listDefinition.setItems(ITEMS_NAME);
        listDefinitions.add(listDefinition);
        
        return listDefinitions;
    }
    
    private Map<String, List<FilterListTreeNode>> getFilterListItems() {
        Map<String, List<FilterListTreeNode>> items = new HashMap<>();
        List<FilterListTreeNode> nodes = new ArrayList<>();
        
        long curYear = new GregorianCalendar().get(Calendar.YEAR);
        
        FilterListTreeNode currYearNode = new FilterListTreeNode();
        currYearNode.setId(curYear);
        currYearNode.setName(TranslatorWorker.translateText(FiltersConstants.CURRENT));
        currYearNode.setValue(String.valueOf(curYear));
        nodes.add(currYearNode);
       
        for (long year = curYear - 1; year > curYear - NUM_OF_YEARS; year--) {
            String yearStr = String.valueOf(year);
            FilterListTreeNode yearNode = new FilterListTreeNode();
            yearNode.setId(year);
            yearNode.setName(yearStr);
            yearNode.setValue(yearStr);
            nodes.add(yearNode);
        }
        
        items.put(ITEMS_NAME, nodes);

        return items;
    }
}
