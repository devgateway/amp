/**
 * 
 * @author Alexandru Cartaleanu 
 * @description class for passing parameters to the query builder
 * of AmpARFilter, after it's been disconnected from direct interaction with
 * HttpServletRequest and related elements
 *
 */
package org.dgfoundation.amp.ar;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;




public class AmpARFilterParams {
	
	
	public TeamMember getMember() {
		return member;
	}

	public String getLuceneSearchModeParam() {
		return luceneSearchModeParam;
	}

	public String getLuceneRealPath() {
		return luceneRealPath;
	}

	public AmpARFilter getTeamFilter() {
		return teamFilter;
	}
	public Boolean getWorkspaceFilter() {
		return workspaceFilter;
	}

	public Boolean getSkipPledgeCheck() {
		return skipPledgeCheck;
	}
	/*TeamMember tm, String searchModeParam, String realPath, AmpARFilter teamFilter, AmpTeam team, boolean workspaceFilter, boolean skipPledgeCheck*/
	private TeamMember member;
	private String luceneSearchModeParam;
	private String luceneRealPath;
	private AmpARFilter teamFilter;
	private Boolean workspaceFilter;
	private Boolean skipPledgeCheck;
	
	private AmpARFilterParams() {	}
	
	public static AmpARFilterParams getParamsForWorkspaceFilter(TeamMember tm) {
		AmpARFilterParams params = new AmpARFilterParams();
		params.member = tm;
		params.luceneRealPath = null;
		params.luceneSearchModeParam = null;
		params.teamFilter = null;
		params.workspaceFilter = true;
		params.skipPledgeCheck = false;
		return params;
	}
	
	public static AmpARFilterParams getParamsFromRequest(HttpServletRequest request, boolean workspaceFilter, boolean skipPledgeCheck) {
		AmpARFilterParams params = new AmpARFilterParams();
		
		params.luceneSearchModeParam = null;
		if(request.getParameter("searchMode") != null)
			params.luceneSearchModeParam = request.getParameter("searchMode");
		HttpSession session = request.getSession();
		ServletContext ampContext = session.getServletContext();
		params.luceneRealPath = ampContext.getRealPath("/");
		params.teamFilter = (AmpARFilter) request.getSession().getAttribute(ArConstants.TEAM_FILTER);
		params.member = (TeamMember) request.getSession().getAttribute(Constants.CURRENT_MEMBER);
		params.skipPledgeCheck = skipPledgeCheck;
		params.workspaceFilter = workspaceFilter;
        
        return params;
	}
	
}