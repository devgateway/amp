package org.dgfoundation.amp.newreports;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.stream.Collectors;

/**
 * this defines a report specification, e.g. the requested structure of a report. 
 * An instance of this class defines univoquely the kind of report to be generated: columns, hierarchies, filters, sorting
 * @author Dolghier Constantin
 *
 */
public interface ReportSpecification extends Cloneable {
    
    /**
     * one of the ArConstants.LEGAL_REPORT_TYPES constants 
     * @return
     */
    public int getReportType();
    
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
    public Set<ReportMeasure> getMeasures();
    
    /**
     * this set should be a strict subset of the one returned at {@link #getColumns()}
     * @return
     */
    public Set<ReportColumn> getHierarchies();

    /**
     * List of hierarchies that must be added before executing the report and then removed from output.
     */
    Set<ReportColumn> getInvisibleHierarchies();

    public ReportFilters getFilters();
    
    /** @return {@link ReportSettings} - settings of the current report */
    public ReportSettings getSettings();
    
    public List<SortingInfo> getSorters();
    
    public GroupingCriteria getGroupingCriteria();
            
    /**
     * @return whether columns with no funding data should be displayed or not
     */
    public boolean isDisplayEmptyFundingColumns();
    
    /**
     * @return whether rows with no funding data should be displayed or not
     */
    public boolean isDisplayEmptyFundingRows();

    /**
     * @return whether rows with no funding data should be displayed when filtering by transaction hierarchy
     */
    public boolean isDisplayEmptyFundingRowsWhenFilteringByTransactionHierarchy();

    /** 
     * @return whether textual cells with no data should display "" (if true) or "[columnName] unspecified" (if false) 
     */
    public boolean isEmptyOutputForUnspecifiedData();

    /**
     * If the report query returns empty response the list of column headers is populated from the request
     * @return
     */
    public boolean isPopulateReportHeadersIfEmpty();
    
    /**
     * if the report type is different from ArConstants.PLEDGES_TYPE, this will lead to including the pledges which match the filter into the report
     * @return
     */
    public boolean isAlsoShowPledges();

    /**
     * whether to collapse the CRDs to trail cells only when displaying
     * @return
     */
    public boolean isSummaryReport();
    
    /**
     * whether to show original currencies of transactions
     * @return
     */
    boolean isShowOriginalCurrency();
    
    /**
     * whether to collapse same-named hierarchies with different IDs
     * @return
     */
    public default ReportCollapsingStrategy getSubreportsCollapsing() {
        return ReportCollapsingStrategy.UNKNOWNS;
    }
    
    /**
     * these are the simple names of the columns {@see #getColumns()}
     * @return the set of column names to use
     */
    @JsonIgnore
    public default Set<String> getColumnNames() {
        return new LinkedHashSet<>(getColumns().stream().map(z -> z.getColumnName()).collect(Collectors.toList()));
    }

    /**
     * these are the simple names of the hierarchies {@see #getHierarchies()}
     * @return the set of column names to use
     */
    @JsonIgnore
    public default Set<String> getHierarchyNames() {
        return new LinkedHashSet<>(getHierarchies().stream().map(z -> z.getColumnName()).collect(Collectors.toList()));
    }

    @JsonIgnore
    default Set<String> getInvisibleHierarchyNames() {
        return new LinkedHashSet<>(getInvisibleHierarchies().stream()
                .map(ReportColumn::getColumnName)
                .collect(Collectors.toList()));
    }

    @JsonIgnore
    public default Set<String> getMeasureNames() {
        return new LinkedHashSet<>(getMeasures().stream().map(z -> z.getMeasureName()).collect(Collectors.toList()));
    }

    public boolean isDisplayTimeRangeSubTotals();

}
