package org.digijava.kernel.ampapi.endpoints.activity;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.WorkspaceFilter;
import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.kernel.ampapi.endpoints.activity.dto.ActivitySummary;
import org.digijava.kernel.ampapi.endpoints.dto.MultilingualContent;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.UserUtils;
import org.digijava.module.aim.dbentity.AmpActivityFields;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.hibernate.jdbc.Work;
import org.hibernate.type.LongType;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Project List generation class
 *
 * @author Emanuel Perez
 */
public class ProjectList {

    private static ProjectListCacher cacher = new ProjectListCacher();
    private static final Logger LOGGER = Logger.getLogger(ProjectList.class);

    /**
     * Gets the List <JsonBean> of activities the user can and can't view, edit
     * using a LRU caching mechanism. The pid is used as the cache key.
     *
     * @param pid current pagination request reference (random id) to keep a snapshot for the pagination chunks
     * @param tm TeamMember, current logged user
     * @return the Collection <JsonBean> with the list of activities for the user
     */
    public static Collection<ActivitySummary> getActivityList(String pid, TeamMember tm) {
        Collection<ActivitySummary> projectList = null;
        if (pid != null) {
            projectList = cacher.getCachedProjectList(pid);
        }
        if (projectList == null) {
            projectList = getActivityList(tm);
            if (pid != null) {
                cacher.addCachedProjectList(pid, projectList);
            }
        }
        return projectList;
    }

    /**
     * Builds full project list of activities with clarification on rights to edit for the given team member
     * @param tm the team member
     * @return the list with a short JSON description for each activity
     */
    public static Collection<ActivitySummary> getActivityList(TeamMember tm) {

        Map<String, ActivitySummary> activityMap = new HashMap<String, ActivitySummary>();
        List<ActivitySummary> viewableActivities = new ArrayList<ActivitySummary>();
        List<ActivitySummary> editableActivities = new ArrayList<ActivitySummary>();

        final List<Long> viewableIds = getViewableActivityIds(tm);
        List<Long> editableIds = ActivityUtil.getEditableActivityIdsNoSession(tm);

        // get list of the workspaces where the user is member of and can edit each activity
        Map<Long, Set<Long>> activitiesWs = getEditableWorkspacesForActivities(tm);

        List<ActivitySummary> notViewableActivities =
                getActivitiesByIds(viewableIds, activitiesWs, tm, false, false);

        if (viewableIds.size() > 0) {
            viewableIds.removeAll(editableIds);
            viewableActivities = getActivitiesByIds(viewableIds, activitiesWs, tm, true, true);
        }

        if (editableIds.size() > 0) {
            editableActivities = getActivitiesByIds(editableIds, activitiesWs, tm, true, true);
        }

        populateActivityMap(activityMap, editableActivities);
        populateActivityMap(activityMap, notViewableActivities);
        populateActivityMap(activityMap, viewableActivities);

        return activityMap.values();
    }

    public static Map<Long, Set<Long>> getEditableWorkspacesForActivities(TeamMember tm) {
        Map<Long, Set<Long>> activitiesWs = new HashMap<>();
        User currentUser = UserUtils.getUserByEmailAddress(tm.getEmail());
        Collection<AmpTeamMember> currentTeamMembers = TeamMemberUtil.getAllAmpTeamMembersByUser(currentUser);

        for (AmpTeamMember atm : currentTeamMembers) {
            TeamMemberUtil.getActivitiesWsByTeamMember(activitiesWs, atm);
        }

        return activitiesWs;
    }



    private static void populateActivityMap(Map<String, ActivitySummary> activityMap,
            List<ActivitySummary> activities) {

        for (ActivitySummary activity : activities) {
            ActivitySummary activityOnMap = activityMap.get(activity.getAmpId());
            // if it is not on the map, or activity is a newer
            // version than the one already on the Map then we put it on the Map
            if (activityOnMap == null || compareTo(activity, activityOnMap) > 0) {
                activityMap.put(activity.getAmpId(), activity);
            }
        }
    }

    private static int compareTo(ActivitySummary a1, ActivitySummary a2) {
        int r = ((Long) a1.getAmpActivityId()).compareTo((Long) a2.getAmpActivityId());
        if (r == 0) {
            r = a1.getAmpActivityGroup().getVersion().compareTo(a2.getAmpActivityGroup().getVersion());
        }
        return r;
    }

    /**
     * Gets the list of ids of the activities that the logged user can view.
     *
     * @param tm Logged teamMember
     * @return the List<Long> of ids of the activities that the logged user can view.
     */
    public static List<Long> getViewableActivityIds(TeamMember tm) {
        List<Long> viewableActivityIds = new ArrayList<Long>();
        if (tm != null) {
            User user = UserUtils.getUserByEmailAddress(tm.getEmail());
            // Gets the list of all the workspaces that the current logged user is a member
            Collection<AmpTeamMember> teamMemberList = TeamMemberUtil.getAllAmpTeamMembersByUser(user);

            // for every workspace generate the workspace query to get the activities.
            final String query = WorkspaceFilter.getViewableActivitiesIdByTeams(teamMemberList);
            viewableActivityIds = PersistenceManager.getSession().createNativeQuery(query)
                    .addScalar("amp_activity_id", LongType.INSTANCE).list();
        }
        return viewableActivityIds;
    }

    /**
     * Returns all AmpActivityVersions from AMP that are included/excluded from
     * the activityIds parameter
     *
     * @param include whether to include or exclude the ids of the List<Long> activityIds into the result
     * @param activityIds List with the ids (amp_activity_id) of the activities to include or exclude
     * @param activitiesWs
     * @param viewable whether the list of activities is viewable or not
     * @return List <JsonBean> of the activities generated from including/excluding the List<Long> of activityIds
     */
    public static List<ActivitySummary> getActivitiesByIds(final List<Long> activityIds,
            final Map<Long, Set<Long>> activitiesWs, final TeamMember tm,
            final boolean include, final boolean viewable) {
        final List<ActivitySummary> activitiesList = new ArrayList<ActivitySummary>();

        PersistenceManager.getSession().doWork(new Work() {
            @Override
            public void execute(Connection conn) throws SQLException {
                String ids = StringUtils.join(activityIds, ",");
                String negate = include ? "" : " NOT ";
                String query = "SELECT act.amp_activity_id as amp_activity_id, act.amp_id as amp_id, act.name as name, "
                        + "act.date_created as date_created, "
                        + "act.iati_identifier as iati_identifier, act.date_updated as date_updated, "
                        + "at.name as team_name "
                        + "FROM amp_activity act "
                        + "JOIN amp_team at ON act.amp_team_id = at.amp_team_id ";

                if (activityIds.size() > 0) {
                    query += " WHERE act.amp_activity_id " + negate + " in (" + ids + ")";
                }

                try (RsInfo rsi = SQLUtils.rawRunQuery(conn, query, null)) {
                    ResultSet rs = rsi.rs;
                    while (rs.next()) {
                        long actId = rs.getLong("amp_activity_id");
                        Set<Long> workspaces = activitiesWs.containsKey(actId) ? activitiesWs.get(actId) : null;
                        boolean editable = workspaces != null ? workspaces.contains(tm.getTeamId()) : false;

                        ActivitySummary as = new ActivitySummary();
                        as.setAmpActivityId(actId);
                        as.setCreatedDate(rs.getTimestamp("date_created"));
                        as.setName(getTranslatableFieldValue("name", rs.getString("name"), actId));
                        as.setIatiIdentifier(rs.getString("iati_identifier"));
                        as.setUpdatedDate(rs.getTimestamp("date_updated"));
                        as.setAmpId(rs.getString("amp_id"));
                        as.setWorkspaces(workspaces);
                        as.setEditable(editable);
                        as.setViewable(viewable);

                        activitiesList.add(as);
                    }
                    rs.close();
                }
            }
        });
        return activitiesList;
    }

    /**
     * Transforms and activity into Project List format
     * @param a         the activity
     * @param editable  true if it can be edited in the current workspaces
     * @param viewable  true if it can be viewed from any user workspace
     * @return JsonBean representation of the activity in Project List format
     */
    public static ActivitySummary getActivityInProjectListFormat(AmpActivityVersion a, boolean editable,
            boolean viewable) {
        ActivitySummary as = new ActivitySummary();
        as.setAmpActivityId(a.getIdentifier());
        as.setCreatedDate(a.getCreatedDate());
        as.setName(getTranslatableFieldValue("name", a.getName(), (Long) a.getIdentifier()));
        as.setIatiIdentifier(a.getIatiIdentifier());
        as.setUpdatedDate(a.getUpdatedDate());
        as.setAmpId(a.getAmpId());
        as.setAmpActivityGroup(a.getAmpActivityGroup());
        as.setEditable(editable);
        as.setViewable(viewable);
        return as;
    }

    /**
     * Gets a object containing the values for requested languages.
     * In fact the method returns a Map<String, String>, where the key is the code of the language and
     * the value in that language.
     * The keys (languages) is a reunion of language codes containing the default_locale, language parameter and
     * translations parameter.
     *
     * @param fieldName name of the field
     * @param fieldValue value of the object
     * @param parentObjectId the object id of the activity
     * @return MultilingualContent with translated values
     */
    public static MultilingualContent getTranslatableFieldValue(String fieldName, String fieldValue,
            Long parentObjectId) {

        try {
            Field field = AmpActivityFields.class.getDeclaredField(fieldName);
            boolean isMultilingual = TranslationSettings.getCurrent().isMultilingual();

            Object content = ActivityTranslationUtils.getTranslationValues(field, AmpActivityVersion.class, fieldValue,
                    parentObjectId);
            return new MultilingualContent(content, isMultilingual);
        } catch (Exception e) {
            LOGGER.error("Couldn't translate the field value", e);
            throw new RuntimeException(e);
        }
    }
}
