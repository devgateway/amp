package org.digijava.kernel.ampapi.endpoints.activityUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpContentTranslation;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.startup.AmpBackgroundActivitiesCloser;
import org.digijava.module.aim.util.ActivityVersionUtil;
import org.digijava.module.aim.util.DbUtil;

public class ActivityEndpointUtils {

	protected static final Logger logger = Logger.getLogger(ActivityEndpointUtils.class);
	
	private static final Comparator<AmpActivityVersion> activityComparatorByAmpId = new Comparator<AmpActivityVersion>() {
		@Override
		public int compare(AmpActivityVersion o1, AmpActivityVersion o2) {
			return Long.compare(o1.getAmpActivityId(), o2.getAmpActivityId());
		}
	}; 

	public static JsonBean cloneActivities(Set<String> uniqueActivityIds) {
		JsonBean response = new JsonBean();
		List<Map<String, Long[]>> activitiesResponse = new ArrayList<Map<String, Long[]>>();
		List<String> failed = new ArrayList<String>();
		
		// Check if activity versioning is enabled.
		if (!ActivityVersionUtil.isVersioningEnabled()) {
			response.set("errors", "Versioning is not enabled, clone not available.");
		}
		
		Iterator<String> iIds = uniqueActivityIds.iterator();
		while (iIds.hasNext()) {
			try {
				// Get the list of versions of an activity by its AMP_ID.
				String id = iIds.next();
				Set<AmpActivityVersion> activitiesHistory = DbUtil.getActivitiesByAmpId(id);
				if (activitiesHistory.size() == 0) {
					failed.add(id);
					throw new Exception("AMPID not found: " + id);
				}
				// Get currenct (active) version.
				AmpActivityVersion currentActivityVersion = Collections.max(activitiesHistory, activityComparatorByAmpId);
				
				// Clone activity.
				AmpTeamMember ampClosingMember = AmpBackgroundActivitiesCloser.createActivityCloserTeamMemberIfNeeded(currentActivityVersion.getTeam());
				List<AmpContentTranslation> translations = new ArrayList<AmpContentTranslation>();
				AmpActivityVersion newAmpActivity = org.dgfoundation.amp.onepager.util.ActivityUtil.saveActivityNewVersion(currentActivityVersion, translations, ampClosingMember, false, PersistenceManager.getRequestDBSession(), false, false);
				// Return data about the new activity.
				Map<String, Long[]> activityData = new HashMap<String, Long[]>();
				activityData.put(id, new Long[] {currentActivityVersion.getAmpActivityId(), newAmpActivity.getAmpActivityId()});
				activitiesResponse.add(activityData);
			} catch (Exception e) {
				logger.error(e);
			}
		}
		response.set("activities", activitiesResponse);
		response.set("failed", failed);
		return response;
	}

}
