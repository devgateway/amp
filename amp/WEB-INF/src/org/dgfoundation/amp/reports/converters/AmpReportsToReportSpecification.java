package org.dgfoundation.amp.reports.converters;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.error.AMPException;
import org.dgfoundation.amp.newreports.*;
import org.digijava.kernel.ampapi.endpoints.reports.ReportsUtil;
import org.digijava.module.aim.ar.util.FilterUtil;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;

import java.util.*;
import java.util.Map.Entry;

/**
 * Converter for {@link AmpReports} to {@link ReportSpecification}
 * @author Nadejda Mandrescu
 */
public class AmpReportsToReportSpecification {
    protected static final Logger logger = Logger.getLogger(AmpReportsToReportSpecification.class);
    
    private static final String TITLE = ArConstants.HIERARCHY_SORTER_TITLE;
    private static final String ASC = "ascending";
    private static final String DESC = "descending";

    private AmpReports report;
    private AmpARFilter arFilter;
    private ReportSpecificationImpl spec;
    
    private AmpReportsToReportSpecification(AmpReports report) {
        this.report = report;
    }
    
    /**
     * Conversion of {@link AmpReports} report to Reports API report structure 
     * @param report - {@link AmpReports} 
     * @return {@link ReportSpecificationImpl}
     * @throws AMPException 
     */
    public static ReportSpecificationImpl convert(AmpReports report) {
        return (new AmpReportsToReportSpecification(report)).convert();
    }
    
    private ReportSpecificationImpl convert() {
        logger.info("converting report " + report.getName() + " to ReportSpec");
        //init data
        arFilter = FilterUtil.buildFilterFromSource(report);
        spec = new ReportSpecificationImpl(report.getName(), report.getType().intValue());
        
        //configure report
        configureReportData();
        configureNonEmpty();
        configureHierarchies();
        configureInvisibleHierarchies();
        configureSorting();

        spec.setEmptyOutputForUnspecifiedData(report.getDrilldownTab() == null || !report.getDrilldownTab());
        spec.setAlsoShowPledges(report.shouldInjectPledgeColumnsAsProjectColumns());
        spec.setShowOriginalCurrency(report.getShowOriginalCurrency());
        spec.setIncludeLocationChildren(arFilter.isIncludeLocationChildren());
        
        //configure filters & settings
        AmpARFilterConverter arFilterTranslator = new AmpARFilterConverter(arFilter);
        spec.setSettings(arFilterTranslator.buildSettings());
        spec.setFilters(arFilterTranslator.buildFilters());
        return spec;
    }

    private void configureInvisibleHierarchies() {
        if (report.getType() == ArConstants.DONOR_TYPE && report.getSplitByFunding()) {
            spec.addColumn(new ReportColumn(ColumnConstants.FUNDING_ID));
            spec.addInvisibleHierarchy(new ReportColumn(ColumnConstants.FUNDING_ID));
        }
    }

    private void configureReportData() {
        spec.setSummaryReport(report.getHideActivities());
        if (report.getHideActivities()) {
            //this is a summary report
            for (AmpReportHierarchy hierarchy : report.getHierarchies())
                spec.addColumn(new ReportColumn(hierarchy.getColumn().getColumnName()));
            Set<String> hierNames = spec.getHierarchyNames();
            for(AmpColumns col:getOrderedColumns())
                if (!hierNames.contains(col.getColumnName()))
                    spec.addColumn(new ReportColumn(col.getColumnName()));
        } else {
            for (AmpColumns column : getOrderedColumns())
                spec.addColumn(new ReportColumn(column.getColumnName()));
        }
        
        for (AmpMeasures measure: report.getOrderedMeasures()) {
            // if old reports will become obsolete, then remove these compatibility adjustments
            String measureName = measure.getMeasureName();
            spec.addMeasure(new ReportMeasure(measureName));
        }
                
        //workaround for AMP-18257, issue #1
        final String groupingOption = report.getDrilldownTab() ? "" : report.getOptions();
        ReportsUtil.setGroupingCriteria(spec, groupingOption);
    }
    
    private Set<AmpColumns> getOrderedColumns() {
        //workaround for AMP-18257, issue #2
        /* cannot simply use this, because it still doesn't bring the ordered list of columns :(
         * ARUtil.createOrderedColumns(getShowAblesColumns(), getHierarchies());
         */
        Set<AmpColumns> orderedColumns = new LinkedHashSet<AmpColumns>(report.getColumns().size());
        
        TreeMap<Long, AmpColumns> columns = new TreeMap<Long, AmpColumns>();
        for (AmpReportColumn col : report.getColumns())
            columns.put(col.getOrderId(), col.getColumn());
        Collection<AmpColumns> registeredOrderColumns = columns.values();
        
        for (AmpReportHierarchy hierarchy : report.getHierarchies()) {
            if(registeredOrderColumns.contains(hierarchy.getColumn()))
                orderedColumns.add(hierarchy.getColumn());
        }

        orderedColumns.addAll(registeredOrderColumns);
        
        return orderedColumns;
    }
    
    private void configureNonEmpty() {
        // default expectations
        Boolean allowEmptyFundingColumns = report.getAllowEmptyFundingColumns();
        // We make this check because some old tabs return allowEmptyFundingColumns = null which causes an exception in
        // the next line.
        spec.setDisplayEmptyFundingColumns(allowEmptyFundingColumns != null ? allowEmptyFundingColumns : false);
        
        //detect if we should display empty rows or not
        //the existing logic rules are applied here from old reports generation mechanism
        String removeEmptyRows = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.REPORTS_REMOVE_EMPTY_ROWS);
        boolean dateFilterHidesProjects = "true".equalsIgnoreCase(removeEmptyRows);
        spec.setDisplayEmptyFundingRows(!dateFilterHidesProjects || report.getDrilldownTab() ||
                (!arFilter.wasDateFilterUsed() && (report.getHierarchies().size() == 0)));

        spec.setDisplayEmptyFundingRowsWhenFilteringByTransactionHierarchy(true);
    }
    
    private void configureHierarchies() {
        Set<ReportColumn> hierarchies = new LinkedHashSet<ReportColumn>(report.getHierarchies().size());
        for (AmpReportHierarchy column : report.getHierarchies()) {
            hierarchies.add(new ReportColumn(column.getColumn().getColumnName()));
        }
        spec.setHierarchies(hierarchies);
    }
    
    /**************                 SORTING                    **************************/
    
    private void configureSorting() {
        //TODO: please cleanup this function from workarounds when AMP-18205 is fixed

        //hierarchy sorting has a priority
        //now check if we have hierarchies sorting
        if (arFilter.getHierarchySorters() != null && arFilter.getHierarchySorters().size() > 0
                && spec.getHierarchies().size() > 0) { 
            ReportColumn[] hierarchies = spec.getHierarchies().toArray(new ReportColumn[0]);
            //start workaround for AMP-18205, issue #1 & #2
            Map<Integer, String[]> validSortingRules = new TreeMap<Integer, String[]>();
            for (String hierarchySorter : arFilter.getHierarchySorters()) {
                //sorting settings are encoded as "<column_number>_<property to filter by>_{ascending|descending}", 
                String[] sortingArray = hierarchySorter.split("_"); 
                if (isValidHierarchySortingString(sortingArray)) {
                    //column number starts from 1, so we decrese to the actual index by 1
                    validSortingRules.put(Integer.parseInt(sortingArray[0]) - 1, sortingArray); //keep the latest sorting, which is the valid one
                }
            }
            
            logger.info("[" + spec.getReportName() +"] sorting rules :" + getSorters(validSortingRules));
            //end workaround for AMP-18205, issue #1 & #2, now we'll work with valid sortings
            for(Entry<Integer, String[]> pair : validSortingRules.entrySet()) {
                ReportColumn hierarchyColumn = hierarchies[pair.getKey()]; 
                boolean asc = ASC.equals(pair.getValue()[2]);
                if (TITLE.equals(pair.getValue()[1]))
                    spec.addSorter(new SortingInfo(hierarchyColumn, asc));
                else { 
                    //funding columns syntax is "{Funding|Total Costs}[--year[--{quarter|month}]]--<measure name>" 
                    String[] fundingColumns = pair.getValue()[1].split("--");
                    spec.addSorter(buildFundingSorting(fundingColumns, asc, hierarchyColumn));
                }
            }
        }
        
        //check sort by setting first
        if (StringUtils.isNotBlank(arFilter.getSortBy())) {
            //sort by syntax example: 
            //1) sorting by non-hierarchical column: /Primary Sector
            //2) sorting by funding: /Funding/2010/Actual Disbursements
            //3) sorting by totals: /Total Costs/Actual Commitments
            String[] sorting = arFilter.getSortBy().substring(1).split("/");
            if (sorting.length == 1){ //use case 1) non-hierarchical column
                ReportColumn rCol = new ReportColumn(sorting[0]);
                if (!spec.getHierarchies().contains(rCol)) //workaround for AMP-18205, issue #4 if column changes from non-hierarchy to hierarchy
                    spec.addSorter(new SortingInfo(rCol, arFilter.getSortByAsc()));
            } else //use case 2) or 3) 
                spec.addSorter(buildFundingSorting(sorting, arFilter.getSortByAsc(), null));
        } 
    }
    
    private SortingInfo buildFundingSorting(String[] fundingColumns, boolean asc, ReportColumn hierarchyColumn) {
        if (ArConstants.COLUMN_FUNDING.equals(fundingColumns[0])) {
            //this is a funding column grouped by year/quarter/month, not the total costs
            return new SortingInfo(Arrays.asList(fundingColumns).subList(1, fundingColumns.length), SortingInfo.ROOT_PATH_FUNDING, asc);
        } else {
            //this is the totals costs
            return new SortingInfo(Arrays.asList(fundingColumns).subList(1, fundingColumns.length), SortingInfo.ROOT_PATH_TOTALS, asc);
        }
    }
    
    private boolean isValidHierarchySortingString(String[] sortingInfo) {
        //sorting settings are encoded as "<column_number>_<property to filter by>_{ascending|descending}",
        //3 entries are expected
        String err = null;
        String prefix = "AmpReport sorting info in AmpARFilter hierarchy Sorters";
        if (sortingInfo.length != 3)
            err = " is expected to be formed as '<column_number>_<property to filter by>_{ascending|descending}'";
        else if (!StringUtils.isNumeric(sortingInfo[0]))
            err = " is expecting a number on as first argument of its structure '<column_number>_<property to filter by>_{ascending|descending}'";
        else {
            int colId = Integer.parseInt(sortingInfo[0]) -1;
            if (colId < 0 || colId >= spec.getHierarchies().size())
                err = " is expecting the column number to be within 1 and hiearchies size = " + spec.getHierarchies().size() + ", while it is = " + colId;
            else if (!ASC.equals(sortingInfo[2]) && !DESC.equals(sortingInfo[2]))
                    err = " is expecting to be sorted '" + ASC + "' or '" + DESC + "', but is sorted '" + sortingInfo[2] + "'";
        }
        if (err != null) {
            logger.error(prefix + err);
            return false;
        }
        return true;
    }
    
    private String getSorters(Map<Integer,String[]> sorters) {
        StringBuilder str = new StringBuilder();
        for(String[] s : sorters.values()) 
            str.append(",").append(Arrays.toString(s));
        return str.toString();
    }
}
