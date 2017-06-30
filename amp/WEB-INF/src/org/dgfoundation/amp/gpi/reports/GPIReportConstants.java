package org.dgfoundation.amp.gpi.reports;

import java.util.LinkedHashMap;
import java.util.Map;

import org.dgfoundation.amp.ar.ColumnConstants;

public class GPIReportConstants {

	public static final String REPORT_1 = "1";
	public static final String REPORT_5a = "5a";
	public static final String REPORT_5b = "5b";
	public static final String REPORT_6 = "6";
	public static final String REPORT_9b = "9b";
	public static final String REPORT_1_1 = "1";
	public static final String REPORT_1_2 = "1";

	public static final String REPORT_1_GFM_NAME = "Indicator 1";
	public static final String REPORT_5a_GFM_NAME = "Indicator 5a";
	public static final String REPORT_5b_GFM_NAME = "Indicator 5b";
	public static final String REPORT_6_GFM_NAME = "Indicator 6";
	public static final String REPORT_9b_GFM_NAME = "Indicator 9b";

	public static final String COLUMN_YEAR = "Year";
	public static final String COLUMN_REMARK = "Remark";
	public static final String COLUMN_ANNUAL_GOV_BUDGET = "Annual Government Budget";
	public static final String COLUMN_PLANNED_ON_BUDGET = "% of planned on budget";
	public static final String COLUMN_INDICATOR_5B = "Indicator 5b";
	public static final String COLUMN_TOTAL_ACTUAL_DISBURSEMENTS = "Total Actual Disbursements";
	public static final String COLUMN_CONCESSIONAL = "Concessional";
	public static final String COLUMN_DISBURSEMENTS_OTHER_PROVIDERS = "Disbursements through other providers";
	public static final String COLUMN_EXTENT_OF_USE_OF_COUNTRY_RESULT = "Result";
	public static final String COLUMN_EXTENT_OF_USE_OF_GOV_SOURCES = "M&E";

	public static final String HIERARCHY_PARAMETER = "hierarchy";
	public static final String SUMMARY_PARAMETER = "summary";
	public static final String HIERARCHY_DONOR_GROUP = "donor-group";
	public static final String HIERARCHY_DONOR_AGENCY = "donor-agency";

	public static final String GPI_1_Q1 = "Q1";
	public static final String GPI_1_Q2 = "Q2";
	public static final String GPI_1_Q3 = "Q3";
	public static final String GPI_1_Q4 = "Q4";
	public static final String GPI_1_Q5 = "Q5";
	public static final String GPI_1_Q11 = "Q11";
	public static final String GPI_1_Q11a = "Q11a";
	public static final String GPI_1_Q11b = "Q11b";
	public static final String GPI_1_Q11c = "Q11c";

	public static final String GPI_REMARK_ENDPOINT = "/rest/gpi/report/remarks";

	public static final String PDF = "pdf";
	public static final String XLSX = "xlsx";

	public static final Map<String, String> REPORT_1_OUTPUT_1_TOOLTIP = new LinkedHashMap<String, String>() {
		private static final long serialVersionUID = 1773783564463355721L;
		{
			put(GPIReportConstants.GPI_1_Q1, "What is the approved amount for the intervention?");
			put(GPIReportConstants.GPI_1_Q2, "When was the intervention approved?");
			put(GPIReportConstants.GPI_1_Q3, "What is the type of intervention?");
			put(GPIReportConstants.GPI_1_Q4, "Who will lead the implementation of the intervention?");
			put(GPIReportConstants.GPI_1_Q5, "What is the sector that the intervention targets?");
			put(ColumnConstants.GPI_1_Q6,
					"Where have the project’s objectives and topics of intervention been drawn from?");
			put(ColumnConstants.GPI_1_Q7,
					"How many results indicators are included in total in the intervention’s result framework or logical framework?");
			put(ColumnConstants.GPI_1_Q8,
					"How many results indicators draw on results indicators included in existing government results frameworks, plans and strategies?");
			put(ColumnConstants.GPI_1_Q9,
					"How many results indicators will be reported using ongoing sources of information directly provided by existing government monitoring systems or national statistical services?");
			put(ColumnConstants.GPI_1_Q10,
					"Is there an ex-post (final) evaluation planned at project closing to measure the impacts of the intervention?");
			put(GPIReportConstants.COLUMN_EXTENT_OF_USE_OF_COUNTRY_RESULT,
					"Extent of use of county owned result framework or similar planning document");
			put(GPIReportConstants.COLUMN_EXTENT_OF_USE_OF_GOV_SOURCES,
					"Extent of use of Gov’t sources and M&E sytems to track project progress");
		}
	};

}
