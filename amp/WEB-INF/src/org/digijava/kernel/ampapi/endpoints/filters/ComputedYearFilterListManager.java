package org.digijava.kernel.ampapi.endpoints.filters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;

/**
 * This class generates the filter list object for computed year
 * 
 * @author Viorel Chihai
 *
 */
public final class ComputedYearFilterListManager implements FilterListManager {
    
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
             
        int startYear = FeaturesUtil.getGlobalSettingValueInteger(GlobalSettingsConstants.YEAR_RANGE_START);
        int range = FeaturesUtil.getGlobalSettingValueInteger(GlobalSettingsConstants.NUMBER_OF_YEARS_IN_RANGE);
        int endYear = startYear + range;        
       
        for (long year = endYear; year >= startYear; year--) {
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
