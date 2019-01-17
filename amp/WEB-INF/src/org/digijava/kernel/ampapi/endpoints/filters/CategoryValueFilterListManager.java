package org.digijava.kernel.ampapi.endpoints.filters;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

import com.google.common.collect.ImmutableMap;

/**
 * This class generates the filter list (tree) object for category values
 * 
 * @author Viorel Chihai
 *
 */
public final class CategoryValueFilterListManager implements FilterListManager {
    
    private static CategoryValueFilterListManager catValFilterListManager;
    
    public static final Map<String, CategoryBean> FILTER_NAME_TO_CATEGORY_ITEM =
        new ImmutableMap.Builder<String, CategoryBean>()
            .put(FiltersConstants.TYPE_OF_ASSISTANCE, 
                new CategoryBean(ColumnConstants.TYPE_OF_ASSISTANCE, CategoryConstants.TYPE_OF_ASSISTENCE_KEY))
            .put(FiltersConstants.MODE_OF_PAYMENT, 
                new CategoryBean(ColumnConstants.MODE_OF_PAYMENT, CategoryConstants.MODE_OF_PAYMENT_KEY))
            .put(FiltersConstants.STATUS, 
                new CategoryBean(FiltersConstants.ACTIVITY_STATUS_NAME, CategoryConstants.ACTIVITY_STATUS_KEY))
            .put(FiltersConstants.ON_OFF_TREASURY_BUDGET, 
                new CategoryBean(FiltersConstants.ACTIVITY_BUDGET_NAME, CategoryConstants.ACTIVITY_BUDGET_KEY))
            .put(FiltersConstants.FUNDING_STATUS, 
                new CategoryBean(ColumnConstants.FUNDING_STATUS, CategoryConstants.FUNDING_STATUS_KEY))
            .put(FiltersConstants.EXPENDITURE_CLASS, 
                new CategoryBean(ColumnConstants.EXPENDITURE_CLASS, CategoryConstants.EXPENDITURE_CLASS_KEY))
            .put(FiltersConstants.CONCESSIONALITY_LEVEL, 
                new CategoryBean(ColumnConstants.CONCESSIONALITY_LEVEL, CategoryConstants.CONCESSIONALITY_LEVEL_KEY))
            .put(FiltersConstants.PERFORMANCE_ALERT_LEVEL, 
                new CategoryBean(ColumnConstants.PERFORMANCE_ALERT_LEVEL, 
                        CategoryConstants.PERFORMANCE_ALERT_LEVEL_KEY))
            .put(FiltersConstants.FINANCING_INSTRUMENT, 
                new CategoryBean(ColumnConstants.FINANCING_INSTRUMENT, CategoryConstants.FINANCING_INSTRUMENT_KEY))
            .put(FiltersConstants.PLEDGES_STATUS, 
                    new CategoryBean(ColumnConstants.PLEDGE_STATUS, CategoryConstants.ACTIVITY_STATUS_KEY))
            .put(FiltersConstants.PLEDGES_AID_MODALITY, 
                    new CategoryBean(ColumnConstants.PLEDGES_AID_MODALITY, CategoryConstants.FINANCING_INSTRUMENT_KEY))
            .put(FiltersConstants.PLEDGES_TYPE_OF_ASSISTANCE, 
                    new CategoryBean(ColumnConstants.PLEDGES_TYPE_OF_ASSISTANCE, 
                        CategoryConstants.TYPE_OF_ASSISTENCE_KEY))
            .build();

    public static CategoryValueFilterListManager getInstance() {
        if (catValFilterListManager == null) {
            catValFilterListManager = new CategoryValueFilterListManager();
        }

        return catValFilterListManager;
    }
    
    private CategoryValueFilterListManager() { }
    
    @Override
    public FilterList getFilterList(String filterName) {
        List<FilterListDefinition> filterDefinition = getFilterDefinition(filterName);
        Map<String, List<FilterListTreeNode>> filterItems = getFilterListItems(filterName);
        
        return new FilterList(filterDefinition, filterItems);
    }
    
    private List<FilterListDefinition> getFilterDefinition(String filterName) {
        List<FilterListDefinition> listDefinitions = new ArrayList<>();
        FilterListDefinition listDefinition = new FilterListDefinition();
        
        String columnName = FILTER_NAME_TO_CATEGORY_ITEM.get(filterName).getName();
        
        List<String> filterIds = new ArrayList<>();
        filterIds.add(filterName);
        
        listDefinition.setId(null);
        listDefinition.setName(columnName);
        listDefinition.setDisplayName(TranslatorWorker.translateText(columnName));
        listDefinition.setFilterIds(filterIds);
        listDefinition.setFiltered(true);
        listDefinition.setItems(ITEMS_NAME);
        listDefinitions.add(listDefinition);
        
        return listDefinitions;
    }
    
    private Map<String, List<FilterListTreeNode>> getFilterListItems(String filterName) {
        Map<String, List<FilterListTreeNode>> items = new HashMap<>();
        
        List<FilterListTreeNode> nodes = new ArrayList<>();
        List<AmpCategoryValue> categoryItems = CategoryManagerUtil
                .getAmpCategoryValueCollectionByKey(FILTER_NAME_TO_CATEGORY_ITEM.get(filterName).getKey(), true)
                .stream()
                .filter(catValue -> !Boolean.TRUE.equals(catValue.getDeleted()))
                .sorted(Comparator.comparing(acv -> TranslatorWorker.translateText(acv.getValue())))
                .collect(Collectors.toList());
              
        
        for (AmpCategoryValue ampCategoryValue : categoryItems) {
            FilterListTreeNode node = new FilterListTreeNode();
            node.setId(ampCategoryValue.getId());
            node.setValue(ampCategoryValue.getValue());
            node.setName(TranslatorWorker.translateText(ampCategoryValue.getValue()));
            nodes.add(node);
        }
        nodes.add(getUndefinedOption());
        
        items.put(ITEMS_NAME, nodes);

        return items;
    }
    
    private static class CategoryBean {

        private String name;
        private String key;

        CategoryBean(String name, String key) {
            this.name = name;
            this.key = key;
        }

        public String getName() {
            return name;
        }

        public String getKey() {
            return key;
        }
    }
}
