package org.dgfoundation.amp.gpi.reports.export;

import org.dgfoundation.amp.gpi.reports.GPIReport;

/**
 * @author Viorel Chihai
 *
 */
public interface GPIReportExporter {
	
	public static final String XLSX = "xlsx";
	public static final String PDF = "pdf";
	
	/**
	 * @param report
	 * @return
	 * @throws Exception
	 */
	public abstract byte[] exportReport(GPIReport report) throws Exception;
}
