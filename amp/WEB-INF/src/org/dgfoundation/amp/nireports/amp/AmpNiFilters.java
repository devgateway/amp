package org.dgfoundation.amp.nireports.amp;

import java.sql.Connection;
import java.util.HashSet;
import java.util.Set;

import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.nireports.NiFilters;
import org.dgfoundation.amp.nireports.NiReportsEngine;

/**
 * TODO: this class should become an AmpARFilter wrapper
 * @author Dolghier Constantin
 *
 */
public class AmpNiFilters implements NiFilters {

	@Override
	public Set<Long> getActivityIds(NiReportsEngine engine) {
		Connection conn = AmpReportsScratchpad.get(engine).connection;
		return new HashSet<Long>(SQLUtils.fetchLongs(conn, "SELECT amp_activity_id FROM amp_activity"));
//		return Arrays.asList(13,15,17,18,19,21,24,25,27,28,29,30,31,33,32,36,38,39,40,41,43,44,45,46,48,23,50,52,53,61,63,64,65,66,67,68,12,26,69,70,71,73,76,77,78)
//			.stream().map(i -> Long.valueOf(i)).collect(Collectors.toSet());
	}

	@Override
	public Set<Long> getSelectedIds(NiReportsEngine engine, String columnName) {
		return null;
	}
}
