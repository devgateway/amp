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

import org.digijava.kernel.ampapi.mondrian.util.MondrianUtils;
import org.saiku.olap.dto.resultset.AbstractBaseCell;
import org.saiku.olap.dto.resultset.CellDataSet;
import org.saiku.olap.dto.resultset.MemberCell;
import org.saiku.service.olap.totals.aggregators.TotalAggregator;
import org.saiku.web.rest.objects.resultset.Cell;
import org.saiku.web.rest.objects.resultset.Total;

/**
 * Printing utility class for Saiku CellDataSet structure (for debug)
 * @author Nadejda Mandrescu
 */
public class SaikuPrintUtils {
	private CellDataSet cellDataSet;
	private PrintWriter writer;
	private Total[][] rowTotals;
	private TotalAggregator[][] colTotals;
	private NumberFormat numberFormat;
	private int noOfMeasures;
	private int leftOffset;
	private int lineLength;
	
	//stores measures totals for each sub group, keeping only the latest data required for the next processing 
	private Deque<List<Double[]>> manualMeasuresGrandTotal = new ArrayDeque<List<Double[]>>();
	//the grand totals of <toatal measures> columns for the current sub-group of results (they are not calculated by Saiku, so we need to build them manually)
	private Double[] currentTotalMeasuresColumnTotals;
	private int[] currentSubGroupIndex;
	
	public SaikuPrintUtils(CellDataSet cellDataSet, PrintWriter writer) {
		this.cellDataSet = cellDataSet;
		this.writer = writer;
	}
	
	/**
	 * Prints a cellDataSet to a PrintWriter
	 * @param cellDataSet - the data set to operate over
	 * @param writer - then cellDataSet will be written to this writer
	 */
	public void print() {
		init();
		
		AbstractBaseCell[][] data = cellDataSet.getCellSetBody();
		
		//print headers
		printHeaders ();
		
		refillStack(manualMeasuresGrandTotal, leftOffset);
		
		for (int i = 0; i < data.length; i++) {
			int curTotColId = 0;
			int nextTotColId = i + 1 == data.length ? -1 : 0;
			
			//iterating through actual values
			for (int j = 0; j < data[i].length; j++ ) {
				String value = data[i][j].getFormattedValue();
				if (value == null) value = "";
				if (value.length() > 40) value = value.substring(0, 37) + "...";
				writer.format("%" + (j < leftOffset ? "-" + 40 : 20) + "s\t|", value);
				if (j < leftOffset) {
					if (data[i][j].getRawValue() == null)
						curTotColId++;
					if (i + 1 < data.length && data[i + 1][j].getRawValue() == null)
						nextTotColId++;
				}	
			}
			
			//print total column entries for the current row && calculate total measures values
			printMeasuresTotals(i);
			writer.write(System.lineSeparator());
			 
			//print row totals for the latest group
			if (rowTotals != null && rowTotals.length > 0 &&  
					( i + 1 == data.length //this is the last row 
					|| curTotColId < leftOffset && data[i + 1][curTotColId].getRawValue() != null //this is 1 row entry for some non-final column
					|| curTotColId == 0 && nextTotColId < leftOffset // this is firt row of a larger grup, but 1 row group as well
					|| curTotColId > 0 && data[i+1][curTotColId-1].getRawValue() != null)) { //this is the last row of the rightmost subgroup

				//print all totals till the next total column level
				processForPrintRowTotals(nextTotColId);
			}
		}
	}
	
	private void init() {
		this.rowTotals = SaikuUtils.convertTotals(cellDataSet.getRowTotalsLists());
		this.colTotals  = null;
		if (cellDataSet.getColTotalsLists() != null && cellDataSet.getColTotalsLists().length > 0
				&& cellDataSet.getColTotalsLists()[0] != null && cellDataSet.getColTotalsLists()[0].size() > 0) {
			this.colTotals = cellDataSet.getColTotalsLists()[0].get(0).getTotalGroups();
		}
		this.numberFormat = NumberFormat.getNumberInstance();
		
		this.noOfMeasures = 0;
		if (colTotals != null ) {
			int firstGroupingTotalsLevel = Math.max(cellDataSet.getColTotalsLists().length - 2, 0);
			int pos = cellDataSet.getColTotalsLists()[firstGroupingTotalsLevel].size() - 1;
			noOfMeasures = cellDataSet.getColTotalsLists()[firstGroupingTotalsLevel].get(pos).getWidth();
		}
		
		this.currentTotalMeasuresColumnTotals = new Double[noOfMeasures];
		Arrays.fill(currentTotalMeasuresColumnTotals, 0d);
		
		this.leftOffset = cellDataSet.getLeftOffset(); //this is actually the number of columns in the cellDataSet
		this.lineLength = cellDataSet.getCellSetBody().length > 0 ? ((cellDataSet.getCellSetBody()[0].length + noOfMeasures) * 25 + leftOffset * 20) : 0;
		
		this.currentSubGroupIndex = new int[leftOffset];
	}
	
	private void printHeaders() {
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
	
	private void printMeasuresTotals(int rowId) {
		if (colTotals == null || colTotals.length == 0)  return;
		
		//print only final totals (no subtotals on column) (each measure total)
		int mPos = 0;
		int max = cellDataSet.getColTotalsLists()[0].get(0).getWidth();
		for (int a = max - noOfMeasures; a < max; a++, mPos++) {
			Number num = 0;
			try {
				//cannot use getValue, because during concatenation we cannot update the value, only formatted value
				num = numberFormat.parse(colTotals[a][rowId].getFormattedValue()); 
			} catch(ParseException e) {
				//
			}
			writer.format("%20s\t|", colTotals[a][rowId].getFormattedValue());
			currentTotalMeasuresColumnTotals[mPos] += num.doubleValue();  
		}
	}
	
	private void processForPrintRowTotals(int curTotColId) {
		//when no concatenation, then leftOffset -1 is the default, otherwise, when concatenation was done, then the max size of rowTotals
		int start = Math.min(leftOffset, rowTotals.length) - 1;
		for (int totId = start; totId > curTotColId; totId --) {
			Total total = rowTotals[totId][currentSubGroupIndex[totId]];
			currentSubGroupIndex[totId] ++;
			
			//get the latest list of sub-group totals
			if (colTotals != null) {
				List<Double[]> mGroupList = manualMeasuresGrandTotal.peek();
				if ((totId < leftOffset - 1) && mGroupList != null) {
					//we moved to the previous column (with lower index) => merge previous sub-totals to compute current level totals
					
					//merging the totals of the <totals measures> columns 
					for (Double[] mGroup : mGroupList) {
						for (int a = 0; a < noOfMeasures; a++)
							currentTotalMeasuresColumnTotals[a] += mGroup[a];
					}
					manualMeasuresGrandTotal.pop();
					mGroupList  = manualMeasuresGrandTotal.peek(); // get parent group
				}
				mGroupList.add(currentTotalMeasuresColumnTotals.clone());
			}
			
			printRowTotals(total, totId);
			Arrays.fill(currentTotalMeasuresColumnTotals, 0d);
		}
		
		refillStack(manualMeasuresGrandTotal, leftOffset);
	}
	
	private void printRowTotals(Total total,int totColId) {
		if (writer == null) return;
		
		printDashLine(writer, lineLength);
		for (int a = 0; a < leftOffset; a++) {
			String value = a == totColId ? "Totals" : "";
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

	private void refillStack(Deque<List<Double[]>> stack, int size) {
		for (int i = stack.size() - 1; i < size; i ++)
			stack.push(new ArrayList<Double[]>());
	}
	
	private void printDashLine(PrintWriter writer, int lineLength) {
		if (writer == null) return;
		for (int j = 0; j < lineLength; j++ ) {
			writer.write("-");
		}
		writer.write(System.lineSeparator());
	}
	
	/**************************************
	 **************************************/
	/**
	 * Prints Saiku cellDataSet to an output
	 * @param cellDataSet
	 * @param reportName
	 */
	public static void print(CellDataSet cellDataSet, String reportName) {
		PrintWriter writer = MondrianUtils.getOutput(reportName);
		writer.write("printing report " + reportName + " of size (" + cellDataSet.getWidth() + "x" + cellDataSet.getHeight() + ")");
		writer.println("\n");
		try {
			(new SaikuPrintUtils(cellDataSet, writer)).print();
		} finally {
			writer.write(System.lineSeparator() + "Runtime = " + cellDataSet.runtime + " ms");
			
			writer.flush();
			writer.close();
		}
	}
}
