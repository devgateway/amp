package org.digijava.kernel.ampapi.endpoints.permissions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.Util;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.gateperm.core.GatePermConst;
import org.hibernate.Query;

import clover.com.google.gson.JsonArray;

public class PermissionsService {
	public static List<JsonBean> getPermissionsForActivities(List<String> activitIds) {
		List<JsonBean> activityCanDo = new ArrayList<>();
		Map scope = new HashMap();
		scope.put(GatePermConst.ScopeKeys.CURRENT_MEMBER, (TeamMember) TLSUtils.getRequest().getSession().getAttribute(Constants.CURRENT_MEMBER));
		
		List<AmpActivity> ampActivities = PermissionsService.getAmpActivities(activitIds);

		for (AmpActivity ampActivity : ampActivities) {
			JsonBean ampActivityPermissions=new JsonBean();
			ampActivityPermissions.set("activityId", ampActivity.getAmpActivityId());
			ampActivityPermissions.set("permissions", ampActivity.getAllowedActions(scope));
			

			activityCanDo.add(ampActivityPermissions);
		}

		return activityCanDo;
	}

	private static List<AmpActivity> getAmpActivities(List<String> ampActivityIds) {
		String queryString = "from " + AmpActivity.class.getName() + " where" + " ampActivityId in ("
				+ Util.toCSStringForIN(ampActivityIds) + " )";
		Query q = PersistenceManager.getSession().createQuery(queryString);
		return q.list();
	}
}
