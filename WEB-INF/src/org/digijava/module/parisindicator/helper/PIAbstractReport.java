package org.digijava.module.parisindicator.helper;

import java.util.Collection;

public abstract class PIAbstractReport implements PIOperations {

	/*
	 * This field is changed by each report.
	 */
	private final String reportCode = "";
	
	/*
	 * This collection has all the rows to generate the report.
	 */
	private Collection<PIReportAbstractRow> reportRows;

	public String getReportCode() {
		return reportCode;
	}

	public Collection<PIReportAbstractRow> getReportRows() {
		return reportRows;
	}

	public void setReportRows(Collection<PIReportAbstractRow> reportRows) {
		this.reportRows = reportRows;
	}
}
