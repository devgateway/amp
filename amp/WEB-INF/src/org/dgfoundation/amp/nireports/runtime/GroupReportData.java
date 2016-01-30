package org.dgfoundation.amp.nireports.runtime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

import org.dgfoundation.amp.newreports.ReportCollapsingStrategy;
import org.dgfoundation.amp.nireports.NiReportsEngine;

/**
 * a report containing subreports
 * @author Dolghier Constantin
 *
 */
public class GroupReportData extends ReportData {
	protected final List<ReportData> subreports;
	
	public GroupReportData(NiReportsEngine context, NiCell splitter, List<? extends ReportData> subreports) {
		super(context, splitter);
		this.subreports = Collections.unmodifiableList(new ArrayList<ReportData>(subreports));
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
		return subreports;
	}
	
	@Override
	public GroupReportData horizSplit(CellColumn column) {
		GroupReportData res = this.clone(subreports.stream().map(z -> z.horizSplit(column)).collect(toList()));
		return res;
	}

	@Override
	public <K> K accept(ReportDataVisitor<K> visitor) {
		K res = visitor.visitGroup(this);
		return res;
	}
}
