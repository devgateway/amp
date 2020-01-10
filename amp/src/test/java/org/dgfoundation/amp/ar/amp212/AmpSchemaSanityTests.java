package org.dgfoundation.amp.ar.amp212;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.dgfoundation.amp.test.categories.DatabaseTests;
import org.dgfoundation.amp.StandaloneAMPInitializer;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.newreports.ReportAreaForTests;
import org.dgfoundation.amp.newreports.AmountsUnits;
import org.dgfoundation.amp.newreports.AreaOwner;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.nireports.GrandTotalsDigest;
import org.dgfoundation.amp.nireports.TestcasesReportsSchema;
import org.dgfoundation.amp.nireports.amp.AmpReportsScratchpad;
import org.dgfoundation.amp.nireports.output.NiReportExecutor;
import org.dgfoundation.amp.nireports.testcases.NiReportModel;
import org.dgfoundation.amp.nireports.testcases.ReportModelGenerator;
import org.dgfoundation.amp.nireports.testcases.generic.HardcodedActivities;
import org.digijava.module.aim.util.DbUtil;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * 
 * testcases for the fetching states of AMP + the AMP schema
 * 
 * @author Constantin Dolghier
 *
 */
@Category(DatabaseTests.class)
public class AmpSchemaSanityTests extends BasicSanityChecks {

    final List<String> mtefActs = Arrays.asList(
        "mtef activity 1",
        "mtef activity 2",
        "Pure MTEF Project",
        "activity with MTEFs",
        "Activity with both MTEFs and Act.Comms",
        "activity with many MTEFs",
        "Test MTEF directed",
        "activity with pipeline MTEFs and act. disb"
    );
    
    final List<String> ppcActs = Arrays.asList(
            "Proposed Project Cost 1 - USD",
            "Proposed Project Cost 2 - EUR",
            "SubNational no percentages",
            "Activity with primary_tertiary_program",
            "activity with primary_program",
            "activity with tertiary_program",
            "activity 1 with agreement",
            "activity with directed MTEFs"
        );
    
    final List<String> humanitarianAidActs = Arrays.asList(
            "TAC_activity_1", 
            "crazy funding 1", 
            "date-filters-activity", 
            "Activity with planned disbursements", 
            "TAC_activity_2", 
            "pledged 2"
        );
    
    final List<String> sscActs = Arrays.asList(
        "Real SSC Activity 1",
        "Real SSC Activity 2"
    );

    final static GrandTotalsDigest proposedProjectCostDigester = new GrandTotalsDigest(z -> z.equals("RAW / Proposed Project Amount") || z.startsWith("RAW / Revised Project Amount"));
    final static String correctTotalsPPC = "{RAW / Proposed Project Amount=5096901.715878, RAW / Revised Project Amount=4412539.842263}";
    
    @Override
    protected NiReportExecutor getNiExecutor(List<String> activityNames) {
        return getDbExecutor(activityNames);
    }

    @BeforeClass
    public static void setUp() {
        StandaloneAMPInitializer.initialize();
    }

    @Test
    public void testHierarchiesWithEverything() {       
        List<String> columns = Arrays.asList(ColumnConstants.AC_CHAPTER, ColumnConstants.IMPLEMENTATION_LEVEL, ColumnConstants.IMPLEMENTATION_LOCATION, 
                ColumnConstants.ACCESSION_INSTRUMENT, ColumnConstants.STATUS, ColumnConstants.TYPE_OF_ASSISTANCE, 
                ColumnConstants.FINANCING_INSTRUMENT, ColumnConstants.DONOR_TYPE, ColumnConstants.CREDIT_DONATION, 
                ColumnConstants.INSTITUTIONS, ColumnConstants.COMPONENT_TYPE, ColumnConstants.ACTIVITY_CREATED_BY, 
                ColumnConstants.PROJECT_CATEGORY, ColumnConstants.FUNDING_STATUS, ColumnConstants.MODE_OF_PAYMENT,
                ColumnConstants.PAYMENT_CAPITAL___RECURRENT, ColumnConstants.BUDGET_DEPARTMENT, ColumnConstants.BUDGET_ORGANIZATION,
                ColumnConstants.BUDGET_SECTOR, ColumnConstants.BUDGET_PROGRAM, ColumnConstants.GOVERNMENT_APPROVAL_PROCEDURES, 
                ColumnConstants.JOINT_CRITERIA, ColumnConstants.ACTIVITY_BUDGET, ColumnConstants.MULTI_DONOR,
                ColumnConstants.AGREEMENT_TITLE_CODE, ColumnConstants.AGREEMENT_CODE, ColumnConstants.PROJECT_IMPLEMENTING_UNIT,
                ColumnConstants.TYPE_OF_COOPERATION, ColumnConstants.TYPE_OF_IMPLEMENTATION, ColumnConstants.MODALITIES, 
                ColumnConstants.BUDGET_STRUCTURE, ColumnConstants.INDIRECT_ON_BUDGET, ColumnConstants.HUMANITARIAN_AID,
                ColumnConstants.DISASTER_RESPONSE_MARKER);
        
        buildDigest(
            buildSpecification("testcase with all unusual hierarchies", 
                columns, 
                Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS), 
                columns, 
                GroupingCriteria.GROUPING_TOTALS_ONLY), 
            new HardcodedActivities().getActNamesList(),
            new ReportModelGenerator());
    }

    @Test
    public void testAllMeasures() {
        List<String> columns = Arrays.asList(ColumnConstants.PROJECT_TITLE);

        List<String> measures = Arrays.asList(
                MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_ARREARS, MeasureConstants.ACTUAL_DISBURSEMENT_ORDERS,
                MeasureConstants.ACTUAL_DISBURSEMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS_CAPITAL, MeasureConstants.ACTUAL_DISBURSEMENTS_RECURRENT,
                MeasureConstants.ACTUAL_EXPENDITURES, MeasureConstants.BILATERAL_SSC_COMMITMENTS,
                MeasureConstants.CUMULATED_DISBURSEMENTS, MeasureConstants.CUMULATIVE_COMMITMENT, MeasureConstants.CUMULATIVE_DISBURSEMENT,
                MeasureConstants.CUMULATED_SSC_COMMITMENTS, MeasureConstants.OFFICIAL_DEVELOPMENT_AID_COMMITMENTS,
                MeasureConstants.PIPELINE_COMMITMENTS, MeasureConstants.PLANNED_COMMITMENTS, MeasureConstants.PLANNED_DISBURSEMENT_ORDERS,
                MeasureConstants.PLANNED_DISBURSEMENTS, MeasureConstants.PLANNED_ARREARS, MeasureConstants.PLANNED_DISBURSEMENTS_CAPITAL,
                MeasureConstants.PLANNED_DISBURSEMENTS_EXPENDITURE, MeasureConstants.PLANNED_EXPENDITURES, MeasureConstants.PREVIOUS_MONTH_DISBURSEMENTS,
                MeasureConstants.CURRENT_MONTH_DISBURSEMENTS, MeasureConstants.PRIOR_ACTUAL_DISBURSEMENTS, MeasureConstants.SELECTED_YEAR_PLANNED_DISBURSEMENTS,
                MeasureConstants.TRIANGULAR_SSC_COMMITMENTS, MeasureConstants.UNDISBURSED_BALANCE, MeasureConstants.REAL_PLANNED_DISBURSEMENTS,
                MeasureConstants.REAL_DISBURSEMENTS, MeasureConstants.REAL_COMMITMENTS, MeasureConstants.REAL_MTEFS,
                MeasureConstants.PERCENTAGE_OF_TOTAL_COMMITMENTS, MeasureConstants.PERCENTAGE_OF_TOTAL_DISBURSEMENTS, MeasureConstants.LAST_YEAR_OF_PLANNED_DISBURSEMENTS,
                MeasureConstants.PERCENTAGE_OF_DISBURSEMENT, MeasureConstants.ACTUAL_CLASSIFIED_EXPENDITURES, MeasureConstants.PLANNED_CLASSIFIED_EXPENDITURES,
                MeasureConstants.UNCOMMITTED_CUMULATIVE_BALANCE, MeasureConstants.UNDISBURSED_CUMULATIVE_BALANCE, MeasureConstants.ANNUAL_PROPOSED_PROJECT_COST,
                MeasureConstants.PROPOSED_PROJECT_AMOUNT_PER_PROJECT, MeasureConstants.ACTUAL_ESTIMATED_DISBURSEMENTS, MeasureConstants.ACTUAL_RELEASE_OF_FUNDS,
                MeasureConstants.PIPELINE_ESTIMATED_DISBURSEMENTS, MeasureConstants.PIPELINE_RELEASE_OF_FUNDS, MeasureConstants.PLANNED_ESTIMATED_DISBURSEMENTS,
                MeasureConstants.PLANNED_RELEASE_OF_FUNDS, MeasureConstants.PIPELINE_MTEF_PROJECTIONS, MeasureConstants.PROJECTION_MTEF_PROJECTIONS,
                MeasureConstants.VARIANCE_OF_COMMITMENTS, MeasureConstants.VARIANCE_OF_DISBURSEMENTS, MeasureConstants.AVERAGE_SIZE_DISBURSEMENTS,
                MeasureConstants.PLEDGES_ACTUAL_COMMITMENTS, MeasureConstants.PLEDGES_ACTUAL_DISBURSEMENTS, MeasureConstants.PLEDGES_ACTUAL_PLEDGE,
                MeasureConstants.PLEDGES_COMMITMENT_GAP, MeasureConstants.PLEDGES_PERCENTAGE_OF_DISBURSEMENT, MeasureConstants.PLEDGES_PLANNED_COMMITMENTS,
                MeasureConstants.PLEDGES_PLANNED_DISBURSEMENTS, MeasureConstants.MTEF_PROJECTIONS, MeasureConstants.EXECUTION_RATE,
                MeasureConstants.PREDICTABILITY_OF_FUNDING, MeasureConstants.CUMULATIVE_EXECUTION_RATE, MeasureConstants.AVERAGE_DISBURSEMENT_RATE,
                MeasureConstants.FORECAST_EXECUTION_RATE, MeasureConstants.ALWAYS_PRESENT);

        buildDigest(
                buildSpecification("testcase with all unusual measures",
                        columns,
                        measures,
                        columns,
                        GroupingCriteria.GROUPING_MONTHLY),
                new HardcodedActivities().getActNamesList(),
                new ReportModelGenerator());
    }
    
    @Test
    public void testActivityIds() {
        NiReportModel cor = new NiReportModel("testcase amp activity ids")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 0, colSpan: 4))",
                        "(Activity Id: (startRow: 1, rowSpan: 2, totalRowSpan: 2, colStart: 0, colSpan: 1));(Project Title: (startRow: 1, rowSpan: 2, totalRowSpan: 2, colStart: 1, colSpan: 1));(Totals: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 2))",
                        "(Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1))"))
                    .withWarnings(Arrays.asList())
                    .withBody(      new ReportAreaForTests(null)
                      .withContents("Activity Id", "", "Project Title", "", "Totals-Actual Commitments", "1,011,456", "Totals-Actual Disbursements", "0")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(19), "Activity Id", "19", "Project Title", "Pure MTEF Project"),
                        new ReportAreaForTests(new AreaOwner(70), "Activity Id", "70", "Project Title", "Activity with both MTEFs and Act.Comms", "Totals-Actual Commitments", "888,000"),
                        new ReportAreaForTests(new AreaOwner(73), "Activity Id", "73", "Project Title", "activity with directed MTEFs", "Totals-Actual Commitments", "123,456")      ));
        runNiTestCase(
                buildSpecification("testcase amp activity ids", 
                        Arrays.asList(ColumnConstants.ACTIVITY_ID, ColumnConstants.PROJECT_TITLE), 
                        Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS), 
                        null, GroupingCriteria.GROUPING_TOTALS_ONLY),
                "en", 
                Arrays.asList("Pure MTEF Project", "activity with directed MTEFs", "Activity with both MTEFs and Act.Comms"),
                cor);
    }

    /**
     * generates reports with many hierarchies and checks that, for any of them, the totals do not change
     * @throws Exception
     */
    @Test
    public void testProposedProjectCostDoesNotChangeTotals() throws Exception {
        
        List<String> allActs = new ArrayList<>();
        allActs.addAll(acts);
        allActs.addAll(sscActs);

        ReportSpecificationImpl initSpec = buildSpecification("initSpec",
                Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.PROPOSED_PROJECT_AMOUNT, ColumnConstants.REVISED_PROJECT_AMOUNT), 
                Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS), 
                null, 
                GroupingCriteria.GROUPING_YEARLY);
                
        assertEquals(correctTotalsPPC, buildDigest(initSpec, allActs, proposedProjectCostDigester).toString());

        // single-hierarchy reports
        for(boolean isSummary:Arrays.asList(true, false)) {
            for (String hierName : DONOR_HIERARCHIES_TO_TRY) {
                ReportSpecificationImpl spec = buildSpecification(String.format("%s summary: %b", hierName, isSummary), 
                        Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.PROPOSED_PROJECT_AMOUNT, ColumnConstants.REVISED_PROJECT_AMOUNT, hierName), 
                        Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS), 
                        Arrays.asList(hierName), 
                        GroupingCriteria.GROUPING_YEARLY);
                spec.setSummaryReport(isSummary);
                assertEquals(spec.getReportName(), correctTotalsPPC, buildDigest(spec, allActs, proposedProjectCostDigester).toString());
            }
        }
    }
    
    @Test
    public void testRawLocations() {
        NiReportModel cor = new NiReportModel("testcase raw locations (for pp), hier")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 0, colSpan: 3))",
                        "(Location: (startRow: 1, rowSpan: 2, totalRowSpan: 2, colStart: 0, colSpan: 1));(Project Title: (startRow: 1, rowSpan: 2, totalRowSpan: 2, colStart: 1, colSpan: 1));(Totals: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 1))",
                        "(Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1))"))
                    .withWarnings(Arrays.asList())
                    .withBody(      new ReportAreaForTests(null)
                      .withContents("Location", "", "Project Title", "", "Totals-Actual Commitments", "1,098,173,58")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner("Location", "Anenii Noi County", 9085)).withContents("Project Title", "", "Totals-Actual Commitments", "111,333", "Location", "Anenii Noi County")
                        .withChildren(
                          new ReportAreaForTests(new AreaOwner(30), "Project Title", "SSC Project 1", "Totals-Actual Commitments", "111,333")        ),
                        new ReportAreaForTests(new AreaOwner("Location", "Apareni", 9113)).withContents("Project Title", "", "Totals-Actual Commitments", "53,262,32", "Location", "Apareni")
                        .withChildren(
                          new ReportAreaForTests(new AreaOwner(52), "Project Title", "activity with contracting agency", "Totals-Actual Commitments", "53,262,32")        ),
                        new ReportAreaForTests(new AreaOwner("Location", "Dolboaca", 9110)).withContents("Project Title", "", "Totals-Actual Commitments", "178,000", "Location", "Dolboaca")
                        .withChildren(
                          new ReportAreaForTests(new AreaOwner(36), "Project Title", "Activity With Zones and Percentages", "Totals-Actual Commitments", "178,000")        ),
                        new ReportAreaForTests(new AreaOwner("Location", "Glodeni", 9111)).withContents("Project Title", "", "Totals-Actual Commitments", "712,000", "Location", "Glodeni")
                        .withChildren(
                          new ReportAreaForTests(new AreaOwner(36), "Project Title", "Activity With Zones and Percentages", "Totals-Actual Commitments", "712,000")        ),
                        new ReportAreaForTests(new AreaOwner("Location", "Slobozia", 9115)).withContents("Project Title", "", "Totals-Actual Commitments", "43,578,26", "Location", "Slobozia")
                        .withChildren(
                          new ReportAreaForTests(new AreaOwner(52), "Project Title", "activity with contracting agency", "Totals-Actual Commitments", "43,578,26")        )      ));
        runNiTestCase(
                buildSpecification("testcase raw locations (for pp), hier", 
                        Arrays.asList(ColumnConstants.LOCATION, ColumnConstants.PROJECT_TITLE), 
                        Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS), 
                        Arrays.asList(ColumnConstants.LOCATION), 
                        GroupingCriteria.GROUPING_TOTALS_ONLY),
                "en", 
                Arrays.asList("SSC Project 1", "activity with contracting agency", "Activity With Zones and Percentages"),
                cor);
        cor = new NiReportModel("testcase raw locations (for pp), flat")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 0, colSpan: 3))",
                        "(Location: (startRow: 1, rowSpan: 2, totalRowSpan: 2, colStart: 0, colSpan: 1));(Project Title: (startRow: 1, rowSpan: 2, totalRowSpan: 2, colStart: 1, colSpan: 1));(Totals: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 1))",
                        "(Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1))"))
                    .withWarnings(Arrays.asList())
                    .withBody(      new ReportAreaForTests(null)
                      .withContents("Location", "", "Project Title", "", "Totals-Actual Commitments", "1,098,173,58")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(30), "Location", "Anenii Noi County", "Project Title", "SSC Project 1", "Totals-Actual Commitments", "111,333"),
                        new ReportAreaForTests(new AreaOwner(36), "Location", "Dolboaca, Glodeni", "Project Title", "Activity With Zones and Percentages", "Totals-Actual Commitments", "890,000"),
                        new ReportAreaForTests(new AreaOwner(52), "Location", "Apareni, Slobozia", "Project Title", "activity with contracting agency", "Totals-Actual Commitments", "96,840,58") ));
        runNiTestCase(
                buildSpecification("testcase raw locations (for pp), flat", 
                        Arrays.asList(ColumnConstants.LOCATION, ColumnConstants.PROJECT_TITLE), 
                        Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS), 
                        null, 
                        GroupingCriteria.GROUPING_TOTALS_ONLY),
                "en", 
                Arrays.asList("SSC Project 1", "activity with contracting agency", "Activity With Zones and Percentages"),
                cor);
    }
    
    @Test
    public void testProjectImplementationDelay() {
        NiReportModel cor = new NiReportModel("testcase for Project Implementation Delay")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 3))",
                        "(Project Implementation Delay: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 2, colSpan: 1))",
                        "",
                        "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1))"))
                    .withWarnings(Arrays.asList())
                    .withBody(      new ReportAreaForTests(null)
                      .withContents("Project Implementation Delay", "", "Project Title", "", "Totals-Actual Commitments", "0")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(81), "Project Implementation Delay", "20 days", "Project Title", "PID: original, proposed, actual"),
                        new ReportAreaForTests(new AreaOwner(82), "Project Implementation Delay", "6 years 21 days", "Project Title", "PID: original, actual"),
                        new ReportAreaForTests(new AreaOwner(83), "Project Title", "PID: original > actual"),
                        //new ReportAreaForTests(new AreaOwner(84), "Project Implementation Delay", "1 month 23 days", "Project Title", "PID: original"),
                        new ReportAreaForTests(new AreaOwner(85), "Project Implementation Delay", "20 days", "Project Title", "PID: original, proposed")      ));
        AmpReportsScratchpad.forcedNowDate = LocalDate.of(2016, 5, 3);

        runNiTestCase(
                buildSpecification("testcase for Project Implementation Delay", 
                        Arrays.asList(ColumnConstants.PROJECT_IMPLEMENTATION_DELAY, ColumnConstants.PROJECT_TITLE), 
                        Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS), 
                        null, GroupingCriteria.GROUPING_YEARLY),
                "en", 
                Arrays.asList("PID: original, proposed, actual", "PID: original, actual", "PID: original > actual", /*"PID: original", */"PID: original, proposed"),
                cor);
        AmpReportsScratchpad.forcedNowDate = null;
    }

    @Test
    public void testDepartmentDivisions() {
        NiReportModel cor = new NiReportModel("testcase for Department Division")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 6))",
                        "(Beneficiary Agency: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Beneficiary Agency  Department/Division: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1));(Implementing Agency Department/Division: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 3, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 4, colSpan: 1));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 5, colSpan: 1))",
                        "(2016: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 1))",
                        "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1))"))
                    .withWarnings(Arrays.asList())
                    .withBody(      new ReportAreaForTests(null).withContents("Beneficiary Agency", "", "Project Title", "", "Beneficiary Agency  Department/Division", "", "Implementing Agency Department/Division", "", "Funding-2016-Actual Commitments", "333,333", "Totals-Actual Commitments", "333,333")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner("Beneficiary Agency", "Norway", 21694)).withContents("Project Title", "", "Beneficiary Agency  Department/Division", "", "Implementing Agency Department/Division", "", "Funding-2016-Actual Commitments", "333,333", "Totals-Actual Commitments", "333,333", "Beneficiary Agency", "Norway")
                        .withChildren(
                          new ReportAreaForTests(new AreaOwner(90), "Project Title", "department/division", "Beneficiary Agency  Department/Division", "norway benef dep", "Implementing Agency Department/Division", "minfin impl ag dep", "Funding-2016-Actual Commitments", "333,333", "Totals-Actual Commitments", "333,333")        )      ));
        
        runNiTestCase(
                buildSpecification("testcase for Department Division", 
                        Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.BENEFICIARY_AGENCY__DEPARTMENT_DIVISION, ColumnConstants.IMPLEMENTING_AGENCY_DEPARTMENT_DIVISION, ColumnConstants.BENEFICIARY_AGENCY), 
                        Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS), 
                        Arrays.asList(ColumnConstants.BENEFICIARY_AGENCY), 
                        GroupingCriteria.GROUPING_YEARLY),
                "en", 
                Arrays.asList("department/division", "Test MTEF directed"),
                cor);
    }
    
    @Test
    public void testPlannedActualArrears() {
        NiReportModel cor = new NiReportModel("Testcase for Actual and Planned Arrears")
            .withHeaders(Arrays.asList(
                    "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 0, colSpan: 3))",
                    "(Project Title: (startRow: 1, rowSpan: 2, totalRowSpan: 2, colStart: 0, colSpan: 1));(Totals: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 1, colSpan: 2))",
                    "(Actual Arrears: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1));(Planned Arrears: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                  .withContents("Project Title", "", "Totals-Actual Arrears", "132,000", "Totals-Planned Arrears", "72,000")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(78), "Project Title", "activity with many MTEFs"),
                    new ReportAreaForTests(new AreaOwner(80), "Project Title", "arrears test", "Totals-Actual Arrears", "132,000", "Totals-Planned Arrears", "72,000")      ));

        runNiTestCase(
                buildSpecification("Testcase for Actual and Planned Arrears", 
                        Arrays.asList(ColumnConstants.PROJECT_TITLE), 
                        Arrays.asList(MeasureConstants.ACTUAL_ARREARS, MeasureConstants.PLANNED_ARREARS), 
                        null, GroupingCriteria.GROUPING_TOTALS_ONLY),
                "en", 
                Arrays.asList("activity with many MTEFs", "arrears test"),
                cor);
    }
    
    @Test
    public void testMtefColumnsPlain() {
        assertEquals("{RAW / Project Title=, RAW / MTEF 2011=1283182.4159, RAW / MTEF 2012=202437, RAW / MTEF 2013=120180.405, RAW / Funding / 2006 / Actual Commitments=80000, RAW / Funding / 2006 / Actual Disbursements=0, RAW / Funding / 2009 / Actual Commitments=78470, RAW / Funding / 2009 / Actual Disbursements=0, RAW / Funding / 2010 / Actual Commitments=0, RAW / Funding / 2010 / Actual Disbursements=613561.3161, RAW / Funding / 2011 / Actual Commitments=896327.2977, RAW / Funding / 2011 / Actual Disbursements=0, RAW / Funding / 2012 / Actual Commitments=19577.5, RAW / Funding / 2012 / Actual Disbursements=9162, RAW / Funding / 2013 / Actual Commitments=5905874.9666, RAW / Funding / 2013 / Actual Disbursements=954144.5636, RAW / Funding / 2014 / Actual Commitments=7409649.482335, RAW / Funding / 2014 / Actual Disbursements=576269.62, RAW / Funding / 2015 / Actual Commitments=1803396.8724, RAW / Funding / 2015 / Actual Disbursements=399024.454, RAW / Totals / Actual Commitments=16193296.119035, RAW / Totals / Actual Disbursements=2552161.9537, RAW / Totals / MTEF=1605799.8209}", 
            buildDigest(spec("AMP-16100-flat-mtefs-eur"), acts, new GrandTotalsDigest(z -> true)).toString());
    }
    
    @Test
    public void testMtefColumnsMixedPlain() {
        assertEquals("{RAW / Project Title=, RAW / MTEF 2011=1718011, RAW / Pipeline MTEF Projections 2011=908888, RAW / Projection MTEF Projections 2011=809123, RAW / MTEF 2012=271000, RAW / Pipeline MTEF Projections 2012=108000, RAW / Projection MTEF Projections 2012=163000, RAW / MTEF 2013=158654, RAW / Pipeline MTEF Projections 2013=158654, RAW / Projection MTEF Projections 2013=0, RAW / Funding / 2006 / Actual Commitments=96840.576201, RAW / Funding / 2009 / Actual Commitments=100000, RAW / Funding / 2011 / Actual Commitments=1213119, RAW / Funding / 2012 / Actual Commitments=25000, RAW / Funding / 2013 / Actual Commitments=7842086, RAW / Funding / 2014 / Actual Commitments=8159813.768451, RAW / Funding / 2015 / Actual Commitments=1971831.841736, RAW / Totals / Actual Commitments=19408691.186388, RAW / Totals / MTEF=2147665, RAW / Totals / Pipeline MTEF=1175542, RAW / Totals / Projection MTEF=972123}", 
            buildDigest(spec("AMP-21275-all-plain-mtefs"), acts, new GrandTotalsDigest(z -> true)).toString());
    }

    @Test
    public void testMtefColumnsMixedPlain2() {
        assertEquals("{RAW / Project Title=, RAW / MTEF 2011=1718011, RAW / Pipeline MTEF Projections 2011=908888, RAW / MTEF 2012=271000, RAW / Projection MTEF Projections 2012=163000, RAW / MTEF 2013=158654, RAW / Funding / 2006 / Actual Commitments=96840.576201, RAW / Funding / 2009 / Actual Commitments=100000, RAW / Funding / 2011 / Actual Commitments=1213119, RAW / Funding / 2012 / Actual Commitments=25000, RAW / Funding / 2013 / Actual Commitments=7842086, RAW / Funding / 2014 / Actual Commitments=8159813.768451, RAW / Funding / 2015 / Actual Commitments=1971831.841736, RAW / Totals / Actual Commitments=19408691.186388, RAW / Totals / MTEF=2147665, RAW / Totals / Pipeline MTEF=908888, RAW / Totals / Projection MTEF=163000}", 
            buildDigest(spec("AMP-21275-all-plain-mtefs-rare"), acts, new GrandTotalsDigest(z -> true)).toString());
    }
    
    @Test
    public void testMtefColumnsBehaveLikeTrivialMeasuresOnHierarchies() {
        NiReportModel cor = new NiReportModel("AMP-22422-test-mtefs-hiers")
            .withHeaders(Arrays.asList(
                    "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 15))",
                    "(Executing Agency: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Contracting Agency: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1));(MTEF 2010: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 3, colSpan: 1));(MTEF 2011: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 4, colSpan: 1));(MTEF 2012: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 5, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 6, colSpan: 6));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 12, colSpan: 3))",
                    "(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 6, colSpan: 2));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 8, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 10, colSpan: 2))",
                    "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1));(MTEF: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 14, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                  .withContents("Executing Agency", "", "Contracting Agency", "", "Project Title", "", "MTEF 2010", "0", "MTEF 2011", "1 718 011", "MTEF 2012", "271 000", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "143 777", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "545 000", "Funding-2015-Actual Commitments", "1 011 456", "Funding-2015-Actual Disbursements", "80 000", "Totals-Actual Commitments", "1 011 456", "Totals-Actual Disbursements", "768 777", "Totals-MTEF", "1 989 011")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner("Executing Agency", "Finland")).withContents("Contracting Agency", "", "Project Title", "", "MTEF 2010", "0", "MTEF 2011", "0", "MTEF 2012", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "40 740,48", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "40 740,48", "Totals-Actual Disbursements", "0", "Totals-MTEF", "0", "Executing Agency", "Finland")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner("Contracting Agency", "Contracting Agency: Undefined")).withContents("Project Title", "", "MTEF 2010", "0", "MTEF 2011", "0", "MTEF 2012", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "40 740,48", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "40 740,48", "Totals-Actual Disbursements", "0", "Totals-MTEF", "0", "Contracting Agency", "Contracting Agency: Undefined")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(73), "Project Title", "activity with directed MTEFs", "Funding-2015-Actual Commitments", "40 740,48", "Totals-Actual Commitments", "40 740,48")          )        ),
                    new ReportAreaForTests(new AreaOwner("Executing Agency", "Ministry of Economy")).withContents("Contracting Agency", "", "Project Title", "", "MTEF 2010", "0", "MTEF 2011", "33 888", "MTEF 2012", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "0", "Totals-Actual Disbursements", "0", "Totals-MTEF", "33 888", "Executing Agency", "Ministry of Economy")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner("Contracting Agency", "Contracting Agency: Undefined")).withContents("Project Title", "", "MTEF 2010", "0", "MTEF 2011", "33 888", "MTEF 2012", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "0", "Totals-Actual Disbursements", "0", "Totals-MTEF", "33 888", "Contracting Agency", "Contracting Agency: Undefined")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(19), "Project Title", "Pure MTEF Project", "MTEF 2011", "33 888", "Totals-MTEF", "33 888")          )        ),
                    new ReportAreaForTests(new AreaOwner("Executing Agency", "Norway")).withContents("Contracting Agency", "", "Project Title", "", "MTEF 2010", "0", "MTEF 2011", "0", "MTEF 2012", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "27 160,32", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "27 160,32", "Totals-Actual Disbursements", "0", "Totals-MTEF", "0", "Executing Agency", "Norway")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner("Contracting Agency", "Contracting Agency: Undefined")).withContents("Project Title", "", "MTEF 2010", "0", "MTEF 2011", "0", "MTEF 2012", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "27 160,32", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "27 160,32", "Totals-Actual Disbursements", "0", "Totals-MTEF", "0", "Contracting Agency", "Contracting Agency: Undefined")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(73), "Project Title", "activity with directed MTEFs", "Funding-2015-Actual Commitments", "27 160,32", "Totals-Actual Commitments", "27 160,32")          )        ),
                    new ReportAreaForTests(new AreaOwner("Executing Agency", "UNDP")).withContents("Contracting Agency", "", "Project Title", "", "MTEF 2010", "0", "MTEF 2011", "0", "MTEF 2012", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "272 500", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "0", "Totals-Actual Disbursements", "272 500", "Totals-MTEF", "0", "Executing Agency", "UNDP")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner("Contracting Agency", "Contracting Agency: Undefined")).withContents("Project Title", "", "MTEF 2010", "0", "MTEF 2011", "0", "MTEF 2012", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "272 500", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "0", "Totals-Actual Disbursements", "272 500", "Totals-MTEF", "0", "Contracting Agency", "Contracting Agency: Undefined")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Funding-2013-Actual Disbursements", "272 500", "Totals-Actual Disbursements", "272 500")          )        ),
                    new ReportAreaForTests(new AreaOwner("Executing Agency", "USAID")).withContents("Contracting Agency", "", "Project Title", "", "MTEF 2010", "0", "MTEF 2011", "0", "MTEF 2012", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "55 555,2", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "55 555,2", "Totals-Actual Disbursements", "0", "Totals-MTEF", "0", "Executing Agency", "USAID")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner("Contracting Agency", "Contracting Agency: Undefined")).withContents("Project Title", "", "MTEF 2010", "0", "MTEF 2011", "0", "MTEF 2012", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "55 555,2", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "55 555,2", "Totals-Actual Disbursements", "0", "Totals-MTEF", "0", "Contracting Agency", "Contracting Agency: Undefined")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(73), "Project Title", "activity with directed MTEFs", "Funding-2015-Actual Commitments", "55 555,2", "Totals-Actual Commitments", "55 555,2")          )        ),
                    new ReportAreaForTests(new AreaOwner("Executing Agency", "Water Foundation")).withContents("Contracting Agency", "", "Project Title", "", "MTEF 2010", "0", "MTEF 2011", "150 000", "MTEF 2012", "65 000", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "143 777", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "0", "Totals-Actual Disbursements", "143 777", "Totals-MTEF", "215 000", "Executing Agency", "Water Foundation")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner("Contracting Agency", "Contracting Agency: Undefined")).withContents("Project Title", "", "MTEF 2010", "0", "MTEF 2011", "150 000", "MTEF 2012", "65 000", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "143 777", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "0", "Totals-Actual Disbursements", "143 777", "Totals-MTEF", "215 000", "Contracting Agency", "Contracting Agency: Undefined")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(18), "Project Title", "Test MTEF directed", "MTEF 2011", "150 000", "MTEF 2012", "65 000", "Funding-2010-Actual Disbursements", "143 777", "Totals-Actual Disbursements", "143 777", "Totals-MTEF", "215 000")          )        ),
                    new ReportAreaForTests(new AreaOwner("Executing Agency", "World Bank")).withContents("Contracting Agency", "", "Project Title", "", "MTEF 2010", "0", "MTEF 2011", "0", "MTEF 2012", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "272 500", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "0", "Totals-Actual Disbursements", "272 500", "Totals-MTEF", "0", "Executing Agency", "World Bank")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner("Contracting Agency", "Contracting Agency: Undefined")).withContents("Project Title", "", "MTEF 2010", "0", "MTEF 2011", "0", "MTEF 2012", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "272 500", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "0", "Totals-Actual Disbursements", "272 500", "Totals-MTEF", "0", "Contracting Agency", "Contracting Agency: Undefined")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Funding-2013-Actual Disbursements", "272 500", "Totals-Actual Disbursements", "272 500")          )        ),
                    new ReportAreaForTests(new AreaOwner("Executing Agency", "Executing Agency: Undefined")).withContents("Contracting Agency", "", "Project Title", "", "MTEF 2010", "0", "MTEF 2011", "1 534 123", "MTEF 2012", "206 000", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "888 000", "Funding-2015-Actual Disbursements", "80 000", "Totals-Actual Commitments", "888 000", "Totals-Actual Disbursements", "80 000", "Totals-MTEF", "1 740 123", "Executing Agency", "Executing Agency: Undefined")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner("Contracting Agency", "Contracting Agency: Undefined"))
                      .withContents("Project Title", "", "MTEF 2010", "0", "MTEF 2011", "1 534 123", "MTEF 2012", "206 000", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "888 000", "Funding-2015-Actual Disbursements", "80 000", "Totals-Actual Commitments", "888 000", "Totals-Actual Disbursements", "80 000", "Totals-MTEF", "1 740 123", "Contracting Agency", "Contracting Agency: Undefined")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(25), "Project Title", "mtef activity 1", "MTEF 2011", "789 123", "Totals-MTEF", "789 123"),
                        new ReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms", "MTEF 2011", "700 000", "MTEF 2012", "150 000", "Funding-2015-Actual Commitments", "888 000", "Totals-Actual Commitments", "888 000", "Totals-MTEF", "850 000"),
                        new ReportAreaForTests(new AreaOwner(78), "Project Title", "activity with many MTEFs", "MTEF 2011", "45 000", "MTEF 2012", "56 000", "Funding-2015-Actual Disbursements", "80 000", "Totals-Actual Disbursements", "80 000", "Totals-MTEF", "101 000")          )        )      ));

        runNiTestCase(cor, spec("AMP-22422-test-mtefs-hiers"), Arrays.asList("Pure MTEF Project", "activity with directed MTEFs", "Activity with both MTEFs and Act.Comms", "activity with many MTEFs", "mtef activity 1", "Test MTEF directed", "Eth Water"));
    }
    
    @Test
    public void testMtefMeasuresOnHierarchies() {
        NiReportModel correctReport = new NiReportModel("testcase mtefs measures hiers")
            .withHeaders(Arrays.asList(
                    "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 19))",
                    "(Executing Agency: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Contracting Agency: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 3, colSpan: 12));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 15, colSpan: 4))",
                    "(2011: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 4));(2012: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 7, colSpan: 4));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 11, colSpan: 4))",
                    "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(MTEF: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Projection MTEF Projections: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Pipeline MTEF Projections: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(MTEF: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Projection MTEF Projections: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Pipeline MTEF Projections: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1));(MTEF: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1));(Projection MTEF Projections: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1));(Pipeline MTEF Projections: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 14, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 15, colSpan: 1));(MTEF: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 16, colSpan: 1));(Projection MTEF Projections: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 17, colSpan: 1));(Pipeline MTEF Projections: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 18, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                  .withContents("Executing Agency", "", "Contracting Agency", "", "Project Title", "", "Funding-2011-Actual Commitments", "0", "Funding-2011-MTEF", "1,718,011", "Funding-2011-Projection MTEF Projections", "809,123", "Funding-2011-Pipeline MTEF Projections", "908,888", "Funding-2012-Actual Commitments", "0", "Funding-2012-MTEF", "271,000", "Funding-2012-Projection MTEF Projections", "163,000", "Funding-2012-Pipeline MTEF Projections", "108,000", "Funding-2015-Actual Commitments", "1,011,456", "Funding-2015-MTEF", "0", "Funding-2015-Projection MTEF Projections", "0", "Funding-2015-Pipeline MTEF Projections", "0", "Totals-Actual Commitments", "1,011,456", "Totals-MTEF", "1,989,011", "Totals-Projection MTEF Projections", "972,123", "Totals-Pipeline MTEF Projections", "1,016,888")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner("Executing Agency", "Finland", 21698)).withContents("Contracting Agency", "", "Project Title", "", "Funding-2011-Actual Commitments", "0", "Funding-2011-MTEF", "0", "Funding-2011-Projection MTEF Projections", "0", "Funding-2011-Pipeline MTEF Projections", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-MTEF", "0", "Funding-2012-Projection MTEF Projections", "0", "Funding-2012-Pipeline MTEF Projections", "0", "Funding-2015-Actual Commitments", "40,740,48", "Funding-2015-MTEF", "0", "Funding-2015-Projection MTEF Projections", "0", "Funding-2015-Pipeline MTEF Projections", "0", "Totals-Actual Commitments", "40,740,48", "Totals-MTEF", "0", "Totals-Projection MTEF Projections", "0", "Totals-Pipeline MTEF Projections", "0", "Executing Agency", "Finland")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner("Contracting Agency", "Contracting Agency: Undefined", -999999999)).withContents("Project Title", "", "Funding-2011-Actual Commitments", "0", "Funding-2011-MTEF", "0", "Funding-2011-Projection MTEF Projections", "0", "Funding-2011-Pipeline MTEF Projections", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-MTEF", "0", "Funding-2012-Projection MTEF Projections", "0", "Funding-2012-Pipeline MTEF Projections", "0", "Funding-2015-Actual Commitments", "40,740,48", "Funding-2015-MTEF", "0", "Funding-2015-Projection MTEF Projections", "0", "Funding-2015-Pipeline MTEF Projections", "0", "Totals-Actual Commitments", "40,740,48", "Totals-MTEF", "0", "Totals-Projection MTEF Projections", "0", "Totals-Pipeline MTEF Projections", "0", "Contracting Agency", "Contracting Agency: Undefined")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(73), "Project Title", "activity with directed MTEFs", "Funding-2015-Actual Commitments", "40,740,48", "Totals-Actual Commitments", "40,740,48")          )        ),
                    new ReportAreaForTests(new AreaOwner("Executing Agency", "Ministry of Economy", 21700)).withContents("Contracting Agency", "", "Project Title", "", "Funding-2011-Actual Commitments", "0", "Funding-2011-MTEF", "33,888", "Funding-2011-Projection MTEF Projections", "0", "Funding-2011-Pipeline MTEF Projections", "33,888", "Funding-2012-Actual Commitments", "0", "Funding-2012-MTEF", "0", "Funding-2012-Projection MTEF Projections", "0", "Funding-2012-Pipeline MTEF Projections", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-MTEF", "0", "Funding-2015-Projection MTEF Projections", "0", "Funding-2015-Pipeline MTEF Projections", "0", "Totals-Actual Commitments", "0", "Totals-MTEF", "33,888", "Totals-Projection MTEF Projections", "0", "Totals-Pipeline MTEF Projections", "33,888", "Executing Agency", "Ministry of Economy")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner("Contracting Agency", "Contracting Agency: Undefined", -999999999)).withContents("Project Title", "", "Funding-2011-Actual Commitments", "0", "Funding-2011-MTEF", "33,888", "Funding-2011-Projection MTEF Projections", "0", "Funding-2011-Pipeline MTEF Projections", "33,888", "Funding-2012-Actual Commitments", "0", "Funding-2012-MTEF", "0", "Funding-2012-Projection MTEF Projections", "0", "Funding-2012-Pipeline MTEF Projections", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-MTEF", "0", "Funding-2015-Projection MTEF Projections", "0", "Funding-2015-Pipeline MTEF Projections", "0", "Totals-Actual Commitments", "0", "Totals-MTEF", "33,888", "Totals-Projection MTEF Projections", "0", "Totals-Pipeline MTEF Projections", "33,888", "Contracting Agency", "Contracting Agency: Undefined")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(19), "Project Title", "Pure MTEF Project", "Funding-2011-MTEF", "33,888", "Funding-2011-Pipeline MTEF Projections", "33,888", "Totals-MTEF", "33,888", "Totals-Pipeline MTEF Projections", "33,888")          )        ),
                    new ReportAreaForTests(new AreaOwner("Executing Agency", "Norway", 21694)).withContents("Contracting Agency", "", "Project Title", "", "Funding-2011-Actual Commitments", "0", "Funding-2011-MTEF", "0", "Funding-2011-Projection MTEF Projections", "0", "Funding-2011-Pipeline MTEF Projections", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-MTEF", "0", "Funding-2012-Projection MTEF Projections", "0", "Funding-2012-Pipeline MTEF Projections", "0", "Funding-2015-Actual Commitments", "27,160,32", "Funding-2015-MTEF", "0", "Funding-2015-Projection MTEF Projections", "0", "Funding-2015-Pipeline MTEF Projections", "0", "Totals-Actual Commitments", "27,160,32", "Totals-MTEF", "0", "Totals-Projection MTEF Projections", "0", "Totals-Pipeline MTEF Projections", "0", "Executing Agency", "Norway")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner("Contracting Agency", "Contracting Agency: Undefined", -999999999)).withContents("Project Title", "", "Funding-2011-Actual Commitments", "0", "Funding-2011-MTEF", "0", "Funding-2011-Projection MTEF Projections", "0", "Funding-2011-Pipeline MTEF Projections", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-MTEF", "0", "Funding-2012-Projection MTEF Projections", "0", "Funding-2012-Pipeline MTEF Projections", "0", "Funding-2015-Actual Commitments", "27,160,32", "Funding-2015-MTEF", "0", "Funding-2015-Projection MTEF Projections", "0", "Funding-2015-Pipeline MTEF Projections", "0", "Totals-Actual Commitments", "27,160,32", "Totals-MTEF", "0", "Totals-Projection MTEF Projections", "0", "Totals-Pipeline MTEF Projections", "0", "Contracting Agency", "Contracting Agency: Undefined")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(73), "Project Title", "activity with directed MTEFs", "Funding-2015-Actual Commitments", "27,160,32", "Totals-Actual Commitments", "27,160,32")          )        ),
                    new ReportAreaForTests(new AreaOwner("Executing Agency", "USAID", 21696)).withContents("Contracting Agency", "", "Project Title", "", "Funding-2011-Actual Commitments", "0", "Funding-2011-MTEF", "0", "Funding-2011-Projection MTEF Projections", "0", "Funding-2011-Pipeline MTEF Projections", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-MTEF", "0", "Funding-2012-Projection MTEF Projections", "0", "Funding-2012-Pipeline MTEF Projections", "0", "Funding-2015-Actual Commitments", "55,555,2", "Funding-2015-MTEF", "0", "Funding-2015-Projection MTEF Projections", "0", "Funding-2015-Pipeline MTEF Projections", "0", "Totals-Actual Commitments", "55,555,2", "Totals-MTEF", "0", "Totals-Projection MTEF Projections", "0", "Totals-Pipeline MTEF Projections", "0", "Executing Agency", "USAID")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner("Contracting Agency", "Contracting Agency: Undefined", -999999999)).withContents("Project Title", "", "Funding-2011-Actual Commitments", "0", "Funding-2011-MTEF", "0", "Funding-2011-Projection MTEF Projections", "0", "Funding-2011-Pipeline MTEF Projections", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-MTEF", "0", "Funding-2012-Projection MTEF Projections", "0", "Funding-2012-Pipeline MTEF Projections", "0", "Funding-2015-Actual Commitments", "55,555,2", "Funding-2015-MTEF", "0", "Funding-2015-Projection MTEF Projections", "0", "Funding-2015-Pipeline MTEF Projections", "0", "Totals-Actual Commitments", "55,555,2", "Totals-MTEF", "0", "Totals-Projection MTEF Projections", "0", "Totals-Pipeline MTEF Projections", "0", "Contracting Agency", "Contracting Agency: Undefined")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(73), "Project Title", "activity with directed MTEFs", "Funding-2015-Actual Commitments", "55,555,2", "Totals-Actual Commitments", "55,555,2")          )        ),
                    new ReportAreaForTests(new AreaOwner("Executing Agency", "Water Foundation", 21702)).withContents("Contracting Agency", "", "Project Title", "", "Funding-2011-Actual Commitments", "0", "Funding-2011-MTEF", "150,000", "Funding-2011-Projection MTEF Projections", "0", "Funding-2011-Pipeline MTEF Projections", "150,000", "Funding-2012-Actual Commitments", "0", "Funding-2012-MTEF", "65,000", "Funding-2012-Projection MTEF Projections", "0", "Funding-2012-Pipeline MTEF Projections", "65,000", "Funding-2015-Actual Commitments", "0", "Funding-2015-MTEF", "0", "Funding-2015-Projection MTEF Projections", "0", "Funding-2015-Pipeline MTEF Projections", "0", "Totals-Actual Commitments", "0", "Totals-MTEF", "215,000", "Totals-Projection MTEF Projections", "0", "Totals-Pipeline MTEF Projections", "215,000", "Executing Agency", "Water Foundation")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner("Contracting Agency", "Contracting Agency: Undefined", -999999999)).withContents("Project Title", "", "Funding-2011-Actual Commitments", "0", "Funding-2011-MTEF", "150,000", "Funding-2011-Projection MTEF Projections", "0", "Funding-2011-Pipeline MTEF Projections", "150,000", "Funding-2012-Actual Commitments", "0", "Funding-2012-MTEF", "65,000", "Funding-2012-Projection MTEF Projections", "0", "Funding-2012-Pipeline MTEF Projections", "65,000", "Funding-2015-Actual Commitments", "0", "Funding-2015-MTEF", "0", "Funding-2015-Projection MTEF Projections", "0", "Funding-2015-Pipeline MTEF Projections", "0", "Totals-Actual Commitments", "0", "Totals-MTEF", "215,000", "Totals-Projection MTEF Projections", "0", "Totals-Pipeline MTEF Projections", "215,000", "Contracting Agency", "Contracting Agency: Undefined")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(18), "Project Title", "Test MTEF directed", "Funding-2011-MTEF", "150,000", "Funding-2011-Pipeline MTEF Projections", "150,000", "Funding-2012-MTEF", "65,000", "Funding-2012-Pipeline MTEF Projections", "65,000", "Totals-MTEF", "215,000", "Totals-Pipeline MTEF Projections", "215,000")          )        ),
                    new ReportAreaForTests(new AreaOwner("Executing Agency", "Executing Agency: Undefined", -999999999)).withContents("Contracting Agency", "", "Project Title", "", "Funding-2011-Actual Commitments", "0", "Funding-2011-MTEF", "1,534,123", "Funding-2011-Projection MTEF Projections", "809,123", "Funding-2011-Pipeline MTEF Projections", "725,000", "Funding-2012-Actual Commitments", "0", "Funding-2012-MTEF", "206,000", "Funding-2012-Projection MTEF Projections", "163,000", "Funding-2012-Pipeline MTEF Projections", "43,000", "Funding-2015-Actual Commitments", "888,000", "Funding-2015-MTEF", "0", "Funding-2015-Projection MTEF Projections", "0", "Funding-2015-Pipeline MTEF Projections", "0", "Totals-Actual Commitments", "888,000", "Totals-MTEF", "1,740,123", "Totals-Projection MTEF Projections", "972,123", "Totals-Pipeline MTEF Projections", "768,000", "Executing Agency", "Executing Agency: Undefined")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner("Contracting Agency", "Contracting Agency: Undefined", -999999999))
                      .withContents("Project Title", "", "Funding-2011-Actual Commitments", "0", "Funding-2011-MTEF", "1,534,123", "Funding-2011-Projection MTEF Projections", "809,123", "Funding-2011-Pipeline MTEF Projections", "725,000", "Funding-2012-Actual Commitments", "0", "Funding-2012-MTEF", "206,000", "Funding-2012-Projection MTEF Projections", "163,000", "Funding-2012-Pipeline MTEF Projections", "43,000", "Funding-2015-Actual Commitments", "888,000", "Funding-2015-MTEF", "0", "Funding-2015-Projection MTEF Projections", "0", "Funding-2015-Pipeline MTEF Projections", "0", "Totals-Actual Commitments", "888,000", "Totals-MTEF", "1,740,123", "Totals-Projection MTEF Projections", "972,123", "Totals-Pipeline MTEF Projections", "768,000", "Contracting Agency", "Contracting Agency: Undefined")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(25), "Project Title", "mtef activity 1", "Funding-2011-MTEF", "789,123", "Funding-2011-Projection MTEF Projections", "789,123", "Totals-MTEF", "789,123", "Totals-Projection MTEF Projections", "789,123"),
                        new ReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms", "Funding-2011-MTEF", "700,000", "Funding-2011-Pipeline MTEF Projections", "700,000", "Funding-2012-MTEF", "150,000", "Funding-2012-Projection MTEF Projections", "150,000", "Funding-2015-Actual Commitments", "888,000", "Totals-Actual Commitments", "888,000", "Totals-MTEF", "850,000", "Totals-Projection MTEF Projections", "150,000", "Totals-Pipeline MTEF Projections", "700,000"),
                        new ReportAreaForTests(new AreaOwner(78), "Project Title", "activity with many MTEFs", "Funding-2011-MTEF", "45,000", "Funding-2011-Projection MTEF Projections", "20,000", "Funding-2011-Pipeline MTEF Projections", "25,000", "Funding-2012-MTEF", "56,000", "Funding-2012-Projection MTEF Projections", "13,000", "Funding-2012-Pipeline MTEF Projections", "43,000", "Totals-MTEF", "101,000", "Totals-Projection MTEF Projections", "33,000", "Totals-Pipeline MTEF Projections", "68,000")          )        )      ));

        runNiTestCase(
                this.buildSpecification("testcase mtefs measures hiers", 
                        
                        Arrays.asList(ColumnConstants.EXECUTING_AGENCY, ColumnConstants.CONTRACTING_AGENCY, ColumnConstants.PROJECT_TITLE), 
                        Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.MTEF, MeasureConstants.PROJECTION_MTEF_PROJECTIONS, MeasureConstants.PIPELINE_MTEF_PROJECTIONS), 
                        Arrays.asList(ColumnConstants.EXECUTING_AGENCY,ColumnConstants.CONTRACTING_AGENCY), GroupingCriteria.GROUPING_YEARLY),                       
                        "en", 
                        Arrays.asList("Pure MTEF Project", "activity with directed MTEFs", "Activity with both MTEFs and Act.Comms", "activity with many MTEFs", "mtef activity 1", "Test MTEF directed", "Eth Water"),
                        correctReport); 
    }
    
    @Test
    public void testProjectTitleLanguages() {
        NiReportModel correctReport = new NiReportModel("testcase EN")
            .withHeaders(Arrays.asList(
                    "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 0, colSpan: 3))",
                    "(Project Title: (startRow: 1, rowSpan: 2, totalRowSpan: 2, colStart: 0, colSpan: 1));(Totals: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 1, colSpan: 2))",
                    "(Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1));(Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                  .withContents("Project Title", "", "Totals-Actual Commitments", "7,181,333", "Totals-Actual Disbursements", "1,550,111")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Totals-Actual Disbursements", "545,000"),
                    new ReportAreaForTests(new AreaOwner(30), "Project Title", "SSC Project 1", "Totals-Actual Commitments", "111,333", "Totals-Actual Disbursements", "555,111"),
                    new ReportAreaForTests(new AreaOwner(48), "Project Title", "pledged 2", "Totals-Actual Commitments", "7,070,000", "Totals-Actual Disbursements", "450,000")      ));
        
        runNiTestCase(
                this.buildSpecification("testcase EN", 
                        Arrays.asList(ColumnConstants.PROJECT_TITLE), 
                        Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS), 
                        null, GroupingCriteria.GROUPING_TOTALS_ONLY),                       
                        "en", 
                        Arrays.asList("Eth Water", "SSC Project 1", "pledged 2"),
                        correctReport); 
                
        NiReportModel correctReportRu = new NiReportModel("testcase RU")
            .withHeaders(Arrays.asList(
                    "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 0, colSpan: 3))",
                    "(Project Title: (startRow: 1, rowSpan: 2, totalRowSpan: 2, colStart: 0, colSpan: 1));(Totals: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 1, colSpan: 2))",
                    "(Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1));(Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                  .withContents("Project Title", "", "Totals-Actual Commitments", "7,181,333", "Totals-Actual Disbursements", "1,550,111")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(24), "Project Title", "Вода Eth", "Totals-Actual Disbursements", "545,000"),
                    new ReportAreaForTests(new AreaOwner(30), "Project Title", "Проект КЮЮ 1", "Totals-Actual Commitments", "111,333", "Totals-Actual Disbursements", "555,111"),
                    new ReportAreaForTests(new AreaOwner(48), "Project Title", "обещание 2", "Totals-Actual Commitments", "7,070,000", "Totals-Actual Disbursements", "450,000")      ));
        
        runNiTestCase(
                buildSpecification("testcase RU", 
                        Arrays.asList(ColumnConstants.PROJECT_TITLE), 
                        Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS), 
                        null, GroupingCriteria.GROUPING_TOTALS_ONLY),
                "ru", 
                Arrays.asList("Eth Water", "SSC Project 1", "pledged 2"),
                correctReportRu);
    }
    
    @Test
    public void testMtefColumnsInMillions() {
        NiReportModel cor =  new NiReportModel("MTEF millions")
            .withHeaders(Arrays.asList(
                    "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 0, colSpan: 4))",
                    "(Project Title: (startRow: 1, rowSpan: 2, totalRowSpan: 2, colStart: 0, colSpan: 1));(MTEF 2011: (startRow: 1, rowSpan: 2, totalRowSpan: 2, colStart: 1, colSpan: 1));(Totals: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 2))",
                    "(Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(MTEF: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                  .withContents("Project Title", "", "MTEF 2011", "1,72", "Totals-Actual Commitments", "0,89", "Totals-MTEF", "1,72")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(18), "Project Title", "Test MTEF directed", "MTEF 2011", "0,15", "Totals-MTEF", "0,15"),
                    new ReportAreaForTests(new AreaOwner(19), "Project Title", "Pure MTEF Project", "MTEF 2011", "0,03", "Totals-MTEF", "0,03"),
                    new ReportAreaForTests(new AreaOwner(25), "Project Title", "mtef activity 1", "MTEF 2011", "0,79", "Totals-MTEF", "0,79"),
                    new ReportAreaForTests(new AreaOwner(27), "Project Title", "mtef activity 2"),
                    new ReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms", "MTEF 2011", "0,7", "Totals-Actual Commitments", "0,89", "Totals-MTEF", "0,7"),
                    new ReportAreaForTests(new AreaOwner(76), "Project Title", "activity with pipeline MTEFs and act. disb"),
                    new ReportAreaForTests(new AreaOwner(78), "Project Title", "activity with many MTEFs", "MTEF 2011", "0,04", "Totals-MTEF", "0,04")      ));

        ReportSpecificationImpl spec = buildSpecification("MTEF millions",
            Arrays.asList(ColumnConstants.PROJECT_TITLE, "MTEF 2011"), 
            Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS), 
            null,
            GroupingCriteria.GROUPING_TOTALS_ONLY);
        
        spec.getOrCreateSettings().setUnitsOption(AmountsUnits.AMOUNTS_OPTION_MILLIONS);
        runNiTestCase(cor, spec, mtefActs);
    }
    
    @Test
    public void testMtefMeasuresInMillions() {
        NiReportModel cor =  new NiReportModel("MTEF measures millions")
            .withHeaders(Arrays.asList(
                    "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 0, colSpan: 4))",
                    "(Project Title: (startRow: 1, rowSpan: 2, totalRowSpan: 2, colStart: 0, colSpan: 1));(MTEF 2011: (startRow: 1, rowSpan: 2, totalRowSpan: 2, colStart: 1, colSpan: 1));(Totals: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 2))",
                    "(Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(MTEF: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                  .withContents("Project Title", "", "MTEF 2011", "1,72", "Totals-Actual Commitments", "0,89", "Totals-MTEF", "1,72")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(18), "Project Title", "Test MTEF directed", "MTEF 2011", "0,15", "Totals-MTEF", "0,15"),
                    new ReportAreaForTests(new AreaOwner(19), "Project Title", "Pure MTEF Project", "MTEF 2011", "0,03", "Totals-MTEF", "0,03"),
                    new ReportAreaForTests(new AreaOwner(25), "Project Title", "mtef activity 1", "MTEF 2011", "0,79", "Totals-MTEF", "0,79"),
                    new ReportAreaForTests(new AreaOwner(27), "Project Title", "mtef activity 2"),
                    new ReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms", "MTEF 2011", "0,7", "Totals-Actual Commitments", "0,89", "Totals-MTEF", "0,7"),
                    new ReportAreaForTests(new AreaOwner(76), "Project Title", "activity with pipeline MTEFs and act. disb"),
                    new ReportAreaForTests(new AreaOwner(78), "Project Title", "activity with many MTEFs", "MTEF 2011", "0,04", "Totals-MTEF", "0,04")      ));

        ReportSpecificationImpl spec = buildSpecification("MTEF measures millions",
            Arrays.asList(ColumnConstants.PROJECT_TITLE, "MTEF 2011"), 
            Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS), 
            null,
            GroupingCriteria.GROUPING_TOTALS_ONLY);
        
        spec.getOrCreateSettings().setUnitsOption(AmountsUnits.AMOUNTS_OPTION_MILLIONS);
        runNiTestCase(cor, spec, mtefActs);
    }
    
    @Test
    public void testAllMtefMeasures() {
        NiReportModel cor =  new NiReportModel("MTEF measures")
            .withHeaders(Arrays.asList(
                    "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 25))",
                    "(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 1, colSpan: 20));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 21, colSpan: 4))",
                    "(2011: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 1, colSpan: 4));(2012: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 4));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 9, colSpan: 4));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 13, colSpan: 4));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 17, colSpan: 4))",
                    "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1));(MTEF: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Projection MTEF Projections: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Pipeline MTEF Projections: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(MTEF: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Projection MTEF Projections: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Pipeline MTEF Projections: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(MTEF: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Projection MTEF Projections: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1));(Pipeline MTEF Projections: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1));(MTEF: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 14, colSpan: 1));(Projection MTEF Projections: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 15, colSpan: 1));(Pipeline MTEF Projections: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 16, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 17, colSpan: 1));(MTEF: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 18, colSpan: 1));(Projection MTEF Projections: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 19, colSpan: 1));(Pipeline MTEF Projections: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 20, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 21, colSpan: 1));(MTEF: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 22, colSpan: 1));(Projection MTEF Projections: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 23, colSpan: 1));(Pipeline MTEF Projections: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 24, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                  .withContents("Project Title", "", "Funding-2011-Actual Commitments", "0", "Funding-2011-MTEF", "1,718,011", "Funding-2011-Projection MTEF Projections", "809,123", "Funding-2011-Pipeline MTEF Projections", "908,888", "Funding-2012-Actual Commitments", "0", "Funding-2012-MTEF", "271,000", "Funding-2012-Projection MTEF Projections", "163,000", "Funding-2012-Pipeline MTEF Projections", "108,000", "Funding-2013-Actual Commitments", "0", "Funding-2013-MTEF", "158,654", "Funding-2013-Projection MTEF Projections", "0", "Funding-2013-Pipeline MTEF Projections", "158,654", "Funding-2014-Actual Commitments", "0", "Funding-2014-MTEF", "105,000", "Funding-2014-Projection MTEF Projections", "55,000", "Funding-2014-Pipeline MTEF Projections", "50,000", "Funding-2015-Actual Commitments", "888,000", "Funding-2015-MTEF", "0", "Funding-2015-Projection MTEF Projections", "0", "Funding-2015-Pipeline MTEF Projections", "0", "Totals-Actual Commitments", "888,000", "Totals-MTEF", "2,252,665", "Totals-Projection MTEF Projections", "1,027,123", "Totals-Pipeline MTEF Projections", "1,225,542")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(18), "Project Title", "Test MTEF directed", "Funding-2011-MTEF", "150,000", "Funding-2011-Pipeline MTEF Projections", "150,000", "Funding-2012-MTEF", "65,000", "Funding-2012-Pipeline MTEF Projections", "65,000", "Totals-MTEF", "215,000", "Totals-Pipeline MTEF Projections", "215,000"),
                    new ReportAreaForTests(new AreaOwner(19), "Project Title", "Pure MTEF Project", "Funding-2011-MTEF", "33,888", "Funding-2011-Pipeline MTEF Projections", "33,888", "Totals-MTEF", "33,888", "Totals-Pipeline MTEF Projections", "33,888"),
                    new ReportAreaForTests(new AreaOwner(25), "Project Title", "mtef activity 1", "Funding-2011-MTEF", "789,123", "Funding-2011-Projection MTEF Projections", "789,123", "Totals-MTEF", "789,123", "Totals-Projection MTEF Projections", "789,123"),
                    new ReportAreaForTests(new AreaOwner(27), "Project Title", "mtef activity 2", "Funding-2013-MTEF", "123,654", "Funding-2013-Pipeline MTEF Projections", "123,654", "Totals-MTEF", "123,654", "Totals-Pipeline MTEF Projections", "123,654"),
                    new ReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms", "Funding-2011-MTEF", "700,000", "Funding-2011-Pipeline MTEF Projections", "700,000", "Funding-2012-MTEF", "150,000", "Funding-2012-Projection MTEF Projections", "150,000", "Funding-2015-Actual Commitments", "888,000", "Totals-Actual Commitments", "888,000", "Totals-MTEF", "850,000", "Totals-Projection MTEF Projections", "150,000", "Totals-Pipeline MTEF Projections", "700,000"),
                    new ReportAreaForTests(new AreaOwner(76), "Project Title", "activity with pipeline MTEFs and act. disb", "Funding-2013-MTEF", "35,000", "Funding-2013-Pipeline MTEF Projections", "35,000", "Funding-2014-MTEF", "105,000", "Funding-2014-Projection MTEF Projections", "55,000", "Funding-2014-Pipeline MTEF Projections", "50,000", "Totals-MTEF", "140,000", "Totals-Projection MTEF Projections", "55,000", "Totals-Pipeline MTEF Projections", "85,000"),
                    new ReportAreaForTests(new AreaOwner(78), "Project Title", "activity with many MTEFs", "Funding-2011-MTEF", "45,000", "Funding-2011-Projection MTEF Projections", "20,000", "Funding-2011-Pipeline MTEF Projections", "25,000", "Funding-2012-MTEF", "56,000", "Funding-2012-Projection MTEF Projections", "13,000", "Funding-2012-Pipeline MTEF Projections", "43,000", "Totals-MTEF", "101,000", "Totals-Projection MTEF Projections", "33,000", "Totals-Pipeline MTEF Projections", "68,000")      ));

        ReportSpecificationImpl spec = buildSpecification("MTEF measures",
            Arrays.asList(ColumnConstants.PROJECT_TITLE), 
            Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.MTEF, MeasureConstants.PROJECTION_MTEF_PROJECTIONS, MeasureConstants.PIPELINE_MTEF_PROJECTIONS, MeasureConstants.REAL_MTEF),
            null,
            GroupingCriteria.GROUPING_YEARLY);
        
        runNiTestCase(cor, spec, mtefActs);
    }
    
    @Test
    public void test_AMP_18499_should_fail_for_now() {
        // for running manually: open http://localhost:8080/aim/viewNewAdvancedReport.do~view=reset~widget=false~resetSettings=true~ampReportId=73 OR http://localhost:8080/TEMPLATE/ampTemplate/saikuui_reports/index.html#report/open/73
        NiReportModel cor = new NiReportModel("AMP-18499")
            .withHeaders(Arrays.asList(
                "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 0, colSpan: 2))",
                "(Project Title: (startRow: 1, rowSpan: 2, totalRowSpan: 2, colStart: 0, colSpan: 1));(Totals: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 1, colSpan: 1))",
                "(Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1))"))
            .withWarnings(Arrays.asList())
            .withBody(      new ReportAreaForTests(null)
                 .withContents("Project Title", "", "Totals-Actual Commitments", "666,777")
                 .withChildren(
                   new ReportAreaForTests(new AreaOwner(15), "Project Title", "Proposed Project Cost 1 - USD"),
                   new ReportAreaForTests(new AreaOwner(23), "Project Title", "Project with documents"),
                   new ReportAreaForTests(new AreaOwner(28), "Project Title", "ptc activity 1", "Totals-Actual Commitments", "666,777")));
        
        runNiTestCase(
                buildSpecification("AMP-18499", Arrays.asList(ColumnConstants.PROJECT_TITLE), Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS), null, GroupingCriteria.GROUPING_TOTALS_ONLY),
                "en",
                Arrays.asList("Proposed Project Cost 1 - USD", "Project with documents", "ptc activity 1"),
                cor);
    }
    
    @Test
    public void test_AMP_18504_should_fail_for_now() {
        // for running manually: http://localhost:8080/aim/viewNewAdvancedReport.do~view=reset~widget=false~resetSettings=true~ampReportId=24 or http://localhost:8080/TEMPLATE/ampTemplate/saikuui_reports/index.html#report/open/24
        
        NiReportModel cor = new NiReportModel("AMP-18504")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 14))",
                        "(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Donor Agency: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 10));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 12, colSpan: 2))",
                        "(2009: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 2));(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 2));(2012: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 6, colSpan: 2));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 8, colSpan: 2));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 10, colSpan: 2))",
                        "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                        .withContents("Project Title", "", "Donor Agency", "", "Funding-2009-Actual Commitments", "100,000", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "60,000", "Funding-2012-Actual Commitments", "25,000", "Funding-2012-Actual Disbursements", "12,000", "Funding-2013-Actual Commitments", "2,670,000", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "4,400,000", "Funding-2014-Actual Disbursements", "450,000", "Totals-Actual Commitments", "7,195,000", "Totals-Actual Disbursements", "522,000")
                        .withChildren(
                                new ReportAreaForTests(new AreaOwner(26), "Project Title", "date-filters-activity", "Donor Agency", "Ministry of Finance", "Funding-2009-Actual Commitments", "100,000", "Funding-2010-Actual Disbursements", "60,000", "Funding-2012-Actual Commitments", "25,000", "Funding-2012-Actual Disbursements", "12,000", "Totals-Actual Commitments", "125,000", "Totals-Actual Disbursements", "72,000"),
                                new ReportAreaForTests(new AreaOwner(48), "Project Title", "pledged 2", "Donor Agency", "Finland, USAID", "Funding-2013-Actual Commitments", "2,670,000", "Funding-2014-Actual Commitments", "4,400,000", "Funding-2014-Actual Disbursements", "450,000", "Totals-Actual Commitments", "7,070,000", "Totals-Actual Disbursements", "450,000")      ));
        
        runNiTestCase(
            buildSpecification("AMP-18504",
                Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.DONOR_AGENCY),
                Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS),
                null, GroupingCriteria.GROUPING_YEARLY),
        "en",
        Arrays.asList("date-filters-activity", "pledged 2"),
        cor);
    }
    
    @Test
    public void test_AMP_18509() {
        NiReportModel cor = new NiReportModel("AMP-18509")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 5, colStart: 0, colSpan: 39))",
                        "(Project Title: (startRow: 1, rowSpan: 4, totalRowSpan: 4, colStart: 0, colSpan: 1));(Region: (startRow: 1, rowSpan: 4, totalRowSpan: 4, colStart: 1, colSpan: 1));(AMP ID: (startRow: 1, rowSpan: 4, totalRowSpan: 4, colStart: 2, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 4, colStart: 3, colSpan: 33));(Totals: (startRow: 1, rowSpan: 3, totalRowSpan: 4, colStart: 36, colSpan: 3))",
                        "(2009: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 3, colSpan: 6));(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 9, colSpan: 6));(2012: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 15, colSpan: 9));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 24, colSpan: 6));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 30, colSpan: 6))",
                        "(Q1: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 3));(Total: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 6, colSpan: 3));(Q2: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 9, colSpan: 3));(Total: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 12, colSpan: 3));(Q3: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 15, colSpan: 3));(Q4: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 18, colSpan: 3));(Total: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 21, colSpan: 3));(Q4: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 24, colSpan: 3));(Total: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 27, colSpan: 3));(Q2: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 30, colSpan: 3));(Total: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 33, colSpan: 3))",
                        "(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Disbursements: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Expenditures: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Disbursements: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Expenditures: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Disbursements: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Actual Expenditures: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1));(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1));(Actual Disbursements: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1));(Actual Expenditures: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 14, colSpan: 1));(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 15, colSpan: 1));(Actual Disbursements: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 16, colSpan: 1));(Actual Expenditures: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 17, colSpan: 1));(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 18, colSpan: 1));(Actual Disbursements: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 19, colSpan: 1));(Actual Expenditures: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 20, colSpan: 1));(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 21, colSpan: 1));(Actual Disbursements: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 22, colSpan: 1));(Actual Expenditures: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 23, colSpan: 1));(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 24, colSpan: 1));(Actual Disbursements: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 25, colSpan: 1));(Actual Expenditures: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 26, colSpan: 1));(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 27, colSpan: 1));(Actual Disbursements: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 28, colSpan: 1));(Actual Expenditures: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 29, colSpan: 1));(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 30, colSpan: 1));(Actual Disbursements: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 31, colSpan: 1));(Actual Expenditures: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 32, colSpan: 1));(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 33, colSpan: 1));(Actual Disbursements: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 34, colSpan: 1));(Actual Expenditures: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 35, colSpan: 1));(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 36, colSpan: 1));(Actual Disbursements: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 37, colSpan: 1));(Actual Expenditures: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 38, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                        .withContents("Project Title", "", "Region", "", "AMP ID", "", "Funding-2009-Q1-Actual Commitments", "100,000", "Funding-2009-Q1-Actual Disbursements", "0", "Funding-2009-Q1-Actual Expenditures", "0", "Funding-2009-Total-Actual Commitments", "100,000", "Funding-2009-Total-Actual Disbursements", "0", "Funding-2009-Total-Actual Expenditures", "0", "Funding-2010-Q2-Actual Commitments", "0", "Funding-2010-Q2-Actual Disbursements", "60,000", "Funding-2010-Q2-Actual Expenditures", "0", "Funding-2010-Total-Actual Commitments", "0", "Funding-2010-Total-Actual Disbursements", "60,000", "Funding-2010-Total-Actual Expenditures", "0", "Funding-2012-Q3-Actual Commitments", "25,000", "Funding-2012-Q3-Actual Disbursements", "0", "Funding-2012-Q3-Actual Expenditures", "0", "Funding-2012-Q4-Actual Commitments", "0", "Funding-2012-Q4-Actual Disbursements", "12,000", "Funding-2012-Q4-Actual Expenditures", "0", "Funding-2012-Total-Actual Commitments", "25,000", "Funding-2012-Total-Actual Disbursements", "12,000", "Funding-2012-Total-Actual Expenditures", "0", "Funding-2013-Q4-Actual Commitments", "2,670,000", "Funding-2013-Q4-Actual Disbursements", "0", "Funding-2013-Q4-Actual Expenditures", "0", "Funding-2013-Total-Actual Commitments", "2,670,000", "Funding-2013-Total-Actual Disbursements", "0", "Funding-2013-Total-Actual Expenditures", "0", "Funding-2014-Q2-Actual Commitments", "4,400,000", "Funding-2014-Q2-Actual Disbursements", "450,000", "Funding-2014-Q2-Actual Expenditures", "0", "Funding-2014-Total-Actual Commitments", "4,400,000", "Funding-2014-Total-Actual Disbursements", "450,000", "Funding-2014-Total-Actual Expenditures", "0", "Totals-Actual Commitments", "7,195,000", "Totals-Actual Disbursements", "522,000", "Totals-Actual Expenditures", "0")
                        .withChildren(
                                new ReportAreaForTests(new AreaOwner(26), "Project Title", "date-filters-activity", "Region", "", "AMP ID", "87211314", "Funding-2009-Q1-Actual Commitments", "100,000", "Funding-2009-Total-Actual Commitments", "100,000", "Funding-2010-Q2-Actual Disbursements", "60,000", "Funding-2010-Total-Actual Disbursements", "60,000", "Funding-2012-Q3-Actual Commitments", "25,000", "Funding-2012-Q4-Actual Disbursements", "12,000", "Funding-2012-Total-Actual Commitments", "25,000", "Funding-2012-Total-Actual Disbursements", "12,000", "Totals-Actual Commitments", "125,000", "Totals-Actual Disbursements", "72,000"),
                                new ReportAreaForTests(new AreaOwner(48), "Project Title", "pledged 2", "Region", "Cahul County", "AMP ID", "87211347", "Funding-2013-Q4-Actual Commitments", "2,670,000", "Funding-2013-Total-Actual Commitments", "2,670,000", "Funding-2014-Q2-Actual Commitments", "4,400,000", "Funding-2014-Q2-Actual Disbursements", "450,000", "Funding-2014-Total-Actual Commitments", "4,400,000", "Funding-2014-Total-Actual Disbursements", "450,000", "Totals-Actual Commitments", "7,070,000", "Totals-Actual Disbursements", "450,000")      ));
        
        runNiTestCase(
            buildSpecification("AMP-18509", 
                Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.REGION, ColumnConstants.AMP_ID),
                Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS, MeasureConstants.ACTUAL_EXPENDITURES),
                null,
                GroupingCriteria.GROUPING_QUARTERLY),
            "en",
            Arrays.asList("date-filters-activity", "pledged 2"),
            cor);
    }
    
    @Test
    public void test_AMP_18577_only_count_donor_transactions() {
        NiReportModel cor = new NiReportModel("AMP_18577_only_count_donor_transaction")
        .withHeaders(Arrays.asList(
                "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 4))",
                "(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Region: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 1));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 3, colSpan: 1))",
                "(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 1))",
                "(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1))"))
            .withWarnings(Arrays.asList())
            .withBody(      new ReportAreaForTests(null).withContents("Project Title", "", "Region", "", "Funding-2010-Actual Disbursements", "143,777", "Totals-Actual Disbursements", "143,777")
              .withChildren(
                new ReportAreaForTests(null, "Project Title", "Test MTEF directed", "Region", "Anenii Noi County", "Funding-2010-Actual Disbursements", "143,777", "Totals-Actual Disbursements", "143,777")      ));
        
        runNiTestCase(
                buildSpecification("AMP_18577_only_count_donor_transaction",
                Arrays.asList("Project Title", "Region"),
                Arrays.asList(MeasureConstants.ACTUAL_DISBURSEMENTS),
                null,
                GroupingCriteria.GROUPING_YEARLY),
            "en",
            Arrays.asList("Test MTEF directed"),
            cor
        );
    }
    
    @Test
    public void test_AMP_18330_empty_rows() {
        NiReportModel cor = new NiReportModel("test_AMP_18330_empty_rows")
        .withHeaders(Arrays.asList(
                "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 4))",
                "(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Region: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 1));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 3, colSpan: 1))",
                "(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 1))",
                "(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1))"))
            .withWarnings(Arrays.asList())
            .withBody(      new ReportAreaForTests(null)
              .withContents("Project Title", "", "Region", "", "Funding-2010-Actual Disbursements", "143,777", "Totals-Actual Disbursements", "143,777")
              .withChildren(
                new ReportAreaForTests(null, "Project Title", "Test MTEF directed", "Region", "Anenii Noi County", "Funding-2010-Actual Disbursements", "143,777", "Totals-Actual Disbursements", "143,777"),
                new ReportAreaForTests(null, "Project Title", "activity with primary_program", "Region", "")      ));
        
        ReportSpecificationImpl spec = buildSpecification("test_AMP_18330_empty_rows",
                Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.REGION),
                Arrays.asList(MeasureConstants.ACTUAL_DISBURSEMENTS),
                null,
                GroupingCriteria.GROUPING_YEARLY);
        
        spec.setDisplayEmptyFundingRows(true);
        
        runNiTestCase(spec, "en",
            Arrays.asList("Test MTEF directed", "activity with primary_program"),
            cor
        );
    }
    
    @Test
    public void test_AMP_18748_no_data() {
        NiReportModel cor = new NiReportModel("test_AMP_18748_no_data")
        .withHeaders(Arrays.asList(
                "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 3))",
                "(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Region: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 2, colSpan: 1))",
                "",
                "(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1))"))
            .withWarnings(Arrays.asList())
            .withBody(      new ReportAreaForTests(null).withContents("Project Title", "", "Region", "", "Totals-Actual Disbursements", "0")
              .withChildren(      ));
        
        ReportSpecificationImpl spec = buildSpecification("test_AMP_18748_no_data",
                Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.REGION),
                Arrays.asList(MeasureConstants.ACTUAL_DISBURSEMENTS),
                null,
                GroupingCriteria.GROUPING_YEARLY);
        
        runNiTestCase(spec, "en",
                Arrays.asList("__hopefully____invalid________name____"),
            cor
        );
    }
    
    @Test
    public void test_AMP_22343_Monthly_Fiscal_Calendar() {
        // tests that the headers come out with the months sorted out in the fiscal year order
        NiReportModel cor = new NiReportModel("AMP-22343-fiscal-monthly")
            .withHeaders(Arrays.asList(
                    "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 5, colStart: 0, colSpan: 9))",
                    "(Project Title: (startRow: 1, rowSpan: 4, totalRowSpan: 4, colStart: 0, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 4, colStart: 1, colSpan: 6));(Totals: (startRow: 1, rowSpan: 3, totalRowSpan: 4, colStart: 7, colSpan: 2))",
                    "(Fiscal Year 2013 - 2014: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 1, colSpan: 6))",
                    "(August: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 1, colSpan: 2));(December: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 2));(February: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 2))",
                    "(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1));(Actual Disbursements: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Disbursements: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Disbursements: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Disbursements: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                  .withContents("Project Title", "", "Funding-Fiscal Year 2013 - 2014-August-Actual Commitments", "111 333", "Funding-Fiscal Year 2013 - 2014-August-Actual Disbursements", "1 100 111", "Funding-Fiscal Year 2013 - 2014-December-Actual Commitments", "890 000", "Funding-Fiscal Year 2013 - 2014-December-Actual Disbursements", "0", "Funding-Fiscal Year 2013 - 2014-February-Actual Commitments", "75 000", "Funding-Fiscal Year 2013 - 2014-February-Actual Disbursements", "0", "Totals-Actual Commitments", "1 076 333", "Totals-Actual Disbursements", "1 100 111")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Funding-Fiscal Year 2013 - 2014-August-Actual Disbursements", "545 000", "Totals-Actual Disbursements", "545 000"),
                    new ReportAreaForTests(new AreaOwner(30), "Project Title", "SSC Project 1", "Funding-Fiscal Year 2013 - 2014-August-Actual Commitments", "111 333", "Funding-Fiscal Year 2013 - 2014-August-Actual Disbursements", "555 111", "Totals-Actual Commitments", "111 333", "Totals-Actual Disbursements", "555 111"),
                    new ReportAreaForTests(new AreaOwner(36), "Project Title", "Activity With Zones and Percentages", "Funding-Fiscal Year 2013 - 2014-December-Actual Commitments", "890 000", "Totals-Actual Commitments", "890 000"),
                    new ReportAreaForTests(new AreaOwner(40), "Project Title", "SubNational no percentages", "Funding-Fiscal Year 2013 - 2014-February-Actual Commitments", "75 000", "Totals-Actual Commitments", "75 000")      ));
        
        runNiTestCase(spec("AMP-22343-fiscal-monthly"), "en",
            Arrays.asList("Eth Water", "SSC Project 1", "Activity With Zones and Percentages", "SubNational no percentages"), 
            cor);
    }
    
    @Test
    public void test_AMP_22322_directed_mtefs_as_plain_mtefs_columns() {
        NiReportModel corPlain = new NiReportModel("AMP-22322-directed-mtefs")
            .withHeaders(Arrays.asList(
                    "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 14))",
                    "(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(MTEF 2011: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(MTEF 2012: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 3, colSpan: 8));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 11, colSpan: 3))",
                    "(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 2));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 2));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 7, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 9, colSpan: 2))",
                    "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1));(MTEF: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1))"))
                .withWarnings(Arrays.asList(
                    "-1: [entityId: -1, message: measure \"Real MTEFs\" not supported in NiReports]"))
                .withBody(      new ReportAreaForTests(null)
                  .withContents("Project Title", "", "MTEF 2011", "1 718 011", "MTEF 2012", "271 000", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "143 777", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "35 000", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "75 000", "Funding-2015-Actual Commitments", "888 000", "Funding-2015-Actual Disbursements", "80 000", "Totals-Actual Commitments", "888 000", "Totals-Actual Disbursements", "333 777", "Totals-MTEF", "1 989 011")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(18), "Project Title", "Test MTEF directed", "MTEF 2011", "150 000", "MTEF 2012", "65 000", "Funding-2010-Actual Disbursements", "143 777", "Totals-Actual Disbursements", "143 777", "Totals-MTEF", "215 000"),
                    new ReportAreaForTests(new AreaOwner(19), "Project Title", "Pure MTEF Project", "MTEF 2011", "33 888", "Totals-MTEF", "33 888"),
                    new ReportAreaForTests(new AreaOwner(25), "Project Title", "mtef activity 1", "MTEF 2011", "789 123", "Totals-MTEF", "789 123"),
                    new ReportAreaForTests(new AreaOwner(27), "Project Title", "mtef activity 2"),
                    new ReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms", "MTEF 2011", "700 000", "MTEF 2012", "150 000", "Funding-2015-Actual Commitments", "888 000", "Totals-Actual Commitments", "888 000", "Totals-MTEF", "850 000"),
                    new ReportAreaForTests(new AreaOwner(76), "Project Title", "activity with pipeline MTEFs and act. disb", "Funding-2013-Actual Disbursements", "35 000", "Funding-2014-Actual Disbursements", "75 000", "Totals-Actual Disbursements", "110 000"),
                    new ReportAreaForTests(new AreaOwner(78), "Project Title", "activity with many MTEFs", "MTEF 2011", "45 000", "MTEF 2012", "56 000", "Funding-2015-Actual Disbursements", "80 000", "Totals-Actual Disbursements", "80 000", "Totals-MTEF", "101 000")      ));

        NiReportModel corByBenf = new NiReportModel("AMP-22322-directed-mtefs-by-beneficiary")
            .withHeaders(Arrays.asList(
                    "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 15))",
                    "(Beneficiary Agency: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(MTEF 2011: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1));(MTEF 2012: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 3, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 4, colSpan: 8));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 12, colSpan: 3))",
                    "(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 2));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 6, colSpan: 2));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 8, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 10, colSpan: 2))",
                    "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1));(MTEF: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 14, colSpan: 1))"))
                .withWarnings(Arrays.asList(
                    "-1: [entityId: -1, message: measure \"Real MTEFs\" not supported in NiReports]"))
                .withBody(      new ReportAreaForTests(null).withContents("Beneficiary Agency", "", "Project Title", "", "MTEF 2011", "1 718 011", "MTEF 2012", "271 000", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "143 777", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "35 000", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "75 000", "Funding-2015-Actual Commitments", "888 000", "Funding-2015-Actual Disbursements", "80 000", "Totals-Actual Commitments", "888 000", "Totals-Actual Disbursements", "333 777", "Totals-MTEF", "1 989 011")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner("Beneficiary Agency", "Beneficiary Agency: Undefined"))
                    .withContents("Project Title", "", "MTEF 2011", "1 718 011", "MTEF 2012", "271 000", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "143 777", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "35 000", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "75 000", "Funding-2015-Actual Commitments", "888 000", "Funding-2015-Actual Disbursements", "80 000", "Totals-Actual Commitments", "888 000", "Totals-Actual Disbursements", "333 777", "Totals-MTEF", "1 989 011", "Beneficiary Agency", "Beneficiary Agency: Undefined")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner(18), "Project Title", "Test MTEF directed", "MTEF 2011", "150 000", "MTEF 2012", "65 000", "Funding-2010-Actual Disbursements", "143 777", "Totals-Actual Disbursements", "143 777", "Totals-MTEF", "215 000"),
                      new ReportAreaForTests(new AreaOwner(19), "Project Title", "Pure MTEF Project", "MTEF 2011", "33 888", "Totals-MTEF", "33 888"),
                      new ReportAreaForTests(new AreaOwner(25), "Project Title", "mtef activity 1", "MTEF 2011", "789 123", "Totals-MTEF", "789 123"),
                      new ReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms", "MTEF 2011", "700 000", "MTEF 2012", "150 000", "Funding-2015-Actual Commitments", "888 000", "Totals-Actual Commitments", "888 000", "Totals-MTEF", "850 000"),
                      new ReportAreaForTests(new AreaOwner(76), "Project Title", "activity with pipeline MTEFs and act. disb", "Funding-2013-Actual Disbursements", "35 000", "Funding-2014-Actual Disbursements", "75 000", "Totals-Actual Disbursements", "110 000"),
                      new ReportAreaForTests(new AreaOwner(78), "Project Title", "activity with many MTEFs", "MTEF 2011", "45 000", "MTEF 2012", "56 000", "Funding-2015-Actual Disbursements", "80 000", "Totals-Actual Disbursements", "80 000", "Totals-MTEF", "101 000")        )      ));
        
        runNiTestCase(spec("AMP-22322-directed-mtefs"), "en", mtefActs, corPlain);
        runNiTestCase(spec("AMP-22322-directed-mtefs-by-beneficiary"), "en", mtefActs, corByBenf);
    }
    
    @Test
    public void testProposedProjectCost() {
        NiReportModel corPPCUSD = new NiReportModel("Proposed-cost-USD")
            .withHeaders(Arrays.asList(
                    "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 8))",
                    "(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Proposed Project Amount: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 4));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 6, colSpan: 2))",
                    "(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 2))",
                    "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                  .withContents("Project Title", "", "Proposed Project Amount", "4 630 902,72", "Funding-2014-Actual Commitments", "172 000", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "580 245", "Funding-2015-Actual Disbursements", "321 765", "Totals-Actual Commitments", "752 245", "Totals-Actual Disbursements", "321 765")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(15), "Project Title", "Proposed Project Cost 1 - USD", "Proposed Project Amount", "1 000 000"),
                    new ReportAreaForTests(new AreaOwner(17), "Project Title", "Proposed Project Cost 2 - EUR", "Proposed Project Amount", "3 399 510,47"),
                    new ReportAreaForTests(new AreaOwner(40), "Project Title", "SubNational no percentages", "Proposed Project Amount", "60 000", "Funding-2014-Actual Commitments", "75 000", "Totals-Actual Commitments", "75 000"),
                    new ReportAreaForTests(new AreaOwner(43), "Project Title", "Activity with primary_tertiary_program", "Proposed Project Amount", "66 392,25", "Funding-2014-Actual Commitments", "50 000", "Totals-Actual Commitments", "50 000"),
                    new ReportAreaForTests(new AreaOwner(44), "Project Title", "activity with primary_program", "Proposed Project Amount", "35 000", "Funding-2014-Actual Commitments", "32 000", "Totals-Actual Commitments", "32 000"),
                    new ReportAreaForTests(new AreaOwner(45), "Project Title", "activity with tertiary_program", "Proposed Project Amount", "70 000", "Funding-2014-Actual Commitments", "15 000", "Totals-Actual Commitments", "15 000"),
                    new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Funding-2015-Actual Commitments", "456 789", "Funding-2015-Actual Disbursements", "321 765", "Totals-Actual Commitments", "456 789", "Totals-Actual Disbursements", "321 765"),
                    new ReportAreaForTests(new AreaOwner(73), "Project Title", "activity with directed MTEFs", "Funding-2015-Actual Commitments", "123 456", "Totals-Actual Commitments", "123 456")      ));

        runNiTestCase(spec("Proposed-cost-USD"), "en", ppcActs, corPPCUSD);
        
        NiReportModel corPPCEUR = new NiReportModel("Proposed-cost-EUR")
            .withHeaders(Arrays.asList(
                    "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 8))",
                    "(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Proposed Project Amount: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 4));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 6, colSpan: 2))",
                    "(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 2))",
                    "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                  .withContents("Project Title", "", "Proposed Project Amount", "3 444 862", "Funding-2014-Actual Commitments", "129 533", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "529 416", "Funding-2015-Actual Disbursements", "293 578", "Totals-Actual Commitments", "658 949", "Totals-Actual Disbursements", "293 578")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(15), "Project Title", "Proposed Project Cost 1 - USD", "Proposed Project Amount", "770 600"),
                    new ReportAreaForTests(new AreaOwner(17), "Project Title", "Proposed Project Cost 2 - EUR", "Proposed Project Amount", "2 500 000"),
                    new ReportAreaForTests(new AreaOwner(40), "Project Title", "SubNational no percentages", "Proposed Project Amount", "45 186", "Funding-2014-Actual Commitments", "56 482", "Totals-Actual Commitments", "56 482"),
                    new ReportAreaForTests(new AreaOwner(43), "Project Title", "Activity with primary_tertiary_program", "Proposed Project Amount", "50 000", "Funding-2014-Actual Commitments", "37 655", "Totals-Actual Commitments", "37 655"),
                    new ReportAreaForTests(new AreaOwner(44), "Project Title", "activity with primary_program", "Proposed Project Amount", "26 358", "Funding-2014-Actual Commitments", "24 099", "Totals-Actual Commitments", "24 099"),
                    new ReportAreaForTests(new AreaOwner(45), "Project Title", "activity with tertiary_program", "Proposed Project Amount", "52 717", "Funding-2014-Actual Commitments", "11 296", "Totals-Actual Commitments", "11 296"),
                    new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Funding-2015-Actual Commitments", "416 774", "Funding-2015-Actual Disbursements", "293 578", "Totals-Actual Commitments", "416 774", "Totals-Actual Disbursements", "293 578"),
                    new ReportAreaForTests(new AreaOwner(73), "Project Title", "activity with directed MTEFs", "Funding-2015-Actual Commitments", "112 641", "Totals-Actual Commitments", "112 641")      ));

        runNiTestCase(spec("Proposed-cost-EUR"), "en", ppcActs, corPPCEUR);
    }
    
    @Test
    public void testProposedProjectCostMillions() {
        NiReportModel cor =  new NiReportModel("PPCMillions")
            .withHeaders(Arrays.asList(
                    "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 0, colSpan: 3))",
                    "(Project Title: (startRow: 1, rowSpan: 2, totalRowSpan: 2, colStart: 0, colSpan: 1));(Proposed Project Amount: (startRow: 1, rowSpan: 2, totalRowSpan: 2, colStart: 1, colSpan: 1));(Totals: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 1))",
                    "(Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                  .withContents("Project Title", "", "Proposed Project Amount", "4,63", "Totals-Actual Commitments", "0,75")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(15), "Project Title", "Proposed Project Cost 1 - USD", "Proposed Project Amount", "1"),
                    new ReportAreaForTests(new AreaOwner(17), "Project Title", "Proposed Project Cost 2 - EUR", "Proposed Project Amount", "3,4"),
                    new ReportAreaForTests(new AreaOwner(40), "Project Title", "SubNational no percentages", "Proposed Project Amount", "0,06", "Totals-Actual Commitments", "0,08"),
                    new ReportAreaForTests(new AreaOwner(43), "Project Title", "Activity with primary_tertiary_program", "Proposed Project Amount", "0,07", "Totals-Actual Commitments", "0,05"),
                    new ReportAreaForTests(new AreaOwner(44), "Project Title", "activity with primary_program", "Proposed Project Amount", "0,04", "Totals-Actual Commitments", "0,03"),
                    new ReportAreaForTests(new AreaOwner(45), "Project Title", "activity with tertiary_program", "Proposed Project Amount", "0,07", "Totals-Actual Commitments", "0,02"),
                    new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Totals-Actual Commitments", "0,46"),
                    new ReportAreaForTests(new AreaOwner(73), "Project Title", "activity with directed MTEFs", "Totals-Actual Commitments", "0,12")      ));
        
        ReportSpecificationImpl spec = buildSpecification("PPCMillions", 
            Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.PROPOSED_PROJECT_AMOUNT),
            Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS), 
            null,
            GroupingCriteria.GROUPING_TOTALS_ONLY);
        spec.getOrCreateSettings().setUnitsOption(AmountsUnits.AMOUNTS_OPTION_MILLIONS);
        
        runNiTestCase(spec, "en", ppcActs, cor);
    }
    
    @Test
    public void testAnnualProposedProjectCost() {
        NiReportModel corAnnualPPC = new NiReportModel("Annual-Proposed-Project-Cost")
            .withHeaders(Arrays.asList(
                    "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 12))",
                    "(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Proposed Project Amount: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 8));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 10, colSpan: 2))",
                    "(2012: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 2));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 2));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 6, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 8, colSpan: 2))",
                    "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Annual Proposed Project Cost: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Annual Proposed Project Cost: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Annual Proposed Project Cost: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Annual Proposed Project Cost: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Annual Proposed Project Cost: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                  .withContents("Project Title", "", "Proposed Project Amount", "4 630 902,72", "Funding-2012-Actual Commitments", "0", "Funding-2012-Annual Proposed Project Cost", "350 000", "Funding-2013-Actual Commitments", "0", "Funding-2013-Annual Proposed Project Cost", "726 072,61", "Funding-2014-Actual Commitments", "172 000", "Funding-2014-Annual Proposed Project Cost", "3 382 784,49", "Funding-2015-Actual Commitments", "580 245", "Funding-2015-Annual Proposed Project Cost", "0", "Totals-Actual Commitments", "752 245", "Totals-Annual Proposed Project Cost", "4 458 857,1")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(15), "Project Title", "Proposed Project Cost 1 - USD", "Proposed Project Amount", "1 000 000", "Funding-2012-Annual Proposed Project Cost", "350 000", "Funding-2013-Annual Proposed Project Cost", "726 072,61", "Funding-2014-Annual Proposed Project Cost", "132 784,49", "Totals-Annual Proposed Project Cost", "1 208 857,1"),
                    new ReportAreaForTests(new AreaOwner(17), "Project Title", "Proposed Project Cost 2 - EUR", "Proposed Project Amount", "3 399 510,47", "Funding-2014-Annual Proposed Project Cost", "3 250 000", "Totals-Annual Proposed Project Cost", "3 250 000"),
                    new ReportAreaForTests(new AreaOwner(40), "Project Title", "SubNational no percentages", "Proposed Project Amount", "60 000", "Funding-2014-Actual Commitments", "75 000", "Totals-Actual Commitments", "75 000"),
                    new ReportAreaForTests(new AreaOwner(43), "Project Title", "Activity with primary_tertiary_program", "Proposed Project Amount", "66 392,25", "Funding-2014-Actual Commitments", "50 000", "Totals-Actual Commitments", "50 000"),
                    new ReportAreaForTests(new AreaOwner(44), "Project Title", "activity with primary_program", "Proposed Project Amount", "35 000", "Funding-2014-Actual Commitments", "32 000", "Totals-Actual Commitments", "32 000"),
                    new ReportAreaForTests(new AreaOwner(45), "Project Title", "activity with tertiary_program", "Proposed Project Amount", "70 000", "Funding-2014-Actual Commitments", "15 000", "Totals-Actual Commitments", "15 000"),
                    new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Funding-2015-Actual Commitments", "456 789", "Totals-Actual Commitments", "456 789"),
                    new ReportAreaForTests(new AreaOwner(73), "Project Title", "activity with directed MTEFs", "Funding-2015-Actual Commitments", "123 456", "Totals-Actual Commitments", "123 456")      ));

        runNiTestCase(spec("Annual-Proposed-Project-Cost"), "en", ppcActs, corAnnualPPC);
    }
    
    @Test
    public void testRevisedProjectCost() {
        NiReportModel cor =  new NiReportModel("AMP-22375-revised-project-cost")
            .withHeaders(Arrays.asList(
                    "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 9))",
                    "(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Proposed Project Amount: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Revised Project Amount: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 3, colSpan: 4));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 7, colSpan: 2))",
                    "(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 2))",
                    "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                  .withContents("Project Title", "", "Proposed Project Amount", "4 630 902,72", "Revised Project Amount", "4 412 539,84", "Funding-2014-Actual Commitments", "172 000", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "580 245", "Funding-2015-Actual Disbursements", "321 765", "Totals-Actual Commitments", "752 245", "Totals-Actual Disbursements", "321 765")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(15), "Project Title", "Proposed Project Cost 1 - USD", "Proposed Project Amount", "1 000 000", "Revised Project Amount", "1 217 000"),
                    new ReportAreaForTests(new AreaOwner(17), "Project Title", "Proposed Project Cost 2 - EUR", "Proposed Project Amount", "3 399 510,47", "Revised Project Amount", "3 195 539,84"),
                    new ReportAreaForTests(new AreaOwner(40), "Project Title", "SubNational no percentages", "Proposed Project Amount", "60 000", "Funding-2014-Actual Commitments", "75 000", "Totals-Actual Commitments", "75 000"),
                    new ReportAreaForTests(new AreaOwner(43), "Project Title", "Activity with primary_tertiary_program", "Proposed Project Amount", "66 392,25", "Funding-2014-Actual Commitments", "50 000", "Totals-Actual Commitments", "50 000"),
                    new ReportAreaForTests(new AreaOwner(44), "Project Title", "activity with primary_program", "Proposed Project Amount", "35 000", "Funding-2014-Actual Commitments", "32 000", "Totals-Actual Commitments", "32 000"),
                    new ReportAreaForTests(new AreaOwner(45), "Project Title", "activity with tertiary_program", "Proposed Project Amount", "70 000", "Funding-2014-Actual Commitments", "15 000", "Totals-Actual Commitments", "15 000"),
                    new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Funding-2015-Actual Commitments", "456 789", "Funding-2015-Actual Disbursements", "321 765", "Totals-Actual Commitments", "456 789", "Totals-Actual Disbursements", "321 765"),
                    new ReportAreaForTests(new AreaOwner(73), "Project Title", "activity with directed MTEFs", "Funding-2015-Actual Commitments", "123 456", "Totals-Actual Commitments", "123 456")));

        runNiTestCase(spec("AMP-22375-revised-project-cost"), "en", ppcActs, cor);
    }
    
    @Test
    public void testHierByHumanitarianAid() {
        NiReportModel cor = new NiReportModel("AMP-20325-by-humanitarian-aid")
            .withHeaders(Arrays.asList(
                    "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 18))",
                    "(Humanitarian Aid: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 14));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 16, colSpan: 2))",
                    "(2009: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 2));(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 2));(2011: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 6, colSpan: 2));(2012: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 8, colSpan: 2));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 10, colSpan: 2));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 12, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 14, colSpan: 2))",
                    "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 14, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 15, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 16, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 17, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                  .withContents("Humanitarian Aid", "", "Project Title", "", "Funding-2009-Actual Commitments", "100 000", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "636 534", "Funding-2011-Actual Commitments", "1 213 119", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "25 000", "Funding-2012-Actual Disbursements", "12 000", "Funding-2013-Actual Commitments", "3 003 333", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "4 400 000", "Funding-2014-Actual Disbursements", "450 200", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "570", "Totals-Actual Commitments", "8 741 452", "Totals-Actual Disbursements", "1 099 304")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner("Humanitarian Aid", "No"))
                    .withContents("Project Title", "", "Funding-2009-Actual Commitments", "100 000", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "60 000", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "25 000", "Funding-2012-Actual Disbursements", "12 000", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "200", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "570", "Totals-Actual Commitments", "125 000", "Totals-Actual Disbursements", "72 770", "Humanitarian Aid", "No")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner(26), "Project Title", "date-filters-activity", "Funding-2009-Actual Commitments", "100 000", "Funding-2010-Actual Disbursements", "60 000", "Funding-2012-Actual Commitments", "25 000", "Funding-2012-Actual Disbursements", "12 000", "Totals-Actual Commitments", "125 000", "Totals-Actual Disbursements", "72 000"),
                      new ReportAreaForTests(new AreaOwner(69), "Project Title", "Activity with planned disbursements", "Funding-2014-Actual Disbursements", "200", "Funding-2015-Actual Disbursements", "570", "Totals-Actual Disbursements", "770")        ),
                    new ReportAreaForTests(new AreaOwner("Humanitarian Aid", "Yes"))
                    .withContents("Project Title", "", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "123 321", "Funding-2011-Actual Commitments", "213 231", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "333 333", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "546 564", "Totals-Actual Disbursements", "123 321", "Humanitarian Aid", "Yes")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner(12), "Project Title", "TAC_activity_1", "Funding-2010-Actual Disbursements", "123 321", "Funding-2011-Actual Commitments", "213 231", "Totals-Actual Commitments", "213 231", "Totals-Actual Disbursements", "123 321"),
                      new ReportAreaForTests(new AreaOwner(32), "Project Title", "crazy funding 1", "Funding-2013-Actual Commitments", "333 333", "Totals-Actual Commitments", "333 333")        ),
                    new ReportAreaForTests(new AreaOwner("Humanitarian Aid", "Humanitarian Aid: Undefined"))
                    .withContents("Project Title", "", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "453 213", "Funding-2011-Actual Commitments", "999 888", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "2 670 000", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "4 400 000", "Funding-2014-Actual Disbursements", "450 000", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "8 069 888", "Totals-Actual Disbursements", "903 213", "Humanitarian Aid", "Humanitarian Aid: Undefined")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner(13), "Project Title", "TAC_activity_2", "Funding-2010-Actual Disbursements", "453 213", "Funding-2011-Actual Commitments", "999 888", "Totals-Actual Commitments", "999 888", "Totals-Actual Disbursements", "453 213"),
                      new ReportAreaForTests(new AreaOwner(48), "Project Title", "pledged 2", "Funding-2013-Actual Commitments", "2 670 000", "Funding-2014-Actual Commitments", "4 400 000", "Funding-2014-Actual Disbursements", "450 000", "Totals-Actual Commitments", "7 070 000", "Totals-Actual Disbursements", "450 000")        )      ));

        
        runNiTestCase(spec("AMP-20325-by-humanitarian-aid"), "en", 
            humanitarianAidActs, cor);
    }
    
    @Test
    public void testHierByDisasterResponseMarker() {
        NiReportModel cor = new NiReportModel("AMP-20980-disaster-response-marker-hier")
            .withHeaders(Arrays.asList(
                    "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 8))",
                    "(Disaster Response Marker: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 5));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 7, colSpan: 1))",
                    "(2009: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 1));(2012: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 1));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 1));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 1));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 6, colSpan: 1))",
                    "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                  .withContents("Disaster Response Marker", "", "Project Title", "", "Funding-2009-Actual Commitments", "100 000", "Funding-2012-Actual Commitments", "25 000", "Funding-2013-Actual Commitments", "2 670 000", "Funding-2014-Actual Commitments", "4 433 000", "Funding-2015-Actual Commitments", "117 000", "Totals-Actual Commitments", "7 345 000")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner("Disaster Response Marker", "No")).withContents("Project Title", "", "Funding-2009-Actual Commitments", "0", "Funding-2012-Actual Commitments", "0", "Funding-2013-Actual Commitments", "0", "Funding-2014-Actual Commitments", "33 000", "Funding-2015-Actual Commitments", "0", "Totals-Actual Commitments", "33 000", "Disaster Response Marker", "No")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner(71), "Project Title", "activity_with_disaster_response", "Funding-2014-Actual Commitments", "33 000", "Totals-Actual Commitments", "33 000")        ),
                    new ReportAreaForTests(new AreaOwner("Disaster Response Marker", "Yes")).withContents("Project Title", "", "Funding-2009-Actual Commitments", "0", "Funding-2012-Actual Commitments", "0", "Funding-2013-Actual Commitments", "0", "Funding-2014-Actual Commitments", "0", "Funding-2015-Actual Commitments", "67 000", "Totals-Actual Commitments", "67 000", "Disaster Response Marker", "Yes")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner(71), "Project Title", "activity_with_disaster_response", "Funding-2015-Actual Commitments", "67 000", "Totals-Actual Commitments", "67 000")        ),
                    new ReportAreaForTests(new AreaOwner("Disaster Response Marker", "Disaster Response Marker: Undefined"))
                    .withContents("Project Title", "", "Funding-2009-Actual Commitments", "100 000", "Funding-2012-Actual Commitments", "25 000", "Funding-2013-Actual Commitments", "2 670 000", "Funding-2014-Actual Commitments", "4 400 000", "Funding-2015-Actual Commitments", "50 000", "Totals-Actual Commitments", "7 245 000", "Disaster Response Marker", "Disaster Response Marker: Undefined")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner(26), "Project Title", "date-filters-activity", "Funding-2009-Actual Commitments", "100 000", "Funding-2012-Actual Commitments", "25 000", "Totals-Actual Commitments", "125 000"),
                      new ReportAreaForTests(new AreaOwner(48), "Project Title", "pledged 2", "Funding-2013-Actual Commitments", "2 670 000", "Funding-2014-Actual Commitments", "4 400 000", "Totals-Actual Commitments", "7 070 000"),
                      new ReportAreaForTests(new AreaOwner(71), "Project Title", "activity_with_disaster_response", "Funding-2015-Actual Commitments", "50 000", "Totals-Actual Commitments", "50 000")        )      ));

        
        runNiTestCase(spec("AMP-20980-disaster-response-marker-hier"), "en",
            Arrays.asList("activity_with_disaster_response", "date-filters-activity", "pledged 2"), 
            cor);
    }
    
    @Test
    public void testPlainDisasterResponseMarker() {
        NiReportModel cor = new NiReportModel("AMP-20980-disaster-response-marker")
            .withHeaders(Arrays.asList(
                    "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 8))",
                    "(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Disaster Response Marker: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 5));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 7, colSpan: 1))",
                    "(2009: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 1));(2012: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 1));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 1));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 1));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 6, colSpan: 1))",
                    "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                  .withContents("Project Title", "", "Disaster Response Marker", "", "Funding-2009-Actual Commitments", "100 000", "Funding-2012-Actual Commitments", "25 000", "Funding-2013-Actual Commitments", "2 670 000", "Funding-2014-Actual Commitments", "4 433 000", "Funding-2015-Actual Commitments", "117 000", "Totals-Actual Commitments", "7 345 000")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(26), "Project Title", "date-filters-activity", "Disaster Response Marker", "", "Funding-2009-Actual Commitments", "100 000", "Funding-2012-Actual Commitments", "25 000", "Totals-Actual Commitments", "125 000"),
                    new ReportAreaForTests(new AreaOwner(48), "Project Title", "pledged 2", "Disaster Response Marker", "", "Funding-2013-Actual Commitments", "2 670 000", "Funding-2014-Actual Commitments", "4 400 000", "Totals-Actual Commitments", "7 070 000"),
                    new ReportAreaForTests(new AreaOwner(71), "Project Title", "activity_with_disaster_response", "Disaster Response Marker", "No, Yes", "Funding-2014-Actual Commitments", "33 000", "Funding-2015-Actual Commitments", "117 000", "Totals-Actual Commitments", "150 000")      ));
        
        runNiTestCase(spec("AMP-20980-disaster-response-marker"), "en",
            Arrays.asList("activity_with_disaster_response", "date-filters-activity", "pledged 2"), 
            cor);
    }
    
    @Test
    public void testActivityCountNonSummary() {
        NiReportModel cor = new NiReportModel("ActivityCountReport")
            .withHeaders(Arrays.asList(
                    "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 0, colSpan: 6))",
                    "(Region: (startRow: 1, rowSpan: 2, totalRowSpan: 2, colStart: 0, colSpan: 1));(Project Title: (startRow: 1, rowSpan: 2, totalRowSpan: 2, colStart: 1, colSpan: 1));(Activity Count: (startRow: 1, rowSpan: 2, totalRowSpan: 2, colStart: 2, colSpan: 1));(Zone: (startRow: 1, rowSpan: 2, totalRowSpan: 2, colStart: 3, colSpan: 1));(Totals: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 2))",
                    "(Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                  .withContents("Region", "", "Project Title", "", "Activity Count", "12", "Zone", "", "Totals-Actual Commitments", "6 678 792,63", "Totals-Actual Disbursements", "1 433 888")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner("Region", "Anenii Noi County", 9085))
                    .withContents("Project Title", "", "Activity Count", "8", "Zone", "", "Totals-Actual Commitments", "1 611 832", "Totals-Actual Disbursements", "1 243 888", "Region", "Anenii Noi County")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner(18), "Project Title", "Test MTEF directed", "Zone", "", "Totals-Actual Disbursements", "143 777"),
                      new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Zone", "", "Totals-Actual Disbursements", "545 000"),
                      new ReportAreaForTests(new AreaOwner(28), "Project Title", "ptc activity 1", "Zone", "", "Totals-Actual Commitments", "666 777"),
                      new ReportAreaForTests(new AreaOwner(29), "Project Title", "ptc activity 2", "Zone", "", "Totals-Actual Commitments", "333 222"),
                      new ReportAreaForTests(new AreaOwner(30), "Project Title", "SSC Project 1", "Zone", "", "Totals-Actual Commitments", "111 333", "Totals-Actual Disbursements", "555 111"),
                      new ReportAreaForTests(new AreaOwner(33), "Project Title", "Activity with Zones", "Zone", "Bulboaca", "Totals-Actual Commitments", "285 000"),
                      new ReportAreaForTests(new AreaOwner(36), "Project Title", "Activity With Zones and Percentages", "Zone", "Dolboaca", "Totals-Actual Commitments", "178 000"),
                      new ReportAreaForTests(new AreaOwner(40), "Project Title", "SubNational no percentages", "Zone", "", "Totals-Actual Commitments", "37 500")        ),
                    new ReportAreaForTests(new AreaOwner("Region", "Chisinau County", 9089))
                    .withContents("Project Title", "", "Activity Count", "4", "Zone", "", "Totals-Actual Commitments", "5 066 960,63", "Totals-Actual Disbursements", "190 000", "Region", "Chisinau County")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner(46), "Project Title", "pledged education activity 1", "Zone", "", "Totals-Actual Commitments", "5 000 000"),
                      new ReportAreaForTests(new AreaOwner(50), "Project Title", "activity with capital spending", "Zone", "", "Totals-Actual Commitments", "65 760,63", "Totals-Actual Disbursements", "80 000"),
                      new ReportAreaForTests(new AreaOwner(66), "Project Title", "Activity 2 with multiple agreements", "Zone", "", "Totals-Actual Commitments", "1 200"),
                      new ReportAreaForTests(new AreaOwner(76), "Project Title", "activity with pipeline MTEFs and act. disb", "Zone", "", "Totals-Actual Disbursements", "110 000")        )      ));
        
        runNiTestCase(spec("ActivityCountReport"), "en", acts, cor);
    }
    
    @Test
    public void testActivityCountUnarySummary() {
        NiReportModel cor = new NiReportModel("activity-count-summary")
            .withHeaders(Arrays.asList(
                    "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 20))",
                    "(Primary Sector: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Activity Count: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 16));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 18, colSpan: 2))",
                    "(2006: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 2));(2009: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 2));(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 6, colSpan: 2));(2011: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 8, colSpan: 2));(2012: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 10, colSpan: 2));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 12, colSpan: 2));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 14, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 16, colSpan: 2))",
                    "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 14, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 15, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 16, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 17, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 18, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 19, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                  .withContents("Primary Sector", "", "Activity Count", "37", "Funding-2006-Actual Commitments", "96 840,58", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "100 000", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "780 311", "Funding-2011-Actual Commitments", "1 213 119", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "25 000", "Funding-2012-Actual Disbursements", "12 000", "Funding-2013-Actual Commitments", "7 842 086", "Funding-2013-Actual Disbursements", "1 266 956", "Funding-2014-Actual Commitments", "8 159 813,77", "Funding-2014-Actual Disbursements", "710 200", "Funding-2015-Actual Commitments", "1 971 831,84", "Funding-2015-Actual Disbursements", "437 335", "Totals-Actual Commitments", "19 408 691,19", "Totals-Actual Disbursements", "3 206 802")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner("Primary Sector", "110 - EDUCATION", 6236), "Activity Count", "31", "Funding-2006-Actual Commitments", "58 104,35", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "100 000", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "203 777", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "25 000", "Funding-2012-Actual Disbursements", "12 000", "Funding-2013-Actual Commitments", "3 981 665", "Funding-2013-Actual Disbursements", "1 135 111", "Funding-2014-Actual Commitments", "3 734 250,55", "Funding-2014-Actual Disbursements", "240 000", "Funding-2015-Actual Commitments", "1 332 044,26", "Funding-2015-Actual Disbursements", "275 882,5", "Totals-Actual Commitments", "9 231 064,16", "Totals-Actual Disbursements", "1 866 770,5", "Primary Sector", "110 - EDUCATION"),
                    new ReportAreaForTests(new AreaOwner("Primary Sector", "112 - BASIC EDUCATION", 6242), "Activity Count", "7", "Funding-2006-Actual Commitments", "9 684,06", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "123 321", "Funding-2011-Actual Commitments", "213 231", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "567 421", "Funding-2013-Actual Disbursements", "131 845", "Funding-2014-Actual Commitments", "363,21", "Funding-2014-Actual Disbursements", "5 200", "Funding-2015-Actual Commitments", "592 987,58", "Funding-2015-Actual Disbursements", "161 452,5", "Totals-Actual Commitments", "1 383 686,86", "Totals-Actual Disbursements", "421 818,5", "Primary Sector", "112 - BASIC EDUCATION"),
                    new ReportAreaForTests(new AreaOwner("Primary Sector", "113 - SECONDARY EDUCATION", 6246), "Activity Count", "2", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "2 670 000", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "4 413 200", "Funding-2014-Actual Disbursements", "450 000", "Funding-2015-Actual Commitments", "46 800", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "7 130 000", "Totals-Actual Disbursements", "450 000", "Primary Sector", "113 - SECONDARY EDUCATION"),
                    new ReportAreaForTests(new AreaOwner("Primary Sector", "120 - HEALTH", 6252), "Activity Count", "2", "Funding-2006-Actual Commitments", "29 052,17", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "623 000", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "15 000", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "652 052,17", "Totals-Actual Disbursements", "15 000", "Primary Sector", "120 - HEALTH"),
                    new ReportAreaForTests(new AreaOwner("Primary Sector", "130 - POPULATION POLICIES/PROGRAMMES AND REPRODUCTIVE HEALTH", 6267), "Activity Count", "1", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "453 213", "Funding-2011-Actual Commitments", "999 888", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "999 888", "Totals-Actual Disbursements", "453 213", "Primary Sector", "130 - POPULATION POLICIES/PROGRAMMES AND REPRODUCTIVE HEALTH"),
                    new ReportAreaForTests(new AreaOwner("Primary Sector", "Primary Sector: Undefined", -999999999), "Activity Count", "1", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "12 000", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "12 000", "Totals-Actual Disbursements", "0", "Primary Sector", "Primary Sector: Undefined")      ));
        
        runNiTestCase(spec("activity-count-summary"), "en", acts, cor);
    }
    
    @Test
    public void testActivityCountDoubleSummary() {
        NiReportModel cor = new NiReportModel("activity-count-summary-dual")
            .withHeaders(Arrays.asList(
                    "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 21))",
                    "(Region: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Primary Sector: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Activity Count: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 3, colSpan: 16));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 19, colSpan: 2))",
                    "(2006: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 2));(2009: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 2));(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 7, colSpan: 2));(2011: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 9, colSpan: 2));(2012: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 11, colSpan: 2));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 13, colSpan: 2));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 15, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 17, colSpan: 2))",
                    "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 14, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 15, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 16, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 17, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 18, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 19, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 20, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                  .withContents("Region", "", "Primary Sector", "", "Activity Count", "37", "Funding-2006-Actual Commitments", "96 840,58", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "100 000", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "780 311", "Funding-2011-Actual Commitments", "1 213 119", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "25 000", "Funding-2012-Actual Disbursements", "12 000", "Funding-2013-Actual Commitments", "7 842 086", "Funding-2013-Actual Disbursements", "1 266 956", "Funding-2014-Actual Commitments", "8 159 813,77", "Funding-2014-Actual Disbursements", "710 200", "Funding-2015-Actual Commitments", "1 971 831,84", "Funding-2015-Actual Disbursements", "437 335", "Totals-Actual Commitments", "19 408 691,19", "Totals-Actual Disbursements", "3 206 802")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner("Region", "Anenii Noi County", 9085))
                    .withContents("Primary Sector", "", "Activity Count", "8", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "143 777", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "1 574 332", "Funding-2013-Actual Disbursements", "1 100 111", "Funding-2014-Actual Commitments", "37 500", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "1 611 832", "Totals-Actual Disbursements", "1 243 888", "Region", "Anenii Noi County")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner("Primary Sector", "110 - EDUCATION", 6236), "Activity Count", "8", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "143 777", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "1 449 732", "Funding-2013-Actual Disbursements", "1 100 111", "Funding-2014-Actual Commitments", "37 500", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "1 487 232", "Totals-Actual Disbursements", "1 243 888", "Primary Sector", "110 - EDUCATION"),
                      new ReportAreaForTests(new AreaOwner("Primary Sector", "120 - HEALTH", 6252), "Activity Count", "1", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "124 600", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "124 600", "Totals-Actual Disbursements", "0", "Primary Sector", "120 - HEALTH")        ),
                    new ReportAreaForTests(new AreaOwner("Region", "Balti County", 9086))
                    .withContents("Primary Sector", "", "Activity Count", "7", "Funding-2006-Actual Commitments", "53 262,32", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "1 330 333", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "37 500", "Funding-2014-Actual Disbursements", "27 500", "Funding-2015-Actual Commitments", "723 189", "Funding-2015-Actual Disbursements", "321 765", "Totals-Actual Commitments", "2 144 284,32", "Totals-Actual Disbursements", "349 265", "Region", "Balti County")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner("Primary Sector", "110 - EDUCATION", 6236), "Activity Count", "7", "Funding-2006-Actual Commitments", "31 957,39", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "831 933", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "37 500", "Funding-2014-Actual Disbursements", "16 500", "Funding-2015-Actual Commitments", "388 234,5", "Funding-2015-Actual Disbursements", "160 882,5", "Totals-Actual Commitments", "1 289 624,89", "Totals-Actual Disbursements", "177 382,5", "Primary Sector", "110 - EDUCATION"),
                      new ReportAreaForTests(new AreaOwner("Primary Sector", "112 - BASIC EDUCATION", 6242), "Activity Count", "3", "Funding-2006-Actual Commitments", "5 326,23", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "2 750", "Funding-2015-Actual Commitments", "334 954,5", "Funding-2015-Actual Disbursements", "160 882,5", "Totals-Actual Commitments", "340 280,73", "Totals-Actual Disbursements", "163 632,5", "Primary Sector", "112 - BASIC EDUCATION"),
                      new ReportAreaForTests(new AreaOwner("Primary Sector", "120 - HEALTH", 6252), "Activity Count", "2", "Funding-2006-Actual Commitments", "15 978,7", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "498 400", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "8 250", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "514 378,7", "Totals-Actual Disbursements", "8 250", "Primary Sector", "120 - HEALTH")        ),
                    new ReportAreaForTests(new AreaOwner("Region", "Cahul County", 9087)).withContents("Primary Sector", "", "Activity Count", "1", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "2 670 000", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "4 400 000", "Funding-2014-Actual Disbursements", "450 000", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "7 070 000", "Totals-Actual Disbursements", "450 000", "Region", "Cahul County")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner("Primary Sector", "113 - SECONDARY EDUCATION", 6246), "Activity Count", "1", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "2 670 000", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "4 400 000", "Funding-2014-Actual Disbursements", "450 000", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "7 070 000", "Totals-Actual Disbursements", "450 000", "Primary Sector", "113 - SECONDARY EDUCATION")        ),
                    new ReportAreaForTests(new AreaOwner("Region", "Chisinau City", 9088)).withContents("Primary Sector", "", "Activity Count", "4", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "50 000", "Funding-2014-Actual Disbursements", "27 500", "Funding-2015-Actual Commitments", "246 912", "Funding-2015-Actual Disbursements", "17 500", "Totals-Actual Commitments", "296 912", "Totals-Actual Disbursements", "45 000", "Region", "Chisinau City")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner("Primary Sector", "110 - EDUCATION", 6236), "Activity Count", "4", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "50 000", "Funding-2014-Actual Disbursements", "27 500", "Funding-2015-Actual Commitments", "246 912", "Funding-2015-Actual Disbursements", "17 500", "Totals-Actual Commitments", "296 912", "Totals-Actual Disbursements", "45 000", "Primary Sector", "110 - EDUCATION")        ),
                    new ReportAreaForTests(new AreaOwner("Region", "Chisinau County", 9089)).withContents("Primary Sector", "", "Activity Count", "4", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "1 700 000", "Funding-2013-Actual Disbursements", "35 000", "Funding-2014-Actual Commitments", "3 365 760,63", "Funding-2014-Actual Disbursements", "155 000", "Funding-2015-Actual Commitments", "1 200", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "5 066 960,63", "Totals-Actual Disbursements", "190 000", "Region", "Chisinau County")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner("Primary Sector", "110 - EDUCATION", 6236), "Activity Count", "4", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "1 700 000", "Funding-2013-Actual Disbursements", "35 000", "Funding-2014-Actual Commitments", "3 365 760,63", "Funding-2014-Actual Disbursements", "155 000", "Funding-2015-Actual Commitments", "1 200", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "5 066 960,63", "Totals-Actual Disbursements", "190 000", "Primary Sector", "110 - EDUCATION")        ),
                    new ReportAreaForTests(new AreaOwner("Region", "Drochia County", 9090))
                    .withContents("Primary Sector", "", "Activity Count", "2", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "621 600", "Funding-2015-Actual Disbursements", "80 000", "Totals-Actual Commitments", "621 600", "Totals-Actual Disbursements", "80 000", "Region", "Drochia County")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner("Primary Sector", "110 - EDUCATION", 6236), "Activity Count", "2", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "372 960", "Funding-2015-Actual Disbursements", "80 000", "Totals-Actual Commitments", "372 960", "Totals-Actual Disbursements", "80 000", "Primary Sector", "110 - EDUCATION"),
                      new ReportAreaForTests(new AreaOwner("Primary Sector", "112 - BASIC EDUCATION", 6242), "Activity Count", "1", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "248 640", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "248 640", "Totals-Actual Disbursements", "0", "Primary Sector", "112 - BASIC EDUCATION")        ),
                    new ReportAreaForTests(new AreaOwner("Region", "Dubasari County", 9091))
                    .withContents("Primary Sector", "", "Activity Count", "2", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "123 321", "Funding-2011-Actual Commitments", "213 231", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "27 500", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "17 500", "Totals-Actual Commitments", "213 231", "Totals-Actual Disbursements", "168 321", "Region", "Dubasari County")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner("Primary Sector", "110 - EDUCATION", 6236), "Activity Count", "1", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "27 500", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "17 500", "Totals-Actual Commitments", "0", "Totals-Actual Disbursements", "45 000", "Primary Sector", "110 - EDUCATION"),
                      new ReportAreaForTests(new AreaOwner("Primary Sector", "112 - BASIC EDUCATION", 6242), "Activity Count", "1", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "123 321", "Funding-2011-Actual Commitments", "213 231", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "213 231", "Totals-Actual Disbursements", "123 321", "Primary Sector", "112 - BASIC EDUCATION")        ),
                    new ReportAreaForTests(new AreaOwner("Region", "Edinet County", 9092)).withContents("Primary Sector", "", "Activity Count", "1", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "567 421", "Funding-2013-Actual Disbursements", "131 845", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "567 421", "Totals-Actual Disbursements", "131 845", "Region", "Edinet County")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner("Primary Sector", "112 - BASIC EDUCATION", 6242), "Activity Count", "1", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "567 421", "Funding-2013-Actual Disbursements", "131 845", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "567 421", "Totals-Actual Disbursements", "131 845", "Primary Sector", "112 - BASIC EDUCATION")        ),
                    new ReportAreaForTests(new AreaOwner("Region", "Falesti County", 9093)).withContents("Primary Sector", "", "Activity Count", "1", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "453 213", "Funding-2011-Actual Commitments", "999 888", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "999 888", "Totals-Actual Disbursements", "453 213", "Region", "Falesti County")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner("Primary Sector", "130 - POPULATION POLICIES/PROGRAMMES AND REPRODUCTIVE HEALTH", 6267), "Activity Count", "1", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "453 213", "Funding-2011-Actual Commitments", "999 888", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "999 888", "Totals-Actual Disbursements", "453 213", "Primary Sector", "130 - POPULATION POLICIES/PROGRAMMES AND REPRODUCTIVE HEALTH")        ),
                    new ReportAreaForTests(new AreaOwner("Region", "Transnistrian Region", 9105))
                    .withContents("Primary Sector", "", "Activity Count", "2", "Funding-2006-Actual Commitments", "43 578,26", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "123 321", "Funding-2014-Actual Disbursements", "22 500", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "166 899,26", "Totals-Actual Disbursements", "22 500", "Region", "Transnistrian Region")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner("Primary Sector", "110 - EDUCATION", 6236), "Activity Count", "2", "Funding-2006-Actual Commitments", "26 146,96", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "123 321", "Funding-2014-Actual Disbursements", "13 500", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "149 467,96", "Totals-Actual Disbursements", "13 500", "Primary Sector", "110 - EDUCATION"),
                      new ReportAreaForTests(new AreaOwner("Primary Sector", "112 - BASIC EDUCATION", 6242), "Activity Count", "1", "Funding-2006-Actual Commitments", "4 357,83", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "2 250", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "4 357,83", "Totals-Actual Disbursements", "2 250", "Primary Sector", "112 - BASIC EDUCATION"),
                      new ReportAreaForTests(new AreaOwner("Primary Sector", "120 - HEALTH", 6252), "Activity Count", "1", "Funding-2006-Actual Commitments", "13 073,48", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "6 750", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "13 073,48", "Totals-Actual Disbursements", "6 750", "Primary Sector", "120 - HEALTH")        ),
                    new ReportAreaForTests(new AreaOwner("Region", "Region: Undefined", -8977))
                    .withContents("Primary Sector", "", "Activity Count", "11", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "100 000", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "60 000", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "25 000", "Funding-2012-Actual Disbursements", "12 000", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "145 732,14", "Funding-2014-Actual Disbursements", "200", "Funding-2015-Actual Commitments", "378 930,84", "Funding-2015-Actual Disbursements", "570", "Totals-Actual Commitments", "649 662,98", "Totals-Actual Disbursements", "72 770", "Region", "Region: Undefined")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner("Primary Sector", "110 - EDUCATION", 6236), "Activity Count", "9", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "100 000", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "60 000", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "25 000", "Funding-2012-Actual Disbursements", "12 000", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "120 168,92", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "322 737,76", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "567 906,68", "Totals-Actual Disbursements", "72 000", "Primary Sector", "110 - EDUCATION"),
                      new ReportAreaForTests(new AreaOwner("Primary Sector", "112 - BASIC EDUCATION", 6242), "Activity Count", "2", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "363,21", "Funding-2014-Actual Disbursements", "200", "Funding-2015-Actual Commitments", "9 393,08", "Funding-2015-Actual Disbursements", "570", "Totals-Actual Commitments", "9 756,3", "Totals-Actual Disbursements", "770", "Primary Sector", "112 - BASIC EDUCATION"),
                      new ReportAreaForTests(new AreaOwner("Primary Sector", "113 - SECONDARY EDUCATION", 6246), "Activity Count", "1", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "13 200", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "46 800", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "60 000", "Totals-Actual Disbursements", "0", "Primary Sector", "113 - SECONDARY EDUCATION"),
                      new ReportAreaForTests(new AreaOwner("Primary Sector", "Primary Sector: Undefined", -999999999), "Activity Count", "1", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "12 000", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "12 000", "Totals-Actual Disbursements", "0", "Primary Sector", "Primary Sector: Undefined")        )      ));
        
        runNiTestCase(spec("activity-count-summary-dual"), "en", acts, cor);
    }
    
    @Test
    public void testCapitalExpenditurePercentages() {
        NiReportModel cor = new NiReportModel("testCapitalExpenditurePercentages")
            .withHeaders(Arrays.asList(
                    "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 19))",
                    "(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 1, colSpan: 12));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 13, colSpan: 6))",
                    "(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 1, colSpan: 6));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 7, colSpan: 6))",
                    "(Planned Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Planned Disbursements - Capital: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Planned Disbursements - Expenditure: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Disbursements - Capital: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Disbursements - Recurrent: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Planned Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Planned Disbursements - Capital: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Planned Disbursements - Expenditure: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Actual Disbursements - Capital: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1));(Actual Disbursements - Recurrent: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1));(Planned Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 14, colSpan: 1));(Planned Disbursements - Capital: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 15, colSpan: 1));(Planned Disbursements - Expenditure: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 16, colSpan: 1));(Actual Disbursements - Capital: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 17, colSpan: 1));(Actual Disbursements - Recurrent: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 18, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                  .withContents("Project Title", "", "Funding-2010-Planned Disbursements", "0", "Funding-2010-Actual Disbursements", "123,321", "Funding-2010-Planned Disbursements - Capital", "0", "Funding-2010-Planned Disbursements - Expenditure", "0", "Funding-2010-Actual Disbursements - Capital", "0", "Funding-2010-Actual Disbursements - Recurrent", "0", "Funding-2014-Planned Disbursements", "90,000", "Funding-2014-Actual Disbursements", "80,000", "Funding-2014-Planned Disbursements - Capital", "10,800", "Funding-2014-Planned Disbursements - Expenditure", "79,200", "Funding-2014-Actual Disbursements - Capital", "27,200", "Funding-2014-Actual Disbursements - Recurrent", "52,800", "Totals-Planned Disbursements", "90,000", "Totals-Actual Disbursements", "203,321", "Totals-Planned Disbursements - Capital", "10,800", "Totals-Planned Disbursements - Expenditure", "79,200", "Totals-Actual Disbursements - Capital", "27,200", "Totals-Actual Disbursements - Recurrent", "52,800")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(12), "Project Title", "TAC_activity_1", "Funding-2010-Actual Disbursements", "123,321", "Totals-Actual Disbursements", "123,321"),
                    new ReportAreaForTests(new AreaOwner(50), "Project Title", "activity with capital spending", "Funding-2014-Planned Disbursements", "90,000", "Funding-2014-Actual Disbursements", "80,000", "Funding-2014-Planned Disbursements - Capital", "10,800", "Funding-2014-Planned Disbursements - Expenditure", "79,200", "Funding-2014-Actual Disbursements - Capital", "27,200", "Funding-2014-Actual Disbursements - Recurrent", "52,800", "Totals-Planned Disbursements", "90,000", "Totals-Actual Disbursements", "80,000", "Totals-Planned Disbursements - Capital", "10,800", "Totals-Planned Disbursements - Expenditure", "79,200", "Totals-Actual Disbursements - Capital", "27,200", "Totals-Actual Disbursements - Recurrent", "52,800")      ));

        
        ReportSpecificationImpl spec = buildSpecification("testCapitalExpenditurePercentages", 
            Arrays.asList(ColumnConstants.PROJECT_TITLE),
            Arrays.asList(MeasureConstants.PLANNED_DISBURSEMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS, MeasureConstants.PLANNED_DISBURSEMENTS_CAPITAL, MeasureConstants.PLANNED_DISBURSEMENTS_EXPENDITURE, MeasureConstants.ACTUAL_DISBURSEMENTS_CAPITAL, MeasureConstants.ACTUAL_DISBURSEMENTS_RECURRENT), 
            null, 
            GroupingCriteria.GROUPING_YEARLY);
        
        runNiTestCase(spec, "en", Arrays.asList("activity with capital spending", "TAC_activity_1"), cor);
    }
    
    @Test
    public void testProjectTitleAsSingleHierarchy() {
        NiReportModel cor = new NiReportModel("testProjectTitleAsSingleHierarchy")
        .withHeaders(Arrays.asList(
                "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 21))",
                "(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Region: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Primary Sector: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 3, colSpan: 16));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 19, colSpan: 2))",
                "(2006: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 2));(2009: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 2));(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 7, colSpan: 2));(2011: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 9, colSpan: 2));(2012: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 11, colSpan: 2));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 13, colSpan: 2));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 15, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 17, colSpan: 2))",
                "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 14, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 15, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 16, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 17, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 18, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 19, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 20, colSpan: 1))"))
            .withWarnings(Arrays.asList())
            .withBody(      new ReportAreaForTests(null)
              .withContents("Project Title", "", "Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "96,840,58", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "100,000", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "780,311", "Funding-2011-Actual Commitments", "1,213,119", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "25,000", "Funding-2012-Actual Disbursements", "12,000", "Funding-2013-Actual Commitments", "7,842,086", "Funding-2013-Actual Disbursements", "1,266,956", "Funding-2014-Actual Commitments", "8,159,813,77", "Funding-2014-Actual Disbursements", "710,200", "Funding-2015-Actual Commitments", "1,971,831,84", "Funding-2015-Actual Disbursements", "437,335", "Totals-Actual Commitments", "19,408,691,19", "Totals-Actual Disbursements", "3,206,802")
              .withChildren(
                new ReportAreaForTests(new AreaOwner("Project Title", "Activity 2 with multiple agreements", 66)).withContents("Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "1,200", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "1,200", "Totals-Actual Disbursements", "0", "Project Title", "Activity 2 with multiple agreements")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner(66), "Region", "Chisinau County", "Primary Sector", "110 - EDUCATION", "Funding-2015-Actual Commitments", "1,200", "Totals-Actual Commitments", "1,200")        ),
                new ReportAreaForTests(new AreaOwner("Project Title", "Activity Linked With Pledge", 41)).withContents("Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "50,000", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "50,000", "Totals-Actual Disbursements", "0", "Project Title", "Activity Linked With Pledge")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner(41), "Region", "Chisinau City", "Primary Sector", "110 - EDUCATION", "Funding-2014-Actual Commitments", "50,000", "Totals-Actual Commitments", "50,000")        ),
                new ReportAreaForTests(new AreaOwner("Project Title", "Activity With Zones and Percentages", 36)).withContents("Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "890,000", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "890,000", "Totals-Actual Disbursements", "0", "Project Title", "Activity With Zones and Percentages")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner(36), "Region", "Anenii Noi County, Balti County", "Primary Sector", "110 - EDUCATION, 120 - HEALTH", "Funding-2013-Actual Commitments", "890,000", "Totals-Actual Commitments", "890,000")        ),
                new ReportAreaForTests(new AreaOwner("Project Title", "Activity with Zones", 33)).withContents("Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "570,000", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "570,000", "Totals-Actual Disbursements", "0", "Project Title", "Activity with Zones")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner(33), "Region", "Anenii Noi County, Balti County", "Primary Sector", "110 - EDUCATION", "Funding-2013-Actual Commitments", "570,000", "Totals-Actual Commitments", "570,000")        ),
                new ReportAreaForTests(new AreaOwner("Project Title", "Activity with both MTEFs and Act.Comms", 70)).withContents("Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "888,000", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "888,000", "Totals-Actual Disbursements", "0", "Project Title", "Activity with both MTEFs and Act.Comms")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner(70), "Region", "Balti County, Drochia County", "Primary Sector", "110 - EDUCATION, 112 - BASIC EDUCATION", "Funding-2015-Actual Commitments", "888,000", "Totals-Actual Commitments", "888,000")        ),
                new ReportAreaForTests(new AreaOwner("Project Title", "Activity with planned disbursements", 69)).withContents("Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "200", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "570", "Totals-Actual Commitments", "0", "Totals-Actual Disbursements", "770", "Project Title", "Activity with planned disbursements")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner(69), "Region", "", "Primary Sector", "112 - BASIC EDUCATION", "Funding-2014-Actual Disbursements", "200", "Funding-2015-Actual Disbursements", "570", "Totals-Actual Disbursements", "770")        ),
                new ReportAreaForTests(new AreaOwner("Project Title", "Activity with primary_tertiary_program", 43)).withContents("Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "50,000", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "50,000", "Totals-Actual Disbursements", "0", "Project Title", "Activity with primary_tertiary_program")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner(43), "Region", "", "Primary Sector", "110 - EDUCATION", "Funding-2014-Actual Commitments", "50,000", "Totals-Actual Commitments", "50,000")        ),
                new ReportAreaForTests(new AreaOwner("Project Title", "Eth Water", 24)).withContents("Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "545,000", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "0", "Totals-Actual Disbursements", "545,000", "Project Title", "Eth Water")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner(24), "Region", "Anenii Noi County", "Primary Sector", "110 - EDUCATION", "Funding-2013-Actual Disbursements", "545,000", "Totals-Actual Disbursements", "545,000")        ),
                new ReportAreaForTests(new AreaOwner("Project Title", "SSC Project 1", 30)).withContents("Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "111,333", "Funding-2013-Actual Disbursements", "555,111", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "111,333", "Totals-Actual Disbursements", "555,111", "Project Title", "SSC Project 1")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner(30), "Region", "Anenii Noi County", "Primary Sector", "110 - EDUCATION", "Funding-2013-Actual Commitments", "111,333", "Funding-2013-Actual Disbursements", "555,111", "Totals-Actual Commitments", "111,333", "Totals-Actual Disbursements", "555,111")        ),
                new ReportAreaForTests(new AreaOwner("Project Title", "SSC Project 2", 31)).withContents("Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "567,421", "Funding-2013-Actual Disbursements", "131,845", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "567,421", "Totals-Actual Disbursements", "131,845", "Project Title", "SSC Project 2")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner(31), "Region", "Edinet County", "Primary Sector", "112 - BASIC EDUCATION", "Funding-2013-Actual Commitments", "567,421", "Funding-2013-Actual Disbursements", "131,845", "Totals-Actual Commitments", "567,421", "Totals-Actual Disbursements", "131,845")        ),
                new ReportAreaForTests(new AreaOwner("Project Title", "SubNational no percentages", 40)).withContents("Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "75,000", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "75,000", "Totals-Actual Disbursements", "0", "Project Title", "SubNational no percentages")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner(40), "Region", "Anenii Noi County, Balti County", "Primary Sector", "110 - EDUCATION", "Funding-2014-Actual Commitments", "75,000", "Totals-Actual Commitments", "75,000")        ),
                new ReportAreaForTests(new AreaOwner("Project Title", "TAC_activity_1", 12)).withContents("Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "123,321", "Funding-2011-Actual Commitments", "213,231", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "213,231", "Totals-Actual Disbursements", "123,321", "Project Title", "TAC_activity_1")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner(12), "Region", "Dubasari County", "Primary Sector", "112 - BASIC EDUCATION", "Funding-2010-Actual Disbursements", "123,321", "Funding-2011-Actual Commitments", "213,231", "Totals-Actual Commitments", "213,231", "Totals-Actual Disbursements", "123,321")        ),
                new ReportAreaForTests(new AreaOwner("Project Title", "TAC_activity_2", 13)).withContents("Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "453,213", "Funding-2011-Actual Commitments", "999,888", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "999,888", "Totals-Actual Disbursements", "453,213", "Project Title", "TAC_activity_2")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner(13), "Region", "Falesti County", "Primary Sector", "130 - POPULATION POLICIES/PROGRAMMES AND REPRODUCTIVE HEALTH", "Funding-2010-Actual Disbursements", "453,213", "Funding-2011-Actual Commitments", "999,888", "Totals-Actual Commitments", "999,888", "Totals-Actual Disbursements", "453,213")        ),
                new ReportAreaForTests(new AreaOwner("Project Title", "Test MTEF directed", 18)).withContents("Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "143,777", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "0", "Totals-Actual Disbursements", "143,777", "Project Title", "Test MTEF directed")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner(18), "Region", "Anenii Noi County", "Primary Sector", "110 - EDUCATION", "Funding-2010-Actual Disbursements", "143,777", "Totals-Actual Disbursements", "143,777")        ),
                new ReportAreaForTests(new AreaOwner("Project Title", "Unvalidated activity", 64)).withContents("Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "45,000", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "45,000", "Totals-Actual Disbursements", "0", "Project Title", "Unvalidated activity")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner(64), "Region", "", "Primary Sector", "110 - EDUCATION", "Funding-2015-Actual Commitments", "45,000", "Totals-Actual Commitments", "45,000")        ),
                new ReportAreaForTests(new AreaOwner("Project Title", "activity 1 with agreement", 65)).withContents("Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "456,789", "Funding-2015-Actual Disbursements", "321,765", "Totals-Actual Commitments", "456,789", "Totals-Actual Disbursements", "321,765", "Project Title", "activity 1 with agreement")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner(65), "Region", "Balti County", "Primary Sector", "110 - EDUCATION, 112 - BASIC EDUCATION", "Funding-2015-Actual Commitments", "456,789", "Funding-2015-Actual Disbursements", "321,765", "Totals-Actual Commitments", "456,789", "Totals-Actual Disbursements", "321,765")        ),
                new ReportAreaForTests(new AreaOwner("Project Title", "activity with capital spending", 50)).withContents("Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "65,760,63", "Funding-2014-Actual Disbursements", "80,000", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "65,760,63", "Totals-Actual Disbursements", "80,000", "Project Title", "activity with capital spending")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner(50), "Region", "Chisinau County", "Primary Sector", "110 - EDUCATION", "Funding-2014-Actual Commitments", "65,760,63", "Funding-2014-Actual Disbursements", "80,000", "Totals-Actual Commitments", "65,760,63", "Totals-Actual Disbursements", "80,000")        ),
                new ReportAreaForTests(new AreaOwner("Project Title", "activity with contracting agency", 52)).withContents("Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "96,840,58", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "50,000", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "96,840,58", "Totals-Actual Disbursements", "50,000", "Project Title", "activity with contracting agency")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner(52), "Region", "Balti County, Transnistrian Region", "Primary Sector", "110 - EDUCATION, 112 - BASIC EDUCATION, 120 - HEALTH", "Funding-2006-Actual Commitments", "96,840,58", "Funding-2014-Actual Disbursements", "50,000", "Totals-Actual Commitments", "96,840,58", "Totals-Actual Disbursements", "50,000")        ),
                new ReportAreaForTests(new AreaOwner("Project Title", "activity with directed MTEFs", 73)).withContents("Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "123,456", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "123,456", "Totals-Actual Disbursements", "0", "Project Title", "activity with directed MTEFs")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner(73), "Region", "Chisinau City", "Primary Sector", "110 - EDUCATION", "Funding-2015-Actual Commitments", "123,456", "Totals-Actual Commitments", "123,456")        ),
                new ReportAreaForTests(new AreaOwner("Project Title", "activity with funded components", 63)).withContents("Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "100", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "100", "Totals-Actual Disbursements", "0", "Project Title", "activity with funded components")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner(63), "Region", "", "Primary Sector", "110 - EDUCATION", "Funding-2014-Actual Commitments", "100", "Totals-Actual Commitments", "100")        ),
                new ReportAreaForTests(new AreaOwner("Project Title", "activity with incomplete agreement", 68)).withContents("Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "123,000", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "123,000", "Totals-Actual Disbursements", "0", "Project Title", "activity with incomplete agreement")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner(68), "Region", "", "Primary Sector", "110 - EDUCATION", "Funding-2015-Actual Commitments", "123,000", "Totals-Actual Commitments", "123,000")        ),
                new ReportAreaForTests(new AreaOwner("Project Title", "activity with many MTEFs", 78)).withContents("Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "80,000", "Totals-Actual Commitments", "0", "Totals-Actual Disbursements", "80,000", "Project Title", "activity with many MTEFs")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner(78), "Region", "Drochia County", "Primary Sector", "110 - EDUCATION", "Funding-2015-Actual Disbursements", "80,000", "Totals-Actual Disbursements", "80,000")        ),
                new ReportAreaForTests(new AreaOwner("Project Title", "activity with pipeline MTEFs and act. disb", 76)).withContents("Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "35,000", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "75,000", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "0", "Totals-Actual Disbursements", "110,000", "Project Title", "activity with pipeline MTEFs and act. disb")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner(76), "Region", "Chisinau County", "Primary Sector", "110 - EDUCATION", "Funding-2013-Actual Disbursements", "35,000", "Funding-2014-Actual Disbursements", "75,000", "Totals-Actual Disbursements", "110,000")        ),
                new ReportAreaForTests(new AreaOwner("Project Title", "activity with primary_program", 44)).withContents("Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "32,000", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "32,000", "Totals-Actual Disbursements", "0", "Project Title", "activity with primary_program")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner(44), "Region", "", "Primary Sector", "110 - EDUCATION", "Funding-2014-Actual Commitments", "32,000", "Totals-Actual Commitments", "32,000")        ),
                new ReportAreaForTests(new AreaOwner("Project Title", "activity with tertiary_program", 45)).withContents("Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "15,000", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "15,000", "Totals-Actual Disbursements", "0", "Project Title", "activity with tertiary_program")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner(45), "Region", "", "Primary Sector", "110 - EDUCATION", "Funding-2014-Actual Commitments", "15,000", "Totals-Actual Commitments", "15,000")        ),
                new ReportAreaForTests(new AreaOwner("Project Title", "activity-with-unfunded-components", 61)).withContents("Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "123,321", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "123,321", "Totals-Actual Disbursements", "0", "Project Title", "activity-with-unfunded-components")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner(61), "Region", "Transnistrian Region", "Primary Sector", "110 - EDUCATION", "Funding-2014-Actual Commitments", "123,321", "Totals-Actual Commitments", "123,321")        ),
                new ReportAreaForTests(new AreaOwner("Project Title", "activity_with_disaster_response", 71)).withContents("Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "33,000", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "117,000", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "150,000", "Totals-Actual Disbursements", "0", "Project Title", "activity_with_disaster_response")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner(71), "Region", "", "Primary Sector", "110 - EDUCATION, 113 - SECONDARY EDUCATION", "Funding-2014-Actual Commitments", "33,000", "Funding-2015-Actual Commitments", "117,000", "Totals-Actual Commitments", "150,000")        ),
                new ReportAreaForTests(new AreaOwner("Project Title", "crazy funding 1", 32)).withContents("Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "333,333", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "333,333", "Totals-Actual Disbursements", "0", "Project Title", "crazy funding 1")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner(32), "Region", "Balti County", "Primary Sector", "110 - EDUCATION", "Funding-2013-Actual Commitments", "333,333", "Totals-Actual Commitments", "333,333")        ),
                new ReportAreaForTests(new AreaOwner("Project Title", "date-filters-activity", 26)).withContents("Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "100,000", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "60,000", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "25,000", "Funding-2012-Actual Disbursements", "12,000", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "125,000", "Totals-Actual Disbursements", "72,000", "Project Title", "date-filters-activity")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner(26), "Region", "", "Primary Sector", "110 - EDUCATION", "Funding-2009-Actual Commitments", "100,000", "Funding-2010-Actual Disbursements", "60,000", "Funding-2012-Actual Commitments", "25,000", "Funding-2012-Actual Disbursements", "12,000", "Totals-Actual Commitments", "125,000", "Totals-Actual Disbursements", "72,000")        ),
                new ReportAreaForTests(new AreaOwner("Project Title", "execution rate activity", 77)).withContents("Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "55,000", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "35,000", "Totals-Actual Commitments", "0", "Totals-Actual Disbursements", "90,000", "Project Title", "execution rate activity")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner(77), "Region", "Chisinau City, Dubasari County", "Primary Sector", "110 - EDUCATION", "Funding-2014-Actual Disbursements", "55,000", "Funding-2015-Actual Disbursements", "35,000", "Totals-Actual Disbursements", "90,000")        ),
                new ReportAreaForTests(new AreaOwner("Project Title", "new activity with contracting", 53)).withContents("Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "12,000", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "12,000", "Totals-Actual Disbursements", "0", "Project Title", "new activity with contracting")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner(53), "Region", "", "Primary Sector", "", "Funding-2014-Actual Commitments", "12,000", "Totals-Actual Commitments", "12,000")        ),
                new ReportAreaForTests(new AreaOwner("Project Title", "pledged 2", 48)).withContents("Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "2,670,000", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "4,400,000", "Funding-2014-Actual Disbursements", "450,000", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "7,070,000", "Totals-Actual Disbursements", "450,000", "Project Title", "pledged 2")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner(48), "Region", "Cahul County", "Primary Sector", "113 - SECONDARY EDUCATION", "Funding-2013-Actual Commitments", "2,670,000", "Funding-2014-Actual Commitments", "4,400,000", "Funding-2014-Actual Disbursements", "450,000", "Totals-Actual Commitments", "7,070,000", "Totals-Actual Disbursements", "450,000")        ),
                new ReportAreaForTests(new AreaOwner("Project Title", "pledged education activity 1", 46)).withContents("Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "1,700,000", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "3,300,000", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "5,000,000", "Totals-Actual Disbursements", "0", "Project Title", "pledged education activity 1")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner(46), "Region", "Chisinau County", "Primary Sector", "110 - EDUCATION", "Funding-2013-Actual Commitments", "1,700,000", "Funding-2014-Actual Commitments", "3,300,000", "Totals-Actual Commitments", "5,000,000")        ),
                new ReportAreaForTests(new AreaOwner("Project Title", "ptc activity 1", 28)).withContents("Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "666,777", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "666,777", "Totals-Actual Disbursements", "0", "Project Title", "ptc activity 1")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner(28), "Region", "Anenii Noi County", "Primary Sector", "110 - EDUCATION", "Funding-2013-Actual Commitments", "666,777", "Totals-Actual Commitments", "666,777")        ),
                new ReportAreaForTests(new AreaOwner("Project Title", "ptc activity 2", 29)).withContents("Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "333,222", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "333,222", "Totals-Actual Disbursements", "0", "Project Title", "ptc activity 2")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner(29), "Region", "Anenii Noi County", "Primary Sector", "110 - EDUCATION", "Funding-2013-Actual Commitments", "333,222", "Totals-Actual Commitments", "333,222")        ),
                new ReportAreaForTests(new AreaOwner("Project Title", "third activity with agreements", 67)).withContents("Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "123,456", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "123,456", "Totals-Actual Disbursements", "0", "Project Title", "third activity with agreements")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner(67), "Region", "Chisinau City", "Primary Sector", "110 - EDUCATION", "Funding-2015-Actual Commitments", "123,456", "Totals-Actual Commitments", "123,456")        ),
                new ReportAreaForTests(new AreaOwner("Project Title", "with weird currencies", 79)).withContents("Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "3,632,14", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "93,930,84", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "97,562,98", "Totals-Actual Disbursements", "0", "Project Title", "with weird currencies")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner(79), "Region", "", "Primary Sector", "110 - EDUCATION, 112 - BASIC EDUCATION", "Funding-2014-Actual Commitments", "3,632,14", "Funding-2015-Actual Commitments", "93,930,84", "Totals-Actual Commitments", "97,562,98")        )      ));

        
        ReportSpecificationImpl spec = buildSpecification("testProjectTitleAsSingleHierarchy",
            Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.REGION, ColumnConstants.PRIMARY_SECTOR),
            Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS),
            Arrays.asList(ColumnConstants.PROJECT_TITLE),
            GroupingCriteria.GROUPING_YEARLY);
        
        runNiTestCase(spec, "en", acts, cor);
    }
    
    @Test
    public void testProjectTitleAsFirstHierarchy() {
        NiReportModel cor = new NiReportModel("testProjectTitleAsFirstHierarchy")
        .withHeaders(Arrays.asList(
                "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 22))",
                "(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Donor Agency: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Region: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1));(Primary Sector: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 3, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 4, colSpan: 16));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 20, colSpan: 2))",
                "(2006: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 2));(2009: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 6, colSpan: 2));(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 8, colSpan: 2));(2011: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 10, colSpan: 2));(2012: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 12, colSpan: 2));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 14, colSpan: 2));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 16, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 18, colSpan: 2))",
                "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 14, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 15, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 16, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 17, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 18, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 19, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 20, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 21, colSpan: 1))"))
            .withWarnings(Arrays.asList())
            .withBody(      new ReportAreaForTests(null)
              .withContents("Project Title", "", "Donor Agency", "", "Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "96,840,58", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "100,000", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "780,311", "Funding-2011-Actual Commitments", "1,213,119", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "25,000", "Funding-2012-Actual Disbursements", "12,000", "Funding-2013-Actual Commitments", "7,842,086", "Funding-2013-Actual Disbursements", "1,266,956", "Funding-2014-Actual Commitments", "8,159,813,77", "Funding-2014-Actual Disbursements", "710,200", "Funding-2015-Actual Commitments", "1,971,831,84", "Funding-2015-Actual Disbursements", "437,335", "Totals-Actual Commitments", "19,408,691,19", "Totals-Actual Disbursements", "3,206,802")
              .withChildren(
                new ReportAreaForTests(new AreaOwner("Project Title", "Activity 2 with multiple agreements", 66))
                .withContents("Donor Agency", "", "Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "1,200", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "1,200", "Totals-Actual Disbursements", "0", "Project Title", "Activity 2 with multiple agreements")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner("Donor Agency", "UNDP", 21695)).withContents("Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "700", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "700", "Totals-Actual Disbursements", "0", "Donor Agency", "UNDP")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(66), "Region", "Chisinau County", "Primary Sector", "110 - EDUCATION", "Funding-2015-Actual Commitments", "700", "Totals-Actual Commitments", "700")          ),
                  new ReportAreaForTests(new AreaOwner("Donor Agency", "USAID", 21696)).withContents("Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "500", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "500", "Totals-Actual Disbursements", "0", "Donor Agency", "USAID")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(66), "Region", "Chisinau County", "Primary Sector", "110 - EDUCATION", "Funding-2015-Actual Commitments", "500", "Totals-Actual Commitments", "500")          )        ),
                new ReportAreaForTests(new AreaOwner("Project Title", "Activity Linked With Pledge", 41)).withContents("Donor Agency", "", "Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "50,000", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "50,000", "Totals-Actual Disbursements", "0", "Project Title", "Activity Linked With Pledge")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner("Donor Agency", "Finland", 21698)).withContents("Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "50,000", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "50,000", "Totals-Actual Disbursements", "0", "Donor Agency", "Finland")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(41), "Region", "Chisinau City", "Primary Sector", "110 - EDUCATION", "Funding-2014-Actual Commitments", "50,000", "Totals-Actual Commitments", "50,000")          )        ),
                new ReportAreaForTests(new AreaOwner("Project Title", "Activity With Zones and Percentages", 36)).withContents("Donor Agency", "", "Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "890,000", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "890,000", "Totals-Actual Disbursements", "0", "Project Title", "Activity With Zones and Percentages")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner("Donor Agency", "Norway", 21694)).withContents("Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "890,000", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "890,000", "Totals-Actual Disbursements", "0", "Donor Agency", "Norway")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(36), "Region", "Anenii Noi County, Balti County", "Primary Sector", "110 - EDUCATION, 120 - HEALTH", "Funding-2013-Actual Commitments", "890,000", "Totals-Actual Commitments", "890,000")          )        ),
                new ReportAreaForTests(new AreaOwner("Project Title", "Activity with Zones", 33)).withContents("Donor Agency", "", "Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "570,000", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "570,000", "Totals-Actual Disbursements", "0", "Project Title", "Activity with Zones")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner("Donor Agency", "Finland", 21698)).withContents("Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "570,000", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "570,000", "Totals-Actual Disbursements", "0", "Donor Agency", "Finland")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(33), "Region", "Anenii Noi County, Balti County", "Primary Sector", "110 - EDUCATION", "Funding-2013-Actual Commitments", "570,000", "Totals-Actual Commitments", "570,000")          )        ),
                new ReportAreaForTests(new AreaOwner("Project Title", "Activity with both MTEFs and Act.Comms", 70)).withContents("Donor Agency", "", "Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "888,000", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "888,000", "Totals-Actual Disbursements", "0", "Project Title", "Activity with both MTEFs and Act.Comms")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner("Donor Agency", "Finland", 21698)).withContents("Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "888,000", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "888,000", "Totals-Actual Disbursements", "0", "Donor Agency", "Finland")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(70), "Region", "Balti County, Drochia County", "Primary Sector", "110 - EDUCATION, 112 - BASIC EDUCATION", "Funding-2015-Actual Commitments", "888,000", "Totals-Actual Commitments", "888,000")          )        ),
                new ReportAreaForTests(new AreaOwner("Project Title", "Activity with planned disbursements", 69))
                .withContents("Donor Agency", "", "Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "200", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "570", "Totals-Actual Commitments", "0", "Totals-Actual Disbursements", "770", "Project Title", "Activity with planned disbursements")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner("Donor Agency", "Norway", 21694)).withContents("Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "200", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "500", "Totals-Actual Commitments", "0", "Totals-Actual Disbursements", "700", "Donor Agency", "Norway")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(69), "Region", "", "Primary Sector", "112 - BASIC EDUCATION", "Funding-2014-Actual Disbursements", "200", "Funding-2015-Actual Disbursements", "500", "Totals-Actual Disbursements", "700")          ),
                  new ReportAreaForTests(new AreaOwner("Donor Agency", "USAID", 21696)).withContents("Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "70", "Totals-Actual Commitments", "0", "Totals-Actual Disbursements", "70", "Donor Agency", "USAID")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(69), "Region", "", "Primary Sector", "112 - BASIC EDUCATION", "Funding-2015-Actual Disbursements", "70", "Totals-Actual Disbursements", "70")          )        ),
                new ReportAreaForTests(new AreaOwner("Project Title", "Activity with primary_tertiary_program", 43)).withContents("Donor Agency", "", "Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "50,000", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "50,000", "Totals-Actual Disbursements", "0", "Project Title", "Activity with primary_tertiary_program")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner("Donor Agency", "UNDP", 21695)).withContents("Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "50,000", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "50,000", "Totals-Actual Disbursements", "0", "Donor Agency", "UNDP")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(43), "Region", "", "Primary Sector", "110 - EDUCATION", "Funding-2014-Actual Commitments", "50,000", "Totals-Actual Commitments", "50,000")          )        ),
                new ReportAreaForTests(new AreaOwner("Project Title", "Eth Water", 24))
                .withContents("Donor Agency", "", "Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "545,000", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "0", "Totals-Actual Disbursements", "545,000", "Project Title", "Eth Water")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner("Donor Agency", "Finland", 21698)).withContents("Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "20,000", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "0", "Totals-Actual Disbursements", "20,000", "Donor Agency", "Finland")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(24), "Region", "Anenii Noi County", "Primary Sector", "110 - EDUCATION", "Funding-2013-Actual Disbursements", "20,000", "Totals-Actual Disbursements", "20,000")          ),
                  new ReportAreaForTests(new AreaOwner("Donor Agency", "Norway", 21694)).withContents("Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "110,000", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "0", "Totals-Actual Disbursements", "110,000", "Donor Agency", "Norway")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(24), "Region", "Anenii Noi County", "Primary Sector", "110 - EDUCATION", "Funding-2013-Actual Disbursements", "110,000", "Totals-Actual Disbursements", "110,000")          ),
                  new ReportAreaForTests(new AreaOwner("Donor Agency", "USAID", 21696)).withContents("Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "415,000", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "0", "Totals-Actual Disbursements", "415,000", "Donor Agency", "USAID")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(24), "Region", "Anenii Noi County", "Primary Sector", "110 - EDUCATION", "Funding-2013-Actual Disbursements", "415,000", "Totals-Actual Disbursements", "415,000")          )        ),
                new ReportAreaForTests(new AreaOwner("Project Title", "SSC Project 1", 30)).withContents("Donor Agency", "", "Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "111,333", "Funding-2013-Actual Disbursements", "555,111", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "111,333", "Totals-Actual Disbursements", "555,111", "Project Title", "SSC Project 1")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner("Donor Agency", "Finland", 21698)).withContents("Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "111,333", "Funding-2013-Actual Disbursements", "555,111", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "111,333", "Totals-Actual Disbursements", "555,111", "Donor Agency", "Finland")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(30), "Region", "Anenii Noi County", "Primary Sector", "110 - EDUCATION", "Funding-2013-Actual Commitments", "111,333", "Funding-2013-Actual Disbursements", "555,111", "Totals-Actual Commitments", "111,333", "Totals-Actual Disbursements", "555,111")          )        ),
                new ReportAreaForTests(new AreaOwner("Project Title", "SSC Project 2", 31)).withContents("Donor Agency", "", "Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "567,421", "Funding-2013-Actual Disbursements", "131,845", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "567,421", "Totals-Actual Disbursements", "131,845", "Project Title", "SSC Project 2")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner("Donor Agency", "Water Org", 21701)).withContents("Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "567,421", "Funding-2013-Actual Disbursements", "131,845", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "567,421", "Totals-Actual Disbursements", "131,845", "Donor Agency", "Water Org")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(31), "Region", "Edinet County", "Primary Sector", "112 - BASIC EDUCATION", "Funding-2013-Actual Commitments", "567,421", "Funding-2013-Actual Disbursements", "131,845", "Totals-Actual Commitments", "567,421", "Totals-Actual Disbursements", "131,845")          )        ),
                new ReportAreaForTests(new AreaOwner("Project Title", "SubNational no percentages", 40)).withContents("Donor Agency", "", "Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "75,000", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "75,000", "Totals-Actual Disbursements", "0", "Project Title", "SubNational no percentages")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner("Donor Agency", "Ministry of Economy", 21700)).withContents("Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "75,000", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "75,000", "Totals-Actual Disbursements", "0", "Donor Agency", "Ministry of Economy")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(40), "Region", "Anenii Noi County, Balti County", "Primary Sector", "110 - EDUCATION", "Funding-2014-Actual Commitments", "75,000", "Totals-Actual Commitments", "75,000")          )        ),
                new ReportAreaForTests(new AreaOwner("Project Title", "TAC_activity_1", 12)).withContents("Donor Agency", "", "Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "123,321", "Funding-2011-Actual Commitments", "213,231", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "213,231", "Totals-Actual Disbursements", "123,321", "Project Title", "TAC_activity_1")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner("Donor Agency", "World Bank", 21697)).withContents("Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "123,321", "Funding-2011-Actual Commitments", "213,231", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "213,231", "Totals-Actual Disbursements", "123,321", "Donor Agency", "World Bank")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(12), "Region", "Dubasari County", "Primary Sector", "112 - BASIC EDUCATION", "Funding-2010-Actual Disbursements", "123,321", "Funding-2011-Actual Commitments", "213,231", "Totals-Actual Commitments", "213,231", "Totals-Actual Disbursements", "123,321")          )        ),
                new ReportAreaForTests(new AreaOwner("Project Title", "TAC_activity_2", 13)).withContents("Donor Agency", "", "Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "453,213", "Funding-2011-Actual Commitments", "999,888", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "999,888", "Totals-Actual Disbursements", "453,213", "Project Title", "TAC_activity_2")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner("Donor Agency", "Water Foundation", 21702)).withContents("Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "453,213", "Funding-2011-Actual Commitments", "999,888", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "999,888", "Totals-Actual Disbursements", "453,213", "Donor Agency", "Water Foundation")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(13), "Region", "Falesti County", "Primary Sector", "130 - POPULATION POLICIES/PROGRAMMES AND REPRODUCTIVE HEALTH", "Funding-2010-Actual Disbursements", "453,213", "Funding-2011-Actual Commitments", "999,888", "Totals-Actual Commitments", "999,888", "Totals-Actual Disbursements", "453,213")          )        ),
                new ReportAreaForTests(new AreaOwner("Project Title", "Test MTEF directed", 18)).withContents("Donor Agency", "", "Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "143,777", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "0", "Totals-Actual Disbursements", "143,777", "Project Title", "Test MTEF directed")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner("Donor Agency", "Ministry of Economy", 21700)).withContents("Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "143,777", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "0", "Totals-Actual Disbursements", "143,777", "Donor Agency", "Ministry of Economy")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(18), "Region", "Anenii Noi County", "Primary Sector", "110 - EDUCATION", "Funding-2010-Actual Disbursements", "143,777", "Totals-Actual Disbursements", "143,777")          )        ),
                new ReportAreaForTests(new AreaOwner("Project Title", "Unvalidated activity", 64)).withContents("Donor Agency", "", "Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "45,000", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "45,000", "Totals-Actual Disbursements", "0", "Project Title", "Unvalidated activity")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner("Donor Agency", "UNDP", 21695)).withContents("Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "45,000", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "45,000", "Totals-Actual Disbursements", "0", "Donor Agency", "UNDP")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(64), "Region", "", "Primary Sector", "110 - EDUCATION", "Funding-2015-Actual Commitments", "45,000", "Totals-Actual Commitments", "45,000")          )        ),
                new ReportAreaForTests(new AreaOwner("Project Title", "activity 1 with agreement", 65)).withContents("Donor Agency", "", "Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "456,789", "Funding-2015-Actual Disbursements", "321,765", "Totals-Actual Commitments", "456,789", "Totals-Actual Disbursements", "321,765", "Project Title", "activity 1 with agreement")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner("Donor Agency", "Finland", 21698)).withContents("Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "456,789", "Funding-2015-Actual Disbursements", "321,765", "Totals-Actual Commitments", "456,789", "Totals-Actual Disbursements", "321,765", "Donor Agency", "Finland")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(65), "Region", "Balti County", "Primary Sector", "110 - EDUCATION, 112 - BASIC EDUCATION", "Funding-2015-Actual Commitments", "456,789", "Funding-2015-Actual Disbursements", "321,765", "Totals-Actual Commitments", "456,789", "Totals-Actual Disbursements", "321,765")          )        ),
                new ReportAreaForTests(new AreaOwner("Project Title", "activity with capital spending", 50)).withContents("Donor Agency", "", "Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "65,760,63", "Funding-2014-Actual Disbursements", "80,000", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "65,760,63", "Totals-Actual Disbursements", "80,000", "Project Title", "activity with capital spending")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner("Donor Agency", "Finland", 21698)).withContents("Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "65,760,63", "Funding-2014-Actual Disbursements", "80,000", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "65,760,63", "Totals-Actual Disbursements", "80,000", "Donor Agency", "Finland")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(50), "Region", "Chisinau County", "Primary Sector", "110 - EDUCATION", "Funding-2014-Actual Commitments", "65,760,63", "Funding-2014-Actual Disbursements", "80,000", "Totals-Actual Commitments", "65,760,63", "Totals-Actual Disbursements", "80,000")          )        ),
                new ReportAreaForTests(new AreaOwner("Project Title", "activity with contracting agency", 52)).withContents("Donor Agency", "", "Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "96,840,58", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "50,000", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "96,840,58", "Totals-Actual Disbursements", "50,000", "Project Title", "activity with contracting agency")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner("Donor Agency", "Finland", 21698)).withContents("Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "96,840,58", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "50,000", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "96,840,58", "Totals-Actual Disbursements", "50,000", "Donor Agency", "Finland")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(52), "Region", "Balti County, Transnistrian Region", "Primary Sector", "110 - EDUCATION, 112 - BASIC EDUCATION, 120 - HEALTH", "Funding-2006-Actual Commitments", "96,840,58", "Funding-2014-Actual Disbursements", "50,000", "Totals-Actual Commitments", "96,840,58", "Totals-Actual Disbursements", "50,000")          )        ),
                new ReportAreaForTests(new AreaOwner("Project Title", "activity with directed MTEFs", 73)).withContents("Donor Agency", "", "Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "123,456", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "123,456", "Totals-Actual Disbursements", "0", "Project Title", "activity with directed MTEFs")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner("Donor Agency", "Ministry of Economy", 21700)).withContents("Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "123,456", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "123,456", "Totals-Actual Disbursements", "0", "Donor Agency", "Ministry of Economy")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(73), "Region", "Chisinau City", "Primary Sector", "110 - EDUCATION", "Funding-2015-Actual Commitments", "123,456", "Totals-Actual Commitments", "123,456")          )        ),
                new ReportAreaForTests(new AreaOwner("Project Title", "activity with funded components", 63)).withContents("Donor Agency", "", "Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "100", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "100", "Totals-Actual Disbursements", "0", "Project Title", "activity with funded components")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner("Donor Agency", "UNDP", 21695)).withContents("Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "100", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "100", "Totals-Actual Disbursements", "0", "Donor Agency", "UNDP")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(63), "Region", "", "Primary Sector", "110 - EDUCATION", "Funding-2014-Actual Commitments", "100", "Totals-Actual Commitments", "100")          )        ),
                new ReportAreaForTests(new AreaOwner("Project Title", "activity with incomplete agreement", 68)).withContents("Donor Agency", "", "Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "123,000", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "123,000", "Totals-Actual Disbursements", "0", "Project Title", "activity with incomplete agreement")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner("Donor Agency", "Finland", 21698)).withContents("Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "123,000", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "123,000", "Totals-Actual Disbursements", "0", "Donor Agency", "Finland")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(68), "Region", "", "Primary Sector", "110 - EDUCATION", "Funding-2015-Actual Commitments", "123,000", "Totals-Actual Commitments", "123,000")          )        ),
                new ReportAreaForTests(new AreaOwner("Project Title", "activity with many MTEFs", 78)).withContents("Donor Agency", "", "Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "80,000", "Totals-Actual Commitments", "0", "Totals-Actual Disbursements", "80,000", "Project Title", "activity with many MTEFs")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner("Donor Agency", "USAID", 21696)).withContents("Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "80,000", "Totals-Actual Commitments", "0", "Totals-Actual Disbursements", "80,000", "Donor Agency", "USAID")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(78), "Region", "Drochia County", "Primary Sector", "110 - EDUCATION", "Funding-2015-Actual Disbursements", "80,000", "Totals-Actual Disbursements", "80,000")          )        ),
                new ReportAreaForTests(new AreaOwner("Project Title", "activity with pipeline MTEFs and act. disb", 76)).withContents("Donor Agency", "", "Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "35,000", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "75,000", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "0", "Totals-Actual Disbursements", "110,000", "Project Title", "activity with pipeline MTEFs and act. disb")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner("Donor Agency", "UNDP", 21695)).withContents("Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "35,000", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "75,000", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "0", "Totals-Actual Disbursements", "110,000", "Donor Agency", "UNDP")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(76), "Region", "Chisinau County", "Primary Sector", "110 - EDUCATION", "Funding-2013-Actual Disbursements", "35,000", "Funding-2014-Actual Disbursements", "75,000", "Totals-Actual Disbursements", "110,000")          )        ),
                new ReportAreaForTests(new AreaOwner("Project Title", "activity with primary_program", 44)).withContents("Donor Agency", "", "Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "32,000", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "32,000", "Totals-Actual Disbursements", "0", "Project Title", "activity with primary_program")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner("Donor Agency", "World Bank", 21697)).withContents("Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "32,000", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "32,000", "Totals-Actual Disbursements", "0", "Donor Agency", "World Bank")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(44), "Region", "", "Primary Sector", "110 - EDUCATION", "Funding-2014-Actual Commitments", "32,000", "Totals-Actual Commitments", "32,000")          )        ),
                new ReportAreaForTests(new AreaOwner("Project Title", "activity with tertiary_program", 45)).withContents("Donor Agency", "", "Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "15,000", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "15,000", "Totals-Actual Disbursements", "0", "Project Title", "activity with tertiary_program")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner("Donor Agency", "Ministry of Economy", 21700)).withContents("Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "15,000", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "15,000", "Totals-Actual Disbursements", "0", "Donor Agency", "Ministry of Economy")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(45), "Region", "", "Primary Sector", "110 - EDUCATION", "Funding-2014-Actual Commitments", "15,000", "Totals-Actual Commitments", "15,000")          )        ),
                new ReportAreaForTests(new AreaOwner("Project Title", "activity-with-unfunded-components", 61)).withContents("Donor Agency", "", "Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "123,321", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "123,321", "Totals-Actual Disbursements", "0", "Project Title", "activity-with-unfunded-components")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner("Donor Agency", "Finland", 21698)).withContents("Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "123,321", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "123,321", "Totals-Actual Disbursements", "0", "Donor Agency", "Finland")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(61), "Region", "Transnistrian Region", "Primary Sector", "110 - EDUCATION", "Funding-2014-Actual Commitments", "123,321", "Totals-Actual Commitments", "123,321")          )        ),
                new ReportAreaForTests(new AreaOwner("Project Title", "activity_with_disaster_response", 71)).withContents("Donor Agency", "", "Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "33,000", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "117,000", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "150,000", "Totals-Actual Disbursements", "0", "Project Title", "activity_with_disaster_response")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner("Donor Agency", "Finland", 21698)).withContents("Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "33,000", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "117,000", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "150,000", "Totals-Actual Disbursements", "0", "Donor Agency", "Finland")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(71), "Region", "", "Primary Sector", "110 - EDUCATION, 113 - SECONDARY EDUCATION", "Funding-2014-Actual Commitments", "33,000", "Funding-2015-Actual Commitments", "117,000", "Totals-Actual Commitments", "150,000")          )        ),
                new ReportAreaForTests(new AreaOwner("Project Title", "crazy funding 1", 32)).withContents("Donor Agency", "", "Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "333,333", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "333,333", "Totals-Actual Disbursements", "0", "Project Title", "crazy funding 1")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner("Donor Agency", "Finland", 21698)).withContents("Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "333,333", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "333,333", "Totals-Actual Disbursements", "0", "Donor Agency", "Finland")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(32), "Region", "Balti County", "Primary Sector", "110 - EDUCATION", "Funding-2013-Actual Commitments", "333,333", "Totals-Actual Commitments", "333,333")          )        ),
                new ReportAreaForTests(new AreaOwner("Project Title", "date-filters-activity", 26)).withContents("Donor Agency", "", "Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "100,000", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "60,000", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "25,000", "Funding-2012-Actual Disbursements", "12,000", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "125,000", "Totals-Actual Disbursements", "72,000", "Project Title", "date-filters-activity")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner("Donor Agency", "Ministry of Finance", 21699)).withContents("Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "100,000", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "60,000", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "25,000", "Funding-2012-Actual Disbursements", "12,000", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "125,000", "Totals-Actual Disbursements", "72,000", "Donor Agency", "Ministry of Finance")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(26), "Region", "", "Primary Sector", "110 - EDUCATION", "Funding-2009-Actual Commitments", "100,000", "Funding-2010-Actual Disbursements", "60,000", "Funding-2012-Actual Commitments", "25,000", "Funding-2012-Actual Disbursements", "12,000", "Totals-Actual Commitments", "125,000", "Totals-Actual Disbursements", "72,000")          )        ),
                new ReportAreaForTests(new AreaOwner("Project Title", "execution rate activity", 77)).withContents("Donor Agency", "", "Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "55,000", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "35,000", "Totals-Actual Commitments", "0", "Totals-Actual Disbursements", "90,000", "Project Title", "execution rate activity")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner("Donor Agency", "Ministry of Finance", 21699)).withContents("Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "55,000", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "35,000", "Totals-Actual Commitments", "0", "Totals-Actual Disbursements", "90,000", "Donor Agency", "Ministry of Finance")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(77), "Region", "Chisinau City, Dubasari County", "Primary Sector", "110 - EDUCATION", "Funding-2014-Actual Disbursements", "55,000", "Funding-2015-Actual Disbursements", "35,000", "Totals-Actual Disbursements", "90,000")          )        ),
                new ReportAreaForTests(new AreaOwner("Project Title", "new activity with contracting", 53)).withContents("Donor Agency", "", "Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "12,000", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "12,000", "Totals-Actual Disbursements", "0", "Project Title", "new activity with contracting")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner("Donor Agency", "Finland", 21698)).withContents("Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "12,000", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "12,000", "Totals-Actual Disbursements", "0", "Donor Agency", "Finland")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(53), "Region", "", "Primary Sector", "", "Funding-2014-Actual Commitments", "12,000", "Totals-Actual Commitments", "12,000")          )        ),
                new ReportAreaForTests(new AreaOwner("Project Title", "pledged 2", 48)).withContents("Donor Agency", "", "Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "2,670,000", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "4,400,000", "Funding-2014-Actual Disbursements", "450,000", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "7,070,000", "Totals-Actual Disbursements", "450,000", "Project Title", "pledged 2")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner("Donor Agency", "USAID", 21696)).withContents("Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "2,670,000", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "4,400,000", "Funding-2014-Actual Disbursements", "450,000", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "7,070,000", "Totals-Actual Disbursements", "450,000", "Donor Agency", "USAID")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(48), "Region", "Cahul County", "Primary Sector", "113 - SECONDARY EDUCATION", "Funding-2013-Actual Commitments", "2,670,000", "Funding-2014-Actual Commitments", "4,400,000", "Funding-2014-Actual Disbursements", "450,000", "Totals-Actual Commitments", "7,070,000", "Totals-Actual Disbursements", "450,000")          )        ),
                new ReportAreaForTests(new AreaOwner("Project Title", "pledged education activity 1", 46)).withContents("Donor Agency", "", "Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "1,700,000", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "3,300,000", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "5,000,000", "Totals-Actual Disbursements", "0", "Project Title", "pledged education activity 1")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner("Donor Agency", "USAID", 21696)).withContents("Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "1,700,000", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "3,300,000", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "5,000,000", "Totals-Actual Disbursements", "0", "Donor Agency", "USAID")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(46), "Region", "Chisinau County", "Primary Sector", "110 - EDUCATION", "Funding-2013-Actual Commitments", "1,700,000", "Funding-2014-Actual Commitments", "3,300,000", "Totals-Actual Commitments", "5,000,000")          )        ),
                new ReportAreaForTests(new AreaOwner("Project Title", "ptc activity 1", 28)).withContents("Donor Agency", "", "Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "666,777", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "666,777", "Totals-Actual Disbursements", "0", "Project Title", "ptc activity 1")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner("Donor Agency", "Finland", 21698)).withContents("Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "666,777", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "666,777", "Totals-Actual Disbursements", "0", "Donor Agency", "Finland")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(28), "Region", "Anenii Noi County", "Primary Sector", "110 - EDUCATION", "Funding-2013-Actual Commitments", "666,777", "Totals-Actual Commitments", "666,777")          )        ),
                new ReportAreaForTests(new AreaOwner("Project Title", "ptc activity 2", 29)).withContents("Donor Agency", "", "Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "333,222", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "333,222", "Totals-Actual Disbursements", "0", "Project Title", "ptc activity 2")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner("Donor Agency", "USAID", 21696)).withContents("Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "333,222", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "333,222", "Totals-Actual Disbursements", "0", "Donor Agency", "USAID")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(29), "Region", "Anenii Noi County", "Primary Sector", "110 - EDUCATION", "Funding-2013-Actual Commitments", "333,222", "Totals-Actual Commitments", "333,222")          )        ),
                new ReportAreaForTests(new AreaOwner("Project Title", "third activity with agreements", 67)).withContents("Donor Agency", "", "Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "123,456", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "123,456", "Totals-Actual Disbursements", "0", "Project Title", "third activity with agreements")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner("Donor Agency", "Ministry of Finance", 21699)).withContents("Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "123,456", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "123,456", "Totals-Actual Disbursements", "0", "Donor Agency", "Ministry of Finance")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(67), "Region", "Chisinau City", "Primary Sector", "110 - EDUCATION", "Funding-2015-Actual Commitments", "123,456", "Totals-Actual Commitments", "123,456")          )        ),
                new ReportAreaForTests(new AreaOwner("Project Title", "with weird currencies", 79)).withContents("Donor Agency", "", "Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "3,632,14", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "93,930,84", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "97,562,98", "Totals-Actual Disbursements", "0", "Project Title", "with weird currencies")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner("Donor Agency", "Ministry of Finance", 21699)).withContents("Region", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "3,632,14", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "93,930,84", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "97,562,98", "Totals-Actual Disbursements", "0", "Donor Agency", "Ministry of Finance")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(79), "Region", "", "Primary Sector", "110 - EDUCATION, 112 - BASIC EDUCATION", "Funding-2014-Actual Commitments", "3,632,14", "Funding-2015-Actual Commitments", "93,930,84", "Totals-Actual Commitments", "97,562,98")          )        )      ));
    
        ReportSpecificationImpl spec = buildSpecification("testProjectTitleAsFirstHierarchy",
            Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.REGION, ColumnConstants.PRIMARY_SECTOR, ColumnConstants.DONOR_AGENCY),
            Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS),
            Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.DONOR_AGENCY),
            GroupingCriteria.GROUPING_YEARLY);
        
        runNiTestCase(spec, "en", acts, cor);
    }

    @Test
    public void testSscMeasuresFlat() {
        NiReportModel cor = new NiReportModel("AMP-16688-all-flat")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 18))",
                        "(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Donor Agency: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 12));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 14, colSpan: 4))",
                        "(2012: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 4));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 6, colSpan: 4));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 10, colSpan: 4))",
                        "(Official Development Aid Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Bilateral SSC Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Triangular SSC Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Cumulated SSC Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Official Development Aid Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Bilateral SSC Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Triangular SSC Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Cumulated SSC Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Official Development Aid Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Bilateral SSC Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1));(Triangular SSC Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1));(Cumulated SSC Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1));(Official Development Aid Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 14, colSpan: 1));(Bilateral SSC Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 15, colSpan: 1));(Triangular SSC Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 16, colSpan: 1));(Cumulated SSC Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 17, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                        .withContents("Project Title", "", "Donor Agency", "", "Funding-2012-Official Development Aid Commitments", "0", "Funding-2012-Bilateral SSC Commitments", "12 000", "Funding-2012-Triangular SSC Commitments", "0", "Funding-2012-Cumulated SSC Commitments", "12 000", "Funding-2013-Official Development Aid Commitments", "0", "Funding-2013-Bilateral SSC Commitments", "42 000", "Funding-2013-Triangular SSC Commitments", "64 000", "Funding-2013-Cumulated SSC Commitments", "106 000", "Funding-2014-Official Development Aid Commitments", "175 000", "Funding-2014-Bilateral SSC Commitments", "0", "Funding-2014-Triangular SSC Commitments", "0", "Funding-2014-Cumulated SSC Commitments", "0", "Totals-Official Development Aid Commitments", "175 000", "Totals-Bilateral SSC Commitments", "54 000", "Totals-Triangular SSC Commitments", "64 000", "Totals-Cumulated SSC Commitments", "118 000")
                        .withChildren(
                                new ReportAreaForTests(new AreaOwner(38), "Project Title", "Real SSC Activity 2", "Donor Agency", "Norway", "Funding-2014-Official Development Aid Commitments", "25 000", "Totals-Official Development Aid Commitments", "25 000"),
                                new ReportAreaForTests(new AreaOwner(39), "Project Title", "Real SSC Activity 1", "Donor Agency", "Finland, USAID, World Bank", "Funding-2012-Bilateral SSC Commitments", "12 000", "Funding-2012-Cumulated SSC Commitments", "12 000", "Funding-2013-Bilateral SSC Commitments", "42 000", "Funding-2013-Triangular SSC Commitments", "64 000", "Funding-2013-Cumulated SSC Commitments", "106 000", "Funding-2014-Official Development Aid Commitments", "150 000", "Totals-Official Development Aid Commitments", "150 000", "Totals-Bilateral SSC Commitments", "54 000", "Totals-Triangular SSC Commitments", "64 000", "Totals-Cumulated SSC Commitments", "118 000")      ));
        
        runNiTestCase(spec("AMP-16688-all-flat"), "en", sscActs, cor);
    }
    
    @Test
    public void testShowEmptyFundingRowsTransactionLevelHierarchies() {
        // transaction-level-hierarchies should never show empty funding rows, even if "Date Filter Hides Project" is false
        
        NiReportModel cor = new NiReportModel("by-disaster-response")
            .withHeaders(Arrays.asList(
                "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 0, colSpan: 3))",
                "(Disaster Response Marker: (startRow: 1, rowSpan: 2, totalRowSpan: 2, colStart: 0, colSpan: 1));(Project Title: (startRow: 1, rowSpan: 2, totalRowSpan: 2, colStart: 1, colSpan: 1));(Totals: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 1))",
                "(Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1))"))
            .withWarnings(Arrays.asList())
            .withBody(      new ReportAreaForTests(null)
              .withContents("Disaster Response Marker", "", "Project Title", "", "Totals-Actual Commitments", "578,766")
              .withChildren(
                new ReportAreaForTests(new AreaOwner("Disaster Response Marker", "No", 2))
                .withContents("Project Title", "", "Totals-Actual Commitments", "317,222", "Disaster Response Marker", "No")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner(71), "Project Title", "activity_with_disaster_response", "Totals-Actual Commitments", "33,000"),
                  new ReportAreaForTests(new AreaOwner(87), "Project Title", "expenditure class", "Totals-Actual Commitments", "62,000"),
                  new ReportAreaForTests(new AreaOwner(92), "Project Title", "second with disaster response", "Totals-Actual Commitments", "222,222")        ),
                new ReportAreaForTests(new AreaOwner("Disaster Response Marker", "Yes", 1))
                .withContents("Project Title", "", "Totals-Actual Commitments", "211,444", "Disaster Response Marker", "Yes")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner(71), "Project Title", "activity_with_disaster_response", "Totals-Actual Commitments", "67,000"),
                  new ReportAreaForTests(new AreaOwner(92), "Project Title", "second with disaster response", "Totals-Actual Commitments", "144,444")        ),
                new ReportAreaForTests(new AreaOwner("Disaster Response Marker", "Disaster Response Marker: Undefined", -999999999))
                .withContents("Project Title", "", "Totals-Actual Commitments", "50,100", "Disaster Response Marker", "Disaster Response Marker: Undefined")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner(63), "Project Title", "activity with funded components", "Totals-Actual Commitments", "100"),
                  new ReportAreaForTests(new AreaOwner(71), "Project Title", "activity_with_disaster_response", "Totals-Actual Commitments", "50,000"),
                  new ReportAreaForTests(new AreaOwner(87), "Project Title", "expenditure class"),
                  new ReportAreaForTests(new AreaOwner(95), "Project Title", "activity 1 with indicators")        )      ));
        
        List<String> someActs = Arrays.asList("activity_with_disaster_response", "expenditure class", "second with disaster response", "activity with funded components", "activity 1 with indicators");
        
        ReportSpecificationImpl spec = buildSpecification("by-disaster-response",
            Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.DISASTER_RESPONSE_MARKER), 
            Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS),
            Arrays.asList(ColumnConstants.DISASTER_RESPONSE_MARKER), 
            GroupingCriteria.GROUPING_TOTALS_ONLY);
        
        spec.setDisplayEmptyFundingRows(true);
        runNiTestCase(cor, spec, "en", someActs);
    }
    
    @Test
    public void testShowEmptyFundingRowsDonorAgencyHierarchy() {
        NiReportModel cor = new NiReportModel("by-donor-agency")
        .withHeaders(Arrays.asList(
                "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 0, colSpan: 3))",
                "(Donor Agency: (startRow: 1, rowSpan: 2, totalRowSpan: 2, colStart: 0, colSpan: 1));(Project Title: (startRow: 1, rowSpan: 2, totalRowSpan: 2, colStart: 1, colSpan: 1));(Totals: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 1))",
                "(Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1))"))
            .withWarnings(Arrays.asList())
            .withBody(      new ReportAreaForTests(null)
              .withContents("Donor Agency", "", "Project Title", "", "Totals-Actual Commitments", "578,666")
              .withChildren(
                new ReportAreaForTests(new AreaOwner("Donor Agency", "Finland", 21698)).withContents("Project Title", "", "Totals-Actual Commitments", "150,000", "Donor Agency", "Finland")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner(71), "Project Title", "activity_with_disaster_response", "Totals-Actual Commitments", "150,000")        ),
                new ReportAreaForTests(new AreaOwner("Donor Agency", "Ministry of Economy", 21700)).withContents("Project Title", "", "Totals-Actual Commitments", "62,000", "Donor Agency", "Ministry of Economy")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner(87), "Project Title", "expenditure class", "Totals-Actual Commitments", "62,000")        ),
                new ReportAreaForTests(new AreaOwner("Donor Agency", "Ministry of Finance", 21699)).withContents("Project Title", "", "Totals-Actual Commitments", "0", "Donor Agency", "Ministry of Finance")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner(71), "Project Title", "activity_with_disaster_response")        ),
                new ReportAreaForTests(new AreaOwner("Donor Agency", "Norway", 21694))
                .withContents("Project Title", "", "Totals-Actual Commitments", "366,666", "Donor Agency", "Norway")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner(87), "Project Title", "expenditure class"),
                  new ReportAreaForTests(new AreaOwner(92), "Project Title", "second with disaster response", "Totals-Actual Commitments", "366,666")        ),
                new ReportAreaForTests(new AreaOwner("Donor Agency", "Donor Agency: Undefined", -999999999)).withContents("Project Title", "", "Totals-Actual Commitments", "0", "Donor Agency", "Donor Agency: Undefined")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner(95), "Project Title", "activity 1 with indicators")        )      ));   
        
        List<String> someActs = Arrays.asList("activity_with_disaster_response", "expenditure class", "second with disaster response", "activity 1 with indicators");
        
        ReportSpecificationImpl spec = buildSpecification("by-donor-agency",
            Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.DONOR_AGENCY), 
            Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS),
            Arrays.asList(ColumnConstants.DONOR_AGENCY), 
            GroupingCriteria.GROUPING_TOTALS_ONLY);
        
        spec.setDisplayEmptyFundingRows(true);
        runNiTestCase(cor, spec, "en", someActs);
    }
    
    @Test
    public void testShowEmptyFundingRowsModeOfPaymentAgencyHierarchy() {
        NiReportModel cor = new NiReportModel("by-mode-of-payment")
        .withHeaders(Arrays.asList(
            "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 0, colSpan: 3))",
            "(Mode of Payment: (startRow: 1, rowSpan: 2, totalRowSpan: 2, colStart: 0, colSpan: 1));(Project Title: (startRow: 1, rowSpan: 2, totalRowSpan: 2, colStart: 1, colSpan: 1));(Totals: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 1))",
            "(Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1))"))
        .withWarnings(Arrays.asList())
        .withBody(      new ReportAreaForTests(null)
          .withContents("Mode of Payment", "", "Project Title", "", "Totals-Actual Disbursements", "253,700")
          .withChildren(
            new ReportAreaForTests(new AreaOwner("Mode of Payment", "Direct payment", 2094))
            .withContents("Project Title", "", "Totals-Actual Disbursements", "0", "Mode of Payment", "Direct payment")
            .withChildren(
              new ReportAreaForTests(new AreaOwner(27), "Project Title", "mtef activity 2"),
              new ReportAreaForTests(new AreaOwner(71), "Project Title", "activity_with_disaster_response")        ),
            new ReportAreaForTests(new AreaOwner("Mode of Payment", "Mode of Payment: Undefined", -999999999))
            .withContents("Project Title", "", "Totals-Actual Disbursements", "253,700", "Mode of Payment", "Mode of Payment: Undefined")
            .withChildren(
              new ReportAreaForTests(new AreaOwner(19), "Project Title", "Pure MTEF Project"),
              new ReportAreaForTests(new AreaOwner(87), "Project Title", "expenditure class", "Totals-Actual Disbursements", "253,700"),
              new ReportAreaForTests(new AreaOwner(92), "Project Title", "second with disaster response"),
              new ReportAreaForTests(new AreaOwner(95), "Project Title", "activity 1 with indicators")        )      ));
        
        List<String> someActs = Arrays.asList("activity_with_disaster_response", "expenditure class", "second with disaster response", "activity 1 with indicators", "mtef activity 2", "Pure MTEF Project");
        
        ReportSpecificationImpl spec = buildSpecification("by-mode-of-payment",
            Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.MODE_OF_PAYMENT), 
            Arrays.asList(MeasureConstants.ACTUAL_DISBURSEMENTS),
            Arrays.asList(ColumnConstants.MODE_OF_PAYMENT), 
            GroupingCriteria.GROUPING_TOTALS_ONLY);
        
        spec.setDisplayEmptyFundingRows(true);
        runNiTestCase(cor, spec, "en", someActs);
    }
    
    @Test
    public void testSplitByMoP() {
        NiReportModel cor = new NiReportModel("AMP-15863-mode-of-payment-undisbursed-balance")
        .withHeaders(Arrays.asList(
                "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 5, colStart: 0, colSpan: 8))",
                "(Project Title: (startRow: 1, rowSpan: 4, totalRowSpan: 4, colStart: 0, colSpan: 1));(Mode of Payment: (startRow: 1, rowSpan: 4, totalRowSpan: 4, colStart: 1, colSpan: 1));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 4, colStart: 2, colSpan: 6))",
                "",
                "(Undisbursed Balance: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 6))",
                "(Cash: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Direct payment: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(No Information: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Reimbursable: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Unassigned: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Total: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1))"))
            .withWarnings(Arrays.asList())
            .withBody(      new ReportAreaForTests(null)
              .withContents("Project Title", "", "Mode of Payment", "", "Totals-Undisbursed Balance-Cash", "17 334", "Totals-Undisbursed Balance-Direct payment", "1 438 038,98", "Totals-Undisbursed Balance-No Information", "333 222", "Totals-Undisbursed Balance-Reimbursable", "666 777", "Totals-Undisbursed Balance-Unassigned", "13 746 517,21", "Totals-Undisbursed Balance-Total", "16 201 889,19")
              .withChildren(
                  new ReportAreaForTests(new AreaOwner(12), "Project Title", "TAC_activity_1", "Mode of Payment", "", "Totals-Undisbursed Balance-Unassigned", "89 910", "Totals-Undisbursed Balance-Total", "89 910"),
                  new ReportAreaForTests(new AreaOwner(13), "Project Title", "TAC_activity_2", "Mode of Payment", "", "Totals-Undisbursed Balance-Unassigned", "546 675", "Totals-Undisbursed Balance-Total", "546 675"),
                  new ReportAreaForTests(new AreaOwner(15), "Project Title", "Proposed Project Cost 1 - USD", "Mode of Payment", ""),
                  new ReportAreaForTests(new AreaOwner(17), "Project Title", "Proposed Project Cost 2 - EUR", "Mode of Payment", ""),
                  new ReportAreaForTests(new AreaOwner(18), "Project Title", "Test MTEF directed", "Mode of Payment", "Cash", "Totals-Undisbursed Balance-Cash", "-143 777", "Totals-Undisbursed Balance-Total", "-143 777"),
                  new ReportAreaForTests(new AreaOwner(19), "Project Title", "Pure MTEF Project", "Mode of Payment", ""),
                  new ReportAreaForTests(new AreaOwner(21), "Project Title", "activity with components", "Mode of Payment", ""),
                  new ReportAreaForTests(new AreaOwner(23), "Project Title", "Project with documents", "Mode of Payment", ""),
                  new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Mode of Payment", "Direct payment", "Totals-Undisbursed Balance-Unassigned", "-545 000", "Totals-Undisbursed Balance-Total", "-545 000"),
                  new ReportAreaForTests(new AreaOwner(25), "Project Title", "mtef activity 1", "Mode of Payment", ""),
                  new ReportAreaForTests(new AreaOwner(26), "Project Title", "date-filters-activity", "Mode of Payment", "", "Totals-Undisbursed Balance-Unassigned", "53 000", "Totals-Undisbursed Balance-Total", "53 000"),
                  new ReportAreaForTests(new AreaOwner(27), "Project Title", "mtef activity 2", "Mode of Payment", "Direct payment"),
                  new ReportAreaForTests(new AreaOwner(28), "Project Title", "ptc activity 1", "Mode of Payment", "Reimbursable", "Totals-Undisbursed Balance-Reimbursable", "666 777", "Totals-Undisbursed Balance-Total", "666 777"),
                  new ReportAreaForTests(new AreaOwner(29), "Project Title", "ptc activity 2", "Mode of Payment", "No Information", "Totals-Undisbursed Balance-No Information", "333 222", "Totals-Undisbursed Balance-Total", "333 222"),
                  new ReportAreaForTests(new AreaOwner(30), "Project Title", "SSC Project 1", "Mode of Payment", "Direct payment", "Totals-Undisbursed Balance-Direct payment", "-443 778", "Totals-Undisbursed Balance-Total", "-443 778"),
                  new ReportAreaForTests(new AreaOwner(31), "Project Title", "SSC Project 2", "Mode of Payment", "Direct payment", "Totals-Undisbursed Balance-Direct payment", "435 576", "Totals-Undisbursed Balance-Total", "435 576"),
                  new ReportAreaForTests(new AreaOwner(32), "Project Title", "crazy funding 1", "Mode of Payment", "Cash, Direct payment", "Totals-Undisbursed Balance-Cash", "111 111", "Totals-Undisbursed Balance-Direct payment", "222 222", "Totals-Undisbursed Balance-Total", "333 333"),
                  new ReportAreaForTests(new AreaOwner(33), "Project Title", "Activity with Zones", "Mode of Payment", "", "Totals-Undisbursed Balance-Unassigned", "570 000", "Totals-Undisbursed Balance-Total", "570 000"),
                  new ReportAreaForTests(new AreaOwner(36), "Project Title", "Activity With Zones and Percentages", "Mode of Payment", "", "Totals-Undisbursed Balance-Unassigned", "890 000", "Totals-Undisbursed Balance-Total", "890 000"),
                  new ReportAreaForTests(new AreaOwner(40), "Project Title", "SubNational no percentages", "Mode of Payment", "Direct payment", "Totals-Undisbursed Balance-Direct payment", "75 000", "Totals-Undisbursed Balance-Total", "75 000"),
                  new ReportAreaForTests(new AreaOwner(41), "Project Title", "Activity Linked With Pledge", "Mode of Payment", "", "Totals-Undisbursed Balance-Unassigned", "50 000", "Totals-Undisbursed Balance-Total", "50 000"),
                  new ReportAreaForTests(new AreaOwner(43), "Project Title", "Activity with primary_tertiary_program", "Mode of Payment", "Cash", "Totals-Undisbursed Balance-Cash", "50 000", "Totals-Undisbursed Balance-Total", "50 000"),
                  new ReportAreaForTests(new AreaOwner(44), "Project Title", "activity with primary_program", "Mode of Payment", "", "Totals-Undisbursed Balance-Unassigned", "32 000", "Totals-Undisbursed Balance-Total", "32 000"),
                  new ReportAreaForTests(new AreaOwner(45), "Project Title", "activity with tertiary_program", "Mode of Payment", "", "Totals-Undisbursed Balance-Unassigned", "15 000", "Totals-Undisbursed Balance-Total", "15 000"),
                  new ReportAreaForTests(new AreaOwner(46), "Project Title", "pledged education activity 1", "Mode of Payment", "", "Totals-Undisbursed Balance-Unassigned", "5 000 000", "Totals-Undisbursed Balance-Total", "5 000 000"),
                  new ReportAreaForTests(new AreaOwner(48), "Project Title", "pledged 2", "Mode of Payment", "", "Totals-Undisbursed Balance-Unassigned", "6 620 000", "Totals-Undisbursed Balance-Total", "6 620 000"),
                  new ReportAreaForTests(new AreaOwner(50), "Project Title", "activity with capital spending", "Mode of Payment", "", "Totals-Undisbursed Balance-Unassigned", "-14 239,37", "Totals-Undisbursed Balance-Total", "-14 239,37"),
                  new ReportAreaForTests(new AreaOwner(52), "Project Title", "activity with contracting agency", "Mode of Payment", "", "Totals-Undisbursed Balance-Unassigned", "46 840,58", "Totals-Undisbursed Balance-Total", "46 840,58"),
                  new ReportAreaForTests(new AreaOwner(53), "Project Title", "new activity with contracting", "Mode of Payment", "", "Totals-Undisbursed Balance-Unassigned", "12 000", "Totals-Undisbursed Balance-Total", "12 000"),
                  new ReportAreaForTests(new AreaOwner(61), "Project Title", "activity-with-unfunded-components", "Mode of Payment", "", "Totals-Undisbursed Balance-Unassigned", "123 321", "Totals-Undisbursed Balance-Total", "123 321"),
                  new ReportAreaForTests(new AreaOwner(63), "Project Title", "activity with funded components", "Mode of Payment", "", "Totals-Undisbursed Balance-Unassigned", "100", "Totals-Undisbursed Balance-Total", "100"),
                  new ReportAreaForTests(new AreaOwner(64), "Project Title", "Unvalidated activity", "Mode of Payment", "", "Totals-Undisbursed Balance-Unassigned", "45 000", "Totals-Undisbursed Balance-Total", "45 000"),
                  new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Mode of Payment", "", "Totals-Undisbursed Balance-Unassigned", "135 024", "Totals-Undisbursed Balance-Total", "135 024"),
                  new ReportAreaForTests(new AreaOwner(66), "Project Title", "Activity 2 with multiple agreements", "Mode of Payment", "", "Totals-Undisbursed Balance-Unassigned", "1 200", "Totals-Undisbursed Balance-Total", "1 200"),
                  new ReportAreaForTests(new AreaOwner(67), "Project Title", "third activity with agreements", "Mode of Payment", "", "Totals-Undisbursed Balance-Unassigned", "123 456", "Totals-Undisbursed Balance-Total", "123 456"),
                  new ReportAreaForTests(new AreaOwner(68), "Project Title", "activity with incomplete agreement", "Mode of Payment", "", "Totals-Undisbursed Balance-Unassigned", "123 000", "Totals-Undisbursed Balance-Total", "123 000"),
                  new ReportAreaForTests(new AreaOwner(69), "Project Title", "Activity with planned disbursements", "Mode of Payment", "", "Totals-Undisbursed Balance-Unassigned", "-770", "Totals-Undisbursed Balance-Total", "-770"),
                  new ReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms", "Mode of Payment", "Direct payment", "Totals-Undisbursed Balance-Direct payment", "888 000", "Totals-Undisbursed Balance-Total", "888 000"),
                  new ReportAreaForTests(new AreaOwner(71), "Project Title", "activity_with_disaster_response", "Mode of Payment", "Direct payment", "Totals-Undisbursed Balance-Direct payment", "150 000", "Totals-Undisbursed Balance-Total", "150 000"),
                  new ReportAreaForTests(new AreaOwner(73), "Project Title", "activity with directed MTEFs", "Mode of Payment", "Direct payment, Non-Cash", "Totals-Undisbursed Balance-Direct payment", "123 456", "Totals-Undisbursed Balance-Total", "123 456"),
                  new ReportAreaForTests(new AreaOwner(76), "Project Title", "activity with pipeline MTEFs and act. disb", "Mode of Payment", "Direct payment", "Totals-Undisbursed Balance-Direct payment", "-110 000", "Totals-Undisbursed Balance-Total", "-110 000"),
                  new ReportAreaForTests(new AreaOwner(77), "Project Title", "execution rate activity", "Mode of Payment", "", "Totals-Undisbursed Balance-Unassigned", "-90 000", "Totals-Undisbursed Balance-Total", "-90 000"),
                  new ReportAreaForTests(new AreaOwner(78), "Project Title", "activity with many MTEFs", "Mode of Payment", "", "Totals-Undisbursed Balance-Unassigned", "-80 000", "Totals-Undisbursed Balance-Total", "-80 000"),
                  new ReportAreaForTests(new AreaOwner(79), "Project Title", "with weird currencies", "Mode of Payment", "Direct payment", "Totals-Undisbursed Balance-Direct payment", "97 562,98", "Totals-Undisbursed Balance-Total", "97 562,98")      ));

        try {
            TestcasesReportsSchema.disableToAMoPSplitting = false;
            runNiTestCase(cor, spec("AMP-15863-mode-of-payment-undisbursed-balance"), "en", acts);
        }
        finally {
            TestcasesReportsSchema.disableToAMoPSplitting = true;
        }
    }
    
    @Test
    public void testSplitByMoPRussian() {
        NiReportModel cor = new NiReportModel("AMP-15863-mode-of-payment-undisbursed-balance")
        .withHeaders(Arrays.asList(
                "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 5, colStart: 0, colSpan: 8))",
                "(Project Title: (startRow: 1, rowSpan: 4, totalRowSpan: 4, colStart: 0, colSpan: 1));(Mode of Payment: (startRow: 1, rowSpan: 4, totalRowSpan: 4, colStart: 1, colSpan: 1));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 4, colStart: 2, colSpan: 6))",
                "",
                "(Undisbursed Balance: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 6))",
                "(Cash: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Direct payment: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(No Information: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Reimbursable: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Unassigned: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Всего: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1))"))
            .withWarnings(Arrays.asList())
            .withBody(      new ReportAreaForTests(null)
              .withContents("Project Title", "", "Mode of Payment", "", "Totals-Undisbursed Balance-Cash", "17 334", "Totals-Undisbursed Balance-Direct payment", "1 438 038,98", "Totals-Undisbursed Balance-No Information", "333 222", "Totals-Undisbursed Balance-Reimbursable", "666 777", "Totals-Undisbursed Balance-Unassigned", "13 746 517,21", "Totals-Undisbursed Balance-Всего", "16 201 889,19")
              .withChildren(
                new ReportAreaForTests(new AreaOwner(12), "Project Title", "Проект_TAC_1", "Mode of Payment", "", "Totals-Undisbursed Balance-Unassigned", "89 910", "Totals-Undisbursed Balance-Всего", "89 910"),
                new ReportAreaForTests(new AreaOwner(13), "Project Title", "Проект_TAC_2", "Mode of Payment", "", "Totals-Undisbursed Balance-Unassigned", "546 675", "Totals-Undisbursed Balance-Всего", "546 675"),
                new ReportAreaForTests(new AreaOwner(15), "Project Title", "Предполагаемая стоймость проекта 1 - USD", "Mode of Payment", ""),
                new ReportAreaForTests(new AreaOwner(17), "Project Title", "Предполагаемая стоймость проекта 2 - EUR", "Mode of Payment", ""),
                new ReportAreaForTests(new AreaOwner(18), "Project Title", "Тест направленных МТЕФ", "Mode of Payment", "Наличные", "Totals-Undisbursed Balance-Cash", "-143 777", "Totals-Undisbursed Balance-Всего", "-143 777"),
                new ReportAreaForTests(new AreaOwner(19), "Project Title", "Чисто-МТЕФ-Проект", "Mode of Payment", ""),
                new ReportAreaForTests(new AreaOwner(21), "Project Title", "проект с подпроектами", "Mode of Payment", ""),
                new ReportAreaForTests(new AreaOwner(23), "Project Title", "Проект с документами", "Mode of Payment", ""),
                new ReportAreaForTests(new AreaOwner(24), "Project Title", "Вода Eth", "Mode of Payment", "Прямая оплата", "Totals-Undisbursed Balance-Unassigned", "-545 000", "Totals-Undisbursed Balance-Всего", "-545 000"),
                new ReportAreaForTests(new AreaOwner(25), "Project Title", "Проект МТЕФ 1", "Mode of Payment", ""),
                new ReportAreaForTests(new AreaOwner(26), "Project Title", "проект-фильтры-по-датам", "Mode of Payment", "", "Totals-Undisbursed Balance-Unassigned", "53 000", "Totals-Undisbursed Balance-Всего", "53 000"),
                new ReportAreaForTests(new AreaOwner(27), "Project Title", "Проект МТЕФ 2", "Mode of Payment", "Прямая оплата"),
                new ReportAreaForTests(new AreaOwner(28), "Project Title", "Проект PTC 1", "Mode of Payment", "Компенсируемые", "Totals-Undisbursed Balance-Reimbursable", "666 777", "Totals-Undisbursed Balance-Всего", "666 777"),
                new ReportAreaForTests(new AreaOwner(29), "Project Title", "Проект PTC 2", "Mode of Payment", "Нет данных", "Totals-Undisbursed Balance-No Information", "333 222", "Totals-Undisbursed Balance-Всего", "333 222"),
                new ReportAreaForTests(new AreaOwner(30), "Project Title", "Проект КЮЮ 1", "Mode of Payment", "Прямая оплата", "Totals-Undisbursed Balance-Direct payment", "-443 778", "Totals-Undisbursed Balance-Всего", "-443 778"),
                new ReportAreaForTests(new AreaOwner(31), "Project Title", "Проект КЮЮ 2", "Mode of Payment", "Прямая оплата", "Totals-Undisbursed Balance-Direct payment", "435 576", "Totals-Undisbursed Balance-Всего", "435 576"),
                new ReportAreaForTests(new AreaOwner(32), "Project Title", "crazy funding 1", "Mode of Payment", "Наличные, Прямая оплата", "Totals-Undisbursed Balance-Cash", "111 111", "Totals-Undisbursed Balance-Direct payment", "222 222", "Totals-Undisbursed Balance-Всего", "333 333"),
                new ReportAreaForTests(new AreaOwner(33), "Project Title", "Activity with Zones", "Mode of Payment", "", "Totals-Undisbursed Balance-Unassigned", "570 000", "Totals-Undisbursed Balance-Всего", "570 000"),
                new ReportAreaForTests(new AreaOwner(36), "Project Title", "Activity With Zones and Percentages", "Mode of Payment", "", "Totals-Undisbursed Balance-Unassigned", "890 000", "Totals-Undisbursed Balance-Всего", "890 000"),
                new ReportAreaForTests(new AreaOwner(40), "Project Title", "SubNational no percentages", "Mode of Payment", "Прямая оплата", "Totals-Undisbursed Balance-Direct payment", "75 000", "Totals-Undisbursed Balance-Всего", "75 000"),
                new ReportAreaForTests(new AreaOwner(41), "Project Title", "Activity Linked With Pledge", "Mode of Payment", "", "Totals-Undisbursed Balance-Unassigned", "50 000", "Totals-Undisbursed Balance-Всего", "50 000"),
                new ReportAreaForTests(new AreaOwner(43), "Project Title", "Activity with primary_tertiary_program", "Mode of Payment", "Наличные", "Totals-Undisbursed Balance-Cash", "50 000", "Totals-Undisbursed Balance-Всего", "50 000"),
                new ReportAreaForTests(new AreaOwner(44), "Project Title", "activity with primary_program", "Mode of Payment", "", "Totals-Undisbursed Balance-Unassigned", "32 000", "Totals-Undisbursed Balance-Всего", "32 000"),
                new ReportAreaForTests(new AreaOwner(45), "Project Title", "activity with tertiary_program", "Mode of Payment", "", "Totals-Undisbursed Balance-Unassigned", "15 000", "Totals-Undisbursed Balance-Всего", "15 000"),
                new ReportAreaForTests(new AreaOwner(46), "Project Title", "pledged education activity 1 Русский", "Mode of Payment", "", "Totals-Undisbursed Balance-Unassigned", "5 000 000", "Totals-Undisbursed Balance-Всего", "5 000 000"),
                new ReportAreaForTests(new AreaOwner(48), "Project Title", "обещание 2", "Mode of Payment", "", "Totals-Undisbursed Balance-Unassigned", "6 620 000", "Totals-Undisbursed Balance-Всего", "6 620 000"),
                new ReportAreaForTests(new AreaOwner(50), "Project Title", "activity with capital spending", "Mode of Payment", "", "Totals-Undisbursed Balance-Unassigned", "-14 239,37", "Totals-Undisbursed Balance-Всего", "-14 239,37"),
                new ReportAreaForTests(new AreaOwner(52), "Project Title", "activity with contracting agency", "Mode of Payment", "", "Totals-Undisbursed Balance-Unassigned", "46 840,58", "Totals-Undisbursed Balance-Всего", "46 840,58"),
                new ReportAreaForTests(new AreaOwner(53), "Project Title", "new activity with contracting", "Mode of Payment", "", "Totals-Undisbursed Balance-Unassigned", "12 000", "Totals-Undisbursed Balance-Всего", "12 000"),
                new ReportAreaForTests(new AreaOwner(61), "Project Title", "activity-with-unfunded-components", "Mode of Payment", "", "Totals-Undisbursed Balance-Unassigned", "123 321", "Totals-Undisbursed Balance-Всего", "123 321"),
                new ReportAreaForTests(new AreaOwner(63), "Project Title", "activity with funded components", "Mode of Payment", "", "Totals-Undisbursed Balance-Unassigned", "100", "Totals-Undisbursed Balance-Всего", "100"),
                new ReportAreaForTests(new AreaOwner(64), "Project Title", "Unvalidated activity", "Mode of Payment", "", "Totals-Undisbursed Balance-Unassigned", "45 000", "Totals-Undisbursed Balance-Всего", "45 000"),
                new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Mode of Payment", "", "Totals-Undisbursed Balance-Unassigned", "135 024", "Totals-Undisbursed Balance-Всего", "135 024"),
                new ReportAreaForTests(new AreaOwner(66), "Project Title", "Проект с множеством проектов", "Mode of Payment", "", "Totals-Undisbursed Balance-Unassigned", "1 200", "Totals-Undisbursed Balance-Всего", "1 200"),
                new ReportAreaForTests(new AreaOwner(67), "Project Title", "third activity with agreements", "Mode of Payment", "", "Totals-Undisbursed Balance-Unassigned", "123 456", "Totals-Undisbursed Balance-Всего", "123 456"),
                new ReportAreaForTests(new AreaOwner(68), "Project Title", "activity with incomplete agreement", "Mode of Payment", "", "Totals-Undisbursed Balance-Unassigned", "123 000", "Totals-Undisbursed Balance-Всего", "123 000"),
                new ReportAreaForTests(new AreaOwner(69), "Project Title", "Activity with planned disbursements", "Mode of Payment", "", "Totals-Undisbursed Balance-Unassigned", "-770", "Totals-Undisbursed Balance-Всего", "-770"),
                new ReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms", "Mode of Payment", "Прямая оплата", "Totals-Undisbursed Balance-Direct payment", "888 000", "Totals-Undisbursed Balance-Всего", "888 000"),
                new ReportAreaForTests(new AreaOwner(71), "Project Title", "activity_with_disaster_response", "Mode of Payment", "Прямая оплата", "Totals-Undisbursed Balance-Direct payment", "150 000", "Totals-Undisbursed Balance-Всего", "150 000"),
                new ReportAreaForTests(new AreaOwner(73), "Project Title", "activity with directed MTEFs", "Mode of Payment", "Безналичные, Прямая оплата", "Totals-Undisbursed Balance-Direct payment", "123 456", "Totals-Undisbursed Balance-Всего", "123 456"),
                new ReportAreaForTests(new AreaOwner(76), "Project Title", "activity with pipeline MTEFs and act. disb", "Mode of Payment", "Прямая оплата", "Totals-Undisbursed Balance-Direct payment", "-110 000", "Totals-Undisbursed Balance-Всего", "-110 000"),
                new ReportAreaForTests(new AreaOwner(77), "Project Title", "execution rate activity", "Mode of Payment", "", "Totals-Undisbursed Balance-Unassigned", "-90 000", "Totals-Undisbursed Balance-Всего", "-90 000"),
                new ReportAreaForTests(new AreaOwner(78), "Project Title", "activity with many MTEFs", "Mode of Payment", "", "Totals-Undisbursed Balance-Unassigned", "-80 000", "Totals-Undisbursed Balance-Всего", "-80 000"),
                new ReportAreaForTests(new AreaOwner(79), "Project Title", "with weird currencies", "Mode of Payment", "Прямая оплата", "Totals-Undisbursed Balance-Direct payment", "97 562,98", "Totals-Undisbursed Balance-Всего", "97 562,98")      ));

        try {
            TestcasesReportsSchema.disableToAMoPSplitting = false;
            runNiTestCase(cor, spec("AMP-15863-mode-of-payment-undisbursed-balance"), "ru", acts);
        }
        finally {
            TestcasesReportsSchema.disableToAMoPSplitting = true;
        }
    }
    
    @Test
    public void testSplitByToA() {
        NiReportModel cor = new NiReportModel("a-type-of-assistance")
        .withHeaders(Arrays.asList(
                "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 5, colStart: 0, colSpan: 39))",
                "(Project Title: (startRow: 1, rowSpan: 4, totalRowSpan: 4, colStart: 0, colSpan: 1));(Type Of Assistance: (startRow: 1, rowSpan: 4, totalRowSpan: 4, colStart: 1, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 4, colStart: 2, colSpan: 31));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 4, colStart: 33, colSpan: 6))",
                "(2006: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 3));(2009: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 5, colSpan: 3));(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 8, colSpan: 3));(2011: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 11, colSpan: 3));(2012: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 14, colSpan: 4));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 18, colSpan: 5));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 23, colSpan: 6));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 29, colSpan: 4))",
                "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 2));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 2));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 7, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 8, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 9, colSpan: 2));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 11, colSpan: 2));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 13, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 14, colSpan: 2));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 16, colSpan: 2));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 18, colSpan: 3));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 21, colSpan: 2));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 23, colSpan: 3));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 26, colSpan: 3));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 29, colSpan: 2));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 31, colSpan: 2));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 33, colSpan: 3));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 36, colSpan: 3))",
                "(default type of assistance: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Total: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Total: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(default type of assistance: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Total: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Total: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Total: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(default type of assistance: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Total: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(default type of assistance: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1));(Total: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1));(Total: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1));(default type of assistance: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 14, colSpan: 1));(Total: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 15, colSpan: 1));(default type of assistance: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 16, colSpan: 1));(Total: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 17, colSpan: 1));(default type of assistance: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 18, colSpan: 1));(second type of assistance: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 19, colSpan: 1));(Total: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 20, colSpan: 1));(default type of assistance: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 21, colSpan: 1));(Total: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 22, colSpan: 1));(default type of assistance: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 23, colSpan: 1));(second type of assistance: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 24, colSpan: 1));(Total: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 25, colSpan: 1));(default type of assistance: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 26, colSpan: 1));(second type of assistance: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 27, colSpan: 1));(Total: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 28, colSpan: 1));(default type of assistance: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 29, colSpan: 1));(Total: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 30, colSpan: 1));(default type of assistance: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 31, colSpan: 1));(Total: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 32, colSpan: 1));(default type of assistance: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 33, colSpan: 1));(second type of assistance: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 34, colSpan: 1));(Total: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 35, colSpan: 1));(default type of assistance: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 36, colSpan: 1));(second type of assistance: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 37, colSpan: 1));(Total: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 38, colSpan: 1))"))
            .withWarnings(Arrays.asList())
            .withBody(      new ReportAreaForTests(null)
              .withContents("Project Title", "", "Type Of Assistance", "", "Funding-2006-Actual Commitments-default type of assistance", "96 840,58", "Funding-2006-Actual Commitments-Total", "96 840,58", "Funding-2006-Actual Disbursements-Total", "0", "Funding-2009-Actual Commitments-default type of assistance", "100 000", "Funding-2009-Actual Commitments-Total", "100 000", "Funding-2009-Actual Disbursements-Total", "0", "Funding-2010-Actual Commitments-Total", "0", "Funding-2010-Actual Disbursements-default type of assistance", "780 311", "Funding-2010-Actual Disbursements-Total", "780 311", "Funding-2011-Actual Commitments-default type of assistance", "1 213 119", "Funding-2011-Actual Commitments-Total", "1 213 119", "Funding-2011-Actual Disbursements-Total", "0", "Funding-2012-Actual Commitments-default type of assistance", "25 000", "Funding-2012-Actual Commitments-Total", "25 000", "Funding-2012-Actual Disbursements-default type of assistance", "12 000", "Funding-2012-Actual Disbursements-Total", "12 000", "Funding-2013-Actual Commitments-default type of assistance", "4 949 864", "Funding-2013-Actual Commitments-second type of assistance", "2 892 222", "Funding-2013-Actual Commitments-Total", "7 842 086", "Funding-2013-Actual Disbursements-default type of assistance", "1 266 956", "Funding-2013-Actual Disbursements-Total", "1 266 956", "Funding-2014-Actual Commitments-default type of assistance", "3 570 732,14", "Funding-2014-Actual Commitments-second type of assistance", "4 589 081,63", "Funding-2014-Actual Commitments-Total", "8 159 813,77", "Funding-2014-Actual Disbursements-default type of assistance", "180 200", "Funding-2014-Actual Disbursements-second type of assistance", "530 000", "Funding-2014-Actual Disbursements-Total", "710 200", "Funding-2015-Actual Commitments-default type of assistance", "1 971 831,84", "Funding-2015-Actual Commitments-Total", "1 971 831,84", "Funding-2015-Actual Disbursements-default type of assistance", "437 335", "Funding-2015-Actual Disbursements-Total", "437 335", "Totals-Actual Commitments-default type of assistance", "11 927 387,56", "Totals-Actual Commitments-second type of assistance", "7 481 303,63", "Totals-Actual Commitments-Total", "19 408 691,19", "Totals-Actual Disbursements-default type of assistance", "2 676 802", "Totals-Actual Disbursements-second type of assistance", "530 000", "Totals-Actual Disbursements-Total", "3 206 802")
              .withChildren(
                new ReportAreaForTests(new AreaOwner(12), "Project Title", "TAC_activity_1", "Type Of Assistance", "default type of assistance", "Funding-2010-Actual Disbursements-default type of assistance", "123 321", "Funding-2010-Actual Disbursements-Total", "123 321", "Funding-2011-Actual Commitments-default type of assistance", "213 231", "Funding-2011-Actual Commitments-Total", "213 231", "Totals-Actual Commitments-default type of assistance", "213 231", "Totals-Actual Commitments-Total", "213 231", "Totals-Actual Disbursements-default type of assistance", "123 321", "Totals-Actual Disbursements-Total", "123 321"),
                new ReportAreaForTests(new AreaOwner(13), "Project Title", "TAC_activity_2", "Type Of Assistance", "default type of assistance", "Funding-2010-Actual Disbursements-default type of assistance", "453 213", "Funding-2010-Actual Disbursements-Total", "453 213", "Funding-2011-Actual Commitments-default type of assistance", "999 888", "Funding-2011-Actual Commitments-Total", "999 888", "Totals-Actual Commitments-default type of assistance", "999 888", "Totals-Actual Commitments-Total", "999 888", "Totals-Actual Disbursements-default type of assistance", "453 213", "Totals-Actual Disbursements-Total", "453 213"),
                new ReportAreaForTests(new AreaOwner(15), "Project Title", "Proposed Project Cost 1 - USD", "Type Of Assistance", ""),
                new ReportAreaForTests(new AreaOwner(17), "Project Title", "Proposed Project Cost 2 - EUR", "Type Of Assistance", ""),
                new ReportAreaForTests(new AreaOwner(18), "Project Title", "Test MTEF directed", "Type Of Assistance", "default type of assistance", "Funding-2010-Actual Disbursements-default type of assistance", "143 777", "Funding-2010-Actual Disbursements-Total", "143 777", "Totals-Actual Disbursements-default type of assistance", "143 777", "Totals-Actual Disbursements-Total", "143 777"),
                new ReportAreaForTests(new AreaOwner(19), "Project Title", "Pure MTEF Project", "Type Of Assistance", "default type of assistance"),
                new ReportAreaForTests(new AreaOwner(21), "Project Title", "activity with components", "Type Of Assistance", ""),
                new ReportAreaForTests(new AreaOwner(23), "Project Title", "Project with documents", "Type Of Assistance", ""),
                new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Type Of Assistance", "default type of assistance, second type of assistance", "Funding-2013-Actual Disbursements-default type of assistance", "545 000", "Funding-2013-Actual Disbursements-Total", "545 000", "Totals-Actual Disbursements-default type of assistance", "545 000", "Totals-Actual Disbursements-Total", "545 000"),
                new ReportAreaForTests(new AreaOwner(25), "Project Title", "mtef activity 1", "Type Of Assistance", "default type of assistance"),
                new ReportAreaForTests(new AreaOwner(26), "Project Title", "date-filters-activity", "Type Of Assistance", "default type of assistance", "Funding-2009-Actual Commitments-default type of assistance", "100 000", "Funding-2009-Actual Commitments-Total", "100 000", "Funding-2010-Actual Disbursements-default type of assistance", "60 000", "Funding-2010-Actual Disbursements-Total", "60 000", "Funding-2012-Actual Commitments-default type of assistance", "25 000", "Funding-2012-Actual Commitments-Total", "25 000", "Funding-2012-Actual Disbursements-default type of assistance", "12 000", "Funding-2012-Actual Disbursements-Total", "12 000", "Totals-Actual Commitments-default type of assistance", "125 000", "Totals-Actual Commitments-Total", "125 000", "Totals-Actual Disbursements-default type of assistance", "72 000", "Totals-Actual Disbursements-Total", "72 000"),
                new ReportAreaForTests(new AreaOwner(27), "Project Title", "mtef activity 2", "Type Of Assistance", "default type of assistance"),
                new ReportAreaForTests(new AreaOwner(28), "Project Title", "ptc activity 1", "Type Of Assistance", "default type of assistance", "Funding-2013-Actual Commitments-default type of assistance", "666 777", "Funding-2013-Actual Commitments-Total", "666 777", "Totals-Actual Commitments-default type of assistance", "666 777", "Totals-Actual Commitments-Total", "666 777"),
                new ReportAreaForTests(new AreaOwner(29), "Project Title", "ptc activity 2", "Type Of Assistance", "default type of assistance", "Funding-2013-Actual Commitments-default type of assistance", "333 222", "Funding-2013-Actual Commitments-Total", "333 222", "Totals-Actual Commitments-default type of assistance", "333 222", "Totals-Actual Commitments-Total", "333 222"),
                new ReportAreaForTests(new AreaOwner(30), "Project Title", "SSC Project 1", "Type Of Assistance", "default type of assistance", "Funding-2013-Actual Commitments-default type of assistance", "111 333", "Funding-2013-Actual Commitments-Total", "111 333", "Funding-2013-Actual Disbursements-default type of assistance", "555 111", "Funding-2013-Actual Disbursements-Total", "555 111", "Totals-Actual Commitments-default type of assistance", "111 333", "Totals-Actual Commitments-Total", "111 333", "Totals-Actual Disbursements-default type of assistance", "555 111", "Totals-Actual Disbursements-Total", "555 111"),
                new ReportAreaForTests(new AreaOwner(31), "Project Title", "SSC Project 2", "Type Of Assistance", "default type of assistance", "Funding-2013-Actual Commitments-default type of assistance", "567 421", "Funding-2013-Actual Commitments-Total", "567 421", "Funding-2013-Actual Disbursements-default type of assistance", "131 845", "Funding-2013-Actual Disbursements-Total", "131 845", "Totals-Actual Commitments-default type of assistance", "567 421", "Totals-Actual Commitments-Total", "567 421", "Totals-Actual Disbursements-default type of assistance", "131 845", "Totals-Actual Disbursements-Total", "131 845"),
                new ReportAreaForTests(new AreaOwner(32), "Project Title", "crazy funding 1", "Type Of Assistance", "default type of assistance, second type of assistance", "Funding-2013-Actual Commitments-default type of assistance", "111 111", "Funding-2013-Actual Commitments-second type of assistance", "222 222", "Funding-2013-Actual Commitments-Total", "333 333", "Totals-Actual Commitments-default type of assistance", "111 111", "Totals-Actual Commitments-second type of assistance", "222 222", "Totals-Actual Commitments-Total", "333 333"),
                new ReportAreaForTests(new AreaOwner(33), "Project Title", "Activity with Zones", "Type Of Assistance", "default type of assistance", "Funding-2013-Actual Commitments-default type of assistance", "570 000", "Funding-2013-Actual Commitments-Total", "570 000", "Totals-Actual Commitments-default type of assistance", "570 000", "Totals-Actual Commitments-Total", "570 000"),
                new ReportAreaForTests(new AreaOwner(36), "Project Title", "Activity With Zones and Percentages", "Type Of Assistance", "default type of assistance", "Funding-2013-Actual Commitments-default type of assistance", "890 000", "Funding-2013-Actual Commitments-Total", "890 000", "Totals-Actual Commitments-default type of assistance", "890 000", "Totals-Actual Commitments-Total", "890 000"),
                new ReportAreaForTests(new AreaOwner(40), "Project Title", "SubNational no percentages", "Type Of Assistance", "default type of assistance", "Funding-2014-Actual Commitments-default type of assistance", "75 000", "Funding-2014-Actual Commitments-Total", "75 000", "Totals-Actual Commitments-default type of assistance", "75 000", "Totals-Actual Commitments-Total", "75 000"),
                new ReportAreaForTests(new AreaOwner(41), "Project Title", "Activity Linked With Pledge", "Type Of Assistance", "default type of assistance", "Funding-2014-Actual Commitments-default type of assistance", "50 000", "Funding-2014-Actual Commitments-Total", "50 000", "Totals-Actual Commitments-default type of assistance", "50 000", "Totals-Actual Commitments-Total", "50 000"),
                new ReportAreaForTests(new AreaOwner(43), "Project Title", "Activity with primary_tertiary_program", "Type Of Assistance", "default type of assistance", "Funding-2014-Actual Commitments-default type of assistance", "50 000", "Funding-2014-Actual Commitments-Total", "50 000", "Totals-Actual Commitments-default type of assistance", "50 000", "Totals-Actual Commitments-Total", "50 000"),
                new ReportAreaForTests(new AreaOwner(44), "Project Title", "activity with primary_program", "Type Of Assistance", "default type of assistance", "Funding-2014-Actual Commitments-default type of assistance", "32 000", "Funding-2014-Actual Commitments-Total", "32 000", "Totals-Actual Commitments-default type of assistance", "32 000", "Totals-Actual Commitments-Total", "32 000"),
                new ReportAreaForTests(new AreaOwner(45), "Project Title", "activity with tertiary_program", "Type Of Assistance", "default type of assistance", "Funding-2014-Actual Commitments-default type of assistance", "15 000", "Funding-2014-Actual Commitments-Total", "15 000", "Totals-Actual Commitments-default type of assistance", "15 000", "Totals-Actual Commitments-Total", "15 000"),
                new ReportAreaForTests(new AreaOwner(46), "Project Title", "pledged education activity 1", "Type Of Assistance", "default type of assistance", "Funding-2013-Actual Commitments-default type of assistance", "1 700 000", "Funding-2013-Actual Commitments-Total", "1 700 000", "Funding-2014-Actual Commitments-default type of assistance", "3 300 000", "Funding-2014-Actual Commitments-Total", "3 300 000", "Totals-Actual Commitments-default type of assistance", "5 000 000", "Totals-Actual Commitments-Total", "5 000 000"),
                new ReportAreaForTests(new AreaOwner(48), "Project Title", "pledged 2", "Type Of Assistance", "second type of assistance", "Funding-2013-Actual Commitments-second type of assistance", "2 670 000", "Funding-2013-Actual Commitments-Total", "2 670 000", "Funding-2014-Actual Commitments-second type of assistance", "4 400 000", "Funding-2014-Actual Commitments-Total", "4 400 000", "Funding-2014-Actual Disbursements-second type of assistance", "450 000", "Funding-2014-Actual Disbursements-Total", "450 000", "Totals-Actual Commitments-second type of assistance", "7 070 000", "Totals-Actual Commitments-Total", "7 070 000", "Totals-Actual Disbursements-second type of assistance", "450 000", "Totals-Actual Disbursements-Total", "450 000"),
                new ReportAreaForTests(new AreaOwner(50), "Project Title", "activity with capital spending", "Type Of Assistance", "second type of assistance", "Funding-2014-Actual Commitments-second type of assistance", "65 760,63", "Funding-2014-Actual Commitments-Total", "65 760,63", "Funding-2014-Actual Disbursements-second type of assistance", "80 000", "Funding-2014-Actual Disbursements-Total", "80 000", "Totals-Actual Commitments-second type of assistance", "65 760,63", "Totals-Actual Commitments-Total", "65 760,63", "Totals-Actual Disbursements-second type of assistance", "80 000", "Totals-Actual Disbursements-Total", "80 000"),
                new ReportAreaForTests(new AreaOwner(52), "Project Title", "activity with contracting agency", "Type Of Assistance", "default type of assistance", "Funding-2006-Actual Commitments-default type of assistance", "96 840,58", "Funding-2006-Actual Commitments-Total", "96 840,58", "Funding-2014-Actual Disbursements-default type of assistance", "50 000", "Funding-2014-Actual Disbursements-Total", "50 000", "Totals-Actual Commitments-default type of assistance", "96 840,58", "Totals-Actual Commitments-Total", "96 840,58", "Totals-Actual Disbursements-default type of assistance", "50 000", "Totals-Actual Disbursements-Total", "50 000"),
                new ReportAreaForTests(new AreaOwner(53), "Project Title", "new activity with contracting", "Type Of Assistance", "default type of assistance", "Funding-2014-Actual Commitments-default type of assistance", "12 000", "Funding-2014-Actual Commitments-Total", "12 000", "Totals-Actual Commitments-default type of assistance", "12 000", "Totals-Actual Commitments-Total", "12 000"),
                new ReportAreaForTests(new AreaOwner(61), "Project Title", "activity-with-unfunded-components", "Type Of Assistance", "second type of assistance", "Funding-2014-Actual Commitments-second type of assistance", "123 321", "Funding-2014-Actual Commitments-Total", "123 321", "Totals-Actual Commitments-second type of assistance", "123 321", "Totals-Actual Commitments-Total", "123 321"),
                new ReportAreaForTests(new AreaOwner(63), "Project Title", "activity with funded components", "Type Of Assistance", "default type of assistance", "Funding-2014-Actual Commitments-default type of assistance", "100", "Funding-2014-Actual Commitments-Total", "100", "Totals-Actual Commitments-default type of assistance", "100", "Totals-Actual Commitments-Total", "100"),
                new ReportAreaForTests(new AreaOwner(64), "Project Title", "Unvalidated activity", "Type Of Assistance", "default type of assistance", "Funding-2015-Actual Commitments-default type of assistance", "45 000", "Funding-2015-Actual Commitments-Total", "45 000", "Totals-Actual Commitments-default type of assistance", "45 000", "Totals-Actual Commitments-Total", "45 000"),
                new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Type Of Assistance", "default type of assistance", "Funding-2015-Actual Commitments-default type of assistance", "456 789", "Funding-2015-Actual Commitments-Total", "456 789", "Funding-2015-Actual Disbursements-default type of assistance", "321 765", "Funding-2015-Actual Disbursements-Total", "321 765", "Totals-Actual Commitments-default type of assistance", "456 789", "Totals-Actual Commitments-Total", "456 789", "Totals-Actual Disbursements-default type of assistance", "321 765", "Totals-Actual Disbursements-Total", "321 765"),
                new ReportAreaForTests(new AreaOwner(66), "Project Title", "Activity 2 with multiple agreements", "Type Of Assistance", "default type of assistance", "Funding-2015-Actual Commitments-default type of assistance", "1 200", "Funding-2015-Actual Commitments-Total", "1 200", "Totals-Actual Commitments-default type of assistance", "1 200", "Totals-Actual Commitments-Total", "1 200"),
                new ReportAreaForTests(new AreaOwner(67), "Project Title", "third activity with agreements", "Type Of Assistance", "default type of assistance", "Funding-2015-Actual Commitments-default type of assistance", "123 456", "Funding-2015-Actual Commitments-Total", "123 456", "Totals-Actual Commitments-default type of assistance", "123 456", "Totals-Actual Commitments-Total", "123 456"),
                new ReportAreaForTests(new AreaOwner(68), "Project Title", "activity with incomplete agreement", "Type Of Assistance", "default type of assistance", "Funding-2015-Actual Commitments-default type of assistance", "123 000", "Funding-2015-Actual Commitments-Total", "123 000", "Totals-Actual Commitments-default type of assistance", "123 000", "Totals-Actual Commitments-Total", "123 000"),
                new ReportAreaForTests(new AreaOwner(69), "Project Title", "Activity with planned disbursements", "Type Of Assistance", "default type of assistance", "Funding-2014-Actual Disbursements-default type of assistance", "200", "Funding-2014-Actual Disbursements-Total", "200", "Funding-2015-Actual Disbursements-default type of assistance", "570", "Funding-2015-Actual Disbursements-Total", "570", "Totals-Actual Disbursements-default type of assistance", "770", "Totals-Actual Disbursements-Total", "770"),
                new ReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms", "Type Of Assistance", "default type of assistance", "Funding-2015-Actual Commitments-default type of assistance", "888 000", "Funding-2015-Actual Commitments-Total", "888 000", "Totals-Actual Commitments-default type of assistance", "888 000", "Totals-Actual Commitments-Total", "888 000"),
                new ReportAreaForTests(new AreaOwner(71), "Project Title", "activity_with_disaster_response", "Type Of Assistance", "default type of assistance", "Funding-2014-Actual Commitments-default type of assistance", "33 000", "Funding-2014-Actual Commitments-Total", "33 000", "Funding-2015-Actual Commitments-default type of assistance", "117 000", "Funding-2015-Actual Commitments-Total", "117 000", "Totals-Actual Commitments-default type of assistance", "150 000", "Totals-Actual Commitments-Total", "150 000"),
                new ReportAreaForTests(new AreaOwner(73), "Project Title", "activity with directed MTEFs", "Type Of Assistance", "default type of assistance, second type of assistance", "Funding-2015-Actual Commitments-default type of assistance", "123 456", "Funding-2015-Actual Commitments-Total", "123 456", "Totals-Actual Commitments-default type of assistance", "123 456", "Totals-Actual Commitments-Total", "123 456"),
                new ReportAreaForTests(new AreaOwner(76), "Project Title", "activity with pipeline MTEFs and act. disb", "Type Of Assistance", "default type of assistance", "Funding-2013-Actual Disbursements-default type of assistance", "35 000", "Funding-2013-Actual Disbursements-Total", "35 000", "Funding-2014-Actual Disbursements-default type of assistance", "75 000", "Funding-2014-Actual Disbursements-Total", "75 000", "Totals-Actual Disbursements-default type of assistance", "110 000", "Totals-Actual Disbursements-Total", "110 000"),
                new ReportAreaForTests(new AreaOwner(77), "Project Title", "execution rate activity", "Type Of Assistance", "default type of assistance", "Funding-2014-Actual Disbursements-default type of assistance", "55 000", "Funding-2014-Actual Disbursements-Total", "55 000", "Funding-2015-Actual Disbursements-default type of assistance", "35 000", "Funding-2015-Actual Disbursements-Total", "35 000", "Totals-Actual Disbursements-default type of assistance", "90 000", "Totals-Actual Disbursements-Total", "90 000"),
                new ReportAreaForTests(new AreaOwner(78), "Project Title", "activity with many MTEFs", "Type Of Assistance", "default type of assistance", "Funding-2015-Actual Disbursements-default type of assistance", "80 000", "Funding-2015-Actual Disbursements-Total", "80 000", "Totals-Actual Disbursements-default type of assistance", "80 000", "Totals-Actual Disbursements-Total", "80 000"),
                new ReportAreaForTests(new AreaOwner(79), "Project Title", "with weird currencies", "Type Of Assistance", "default type of assistance", "Funding-2014-Actual Commitments-default type of assistance", "3 632,14", "Funding-2014-Actual Commitments-Total", "3 632,14", "Funding-2015-Actual Commitments-default type of assistance", "93 930,84", "Funding-2015-Actual Commitments-Total", "93 930,84", "Totals-Actual Commitments-default type of assistance", "97 562,98", "Totals-Actual Commitments-Total", "97 562,98")      ));

        try {
            TestcasesReportsSchema.disableToAMoPSplitting = false;
            runNiTestCase(cor, spec("a-type-of-assistance"), "en", acts);
        }
        finally {
            TestcasesReportsSchema.disableToAMoPSplitting = true;
        }
    }
    
    @Test
    public void testSplitByToARussian() {
        NiReportModel cor = new NiReportModel("a-type-of-assistance")
        .withHeaders(Arrays.asList(
                "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 5, colStart: 0, colSpan: 39))",
                "(Project Title: (startRow: 1, rowSpan: 4, totalRowSpan: 4, colStart: 0, colSpan: 1));(Type Of Assistance: (startRow: 1, rowSpan: 4, totalRowSpan: 4, colStart: 1, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 4, colStart: 2, colSpan: 31));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 4, colStart: 33, colSpan: 6))",
                "(2006: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 3));(2009: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 5, colSpan: 3));(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 8, colSpan: 3));(2011: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 11, colSpan: 3));(2012: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 14, colSpan: 4));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 18, colSpan: 5));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 23, colSpan: 6));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 29, colSpan: 4))",
                "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 2));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 2));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 7, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 8, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 9, colSpan: 2));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 11, colSpan: 2));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 13, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 14, colSpan: 2));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 16, colSpan: 2));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 18, colSpan: 3));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 21, colSpan: 2));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 23, colSpan: 3));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 26, colSpan: 3));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 29, colSpan: 2));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 31, colSpan: 2));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 33, colSpan: 3));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 36, colSpan: 3))",
                "(default type of assistance: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Всего: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Всего: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(default type of assistance: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Всего: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Всего: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Всего: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(default type of assistance: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Всего: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(default type of assistance: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1));(Всего: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1));(Всего: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1));(default type of assistance: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 14, colSpan: 1));(Всего: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 15, colSpan: 1));(default type of assistance: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 16, colSpan: 1));(Всего: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 17, colSpan: 1));(default type of assistance: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 18, colSpan: 1));(second type of assistance: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 19, colSpan: 1));(Всего: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 20, colSpan: 1));(default type of assistance: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 21, colSpan: 1));(Всего: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 22, colSpan: 1));(default type of assistance: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 23, colSpan: 1));(second type of assistance: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 24, colSpan: 1));(Всего: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 25, colSpan: 1));(default type of assistance: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 26, colSpan: 1));(second type of assistance: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 27, colSpan: 1));(Всего: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 28, colSpan: 1));(default type of assistance: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 29, colSpan: 1));(Всего: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 30, colSpan: 1));(default type of assistance: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 31, colSpan: 1));(Всего: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 32, colSpan: 1));(default type of assistance: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 33, colSpan: 1));(second type of assistance: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 34, colSpan: 1));(Всего: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 35, colSpan: 1));(default type of assistance: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 36, colSpan: 1));(second type of assistance: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 37, colSpan: 1));(Всего: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 38, colSpan: 1))"))
            .withWarnings(Arrays.asList())
            .withBody(      new ReportAreaForTests(null)
              .withContents("Project Title", "", "Type Of Assistance", "", "Funding-2006-Actual Commitments-default type of assistance", "96 840,58", "Funding-2006-Actual Commitments-Всего", "96 840,58", "Funding-2006-Actual Disbursements-Всего", "0", "Funding-2009-Actual Commitments-default type of assistance", "100 000", "Funding-2009-Actual Commitments-Всего", "100 000", "Funding-2009-Actual Disbursements-Всего", "0", "Funding-2010-Actual Commitments-Всего", "0", "Funding-2010-Actual Disbursements-default type of assistance", "780 311", "Funding-2010-Actual Disbursements-Всего", "780 311", "Funding-2011-Actual Commitments-default type of assistance", "1 213 119", "Funding-2011-Actual Commitments-Всего", "1 213 119", "Funding-2011-Actual Disbursements-Всего", "0", "Funding-2012-Actual Commitments-default type of assistance", "25 000", "Funding-2012-Actual Commitments-Всего", "25 000", "Funding-2012-Actual Disbursements-default type of assistance", "12 000", "Funding-2012-Actual Disbursements-Всего", "12 000", "Funding-2013-Actual Commitments-default type of assistance", "4 949 864", "Funding-2013-Actual Commitments-second type of assistance", "2 892 222", "Funding-2013-Actual Commitments-Всего", "7 842 086", "Funding-2013-Actual Disbursements-default type of assistance", "1 266 956", "Funding-2013-Actual Disbursements-Всего", "1 266 956", "Funding-2014-Actual Commitments-default type of assistance", "3 570 732,14", "Funding-2014-Actual Commitments-second type of assistance", "4 589 081,63", "Funding-2014-Actual Commitments-Всего", "8 159 813,77", "Funding-2014-Actual Disbursements-default type of assistance", "180 200", "Funding-2014-Actual Disbursements-second type of assistance", "530 000", "Funding-2014-Actual Disbursements-Всего", "710 200", "Funding-2015-Actual Commitments-default type of assistance", "1 971 831,84", "Funding-2015-Actual Commitments-Всего", "1 971 831,84", "Funding-2015-Actual Disbursements-default type of assistance", "437 335", "Funding-2015-Actual Disbursements-Всего", "437 335", "Totals-Actual Commitments-default type of assistance", "11 927 387,56", "Totals-Actual Commitments-second type of assistance", "7 481 303,63", "Totals-Actual Commitments-Всего", "19 408 691,19", "Totals-Actual Disbursements-default type of assistance", "2 676 802", "Totals-Actual Disbursements-second type of assistance", "530 000", "Totals-Actual Disbursements-Всего", "3 206 802")
              .withChildren(
                new ReportAreaForTests(new AreaOwner(12), "Project Title", "Проект_TAC_1", "Type Of Assistance", "первичный тип помощи", "Funding-2010-Actual Disbursements-default type of assistance", "123 321", "Funding-2010-Actual Disbursements-Всего", "123 321", "Funding-2011-Actual Commitments-default type of assistance", "213 231", "Funding-2011-Actual Commitments-Всего", "213 231", "Totals-Actual Commitments-default type of assistance", "213 231", "Totals-Actual Commitments-Всего", "213 231", "Totals-Actual Disbursements-default type of assistance", "123 321", "Totals-Actual Disbursements-Всего", "123 321"),
                new ReportAreaForTests(new AreaOwner(13), "Project Title", "Проект_TAC_2", "Type Of Assistance", "первичный тип помощи", "Funding-2010-Actual Disbursements-default type of assistance", "453 213", "Funding-2010-Actual Disbursements-Всего", "453 213", "Funding-2011-Actual Commitments-default type of assistance", "999 888", "Funding-2011-Actual Commitments-Всего", "999 888", "Totals-Actual Commitments-default type of assistance", "999 888", "Totals-Actual Commitments-Всего", "999 888", "Totals-Actual Disbursements-default type of assistance", "453 213", "Totals-Actual Disbursements-Всего", "453 213"),
                new ReportAreaForTests(new AreaOwner(15), "Project Title", "Предполагаемая стоймость проекта 1 - USD", "Type Of Assistance", ""),
                new ReportAreaForTests(new AreaOwner(17), "Project Title", "Предполагаемая стоймость проекта 2 - EUR", "Type Of Assistance", ""),
                new ReportAreaForTests(new AreaOwner(18), "Project Title", "Тест направленных МТЕФ", "Type Of Assistance", "первичный тип помощи", "Funding-2010-Actual Disbursements-default type of assistance", "143 777", "Funding-2010-Actual Disbursements-Всего", "143 777", "Totals-Actual Disbursements-default type of assistance", "143 777", "Totals-Actual Disbursements-Всего", "143 777"),
                new ReportAreaForTests(new AreaOwner(19), "Project Title", "Чисто-МТЕФ-Проект", "Type Of Assistance", "первичный тип помощи"),
                new ReportAreaForTests(new AreaOwner(21), "Project Title", "проект с подпроектами", "Type Of Assistance", ""),
                new ReportAreaForTests(new AreaOwner(23), "Project Title", "Проект с документами", "Type Of Assistance", ""),
                new ReportAreaForTests(new AreaOwner(24), "Project Title", "Вода Eth", "Type Of Assistance", "вторичный тип помощи, первичный тип помощи", "Funding-2013-Actual Disbursements-default type of assistance", "545 000", "Funding-2013-Actual Disbursements-Всего", "545 000", "Totals-Actual Disbursements-default type of assistance", "545 000", "Totals-Actual Disbursements-Всего", "545 000"),
                new ReportAreaForTests(new AreaOwner(25), "Project Title", "Проект МТЕФ 1", "Type Of Assistance", "первичный тип помощи"),
                new ReportAreaForTests(new AreaOwner(26), "Project Title", "проект-фильтры-по-датам", "Type Of Assistance", "первичный тип помощи", "Funding-2009-Actual Commitments-default type of assistance", "100 000", "Funding-2009-Actual Commitments-Всего", "100 000", "Funding-2010-Actual Disbursements-default type of assistance", "60 000", "Funding-2010-Actual Disbursements-Всего", "60 000", "Funding-2012-Actual Commitments-default type of assistance", "25 000", "Funding-2012-Actual Commitments-Всего", "25 000", "Funding-2012-Actual Disbursements-default type of assistance", "12 000", "Funding-2012-Actual Disbursements-Всего", "12 000", "Totals-Actual Commitments-default type of assistance", "125 000", "Totals-Actual Commitments-Всего", "125 000", "Totals-Actual Disbursements-default type of assistance", "72 000", "Totals-Actual Disbursements-Всего", "72 000"),
                new ReportAreaForTests(new AreaOwner(27), "Project Title", "Проект МТЕФ 2", "Type Of Assistance", "первичный тип помощи"),
                new ReportAreaForTests(new AreaOwner(28), "Project Title", "Проект PTC 1", "Type Of Assistance", "первичный тип помощи", "Funding-2013-Actual Commitments-default type of assistance", "666 777", "Funding-2013-Actual Commitments-Всего", "666 777", "Totals-Actual Commitments-default type of assistance", "666 777", "Totals-Actual Commitments-Всего", "666 777"),
                new ReportAreaForTests(new AreaOwner(29), "Project Title", "Проект PTC 2", "Type Of Assistance", "первичный тип помощи", "Funding-2013-Actual Commitments-default type of assistance", "333 222", "Funding-2013-Actual Commitments-Всего", "333 222", "Totals-Actual Commitments-default type of assistance", "333 222", "Totals-Actual Commitments-Всего", "333 222"),
                new ReportAreaForTests(new AreaOwner(30), "Project Title", "Проект КЮЮ 1", "Type Of Assistance", "первичный тип помощи", "Funding-2013-Actual Commitments-default type of assistance", "111 333", "Funding-2013-Actual Commitments-Всего", "111 333", "Funding-2013-Actual Disbursements-default type of assistance", "555 111", "Funding-2013-Actual Disbursements-Всего", "555 111", "Totals-Actual Commitments-default type of assistance", "111 333", "Totals-Actual Commitments-Всего", "111 333", "Totals-Actual Disbursements-default type of assistance", "555 111", "Totals-Actual Disbursements-Всего", "555 111"),
                new ReportAreaForTests(new AreaOwner(31), "Project Title", "Проект КЮЮ 2", "Type Of Assistance", "первичный тип помощи", "Funding-2013-Actual Commitments-default type of assistance", "567 421", "Funding-2013-Actual Commitments-Всего", "567 421", "Funding-2013-Actual Disbursements-default type of assistance", "131 845", "Funding-2013-Actual Disbursements-Всего", "131 845", "Totals-Actual Commitments-default type of assistance", "567 421", "Totals-Actual Commitments-Всего", "567 421", "Totals-Actual Disbursements-default type of assistance", "131 845", "Totals-Actual Disbursements-Всего", "131 845"),
                new ReportAreaForTests(new AreaOwner(32), "Project Title", "crazy funding 1", "Type Of Assistance", "вторичный тип помощи, первичный тип помощи", "Funding-2013-Actual Commitments-default type of assistance", "111 111", "Funding-2013-Actual Commitments-second type of assistance", "222 222", "Funding-2013-Actual Commitments-Всего", "333 333", "Totals-Actual Commitments-default type of assistance", "111 111", "Totals-Actual Commitments-second type of assistance", "222 222", "Totals-Actual Commitments-Всего", "333 333"),
                new ReportAreaForTests(new AreaOwner(33), "Project Title", "Activity with Zones", "Type Of Assistance", "первичный тип помощи", "Funding-2013-Actual Commitments-default type of assistance", "570 000", "Funding-2013-Actual Commitments-Всего", "570 000", "Totals-Actual Commitments-default type of assistance", "570 000", "Totals-Actual Commitments-Всего", "570 000"),
                new ReportAreaForTests(new AreaOwner(36), "Project Title", "Activity With Zones and Percentages", "Type Of Assistance", "первичный тип помощи", "Funding-2013-Actual Commitments-default type of assistance", "890 000", "Funding-2013-Actual Commitments-Всего", "890 000", "Totals-Actual Commitments-default type of assistance", "890 000", "Totals-Actual Commitments-Всего", "890 000"),
                new ReportAreaForTests(new AreaOwner(40), "Project Title", "SubNational no percentages", "Type Of Assistance", "первичный тип помощи", "Funding-2014-Actual Commitments-default type of assistance", "75 000", "Funding-2014-Actual Commitments-Всего", "75 000", "Totals-Actual Commitments-default type of assistance", "75 000", "Totals-Actual Commitments-Всего", "75 000"),
                new ReportAreaForTests(new AreaOwner(41), "Project Title", "Activity Linked With Pledge", "Type Of Assistance", "первичный тип помощи", "Funding-2014-Actual Commitments-default type of assistance", "50 000", "Funding-2014-Actual Commitments-Всего", "50 000", "Totals-Actual Commitments-default type of assistance", "50 000", "Totals-Actual Commitments-Всего", "50 000"),
                new ReportAreaForTests(new AreaOwner(43), "Project Title", "Activity with primary_tertiary_program", "Type Of Assistance", "первичный тип помощи", "Funding-2014-Actual Commitments-default type of assistance", "50 000", "Funding-2014-Actual Commitments-Всего", "50 000", "Totals-Actual Commitments-default type of assistance", "50 000", "Totals-Actual Commitments-Всего", "50 000"),
                new ReportAreaForTests(new AreaOwner(44), "Project Title", "activity with primary_program", "Type Of Assistance", "первичный тип помощи", "Funding-2014-Actual Commitments-default type of assistance", "32 000", "Funding-2014-Actual Commitments-Всего", "32 000", "Totals-Actual Commitments-default type of assistance", "32 000", "Totals-Actual Commitments-Всего", "32 000"),
                new ReportAreaForTests(new AreaOwner(45), "Project Title", "activity with tertiary_program", "Type Of Assistance", "первичный тип помощи", "Funding-2014-Actual Commitments-default type of assistance", "15 000", "Funding-2014-Actual Commitments-Всего", "15 000", "Totals-Actual Commitments-default type of assistance", "15 000", "Totals-Actual Commitments-Всего", "15 000"),
                new ReportAreaForTests(new AreaOwner(46), "Project Title", "pledged education activity 1 Русский", "Type Of Assistance", "первичный тип помощи", "Funding-2013-Actual Commitments-default type of assistance", "1 700 000", "Funding-2013-Actual Commitments-Всего", "1 700 000", "Funding-2014-Actual Commitments-default type of assistance", "3 300 000", "Funding-2014-Actual Commitments-Всего", "3 300 000", "Totals-Actual Commitments-default type of assistance", "5 000 000", "Totals-Actual Commitments-Всего", "5 000 000"),
                new ReportAreaForTests(new AreaOwner(48), "Project Title", "обещание 2", "Type Of Assistance", "вторичный тип помощи", "Funding-2013-Actual Commitments-second type of assistance", "2 670 000", "Funding-2013-Actual Commitments-Всего", "2 670 000", "Funding-2014-Actual Commitments-second type of assistance", "4 400 000", "Funding-2014-Actual Commitments-Всего", "4 400 000", "Funding-2014-Actual Disbursements-second type of assistance", "450 000", "Funding-2014-Actual Disbursements-Всего", "450 000", "Totals-Actual Commitments-second type of assistance", "7 070 000", "Totals-Actual Commitments-Всего", "7 070 000", "Totals-Actual Disbursements-second type of assistance", "450 000", "Totals-Actual Disbursements-Всего", "450 000"),
                new ReportAreaForTests(new AreaOwner(50), "Project Title", "activity with capital spending", "Type Of Assistance", "вторичный тип помощи", "Funding-2014-Actual Commitments-second type of assistance", "65 760,63", "Funding-2014-Actual Commitments-Всего", "65 760,63", "Funding-2014-Actual Disbursements-second type of assistance", "80 000", "Funding-2014-Actual Disbursements-Всего", "80 000", "Totals-Actual Commitments-second type of assistance", "65 760,63", "Totals-Actual Commitments-Всего", "65 760,63", "Totals-Actual Disbursements-second type of assistance", "80 000", "Totals-Actual Disbursements-Всего", "80 000"),
                new ReportAreaForTests(new AreaOwner(52), "Project Title", "activity with contracting agency", "Type Of Assistance", "первичный тип помощи", "Funding-2006-Actual Commitments-default type of assistance", "96 840,58", "Funding-2006-Actual Commitments-Всего", "96 840,58", "Funding-2014-Actual Disbursements-default type of assistance", "50 000", "Funding-2014-Actual Disbursements-Всего", "50 000", "Totals-Actual Commitments-default type of assistance", "96 840,58", "Totals-Actual Commitments-Всего", "96 840,58", "Totals-Actual Disbursements-default type of assistance", "50 000", "Totals-Actual Disbursements-Всего", "50 000"),
                new ReportAreaForTests(new AreaOwner(53), "Project Title", "new activity with contracting", "Type Of Assistance", "первичный тип помощи", "Funding-2014-Actual Commitments-default type of assistance", "12 000", "Funding-2014-Actual Commitments-Всего", "12 000", "Totals-Actual Commitments-default type of assistance", "12 000", "Totals-Actual Commitments-Всего", "12 000"),
                new ReportAreaForTests(new AreaOwner(61), "Project Title", "activity-with-unfunded-components", "Type Of Assistance", "вторичный тип помощи", "Funding-2014-Actual Commitments-second type of assistance", "123 321", "Funding-2014-Actual Commitments-Всего", "123 321", "Totals-Actual Commitments-second type of assistance", "123 321", "Totals-Actual Commitments-Всего", "123 321"),
                new ReportAreaForTests(new AreaOwner(63), "Project Title", "activity with funded components", "Type Of Assistance", "первичный тип помощи", "Funding-2014-Actual Commitments-default type of assistance", "100", "Funding-2014-Actual Commitments-Всего", "100", "Totals-Actual Commitments-default type of assistance", "100", "Totals-Actual Commitments-Всего", "100"),
                new ReportAreaForTests(new AreaOwner(64), "Project Title", "Unvalidated activity", "Type Of Assistance", "первичный тип помощи", "Funding-2015-Actual Commitments-default type of assistance", "45 000", "Funding-2015-Actual Commitments-Всего", "45 000", "Totals-Actual Commitments-default type of assistance", "45 000", "Totals-Actual Commitments-Всего", "45 000"),
                new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Type Of Assistance", "первичный тип помощи", "Funding-2015-Actual Commitments-default type of assistance", "456 789", "Funding-2015-Actual Commitments-Всего", "456 789", "Funding-2015-Actual Disbursements-default type of assistance", "321 765", "Funding-2015-Actual Disbursements-Всего", "321 765", "Totals-Actual Commitments-default type of assistance", "456 789", "Totals-Actual Commitments-Всего", "456 789", "Totals-Actual Disbursements-default type of assistance", "321 765", "Totals-Actual Disbursements-Всего", "321 765"),
                new ReportAreaForTests(new AreaOwner(66), "Project Title", "Проект с множеством проектов", "Type Of Assistance", "первичный тип помощи", "Funding-2015-Actual Commitments-default type of assistance", "1 200", "Funding-2015-Actual Commitments-Всего", "1 200", "Totals-Actual Commitments-default type of assistance", "1 200", "Totals-Actual Commitments-Всего", "1 200"),
                new ReportAreaForTests(new AreaOwner(67), "Project Title", "third activity with agreements", "Type Of Assistance", "первичный тип помощи", "Funding-2015-Actual Commitments-default type of assistance", "123 456", "Funding-2015-Actual Commitments-Всего", "123 456", "Totals-Actual Commitments-default type of assistance", "123 456", "Totals-Actual Commitments-Всего", "123 456"),
                new ReportAreaForTests(new AreaOwner(68), "Project Title", "activity with incomplete agreement", "Type Of Assistance", "первичный тип помощи", "Funding-2015-Actual Commitments-default type of assistance", "123 000", "Funding-2015-Actual Commitments-Всего", "123 000", "Totals-Actual Commitments-default type of assistance", "123 000", "Totals-Actual Commitments-Всего", "123 000"),
                new ReportAreaForTests(new AreaOwner(69), "Project Title", "Activity with planned disbursements", "Type Of Assistance", "первичный тип помощи", "Funding-2014-Actual Disbursements-default type of assistance", "200", "Funding-2014-Actual Disbursements-Всего", "200", "Funding-2015-Actual Disbursements-default type of assistance", "570", "Funding-2015-Actual Disbursements-Всего", "570", "Totals-Actual Disbursements-default type of assistance", "770", "Totals-Actual Disbursements-Всего", "770"),
                new ReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms", "Type Of Assistance", "первичный тип помощи", "Funding-2015-Actual Commitments-default type of assistance", "888 000", "Funding-2015-Actual Commitments-Всего", "888 000", "Totals-Actual Commitments-default type of assistance", "888 000", "Totals-Actual Commitments-Всего", "888 000"),
                new ReportAreaForTests(new AreaOwner(71), "Project Title", "activity_with_disaster_response", "Type Of Assistance", "первичный тип помощи", "Funding-2014-Actual Commitments-default type of assistance", "33 000", "Funding-2014-Actual Commitments-Всего", "33 000", "Funding-2015-Actual Commitments-default type of assistance", "117 000", "Funding-2015-Actual Commitments-Всего", "117 000", "Totals-Actual Commitments-default type of assistance", "150 000", "Totals-Actual Commitments-Всего", "150 000"),
                new ReportAreaForTests(new AreaOwner(73), "Project Title", "activity with directed MTEFs", "Type Of Assistance", "вторичный тип помощи, первичный тип помощи", "Funding-2015-Actual Commitments-default type of assistance", "123 456", "Funding-2015-Actual Commitments-Всего", "123 456", "Totals-Actual Commitments-default type of assistance", "123 456", "Totals-Actual Commitments-Всего", "123 456"),
                new ReportAreaForTests(new AreaOwner(76), "Project Title", "activity with pipeline MTEFs and act. disb", "Type Of Assistance", "первичный тип помощи", "Funding-2013-Actual Disbursements-default type of assistance", "35 000", "Funding-2013-Actual Disbursements-Всего", "35 000", "Funding-2014-Actual Disbursements-default type of assistance", "75 000", "Funding-2014-Actual Disbursements-Всего", "75 000", "Totals-Actual Disbursements-default type of assistance", "110 000", "Totals-Actual Disbursements-Всего", "110 000"),
                new ReportAreaForTests(new AreaOwner(77), "Project Title", "execution rate activity", "Type Of Assistance", "первичный тип помощи", "Funding-2014-Actual Disbursements-default type of assistance", "55 000", "Funding-2014-Actual Disbursements-Всего", "55 000", "Funding-2015-Actual Disbursements-default type of assistance", "35 000", "Funding-2015-Actual Disbursements-Всего", "35 000", "Totals-Actual Disbursements-default type of assistance", "90 000", "Totals-Actual Disbursements-Всего", "90 000"),
                new ReportAreaForTests(new AreaOwner(78), "Project Title", "activity with many MTEFs", "Type Of Assistance", "первичный тип помощи", "Funding-2015-Actual Disbursements-default type of assistance", "80 000", "Funding-2015-Actual Disbursements-Всего", "80 000", "Totals-Actual Disbursements-default type of assistance", "80 000", "Totals-Actual Disbursements-Всего", "80 000"),
                new ReportAreaForTests(new AreaOwner(79), "Project Title", "with weird currencies", "Type Of Assistance", "первичный тип помощи", "Funding-2014-Actual Commitments-default type of assistance", "3 632,14", "Funding-2014-Actual Commitments-Всего", "3 632,14", "Funding-2015-Actual Commitments-default type of assistance", "93 930,84", "Funding-2015-Actual Commitments-Всего", "93 930,84", "Totals-Actual Commitments-default type of assistance", "97 562,98", "Totals-Actual Commitments-Всего", "97 562,98")      ));

        try {
            TestcasesReportsSchema.disableToAMoPSplitting = false;
            runNiTestCase(cor, spec("a-type-of-assistance"), "ru", acts);
        }
        finally {
            TestcasesReportsSchema.disableToAMoPSplitting = true;
        }
    }
    
    @Test
    public void testVSplitEmptyColumn() {
        NiReportModel cor = new NiReportModel("cumulated-disbursements-by-mop")
        .withHeaders(Arrays.asList(
                "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 9))",
                "(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Mode of Payment: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Totals: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 7))",
                "(Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 4));(Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 6, colSpan: 2));(Cumulated Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 8, colSpan: 1))",
                "(Cash: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Direct payment: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Unassigned: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Total: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Unassigned: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Total: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Total: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1))"))
            .withWarnings(Arrays.asList())
            .withBody(      new ReportAreaForTests(null)
              .withContents("Project Title", "", "Mode of Payment", "", "Totals-Actual Commitments-Cash", "111,111", "Totals-Actual Commitments-Direct payment", "222,222", "Totals-Actual Commitments-Unassigned", "8,408,119", "Totals-Actual Commitments-Total", "8,741,452", "Totals-Actual Disbursements-Unassigned", "1,099,304", "Totals-Actual Disbursements-Total", "1,099,304", "Totals-Cumulated Disbursements-Total", "0")
              .withChildren(
                new ReportAreaForTests(new AreaOwner(12), "Project Title", "TAC_activity_1", "Mode of Payment", "", "Totals-Actual Commitments-Unassigned", "213,231", "Totals-Actual Commitments-Total", "213,231", "Totals-Actual Disbursements-Unassigned", "123,321", "Totals-Actual Disbursements-Total", "123,321"),
                new ReportAreaForTests(new AreaOwner(13), "Project Title", "TAC_activity_2", "Mode of Payment", "", "Totals-Actual Commitments-Unassigned", "999,888", "Totals-Actual Commitments-Total", "999,888", "Totals-Actual Disbursements-Unassigned", "453,213", "Totals-Actual Disbursements-Total", "453,213"),
                new ReportAreaForTests(new AreaOwner(26), "Project Title", "date-filters-activity", "Mode of Payment", "", "Totals-Actual Commitments-Unassigned", "125,000", "Totals-Actual Commitments-Total", "125,000", "Totals-Actual Disbursements-Unassigned", "72,000", "Totals-Actual Disbursements-Total", "72,000"),
                new ReportAreaForTests(new AreaOwner(32), "Project Title", "crazy funding 1", "Mode of Payment", "Cash, Direct payment", "Totals-Actual Commitments-Cash", "111,111", "Totals-Actual Commitments-Direct payment", "222,222", "Totals-Actual Commitments-Total", "333,333"),
                new ReportAreaForTests(new AreaOwner(48), "Project Title", "pledged 2", "Mode of Payment", "", "Totals-Actual Commitments-Unassigned", "7,070,000", "Totals-Actual Commitments-Total", "7,070,000", "Totals-Actual Disbursements-Unassigned", "450,000", "Totals-Actual Disbursements-Total", "450,000"),
                new ReportAreaForTests(new AreaOwner(69), "Project Title", "Activity with planned disbursements", "Mode of Payment", "", "Totals-Actual Disbursements-Unassigned", "770", "Totals-Actual Disbursements-Total", "770")      ));

        try {
            TestcasesReportsSchema.disableToAMoPSplitting = false;
            runNiTestCase(cor, buildSpecification("cumulated-disbursements-by-mop", 
                Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.MODE_OF_PAYMENT), 
                Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS, MeasureConstants.CUMULATED_DISBURSEMENTS), 
                null,
                GroupingCriteria.GROUPING_TOTALS_ONLY), 
                "en", humanitarianAidActs);
        }
        finally {
            TestcasesReportsSchema.disableToAMoPSplitting = true;
        }
    }

    @Test
    public void testPredictabilityOfFunding() {
        NiReportModel cor = new NiReportModel("AMP-22639-predictability-of-funding")
        .withHeaders(Arrays.asList(
                "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 14))",
                "(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 1, colSpan: 10));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 11, colSpan: 3))",
                "(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 1, colSpan: 2));(2012: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 2));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 2));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 7, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 9, colSpan: 2))",
                "(Planned Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Planned Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Planned Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Planned Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Planned Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Planned Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1));(Predictability of Funding: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1))"))
            .withWarnings(Arrays.asList())
            .withBody(      new ReportAreaForTests(null)
              .withContents("Project Title", "", "Funding-2010-Planned Disbursements", "0", "Funding-2010-Actual Disbursements", "780 311", "Funding-2012-Planned Disbursements", "0", "Funding-2012-Actual Disbursements", "12 000", "Funding-2013-Planned Disbursements", "0", "Funding-2013-Actual Disbursements", "1 266 956", "Funding-2014-Planned Disbursements", "146 300", "Funding-2014-Actual Disbursements", "710 200", "Funding-2015-Planned Disbursements", "36 500", "Funding-2015-Actual Disbursements", "437 335", "Totals-Planned Disbursements", "182 800", "Totals-Actual Disbursements", "3 206 802", "Totals-Predictability of Funding", "-1 654,27")
              .withChildren(
                new ReportAreaForTests(new AreaOwner(12), "Project Title", "TAC_activity_1", "Funding-2010-Actual Disbursements", "123 321", "Totals-Actual Disbursements", "123 321"),
                new ReportAreaForTests(new AreaOwner(13), "Project Title", "TAC_activity_2", "Funding-2010-Actual Disbursements", "453 213", "Totals-Actual Disbursements", "453 213"),
                new ReportAreaForTests(new AreaOwner(15), "Project Title", "Proposed Project Cost 1 - USD"),
                new ReportAreaForTests(new AreaOwner(17), "Project Title", "Proposed Project Cost 2 - EUR"),
                new ReportAreaForTests(new AreaOwner(18), "Project Title", "Test MTEF directed", "Funding-2010-Actual Disbursements", "143 777", "Totals-Actual Disbursements", "143 777"),
                new ReportAreaForTests(new AreaOwner(19), "Project Title", "Pure MTEF Project"),
                new ReportAreaForTests(new AreaOwner(21), "Project Title", "activity with components"),
                new ReportAreaForTests(new AreaOwner(23), "Project Title", "Project with documents"),
                new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Funding-2013-Actual Disbursements", "545 000", "Totals-Actual Disbursements", "545 000"),
                new ReportAreaForTests(new AreaOwner(25), "Project Title", "mtef activity 1"),
                new ReportAreaForTests(new AreaOwner(26), "Project Title", "date-filters-activity", "Funding-2010-Actual Disbursements", "60 000", "Funding-2012-Actual Disbursements", "12 000", "Totals-Actual Disbursements", "72 000"),
                new ReportAreaForTests(new AreaOwner(27), "Project Title", "mtef activity 2"),
                new ReportAreaForTests(new AreaOwner(28), "Project Title", "ptc activity 1"),
                new ReportAreaForTests(new AreaOwner(29), "Project Title", "ptc activity 2"),
                new ReportAreaForTests(new AreaOwner(30), "Project Title", "SSC Project 1", "Funding-2013-Actual Disbursements", "555 111", "Totals-Actual Disbursements", "555 111"),
                new ReportAreaForTests(new AreaOwner(31), "Project Title", "SSC Project 2", "Funding-2013-Actual Disbursements", "131 845", "Totals-Actual Disbursements", "131 845"),
                new ReportAreaForTests(new AreaOwner(32), "Project Title", "crazy funding 1"),
                new ReportAreaForTests(new AreaOwner(33), "Project Title", "Activity with Zones"),
                new ReportAreaForTests(new AreaOwner(36), "Project Title", "Activity With Zones and Percentages"),
                new ReportAreaForTests(new AreaOwner(40), "Project Title", "SubNational no percentages"),
                new ReportAreaForTests(new AreaOwner(41), "Project Title", "Activity Linked With Pledge"),
                new ReportAreaForTests(new AreaOwner(43), "Project Title", "Activity with primary_tertiary_program"),
                new ReportAreaForTests(new AreaOwner(44), "Project Title", "activity with primary_program"),
                new ReportAreaForTests(new AreaOwner(45), "Project Title", "activity with tertiary_program"),
                new ReportAreaForTests(new AreaOwner(46), "Project Title", "pledged education activity 1"),
                new ReportAreaForTests(new AreaOwner(48), "Project Title", "pledged 2", "Funding-2014-Actual Disbursements", "450 000", "Totals-Actual Disbursements", "450 000"),
                new ReportAreaForTests(new AreaOwner(50), "Project Title", "activity with capital spending", "Funding-2014-Planned Disbursements", "90 000", "Funding-2014-Actual Disbursements", "80 000", "Totals-Planned Disbursements", "90 000", "Totals-Actual Disbursements", "80 000", "Totals-Predictability of Funding", "11,11"),
                new ReportAreaForTests(new AreaOwner(52), "Project Title", "activity with contracting agency", "Funding-2014-Actual Disbursements", "50 000", "Totals-Actual Disbursements", "50 000"),
                new ReportAreaForTests(new AreaOwner(53), "Project Title", "new activity with contracting"),
                new ReportAreaForTests(new AreaOwner(61), "Project Title", "activity-with-unfunded-components"),
                new ReportAreaForTests(new AreaOwner(63), "Project Title", "activity with funded components"),
                new ReportAreaForTests(new AreaOwner(64), "Project Title", "Unvalidated activity"),
                new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Funding-2015-Actual Disbursements", "321 765", "Totals-Actual Disbursements", "321 765"),
                new ReportAreaForTests(new AreaOwner(66), "Project Title", "Activity 2 with multiple agreements"),
                new ReportAreaForTests(new AreaOwner(67), "Project Title", "third activity with agreements"),
                new ReportAreaForTests(new AreaOwner(68), "Project Title", "activity with incomplete agreement"),
                new ReportAreaForTests(new AreaOwner(69), "Project Title", "Activity with planned disbursements", "Funding-2014-Planned Disbursements", "300", "Funding-2014-Actual Disbursements", "200", "Funding-2015-Planned Disbursements", "500", "Funding-2015-Actual Disbursements", "570", "Totals-Planned Disbursements", "800", "Totals-Actual Disbursements", "770", "Totals-Predictability of Funding", "3,75"),
                new ReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms"),
                new ReportAreaForTests(new AreaOwner(71), "Project Title", "activity_with_disaster_response"),
                new ReportAreaForTests(new AreaOwner(73), "Project Title", "activity with directed MTEFs"),
                new ReportAreaForTests(new AreaOwner(76), "Project Title", "activity with pipeline MTEFs and act. disb", "Funding-2013-Actual Disbursements", "35 000", "Funding-2014-Actual Disbursements", "75 000", "Totals-Actual Disbursements", "110 000"),
                new ReportAreaForTests(new AreaOwner(77), "Project Title", "execution rate activity", "Funding-2014-Planned Disbursements", "56 000", "Funding-2014-Actual Disbursements", "55 000", "Funding-2015-Planned Disbursements", "36 000", "Funding-2015-Actual Disbursements", "35 000", "Totals-Planned Disbursements", "92 000", "Totals-Actual Disbursements", "90 000", "Totals-Predictability of Funding", "2,17"),
                new ReportAreaForTests(new AreaOwner(78), "Project Title", "activity with many MTEFs", "Funding-2015-Actual Disbursements", "80 000", "Totals-Actual Disbursements", "80 000"),
                new ReportAreaForTests(new AreaOwner(79), "Project Title", "with weird currencies")      ));

        runNiTestCase(cor, spec("AMP-22639-predictability-of-funding"), "en", acts);
    }
    
    @Test
    public void testPercentageOfCommsDisbsNoPrecursors() {
        NiReportModel cor = new NiReportModel("AMP-15795-percentage-of-total-commitments-no-precursors-hier")
        .withHeaders(Arrays.asList(
                "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 5))",
                "(Region: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Type Of Assistance: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 3, colSpan: 2))",
                "",
                "(Percentage of Total Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Percentage Of Total Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1))"))
            .withWarnings(Arrays.asList())
            .withBody(      new ReportAreaForTests(null)
              .withContents("Region", "", "Type Of Assistance", "", "Project Title", "", "Totals-Percentage of Total Commitments", "100", "Totals-Percentage Of Total Disbursements", "100")
              .withChildren(
                new ReportAreaForTests(new AreaOwner("Region", "Anenii Noi County", 9085)).withContents("Type Of Assistance", "", "Project Title", "", "Totals-Percentage of Total Commitments", "8,3", "Totals-Percentage Of Total Disbursements", "38,79", "Region", "Anenii Noi County")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner("Type Of Assistance", "default type of assistance", 2119))
                  .withContents("Project Title", "", "Totals-Percentage of Total Commitments", "8,3", "Totals-Percentage Of Total Disbursements", "38,79", "Type Of Assistance", "default type of assistance")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(18), "Project Title", "Test MTEF directed", "Totals-Percentage Of Total Disbursements", "4,48"),
                    new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Totals-Percentage Of Total Disbursements", "17"),
                    new ReportAreaForTests(new AreaOwner(28), "Project Title", "ptc activity 1", "Totals-Percentage of Total Commitments", "3,44"),
                    new ReportAreaForTests(new AreaOwner(29), "Project Title", "ptc activity 2", "Totals-Percentage of Total Commitments", "1,72"),
                    new ReportAreaForTests(new AreaOwner(30), "Project Title", "SSC Project 1", "Totals-Percentage of Total Commitments", "0,57", "Totals-Percentage Of Total Disbursements", "17,31"),
                    new ReportAreaForTests(new AreaOwner(33), "Project Title", "Activity with Zones", "Totals-Percentage of Total Commitments", "1,47"),
                    new ReportAreaForTests(new AreaOwner(36), "Project Title", "Activity With Zones and Percentages", "Totals-Percentage of Total Commitments", "0,92"),
                    new ReportAreaForTests(new AreaOwner(40), "Project Title", "SubNational no percentages", "Totals-Percentage of Total Commitments", "0,19")          )        ),
                new ReportAreaForTests(new AreaOwner("Region", "Balti County", 9086))
                .withContents("Type Of Assistance", "", "Project Title", "", "Totals-Percentage of Total Commitments", "11,05", "Totals-Percentage Of Total Disbursements", "10,89", "Region", "Balti County")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner("Type Of Assistance", "default type of assistance", 2119))
                  .withContents("Project Title", "", "Totals-Percentage of Total Commitments", "9,9", "Totals-Percentage Of Total Disbursements", "10,89", "Type Of Assistance", "default type of assistance")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(32), "Project Title", "crazy funding 1", "Totals-Percentage of Total Commitments", "0,57"),
                    new ReportAreaForTests(new AreaOwner(33), "Project Title", "Activity with Zones", "Totals-Percentage of Total Commitments", "1,47"),
                    new ReportAreaForTests(new AreaOwner(36), "Project Title", "Activity With Zones and Percentages", "Totals-Percentage of Total Commitments", "3,67"),
                    new ReportAreaForTests(new AreaOwner(40), "Project Title", "SubNational no percentages", "Totals-Percentage of Total Commitments", "0,19"),
                    new ReportAreaForTests(new AreaOwner(52), "Project Title", "activity with contracting agency", "Totals-Percentage of Total Commitments", "0,27", "Totals-Percentage Of Total Disbursements", "0,86"),
                    new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Totals-Percentage of Total Commitments", "2,35", "Totals-Percentage Of Total Disbursements", "10,03"),
                    new ReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms", "Totals-Percentage of Total Commitments", "1,37")          ),
                  new ReportAreaForTests(new AreaOwner("Type Of Assistance", "second type of assistance", 2124)).withContents("Project Title", "", "Totals-Percentage of Total Commitments", "1,14", "Totals-Percentage Of Total Disbursements", "0", "Type Of Assistance", "second type of assistance")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(32), "Project Title", "crazy funding 1", "Totals-Percentage of Total Commitments", "1,14")          )        ),
                new ReportAreaForTests(new AreaOwner("Region", "Cahul County", 9087)).withContents("Type Of Assistance", "", "Project Title", "", "Totals-Percentage of Total Commitments", "36,43", "Totals-Percentage Of Total Disbursements", "14,03", "Region", "Cahul County")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner("Type Of Assistance", "second type of assistance", 2124)).withContents("Project Title", "", "Totals-Percentage of Total Commitments", "36,43", "Totals-Percentage Of Total Disbursements", "14,03", "Type Of Assistance", "second type of assistance")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(48), "Project Title", "pledged 2", "Totals-Percentage of Total Commitments", "36,43", "Totals-Percentage Of Total Disbursements", "14,03")          )        ),
                new ReportAreaForTests(new AreaOwner("Region", "Chisinau City", 9088)).withContents("Type Of Assistance", "", "Project Title", "", "Totals-Percentage of Total Commitments", "1,53", "Totals-Percentage Of Total Disbursements", "1,4", "Region", "Chisinau City")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner("Type Of Assistance", "default type of assistance", 2119))
                  .withContents("Project Title", "", "Totals-Percentage of Total Commitments", "1,53", "Totals-Percentage Of Total Disbursements", "1,4", "Type Of Assistance", "default type of assistance")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(41), "Project Title", "Activity Linked With Pledge", "Totals-Percentage of Total Commitments", "0,26"),
                    new ReportAreaForTests(new AreaOwner(67), "Project Title", "third activity with agreements", "Totals-Percentage of Total Commitments", "0,64"),
                    new ReportAreaForTests(new AreaOwner(73), "Project Title", "activity with directed MTEFs", "Totals-Percentage of Total Commitments", "0,64"),
                    new ReportAreaForTests(new AreaOwner(77), "Project Title", "execution rate activity", "Totals-Percentage Of Total Disbursements", "1,4")          )        ),
                new ReportAreaForTests(new AreaOwner("Region", "Chisinau County", 9089))
                .withContents("Type Of Assistance", "", "Project Title", "", "Totals-Percentage of Total Commitments", "26,11", "Totals-Percentage Of Total Disbursements", "5,92", "Region", "Chisinau County")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner("Type Of Assistance", "default type of assistance", 2119))
                  .withContents("Project Title", "", "Totals-Percentage of Total Commitments", "25,77", "Totals-Percentage Of Total Disbursements", "3,43", "Type Of Assistance", "default type of assistance")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(46), "Project Title", "pledged education activity 1", "Totals-Percentage of Total Commitments", "25,76"),
                    new ReportAreaForTests(new AreaOwner(66), "Project Title", "Activity 2 with multiple agreements", "Totals-Percentage of Total Commitments", "0,01"),
                    new ReportAreaForTests(new AreaOwner(76), "Project Title", "activity with pipeline MTEFs and act. disb", "Totals-Percentage Of Total Disbursements", "3,43")          ),
                  new ReportAreaForTests(new AreaOwner("Type Of Assistance", "second type of assistance", 2124)).withContents("Project Title", "", "Totals-Percentage of Total Commitments", "0,34", "Totals-Percentage Of Total Disbursements", "2,49", "Type Of Assistance", "second type of assistance")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(50), "Project Title", "activity with capital spending", "Totals-Percentage of Total Commitments", "0,34", "Totals-Percentage Of Total Disbursements", "2,49")          )        ),
                new ReportAreaForTests(new AreaOwner("Region", "Drochia County", 9090)).withContents("Type Of Assistance", "", "Project Title", "", "Totals-Percentage of Total Commitments", "3,2", "Totals-Percentage Of Total Disbursements", "2,49", "Region", "Drochia County")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner("Type Of Assistance", "default type of assistance", 2119))
                  .withContents("Project Title", "", "Totals-Percentage of Total Commitments", "3,2", "Totals-Percentage Of Total Disbursements", "2,49", "Type Of Assistance", "default type of assistance")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms", "Totals-Percentage of Total Commitments", "3,2"),
                    new ReportAreaForTests(new AreaOwner(78), "Project Title", "activity with many MTEFs", "Totals-Percentage Of Total Disbursements", "2,49")          )        ),
                new ReportAreaForTests(new AreaOwner("Region", "Dubasari County", 9091)).withContents("Type Of Assistance", "", "Project Title", "", "Totals-Percentage of Total Commitments", "1,1", "Totals-Percentage Of Total Disbursements", "5,25", "Region", "Dubasari County")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner("Type Of Assistance", "default type of assistance", 2119))
                  .withContents("Project Title", "", "Totals-Percentage of Total Commitments", "1,1", "Totals-Percentage Of Total Disbursements", "5,25", "Type Of Assistance", "default type of assistance")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(12), "Project Title", "TAC_activity_1", "Totals-Percentage of Total Commitments", "1,1", "Totals-Percentage Of Total Disbursements", "3,85"),
                    new ReportAreaForTests(new AreaOwner(77), "Project Title", "execution rate activity", "Totals-Percentage Of Total Disbursements", "1,4")          )        ),
                new ReportAreaForTests(new AreaOwner("Region", "Edinet County", 9092)).withContents("Type Of Assistance", "", "Project Title", "", "Totals-Percentage of Total Commitments", "2,92", "Totals-Percentage Of Total Disbursements", "4,11", "Region", "Edinet County")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner("Type Of Assistance", "default type of assistance", 2119)).withContents("Project Title", "", "Totals-Percentage of Total Commitments", "2,92", "Totals-Percentage Of Total Disbursements", "4,11", "Type Of Assistance", "default type of assistance")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(31), "Project Title", "SSC Project 2", "Totals-Percentage of Total Commitments", "2,92", "Totals-Percentage Of Total Disbursements", "4,11")          )        ),
                new ReportAreaForTests(new AreaOwner("Region", "Falesti County", 9093)).withContents("Type Of Assistance", "", "Project Title", "", "Totals-Percentage of Total Commitments", "5,15", "Totals-Percentage Of Total Disbursements", "14,13", "Region", "Falesti County")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner("Type Of Assistance", "default type of assistance", 2119)).withContents("Project Title", "", "Totals-Percentage of Total Commitments", "5,15", "Totals-Percentage Of Total Disbursements", "14,13", "Type Of Assistance", "default type of assistance")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(13), "Project Title", "TAC_activity_2", "Totals-Percentage of Total Commitments", "5,15", "Totals-Percentage Of Total Disbursements", "14,13")          )        ),
                new ReportAreaForTests(new AreaOwner("Region", "Transnistrian Region", 9105))
                .withContents("Type Of Assistance", "", "Project Title", "", "Totals-Percentage of Total Commitments", "0,86", "Totals-Percentage Of Total Disbursements", "0,7", "Region", "Transnistrian Region")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner("Type Of Assistance", "default type of assistance", 2119)).withContents("Project Title", "", "Totals-Percentage of Total Commitments", "0,22", "Totals-Percentage Of Total Disbursements", "0,7", "Type Of Assistance", "default type of assistance")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(52), "Project Title", "activity with contracting agency", "Totals-Percentage of Total Commitments", "0,22", "Totals-Percentage Of Total Disbursements", "0,7")          ),
                  new ReportAreaForTests(new AreaOwner("Type Of Assistance", "second type of assistance", 2124)).withContents("Project Title", "", "Totals-Percentage of Total Commitments", "0,64", "Totals-Percentage Of Total Disbursements", "0", "Type Of Assistance", "second type of assistance")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(61), "Project Title", "activity-with-unfunded-components", "Totals-Percentage of Total Commitments", "0,64")          )        ),
                new ReportAreaForTests(new AreaOwner("Region", "Region: Undefined", -8977)).withContents("Type Of Assistance", "", "Project Title", "", "Totals-Percentage of Total Commitments", "3,35", "Totals-Percentage Of Total Disbursements", "2,27", "Region", "Region: Undefined")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner("Type Of Assistance", "default type of assistance", 2119))
                  .withContents("Project Title", "", "Totals-Percentage of Total Commitments", "3,35", "Totals-Percentage Of Total Disbursements", "2,27", "Type Of Assistance", "default type of assistance")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(26), "Project Title", "date-filters-activity", "Totals-Percentage of Total Commitments", "0,64", "Totals-Percentage Of Total Disbursements", "2,25"),
                    new ReportAreaForTests(new AreaOwner(43), "Project Title", "Activity with primary_tertiary_program", "Totals-Percentage of Total Commitments", "0,26"),
                    new ReportAreaForTests(new AreaOwner(44), "Project Title", "activity with primary_program", "Totals-Percentage of Total Commitments", "0,16"),
                    new ReportAreaForTests(new AreaOwner(45), "Project Title", "activity with tertiary_program", "Totals-Percentage of Total Commitments", "0,08"),
                    new ReportAreaForTests(new AreaOwner(53), "Project Title", "new activity with contracting", "Totals-Percentage of Total Commitments", "0,06"),
                    new ReportAreaForTests(new AreaOwner(63), "Project Title", "activity with funded components", "Totals-Percentage of Total Commitments", "0"),
                    new ReportAreaForTests(new AreaOwner(64), "Project Title", "Unvalidated activity", "Totals-Percentage of Total Commitments", "0,23"),
                    new ReportAreaForTests(new AreaOwner(68), "Project Title", "activity with incomplete agreement", "Totals-Percentage of Total Commitments", "0,63"),
                    new ReportAreaForTests(new AreaOwner(69), "Project Title", "Activity with planned disbursements", "Totals-Percentage Of Total Disbursements", "0,02"),
                    new ReportAreaForTests(new AreaOwner(71), "Project Title", "activity_with_disaster_response", "Totals-Percentage of Total Commitments", "0,77"),
                    new ReportAreaForTests(new AreaOwner(79), "Project Title", "with weird currencies", "Totals-Percentage of Total Commitments", "0,5")          )        )      ));
        
        runNiTestCase(cor, spec("AMP-15795-percentage-of-total-commitments-no-precursors-hier"), "en", acts);
    }

    public void testOutputHeaders_noDesc() {
        String expectedHeaders = "{rootHeaders=" +
                "{name=Project Title}, " +
                "{name=Totals}, " +
                "leafHeaders=" +
                "{name=Project Title}, " +
                "{parent={name=Totals}, name=Actual Disbursements}}";
        String actualHeaders = buildDigest(
                buildSpecification("testOutputHeaders_noDesc",
                        Arrays.asList(ColumnConstants.PROJECT_TITLE),
                        Arrays.asList(MeasureConstants.ACTUAL_DISBURSEMENTS),
                        null,
                        GroupingCriteria.GROUPING_TOTALS_ONLY), acts, new AmpSchemaHeaderDigest());
        assertEquals(expectedHeaders, actualHeaders);
    }

    public void testOutputHeaders_columnWithDesc() {
        String expectedHeaders = "{rootHeaders=" +
                "{name=Activity Count, desc=Count Of Activities under the current hierarchy}, " +
                "{name=Totals}, " +
                "leafHeaders=" +
                "{name=Activity Count, desc=Count Of Activities under the current hierarchy}, " +
                "{parent={name=Totals}, name=Actual Disbursements}}";
        String actualHeaders = buildDigest(
                buildSpecification("testOutputHeaders_columnWithDesc",
                        Arrays.asList(ColumnConstants.ACTIVITY_COUNT),
                        Arrays.asList(MeasureConstants.ACTUAL_DISBURSEMENTS),
                        null,
                        GroupingCriteria.GROUPING_TOTALS_ONLY), acts, new AmpSchemaHeaderDigest());
        assertEquals(expectedHeaders, actualHeaders);
    }

    public void testOutputHeaders_measureWithDesc() {
        String expectedHeaders = "{rootHeaders=" +
                "{name=Project Title}, " +
                "{name=Totals}, " +
                "leafHeaders=" +
                "{name=Project Title}, " +
                "{parent={name=Totals}, name=Last Year of Planned Disbursements, desc=Previous Year Planned Disbursements}}";
        String actualHeaders = buildDigest(
                buildSpecification("testOutputHeaders_measureWithDesc",
                        Arrays.asList(ColumnConstants.PROJECT_TITLE),
                        Arrays.asList(MeasureConstants.LAST_YEAR_OF_PLANNED_DISBURSEMENTS),
                        null,
                        GroupingCriteria.GROUPING_TOTALS_ONLY), acts, new AmpSchemaHeaderDigest());
        assertEquals(expectedHeaders, actualHeaders);
    }

    public void testOutputHeaders_columnAndMeasureWithDesc() {
        String expectedHeaders = "{rootHeaders=" +
                "{name=Activity Count, desc=Count Of Activities under the current hierarchy}, " +
                "{name=Totals}, " +
                "leafHeaders={name=Activity Count, desc=Count Of Activities under the current hierarchy}, " +
                "{parent={name=Totals}, name=Last Year of Planned Disbursements, desc=Previous Year Planned Disbursements}}";
        String actualHeaders = buildDigest(
                buildSpecification("testOutputHeaders_columnAndMeasureWithDesc",
                        Arrays.asList(ColumnConstants.ACTIVITY_COUNT),
                        Arrays.asList(MeasureConstants.LAST_YEAR_OF_PLANNED_DISBURSEMENTS),
                        null,
                        GroupingCriteria.GROUPING_TOTALS_ONLY), acts, new AmpSchemaHeaderDigest());
        assertEquals(expectedHeaders, actualHeaders);
    }

    public void testOutputHeaders_nonLeafDesc() {
        try {
            TestcasesReportsSchema.disableToAMoPSplitting = false;
            String expectedHeaders = "{rootHeaders=" +
                    "{name=Project Title}, " +
                    "{name=Mode of Payment}, " +
                    "{name=Totals}, " +
                    "leafHeaders=" +
                    "{name=Project Title}, " +
                    "{name=Mode of Payment}, " +
                    "{parent={parent={name=Totals}, name=Undisbursed Balance, desc=Total Actual Commitment - Total Actual Disbursement}, name=Cash}, " +
                    "{parent={parent={name=Totals}, name=Undisbursed Balance, desc=Total Actual Commitment - Total Actual Disbursement}, name=Direct payment}, " +
                    "{parent={parent={name=Totals}, name=Undisbursed Balance, desc=Total Actual Commitment - Total Actual Disbursement}, name=No Information}, " +
                    "{parent={parent={name=Totals}, name=Undisbursed Balance, desc=Total Actual Commitment - Total Actual Disbursement}, name=Reimbursable}, " +
                    "{parent={parent={name=Totals}, name=Undisbursed Balance, desc=Total Actual Commitment - Total Actual Disbursement}, name=Unassigned}, " +
                    "{parent={parent={name=Totals}, name=Undisbursed Balance, desc=Total Actual Commitment - Total Actual Disbursement}, name=Total}}";
            String actualHeaders = buildDigest(spec("AMP-15863-mode-of-payment-undisbursed-balance"), acts, new AmpSchemaHeaderDigest());
            assertEquals(expectedHeaders, actualHeaders);
        } finally {
            TestcasesReportsSchema.disableToAMoPSplitting = true;
        }
    }

    @Test
    public void testSplitByFundingSimpleHierarchy() {
        NiReportModel cor = new NiReportModel("test split by funding simple hierarchy")
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null).withContents("Project Title", "", "Type Of Assistance", "", "Funding-2013-Actual Commitments", "333,333", "Totals-Actual Commitments", "333,333")
                        .withChildren(
                                new ReportAreaForTests(new AreaOwner("Project Title", "crazy funding 1", 32))
                                        .withContents("Type Of Assistance", "", "Funding-2013-Actual Commitments", "333,333", "Totals-Actual Commitments", "333,333", "Project Title", "crazy funding 1")
                                        .withChildren(
                                                new ReportAreaForTests(new AreaOwner(32), "Type Of Assistance", "default type of assistance", "Funding-2013-Actual Commitments", "111,111", "Totals-Actual Commitments", "111,111"),
                                                new ReportAreaForTests(new AreaOwner(32), "Type Of Assistance", "second type of assistance", "Funding-2013-Actual Commitments", "222,222", "Totals-Actual Commitments", "222,222")        )      ));

        List<String> acts = Arrays.asList("crazy funding 1");

        ReportSpecificationImpl spec = buildSpecification("test split by funding simple hierarchy",
                Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.TYPE_OF_ASSISTANCE, ColumnConstants.FUNDING_ID),
                Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS),
                Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.FUNDING_ID), GroupingCriteria.GROUPING_YEARLY);
        spec.addInvisibleHierarchy(new ReportColumn(ColumnConstants.FUNDING_ID));

        runNiTestCase(spec, "en", acts, cor);
    }

    @Test
    public void testSplitByFundingNoHierarchy() {
        NiReportModel cor = new NiReportModel("test split by funding no hierarchy")
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                        .withContents("Project Title", "", "Type Of Assistance", "", "Funding-2013-Actual Commitments", "333,333", "Totals-Actual Commitments", "333,333")
                        .withChildren(
                                new ReportAreaForTests(new AreaOwner(32), "Project Title", "crazy funding 1", "Type Of Assistance", "default type of assistance", "Funding-2013-Actual Commitments", "111,111", "Totals-Actual Commitments", "111,111"),
                                new ReportAreaForTests(new AreaOwner(32), "Project Title", "crazy funding 1", "Type Of Assistance", "second type of assistance", "Funding-2013-Actual Commitments", "222,222", "Totals-Actual Commitments", "222,222")      ));

        List<String> acts = Arrays.asList("crazy funding 1");

        ReportSpecificationImpl spec = buildSpecification("test split by funding no hierarchy",
                Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.TYPE_OF_ASSISTANCE, ColumnConstants.FUNDING_ID),
                Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS),
                Arrays.asList(ColumnConstants.FUNDING_ID), GroupingCriteria.GROUPING_YEARLY);
        spec.addInvisibleHierarchy(new ReportColumn(ColumnConstants.FUNDING_ID));

        runNiTestCase(spec, "en", acts, cor);
    }

    @Test
    public void testSplitByFundingDoesNotAffectSummaryReports() {
        NiReportModel cor = new NiReportModel("test split by funding does not affect summary")
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null).withContents("Project Title", "", "Funding-2013-Actual Commitments", "333,333", "Totals-Actual Commitments", "333,333")
                        .withChildren(
                                new ReportAreaForTests(new AreaOwner("Project Title", "crazy funding 1", 32), "Funding-2013-Actual Commitments", "333,333", "Totals-Actual Commitments", "333,333", "Project Title", "crazy funding 1")      ));

        List<String> acts = Arrays.asList("crazy funding 1");

        ReportSpecificationImpl specWithSplit = buildSpecification("test split by funding does not affect summary",
                Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.FUNDING_ID),
                Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS),
                Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.FUNDING_ID), GroupingCriteria.GROUPING_YEARLY);
        specWithSplit.addInvisibleHierarchy(new ReportColumn(ColumnConstants.FUNDING_ID));
        specWithSplit.setSummaryReport(true);

        runNiTestCase(specWithSplit, "en", acts, cor);

        ReportSpecificationImpl specWithoutSplit = buildSpecification("test split by funding does not affect summary",
                Arrays.asList(ColumnConstants.PROJECT_TITLE),
                Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS),
                Arrays.asList(ColumnConstants.PROJECT_TITLE), GroupingCriteria.GROUPING_YEARLY);
        specWithoutSplit.setSummaryReport(true);

        runNiTestCase(specWithoutSplit, "en", acts, cor);
    }
    
    @Test
    public void testSimpleHasExecutingAgencyReport() {
        NiReportModel cor = new NiReportModel("testSimpleHasExecutingAgencyReport")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 7))",
                        "(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Executing Agency: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Has Executing Agency: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 3, colSpan: 3));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 6, colSpan: 1))",
                        "(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 1));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 1));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 1))",
                        "(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                      .withContents("Project Title", "", "Executing Agency", "", "Has Executing Agency", "", "Funding-2010-Actual Disbursements", "143,777", "Funding-2013-Actual Disbursements", "545,000", "Funding-2014-Actual Disbursements", "50,000", "Totals-Actual Disbursements", "738,777")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(18), "Project Title", "Test MTEF directed", "Executing Agency", "Water Foundation", "Has Executing Agency", "yes", "Funding-2010-Actual Disbursements", "143,777", "Totals-Actual Disbursements", "143,777"),
                        new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Executing Agency", "UNDP, World Bank", "Has Executing Agency", "yes", "Funding-2013-Actual Disbursements", "545,000", "Totals-Actual Disbursements", "545,000"),
                        new ReportAreaForTests(new AreaOwner(33), "Project Title", "Activity with Zones", "Executing Agency", "", "Has Executing Agency", "no"),
                        new ReportAreaForTests(new AreaOwner(52), "Project Title", "activity with contracting agency", "Executing Agency", "Water Foundation", "Has Executing Agency", "yes", "Funding-2014-Actual Disbursements", "50,000", "Totals-Actual Disbursements", "50,000"),
                        new ReportAreaForTests(new AreaOwner(79), "Project Title", "with weird currencies", "Executing Agency", "", "Has Executing Agency", "no")      ));

        List<String> execActs = Arrays.asList("Eth Water", "Test MTEF directed", "Activity with Zones",
                "activity with contracting agency", "with weird currencies","activity with contracting agency");
        
        ReportSpecificationImpl spec = buildSpecification("testSimpleHasExecutingAgencyReport",
                Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.EXECUTING_AGENCY, ColumnConstants.HAS_EXECUTING_AGENCY),
                Arrays.asList(MeasureConstants.ACTUAL_DISBURSEMENTS),
                null,
                GroupingCriteria.GROUPING_YEARLY);

        runNiTestCase(spec, "en", execActs, cor);
    }
    
    @Test
    public void testReportWithHierByHasExecutingAgencyReport() {
        NiReportModel cor = new NiReportModel("testReportWithHierByHasExecutingAgencyReport")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 7))",
                        "(Has Executing Agency: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Executing Agency: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 3, colSpan: 3));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 6, colSpan: 1))",
                        "(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 1));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 1));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 1))",
                        "(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null).withContents("Has Executing Agency", "", "Project Title", "", "Executing Agency", "", "Funding-2010-Actual Disbursements", "143,777", "Funding-2013-Actual Disbursements", "545,000", "Funding-2014-Actual Disbursements", "50,000", "Totals-Actual Disbursements", "738,777")
                      .withChildren(
                              new ReportAreaForTests(new AreaOwner("Has Executing Agency", "yes", 1))
                                .withContents("Project Title", "", "Executing Agency", "", "Funding-2010-Actual Disbursements", "143,777", "Funding-2013-Actual Disbursements", "545,000", "Funding-2014-Actual Disbursements", "50,000", "Totals-Actual Disbursements", "738,777", "Has Executing Agency", "yes")
                                .withChildren(
                                        new ReportAreaForTests(new AreaOwner(18), "Project Title", "Test MTEF directed", "Executing Agency", "Water Foundation", "Funding-2010-Actual Disbursements", "143,777", "Totals-Actual Disbursements", "143,777"),
                                        new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Executing Agency", "UNDP, World Bank", "Funding-2013-Actual Disbursements", "545,000", "Totals-Actual Disbursements", "545,000"),
                                        new ReportAreaForTests(new AreaOwner(52), "Project Title", "activity with contracting agency", "Executing Agency", "Water Foundation", "Funding-2014-Actual Disbursements", "50,000", "Totals-Actual Disbursements", "50,000")        )      ));

        List<String> execActs = Arrays.asList("Eth Water", "Test MTEF directed", "Activity with Zones",
                "activity with contracting agency", "with weird currencies","activity with contracting agency");
        
        ReportSpecificationImpl spec = buildSpecification("testReportWithHierByHasExecutingAgencyReport",
                Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.EXECUTING_AGENCY, ColumnConstants.HAS_EXECUTING_AGENCY),
                Arrays.asList(MeasureConstants.ACTUAL_DISBURSEMENTS),
                Arrays.asList(ColumnConstants.HAS_EXECUTING_AGENCY),
                GroupingCriteria.GROUPING_YEARLY);

        runNiTestCase(spec, "en", execActs, cor);
    }
    
    @Test
    public void testOverDisbursedAsScheduledFlatReport() {
        NiReportModel cor = new NiReportModel("testOverDisbursedAsScheduledFlatReport")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 27))",
                        "(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Donor Agency: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Primary Sector: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 3, colSpan: 20));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 23, colSpan: 4))",
                        "(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 4));(2012: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 7, colSpan: 4));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 11, colSpan: 4));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 15, colSpan: 4));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 19, colSpan: 4))",
                        "(Planned Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Disbursed as Scheduled: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Over Disbursed: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Planned Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Disbursed as Scheduled: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Over Disbursed: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Planned Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1));(Disbursed as Scheduled: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1));(Over Disbursed: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 14, colSpan: 1));(Planned Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 15, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 16, colSpan: 1));(Disbursed as Scheduled: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 17, colSpan: 1));(Over Disbursed: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 18, colSpan: 1));(Planned Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 19, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 20, colSpan: 1));(Disbursed as Scheduled: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 21, colSpan: 1));(Over Disbursed: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 22, colSpan: 1));(Planned Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 23, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 24, colSpan: 1));(Disbursed as Scheduled: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 25, colSpan: 1));(Over Disbursed: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 26, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                        .withContents("Project Title", "", "Donor Agency", "", "Primary Sector", "", "Funding-2010-Planned Disbursements", "0", "Funding-2010-Actual Disbursements", "780,311", "Funding-2012-Planned Disbursements", "0", "Funding-2012-Actual Disbursements", "12,000", "Funding-2013-Planned Disbursements", "0", "Funding-2013-Actual Disbursements", "1,266,956", "Funding-2014-Planned Disbursements", "146,300", "Funding-2014-Actual Disbursements", "710,200", "Funding-2014-Disbursed as Scheduled", "100", "Funding-2014-Over Disbursed", "79,4", "Funding-2015-Planned Disbursements", "36,500", "Funding-2015-Actual Disbursements", "437,335", "Funding-2015-Disbursed as Scheduled", "100", "Funding-2015-Over Disbursed", "91,65", "Totals-Planned Disbursements", "182,800", "Totals-Actual Disbursements", "3,206,802", "Totals-Disbursed as Scheduled", "100", "Totals-Over Disbursed", "94,3")
                        .withChildren(
                                new ReportAreaForTests(new AreaOwner(12), "Project Title", "TAC_activity_1", "Donor Agency", "World Bank", "Primary Sector", "112 - BASIC EDUCATION", "Funding-2010-Actual Disbursements", "123,321", "Totals-Actual Disbursements", "123,321"),
                                new ReportAreaForTests(new AreaOwner(13), "Project Title", "TAC_activity_2", "Donor Agency", "Water Foundation", "Primary Sector", "130 - POPULATION POLICIES/PROGRAMMES AND REPRODUCTIVE HEALTH", "Funding-2010-Actual Disbursements", "453,213", "Totals-Actual Disbursements", "453,213"),
                                new ReportAreaForTests(new AreaOwner(15), "Project Title", "Proposed Project Cost 1 - USD", "Donor Agency", "Finland", "Primary Sector", "110 - EDUCATION"),
                                new ReportAreaForTests(new AreaOwner(17), "Project Title", "Proposed Project Cost 2 - EUR", "Donor Agency", "Ministry of Economy", "Primary Sector", "110 - EDUCATION"),
                                new ReportAreaForTests(new AreaOwner(18), "Project Title", "Test MTEF directed", "Donor Agency", "Ministry of Economy, USAID", "Primary Sector", "110 - EDUCATION", "Funding-2010-Actual Disbursements", "143,777", "Totals-Actual Disbursements", "143,777"),
                                new ReportAreaForTests(new AreaOwner(19), "Project Title", "Pure MTEF Project", "Donor Agency", "Ministry of Economy, Ministry of Finance", "Primary Sector", "110 - EDUCATION"),
                                new ReportAreaForTests(new AreaOwner(21), "Project Title", "activity with components", "Donor Agency", "Finland", "Primary Sector", "110 - EDUCATION"),
                                new ReportAreaForTests(new AreaOwner(23), "Project Title", "Project with documents", "Donor Agency", "USAID", "Primary Sector", "110 - EDUCATION"),
                                new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Donor Agency", "Finland, Norway, USAID", "Primary Sector", "110 - EDUCATION", "Funding-2013-Actual Disbursements", "545,000", "Totals-Actual Disbursements", "545,000"),
                                new ReportAreaForTests(new AreaOwner(25), "Project Title", "mtef activity 1", "Donor Agency", "UNDP", "Primary Sector", "110 - EDUCATION"),
                                new ReportAreaForTests(new AreaOwner(26), "Project Title", "date-filters-activity", "Donor Agency", "Ministry of Finance", "Primary Sector", "110 - EDUCATION", "Funding-2010-Actual Disbursements", "60,000", "Funding-2012-Actual Disbursements", "12,000", "Totals-Actual Disbursements", "72,000"),
                                new ReportAreaForTests(new AreaOwner(27), "Project Title", "mtef activity 2", "Donor Agency", "Finland", "Primary Sector", "110 - EDUCATION"),
                                new ReportAreaForTests(new AreaOwner(28), "Project Title", "ptc activity 1", "Donor Agency", "Finland", "Primary Sector", "110 - EDUCATION"),
                                new ReportAreaForTests(new AreaOwner(29), "Project Title", "ptc activity 2", "Donor Agency", "USAID", "Primary Sector", "110 - EDUCATION"),
                                new ReportAreaForTests(new AreaOwner(30), "Project Title", "SSC Project 1", "Donor Agency", "Finland", "Primary Sector", "110 - EDUCATION", "Funding-2013-Actual Disbursements", "555,111", "Totals-Actual Disbursements", "555,111"),
                                new ReportAreaForTests(new AreaOwner(31), "Project Title", "SSC Project 2", "Donor Agency", "Water Org", "Primary Sector", "112 - BASIC EDUCATION", "Funding-2013-Actual Disbursements", "131,845", "Totals-Actual Disbursements", "131,845"),
                                new ReportAreaForTests(new AreaOwner(32), "Project Title", "crazy funding 1", "Donor Agency", "Finland", "Primary Sector", "110 - EDUCATION"),
                                new ReportAreaForTests(new AreaOwner(33), "Project Title", "Activity with Zones", "Donor Agency", "Finland", "Primary Sector", "110 - EDUCATION"),
                                new ReportAreaForTests(new AreaOwner(36), "Project Title", "Activity With Zones and Percentages", "Donor Agency", "Norway", "Primary Sector", "110 - EDUCATION, 120 - HEALTH"),
                                new ReportAreaForTests(new AreaOwner(40), "Project Title", "SubNational no percentages", "Donor Agency", "Ministry of Economy", "Primary Sector", "110 - EDUCATION"),
                                new ReportAreaForTests(new AreaOwner(41), "Project Title", "Activity Linked With Pledge", "Donor Agency", "Finland", "Primary Sector", "110 - EDUCATION"),
                                new ReportAreaForTests(new AreaOwner(43), "Project Title", "Activity with primary_tertiary_program", "Donor Agency", "UNDP", "Primary Sector", "110 - EDUCATION"),
                                new ReportAreaForTests(new AreaOwner(44), "Project Title", "activity with primary_program", "Donor Agency", "World Bank", "Primary Sector", "110 - EDUCATION"),
                                new ReportAreaForTests(new AreaOwner(45), "Project Title", "activity with tertiary_program", "Donor Agency", "Ministry of Economy", "Primary Sector", "110 - EDUCATION"),
                                new ReportAreaForTests(new AreaOwner(46), "Project Title", "pledged education activity 1", "Donor Agency", "USAID", "Primary Sector", "110 - EDUCATION"),
                                new ReportAreaForTests(new AreaOwner(48), "Project Title", "pledged 2", "Donor Agency", "Finland, USAID", "Primary Sector", "113 - SECONDARY EDUCATION", "Funding-2014-Actual Disbursements", "450,000", "Totals-Actual Disbursements", "450,000"),
                                new ReportAreaForTests(new AreaOwner(50), "Project Title", "activity with capital spending", "Donor Agency", "Finland", "Primary Sector", "110 - EDUCATION", "Funding-2014-Planned Disbursements", "90,000", "Funding-2014-Actual Disbursements", "80,000", "Funding-2014-Disbursed as Scheduled", "88,89", "Totals-Planned Disbursements", "90,000", "Totals-Actual Disbursements", "80,000", "Totals-Disbursed as Scheduled", "88,89"),
                                new ReportAreaForTests(new AreaOwner(52), "Project Title", "activity with contracting agency", "Donor Agency", "Finland, Ministry of Finance", "Primary Sector", "110 - EDUCATION, 112 - BASIC EDUCATION, 120 - HEALTH", "Funding-2014-Actual Disbursements", "50,000", "Totals-Actual Disbursements", "50,000"),
                                new ReportAreaForTests(new AreaOwner(53), "Project Title", "new activity with contracting", "Donor Agency", "Finland", "Primary Sector", ""),
                                new ReportAreaForTests(new AreaOwner(61), "Project Title", "activity-with-unfunded-components", "Donor Agency", "Finland, Ministry of Economy", "Primary Sector", "110 - EDUCATION"),
                                new ReportAreaForTests(new AreaOwner(63), "Project Title", "activity with funded components", "Donor Agency", "UNDP, Water Foundation, World Bank", "Primary Sector", "110 - EDUCATION"),
                                new ReportAreaForTests(new AreaOwner(64), "Project Title", "Unvalidated activity", "Donor Agency", "UNDP", "Primary Sector", "110 - EDUCATION"),
                                new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Donor Agency", "Finland", "Primary Sector", "110 - EDUCATION, 112 - BASIC EDUCATION", "Funding-2015-Actual Disbursements", "321,765", "Totals-Actual Disbursements", "321,765"),
                                new ReportAreaForTests(new AreaOwner(66), "Project Title", "Activity 2 with multiple agreements", "Donor Agency", "Ministry of Finance, UNDP, USAID", "Primary Sector", "110 - EDUCATION"),
                                new ReportAreaForTests(new AreaOwner(67), "Project Title", "third activity with agreements", "Donor Agency", "Finland, Ministry of Finance", "Primary Sector", "110 - EDUCATION"),
                                new ReportAreaForTests(new AreaOwner(68), "Project Title", "activity with incomplete agreement", "Donor Agency", "Finland", "Primary Sector", "110 - EDUCATION"),
                                new ReportAreaForTests(new AreaOwner(69), "Project Title", "Activity with planned disbursements", "Donor Agency", "Norway, USAID", "Primary Sector", "112 - BASIC EDUCATION", "Funding-2014-Planned Disbursements", "300", "Funding-2014-Actual Disbursements", "200", "Funding-2014-Disbursed as Scheduled", "66,67", "Funding-2015-Planned Disbursements", "500", "Funding-2015-Actual Disbursements", "570", "Funding-2015-Disbursed as Scheduled", "100", "Funding-2015-Over Disbursed", "12,28", "Totals-Planned Disbursements", "800", "Totals-Actual Disbursements", "770", "Totals-Disbursed as Scheduled", "96,25"),
                                new ReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms", "Donor Agency", "Finland, Ministry of Finance", "Primary Sector", "110 - EDUCATION, 112 - BASIC EDUCATION"),
                                new ReportAreaForTests(new AreaOwner(71), "Project Title", "activity_with_disaster_response", "Donor Agency", "Finland, Ministry of Finance", "Primary Sector", "110 - EDUCATION, 113 - SECONDARY EDUCATION"),
                                new ReportAreaForTests(new AreaOwner(73), "Project Title", "activity with directed MTEFs", "Donor Agency", "Ministry of Economy", "Primary Sector", "110 - EDUCATION"),
                                new ReportAreaForTests(new AreaOwner(76), "Project Title", "activity with pipeline MTEFs and act. disb", "Donor Agency", "Norway, UNDP", "Primary Sector", "110 - EDUCATION", "Funding-2013-Actual Disbursements", "35,000", "Funding-2014-Actual Disbursements", "75,000", "Totals-Actual Disbursements", "110,000"),
                                new ReportAreaForTests(new AreaOwner(77), "Project Title", "execution rate activity", "Donor Agency", "Ministry of Finance, UNDP", "Primary Sector", "110 - EDUCATION", "Funding-2014-Planned Disbursements", "56,000", "Funding-2014-Actual Disbursements", "55,000", "Funding-2014-Disbursed as Scheduled", "98,21", "Funding-2015-Planned Disbursements", "36,000", "Funding-2015-Actual Disbursements", "35,000", "Funding-2015-Disbursed as Scheduled", "97,22", "Totals-Planned Disbursements", "92,000", "Totals-Actual Disbursements", "90,000", "Totals-Disbursed as Scheduled", "97,83"),
                                new ReportAreaForTests(new AreaOwner(78), "Project Title", "activity with many MTEFs", "Donor Agency", "Finland, Norway, USAID", "Primary Sector", "110 - EDUCATION", "Funding-2015-Actual Disbursements", "80,000", "Totals-Actual Disbursements", "80,000"),
                                new ReportAreaForTests(new AreaOwner(79), "Project Title", "with weird currencies", "Donor Agency", "Finland, Ministry of Finance, Norway", "Primary Sector", "110 - EDUCATION, 112 - BASIC EDUCATION")      ));

        ReportSpecificationImpl spec = buildSpecification("testOverDisbursedAsScheduledFlatReport",
            Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.DONOR_AGENCY, ColumnConstants.PRIMARY_SECTOR),
            Arrays.asList(MeasureConstants.PLANNED_DISBURSEMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS, MeasureConstants.DISBURSED_AS_SCHEDULED, MeasureConstants.OVER_DISBURSED),
            null,
            GroupingCriteria.GROUPING_YEARLY
        );

        runNiTestCase(spec, "en", acts, cor);
    }

    @Test
    public void testOverDisbursedAsScheduledByDonor() {
        NiReportModel cor = new NiReportModel("testOverDisbursedAsScheduledByDonor")
            .withHeaders(Arrays.asList(
                    "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 27))",
                    "(Donor Agency: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Primary Sector: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 3, colSpan: 20));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 23, colSpan: 4))",
                    "(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 4));(2012: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 7, colSpan: 4));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 11, colSpan: 4));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 15, colSpan: 4));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 19, colSpan: 4))",
                    "(Planned Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Disbursed as Scheduled: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Over Disbursed: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Planned Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Disbursed as Scheduled: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Over Disbursed: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Planned Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1));(Disbursed as Scheduled: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1));(Over Disbursed: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 14, colSpan: 1));(Planned Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 15, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 16, colSpan: 1));(Disbursed as Scheduled: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 17, colSpan: 1));(Over Disbursed: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 18, colSpan: 1));(Planned Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 19, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 20, colSpan: 1));(Disbursed as Scheduled: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 21, colSpan: 1));(Over Disbursed: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 22, colSpan: 1));(Planned Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 23, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 24, colSpan: 1));(Disbursed as Scheduled: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 25, colSpan: 1));(Over Disbursed: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 26, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                  .withContents("Donor Agency", "", "Project Title", "", "Primary Sector", "", "Funding-2010-Planned Disbursements", "0", "Funding-2010-Actual Disbursements", "780,311", "Funding-2012-Planned Disbursements", "0", "Funding-2012-Actual Disbursements", "12,000", "Funding-2013-Planned Disbursements", "0", "Funding-2013-Actual Disbursements", "1,266,956", "Funding-2014-Planned Disbursements", "146,300", "Funding-2014-Actual Disbursements", "710,200", "Funding-2014-Disbursed as Scheduled", "100", "Funding-2014-Over Disbursed", "79,4", "Funding-2015-Planned Disbursements", "36,500", "Funding-2015-Actual Disbursements", "437,335", "Funding-2015-Disbursed as Scheduled", "100", "Funding-2015-Over Disbursed", "91,65", "Totals-Planned Disbursements", "182,800", "Totals-Actual Disbursements", "3,206,802", "Totals-Disbursed as Scheduled", "100", "Totals-Over Disbursed", "94,3")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner("Donor Agency", "Finland", 21698))
                    .withContents("Project Title", "", "Primary Sector", "", "Funding-2010-Planned Disbursements", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2012-Planned Disbursements", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Planned Disbursements", "0", "Funding-2013-Actual Disbursements", "575,111", "Funding-2014-Planned Disbursements", "90,000", "Funding-2014-Actual Disbursements", "130,000", "Funding-2014-Disbursed as Scheduled", "100", "Funding-2014-Over Disbursed", "30,77", "Funding-2015-Planned Disbursements", "0", "Funding-2015-Actual Disbursements", "321,765", "Totals-Planned Disbursements", "90,000", "Totals-Actual Disbursements", "1,026,876", "Totals-Disbursed as Scheduled", "100", "Totals-Over Disbursed", "91,24", "Donor Agency", "Finland")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Primary Sector", "110 - EDUCATION", "Funding-2013-Actual Disbursements", "20,000", "Totals-Actual Disbursements", "20,000"),
                      new ReportAreaForTests(new AreaOwner(30), "Project Title", "SSC Project 1", "Primary Sector", "110 - EDUCATION", "Funding-2013-Actual Disbursements", "555,111", "Totals-Actual Disbursements", "555,111"),
                      new ReportAreaForTests(new AreaOwner(50), "Project Title", "activity with capital spending", "Primary Sector", "110 - EDUCATION", "Funding-2014-Planned Disbursements", "90,000", "Funding-2014-Actual Disbursements", "80,000", "Funding-2014-Disbursed as Scheduled", "88,89", "Totals-Planned Disbursements", "90,000", "Totals-Actual Disbursements", "80,000", "Totals-Disbursed as Scheduled", "88,89"),
                      new ReportAreaForTests(new AreaOwner(52), "Project Title", "activity with contracting agency", "Primary Sector", "110 - EDUCATION, 112 - BASIC EDUCATION, 120 - HEALTH", "Funding-2014-Actual Disbursements", "50,000", "Totals-Actual Disbursements", "50,000"),
                      new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Primary Sector", "110 - EDUCATION, 112 - BASIC EDUCATION", "Funding-2015-Actual Disbursements", "321,765", "Totals-Actual Disbursements", "321,765")        ),
                    new ReportAreaForTests(new AreaOwner("Donor Agency", "Ministry of Economy", 21700)).withContents("Project Title", "", "Primary Sector", "", "Funding-2010-Planned Disbursements", "0", "Funding-2010-Actual Disbursements", "143,777", "Funding-2012-Planned Disbursements", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Planned Disbursements", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Planned Disbursements", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Planned Disbursements", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Planned Disbursements", "0", "Totals-Actual Disbursements", "143,777", "Donor Agency", "Ministry of Economy")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner(18), "Project Title", "Test MTEF directed", "Primary Sector", "110 - EDUCATION", "Funding-2010-Actual Disbursements", "143,777", "Totals-Actual Disbursements", "143,777")        ),
                    new ReportAreaForTests(new AreaOwner("Donor Agency", "Ministry of Finance", 21699))
                    .withContents("Project Title", "", "Primary Sector", "", "Funding-2010-Planned Disbursements", "0", "Funding-2010-Actual Disbursements", "60,000", "Funding-2012-Planned Disbursements", "0", "Funding-2012-Actual Disbursements", "12,000", "Funding-2013-Planned Disbursements", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Planned Disbursements", "56,000", "Funding-2014-Actual Disbursements", "55,000", "Funding-2014-Disbursed as Scheduled", "98,21", "Funding-2015-Planned Disbursements", "36,000", "Funding-2015-Actual Disbursements", "35,000", "Funding-2015-Disbursed as Scheduled", "97,22", "Totals-Planned Disbursements", "92,000", "Totals-Actual Disbursements", "162,000", "Totals-Disbursed as Scheduled", "100", "Totals-Over Disbursed", "43,21", "Donor Agency", "Ministry of Finance")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner(26), "Project Title", "date-filters-activity", "Primary Sector", "110 - EDUCATION", "Funding-2010-Actual Disbursements", "60,000", "Funding-2012-Actual Disbursements", "12,000", "Totals-Actual Disbursements", "72,000"),
                      new ReportAreaForTests(new AreaOwner(77), "Project Title", "execution rate activity", "Primary Sector", "110 - EDUCATION", "Funding-2014-Planned Disbursements", "56,000", "Funding-2014-Actual Disbursements", "55,000", "Funding-2014-Disbursed as Scheduled", "98,21", "Funding-2015-Planned Disbursements", "36,000", "Funding-2015-Actual Disbursements", "35,000", "Funding-2015-Disbursed as Scheduled", "97,22", "Totals-Planned Disbursements", "92,000", "Totals-Actual Disbursements", "90,000", "Totals-Disbursed as Scheduled", "97,83")        ),
                    new ReportAreaForTests(new AreaOwner("Donor Agency", "Norway", 21694))
                    .withContents("Project Title", "", "Primary Sector", "", "Funding-2010-Planned Disbursements", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2012-Planned Disbursements", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Planned Disbursements", "0", "Funding-2013-Actual Disbursements", "110,000", "Funding-2014-Planned Disbursements", "300", "Funding-2014-Actual Disbursements", "200", "Funding-2014-Disbursed as Scheduled", "66,67", "Funding-2015-Planned Disbursements", "400", "Funding-2015-Actual Disbursements", "500", "Funding-2015-Disbursed as Scheduled", "100", "Funding-2015-Over Disbursed", "20", "Totals-Planned Disbursements", "700", "Totals-Actual Disbursements", "110,700", "Totals-Disbursed as Scheduled", "100", "Totals-Over Disbursed", "99,37", "Donor Agency", "Norway")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Primary Sector", "110 - EDUCATION", "Funding-2013-Actual Disbursements", "110,000", "Totals-Actual Disbursements", "110,000"),
                      new ReportAreaForTests(new AreaOwner(69), "Project Title", "Activity with planned disbursements", "Primary Sector", "112 - BASIC EDUCATION", "Funding-2014-Planned Disbursements", "300", "Funding-2014-Actual Disbursements", "200", "Funding-2014-Disbursed as Scheduled", "66,67", "Funding-2015-Planned Disbursements", "400", "Funding-2015-Actual Disbursements", "500", "Funding-2015-Disbursed as Scheduled", "100", "Funding-2015-Over Disbursed", "20", "Totals-Planned Disbursements", "700", "Totals-Actual Disbursements", "700", "Totals-Disbursed as Scheduled", "100", "Totals-Over Disbursed", "0")        ),
                    new ReportAreaForTests(new AreaOwner("Donor Agency", "UNDP", 21695)).withContents("Project Title", "", "Primary Sector", "", "Funding-2010-Planned Disbursements", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2012-Planned Disbursements", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Planned Disbursements", "0", "Funding-2013-Actual Disbursements", "35,000", "Funding-2014-Planned Disbursements", "0", "Funding-2014-Actual Disbursements", "75,000", "Funding-2015-Planned Disbursements", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Planned Disbursements", "0", "Totals-Actual Disbursements", "110,000", "Donor Agency", "UNDP")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner(76), "Project Title", "activity with pipeline MTEFs and act. disb", "Primary Sector", "110 - EDUCATION", "Funding-2013-Actual Disbursements", "35,000", "Funding-2014-Actual Disbursements", "75,000", "Totals-Actual Disbursements", "110,000")        ),
                    new ReportAreaForTests(new AreaOwner("Donor Agency", "USAID", 21696))
                    .withContents("Project Title", "", "Primary Sector", "", "Funding-2010-Planned Disbursements", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2012-Planned Disbursements", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Planned Disbursements", "0", "Funding-2013-Actual Disbursements", "415,000", "Funding-2014-Planned Disbursements", "0", "Funding-2014-Actual Disbursements", "450,000", "Funding-2015-Planned Disbursements", "100", "Funding-2015-Actual Disbursements", "80,070", "Funding-2015-Disbursed as Scheduled", "100", "Funding-2015-Over Disbursed", "99,88", "Totals-Planned Disbursements", "100", "Totals-Actual Disbursements", "945,070", "Totals-Disbursed as Scheduled", "100", "Totals-Over Disbursed", "99,99", "Donor Agency", "USAID")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Primary Sector", "110 - EDUCATION", "Funding-2013-Actual Disbursements", "415,000", "Totals-Actual Disbursements", "415,000"),
                      new ReportAreaForTests(new AreaOwner(48), "Project Title", "pledged 2", "Primary Sector", "113 - SECONDARY EDUCATION", "Funding-2014-Actual Disbursements", "450,000", "Totals-Actual Disbursements", "450,000"),
                      new ReportAreaForTests(new AreaOwner(69), "Project Title", "Activity with planned disbursements", "Primary Sector", "112 - BASIC EDUCATION", "Funding-2015-Planned Disbursements", "100", "Funding-2015-Actual Disbursements", "70", "Funding-2015-Disbursed as Scheduled", "70", "Totals-Planned Disbursements", "100", "Totals-Actual Disbursements", "70", "Totals-Disbursed as Scheduled", "70"),
                      new ReportAreaForTests(new AreaOwner(78), "Project Title", "activity with many MTEFs", "Primary Sector", "110 - EDUCATION", "Funding-2015-Actual Disbursements", "80,000", "Totals-Actual Disbursements", "80,000")        ),
                    new ReportAreaForTests(new AreaOwner("Donor Agency", "Water Foundation", 21702)).withContents("Project Title", "", "Primary Sector", "", "Funding-2010-Planned Disbursements", "0", "Funding-2010-Actual Disbursements", "453,213", "Funding-2012-Planned Disbursements", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Planned Disbursements", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Planned Disbursements", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Planned Disbursements", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Planned Disbursements", "0", "Totals-Actual Disbursements", "453,213", "Donor Agency", "Water Foundation")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner(13), "Project Title", "TAC_activity_2", "Primary Sector", "130 - POPULATION POLICIES/PROGRAMMES AND REPRODUCTIVE HEALTH", "Funding-2010-Actual Disbursements", "453,213", "Totals-Actual Disbursements", "453,213")        ),
                    new ReportAreaForTests(new AreaOwner("Donor Agency", "Water Org", 21701)).withContents("Project Title", "", "Primary Sector", "", "Funding-2010-Planned Disbursements", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2012-Planned Disbursements", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Planned Disbursements", "0", "Funding-2013-Actual Disbursements", "131,845", "Funding-2014-Planned Disbursements", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Planned Disbursements", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Planned Disbursements", "0", "Totals-Actual Disbursements", "131,845", "Donor Agency", "Water Org")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner(31), "Project Title", "SSC Project 2", "Primary Sector", "112 - BASIC EDUCATION", "Funding-2013-Actual Disbursements", "131,845", "Totals-Actual Disbursements", "131,845")        ),
                    new ReportAreaForTests(new AreaOwner("Donor Agency", "World Bank", 21697)).withContents("Project Title", "", "Primary Sector", "", "Funding-2010-Planned Disbursements", "0", "Funding-2010-Actual Disbursements", "123,321", "Funding-2012-Planned Disbursements", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Planned Disbursements", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Planned Disbursements", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Planned Disbursements", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Planned Disbursements", "0", "Totals-Actual Disbursements", "123,321", "Donor Agency", "World Bank")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner(12), "Project Title", "TAC_activity_1", "Primary Sector", "112 - BASIC EDUCATION", "Funding-2010-Actual Disbursements", "123,321", "Totals-Actual Disbursements", "123,321")        )      ));
    
        ReportSpecificationImpl spec = buildSpecification("testOverDisbursedAsScheduledByDonor",
            Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.DONOR_AGENCY, ColumnConstants.PRIMARY_SECTOR),
            Arrays.asList(MeasureConstants.PLANNED_DISBURSEMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS, MeasureConstants.DISBURSED_AS_SCHEDULED, MeasureConstants.OVER_DISBURSED),
            Arrays.asList(ColumnConstants.DONOR_AGENCY),
            GroupingCriteria.GROUPING_YEARLY
        );
        
        runNiTestCase(spec, "en", acts, cor);
    }
    
    @Test
    public void testOverDisbursedAsScheduledByDonorByPrimarySector() {
        NiReportModel cor = new NiReportModel("testOverDisbursedAsScheduledByDonorByPrimarySector")
            .withHeaders(Arrays.asList(
                    "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 27))",
                    "(Donor Agency: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Primary Sector: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 3, colSpan: 20));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 23, colSpan: 4))",
                    "(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 4));(2012: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 7, colSpan: 4));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 11, colSpan: 4));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 15, colSpan: 4));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 19, colSpan: 4))",
                    "(Planned Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Disbursed as Scheduled: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Over Disbursed: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Planned Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Disbursed as Scheduled: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Over Disbursed: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Planned Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1));(Disbursed as Scheduled: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1));(Over Disbursed: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 14, colSpan: 1));(Planned Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 15, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 16, colSpan: 1));(Disbursed as Scheduled: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 17, colSpan: 1));(Over Disbursed: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 18, colSpan: 1));(Planned Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 19, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 20, colSpan: 1));(Disbursed as Scheduled: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 21, colSpan: 1));(Over Disbursed: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 22, colSpan: 1));(Planned Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 23, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 24, colSpan: 1));(Disbursed as Scheduled: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 25, colSpan: 1));(Over Disbursed: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 26, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                  .withContents("Donor Agency", "", "Primary Sector", "", "Project Title", "", "Funding-2010-Planned Disbursements", "0", "Funding-2010-Actual Disbursements", "780,311", "Funding-2012-Planned Disbursements", "0", "Funding-2012-Actual Disbursements", "12,000", "Funding-2013-Planned Disbursements", "0", "Funding-2013-Actual Disbursements", "1,266,956", "Funding-2014-Planned Disbursements", "146,300", "Funding-2014-Actual Disbursements", "710,200", "Funding-2014-Disbursed as Scheduled", "100", "Funding-2014-Over Disbursed", "79,4", "Funding-2015-Planned Disbursements", "36,500", "Funding-2015-Actual Disbursements", "437,335", "Funding-2015-Disbursed as Scheduled", "100", "Funding-2015-Over Disbursed", "91,65", "Totals-Planned Disbursements", "182,800", "Totals-Actual Disbursements", "3,206,802", "Totals-Disbursed as Scheduled", "100", "Totals-Over Disbursed", "94,3")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner("Donor Agency", "Finland", 21698))
                    .withContents("Primary Sector", "", "Project Title", "", "Funding-2010-Planned Disbursements", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2012-Planned Disbursements", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Planned Disbursements", "0", "Funding-2013-Actual Disbursements", "575,111", "Funding-2014-Planned Disbursements", "90,000", "Funding-2014-Actual Disbursements", "130,000", "Funding-2014-Disbursed as Scheduled", "100", "Funding-2014-Over Disbursed", "30,77", "Funding-2015-Planned Disbursements", "0", "Funding-2015-Actual Disbursements", "321,765", "Totals-Planned Disbursements", "90,000", "Totals-Actual Disbursements", "1,026,876", "Totals-Disbursed as Scheduled", "100", "Totals-Over Disbursed", "91,24", "Donor Agency", "Finland")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner("Primary Sector", "110 - EDUCATION", 6236))
                      .withContents("Project Title", "", "Funding-2010-Planned Disbursements", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2012-Planned Disbursements", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Planned Disbursements", "0", "Funding-2013-Actual Disbursements", "575,111", "Funding-2014-Planned Disbursements", "90,000", "Funding-2014-Actual Disbursements", "110,000", "Funding-2014-Disbursed as Scheduled", "100", "Funding-2014-Over Disbursed", "18,18", "Funding-2015-Planned Disbursements", "0", "Funding-2015-Actual Disbursements", "160,882,5", "Totals-Planned Disbursements", "90,000", "Totals-Actual Disbursements", "845,993,5", "Totals-Disbursed as Scheduled", "100", "Totals-Over Disbursed", "89,36", "Primary Sector", "110 - EDUCATION")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Funding-2013-Actual Disbursements", "20,000", "Totals-Actual Disbursements", "20,000"),
                        new ReportAreaForTests(new AreaOwner(30), "Project Title", "SSC Project 1", "Funding-2013-Actual Disbursements", "555,111", "Totals-Actual Disbursements", "555,111"),
                        new ReportAreaForTests(new AreaOwner(50), "Project Title", "activity with capital spending", "Funding-2014-Planned Disbursements", "90,000", "Funding-2014-Actual Disbursements", "80,000", "Funding-2014-Disbursed as Scheduled", "88,89", "Totals-Planned Disbursements", "90,000", "Totals-Actual Disbursements", "80,000", "Totals-Disbursed as Scheduled", "88,89"),
                        new ReportAreaForTests(new AreaOwner(52), "Project Title", "activity with contracting agency", "Funding-2014-Actual Disbursements", "30,000", "Totals-Actual Disbursements", "30,000"),
                        new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Funding-2015-Actual Disbursements", "160,882,5", "Totals-Actual Disbursements", "160,882,5")          ),
                      new ReportAreaForTests(new AreaOwner("Primary Sector", "112 - BASIC EDUCATION", 6242))
                      .withContents("Project Title", "", "Funding-2010-Planned Disbursements", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2012-Planned Disbursements", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Planned Disbursements", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Planned Disbursements", "0", "Funding-2014-Actual Disbursements", "5,000", "Funding-2015-Planned Disbursements", "0", "Funding-2015-Actual Disbursements", "160,882,5", "Totals-Planned Disbursements", "0", "Totals-Actual Disbursements", "165,882,5", "Primary Sector", "112 - BASIC EDUCATION")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(52), "Project Title", "activity with contracting agency", "Funding-2014-Actual Disbursements", "5,000", "Totals-Actual Disbursements", "5,000"),
                        new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Funding-2015-Actual Disbursements", "160,882,5", "Totals-Actual Disbursements", "160,882,5")          ),
                      new ReportAreaForTests(new AreaOwner("Primary Sector", "120 - HEALTH", 6252)).withContents("Project Title", "", "Funding-2010-Planned Disbursements", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2012-Planned Disbursements", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Planned Disbursements", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Planned Disbursements", "0", "Funding-2014-Actual Disbursements", "15,000", "Funding-2015-Planned Disbursements", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Planned Disbursements", "0", "Totals-Actual Disbursements", "15,000", "Primary Sector", "120 - HEALTH")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(52), "Project Title", "activity with contracting agency", "Funding-2014-Actual Disbursements", "15,000", "Totals-Actual Disbursements", "15,000")          )        ),
                    new ReportAreaForTests(new AreaOwner("Donor Agency", "Ministry of Economy", 21700)).withContents("Primary Sector", "", "Project Title", "", "Funding-2010-Planned Disbursements", "0", "Funding-2010-Actual Disbursements", "143,777", "Funding-2012-Planned Disbursements", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Planned Disbursements", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Planned Disbursements", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Planned Disbursements", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Planned Disbursements", "0", "Totals-Actual Disbursements", "143,777", "Donor Agency", "Ministry of Economy")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner("Primary Sector", "110 - EDUCATION", 6236)).withContents("Project Title", "", "Funding-2010-Planned Disbursements", "0", "Funding-2010-Actual Disbursements", "143,777", "Funding-2012-Planned Disbursements", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Planned Disbursements", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Planned Disbursements", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Planned Disbursements", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Planned Disbursements", "0", "Totals-Actual Disbursements", "143,777", "Primary Sector", "110 - EDUCATION")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(18), "Project Title", "Test MTEF directed", "Funding-2010-Actual Disbursements", "143,777", "Totals-Actual Disbursements", "143,777")          )        ),
                    new ReportAreaForTests(new AreaOwner("Donor Agency", "Ministry of Finance", 21699)).withContents("Primary Sector", "", "Project Title", "", "Funding-2010-Planned Disbursements", "0", "Funding-2010-Actual Disbursements", "60,000", "Funding-2012-Planned Disbursements", "0", "Funding-2012-Actual Disbursements", "12,000", "Funding-2013-Planned Disbursements", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Planned Disbursements", "56,000", "Funding-2014-Actual Disbursements", "55,000", "Funding-2014-Disbursed as Scheduled", "98,21", "Funding-2015-Planned Disbursements", "36,000", "Funding-2015-Actual Disbursements", "35,000", "Funding-2015-Disbursed as Scheduled", "97,22", "Totals-Planned Disbursements", "92,000", "Totals-Actual Disbursements", "162,000", "Totals-Disbursed as Scheduled", "100", "Totals-Over Disbursed", "43,21", "Donor Agency", "Ministry of Finance")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner("Primary Sector", "110 - EDUCATION", 6236))
                      .withContents("Project Title", "", "Funding-2010-Planned Disbursements", "0", "Funding-2010-Actual Disbursements", "60,000", "Funding-2012-Planned Disbursements", "0", "Funding-2012-Actual Disbursements", "12,000", "Funding-2013-Planned Disbursements", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Planned Disbursements", "56,000", "Funding-2014-Actual Disbursements", "55,000", "Funding-2014-Disbursed as Scheduled", "98,21", "Funding-2015-Planned Disbursements", "36,000", "Funding-2015-Actual Disbursements", "35,000", "Funding-2015-Disbursed as Scheduled", "97,22", "Totals-Planned Disbursements", "92,000", "Totals-Actual Disbursements", "162,000", "Totals-Disbursed as Scheduled", "100", "Totals-Over Disbursed", "43,21", "Primary Sector", "110 - EDUCATION")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(26), "Project Title", "date-filters-activity", "Funding-2010-Actual Disbursements", "60,000", "Funding-2012-Actual Disbursements", "12,000", "Totals-Actual Disbursements", "72,000"),
                        new ReportAreaForTests(new AreaOwner(77), "Project Title", "execution rate activity", "Funding-2014-Planned Disbursements", "56,000", "Funding-2014-Actual Disbursements", "55,000", "Funding-2014-Disbursed as Scheduled", "98,21", "Funding-2015-Planned Disbursements", "36,000", "Funding-2015-Actual Disbursements", "35,000", "Funding-2015-Disbursed as Scheduled", "97,22", "Totals-Planned Disbursements", "92,000", "Totals-Actual Disbursements", "90,000", "Totals-Disbursed as Scheduled", "97,83")          )        ),
                    new ReportAreaForTests(new AreaOwner("Donor Agency", "Norway", 21694))
                    .withContents("Primary Sector", "", "Project Title", "", "Funding-2010-Planned Disbursements", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2012-Planned Disbursements", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Planned Disbursements", "0", "Funding-2013-Actual Disbursements", "110,000", "Funding-2014-Planned Disbursements", "300", "Funding-2014-Actual Disbursements", "200", "Funding-2014-Disbursed as Scheduled", "66,67", "Funding-2015-Planned Disbursements", "400", "Funding-2015-Actual Disbursements", "500", "Funding-2015-Disbursed as Scheduled", "100", "Funding-2015-Over Disbursed", "20", "Totals-Planned Disbursements", "700", "Totals-Actual Disbursements", "110,700", "Totals-Disbursed as Scheduled", "100", "Totals-Over Disbursed", "99,37", "Donor Agency", "Norway")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner("Primary Sector", "110 - EDUCATION", 6236)).withContents("Project Title", "", "Funding-2010-Planned Disbursements", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2012-Planned Disbursements", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Planned Disbursements", "0", "Funding-2013-Actual Disbursements", "110,000", "Funding-2014-Planned Disbursements", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Planned Disbursements", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Planned Disbursements", "0", "Totals-Actual Disbursements", "110,000", "Primary Sector", "110 - EDUCATION")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Funding-2013-Actual Disbursements", "110,000", "Totals-Actual Disbursements", "110,000")          ),
                      new ReportAreaForTests(new AreaOwner("Primary Sector", "112 - BASIC EDUCATION", 6242)).withContents("Project Title", "", "Funding-2010-Planned Disbursements", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2012-Planned Disbursements", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Planned Disbursements", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Planned Disbursements", "300", "Funding-2014-Actual Disbursements", "200", "Funding-2014-Disbursed as Scheduled", "66,67", "Funding-2015-Planned Disbursements", "400", "Funding-2015-Actual Disbursements", "500", "Funding-2015-Disbursed as Scheduled", "100", "Funding-2015-Over Disbursed", "20", "Totals-Planned Disbursements", "700", "Totals-Actual Disbursements", "700", "Totals-Disbursed as Scheduled", "100", "Totals-Over Disbursed", "0", "Primary Sector", "112 - BASIC EDUCATION")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(69), "Project Title", "Activity with planned disbursements", "Funding-2014-Planned Disbursements", "300", "Funding-2014-Actual Disbursements", "200", "Funding-2014-Disbursed as Scheduled", "66,67", "Funding-2015-Planned Disbursements", "400", "Funding-2015-Actual Disbursements", "500", "Funding-2015-Disbursed as Scheduled", "100", "Funding-2015-Over Disbursed", "20", "Totals-Planned Disbursements", "700", "Totals-Actual Disbursements", "700", "Totals-Disbursed as Scheduled", "100", "Totals-Over Disbursed", "0")          )        ),
                    new ReportAreaForTests(new AreaOwner("Donor Agency", "UNDP", 21695)).withContents("Primary Sector", "", "Project Title", "", "Funding-2010-Planned Disbursements", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2012-Planned Disbursements", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Planned Disbursements", "0", "Funding-2013-Actual Disbursements", "35,000", "Funding-2014-Planned Disbursements", "0", "Funding-2014-Actual Disbursements", "75,000", "Funding-2015-Planned Disbursements", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Planned Disbursements", "0", "Totals-Actual Disbursements", "110,000", "Donor Agency", "UNDP")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner("Primary Sector", "110 - EDUCATION", 6236)).withContents("Project Title", "", "Funding-2010-Planned Disbursements", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2012-Planned Disbursements", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Planned Disbursements", "0", "Funding-2013-Actual Disbursements", "35,000", "Funding-2014-Planned Disbursements", "0", "Funding-2014-Actual Disbursements", "75,000", "Funding-2015-Planned Disbursements", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Planned Disbursements", "0", "Totals-Actual Disbursements", "110,000", "Primary Sector", "110 - EDUCATION")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(76), "Project Title", "activity with pipeline MTEFs and act. disb", "Funding-2013-Actual Disbursements", "35,000", "Funding-2014-Actual Disbursements", "75,000", "Totals-Actual Disbursements", "110,000")          )        ),
                    new ReportAreaForTests(new AreaOwner("Donor Agency", "USAID", 21696))
                    .withContents("Primary Sector", "", "Project Title", "", "Funding-2010-Planned Disbursements", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2012-Planned Disbursements", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Planned Disbursements", "0", "Funding-2013-Actual Disbursements", "415,000", "Funding-2014-Planned Disbursements", "0", "Funding-2014-Actual Disbursements", "450,000", "Funding-2015-Planned Disbursements", "100", "Funding-2015-Actual Disbursements", "80,070", "Funding-2015-Disbursed as Scheduled", "100", "Funding-2015-Over Disbursed", "99,88", "Totals-Planned Disbursements", "100", "Totals-Actual Disbursements", "945,070", "Totals-Disbursed as Scheduled", "100", "Totals-Over Disbursed", "99,99", "Donor Agency", "USAID")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner("Primary Sector", "110 - EDUCATION", 6236))
                      .withContents("Project Title", "", "Funding-2010-Planned Disbursements", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2012-Planned Disbursements", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Planned Disbursements", "0", "Funding-2013-Actual Disbursements", "415,000", "Funding-2014-Planned Disbursements", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Planned Disbursements", "0", "Funding-2015-Actual Disbursements", "80,000", "Totals-Planned Disbursements", "0", "Totals-Actual Disbursements", "495,000", "Primary Sector", "110 - EDUCATION")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Funding-2013-Actual Disbursements", "415,000", "Totals-Actual Disbursements", "415,000"),
                        new ReportAreaForTests(new AreaOwner(78), "Project Title", "activity with many MTEFs", "Funding-2015-Actual Disbursements", "80,000", "Totals-Actual Disbursements", "80,000")          ),
                      new ReportAreaForTests(new AreaOwner("Primary Sector", "112 - BASIC EDUCATION", 6242)).withContents("Project Title", "", "Funding-2010-Planned Disbursements", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2012-Planned Disbursements", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Planned Disbursements", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Planned Disbursements", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Planned Disbursements", "100", "Funding-2015-Actual Disbursements", "70", "Funding-2015-Disbursed as Scheduled", "70", "Totals-Planned Disbursements", "100", "Totals-Actual Disbursements", "70", "Totals-Disbursed as Scheduled", "70", "Primary Sector", "112 - BASIC EDUCATION")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(69), "Project Title", "Activity with planned disbursements", "Funding-2015-Planned Disbursements", "100", "Funding-2015-Actual Disbursements", "70", "Funding-2015-Disbursed as Scheduled", "70", "Totals-Planned Disbursements", "100", "Totals-Actual Disbursements", "70", "Totals-Disbursed as Scheduled", "70")          ),
                      new ReportAreaForTests(new AreaOwner("Primary Sector", "113 - SECONDARY EDUCATION", 6246)).withContents("Project Title", "", "Funding-2010-Planned Disbursements", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2012-Planned Disbursements", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Planned Disbursements", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Planned Disbursements", "0", "Funding-2014-Actual Disbursements", "450,000", "Funding-2015-Planned Disbursements", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Planned Disbursements", "0", "Totals-Actual Disbursements", "450,000", "Primary Sector", "113 - SECONDARY EDUCATION")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(48), "Project Title", "pledged 2", "Funding-2014-Actual Disbursements", "450,000", "Totals-Actual Disbursements", "450,000")          )        ),
                    new ReportAreaForTests(new AreaOwner("Donor Agency", "Water Foundation", 21702)).withContents("Primary Sector", "", "Project Title", "", "Funding-2010-Planned Disbursements", "0", "Funding-2010-Actual Disbursements", "453,213", "Funding-2012-Planned Disbursements", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Planned Disbursements", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Planned Disbursements", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Planned Disbursements", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Planned Disbursements", "0", "Totals-Actual Disbursements", "453,213", "Donor Agency", "Water Foundation")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner("Primary Sector", "130 - POPULATION POLICIES/PROGRAMMES AND REPRODUCTIVE HEALTH", 6267)).withContents("Project Title", "", "Funding-2010-Planned Disbursements", "0", "Funding-2010-Actual Disbursements", "453,213", "Funding-2012-Planned Disbursements", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Planned Disbursements", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Planned Disbursements", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Planned Disbursements", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Planned Disbursements", "0", "Totals-Actual Disbursements", "453,213", "Primary Sector", "130 - POPULATION POLICIES/PROGRAMMES AND REPRODUCTIVE HEALTH")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(13), "Project Title", "TAC_activity_2", "Funding-2010-Actual Disbursements", "453,213", "Totals-Actual Disbursements", "453,213")          )        ),
                    new ReportAreaForTests(new AreaOwner("Donor Agency", "Water Org", 21701)).withContents("Primary Sector", "", "Project Title", "", "Funding-2010-Planned Disbursements", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2012-Planned Disbursements", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Planned Disbursements", "0", "Funding-2013-Actual Disbursements", "131,845", "Funding-2014-Planned Disbursements", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Planned Disbursements", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Planned Disbursements", "0", "Totals-Actual Disbursements", "131,845", "Donor Agency", "Water Org")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner("Primary Sector", "112 - BASIC EDUCATION", 6242)).withContents("Project Title", "", "Funding-2010-Planned Disbursements", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2012-Planned Disbursements", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Planned Disbursements", "0", "Funding-2013-Actual Disbursements", "131,845", "Funding-2014-Planned Disbursements", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Planned Disbursements", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Planned Disbursements", "0", "Totals-Actual Disbursements", "131,845", "Primary Sector", "112 - BASIC EDUCATION")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(31), "Project Title", "SSC Project 2", "Funding-2013-Actual Disbursements", "131,845", "Totals-Actual Disbursements", "131,845")          )        ),
                    new ReportAreaForTests(new AreaOwner("Donor Agency", "World Bank", 21697)).withContents("Primary Sector", "", "Project Title", "", "Funding-2010-Planned Disbursements", "0", "Funding-2010-Actual Disbursements", "123,321", "Funding-2012-Planned Disbursements", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Planned Disbursements", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Planned Disbursements", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Planned Disbursements", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Planned Disbursements", "0", "Totals-Actual Disbursements", "123,321", "Donor Agency", "World Bank")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner("Primary Sector", "112 - BASIC EDUCATION", 6242)).withContents("Project Title", "", "Funding-2010-Planned Disbursements", "0", "Funding-2010-Actual Disbursements", "123,321", "Funding-2012-Planned Disbursements", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Planned Disbursements", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Planned Disbursements", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Planned Disbursements", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Planned Disbursements", "0", "Totals-Actual Disbursements", "123,321", "Primary Sector", "112 - BASIC EDUCATION")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(12), "Project Title", "TAC_activity_1", "Funding-2010-Actual Disbursements", "123,321", "Totals-Actual Disbursements", "123,321")          )        )      ));

        
        ReportSpecificationImpl spec = buildSpecification("testOverDisbursedAsScheduledByDonorByPrimarySector",
            Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.DONOR_AGENCY, ColumnConstants.PRIMARY_SECTOR),
            Arrays.asList(MeasureConstants.PLANNED_DISBURSEMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS, MeasureConstants.DISBURSED_AS_SCHEDULED, MeasureConstants.OVER_DISBURSED),
            Arrays.asList(ColumnConstants.DONOR_AGENCY, ColumnConstants.PRIMARY_SECTOR),
            GroupingCriteria.GROUPING_YEARLY
        );
        
        runNiTestCase(spec, "en", acts, cor);
    }
    
    @Test
    public void testEthCalendarDateColumns() {
        NiReportModel cor = new NiReportModel("test_eth_calendar_date_columns")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 0, colSpan: 5))",
                        "(Project Title: (startRow: 1, rowSpan: 2, totalRowSpan: 2, colStart: 0, colSpan: 1));(Activity Created On: (startRow: 1, rowSpan: 2, totalRowSpan: 2, colStart: 1, colSpan: 1));(Activity Updated On: (startRow: 1, rowSpan: 2, totalRowSpan: 2, colStart: 2, colSpan: 1));(Totals: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 2))",
                        "(Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1))"))
                    .withWarnings(Arrays.asList())
                    .withBody(      new ReportAreaForTests(null)
                      .withContents("Project Title", "", "Activity Created On", "", "Activity Updated On", "", "Totals-Actual Commitments", "999,999", "Totals-Actual Disbursements", "0")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(25), "Project Title", "mtef activity 1", "Activity Created On", "29/11/2005", "Activity Updated On", "11/04/2006"),
                        new ReportAreaForTests(new AreaOwner(27), "Project Title", "mtef activity 2", "Activity Created On", "29/11/2005", "Activity Updated On", "11/04/2006"),
                        new ReportAreaForTests(new AreaOwner(28), "Project Title", "ptc activity 1", "Activity Created On", "13/12/2005", "Activity Updated On", "11/04/2006", "Totals-Actual Commitments", "666,777"),
                        new ReportAreaForTests(new AreaOwner(29), "Project Title", "ptc activity 2", "Activity Created On", "13/12/2005", "Activity Updated On", "11/04/2006", "Totals-Actual Commitments", "333,222")      ));
        
        ReportSpecificationImpl spec = buildSpecification("test_eth_calendar_date_columns",
            Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.ACTIVITY_CREATED_ON, ColumnConstants.ACTIVITY_UPDATED_ON),
            Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS),
            null,
            GroupingCriteria.GROUPING_TOTALS_ONLY);
        
        spec.getOrCreateSettings().setCalendar(DbUtil.getAmpFiscalCalendar(172L));
        
        runNiTestCase(spec, "en", Arrays.asList("ptc activity 1", "mtef activity 1", "mtef activity 2", "ptc activity 2"), cor);
    }

    @Test
    public void testSimpleIndicatorReport() {
        NiReportModel cor = new NiReportModel("testSimpleIndicatorReport")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 2, colStart: 0, colSpan: 2))",
                        "(Project Title: (startRow: 1, rowSpan: 1, totalRowSpan: 1, colStart: 0, colSpan: 1));(Indicator Name: (startRow: 1, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                        .withContents("Project Title", "", "Indicator Name", "")
                        .withChildren(
                                new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement"),
                                new ReportAreaForTests(new AreaOwner(95), "Project Title", "activity 1 with indicators", "Indicator Name", "indicator 1, indicator 2"),
                                new ReportAreaForTests(new AreaOwner(96), "Project Title", "activity 2 with indicators", "Indicator Name", "indicator 1, indicator 3")      ));

        ReportSpecificationImpl spec = buildSpecification("testSimpleIndicatorReport",
                Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.INDICATOR_NAME),
                null,
                null,
                GroupingCriteria.GROUPING_TOTALS_ONLY);

        runNiTestCase(spec, "en", indicatorActs, cor);
    }

    @Test
    public void testIndicatorReportWithHierByProjectTitleAndIndicatorName() {
        NiReportModel cor = new NiReportModel("testIndicatorReportWithHierByProjectTitleAndIndicatorName")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 2, colStart: 0, colSpan: 2))",
                        "(Project Title: (startRow: 1, rowSpan: 1, totalRowSpan: 1, colStart: 0, colSpan: 1));(Indicator Name: (startRow: 1, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                        .withContents("Project Title", "", "Indicator Name", "")
                        .withChildren(
                                new ReportAreaForTests(new AreaOwner("Project Title", "activity 1 with agreement", 65)).withContents("Indicator Name", "", "Project Title", "activity 1 with agreement")
                                        .withChildren(
                                                new ReportAreaForTests(new AreaOwner("Indicator Name", "Indicator Name: Undefined", -999999999)).withContents("Indicator Name", "Indicator Name: Undefined")
                                                        .withChildren(
                                                                new ReportAreaForTests(new AreaOwner(65))          )        ),
                                new ReportAreaForTests(new AreaOwner("Project Title", "activity 1 with indicators", 95))
                                        .withContents("Indicator Name", "", "Project Title", "activity 1 with indicators")
                                        .withChildren(
                                                new ReportAreaForTests(new AreaOwner("Indicator Name", "indicator 1", 1)).withContents("Indicator Name", "indicator 1")
                                                        .withChildren(
                                                                new ReportAreaForTests(new AreaOwner(95))          ),
                                                new ReportAreaForTests(new AreaOwner("Indicator Name", "indicator 2", 2)).withContents("Indicator Name", "indicator 2")
                                                        .withChildren(
                                                                new ReportAreaForTests(new AreaOwner(95))          )        ),
                                new ReportAreaForTests(new AreaOwner("Project Title", "activity 2 with indicators", 96))
                                        .withContents("Indicator Name", "", "Project Title", "activity 2 with indicators")
                                        .withChildren(
                                                new ReportAreaForTests(new AreaOwner("Indicator Name", "indicator 1", 1)).withContents("Indicator Name", "indicator 1")
                                                        .withChildren(
                                                                new ReportAreaForTests(new AreaOwner(96))          ),
                                                new ReportAreaForTests(new AreaOwner("Indicator Name", "indicator 3", 3)).withContents("Indicator Name", "indicator 3")
                                                        .withChildren(
                                                                new ReportAreaForTests(new AreaOwner(96))          )        )      ));

        ReportSpecificationImpl spec = buildSpecification("testIndicatorReportWithHierByProjectTitleAndIndicatorName",
                Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.INDICATOR_NAME),
                null,
                Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.INDICATOR_NAME),
                GroupingCriteria.GROUPING_TOTALS_ONLY);

        runNiTestCase(spec, "en", indicatorActs, cor);
    }

    @Test
    public void testIndicatorReportWithHierByProjectIndicatorName() {
        NiReportModel cor = new NiReportModel("testIndicatorReportWithHierByProjectIndicatorName")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 2, colStart: 0, colSpan: 2))",
                        "(Indicator Name: (startRow: 1, rowSpan: 1, totalRowSpan: 1, colStart: 0, colSpan: 1));(Project Title: (startRow: 1, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                        .withContents("Indicator Name", "", "Project Title", "")
                        .withChildren(
                                new ReportAreaForTests(new AreaOwner("Indicator Name", "indicator 1", 1))
                                        .withContents("Project Title", "", "Indicator Name", "indicator 1")
                                        .withChildren(
                                                new ReportAreaForTests(new AreaOwner(95), "Project Title", "activity 1 with indicators"),
                                                new ReportAreaForTests(new AreaOwner(96), "Project Title", "activity 2 with indicators")        ),
                                new ReportAreaForTests(new AreaOwner("Indicator Name", "indicator 2", 2)).withContents("Project Title", "", "Indicator Name", "indicator 2")
                                        .withChildren(
                                                new ReportAreaForTests(new AreaOwner(95), "Project Title", "activity 1 with indicators")        ),
                                new ReportAreaForTests(new AreaOwner("Indicator Name", "indicator 3", 3)).withContents("Project Title", "", "Indicator Name", "indicator 3")
                                        .withChildren(
                                                new ReportAreaForTests(new AreaOwner(96), "Project Title", "activity 2 with indicators")        ),
                                new ReportAreaForTests(new AreaOwner("Indicator Name", "Indicator Name: Undefined", -999999999)).withContents("Project Title", "", "Indicator Name", "Indicator Name: Undefined")
                                        .withChildren(
                                                new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement")        )      ));

        ReportSpecificationImpl spec = buildSpecification("testIndicatorReportWithHierByProjectIndicatorName",
                Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.INDICATOR_NAME),
                null,
                Arrays.asList(ColumnConstants.INDICATOR_NAME),
                GroupingCriteria.GROUPING_TOTALS_ONLY);

        runNiTestCase(spec, "en", indicatorActs, cor);
    }

    @Test
    public void testIndicatorValuesInReport() {
        NiReportModel cor = new NiReportModel("testIndicatorValuesInReport")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 2, colStart: 0, colSpan: 4))",
                        "(Indicator Name: (startRow: 1, rowSpan: 1, totalRowSpan: 1, colStart: 0, colSpan: 1));(Project Title: (startRow: 1, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1));(Indicator Base Value: (startRow: 1, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Indicator Target Value: (startRow: 1, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                        .withContents("Indicator Name", "", "Project Title", "", "Indicator Base Value", "", "Indicator Target Value", "")
                        .withChildren(
                                new ReportAreaForTests(new AreaOwner("Indicator Name", "indicator 1", 1))
                                        .withContents("Project Title", "", "Indicator Base Value", "", "Indicator Target Value", "", "Indicator Name", "indicator 1")
                                        .withChildren(
                                                new ReportAreaForTests(new AreaOwner("Project Title", "activity 1 with indicators", 95)).withContents("Indicator Base Value", "", "Indicator Target Value", "", "Project Title", "activity 1 with indicators")
                                                        .withChildren(
                                                                new ReportAreaForTests(new AreaOwner(95), "Indicator Base Value", "10", "Indicator Target Value", "50")          ),
                                                new ReportAreaForTests(new AreaOwner("Project Title", "activity 2 with indicators", 96)).withContents("Indicator Base Value", "", "Indicator Target Value", "", "Project Title", "activity 2 with indicators")
                                                        .withChildren(
                                                                new ReportAreaForTests(new AreaOwner(96), "Indicator Base Value", "5", "Indicator Target Value", "33")          )        ),
                                new ReportAreaForTests(new AreaOwner("Indicator Name", "indicator 2", 2)).withContents("Project Title", "", "Indicator Base Value", "", "Indicator Target Value", "", "Indicator Name", "indicator 2")
                                        .withChildren(
                                                new ReportAreaForTests(new AreaOwner("Project Title", "activity 1 with indicators", 95)).withContents("Indicator Base Value", "", "Indicator Target Value", "", "Project Title", "activity 1 with indicators")
                                                        .withChildren(
                                                                new ReportAreaForTests(new AreaOwner(95), "Indicator Base Value", "0", "Indicator Target Value", "100")          )        ),
                                new ReportAreaForTests(new AreaOwner("Indicator Name", "indicator 3", 3)).withContents("Project Title", "", "Indicator Base Value", "", "Indicator Target Value", "", "Indicator Name", "indicator 3")
                                        .withChildren(
                                                new ReportAreaForTests(new AreaOwner("Project Title", "activity 2 with indicators", 96)).withContents("Indicator Base Value", "", "Indicator Target Value", "", "Project Title", "activity 2 with indicators")
                                                        .withChildren(
                                                                new ReportAreaForTests(new AreaOwner(96), "Indicator Base Value", "1", "Indicator Target Value", "3")          )        ),
                                new ReportAreaForTests(new AreaOwner("Indicator Name", "Indicator Name: Undefined", -999999999)).withContents("Project Title", "", "Indicator Base Value", "", "Indicator Target Value", "", "Indicator Name", "Indicator Name: Undefined")
                                        .withChildren(
                                                new ReportAreaForTests(new AreaOwner("Project Title", "activity 1 with agreement", 65)).withContents("Indicator Base Value", "", "Indicator Target Value", "", "Project Title", "activity 1 with agreement")
                                                        .withChildren(
                                                                new ReportAreaForTests(new AreaOwner(65))          )        )      ));

        ReportSpecificationImpl spec = buildSpecification("testIndicatorValuesInReport",
                Arrays.asList(ColumnConstants.INDICATOR_NAME, ColumnConstants.PROJECT_TITLE, ColumnConstants.INDICATOR_BASE_VALUE, ColumnConstants.INDICATOR_TARGET_VALUE),
                null,
                Arrays.asList(ColumnConstants.INDICATOR_NAME, ColumnConstants.PROJECT_TITLE),
                GroupingCriteria.GROUPING_TOTALS_ONLY);

        runNiTestCase(spec, "en", indicatorActs, cor);
    }

    @Test
    public void testIndicatorsOnlyFlat() {
        NiReportModel cor = new NiReportModel("testIndicatorsOnlyFlat")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 2, colStart: 0, colSpan: 3))",
                        "(Indicator Name: (startRow: 1, rowSpan: 1, totalRowSpan: 1, colStart: 0, colSpan: 1));(Indicator Base Value: (startRow: 1, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1));(Indicator Target Value: (startRow: 1, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                        .withContents("Indicator Name", "", "Indicator Base Value", "", "Indicator Target Value", "")
                        .withChildren(
                                new ReportAreaForTests(new AreaOwner(95), "Indicator Name", "indicator 1, indicator 2", "Indicator Base Value", "10, 0", "Indicator Target Value", "50, 100"),
                                new ReportAreaForTests(new AreaOwner(96), "Indicator Name", "indicator 1, indicator 3", "Indicator Base Value", "5, 1", "Indicator Target Value", "33, 3")      ));

        ReportSpecificationImpl spec = buildSpecification("testIndicatorsOnlyFlat",
                Arrays.asList(ColumnConstants.INDICATOR_NAME, ColumnConstants.INDICATOR_BASE_VALUE, ColumnConstants.INDICATOR_TARGET_VALUE),
                null,
                null,
                GroupingCriteria.GROUPING_TOTALS_ONLY);

        runNiTestCase(spec, "en", indicatorActs, cor);
    }

    @Test
    public void testIndicatorsOnlyOneHier() {
        NiReportModel cor = new NiReportModel("testIndicatorsOnlyOneHier")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 2, colStart: 0, colSpan: 3))",
                        "(Indicator Name: (startRow: 1, rowSpan: 1, totalRowSpan: 1, colStart: 0, colSpan: 1));(Indicator Base Value: (startRow: 1, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1));(Indicator Target Value: (startRow: 1, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                        .withContents("Indicator Name", "", "Indicator Base Value", "", "Indicator Target Value", "")
                        .withChildren(
                                new ReportAreaForTests(new AreaOwner("Indicator Name", "indicator 1", 1))
                                        .withContents("Indicator Base Value", "", "Indicator Target Value", "", "Indicator Name", "indicator 1")
                                        .withChildren(
                                                new ReportAreaForTests(new AreaOwner(95), "Indicator Base Value", "10", "Indicator Target Value", "50"),
                                                new ReportAreaForTests(new AreaOwner(96), "Indicator Base Value", "5", "Indicator Target Value", "33")        ),
                                new ReportAreaForTests(new AreaOwner("Indicator Name", "indicator 2", 2)).withContents("Indicator Base Value", "", "Indicator Target Value", "", "Indicator Name", "indicator 2")
                                        .withChildren(
                                                new ReportAreaForTests(new AreaOwner(95), "Indicator Base Value", "0", "Indicator Target Value", "100")        ),
                                new ReportAreaForTests(new AreaOwner("Indicator Name", "indicator 3", 3)).withContents("Indicator Base Value", "", "Indicator Target Value", "", "Indicator Name", "indicator 3")
                                        .withChildren(
                                                new ReportAreaForTests(new AreaOwner(96), "Indicator Base Value", "1", "Indicator Target Value", "3")        )      ));

        ReportSpecificationImpl spec = buildSpecification("testIndicatorsOnlyOneHier",
                Arrays.asList(ColumnConstants.INDICATOR_NAME, ColumnConstants.INDICATOR_BASE_VALUE, ColumnConstants.INDICATOR_TARGET_VALUE),
                null,
                Arrays.asList(ColumnConstants.INDICATOR_NAME),
                GroupingCriteria.GROUPING_TOTALS_ONLY);

        runNiTestCase(spec, "en", indicatorActs, cor);
    }

    @Test
    public void testIndicatorsAllColumns() {
        NiReportModel cor = new NiReportModel("testIndicatorsAllColumns")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 2, colStart: 0, colSpan: 20))",
                        "(Indicator Name: (startRow: 1, rowSpan: 1, totalRowSpan: 1, colStart: 0, colSpan: 1));(Indicator Code: (startRow: 1, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1));(Indicator Sector: (startRow: 1, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Indicator Description: (startRow: 1, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Indicator Type: (startRow: 1, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Indicator Creation Date: (startRow: 1, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Logframe Category: (startRow: 1, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Risk: (startRow: 1, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Indicator Base Value: (startRow: 1, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Indicator Base Date: (startRow: 1, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Indicator Base Comment: (startRow: 1, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Indicator Target Value: (startRow: 1, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1));(Indicator Target Date: (startRow: 1, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1));(Indicator Target Comment: (startRow: 1, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1));(Indicator Revised Target Value: (startRow: 1, rowSpan: 1, totalRowSpan: 1, colStart: 14, colSpan: 1));(Indicator Revised Target Date: (startRow: 1, rowSpan: 1, totalRowSpan: 1, colStart: 15, colSpan: 1));(Indicator Revised Target Comment: (startRow: 1, rowSpan: 1, totalRowSpan: 1, colStart: 16, colSpan: 1));(Indicator Current Value: (startRow: 1, rowSpan: 1, totalRowSpan: 1, colStart: 17, colSpan: 1));(Indicator Current Date: (startRow: 1, rowSpan: 1, totalRowSpan: 1, colStart: 18, colSpan: 1));(Indicator Current Comment: (startRow: 1, rowSpan: 1, totalRowSpan: 1, colStart: 19, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                        .withContents("Indicator Name", "", "Indicator Code", "", "Indicator Sector", "", "Indicator Description", "", "Indicator Type", "", "Indicator Creation Date", "", "Logframe Category", "", "Risk", "", "Indicator Base Value", "", "Indicator Base Date", "", "Indicator Base Comment", "", "Indicator Target Value", "", "Indicator Target Date", "", "Indicator Target Comment", "", "Indicator Revised Target Value", "", "Indicator Revised Target Date", "", "Indicator Revised Target Comment", "", "Indicator Current Value", "", "Indicator Current Date", "", "Indicator Current Comment", "")
                        .withChildren(
                                new ReportAreaForTests(new AreaOwner("Indicator Name", "indicator 1", 1))
                                        .withContents("Indicator Code", "", "Indicator Sector", "", "Indicator Description", "", "Indicator Type", "", "Indicator Creation Date", "", "Logframe Category", "", "Risk", "", "Indicator Base Value", "", "Indicator Base Date", "", "Indicator Base Comment", "", "Indicator Target Value", "", "Indicator Target Date", "", "Indicator Target Comment", "", "Indicator Revised Target Value", "", "Indicator Revised Target Date", "", "Indicator Revised Target Comment", "", "Indicator Current Value", "", "Indicator Current Date", "", "Indicator Current Comment", "", "Indicator Name", "indicator 1")
                                        .withChildren(
                                                new ReportAreaForTests(new AreaOwner(95), "Indicator Code", "IND-1", "Indicator Sector", "110 - EDUCATION", "Indicator Description", "ind1desc", "Indicator Type", "", "Indicator Creation Date", "10/02/2017", "Logframe Category", "Outcome", "Risk", "High", "Indicator Base Value", "10", "Indicator Base Date", "01/02/2017", "Indicator Base Comment", "bc1-1", "Indicator Target Value", "50", "Indicator Target Date", "11/02/2017", "Indicator Target Comment", "tc1-1", "Indicator Revised Target Value", "", "Indicator Revised Target Date", "", "Indicator Revised Target Comment", "", "Indicator Current Value", "15", "Indicator Current Date", "09/02/2017", "Indicator Current Comment", "cc1-1"),
                                                new ReportAreaForTests(new AreaOwner(96), "Indicator Code", "IND-1", "Indicator Sector", "110 - EDUCATION", "Indicator Description", "ind1desc", "Indicator Type", "", "Indicator Creation Date", "10/02/2017", "Logframe Category", "Activity", "Risk", "Critical", "Indicator Base Value", "5", "Indicator Base Date", "02/02/2017", "Indicator Base Comment", "bc2-1", "Indicator Target Value", "33", "Indicator Target Date", "07/02/2017", "Indicator Target Comment", "tc2-1", "Indicator Revised Target Value", "", "Indicator Revised Target Date", "", "Indicator Revised Target Comment", "", "Indicator Current Value", "8", "Indicator Current Date", "20/02/2017", "Indicator Current Comment", "cc2-1")        ),
                                new ReportAreaForTests(new AreaOwner("Indicator Name", "indicator 2", 2)).withContents("Indicator Code", "", "Indicator Sector", "", "Indicator Description", "", "Indicator Type", "", "Indicator Creation Date", "", "Logframe Category", "", "Risk", "", "Indicator Base Value", "", "Indicator Base Date", "", "Indicator Base Comment", "", "Indicator Target Value", "", "Indicator Target Date", "", "Indicator Target Comment", "", "Indicator Revised Target Value", "", "Indicator Revised Target Date", "", "Indicator Revised Target Comment", "", "Indicator Current Value", "", "Indicator Current Date", "", "Indicator Current Comment", "", "Indicator Name", "indicator 2")
                                        .withChildren(
                                                new ReportAreaForTests(new AreaOwner(95), "Indicator Code", "IND-2", "Indicator Sector", "120 - HEALTH", "Indicator Description", "ind2desc", "Indicator Type", "", "Indicator Creation Date", "13/02/2017", "Logframe Category", "Output", "Risk", "Very Low", "Indicator Base Value", "0", "Indicator Base Date", "01/02/2017", "Indicator Base Comment", "bc1-2", "Indicator Target Value", "100", "Indicator Target Date", "28/02/2017", "Indicator Target Comment", "tc1-2", "Indicator Revised Target Value", "120", "Indicator Revised Target Date", "", "Indicator Revised Target Comment", "rc1-2", "Indicator Current Value", "0", "Indicator Current Date", "10/02/2017", "Indicator Current Comment", "cc1-2")        ),
                                new ReportAreaForTests(new AreaOwner("Indicator Name", "indicator 3", 3)).withContents("Indicator Code", "", "Indicator Sector", "", "Indicator Description", "", "Indicator Type", "", "Indicator Creation Date", "", "Logframe Category", "", "Risk", "", "Indicator Base Value", "", "Indicator Base Date", "", "Indicator Base Comment", "", "Indicator Target Value", "", "Indicator Target Date", "", "Indicator Target Comment", "", "Indicator Revised Target Value", "", "Indicator Revised Target Date", "", "Indicator Revised Target Comment", "", "Indicator Current Value", "", "Indicator Current Date", "", "Indicator Current Comment", "", "Indicator Name", "indicator 3")
                                        .withChildren(
                                                new ReportAreaForTests(new AreaOwner(96), "Indicator Code", "IND-3", "Indicator Sector", "140 - WATER AND SANITATION, 160 - OTHER SOCIAL INFRASTRUCTURE AND SERVICES", "Indicator Description", "ind3desc", "Indicator Type", "", "Indicator Creation Date", "10/02/2017", "Logframe Category", "None", "Risk", "Very High", "Indicator Base Value", "1", "Indicator Base Date", "04/02/2017", "Indicator Base Comment", "bc2-3", "Indicator Target Value", "3", "Indicator Target Date", "06/02/2017", "Indicator Target Comment", "tc2-3", "Indicator Revised Target Value", "", "Indicator Revised Target Date", "", "Indicator Revised Target Comment", "", "Indicator Current Value", "2", "Indicator Current Date", "05/02/2017", "Indicator Current Comment", "cc2-3")        )      ));

        ReportSpecificationImpl spec = buildSpecification("testIndicatorsAllColumns",
                Arrays.asList(ColumnConstants.INDICATOR_NAME,
                        ColumnConstants.INDICATOR_CODE,
                        ColumnConstants.INDICATOR_SECTOR,
                        ColumnConstants.INDICATOR_DESCRIPTION,
                        ColumnConstants.INDICATOR_TYPE,
                        ColumnConstants.INDICATOR_CREATION_DATE,
                        ColumnConstants.INDICATOR_LOGFRAME_CATEGORY,
                        ColumnConstants.INDICATOR_RISK,
                        ColumnConstants.INDICATOR_BASE_VALUE,
                        ColumnConstants.INDICATOR_BASE_DATE,
                        ColumnConstants.INDICATOR_BASE_COMMENT,
                        ColumnConstants.INDICATOR_TARGET_VALUE,
                        ColumnConstants.INDICATOR_TARGET_DATE,
                        ColumnConstants.INDICATOR_TARGET_COMMENT,
                        ColumnConstants.INDICATOR_REVISED_TARGET_VALUE,
                        ColumnConstants.INDICATOR_REVISED_TARGET_DATE,
                        ColumnConstants.INDICATOR_REVISED_TARGET_COMMENT,
                        ColumnConstants.INDICATOR_ACTUAL_VALUE,
                        ColumnConstants.INDICATOR_ACTUAL_DATE,
                        ColumnConstants.INDICATOR_ACTUAL_COMMENT),
                null,
                Arrays.asList(ColumnConstants.INDICATOR_NAME),
                GroupingCriteria.GROUPING_TOTALS_ONLY);

        runNiTestCase(spec, "en", indicatorActs, cor);
    }

    @Test
    public void testIndicatorsHierarchyBySector() {
        NiReportModel cor = new NiReportModel("testIndicatorsHierarchyBySector")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 2, colStart: 0, colSpan: 3))",
                        "(Indicator Sector: (startRow: 1, rowSpan: 1, totalRowSpan: 1, colStart: 0, colSpan: 1));(Indicator Name: (startRow: 1, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1));(Indicator Base Value: (startRow: 1, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                        .withContents("Indicator Sector", "", "Indicator Name", "", "Indicator Base Value", "")
                        .withChildren(
                                new ReportAreaForTests(new AreaOwner("Indicator Sector", "110 - EDUCATION", 6236))
                                        .withContents("Indicator Name", "", "Indicator Base Value", "", "Indicator Sector", "110 - EDUCATION")
                                        .withChildren(
                                                new ReportAreaForTests(new AreaOwner(95), "Indicator Name", "indicator 1", "Indicator Base Value", "10"),
                                                new ReportAreaForTests(new AreaOwner(96), "Indicator Name", "indicator 1", "Indicator Base Value", "5")        ),
                                new ReportAreaForTests(new AreaOwner("Indicator Sector", "120 - HEALTH", 6252)).withContents("Indicator Name", "", "Indicator Base Value", "", "Indicator Sector", "120 - HEALTH")
                                        .withChildren(
                                                new ReportAreaForTests(new AreaOwner(95), "Indicator Name", "indicator 2", "Indicator Base Value", "0")        ),
                                new ReportAreaForTests(new AreaOwner("Indicator Sector", "140 - WATER AND SANITATION", 6273)).withContents("Indicator Name", "", "Indicator Base Value", "", "Indicator Sector", "140 - WATER AND SANITATION")
                                        .withChildren(
                                                new ReportAreaForTests(new AreaOwner(96), "Indicator Name", "indicator 3", "Indicator Base Value", "1")        ),
                                new ReportAreaForTests(new AreaOwner("Indicator Sector", "160 - OTHER SOCIAL INFRASTRUCTURE AND SERVICES", 6305)).withContents("Indicator Name", "", "Indicator Base Value", "", "Indicator Sector", "160 - OTHER SOCIAL INFRASTRUCTURE AND SERVICES")
                                        .withChildren(
                                                new ReportAreaForTests(new AreaOwner(96), "Indicator Name", "indicator 3", "Indicator Base Value", "1")        )      ));

        ReportSpecificationImpl spec = buildSpecification("testIndicatorsHierarchyBySector",
                Arrays.asList(ColumnConstants.INDICATOR_SECTOR, ColumnConstants.INDICATOR_NAME, ColumnConstants.INDICATOR_BASE_VALUE),
                null,
                Arrays.asList(ColumnConstants.INDICATOR_SECTOR),
                GroupingCriteria.GROUPING_TOTALS_ONLY);

        runNiTestCase(spec, "en", indicatorActs, cor);
    }

    @Test
    public void testIndicatorsHierarchyByLogframe() {
        NiReportModel cor = new NiReportModel("testIndicatorsHierarchyByLogframe")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 2, colStart: 0, colSpan: 3))",
                        "(Logframe Category: (startRow: 1, rowSpan: 1, totalRowSpan: 1, colStart: 0, colSpan: 1));(Indicator Name: (startRow: 1, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1));(Indicator Base Value: (startRow: 1, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                        .withContents("Logframe Category", "", "Indicator Name", "", "Indicator Base Value", "")
                        .withChildren(
                                new ReportAreaForTests(new AreaOwner("Logframe Category", "Activity", 45)).withContents("Indicator Name", "", "Indicator Base Value", "", "Logframe Category", "Activity")
                                        .withChildren(
                                                new ReportAreaForTests(new AreaOwner(96), "Indicator Name", "indicator 1", "Indicator Base Value", "5")        ),
                                new ReportAreaForTests(new AreaOwner("Logframe Category", "None", 7)).withContents("Indicator Name", "", "Indicator Base Value", "", "Logframe Category", "None")
                                        .withChildren(
                                                new ReportAreaForTests(new AreaOwner(96), "Indicator Name", "indicator 3", "Indicator Base Value", "1")        ),
                                new ReportAreaForTests(new AreaOwner("Logframe Category", "Outcome", 8)).withContents("Indicator Name", "", "Indicator Base Value", "", "Logframe Category", "Outcome")
                                        .withChildren(
                                                new ReportAreaForTests(new AreaOwner(95), "Indicator Name", "indicator 1", "Indicator Base Value", "10")        ),
                                new ReportAreaForTests(new AreaOwner("Logframe Category", "Output", 9)).withContents("Indicator Name", "", "Indicator Base Value", "", "Logframe Category", "Output")
                                        .withChildren(
                                                new ReportAreaForTests(new AreaOwner(95), "Indicator Name", "indicator 2", "Indicator Base Value", "0")        )      ));

        ReportSpecificationImpl spec = buildSpecification("testIndicatorsHierarchyByLogframe",
                Arrays.asList(ColumnConstants.INDICATOR_LOGFRAME_CATEGORY, ColumnConstants.INDICATOR_NAME, ColumnConstants.INDICATOR_BASE_VALUE),
                null,
                Arrays.asList(ColumnConstants.INDICATOR_LOGFRAME_CATEGORY),
                GroupingCriteria.GROUPING_TOTALS_ONLY);

        runNiTestCase(spec, "en", indicatorActs, cor);
    }

    @Test
    public void testIndicatorsHierarchyByRisk() {
        NiReportModel cor = new NiReportModel("testIndicatorsHierarchyByRisk")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 2, colStart: 0, colSpan: 3))",
                        "(Risk: (startRow: 1, rowSpan: 1, totalRowSpan: 1, colStart: 0, colSpan: 1));(Indicator Name: (startRow: 1, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1));(Indicator Base Value: (startRow: 1, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                        .withContents("Risk", "", "Indicator Name", "", "Indicator Base Value", "")
                        .withChildren(
                                new ReportAreaForTests(new AreaOwner("Risk", "Critical", 6)).withContents("Indicator Name", "", "Indicator Base Value", "", "Risk", "Critical")
                                        .withChildren(
                                                new ReportAreaForTests(new AreaOwner(96), "Indicator Name", "indicator 1", "Indicator Base Value", "5")        ),
                                new ReportAreaForTests(new AreaOwner("Risk", "High", 4)).withContents("Indicator Name", "", "Indicator Base Value", "", "Risk", "High")
                                        .withChildren(
                                                new ReportAreaForTests(new AreaOwner(95), "Indicator Name", "indicator 1", "Indicator Base Value", "10")        ),
                                new ReportAreaForTests(new AreaOwner("Risk", "Very High", 5)).withContents("Indicator Name", "", "Indicator Base Value", "", "Risk", "Very High")
                                        .withChildren(
                                                new ReportAreaForTests(new AreaOwner(96), "Indicator Name", "indicator 3", "Indicator Base Value", "1")        ),
                                new ReportAreaForTests(new AreaOwner("Risk", "Very Low", 1)).withContents("Indicator Name", "", "Indicator Base Value", "", "Risk", "Very Low")
                                        .withChildren(
                                                new ReportAreaForTests(new AreaOwner(95), "Indicator Name", "indicator 2", "Indicator Base Value", "0")        )      ));

        ReportSpecificationImpl spec = buildSpecification("testIndicatorsHierarchyByRisk",
                Arrays.asList(ColumnConstants.INDICATOR_RISK, ColumnConstants.INDICATOR_NAME, ColumnConstants.INDICATOR_BASE_VALUE),
                null,
                Arrays.asList(ColumnConstants.INDICATOR_RISK),
                GroupingCriteria.GROUPING_TOTALS_ONLY);

        runNiTestCase(spec, "en", indicatorActs, cor);
    }

    @Test
    public void testIndicatorsHierarchyByType() {
        NiReportModel cor = new NiReportModel("testIndicatorsHierarchyByType")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 2, colStart: 0, colSpan: 3))",
                        "(Indicator Type: (startRow: 1, rowSpan: 1, totalRowSpan: 1, colStart: 0, colSpan: 1));(Indicator Name: (startRow: 1, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1));(Indicator Base Value: (startRow: 1, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null).withContents("Indicator Type", "", "Indicator Name", "", "Indicator Base Value", "")
                        .withChildren(
                                new ReportAreaForTests(new AreaOwner("Indicator Type", "", 3))
                                        .withContents("Indicator Name", "", "Indicator Base Value", "", "Indicator Type", "")
                                        .withChildren(
                                                new ReportAreaForTests(new AreaOwner(95), "Indicator Name", "indicator 1, indicator 2", "Indicator Base Value", "10, 0"),
                                                new ReportAreaForTests(new AreaOwner(96), "Indicator Name", "indicator 1, indicator 3", "Indicator Base Value", "5, 1")        )      ));

        ReportSpecificationImpl spec = buildSpecification("testIndicatorsHierarchyByType",
                Arrays.asList(ColumnConstants.INDICATOR_TYPE, ColumnConstants.INDICATOR_NAME, ColumnConstants.INDICATOR_BASE_VALUE),
                null,
                Arrays.asList(ColumnConstants.INDICATOR_TYPE),
                GroupingCriteria.GROUPING_TOTALS_ONLY);

        runNiTestCase(spec, "en", indicatorActs, cor);
    }
}
