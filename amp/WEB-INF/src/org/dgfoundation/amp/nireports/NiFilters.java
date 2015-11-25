package org.dgfoundation.amp.nireports;

import java.util.Set;

/**
 * the NiReports faucet of filtering 
 * @author Dolghier Constantin
 *
 */
public interface NiFilters {
	
	/**
	 * the IDs of the entities (activities/pledges) which should be taken into this report
	 * null = no filtering
	 * @return
	 */
	public Set<Long> getActivityIds();
	
	/**
	 * the IDs of the entities (sector/donor/etc) which should be fetched for a given column
	 * @param columnName
	 * @return
	 */
	public Set<Long> getSelectedIds(String columnName);
}
