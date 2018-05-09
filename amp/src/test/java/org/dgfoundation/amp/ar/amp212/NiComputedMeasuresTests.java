package org.dgfoundation.amp.ar.amp212;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.mondrian.ReportAreaForTests;
import org.dgfoundation.amp.mondrian.ReportingTestCase;
import org.dgfoundation.amp.newreports.AmpReportFilters;
import org.dgfoundation.amp.newreports.AreaOwner;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.nireports.amp.AmpReportsScratchpad;
import org.dgfoundation.amp.nireports.output.NiReportExecutor;
import org.dgfoundation.amp.nireports.testcases.NiReportModel;
import org.junit.After;
import org.junit.Test;

/**
 * 
 * testcases for the fetching states of AMP + the AMP schema
 * 
 * @author Constantin Dolghier
 *
 */
public class NiComputedMeasuresTests extends ReportingTestCase {
    
    final List<String> acts = Arrays.asList(
            "expenditure class", 
            "Eth Water",
            "activity with directed MTEFs"
        );
    
    @Override
    protected NiReportExecutor getNiExecutor(List<String> activityNames) {
        AmpReportsScratchpad.forcedNowDate = LocalDate.of(2016, 5, 3);
        return getDbExecutor(activityNames);
    }
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
    
    @Test
    public void testNonTimebound() {
        NiReportModel cor = new NiReportModel("Non-timebound computed measures")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 0, colSpan: 8))",
                        "(Project Title: (startRow: 1, rowSpan: 2, totalRowSpan: 2, colStart: 0, colSpan: 1));(Proposed Project Amount: (startRow: 1, rowSpan: 2, totalRowSpan: 2, colStart: 1, colSpan: 1));(Totals: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 6))",
                        "(Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Cumulative Commitment: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Cumulative Disbursement: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Uncommitted Cumulative Balance: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Undisbursed Cumulative Balance: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1))"))
                    .withWarnings(Arrays.asList())
                    .withBody(      new ReportAreaForTests(null)
                      .withContents("Project Title", "", "Proposed Project Amount", "4,630,902,72", "Totals-Actual Commitments", "752,245", "Totals-Actual Disbursements", "321,765", "Totals-Cumulative Commitment", "752,245", "Totals-Cumulative Disbursement", "321,765", "Totals-Uncommitted Cumulative Balance", "3,878,657,72", "Totals-Undisbursed Cumulative Balance", "430,480")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(15), "Project Title", "Proposed Project Cost 1 - USD", "Proposed Project Amount", "1,000,000", "Totals-Uncommitted Cumulative Balance", "1,000,000"),
                        new ReportAreaForTests(new AreaOwner(17), "Project Title", "Proposed Project Cost 2 - EUR", "Proposed Project Amount", "3,399,510,47", "Totals-Uncommitted Cumulative Balance", "3,399,510,47"),
                        new ReportAreaForTests(new AreaOwner(40), "Project Title", "SubNational no percentages", "Proposed Project Amount", "60,000", "Totals-Actual Commitments", "75,000", "Totals-Cumulative Commitment", "75,000", "Totals-Uncommitted Cumulative Balance", "-15,000", "Totals-Undisbursed Cumulative Balance", "75,000"),
                        new ReportAreaForTests(new AreaOwner(43), "Project Title", "Activity with primary_tertiary_program", "Proposed Project Amount", "66,392,25", "Totals-Actual Commitments", "50,000", "Totals-Cumulative Commitment", "50,000", "Totals-Uncommitted Cumulative Balance", "16,392,25", "Totals-Undisbursed Cumulative Balance", "50,000"),
                        new ReportAreaForTests(new AreaOwner(44), "Project Title", "activity with primary_program", "Proposed Project Amount", "35,000", "Totals-Actual Commitments", "32,000", "Totals-Cumulative Commitment", "32,000", "Totals-Uncommitted Cumulative Balance", "3,000", "Totals-Undisbursed Cumulative Balance", "32,000"),
                        new ReportAreaForTests(new AreaOwner(45), "Project Title", "activity with tertiary_program", "Proposed Project Amount", "70,000", "Totals-Actual Commitments", "15,000", "Totals-Cumulative Commitment", "15,000", "Totals-Uncommitted Cumulative Balance", "55,000", "Totals-Undisbursed Cumulative Balance", "15,000"),
                        new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Totals-Actual Commitments", "456,789", "Totals-Actual Disbursements", "321,765", "Totals-Cumulative Commitment", "456,789", "Totals-Cumulative Disbursement", "321,765", "Totals-Uncommitted Cumulative Balance", "-456,789", "Totals-Undisbursed Cumulative Balance", "135,024"),
                        new ReportAreaForTests(new AreaOwner(73), "Project Title", "activity with directed MTEFs", "Totals-Actual Commitments", "123,456", "Totals-Cumulative Commitment", "123,456", "Totals-Uncommitted Cumulative Balance", "-123,456", "Totals-Undisbursed Cumulative Balance", "123,456")      ));
            
            ReportSpecificationImpl spec = buildSpecification("Non-timebound computed measures", 
                Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.PROPOSED_PROJECT_AMOUNT),
                Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS, MeasureConstants.CUMULATIVE_COMMITMENT, 
                        MeasureConstants.CUMULATIVE_DISBURSEMENT, MeasureConstants.UNCOMMITTED_CUMULATIVE_BALANCE, MeasureConstants.UNDISBURSED_CUMULATIVE_BALANCE), 
                null,
                GroupingCriteria.GROUPING_TOTALS_ONLY);
            runNiTestCase(spec, "en", ppcActs, cor);
    }
    
    @Test
    public void testNonTimeboundWithHiers() {
        NiReportModel   cor = new NiReportModel("Non-timebound computed measures with hiers")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 0, colSpan: 9))",
                        "(Primary Program: (startRow: 1, rowSpan: 2, totalRowSpan: 2, colStart: 0, colSpan: 1));(Project Title: (startRow: 1, rowSpan: 2, totalRowSpan: 2, colStart: 1, colSpan: 1));(Proposed Project Amount: (startRow: 1, rowSpan: 2, totalRowSpan: 2, colStart: 2, colSpan: 1));(Totals: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 6))",
                        "(Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Cumulative Commitment: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Cumulative Disbursement: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Uncommitted Cumulative Balance: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Undisbursed Cumulative Balance: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1))"))
                    .withWarnings(Arrays.asList())
                    .withBody(      new ReportAreaForTests(null)
                      .withContents("Primary Program", "", "Project Title", "", "Proposed Project Amount", "4,630,902,72", "Totals-Actual Commitments", "752,245", "Totals-Actual Disbursements", "321,765", "Totals-Cumulative Commitment", "752,245", "Totals-Cumulative Disbursement", "321,765", "Totals-Uncommitted Cumulative Balance", "3,878,657,72", "Totals-Undisbursed Cumulative Balance", "430,480")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner("Primary Program", "Subprogram p1", 2))
                        .withContents("Project Title", "", "Proposed Project Amount", "33,196,12", "Totals-Actual Commitments", "148,456", "Totals-Actual Disbursements", "0", "Totals-Cumulative Commitment", "148,456", "Totals-Cumulative Disbursement", "0", "Totals-Uncommitted Cumulative Balance", "-115,259,88", "Totals-Undisbursed Cumulative Balance", "148,456", "Primary Program", "Subprogram p1")
                        .withChildren(
                          new ReportAreaForTests(new AreaOwner(43), "Project Title", "Activity with primary_tertiary_program", "Proposed Project Amount", "33,196,12", "Totals-Actual Commitments", "25,000", "Totals-Cumulative Commitment", "25,000", "Totals-Uncommitted Cumulative Balance", "8,196,12", "Totals-Undisbursed Cumulative Balance", "25,000"),
                          new ReportAreaForTests(new AreaOwner(73), "Project Title", "activity with directed MTEFs", "Totals-Actual Commitments", "123,456", "Totals-Cumulative Commitment", "123,456", "Totals-Uncommitted Cumulative Balance", "-123,456", "Totals-Undisbursed Cumulative Balance", "123,456")        ),
                        new ReportAreaForTests(new AreaOwner("Primary Program", "Subprogram p1.b", 3)).withContents("Project Title", "", "Proposed Project Amount", "33,196,12", "Totals-Actual Commitments", "25,000", "Totals-Actual Disbursements", "0", "Totals-Cumulative Commitment", "25,000", "Totals-Cumulative Disbursement", "0", "Totals-Uncommitted Cumulative Balance", "8,196,12", "Totals-Undisbursed Cumulative Balance", "25,000", "Primary Program", "Subprogram p1.b")
                        .withChildren(
                          new ReportAreaForTests(new AreaOwner(43), "Project Title", "Activity with primary_tertiary_program", "Proposed Project Amount", "33,196,12", "Totals-Actual Commitments", "25,000", "Totals-Cumulative Commitment", "25,000", "Totals-Uncommitted Cumulative Balance", "8,196,12", "Totals-Undisbursed Cumulative Balance", "25,000")        ),
                        new ReportAreaForTests(new AreaOwner("Primary Program", "Primary Program: Undefined", -1))
                        .withContents("Project Title", "", "Proposed Project Amount", "4,564,510,47", "Totals-Actual Commitments", "578,789", "Totals-Actual Disbursements", "321,765", "Totals-Cumulative Commitment", "578,789", "Totals-Cumulative Disbursement", "321,765", "Totals-Uncommitted Cumulative Balance", "3,985,721,47", "Totals-Undisbursed Cumulative Balance", "257,024", "Primary Program", "Primary Program: Undefined")
                        .withChildren(
                          new ReportAreaForTests(new AreaOwner(15), "Project Title", "Proposed Project Cost 1 - USD", "Proposed Project Amount", "1,000,000", "Totals-Uncommitted Cumulative Balance", "1,000,000"),
                          new ReportAreaForTests(new AreaOwner(17), "Project Title", "Proposed Project Cost 2 - EUR", "Proposed Project Amount", "3,399,510,47", "Totals-Uncommitted Cumulative Balance", "3,399,510,47"),
                          new ReportAreaForTests(new AreaOwner(40), "Project Title", "SubNational no percentages", "Proposed Project Amount", "60,000", "Totals-Actual Commitments", "75,000", "Totals-Cumulative Commitment", "75,000", "Totals-Uncommitted Cumulative Balance", "-15,000", "Totals-Undisbursed Cumulative Balance", "75,000"),
                          new ReportAreaForTests(new AreaOwner(44), "Project Title", "activity with primary_program", "Proposed Project Amount", "35,000", "Totals-Actual Commitments", "32,000", "Totals-Cumulative Commitment", "32,000", "Totals-Uncommitted Cumulative Balance", "3,000", "Totals-Undisbursed Cumulative Balance", "32,000"),
                          new ReportAreaForTests(new AreaOwner(45), "Project Title", "activity with tertiary_program", "Proposed Project Amount", "70,000", "Totals-Actual Commitments", "15,000", "Totals-Cumulative Commitment", "15,000", "Totals-Uncommitted Cumulative Balance", "55,000", "Totals-Undisbursed Cumulative Balance", "15,000"),
                          new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Totals-Actual Commitments", "456,789", "Totals-Actual Disbursements", "321,765", "Totals-Cumulative Commitment", "456,789", "Totals-Cumulative Disbursement", "321,765", "Totals-Uncommitted Cumulative Balance", "-456,789", "Totals-Undisbursed Cumulative Balance", "135,024")        )      ));
        
        ReportSpecificationImpl spec = buildSpecification("Non-timebound computed measures with hiers", 
                Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.PROPOSED_PROJECT_AMOUNT, ColumnConstants.PRIMARY_PROGRAM),
                Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS, MeasureConstants.CUMULATIVE_COMMITMENT, 
                        MeasureConstants.CUMULATIVE_DISBURSEMENT, MeasureConstants.UNCOMMITTED_CUMULATIVE_BALANCE, MeasureConstants.UNDISBURSED_CUMULATIVE_BALANCE), 
                Arrays.asList(ColumnConstants.PRIMARY_PROGRAM),
                GroupingCriteria.GROUPING_TOTALS_ONLY);

            
        runNiTestCase(spec, "en", ppcActs, cor);
    }
    
    
    @Test
    public void testSelectedYearPlannedDisbursements() {
        NiReportModel   cor = new NiReportModel("Selected year planned disbursements")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 0, colSpan: 3))",
                        "(Project Title: (startRow: 1, rowSpan: 2, totalRowSpan: 2, colStart: 0, colSpan: 1));(Totals: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 1, colSpan: 2))",
                        "(Planned Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1));(Selected Year Planned Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1))"))
                    .withWarnings(Arrays.asList())
                    .withBody(      new ReportAreaForTests(null)
                      .withContents("Project Title", "", "Totals-Planned Disbursements", "214,121", "Totals-Selected Year Planned Disbursements", "90,300")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(50), "Project Title", "activity with capital spending", "Totals-Planned Disbursements", "90,000", "Totals-Selected Year Planned Disbursements", "90,000"),
                        new ReportAreaForTests(new AreaOwner(69), "Project Title", "Activity with planned disbursements", "Totals-Planned Disbursements", "800", "Totals-Selected Year Planned Disbursements", "300"),
                        new ReportAreaForTests(new AreaOwner(88), "Project Title", "activity-weird-funding", "Totals-Planned Disbursements", "123,321")      ));
        
        ReportSpecificationImpl spec = buildSpecification("Selected year planned disbursements", 
                Arrays.asList(ColumnConstants.PROJECT_TITLE),
                Arrays.asList(MeasureConstants.PLANNED_DISBURSEMENTS, MeasureConstants.SELECTED_YEAR_PLANNED_DISBURSEMENTS), 
                null,
                GroupingCriteria.GROUPING_TOTALS_ONLY);

        AmpReportFilters filters = new AmpReportFilters(new HashMap<>());
        filters.setComputedYear(2014);
        spec.setFilters(filters);
        runNiTestCase(spec, "en", Arrays.asList("activity-weird-funding", "Activity with planned disbursements", "activity with capital spending"), cor);
    }
    @Test
    public void testTimeboundMeasures() {
        NiReportModel cor = new NiReportModel("Timebound computed measures (timelocked)")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 0, colSpan: 7))",
                        "(Project Title: (startRow: 1, rowSpan: 2, totalRowSpan: 2, colStart: 0, colSpan: 1));(Totals: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 1, colSpan: 6))",
                        "(Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1));(Previous Month Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Selected Year Planned Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Cumulated Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Current Month Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Last Year of Planned Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1))"))
                    .withWarnings(Arrays.asList())
                    .withBody(      new ReportAreaForTests(null)
                      .withContents("Project Title", "", "Totals-Actual Disbursements", "400,036", "Totals-Previous Month Disbursements", "84,200", "Totals-Selected Year Planned Disbursements", "123,321", "Totals-Cumulated Disbursements", "171,700", "Totals-Current Month Disbursements", "82,000", "Totals-Last Year of Planned Disbursements", "36,500")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(69), "Project Title", "Activity with planned disbursements", "Totals-Actual Disbursements", "770", "Totals-Last Year of Planned Disbursements", "500"),
                        new ReportAreaForTests(new AreaOwner(77), "Project Title", "execution rate activity", "Totals-Actual Disbursements", "90,000", "Totals-Last Year of Planned Disbursements", "36,000"),
                        new ReportAreaForTests(new AreaOwner(87), "Project Title", "expenditure class", "Totals-Actual Disbursements", "253,700", "Totals-Previous Month Disbursements", "84,200", "Totals-Cumulated Disbursements", "171,700", "Totals-Current Month Disbursements", "82,000"),
                        new ReportAreaForTests(new AreaOwner(88), "Project Title", "activity-weird-funding", "Totals-Actual Disbursements", "55,566", "Totals-Selected Year Planned Disbursements", "123,321")      ));
        
        ReportSpecificationImpl spec = buildSpecification("Timebound computed measures (timelocked)", 
                Arrays.asList(ColumnConstants.PROJECT_TITLE),
                Arrays.asList(MeasureConstants.ACTUAL_DISBURSEMENTS, MeasureConstants.PREVIOUS_MONTH_DISBURSEMENTS, 
                        MeasureConstants.SELECTED_YEAR_PLANNED_DISBURSEMENTS, MeasureConstants.CUMULATED_DISBURSEMENTS, 
                        MeasureConstants.CURRENT_MONTH_DISBURSEMENTS, MeasureConstants.LAST_YEAR_OF_PLANNED_DISBURSEMENTS),
                null,
                GroupingCriteria.GROUPING_TOTALS_ONLY);
        
        AmpReportFilters filters = new AmpReportFilters(new HashMap<>());
        filters.setComputedYear(2016);
        spec.setFilters(filters);

        runNiTestCase(spec, "en", Arrays.asList("activity-weird-funding", "expenditure class", 
                "Activity with planned disbursements", "execution rate activity"), cor);
    }
    
    @Test
    public void testPriorActualDisbursements() {
        NiReportModel cor = new NiReportModel("AMP-22639-prior-month")
            .withHeaders(Arrays.asList(
                    "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 11))",
                    "(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 1, colSpan: 6));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 7, colSpan: 4))",
                    "(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 1, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 2));(2016: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 2))",
                    "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Prior Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Previous Month Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                  .withContents("Project Title", "", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "545 000", "Funding-2015-Actual Commitments", "123 456", "Funding-2015-Actual Disbursements", "0", "Funding-2016-Actual Commitments", "62 000", "Funding-2016-Actual Disbursements", "253 700", "Totals-Actual Commitments", "185 456", "Totals-Actual Disbursements", "798 700", "Totals-Prior Actual Disbursements", "87 500", "Totals-Previous Month Disbursements", "84 200")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Funding-2013-Actual Disbursements", "545 000", "Totals-Actual Disbursements", "545 000"),
                    new ReportAreaForTests(new AreaOwner(73), "Project Title", "activity with directed MTEFs", "Funding-2015-Actual Commitments", "123 456", "Totals-Actual Commitments", "123 456"),
                    new ReportAreaForTests(new AreaOwner(87), "Project Title", "expenditure class", "Funding-2016-Actual Commitments", "62 000", "Funding-2016-Actual Disbursements", "253 700", "Totals-Actual Commitments", "62 000", "Totals-Actual Disbursements", "253 700", "Totals-Prior Actual Disbursements", "87 500", "Totals-Previous Month Disbursements", "84 200")));

        runNiTestCase(cor, spec("AMP-22639-prior-month"), acts);
    }
    
    @Test
    public void testCumulativeMeasuresIgnoringFilters(){
        NiReportModel cor = new NiReportModel("Columns ignoring filters: Cumulative Commitment, Cumulative Disbursement")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 0, colSpan: 6))",
                        "(Project Title: (startRow: 1, rowSpan: 2, totalRowSpan: 2, colStart: 0, colSpan: 1));(Proposed Project Amount: (startRow: 1, rowSpan: 2, totalRowSpan: 2, colStart: 1, colSpan: 1));(Totals: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 4))",
                        "(Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Cumulative Commitment: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Cumulative Disbursement: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1))"))
                    .withWarnings(Arrays.asList())
                    .withBody(      new ReportAreaForTests(null).withContents("Project Title", "", "Proposed Project Amount", "0", "Totals-Actual Commitments", "67,000", "Totals-Actual Disbursements", "0", "Totals-Cumulative Commitment", "150,000", "Totals-Cumulative Disbursement", "0")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(71), "Project Title", "activity_with_disaster_response", "Totals-Actual Commitments", "67,000", "Totals-Cumulative Commitment", "150,000")      ));
        
        ReportSpecificationImpl spec = buildSpecification("Columns ignoring filters: Cumulative Commitment, Cumulative Disbursement",
                Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.PROPOSED_PROJECT_AMOUNT),
                Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS,
                        MeasureConstants.CUMULATIVE_COMMITMENT, MeasureConstants.CUMULATIVE_DISBURSEMENT),
                null,
                GroupingCriteria.GROUPING_TOTALS_ONLY);
        spec.setFilters(buildSimpleFilter(ColumnConstants.DISASTER_RESPONSE_MARKER, FilterRule.TRUE_VALUE, true));
        runNiTestCase(spec, "en", Arrays.asList("activity_with_disaster_response"), cor);
    }
    
    @Test
    public void testCumulativeDeltaMeasuresIgnoringFilters(){
        NiReportModel cor = new NiReportModel("Linear columns ignoring filters: Undisbursed Cumulative Balance, Uncommitted Cumulative Balance")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 0, colSpan: 8))",
                        "(Project Title: (startRow: 1, rowSpan: 2, totalRowSpan: 2, colStart: 0, colSpan: 1));(Proposed Project Amount: (startRow: 1, rowSpan: 2, totalRowSpan: 2, colStart: 1, colSpan: 1));(Totals: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 6))",
                        "(Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Cumulative Commitment: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Cumulative Disbursement: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Uncommitted Cumulative Balance: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Undisbursed Cumulative Balance: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1))"))
                    .withWarnings(Arrays.asList())
                    .withBody(      new ReportAreaForTests(null).withContents("Project Title", "", "Proposed Project Amount", "0", "Totals-Actual Commitments", "67,000", "Totals-Actual Disbursements", "0", "Totals-Cumulative Commitment", "150,000", "Totals-Cumulative Disbursement", "0", "Totals-Uncommitted Cumulative Balance", "-150,000", "Totals-Undisbursed Cumulative Balance", "150,000")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(71), "Project Title", "activity_with_disaster_response", "Totals-Actual Commitments", "67,000", "Totals-Cumulative Commitment", "150,000", "Totals-Uncommitted Cumulative Balance", "-150,000", "Totals-Undisbursed Cumulative Balance", "150,000")      ));
        
        ReportSpecificationImpl spec = buildSpecification("Linear columns ignoring filters: Undisbursed Cumulative Balance, Uncommitted Cumulative Balance",
                Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.PROPOSED_PROJECT_AMOUNT),
                Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS,
                        MeasureConstants.CUMULATIVE_COMMITMENT, MeasureConstants.CUMULATIVE_DISBURSEMENT,
                        MeasureConstants.UNCOMMITTED_CUMULATIVE_BALANCE, MeasureConstants.UNDISBURSED_CUMULATIVE_BALANCE),
                null,
                GroupingCriteria.GROUPING_TOTALS_ONLY);
        spec.setFilters(buildSimpleFilter(ColumnConstants.DISASTER_RESPONSE_MARKER, FilterRule.TRUE_VALUE, true));
        runNiTestCase(spec, "en", Arrays.asList("activity_with_disaster_response"), cor);
    }
    
    @Test
    public void testCumulativeDeltaMeasuresIgnoringFiltersHier(){
        NiReportModel cor = new NiReportModel("Linear columns ignoring filters: Undisbursed Cumulative Balance, Uncommitted Cumulative Balancewith hierarchy ")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 0, colSpan: 9))",
                        "(Donor Agency: (startRow: 1, rowSpan: 2, totalRowSpan: 2, colStart: 0, colSpan: 1));(Project Title: (startRow: 1, rowSpan: 2, totalRowSpan: 2, colStart: 1, colSpan: 1));(Proposed Project Amount: (startRow: 1, rowSpan: 2, totalRowSpan: 2, colStart: 2, colSpan: 1));(Totals: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 6))",
                        "(Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Cumulative Commitment: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Cumulative Disbursement: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Uncommitted Cumulative Balance: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Undisbursed Cumulative Balance: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                        .withContents("Donor Agency", "", "Project Title", "", "Proposed Project Amount", "0", "Totals-Actual Commitments", "67,000", "Totals-Actual Disbursements", "0", "Totals-Cumulative Commitment", "150,000", "Totals-Cumulative Disbursement", "0", "Totals-Uncommitted Cumulative Balance", "-150,000", "Totals-Undisbursed Cumulative Balance", "150,000")
                        .withChildren(
                                new ReportAreaForTests(new AreaOwner("Donor Agency", "Finland", 21698)).withContents("Project Title", "", "Proposed Project Amount", "0", "Totals-Actual Commitments", "67,000", "Totals-Actual Disbursements", "0", "Totals-Cumulative Commitment", "150,000", "Totals-Cumulative Disbursement", "0", "Totals-Uncommitted Cumulative Balance", "0", "Totals-Undisbursed Cumulative Balance", "150,000", "Donor Agency", "Finland")
                                        .withChildren(
                                                new ReportAreaForTests(new AreaOwner(71), "Project Title", "activity_with_disaster_response", "Totals-Actual Commitments", "67,000", "Totals-Cumulative Commitment", "150,000", "Totals-Undisbursed Cumulative Balance", "150,000")        ),
                                new ReportAreaForTests(new AreaOwner("Donor Agency", "Donor Agency: Undefined", -999999999)).withContents("Project Title", "", "Proposed Project Amount", "0", "Totals-Actual Commitments", "0", "Totals-Actual Disbursements", "0", "Totals-Cumulative Commitment", "0", "Totals-Cumulative Disbursement", "0", "Totals-Uncommitted Cumulative Balance", "-150,000", "Totals-Undisbursed Cumulative Balance", "0", "Donor Agency", "Donor Agency: Undefined")
                                        .withChildren(
                                                new ReportAreaForTests(new AreaOwner(71), "Project Title", "activity_with_disaster_response", "Totals-Uncommitted Cumulative Balance", "-150,000")        )      ));
        
        ReportSpecificationImpl spec = buildSpecification("Linear columns ignoring filters: Undisbursed Cumulative Balance, Uncommitted Cumulative Balance"
                + "with hierarchy ",
                Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.PROPOSED_PROJECT_AMOUNT,
                        ColumnConstants.DONOR_AGENCY),
                Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS,
                        MeasureConstants.CUMULATIVE_COMMITMENT, MeasureConstants.CUMULATIVE_DISBURSEMENT,
                        MeasureConstants.UNCOMMITTED_CUMULATIVE_BALANCE, MeasureConstants.UNDISBURSED_CUMULATIVE_BALANCE),
                Arrays.asList(ColumnConstants.DONOR_AGENCY),
                GroupingCriteria.GROUPING_TOTALS_ONLY);
        spec.setFilters(buildSimpleFilter(ColumnConstants.DISASTER_RESPONSE_MARKER, FilterRule.TRUE_VALUE, true));
        runNiTestCase(spec, "en", Arrays.asList("activity_with_disaster_response"), cor);
    }

    @Test
    public void testCumulatedDisbursements(){
        NiReportModel cor = new NiReportModel("Cumulated Disbursements")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 0, colSpan: 5))",
                        "(Project Title: (startRow: 1, rowSpan: 2, totalRowSpan: 2, colStart: 0, colSpan: 1));(Totals: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 1, colSpan: 4))",
                        "(Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1));(Previous Month Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Prior Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Cumulated Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1))"))
                    .withWarnings(Arrays.asList())
                    .withBody(      new ReportAreaForTests(null).withContents("Project Title", "", "Totals-Actual Disbursements", "253,700", "Totals-Previous Month Disbursements", "84,200", "Totals-Prior Actual Disbursements", "87,500", "Totals-Cumulated Disbursements", "171,700")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(87), "Project Title", "expenditure class", "Totals-Actual Disbursements", "253,700", "Totals-Previous Month Disbursements", "84,200", "Totals-Prior Actual Disbursements", "87,500", "Totals-Cumulated Disbursements", "171,700")      ));
        
        ReportSpecificationImpl spec = buildSpecification("Cumulated Disbursements",
                Arrays.asList(ColumnConstants.PROJECT_TITLE),
                Arrays.asList(MeasureConstants.ACTUAL_DISBURSEMENTS, MeasureConstants.PREVIOUS_MONTH_DISBURSEMENTS,
                        MeasureConstants.PRIOR_ACTUAL_DISBURSEMENTS, MeasureConstants.CUMULATED_DISBURSEMENTS),
                null,
                GroupingCriteria.GROUPING_TOTALS_ONLY);
        runNiTestCase(spec, "en", Arrays.asList("expenditure class"), cor);
        
    }
    
    
    @After
    public void tearDown() {
        AmpReportsScratchpad.forcedNowDate = null;
    }
}
