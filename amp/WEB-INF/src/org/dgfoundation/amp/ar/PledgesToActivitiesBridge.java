package org.dgfoundation.amp.ar;

import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

import org.digijava.module.aim.dbentity.AmpColumns;

public class PledgesToActivitiesBridge {
	
	/**
	 * map<activity_extractor_view, pledge_extractor_view_name> for (meta)text columns
	 */
	 public final static Map<String, String> activityViewToPledgeView = Collections.unmodifiableMap(new HashMap<String, String>() {{
		 																	put("v_titles", "v_pledges_titles");
	 																	}});
	 
	 public static boolean hasCorrespondingPledgeView(AmpColumns col) {
		 return activityViewToPledgeView.containsKey(col.getExtractorView());
	 }
	 
	 public static String getCorrespondingPledgeView(AmpColumns col) {
		 return activityViewToPledgeView.get(col.getExtractorView());
	 }
}
