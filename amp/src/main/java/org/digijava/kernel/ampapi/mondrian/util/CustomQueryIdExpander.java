package org.digijava.kernel.ampapi.mondrian.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dgfoundation.amp.Util;
import org.digijava.module.aim.util.ActivityUtil;

public class CustomQueryIdExpander extends IdsExpander {
	
	public final String query;
	
	public CustomQueryIdExpander(String factColumnName, String query) {
		super(factColumnName);
		this.query = query;
	}
	
	@Override public Set<Long> expandIds(List<Long> values) {
		Set<Long> res = new HashSet<>();
		res.addAll(ActivityUtil.fetchLongs(query.replaceAll("QQQ",  Util.toCSStringForIN(values))));
		return res;
	}
}
