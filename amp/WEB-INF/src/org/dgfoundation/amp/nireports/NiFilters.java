package org.dgfoundation.amp.nireports;

import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import org.dgfoundation.amp.nireports.schema.NiDimension;
import org.dgfoundation.amp.nireports.schema.NiDimension.NiDimensionUsage;

/**
 * the NiReports faucet of filtering 
 * @author Dolghier Constantin
 *
 */
public interface NiFilters {
	
	/**
	 * the IDs of the entities (activities / pledges) which are selected by the workspace filter
	 * @return
	 */
	public Set<Long> getWorkspaceActivityIds();
	
	/**
	 * the IDs of the entities (activities/pledges) which should be taken into this report (taking filtering into account)
	 * call it after having populated the columns with data
	 * @return
	 */
	public Set<Long> getFilteredActivityIds();
	
	/**
	 * returns the Predicate-filters to impose on all the cells which come out of fetchers IF they have the given {@link NiDimensionUsage}
	 * @param engine
	 * @return
	 */
	public Map<NiDimensionUsage, Predicate<NiDimension.Coordinate>> getProcessedFilters();
	
	/**
	 * returns the predicate-filters to impuse on all the raw cells which come out of fetchers on a given column
	 * @return
	 */
	public Map<String, Predicate<Cell>> getCellPredicates();
	
	
	/**
	 * returns a list of hierarchies which should be mandatorily part of the report. In case the ReportSpec does not mandate any of them, the hierarchy will be added artificially and then removed
	 * @return
	 */
	public Set<String> getMandatoryHierarchies();
	
	//public Map<NiDimensionUsage, Predicate<NiDimension.Coordinate>> getTransformedFilters();
}
