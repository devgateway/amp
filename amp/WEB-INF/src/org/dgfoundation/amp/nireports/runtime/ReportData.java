package org.dgfoundation.amp.nireports.runtime;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.schema.Behaviour;


/**
 * this class is a hybrid between the old-reports-engine ColumnReportData, GroupReportData and will in the end implement the ReportsAPI {@link ReportArea}
 * @author Dolghier Constantin
 *
 */
public abstract class ReportData {
	public final Map<CellColumn, NiCell> trailCells = new HashMap<>();
	
	/**TODO: maybe turn it into a reference to {@link IdsAcceptorsBuilder} */
	public final NiReportsEngine context;

	/**
	 * the value cell which generated this subreport during horizSplit
	 */
	public final NiCell splitter;
	
	public final HierarchiesTracker hierarchies;

	protected ReportData(NiReportsEngine context, NiCell splitter, HierarchiesTracker hierarchies) {
		this.context = context;
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
	public abstract<K> K accept(ReportDataVisitor<K> visitor);
}
