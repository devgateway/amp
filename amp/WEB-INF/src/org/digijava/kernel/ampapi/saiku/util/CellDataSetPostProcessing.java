/**
 * 
 */
package org.digijava.kernel.ampapi.saiku.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportEnvironment;
import org.dgfoundation.amp.newreports.ReportMeasure;
import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.reports.CustomMeasures;
import org.saiku.olap.dto.resultset.CellDataSet;
import org.saiku.service.olap.totals.TotalNode;
import org.saiku.service.olap.totals.aggregators.TotalAggregator;

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
	
	public void removeEmptyFlowsColumns(boolean internalIdUsed) {
//		if (System.currentTimeMillis() > 1)
//			return;
		SortedSet<Integer> colsToDelete = new TreeSet<>();
		List<TotalNode>[] rowTotals = cellDataSet.getRowTotalsLists();
		boolean canLook = rowTotals != null && rowTotals.length > 0 && rowTotals[0].size() > 0 && rowTotals[0].get(0).getTotalGroups() != null;
		if (!canLook) return; // nothing to do
		TotalAggregator[][] matrixTotals = rowTotals[0].get(0).getTotalGroups();
		if (matrixTotals == null || matrixTotals.length == 0 || matrixTotals[0] == null) return;
		
		TotalAggregator[] byColTotals = matrixTotals[0];
		
		int measureStartId = (internalIdUsed ? 1 : 0);
		int measuresEndId = byColTotals.length;
		
		for(int i = measureStartId; i < measuresEndId; i++) {
			if (byColTotals[i] != null && byColTotals[i].getFormattedValue().equals("0"))
				colsToDelete.add(i + spec.getColumns().size());
		}
		//colsToDelete.clear();
		//colsToDelete.add(1 + spec.getColumns().size());
		
		SortedSet<Integer> measToDelete = new TreeSet<>();
		for(int colNr:colsToDelete)
			measToDelete.add(colNr - spec.getColumns().size());

		//cellDataSet.setCellSetHeaders(SaikuUtils.removeColumns(cellDataSet.getCellSetHeaders(), colsToDelete));
		//cellDataSet.setCellSetBody(SaikuUtils.removeColumns(cellDataSet.getCellSetBody(), colsToDelete));
		cleanupColumnsFromCellDataSet(colsToDelete, measToDelete, new ArrayList<String>());
		removeColumnsFromLeafHeaders(colsToDelete, 0);
	}
	
	protected void removeColumnsFromLeafHeaders(SortedSet<Integer> colsToDelete, int delta) {
		// remove associated headers for dummy columns
		int origHeaderPos = - delta;
		for (Iterator<ReportOutputColumn> iter = this.leafHeaders.iterator(); iter.hasNext(); origHeaderPos++ ) {
			ReportOutputColumn roc = iter.next();
			if (colsToDelete.contains(origHeaderPos)) {
				iter.remove();
			}
		}
	}
	
	public void nullifyFundingFlowsMeasuresTotals() {
		if (cellDataSet.getColTotalsLists() == null || cellDataSet.getColTotalsLists().length == 0) return;
		List<TotalNode> level0 = cellDataSet.getColTotalsLists()[0];
		if (level0.isEmpty()) return;
		TotalNode byMeasureTotals = level0.get(0);
		String[] columnNames = byMeasureTotals.getMemberCaptions();
		if (columnNames == null) return;
		TotalAggregator[][] matrix = byMeasureTotals.getTotalGroups();
		if (matrix == null) return;
		for(int i = 0; i < Math.min(columnNames.length, matrix.length); i++) {
			String columnName = columnNames[i];
			if (ArConstants.DIRECTED_MEASURE_TO_DIRECTED_TRANSACTION_VALUE.containsKey(columnName)) {
				for(int j = 0; j < matrix[i].length; j++)
					matrix[i][j].setFormattedValue("");
			}
		}
	}
	
	/**
	 * Removes dummy columns & hierarchies that were needed to retrieve appropriate reports result,
	 * while those columns & hierarchies where not requested
	 * returns internalIdUsed  
	 */
	public boolean removeDummyColumns() {
		// build the list of measures that must be distributed
		List<String> distributionMeasures = new ArrayList<String>();
		boolean isTotalsOnly = GroupingCriteria.GROUPING_TOTALS_ONLY.equals(spec.getGroupingCriteria()); 
		if (!isTotalsOnly) {
			for (ReportMeasure m : spec.getMeasures()) {
				if (!CustomMeasures.NO_DISTRIBUTION.contains(m.getMeasureName())) {
					distributionMeasures.add(m.getMeasureName());
				}
			}
		}
		boolean hasDistributionColumnsToRemove = !isTotalsOnly && distributionMeasures.size() < spec.getMeasures().size(); 
		// identify non-measures dummy columns count
		int dummyNo = spec.getDummyColumns().size();
		if (dummyNo == 0 && !hasDistributionColumnsToRemove) {
			// no dummy columns to remove
			return false;
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
		SortedSet<Integer> measureColIdsToHide = new TreeSet<Integer>();
		
		if (hasDistributionColumnsToRemove) {
			measureColIdsToHide = detectFullyEmptyGroups(dummyHierarchy != null);
			for (Integer colId : measureColIdsToHide) {
				dummyColumnsIds.add(colId + spec.getColumns().size());
				dummyColumnsIdsIfInternalIdUsed.add(colId + spec.getColumns().size() - 1);
			}
		}
		removeColumnsFromLeafHeaders(dummyColumnsIds, 0);
		if (dummyHierarchy != null) {
			spec.getHierarchies().remove(dummyHierarchy);
			dummyColumnsIds = dummyColumnsIdsIfInternalIdUsed;
		}
		
		cleanupColumnsFromCellDataSet(dummyColumnsIds, measureColIdsToHide, distributionMeasures);
		
		spec.removeDummyColumns();
		return dummyHierarchy != null;
	}
	
	private SortedSet<Integer> detectFullyEmptyGroups(boolean internalIdUsed) {
		// remember the columns that are fully empty - side effect from AMP-19940: 
		// non-empty columns are removed afterwards the column groups are built and thus invalid empty column groups remain
		SortedSet<Integer> fullEmptyColIds = new TreeSet<Integer>();
		if (cellDataSet.getCellSetBody() == null || cellDataSet.getCellSetBody().length == 0 
				|| cellDataSet.getCellSetBody()[0] == null || cellDataSet.getCellSetBody()[0].length == 0)
			return fullEmptyColIds;
		
		
		int measureStartId = spec.getColumns().size() - (internalIdUsed ? 1 : 0);
		int measureStartIdInHeaders = spec.getColumns().size();
		int headerPos = measureStartIdInHeaders;
		int measuresEndId = cellDataSet.getCellSetBody()[0].length;
		SortedSet<Integer> mustDelete = new TreeSet<Integer>();
		// assume initially that all are fully empty
		for (int measureColId = measureStartId; measureColId < measuresEndId; measureColId++) {
			// remember the 0 based measure columns ids
			int zeroBasedColId = measureColId - measureStartId;
			fullEmptyColIds.add(zeroBasedColId); 
			if (CustomMeasures.NO_DISTRIBUTION.contains(leafHeaders.get(headerPos++).originalColumnName)) {
				mustDelete.add(zeroBasedColId);
			}
		}
		// detect fully empty columns
		for (int rowId = 0; rowId < cellDataSet.getCellSetBody().length; rowId++) {
			for (Iterator<Integer> iter = fullEmptyColIds.iterator(); iter.hasNext(); ) {
				int measureColId = iter.next();
				if (mustDelete.contains(measureColId))
					continue;
				String value = cellDataSet.getCellSetBody()[rowId][measureColId + measureStartId].getFormattedValue();
				if (value != null && !value.isEmpty()) {
					iter.remove();
				}
			}
		}
		// detect fully empty column groups: we should remove fully empty column groups, while empty sub-columns groups are allowed
		Set<ReportOutputColumn> columnGroupsToRemove = new HashSet<ReportOutputColumn>();
		for (Integer colId : fullEmptyColIds) {
			columnGroupsToRemove.add(leafHeaders.get(measureStartIdInHeaders + colId));
		}
		for(Iterator<ReportOutputColumn> iter = columnGroupsToRemove.iterator(); iter.hasNext(); ) {
			ReportOutputColumn child = iter.next();
			boolean isLeaf = child.children.size() == 0;
			ReportOutputColumn parent = child.parentColumn;
			if (parent != null) {
				boolean allSiblingsAreEmpty = true;
				for (ReportOutputColumn c : parent.children) {
					if (!columnGroupsToRemove.contains(c) && !CustomMeasures.NO_DISTRIBUTION.contains(c.originalColumnName)) {
						allSiblingsAreEmpty = false;
						break;
					}
				}
				// remove all siblings whether it is a leaf header or whether all siblings are also empty  
				if (isLeaf || allSiblingsAreEmpty) {
					for (ReportOutputColumn c : parent.children) {
						columnGroupsToRemove.remove(c);
						// remove all empty leaf siblings from the full empty list if not entire group is empty
						if (isLeaf && !allSiblingsAreEmpty 
								&& !CustomMeasures.NO_DISTRIBUTION.contains(c.originalColumnName)) {
							fullEmptyColIds.remove(leafHeaders.indexOf(c) - measureStartIdInHeaders);
						}
					}
					// restart the iterator
					iter = columnGroupsToRemove.iterator();
				}
			}
		}
		
		// if we remove all distribution columns
		if (measuresEndId - measureStartId == fullEmptyColIds.size() 
				&& cellDataSet.getCellSetHeaders() != null && cellDataSet.getCellSetHeaders().length > 0) {
			cellDataSet.setOffset(0);
		}
		
		return fullEmptyColIds;
	}
	
	protected void cleanupColumnsFromCellDataSet(SortedSet<Integer> dummyColumnsIds, 
			SortedSet<Integer> measureColIdsToHide, List<String> distributionMeasures) {
		cellDataSet.setCellSetHeaders(SaikuUtils.removeColumns(cellDataSet.getCellSetHeaders(), dummyColumnsIds));
		cellDataSet.setCellSetBody(SaikuUtils.removeColumns(cellDataSet.getCellSetBody(), dummyColumnsIds));
		
		if (measureColIdsToHide.size() == 0)
			return;
		
		
		// adjust column sub-totals 
		if (cellDataSet.getColTotalsLists() != null) {
			String[] captions = distributionMeasures.toArray(new String[distributionMeasures.size()]);
			// start from the hidden sub-totals (idx = 1), because grand column totals are not affected in this case
			for (int i = 1; i < cellDataSet.getColTotalsLists().length; i++) {
				List<TotalNode> newTotals = new ArrayList<TotalNode>(cellDataSet.getColTotalsLists()[i].size());
				for (TotalNode tn : cellDataSet.getColTotalsLists()[i]) {
					int newSpan = tn.getMemberCaptions() == null ? 1 : (tn.getSpan() / tn.getMemberCaptions().length) * captions.length;
					int newWidth = tn.getMemberCaptions() == null ? 0 : newSpan;
					tn = new TotalNode(captions, null, TotalAggregator.newInstanceByFunctionName("not"), null, captions.length);
					tn.setSpan(newSpan);
					tn.setWidth(newWidth);
					newTotals.add(tn);
				}
				cellDataSet.getColTotalsLists()[i] = newTotals;
			}
		}
		// remove totals associated to "no distribution" columns from row totals 
		SaikuUtils.removeTotalsColumns(cellDataSet.getRowTotalsLists(), measureColIdsToHide);
	}
	
}
