package org.dgfoundation.amp.nireports;

import java.util.ArrayList;
import java.util.List;

public class GroupReportData extends ReportData {
	public final List<ReportData> subreports;

	public GroupReportData(NiReportContext context) {
		super(context);
		this.subreports = new ArrayList<ReportData>();
	}
	
	@Override
	public boolean isLeaf() {
		return true;
	}
}
