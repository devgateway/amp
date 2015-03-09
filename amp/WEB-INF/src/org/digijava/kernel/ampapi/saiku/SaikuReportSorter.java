/**
 * 
 */
package org.digijava.kernel.ampapi.saiku;

import java.util.ArrayList;
import java.util.List;

import org.dgfoundation.amp.error.AMPException;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.ReportArea;
import org.dgfoundation.amp.newreports.ReportEnvironment;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.reports.mondrian.MondrianReportSorter;
import org.saiku.olap.dto.resultset.AbstractBaseCell;
import org.saiku.olap.dto.resultset.CellDataSet;
import org.saiku.service.olap.totals.TotalNode;
import org.saiku.service.olap.totals.aggregators.TotalAggregator;

/**
 * Sorting of Saiku CellDataSet. <br> <br>
 * 
 * We can do sorting in MDX and the functionality to sort via MDX is available. 
 * However we had to move non-hierarchical reports merge and totals calculation 
 * to post-processing phase and thus sorting must be done after these actions.
 * 
 * @author Nadejda Mandrescu
 *
 */
public class SaikuReportSorter extends MondrianReportSorter {
	protected CellDataSet cellDataSet;
	private int measuresCount;
	
	private SaikuReportSorter(SaikuGeneratedReport report, ReportEnvironment environment) {
		super(report, environment);
		this.cellDataSet = report.cellDataSet;
		this.measuresCount = spec.getMeasures() == null ? 0 : spec.getMeasures().size();
	}
	
	/**
	 * Sorts {@link SaikuReportSorter} based on sorting information from {@link ReportSpecification}.
	 * @see MondrianReportSorter
	 * @param report - {@link SaikuReportSorter} to sort
	 * @return sorting duration or -1 if no sorting was performed
	 * @throws AMPException 
	 */
	public static int sort(GeneratedReport report, ReportEnvironment environment) throws AMPException {
		return (new SaikuReportSorter((SaikuGeneratedReport) report, environment)).sort();
	}
	
	@Override
	protected int sort() throws AMPException {
		long startTime = System.currentTimeMillis();
		
		//if no sorting was performed, then we have nothing to update on CellDataSet 
		if (super.sort() == -1)
			return -1;
		
		updateCellDataSet();
		
		return (int)(System.currentTimeMillis() - startTime);
	}
	
	/**
	 * Updates the CellDataSet structure 
	 */
	private void updateCellDataSet() {
		if (cellDataSet.getCellSetBody() == null || cellDataSet.getCellSetBody().length == 0) return;
		
		//will keep the reordered actual data
		AbstractBaseCell[][] newData = new AbstractBaseCell[cellDataSet.getCellSetBody().length][cellDataSet.getCellSetBody()[0].length];
		//will keep the reordered list of row totals
		List<TotalNode>[] newRowTotalsList = (List<TotalNode>[]) new ArrayList[cellDataSet.getRowTotalsLists().length];
		int[] rowTotalsIds = new int[1 + spec.getHierarchies().size()];
		initTotals(newRowTotalsList);
		//will keep the reordered list of column totals
		TotalAggregator[][] oldColTotals = cellDataSet.getColTotalsLists()[0].get(0).getTotalGroups();
		TotalAggregator[][] newColTotals = new TotalAggregator[oldColTotals.length][oldColTotals[0].length]; 
		
		updateRecursively((SaikuReportArea) rootArea, 0, 0, newData, newRowTotalsList, rowTotalsIds, oldColTotals, newColTotals);
		
		cellDataSet.setCellSetBody(newData);
		cellDataSet.setRowTotalsLists(newRowTotalsList);
		updateColTotals(oldColTotals, newColTotals);
	}
	
	//returns next row index 
	private int updateRecursively(SaikuReportArea current, int rowId, int depth,  
			AbstractBaseCell[][] newData, List<TotalNode>[] newRowTotalsList, int[] rowTotalsIds,
			TotalAggregator[][] oldColTotals, TotalAggregator[][] newColTotals) {
		//it is either a total row, either a parent with children (and totals), either leaf with actual data 
		if(current.isTotalRow()) {
			newRowTotalsList[depth].add(cellDataSet.getRowTotalsLists()[depth].get(current.getOrigId()));
			current.setOrigId(rowTotalsIds[depth]); //update origId
			rowTotalsIds[depth] ++;
		} else if (current.getContents() != null  && current.getContents().size() > 0) {
			//this is leaf with actual data
			newData[rowId] = cellDataSet.getCellSetBody()[current.getOrigId()];
			//update the measure totals as well
			for (int measureId = 0; measureId < measuresCount; measureId ++)
				newColTotals[measureId][rowId] = oldColTotals[measureId][current.getOrigId()];
			current.setOrigId(rowId);//update origId
			rowId ++;
		}	
		if (current.getChildren() != null && current.getChildren().size() > 0) {
			int currFirstChildRowId = rowId;
			for(ReportArea child : current.getChildren()) {
				rowId = updateRecursively((SaikuReportArea)child, rowId, depth + 1, 
						newData, newRowTotalsList, rowTotalsIds, 
						oldColTotals, newColTotals);
			}
			//swap prefixes
			int prevFirstChildRowId = current.getOrigLeafId();
			swapFirstChildPrefix(currFirstChildRowId, prevFirstChildRowId, depth, newData);
			current.setOrigLeafId(currFirstChildRowId);
		}
		return rowId;
	}
	
	private void swapFirstChildPrefix(int currFirstChildRowId, int prevFirstChildRowId, int depth, AbstractBaseCell[][] newData) {
		//update only if sorting changed the 1st child position in the list
		if (prevFirstChildRowId == -1) return;
		
		for (int idx = 0; idx < depth && newData[currFirstChildRowId][idx].getFormattedValue() == null; idx ++) {
			//update actual data prefix from previous first entry to the current first entry 
			newData[currFirstChildRowId][idx].setFormattedValue(cellDataSet.getCellSetBody()[prevFirstChildRowId][idx].getFormattedValue());
			newData[currFirstChildRowId][idx].setRawValue(cellDataSet.getCellSetBody()[prevFirstChildRowId][idx].getRawValue());
			cellDataSet.getCellSetBody()[prevFirstChildRowId][idx].setFormattedValue(null);
			cellDataSet.getCellSetBody()[prevFirstChildRowId][idx].setRawValue(null);
		}
	}
	
	private void updateColTotals(TotalAggregator[][] oldColTotals, TotalAggregator[][] newColTotals) {
		for (int idx = 0; idx < oldColTotals.length; idx ++) {
			oldColTotals[idx] = newColTotals[idx];
		}
	}
	
	private void initTotals(List<TotalNode>[] totals) {
		for (int idx = 0; idx < totals.length; idx ++)
			totals[idx] = new ArrayList<TotalNode>();
	}
}
