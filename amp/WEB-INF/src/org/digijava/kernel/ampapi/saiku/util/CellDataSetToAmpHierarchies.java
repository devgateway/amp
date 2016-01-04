/**
 * 
 */
package org.digijava.kernel.ampapi.saiku.util;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang.math.IntRange;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportElement;
import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.newreports.SortingInfo;
import org.dgfoundation.amp.reports.mondrian.MondrianReportSpec;
import org.dgfoundation.amp.reports.mondrian.MondrianReportUtils;
import org.saiku.olap.dto.resultset.AbstractBaseCell;
import org.saiku.olap.dto.resultset.CellDataSet;
import org.saiku.service.olap.totals.TotalNode;
import org.saiku.service.olap.totals.aggregators.TotalAggregator;

/**
 * Merges non-hierarchical columns of a Saiku CellDataSet structure, based on ReportSpecification configuration. 
 * This is AMP report specific feature, that is slow to be built via MDX queries.
 * @author Nadejda Mandrescu
 */
public class CellDataSetToAmpHierarchies {
	private final MondrianReportSpec spec;
	private final CellDataSet cellDataSet;

	private final NumberFormat numberFormat = NumberFormat.getNumberInstance();

	private List<TotalNode>[] rowTotals;
	private TotalAggregator[][] colTotals  = null;
	private int startColumnIndex;
	private int noOfColumnsToMerge;
	private SortedSet<String>[] sbList;
	private final List<ReportOutputColumn> leafHeaders;
	private final List<Integer> activities;
	private final String translatedUndefined;
	
	private CellDataSetToAmpHierarchies(MondrianReportSpec spec, CellDataSet cellDataSet, 
			List<ReportOutputColumn> leafHeaders, String translatedUndefined, List<Integer> activities) {
		this.spec = spec;
		this.cellDataSet = cellDataSet;
		this.leafHeaders = leafHeaders;
		this.translatedUndefined = translatedUndefined;
		this.activities = activities;
	}

	/**
	 * Concatenates the non-hierarchical columns and updates the data of the cellDataSet
	 * @param spec - report specification that provides hierarchies information
	 * @param cellDataSet - the data set to update
	 * @param leafHeaders - list of leaf headers 
	 * @param activities - list of internal ids (those that are merged) 
	 */
	public static void concatenateNonHierarchicalColumns(MondrianReportSpec spec, CellDataSet cellDataSet, 
			List<ReportOutputColumn> leafHeaders, String translatedUndefined, List<Integer> activities) {
		(new CellDataSetToAmpHierarchies(spec, cellDataSet, leafHeaders, translatedUndefined, activities)).concatenate();
	}
	
	private void init() {
		rowTotals = cellDataSet.getRowTotalsLists();
		//the starting index of the column to concatenate all totals
		startColumnIndex = spec.getHierarchies().size();
		noOfColumnsToMerge = spec.getColumns().size() - startColumnIndex;
		if (noOfColumnsToMerge <= 0) return;
		
		//list of merged column entries for the current group
		initSortedSetsList();
		
		//init measure column totals
		if (spec.isCalculateColumnTotals() && 
				cellDataSet.getColTotalsLists() != null && cellDataSet.getColTotalsLists().length > 0
				&& cellDataSet.getColTotalsLists()[0] != null && cellDataSet.getColTotalsLists()[0].size() > 0)
			colTotals = cellDataSet.getColTotalsLists()[0].get(0).getTotalGroups();
		else
			cellDataSet.setColTotalsLists(null);
	}
	
	private void initSortedSetsList() {
		sbList = (SortedSet<String>[]) new TreeSet<?>[noOfColumnsToMerge];
		Boolean[] sortOrder = new Boolean[noOfColumnsToMerge];
		if (spec.getSorters() != null)
			for(SortingInfo sInfo : spec.getSorters())
				//a non-hierarchical sorting has 1 entry in the sorting tuple 
				if (sInfo.sortByTuple.entrySet().size() == 1) {
					ReportElement elem = sInfo.sortByTuple.entrySet().iterator().next().getKey();
					//and is a report column, not a measure (i.e. total measure)
					if (elem.entity != null && ReportColumn.class.isAssignableFrom(elem.entity.getClass())) {
						int colId = MondrianReportUtils.getColumnId((ReportColumn)elem.entity, spec);
						if(colId >= startColumnIndex) 
							sortOrder[colId - startColumnIndex] = sInfo.ascending; 
					}
				}
		for (int i = 0; i < noOfColumnsToMerge; i++) {
			final boolean asc = sortOrder[i] == null ? true : sortOrder[i]; 
			sbList[i] = new TreeSet<String>(new Comparator<String>() {
				@Override
				public int compare(String o1, String o2) {
					return o1.compareTo(o2) * (asc ? 1 : -1);
				}
			});
		}
	}
	
	private void concatenate() {
		init();
		//no totals or no data or all hierarchies => nothing to concatenate and replace
		if (rowTotals == null || rowTotals.length == 0 || cellDataSet.getCellSetBody().length == 0 || noOfColumnsToMerge <= 0) 
			return;
		
		double[] currentTotalMeasuresColumnTotals = (colTotals == null ? null : new double[colTotals.length]);
		ArrayList<double[]> measuresTotalsToKeep = new ArrayList<double[]>();
		
		//list of row index ranges to delete when all concatenations are done
		List<IntRange> rowsRangesToDelte = new ArrayList<IntRange>();
		
		int currentSubGroupIndex = 0; //the initial counter of the current sub-group index
		int rowsToKeepCount = cellDataSet.getCellSetBody().length;
		//remember current group start rowId
		int groupStartRowId = 0;
		
		for (int rowId = 0; rowId < cellDataSet.getCellSetBody().length; rowId++) {
			//concatenate non-hierarchical columns data
			mergeNonHierarchicalText(rowId);
			//merge measures totals
			mergeMeasureTotals(currentTotalMeasuresColumnTotals, rowId);
			
			//check if we reached the end of the sub-group
			if (isEndOfGroup(rowId)) {
				if (groupStartRowId != rowId) { //no need to merge if this is 1 row group
				
					rowsRangesToDelte.add(new IntRange(groupStartRowId + 1, rowId)); //starting index will store the totals and will not be removed
					rowsToKeepCount -= rowId - groupStartRowId; //deduct the number of rows that will be deleted
					
					//a) update concatenated data
					for (int j = startColumnIndex; j < cellDataSet.getLeftOffset(); j++) {
						String mergedData = sbList[j - startColumnIndex].toString();
						cellDataSet.getCellSetBody()[groupStartRowId][j].setFormattedValue(mergedData.substring(1, mergedData.length() - 1));
					}
					
					TotalAggregator[][] totals = rowTotals[startColumnIndex].get(currentSubGroupIndex).getTotalGroups();
					//b) update data with totals
					for (int a = cellDataSet.getLeftOffset(); a < cellDataSet.getCellSetBody()[groupStartRowId].length; a++) {
						Double totalAmount = getValidAmount(totals[0][a - cellDataSet.getLeftOffset()].getValue(), a, groupStartRowId, rowId);
						String newVal = totalAmount == null ? "" : numberFormat.format(totalAmount);
						cellDataSet.getCellSetBody()[groupStartRowId][a].setRawValue(String.valueOf(totalAmount));
						cellDataSet.getCellSetBody()[groupStartRowId][a].setFormattedValue(newVal);
					}
				}
				//remember cumulative measures totals && reset the current storage
				if (colTotals != null && colTotals.length > 0) {
					measuresTotalsToKeep.add(currentTotalMeasuresColumnTotals.clone());
					Arrays.fill(currentTotalMeasuresColumnTotals, 0d);
				}
				// remember the internal id
				if (activities != null) {
					activities.add(Integer.valueOf(cellDataSet.getCellSetBody()[groupStartRowId][startColumnIndex - 1].getFormattedValue()));
				}
				//update indexes
				groupStartRowId = rowId + 1;
				currentSubGroupIndex ++;
				//reset string builders
				for (int j = startColumnIndex; j < cellDataSet.getLeftOffset(); j++) {
					sbList[j - startColumnIndex].clear();
				}
			}
		}
		
		setNewData(rowsToKeepCount, rowsRangesToDelte, measuresTotalsToKeep);
	}
	
	private void mergeNonHierarchicalText(int rowId) {
		for (int colId = startColumnIndex; colId < cellDataSet.getLeftOffset(); colId++)
			if (cellDataSet.getCellSetBody()[rowId][colId].getRawValue() != null) {
				String valueToConcat = cellDataSet.getCellSetBody()[rowId][colId].getFormattedValue();
				if (!(valueToConcat.equals("#null") || valueToConcat.equals(translatedUndefined) || valueToConcat.equals("")))
					sbList[colId - startColumnIndex].add(valueToConcat);
			}
	}
	
	private void mergeMeasureTotals(double[] currentTotalMeasuresColumnTotals, int rowId) {
		if (colTotals != null && colTotals.length > 0) {
			//get final totals reference
			int mPos = 0;
			for (int a = 0; a < colTotals.length; a++, mPos++) {
				double value = colTotals[a][rowId].getValue();
				//Double value = a < 0 ? 0 : colTotals[a][rowId].getValue();
				currentTotalMeasuresColumnTotals[mPos] += value;
			}
		}
	}
	
	//verifies if this is the end of a group
	private boolean isEndOfGroup(int rowId) {
		if(rowId + 1 == cellDataSet.getCellSetBody().length  //this is the last result row 
				|| cellDataSet.getCellSetBody()[rowId + 1][startColumnIndex - 1].getRawValue() != null) //this is the last row of the sub-group to merge
			return true;
		return false;
	}
	
	private void setNewData(int rowsToKeepCount, List<IntRange> rowsRangesToDelte, ArrayList<double[]> measuresTotalsToKeep) {
		//remove the dummy hierarchy from headers
		SortedSet<Integer> columnsToRemove = new TreeSet<Integer>();
		columnsToRemove.add(spec.getHierarchies().size() - 1); //to remove the last dummy hierarchy
		cellDataSet.setCellSetHeaders(SaikuUtils.removeColumns(cellDataSet.getCellSetHeaders(), columnsToRemove));
		
		//update row totals to remove unneeded totals
		{
			@SuppressWarnings("unchecked")
			List<TotalNode>[] newTotalLists = (List<TotalNode>[])new ArrayList[startColumnIndex + 1];
			for (int i = 0; i < startColumnIndex + 1; i++) {
				newTotalLists[i] = cellDataSet.getRowTotalsLists()[i];
			}
			cellDataSet.setRowTotalsLists(newTotalLists);
			SaikuUtils.recalculateWidths(newTotalLists);
		}
		
		int start = 0;
		//create new data of rows
		AbstractBaseCell[][] newData = new AbstractBaseCell[rowsToKeepCount][cellDataSet.getCellSetBody()[0].length - 1]; // -1 because the size will be reduced by dummy hierarchy
		int newDataRowId = 0;
		//get measures totals reference
		TotalNode total = spec.isCalculateColumnTotals() ? cellDataSet.getColTotalsLists()[0].get(0) : null;
		TotalAggregator[][] res = total == null ? null : total.getTotalGroups();
		
		//add dummy range to add since last range till the end of the data 
		rowsRangesToDelte.add(new IntRange(cellDataSet.getCellSetBody().length, cellDataSet.getCellSetBody().length));
		
		for (IntRange range : rowsRangesToDelte) {
			for (int i = start; i < range.getMinimumInteger(); i++, newDataRowId ++ ) {
				//remove dummy hierarchy
				newData[newDataRowId] = SaikuUtils.removeColumnsInArray(cellDataSet.getCellSetBody()[i], columnsToRemove);
				
				//update the measures totals
				if (total != null) {
					int mPos = 0;
					for (int a = 0; a < measuresTotalsToKeep.get(newDataRowId).length; a++, mPos++) {
						String value = numberFormat.format(measuresTotalsToKeep.get(newDataRowId)[mPos]);
						res[a][newDataRowId].setFormattedValue(value);
					}
				}
			}
			start = range.getMaximumInteger() + 1;
		}
		cellDataSet.setCellSetBody(newData);
	}
	
	/** 
	 * For any 0 total amount we need to know the right formatting:
	 * a) 0 if there are amounts that compensate each other (positive & negative) 
	 * b) EMPTY if all group entries are empty
	 * @param totalAmount
	 * @param colId
	 * @param startRowId
	 * @param endRowId
	 * @return
	 */
	protected Double getValidAmount(Double totalAmount, int colId, int startRowId, int endRowId) {
		if (totalAmount == 0) {
			boolean invalid0Amount = true;
			for (int rowId = startRowId; rowId <= endRowId; rowId ++) {
				String value = cellDataSet.getCellSetBody()[rowId][colId].getFormattedValue();
				if (value != null && !value.isEmpty()) {
					invalid0Amount = false;
					break;
				}
			}
			if (invalid0Amount) {
				totalAmount = null;
			}
		}
		return totalAmount;
	}
	
}
