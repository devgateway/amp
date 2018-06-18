package org.digijava.kernel.ampapi.endpoints.filters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.util.TeamMemberUtil;

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
        for (String key : AmpARFilter.activityApprovalStatus.keySet()) {
            FilterListTreeNode node = new FilterListTreeNode();
            node.setId((long) AmpARFilter.activityApprovalStatus.get(key));
            node.setValue(key);
            node.setName(TranslatorWorker.translateText(key));
            statusItems.add(node);
        }
        statusItems.add(UNDEFINED_OPTION);
        
        items.put(ITEMS_NAME, statusItems);

        return items;
    }
    
    @Override
    public boolean isVisible() {
        return TeamMemberUtil.getLoggedInTeamMember() != null;
    }
}
