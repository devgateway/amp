package org.dgfoundation.amp.nireports.runtime;

import java.util.ArrayList;
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
	public final List<ReportData> subreports;

	public GroupReportData(NiReportsEngine context) {
		super(context);
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
}
