package org.dgfoundation.amp.nireports.schema;

import static java.util.stream.Collectors.toSet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Predicate;

import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.DatedCell;
import org.dgfoundation.amp.nireports.ImmutablePair;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.NiUtils;
import org.dgfoundation.amp.nireports.output.NiOutCell;
import org.dgfoundation.amp.nireports.output.NiSplitCell;
import org.dgfoundation.amp.nireports.runtime.CellColumn;
import org.dgfoundation.amp.nireports.runtime.ColumnContents;
import org.dgfoundation.amp.nireports.runtime.NiCell;
import org.dgfoundation.amp.nireports.runtime.VSplitStrategy;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;
import org.dgfoundation.amp.nireports.schema.NiDimension.NiDimensionUsage;

/**
 * a specification of the behaviour of a given {@link NiReportColumn} / {@link NiReportMeasure}
 * @author Dolghier Constantin
 *
 */
public interface Behaviour<V extends NiOutCell> {
	
	/**
	 * @return the maximum supported resolution. For any result which is not NONE, the column should contain cells which implement {@link DatedCell}
	 */
	public TimeRange getTimeRange();
	
	/**
	 * performs a horizontal reducing operation on cells: for multiple {@link Cell}'s in one displayed locum<br />
	 * Client code should probably use {@link #horizontalReduce(List)} instead, as that one has guards for null/empty input
	 * @param cells
	 * @return
	 */
	public V doHorizontalReduce(List<NiCell> cells);
	public default Cell filterCell(Map<NiDimensionUsage, IdsAcceptor> acceptors, NiCell oldCell, NiCell splitCell) {
		if (cellMeetsCoos(acceptors, oldCell, splitCell))
			return oldCell.getCell();
		else
			return null;
	}
	
	/** 
	 * <strong>make the common case fast: if this function returns null, it is a shortcut for "measure listenes to all hierarchies"</strong>
	 * @return a predicate which governs the hierarchies whose percentages are listened to by an entity 
	 */
	public default Predicate<NiDimensionUsage> getHierarchiesListener() {
		return null;
	}
	
	public default ColumnContents horizSplit(ColumnContents oldContents, Map<Long, NiCell> splitCells, Set<Long> acceptableMainIds, Map<NiDimensionUsage, IdsAcceptor> acceptors) {
		Map<Long, List<NiCell>> z = new HashMap<>();
		for(Long mainId:acceptableMainIds) {
			List<NiCell> oldCells = oldContents.data.get(mainId);
			NiCell splitCell = splitCells.get(mainId);
			if (oldCells == null)
				continue;
			for(NiCell oldCell:oldCells) {
				Cell filteredCell = filterCell(acceptors, oldCell, splitCell);
				if (filteredCell != null)
					z.computeIfAbsent(mainId, id -> new ArrayList<>()).add(oldCell.advanceHierarchy(filteredCell, splitCell.getCell()));
			}
		}
		return new ColumnContents(z);
	}
	
	public default NiOutCell horizontalReduce(List<NiCell> cells) {
		if (cells == null || cells.isEmpty())
			return getZeroCell();
		return doHorizontalReduce(cells);
	}
	
	public Cell buildUnallocatedCell(long mainId, long entityId, LevelColumn levelColumn);
	
	/**
	 * computes a "zero" cell, which is different from an "empty" cell. For numerical cells, this is a cell with zero. For textual cells, this is an "" cell, although these do not have 
	 * @return
	 */
	public V getZeroCell();
	
	/**
	 * merges 1+ splitter cells with the same displayed value into one NiSplitCell
	 * @param splitterCells
	 * @return
	 */
	public default NiSplitCell mergeSplitterCells(List<NiCell> splitterCells) {
		return new NiSplitCell((NiReportColumn<?>) splitterCells.get(0).getEntity(), 
			splitterCells.get(0).getDisplayedValue(), 
			splitterCells.stream().map(z -> z.getCell().entityId).collect(toSet()), 
			splitterCells.stream().anyMatch(z -> z.isUndefinedCell()));
	}
	
	public default String getDebugDigest() {
		return String.format("%s", getTimeRange());
	}
	
	public static boolean cellMeetsCoos(Map<NiDimensionUsage, IdsAcceptor> acceptors, NiCell oldCell, NiCell splitCell) {
		Map<NiDimensionUsage, NiDimension.Coordinate> cellCoos = oldCell.getCell().getCoordinates();
		NiUtils.failIf(cellCoos == null, "null cellCoos");
		for(Entry<NiDimensionUsage, NiDimension.Coordinate> splitterElem:splitCell.getCell().getCoordinates().entrySet()) {
			NiDimensionUsage dimUsage = splitterElem.getKey();
//			NiDimension.Coordinate splitterCoo = splitterElem.getValue();
			NiDimension.Coordinate cellCoo = cellCoos.get(dimUsage);
			if (cellCoo == null)
				continue; // cell is indifferent to this coordinate
			IdsAcceptor acceptor = acceptors.get(dimUsage);
			boolean isAcceptable = acceptor.isAcceptable(cellCoo);
			if (!isAcceptable)
				return false;
		}
		return true;
	}

	public default V doVerticalReduce(Collection<V> cells) {
		return getZeroCell();
	}
	
	/**
	 * whether this CellColumn having a value in a given subreport means keeping the subreport in case {@link ReportSpecification#isDisplayEmptyFundingRows()} is true and we are running a report with hierarchies
	 * @return
	 */
	public boolean isKeepingSubreports();
	
	public default ImmutablePair<String, ColumnContents> getTotalCells(NiReportsEngine context, NiReportedEntity<?> entity, ColumnContents fetchedContents) {
		return null; // by default no entities go to totals
	}
	
	public default List<VSplitStrategy> getSubMeasureHierarchies(NiReportsEngine context) {
		return null; // by default entities do not split sub-measure
	}
	
	/**
	 * whether the reports engine should delete leaves of this column when they are empty report-wise
	 * @param column
	 * @return
	 */
	public default boolean shouldDeleteLeafIfEmpty(CellColumn column) {
		return false;
	}
}
