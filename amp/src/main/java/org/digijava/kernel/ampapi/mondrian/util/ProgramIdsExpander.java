package org.digijava.kernel.ampapi.mondrian.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.digijava.module.aim.util.ProgramUtil;

public class ProgramIdsExpander extends IdsExpander {
	
	public ProgramIdsExpander(String factColumnName) {
		super(factColumnName);
	}
	
	@Override public Set<Long> expandIds(List<Long> values) {
		Set<Long> res = new HashSet<>();
		res.addAll(ProgramUtil.getRecursiveChildrenOfPrograms(values));
		//res.addAll(ProgramUtil.getRecursiveAscendantsOfPrograms(values));
		return res;
	}
}
