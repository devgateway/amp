package org.dgfoundation.amp.ar;

import java.util.*;

import javax.servlet.http.HttpSession;

import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
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
	private boolean draft;
	private long activitiesRejectedByFilter;
	
	/**
	 * take care for special values of teamMemberId, like TEAM_MEMBER_ALL_MANAGEMENT_WORKSPACES !
	 * @param teamMemberId
	 * @param accessType
	 * @param draft
	 */
	public WorkspaceFilter(Long teamMemberId, String accessType, boolean draft)
	{
		this.teamMemberId = teamMemberId;
		this.draft = draft;
		this.accessType = accessType;
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
		TeamMember tm = teamMemberId == null ? null : TeamMemberUtil.getTeamMember(teamMemberId);
		if (tm != null) {
			this.setAmpTeams(TeamUtil.getRelatedTeamsForMember(tm));
			fillTeamAO();
		}
		else {
			// nothing to do?
		}
	}
	
	public String getGeneratedQuery()
	{
		String TEAM_FILTER = "";
		
		//new computed filter - after permissions #3167
		//AMP-3726
//		activityStatus.add(Constants.STARTED_STATUS);
		String NO_MANAGEMENT_ACTIVITIES="";
		

		if("Management".equals(this.getAccessType()))
			TEAM_FILTER = "SELECT amp_activity_id FROM amp_activity WHERE approval_status IN ("+Util.toCSString(AmpARFilter.activityStatus)+") AND draft<>true AND " +
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
			

	// computed workspace filter -- append it to the team filter so normal
	// team activities are also possible
		//AMP-4495 - in computed workspace, the unapproved or draft activities from other
		//worskpaces should not be displayed
			if (teamAssignedOrgs != null && teamAssignedOrgs.size() > 0) {
				
				TEAM_FILTER += " OR amp_activity_id IN (SELECT DISTINCT(aor.activity) FROM amp_org_role aor, amp_activity a WHERE aor.organisation IN ("
						+ Util.toCSString(teamAssignedOrgs) + ") AND aor.activity=a.amp_activity_id AND a.amp_team_id IS NOT NULL AND a.approval_status IN (" +
						Util.toCSString(AmpARFilter.activityStatus)	+")  ) " + (draft?"AND draft<>true ":"");
				TEAM_FILTER += " OR amp_activity_id IN (SELECT distinct(af.amp_activity_id) FROM amp_funding af, amp_activity b WHERE af.amp_donor_org_id IN ("
						+ Util.toCSString(teamAssignedOrgs) + ") AND af.amp_activity_id=b.amp_activity_id AND b.amp_team_id IS NOT NULL AND b.approval_status IN (" +
						Util.toCSString(AmpARFilter.activityStatus)	+")  ) " + (draft?"AND draft<>true ":"");
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
			if(draft){
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
	public static String getWorkspaceFilterQuery(Long teamMemberId, String accessType, boolean draft)
	{
		return new WorkspaceFilter(teamMemberId, accessType, draft).getGeneratedQuery();
	}
	
	/**
	 * entry point for getting "current user's workspace filter"
	 * @param session
	 * @return
	 */
	public static String getWorkspaceFilterQuery(HttpSession session)
	{
		TeamMember tm = (TeamMember) session.getAttribute("currentMember");
		if (tm == null)
		{
			//public view
			return getWorkspaceFilterQuery(AmpARFilter.TEAM_MEMBER_ALL_MANAGEMENT_WORKSPACES, "Management", false);
		}
		else
		{
			boolean useDraft = Constants.ACCESS_TYPE_MNGMT.equalsIgnoreCase(tm.getTeamAccessType()) ||
					"Donor".equalsIgnoreCase(tm.getTeamType());
			return getWorkspaceFilterQuery(tm.getMemberId(), tm.getTeamAccessType(), useDraft);
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
