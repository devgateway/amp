/**
 * 
 */
package org.dgfoundation.amp.reports.mondrian.converters;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.error.AMPException;
import org.dgfoundation.amp.mondrian.MondrianETL;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportElement;
import org.dgfoundation.amp.newreports.ReportMeasure;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.newreports.SortingInfo;
import org.digijava.kernel.ampapi.mondrian.util.MondrianMapping;
import org.digijava.module.aim.ar.util.FilterUtil;
import org.digijava.module.aim.dbentity.AmpColumns;
import org.digijava.module.aim.dbentity.AmpMeasures;
import org.digijava.module.aim.dbentity.AmpReportColumn;
import org.digijava.module.aim.dbentity.AmpReportHierarchy;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;

/**
 * Converter for {@link AmpReport} to {@link ReportSpecification} 
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
	public static ReportSpecificationImpl convert(AmpReports report) throws AMPException {
		return (new AmpReportsToReportSpecification(report)).convert();
	}
	
	private ReportSpecificationImpl convert() throws AMPException {
		//init data
		arFilter = FilterUtil.buildFilterFromSource(report);
		spec = new ReportSpecificationImpl(report.getName(), report.getType().intValue());
		
		//configure report
		configureReportData();
		configureNonEmpty();
		configureHierarchies();
		makeFundingFlowsHacks();
		configureSorting();
		
		spec.setEmptyOutputForUnspecifiedData(report.getDrilldownTab() == null || !report.getDrilldownTab());
		spec.setAlsoShowPledges(report.getAlsoShowPledges() == null ? false : report.getAlsoShowPledges());
		
		//configure filters & settings
		AmpARFilterConverter arFilterTranslator = new AmpARFilterConverter(arFilter);
		spec.setSettings(arFilterTranslator.buildSettings());
		spec.setFilters(arFilterTranslator.buildFilters());
		
		MtefConverter.instance.convertMtefs(this.report, this.spec);
		makeFundingFlowsHacks(); // yes, twice
		
		return spec;
	}

	/**
	 * AMP-21236: delete all roles- and roles-superior columns (like GROUP / TYPE)
	 * minimum-cost minimally-breaking
	 */
	protected void makeFundingFlowsHacks() {
		if (!MondrianETL.BUG_CHOOSER)
			return; // if it is true, then no workaround is needed - the bug does not exist (but an another one, unworkaroundable starts manifesting)
		
		spec.computeUsesFundingFlows();
		
		Set<String> hierNames = new HashSet<>();
		if (spec.getHierarchies() != null) {
			for(ReportColumn hier:spec.getHierarchies())
				hierNames.add(hier.getColumnName());
		}
		
		Set<String> columnsToRemove = new HashSet<>();
		if (spec.getUsesFundingFlows()) {
			for(ReportColumn col:spec.getColumns()) {
				if (ArConstants.COLUMNS_LINKED_WITH_FLOW_ROLES.contains(col.getColumnName()) && (!hierNames.contains(col.getColumnName())))
					columnsToRemove.add(col.getColumnName());
			}
		}
		
		// delete columns
		Iterator<ReportColumn> colsIt = spec.getColumns().iterator();
		while (colsIt.hasNext())
			if (columnsToRemove.contains(colsIt.next().getColumnName()))
				colsIt.remove();

		// this code should not normally change anything, because columnsToRemove only contains cols which are NOT in hiers. Left here for refactoring easiness
		Iterator<ReportColumn> hiersIt = spec.getHierarchies().iterator();
		while (hiersIt.hasNext())
			if (columnsToRemove.contains(hiersIt.next().getColumnName()))
				hiersIt.remove();
	}
	
	private void configureReportData() {
		if (report.getHideActivities()) {
			//this is a summary report
			for (AmpReportHierarchy hierarchy : report.getHierarchies())
				spec.addColumn(new ReportColumn(hierarchy.getColumn().getColumnName()));
		} else {
			for (AmpColumns column : getOrderedColumns()) 
				if (!column.isMtefColumn() && !column.isRealMtefColumn()) { // MTEF columns are processed separately by MtefConverter, which reads the AmpReports instance directly
					spec.addColumn(new ReportColumn(column.getColumnName()));
				}
		}
		
		boolean measuresMovedAsColumns = false;
		for (AmpMeasures measure: report.getOrderedMeasures()) {
			// if old reports will become obsolete, then remove these compatibility adjustments
			String measureName = measure.getMeasureName();
			if (MondrianMapping.definedColumns.contains(measureName)) {
				spec.addColumn(new ReportColumn(measureName));
				measuresMovedAsColumns = true;
			} else {
				spec.addMeasure(new ReportMeasure(measureName));
			}
		}
		/* workaround for reports that have all measures that are now columns in Mondrian based reports:
		 * add a dummy measure (most common Actual Commitments) & follow up ticket: AMP-19808
		 * => remove this workaround when a proper fix is available
		 */
		if (measuresMovedAsColumns && spec.getMeasures().size() == 0) {
			spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_COMMITMENTS));
		}
		// end AMP-19808 workaround
		
		spec.setCalculateColumnTotals(true);
		spec.setCalculateRowTotals(true);
		
		//workaround for AMP-18257, issue #1
		final String groupingOption = report.getDrilldownTab() ? "" : report.getOptions(); 
		
		switch(groupingOption) {
		case "A": spec.setGroupingCriteria(GroupingCriteria.GROUPING_YEARLY); break;
		case "Q": spec.setGroupingCriteria(GroupingCriteria.GROUPING_QUARTERLY); break;
		case "M": spec.setGroupingCriteria(GroupingCriteria.GROUPING_MONTHLY); break;
		default: 
			spec.setGroupingCriteria(GroupingCriteria.GROUPING_TOTALS_ONLY);
			spec.setCalculateColumnTotals(false);
			break;
		}
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
		
		for (AmpColumns col : registeredOrderColumns)
			if (!orderedColumns.contains(col))
				orderedColumns.add(col);
		
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
		boolean dateFilterHidesProjects = "true".equalsIgnoreCase(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DATE_FILTER_HIDES_PROJECTS));
		if (dateFilterHidesProjects && !report.getDrilldownTab() && 
				(arFilter.wasDateFilterUsed() || (report.getHierarchies().size() > 0))
				)
			spec.setDisplayEmptyFundingRows(false);
		else 
			spec.setDisplayEmptyFundingRows(true);
	}
	
	private void configureHierarchies() {
		spec.setColsHierarchyTotals(0);
		spec.setRowsHierarchiesTotals(report.getHierarchies().size());

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
					validSortingRules.put(Integer.valueOf(sortingArray[0]) - 1, sortingArray); //keep the latest sorting, which is the valid one
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
					addFundingSorting(fundingColumns, asc, hierarchyColumn);
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
				addFundingSorting(sorting, arFilter.getSortByAsc(), null);
		} 
	}
	
	private void addFundingSorting(String[] fundingColumns, boolean asc, ReportColumn hierarchyColumn) {
		String measureName = fundingColumns[fundingColumns.length - 1];
		ReportMeasure measureCol = new ReportMeasure(measureName);
		
		if (ArConstants.COLUMN_FUNDING.equals(fundingColumns[0])) {
			//this is a funding column grouped by year/quarter/month, not the total costs
			if (isValidFundingColumn(fundingColumns)) {
				try {
					String year = fundingColumns[1]; 
					String quarter = null, month = null; 
					switch (spec.getGroupingCriteria()) {
					case GROUPING_QUARTERLY: 
						quarter = fundingColumns[2];
						break;
					case GROUPING_MONTHLY: 
						month = fundingColumns[2];
						break;
					default: break; 
					}
					if (hierarchyColumn == null)
						spec.addSorter(new SortingInfo(year, quarter, month, measureCol, asc));
					else 
						spec.addSorter(new SortingInfo(hierarchyColumn, year, quarter, month, measureCol, asc));
				} catch(Exception e) {
					logger.error("Skipping the problemating sorting. Please check the cause: " + e.getMessage());
				}
			} 
		} else {
			//this is the totals costs
			if (hierarchyColumn == null)
				spec.addSorter(new SortingInfo(measureCol, asc, true));
			else {
				LinkedHashMap<ReportElement, FilterRule> tuple = new LinkedHashMap<ReportElement, FilterRule>();
				tuple.put(new ReportElement(hierarchyColumn), null);
				tuple.put(new ReportElement(measureCol), null);
				spec.addSorter(new SortingInfo(tuple, asc));
			}
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
			int colId = Integer.valueOf(sortingInfo[0]) -1;
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
	
	private boolean isValidFundingColumn(String[] fundingColumns) {
		final String suffix = " thus cannot sort by " + fundingColumns;
		String err = null;
		switch (spec.getGroupingCriteria()) {
		case GROUPING_TOTALS_ONLY: err = "No grouping by dates,"; break;
		case GROUPING_YEARLY: if (fundingColumns.length != 3) err = "Grouping by years,"; break;
		case GROUPING_QUARTERLY: if (fundingColumns.length != 4) err = "Grouping by quarters,"; break;
		//old reports group by year + month, no quarter in between
		case GROUPING_MONTHLY: if (fundingColumns.length != 4) err = "Grouping by months,"; break; 
		}
		if (err != null) {
			logger.error(err + suffix);
			return false;
		}
		return true;
	}
	
	private String getSorters(Map<Integer,String[]> sorters) {
		String str = "";
		for(String[] s : sorters.values()) 
			str += "," + Arrays.toString(s);
		return str.length() > 0 ? str.substring(0) : str;
	}
}
