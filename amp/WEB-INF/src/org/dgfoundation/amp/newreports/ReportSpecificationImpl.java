/**
 * 
 */
package org.dgfoundation.amp.newreports;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ColumnConstants;

/**
 * Stores a report configuration by implementing {@link ReportSpecification} and defines all data required to generate a report. 
 * this is the Mondrian-specific implementation (full of hacks)
 * @author Nadejda Mandrescu
 *
 */
public class ReportSpecificationImpl implements ReportSpecification {
	private int reportType;	
	private String reportName = null;
	private Set<ReportColumn> columns = new LinkedHashSet<ReportColumn>();
	private Set<ReportColumn> dummyColumns = new LinkedHashSet<ReportColumn>();
	private Set<ReportColumn> hierarchies = new LinkedHashSet<ReportColumn>();
	private List<ReportMeasure> measures = new ArrayList<ReportMeasure>();
	private ReportFilters filters = null;
	private ReportSettings settings = null;
	private List<SortingInfo> sorters = null;
	private GroupingCriteria groupingCriteria = GroupingCriteria.GROUPING_TOTALS_ONLY;
	//private boolean summaryReport = false;
	private boolean calculateRowTotals = false;
	private boolean calculateColumnTotals = false;
	private int rowsHierarchiesTotals = 0;
	private int colsHierarchyTotals = 0;
	private boolean displayEmptyFundingColumns = false;
	private boolean displayEmptyFundingRows = false;
	private boolean emptyOutputForUnspecifiedData = true;
	private boolean alsoShowPledges = false;
	private boolean usesFundingFlows = false;
	private String projectTitleColumn = ColumnConstants.PROJECT_TITLE;
	
	/**
	 * Mondrian-specific hack - not part of the API, thus public
	 */
	public Map<String, SortedSet<Integer>> allowedYearsPerMeasure = new HashMap<>();

    /**
     * If the report query results in empty data
     * Should the headers be populated
     */
    private boolean populateReportHeadersIfEmpty = false;
	
	public ReportSpecificationImpl(String reportName, int reportType) {
		if (!ArConstants.LEGAL_REPORT_TYPES.contains(reportType))
			throw new RuntimeException("report type unknown: " + reportType);
		this.reportName = reportName;
		this.reportType = reportType;
	}

	@Override
	public String getReportName() {
		return reportName;
	}

	@Override
	public Set<ReportColumn> getColumns() {
		return columns;
	}
	
	@Override
	public Set<String> getColumnNames() {
		LinkedHashSet<String> res = new LinkedHashSet<String>();
		for(ReportColumn col:columns)
			res.add(col.getColumnName());
		return Collections.unmodifiableSet(res);
	}

	/**
	 * Adds a column to the columns set. 
	 * Order is important, i.e. first column added will be the first one displayed.
	 * Refer to {@link #getColumns()} for more details
	 * @param column - {@link ReportColumn}  
	 */
	public void addColumn(ReportColumn column) {
		addColumn(column, false);;
	}
	
	@Override
	public Set<ReportColumn> getDummyColumns() {
		return Collections.unmodifiableSet(dummyColumns);
	}
	
	public void addColumn(ReportColumn dummyColumn, boolean dummy) {
		this.columns.add(dummyColumn);
		if (dummy) {
			this.dummyColumns.add(dummyColumn);
		}
	}

	/**
	 * remove the dummyColumns from the columns list; also clear {@link #dummyColumns}
	 */
	@Override public void removeDummyColumns() {
		Iterator<ReportColumn> columnsIter = columns.iterator();
		while(columnsIter.hasNext()) {
			ReportColumn col = columnsIter.next();
			if (dummyColumns.contains(col)) columnsIter.remove();
		}
		dummyColumns.clear();
	}
	
	@Override
	public List<ReportMeasure> getMeasures() {
		return measures;
	}
		
	/**
	 * Adds a measure to the measure list.
	 * Order is important, i.e. first measure added will be the first one displayed.
	 * Refer to {@link #getMeasures()} for more details
	 * @param measure - {@link ReportMeasure}
	 */
	public void addMeasure(ReportMeasure measure) {
		this.measures.add(measure);
	}

	@Override
	public Set<ReportColumn> getHierarchies() {
		return hierarchies;
	}
	
	/**
	 * Configures which columns are hierarchies to group data by.
	 * Any data starting from the 2nd non-hierarchical column 
	 * is merged up for the group associated to the 1st non-hierarchical column 
	 * and are not displayed in a separate row (titles are concatenated in a single row). 
	 * @param hierarchies
	 */
	public void setHierarchies(Set<ReportColumn> hierarchies) {
		this.hierarchies.clear();
		this.hierarchies.addAll(hierarchies);
	}
	
	@Override
	public ReportFilters getFilters() {
		return filters;
	}
	
	public void setFilters(ReportFilters filters) {
		this.filters = filters;
	}

	/**
	 * @return the settings
	 */
	public ReportSettings getSettings() {
		return settings;
	}

	/**
	 * @param settings the settings to set
	 */
	public void setSettings(ReportSettings settings) {
		this.settings = settings;
	}

	@Override
	public List<SortingInfo> getSorters() {
		return sorters;
	}
	
	/**
	 * Configures sorting information. 
	 * @param sorters - a list of {@link SortingInfo} data
	 */
	public void setSorters(List<SortingInfo> sorters) {
		this.sorters = sorters;
	}
	
	/**
	 * Adds sorting criteria
	 * @param sorter - {@link SortingInfo}
	 */
	public void addSorter(SortingInfo sorter) {
		if (sorters == null) {
			this.sorters = new ArrayList<SortingInfo>();
		}
		this.sorters.add(sorter);
	}

	@Override
	public GroupingCriteria getGroupingCriteria() {
		return groupingCriteria;
	}
	
	/**
	 * Configures grouping criteria by year/quarter/month or no grouping (the default)
	 * @param groupingCriteria
	 */
	public void setGroupingCriteria(GroupingCriteria groupingCriteria) {
		this.groupingCriteria = groupingCriteria;
	}

//	@Override
//	public boolean isSummaryReport() {
//		return summaryReport;
//	}
//	
//	/**
//	 * @param summaryReport - true if this must be a summary report, i.e. show only sub-totals and totals
//	 */
//	public void setSummaryReport(boolean summaryReport) {
//		this.summaryReport = summaryReport;
//	}

	/**
	 * Configures whether totals for each hierarchy group and grand row totals must be calculated
	 * @param calculateRowTotals 
	 */
	public void setCalculateRowTotals(boolean calculateRowTotals) {
		this.calculateRowTotals = calculateRowTotals;
	}

	@Override
	public boolean isCalculateRowTotals() {
		return calculateRowTotals;
	}

	/**
	 * Configures whether totals per each measure column must be calculated when grouping by dates is configured.
	 * @param calculateColumnTotals 
	 */
	public void setCalculateColumnTotals(boolean calculateColumnTotals) {
		this.calculateColumnTotals = calculateColumnTotals;
	}
	
	@Override
	public boolean isCalculateColumnTotals() {
		return calculateColumnTotals;
	}
	
	/**
	 * Configures whether columns with no funding data should be displayed or not
	 * @param displayEmptyFundingColumns
	 */
	public void setDisplayEmptyFundingColumns(boolean displayEmptyFundingColumns) {
		this.displayEmptyFundingColumns = displayEmptyFundingColumns;
	}

	@Override
	public boolean isDisplayEmptyFundingColumns() {
		return this.displayEmptyFundingColumns;
	}

	/**
	 * 
	 * @param displayEmptyFundingRows
	 */
	public void setDisplayEmptyFundingRows(boolean displayEmptyFundingRows) {
		this.displayEmptyFundingRows = displayEmptyFundingRows;
	}
	
	@Override
	public boolean isDisplayEmptyFundingRows() {
		return this.displayEmptyFundingRows;
	}

	/**
	 * @return the number of rows hierarchies to calculate the subtotals
	 */
	public int getRowsHierarchiesTotals() {
		return rowsHierarchiesTotals;
	}

	/**
	 * @param rowsHierarchiesTotals the rowHierarchiesTotals to set
	 */
	public void setRowsHierarchiesTotals(int rowsHierarchiesTotals) {
		this.rowsHierarchiesTotals = rowsHierarchiesTotals;
	}

	/**
	 * @return the number of column hierarchies to calculate the subtotals
	 */
	public int getColsHierarchyTotals() {
		return colsHierarchyTotals;
	}

	/**
	 * @param colsHierarchyTotals the number of column hierarchies to calculate the subtotals
	 */
	public void setColsHierarchyTotals(int colsHierarchyTotals) {
		this.colsHierarchyTotals = colsHierarchyTotals;
	}

	@Override
	public boolean isEmptyOutputForUnspecifiedData() {
		return emptyOutputForUnspecifiedData;
	}
	
	public void setEmptyOutputForUnspecifiedData(boolean val) {
		this.emptyOutputForUnspecifiedData = val;
	}
	
	@Override public int getReportType() {
		return reportType;
	}

	
	@Override public boolean isPopulateReportHeadersIfEmpty(){
        return populateReportHeadersIfEmpty;
    }

    /**
     * Sets the flag, if the report query returns empty response the list of column headers is populated from the request
     * @param populateReportHeadersIfEmpty
     */
    public void setPopulateReportHeadersIfEmpty(boolean populateReportHeadersIfEmpty) {
        this.populateReportHeadersIfEmpty = populateReportHeadersIfEmpty;
    }
    
    @Override public boolean isAlsoShowPledges() {
    	return this.alsoShowPledges;
    }
    
    public void setAlsoShowPledges(boolean alsoShowPledges) {
    	this.alsoShowPledges = alsoShowPledges;
    }
    
	/**
	 * 1. checks that every column specified in "hierarchies" is also present in "columns"
	 * 2. brings the columns specified as hierarchies to front
	 * 
	 * Somehow hacky, but faster to code than redoing the whole rest of the code to support input in any order. Sorry, Nadia :=)  
	 * 
	 * Nadia's note: I'm glad that you made up your mind to not change the rest of the code :P
	 */
	public void reorderColumnsByHierarchies() {
		//if (getHierarchies() == null) return; // nothing to do
		LinkedHashSet<ReportColumn> newCols = new LinkedHashSet<>();
		for(ReportColumn hier:getHierarchies()) {
			if (!columns.contains(hier))
				throw new RuntimeException("column specified as hierarchy, but not as column: " + hier);
			newCols.add(hier);
		}
		for(ReportColumn col:columns) {
			if (!newCols.contains(col))
				newCols.add(col);
		}
		this.columns = newCols;
		
		/**
		 * ugly workaround for AMP-18558 when the report has no hierarchies - a saiku bug which is easier to workaround than fix
		 */
		if (getColumns().isEmpty() && getHierarchies().isEmpty()) {
			ReportColumn constantDummyColumn = new ReportColumn(ColumnConstants.CONSTANT);
			getColumns().add(constantDummyColumn);
			getHierarchies().add(constantDummyColumn);
			setCalculateRowTotals(false);
			/* we must not reconfigure column totals to be calculated 
			setCalculateColumnTotals(true);
			*/
		}
	}

	public boolean getUsesFundingFlows() {
		return usesFundingFlows;
	}

	void setUsesFundingFlows(boolean usesFundingFlows) {
		this.usesFundingFlows = usesFundingFlows;
	}
	
	/**
	 * sets {@link #usesFundingFlows} to true iff one of the funding-flows-using measure is used by the report
	 */
	public void computeUsesFundingFlows() {
		boolean res = false;
		for(ReportMeasure rm:this.measures) {
			res |= (ArConstants.DIRECTED_MEASURE_TO_DIRECTED_TRANSACTION_VALUE.keySet().contains(rm.getMeasureName()));
		}
		this.setUsesFundingFlows(res);
	}

	public String getProjectTitleColumn() {
		return projectTitleColumn;
	}

	public void setProjectTitleColumn(String projectTitleColumn) {
		this.projectTitleColumn = projectTitleColumn;
	}
}
