package org.dgfoundation.amp.gpi.reports;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

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
    public static final String COLUMN_ANNUAL_GOV_BUDGET = "Funds Recorded in Government Annual Budget";
    public static final String COLUMN_PLANNED_ON_BUDGET = "% of planned on budget";
    public static final String COLUMN_INDICATOR_5B = "Indicator 5b";
    public static final String COLUMN_TOTAL_ACTUAL_DISBURSEMENTS = "Total Actual Disbursements";
    public static final String COLUMN_CONCESSIONAL = "Concessional";
    public static final String COLUMN_DISBURSEMENTS_OTHER_PROVIDERS = "Disbursements through other providers";
    public static final String COLUMN_EXTENT_OF_USE_OF_COUNTRY_RESULT = "Result";
    public static final String COLUMN_EXTENT_OF_USE_OF_GOV_SOURCES = "M&E";
    public static final String COLUMN_USE_OF_COUNTRY_SYSTEMS = "Use of country systems";
    public static final String COLUMN_REMARKS_COUNT = "Number of Remarks";
    
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

    public static final String YEAR_1 = "Year_1";
    public static final String YEAR_2 = "Year_2";
    public static final String YEAR_3 = "Year_3";

    public static final String GPI_REMARK_ENDPOINT = "/rest/gpi/report/remarks";

    public static final String INDICATOR1_EXPORT_APPROVAL_DATE_FORMAT = "MM/yyyy";
    public static final int ETHIOPIAN_FORMATTED_DATE_DAYS_OFFSET = 3;

    public static final Map<String, String> REPORT_1_OUTPUT_1_TOOLTIP = new LinkedHashMap<String, String>() {
        private static final long serialVersionUID = 1773783564463355721L;
        {
            put(GPIReportConstants.GPI_1_Q1, "What is the approved amount for the intervention?");
            put(GPIReportConstants.GPI_1_Q2, "When was the intervention approved?");
            put(GPIReportConstants.GPI_1_Q3, "What is the type of intervention?");
            put(GPIReportConstants.GPI_1_Q4, "Who will lead the implementation of the intervention?");
            put(GPIReportConstants.GPI_1_Q5, "What is the sector that the intervention targets?");
            put(ColumnConstants.GPI_1_Q6,
                    "Is the objective drawn from government result framework/s or other planning document?");
            put(ColumnConstants.GPI_1_Q7,
                    "How many results indicators are included in total in the intervention’s result framework "
                            + "or logical framework?");
            put(ColumnConstants.GPI_1_Q8,
                    "How many results indicators draw on results indicators included in existing government "
                            + "results frameworks, plans and strategies?");
            put(ColumnConstants.GPI_1_Q9,
                    "How many results indicators will be reported using ongoing sources of information directly "
                            + "provided by existing government monitoring systems or national statistical services?");
            put(ColumnConstants.GPI_1_Q10,
                    "Is there an ex-post (final) evaluation planned at project closing to measure the impacts of "
                            + "the intervention?");
            put(GPIReportConstants.COLUMN_EXTENT_OF_USE_OF_COUNTRY_RESULT,
                    "Extent of use of country owned result framework or similar planning document");
            put(GPIReportConstants.COLUMN_EXTENT_OF_USE_OF_GOV_SOURCES,
                    "Extent of use of Gov’t sources and M&E sytems to track project progress");

        }
    };
    public static final Map<String, String> REPORT_5A_TOOLTIP = new LinkedHashMap<String, String>() {
        private static final long serialVersionUID = 1773783564463355721L;
        {
            put(GPIReportConstants.COLUMN_CONCESSIONAL, "(Yes=1/ No=0)");
            put(GPIReportConstants.COLUMN_DISBURSEMENTS_OTHER_PROVIDERS,
                    "Total Actual Disbursements for on-budget projects which have an Executing Agency listed");
            put(MeasureConstants.DISBURSED_AS_SCHEDULED,
                    "% of development cooperation funding for the government sector disbursed in the year for "
                            + "which it was scheduled by providers of development co-operation");
            put(MeasureConstants.OVER_DISBURSED,
                    "% of development cooperation funding for the government sector over-disbursed in the year "
                            + "for which it was scheduled by providers of development co-operation");
        }
    };
    public static final Map<String, String> REPORT_5B_TOOLTIP = new LinkedHashMap<String, String>() {
        private static final long serialVersionUID = 1773783564463355721L;
        {
            put(GPIReportConstants.YEAR_1,
                    "Has the provider of development co-operation made available a comprehensive forward expenditure "
                            + "and/or implementation plan setting out expected development co-operation flows in the "
                            + "selected year? (Yes=1; No=0)");
            put(GPIReportConstants.YEAR_2,
                    "Has the provider of development co-operation made available a comprehensive forward expenditure "
                            + "and/or implementation plan setting out expected development co-operation flows in the "
                            + "selected year? (Yes=1; No=0)");
            put(GPIReportConstants.YEAR_3,
                    "Has the provider of development co-operation made available a comprehensive forward expenditure "
                            + "and/or implementation plan setting out expected development co-operation flows in "
                            + "the selected year? (Yes=1; No=0)");
            put(GPIReportConstants.COLUMN_INDICATOR_5B, "Average result of the next three years.");
        }
    };
    public static final Map<String, String> REPORT_6_TOOLTIP = new LinkedHashMap<String, String>() {
        private static final long serialVersionUID = 1773783564463355721L;
        {
            put(GPIReportConstants.COLUMN_ANNUAL_GOV_BUDGET,
                    "How much estimated development co-operation funding was recorded in the annual"
                            + " budget of the reporting year of reference as grants, revenue or loans "
                            + "(concessional and non-concessional)?");
            put(GPIReportConstants.COLUMN_PLANNED_ON_BUDGET,
                    "% of development co-operation funding scheduled for disbursement that is recorded "
                            + "in the annual budgets approved by the legislatures of developing countries");
        }
    };
    public static final Map<String, String> REPORT_9_TOOLTIP = new LinkedHashMap<String, String>() {
        private static final long serialVersionUID = 1773783564463355721L;
        {
            put(MeasureConstants.NATIONAL_BUDGET_EXECUTION_PROCEDURES,
                    "In the reporting year of reference, how much development co-operation funding "
                    + "disbursed for the government sector used national budget execution procedures?");
            put(MeasureConstants.NATIONAL_FINANCIAL_REPORTING_PROCEDURES,
                    "In the reporting year of reference, how much development co-operation funding disbursed "
                    + "for the government sector used national financial reporting procedures?");
            put(MeasureConstants.NATIONAL_AUDITING_PROCEDURES,
                    "In the reporting year of reference, how much development co-operation funding disbursed "
                    + "for the government sector used national auditing procedures?");
            put(MeasureConstants.NATIONAL_PROCUREMENT_EXECUTION_PROCEDURES,
                    "In the reporting year of reference, how much development co-operation funding disbursed "
                    + "for the government sector used national procurement systems?");
        }
    };
    
    public static final Map<String, String> INDICATOR_1_1_COLUMN_LABELS = 
            Collections.unmodifiableMap(new HashMap<String, String>() {
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
    
    public static final Map<String, String> INDICATOR_1_1_SUMMARY_LABELS = 
            Collections.unmodifiableMap(new HashMap<String, String>() {
        private static final long serialVersionUID = 1L;
        {
            put(GPIReportConstants.GPI_1_Q1, "Overall extent of use existing CRFs");
            put(GPIReportConstants.GPI_1_Q2, "Overall use of country owned Results Frameworks");
            put(GPIReportConstants.GPI_1_Q3, "Overall use of country lead Results Monitoring Frameworks");
            put(GPIReportConstants.GPI_1_Q4, "Overall existence of ex post (final Evaluations)");

        }
    });
    
    public static final Map<String, String> INDICATOR_5A_COLUMN_LABELS = 
            Collections.unmodifiableMap(new HashMap<String, String>() {
        private static final long serialVersionUID = 1L;
        {
            put(GPIReportConstants.COLUMN_TOTAL_ACTUAL_DISBURSEMENTS,
                    String.format("%s %s", "Total Disbursements", "(Q1)"));
            put(GPIReportConstants.COLUMN_CONCESSIONAL,
                    String.format("%s?\n%s", GPIReportConstants.COLUMN_CONCESSIONAL, "(Yes=1 / No=0)"));
            put(MeasureConstants.ACTUAL_DISBURSEMENTS,
                    String.format("%s %s", "Actual Disbursements for government sector", "(Q2)"));
            put(MeasureConstants.PLANNED_DISBURSEMENTS,
                    String.format("%s %s", "Scheduled Disbursements for government sector", "(Q3)"));
            put(MeasureConstants.DISBURSED_AS_SCHEDULED, "% " + MeasureConstants.DISBURSED_AS_SCHEDULED);
            put(MeasureConstants.OVER_DISBURSED, "% " + MeasureConstants.OVER_DISBURSED);
        }
    });
    
}
