package org.digijava.kernel.ampapi.endpoints.filters;

import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class generates the filter list object for computed year
 * 
 * @author Viorel Chihai
 *
 */
public final class DateFilterListManager implements FilterListManager {
    
    private static final String DATE_FILTER_NAME = "Date";
    private static final String START_YEAR = "startYear";
    private static final String END_YEAR = "endYear";
    
    private static DateFilterListManager dateFilterListManager;
    
    public static DateFilterListManager getInstance() {
        if (dateFilterListManager == null) {
            dateFilterListManager = new DateFilterListManager();
        }

        return dateFilterListManager;
    }
    
    private DateFilterListManager() { }
    
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
        filterIds.add(FiltersConstants.DATE);
        
        listDefinition.setId(null);
        listDefinition.setName(DATE_FILTER_NAME);
        listDefinition.setDisplayName(TranslatorWorker.translateText(DATE_FILTER_NAME));
        listDefinition.setFilterIds(filterIds);
        listDefinition.setFiltered(true);
        listDefinition.setItems(ITEMS_NAME);
        listDefinitions.add(listDefinition);
        
        return listDefinitions;
    }
    
    private Map<String, List<FilterListTreeNode>> getFilterListItems() {
        Map<String, List<FilterListTreeNode>> items = new HashMap<>();
        List<FilterListTreeNode> nodes = new ArrayList<>();
        
        long startYear = FeaturesUtil.getGlobalSettingValueLong(GlobalSettingsConstants.YEAR_RANGE_START);
        long range = FeaturesUtil.getGlobalSettingValueLong(GlobalSettingsConstants.NUMBER_OF_YEARS_IN_RANGE);
        long endYear = startYear + range;
        
        FilterListTreeNode startYearNode = new FilterListTreeNode();
        startYearNode.setId(startYear);
        startYearNode.setName(START_YEAR);
        startYearNode.setValue(String.valueOf(startYear));
        nodes.add(startYearNode);
        
        FilterListTreeNode endYearNode = new FilterListTreeNode();
        endYearNode.setId(endYear);
        endYearNode.setName(END_YEAR);
        endYearNode.setValue(String.valueOf(endYear));
        nodes.add(endYearNode);
        
        items.put(ITEMS_NAME, nodes);

        return items;
    }
}
