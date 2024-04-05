package org.digijava.kernel.ampapi.endpoints.filters;

import com.google.common.collect.ImmutableMap;
import org.digijava.kernel.ampapi.endpoints.util.FilterUtils;
import org.digijava.kernel.translator.TranslatorWorker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class generates the filter list (tree) object for boolean values
 * 
 * @author Viorel Chihai
 *
 */
public final class BooleanFilterListManager implements FilterListManager {
    
    public static final Long ANY_BOOLEAN = 999888777L;
    private static final String ITEMS_NAME = "values";
    private static BooleanFilterListManager booleanFilterListManager;
    
    public static final Map<Long, String> BOOLEAN_VALUES_MAP =
            new ImmutableMap.Builder<Long, String>()
                .put(1L, "Yes")
                .put(2L, "No")
                .build();
    
    public static BooleanFilterListManager getInstance() {
        if (booleanFilterListManager == null) {
            booleanFilterListManager = new BooleanFilterListManager();
        }

        return booleanFilterListManager;
    }
    
    private BooleanFilterListManager() { }
    
    @Override
    public FilterList getFilterList(String filterId) {
        List<FilterListDefinition> filterDefinition = getFilterDefinition(filterId);
        Map<String, List<FilterListTreeNode>> filterItems = getFilterListItems();
        
        return new FilterList(filterDefinition, filterItems);
    }
    
    private List<FilterListDefinition> getFilterDefinition(String filterId) {
        List<FilterListDefinition> listDefinitions = new ArrayList<>();
        FilterListDefinition listDefinition = new FilterListDefinition();
        
        String columnName = FilterUtils.INSTANCE.simpleColumnNameFromFilterId(filterId);
        
        List<String> filterIds = new ArrayList<>();
        filterIds.add(filterId);
        
        listDefinition.setId(null);
        listDefinition.setName(columnName);
        listDefinition.setDisplayName(TranslatorWorker.translateText(columnName));
        listDefinition.setFilterIds(filterIds);
        listDefinition.setFiltered(true);
        listDefinition.setItems(ITEMS_NAME);
        listDefinitions.add(listDefinition);
        
        return listDefinitions;
    }
    
    private Map<String, List<FilterListTreeNode>> getFilterListItems() {
        Map<String, List<FilterListTreeNode>> items = new HashMap<>();
        
        FilterListTreeNode nodeAll = new FilterListTreeNode();
        nodeAll.setId(ANY_BOOLEAN);
        
        BOOLEAN_VALUES_MAP.entrySet().forEach(e -> {
            FilterListTreeNode node = new FilterListTreeNode();
            node.setId(e.getKey());
            node.setValue(e.getValue());
            node.setName(TranslatorWorker.translateText(e.getValue()));
            nodeAll.addChild(node);
        });
        nodeAll.addChild(getUndefinedOption());
        
        List<FilterListTreeNode> nodes = new ArrayList<>();
        nodes.add(nodeAll);
        items.put(ITEMS_NAME, nodes);

        return items;
    }
}
