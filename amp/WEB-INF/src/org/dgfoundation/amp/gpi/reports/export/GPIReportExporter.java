package org.dgfoundation.amp.gpi.reports.export;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.gpi.reports.GPIReport;
import org.dgfoundation.amp.gpi.reports.GPIReportConstants;

/**
 * @author Viorel Chihai
 *
 */
public interface GPIReportExporter {
	
	public static final String XLSX = "xlsx";
	public static final String PDF = "pdf";
	
	public static final String INDICATOR_5B_SUMMARY_LABEL = "Indicator 5b % at country level";
	public static final String COLUMN_QUESTION = "Question";
	public static final String COLUMN_VALUE = "Value";
	
	public static final Map<String, String> INDICATOR_5A_COLUMN_LABELS = 
			Collections.unmodifiableMap(new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		{
			put(GPIReportConstants.COLUMN_TOTAL_ACTUAL_DISBURSEMENTS,
					String.format("%s %s", GPIReportConstants.COLUMN_TOTAL_ACTUAL_DISBURSEMENTS, "(Q1)"));
			put(GPIReportConstants.COLUMN_CONCESSIONAL,
					String.format("%s?\n%s", GPIReportConstants.COLUMN_CONCESSIONAL, "(Yes=1 / No=0)"));
			put(MeasureConstants.ACTUAL_DISBURSEMENTS,
					String.format("%s %s", MeasureConstants.ACTUAL_DISBURSEMENTS, "(Q2)"));
			put(MeasureConstants.PLANNED_DISBURSEMENTS,
					String.format("%s %s", MeasureConstants.PLANNED_DISBURSEMENTS, "(Q3)"));
		}
	});
	
	/**
	 * @param report
	 * @return
	 * @throws Exception
	 */
	public abstract byte[] exportReport(GPIReport report) throws Exception;
}
