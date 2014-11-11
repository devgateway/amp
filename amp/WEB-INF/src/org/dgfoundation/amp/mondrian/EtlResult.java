package org.dgfoundation.amp.mondrian;

import java.util.ArrayList;
import java.util.List;

public class EtlResult {
	/**
	 * etl duration in seconds
	 */
	public final double duration;
	
	/**
	 * whether cache should be invalidated
	 */
	public final boolean cacheInvalidated;
		
	/**
	 * number of activities / pledges affected by ETl
	 */
	public final long nrAffectedEntities;
	
	/**
	 * number of dates affected by ETL
	 */
	public final long nrAffectedDates;
	
	/**
	 * amp_etl_changelog.event_id
	 */
	public final long eventId;
	
	public final List<String> fullEtlReasons;
	
	public EtlResult(long eventId, double duration, boolean cacheInvalidated, long nrAffectedEntities, long nrAffectedDates, List<String> fullEtlReasons) {
		this.eventId = eventId;
		this.duration = duration;
		this.cacheInvalidated = /*true || */cacheInvalidated;
		this.nrAffectedEntities = nrAffectedEntities;
		this.nrAffectedDates = nrAffectedDates;
		this.fullEtlReasons = new ArrayList<>(fullEtlReasons);
	}
	
	@Override public String toString() {
		return String.format("etl: took %.2f, invalidated cache: %b, affected entities: %d, affected dates: %d", duration, cacheInvalidated, nrAffectedEntities, nrAffectedDates);
	}
}
