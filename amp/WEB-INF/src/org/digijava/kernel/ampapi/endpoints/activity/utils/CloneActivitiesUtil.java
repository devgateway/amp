package org.digijava.kernel.ampapi.endpoints.activity.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.errors.ApiEMGroup;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpContentTranslation;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.startup.AmpBackgroundActivitiesCloser;
import org.digijava.module.aim.util.ActivityVersionUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.translation.util.ContentTranslationUtil;

/**
 * Util class for cloning activities from an EP.
 * 
 * @author Gabriel Inchauspe
 *
 */
public class CloneActivitiesUtil {


	protected static final ApiErrorMessage ERROR_NO_ALLOWED = new ApiErrorMessage(1, "Not allowed");
	protected static final ApiErrorMessage ERROR_UN_EXPECTED = new ApiErrorMessage(2, "Un-expected error");
	
	protected static final String PARAM_SUCCEED = "succeed";
	protected static final String PARAM_FAILED = "failed";
	protected static final String PARAM_DRAFT = "draft";
	protected static final String PARAM_NOT_FOUND = "not_found";
	protected static final String PARAM_ACTIVITIES = "activities";
	protected static final Logger logger = Logger.getLogger(CloneActivitiesUtil.class);
	
	/**
	 * Given a list of amp ids (String) we try and clone the current active
	 * version (only if versioning is enabled).
	 * 
	 * @param config
	 * @return
	 */
	public static JsonBean cloneActivities(JsonBean config) {		
	    ApiEMGroup errors = new ApiEMGroup();

		JsonBean response = new JsonBean();
		List<Map<String, Long[]>> activitiesResponse = new ArrayList<Map<String, Long[]>>();
		List<Map<String,String>> failedActivities = new ArrayList<Map<String,String>>();
		List <String>  draftActivities = new  ArrayList<String>();
		List<String> clonedActivities = new ArrayList<String>();
		
		// Check if activity versioning is enabled.
		if (!ActivityVersionUtil.isVersioningEnabled()) {
            EndpointUtils.setResponseStatusMarker(HttpServletResponse.SC_BAD_REQUEST);
            errors.addApiErrorMessage(ERROR_NO_ALLOWED, "activity");
            return ApiError.toError(errors.getAllErrors());

		} else {
			// Convert to Set just in case the data contains the same AMP_ID more than one time.
			Set<String> uniqueAmpIds = new HashSet<String>((List<String>) config.get(PARAM_ACTIVITIES));
			List<AmpActivityVersion> activities = DbUtil.getActivitiesByAmpId(uniqueAmpIds); 
			for (AmpActivityVersion activity : activities) {
				if (activity.getDraft()) {
					//if the activity is draft we dont updated
					draftActivities.add(activity.getAmpId());
				} else {
					try {
						AmpTeamMember ampCloningMember;
						if (activity.getModifiedBy() != null) {
							ampCloningMember = activity.getModifiedBy();
						} else {
							if (activity.getActivityCreator() != null) {
								ampCloningMember = activity.getActivityCreator();
							} else {
								ampCloningMember = AmpBackgroundActivitiesCloser
										.createActivityCloserTeamMemberIfNeeded(activity.getTeam());
							}
						}

						List<AmpContentTranslation> translations = null;// =
																		// ContentTranslationUtil.loadFieldTranslations(activity.getClass().getName(),
																		// objId,
																		// fieldName)
						// Save new version.
						AmpActivityVersion newAmpActivity = org.dgfoundation.amp.onepager.util.ActivityUtil
								.saveActivityNewVersion(activity, translations, ampCloningMember, false,
										PersistenceManager.getRequestDBSession(), false, false);

						// Return data about the new activity.
						Map<String, Long[]> activityData = new HashMap<String, Long[]>();
						activityData.put(activity.getAmpId(),
								new Long[] { activity.getAmpActivityId(), newAmpActivity.getAmpActivityId() });
						activitiesResponse.add(activityData);
						clonedActivities.add(activity.getAmpId());

					} catch (Exception e) {
						Map<String, String> error = new HashMap<String, String>();
						error.put(activity.getAmpId(), e.getMessage());
						failedActivities.add(error);

						logger.error(e);
					}
				}
			}
			response.set(PARAM_SUCCEED, activitiesResponse);


			//from uniqueAmpIds we remove falied, draft and cloned and we get activities that were not found
			uniqueAmpIds.removeAll(failedActivities);
			uniqueAmpIds.removeAll(draftActivities);
			uniqueAmpIds.removeAll(clonedActivities);
			response.set(PARAM_FAILED, failedActivities);
			response.set(PARAM_DRAFT, draftActivities);
			response.set(PARAM_NOT_FOUND, uniqueAmpIds);
		}
		return response;
	}
}