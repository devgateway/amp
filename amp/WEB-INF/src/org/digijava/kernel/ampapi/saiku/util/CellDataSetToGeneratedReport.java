/**
 * 
 */
package org.digijava.kernel.ampapi.saiku.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.error.AMPException;
import org.dgfoundation.amp.newreports.AmountCell;
import org.dgfoundation.amp.newreports.AmountsUnits;
import org.dgfoundation.amp.newreports.DateCell;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.ReportArea;
import org.dgfoundation.amp.newreports.ReportAreaImpl;
import org.dgfoundation.amp.newreports.ReportCell;
import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.dgfoundation.amp.newreports.ReportSettings;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.newreports.TextCell;
import org.dgfoundation.amp.reports.CustomAmounts;
import org.dgfoundation.amp.reports.DateColumns;
import org.dgfoundation.amp.reports.PartialReportArea;
import org.dgfoundation.amp.reports.mondrian.MondrianReportSpec;
import org.dgfoundation.amp.reports.mondrian.MondrianReportUtils;
import org.digijava.kernel.ampapi.mondrian.util.MoConstants;
import org.digijava.kernel.ampapi.saiku.SaikuReportArea;
import org.saiku.olap.dto.resultset.AbstractBaseCell;
import org.saiku.olap.dto.resultset.CellDataSet;
import org.saiku.service.olap.totals.TotalNode;
import org.saiku.service.olap.totals.aggregators.TotalAggregator;

/**
 * Translator of the Saiku CellDataSet to API GeneratedReport
 * @author Nadejda Mandrescu
 *
 */
public class CellDataSetToGeneratedReport {
	protected static final Logger logger = Logger.getLogger(CellDataSetToGeneratedReport.class);
	
	private MondrianReportSpec spec;
	private final CellDataSet cellDataSet;
	private final List<ReportOutputColumn> leafHeaders;
	private final List<Long> cellDataSetActivities;
	private DecimalFormat numberFormat;
	private NumberFormat readingNumberFormat;
	
	/**
	 * measureTotals[MEASURE][ROW]
	 */
	private TotalAggregator[][] measureTotals = null;
	private List<TotalNode>[] rowTotals = null;
	private int[] currentSubGroupIndex;
	private boolean isTotalsOnlyReport = false;
	private Set<Integer> emptyColTotalsMeasuresIndexes = new TreeSet<Integer>();
	private Set<Integer> emptyRowTotalsMeasuresIndexes = new TreeSet<Integer>();
	private Set<Integer> amountMultiplierColumns = new TreeSet<Integer>();
	private final AmountsUnits unitsOption;
	
	public CellDataSetToGeneratedReport(MondrianReportSpec spec, CellDataSet cellDataSet, 
			List<ReportOutputColumn> leafHeaders, List<Long> cellDataSetActivities) {
		this.spec = spec;
		this.cellDataSet = cellDataSet;
		this.leafHeaders = leafHeaders;
		this.cellDataSetActivities = cellDataSetActivities;
		this.unitsOption = this.spec.getSettings().getUnitsOption();
		init();
	}
	
	private void init() {
		ReportSettings settings = spec.getSettings() == null ? 
				MondrianReportUtils.getCurrentUserDefaultSettings() : spec.getSettings(); 
		if (settings.getCurrencyFormat() != null ) {
			this.numberFormat = settings.getCurrencyFormat();
		} else { 
			this.numberFormat = MondrianReportUtils.getCurrentUserDefaultSettings().getCurrencyFormat();
		}
		initColumnIdsToApplyAmountsMultiplier();
		// This is a bit ugly it will fix end points and tabs with number formating issues 
		// Ensure Locale is always US
		Locale locale  = new Locale("en", "US");
		readingNumberFormat = NumberFormat.getInstance(locale);
		//init measure totals if they are available
		if (cellDataSet.getColTotalsLists() != null && cellDataSet.getColTotalsLists().length > 0 
				&& cellDataSet.getColTotalsLists()[0] != null && cellDataSet.getColTotalsLists()[0].size() > 0) {
			this.measureTotals = cellDataSet.getColTotalsLists()[0].get(0).getTotalGroups();
			emptyColTotalsMeasuresIndexes = MondrianReportUtils.getEmptyColTotalsMeasuresIndexes(spec);
		}
		this.rowTotals = cellDataSet.getRowTotalsLists();
		this.isTotalsOnlyReport = GroupingCriteria.GROUPING_TOTALS_ONLY.equals(spec.getGroupingCriteria());
		if (!this.isTotalsOnlyReport) {
			emptyRowTotalsMeasuresIndexes = MondrianReportUtils.getEmptyRowTotalsMeasuresIndexes(spec, leafHeaders);
		}
	}
	
	public ReportAreaImpl transformTo(Class<? extends ReportAreaImpl> reportAreaType) throws AMPException {
		ReportAreaImpl root = MondrianReportUtils.getNewReportArea(reportAreaType);
		boolean isSaikuReport = root instanceof SaikuReportArea;
		boolean isPartialArea = root instanceof PartialReportArea;
		Iterator<Long> idIter =  isPartialArea && cellDataSetActivities != null 
				? cellDataSetActivities.iterator() : null;
		
		Deque<List<ReportArea>> stack = new ArrayDeque<List<ReportArea>>();
		//assumption that concatenation was done and totals are required starting for the 1st non-hierarchical column backwards
		int hSize = spec.getHierarchies().size();
		if (spec.getColumns().size() == spec.getHierarchies().size() && !spec.getHierarchies().isEmpty())
			hSize--;
		
		int maxDepth = spec.getColumns().isEmpty() ? 1 :  
				(spec.getCalculateRowTotals() ? Math.max(1, hSize) - (hSize / spec.getColumns().size()) : 1);

		int maxStackSize = 1 + maxDepth * 2; //* 2 for totals, where maxDepth != 0 
		
		refillStack(stack, maxStackSize); //prepare the stack
		currentSubGroupIndex = new int[maxDepth + 1];
		
		for (int rowId = 0; rowId < cellDataSet.getCellSetBody().length; rowId++) {
			Map<ReportOutputColumn, ReportCell> contents = new LinkedHashMap<ReportOutputColumn, ReportCell>();
			ReportAreaImpl reportArea = MondrianReportUtils.getNewReportArea(reportAreaType);
			
			//stores the current textual column id with not null data
			int notNullColId = addRowData(rowId, contents); 
			int nextNotNullColId = nextNotNull(rowId, maxDepth);
			
			reportArea.setContents(contents);
			//remember the source row id that will be used during sorting
			if (isSaikuReport) {
				((SaikuReportArea)reportArea).setOrigId(rowId);
			}
			if (idIter != null) {
				if (idIter.hasNext()) {
					((PartialReportArea) reportArea).addInternalUseId(idIter.next());
				} else {
					logger.error("Abnormal case: each CellDataSet row must have an associated ID");
				}
			}
			
			boolean areaEnd = isEndOfArea(rowId, notNullColId, nextNotNullColId);
			
			if (areaEnd) {
				//check if this is the end of the entire report, i.e. nextNotNullColId == -1
				boolean reportEndNoHierarchies = nextNotNullColId == -1 && hSize == 0; 
				int depth = reportEndNoHierarchies ? 1 : maxDepth - nextNotNullColId;
				int totColId = reportEndNoHierarchies ? 0 : (hSize == 0 ? -1 : hSize);
				updateGroupData (stack, reportArea, rowId,  totColId, depth, nextNotNullColId == -1 ? 0 : maxStackSize);
			} else { 
				stack.peek().add(reportArea);
			}
		}
		root.setChildren(stack.pop());
		// move up the report area level only if the inner structure is also a group of areas 
		if (root.getChildren().size() == 1 
				&& root.getChildren().get(0).getChildren() != null 
				&& root.getChildren().get(0).getChildren().size() > 0)
			root = (ReportAreaImpl) root.getChildren().get(0);
		else if(root instanceof SaikuReportArea)
			((SaikuReportArea)root).setOrigLeafId(getOrigLeafId((SaikuReportArea)root));
		
		/* if this is a partial report area and no ids are collected, 
		 * means there are no hierarchies => activities count = children count
		 */
		if (isPartialArea && idIter == null) {
			((PartialReportArea) root).setTotalLeafActivitiesCount(root.getChildren().size());
		}
		
		return root;
		//return (ReportAreaImpl) root.getChildren().get(1);
	}
	
	//returns not null textual column id
	private int addRowData(int rowId, Map<ReportOutputColumn, ReportCell> contents) throws AMPException {
		int notNullColId = 0;
		int rowLength = cellDataSet.getCellSetBody()[rowId].length;
		//adds data to the content
		for(int colId = 0; colId < rowLength; colId++) {
			boolean reformat = true;
			AbstractBaseCell setCell = cellDataSet.getCellSetBody()[rowId][colId]; 
			String value = setCell.getFormattedValue();
			ReportOutputColumn roc = leafHeaders.get(colId);
			ReportCell cellData;
			//textual columns
			if (colId < spec.getColumns().size() && !CustomAmounts.ACTIVITY_AMOUNTS.contains(roc.originalColumnName)) { 
				if (DateColumns.ACTIVITY_DATES.contains(roc.originalColumnName)) {
					cellData = DateCell.buildDateFromRepOut(value);
				} else { 
					// do not reformat only text cells (we rely upon formatted value == null) 
					reformat = false;
					cellData = new TextCell(value == null ? "" : value);
				}
				if (value == null) {
					notNullColId ++;
				}
			} else { // measure or amount columns
				Double dVal = parseValue(value, !isTotalsOnlyReport || colId < spec.getColumns().size(), colId);
				cellData = new AmountCell(dVal, this.numberFormat);
			}
			if (reformat) {
				setCell.setFormattedValue(cellData.displayedValue);
			}
			contents.put(roc, cellData);
		}
		
		//adds measure totals to the content
		if (measureTotals != null) {
			int headerColId = rowLength;
			for (int colId = 0; colId < measureTotals.length; colId ++) {
				if (headerColId >= leafHeaders.size())
					break; // AMP-20702: ugly workaround for a Saiku bug which has been previously workarounded "too toughly" via AMP-18748
				//Unfortunately cannot use getValue() because during concatenation we override the value, but the only way to override is via formatted value
				double value = parseValue(measureTotals[colId][rowId].getFormattedValue(), false, headerColId); 
				contents.put(leafHeaders.get(headerColId++), new AmountCell(value, this.numberFormat));
				//also re-format, via MDX formatting works a bit differently
				measureTotals[colId][rowId].setFormattedValue(this.numberFormat.format(value));
			}
		}
		
		return notNullColId;
	}

//	/**
//	 * debug code - do not delete until the whole thing is stabilized
//	 * @return
//	 */
//	public static String writeStats() {
//		int sum = 0;
//		for(int j:counts.values())
//			sum += j;
//		if (sum == 0) sum = 1;
//		
//		TreeSet<ComparableValue<Double>> values = new TreeSet<>();
//		for(String cell:counts.keySet())
//			values.add(new ComparableValue<Double>(cell, counts.get(cell) * 1.0 / sum));
//
//		return String.format("%d values: %s", values.size(), values.toString());
//	}
	
	public static Map<String, Integer> counts = new TreeMap<String, Integer>();
	
	/**
	 * parses the value and multiplies the value by amountMultiplier (if value is part of a column/measure eligible for it)
	 * @param value
	 * @param emptyAsNull
	 * @param colId
	 * @return
	 * @throws AMPException
	 */
	private Double parseValue(String value, boolean emptyAsNull, int colId) throws AMPException {
		if (value == null)
			throw new AMPException("Textual column value sent for parsing - invalid request. Please fix");
		// return null result only for computed columns or for measures that are distributed over the years
		if (emptyAsNull && (value.isEmpty() || value.equalsIgnoreCase(MoConstants.UNDEFINED_AMOUNT_STR))) {
			return null;
		}
		
//		Integer oldCount = counts.get(value);
//		if (oldCount == null) oldCount = 0;
//		counts.put(value, oldCount + 1);
		
		Double dValue = 0d;
		if (!value.equals("0")) {
			try {
				dValue = readingNumberFormat.parse(value).doubleValue();
				if (amountMultiplierColumns.contains(colId)) {
					dValue *= unitsOption.multiplier;
				}
			} catch (ParseException e) {
				//empty string
			} catch (Exception e) {
				//should not get here, only parse exception can happen for empty string. If null -> this is textual column, not amounts
				throw new AMPException("Cannot parse value=" + value + ", error=" + e.getMessage());
			}
		}
		return dValue;
	}
	
	private int nextNotNull(int rowId, int colId) {
		//if this is the end of the report, then not null is out of range, -1
		if (rowId + 1 == cellDataSet.getCellSetBody().length)
			colId = -1;
		else 
			//otherwise detect next row not null column id 
			while (colId > 0 && cellDataSet.getCellSetBody()[rowId + 1][colId-1].getRawValue() != null)
				colId --;
		return colId;
	}
	
	/**
	 * Adds and updates the stack with report area and also stores the totals
	 * @param stack - the stack that stores report area
	 * @param current - the current report area to be added to the stack
	 * @param depth - how depth we should navigate through the stack
	 * @param maxStackSize - the maximum size of the stack
	 * @throws AMPException
	 */
	private void updateGroupData(Deque<List<ReportArea>> stack, ReportAreaImpl current, int rowId, int totColId, 
			int depth, int maxStackSize) throws AMPException {
		//logger.error(String.format("updateGroupData: rowId = %d, totColId = %d, depth = %d, maxStackSize = %d", rowId, totColId, depth, maxStackSize));
		while(depth > 0) {
			stack.peek().add(current);
			depth --;
			if (totColId >=0) {
				current = MondrianReportUtils.getNewReportArea(current.getClass());
				current.setChildren(stack.pop());
				if(current instanceof SaikuReportArea)
					((SaikuReportArea)current).setOrigLeafId(getOrigLeafId((SaikuReportArea)current));
				addCurentRowTotal(current, totColId);
				if (depth == 0)
					stack.peek().add(current);
				totColId --;
			} else if (depth > 0) {
				current = MondrianReportUtils.getNewReportArea(current.getClass());
				current.setChildren(stack.pop());
			}
		}
		refillStack(stack, maxStackSize);
	}
	
	private int getOrigLeafId(SaikuReportArea current) {
		if (current.getChildren() != null && current.getChildren().size() > 0)
			return getOrigLeafId((SaikuReportArea)current.getChildren().get(0));
		return current.getOrigId();
	}
	
	private void addCurentRowTotal(ReportAreaImpl current, int colId) {
		if (rowTotals == null || rowTotals.length == 0) return; //if needed, we can be built manually in this case, but... it's getting more and more like a custom report generation
		Map<ReportOutputColumn, ReportCell> contents = new LinkedHashMap<ReportOutputColumn, ReportCell>(leafHeaders.size());
		
		if (current instanceof SaikuReportArea) {
			((SaikuReportArea)current).setOrigId(currentSubGroupIndex[colId]);
			((SaikuReportArea)current).setTotalRow(true);
		}
		
		//taking row totals for the subgroup identified by colId and this is currentSubGroupIndex[colId] retrieval from this subgroup
		TotalAggregator[][] totals = rowTotals[colId].get(currentSubGroupIndex[colId]).getTotalGroups();
		currentSubGroupIndex[colId] ++;
		int headerPos = 0;
		//adding textual column data
		String totalsName = getTotalsName(current, colId - 1);
		int totColNameId = Math.max(0,  colId - 1);
		for (int a = 0; a < cellDataSet.getLeftOffset(); a++, headerPos++) {
			String value = a == totColNameId ? totalsName : ""; 
			contents.put(leafHeaders.get(headerPos), new TextCell(value));
		}
		//adding data totals of the current area
		for (int a = 0; a < totals.length; a ++) //normally totals.length == 1
			for (int b = 0; b < totals[a].length; b++) {
				Double value = totals[a][b].getValue();
				if (emptyRowTotalsMeasuresIndexes.contains(b)) {
					totals[a][b] = totals[a][b].newInstance("");
					value = null;
				} else if (amountMultiplierColumns.contains(headerPos)) {
					value *= unitsOption.multiplier;
				}
				contents.put(leafHeaders.get(headerPos++), new AmountCell(value, this.numberFormat));
				totals[a][b].setFormattedValue(value == null ? "" : this.numberFormat.format(value));
			}
			
		//calculate total measures of the current area
		if (spec.isCalculateColumnTotals()) {
			double[] currentTotalMeasuresColumnTotals = new double[leafHeaders.size() - headerPos];
			for (ReportArea childArea : current.getChildren()) {
				ReportCell[] childContent = childArea.getContents().values().toArray(new ReportCell[0]);
				for (int a = headerPos; a < leafHeaders.size() && a < childContent.length; a ++) { //  THIS IS A HACK FOR IT NOT TO CRASH. YOU SHOULD UPDATE SaikuUtils.doTotals() instead
					Double value = (Double)((AmountCell)childContent[a]).value;
					currentTotalMeasuresColumnTotals[a - headerPos] += value == null ? 0 : value;
				}
			}
			//adding total measures
			for (int b = 0; b < currentTotalMeasuresColumnTotals.length; b++, headerPos++) {
				Double total = emptyColTotalsMeasuresIndexes.contains(b) ? null : currentTotalMeasuresColumnTotals[b];
				contents.put(leafHeaders.get(headerPos), new AmountCell(total, this.numberFormat));
			}
		}
		
		current.setContents(contents);
	}
	
	//TODO: replace with translatable value and TBD how to name?
	private String getTotalsName(ReportAreaImpl current, int colId) {
		if (colId == -1)
			return "Report Totals";
		if (current.getChildren() != null && current.getChildren().size() > 0)
			return getTotalsName((ReportAreaImpl)current.getChildren().get(0), colId);
		Iterator<ReportCell> iter = current.getContents().values().iterator();
		while(iter.hasNext() && colId > 0) {
			colId --;
			iter.next();
		}
		return iter.hasNext() ? iter.next().displayedValue + " Totals" : "Totals"; 
	}
	
	/**
	 * Verifies if this is an area end (the last row of a group) 
	 * @param rowId - current row index from result data
	 * @param notNullColId - currently detected not empty textual column index 
	 * @return true if this is an end of an area group
	 */
	private boolean isEndOfArea(int rowId, int notNullColId, int nextNotNullColId) {
		//check if this is last row in the entire result set
		if (nextNotNullColId == -1)
			return true;
		//check if next is coming a new hierarchy group of not last column hierarchy
		if (nextNotNullColId < spec.getHierarchies().size() && nextNotNullColId < spec.getColumns().size() - 1)
			return true;
		/* After some non-hierarchical changes, this part seems to be obsolete. 
		 * Commenting out, but not removing until confirmed that is not causing issues.
		 * This fixes the result grouping in the area. 
		//check if this is 1 row group: if this is not the last column  
		if (notNullColId + 1 < cellDataSet.getLeftOffset() && cellDataSet.getCellSetBody()[rowId + 1][notNullColId].getRawValue() != null)
			return true;
		//check if we are at not first level which is the last row of the current group
		if (notNullColId > 0 && cellDataSet.getCellSetBody()[rowId + 1][notNullColId - 1].getRawValue() != null)
			return true;
		*/
		return false;
	}
	
	private void refillStack(Deque<List<ReportArea>> stack, int maxSize) {
		for (int i = stack.size(); i < maxSize; i++) {
			stack.push(new ArrayList<ReportArea>()); 
		}
	}
	
	/**
	 * @return column ids to apply the amounts multiplier
	 */
	private void initColumnIdsToApplyAmountsMultiplier() {
		int headerPos = 0;
		for (ReportOutputColumn roc : leafHeaders) {
			if (!CustomAmounts.UNIT_MULTIPLIER_NOT_APPLICABLE.contains(roc.originalColumnName)) {
				amountMultiplierColumns.add(headerPos);
			}
			headerPos ++;
		}	 
	}
	
}
