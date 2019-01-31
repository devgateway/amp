package org.dgfoundation.amp.ar;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import com.google.common.base.Joiner;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.ar.dbentity.FilterDataSetInterface;
import org.dgfoundation.amp.newreports.ReportEnvBuilder;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.ar.util.FilterUtil;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;

/**
 * Returns activities visible to any team member or anonymous user.
 */
public final class WorkspaceFilter {

    private AmpTeam team;
    private Set<AmpTeam> relatedTeams;
    private boolean accessTypeManagement;

    /**
     * @param teamId if null we're in public view, otherwise we're filtering activities from that team
     */
    private WorkspaceFilter(Long teamId) {
        this.team = TeamUtil.getAmpTeam(teamId);
        this.accessTypeManagement = inPublicView() || "Management".equals(team.getAccessType());
        prepareTeams();
    }
    
    private void prepareTeams() {
        List<AmpTeam> leafTeams;
        if (inPublicView()) {
            // special case: simulate like "all management workspaces" has been selected
            leafTeams = TeamUtil.getAllManagementWorkspaces();
        } else {
            leafTeams = Collections.singletonList(team);
        }
        relatedTeams = TeamUtil.getRelatedTeamsForTeams(leafTeams);
    }

    private boolean inPublicView() {
        return team == null;
    }

    /**
     * Return query for visible activities.
     */
    private String getGeneratedQuery() {
        if (accessTypeManagement) {
            return getManagementWorkspaceQuery();
        } else {
            if (AmpARFilter.isTrue(team.getComputation())) {
                return getComputedWorkspaceQuery(team);
            } else {
                return getNormalWorkspaceQuery(team.getAmpTeamId());
            }
        }
    }

    /**
     * Returns query for activities in one or more management workspaces.
     */
    private String getManagementWorkspaceQuery() {
        String approvalStatus = Util.toCSString(AmpARFilter.VALIDATED_ACTIVITY_STATUS);
        String activityTable = inPublicView() ? "cached_amp_activity" : "amp_activity";
        String teamIds = Util.toCSStringForIN(relatedTeams);
        return String.format("SELECT amp_activity_id "
                + "FROM %s "
                + "WHERE approval_status IN (%s) "
                + "AND draft<>true "
                + "AND amp_team_id IN (%s)",
                activityTable,
                approvalStatus,
                teamIds);
    }

    /**
     * Returns query for activities created in this computed workspace as well activities included by filters.
     */
    private String getComputedWorkspaceQuery(AmpTeam team) {
        Set<Long> ids;

        if (AmpARFilter.isTrue(team.getUseFilter())) {
            ids = getActivitiesByFilter(team);
        } else {
            ids = getActivitiesByOrgs(team.getOrganizations());
        }

        // remove draft activities at end since filters don't not know of this condition
        if (team.getHideDraftActivities()) {
            String draftActsSql = "SELECT amp_activity_id FROM amp_activity WHERE draft = TRUE";
            Set<Long> draftActivities = ActivityUtil.fetchLongs(draftActsSql);
            ids.removeAll(draftActivities);
        }

        // remove activities from isolated workspaces
        if (!team.getIsolated()) {
            String privateActsQuery = "SELECT a.amp_activity_id "
                    + "FROM amp_activity a, amp_team t "
                    + "WHERE a.amp_team_id = t.amp_team_id "
                    + "AND t.isolated = TRUE";
            Set<Long> privateActs = ActivityUtil.fetchLongs(privateActsQuery);
            ids.removeAll(privateActs);
        }

        return getNormalWorkspaceQuery(team.getAmpTeamId())
                + " OR amp_activity_id IN (" + Util.toCSStringForIN(ids) + ")";
    }

    /**
     * Get activities for a normal workspace.
     * Note: This does not account filters of computed workspaces.
     */
    private String getNormalWorkspaceQuery(Long ampTeamId) {
        return "SELECT amp_activity_id FROM amp_activity WHERE amp_team_id = " + ampTeamId;
    }

    /**
     * Filter activities in global context.
     */
    private Set<Long> getActivitiesByFilter(FilterDataSetInterface filter) {
        AmpARFilter af = FilterUtil.buildFilterFromSource(filter);
        return ActivityFilter.getInstance().filter(af, ReportEnvBuilder.global());
    }

    private Set<Long> getActivitiesByOrgs(Set orgs) {
        String orgsClause = Util.toCSStringForIN(orgs);

        String query = " SELECT DISTINCT(aor.activity) "
                + "FROM amp_org_role aor, amp_activity a "
                + "WHERE aor.organisation IN (" + orgsClause + ") "
                + "AND aor.activity = a.amp_activity_id "
                + "AND a.amp_team_id IS NOT NULL "
                + "UNION "
                + "SELECT DISTINCT(af.amp_activity_id) "
                + "FROM amp_funding af, amp_activity b "
                + "WHERE af.amp_donor_org_id IN (" + orgsClause + ") "
                + "AND af.amp_activity_id = b.amp_activity_id "
                + "AND b.amp_team_id IS NOT NULL";

        return ActivityUtil.fetchLongs(query);
    }

    /**
     * Returns SQL query for activities visible by specified team. If teamId is null then return public view.
     */
    public static String generateWorkspaceFilterQueryForTeam(Long teamId) {
        return new WorkspaceFilter(teamId).getGeneratedQuery();
    }
    
    /**
     * This method return a set with the teams needed for pledges related activities column filter.
     */
    public static Set getAmpTeamsSet(Long teamMemberId) {
        Long teamId;
        if (teamMemberId.equals(AmpARFilter.TEAM_MEMBER_ALL_MANAGEMENT_WORKSPACES)) {
            teamId = null;
        } else {
            teamId = TeamMemberUtil.getAmpTeamMember(teamMemberId).getAmpTeam().getAmpTeamId();
        }
        return new WorkspaceFilter(teamId).relatedTeams;
    }
    
    /**
     * entry point for getting "current user's workspace filter"
     * @return a SQL query which generates a list of AmpActivityIds
     */
    public static String getWorkspaceFilterQuery(HttpSession session) {
        TeamMember tm = (TeamMember) session.getAttribute("currentMember");
        return generateWorkspaceFilterQuery(tm);
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

    public static String generateWorkspaceFilterQuery(TeamMember tm) {
        if (tm == null) {
            return generateWorkspaceFilterQueryForTeam(null);
        } else {
            return generateWorkspaceFilterQueryForTeam(tm.getTeamId());
        }
    }

    public static String getViewableActivitiesIdByTeams(Collection<AmpTeamMember> teamMemberList) {
        StringBuilder finalActivityQuery = new StringBuilder();
        for (AmpTeamMember teamMember : teamMemberList) {
            TeamMember aux = new TeamMember(teamMember);
            finalActivityQuery.append(WorkspaceFilter.generateWorkspaceFilterQuery(aux));
            finalActivityQuery.append(" UNION ");
        }
        int index = finalActivityQuery.lastIndexOf("UNION");
        return  finalActivityQuery.substring(0, index);
    }   

    /**
     * For current workspace (if it's computed WS) first gets list of related activities,
     * Then returns list of related workspaces where all there activities belong to
     * @return
     */
    public static Set<AmpTeam> getComputedRelatedWorkspaces() {
        String wsQuery = getWorkspaceFilterQuery(TLSUtils.getRequest().getSession());
        List res = PersistenceManager.getSession().createSQLQuery(wsQuery).list();
        if (res != null && !res.isEmpty()) {
            String activitiesQuery = "select amp_team_id from amp_activity_Version where amp_activity_id IN (" +
                    Joiner.on(',').join(res) + ")";
            List<Number> teamIds = PersistenceManager.getSession().createSQLQuery(activitiesQuery).list();
            return TeamUtil.getRelatedTeamsForTeamsById(teamIds);
        }
        
        return null;
    }
}
