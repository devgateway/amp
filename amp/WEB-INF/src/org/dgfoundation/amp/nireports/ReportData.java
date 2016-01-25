package org.dgfoundation.amp.nireports;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.dgfoundation.amp.nireports.runtime.CellColumn;
import org.dgfoundation.amp.nireports.runtime.GroupReportData;
import org.dgfoundation.amp.nireports.runtime.HierarchiesTracker;
import org.dgfoundation.amp.nireports.runtime.PerItemHierarchiesTracker;
import org.dgfoundation.amp.nireports.runtime.IdsAcceptorsBuilder;
import org.dgfoundation.amp.nireports.runtime.NiCell;
import org.dgfoundation.amp.nireports.schema.Behaviour;


/**
 * this class is a hybrid between the old-reports-engine ColumnReportData, GroupReportData and will in the end implement the ReportsAPI {@link ReportArea}
 * @author Dolghier Constantin
 *
 */
public abstract class ReportData {
	public final Map<CellColumn, NiCell> trailCells;
	
	/**TODO: maybe turn it into a reference to {@link IdsAcceptorsBuilder} */
	public final NiReportsEngine context;

	/**
	 * the value cell which generated this subreport during horizSplit
	 */
	public final NiCell splitter;
	
	public final HierarchiesTracker hierarchies;

		
	protected ReportData(NiReportsEngine context, NiCell splitter, HierarchiesTracker hierarchies) {
		this.context = context;
		this.trailCells = new HashMap<>();
		this.splitter = splitter;
		this.hierarchies = hierarchies;
	}
	
	/**
	 * recursively split this instance into subinstances. The leaves should generate new leaves, each one of them equaling a given value, governed by the respective leaf's {@link Behaviour}
	 * @param column
	 * @return
	 */
	public abstract GroupReportData horizSplit(CellColumn column);
	public abstract Set<Long> getIds();
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
