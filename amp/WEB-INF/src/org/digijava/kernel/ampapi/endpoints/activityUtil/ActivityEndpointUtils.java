package org.digijava.kernel.ampapi.endpoints.activityUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpContentTranslation;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.startup.AmpBackgroundActivitiesCloser;
import org.digijava.module.aim.util.ActivityVersionUtil;
import org.digijava.module.aim.util.DbUtil;

/**
 * 
 * @author Gabriel
 *
 */
public class ActivityEndpointUtils {
	
	protected static final String ERROR_NO_VERSIONING = "Versioning is not enabled, clone not available.";
	protected static final String PARAM_SUCCEED = "succeed";
	protected static final String PARAM_FAILED = "failed";
	protected static final String PARAM_ACTIVITIES = "activities";
	protected static final Logger logger = Logger.getLogger(ActivityEndpointUtils.class);
	
	/**
	 * 
	 * @param uniqueActivityIds
	 * @return
	 */
	public static JsonBean cloneActivities(JsonBean config) {		
		JsonBean response = new JsonBean();
		List<Map<String, Long[]>> activitiesResponse = new ArrayList<Map<String, Long[]>>();
		List<String> failed = new ArrayList<String>();
		
		// Check if activity versioning is enabled.
		if (!ActivityVersionUtil.isVersioningEnabled()) {
			response.set("errors", ERROR_NO_VERSIONING);
		} else {
			// Convert to set just in case the data contains the same AMP_ID more than once.
			Set<String> uniqueActivityIds = new HashSet<String>((List<String>) config.get(PARAM_ACTIVITIES));
			
			Set<AmpActivityVersion> activities = DbUtil.getActivitiesByAmpId(uniqueActivityIds);		
			Iterator<AmpActivityVersion> iActivities = activities.iterator();
			while (iActivities.hasNext()) {
				try {
					AmpActivityVersion activity = iActivities.next();
					
					// Save new version.
					AmpTeamMember ampClosingMember = AmpBackgroundActivitiesCloser.createActivityCloserTeamMemberIfNeeded(activity.getTeam());
					List<AmpContentTranslation> translations = new ArrayList<AmpContentTranslation>();
					AmpActivityVersion newAmpActivity = org.dgfoundation.amp.onepager.util.ActivityUtil.saveActivityNewVersion(activity, translations, ampClosingMember, false, PersistenceManager.getRequestDBSession(), false, false);
					
					// If oldId == newId --> the activity was not cloned but updated.
					if (activity.getAmpActivityId().equals(newAmpActivity.getAmpActivityId())) {
						failed.add(activity.getAmpId());
					} else {
						// Return data about the new activity.
						Map<String, Long[]> activityData = new HashMap<String, Long[]>();
						activityData.put(activity.getAmpId(), new Long[] {activity.getAmpActivityId(), newAmpActivity.getAmpActivityId()});
						activitiesResponse.add(activityData);
					}
				} catch (Exception e) {
					logger.error(e);
				}
			}
			response.set(PARAM_SUCCEED, activitiesResponse);
			
			// Find failed ids.
			Iterator<String> iIds = uniqueActivityIds.iterator();
			while (iIds.hasNext()) {
				String id = iIds.next();
				Object existActivity = CollectionUtils.find(activities, new Predicate() {
					@Override
					public boolean evaluate(Object object) {
						return (((AmpActivityVersion) object).getAmpId().equals(id));
					}
				});
				if (existActivity == null) {
					failed.add(id);
				}
			}
			response.set(PARAM_FAILED, failed);
		}
		return response;
	}

}