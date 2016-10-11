package org.digijava.kernel.ampapi.mondrian.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * set(org group) -> set(org type) expander
 * @author Constantin Dolghier
 *
 */
public class IdentityExpander extends IdsExpander {
	
	public final Long extraValue;
	
	public IdentityExpander(String factColumnName, Long extraValue) {
		super(factColumnName);
		this.extraValue = extraValue;
	}
	
	public IdentityExpander(String factColumnName) {
		this(factColumnName, null);
	}
	
	@Override public Set<Long> expandIds(final List<Long> values) {
		final Set<Long> res = new HashSet<>(values);
		if (extraValue != null)
			res.add(extraValue);
		return res;
	}
}
