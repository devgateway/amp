package org.dgfoundation.amp.nireports.output;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.dgfoundation.amp.nireports.runtime.CellColumn;
import org.dgfoundation.amp.nireports.runtime.ReportData;

/**
 * a flattened report output subregion (see {@link ReportData})
 * @author Dolghier Constantin
 *
 */
public abstract class NiReportData {
	public final Map<CellColumn, NiOutCell> trailCells;
	public final NiSplitCell splitter;
	public final Set<Long> ids;
	
	protected NiReportData(Map<CellColumn, NiOutCell> trailCells, Set<Long> ids, NiSplitCell splitter) {
		this.trailCells = Collections.unmodifiableMap(trailCells);
		this.splitter = splitter;
		this.ids = Collections.unmodifiableSet(ids);
	}
	
	public Set<Long> getIds() {
		return ids;
	}
	
	public abstract boolean isLeaf();
	/** function will only be called once per instance and the value of the argument will not change */
	protected abstract int computeRowSpan(boolean summaryReport);
	
	protected int _rowSpan = -1;
	
	/** computes the rowspan  */
	public int getRowSpan(boolean summaryReport) {
		if (_rowSpan < 0) {
			_rowSpan = computeRowSpan(summaryReport);
		}
		return _rowSpan;
	}
}
