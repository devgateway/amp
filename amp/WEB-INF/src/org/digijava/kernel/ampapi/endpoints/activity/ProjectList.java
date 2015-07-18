package org.digijava.kernel.ampapi.endpoints.activity;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.WorkspaceFilter;
import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.UserUtils;
import org.digijava.module.aim.annotations.interchange.ActivityFieldsConstants;
import org.digijava.module.aim.dbentity.AmpActivityFields;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.hibernate.jdbc.Work;

import clover.org.apache.commons.lang.StringUtils;

/**
 * Project List generation class
 * @author Emanuel Perez
 * 
 */
public class ProjectList {
	
	private static ProjectListCacher cacher = new ProjectListCacher();
	public static final Logger LOGGER = Logger.getLogger(ProjectList.class);
	
	/**
	 * Gets the List <JsonBean> of activities the user can and can't view, edit
	 * using a LRU caching mechanism. The pid is used as the cache key.
	 * 
	 * @param pid current pagination request reference (random id) to keep a snapshot for the pagination chunks
	 * @param tm TeamMember, current logged user 
	 * @return the Collection <JsonBean> with the list of activities for the user
	 */
	public static Collection<JsonBean> getActivityList(String pid, TeamMember tm) {
		Collection<JsonBean> projectList = null;
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
	
	public static Collection<JsonBean> getActivityList(TeamMember tm) {
		Map<String, JsonBean> activityMap = new HashMap<String, JsonBean>();
		List<JsonBean> viewableActivities = new ArrayList<JsonBean>();
		List<JsonBean> editableActivities = new ArrayList<JsonBean>();
		final List<Long> viewableIds = getViewableActivityIds(tm);
		List<Long> editableIds = getEditableActivityIds();
		List<JsonBean> notViewableActivities = getActivitiesByIds(viewableIds, false, false, false);
		if (viewableIds.size() > 0) {
			viewableIds.removeAll(editableIds);
			viewableActivities = getActivitiesByIds(viewableIds, true, true, false);
		}
		if (editableIds.size() > 0) {
			
			editableActivities = getActivitiesByIds(editableIds, true, true, !TeamMemberUtil.isManagementWorkspace(tm));
		}
		populateActivityMap(activityMap, editableActivities);
		populateActivityMap(activityMap, notViewableActivities);
		populateActivityMap(activityMap, viewableActivities);
		return activityMap.values();
	}

	private static void populateActivityMap(Map<String, JsonBean> activityMap, List<JsonBean> activities) {
		for (JsonBean activity : activities) {
			JsonBean activityOnMap = activityMap.get((String) activity.get(InterchangeUtils.underscorify(ActivityFieldsConstants.AMP_ID)));
			// if it is not on the map, or activity is a newer
			// version than the one already on the Map
			// then we put it on the Map
			if (activityOnMap == null
					|| (Long) activity.get(InterchangeUtils.underscorify(ActivityFieldsConstants.AMP_ACTIVITY_ID)) > (Long) activityOnMap
							.get(InterchangeUtils.underscorify(ActivityFieldsConstants.AMP_ACTIVITY_ID))) {
				activityMap.put((String) activity.get(InterchangeUtils.underscorify(ActivityFieldsConstants.AMP_ID)), activity);
			}
		}
	}

	/**
	 * Get the activities ids for the current workspace
	 * 
	 * @param session HttpSession
	 * @return List<Long> with the editable activity Ids
	 */
	public static List<Long> getEditableActivityIds() {
		HttpSession session = TLSUtils.getRequest().getSession();
		String query = WorkspaceFilter.getWorkspaceFilterQuery(session);
		return PersistenceManager.getSession().createSQLQuery(query).list();

	}

	/**
	 * Gets the list of ids of the activities that the logged user can view.
	 * 
	 * @param tm Logged teamMember
	 * @return the List<Long> of ids of the activities that the logged user can view.
	 */
	public static List<Long> getViewableActivityIds(TeamMember tm) {
		List<Long> viewableActivityIds = new ArrayList<Long>();
		try {

			User user = UserUtils.getUserByEmail(tm.getEmail());
			// Gets the list of all the workspaces that the current logged user
			// is a member
			Collection<AmpTeamMember> teamMemberList = TeamMemberUtil.getAllAmpTeamMembersByUser(user);
			
			// for every workspace generate the workspace query to get the
			// activities.
			final String query = WorkspaceFilter.getViewableActivitiesIdByTeams( teamMemberList);
			viewableActivityIds = PersistenceManager.getSession().createSQLQuery(query).list();
		} catch (DgException e1) {
			LOGGER.warn("Couldn't generate the List of viewable activity ids", e1);
			throw new RuntimeException(e1);
		}
		return viewableActivityIds;
	}



	/**
	 * Returns all AmpActivityVersions from AMP that are included/excluded from
	 * the activityIds parameter
	 * 
	 * @param include whether to include or exclude the ids of the List<Long> activityIds into the result
	 * @param activityIds List with the ids (amp_activity_id) of the activities to include or exclude
	 * @param viewable whether the list of activities is viewable or not
	 * @param editable whether the list of activities is editable or not
	 * @return List <JsonBean> of the activities generated from including/excluding the List<Long> of activityIds
	 */
	public static List<JsonBean> getActivitiesByIds(final List<Long> activityIds, final boolean include,
			final boolean viewable, final boolean editable) {
		final List<JsonBean> activitiesList = new ArrayList<JsonBean>();

		PersistenceManager.getSession().doWork(new Work() {
			public void execute(Connection conn) throws SQLException {
				String ids = StringUtils.join(activityIds, ",");
				String negate = "";
				if (!include) {
					negate = " NOT ";
				}
				String allActivitiesQuery = "SELECT amp_activity_id,amp_id,name,date_created,project_code,date_updated from amp_activity ";
				if (activityIds.size() > 0) {
					allActivitiesQuery += " where amp_activity_id " + negate + " in (" + ids + ")";
				}
				try (RsInfo rsi = SQLUtils.rawRunQuery(conn, allActivitiesQuery, null)) {
					ResultSet rs = rsi.rs;
					while (rs.next()) {
						JsonBean bean = new JsonBean();
						bean.set(InterchangeUtils.underscorify(ActivityFieldsConstants.AMP_ACTIVITY_ID), rs.getLong("amp_activity_id"));
						bean.set(InterchangeUtils.underscorify(ActivityFieldsConstants.CREATED_DATE), InterchangeUtils.formatISO8601Date(rs.getTimestamp("date_created")));
						bean.set(InterchangeUtils.underscorify(ActivityFieldsConstants.PROJECT_TITLE), getTranslatableFieldValue("name", rs.getString("name"), rs.getLong("amp_activity_id")));
						bean.set(InterchangeUtils.underscorify(ActivityFieldsConstants.PROJECT_CODE), rs.getString("project_code"));
						bean.set(InterchangeUtils.underscorify(ActivityFieldsConstants.UPDATE_DATE), InterchangeUtils.formatISO8601Date(rs.getTimestamp("date_updated")));
						bean.set(InterchangeUtils.underscorify(ActivityFieldsConstants.AMP_ID), rs.getString("amp_id"));
						bean.set(ActivityEPConstants.EDIT, editable);
						bean.set(ActivityEPConstants.VIEW, viewable);
						activitiesList.add(bean);
					}
					rs.close();
				}
			}
		});
		return activitiesList;
	}
	
	public static JsonBean getActivityInProjectListFormat(AmpActivityVersion a) {
		JsonBean bean = new JsonBean();
		bean.set(InterchangeUtils.underscorify(ActivityFieldsConstants.AMP_ACTIVITY_ID), a.getIdentifier());
		bean.set(InterchangeUtils.underscorify(ActivityFieldsConstants.CREATED_DATE), InterchangeUtils.formatISO8601Date(a.getCreatedDate()));
		bean.set(InterchangeUtils.underscorify(ActivityFieldsConstants.PROJECT_TITLE), getTranslatableFieldValue("name", a.getName(), (Long) a.getIdentifier()));
		bean.set(InterchangeUtils.underscorify(ActivityFieldsConstants.PROJECT_CODE), a.getProjectCode());
		bean.set(InterchangeUtils.underscorify(ActivityFieldsConstants.UPDATE_DATE), InterchangeUtils.formatISO8601Date(a.getUpdatedDate()));
		bean.set(InterchangeUtils.underscorify(ActivityFieldsConstants.AMP_ID), a.getAmpId());
		bean.set(ActivityEPConstants.EDIT, true);
		bean.set(ActivityEPConstants.VIEW, true);
		return bean;
	}

	/**
	 * Gets a object containing the values for requested languages. 
	 * In fact the method returns a Map<String, String>, where the key is the code of the language and the value in that language
	 * The keys (languages) is a reunion of language codes containing the default_locale, language parameter and translations parameter
	 * 
	 * @param fieldName name of the field
	 * @param fieldValue value of the object
	 * @param parentObjectId the object id of the activity
	 * @return Object with translated values
	 */
	public static Object getTranslatableFieldValue(String fieldName, String fieldValue, Long parentObjectId) {
		try {
			Field field = AmpActivityFields.class.getDeclaredField(fieldName);
			
			return InterchangeUtils.getTranslationValues(field, AmpActivityVersion.class, fieldValue, parentObjectId);
		} catch (Exception e) {
			LOGGER.error("Couldn't translate the field value", e);
			throw new RuntimeException(e);
		} 
	}
}
