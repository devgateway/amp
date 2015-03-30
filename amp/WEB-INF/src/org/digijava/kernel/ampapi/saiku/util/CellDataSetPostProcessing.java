/**
 * 
 */
package org.digijava.kernel.ampapi.saiku.util;

import java.util.Iterator;
import java.util.LinkedHashSet;
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
 * that do not require a separate class like {@link CellDataSetToAmpHierachies}
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
		spec.getColumnNames().clear();
		Set<ReportColumn> newColumns = new LinkedHashSet<ReportColumn>(spec.getColumns().size() - dummyNo);
		ReportColumn dummyHierarchy = null;
		SortedSet<Integer> dummyColumnsIds = new TreeSet<Integer>();
		SortedSet<Integer> dummyColumnsIdsIfInternalIdUsed = new TreeSet<Integer>();
		int currentId = 0;
		// collect real columns only
		for (ReportColumn col : spec.getColumns()) { 
			if (!spec.getDummyColumns().contains(col)) {
				newColumns.add(col);
				spec.getColumnNames().add(col.getColumnName());
			} else {
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
		for (Iterator<ReportOutputColumn> iter = this.leafHeaders.iterator(); iter.hasNext(); origHeaderPos++ ) {
			iter.next();
			if (dummyColumnsIds.contains(origHeaderPos)) {
				iter.remove();
			}
		}
		if (dummyHierarchy != null) {
			spec.getHierarchies().remove(dummyHierarchy);
			dummyColumnsIds = dummyColumnsIdsIfInternalIdUsed;
		}
		removeDummyColumnsFromCellDataSet(dummyColumnsIds);
		
		spec.getColumns().clear();
		spec.getDummyColumns().clear();
		spec.getColumns().addAll(newColumns);
	}
	
	protected void removeDummyColumnsFromCellDataSet(SortedSet<Integer> dummyColumnsIds) {
		cellDataSet.setCellSetHeaders(SaikuUtils.removeCollumns(cellDataSet.getCellSetHeaders(), dummyColumnsIds));
		cellDataSet.setCellSetBody(SaikuUtils.removeCollumns(cellDataSet.getCellSetBody(), dummyColumnsIds));
	}
	
}
