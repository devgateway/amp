/**
 * 
 */
package org.digijava.kernel.ampapi.saiku.util;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.math.IntRange;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.saiku.olap.dto.resultset.AbstractBaseCell;
import org.saiku.olap.dto.resultset.CellDataSet;
import org.saiku.service.olap.totals.TotalNode;
import org.saiku.service.olap.totals.aggregators.TotalAggregator;

/**
 * Merges non-hierarchical columns of a Saiku CellDataSet structure, based on ReportSpecification configuration. 
 * This is AMP report specific feature, that is slow to be built via MDX queries.
 * @author Nadejda Mandrescu
 */
public class CellDataSetToAmpHierachies {
	private ReportSpecification spec;
	private CellDataSet cellDataSet;

	//TODO: for debug:
	//final String sep = System.lineSeparator() + String.format("%" + (21 * startColumnIndex -1) + "s\t|", "");
	//TODO: for production:
	private final String sep = ", ";
	private final NumberFormat numberFormat = NumberFormat.getNumberInstance();

	private List<TotalNode>[] rowTotals;
	private TotalAggregator[][] colTotals  = null;
	private int startColumnIndex;
	private int noOfColumnsToMerge;
	private StringBuilder[] sbList;
	
	private CellDataSetToAmpHierachies(ReportSpecification spec, CellDataSet cellDataSet) {
		this.spec = spec;
		this.cellDataSet = cellDataSet;
	}

	/**
	 * Concatenates the non-hierarchical columns and updates the data of the cellDataSet
	 * @param spec - report specification that provides hierarchies information
	 * @param cellDataSet - the data set to update
	 */
	public static void concatenateNonHierarchicalColumns(ReportSpecification spec, CellDataSet cellDataSet) {
		(new CellDataSetToAmpHierachies(spec, cellDataSet)).concatenate();
	}
	
	private void init() {
		rowTotals = cellDataSet.getRowTotalsLists();
		//the starting index of the column to concatenate all totals 
		startColumnIndex = spec.getHierarchies() != null && spec.getHierarchies().size() > 0 ? spec.getHierarchies().size() : 1;
		noOfColumnsToMerge = cellDataSet.getLeftOffset() - startColumnIndex;
		
		//list of merged column entries for the current group
		sbList = new StringBuilder[noOfColumnsToMerge];
		for (int i = 0; i < noOfColumnsToMerge; i++)
			sbList[i] = new StringBuilder();
		
		//init measure column totals
		if (cellDataSet.getColTotalsLists() != null && cellDataSet.getColTotalsLists().length > 0
				&& cellDataSet.getColTotalsLists()[0] != null && cellDataSet.getColTotalsLists()[0].size() > 0)
			colTotals = cellDataSet.getColTotalsLists()[0].get(0).getTotalGroups();
	}
	
	private void concatenate() {
		init();
		//no totals or no data or all hierarchies => nothing to concatenate and replace
		if (rowTotals == null || rowTotals.length == 0 || cellDataSet.getCellSetBody().length == 0 || noOfColumnsToMerge == 0) 
			return;
		
		Double[] currentTotalMeasuresColumnTotals = new Double[spec.getMeasures().size()];
		Arrays.fill(currentTotalMeasuresColumnTotals, 0d);
		ArrayList<Double[]> measuresTotalsToKeep = new ArrayList<Double[]>();
		
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
			if (isEndOfGroup(rowId)){
				if (groupStartRowId != rowId) { //no need to merge if this is 1 row group
				
					rowsRangesToDelte.add(new IntRange(groupStartRowId + 1, rowId)); //starting index will store the totals and will not be removed
					rowsToKeepCount -= rowId - groupStartRowId; //deduct the number of rows that will be deleted
					
					//a) update concatenated data
					for (int j = startColumnIndex; j < cellDataSet.getLeftOffset(); j++) {
						int subStrEnd = sbList[j - startColumnIndex].length() - sep.length();
						cellDataSet.getCellSetBody()[groupStartRowId][j].setFormattedValue(sbList[j - startColumnIndex].substring(0, subStrEnd));
					}
					
					TotalAggregator[][] totals = rowTotals[startColumnIndex].get(currentSubGroupIndex).getTotalGroups();
					//b) update data with totals
					for (int a = cellDataSet.getLeftOffset(); a < cellDataSet.getCellSetBody()[groupStartRowId].length; a++) {
						String newVal = numberFormat.format(totals[0][a - cellDataSet.getLeftOffset()].getValue());
						cellDataSet.getCellSetBody()[groupStartRowId][a].setRawValue(String.valueOf(totals[0][a - cellDataSet.getLeftOffset()].getValue()));
						cellDataSet.getCellSetBody()[groupStartRowId][a].setFormattedValue(newVal);
					}
				}
				//remember cumulative measures totals && reset the current storage
				if (colTotals != null && colTotals.length > 0) {
					measuresTotalsToKeep.add(currentTotalMeasuresColumnTotals.clone());
					Arrays.fill(currentTotalMeasuresColumnTotals, 0d);
				}
				//update indexes
				groupStartRowId = rowId + 1;
				currentSubGroupIndex ++;
				//reset string builders
				for (int j = startColumnIndex; j < cellDataSet.getLeftOffset(); j++)
					sbList[j - startColumnIndex].setLength(0);
			}
		}
		
		setNewData(rowsToKeepCount, rowsRangesToDelte, measuresTotalsToKeep);
	}
	
	private void mergeNonHierarchicalText(int rowId) {
		for (int colId = startColumnIndex; colId < cellDataSet.getLeftOffset(); colId++)
			if (cellDataSet.getCellSetBody()[rowId][colId].getRawValue() != null) 
				sbList[colId - startColumnIndex].append(cellDataSet.getCellSetBody()[rowId][colId].getFormattedValue()).append(sep);
	}
	
	private void mergeMeasureTotals(Double[] currentTotalMeasuresColumnTotals, int rowId) {
		if (colTotals != null && colTotals.length > 0) {
			//get final totals reference
			int mPos = 0;
			for (int a = colTotals.length - spec.getMeasures().size(); a < colTotals.length; a++, mPos++) {
				Double value = colTotals[a][rowId].getValue();
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
	
	private void setNewData(int rowsToKeepCount, List<IntRange> rowsRangesToDelte, ArrayList<Double[]> measuresTotalsToKeep) {
		//update row totals to remove unneeded totals
		@SuppressWarnings("unchecked")
		List<TotalNode>[] newTotalLists = (List<TotalNode>[])new ArrayList[startColumnIndex + 1];
		for (int i = 0; i < startColumnIndex + 1; i++) {
			newTotalLists[i] = cellDataSet.getRowTotalsLists()[i];
		}
		cellDataSet.setRowTotalsLists(newTotalLists);
		
		int start = 0;
		//create new data of rows
		AbstractBaseCell[][] newData = new AbstractBaseCell[rowsToKeepCount][cellDataSet.getCellSetBody()[0].length];
		int newDataRowId = 0;
		//get measures totals reference
		TotalNode total = cellDataSet.getColTotalsLists()[0].get(0);
		TotalAggregator[][] res = total.getTotalGroups();
		
		for (IntRange range : rowsRangesToDelte) {
			for (int i = start; i < range.getMinimumInteger(); i++, newDataRowId ++ ) {
				newData[newDataRowId] = cellDataSet.getCellSetBody()[i].clone(); //to mark as free the data reference by
				//update the measures totals
				
				int mPos = 0;
				for (int a = total.getWidth() - spec.getMeasures().size(); a < total.getWidth(); a++, mPos++) {
					String value = numberFormat.format(measuresTotalsToKeep.get(newDataRowId)[mPos]);
					res[a][newDataRowId].setFormattedValue(value);
				}		
			}
			start = range.getMaximumInteger() + 1;
		}
		cellDataSet.setCellSetBody(newData);
	}
}
