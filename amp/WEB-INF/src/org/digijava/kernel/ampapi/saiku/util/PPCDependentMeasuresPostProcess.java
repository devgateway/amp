/**
 * 
 */
package org.digijava.kernel.ampapi.saiku.util;

import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.reports.CustomMeasures;
import org.digijava.kernel.ampapi.mondrian.util.MoConstants;
import org.olap4j.metadata.Measure;
import org.saiku.olap.dto.resultset.AbstractBaseCell;
import org.saiku.olap.dto.resultset.CellDataSet;
import org.saiku.olap.dto.resultset.DataCell;
import org.saiku.service.olap.totals.aggregators.SumAggregator;
import org.saiku.service.olap.totals.aggregators.TotalAggregator;

import clover.org.apache.commons.lang.StringUtils;

/**
 * <pre>
 * In order to provide correct result in Mondrian for PPC dependent measures like
 * Uncommitted Balance = Propose Project Amount - Actual Commitments (filtered)
 * we need to define Uncommitted Balance as a measure and remove duplicate PPC values from formula
 * </pre>
 * @author Nadejda Mandrescu
 */
public class PPCDependentMeasuresPostProcess {
	protected static final Logger logger = Logger.getLogger(PPCDependentMeasuresPostProcess.class);
	
	protected ReportSpecification spec;
	protected CellDataSet cellDataSet;
	protected List<ReportOutputColumn> leafHeaders;
	protected Map<Long, Double> internalIdPPC = new TreeMap<Long, Double>();
	
	protected Boolean mustProcess = null;
	
	public PPCDependentMeasuresPostProcess(ReportSpecification spec, CellDataSet cellDataSet, 
			List<ReportOutputColumn> leafHeaders) {
		this.spec = spec;
		this.leafHeaders = leafHeaders;
		this.cellDataSet = cellDataSet;
	}
	
	public void processBeforeMergeByHierarchy() {
		// remember if we actually need to do something
		this.mustProcess = deductColumnTotals();
	}
	
	protected boolean deductColumnTotals() {
		if (!spec.isCalculateColumnTotals() || cellDataSet.getColTotalsLists() == null 
				|| cellDataSet.getColTotalsLists().length == 0)
			return false;
		
		SortedSet<Integer> ppcDepependentMeasuresPos = getPPCDependentMeasuresPositions();
		int ppcPos = getColumnPosition(ColumnConstants.PROPOSED_PROJECT_AMOUNT);
		int internalIdPos = getColumnPosition(ColumnConstants.INTERNAL_USE_ID);
		if (ppcDepependentMeasuresPos.isEmpty() || ppcPos == -1)
			return false;
		
		TotalAggregator[][] colTotals = cellDataSet.getColTotalsLists()[0].get(0).getTotalGroups();
		AbstractBaseCell[][] cells = cellDataSet.getCellSetBody();
		
		Double currentPPC = null;
		
		for (int row = 0; row < cells.length; row ++) {
			String ppcColValue = cells[row][ppcPos].getFormattedValue();
			boolean increaseOccurance = false;
			if (StringUtils.isNotBlank(ppcColValue)) {
				if (MoConstants.UNDEFINED_AMOUNT_STR.equals(ppcColValue)) {
					currentPPC = 0d;
				} else {
					currentPPC = Double.valueOf(ppcColValue);
				}
				// keep reference of PPC per internal ids to adjust the hierarchical totals later
				Long internalId = Long.valueOf(cells[row][internalIdPos].getFormattedValue());
				internalIdPPC.put(internalId, currentPPC);
			} else {
				// this row will be merged to group by project => deduct here all PPC occurrences (start from 0)
				increaseOccurance = true;
			}
			
			// count how many repeated PPC amounts are used, for each PPC dependent measure
			for (Integer measurePos : ppcDepependentMeasuresPos) {
				int count = increaseOccurance ? 0 : -1;
				for (int col = measurePos + cellDataSet.getLeftOffset(); col < cells[row].length; 
						col += cellDataSet.getSelectedMeasures().length) {
					if (((DataCell) cells[row][col]).getRawNumber() != null) {
						count++;
					}
				}				
				
				SumAggregator sa = (SumAggregator) colTotals[measurePos][row];
				// remember PPC of the current total row (formatted value of column totals is not used)
				sa.setFormattedValue(String.valueOf(currentPPC));
				
				// deduct if more than once
				if (count > 0) {
					sa.addData(- currentPPC * count);
				}
			}
		}
		return true;
	}
	
	protected SortedSet<Integer> getPPCDependentMeasuresPositions() {
		SortedSet<Integer> positions = new TreeSet<Integer>();
		int pos = -1;
		for (Measure rm : cellDataSet.getSelectedMeasures()) {
			pos ++;
			if (CustomMeasures.PPC_DEPENDENCY.contains(rm.getName())) {
				positions.add(pos);
			}
		}
		return positions;
	}
	
	protected int getColumnPosition(String columnName) {
		int pos = -1;
		for (ReportOutputColumn roc : leafHeaders) {
			pos ++;
			if (columnName.equals(roc.originalColumnName)) {
				return pos;
			}
		}
		return -1;
	}
	
}
