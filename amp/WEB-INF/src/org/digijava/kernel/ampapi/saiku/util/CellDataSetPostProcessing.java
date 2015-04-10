/**
 * 
 */
package org.digijava.kernel.ampapi.saiku.util;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportEnvironment;
import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.saiku.olap.dto.resultset.CellDataSet;

/**
 * Stores {@link CellDataSet} post-processing actions 
 * that do not require a separate class like {@link CellDataSetToAmpHierarchies}
 * @author Nadejda Mandrescu
 *
 */
public class CellDataSetPostProcessing {
	
	protected ReportSpecification spec;
	protected CellDataSet cellDataSet;
	protected List<ReportOutputColumn> leafHeaders;
	protected ReportEnvironment environment;
	
	public CellDataSetPostProcessing(ReportSpecification spec, CellDataSet cellDataSet, 
			List<ReportOutputColumn> leafHeaders, ReportEnvironment environment) {
		this.spec = spec;
		this.cellDataSet = cellDataSet;
		this.leafHeaders = leafHeaders;
		this.environment = environment;
	}
	
	protected CellDataSetPostProcessing(CellDataSetPostProcessing o) {
		this.spec = o.spec;
		this.cellDataSet = o.cellDataSet;
		this.leafHeaders = o.leafHeaders;
		this.environment = o.environment;
	}
	
	/**
	 * Removes dummy columns & hierarchies that were needed to retrieve appropriate reports result,
	 * while those columns & hierarchies where not requested  
	 */
	public void removeDummyColumns() {
		int dummyNo = spec.getDummyColumns().size();
		if (dummyNo == 0) {
			return;
		}
		
		cellDataSet.setLeftOffset(cellDataSet.getLeftOffset() - dummyNo);
		cellDataSet.setWidth(cellDataSet.getWidth() - dummyNo);
		ReportColumn dummyHierarchy = null;
		SortedSet<Integer> dummyColumnsIds = new TreeSet<Integer>();
		SortedSet<Integer> dummyColumnsIdsIfInternalIdUsed = new TreeSet<Integer>();
		int currentId = 0;
		// collect real columns only
		for (ReportColumn col : spec.getColumns()) { 
			if (spec.getDummyColumns().contains(col)) {
				dummyColumnsIds.add(currentId);
				// remember the hierarchy used by internal id
				if (ColumnConstants.INTERNAL_USE_ID.equals(col.getColumnName())) {
					dummyHierarchy = col;
				} else {
					// internal id column was removed during non-hierarchical columns merge (see CellDataSetToAmpHierarchies.setNewData())
					dummyColumnsIdsIfInternalIdUsed.add(currentId - 1);
				}
			}
			currentId++;
		}
		// remove associated headers for dummy columns
		int origHeaderPos = 0;
		SortedSet<Integer> rowTotalsColIdsToClear = new TreeSet<Integer>(); // stores column indexes from rows totals that must be cleared (until AMP-19940, then may have to be removed)
		for (Iterator<ReportOutputColumn> iter = this.leafHeaders.iterator(); iter.hasNext(); origHeaderPos++ ) {
			ReportOutputColumn roc = iter.next();
			if (dummyColumnsIds.contains(origHeaderPos)) {
				iter.remove();
			}
		/*	if (TotalsOnlyMeasures.MEASURES.contains(roc.originalColumnName) 
					&& roc.parentColumn != null && !roc.parentColumn.originalColumnName.equals(MoConstants.TOTAL_MEASURES)) {
				rowTotalsColIdsToClear.add(origHeaderPos - spec.getColumns().size());
				 within AMP-19940 we may need to
				iter.remove();
				 
			}*/
		}
		if (dummyHierarchy != null) {
			spec.getHierarchies().remove(dummyHierarchy);
			dummyColumnsIds = dummyColumnsIdsIfInternalIdUsed;
		}
		
		cleanupColumnsFromCellDataSet(dummyColumnsIds, rowTotalsColIdsToClear);
		
		spec.removeDummyColumns();
	}
	
	protected void cleanupColumnsFromCellDataSet(SortedSet<Integer> dummyColumnsIds, 
			SortedSet<Integer> rowTotalsColIdsToClear) {
		cellDataSet.setCellSetHeaders(SaikuUtils.removeColumns(cellDataSet.getCellSetHeaders(), dummyColumnsIds));
		cellDataSet.setCellSetBody(SaikuUtils.removeColumns(cellDataSet.getCellSetBody(), dummyColumnsIds));
		//SaikuUtils.clearRowTotals(cellDataSet, rowTotalsColIdsToClear);
	}
	
}
