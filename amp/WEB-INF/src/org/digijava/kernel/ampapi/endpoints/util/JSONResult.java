package org.digijava.kernel.ampapi.endpoints.util;

import org.dgfoundation.amp.newreports.GeneratedReport;

public class JSONResult {
	private ReportMetadata metadata;
	private String mdx = "";
	private GeneratedReport generatedReport;

	public String getMdx() {
		return mdx;
	}

	public void setMdx(String mdx) {
		this.mdx = mdx;
	}

	public ReportMetadata getReportMetadata() {
		return metadata;
	}

	public void setReportMetadata(ReportMetadata metadata) {
		this.metadata = metadata;
	}

	public GeneratedReport getGeneratedReport() {
		return generatedReport;
	}

	public void setGeneratedReport(GeneratedReport generatedReport) {
		this.generatedReport = generatedReport;
	}	
}
