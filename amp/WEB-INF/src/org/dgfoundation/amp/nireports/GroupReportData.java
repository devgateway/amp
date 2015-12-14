package org.dgfoundation.amp.nireports;

import java.util.ArrayList;
import java.util.List;

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
}
