/**
 * 
 * @author Alexandru Cartaleanu 
 * @description class for passing parameters to the query builder
 * of AmpARFilter, after it's been disconnected from direct interaction with
 * HttpServletRequest and related elements
 *
 */
package org.dgfoundation.amp.ar;

import javax.servlet.http.HttpServletRequest;

import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;

public class AmpARFilterParams {
    
    /*TeamMember tm, String searchModeParam, String realPath, AmpARFilter teamFilter, AmpTeam team, boolean workspaceFilter, boolean skipPledgeCheck*/
    private TeamMember member;
    private String luceneSearchModeParam;
    private AmpARFilter teamFilter;
    private boolean workspaceFilter;
    private boolean skipPledgeCheck;    
    private Long activityIdFilter;
    
    
    public Long getActivityIdFilter() {
        return activityIdFilter;
    }
    
    public TeamMember getMember() {
        return member;
    }

    public String getLuceneSearchModeParam() {
        return luceneSearchModeParam;
    }

    public AmpARFilter getTeamFilter() {
        return teamFilter;
    }
    public boolean getWorkspaceFilter() {
        return workspaceFilter;
    }

    public boolean getSkipPledgeCheck() {
        return skipPledgeCheck;
    }

    
    private AmpARFilterParams() {   }
    
    /*
     * activity ID filter should be the ID of the only activity you want to see,
     * or null if you don't want to use said filter
     * */
    public static AmpARFilterParams getParamsForWorkspaceFilter(TeamMember tm, Long ampActivityId) {
        AmpARFilterParams params = new AmpARFilterParams();
        params.member = tm;
        params.luceneSearchModeParam = null;
        params.teamFilter = null;
        params.workspaceFilter = true;
        params.skipPledgeCheck = false;
        params.activityIdFilter = ampActivityId;
        return params;
    }
    
    public static AmpARFilterParams getParamsFromRequest(HttpServletRequest request, boolean workspaceFilter, boolean skipPledgeCheck) {
        AmpARFilterParams params = new AmpARFilterParams();
        
        params.luceneSearchModeParam = null;
        if(request.getParameter("searchMode") != null)
            params.luceneSearchModeParam = request.getParameter("searchMode");
        params.teamFilter = workspaceFilter ? null : (AmpARFilter) request.getSession().getAttribute(ArConstants.TEAM_FILTER);
        params.member = (TeamMember) request.getSession().getAttribute(Constants.CURRENT_MEMBER);
        params.skipPledgeCheck = skipPledgeCheck;
        params.workspaceFilter = workspaceFilter;
        params.activityIdFilter = null;
        return params;
    }
    
}
