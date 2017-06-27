package org.dgfoundation.amp.gpi.reports.export;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.dgfoundation.amp.ar.ColumnConstants;
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
	
	Map<String, String> INDICATOR_5A_COLUMN_LABELS = Collections.unmodifiableMap(new HashMap<String, String>() {
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
	
	Map<String, String> INDICATOR_1_1_COLUMN_LABELS = Collections.unmodifiableMap(new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		{
			put(GPIReportConstants.COLUMN_YEAR, "Year");
			put(ColumnConstants.DONOR_AGENCY, "Provider Name");
			put(ColumnConstants.PROJECT_TITLE, "Project Title");
			put(GPIReportConstants.GPI_1_Q1, String.format("%s\n%s", GPIReportConstants.GPI_1_Q1, "Project Amount"));
			put(GPIReportConstants.GPI_1_Q2, String.format("%s\n%s", 
					GPIReportConstants.GPI_1_Q2, "Approval date, (month/year)"));
			put(GPIReportConstants.GPI_1_Q3, String.format("%s\n%s", 
					GPIReportConstants.GPI_1_Q3, "Type of Intervention"));
			put(GPIReportConstants.GPI_1_Q4, String.format("%s\n%s", 
					GPIReportConstants.GPI_1_Q4, "Implementing Entity"));
			put(GPIReportConstants.GPI_1_Q5, String.format("%s\n%s", 
					GPIReportConstants.GPI_1_Q5, "What is the sector that the intervention targets?"));
			put(ColumnConstants.GPI_1_Q6, String.format("%s\n%s", ColumnConstants.GPI_1_Q6, 
					"The objective is drawn from government result frame work's or other splanning document"
					+ "\nYes=1, N0=0"));
			put(ColumnConstants.GPI_1_Q6_DESCRIPTION, String.format("%s\n%s", ColumnConstants.GPI_1_Q6, 
					"The objective is drawn from government result frame work's or other planning document"));
			put(ColumnConstants.GPI_1_Q7, String.format("%s\n%s", ColumnConstants.GPI_1_Q7, 
					"Total number of outcome indicators included in the projects result framework"));
			put(ColumnConstants.GPI_1_Q8, String.format("%s\n%s", ColumnConstants.GPI_1_Q8, 
					"Number of outcome indicators drawn from existing Gov’s result framework "
					+ "and/or other planning documents"));
			put(ColumnConstants.GPI_1_Q9, String.format("%s\n%s", ColumnConstants.GPI_1_Q9, 
					"Number of outcome indication to be tracked using Gov’t ongoing statistical data source "
					+ "or M&E system"));
			put(ColumnConstants.GPI_1_Q10, String.format("%s\n%s", ColumnConstants.GPI_1_Q10, 
					"The project plans a final evaluative\nYes=1, N0=0"));
			put(ColumnConstants.GPI_1_Q10_DESCRIPTION, String.format("%s\n%s", ColumnConstants.GPI_1_Q10, 
					"To what extent will the Gov't participate in carrying out the final evaluation?"
					+ "\n(if there is one planned)"));
			put(GPIReportConstants.COLUMN_EXTENT_OF_USE_OF_COUNTRY_RESULT, "Extent of use of country owned result "
					+ "framework or similar planning document\nCalculation = Q8/Q7");
					put(GPIReportConstants.COLUMN_EXTENT_OF_USE_OF_GOV_SOURCES,
							"Extent of use of Gov’t sources and M&E systems"
							+ " to track project progress\nCalculation = Q9/Q7");
			put(GPIReportConstants.GPI_1_Q11, "Supportive Documents");
			put(GPIReportConstants.GPI_1_Q11a, "Electronic link to project document");
			put(GPIReportConstants.GPI_1_Q11b, "Electronic link to gov. planning doc. or results framework "
					+ "used for project design");
			put(GPIReportConstants.GPI_1_Q11c, "Electronic link to gov. existing data source, statistical database "
					+ "or M&E system that will be used to track project progress");
			
		}
	});
	
	Map<String, String> INDICATOR_1_1_SUMMARY_LABELS = Collections.unmodifiableMap(new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		{
			put(GPIReportConstants.GPI_1_Q1, "Overall extent of use existing CRFs");
			put(GPIReportConstants.GPI_1_Q2, "Overall use of country owned Results Frameworks");
			put(GPIReportConstants.GPI_1_Q3, "Overall use of country lead Results Monitoring Frameworks");
			put(GPIReportConstants.GPI_1_Q4, "Overall existence of ex post (final Evaluations)");

		}
	});
	
	/**
	 * @param report
	 * @return
	 * @throws Exception
	 */
	public abstract byte[] exportReport(GPIReport report) throws Exception;
}
