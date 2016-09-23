package org.digijava.kernel.ampapi.endpoints.activity.utils;

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

	public static JsonBean cloneActivities(Set<String> uniqueActivityIds) {
		JsonBean response = new JsonBean();
		List<Map<String, Long>> activitiesResponse = new ArrayList<Map<String, Long>>();
		
		//Comparator<AmpActivityVersion> activityComparator = Comparator.comparing(AmpActivityVersion::getAmpActivityId); // Lambdas doesnt work with Jersey before 1.19!!!
		Comparator<AmpActivityVersion> activityComparator = new Comparator<AmpActivityVersion>() {
			@Override
			public int compare(AmpActivityVersion o1, AmpActivityVersion o2) {
				return Long.compare(o1.getAmpActivityId(), o2.getAmpActivityId());
			}
		};
		
		Iterator<String> iIds = uniqueActivityIds.iterator();
		while (iIds.hasNext()) {
			try {
				String id = iIds.next();
				Set<AmpActivityVersion> activitiesHistory = DbUtil.getActivitiesByAmpId(id);				
				AmpActivityVersion activeActivityVersion = Collections.max(activitiesHistory, activityComparator);
				logger.info(activeActivityVersion + "-" + activitiesHistory.size() + "-" + activeActivityVersion.getAmpActivityId());
				
				// Clone activity.
				AmpTeamMember ampClosingMember = AmpBackgroundActivitiesCloser.createActivityCloserTeamMemberIfNeeded(activeActivityVersion.getTeam());
				List<AmpContentTranslation> translations = new ArrayList<AmpContentTranslation>();
				AmpActivityVersion newAmpActivity = org.dgfoundation.amp.onepager.util.ActivityUtil.saveActivityNewVersion(activeActivityVersion, translations, ampClosingMember, false, PersistenceManager.getRequestDBSession(), false, false);
				// Return data about the new activity.
				Map<String, Long> activityData = new HashMap<String, Long>();
				activityData.put(id, newAmpActivity.getAmpActivityId());
				activitiesResponse.add(activityData);
			} catch (Exception e) {
				logger.error(e);
			}
		}
		response.set("activities", activitiesResponse);
		return response;
	}

}
