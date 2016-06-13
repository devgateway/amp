package org.dgfoundation.amp.reports.saiku.export;

import org.apache.poi.ss.usermodel.Workbook;
import org.dgfoundation.amp.newreports.GeneratedReport;

/**
 * @author Viorel Chihai
 *
 */
public interface SaikuReportExporter {
	
	public static final String PDF = "pdf";
	public static final String CSV = "csv";
	public static final String XLSX = "xlsx";
	public static final String XLSX_PLAIN = "xlsx_plain";
	
	/**
	 * @param report
	 * @param dualReport
	 * @return
	 * @throws Exception
	 */
	public abstract byte[] exportReport(GeneratedReport report, GeneratedReport dualReport) throws Exception;
	
}
