package org.dgfoundation.amp.nireports.schema;

import java.util.List;

import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.DatedCell;
import org.dgfoundation.amp.nireports.runtime.NiCell;

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
	public Cell doHorizontalReduce(List<NiCell> cells);
	
	public default Cell horizontalReduce(List<NiCell> cells) {
		if (cells == null || cells.isEmpty())
			return getZeroCell();
		return doHorizontalReduce(cells);
	}
	
	/**
	 * computes a "zero" cell, which is different from an "empty" cell. For numerical cells, this is a cell with zero. For textual cells, this is an "" cell, although these do not have 
	 * @return
	 */
	public Cell getZeroCell();
	
	public default String getDebugDigest() {
		return String.format("%s", getTimeRange());
	}
}
