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
import org.dgfoundation.amp.reports.mondrian.MondrianReportGenerator;
import org.saiku.olap.dto.resultset.CellDataSet;
import org.saiku.service.olap.totals.TotalNode;
import org.saiku.service.olap.totals.aggregators.SumAggregator;
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
	
//	protected CellDataSetPostProcessing(CellDataSetPostProcessing o) {
//		this.spec = o.spec;
//		this.cellDataSet = o.cellDataSet;
//		this.leafHeaders = o.leafHeaders;
//		this.environment = o.environment;
//	}
	
	/**
	 * returns true IFF a column has a zero total
	 * @param byColTotals
	 * @param columnNumber
	 * @return
	 */
	protected boolean isZero(TotalAggregator[] byColTotals, int columnNumber) {
		return byColTotals[columnNumber] != null && byColTotals[columnNumber].getFormattedValue().equals("0");
	}
	
	/**
	 * returns true IFF this is a Flow Column which has a parent which is a funding flow-enabled measure 
	 * @param columnNumber
	 * @return
	 */
	protected boolean hasFundingFlowParent(ReportOutputColumn roc) {
		if (roc.parentColumn == null) return false; // no parent -> definitely not a funding flow column
		if (ArConstants.DIRECTED_MEASURE_TO_NONDIRECTED_MEASURE.keySet().contains(roc.parentColumn.originalColumnName))
			return true;
		return false;
	}
	
	protected boolean isFundingFlowBottom(ReportOutputColumn roc) {
		return !(roc.originalColumnName.trim().isEmpty() || roc.originalColumnName.equals("Undefined"));
	}
	
	protected boolean shouldRemoveColumnFromYear(ReportOutputColumn roc) {
		boolean res = hasFundingFlowParent(roc) ^ isFundingFlowBottom(roc);
		return res;
	}
	
	/**
	 * returns true IFF 1) the predecessor has a different parent AND 2) nobody with the same parent to the right is nonzero
	 * @param columnNumber
	 * @return
	 */
	protected boolean isFirstAndOnlyNonZero(ReportOutputColumn selfHeader, ReportOutputColumn previousHeader, TotalAggregator[] byColTotals, int colNr) {
		if (selfHeader == null || selfHeader.parentColumn == null)
			return false;
			
		if (previousHeader != null && previousHeader.parentColumn != null && selfHeader.parentColumn.equals(previousHeader.parentColumn))
			return false; // we are not the first one
		
		colNr ++; // invariant: colNr is the first unchecked one
		while (colNr + spec.getColumnNames().size() < leafHeaders.size()) {
			ReportOutputColumn colH = leafHeaders.get(colNr + spec.getColumnNames().size());
			if (colH.parentColumn == null) break;
			if (!selfHeader.parentColumn.equals(colH.parentColumn)) break;
			// parents equal -> check if it is zero
			if (!isZero(byColTotals, colNr)) return false;
			colNr ++;
		}
		// gone till here and did not exit -> we reached the end of the year, thus return true
		return true;
	}
	
//	public void removeZeroMTEFColumns(Set<Integer> mtefColumns) {
//		List<TotalNode>[] rowTotals = cellDataSet.getRowTotalsLists();
//		TotalAggregator[][] matrixTotals = rowTotals[0].get(0).getTotalGroups();
//		if (matrixTotals == null || matrixTotals.length == 0 || matrixTotals[0] == null) return;
//		
//		TotalAggregator[] byColTotals = matrixTotals[0];
//		SortedSet<Integer> colsToDelete = new TreeSet<>();
//		for(int columnNumber:mtefColumns) {
//			if (isZero(byColTotals, columnNumber))
//				colsToDelete.add(columnNumber);
//		}
//		deleteColumns(colsToDelete);
//	}
	
	/**
	 * returns null if for some reason totals don't exist
	 * @return
	 */
	public TotalAggregator[] getByColTotals() {
		List<TotalNode>[] rowTotals = cellDataSet.getRowTotalsLists();
		boolean canLook = rowTotals != null && rowTotals.length > 0 && rowTotals[0].size() > 0 && rowTotals[0].get(0).getTotalGroups() != null;
		if (!canLook) 
			return null; // nothing to do
		TotalAggregator[][] matrixTotals = rowTotals[0].get(0).getTotalGroups();
		if (matrixTotals == null || matrixTotals.length == 0 || matrixTotals[0] == null)
			return null;
		
		TotalAggregator[] byColTotals = matrixTotals[0];
		return byColTotals;
	}
	
	/**
	 * return column numbers of cell corresponding to dummy funding flows (in the saikucell output) 
	 * @param internalIdUsed
	 * @return
	 */
	public SortedSet<Integer> getEmptyFlowsColumns(boolean internalIdUsed) {
		TotalAggregator[] byColTotals = getByColTotals();
		SortedSet<Integer> colsToDelete = new TreeSet<>();

		if (byColTotals == null)
			return colsToDelete;
		
		int measureStartId = 0; //(internalIdUsed ? 1 : 0);
		int measuresEndId = byColTotals.length;
		
		ReportOutputColumn previousHeader = null; // needed for Mondrian post-processing hack "keep a zero-value Funding Flow entry IFF (it is the first entry for the year AND there are no non-zero-value entries with the same parent)
		
		// pass1: identify output raster columns to delete
		for(int columnNumber = measureStartId; columnNumber < measuresEndId; columnNumber++) {
			ReportOutputColumn selfHeader = leafHeaders.get(columnNumber + spec.getColumnNames().size());
			boolean shouldDeleteThisColumn = false;
			shouldDeleteThisColumn |= isFundingFlowBottom(selfHeader) && (!hasFundingFlowParent(selfHeader)); // [AC / DN-IMPL] is not ok
			shouldDeleteThisColumn |= /*isFundingFlowBottom(selfHeader) && */hasFundingFlowParent(selfHeader) && isZero(byColTotals, columnNumber) && !isFirstAndOnlyNonZero(selfHeader, previousHeader, byColTotals, columnNumber); // because Mondrian does a Carthesian Product inside a year
			if (shouldDeleteThisColumn) // no empty funding flows; also no empty column if desired so
				colsToDelete.add(columnNumber + spec.getColumns().size());
			previousHeader = selfHeader;
		}
		// pass2: change zeroed [RealDisb-DN-EXEC] into zeroes [RealDisb-()]
		for(int columnNumber = measureStartId ; columnNumber < measuresEndId; columnNumber++) {
			ReportOutputColumn selfHeader = leafHeaders.get(columnNumber + spec.getColumnNames().size());
			if (hasFundingFlowParent(selfHeader) && isZero(byColTotals, columnNumber)) {
				leafHeaders.set(columnNumber + spec.getColumnNames().size(), new ReportOutputColumn(" ", selfHeader.parentColumn, " ", selfHeader.flags));
			}
		}
		//colsToDelete.clear();
		//colsToDelete.add(1 + spec.getColumns().size());

		return colsToDelete;
	}
	
	/**
	 * partial copy-paste off {@link #getEmptyFlowsColumns(boolean)}, but this function will disappear in AMP 2.12
	 * @return
	 */
	public SortedSet<Integer> getDummyMTEFColumns() {
		TotalAggregator[] byColTotals = getByColTotals();
		SortedSet<Integer> colsToDelete = new TreeSet<>();

		if (byColTotals == null)
			return colsToDelete;
		
		int measureStartId = 0; //(internalIdUsed ? 1 : 0);
		int measuresEndId = byColTotals.length;
		
		for(int columnNumber = measureStartId; columnNumber < measuresEndId; columnNumber++) {
			ReportOutputColumn selfHeader = leafHeaders.get(columnNumber + spec.getColumnNames().size());
			if (selfHeader.flags.contains(MondrianReportGenerator.MTEF_TO_DELETE)) {
				if (!isZero(byColTotals, columnNumber))
					throw new RuntimeException("mtef column marked for output-deletion has no-zero contents!");
				if (!selfHeader.flags.contains(MondrianReportGenerator.IS_MTEF_COLUMN))
					throw new RuntimeException("mtef column marked for output-deletion is not marked as an MTEF column!");
				colsToDelete.add(columnNumber + spec.getColumns().size());
			}
		}
		
		return colsToDelete;
	}
	
	public void deleteColumns(SortedSet<Integer> colsToDelete) {
		SortedSet<Integer> measToDelete = new TreeSet<>();
		for(int colNr:colsToDelete)
			measToDelete.add(colNr - spec.getColumns().size());

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
				for(int j = 0; j < matrix[i].length; j++) {
					SumAggregator sa = (SumAggregator) matrix[i][j];
					sa.addData(- sa.getValue());
				}
			}
		}
	}
	
	public void postProcessAmountsBeforeHierarchicalMerge() {
		(new PPCDependentMeasuresPostProcess(spec, cellDataSet, leafHeaders)).processBeforeMergeByHierarchy();
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
