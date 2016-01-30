package org.dgfoundation.amp.nireports.runtime;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dgfoundation.amp.newreports.ReportCollapsingStrategy;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.schema.Behaviour;


/**
 * this class is a hybrid between the old-reports-engine ColumnReportData, GroupReportData and will in the end implement the ReportsAPI {@link ReportArea}
 * @author Dolghier Constantin
 *
 */
public abstract class ReportData {
	/**TODO: maybe turn it into a reference to {@link IdsAcceptorsBuilder} */
	public final NiReportsEngine context;

	/**
	 * the value cell which generated this subreport during horizSplit
	 */
	public final NiCell splitter;

	protected ReportData(NiReportsEngine context, NiCell splitter) {
		this.context = context;
		this.splitter = splitter;
	}
	
	public GroupReportData clone(List<? extends ReportData> children) {
		GroupReportData res = new GroupReportData(context, splitter, children);
		return res;
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
		
	@Override
	public String toString() {
		return String.format("%s: %s (id: %d)", this.getClass().getSimpleName(), this.splitter == null ? null : this.splitter.getDisplayedValue(), this.splitter == null ? -1 : this.splitter.cell.entityId);
	}
}
