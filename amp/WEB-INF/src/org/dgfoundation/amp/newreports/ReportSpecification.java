package org.dgfoundation.amp.newreports;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import org.digijava.module.aim.helper.TeamMember;

/**
 * this defines a report specification, e.g. the requested structure of a report. 
 * An instance of this class defines univoquely the kind of report to be generated: columns, hierarchies, filters, sorting
 * @author Dolghier Constantin
 *
 */
public interface ReportSpecification {
	
	/**
	 * the name of the report <strong>localized<strong>
	 * @return
	 */
	public String getReportName();
	
	/**
	 * the names should NOT be localized
	 * @return the set of columns to use. <strong>The order of iteration of the set is meaningful</strong> - so please either use a @link {@link LinkedHashSet} or a @link {@link SortedSet}
	 */
	public Set<ReportColumn> getColumns();
	
	/**
	 * @return the set of measures to use. <strong>The order of iteration of the set is meaningful</strong> - so please either use a @link {@link LinkedHashSet} or a @link {@link SortedSet} 
	 */
	public List<ReportMeasure> getMeasures();
	
	/**
	 * this set should be a strict subset of the one returned at {@link #getColumns()}
	 * @return
	 */
	public Set<ReportColumn> getHierarchies();
	
	public ReportFilters getFilters();
	
	/** @return {@link ReportSettings} - settings of the current report */
	public ReportSettings getSettings();
	
	public List<SortingInfo> getSorters();
	
	public GroupingCriteria getGroupingCriteria();
	
//	/**
//	 * returns true iff the report must hide entities and only show (sub)totals
//	 * @return
//	 */
//	public boolean isSummaryReport();
	
	/**
	 * @return true if totals per each row must be calculated
	 */
	public boolean isCalculateRowTotals();
	
	/**
	 * @return true if totals per each measure column must be calculated
	 */
	public boolean isCalculateColumnTotals();
	
	/**
	 * @return whether columns with no funding data should be displayed or not
	 */
	public boolean isDisplayEmptyFundingColumns();
	
	/**
	 * @return whether rows with no funding data should be displayed or not
	 */
	public boolean isDisplayEmptyFundingRows();
	
}
