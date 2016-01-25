package org.dgfoundation.amp.nireports.schema;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Predicate;

import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.DatedCell;
import org.dgfoundation.amp.nireports.NiUtils;
import org.dgfoundation.amp.nireports.runtime.ColumnContents;
import org.dgfoundation.amp.nireports.runtime.HierarchiesTracker;
import org.dgfoundation.amp.nireports.runtime.NiCell;
import org.dgfoundation.amp.nireports.schema.NiDimension.NiDimensionUsage;

/**
 * a specification of the behaviour of a given {@link NiReportColumn} / {@link NiReportMeasure}
 * @author Dolghier Constantin
 *
 */
public interface Behaviour {
	
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
	public Cell doHorizontalReduce(List<NiCell> cells, HierarchiesTracker hiersTracker);
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
	
	public default ColumnContents horizSplit(ColumnContents oldContents, NiCell splitCell, Set<Long> acceptableMainIds, Map<NiDimensionUsage, IdsAcceptor> acceptors) {
		Map<Long, List<NiCell>> z = new HashMap<>();
		for(Long mainId:acceptableMainIds) {
			List<NiCell> oldCells = oldContents.data.get(mainId);
			if (oldCells == null)
				continue;
			for(NiCell oldCell:oldCells) {
				Cell filteredCell = filterCell(acceptors, oldCell, splitCell);
				if (filteredCell != null)
					z.computeIfAbsent(mainId, id -> new ArrayList<>()).add(new NiCell(filteredCell, oldCell.getEntity()));
			}
		}
		return new ColumnContents(z);
	}
	
	public default Cell horizontalReduce(List<NiCell> cells, HierarchiesTracker hierTracker) {
		if (cells == null || cells.isEmpty())
			return getZeroCell();
		return doHorizontalReduce(cells, hierTracker);
	}
	
	/**
	 * computes a "zero" cell, which is different from an "empty" cell. For numerical cells, this is a cell with zero. For textual cells, this is an "" cell, although these do not have 
	 * @return
	 */
	public Cell getZeroCell();
	
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
}
