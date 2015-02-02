package org.digijava.kernel.ampapi.mondrian.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dgfoundation.amp.Util;
import org.digijava.module.aim.util.ActivityUtil;

/**
 * set(org group) -> set(org type) expander
 * @author Constantin Dolghier
 *
 */
public class ActACVIdsExpander extends IdsExpander {
	
	public ActACVIdsExpander(String factColumnName) {
		super(factColumnName);
	}
	
	@Override public Set<Long> expandIds(final List<Long> values) {
		Set<Long> res = new HashSet<>();
		res.addAll(ActivityUtil.fetchLongs("SELECT amp_activity_id FROM amp_activities_categoryvalues WHERE amp_categoryvalue_id IN ( " + Util.toCSStringForIN(values) + ")"));
		return res;
	}
}
