package org.digijava.kernel.ampapi.endpoints.filters;

import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.util.TeamMemberUtil;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This class generates the filter list (tree) object for activity approval status
 * 
 * @author Viorel Chihai
 *
 */
public final class ApprovalStatusFilterListManager implements FilterListManager {
    
    private static ApprovalStatusFilterListManager catValFilterListManager;

    public static ApprovalStatusFilterListManager getInstance() {
        if (catValFilterListManager == null) {
            catValFilterListManager = new ApprovalStatusFilterListManager();
        }

        return catValFilterListManager;
    }
    
    private ApprovalStatusFilterListManager() { }
    
    @Override
    public FilterList getFilterList() {
        if (TeamMemberUtil.getLoggedInTeamMember() == null) {
            return new FilterList();
        }
        List<FilterListDefinition> approvalStatusDefinition = getApprovalStatusDefinition();
        Map<String, List<FilterListTreeNode>> approvalStatusItems = getApprovalStatusListItems();
        
        return new FilterList(approvalStatusDefinition, approvalStatusItems);
    }
    
    private List<FilterListDefinition> getApprovalStatusDefinition() {
        List<FilterListDefinition> listDefinitions = new ArrayList<>();
        FilterListDefinition listDefinition = new FilterListDefinition();
        
        List<String> filterIds = new ArrayList<>();
        filterIds.add(FiltersConstants.APPROVAL_STATUS);
        
        listDefinition.setId(null);
        listDefinition.setName(ColumnConstants.APPROVAL_STATUS);
        listDefinition.setDisplayName(TranslatorWorker.translateText(ColumnConstants.APPROVAL_STATUS));
        listDefinition.setFilterIds(filterIds);
        listDefinition.setFiltered(true);
        listDefinition.setItems(ITEMS_NAME);
        listDefinitions.add(listDefinition);
        
        return listDefinitions;
    }
    
    private Map<String, List<FilterListTreeNode>> getApprovalStatusListItems() {
        Map<String, List<FilterListTreeNode>> items = new HashMap<>();
        
        List<FilterListTreeNode> statusItems = new ArrayList<>();
        
        List<String> orderedKeys = AmpARFilter.VALIDATION_STATUS.keySet().stream()
                .sorted(Comparator.comparing(key -> TranslatorWorker.translateText(key)))
                .collect(Collectors.toList());
        
        for (String key : orderedKeys) {
            FilterListTreeNode node = new FilterListTreeNode();
            node.setId((long) AmpARFilter.VALIDATION_STATUS.get(key));
            node.setValue(key);
            node.setName(TranslatorWorker.translateText(key));
            statusItems.add(node);
        }
        
        items.put(ITEMS_NAME, statusItems);

        return items;
    }
    
    @Override
    public boolean isVisible() {
        return TeamMemberUtil.getLoggedInTeamMember() != null;
    }
}
