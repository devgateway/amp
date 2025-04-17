package org.digijava.kernel.ampapi.endpoints.filters;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.WorkspaceFilter;
import org.dgfoundation.amp.ar.viewfetcher.DatabaseViewFetcher;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;

import java.util.*;

/**
 * This class generates the filter list (tree) object for workspaces
 * 
 * @author Viorel Chihai
 *
 */
public final class WorkspaceFilterListManager implements FilterListManager {
    
    private static final String PRIVATE_WS_CONDITION = "WHERE (isolated is false) OR (isolated is null)";
    private static final String PARENT_WS_CONDITION = "WHERE parent_team_id = ";
    private static WorkspaceFilterListManager workspaceFilterListManager;

    public static WorkspaceFilterListManager getInstance() {
        if (workspaceFilterListManager == null) {
            workspaceFilterListManager = new WorkspaceFilterListManager();
        }

        return workspaceFilterListManager;
    }
    
    private WorkspaceFilterListManager() { }
    
    @Override
    public FilterList getFilterList() {
        if (TeamMemberUtil.getLoggedInTeamMember() == null) {
            return new FilterList();
        }
        List<FilterListDefinition> definition = getWorkspaceDefinition();
        Map<String, List<FilterListTreeNode>> items = getWorkspaceItems();
        
        return new FilterList(definition, items);
    }
    
    private List<FilterListDefinition> getWorkspaceDefinition() {
        List<FilterListDefinition> listDefinitions = new ArrayList<>();
        FilterListDefinition listDefinition = new FilterListDefinition();
        
        List<String> filterIds = new ArrayList<>();
        filterIds.add(FiltersConstants.TEAM);
        
        listDefinition.setId(null);
        listDefinition.setName(ColumnConstants.WORKSPACES);
        listDefinition.setDisplayName(TranslatorWorker.translateText(ColumnConstants.WORKSPACES));
        listDefinition.setFilterIds(filterIds);
        listDefinition.setFiltered(true);
        listDefinition.setItems(ITEMS_NAME);
        listDefinitions.add(listDefinition);
        
        return listDefinitions;
    }
    
    private Map<String, List<FilterListTreeNode>> getWorkspaceItems() {
        Map<String, List<FilterListTreeNode>> items = new HashMap<>();
        
        List<FilterListTreeNode> workspaceItems = new ArrayList<>();
        
        if (isVisible()) {
            TeamMember tm = TeamMemberUtil.getLoggedInTeamMember();
            AmpTeam ws = tm != null ? TeamUtil.getAmpTeam(tm.getTeamId()) : null;

            Map<Long, String> teamNames = null;

            if (ws != null && ws.getComputation() != null && ws.getComputation()) {
                Set<AmpTeam> workspaces = WorkspaceFilter.getComputedRelatedWorkspaces();
                if (workspaces != null) {
                    teamNames = new HashMap<Long, String>();
                    for (AmpTeam team : workspaces) {
                        teamNames.put(team.getAmpTeamId(), team.getName());
                    }
                }
            } else {
                // display only child workspaces in case of computed workspaces
                if (ws != null && Constants.ACCESS_TYPE_MNGMT.equals(ws.getAccessType())) {
                    teamNames = DatabaseViewFetcher
                            .fetchInternationalizedView("amp_team", PARENT_WS_CONDITION + ws.getAmpTeamId(), 
                                    "amp_team_id", "name");
                } else {
                    teamNames = DatabaseViewFetcher
                            .fetchInternationalizedView("amp_team", PRIVATE_WS_CONDITION, "amp_team_id", "name");
                }
            }

            if (teamNames != null) {
                for (long ampTeamId : teamNames.keySet()) {
                    FilterListTreeNode node = new FilterListTreeNode();
                    node.setId(ampTeamId);
                    node.setName(teamNames.get(ampTeamId));
                    workspaceItems.add(node);
                }
            }
            
            Collections.sort(workspaceItems, 
                    (w1, w2) -> w1.getName().toLowerCase().compareTo(w2.getName().toLowerCase()));
        }
        
        items.put(ITEMS_NAME, workspaceItems);

        return items;
    }
    
    @Override
    public boolean isVisible() {
        boolean isWorkspaceFilterVisible = true;
        boolean showWorkspaceFilterInTeamWorkspace = "true".equalsIgnoreCase(
                FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SHOW_WORKSPACE_FILTER_IN_TEAM_WORKSPACES));
        
        TeamMember tm = TeamMemberUtil.getLoggedInTeamMember();
        AmpTeam ampTeam = tm != null ? TeamUtil.getAmpTeam(tm.getTeamId()) : null;

        //Hide Workspace in public view
        if (ampTeam == null) {
            isWorkspaceFilterVisible = false;
        } else {
            boolean isComputation = ampTeam.getComputation() != null && ampTeam.getComputation();

            // showWorkspaceFilterInTeamWorkspace matters for computation workspace
            if (ampTeam.getAccessType().equals(Constants.ACCESS_TYPE_TEAM) && isComputation 
                    && !showWorkspaceFilterInTeamWorkspace) {
                isWorkspaceFilterVisible = false;
            }

            // showWorkspaceFilterInTeamWorkspace matters for management workspace
            if (ampTeam.getAccessType().equals(Constants.ACCESS_TYPE_MNGMT) && !showWorkspaceFilterInTeamWorkspace) {
                isWorkspaceFilterVisible = false;
            }

            // if it's regular team, non computation workspace
            if (ampTeam.getAccessType().equals(Constants.ACCESS_TYPE_TEAM) && !isComputation) {
                isWorkspaceFilterVisible = false;
            }
        }
        
        return isWorkspaceFilterVisible;
    }
}
