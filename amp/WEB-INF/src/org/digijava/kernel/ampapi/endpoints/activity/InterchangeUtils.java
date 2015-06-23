package org.digijava.kernel.ampapi.endpoints.activity;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.management.RuntimeErrorException;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.WorkspaceFilter;
import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.visibility.data.ColumnsVisibility;
import org.digijava.kernel.ampapi.endpoints.activity.visibility.FMVisibility;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.UserUtils;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.Validators;
import org.digijava.module.aim.dbentity.AmpActivityFields;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.time.StopWatch;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.hibernate.jdbc.Work;
import org.springframework.util.ClassUtils;

import clover.org.apache.commons.lang.StringUtils;
import clover.org.apache.log4j.helpers.ISO8601DateFormat;

public class InterchangeUtils {

	private static final String NOT_REQUIRED = "_NONE_";
	private static final String ALWAYS_REQUIRED = "_ALWAYS_";
	public static final Logger LOGGER = Logger.getLogger(InterchangeUtils.class);
	private static final ISO8601DateFormat dateFormatter = new ISO8601DateFormat();
	private static ProjectListCacher cacher = new ProjectListCacher();

	@SuppressWarnings("serial")
	protected static final Map<Class<?>, String> classToCustomType = new HashMap<Class<?>, String>() {
		{
			put(java.lang.String.class, "string");
			put(java.util.Date.class, "date");
			put(java.lang.Double.class, "float");
			put(java.lang.Boolean.class, "boolean");
			put(java.lang.Long.class, "string");
//			put(java.lang.Long.class, "int");
			put(java.lang.Float.class, "float");
//			put(AmpCategoryValue.class, "string");

		}
	};
	
	private static Set<Class<?>> JSON_SUPPORTED_CLASSES = new HashSet<Class<?>>() {
	    {
	        add(Boolean.class);
	        add(Character.class);
	        add(Byte.class);
	        add(Short.class);
	        add(Integer.class);
	        add(Long.class);
	        add(Float.class);
	        add(Double.class);
	        add (String.class);
	        add (Date.class);
	    }
	};

/**
 * Picks available translations for a string (supposedly field name)
 * @param fieldName the field name to be translated
 * @return a map from the ISO2 code -> translation in said text
 */
	private static Map<String, String> getLabelsForField(String fieldName) {
		Map<String, String> translations = new HashMap<String, String>();
		try {
			Collection<Message> messages = TranslatorWorker.getAllTranslationOfBody(fieldName, Long.valueOf(3));
			for (Message m : messages) {
				translations.put(m.getLocale(), m.getMessage());
			}
			if (translations.isEmpty())
			{
				translations.put("EN", fieldName);
			}
		} catch (WorkerException e) {
			// MEANINGFUL ERROR HERE
		}
		return translations;
	}

	/**
	 * transforms a Map<String,String> to a JsonBean with equal structure
	 * @param map the map to be transformed
	 * @return a JsonBean of the structure
	 * {"\<code1\>":"\<translation1\>", 
	 * 	"\<code2\>":"\<translation2\>", ...}
	 */
	public static JsonBean mapToBean(Map<String, String> map) {
		if (map.isEmpty())
			return null;
		JsonBean bean = new JsonBean();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			bean.set(entry.getKey(), entry.getValue());
		}
		return bean;
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
			editableActivities = getActivitiesByIds(editableIds, true, true, true);
		}
		populateActivityMap(activityMap, editableActivities);
		populateActivityMap(activityMap, notViewableActivities);
		populateActivityMap(activityMap, viewableActivities);
		return activityMap.values();
	}

	private static void populateActivityMap(Map<String, JsonBean> activityMap, List<JsonBean> activities) {
		for (JsonBean activity : activities) {
			JsonBean activityOnMap = activityMap.get((String) activity.get("amp_id"));
			// if it is not on the map, or activity is a newer
			// version than the one already on the Map
			// then we put it on the Map
			if (activityOnMap == null
					|| (Long) activity.get("amp_activity_id") > (Long) activityOnMap.get("amp_activity_id")) {
				activityMap.put((String) activity.get("amp_id"), activity);
			}
		}
	}

	/**
	 * Get the activities ids for the current workspace
	 * 
	 * @param session
	 *            HttpSession
	 * @return List<Long> with the editable activity Ids
	 */
	private static List<Long> getEditableActivityIds() {
		HttpSession session = TLSUtils.getRequest().getSession();
		String query = WorkspaceFilter.getWorkspaceFilterQuery(session);
		return PersistenceManager.getSession().createSQLQuery(query).list();

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
		List<Long> viewableActivityIds = new ArrayList<Long>();
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
	 * @param include
	 *            whether to include or exclude the ids of the List<Long>
	 *            activityIds into the result
	 * @param activityIds
	 *            List with the ids (amp_activity_id) of the activities to
	 *            include or exclude
	 * @param viewable
	 *            whether the list of activities is viewable or not
	 * @param editable
	 *            whether the list of activities is editable or not
	 * @return List <JsonBean> of the activities generated from
	 *         including/excluding the List<Long> of activityIds
	 */
	private static List<JsonBean> getActivitiesByIds(final List<Long> activityIds, final boolean include,
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
						bean.set("amp_activity_id", rs.getLong("amp_activity_id"));
						bean.set("created_date", formatISO8601Date(rs.getTimestamp("date_created")));
						bean.set("title", rs.getString("name"));
						bean.set("project_code", rs.getString("project_code"));
						bean.set("update_date", formatISO8601Date(rs.getTimestamp("date_updated")));
						bean.set("amp_id", rs.getString("amp_id"));
						bean.set("edit", editable);
						bean.set("view", viewable);
						activitiesList.add(bean);
					}
					rs.close();
				}
			}
		});
		return activitiesList;
	}

	/**
	 * checks whether a Field is assignable from a Collection
	 * @param field a Field
	 * @return true/false
	 */
	private static boolean isCollection(Field field) {
		return Collection.class.isAssignableFrom( field.getType());
	}
	
	/**
	 * returns the generic class defined within a Collection,
	 * e.g. Collection<Class_returned>
	 * @param field
	 * @return the generic class  
	 */
	private static Class<?> getGenericClass(Field field) {
		if (!isCollection(field))
			throw new RuntimeException("Not a collection: " + field.toString());
		ParameterizedType collectionType = null;
		collectionType = (ParameterizedType) field.getGenericType();
		Type[] genericTypes = collectionType.getActualTypeArguments();
		if (genericTypes.length > 1)
		{
			//dealing with a map or anything else having > 1 parameterized types 
			//throw an exception, this is a very unexpected case
			throw new RuntimeException("Only collections with one generic type expected!");
		}
		if (genericTypes.length == 0) {
//			return null;
			//dealing with a raw type
			//throw an exception, it won't be complete with no parameterization
			throw new RuntimeException("Raw types are not allowed!");
		}
		return ((Class<?>) genericTypes[0]);
	}
	
	/**
	 * gets fields from the type of the field
	 * @param field
	 * @return a list of JsonBeans, each a description of @Interchangeable 
	 * fields in the definition of the field's class, or field's generic type,
	 * if it's a collection  
	 */
	private static List<JsonBean> getChildrenOfField(Field field) {
		if (!isCollection(field))
			return getAllAvailableFields(field.getType());
		else
			return getAllAvailableFields(getGenericClass(field));
	}
	
	/**
	 * converts the uppercase letters of a string to underscore + lowercase
	 * (except for first one)
	 * @param input String to be converted
	 * @return converted string
	 */
	public static String underscorify(String input) {
		StringBuilder bld = new StringBuilder();
		for (int i = 0; i < input.length(); i++) {
			if (input.charAt(i) == ' ')
				bld.append('_');
			else
				bld.append(Character.toLowerCase(input.charAt(i)));
		}
		return bld.toString();
	}
	
	/**
	 * describes a field in a JSON structure of:
	 * 		field_type: one of the types {string, boolean, float, list}
	 * 		field_name: the field name, obtained from the fieldTitle attribute from the @Interchangeable annotation
	 * 		field_label: translations of the field in the available languages
	 * 		multiple_values: true if it's a collection, false otherwise
	 * 		importable: whether the field is to be imported, or had been exported just for the sake of matching
	 * 		children: if the field is not a basic type (string, boolean, or float), its class may contain other @Interchangeable fields,
	 * 					  which are recursively added here
	 * 		recursive: defined by @Interchangeable.recursive; true for the purpose of avoiding loops
	 * 		allow_empty: specifies whether said field is allowed to be transmitted empty
	 * @param field
	 * @return
	 */
	private static JsonBean describeField(Field field) {
		Interchangeable ant2 = field.getAnnotation(Interchangeable.class);
		if (ant2 == null)
			return null;
		JsonBean bean = new JsonBean();
		bean.set("field_name", underscorify(ant2.fieldTitle()));
		bean.set("field_type", classToCustomType.containsKey(field.getType()) ? classToCustomType.get(field.getType()) : "list");
		bean.set("field_label", mapToBean(getLabelsForField(ant2.fieldTitle())));
		if (!classToCustomType.containsKey(field.getClass())) {/* list type */
			bean.set("importable", ant2.importable()? true: false);
			if (isCollection(field) && !hasMaxSizeValidatorEnabled (field))
				bean.set("multiple_values", true);
			else 
				bean.set("multiple_values", false);
			if (!ant2.recursive()){
				List<JsonBean> children = getChildrenOfField(field);
				if (children != null && children.size() > 0)
					bean.set("children", children);
			}
			else
			{
				bean.set("recursive", true);
			}
			bean.set("unique", hasUniqueValidatorEnabled (field));
			bean.set("allow_empty", !isRequired(field));
		}
		return bean;
	}

	
	public static List<JsonBean> getAllAvailableFields() {
		return getAllAvailableFields(AmpActivityFields.class);
	}
	

	/**
	 * Describes each @Interchangeable field of a class
	 * @param clazz the class to be described
	 * @return 
	 */
	private static List<JsonBean> getAllAvailableFields(Class clazz) {
		List<JsonBean> result = new ArrayList<JsonBean>();
		StopWatch.next("Descending into", false, clazz.getName());
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			if (!FMVisibility.isVisible(field)) {
				continue;
			}
			JsonBean descr = describeField(field);
			if (descr != null)
				result.add(descr);
		}
		return result;
	}

	/**
	 * Gets a date formatted in ISO 8601 format. If the date is null, returns
	 * null.
	 * 
	 * @param date
	 *            the date to be formatted
	 * @return String, date in ISO 8601 format
	 */
	public static String formatISO8601Date(Date date) {
		return date == null ? null : dateFormatter.format(date);  
	}

	/**
	 * Gets the List <JsonBean> of activities the user can and can't view, edit
	 * using a LRU caching mechanism. The pid is used as the cache key.
	 * 
	 * @param pid
	 *            current pagination request reference (random id) to keep a
	 *            snapshot for the pagination chunks
	 * @param tm
	 *            TeamMember, current logged user
	 * @return the Collection <JsonBean> with the list of activities for the
	 *         user
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

	public static JsonBean getActivity(Long projectId) {
		JsonBean activityJson = new JsonBean();
		try {
			AmpActivityVersion activity = ActivityUtil.loadActivity(projectId);
			activityJson.set("amp_activity_id", activity.getAmpActivityId());
			activityJson.set("amp_id", activity.getAmpId());
			Field[] fields = activity.getClass().getSuperclass().getDeclaredFields();

			for (Field field : fields) {
				Interchangeable interchangeable = field.getAnnotation(Interchangeable.class);
				if (interchangeable != null) {
					field.setAccessible(true);
					if (field.getType().isAssignableFrom(Set.class)) {
						Set setObject = (Set) field.get(activity);
						if (setObject != null) {
							for (Object obj : setObject) {

								Field[] setFields = obj.getClass().getDeclaredFields();
								for (Field setField : setFields) {
									Interchangeable interchangeableSetField = setField
											.getAnnotation(Interchangeable.class);

									if (interchangeableSetField != null) {
										setField.setAccessible(true);
										activityJson.set(interchangeableSetField.fieldTitle(), setField.get(obj));

									}
								}
							}
						}
					}
					else {
						Object object = field.get(activity);
						if (object != null) {
							ClassUtils.isPrimitiveOrWrapper(object.getClass());
							//@VersionableFieldTextEditor
							if (!JSON_SUPPORTED_CLASSES.contains(object.getClass())) {
								System.out.println("not supported");
							}
							else {
								activityJson.set(interchangeable.fieldTitle(), object);
											
							}
						}		
					}
				
				}
			}

			/*
			 * activityJson.set("title", activity.getName());
			 * activityJson.set("created_date",
			 * formatISO8601Date(activity.getCreatedDate()));
			 * activityJson.set("updated_date",
			 * formatISO8601Date(activity.getUpdatedDate())); if
			 * (activity.getSectors().size() > 0) { List<JsonBean> sectors = new
			 * ArrayList<JsonBean>(); Iterator<AmpActivitySector> it =
			 * activity.getSectors().iterator(); while (it.hasNext()) {
			 * AmpActivitySector sector = it.next(); JsonBean sectorBean = new
			 * JsonBean(); sectorBean.set("id",
			 * sector.getSectorId().getAmpSectorId()); sectorBean.set("name",
			 * sector.getSectorId().getName()); sectorBean.set("percentage",
			 * sector.getSectorPercentage()); sectors.add(sectorBean); }
			 * activityJson.set("sectors", sectors); } //activityJson.set(name,
			 * activity.)
			 */

		} catch (DgException e) {
			LOGGER.warn("Coudn't load activity with id: " + projectId);
			throw new RuntimeException(e);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return activityJson;
	}
	
	public static boolean hasUniqueValidatorEnabled (Field field){
		boolean isEnabled = false;
		Validators validators = field.getAnnotation(Validators.class);
		if (validators != null) {
			String uniqueValidator = validators.unique();
			if (!uniqueValidator.isEmpty()) {
				isEnabled = FMVisibility.isFmPathEnabled(uniqueValidator);
			}
		}
		
		return isEnabled;
		
	}
	
	public static boolean hasMaxSizeValidatorEnabled (Field field) {
		boolean isEnabled = false;
		Validators validators = field.getAnnotation(Validators.class);
		if (validators != null) {
			String maxSize = validators.maxSize();
			if (!maxSize.isEmpty()) {
				isEnabled = FMVisibility.isFmPathEnabled(maxSize);
			}
		}
		return isEnabled;	
	}
	/**
	 * Checks if a given field is required or not
	 * 
	 * @param field the field to validate
	 * @return true if the field is required, false if it is not.
	 */
	public static boolean isRequired(Field field) {
		boolean isRequired = false;
		String minSize = "";
		Validators validators = field.getAnnotation(Validators.class);
		Interchangeable interchangeable = field.getAnnotation(Interchangeable.class);
		if (validators != null) {
			minSize = validators.minSize();
		}
		String required = interchangeable.required();
		if (required.equals(ALWAYS_REQUIRED) || (!required.equals(NOT_REQUIRED) && FMVisibility.isFmPathEnabled(required))
				|| (!minSize.isEmpty() && FMVisibility.isFmPathEnabled(minSize))) {
			isRequired = true;
		}
		return isRequired;
	}
}
