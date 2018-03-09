package org.dgfoundation.amp.ar;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import clover.com.google.common.base.Joiner;

import org.dgfoundation.amp.Util;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;

public class WorkspaceFilter 
{
    private Long teamMemberId;
    private Set teamAssignedOrgs = null;
    private Set<AmpTeam> ampTeams = null;
    private boolean accessTypeManagement;
    private boolean hideDraft;
    private boolean approved;
    //private boolean publicView;
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
    private WorkspaceFilter(Long teamMemberId, boolean accessTypeManagement, boolean approved, boolean hideDraft)
    {
        this.teamMemberId = teamMemberId;
        this.hideDraft = hideDraft;
        this.approved = approved;
        this.accessTypeManagement = accessTypeManagement;
        //this.publicView = publicView;
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
        if (teamMemberId.equals(AmpARFilter.TEAM_MEMBER_ALL_MANAGEMENT_WORKSPACES))
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
//      activityStatus.add(Constants.STARTED_STATUS);
//      String NO_MANAGEMENT_ACTIVITIES="";
                
        String used_approval_status = this.getAccessTypeManagement() ? 
                Util.toCSString(AmpARFilter.validatedActivityStatus) :          // Management workspace: validated activities only
                (approved ? // non-management workspace, but only validated activities wanted nevertheless
                        Util.toCSString(AmpARFilter.validatedActivityStatus) :
                        Util.toCSString(AmpARFilter.activityStatus) // other workspaces: all kinds of activities
                );
        
        if (this.getAccessTypeManagement()) {
            TEAM_FILTER = "SELECT amp_activity_id FROM amp_activity WHERE approval_status IN ("+used_approval_status+") AND draft<>true AND " +
                    "amp_team_id IS NOT NULL ";
            if (ampTeams != null) {
                TEAM_FILTER += " AND amp_team_id IN ("
                        + Util.toCSStringForIN(ampTeams)
                        + ") "; 
            }

            
        } else{
            TEAM_FILTER = "SELECT amp_activity_id FROM amp_activity WHERE amp_team_id IS NOT NULL ";
                    if (ampTeams != null) {
                    TEAM_FILTER += " AND amp_team_id IN ("
                            + Util.toCSStringForIN((ampTeams))
                            + ") "; }
                //+ " OR amp_activity_id IN (SELECT ata.amp_activity_id FROM amp_team_activities ata WHERE ata.amp_team_id IN ("
                //+ Util.toCSString(ampTeams) + ") )" ;
        }
        //NO_MANAGEMENT_ACTIVITIES +="SELECT amp_activity_id FROM amp_activity WHERE amp_team_id IS NOT NULL ";
        //why is this twice?
        if (ampTeams != null) {
        TEAM_FILTER += " AND amp_team_id IN ("
                + Util.toCSStringForIN((ampTeams))
                + ") "; }
            //+ " OR amp_activity_id IN (SELECT ata.amp_activity_id FROM amp_team_activities ata WHERE ata.amp_team_id IN ("
            //+ Util.toCSString(ampTeams) + ") )" ;
                
        TEAM_FILTER += "AND approval_status IN ("+used_approval_status+") ";
        String isolated_filter = " amp_activity_id IN (select amp_activity_id FROM amp_activity_version aav WHERE "
                + "aav.amp_team_id IN (select amp_team_id from amp_team WHERE isolated = true)) ";

        // computed workspace filter -- append it to the team filter so normal
        // team activities are also possible
        //AMP-4495 - in computed workspace, the unapproved or draft activities from other
        //worskpaces should not be displayed
            if (teamAssignedOrgs != null && teamAssignedOrgs.size() > 0) {
                TEAM_FILTER += " OR amp_activity_id IN (SELECT DISTINCT(aor.activity) FROM amp_org_role aor, amp_activity a WHERE aor.organisation IN ("
                        + Util.toCSStringForIN(teamAssignedOrgs) + ") AND aor.activity=a.amp_activity_id AND a.amp_team_id IS NOT NULL AND a.approval_status IN (" +
                        used_approval_status    +") ) " + (hideDraft ? "AND draft<>true ":"")
                        + String.format(" AND (NOT %s)", isolated_filter);
                TEAM_FILTER += " OR amp_activity_id IN (SELECT distinct(af.amp_activity_id) FROM amp_funding af, amp_activity b WHERE af.amp_donor_org_id IN ("
                        + Util.toCSStringForIN(teamAssignedOrgs) + ") AND af.amp_activity_id=b.amp_activity_id AND b.amp_team_id IS NOT NULL AND b.approval_status IN (" +
                        used_approval_status    +") )" + (hideDraft ? "AND draft<>true ":"")
                        + String.format(" AND (NOT %s)", isolated_filter);
            }

        if (this.teamMember !=null && this.teamMember.getTeamIsolated()) {
            String isolatedSubfilter = String.format(" amp_activity_id IN "
                    + "(SELECT amp_activity_id FROM amp_activity_version WHERE amp_team_id IN (%s))",Util.toCSStringForIN((ampTeams)));
            TEAM_FILTER = String.format("%s OR ( %s AND %s )", TEAM_FILTER, isolated_filter, isolatedSubfilter );
        }
        else {
            TEAM_FILTER = String.format("%s AND (NOT %s)", TEAM_FILTER, isolated_filter );
        }


//      int c;
//      if (hideDraft){
//          c = Math.abs( DbUtil.countActivitiesByQuery(TEAM_FILTER + " AND amp_activity_id IN (SELECT amp_activity_id FROM amp_activity WHERE (draft is null) OR (draft is false ) )",null )-DbUtil.countActivitiesByQuery(NO_MANAGEMENT_ACTIVITIES,null));
//      }
//      else
//          c = Math.abs( DbUtil.countActivitiesByQuery(TEAM_FILTER,null) - DbUtil.countActivitiesByQuery(NO_MANAGEMENT_ACTIVITIES,null) );
//      this.setActivitiesRejectedByFilter(new Long(c));
        
        //return "2435, 1163, 2498, 1301";
        //return "1163, 2498, 1301";
        //return "2498, 1301";
        //return /*"33"; */"36";
        //return "41, 43, 44, 45";
        //return "20, 21"; // masha
        //return "17041";
        //return "SELECT amp_activity_id from amp_activity WHERE name IN ('Management Capacity Development', 'Improverment of maternal health service delivery', 'Advancing Youth Project', 'CLSG-ADF', 'Malaria Care Project', 'Ebola Sector Budget Support TSF')";
        //return "select amp_activity_id from amp_activity";
        //return "SELECT amp_activity_id from amp_activity WHERE name IN ('Proposed Project Cost 1 - USD', 'Test MTEF directed', 'Eth Water')"; //"Proposed Project Cost 1 - USD", "Test MTEF directed", "Eth Water"
        //return "SELECT amp_activity_id from amp_activity WHERE name IN ('Proposed Project Cost 1 - USD', 'Proposed Project Cost 2 - EUR', 'SubNational no percentages', 'Project with documents')";
        //return "SELECT amp_activity_id from amp_activity WHERE name IN ('Proposed Project Cost 1 - USD', 'Test MTEF directed', 'Eth Water', 'activity with directed MTEFs')";
        //return "SELECT amp_activity_id from amp_activity WHERE name IN ('TAC_activity_1', 'TAC_activity_2', 'Proposed Project Cost 2 - EUR', 'Test MTEF directed', 'Eth Water')";
        //return "SELECT amp_activity_id from amp_activity WHERE name IN ('pledged education activity 1', 'pledged 2')"; //"pledged education activity 1", "pledged 2"
        //return "SELECT amp_activity_id FROM amp_activity WHERE name IN ('activity with capital spending', 'Activity with planned disbursements', 'activity with pipeline MTEFs and act. disb')";
        //return "SELECT amp_activity_id FROM amp_activity WHERE amp_activity_id IN (13755, 9790)";
        //return "SELECT amp_activity_id FROM v_ni_mtef_funding where source_role_id = 1 and amp_activity_id NOT IN (175, 176)";
        //return "SELECT amp_activity_id FROM amp_activity WHERE name IN ('SubNational no percentages')";   
        return TEAM_FILTER;
    }
    
    /**
     * take care for special values of teamMemberId, like TEAM_MEMBER_ALL_MANAGEMENT_WORKSPACES !
     * @param teamMemberId
     * @param accessType
     * @param draft
     * @return
     */
    private static String generateWorkspaceFilterQuery(Long teamMemberId, String accessType, boolean approved, boolean hideDraft)
    {
        return new WorkspaceFilter(teamMemberId, "Management".equals(accessType), approved, hideDraft).getGeneratedQuery();
    }
    
    /**
     * This method return a set with the teams needed for pledges related activities column filter.
     * @param teamMemberId
     * @param accessType
     * @param approved
     * @param hideDraft
     * @param publicView
     * @return
     */
    public static Set getAmpTeamsSet(Long teamMemberId, String accessType, boolean approved, boolean hideDraft)
    {
        return new WorkspaceFilter(teamMemberId, "Management".equals(accessType), approved, hideDraft).getAmpTeams();
    }
    
    /**
     * entry point for getting "current user's workspace filter"
     * @param session
     * @return
     */
    private static String generateWorkspaceFilterQuery(HttpSession session)
    {
         // (approval_status='approved' and draft<>true)
//      TeamMember tm = (TeamMember) session.getAttribute("currentMember");
        return generateWorkspaceFilterQuery(session, null);
    }


    
    /**
     * user entry point for getting the filter query of the current workspace
     * gets the workspace filter query from the HttpSession. If none exists, regenerates it (but shouldn't happen)
     * @param session
     * @return a SQL query which generates a list of AmpActivityIds
     */
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
     * returns true IFF an activity is visible from within a workspace
     * @param ampActivityId
     * @param tm
     * @return
     */
    public static boolean isActivityWithinWorkspace(long ampActivityId, TeamMember tm)
    {
        String str = generateWorkspaceFilterQuery(tm);
        String query = String.format("SELECT (%d IN (%s)) AS rs", ampActivityId, str);
        java.util.List<?> res = PersistenceManager.getSession().createSQLQuery(query).list();
        return (Boolean) res.get(0);
    }   
    
    /**
     * returns true IFF an activity is visible from within an workspace
     * @param ampActivityId
     * @return
     */
    public static boolean isActivityWithinWorkspace(long ampActivityId)
    {
        String str = getWorkspaceFilterQuery(TLSUtils.getRequest().getSession());
        String query = String.format("SELECT (%d IN (%s)) AS rs", ampActivityId, str);
        java.util.List<?> res = PersistenceManager.getSession().createSQLQuery(query).list();
        return (Boolean) res.get(0);
    }
    /**
     * forcedTeamMember <b>might have the special value TEAM_MEMBER_ALL_MANAGEMENT_WORKSPACES</b>
     * @param session
     * @param forcedTeamMemberId
     * @return
     */
    public static String generateWorkspaceFilterQuery(TeamMember tm)
    {

        //Hotfix for timor budget integration report
        if (tm == null || tm.getMemberName().equalsIgnoreCase("AMP Admin"))
        {
            //public view
            boolean hideDraft = true;
            boolean approved = true;
            String accessType = Constants.ACCESS_TYPE_MNGMT;
            String onlineQuery = generateWorkspaceFilterQuery(AmpARFilter.TEAM_MEMBER_ALL_MANAGEMENT_WORKSPACES, accessType, approved, hideDraft);
            String offlineQuery = AmpARFilter.getOffLineQuery(onlineQuery);
            return offlineQuery;
        }
        else
        {
            boolean hideDraft = false;
            boolean approved = hideDraft;
            
            /**
             * Checks if the team is a computed workspace and in case it is
             * it checks if it should hide the draft activities
             */
            hideDraft= TeamUtil.hideDraft(tm) || Constants.ACCESS_TYPE_MNGMT.equalsIgnoreCase(tm.getTeamAccessType());
            String accessType = tm.getTeamAccessType();
            return generateWorkspaceFilterQuery(tm.getMemberId(), accessType, approved, hideDraft);
        }
    }

    public static String getViewableActivitiesIdByTeams(Collection<AmpTeamMember> teamMemberList) {
        StringBuffer finalActivityQuery = new StringBuffer();
        for (AmpTeamMember teamMember : teamMemberList) {
            TeamMember aux = new TeamMember(teamMember);
            finalActivityQuery.append(WorkspaceFilter.generateWorkspaceFilterQuery(aux));
            finalActivityQuery.append(" UNION ");
        }
        int index = finalActivityQuery.lastIndexOf("UNION");
        return  finalActivityQuery.substring(0, index);
    }   
    
    /**
     * if forcedTeamMemberId == null, use the logged-in user (or public view). Else use the forcedTeamMember <b>which might have the special value TEAM_MEMBER_ALL_MANAGEMENT_WORKSPACES</b>
     * @param session
     * @param forcedTeamMemberId
     * @return
     */
    public static String generateWorkspaceFilterQuery(HttpSession session, Long forcedTeamMemberId)
    {
        TeamMember tm = (TeamMember) session.getAttribute("currentMember");
        if (forcedTeamMemberId != null && AmpARFilter.TEAM_MEMBER_ALL_MANAGEMENT_WORKSPACES.compareTo(forcedTeamMemberId)!=0)
            tm = TeamMemberUtil.getTeamMember(forcedTeamMemberId);

        return generateWorkspaceFilterQuery(tm);
    }

    /**
     * For current workspace (if it's computed WS) first gets list of related activities,
     * Then returns list of related workspaces where all there activities belong to
     * @return
     */
    public static Set<AmpTeam> getComputedRelatedWorkspaces() {
        String wsQuery = getWorkspaceFilterQuery(TLSUtils.getRequest().getSession());
        List<Number> res = PersistenceManager.getSession().createSQLQuery(wsQuery).list();
        if (res != null && !res.isEmpty()) {
            String activitiesQuery = "select amp_team_id from amp_activity_Version where amp_activity_id IN (" +
                    Joiner.on(',').join(res) + ")";
            List<Number> teamIds = PersistenceManager.getSession().createSQLQuery(activitiesQuery).list();
            return TeamUtil.getRelatedTeamsForTeamsById(teamIds);
        }
        
        return null;
    }

    public void setTeamAssignedOrgs(Set teamAssignedOrgs) {
        this.teamAssignedOrgs = teamAssignedOrgs;
    }
    
    public Set<AmpTeam> getAmpTeams() {
        return ampTeams;
    }

    public void setAmpTeams(Set<AmpTeam> ampTeams) {
        this.ampTeams = ampTeams;
    }
    
    public boolean getAccessTypeManagement()
    {
        return this.accessTypeManagement;
    }
    
    public Long getActivitiesRejectedByFilter() {
        return activitiesRejectedByFilter;
    }

    private void setActivitiesRejectedByFilter(Long activitiesRejectedByFilter) {
        this.activitiesRejectedByFilter = activitiesRejectedByFilter;
    }
    
}
