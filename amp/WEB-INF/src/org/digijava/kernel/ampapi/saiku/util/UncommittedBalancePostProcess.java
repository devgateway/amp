/**
 * 
 */
package org.digijava.kernel.ampapi.saiku.util;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.newreports.ReportArea;
import org.dgfoundation.amp.newreports.ReportMeasure;
import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.digijava.kernel.ampapi.mondrian.util.MoConstants;
import org.saiku.olap.dto.resultset.AbstractBaseCell;
import org.saiku.olap.dto.resultset.CellDataSet;
import org.saiku.olap.dto.resultset.DataCell;
import org.saiku.service.olap.totals.aggregators.SumAggregator;
import org.saiku.service.olap.totals.aggregators.TotalAggregator;

import clover.org.apache.commons.lang.StringUtils;

/**
 * <pre>
 * In order to provide correct result in Mondrian for 
 * Uncommitted Balance = Propose Project Amount - Actual Commitments (filtered)
 * we need to define Uncommitted Balance as a measure and remove duplicate PPC values from formula
 * </pre>
 * @author Nadejda Mandrescu
 */
public class UncommittedBalancePostProcess {
	protected static final Logger logger = Logger.getLogger(UncommittedBalancePostProcess.class);
	
	protected ReportSpecification spec;
	protected CellDataSet cellDataSet;
	protected List<ReportOutputColumn> leafHeaders;
	protected Map<Long, Double> internalIdPPC = new TreeMap<Long, Double>();
	
	protected Boolean mustProcess = null;
	
	public UncommittedBalancePostProcess(ReportSpecification spec, CellDataSet cellDataSet, 
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
		
		int ubPos = getUncommittedBalancePosition();
		int ppcPos = getColumnPosition(ColumnConstants.PROPOSED_PROJECT_AMOUNT);
		int internalIdPos = getColumnPosition(ColumnConstants.INTERNAL_USE_ID);
		if (ubPos == -1 || ppcPos == -1)
			return false;
		
		TotalAggregator[] colTotals = cellDataSet.getColTotalsLists()[0].get(0).getTotalGroups()[ubPos];
		AbstractBaseCell[][] cells = cellDataSet.getCellSetBody();
		
		Double currentPPC = null;
		
		for (int row = 0; row < cells.length; row ++) {
			// count how many repeated PPC amounts are used
			int count = -1;
			for (int col = ubPos + cellDataSet.getLeftOffset(); col < cells[row].length; col += spec.getMeasures().size()) {
				if (((DataCell) cells[row][col]).getRawNumber() != null) {
					count++;
				}
			}
			String ppcColValue = cells[row][ppcPos].getFormattedValue();
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
				count ++;
			}
			
			SumAggregator sa = (SumAggregator) colTotals[row];
			// remember PPC of the current total row (formatted value of column totals is not used)
			sa.setFormattedValue(String.valueOf(currentPPC));
			
			// deduct if more than once
			if (count > 0) {
				sa.addData(- currentPPC * count);
			}
		}
		return true;
	}
	
	protected int getUncommittedBalancePosition() {
		int pos = -1;
		for (ReportMeasure rm : spec.getMeasures()) {
			pos ++;
			if (MeasureConstants.UNCOMMITTED_BALANCE.equals(rm.getMeasureName())) {
				return pos;
			}
		}
		return -1;
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
