package org.dgfoundation.amp.ar;

import java.util.*;

import javax.servlet.http.HttpSession;

import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.helper.Workspace;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.aim.util.DbUtil;
import org.dgfoundation.amp.Util;

public class WorkspaceFilter 
{
	private Long teamMemberId;
	private Set teamAssignedOrgs = null;
	private Set ampTeams = null;
	private String accessType;
	private boolean hideDraft;
	private boolean approved;
	private boolean publicView;
	private long activitiesRejectedByFilter;
	
	/**
	 * might be null!
	 */
	private TeamMember teamMember;
	
	/**
	 * take care for special values of teamMemberId, like TEAM_MEMBER_ALL_MANAGEMENT_WORKSPACES !
	 * @param teamMemberId
	 * @param accessType
	 * @param draft
	 */
	private WorkspaceFilter(Long teamMemberId, String accessType, boolean approved, boolean hideDraft, boolean publicView)
	{
		this.teamMemberId = teamMemberId;
		this.hideDraft = hideDraft;
		this.approved = approved;
		this.accessType = accessType;
		this.publicView = publicView;
		prepareTeams();
	}

	/**
	 * computes teamAssignedOrg based on already-filled ampTeams
	 */
	protected void fillTeamAO()
	{
		// set the computed workspace orgs
		//Set teamAO = TeamUtil.getComputedOrgs(this.getAmpTeams());

		Set teamAO = TeamUtil.getComputedOrgs(this.getAmpTeams());

		if (teamAO != null && teamAO.size() > 0)
			this.setTeamAssignedOrgs(teamAO);
	}
	
	protected void prepareTeams()
	{
		if (teamMemberId == AmpARFilter.TEAM_MEMBER_ALL_MANAGEMENT_WORKSPACES)
		{
			// special case: simulate like "all management workspaces" has been selected
			Set<AmpTeam> allManagementTeams = new HashSet<AmpTeam>();
			allManagementTeams.addAll(TeamUtil.getAllManagementWorkspaces());
			this.setAmpTeams(TeamUtil.getRelatedTeamsForTeams(allManagementTeams));
			
			fillTeamAO();
			return;
		}
		teamMember = teamMemberId == null ? null : TeamMemberUtil.getTeamMember(teamMemberId);
		if (teamMember != null) {
			this.setAmpTeams(TeamUtil.getRelatedTeamsForMember(teamMember));
			fillTeamAO();
		}
		else {
			// nothing to do?
		}
	}
	
	/**
	 * HACK: for computed Workspaces With Filters, will return a query which only selects activities from within the workspace
	 * AmpARFilter is responsible for fetching / filtering the other activities and doing the OR
	 * @return
	 */
	public String getGeneratedQuery()
	{
		String TEAM_FILTER = "";
		
		//new computed filter - after permissions #3167
		//AMP-3726
//		activityStatus.add(Constants.STARTED_STATUS);
		String NO_MANAGEMENT_ACTIVITIES="";
		
		String used_approval_status = "Management".equals(this.getAccessType()) ? 
				Util.toCSString(AmpARFilter.validatedActivityStatus) :			// Management workspace: validated activities only
				(approved ? // non-management workspace, but only validated activities wanted nevertheless
						Util.toCSString(AmpARFilter.validatedActivityStatus) :
						Util.toCSString(AmpARFilter.activityStatus)	// other workspaces: all kinds of activities
				);
		
		if("Management".equals(this.getAccessType()))
			TEAM_FILTER = "SELECT amp_activity_id FROM amp_activity WHERE approval_status IN ("+used_approval_status+") AND draft<>true AND " +
					"amp_team_id IS NOT NULL AND amp_team_id IN ("
				+ Util.toCSString(ampTeams)
				+ ") ";
				// + " OR amp_activity_id IN (SELECT ata.amp_activity_id FROM amp_team_activities ata WHERE ata.amp_team_id IN ("
				//+ Util.toCSString(ampTeams) + ") ) AND draft<>true "; 
		else{
			
			TEAM_FILTER = "SELECT amp_activity_id FROM amp_activity WHERE amp_team_id IS NOT NULL AND amp_team_id IN ("
				+ Util.toCSString(ampTeams)
				+ ") ";
				//+ " OR amp_activity_id IN (SELECT ata.amp_activity_id FROM amp_team_activities ata WHERE ata.amp_team_id IN ("
				//+ Util.toCSString(ampTeams) + ") )" ;
		}
		NO_MANAGEMENT_ACTIVITIES +="SELECT amp_activity_id FROM amp_activity WHERE amp_team_id IS NOT NULL AND amp_team_id IN ("
			+ Util.toCSString(ampTeams)
			+ ") ";
			//+ " OR amp_activity_id IN (SELECT ata.amp_activity_id FROM amp_team_activities ata WHERE ata.amp_team_id IN ("
			//+ Util.toCSString(ampTeams) + ") )" ;
			
		String DRAFT_FILTER = "SELECT amp_activity_id FROM amp_activity WHERE (draft is null) OR (draft is false )";
		if (hideDraft)
			TEAM_FILTER += "AND amp_activity_id IN (" + DRAFT_FILTER + ") ";		
		
		TEAM_FILTER += "AND approval_status IN ("+used_approval_status+") ";
		// computed workspace filter -- append it to the team filter so normal
		// team activities are also possible
		//AMP-4495 - in computed workspace, the unapproved or draft activities from other
		//worskpaces should not be displayed
			if (teamAssignedOrgs != null && teamAssignedOrgs.size() > 0) {
				
				TEAM_FILTER += " OR amp_activity_id IN (SELECT DISTINCT(aor.activity) FROM amp_org_role aor, amp_activity a WHERE aor.organisation IN ("
						+ Util.toCSString(teamAssignedOrgs) + ") AND aor.activity=a.amp_activity_id AND a.amp_team_id IS NOT NULL AND a.approval_status IN (" +
						Util.toCSString(AmpARFilter.validatedActivityStatus)	+")  ) " + (hideDraft ? "AND draft<>true ":"");
				TEAM_FILTER += " OR amp_activity_id IN (SELECT distinct(af.amp_activity_id) FROM amp_funding af, amp_activity b WHERE af.amp_donor_org_id IN ("
						+ Util.toCSString(teamAssignedOrgs) + ") AND af.amp_activity_id=b.amp_activity_id AND b.amp_team_id IS NOT NULL AND b.approval_status IN (" +
						Util.toCSString(AmpARFilter.validatedActivityStatus)	+")  ) " + (hideDraft ? "AND draft<>true ":"");
//				TEAM_FILTER += " OR amp_activity_id IN (SELECT DISTINCT(aor.activity) FROM amp_org_role aor, amp_activity a WHERE aor.organisation IN ("
//					+ Util.toCSString(teamAssignedOrgs) + ") AND aor.activity=a.amp_activity_id AND a.amp_team_id IS NOT NULL )";
//				TEAM_FILTER += " OR amp_activity_id IN (SELECT distinct(af.amp_activity_id) FROM amp_funding af, amp_activity b WHERE af.amp_donor_org_id IN ("
//					+ Util.toCSString(teamAssignedOrgs) + ") AND af.amp_activity_id=b.amp_activity_id AND b.amp_team_id IS NOT NULL )";

				
				NO_MANAGEMENT_ACTIVITIES += " OR amp_activity_id IN (SELECT DISTINCT(aor.activity) FROM amp_org_role aor, amp_activity a WHERE aor.organisation IN ("
					+ Util.toCSString(teamAssignedOrgs) + ") AND aor.activity=a.amp_activity_id AND a.amp_team_id IS NOT NULL )";
				NO_MANAGEMENT_ACTIVITIES +=" OR amp_activity_id IN (SELECT distinct(af.amp_activity_id) FROM amp_funding af, amp_activity b WHERE af.amp_donor_org_id IN ("
					+ Util.toCSString(teamAssignedOrgs) + ") AND af.amp_activity_id=b.amp_activity_id AND b.amp_team_id IS NOT NULL )";		
			}
				
		int c;
		if (hideDraft){
			c = Math.abs( DbUtil.countActivitiesByQuery(TEAM_FILTER + " AND amp_activity_id IN (SELECT amp_activity_id FROM amp_activity WHERE (draft is null) OR (draft is false ) )",null )-DbUtil.countActivitiesByQuery(NO_MANAGEMENT_ACTIVITIES,null));
		}
		else
			c = Math.abs( DbUtil.countActivitiesByQuery(TEAM_FILTER,null) - DbUtil.countActivitiesByQuery(NO_MANAGEMENT_ACTIVITIES,null) );
		this.setActivitiesRejectedByFilter(new Long(c));
		
		
		return TEAM_FILTER;
	}
	
	/**
	 * take care for special values of teamMemberId, like TEAM_MEMBER_ALL_MANAGEMENT_WORKSPACES !
	 * @param teamMemberId
	 * @param accessType
	 * @param draft
	 * @return
	 */
	private static String generateWorkspaceFilterQuery(Long teamMemberId, String accessType, boolean approved, boolean draft, boolean publicView)
	{
		return new WorkspaceFilter(teamMemberId, accessType, approved, draft, publicView).getGeneratedQuery();
	}
	
	/**
	 * entry point for getting "current user's workspace filter"
	 * @param session
	 * @return
	 */
	private static String generateWorkspaceFilterQuery(HttpSession session)
	{
		 // (approval_status='approved' and draft<>true)
		TeamMember tm = (TeamMember) session.getAttribute("currentMember");
		return generateWorkspaceFilterQuery(session, null, tm == null);
	}
	
	public static String getWorkspaceFilterQuery(HttpSession session)
	{
	   	AmpARFilter teamFilter = (AmpARFilter) session.getAttribute(ArConstants.TEAM_FILTER);
    	String usedQuery;
    	if (teamFilter != null)
    		usedQuery = teamFilter.getGeneratedFilterQuery();
    	else
    		usedQuery = generateWorkspaceFilterQuery(session);
    	return usedQuery;
	}
	
	/**
	 * if forcedTeamMemberId == null, use the logged-in user (or public view). Else use the forcedTeamMember <b>which might have the special value TEAM_MEMBER_ALL_MANAGEMENT_WORKSPACES</b>
	 * @param session
	 * @param forcedTeamMemberId
	 * @return
	 */
	public static String generateWorkspaceFilterQuery(HttpSession session, Long forcedTeamMemberId, boolean publicView)
	{
		TeamMember tm = (TeamMember) session.getAttribute("currentMember");
		if (forcedTeamMemberId != null && forcedTeamMemberId != AmpARFilter.TEAM_MEMBER_ALL_MANAGEMENT_WORKSPACES)
			tm = TeamMemberUtil.getTeamMember(forcedTeamMemberId);
		
		if (tm == null)
		{
			//public view
			boolean draft = true;
			boolean approved = true;
			String accessType = Constants.ACCESS_TYPE_MNGMT;
			return generateWorkspaceFilterQuery(AmpARFilter.TEAM_MEMBER_ALL_MANAGEMENT_WORKSPACES, accessType, approved, draft, publicView);
		}
		else
		{
			boolean draft = Constants.ACCESS_TYPE_MNGMT.equalsIgnoreCase(tm.getTeamAccessType()) || "Donor".equalsIgnoreCase(tm.getTeamType());
			boolean approved = draft;
			
			/**
			 * Checks if the team is a computed workspace and in case it is
			 * it checks if it should hide the draft activities
			 */
			if (tm.getComputation() != null && tm.getComputation() ) {
				Workspace wrksp	= TeamUtil.getWorkspace( tm.getTeamId() );
				if ( wrksp != null && wrksp.getHideDraftActivities() != null && wrksp.getHideDraftActivities() )
					draft = true;
			}
			String accessType = tm.getTeamAccessType();
			return generateWorkspaceFilterQuery(tm.getMemberId(), accessType, approved, draft, publicView);
		}
	}
	
	public Set getTeamAssignedOrgs() {
		return teamAssignedOrgs;
	}

	public void setTeamAssignedOrgs(Set teamAssignedOrgs) {
		this.teamAssignedOrgs = teamAssignedOrgs;
	}
	
	public Set getAmpTeams() {
		return ampTeams;
	}

	public void setAmpTeams(Set ampTeams) {
		this.ampTeams = ampTeams;
	}
	
	public String getAccessType()
	{
		return this.accessType;
	}
	
	public Long getActivitiesRejectedByFilter() {
		return activitiesRejectedByFilter;
	}

	private void setActivitiesRejectedByFilter(Long activitiesRejectedByFilter) {
		this.activitiesRejectedByFilter = activitiesRejectedByFilter;
	}
	
}
