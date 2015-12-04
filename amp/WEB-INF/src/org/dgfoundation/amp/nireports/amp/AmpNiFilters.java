package org.dgfoundation.amp.nireports.amp;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import org.dgfoundation.amp.nireports.NiFilters;

public class AmpNiFilters implements NiFilters {

	@Override
	public Set<Long> getActivityIds() {
		return Arrays.asList(13,15,17,18,19,21,24,25,27,28,29,30,31,33,32,36,38,39,40,41,43,44,45,46,48,23,50,52,53,61,63,64,65,66,67,68,12,26,69,70,71,73,76,77,78)
			.stream().map(i -> Long.valueOf(i)).collect(Collectors.toSet());
	}

	@Override
	public Set<Long> getSelectedIds(String columnName) {
		return null;
	}
}
