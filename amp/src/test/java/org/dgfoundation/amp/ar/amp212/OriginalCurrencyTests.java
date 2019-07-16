package org.dgfoundation.amp.ar.amp212;

import java.util.Arrays;
import java.util.List;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.newreports.ReportAreaForTests;
import org.dgfoundation.amp.newreports.AmpReportingTestCase;
import org.dgfoundation.amp.newreports.AmountsUnits;
import org.dgfoundation.amp.newreports.AreaOwner;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.nireports.TestcasesReportsSchema;
import org.dgfoundation.amp.nireports.testcases.NiReportModel;
import org.junit.Test;

/**
 * 
 * testcases for the fetching states of AMP + the AMP schema
 * 
 * @author Viorel Chihai
 *
 */
public class OriginalCurrencyTests extends AmpReportingTestCase {
    
    private final List<String> acts = Arrays.asList(
            "activity 1 with agreement",
            "Activity 2 with multiple agreements",
            "Activity Linked With Pledge",
            "Activity with both MTEFs and Act.Comms",
            "activity with capital spending",
            "activity with components",
            "activity with contracting agency",
            "activity with directed MTEFs",
            "activity_with_disaster_response",
            "activity with funded components",
            "activity with incomplete agreement",
            "activity with many MTEFs",
            "activity with pipeline MTEFs and act. disb",
            "Activity with planned disbursements",
            "activity with primary_program",
            "Activity with primary_tertiary_program",
            "activity with tertiary_program",
            "activity-with-unfunded-components",
            "Activity with Zones",
            "Activity With Zones and Percentages",
            "crazy funding 1",
            "date-filters-activity",
            "Eth Water",
            "execution rate activity",
            "mtef activity 1",
            "mtef activity 2",
            "new activity with contracting",
            "pledged 2",
            "pledged education activity 1",
            "Project with documents",
            "Proposed Project Cost 1 - USD",
            "Proposed Project Cost 2 - EUR",
            "ptc activity 1",
            "ptc activity 2",
            "Pure MTEF Project",
            "SSC Project 1",
            "SSC Project 2",
            "SubNational no percentages",
            "TAC_activity_1",
            "TAC_activity_2",
            "Test MTEF directed",
            "third activity with agreements",
            "Unvalidated activity",
            "with weird currencies"
        );
    
    private final List<String> ppcActs = Arrays.asList(
            "Proposed Project Cost 1 - USD",
            "Proposed Project Cost 2 - EUR",
            "SubNational no percentages",
            "Activity with primary_tertiary_program",
            "activity with primary_program",
            "activity with tertiary_program",
            "activity 1 with agreement",
            "activity with directed MTEFs"
        );
    
    private final List<String> mtefActs = Arrays.asList(
            "mtef activity 1",
            "mtef activity 2",
            "Pure MTEF Project",
            "activity with MTEFs",
            "Activity with both MTEFs and Act.Comms",
            "activity with many MTEFs",
            "Test MTEF directed",
            "activity with pipeline MTEFs and act. disb"
        );

    @Test
    public void testOriginalCurrencyActualCommitments() {
        NiReportModel cor = new NiReportModel("Original currency actual commitments")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 5, colStart: 0, colSpan: 18))",
                        "(Project Title: (startRow: 1, rowSpan: 4, totalRowSpan: 4, colStart: 0, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 4, colStart: 1, colSpan: 17))",
                        "(2006: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 1, colSpan: 2));(2009: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 3, colSpan: 2));(2011: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 5, colSpan: 2));(2012: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 7, colSpan: 2));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 9, colSpan: 2));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 11, colSpan: 4));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 15, colSpan: 3))",
                        "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 1, colSpan: 2));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 2));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 2));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 7, colSpan: 2));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 9, colSpan: 2));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 11, colSpan: 4));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 15, colSpan: 3))",
                        "(EUR: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1));(Total USD: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(USD: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Total USD: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(USD: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Total USD: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(USD: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Total USD: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(USD: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Total USD: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(EUR: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1));(MDL: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1));(USD: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1));(Total USD: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 14, colSpan: 1));(EUR: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 15, colSpan: 1));(USD: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 16, colSpan: 1));(Total USD: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 17, colSpan: 1))"))
                    .withWarnings(Arrays.asList())
                    .withBody(      new ReportAreaForTests(null)
                      .withContents("Project Title", "", "Funding-2006-Actual Commitments-EUR", "80,000", "Funding-2006-Actual Commitments-Total USD", "96,840,58", "Funding-2009-Actual Commitments-USD", "100,000", "Funding-2009-Actual Commitments-Total USD", "100,000", "Funding-2011-Actual Commitments-USD", "1,213,119", "Funding-2011-Actual Commitments-Total USD", "1,213,119", "Funding-2012-Actual Commitments-USD", "25,000", "Funding-2012-Actual Commitments-Total USD", "25,000", "Funding-2013-Actual Commitments-USD", "7,842,086", "Funding-2013-Actual Commitments-Total USD", "7,842,086", "Funding-2014-Actual Commitments-EUR", "60,000", "Funding-2014-Actual Commitments-MDL", "50,000", "Funding-2014-Actual Commitments-USD", "8,090,421", "Funding-2014-Actual Commitments-Total USD", "8,159,813,77", "Funding-2015-Actual Commitments-EUR", "90,000", "Funding-2015-Actual Commitments-USD", "1,877,901", "Funding-2015-Actual Commitments-Total USD", "1,971,831,84")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(12), "Project Title", "TAC_activity_1", "Funding-2011-Actual Commitments-USD", "213,231", "Funding-2011-Actual Commitments-Total USD", "213,231"),
                        new ReportAreaForTests(new AreaOwner(13), "Project Title", "TAC_activity_2", "Funding-2011-Actual Commitments-USD", "999,888", "Funding-2011-Actual Commitments-Total USD", "999,888"),
                        new ReportAreaForTests(new AreaOwner(15), "Project Title", "Proposed Project Cost 1 - USD"),
                        new ReportAreaForTests(new AreaOwner(17), "Project Title", "Proposed Project Cost 2 - EUR"),
                        new ReportAreaForTests(new AreaOwner(18), "Project Title", "Test MTEF directed"),
                        new ReportAreaForTests(new AreaOwner(19), "Project Title", "Pure MTEF Project"),
                        new ReportAreaForTests(new AreaOwner(21), "Project Title", "activity with components"),
                        new ReportAreaForTests(new AreaOwner(23), "Project Title", "Project with documents"),
                        new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water"),
                        new ReportAreaForTests(new AreaOwner(25), "Project Title", "mtef activity 1"),
                        new ReportAreaForTests(new AreaOwner(26), "Project Title", "date-filters-activity", "Funding-2009-Actual Commitments-USD", "100,000", "Funding-2009-Actual Commitments-Total USD", "100,000", "Funding-2012-Actual Commitments-USD", "25,000", "Funding-2012-Actual Commitments-Total USD", "25,000"),
                        new ReportAreaForTests(new AreaOwner(27), "Project Title", "mtef activity 2"),
                        new ReportAreaForTests(new AreaOwner(28), "Project Title", "ptc activity 1", "Funding-2013-Actual Commitments-USD", "666,777", "Funding-2013-Actual Commitments-Total USD", "666,777"),
                        new ReportAreaForTests(new AreaOwner(29), "Project Title", "ptc activity 2", "Funding-2013-Actual Commitments-USD", "333,222", "Funding-2013-Actual Commitments-Total USD", "333,222"),
                        new ReportAreaForTests(new AreaOwner(30), "Project Title", "SSC Project 1", "Funding-2013-Actual Commitments-USD", "111,333", "Funding-2013-Actual Commitments-Total USD", "111,333"),
                        new ReportAreaForTests(new AreaOwner(31), "Project Title", "SSC Project 2", "Funding-2013-Actual Commitments-USD", "567,421", "Funding-2013-Actual Commitments-Total USD", "567,421"),
                        new ReportAreaForTests(new AreaOwner(32), "Project Title", "crazy funding 1", "Funding-2013-Actual Commitments-USD", "333,333", "Funding-2013-Actual Commitments-Total USD", "333,333"),
                        new ReportAreaForTests(new AreaOwner(33), "Project Title", "Activity with Zones", "Funding-2013-Actual Commitments-USD", "570,000", "Funding-2013-Actual Commitments-Total USD", "570,000"),
                        new ReportAreaForTests(new AreaOwner(36), "Project Title", "Activity With Zones and Percentages", "Funding-2013-Actual Commitments-USD", "890,000", "Funding-2013-Actual Commitments-Total USD", "890,000"),
                        new ReportAreaForTests(new AreaOwner(40), "Project Title", "SubNational no percentages", "Funding-2014-Actual Commitments-USD", "75,000", "Funding-2014-Actual Commitments-Total USD", "75,000"),
                        new ReportAreaForTests(new AreaOwner(41), "Project Title", "Activity Linked With Pledge", "Funding-2014-Actual Commitments-USD", "50,000", "Funding-2014-Actual Commitments-Total USD", "50,000"),
                        new ReportAreaForTests(new AreaOwner(43), "Project Title", "Activity with primary_tertiary_program", "Funding-2014-Actual Commitments-USD", "50,000", "Funding-2014-Actual Commitments-Total USD", "50,000"),
                        new ReportAreaForTests(new AreaOwner(44), "Project Title", "activity with primary_program", "Funding-2014-Actual Commitments-USD", "32,000", "Funding-2014-Actual Commitments-Total USD", "32,000"),
                        new ReportAreaForTests(new AreaOwner(45), "Project Title", "activity with tertiary_program", "Funding-2014-Actual Commitments-USD", "15,000", "Funding-2014-Actual Commitments-Total USD", "15,000"),
                        new ReportAreaForTests(new AreaOwner(46), "Project Title", "pledged education activity 1", "Funding-2013-Actual Commitments-USD", "1,700,000", "Funding-2013-Actual Commitments-Total USD", "1,700,000", "Funding-2014-Actual Commitments-USD", "3,300,000", "Funding-2014-Actual Commitments-Total USD", "3,300,000"),
                        new ReportAreaForTests(new AreaOwner(48), "Project Title", "pledged 2", "Funding-2013-Actual Commitments-USD", "2,670,000", "Funding-2013-Actual Commitments-Total USD", "2,670,000", "Funding-2014-Actual Commitments-USD", "4,400,000", "Funding-2014-Actual Commitments-Total USD", "4,400,000"),
                        new ReportAreaForTests(new AreaOwner(50), "Project Title", "activity with capital spending", "Funding-2014-Actual Commitments-EUR", "60,000", "Funding-2014-Actual Commitments-Total USD", "65,760,63"),
                        new ReportAreaForTests(new AreaOwner(52), "Project Title", "activity with contracting agency", "Funding-2006-Actual Commitments-EUR", "80,000", "Funding-2006-Actual Commitments-Total USD", "96,840,58"),
                        new ReportAreaForTests(new AreaOwner(53), "Project Title", "new activity with contracting", "Funding-2014-Actual Commitments-USD", "12,000", "Funding-2014-Actual Commitments-Total USD", "12,000"),
                        new ReportAreaForTests(new AreaOwner(61), "Project Title", "activity-with-unfunded-components", "Funding-2014-Actual Commitments-USD", "123,321", "Funding-2014-Actual Commitments-Total USD", "123,321"),
                        new ReportAreaForTests(new AreaOwner(63), "Project Title", "activity with funded components", "Funding-2014-Actual Commitments-USD", "100", "Funding-2014-Actual Commitments-Total USD", "100"),
                        new ReportAreaForTests(new AreaOwner(64), "Project Title", "Unvalidated activity", "Funding-2015-Actual Commitments-USD", "45,000", "Funding-2015-Actual Commitments-Total USD", "45,000"),
                        new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Funding-2015-Actual Commitments-USD", "456,789", "Funding-2015-Actual Commitments-Total USD", "456,789"),
                        new ReportAreaForTests(new AreaOwner(66), "Project Title", "Activity 2 with multiple agreements", "Funding-2015-Actual Commitments-USD", "1,200", "Funding-2015-Actual Commitments-Total USD", "1,200"),
                        new ReportAreaForTests(new AreaOwner(67), "Project Title", "third activity with agreements", "Funding-2015-Actual Commitments-USD", "123,456", "Funding-2015-Actual Commitments-Total USD", "123,456"),
                        new ReportAreaForTests(new AreaOwner(68), "Project Title", "activity with incomplete agreement", "Funding-2015-Actual Commitments-USD", "123,000", "Funding-2015-Actual Commitments-Total USD", "123,000"),
                        new ReportAreaForTests(new AreaOwner(69), "Project Title", "Activity with planned disbursements"),
                        new ReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms", "Funding-2015-Actual Commitments-USD", "888,000", "Funding-2015-Actual Commitments-Total USD", "888,000"),
                        new ReportAreaForTests(new AreaOwner(71), "Project Title", "activity_with_disaster_response", "Funding-2014-Actual Commitments-USD", "33,000", "Funding-2014-Actual Commitments-Total USD", "33,000", "Funding-2015-Actual Commitments-USD", "117,000", "Funding-2015-Actual Commitments-Total USD", "117,000"),
                        new ReportAreaForTests(new AreaOwner(73), "Project Title", "activity with directed MTEFs", "Funding-2015-Actual Commitments-USD", "123,456", "Funding-2015-Actual Commitments-Total USD", "123,456"),
                        new ReportAreaForTests(new AreaOwner(76), "Project Title", "activity with pipeline MTEFs and act. disb"),
                        new ReportAreaForTests(new AreaOwner(77), "Project Title", "execution rate activity"),
                        new ReportAreaForTests(new AreaOwner(78), "Project Title", "activity with many MTEFs"),
                        new ReportAreaForTests(new AreaOwner(79), "Project Title", "with weird currencies", "Funding-2014-Actual Commitments-MDL", "50,000", "Funding-2014-Actual Commitments-Total USD", "3,632,14", "Funding-2015-Actual Commitments-EUR", "90,000", "Funding-2015-Actual Commitments-Total USD", "93,930,84")      ));
        
        ReportSpecificationImpl spec = buildSpecification("Original currency actual commitments", 
                Arrays.asList(ColumnConstants.PROJECT_TITLE),
                Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS), 
                null,
                GroupingCriteria.GROUPING_YEARLY);
        spec.setShowOriginalCurrency(true);

        try {
            TestcasesReportsSchema.disableToAMoPSplitting = false;
            runNiTestCase(spec, "en", acts, cor);
        } finally {
            TestcasesReportsSchema.disableToAMoPSplitting = true;
        }
    }
    
    @Test
    public void testOriginalCurrencyWithHiers() {
        NiReportModel cor = new NiReportModel("Original currency with hiers")
            .withHeaders(Arrays.asList(
                    "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 5, colStart: 0, colSpan: 33))",
                    "(Primary Program Level 1: (startRow: 1, rowSpan: 4, totalRowSpan: 4, colStart: 0, colSpan: 1));(Project Title: (startRow: 1, rowSpan: 4, totalRowSpan: 4, colStart: 1, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 4, colStart: 2, colSpan: 31))",
                    "(2006: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 3));(2009: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 5, colSpan: 3));(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 8, colSpan: 3));(2011: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 11, colSpan: 3));(2012: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 14, colSpan: 4));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 18, colSpan: 4));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 22, colSpan: 6));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 28, colSpan: 5))",
                    "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 2));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 2));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 7, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 8, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 9, colSpan: 2));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 11, colSpan: 2));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 13, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 14, colSpan: 2));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 16, colSpan: 2));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 18, colSpan: 2));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 20, colSpan: 2));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 22, colSpan: 4));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 26, colSpan: 2));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 28, colSpan: 3));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 31, colSpan: 2))",
                    "(EUR: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Total USD: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Total USD: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(USD: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Total USD: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Total USD: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Total USD: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(USD: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Total USD: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(USD: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1));(Total USD: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1));(Total USD: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1));(USD: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 14, colSpan: 1));(Total USD: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 15, colSpan: 1));(USD: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 16, colSpan: 1));(Total USD: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 17, colSpan: 1));(USD: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 18, colSpan: 1));(Total USD: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 19, colSpan: 1));(USD: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 20, colSpan: 1));(Total USD: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 21, colSpan: 1));(EUR: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 22, colSpan: 1));(MDL: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 23, colSpan: 1));(USD: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 24, colSpan: 1));(Total USD: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 25, colSpan: 1));(USD: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 26, colSpan: 1));(Total USD: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 27, colSpan: 1));(EUR: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 28, colSpan: 1));(USD: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 29, colSpan: 1));(Total USD: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 30, colSpan: 1));(USD: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 31, colSpan: 1));(Total USD: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 32, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                  .withContents("Primary Program Level 1", "", "Project Title", "", "Funding-2006-Actual Commitments-EUR", "80,000", "Funding-2006-Actual Commitments-Total USD", "96,840,58", "Funding-2006-Actual Disbursements-Total USD", "0", "Funding-2009-Actual Commitments-USD", "100,000", "Funding-2009-Actual Commitments-Total USD", "100,000", "Funding-2009-Actual Disbursements-Total USD", "0", "Funding-2010-Actual Commitments-Total USD", "0", "Funding-2010-Actual Disbursements-USD", "780,311", "Funding-2010-Actual Disbursements-Total USD", "780,311", "Funding-2011-Actual Commitments-USD", "1,213,119", "Funding-2011-Actual Commitments-Total USD", "1,213,119", "Funding-2011-Actual Disbursements-Total USD", "0", "Funding-2012-Actual Commitments-USD", "25,000", "Funding-2012-Actual Commitments-Total USD", "25,000", "Funding-2012-Actual Disbursements-USD", "12,000", "Funding-2012-Actual Disbursements-Total USD", "12,000", "Funding-2013-Actual Commitments-USD", "7,842,086", "Funding-2013-Actual Commitments-Total USD", "7,842,086", "Funding-2013-Actual Disbursements-USD", "1,266,956", "Funding-2013-Actual Disbursements-Total USD", "1,266,956", "Funding-2014-Actual Commitments-EUR", "60,000", "Funding-2014-Actual Commitments-MDL", "50,000", "Funding-2014-Actual Commitments-USD", "8,090,421", "Funding-2014-Actual Commitments-Total USD", "8,159,813,77", "Funding-2014-Actual Disbursements-USD", "710,200", "Funding-2014-Actual Disbursements-Total USD", "710,200", "Funding-2015-Actual Commitments-EUR", "90,000", "Funding-2015-Actual Commitments-USD", "1,877,901", "Funding-2015-Actual Commitments-Total USD", "1,971,831,84", "Funding-2015-Actual Disbursements-USD", "437,335", "Funding-2015-Actual Disbursements-Total USD", "437,335")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner("Primary Program Level 1", "Subprogram p1", 2))
                    .withContents("Project Title", "", "Funding-2006-Actual Commitments-EUR", "0", "Funding-2006-Actual Commitments-Total USD", "0", "Funding-2006-Actual Disbursements-Total USD", "0", "Funding-2009-Actual Commitments-USD", "0", "Funding-2009-Actual Commitments-Total USD", "0", "Funding-2009-Actual Disbursements-Total USD", "0", "Funding-2010-Actual Commitments-Total USD", "0", "Funding-2010-Actual Disbursements-USD", "0", "Funding-2010-Actual Disbursements-Total USD", "0", "Funding-2011-Actual Commitments-USD", "0", "Funding-2011-Actual Commitments-Total USD", "0", "Funding-2011-Actual Disbursements-Total USD", "0", "Funding-2012-Actual Commitments-USD", "0", "Funding-2012-Actual Commitments-Total USD", "0", "Funding-2012-Actual Disbursements-USD", "0", "Funding-2012-Actual Disbursements-Total USD", "0", "Funding-2013-Actual Commitments-USD", "0", "Funding-2013-Actual Commitments-Total USD", "0", "Funding-2013-Actual Disbursements-USD", "0", "Funding-2013-Actual Disbursements-Total USD", "0", "Funding-2014-Actual Commitments-EUR", "0", "Funding-2014-Actual Commitments-MDL", "33,500", "Funding-2014-Actual Commitments-USD", "25,100", "Funding-2014-Actual Commitments-Total USD", "27,533,53", "Funding-2014-Actual Disbursements-USD", "55,000", "Funding-2014-Actual Disbursements-Total USD", "55,000", "Funding-2015-Actual Commitments-EUR", "60,300", "Funding-2015-Actual Commitments-USD", "1,011,456", "Funding-2015-Actual Commitments-Total USD", "1,074,389,66", "Funding-2015-Actual Disbursements-USD", "115,000", "Funding-2015-Actual Disbursements-Total USD", "115,000", "Primary Program Level 1", "Subprogram p1")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner(43), "Project Title", "Activity with primary_tertiary_program", "Funding-2014-Actual Commitments-USD", "25,000", "Funding-2014-Actual Commitments-Total USD", "25,000"),
                      new ReportAreaForTests(new AreaOwner(63), "Project Title", "activity with funded components", "Funding-2014-Actual Commitments-USD", "100", "Funding-2014-Actual Commitments-Total USD", "100"),
                      new ReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms", "Funding-2015-Actual Commitments-USD", "888,000", "Funding-2015-Actual Commitments-Total USD", "888,000"),
                      new ReportAreaForTests(new AreaOwner(73), "Project Title", "activity with directed MTEFs", "Funding-2015-Actual Commitments-USD", "123,456", "Funding-2015-Actual Commitments-Total USD", "123,456"),
                      new ReportAreaForTests(new AreaOwner(77), "Project Title", "execution rate activity", "Funding-2014-Actual Disbursements-USD", "55,000", "Funding-2014-Actual Disbursements-Total USD", "55,000", "Funding-2015-Actual Disbursements-USD", "35,000", "Funding-2015-Actual Disbursements-Total USD", "35,000"),
                      new ReportAreaForTests(new AreaOwner(78), "Project Title", "activity with many MTEFs", "Funding-2015-Actual Disbursements-USD", "80,000", "Funding-2015-Actual Disbursements-Total USD", "80,000"),
                      new ReportAreaForTests(new AreaOwner(79), "Project Title", "with weird currencies", "Funding-2014-Actual Commitments-MDL", "33,500", "Funding-2014-Actual Commitments-Total USD", "2,433,53", "Funding-2015-Actual Commitments-EUR", "60,300", "Funding-2015-Actual Commitments-Total USD", "62,933,66")        ),
                    new ReportAreaForTests(new AreaOwner("Primary Program Level 1", "Subprogram p1.b", 3))
                    .withContents("Project Title", "", "Funding-2006-Actual Commitments-EUR", "0", "Funding-2006-Actual Commitments-Total USD", "0", "Funding-2006-Actual Disbursements-Total USD", "0", "Funding-2009-Actual Commitments-USD", "0", "Funding-2009-Actual Commitments-Total USD", "0", "Funding-2009-Actual Disbursements-Total USD", "0", "Funding-2010-Actual Commitments-Total USD", "0", "Funding-2010-Actual Disbursements-USD", "0", "Funding-2010-Actual Disbursements-Total USD", "0", "Funding-2011-Actual Commitments-USD", "0", "Funding-2011-Actual Commitments-Total USD", "0", "Funding-2011-Actual Disbursements-Total USD", "0", "Funding-2012-Actual Commitments-USD", "0", "Funding-2012-Actual Commitments-Total USD", "0", "Funding-2012-Actual Disbursements-USD", "0", "Funding-2012-Actual Disbursements-Total USD", "0", "Funding-2013-Actual Commitments-USD", "0", "Funding-2013-Actual Commitments-Total USD", "0", "Funding-2013-Actual Disbursements-USD", "35,000", "Funding-2013-Actual Disbursements-Total USD", "35,000", "Funding-2014-Actual Commitments-EUR", "0", "Funding-2014-Actual Commitments-MDL", "16,500", "Funding-2014-Actual Commitments-USD", "58,000", "Funding-2014-Actual Commitments-Total USD", "59,198,61", "Funding-2014-Actual Disbursements-USD", "75,000", "Funding-2014-Actual Disbursements-Total USD", "75,000", "Funding-2015-Actual Commitments-EUR", "29,700", "Funding-2015-Actual Commitments-USD", "117,000", "Funding-2015-Actual Commitments-Total USD", "147,997,18", "Funding-2015-Actual Disbursements-USD", "0", "Funding-2015-Actual Disbursements-Total USD", "0", "Primary Program Level 1", "Subprogram p1.b")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner(43), "Project Title", "Activity with primary_tertiary_program", "Funding-2014-Actual Commitments-USD", "25,000", "Funding-2014-Actual Commitments-Total USD", "25,000"),
                      new ReportAreaForTests(new AreaOwner(71), "Project Title", "activity_with_disaster_response", "Funding-2014-Actual Commitments-USD", "33,000", "Funding-2014-Actual Commitments-Total USD", "33,000", "Funding-2015-Actual Commitments-USD", "117,000", "Funding-2015-Actual Commitments-Total USD", "117,000"),
                      new ReportAreaForTests(new AreaOwner(76), "Project Title", "activity with pipeline MTEFs and act. disb", "Funding-2013-Actual Disbursements-USD", "35,000", "Funding-2013-Actual Disbursements-Total USD", "35,000", "Funding-2014-Actual Disbursements-USD", "75,000", "Funding-2014-Actual Disbursements-Total USD", "75,000"),
                      new ReportAreaForTests(new AreaOwner(79), "Project Title", "with weird currencies", "Funding-2014-Actual Commitments-MDL", "16,500", "Funding-2014-Actual Commitments-Total USD", "1,198,61", "Funding-2015-Actual Commitments-EUR", "29,700", "Funding-2015-Actual Commitments-Total USD", "30,997,18")        ),
                    new ReportAreaForTests(new AreaOwner("Primary Program Level 1", "Primary Program Level 1: Undefined", -1))
                    .withContents("Project Title", "", "Funding-2006-Actual Commitments-EUR", "80,000", "Funding-2006-Actual Commitments-Total USD", "96,840,58", "Funding-2006-Actual Disbursements-Total USD", "0", "Funding-2009-Actual Commitments-USD", "100,000", "Funding-2009-Actual Commitments-Total USD", "100,000", "Funding-2009-Actual Disbursements-Total USD", "0", "Funding-2010-Actual Commitments-Total USD", "0", "Funding-2010-Actual Disbursements-USD", "780,311", "Funding-2010-Actual Disbursements-Total USD", "780,311", "Funding-2011-Actual Commitments-USD", "1,213,119", "Funding-2011-Actual Commitments-Total USD", "1,213,119", "Funding-2011-Actual Disbursements-Total USD", "0", "Funding-2012-Actual Commitments-USD", "25,000", "Funding-2012-Actual Commitments-Total USD", "25,000", "Funding-2012-Actual Disbursements-USD", "12,000", "Funding-2012-Actual Disbursements-Total USD", "12,000", "Funding-2013-Actual Commitments-USD", "7,842,086", "Funding-2013-Actual Commitments-Total USD", "7,842,086", "Funding-2013-Actual Disbursements-USD", "1,231,956", "Funding-2013-Actual Disbursements-Total USD", "1,231,956", "Funding-2014-Actual Commitments-EUR", "60,000", "Funding-2014-Actual Commitments-MDL", "0", "Funding-2014-Actual Commitments-USD", "8,007,321", "Funding-2014-Actual Commitments-Total USD", "8,073,081,63", "Funding-2014-Actual Disbursements-USD", "580,200", "Funding-2014-Actual Disbursements-Total USD", "580,200", "Funding-2015-Actual Commitments-EUR", "0", "Funding-2015-Actual Commitments-USD", "749,445", "Funding-2015-Actual Commitments-Total USD", "749,445", "Funding-2015-Actual Disbursements-USD", "322,335", "Funding-2015-Actual Disbursements-Total USD", "322,335", "Primary Program Level 1", "Primary Program Level 1: Undefined")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner(12), "Project Title", "TAC_activity_1", "Funding-2010-Actual Disbursements-USD", "123,321", "Funding-2010-Actual Disbursements-Total USD", "123,321", "Funding-2011-Actual Commitments-USD", "213,231", "Funding-2011-Actual Commitments-Total USD", "213,231"),
                      new ReportAreaForTests(new AreaOwner(13), "Project Title", "TAC_activity_2", "Funding-2010-Actual Disbursements-USD", "453,213", "Funding-2010-Actual Disbursements-Total USD", "453,213", "Funding-2011-Actual Commitments-USD", "999,888", "Funding-2011-Actual Commitments-Total USD", "999,888"),
                      new ReportAreaForTests(new AreaOwner(18), "Project Title", "Test MTEF directed", "Funding-2010-Actual Disbursements-USD", "143,777", "Funding-2010-Actual Disbursements-Total USD", "143,777"),
                      new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Funding-2013-Actual Disbursements-USD", "545,000", "Funding-2013-Actual Disbursements-Total USD", "545,000"),
                      new ReportAreaForTests(new AreaOwner(26), "Project Title", "date-filters-activity", "Funding-2009-Actual Commitments-USD", "100,000", "Funding-2009-Actual Commitments-Total USD", "100,000", "Funding-2010-Actual Disbursements-USD", "60,000", "Funding-2010-Actual Disbursements-Total USD", "60,000", "Funding-2012-Actual Commitments-USD", "25,000", "Funding-2012-Actual Commitments-Total USD", "25,000", "Funding-2012-Actual Disbursements-USD", "12,000", "Funding-2012-Actual Disbursements-Total USD", "12,000"),
                      new ReportAreaForTests(new AreaOwner(28), "Project Title", "ptc activity 1", "Funding-2013-Actual Commitments-USD", "666,777", "Funding-2013-Actual Commitments-Total USD", "666,777"),
                      new ReportAreaForTests(new AreaOwner(29), "Project Title", "ptc activity 2", "Funding-2013-Actual Commitments-USD", "333,222", "Funding-2013-Actual Commitments-Total USD", "333,222"),
                      new ReportAreaForTests(new AreaOwner(30), "Project Title", "SSC Project 1", "Funding-2013-Actual Commitments-USD", "111,333", "Funding-2013-Actual Commitments-Total USD", "111,333", "Funding-2013-Actual Disbursements-USD", "555,111", "Funding-2013-Actual Disbursements-Total USD", "555,111"),
                      new ReportAreaForTests(new AreaOwner(31), "Project Title", "SSC Project 2", "Funding-2013-Actual Commitments-USD", "567,421", "Funding-2013-Actual Commitments-Total USD", "567,421", "Funding-2013-Actual Disbursements-USD", "131,845", "Funding-2013-Actual Disbursements-Total USD", "131,845"),
                      new ReportAreaForTests(new AreaOwner(32), "Project Title", "crazy funding 1", "Funding-2013-Actual Commitments-USD", "333,333", "Funding-2013-Actual Commitments-Total USD", "333,333"),
                      new ReportAreaForTests(new AreaOwner(33), "Project Title", "Activity with Zones", "Funding-2013-Actual Commitments-USD", "570,000", "Funding-2013-Actual Commitments-Total USD", "570,000"),
                      new ReportAreaForTests(new AreaOwner(36), "Project Title", "Activity With Zones and Percentages", "Funding-2013-Actual Commitments-USD", "890,000", "Funding-2013-Actual Commitments-Total USD", "890,000"),
                      new ReportAreaForTests(new AreaOwner(40), "Project Title", "SubNational no percentages", "Funding-2014-Actual Commitments-USD", "75,000", "Funding-2014-Actual Commitments-Total USD", "75,000"),
                      new ReportAreaForTests(new AreaOwner(41), "Project Title", "Activity Linked With Pledge", "Funding-2014-Actual Commitments-USD", "50,000", "Funding-2014-Actual Commitments-Total USD", "50,000"),
                      new ReportAreaForTests(new AreaOwner(44), "Project Title", "activity with primary_program", "Funding-2014-Actual Commitments-USD", "32,000", "Funding-2014-Actual Commitments-Total USD", "32,000"),
                      new ReportAreaForTests(new AreaOwner(45), "Project Title", "activity with tertiary_program", "Funding-2014-Actual Commitments-USD", "15,000", "Funding-2014-Actual Commitments-Total USD", "15,000"),
                      new ReportAreaForTests(new AreaOwner(46), "Project Title", "pledged education activity 1", "Funding-2013-Actual Commitments-USD", "1,700,000", "Funding-2013-Actual Commitments-Total USD", "1,700,000", "Funding-2014-Actual Commitments-USD", "3,300,000", "Funding-2014-Actual Commitments-Total USD", "3,300,000"),
                      new ReportAreaForTests(new AreaOwner(48), "Project Title", "pledged 2", "Funding-2013-Actual Commitments-USD", "2,670,000", "Funding-2013-Actual Commitments-Total USD", "2,670,000", "Funding-2014-Actual Commitments-USD", "4,400,000", "Funding-2014-Actual Commitments-Total USD", "4,400,000", "Funding-2014-Actual Disbursements-USD", "450,000", "Funding-2014-Actual Disbursements-Total USD", "450,000"),
                      new ReportAreaForTests(new AreaOwner(50), "Project Title", "activity with capital spending", "Funding-2014-Actual Commitments-EUR", "60,000", "Funding-2014-Actual Commitments-Total USD", "65,760,63", "Funding-2014-Actual Disbursements-USD", "80,000", "Funding-2014-Actual Disbursements-Total USD", "80,000"),
                      new ReportAreaForTests(new AreaOwner(52), "Project Title", "activity with contracting agency", "Funding-2006-Actual Commitments-EUR", "80,000", "Funding-2006-Actual Commitments-Total USD", "96,840,58", "Funding-2014-Actual Disbursements-USD", "50,000", "Funding-2014-Actual Disbursements-Total USD", "50,000"),
                      new ReportAreaForTests(new AreaOwner(53), "Project Title", "new activity with contracting", "Funding-2014-Actual Commitments-USD", "12,000", "Funding-2014-Actual Commitments-Total USD", "12,000"),
                      new ReportAreaForTests(new AreaOwner(61), "Project Title", "activity-with-unfunded-components", "Funding-2014-Actual Commitments-USD", "123,321", "Funding-2014-Actual Commitments-Total USD", "123,321"),
                      new ReportAreaForTests(new AreaOwner(64), "Project Title", "Unvalidated activity", "Funding-2015-Actual Commitments-USD", "45,000", "Funding-2015-Actual Commitments-Total USD", "45,000"),
                      new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Funding-2015-Actual Commitments-USD", "456,789", "Funding-2015-Actual Commitments-Total USD", "456,789", "Funding-2015-Actual Disbursements-USD", "321,765", "Funding-2015-Actual Disbursements-Total USD", "321,765"),
                      new ReportAreaForTests(new AreaOwner(66), "Project Title", "Activity 2 with multiple agreements", "Funding-2015-Actual Commitments-USD", "1,200", "Funding-2015-Actual Commitments-Total USD", "1,200"),
                      new ReportAreaForTests(new AreaOwner(67), "Project Title", "third activity with agreements", "Funding-2015-Actual Commitments-USD", "123,456", "Funding-2015-Actual Commitments-Total USD", "123,456"),
                      new ReportAreaForTests(new AreaOwner(68), "Project Title", "activity with incomplete agreement", "Funding-2015-Actual Commitments-USD", "123,000", "Funding-2015-Actual Commitments-Total USD", "123,000"),
                      new ReportAreaForTests(new AreaOwner(69), "Project Title", "Activity with planned disbursements", "Funding-2014-Actual Disbursements-USD", "200", "Funding-2014-Actual Disbursements-Total USD", "200", "Funding-2015-Actual Disbursements-USD", "570", "Funding-2015-Actual Disbursements-Total USD", "570")        )      ));
        
        ReportSpecificationImpl spec = buildSpecification("Original currency with hiers", 
                Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.PRIMARY_PROGRAM_LEVEL_1),
                Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS), 
                Arrays.asList(ColumnConstants.PRIMARY_PROGRAM_LEVEL_1),
                GroupingCriteria.GROUPING_YEARLY);
        spec.setShowOriginalCurrency(true);
            
        
        try {
            TestcasesReportsSchema.disableToAMoPSplitting = false;
            runNiTestCase(spec, "en", acts, cor);
        } finally {
            TestcasesReportsSchema.disableToAMoPSplitting = true;
        }
    }
    
    @Test
    public void testProposedProjectOriginalCurrency() {
        NiReportModel cor = new NiReportModel("PPC-original-currency")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 5, colStart: 0, colSpan: 8))",
                        "(Project Title: (startRow: 1, rowSpan: 4, totalRowSpan: 4, colStart: 0, colSpan: 1));(Proposed Project Amount: (startRow: 1, rowSpan: 3, totalRowSpan: 4, colStart: 1, colSpan: 3));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 4, colStart: 4, colSpan: 4))",
                        "(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 4, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 6, colSpan: 2))",
                        "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 2));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 6, colSpan: 2))",
                        "(EUR: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1));(USD: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Total USD: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(USD: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Total USD: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(USD: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Total USD: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1))"))
                    .withWarnings(Arrays.asList())
                    .withBody(      new ReportAreaForTests(null)
                      .withContents("Project Title", "", "Proposed Project Amount-EUR", "2,550", "Proposed Project Amount-USD", "1,165", "Proposed Project Amount-Total USD", "4,630,9", "Funding-2014-Actual Commitments-USD", "172", "Funding-2014-Actual Commitments-Total USD", "172", "Funding-2015-Actual Commitments-USD", "580,24", "Funding-2015-Actual Commitments-Total USD", "580,24")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(15), "Project Title", "Proposed Project Cost 1 - USD", "Proposed Project Amount-USD", "1,000", "Proposed Project Amount-Total USD", "1,000"),
                        new ReportAreaForTests(new AreaOwner(17), "Project Title", "Proposed Project Cost 2 - EUR", "Proposed Project Amount-EUR", "2,500", "Proposed Project Amount-Total USD", "3,399,51"),
                        new ReportAreaForTests(new AreaOwner(40), "Project Title", "SubNational no percentages", "Proposed Project Amount-USD", "60", "Proposed Project Amount-Total USD", "60", "Funding-2014-Actual Commitments-USD", "75", "Funding-2014-Actual Commitments-Total USD", "75"),
                        new ReportAreaForTests(new AreaOwner(43), "Project Title", "Activity with primary_tertiary_program", "Proposed Project Amount-EUR", "50", "Proposed Project Amount-Total USD", "66,39", "Funding-2014-Actual Commitments-USD", "50", "Funding-2014-Actual Commitments-Total USD", "50"),
                        new ReportAreaForTests(new AreaOwner(44), "Project Title", "activity with primary_program", "Proposed Project Amount-USD", "35", "Proposed Project Amount-Total USD", "35", "Funding-2014-Actual Commitments-USD", "32", "Funding-2014-Actual Commitments-Total USD", "32"),
                        new ReportAreaForTests(new AreaOwner(45), "Project Title", "activity with tertiary_program", "Proposed Project Amount-USD", "70", "Proposed Project Amount-Total USD", "70", "Funding-2014-Actual Commitments-USD", "15", "Funding-2014-Actual Commitments-Total USD", "15"),
                        new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Funding-2015-Actual Commitments-USD", "456,79", "Funding-2015-Actual Commitments-Total USD", "456,79"),
                        new ReportAreaForTests(new AreaOwner(73), "Project Title", "activity with directed MTEFs", "Funding-2015-Actual Commitments-USD", "123,46", "Funding-2015-Actual Commitments-Total USD", "123,46")      ));
        
        ReportSpecificationImpl spec = buildSpecification("PPC-original-currency", 
            Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.PROPOSED_PROJECT_AMOUNT),
            Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS), 
            null,
            GroupingCriteria.GROUPING_YEARLY);
        spec.getOrCreateSettings().setUnitsOption(AmountsUnits.AMOUNTS_OPTION_THOUSANDS);
        spec.setShowOriginalCurrency(true);
        
        try {
            TestcasesReportsSchema.disableToAMoPSplitting = false;
            runNiTestCase(spec, "en", ppcActs, cor);
        } finally {
            TestcasesReportsSchema.disableToAMoPSplitting = true;
        }
    }
    
    @Test
    public void testMtefOriginalCurrency() {
        NiReportModel cor = new NiReportModel("MTEF-original-currency")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 5, colStart: 0, colSpan: 9))",
                        "(Project Title: (startRow: 1, rowSpan: 4, totalRowSpan: 4, colStart: 0, colSpan: 1));(MTEF 2011: (startRow: 1, rowSpan: 3, totalRowSpan: 4, colStart: 1, colSpan: 2));(MTEF 2012: (startRow: 1, rowSpan: 3, totalRowSpan: 4, colStart: 3, colSpan: 2));(MTEF 2013: (startRow: 1, rowSpan: 3, totalRowSpan: 4, colStart: 5, colSpan: 2));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 4, colStart: 7, colSpan: 2))",
                        "(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 7, colSpan: 2))",
                        "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 7, colSpan: 2))",
                        "(USD: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1));(Total EUR: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(USD: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Total EUR: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(USD: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Total EUR: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(USD: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Total EUR: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1))"))
                    .withWarnings(Arrays.asList())
                    .withBody(      new ReportAreaForTests(null)
                      .withContents("Project Title", "", "MTEF 2011-USD", "1,718,01", "MTEF 2011-Total EUR", "1,283,18", "MTEF 2012-USD", "271", "MTEF 2012-Total EUR", "202,44", "MTEF 2013-USD", "158,65", "MTEF 2013-Total EUR", "120,18", "Funding-2015-Actual Commitments-USD", "888", "Funding-2015-Actual Commitments-Total EUR", "810,21")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(18), "Project Title", "Test MTEF directed", "MTEF 2011-USD", "150", "MTEF 2011-Total EUR", "112,04", "MTEF 2012-USD", "65", "MTEF 2012-Total EUR", "48,56"),
                        new ReportAreaForTests(new AreaOwner(19), "Project Title", "Pure MTEF Project", "MTEF 2011-USD", "33,89", "MTEF 2011-Total EUR", "25,31"),
                        new ReportAreaForTests(new AreaOwner(25), "Project Title", "mtef activity 1", "MTEF 2011-USD", "789,12", "MTEF 2011-Total EUR", "589,4"),
                        new ReportAreaForTests(new AreaOwner(27), "Project Title", "mtef activity 2", "MTEF 2013-USD", "123,65", "MTEF 2013-Total EUR", "93,67"),
                        new ReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms", "MTEF 2011-USD", "700", "MTEF 2011-Total EUR", "522,83", "MTEF 2012-USD", "150", "MTEF 2012-Total EUR", "112,05", "Funding-2015-Actual Commitments-USD", "888", "Funding-2015-Actual Commitments-Total EUR", "810,21"),
                        new ReportAreaForTests(new AreaOwner(76), "Project Title", "activity with pipeline MTEFs and act. disb", "MTEF 2013-USD", "35", "MTEF 2013-Total EUR", "26,51"),
                        new ReportAreaForTests(new AreaOwner(78), "Project Title", "activity with many MTEFs", "MTEF 2011-USD", "45", "MTEF 2011-Total EUR", "33,61", "MTEF 2012-USD", "56", "MTEF 2012-Total EUR", "41,83")      ));

            ReportSpecificationImpl spec = buildSpecification("MTEF-original-currency",
                Arrays.asList(ColumnConstants.PROJECT_TITLE, "MTEF 2011", "MTEF 2012", "MTEF 2013"), 
                Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS), 
                null,
                GroupingCriteria.GROUPING_YEARLY);
            spec.getOrCreateSettings().setUnitsOption(AmountsUnits.AMOUNTS_OPTION_THOUSANDS);
            spec.getOrCreateSettings().setCurrencyCode("EUR");
            spec.setShowOriginalCurrency(true);
            
            try {
                TestcasesReportsSchema.disableToAMoPSplitting = false;
                runNiTestCase(cor, spec, mtefActs);
            } finally {
                TestcasesReportsSchema.disableToAMoPSplitting = true;
            }
    }
    
}
