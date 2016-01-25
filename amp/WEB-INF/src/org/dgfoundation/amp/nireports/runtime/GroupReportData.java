package org.dgfoundation.amp.nireports.runtime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.ReportData;

/**
 * a report containing subreports
 * @author Dolghier Constantin
 *
 */
public class GroupReportData extends ReportData {
	protected final List<ReportData> subreports;
	
	public GroupReportData(NiReportsEngine context, NiCell splitter, HierarchiesTracker hierarchies) {
		super(context, splitter, hierarchies);
		this.subreports = new ArrayList<ReportData>();
	}
	
	@Override
	public boolean isLeaf() {
		return false;
	}

	@Override
	public Set<Long> getIds() {
		Set<Long> res = new HashSet<>();
		for(ReportData rd:subreports)
			res.addAll(rd.getIds());
		return res;
	}

	public void addSubReport(ReportData rd) {
		this.subreports.add(rd);
	}
	
	public List<ReportData> getSubReports() {
		return Collections.unmodifiableList(subreports);
	}
	
	@Override
	public GroupReportData horizSplit(CellColumn column) {
		GroupReportData res = new GroupReportData(this.context, this.splitter, this.hierarchies);
		for(ReportData oldSubReport:subreports) {
			ReportData newSubReport = oldSubReport.horizSplit(column);
			res.subreports.add(newSubReport);
		}
		return res;
	}

	@Override
	public int computeRowSpan(boolean summaryReport) {
		int sum = /*1*/0;
		for(ReportData rd:subreports)
			sum += rd.getRowSpan(summaryReport);
		return sum;
	}
}
