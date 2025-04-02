package org.dgfoundation.amp.nireports;

import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import org.dgfoundation.amp.newreports.ReportFilters;
import org.dgfoundation.amp.nireports.schema.NiDimension;
import org.dgfoundation.amp.nireports.schema.NiDimension.NiDimensionUsage;

/**
 * This interface is the NiReports faucet of filtering, e.g.  a NiReports-specific reinterpretation of {@link ReportFilters}. <br />
 * The rationale of this interface comes from the fact that the schema is allowed to be flexible wrt to how a rule applied to a given column should action on a report output.
 * Also, multiple rules might interact in schema-specific ways (as an AMP example: filtering on both Primary Sector Level 0 and Primary Sector Level 1 is an OR operation between these 2 rules. <br />
 * For a verbose description of the definition of NiFilters, please read <a href='https://wiki.dgfoundation.org/display/AMPDOC/3.+NiReports+runtime#id-3.NiReportsruntime-3.5Filteringruntime'>here</a>
 * @author Dolghier Constantin
 *
 */
public interface NiFilters {
    
    /**
     * the IDs of the entities (activities / pledges) which are selected by the workspace filter or global filter
     * @return
     */
    Set<Long> getActivityIds();
    
    /**
     * the IDs of the entities (activities/pledges) which should be taken into this report (taking filtering into account)
     * call it after having populated the columns with data
     * @return
     */
    Set<Long> getFilteredActivityIds();
    
    /**
     * returns the Predicate-filters to impose on all the cells which come out of fetchers IF they have the given {@link NiDimensionUsage}.
     * Cells which do not respond to a given NiDimension are unaffected by a given predicate
     * @param engine
     * @return
     */
    Map<NiDimensionUsage, Predicate<NiDimension.Coordinate>> getProcessedFilters();
    
    /**
     * returns the predicate-filters to impose on all the raw cells which come out of fetchers on a given column.
     * @return
     */
    Map<String, Predicate<Cell>> getCellPredicates();
    
    /**
     * returns a set of columns which should be fetched-and-filtered; then, only the activityIds which have survived filtering in these columns should be kept in the rest of the report 
     * @return
     */
    Set<String> getFilteringColumns();

    /**
     * returns a list of hierarchies which should be mandatorily part of the report. In case the ReportSpec does not mandate any of them, the hierarchy will be added artificially and then removed via collapsing
     * @return
     */
    Set<String> getMandatoryHiers();
    //public Map<NiDimensionUsage, Predicate<NiDimension.Coordinate>> getTransformedFilters();
}
