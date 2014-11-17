package org.digijava.kernel.ampapi.mondrian.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * actually a NOP expander
 * @author Constantin Dolghier
 *
 */
public class OrgIdsExpander extends IdsExpander {
	
	public OrgIdsExpander(String factColumnName) {
		super(factColumnName);
	}
	
	@Override public Set<Long> expandIds(List<Long> values) {
		Set<Long> res = new HashSet<>(values);
		return res;
	}
}
