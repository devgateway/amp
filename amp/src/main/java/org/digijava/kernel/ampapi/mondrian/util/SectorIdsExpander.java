package org.digijava.kernel.ampapi.mondrian.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.digijava.module.aim.util.SectorUtil;

public class SectorIdsExpander extends IdsExpander {
	
	public SectorIdsExpander(String factColumnName) {
		super(factColumnName);
	}
	
	@Override public Set<Long> expandIds(List<Long> values) {
		Set<Long> res = new HashSet<>();
		res.addAll(SectorUtil.getRecursiveChildrenOfSectors(values));
		//res.addAll(SectorUtil.getRecursiveAscendantsOfSectors(values));
		return res;
	}
}
