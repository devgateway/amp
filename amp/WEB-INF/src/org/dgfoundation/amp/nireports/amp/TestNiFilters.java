package org.dgfoundation.amp.nireports.amp;

import java.sql.Connection;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.dgfoundation.amp.ar.WorkspaceFilter;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.newreports.ReportEnvironment;
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
		return cachedActivityIds;

	};
	
//	@Override
//	public Set<Long> getSelectedIds(NiReportsEngine engine, String columnName) {
//		return null;
//	}
}
