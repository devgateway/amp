package org.digijava.kernel.ampapi.mondrian.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dgfoundation.amp.mondrian.MondrianETL;


/**
 * set(org group) -> set(org type) expander
 * @author Constantin Dolghier
 *
 */
public class BooleanExpander extends IdsExpander {
	
	public BooleanExpander(String factColumnName) {
		super(factColumnName);
	}
	
	@Override public Set<Long> expandIds(List<Long> values) {
		final Set<Long> res = new HashSet<>();
		for(Long value:values) {
			if (value == 1 || value == 2)
				res.add(value);
			else
				res.add(MondrianETL.MONDRIAN_DUMMY_ID_FOR_ETL);
		}
		return res;
	}
}
