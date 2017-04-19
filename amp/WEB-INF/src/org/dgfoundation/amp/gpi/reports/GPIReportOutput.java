package org.dgfoundation.amp.gpi.reports;

import java.util.List;
import java.util.Map;

/**
 * @author Viorel Chihai
 *
 */
public class GPIReportOutput {
	
	/**
	 * Top report headers list
	 */
	protected List<GPIReportOutputColumn> headers;
	
	/**
	 * The report data. Each lines hold information for each {@link GPIReportOutputColumn} column
	 */
	protected List<Map<GPIReportOutputColumn, String>> contents;

	public List<GPIReportOutputColumn> getHeaders() {
		return headers;
	}

	public void setHeaders(List<GPIReportOutputColumn> headers) {
		this.headers = headers;
	}

	public List<Map<GPIReportOutputColumn, String>> getContents() {
		return contents;
	}

	public void setContents(List<Map<GPIReportOutputColumn, String>> contents) {
		this.contents = contents;
	}
	
}
