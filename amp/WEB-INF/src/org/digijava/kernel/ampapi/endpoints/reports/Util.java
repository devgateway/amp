package org.digijava.kernel.ampapi.endpoints.reports;

import org.digijava.module.aim.dbentity.AmpReports;

public class Util {

	public static JSONTab convert(AmpReports report, Boolean visible) {
		JSONTab tab = new JSONTab();
		tab.setId(report.getAmpReportId());
		tab.setName(report.getName());
		tab.setVisible(visible);
		return tab;
	}

}
