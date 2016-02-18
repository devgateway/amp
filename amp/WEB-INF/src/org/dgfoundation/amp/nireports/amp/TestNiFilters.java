package org.dgfoundation.amp.nireports.amp;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.dgfoundation.amp.nireports.NiFilters;
import org.dgfoundation.amp.nireports.NiReportsEngine;

/**
 * TODO: this class should become an AmpARFilter wrapper
 * One instance will exist per report runtime
 * @author Dolghier Constantin
 *
 */
public class TestNiFilters implements NiFilters {

	protected Set<Long> cachedActivityIds;
	
	public TestNiFilters(Collection<Long> activityIds) {
		this.cachedActivityIds = new HashSet<Long>(activityIds);
	}
	
	@Override
	public Set<Long> getActivityIds(NiReportsEngine engine) {
		return cachedActivityIds;
	}

	
	@Override
	public Set<Long> getSelectedIds(NiReportsEngine engine, String columnName) {
		return null;

	};
	
//	@Override
//	public Set<Long> getSelectedIds(NiReportsEngine engine, String columnName) {
//		return null;
//	}
}
