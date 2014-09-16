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
	 * UNIX timestamp of the moment the ETL started
	 */
	public final long startTime;
	
	/**
	 * number of activities / pledges affected by ETl
	 */
	public final long nrAffectedEntities;
	
	/**
	 * number of dates affected by ETL
	 */
	public final long nrAffectedDates;
	
	public EtlResult(double duration, boolean cacheInvalidated, long startTime, long nrAffectedEntities, long nrAffectedDates) {
		this.duration = duration;
		this.cacheInvalidated = cacheInvalidated;
		this.startTime = startTime;
		this.nrAffectedEntities = nrAffectedEntities;
		this.nrAffectedDates = nrAffectedDates;
	}
	
	@Override public String toString() {
		return String.format("etl: started at %d, took %.2f, invalidated cache: %b, affected entities: %d, affected dates: %d", startTime, duration, cacheInvalidated, nrAffectedEntities, nrAffectedDates);
	}
}
