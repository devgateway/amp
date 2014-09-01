/**
 * 
 */
package org.digijava.kernel.ampapi.saiku.util;

import java.io.PrintWriter;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

import org.apache.commons.lang.math.IntRange;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.digijava.kernel.ampapi.mondrian.util.MondrianUtils;
import org.olap4j.Axis;
import org.olap4j.CellSet;
import org.olap4j.CellSetAxis;
import org.olap4j.Position;
import org.olap4j.metadata.Measure;
import org.olap4j.metadata.Member;
import org.saiku.olap.dto.resultset.AbstractBaseCell;
import org.saiku.olap.dto.resultset.CellDataSet;
import org.saiku.service.olap.totals.AxisInfo;
import org.saiku.service.olap.totals.TotalNode;
import org.saiku.service.olap.totals.TotalsListsBuilder;
import org.saiku.service.olap.totals.aggregators.TotalAggregator;
import org.saiku.web.rest.objects.resultset.Cell;
import org.saiku.web.rest.objects.resultset.Total;

/**
 * Stores any utility methods that are Saiku specific (e.g. use Saiku API)
 * @author Nadejda Mandrescu
 */
public class SaikuUtils {
	
	/**
	 * Saiku mechanism to calculate the totals
	 * @param spec
	 * @param cellDataSet
	 * @throws Exception 
	 */
	public static void doTotals(ReportSpecification spec, CellDataSet result, CellSet cellSet) throws Exception {
		/* start of AMP custom part to detect the selectedMeasures list */ 
		CellSetAxis columnAxis = cellSet.getAxes().get(Axis.COLUMNS.axisOrdinal());
		List<Measure> measures = new ArrayList<Measure>(); 
		
		for(Position colPosition : columnAxis.getPositions())
			for (Member member :  colPosition.getMembers()) {
				if (Member.Type.MEASURE.equals(member.getMemberType())) {
					measures.add((Measure) member);
				} 
			}
		Measure[] selectedMeasures = measures.toArray(new Measure[0]);
		/* end of AMP custom part to detect the selectedMeasures list */
		/* start of Saiku approach to calculate the totals */
		result.setSelectedMeasures(selectedMeasures);
		int rowsIndex = 0;
		if (!cellSet.getAxes().get(0).getAxisOrdinal().equals(Axis.ROWS)) {
			rowsIndex = (rowsIndex + 1) & 1;
		}
		// TODO - refactor this using axis ordinals etc.
		final AxisInfo[] axisInfos = new AxisInfo[]{new AxisInfo(cellSet.getAxes().get(rowsIndex)), new AxisInfo(cellSet.getAxes().get((rowsIndex + 1) & 1))};
		List<TotalNode>[][] totals = new List[2][];
		TotalsListsBuilder builder = null;
		for (int index = 0; index < 2; index++) {
			final int second = (index + 1) & 1;
			TotalAggregator[] aggregators = new TotalAggregator[axisInfos[second].maxDepth + 1];
			for (int i = 1; i < aggregators.length - 1; i++) {
				String totalFunctionName = "sum";//AMP change to configure the function to 'sum' manually instead of using query.getTotalFunction(axisInfos[second].uniqueLevelNames.get(i - 1));
				aggregators[i] = TotalAggregator.newInstanceByFunctionName(totalFunctionName);
			}
			String totalFunctionName = "sum";//AMP change to configure the function to 'sum' manually instead of usingquery.getTotalFunction(axisInfos[second].axis.getAxisOrdinal().name());
			aggregators[0] = totalFunctionName != null ? TotalAggregator.newInstanceByFunctionName(totalFunctionName) : null;
			builder = new TotalsListsBuilder(selectedMeasures, aggregators, cellSet, axisInfos[index], axisInfos[second]);
			totals[index] = builder.buildTotalsLists();
		}
		result.setLeftOffset(axisInfos[0].maxDepth);
		result.setRowTotalsLists(totals[1]);
		result.setColTotalsLists(totals[0]);
		/* end of Saiku approach to calculate the totals */
	}
	
	/**
	 * Concatenates the results of child non-hierarchical columns
	 * @param spec
	 * @param cellDataSet
	 */
	public static void concatenateNonHierarchicalColumns(ReportSpecification spec, CellDataSet cellDataSet) {
		Total[][] rowTotals = convertTotals(cellDataSet.getRowTotalsLists());
		//no totals => cannot concatenate and replace
		if (rowTotals == null || rowTotals.length == 0) return;
		
		//concatenate data
		AbstractBaseCell[][] data = cellDataSet.getCellSetBody();
		int leftOffset = cellDataSet.getLeftOffset(); //this is actually the number of columns in the cellDataSet

		//the starting index of the column to concatenate all totals 
		int startColumnIndex = spec.getHierarchies() != null && spec.getHierarchies().size() > 0 ? spec.getHierarchies().size() : 1;
		int end = spec.getColumns().size();
		int noOfColumnsToMerge = end - startColumnIndex ;
		//if we have hierarchies only and this is not a print call, then we have nothing to do, because no concatenation is needed
		if (noOfColumnsToMerge == 0 || data.length == 0) return;
		
		//TODO: for debug:
		//final String sep = System.lineSeparator() + String.format("%" + (21 * startColumnIndex -1) + "s\t|", "");
		//TODO: for production:
		final String sep = ", ";
		StringBuilder[] sbList = new StringBuilder[noOfColumnsToMerge];
		for (int i = 0; i < noOfColumnsToMerge; i++)
			sbList[i] = new StringBuilder();
		
		NumberFormat numberFormat = NumberFormat.getNumberInstance();
		
		Total[][] colTotals  = convertTotals(cellDataSet.getColTotalsLists());
		int firstGroupingTotalsLevel = colTotals == null ? 0 : Math.max(colTotals.length - 2, 0);
		int pos = colTotals == null ? 0 : colTotals[firstGroupingTotalsLevel].length - 1;
		int noOfMeasures = colTotals == null ? 0 : colTotals[firstGroupingTotalsLevel][pos].getWidth();
		Integer[] currentTotalMeasuresColumnTotals = new Integer[noOfMeasures];
		Arrays.fill(currentTotalMeasuresColumnTotals, 0);
		ArrayList<Integer[]> measuresTotalsToKeep = new ArrayList<Integer[]>();
		
		//list of row index ranges to delete when all concatenations are done
		List<IntRange> rowsRangesToDelte = new ArrayList<IntRange>();
		
		int currentSubGroupIndex = 0; //the initial counter of the current sub-group index
		int rowsToKeepCount = data.length;
		
		int groupStartRowId = 0;
		
		for (int i = 0; i < data.length; i++) {
			//concatenate non-hierarchical columns data
			for (int j = startColumnIndex; j < end; j++)
				if (data[i][j].getRawValue() != null) 
					sbList[j - startColumnIndex].append(data[i][j].getFormattedValue()).append(sep);
			
			//merge measures totals
			if (colTotals != null && colTotals.length > 0) {
				//get final totals reference
				Total total = colTotals[0][0];
				Cell[][] res = total.getCells();
				int mPos = 0;
				for (int a = total.getWidth() - noOfMeasures; a < total.getWidth(); a++, mPos++) {
					String value = res[a][i].getValue();
					Number iVal = 0;
					try {
						//number format is slower than Integer format x 4 times, but string value is combing formated
						// => we may need to pass special formatter that will not format the output to be able to use format
						//also we should check if we can use a formatter that will not round up the totals to the nearest - nice to have 
						iVal =  numberFormat.parse(value);  
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					currentTotalMeasuresColumnTotals[mPos] += iVal.intValue(); //so far we do totals as Integer 
				}
			}
			
			//check if we reached the end of the sub-group
			if (i + 1 == data.length  //this is the last result row 
					|| data[i+1][startColumnIndex - 1].getRawValue() != null) {//this is the last row of the sub-group to merge
				if (groupStartRowId != i) { //no need to merge if this is 1 row group
				
					Total total = rowTotals[startColumnIndex][currentSubGroupIndex];
					
					rowsRangesToDelte.add(new IntRange(groupStartRowId + 1, i)); //starting index will store the totals and will not be removed
					rowsToKeepCount -= i - groupStartRowId; //deduct the number of rows that will be deleted
					
					//a) update concatenated data
					for (int j = startColumnIndex; j < end; j++) {
						int subStrEnd = sbList[j - startColumnIndex].length() - sep.length();
						data[groupStartRowId][j].setFormattedValue(sbList[j - startColumnIndex].substring(0, subStrEnd));
					}
					
					Cell[][] res = total.getCells();
					//b) update data with totals
					for (int a = leftOffset; a < data[groupStartRowId].length; a++) {
						data[groupStartRowId][a].setRawValue(res[0][a - leftOffset].getValue());
						data[groupStartRowId][a].setFormattedValue(res[0][a - leftOffset].getValue());
					}
				}
				//remember cumulative measures totals && reset the current storage
				if (colTotals != null && colTotals.length > 0) {
					measuresTotalsToKeep.add(currentTotalMeasuresColumnTotals.clone());
					Arrays.fill(currentTotalMeasuresColumnTotals, 0);
				}
				//update indexes
				groupStartRowId = i + 1;
				currentSubGroupIndex ++;
				//reset string builders
				for (int j = startColumnIndex; j < end; j++)
					sbList[j - startColumnIndex].setLength(0);
			}
		}
		
		//update row totals to remove unneeded totals
		@SuppressWarnings("unchecked")
		List<TotalNode>[] newTotalLists = (List<TotalNode>[])new ArrayList[startColumnIndex];
		for (int i = 0; i < startColumnIndex; i++) {
			newTotalLists[i] = cellDataSet.getRowTotalsLists()[i];
		}
		cellDataSet.setRowTotalsLists(newTotalLists);
		
		int start = 0;
		//create new data of rows
		AbstractBaseCell[][] newData = new AbstractBaseCell[rowsToKeepCount][data[0].length];
		int newDataRowId = 0;
		//get measures totals reference
		TotalNode total = cellDataSet.getColTotalsLists()[0].get(0);
		TotalAggregator[][] res = total.getTotalGroups();
		
		for (IntRange range : rowsRangesToDelte) {
			for (int i = start; i < range.getMinimumInteger(); i++, newDataRowId ++ ) {
				newData[newDataRowId] = data[i].clone(); //to mark as free the data reference by
				//update the measures totals
				
				int mPos = 0;
				for (int a = total.getWidth() - noOfMeasures; a < total.getWidth(); a++, mPos++) {
					String value = numberFormat.format(measuresTotalsToKeep.get(newDataRowId)[mPos]);
					res[a][newDataRowId].setFormattedValue(value);
				}		
			}
			start = range.getMaximumInteger() + 1;
		}
		cellDataSet.setCellSetBody(newData);
	}
	
	/**
	 * Prints a cellDataSet to a PrintWriter
	 * @param cellDataSet - the data set to operate over
	 * @param writer - then cellDataSet will be written to this writer
	 */
	private static void print(CellDataSet cellDataSet, PrintWriter writer) {
		Total[][] rowTotals = convertTotals(cellDataSet.getRowTotalsLists());
		Total[][] colTotals  = convertTotals(cellDataSet.getColTotalsLists());
		
		NumberFormat numberFormat = NumberFormat.getNumberInstance();
		
		int firstGroupingTotalsLevel = colTotals == null ? 0 : Math.max(colTotals.length - 2, 0);
		int pos = colTotals == null ? 0 : colTotals[firstGroupingTotalsLevel].length - 1;
		int noOfMeasures = colTotals == null ? 0 : colTotals[firstGroupingTotalsLevel][pos].getWidth();
		
		//stores measures totals for each sub group, keeping only the latest data required for the next processing 
		Deque<List<Integer[]>> manualMeasuresGrandTotal = new ArrayDeque<List<Integer[]>>();
		//the grand totals of <toatal measures> columns for the current sub-group of results (they are not calculated by Saiku, so we need to build them manually)
		Integer[] currentTotalMeasuresColumnTotals = new Integer[noOfMeasures];  
		Arrays.fill(currentTotalMeasuresColumnTotals, 0);
		
		//print or concatenate data
		AbstractBaseCell[][] data = cellDataSet.getCellSetBody();
		int leftOffset = cellDataSet.getLeftOffset(); //this is actually the number of columns in the cellDataSet
		int lineLength = data.length > 0 ? ((data[0].length + noOfMeasures) * 25 + leftOffset * 20) : 0;

		//print headers
		printHeaders (writer, cellDataSet, noOfMeasures, lineLength);
		
		int currentSubGroupIndex = 0;
		int lastTotalsColumnIndex = 0;
		
		for (int i = 0; i < data.length + 1; i++) {
			int curTotColId = 0;
			if ( i < data.length) {
				//iterating through actual values
				for (int j = 0; j < data[i].length; j++ ) {
					String value = data[i][j].getRawValue() == null ? "" : data[i][j].getFormattedValue();
					if (value.length() > 40) value = value.substring(0, 37) + "...";
					writer.format("%" + (j < leftOffset ? "-" + 40 : 20) + "s\t|", value);
					if (j < leftOffset && data[i][j].getRawValue() == null)
						curTotColId ++;
				}
				
				//print total column entries for the current row && calculate total measures values
				if (colTotals != null && colTotals.length > 0) {
					//print only final totals (no subtotals on column) (each measure total)
					Total total = colTotals[0][0];
					Cell[][] res = total.getCells();
					int mPos = 0;
					for (int a = total.getWidth() - noOfMeasures; a < total.getWidth(); a++, mPos++) {
						String value = res[a][i].getValue();
						writer.format("%20s\t|", value);
						Number iVal = 0;
						try {
							//number format is slower than Integer format x 4 times, but string value is combing formated
							// => we may need to pass special formatter that will not format the output to be able to use format
							//also we should check if we can use a formatter that will not round up the totals to the nearest - nice to have 
							iVal =  numberFormat.parse(value);  
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						currentTotalMeasuresColumnTotals[mPos] += iVal.intValue(); //so far we totals as Integer, but we might 
					}
				}
				
				writer.write(System.lineSeparator());
			} else 
				currentSubGroupIndex = 0;
			//print row totals for the latest group
			if (rowTotals != null && rowTotals.length > 0 && 
					(i == data.length || i + 1 == data.length //this is the last line 
					|| curTotColId > 0 && i + 1 < data.length && data[i+1][curTotColId-1].getRawValue() != null)) {//this is the last line of the group columns > 1
				
				Total total = rowTotals[curTotColId][currentSubGroupIndex];
				
				if (total == null || total.getWidth() == 0 && !(i == data.length) ) continue;
				currentSubGroupIndex ++;
				
				//get the latest list of sub-group totals
				if (colTotals != null) {
					List<Integer[]> mGroupList = manualMeasuresGrandTotal.peek();
					if ((curTotColId < lastTotalsColumnIndex || i == data.length) && mGroupList != null) {
						//we moved to the previous column (with lower index) => merge previous sub-totals to compute current level totals
						
						//merging the totals of the <totals measures> columns 
						for (Integer[] mGroup : mGroupList) {
							for (int a = 0; a < noOfMeasures; a++)
								currentTotalMeasuresColumnTotals[a] += mGroup[a];
						}
						manualMeasuresGrandTotal.pop();
						mGroupList  = manualMeasuresGrandTotal.peek(); // get parent group
					}
					if (mGroupList == null) {
						mGroupList = new ArrayList<Integer[]>();
						manualMeasuresGrandTotal.add(mGroupList);
					}
					mGroupList.add(currentTotalMeasuresColumnTotals.clone());
					
					//this is a different totals level, start from the beginning
					if (curTotColId != lastTotalsColumnIndex) {
						lastTotalsColumnIndex = curTotColId;
						currentSubGroupIndex = 0; //reset the subGroup counter
					}
				}
				
				printRowTotals(writer, total, numberFormat, currentTotalMeasuresColumnTotals, lineLength, leftOffset, noOfMeasures);
				
				Arrays.fill(currentTotalMeasuresColumnTotals, 0);
				
			}
		}
	}
	
	/**
	 * Saiku method to convert totals
	 * @param totalLists
	 * @return
	 */
	public static Total[][] convertTotals(List<TotalNode>[] totalLists) {
		if (null == totalLists)
			return null;
		Total[][] retVal = new Total[totalLists.length][];
		for (int i = 0; i < totalLists.length; i++) {
			List<TotalNode> current = totalLists [i];
			retVal[i] = new Total[current.size()];
			for (int j = 0; j < current.size(); j++)
				retVal[i][j] = new Total(current.get(j));
		}
		return retVal;
	}
	
	/**
	 * Prints Saiku cellDataSet to an output
	 * @param cellDataSet
	 * @param reportName
	 */
	public static void print(CellDataSet cellDataSet, String reportName) {
		PrintWriter writer = MondrianUtils.getOutput(reportName);
		print(cellDataSet, writer);
		
		writer.write(System.lineSeparator() + "Runtime = " + cellDataSet.runtime + " ms");
		
		writer.flush();
		writer.close();
	}
	
	
	private static void printHeaders(PrintWriter writer, CellDataSet cellDataSet, int noOfMeasures, int lineLength) {
		if (writer == null) return;
		
		AbstractBaseCell[][] data = cellDataSet.getCellSetHeaders();
		int selectedMeasuresLength = cellDataSet.getSelectedMeasures() == null ? 0 : cellDataSet.getSelectedMeasures().length;
		int topOffset = cellDataSet.getTopOffset();
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[i].length; j++ ) {
				String value = data[i][j].getFormattedValue();
				if (j < cellDataSet.getLeftOffset() && topOffset != 1)
					value = "";
				writer.format("%-" + (j < cellDataSet.getLeftOffset() ? 40 : 20) + "s\t|", value);
			}	
			
			//print column totals header	
			if (topOffset == 1 &&  selectedMeasuresLength > 0) {
				//print measures name
				int mLength = cellDataSet.getSelectedMeasures().length;
				for (int a = mLength - noOfMeasures; a < mLength; a++)
					writer.format("%-20s\t|", cellDataSet.getSelectedMeasures()[a].getName());
			} else
				writer.format("%20s\t", "Totals");
			topOffset --;
			writer.write(System.lineSeparator());
		}
		
		printDashLine(writer, lineLength);
	}
	
	private static void printRowTotals(PrintWriter writer, Total total, NumberFormat numberFormat, Integer[] currentTotalMeasuresColumnTotals, 
			int lineLength, int leftOffset, int noOfMeasures) {
		if (writer == null) return;
		
		printDashLine(writer, lineLength);
		for (int a = 0; a < leftOffset; a++) {
			String value = "Totals";
			writer.format("%" + (a < leftOffset ? 40 : 20) + "s\t|", value);
		}
		
		Cell[][] res = total.getCells();
		for (int a = 0; a < res.length; a ++) {
			for (int b = 0; b < res[a].length; b++) {
				String value = res[a][b].getValue();
				writer.format("%20s\t|", value);
			}
			//print measures grand total of the current group;
			for (int b = 0; b < noOfMeasures && b < currentTotalMeasuresColumnTotals.length; b++) 
				writer.format("%20s\t|", numberFormat.format(currentTotalMeasuresColumnTotals[b]));
			
			writer.write(System.lineSeparator());
		}
		printDashLine(writer, lineLength);
	}
	
	private static void printDashLine(PrintWriter writer, int lineLength) {
		if (writer == null) return;
		for (int j = 0; j < lineLength; j++ ) {
			writer.write("_");
		}
		writer.write(System.lineSeparator());
	}
}
