package org.digijava.kernel.ampapi.endpoints.interchange;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.WorkspaceFilter;
import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.visibility.data.ColumnsVisibility;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.UserUtils;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.dbentity.AmpActivityFields;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.hibernate.jdbc.Work;

import clover.org.apache.commons.lang.StringUtils;

public class InterchangeUtils {

	public static final Logger LOGGER = Logger.getLogger(InterchangeUtils.class);

	@SuppressWarnings("serial")
	protected static final Map<Class<?>, String> classToCustomType = new HashMap<Class<?>, String>() {
		{
			put(java.lang.String.class, "string");
			put(java.util.Date.class, "date");
			put(java.lang.Double.class, "float");

		}
	};

	public static String getCustomFieldType(Field field) {
		if (classToCustomType.containsKey(field.getClass())) {
			return classToCustomType.get(field.getClass());
		}
		return "bred";

	}

	public static Collection<JsonBean> getActivityList(TeamMember tm, HttpSession session) {
		Map<String, JsonBean> activityMap = new HashMap<String, JsonBean>();
		List<AmpActivityVersion> viewableActivities = new ArrayList<AmpActivityVersion>();
		List<AmpActivityVersion> editableActivities = new ArrayList<AmpActivityVersion>();
		final List<Long> viewableIds = getViewableActivityIds(tm);
		List<Long> editableIds = getEditableActivityIds(session);
		List<AmpActivityVersion> notViewableActivities = getActivitiesByIds(viewableIds, false);
		if (viewableIds.size() > 0) {
			viewableActivities = getActivitiesByIds(viewableIds, true);
		}
		if (editableIds.size() > 0) {
			editableActivities = getActivitiesByIds(editableIds, true);
		}
		for (AmpActivityVersion editable : editableActivities) {
			JsonBean activityOnMap = activityMap.get(editable.getAmpId());
			// if it is not on the map, or editable activity is a newer
			// version than the one already on the Map
			// then we put it on the Map
			if (activityOnMap == null || editable.getAmpActivityId() > (Long) activityOnMap.get("amp_activity_id")) {
				activityMap.put(editable.getAmpId(), convertToJsonBean(editable, true, true));
			}
		}
		for (AmpActivityVersion notViewable : notViewableActivities) {
			JsonBean activityOnMap = activityMap.get(notViewable.getAmpId());
			if (activityOnMap == null || notViewable.getAmpActivityId() > (Long) activityOnMap.get("amp_activity_id")) {
				activityMap.put(notViewable.getAmpId(), convertToJsonBean(notViewable, false, false));
			}
		}
		for (AmpActivityVersion viewable : viewableActivities) {
			JsonBean activityOnMap = activityMap.get(viewable.getAmpId());
			if (activityOnMap == null || viewable.getAmpActivityId() > (Long) activityOnMap.get("amp_activity_id")) {
				activityMap.put(viewable.getAmpId(), convertToJsonBean(viewable, true, false));
			}
		}
		return activityMap.values();
	}

	/**
	 * Get the activities ids for the current workspace
	 * 
	 * @param session HttpSession
	 * @return List<Long> with the editable activity Ids
	 */
	private static List<Long> getEditableActivityIds(HttpSession session) {
		String query = WorkspaceFilter.getWorkspaceFilterQuery(session);
		return PersistenceManager.getSession().createSQLQuery(query).list();

	}

	public static JsonBean convertToJsonBean(AmpActivityVersion activity, boolean viewable, boolean editable) {
		JsonBean bean = new JsonBean();
		bean.set("amp_activity_id", activity.getAmpActivityId());
		bean.set("created_date", activity.getCreatedDate());
		bean.set("title", activity.getName());
		bean.set("project_code", activity.getProjectCode());
		bean.set("update_date", activity.getUpdatedDate());
		bean.set("amp_id", activity.getAmpId());
		bean.set("edit", editable);
		bean.set("view", viewable);
		return bean;
	}

	/**
	 * Gets the list of ids of the activities that the logged user can view.
	 * 
	 * @param tm
	 *            Logged teamMember
	 * @return the List<Long> of ids of the activities that the logged user can
	 *         view.
	 */
	private static List<Long> getViewableActivityIds(TeamMember tm) {
		final List<Long> viewableActivityIds = new ArrayList<Long>();
		try {
			StringBuffer finalActivityQuery = new StringBuffer();
			User user = UserUtils.getUserByEmail(tm.getEmail());
			// Gets the list of all the workspaces that the current logged user
			// is a member
			Collection<AmpTeamMember> teamMemberList = TeamMemberUtil.getAllAmpTeamMembersByUser(user);

			// for every workspace generate the workspace query to get the
			// activities.
			for (AmpTeamMember teamMember : teamMemberList) {
				TeamMember aux = new TeamMember(teamMember);
				finalActivityQuery.append(WorkspaceFilter.generateWorkspaceFilterQuery(aux));
				finalActivityQuery.append(" UNION ");
			}
			int index = finalActivityQuery.lastIndexOf("UNION");
			final String query = finalActivityQuery.substring(0, index);
			PersistenceManager.getSession().doWork(new Work() {
				public void execute(Connection conn) throws SQLException {
					try (RsInfo rsi = SQLUtils.rawRunQuery(conn, query, null)) {
						ResultSet rs = rsi.rs;
						while (rs.next()) {
							Long id = rs.getLong("amp_activity_id");
							viewableActivityIds.add(id);
						}
					}

				}
			});
		} catch (DgException e1) {
			LOGGER.warn("Couldn't generate the List of viewable activity ids", e1);
		}
		return viewableActivityIds;
	}

	/**
	 * Returns all AmpActivityVersions from AMP that are included/excluded from
	 * the activityIds parameter
	 * 
	 * @param include
	 *            whether to include or exclude the ids of the List<Long>
	 *            activityIds into the result
	 * @param activityIds
	 *            List with the ids (amp_activity_id) of the activities to
	 *            include or exclude
	 * @return List <AmpActivityVersion> of the activities generated from
	 *         including/excluding the List<Long> of activityIds
	 */
	private static List<AmpActivityVersion> getActivitiesByIds(final List<Long> activityIds, final boolean include) {
		final List<AmpActivityVersion> activitiesList = new ArrayList<AmpActivityVersion>();

		PersistenceManager.getSession().doWork(new Work() {
			public void execute(Connection conn) throws SQLException {
				String ids = StringUtils.join(activityIds, ",");
				String negate = "";
				if (!include) {
					negate = " NOT ";
				}
				String allActivitiesQuery = "SELECT amp_activity_id,amp_id,name,date_created,project_code,date_updated from amp_activity_version ";
				if (activityIds.size() > 0) {
					allActivitiesQuery += " where amp_activity_id " + negate + " in (" + ids + ")";
				}
				try (RsInfo rsi = SQLUtils.rawRunQuery(conn, allActivitiesQuery, null)) {
					ResultSet rs = rsi.rs;
					while (rs.next()) {
						AmpActivityVersion activity = new AmpActivityVersion();
						activity.setName(rs.getString("name"));
						activity.setAmpId(rs.getString("amp_id"));
						activity.setCreatedDate(rs.getDate("date_created"));
						activity.setProjectCode(rs.getString("project_code"));
						activity.setUpdatedDate(rs.getDate("date_updated"));
						activity.setAmpActivityId(rs.getLong("amp_activity_id"));
						activitiesList.add(activity);
					}
				}

			}
		});
		return activitiesList;
	}

	public static JsonBean getAllAvailableFields() {
		JsonBean result = new JsonBean();

		Set<String> visibleColumnNames = ColumnsVisibility.getVisibleColumns();
		Field[] fields = AmpActivityFields.class.getDeclaredFields();

		List<Field> exportableFields = new ArrayList<Field>();
		for (Field field : fields) {
			if (field.getAnnotation(Interchangeable.class) != null) {
				exportableFields.add(field);
			}
		}

		for (Field field : exportableFields) {
			result.set(field.getName(), field.getType().toString());
		}

		// for (String col : visibleColumnNames) {
		// result.set(, value);
		// // fieldSet.contains(arg0)
		// // result.set(col, );
		// }
		return result;
	}

}
