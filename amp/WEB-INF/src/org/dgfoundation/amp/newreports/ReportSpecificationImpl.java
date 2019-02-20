package org.dgfoundation.amp.newreports;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.dgfoundation.amp.ar.ArConstants;

/**
 * Stores a report configuration by implementing {@link ReportSpecification} and defines all data required to generate a report. 
 * 
 * @author Nadejda Mandrescu
 */
public class ReportSpecificationImpl implements ReportSpecification {
    protected int reportType;   
    protected String reportName = null;
    protected Set<ReportColumn> columns = new LinkedHashSet<ReportColumn>();
    protected Set<ReportColumn> hierarchies = new LinkedHashSet<ReportColumn>();
    protected Set<ReportColumn> invisibleHierarchies = new LinkedHashSet<>();
    protected Set<ReportMeasure> measures = new LinkedHashSet<ReportMeasure>();
    protected ReportFilters filters = null;
    protected ReportSettings settings = null;
    protected List<SortingInfo> sorters = null;
    protected GroupingCriteria groupingCriteria = GroupingCriteria.GROUPING_TOTALS_ONLY;
    //private boolean summaryReport = false;
    protected boolean displayEmptyFundingColumns = false;
    protected boolean displayEmptyFundingRows = false;
    protected boolean displayEmptyFundingRowsWhenFilteringByTransactionHierarchy = false;
    protected boolean emptyOutputForUnspecifiedData = true;
    protected boolean alsoShowPledges = false;
    protected boolean summaryReport = false;
    protected ReportCollapsingStrategy reportCollapsingStrategy = ReportCollapsingStrategy.UNKNOWNS;
    protected boolean displayTimeRangeSubtotals = true;
    protected boolean showOriginalCurrency = false;
    
    /**
     * If the report query results in empty data
     * Should the headers be populated
     */
    protected boolean populateReportHeadersIfEmpty = false;
    
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
    
    public void setColumns(Set<ReportColumn> cols) {
        this.columns.clear();
        this.columns.addAll(cols);
    }

    /**
     * Adds a column to the columns set. 
     * Order is important, i.e. first column added will be the first one displayed.
     * Refer to {@link #getColumns()} for more details
     * @param column - {@link ReportColumn}
     * @return itself for chaining  
     */
    public ReportSpecificationImpl addColumn(ReportColumn column) {
        this.columns.add(column);
        return this;
    }
        
    @Override
    public Set<ReportMeasure> getMeasures() {
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
    public Set<ReportColumn> getInvisibleHierarchies() {
        return invisibleHierarchies;
    }

    public void addInvisibleHierarchy(ReportColumn hierarchy) {
        invisibleHierarchies.add(hierarchy);
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

    @JsonIgnore
    public ReportSettingsImpl getOrCreateSettings() {
        if (settings == null)
            settings = new ReportSettingsImpl();
        return (ReportSettingsImpl) settings;
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

//  @Override
//  public boolean isSummaryReport() {
//      return summaryReport;
//  }
//  
//  /**
//   * @param summaryReport - true if this must be a summary report, i.e. show only sub-totals and totals
//   */
//  public void setSummaryReport(boolean summaryReport) {
//      this.summaryReport = summaryReport;
//  }

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

    public boolean isDisplayEmptyFundingRowsWhenFilteringByTransactionHierarchy() {
        return displayEmptyFundingRowsWhenFilteringByTransactionHierarchy;
    }

    public void setDisplayEmptyFundingRowsWhenFilteringByTransactionHierarchy(boolean v) {
        this.displayEmptyFundingRowsWhenFilteringByTransactionHierarchy = v;
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
    
    public void setReportCollapsingStrategy(ReportCollapsingStrategy reportCollapsingStrategy) {
        this.reportCollapsingStrategy = reportCollapsingStrategy;
    }
    
    public ReportCollapsingStrategy getSubreportsCollapsing() {
        return reportCollapsingStrategy;
    }
    
    public static ReportSpecificationImpl buildFor(String reportName, List<String> columns, List<String> measures, 
            List<String> hierarchies, GroupingCriteria groupingCriteria) {
        return buildFor(reportName, columns, measures, hierarchies, groupingCriteria, ArConstants.DONOR_TYPE);
    }

    public static ReportSpecificationImpl buildFor(String reportName, List<String> columns, List<String> measures, 
            List<String> hierarchies, GroupingCriteria groupingCriteria, int reportType) {
        ReportSpecificationImpl spec = new ReportSpecificationImpl(reportName, reportType);
        
        for(String columnName:columns)
            spec.addColumn(new ReportColumn(columnName));

        if (measures != null) {
            for (String measureName : measures)
                spec.addMeasure(new ReportMeasure(measureName));
        }
        
        if (hierarchies != null) {
            for(String hierarchyName:hierarchies) {
                if (!columns.contains(hierarchyName))
                    throw new RuntimeException("hierarchy should be present in column list: " + hierarchyName);
                spec.getHierarchies().add(new ReportColumn(hierarchyName));
            }
        }
        
        spec.setGroupingCriteria(groupingCriteria);

        return spec;
    }

    public void setSummaryReport(boolean summaryReport) {
        this.summaryReport = summaryReport;
    }
    
    @Override
    public boolean isSummaryReport() {
        return summaryReport;
    }

    @Override
    public boolean isDisplayTimeRangeSubTotals() {
        return displayTimeRangeSubtotals;
    }

    public void setDisplayTimeRangeSubtotals(Boolean displayTimeRangeSubtotals) {
        this.displayTimeRangeSubtotals = displayTimeRangeSubtotals;
    }

    public boolean isShowOriginalCurrency() {
        return showOriginalCurrency;
    }

    public void setShowOriginalCurrency(boolean showOriginalCurrency) {
        this.showOriginalCurrency = showOriginalCurrency;
    }
    
}
