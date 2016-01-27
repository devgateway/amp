package org.dgfoundation.amp.nireports.amp;

import java.sql.Connection;
import java.util.Arrays;
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
public class AmpNiFilters implements NiFilters {

	protected Set<Long> cachedActivityIds = null;
	
	@Override
	public Set<Long> getActivityIds(NiReportsEngine engine) {
		if (cachedActivityIds == null) {
			Connection conn = AmpReportsScratchpad.get(engine).connection;
			//cachedActivityIds = Collections.unmodifiableSet(new HashSet<Long>(SQLUtils.fetchLongs(conn, "SELECT amp_activity_id FROM amp_activity")));
			ReportEnvironment environ = AmpReportsScratchpad.get(engine).environment;
			cachedActivityIds = Collections.unmodifiableSet(environ.workspaceFilter.getIds());
			//cachedActivityIds = new HashSet<>(Arrays.asList(52l, 65l));
		}
		return cachedActivityIds;
//		return Arrays.asList(13,15,17,18,19,21,24,25,27,28,29,30,31,33,32,36,38,39,40,41,43,44,45,46,48,23,50,52,53,61,63,64,65,66,67,68,12,26,69,70,71,73,76,77,78)
//			.stream().map(i -> Long.valueOf(i)).collect(Collectors.toSet());
	}

	@Override
	public Set<Long> getSelectedIds(NiReportsEngine engine, String columnName) {
		return null;
	}
}
