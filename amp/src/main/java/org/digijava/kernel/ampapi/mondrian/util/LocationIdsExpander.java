package org.digijava.kernel.ampapi.mondrian.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.digijava.module.aim.util.DynLocationManagerUtil;

public class LocationIdsExpander extends IdsExpander {
	
	public LocationIdsExpander(String factColumnName) {
		super(factColumnName);
	}
	
	@Override public Set<Long> expandIds(List<Long> values) {
		Set<Long> res = new HashSet<>();
		res.addAll(DynLocationManagerUtil.getRecursiveChildrenOfCategoryValueLocations(values));
		//res.addAll(DynLocationManagerUtil.getRecursiveAscendantsOfCategoryValueLocations(values));
		return res;
	}
}
