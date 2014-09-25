package org.dgfoundation.amp.mondrian;

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
	
	public EtlResult(long eventId, double duration, boolean cacheInvalidated, long nrAffectedEntities, long nrAffectedDates) {
		this.eventId = eventId;
		this.duration = duration;
		this.cacheInvalidated = cacheInvalidated;
		this.nrAffectedEntities = nrAffectedEntities;
		this.nrAffectedDates = nrAffectedDates;
	}
	
	@Override public String toString() {
		return String.format("etl: took %.2f, invalidated cache: %b, affected entities: %d, affected dates: %d", duration, cacheInvalidated, nrAffectedEntities, nrAffectedDates);
	}
}
