package org.digijava.kernel.ampapi.endpoints.util;

public class JSONResult {
	private ReportMetadata metadata;
	private String mdx = "";

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
}
