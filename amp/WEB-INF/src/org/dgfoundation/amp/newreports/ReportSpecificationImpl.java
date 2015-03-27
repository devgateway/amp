/**
 * 
 */
package org.dgfoundation.amp.newreports;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.dgfoundation.amp.ar.ArConstants;

/**
 * Stores a report configuration by implementing {@link ReportSpecification} and defines all data required to generate a report. 
 * @author Nadejda Mandrescu
 *
 */
public class ReportSpecificationImpl implements ReportSpecification {
	private int reportType;	
	private String reportName = null;
	private Set<ReportColumn> columns = null;
	private Set<String> columnNames = null;
	private Set<ReportColumn> dummyColumns = null;
	private Set<ReportColumn> hierarchies = null;
	private List<ReportMeasure> measures = null;
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
		if (columns == null) {
			columns = new LinkedHashSet<ReportColumn>();
		}	
		return columns;
	}
	
	/**
	 * Configures the columns set.
	 * Refer to {@link #getColumns()} for more details
	 */
	public void setColumns(Set<ReportColumn> columns) {
		this.columns = columns;
		this.getColumnNames().clear();
		for (ReportColumn rc : columns) {
			columnNames.add(rc.getColumnName());
		}
	}
	
	@Override
	public Set<String> getColumnNames() {
		if (columnNames == null) {
			columnNames = new LinkedHashSet<String>();
		}
		return columnNames;
	}

	/**
	 * Adds a column to the columns set. 
	 * Order is important, i.e. first column added will be the first one displayed.
	 * Refer to {@link #getColumns()} for more details
	 * @param column - {@link ReportColumn}  
	 */
	public void addColumn(ReportColumn column) {
		if (columns == null) {
			columns = new LinkedHashSet<ReportColumn>();
		}
		this.columns.add(column);
		this.getColumnNames().add(column.getColumnName());
	}

	@Override
	public Set<ReportColumn> getDummyColumns() {
		if (dummyColumns == null) {
			dummyColumns = new LinkedHashSet<ReportColumn>();
		}
		return dummyColumns;
	}

	/**
	 * @param dummyColumns the dummyColumns to set
	 */
	public void setDummyColumns(Set<ReportColumn> dummyColumns) {
		this.dummyColumns = dummyColumns;
	}
	
	public void addDummyColumn(ReportColumn dummyColumn) {
		this.getDummyColumns().add(dummyColumn);
	}

	@Override
	public List<ReportMeasure> getMeasures() {
		if (measures == null) {
			measures = new ArrayList<ReportMeasure>();
		}
		return measures;
	}
	
	/**
	 * Configures measures list. 
	 * Refer to {@link #getMeasures()} for more details
	 * @param measures
	 */
	public void setMeasures(List<ReportMeasure> measures) {
		this.measures = measures;
	}
	
	/**
	 * Adds a measure to the measure list.
	 * Order is important, i.e. first measure added will be the first one displayed.
	 * Refer to {@link #getMeasures()} for more details
	 * @param measure - {@link ReportMeasure}
	 */
	public void addMeasure(ReportMeasure measure) {
		if (this.measures == null) {
			this.measures = new ArrayList<ReportMeasure>();
		}
		this.measures.add(measure);
	}

	@Override
	public Set<ReportColumn> getHierarchies() {
		if (hierarchies == null)
			hierarchies = new LinkedHashSet<ReportColumn>();
		return hierarchies;
	}
	
	/**
	 * Configures which columns are hierarchies to group data by.
	 * Any data starting from the 2nd non-hierarchical column 
	 * is merged up for the group associated to the 1st non-hierarchical column 
	 * and are not displayed in a separate row (titles are concatenated in a single row). 
	 * The hierarchies must be a set of first consecutive columns configured in the report 
	 * (e.g. first 2 out of 4, i.e. column #1 and #2). 
	 * If you want to display all columns as hierarchies, 
	 * then you can simply .setHierarchies(spec.getColumns()).
	 * @param hierarchies
	 */
	public void setHierarchies(Set<ReportColumn> hierarchies) {
		this.hierarchies = hierarchies;
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
}
