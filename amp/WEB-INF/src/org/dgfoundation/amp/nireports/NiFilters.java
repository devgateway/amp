package org.dgfoundation.amp.nireports;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.nireports.schema.NiDimension;
import org.dgfoundation.amp.nireports.schema.NiDimension.NiDimensionUsage;

/**
 * the NiReports faucet of filtering 
 * @author Dolghier Constantin
 *
 */
public interface NiFilters {
	
	/**
	 * the IDs of the entities (activities/pledges) which should be taken into this report (taken filtering into account)
	 * null = no filtering
	 * @return
	 */
	public Set<Long> getActivityIds(NiReportsEngine engine);
	
	/**
	 * returns the Predicate-filters to impose on all the cells which come out of fetchers
	 * @param engine
	 * @return
	 */
	public Map<NiDimensionUsage, Predicate<NiDimension.Coordinate>> getProcessedFilters(NiReportsEngine engine);
	
	/**
	 * returns a list of hierarchies which should be mandatorily part of the report. In case the ReportSpec does not mandate any of them, the hierarchy will be added artificially and then removed
	 * @return
	 */
	public Set<String> getMandatoryHierarchies();
	
	//public Map<NiDimensionUsage, Predicate<NiDimension.Coordinate>> getTransformedFilters();
}
