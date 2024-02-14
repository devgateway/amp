package org.dgfoundation.amp.ar.amp212;

import org.dgfoundation.amp.StandaloneAMPInitializer;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.newreports.AreaOwner;
import org.dgfoundation.amp.newreports.ReportAreaForTests;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.nireports.output.NiReportExecutor;
import org.dgfoundation.amp.nireports.runtime.ColumnReportData;
import org.dgfoundation.amp.nireports.testcases.NiReportModel;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.Arrays;
import java.util.List;

/**
 * 
 * testcases for the fetching states of AMP + the AMP schema
 * 
 * @author Constantin Dolghier
 *
 */
@Category(org.dgfoundation.amp.test.categories.DatabaseTests.class)
public class AmpSchemaFilteringTests extends FilteringSanityChecks {

    final List<String> flowsActs = Arrays.asList(
        "activity with directed MTEFs",
        "Activity with both MTEFs and Act.Comms",
        "mtef activity 1",
        "mtef activity 2",
        "Pure MTEF Project",
        "activity with MTEFs",
        "activity with many MTEFs",
        "Test MTEF directed",
        "activity with pipeline MTEFs and act. disb",
        "Eth Water",
        "Activity with Zones",
        "TAC_activity_2"
    );
    
    final List<String> humanitarianAidActs = Arrays.asList(
            "TAC_activity_1", 
            "crazy funding 1", 
            "date-filters-activity", 
            "Activity with planned disbursements", 
            "TAC_activity_2", 
            "pledged 2"
        );
    
    @Override
    protected NiReportExecutor getNiExecutor(List<String> activityNames) {
        return getDbExecutor(activityNames);
    }

    @BeforeClass
    public static void setUp() {
        StandaloneAMPInitializer.initialize();
    }
    
    @Test
    public void testSavedModeOfPaymentFilter() {
        NiReportModel cor = new NiReportModel("simple-filtered-by-mode-of-payment")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 7))",
                        "(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Mode of Payment: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(AMP ID: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 3, colSpan: 2));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 5, colSpan: 2))",
                        "(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 2))",
                        "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                        .withContents("Project Title", "", "Mode of Payment", "", "AMP ID", "", "Funding-2013-Actual Commitments", "666 777", "Funding-2013-Actual Disbursements", "0", "Totals-Actual Commitments", "666 777", "Totals-Actual Disbursements", "0")
                        .withChildren(
                                new ReportAreaForTests(new AreaOwner(28), "Project Title", "ptc activity 1", "Mode of Payment", "Reimbursable", "AMP ID", "8721135", "Funding-2013-Actual Commitments", "666 777", "Totals-Actual Commitments", "666 777"),
                                new ReportAreaForTests(new AreaOwner(73), "Project Title", "activity with directed MTEFs", "Mode of Payment", "Non-Cash", "AMP ID", "87211372")      ));

        runNiTestCase(cor, spec("simple-filtered-by-mode-of-payment"), acts);
    }
    
    @Test
    public void testSavedModeOfPaymentFilterByRegion() {
        NiReportModel cor = new NiReportModel("simple-filtered-by-mode-of-payment-by-region")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 12))",
                        "(Administrative Level 1: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Mode of Payment: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1));(AMP ID: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 3, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 4, colSpan: 6));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 10, colSpan: 2))",
                        "(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 2));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 6, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 8, colSpan: 2))",
                        "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1))"))
                    .withWarnings(Arrays.asList())
                    .withBody(      new ReportAreaForTests(null)
                      .withContents("Administrative Level 1", "", "Project Title", "", "Mode of Payment", "", "AMP ID", "", "Funding-2013-Actual Commitments", "1 567 753", "Funding-2013-Actual Disbursements", "721 956", "Funding-2014-Actual Commitments", "111 632,14", "Funding-2014-Actual Disbursements", "75 000", "Funding-2015-Actual Commitments", "1 222 386,84", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "2 901 771,98", "Totals-Actual Disbursements", "796 956")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner("Administrative Level 1", "Anenii Noi County"))
                        .withContents("Project Title", "", "Mode of Payment", "", "AMP ID", "", "Funding-2013-Actual Commitments", "778 110", "Funding-2013-Actual Disbursements", "555 111", "Funding-2014-Actual Commitments", "37 500", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "815 610", "Totals-Actual Disbursements", "555 111", "Administrative Level 1", "Anenii Noi County")
                        .withChildren(
                          new ReportAreaForTests(new AreaOwner(28), "Project Title", "ptc activity 1", "Mode of Payment", "Reimbursable", "AMP ID", "8721135", "Funding-2013-Actual Commitments", "666 777", "Totals-Actual Commitments", "666 777"),
                          new ReportAreaForTests(new AreaOwner(30), "Project Title", "SSC Project 1", "Mode of Payment", "Direct payment", "AMP ID", "8721137", "Funding-2013-Actual Commitments", "111 333", "Funding-2013-Actual Disbursements", "555 111", "Totals-Actual Commitments", "111 333", "Totals-Actual Disbursements", "555 111"),
                          new ReportAreaForTests(new AreaOwner(40), "Project Title", "SubNational no percentages", "Mode of Payment", "Direct payment", "AMP ID", "87211340", "Funding-2014-Actual Commitments", "37 500", "Totals-Actual Commitments", "37 500")        ),
                        new ReportAreaForTests(new AreaOwner("Administrative Level 1", "Balti County"))
                        .withContents("Project Title", "", "Mode of Payment", "", "AMP ID", "", "Funding-2013-Actual Commitments", "222 222", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "37 500", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "266 400", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "526 122", "Totals-Actual Disbursements", "0", "Administrative Level 1", "Balti County")
                        .withChildren(
                          new ReportAreaForTests(new AreaOwner(32), "Project Title", "crazy funding 1", "Mode of Payment", "Direct payment", "AMP ID", "87211332", "Funding-2013-Actual Commitments", "222 222", "Totals-Actual Commitments", "222 222"),
                          new ReportAreaForTests(new AreaOwner(40), "Project Title", "SubNational no percentages", "Mode of Payment", "Direct payment", "AMP ID", "87211340", "Funding-2014-Actual Commitments", "37 500", "Totals-Actual Commitments", "37 500"),
                          new ReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms", "Mode of Payment", "Direct payment", "AMP ID", "87211370", "Funding-2015-Actual Commitments", "266 400", "Totals-Actual Commitments", "266 400")        ),
                        new ReportAreaForTests(new AreaOwner("Administrative Level 1", "Chisinau City")).withContents("Project Title", "", "Mode of Payment", "", "AMP ID", "", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "123 456", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "123 456", "Totals-Actual Disbursements", "0", "Administrative Level 1", "Chisinau City")
                        .withChildren(
                          new ReportAreaForTests(new AreaOwner(73), "Project Title", "activity with directed MTEFs", "Mode of Payment", "Direct payment, Non-Cash", "AMP ID", "87211372", "Funding-2015-Actual Commitments", "123 456", "Totals-Actual Commitments", "123 456")        ),
                        new ReportAreaForTests(new AreaOwner("Administrative Level 1", "Chisinau County")).withContents("Project Title", "", "Mode of Payment", "", "AMP ID", "", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "35 000", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "75 000", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "0", "Totals-Actual Disbursements", "110 000", "Administrative Level 1", "Chisinau County")
                        .withChildren(
                          new ReportAreaForTests(new AreaOwner(76), "Project Title", "activity with pipeline MTEFs and act. disb", "Mode of Payment", "Direct payment", "AMP ID", "87211374", "Funding-2013-Actual Disbursements", "35 000", "Funding-2014-Actual Disbursements", "75 000", "Totals-Actual Disbursements", "110 000")        ),
                        new ReportAreaForTests(new AreaOwner("Administrative Level 1", "Drochia County")).withContents("Project Title", "", "Mode of Payment", "", "AMP ID", "", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "621 600", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "621 600", "Totals-Actual Disbursements", "0", "Administrative Level 1", "Drochia County")
                        .withChildren(
                          new ReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms", "Mode of Payment", "Direct payment", "AMP ID", "87211370", "Funding-2015-Actual Commitments", "621 600", "Totals-Actual Commitments", "621 600")        ),
                        new ReportAreaForTests(new AreaOwner("Administrative Level 1", "Edinet County")).withContents("Project Title", "", "Mode of Payment", "", "AMP ID", "", "Funding-2013-Actual Commitments", "567 421", "Funding-2013-Actual Disbursements", "131 845", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "567 421", "Totals-Actual Disbursements", "131 845", "Administrative Level 1", "Edinet County")
                        .withChildren(
                          new ReportAreaForTests(new AreaOwner(31), "Project Title", "SSC Project 2", "Mode of Payment", "Direct payment", "AMP ID", "87211311", "Funding-2013-Actual Commitments", "567 421", "Funding-2013-Actual Disbursements", "131 845", "Totals-Actual Commitments", "567 421", "Totals-Actual Disbursements", "131 845")        ),
                        new ReportAreaForTests(new AreaOwner("Administrative Level 1", "Administrative Level 1: Undefined"))
                        .withContents("Project Title", "", "Mode of Payment", "", "AMP ID", "", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "36 632,14", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "210 930,84", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "247 562,98", "Totals-Actual Disbursements", "0", "Administrative Level 1", "Administrative Level 1: Undefined")
                        .withChildren(
                          new ReportAreaForTests(new AreaOwner(71), "Project Title", "activity_with_disaster_response", "Mode of Payment", "Direct payment", "AMP ID", "87211371", "Funding-2014-Actual Commitments", "33 000", "Funding-2015-Actual Commitments", "117 000", "Totals-Actual Commitments", "150 000"),
                          new ReportAreaForTests(new AreaOwner(79), "Project Title", "with weird currencies", "Mode of Payment", "Direct payment", "AMP ID", "87211379", "Funding-2014-Actual Commitments", "3 632,14", "Funding-2015-Actual Commitments", "93 930,84", "Totals-Actual Commitments", "97 562,98"))));

        runNiTestCase(cor, spec("simple-filtered-by-mode-of-payment-by-region"), acts);
    }
    
    @Test
    public void testSavedDonorGroupFilter() {
        NiReportModel cor = new NiReportModel("filtered-by-donor-group")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 16))",
                        "(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Administrative Level 1: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Primary Sector: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1));(Donor Agency: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 3, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 4, colSpan: 10));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 14, colSpan: 2))",
                        "(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 2));(2011: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 6, colSpan: 2));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 8, colSpan: 2));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 10, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 12, colSpan: 2))",
                        "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 14, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 15, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                        .withContents("Project Title", "", "Administrative Level 1", "", "Primary Sector", "", "Donor Agency", "", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "123 321", "Funding-2011-Actual Commitments", "213 231", "Funding-2011-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "890 000", "Funding-2013-Actual Disbursements", "145 000", "Funding-2014-Actual Commitments", "82 100", "Funding-2014-Actual Disbursements", "75 200", "Funding-2015-Actual Commitments", "45 700", "Funding-2015-Actual Disbursements", "500", "Totals-Actual Commitments", "1 231 031", "Totals-Actual Disbursements", "344 021")
                        .withChildren(
                                new ReportAreaForTests(new AreaOwner(12), "Project Title", "TAC_activity_1", "Administrative Level 1", "Dubasari County", "Primary Sector", "112 - BASIC EDUCATION", "Donor Agency", "World Bank", "Funding-2010-Actual Disbursements", "123 321", "Funding-2011-Actual Commitments", "213 231", "Totals-Actual Commitments", "213 231", "Totals-Actual Disbursements", "123 321"),
                                new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Administrative Level 1", "Anenii Noi County", "Primary Sector", "110 - EDUCATION", "Donor Agency", "Norway", "Funding-2013-Actual Disbursements", "110 000", "Totals-Actual Disbursements", "110 000"),
                                new ReportAreaForTests(new AreaOwner(25), "Project Title", "mtef activity 1", "Administrative Level 1", "", "Primary Sector", "110 - EDUCATION", "Donor Agency", "UNDP"),
                                new ReportAreaForTests(new AreaOwner(36), "Project Title", "Activity With Zones and Percentages", "Administrative Level 1", "Anenii Noi County, Balti County", "Primary Sector", "110 - EDUCATION, 120 - HEALTH", "Donor Agency", "Norway", "Funding-2013-Actual Commitments", "890 000", "Totals-Actual Commitments", "890 000"),
                                new ReportAreaForTests(new AreaOwner(43), "Project Title", "Activity with primary_tertiary_program", "Administrative Level 1", "", "Primary Sector", "110 - EDUCATION", "Donor Agency", "UNDP", "Funding-2014-Actual Commitments", "50 000", "Totals-Actual Commitments", "50 000"),
                                new ReportAreaForTests(new AreaOwner(44), "Project Title", "activity with primary_program", "Administrative Level 1", "", "Primary Sector", "110 - EDUCATION", "Donor Agency", "World Bank", "Funding-2014-Actual Commitments", "32 000", "Totals-Actual Commitments", "32 000"),
                                new ReportAreaForTests(new AreaOwner(63), "Project Title", "activity with funded components", "Administrative Level 1", "", "Primary Sector", "110 - EDUCATION", "Donor Agency", "UNDP, World Bank", "Funding-2014-Actual Commitments", "100", "Totals-Actual Commitments", "100"),
                                new ReportAreaForTests(new AreaOwner(64), "Project Title", "Unvalidated activity", "Administrative Level 1", "", "Primary Sector", "110 - EDUCATION", "Donor Agency", "UNDP", "Funding-2015-Actual Commitments", "45 000", "Totals-Actual Commitments", "45 000"),
                                new ReportAreaForTests(new AreaOwner(66), "Project Title", "Activity 2 with multiple agreements", "Administrative Level 1", "Chisinau County", "Primary Sector", "110 - EDUCATION", "Donor Agency", "UNDP", "Funding-2015-Actual Commitments", "700", "Totals-Actual Commitments", "700"),
                                new ReportAreaForTests(new AreaOwner(69), "Project Title", "Activity with planned disbursements", "Administrative Level 1", "", "Primary Sector", "112 - BASIC EDUCATION", "Donor Agency", "Norway", "Funding-2014-Actual Disbursements", "200", "Funding-2015-Actual Disbursements", "500", "Totals-Actual Disbursements", "700"),
                                new ReportAreaForTests(new AreaOwner(76), "Project Title", "activity with pipeline MTEFs and act. disb", "Administrative Level 1", "Chisinau County", "Primary Sector", "110 - EDUCATION", "Donor Agency", "Norway, UNDP", "Funding-2013-Actual Disbursements", "35 000", "Funding-2014-Actual Disbursements", "75 000", "Totals-Actual Disbursements", "110 000"),
                                new ReportAreaForTests(new AreaOwner(77), "Project Title", "execution rate activity", "Administrative Level 1", "Chisinau City, Dubasari County", "Primary Sector", "110 - EDUCATION", "Donor Agency", "UNDP"),
                                new ReportAreaForTests(new AreaOwner(78), "Project Title", "activity with many MTEFs", "Administrative Level 1", "Drochia County", "Primary Sector", "110 - EDUCATION", "Donor Agency", "Norway"),
                                new ReportAreaForTests(new AreaOwner(79), "Project Title", "with weird currencies", "Administrative Level 1", "", "Primary Sector", "110 - EDUCATION, 112 - BASIC EDUCATION", "Donor Agency", "Norway")      ));

        runNiTestCase(cor, spec("filtered-by-donor-group"), acts);
    }
    
    @Test
    public void testFundingFlowFilterByBENF() {
        NiReportModel cor = new NiReportModel("flat filter flow by BENF")
            .withHeaders(Arrays.asList(
                    "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 5, colStart: 0, colSpan: 20))",
                    "(Project Title: (startRow: 1, rowSpan: 4, totalRowSpan: 4, colStart: 0, colSpan: 1));(MTEF 2011: (startRow: 1, rowSpan: 4, totalRowSpan: 4, colStart: 1, colSpan: 1));(MTEF 2012: (startRow: 1, rowSpan: 4, totalRowSpan: 4, colStart: 2, colSpan: 1));(Real MTEF 2011: (startRow: 1, rowSpan: 3, totalRowSpan: 4, colStart: 3, colSpan: 1));(Real MTEF 2012: (startRow: 1, rowSpan: 3, totalRowSpan: 4, colStart: 4, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 4, colStart: 5, colSpan: 10));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 4, colStart: 15, colSpan: 5))",
                    "(2009: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 5, colSpan: 2));(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 7, colSpan: 2));(2012: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 9, colSpan: 2));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 11, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 13, colSpan: 2))",
                    "(Actual Commitments: (startRow: 3, rowSpan: 2, totalRowSpan: 2, colStart: 5, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 2, totalRowSpan: 2, colStart: 6, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 2, totalRowSpan: 2, colStart: 7, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 2, totalRowSpan: 2, colStart: 8, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 2, totalRowSpan: 2, colStart: 9, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 2, totalRowSpan: 2, colStart: 10, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 2, totalRowSpan: 2, colStart: 11, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 2, totalRowSpan: 2, colStart: 12, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 2, totalRowSpan: 2, colStart: 13, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 2, totalRowSpan: 2, colStart: 14, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 2, totalRowSpan: 2, colStart: 15, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 2, totalRowSpan: 2, colStart: 16, colSpan: 1));(MTEF: (startRow: 3, rowSpan: 2, totalRowSpan: 2, colStart: 17, colSpan: 1));(Real MTEF: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 18, colSpan: 2))",
                    "(EXEC-BENF: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(IMPL-BENF: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(EXEC-BENF: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 18, colSpan: 1));(IMPL-BENF: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 19, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                  .withContents("Project Title", "", "MTEF 2011", "0", "MTEF 2012", "0", "Real MTEF 2011-EXEC-BENF", "50,000", "Real MTEF 2012-IMPL-BENF", "43,000", "Funding-2009-Actual Commitments", "100,000", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "60,000", "Funding-2012-Actual Commitments", "25,000", "Funding-2012-Actual Disbursements", "12,000", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "272,500", "Funding-2015-Actual Commitments", "123,456", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "248,456", "Totals-Actual Disbursements", "344,500", "Totals-MTEF", "0", "Totals-Real MTEF-EXEC-BENF", "50,000", "Totals-Real MTEF-IMPL-BENF", "43,000")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Funding-2013-Actual Disbursements", "272,500", "Totals-Actual Disbursements", "272,500"),
                    new ReportAreaForTests(new AreaOwner(26), "Project Title", "date-filters-activity", "Funding-2009-Actual Commitments", "100,000", "Funding-2010-Actual Disbursements", "60,000", "Funding-2012-Actual Commitments", "25,000", "Funding-2012-Actual Disbursements", "12,000", "Totals-Actual Commitments", "125,000", "Totals-Actual Disbursements", "72,000"),
                    new ReportAreaForTests(new AreaOwner(73), "Project Title", "activity with directed MTEFs", "Real MTEF 2011-EXEC-BENF", "50,000", "Real MTEF 2012-IMPL-BENF", "43,000", "Funding-2015-Actual Commitments", "123,456", "Totals-Actual Commitments", "123,456", "Totals-Real MTEF-EXEC-BENF", "50,000", "Totals-Real MTEF-IMPL-BENF", "43,000")      ));

        
        ReportSpecificationImpl spec = buildSpecForFiltering("flat filter flow by BENF", 
            Arrays.asList(ColumnConstants.PROJECT_TITLE, "MTEF 2011", "MTEF 2012", "Real MTEF 2011", "Real MTEF 2012"), 
            null, 
            ColumnConstants.BENEFICIARY_AGENCY, Arrays.asList(21696l, 21702l), true); // USAID, Water Foundation
        
        runNiTestCase(cor, spec, acts);
    }
    
    @Test
    public void testFundingFlowFilterByNotBENF() {
        NiReportModel cor = new NiReportModel("flat filter flow by not BENF")
            .withHeaders(Arrays.asList(
                    "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 8))",
                    "(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(MTEF 2011: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(MTEF 2012: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 3, colSpan: 2));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 5, colSpan: 3))",
                    "(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 2))",
                    "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(MTEF: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null).withContents("Project Title", "", "MTEF 2011", "0", "MTEF 2012", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "272,500", "Totals-Actual Commitments", "0", "Totals-Actual Disbursements", "272,500", "Totals-MTEF", "0")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Funding-2013-Actual Disbursements", "272,500", "Totals-Actual Disbursements", "272,500")      ));
        
        ReportSpecificationImpl spec = buildSpecForFiltering("flat filter flow by not BENF", 
            Arrays.asList(ColumnConstants.PROJECT_TITLE, "MTEF 2011", "MTEF 2012", "Real MTEF 2011", "Real MTEF 2012"), 
            null, 
            ColumnConstants.BENEFICIARY_AGENCY, Arrays.asList(ColumnReportData.UNALLOCATED_ID, 21696l, 21702l), false); // USAID, Water Foundation
        
        runNiTestCase(cor, spec, acts);
    }
    
    @Test
    public void testFundingFlowFilterByPrimarySectorStored() {
        NiReportModel cor = new NiReportModel("AMP-22322-directed-mtefs-filter-by-110-education")
            .withHeaders(Arrays.asList(
                    "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 5, colStart: 0, colSpan: 22))",
                    "(Project Title: (startRow: 1, rowSpan: 4, totalRowSpan: 4, colStart: 0, colSpan: 1));(MTEF 2011: (startRow: 1, rowSpan: 4, totalRowSpan: 4, colStart: 1, colSpan: 1));(MTEF 2012: (startRow: 1, rowSpan: 4, totalRowSpan: 4, colStart: 2, colSpan: 1));(Real MTEF 2011: (startRow: 1, rowSpan: 3, totalRowSpan: 4, colStart: 3, colSpan: 2));(Real MTEF 2012: (startRow: 1, rowSpan: 3, totalRowSpan: 4, colStart: 5, colSpan: 2));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 4, colStart: 7, colSpan: 8));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 4, colStart: 15, colSpan: 7))",
                    "(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 7, colSpan: 2));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 9, colSpan: 2));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 11, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 13, colSpan: 2))",
                    "(Actual Commitments: (startRow: 3, rowSpan: 2, totalRowSpan: 2, colStart: 7, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 2, totalRowSpan: 2, colStart: 8, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 2, totalRowSpan: 2, colStart: 9, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 2, totalRowSpan: 2, colStart: 10, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 2, totalRowSpan: 2, colStart: 11, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 2, totalRowSpan: 2, colStart: 12, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 2, totalRowSpan: 2, colStart: 13, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 2, totalRowSpan: 2, colStart: 14, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 2, totalRowSpan: 2, colStart: 15, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 2, totalRowSpan: 2, colStart: 16, colSpan: 1));(MTEF: (startRow: 3, rowSpan: 2, totalRowSpan: 2, colStart: 17, colSpan: 1));(Real MTEF: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 18, colSpan: 4))",
                    "(EXEC-BENF: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(IMPL-EXEC: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(EXEC-EXEC: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(IMPL-BENF: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(EXEC-BENF: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 18, colSpan: 1));(EXEC-EXEC: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 19, colSpan: 1));(IMPL-BENF: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 20, colSpan: 1));(IMPL-EXEC: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 21, colSpan: 1))"))
                .withWarnings(Arrays.asList(
                    "-1: [entityId: -1, message: measure \"Real MTEFs\" not supported in NiReports]"))
                .withBody(      new ReportAreaForTests(null)
                  .withContents("Project Title", "", "MTEF 2011", "1 438 011", "MTEF 2012", "211 000", "Real MTEF 2011-EXEC-BENF", "50 000", "Real MTEF 2011-IMPL-EXEC", "110 500", "Real MTEF 2012-EXEC-EXEC", "22 000", "Real MTEF 2012-IMPL-BENF", "43 000", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "143 777", "Funding-2013-Actual Commitments", "570 000", "Funding-2013-Actual Disbursements", "580 000", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "75 000", "Funding-2015-Actual Commitments", "656 256", "Funding-2015-Actual Disbursements", "80 000", "Totals-Actual Commitments", "1 226 256", "Totals-Actual Disbursements", "878 777", "Totals-MTEF", "1 649 011", "Totals-Real MTEF-EXEC-BENF", "50 000", "Totals-Real MTEF-EXEC-EXEC", "22 000", "Totals-Real MTEF-IMPL-BENF", "43 000", "Totals-Real MTEF-IMPL-EXEC", "110 500")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(18), "Project Title", "Test MTEF directed", "MTEF 2011", "150 000", "MTEF 2012", "65 000", "Funding-2010-Actual Disbursements", "143 777", "Totals-Actual Disbursements", "143 777", "Totals-MTEF", "215 000"),
                    new ReportAreaForTests(new AreaOwner(19), "Project Title", "Pure MTEF Project", "MTEF 2011", "33 888", "Totals-MTEF", "33 888"),
                    new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Funding-2013-Actual Disbursements", "545 000", "Totals-Actual Disbursements", "545 000"),
                    new ReportAreaForTests(new AreaOwner(25), "Project Title", "mtef activity 1", "MTEF 2011", "789 123", "Totals-MTEF", "789 123"),
                    new ReportAreaForTests(new AreaOwner(27), "Project Title", "mtef activity 2"),
                    new ReportAreaForTests(new AreaOwner(33), "Project Title", "Activity with Zones", "Funding-2013-Actual Commitments", "570 000", "Totals-Actual Commitments", "570 000"),
                    new ReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms", "MTEF 2011", "420 000", "MTEF 2012", "90 000", "Funding-2015-Actual Commitments", "532 800", "Totals-Actual Commitments", "532 800", "Totals-MTEF", "510 000"),
                    new ReportAreaForTests(new AreaOwner(73), "Project Title", "activity with directed MTEFs", "Real MTEF 2011-EXEC-BENF", "50 000", "Real MTEF 2011-IMPL-EXEC", "110 500", "Real MTEF 2012-EXEC-EXEC", "22 000", "Real MTEF 2012-IMPL-BENF", "43 000", "Funding-2015-Actual Commitments", "123 456", "Totals-Actual Commitments", "123 456", "Totals-Real MTEF-EXEC-BENF", "50 000", "Totals-Real MTEF-EXEC-EXEC", "22 000", "Totals-Real MTEF-IMPL-BENF", "43 000", "Totals-Real MTEF-IMPL-EXEC", "110 500"),
                    new ReportAreaForTests(new AreaOwner(76), "Project Title", "activity with pipeline MTEFs and act. disb", "Funding-2013-Actual Disbursements", "35 000", "Funding-2014-Actual Disbursements", "75 000", "Totals-Actual Disbursements", "110 000"),
                    new ReportAreaForTests(new AreaOwner(78), "Project Title", "activity with many MTEFs", "MTEF 2011", "45 000", "MTEF 2012", "56 000", "Funding-2015-Actual Disbursements", "80 000", "Totals-Actual Disbursements", "80 000", "Totals-MTEF", "101 000")      ));
        
        runNiTestCase(cor, spec("AMP-22322-directed-mtefs-filter-by-110-education"), flowsActs);
    }

    @Test
    public void testSimpleDoubleHierBySectorAndSubsector() {
        NiReportModel cor = new NiReportModel("double-hierarchy-by-sector-by-subsector")
            .withHeaders(Arrays.asList(
                    "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 5))",
                    "(Primary Sector: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Primary Sector Sub-Sector: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 3, colSpan: 1));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 4, colSpan: 1))",
                    "(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 1))",
                    "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                  .withContents("Primary Sector", "", "Primary Sector Sub-Sector", "", "Project Title", "", "Funding-2015-Actual Commitments", "456 789", "Totals-Actual Commitments", "456 789")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner("Primary Sector", "110 - EDUCATION")).withContents("Primary Sector Sub-Sector", "", "Project Title", "", "Funding-2015-Actual Commitments", "228 394,5", "Totals-Actual Commitments", "228 394,5", "Primary Sector", "110 - EDUCATION")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner("Primary Sector Sub-Sector", "111 - Education, level unspecified")).withContents("Project Title", "", "Funding-2015-Actual Commitments", "228 394,5", "Totals-Actual Commitments", "228 394,5", "Primary Sector Sub-Sector", "111 - Education, level unspecified")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Funding-2015-Actual Commitments", "228 394,5", "Totals-Actual Commitments", "228 394,5")          )        ),
                    new ReportAreaForTests(new AreaOwner("Primary Sector", "112 - BASIC EDUCATION"))
                    .withContents("Primary Sector Sub-Sector", "", "Project Title", "", "Funding-2015-Actual Commitments", "228 394,5", "Totals-Actual Commitments", "228 394,5", "Primary Sector", "112 - BASIC EDUCATION")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner("Primary Sector Sub-Sector", "11230 - Basic life skills for youth and adults")).withContents("Project Title", "", "Funding-2015-Actual Commitments", "105 061,47", "Totals-Actual Commitments", "105 061,47", "Primary Sector Sub-Sector", "11230 - Basic life skills for youth and adults")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Funding-2015-Actual Commitments", "105 061,47", "Totals-Actual Commitments", "105 061,47")          ),
                      new ReportAreaForTests(new AreaOwner("Primary Sector Sub-Sector", "11240 - Early childhood education")).withContents("Project Title", "", "Funding-2015-Actual Commitments", "123 333,03", "Totals-Actual Commitments", "123 333,03", "Primary Sector Sub-Sector", "11240 - Early childhood education")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Funding-2015-Actual Commitments", "123 333,03", "Totals-Actual Commitments", "123 333,03")          )        )      ));

        runNiTestCase(cor, spec("double-hierarchy-by-sector-by-subsector"), Arrays.asList("activity 1 with agreement"));
    }
    
    @Test
    public void testSimpleDoubleHierBySectorAndSubsectorFilteredBySector() {
        NiReportModel cor = new NiReportModel("double-hierarchy-by-sector-by-subsector-filtered-by-sector")
            .withHeaders(Arrays.asList(
                    "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 5))",
                    "(Primary Sector: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Primary Sector Sub-Sector: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 3, colSpan: 1));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 4, colSpan: 1))",
                    "(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 1))",
                    "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null).withContents("Primary Sector", "", "Primary Sector Sub-Sector", "", "Project Title", "", "Funding-2015-Actual Commitments", "228 394,5", "Totals-Actual Commitments", "228 394,5")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner("Primary Sector", "112 - BASIC EDUCATION"))
                    .withContents("Primary Sector Sub-Sector", "", "Project Title", "", "Funding-2015-Actual Commitments", "228 394,5", "Totals-Actual Commitments", "228 394,5", "Primary Sector", "112 - BASIC EDUCATION")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner("Primary Sector Sub-Sector", "11230 - Basic life skills for youth and adults")).withContents("Project Title", "", "Funding-2015-Actual Commitments", "105 061,47", "Totals-Actual Commitments", "105 061,47", "Primary Sector Sub-Sector", "11230 - Basic life skills for youth and adults")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Funding-2015-Actual Commitments", "105 061,47", "Totals-Actual Commitments", "105 061,47")          ),
                      new ReportAreaForTests(new AreaOwner("Primary Sector Sub-Sector", "11240 - Early childhood education")).withContents("Project Title", "", "Funding-2015-Actual Commitments", "123 333,03", "Totals-Actual Commitments", "123 333,03", "Primary Sector Sub-Sector", "11240 - Early childhood education")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Funding-2015-Actual Commitments", "123 333,03", "Totals-Actual Commitments", "123 333,03")          )        )      ));

        runNiTestCase(cor, spec("double-hierarchy-by-sector-by-subsector-filtered-by-sector"), Arrays.asList("activity 1 with agreement"));
    }

    @Test
    public void testSimpleDoubleHierBySectorAndSubsectorFilteredBySubSector() {
        NiReportModel cor = new NiReportModel("double-hierarchy-by-sector-by-subsector-filtered-by-subsector")
            .withHeaders(Arrays.asList(
                    "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 5))",
                    "(Primary Sector: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Primary Sector Sub-Sector: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 3, colSpan: 1));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 4, colSpan: 1))",
                    "(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 1))",
                    "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null).withContents("Primary Sector", "", "Primary Sector Sub-Sector", "", "Project Title", "", "Funding-2015-Actual Commitments", "123 333,03", "Totals-Actual Commitments", "123 333,03")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner("Primary Sector", "112 - BASIC EDUCATION")).withContents("Primary Sector Sub-Sector", "", "Project Title", "", "Funding-2015-Actual Commitments", "123 333,03", "Totals-Actual Commitments", "123 333,03", "Primary Sector", "112 - BASIC EDUCATION")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner("Primary Sector Sub-Sector", "11240 - Early childhood education")).withContents("Project Title", "", "Funding-2015-Actual Commitments", "123 333,03", "Totals-Actual Commitments", "123 333,03", "Primary Sector Sub-Sector", "11240 - Early childhood education")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Funding-2015-Actual Commitments", "123 333,03", "Totals-Actual Commitments", "123 333,03")          )        )      ));

        runNiTestCase(cor, spec("double-hierarchy-by-sector-by-subsector-filtered-by-subsector"), Arrays.asList("activity 1 with agreement"));
    }

    @Test
    public void testSimpleDoubleHierBySectorAndSubsectorFilteredFictivelyBySubSector() {
        NiReportModel cor = new NiReportModel("double-hierarchy-by-sector-by-subsector-filtered-fictively-by-subsector")
            .withHeaders(Arrays.asList(
                    "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 5))",
                    "(Primary Sector: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Primary Sector Sub-Sector: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 3, colSpan: 1));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 4, colSpan: 1))",
                    "(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 1))",
                    "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                  .withContents("Primary Sector", "", "Primary Sector Sub-Sector", "", "Project Title", "", "Funding-2015-Actual Commitments", "456 789", "Totals-Actual Commitments", "456 789")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner("Primary Sector", "110 - EDUCATION")).withContents("Primary Sector Sub-Sector", "", "Project Title", "", "Funding-2015-Actual Commitments", "228 394,5", "Totals-Actual Commitments", "228 394,5", "Primary Sector", "110 - EDUCATION")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner("Primary Sector Sub-Sector", "111 - Education, level unspecified")).withContents("Project Title", "", "Funding-2015-Actual Commitments", "228 394,5", "Totals-Actual Commitments", "228 394,5", "Primary Sector Sub-Sector", "111 - Education, level unspecified")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Funding-2015-Actual Commitments", "228 394,5", "Totals-Actual Commitments", "228 394,5")          )        ),
                    new ReportAreaForTests(new AreaOwner("Primary Sector", "112 - BASIC EDUCATION"))
                    .withContents("Primary Sector Sub-Sector", "", "Project Title", "", "Funding-2015-Actual Commitments", "228 394,5", "Totals-Actual Commitments", "228 394,5", "Primary Sector", "112 - BASIC EDUCATION")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner("Primary Sector Sub-Sector", "11230 - Basic life skills for youth and adults")).withContents("Project Title", "", "Funding-2015-Actual Commitments", "105 061,47", "Totals-Actual Commitments", "105 061,47", "Primary Sector Sub-Sector", "11230 - Basic life skills for youth and adults")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Funding-2015-Actual Commitments", "105 061,47", "Totals-Actual Commitments", "105 061,47")          ),
                      new ReportAreaForTests(new AreaOwner("Primary Sector Sub-Sector", "11240 - Early childhood education")).withContents("Project Title", "", "Funding-2015-Actual Commitments", "123 333,03", "Totals-Actual Commitments", "123 333,03", "Primary Sector Sub-Sector", "11240 - Early childhood education")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Funding-2015-Actual Commitments", "123 333,03", "Totals-Actual Commitments", "123 333,03")          )        )      ));

        runNiTestCase(cor, spec("double-hierarchy-by-sector-by-subsector-filtered-fictively-by-subsector"), Arrays.asList("activity 1 with agreement"));
    }

    @Test
    public void testFundingFlowUnfilteredByIa() {
        NiReportModel cor = new NiReportModel("AMP-22322-all-directed-entities-by-ia")
            .withHeaders(Arrays.asList(
                    "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 5, colStart: 0, colSpan: 22))",
                    "(Implementing Agency: (startRow: 1, rowSpan: 4, totalRowSpan: 4, colStart: 0, colSpan: 1));(Project Title: (startRow: 1, rowSpan: 4, totalRowSpan: 4, colStart: 1, colSpan: 1));(MTEF 2011: (startRow: 1, rowSpan: 4, totalRowSpan: 4, colStart: 2, colSpan: 1));(MTEF 2012: (startRow: 1, rowSpan: 4, totalRowSpan: 4, colStart: 3, colSpan: 1));(Real MTEF 2011: (startRow: 1, rowSpan: 3, totalRowSpan: 4, colStart: 4, colSpan: 1));(Real MTEF 2012: (startRow: 1, rowSpan: 3, totalRowSpan: 4, colStart: 5, colSpan: 1));(Executing Agency: (startRow: 1, rowSpan: 4, totalRowSpan: 4, colStart: 6, colSpan: 1));(Beneficiary Agency: (startRow: 1, rowSpan: 4, totalRowSpan: 4, colStart: 7, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 4, colStart: 8, colSpan: 6));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 4, colStart: 14, colSpan: 8))",
                    "(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 8, colSpan: 2));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 10, colSpan: 4))",
                    "(Actual Disbursements: (startRow: 3, rowSpan: 2, totalRowSpan: 2, colStart: 8, colSpan: 1));(Real Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 9, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 2, totalRowSpan: 2, colStart: 10, colSpan: 1));(Real Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 11, colSpan: 3));(Actual Disbursements: (startRow: 3, rowSpan: 2, totalRowSpan: 2, colStart: 14, colSpan: 1));(Real Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 15, colSpan: 4));(MTEF: (startRow: 3, rowSpan: 2, totalRowSpan: 2, colStart: 19, colSpan: 1));(Real MTEF: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 20, colSpan: 2))",
                    "(IMPL-EXEC: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(IMPL-BENF: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(DN-IMPL: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(EXEC-IMPL: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1));(IMPL-BENF: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1));(IMPL-EXEC: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1));(DN-IMPL: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 15, colSpan: 1));(EXEC-IMPL: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 16, colSpan: 1));(IMPL-BENF: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 17, colSpan: 1));(IMPL-EXEC: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 18, colSpan: 1));(IMPL-BENF: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 20, colSpan: 1));(IMPL-EXEC: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 21, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                  .withContents("Implementing Agency", "", "Project Title", "", "MTEF 2011", "150 000", "MTEF 2012", "65 000", "Real MTEF 2011-IMPL-EXEC", "110 500", "Real MTEF 2012-IMPL-BENF", "43 000", "Executing Agency", "", "Beneficiary Agency", "", "Funding-2010-Actual Disbursements", "143 777", "Funding-2010-Real Disbursements-DN-IMPL", "77 222", "Funding-2013-Actual Disbursements", "545 000", "Funding-2013-Real Disbursements-EXEC-IMPL", "100 000", "Funding-2013-Real Disbursements-IMPL-BENF", "15 000", "Funding-2013-Real Disbursements-IMPL-EXEC", "44 333", "Totals-Actual Disbursements", "688 777", "Totals-Real Disbursements-DN-IMPL", "77 222", "Totals-Real Disbursements-EXEC-IMPL", "100 000", "Totals-Real Disbursements-IMPL-BENF", "15 000", "Totals-Real Disbursements-IMPL-EXEC", "44 333", "Totals-MTEF", "215 000", "Totals-Real MTEF-IMPL-BENF", "43 000", "Totals-Real MTEF-IMPL-EXEC", "110 500")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner("Implementing Agency", "Finland", 21698)).withContents("Project Title", "", "MTEF 2011", "0", "MTEF 2012", "0", "Real MTEF 2011-IMPL-EXEC", "110 500", "Real MTEF 2012-IMPL-BENF", "43 000", "Executing Agency", "", "Beneficiary Agency", "", "Funding-2010-Actual Disbursements", "0", "Funding-2010-Real Disbursements-DN-IMPL", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2013-Real Disbursements-EXEC-IMPL", "0", "Funding-2013-Real Disbursements-IMPL-BENF", "0", "Funding-2013-Real Disbursements-IMPL-EXEC", "0", "Totals-Actual Disbursements", "0", "Totals-Real Disbursements-DN-IMPL", "0", "Totals-Real Disbursements-EXEC-IMPL", "0", "Totals-Real Disbursements-IMPL-BENF", "0", "Totals-Real Disbursements-IMPL-EXEC", "0", "Totals-MTEF", "0", "Totals-Real MTEF-IMPL-BENF", "43 000", "Totals-Real MTEF-IMPL-EXEC", "110 500", "Implementing Agency", "Finland")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner(73), "Project Title", "activity with directed MTEFs", "Real MTEF 2011-IMPL-EXEC", "110 500", "Real MTEF 2012-IMPL-BENF", "43 000", "Executing Agency", "Finland, Norway, USAID", "Beneficiary Agency", "USAID, Water Foundation", "Totals-Real MTEF-IMPL-BENF", "43 000", "Totals-Real MTEF-IMPL-EXEC", "110 500")        ),
                    new ReportAreaForTests(new AreaOwner("Implementing Agency", "Ministry of Economy", 21700)).withContents("Project Title", "", "MTEF 2011", "0", "MTEF 2012", "0", "Real MTEF 2011-IMPL-EXEC", "0", "Real MTEF 2012-IMPL-BENF", "0", "Executing Agency", "", "Beneficiary Agency", "", "Funding-2010-Actual Disbursements", "0", "Funding-2010-Real Disbursements-DN-IMPL", "0", "Funding-2013-Actual Disbursements", "272 500", "Funding-2013-Real Disbursements-EXEC-IMPL", "50 000", "Funding-2013-Real Disbursements-IMPL-BENF", "5 000", "Funding-2013-Real Disbursements-IMPL-EXEC", "0", "Totals-Actual Disbursements", "272 500", "Totals-Real Disbursements-DN-IMPL", "0", "Totals-Real Disbursements-EXEC-IMPL", "50 000", "Totals-Real Disbursements-IMPL-BENF", "5 000", "Totals-Real Disbursements-IMPL-EXEC", "0", "Totals-MTEF", "0", "Totals-Real MTEF-IMPL-BENF", "0", "Totals-Real MTEF-IMPL-EXEC", "0", "Implementing Agency", "Ministry of Economy")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Executing Agency", "UNDP, World Bank", "Beneficiary Agency", "Water Foundation, Water Org", "Funding-2013-Actual Disbursements", "272 500", "Funding-2013-Real Disbursements-EXEC-IMPL", "50 000", "Funding-2013-Real Disbursements-IMPL-BENF", "5 000", "Totals-Actual Disbursements", "272 500", "Totals-Real Disbursements-EXEC-IMPL", "50 000", "Totals-Real Disbursements-IMPL-BENF", "5 000")        ),
                    new ReportAreaForTests(new AreaOwner("Implementing Agency", "Ministry of Finance", 21699)).withContents("Project Title", "", "MTEF 2011", "0", "MTEF 2012", "0", "Real MTEF 2011-IMPL-EXEC", "0", "Real MTEF 2012-IMPL-BENF", "0", "Executing Agency", "", "Beneficiary Agency", "", "Funding-2010-Actual Disbursements", "0", "Funding-2010-Real Disbursements-DN-IMPL", "0", "Funding-2013-Actual Disbursements", "272 500", "Funding-2013-Real Disbursements-EXEC-IMPL", "50 000", "Funding-2013-Real Disbursements-IMPL-BENF", "10 000", "Funding-2013-Real Disbursements-IMPL-EXEC", "0", "Totals-Actual Disbursements", "272 500", "Totals-Real Disbursements-DN-IMPL", "0", "Totals-Real Disbursements-EXEC-IMPL", "50 000", "Totals-Real Disbursements-IMPL-BENF", "10 000", "Totals-Real Disbursements-IMPL-EXEC", "0", "Totals-MTEF", "0", "Totals-Real MTEF-IMPL-BENF", "0", "Totals-Real MTEF-IMPL-EXEC", "0", "Implementing Agency", "Ministry of Finance")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Executing Agency", "UNDP, World Bank", "Beneficiary Agency", "Water Foundation, Water Org", "Funding-2013-Actual Disbursements", "272 500", "Funding-2013-Real Disbursements-EXEC-IMPL", "50 000", "Funding-2013-Real Disbursements-IMPL-BENF", "10 000", "Totals-Actual Disbursements", "272 500", "Totals-Real Disbursements-EXEC-IMPL", "50 000", "Totals-Real Disbursements-IMPL-BENF", "10 000")        ),
                    new ReportAreaForTests(new AreaOwner("Implementing Agency", "USAID", 21696)).withContents("Project Title", "", "MTEF 2011", "150 000", "MTEF 2012", "65 000", "Real MTEF 2011-IMPL-EXEC", "0", "Real MTEF 2012-IMPL-BENF", "0", "Executing Agency", "", "Beneficiary Agency", "", "Funding-2010-Actual Disbursements", "143 777", "Funding-2010-Real Disbursements-DN-IMPL", "77 222", "Funding-2013-Actual Disbursements", "0", "Funding-2013-Real Disbursements-EXEC-IMPL", "0", "Funding-2013-Real Disbursements-IMPL-BENF", "0", "Funding-2013-Real Disbursements-IMPL-EXEC", "44 333", "Totals-Actual Disbursements", "143 777", "Totals-Real Disbursements-DN-IMPL", "77 222", "Totals-Real Disbursements-EXEC-IMPL", "0", "Totals-Real Disbursements-IMPL-BENF", "0", "Totals-Real Disbursements-IMPL-EXEC", "44 333", "Totals-MTEF", "215 000", "Totals-Real MTEF-IMPL-BENF", "0", "Totals-Real MTEF-IMPL-EXEC", "0", "Implementing Agency", "USAID")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner(18), "Project Title", "Test MTEF directed", "MTEF 2011", "150 000", "MTEF 2012", "65 000", "Executing Agency", "Water Foundation", "Beneficiary Agency", "", "Funding-2010-Actual Disbursements", "143 777", "Funding-2010-Real Disbursements-DN-IMPL", "77 222", "Funding-2013-Real Disbursements-IMPL-EXEC", "44 333", "Totals-Actual Disbursements", "143 777", "Totals-Real Disbursements-DN-IMPL", "77 222", "Totals-Real Disbursements-IMPL-EXEC", "44 333", "Totals-MTEF", "215 000")        )      ));

        runNiTestCase(cor, spec("AMP-22322-all-directed-entities-by-ia"), Arrays.asList("Eth Water", "activity with directed MTEFs", "Test MTEF directed"));
    }
    
    @Test
    public void testFundingFlowFilteredByIaFinlandMinEcon() {
        NiReportModel cor = new NiReportModel("AMP-22322-all-directed-entities-filtered-by-ia-finland-minecon")
            .withHeaders(Arrays.asList(
                    "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 5, colStart: 0, colSpan: 17))",
                    "(Project Title: (startRow: 1, rowSpan: 4, totalRowSpan: 4, colStart: 0, colSpan: 1));(MTEF 2011: (startRow: 1, rowSpan: 4, totalRowSpan: 4, colStart: 1, colSpan: 1));(MTEF 2012: (startRow: 1, rowSpan: 4, totalRowSpan: 4, colStart: 2, colSpan: 1));(Real MTEF 2011: (startRow: 1, rowSpan: 3, totalRowSpan: 4, colStart: 3, colSpan: 1));(Real MTEF 2012: (startRow: 1, rowSpan: 3, totalRowSpan: 4, colStart: 4, colSpan: 1));(Executing Agency: (startRow: 1, rowSpan: 4, totalRowSpan: 4, colStart: 5, colSpan: 1));(Implementing Agency: (startRow: 1, rowSpan: 4, totalRowSpan: 4, colStart: 6, colSpan: 1));(Beneficiary Agency: (startRow: 1, rowSpan: 4, totalRowSpan: 4, colStart: 7, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 4, colStart: 8, colSpan: 3));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 4, colStart: 11, colSpan: 6))",
                    "(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 8, colSpan: 3))",
                    "(Actual Disbursements: (startRow: 3, rowSpan: 2, totalRowSpan: 2, colStart: 8, colSpan: 1));(Real Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 9, colSpan: 2));(Actual Disbursements: (startRow: 3, rowSpan: 2, totalRowSpan: 2, colStart: 11, colSpan: 1));(Real Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 12, colSpan: 2));(MTEF: (startRow: 3, rowSpan: 2, totalRowSpan: 2, colStart: 14, colSpan: 1));(Real MTEF: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 15, colSpan: 2))",
                    "(IMPL-EXEC: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(IMPL-BENF: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(EXEC-IMPL: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(IMPL-BENF: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(EXEC-IMPL: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1));(IMPL-BENF: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1));(IMPL-BENF: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 15, colSpan: 1));(IMPL-EXEC: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 16, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                  .withContents("Project Title", "", "MTEF 2011", "0", "MTEF 2012", "0", "Real MTEF 2011-IMPL-EXEC", "110 500", "Real MTEF 2012-IMPL-BENF", "43 000", "Executing Agency", "", "Implementing Agency", "", "Beneficiary Agency", "", "Funding-2013-Actual Disbursements", "272 500", "Funding-2013-Real Disbursements-EXEC-IMPL", "50 000", "Funding-2013-Real Disbursements-IMPL-BENF", "5 000", "Totals-Actual Disbursements", "272 500", "Totals-Real Disbursements-EXEC-IMPL", "50 000", "Totals-Real Disbursements-IMPL-BENF", "5 000", "Totals-MTEF", "0", "Totals-Real MTEF-IMPL-BENF", "43 000", "Totals-Real MTEF-IMPL-EXEC", "110 500")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Executing Agency", "UNDP, World Bank", "Implementing Agency", "Ministry of Economy", "Beneficiary Agency", "Water Foundation, Water Org", "Funding-2013-Actual Disbursements", "272 500", "Funding-2013-Real Disbursements-EXEC-IMPL", "50 000", "Funding-2013-Real Disbursements-IMPL-BENF", "5 000", "Totals-Actual Disbursements", "272 500", "Totals-Real Disbursements-EXEC-IMPL", "50 000", "Totals-Real Disbursements-IMPL-BENF", "5 000"),
                    new ReportAreaForTests(new AreaOwner(73), "Project Title", "activity with directed MTEFs", "Real MTEF 2011-IMPL-EXEC", "110 500", "Real MTEF 2012-IMPL-BENF", "43 000", "Executing Agency", "Finland, Norway, USAID", "Implementing Agency", "Finland", "Beneficiary Agency", "USAID, Water Foundation", "Totals-Real MTEF-IMPL-BENF", "43 000", "Totals-Real MTEF-IMPL-EXEC", "110 500")      ));

        runNiTestCase(cor, spec("AMP-22322-all-directed-entities-filtered-by-ia-finland-minecon"), Arrays.asList("Eth Water", "activity with directed MTEFs", "Test MTEF directed"));
    }

    @Test
    public void testFundingFlowFilteredByIaMinFinMinEcon() {
        NiReportModel cor = new NiReportModel("AMP-22322-all-directed-entities-filtered-by-ia-minfin-minecon")
            .withHeaders(Arrays.asList(
                    "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 5, colStart: 0, colSpan: 13))",
                    "(Project Title: (startRow: 1, rowSpan: 4, totalRowSpan: 4, colStart: 0, colSpan: 1));(MTEF 2011: (startRow: 1, rowSpan: 4, totalRowSpan: 4, colStart: 1, colSpan: 1));(MTEF 2012: (startRow: 1, rowSpan: 4, totalRowSpan: 4, colStart: 2, colSpan: 1));(Executing Agency: (startRow: 1, rowSpan: 4, totalRowSpan: 4, colStart: 3, colSpan: 1));(Implementing Agency: (startRow: 1, rowSpan: 4, totalRowSpan: 4, colStart: 4, colSpan: 1));(Beneficiary Agency: (startRow: 1, rowSpan: 4, totalRowSpan: 4, colStart: 5, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 4, colStart: 6, colSpan: 3));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 4, colStart: 9, colSpan: 4))",
                    "(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 6, colSpan: 3))",
                    "(Actual Disbursements: (startRow: 3, rowSpan: 2, totalRowSpan: 2, colStart: 6, colSpan: 1));(Real Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 7, colSpan: 2));(Actual Disbursements: (startRow: 3, rowSpan: 2, totalRowSpan: 2, colStart: 9, colSpan: 1));(Real Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 10, colSpan: 2));(MTEF: (startRow: 3, rowSpan: 2, totalRowSpan: 2, colStart: 12, colSpan: 1))",
                    "(EXEC-IMPL: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(IMPL-BENF: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(EXEC-IMPL: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(IMPL-BENF: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null).withContents("Project Title", "", "MTEF 2011", "0", "MTEF 2012", "0", "Executing Agency", "", "Implementing Agency", "", "Beneficiary Agency", "", "Funding-2013-Actual Disbursements", "545 000", "Funding-2013-Real Disbursements-EXEC-IMPL", "100 000", "Funding-2013-Real Disbursements-IMPL-BENF", "15 000", "Totals-Actual Disbursements", "545 000", "Totals-Real Disbursements-EXEC-IMPL", "100 000", "Totals-Real Disbursements-IMPL-BENF", "15 000", "Totals-MTEF", "0")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Executing Agency", "UNDP, World Bank", "Implementing Agency", "Ministry of Economy, Ministry of Finance", "Beneficiary Agency", "Water Foundation, Water Org", "Funding-2013-Actual Disbursements", "545 000", "Funding-2013-Real Disbursements-EXEC-IMPL", "100 000", "Funding-2013-Real Disbursements-IMPL-BENF", "15 000", "Totals-Actual Disbursements", "545 000", "Totals-Real Disbursements-EXEC-IMPL", "100 000", "Totals-Real Disbursements-IMPL-BENF", "15 000")      ));

        runNiTestCase(cor, spec("AMP-22322-all-directed-entities-filtered-by-ia-minfin-minecon"), Arrays.asList("Eth Water", "activity with directed MTEFs", "Test MTEF directed"));
    }

    @Test
    public void testFundingFlowFilteredByIaMinFinMinUsaid() {
        NiReportModel cor = new NiReportModel("AMP-22322-all-directed-entities-filtered-by-ia-minfin-usaid")
            .withHeaders(Arrays.asList(
                    "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 5, colStart: 0, colSpan: 18))",
                    "(Project Title: (startRow: 1, rowSpan: 4, totalRowSpan: 4, colStart: 0, colSpan: 1));(MTEF 2011: (startRow: 1, rowSpan: 4, totalRowSpan: 4, colStart: 1, colSpan: 1));(MTEF 2012: (startRow: 1, rowSpan: 4, totalRowSpan: 4, colStart: 2, colSpan: 1));(Executing Agency: (startRow: 1, rowSpan: 4, totalRowSpan: 4, colStart: 3, colSpan: 1));(Implementing Agency: (startRow: 1, rowSpan: 4, totalRowSpan: 4, colStart: 4, colSpan: 1));(Beneficiary Agency: (startRow: 1, rowSpan: 4, totalRowSpan: 4, colStart: 5, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 4, colStart: 6, colSpan: 6));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 4, colStart: 12, colSpan: 6))",
                    "(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 6, colSpan: 2));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 8, colSpan: 4))",
                    "(Actual Disbursements: (startRow: 3, rowSpan: 2, totalRowSpan: 2, colStart: 6, colSpan: 1));(Real Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 7, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 2, totalRowSpan: 2, colStart: 8, colSpan: 1));(Real Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 9, colSpan: 3));(Actual Disbursements: (startRow: 3, rowSpan: 2, totalRowSpan: 2, colStart: 12, colSpan: 1));(Real Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 13, colSpan: 4));(MTEF: (startRow: 3, rowSpan: 2, totalRowSpan: 2, colStart: 17, colSpan: 1))",
                    "(DN-IMPL: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(EXEC-IMPL: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(IMPL-BENF: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(IMPL-EXEC: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1));(DN-IMPL: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1));(EXEC-IMPL: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 14, colSpan: 1));(IMPL-BENF: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 15, colSpan: 1));(IMPL-EXEC: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 16, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                  .withContents("Project Title", "", "MTEF 2011", "150 000", "MTEF 2012", "65 000", "Executing Agency", "", "Implementing Agency", "", "Beneficiary Agency", "", "Funding-2010-Actual Disbursements", "143 777", "Funding-2010-Real Disbursements-DN-IMPL", "77 222", "Funding-2013-Actual Disbursements", "272 500", "Funding-2013-Real Disbursements-EXEC-IMPL", "50 000", "Funding-2013-Real Disbursements-IMPL-BENF", "10 000", "Funding-2013-Real Disbursements-IMPL-EXEC", "44 333", "Totals-Actual Disbursements", "416 277", "Totals-Real Disbursements-DN-IMPL", "77 222", "Totals-Real Disbursements-EXEC-IMPL", "50 000", "Totals-Real Disbursements-IMPL-BENF", "10 000", "Totals-Real Disbursements-IMPL-EXEC", "44 333", "Totals-MTEF", "215 000")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(18), "Project Title", "Test MTEF directed", "MTEF 2011", "150 000", "MTEF 2012", "65 000", "Executing Agency", "Water Foundation", "Implementing Agency", "USAID", "Beneficiary Agency", "", "Funding-2010-Actual Disbursements", "143 777", "Funding-2010-Real Disbursements-DN-IMPL", "77 222", "Funding-2013-Real Disbursements-IMPL-EXEC", "44 333", "Totals-Actual Disbursements", "143 777", "Totals-Real Disbursements-DN-IMPL", "77 222", "Totals-Real Disbursements-IMPL-EXEC", "44 333", "Totals-MTEF", "215 000"),
                    new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Executing Agency", "UNDP, World Bank", "Implementing Agency", "Ministry of Finance", "Beneficiary Agency", "Water Foundation, Water Org", "Funding-2013-Actual Disbursements", "272 500", "Funding-2013-Real Disbursements-EXEC-IMPL", "50 000", "Funding-2013-Real Disbursements-IMPL-BENF", "10 000", "Totals-Actual Disbursements", "272 500", "Totals-Real Disbursements-EXEC-IMPL", "50 000", "Totals-Real Disbursements-IMPL-BENF", "10 000")      ));

        runNiTestCase(cor, spec("AMP-22322-all-directed-entities-filtered-by-ia-minfin-usaid"), Arrays.asList("Eth Water", "activity with directed MTEFs", "Test MTEF directed"));
    }
    
    @Test
    public void testPositiveByApprovalStatusNewUnvalidated() {
        NiReportModel cor = new NiReportModel("testPositiveByApprovalStatusNewUnvalidated")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 8))",
                        "(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Approval Status: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Draft: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1));(Filtered Approval Status: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 3, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 4, colSpan: 2));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 6, colSpan: 2))",
                        "(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 2))",
                        "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1))"))
                    .withWarnings(Arrays.asList())
                    .withBody(      new ReportAreaForTests(null).withContents("Project Title", "", "Approval Status", "", "Draft", "", "Filtered Approval Status", "", "Funding-2015-Actual Commitments", "45,000", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "45,000", "Totals-Actual Disbursements", "0")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(64), "Project Title", "Unvalidated activity", "Approval Status", "4", "Draft", "false", "Filtered Approval Status", "2", "Funding-2015-Actual Commitments", "45,000", "Totals-Actual Commitments", "45,000")      ));
        
        ReportSpecificationImpl spec = buildSpecForFiltering("testPositiveByApprovalStatusNewUnvalidated", 
            Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.APPROVAL_STATUS, ColumnConstants.DRAFT, ColumnConstants.VALIDATION_STATUS),
            null, 
            ColumnConstants.APPROVAL_STATUS, Arrays.asList(2l), true); // All New Unvalidated. See AmpARFilter.VALIDATION_STATUS
        
        runNiTestCase(cor, spec, acts);
    }
    
    @Test
    public void testNegativeByApprovalStatusNewUnvalidated() {
        NiReportModel cor = new NiReportModel("testNegativeByApprovalStatusNewUnvalidated")
            .withHeaders(Arrays.asList(
                    "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 22))",
                    "(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Approval Status: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Draft: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1));(Filtered Approval Status: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 3, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 4, colSpan: 16));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 20, colSpan: 2))",
                    "(2006: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 2));(2009: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 6, colSpan: 2));(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 8, colSpan: 2));(2011: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 10, colSpan: 2));(2012: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 12, colSpan: 2));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 14, colSpan: 2));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 16, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 18, colSpan: 2))",
                    "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 14, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 15, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 16, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 17, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 18, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 19, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 20, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 21, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                  .withContents("Project Title", "", "Approval Status", "", "Draft", "", "Filtered Approval Status", "", "Funding-2006-Actual Commitments", "96,840,58", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "100,000", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "780,311", "Funding-2011-Actual Commitments", "1,213,119", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "25,000", "Funding-2012-Actual Disbursements", "12,000", "Funding-2013-Actual Commitments", "7,842,086", "Funding-2013-Actual Disbursements", "1,266,956", "Funding-2014-Actual Commitments", "8,159,813,77", "Funding-2014-Actual Disbursements", "710,200", "Funding-2015-Actual Commitments", "1,926,831,84", "Funding-2015-Actual Disbursements", "437,335", "Totals-Actual Commitments", "19,363,691,19", "Totals-Actual Disbursements", "3,206,802")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(12), "Project Title", "TAC_activity_1", "Approval Status", "1", "Draft", "false", "Filtered Approval Status", "4", "Funding-2010-Actual Disbursements", "123,321", "Funding-2011-Actual Commitments", "213,231", "Totals-Actual Commitments", "213,231", "Totals-Actual Disbursements", "123,321"),
                    new ReportAreaForTests(new AreaOwner(13), "Project Title", "TAC_activity_2", "Approval Status", "1", "Draft", "false", "Filtered Approval Status", "4", "Funding-2010-Actual Disbursements", "453,213", "Funding-2011-Actual Commitments", "999,888", "Totals-Actual Commitments", "999,888", "Totals-Actual Disbursements", "453,213"),
                    new ReportAreaForTests(new AreaOwner(15), "Project Title", "Proposed Project Cost 1 - USD", "Approval Status", "1", "Draft", "false", "Filtered Approval Status", "4"),
                    new ReportAreaForTests(new AreaOwner(17), "Project Title", "Proposed Project Cost 2 - EUR", "Approval Status", "1", "Draft", "false", "Filtered Approval Status", "4"),
                    new ReportAreaForTests(new AreaOwner(18), "Project Title", "Test MTEF directed", "Approval Status", "1", "Draft", "false", "Filtered Approval Status", "4", "Funding-2010-Actual Disbursements", "143,777", "Totals-Actual Disbursements", "143,777"),
                    new ReportAreaForTests(new AreaOwner(19), "Project Title", "Pure MTEF Project", "Approval Status", "1", "Draft", "false", "Filtered Approval Status", "4"),
                    new ReportAreaForTests(new AreaOwner(21), "Project Title", "activity with components", "Approval Status", "1", "Draft", "false", "Filtered Approval Status", "4"),
                    new ReportAreaForTests(new AreaOwner(23), "Project Title", "Project with documents", "Approval Status", "1", "Draft", "false", "Filtered Approval Status", "4"),
                    new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Approval Status", "1", "Draft", "false", "Filtered Approval Status", "4", "Funding-2013-Actual Disbursements", "545,000", "Totals-Actual Disbursements", "545,000"),
                    new ReportAreaForTests(new AreaOwner(25), "Project Title", "mtef activity 1", "Approval Status", "1", "Draft", "false", "Filtered Approval Status", "4"),
                    new ReportAreaForTests(new AreaOwner(26), "Project Title", "date-filters-activity", "Approval Status", "1", "Draft", "false", "Filtered Approval Status", "4", "Funding-2009-Actual Commitments", "100,000", "Funding-2010-Actual Disbursements", "60,000", "Funding-2012-Actual Commitments", "25,000", "Funding-2012-Actual Disbursements", "12,000", "Totals-Actual Commitments", "125,000", "Totals-Actual Disbursements", "72,000"),
                    new ReportAreaForTests(new AreaOwner(27), "Project Title", "mtef activity 2", "Approval Status", "1", "Draft", "false", "Filtered Approval Status", "4"),
                    new ReportAreaForTests(new AreaOwner(28), "Project Title", "ptc activity 1", "Approval Status", "1", "Draft", "false", "Filtered Approval Status", "4", "Funding-2013-Actual Commitments", "666,777", "Totals-Actual Commitments", "666,777"),
                    new ReportAreaForTests(new AreaOwner(29), "Project Title", "ptc activity 2", "Approval Status", "1", "Draft", "false", "Filtered Approval Status", "4", "Funding-2013-Actual Commitments", "333,222", "Totals-Actual Commitments", "333,222"),
                    new ReportAreaForTests(new AreaOwner(30), "Project Title", "SSC Project 1", "Approval Status", "1", "Draft", "false", "Filtered Approval Status", "4", "Funding-2013-Actual Commitments", "111,333", "Funding-2013-Actual Disbursements", "555,111", "Totals-Actual Commitments", "111,333", "Totals-Actual Disbursements", "555,111"),
                    new ReportAreaForTests(new AreaOwner(31), "Project Title", "SSC Project 2", "Approval Status", "1", "Draft", "false", "Filtered Approval Status", "4", "Funding-2013-Actual Commitments", "567,421", "Funding-2013-Actual Disbursements", "131,845", "Totals-Actual Commitments", "567,421", "Totals-Actual Disbursements", "131,845"),
                    new ReportAreaForTests(new AreaOwner(32), "Project Title", "crazy funding 1", "Approval Status", "1", "Draft", "false", "Filtered Approval Status", "4", "Funding-2013-Actual Commitments", "333,333", "Totals-Actual Commitments", "333,333"),
                    new ReportAreaForTests(new AreaOwner(33), "Project Title", "Activity with Zones", "Approval Status", "1", "Draft", "false", "Filtered Approval Status", "4", "Funding-2013-Actual Commitments", "570,000", "Totals-Actual Commitments", "570,000"),
                    new ReportAreaForTests(new AreaOwner(36), "Project Title", "Activity With Zones and Percentages", "Approval Status", "1", "Draft", "false", "Filtered Approval Status", "4", "Funding-2013-Actual Commitments", "890,000", "Totals-Actual Commitments", "890,000"),
                    new ReportAreaForTests(new AreaOwner(40), "Project Title", "SubNational no percentages", "Approval Status", "1", "Draft", "false", "Filtered Approval Status", "4", "Funding-2014-Actual Commitments", "75,000", "Totals-Actual Commitments", "75,000"),
                    new ReportAreaForTests(new AreaOwner(41), "Project Title", "Activity Linked With Pledge", "Approval Status", "1", "Draft", "false", "Filtered Approval Status", "4", "Funding-2014-Actual Commitments", "50,000", "Totals-Actual Commitments", "50,000"),
                    new ReportAreaForTests(new AreaOwner(43), "Project Title", "Activity with primary_tertiary_program", "Approval Status", "1", "Draft", "false", "Filtered Approval Status", "4", "Funding-2014-Actual Commitments", "50,000", "Totals-Actual Commitments", "50,000"),
                    new ReportAreaForTests(new AreaOwner(44), "Project Title", "activity with primary_program", "Approval Status", "1", "Draft", "false", "Filtered Approval Status", "4", "Funding-2014-Actual Commitments", "32,000", "Totals-Actual Commitments", "32,000"),
                    new ReportAreaForTests(new AreaOwner(45), "Project Title", "activity with tertiary_program", "Approval Status", "1", "Draft", "false", "Filtered Approval Status", "4", "Funding-2014-Actual Commitments", "15,000", "Totals-Actual Commitments", "15,000"),
                    new ReportAreaForTests(new AreaOwner(46), "Project Title", "pledged education activity 1", "Approval Status", "1", "Draft", "false", "Filtered Approval Status", "4", "Funding-2013-Actual Commitments", "1,700,000", "Funding-2014-Actual Commitments", "3,300,000", "Totals-Actual Commitments", "5,000,000"),
                    new ReportAreaForTests(new AreaOwner(48), "Project Title", "pledged 2", "Approval Status", "1", "Draft", "false", "Filtered Approval Status", "4", "Funding-2013-Actual Commitments", "2,670,000", "Funding-2014-Actual Commitments", "4,400,000", "Funding-2014-Actual Disbursements", "450,000", "Totals-Actual Commitments", "7,070,000", "Totals-Actual Disbursements", "450,000"),
                    new ReportAreaForTests(new AreaOwner(50), "Project Title", "activity with capital spending", "Approval Status", "1", "Draft", "false", "Filtered Approval Status", "4", "Funding-2014-Actual Commitments", "65,760,63", "Funding-2014-Actual Disbursements", "80,000", "Totals-Actual Commitments", "65,760,63", "Totals-Actual Disbursements", "80,000"),
                    new ReportAreaForTests(new AreaOwner(52), "Project Title", "activity with contracting agency", "Approval Status", "1", "Draft", "false", "Filtered Approval Status", "4", "Funding-2006-Actual Commitments", "96,840,58", "Funding-2014-Actual Disbursements", "50,000", "Totals-Actual Commitments", "96,840,58", "Totals-Actual Disbursements", "50,000"),
                    new ReportAreaForTests(new AreaOwner(53), "Project Title", "new activity with contracting", "Approval Status", "3", "Draft", "true", "Filtered Approval Status", "1", "Funding-2014-Actual Commitments", "12,000", "Totals-Actual Commitments", "12,000"),
                    new ReportAreaForTests(new AreaOwner(61), "Project Title", "activity-with-unfunded-components", "Approval Status", "1", "Draft", "false", "Filtered Approval Status", "4", "Funding-2014-Actual Commitments", "123,321", "Totals-Actual Commitments", "123,321"),
                    new ReportAreaForTests(new AreaOwner(63), "Project Title", "activity with funded components", "Approval Status", "1", "Draft", "false", "Filtered Approval Status", "4", "Funding-2014-Actual Commitments", "100", "Totals-Actual Commitments", "100"),
                    new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Approval Status", "3", "Draft", "false", "Filtered Approval Status", "4", "Funding-2015-Actual Commitments", "456,789", "Funding-2015-Actual Disbursements", "321,765", "Totals-Actual Commitments", "456,789", "Totals-Actual Disbursements", "321,765"),
                    new ReportAreaForTests(new AreaOwner(66), "Project Title", "Activity 2 with multiple agreements", "Approval Status", "3", "Draft", "false", "Filtered Approval Status", "4", "Funding-2015-Actual Commitments", "1,200", "Totals-Actual Commitments", "1,200"),
                    new ReportAreaForTests(new AreaOwner(67), "Project Title", "third activity with agreements", "Approval Status", "3", "Draft", "false", "Filtered Approval Status", "4", "Funding-2015-Actual Commitments", "123,456", "Totals-Actual Commitments", "123,456"),
                    new ReportAreaForTests(new AreaOwner(68), "Project Title", "activity with incomplete agreement", "Approval Status", "3", "Draft", "false", "Filtered Approval Status", "4", "Funding-2015-Actual Commitments", "123,000", "Totals-Actual Commitments", "123,000"),
                    new ReportAreaForTests(new AreaOwner(69), "Project Title", "Activity with planned disbursements", "Approval Status", "3", "Draft", "false", "Filtered Approval Status", "4", "Funding-2014-Actual Disbursements", "200", "Funding-2015-Actual Disbursements", "570", "Totals-Actual Disbursements", "770"),
                    new ReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms", "Approval Status", "3", "Draft", "false", "Filtered Approval Status", "4", "Funding-2015-Actual Commitments", "888,000", "Totals-Actual Commitments", "888,000"),
                    new ReportAreaForTests(new AreaOwner(71), "Project Title", "activity_with_disaster_response", "Approval Status", "3", "Draft", "false", "Filtered Approval Status", "4", "Funding-2014-Actual Commitments", "33,000", "Funding-2015-Actual Commitments", "117,000", "Totals-Actual Commitments", "150,000"),
                    new ReportAreaForTests(new AreaOwner(73), "Project Title", "activity with directed MTEFs", "Approval Status", "1", "Draft", "false", "Filtered Approval Status", "4", "Funding-2015-Actual Commitments", "123,456", "Totals-Actual Commitments", "123,456"),
                    new ReportAreaForTests(new AreaOwner(76), "Project Title", "activity with pipeline MTEFs and act. disb", "Approval Status", "1", "Draft", "false", "Filtered Approval Status", "4", "Funding-2013-Actual Disbursements", "35,000", "Funding-2014-Actual Disbursements", "75,000", "Totals-Actual Disbursements", "110,000"),
                    new ReportAreaForTests(new AreaOwner(77), "Project Title", "execution rate activity", "Approval Status", "3", "Draft", "false", "Filtered Approval Status", "4", "Funding-2014-Actual Disbursements", "55,000", "Funding-2015-Actual Disbursements", "35,000", "Totals-Actual Disbursements", "90,000"),
                    new ReportAreaForTests(new AreaOwner(78), "Project Title", "activity with many MTEFs", "Approval Status", "3", "Draft", "false", "Filtered Approval Status", "4", "Funding-2015-Actual Disbursements", "80,000", "Totals-Actual Disbursements", "80,000"),
                    new ReportAreaForTests(new AreaOwner(79), "Project Title", "with weird currencies", "Approval Status", "3", "Draft", "false", "Filtered Approval Status", "4", "Funding-2014-Actual Commitments", "3,632,14", "Funding-2015-Actual Commitments", "93,930,84", "Totals-Actual Commitments", "97,562,98")      ));;
    
        ReportSpecificationImpl spec = buildSpecForFiltering("testNegativeByApprovalStatusNewUnvalidated", 
            Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.APPROVAL_STATUS, ColumnConstants.DRAFT, ColumnConstants.VALIDATION_STATUS),
            null, 
            ColumnConstants.APPROVAL_STATUS, Arrays.asList(2l), false); // All but "New Unvalidated"
        
        runNiTestCase(cor, spec, acts);
    }
    
    @Test
    public void testNegativeByApprovalStatusValidated() {
        NiReportModel cor = new NiReportModel("testNegativeByApprovalStatusValidated")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 10))",
                        "(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Approval Status: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Draft: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1));(Filtered Approval Status: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 3, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 4, colSpan: 4));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 8, colSpan: 2))",
                        "(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 6, colSpan: 2))",
                        "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1))"))
                    .withWarnings(Arrays.asList())
                    .withBody(      new ReportAreaForTests(null)
                      .withContents("Project Title", "", "Approval Status", "", "Draft", "", "Filtered Approval Status", "", "Funding-2014-Actual Commitments", "12,000", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "45,000", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "57,000", "Totals-Actual Disbursements", "0")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(53), "Project Title", "new activity with contracting", "Approval Status", "3", "Draft", "true", "Filtered Approval Status", "1", "Funding-2014-Actual Commitments", "12,000", "Totals-Actual Commitments", "12,000"),
                        new ReportAreaForTests(new AreaOwner(64), "Project Title", "Unvalidated activity", "Approval Status", "4", "Draft", "false", "Filtered Approval Status", "2", "Funding-2015-Actual Commitments", "45,000", "Totals-Actual Commitments", "45,000")      ));

        ReportSpecificationImpl spec = buildSpecForFiltering("testNegativeByApprovalStatusValidated", 
            Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.APPROVAL_STATUS, ColumnConstants.DRAFT, ColumnConstants.VALIDATION_STATUS),
            null, 
            ColumnConstants.APPROVAL_STATUS, Arrays.asList(4l), false); // All but Validated
        
        runNiTestCase(cor, spec, acts);
    }
    
    @Test
    public void testFilterFlatByHumanitarianAid() {
        NiReportModel cor = new NiReportModel("testFilterFlatByHumanitarianAid")
            .withHeaders(Arrays.asList(
                    "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 10))",
                    "(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Humanitarian Aid: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 6));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 8, colSpan: 2))",
                    "(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 2));(2011: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 2));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 6, colSpan: 2))",
                    "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                  .withContents("Project Title", "", "Humanitarian Aid", "", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "123,321", "Funding-2011-Actual Commitments", "213,231", "Funding-2011-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "333,333", "Funding-2013-Actual Disbursements", "0", "Totals-Actual Commitments", "546,564", "Totals-Actual Disbursements", "123,321")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(12), "Project Title", "TAC_activity_1", "Humanitarian Aid", "Yes", "Funding-2010-Actual Disbursements", "123,321", "Funding-2011-Actual Commitments", "213,231", "Totals-Actual Commitments", "213,231", "Totals-Actual Disbursements", "123,321"),
                    new ReportAreaForTests(new AreaOwner(32), "Project Title", "crazy funding 1", "Humanitarian Aid", "Yes", "Funding-2013-Actual Commitments", "333,333", "Totals-Actual Commitments", "333,333")      ));

        
        ReportSpecificationImpl spec = buildSpecForFiltering("testFilterFlatByHumanitarianAid", 
                Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.HUMANITARIAN_AID), 
                null, 
                ColumnConstants.HUMANITARIAN_AID, Arrays.asList(1l), true); // yes
            
        runNiTestCase(cor, spec, acts);
    }
    
    @Test
    public void testFilterHierByHumanitarianAid() {
        NiReportModel cor = new NiReportModel("testFilterHierByHumanitarianAidYes")
            .withHeaders(Arrays.asList(
                    "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 10))",
                    "(Humanitarian Aid: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 6));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 8, colSpan: 2))",
                    "(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 2));(2011: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 2));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 6, colSpan: 2))",
                    "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null).withContents("Humanitarian Aid", "", "Project Title", "", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "123,321", "Funding-2011-Actual Commitments", "213,231", "Funding-2011-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "333,333", "Funding-2013-Actual Disbursements", "0", "Totals-Actual Commitments", "546,564", "Totals-Actual Disbursements", "123,321")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner("Humanitarian Aid", "Yes"))
                    .withContents("Project Title", "", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "123,321", "Funding-2011-Actual Commitments", "213,231", "Funding-2011-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "333,333", "Funding-2013-Actual Disbursements", "0", "Totals-Actual Commitments", "546,564", "Totals-Actual Disbursements", "123,321", "Humanitarian Aid", "Yes")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner(12), "Project Title", "TAC_activity_1", "Funding-2010-Actual Disbursements", "123,321", "Funding-2011-Actual Commitments", "213,231", "Totals-Actual Commitments", "213,231", "Totals-Actual Disbursements", "123,321"),
                      new ReportAreaForTests(new AreaOwner(32), "Project Title", "crazy funding 1", "Funding-2013-Actual Commitments", "333,333", "Totals-Actual Commitments", "333,333")        )      ));
        
        ReportSpecificationImpl spec = buildSpecForFiltering("testFilterHierByHumanitarianAidYes", 
                Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.HUMANITARIAN_AID), 
                Arrays.asList(ColumnConstants.HUMANITARIAN_AID), 
                ColumnConstants.HUMANITARIAN_AID, Arrays.asList(1l), true); // yes
            
        runNiTestCase(cor, spec, acts);
    }
    
    @Test
    public void testFilterHierByDisasterResponseFilterYes() {
        NiReportModel cor = new NiReportModel("testFilterHierByDisasterResponseFilterYes")
            .withHeaders(Arrays.asList(
                    "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 6))",
                    "(Disaster Response Marker: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 2));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 4, colSpan: 2))",
                    "(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 2))",
                    "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null).withContents("Disaster Response Marker", "", "Project Title", "", "Funding-2015-Actual Commitments", "67,000", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "67,000", "Totals-Actual Disbursements", "0")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner("Disaster Response Marker", "Yes")).withContents("Project Title", "", "Funding-2015-Actual Commitments", "67,000", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "67,000", "Totals-Actual Disbursements", "0", "Disaster Response Marker", "Yes")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner(71), "Project Title", "activity_with_disaster_response", "Funding-2015-Actual Commitments", "67,000", "Totals-Actual Commitments", "67,000")        )      ));

        ReportSpecificationImpl spec = buildSpecForFiltering("testFilterHierByDisasterResponseFilterYes", 
                Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.DISASTER_RESPONSE_MARKER), 
                Arrays.asList(ColumnConstants.DISASTER_RESPONSE_MARKER), 
                ColumnConstants.DISASTER_RESPONSE_MARKER, Arrays.asList(1l), true); // yes
            
        runNiTestCase(cor, spec, acts);
    }
    
    @Test
    public void testFilterFlatByDisasterResponseNo() {
        NiReportModel cor = new NiReportModel("testFilterFlatByDisasterResponseNo")
            .withHeaders(Arrays.asList(
                    "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 6))",
                    "(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Disaster Response Marker: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 2));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 4, colSpan: 2))",
                    "(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 2))",
                    "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null).withContents("Project Title", "", "Disaster Response Marker", "", "Funding-2014-Actual Commitments", "33,000", "Funding-2014-Actual Disbursements", "0", "Totals-Actual Commitments", "33,000", "Totals-Actual Disbursements", "0")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(71), "Project Title", "activity_with_disaster_response", "Disaster Response Marker", "No", "Funding-2014-Actual Commitments", "33,000", "Totals-Actual Commitments", "33,000")      ));


        ReportSpecificationImpl spec = buildSpecForFiltering("testFilterFlatByDisasterResponseNo", 
                Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.DISASTER_RESPONSE_MARKER), 
                null, 
                ColumnConstants.DISASTER_RESPONSE_MARKER, Arrays.asList(2l), true); // no
            
        runNiTestCase(cor, spec, acts);
    }
    
    @Test
    public void testUndefinedFilterByModeOfPayment() {
        NiReportModel cor = new NiReportModel("testUndefinedFilterByPrimarySector")
            .withHeaders(Arrays.asList(
                    "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 20))",
                    "(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Mode of Payment: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 16));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 18, colSpan: 2))",
                    "(2006: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 2));(2009: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 2));(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 6, colSpan: 2));(2011: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 8, colSpan: 2));(2012: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 10, colSpan: 2));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 12, colSpan: 2));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 14, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 16, colSpan: 2))",
                    "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 14, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 15, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 16, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 17, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 18, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 19, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                  .withContents("Project Title", "", "Mode of Payment", "", "Funding-2006-Actual Commitments", "96,840,58", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "100,000", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "636,534", "Funding-2011-Actual Commitments", "1,213,119", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "25,000", "Funding-2012-Actual Disbursements", "12,000", "Funding-2013-Actual Commitments", "5,830,000", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "7,998,181,63", "Funding-2014-Actual Disbursements", "635,200", "Funding-2015-Actual Commitments", "749,445", "Funding-2015-Actual Disbursements", "437,335", "Totals-Actual Commitments", "16,012,586,21", "Totals-Actual Disbursements", "1,721,069")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(12), "Project Title", "TAC_activity_1", "Mode of Payment", "", "Funding-2010-Actual Disbursements", "123,321", "Funding-2011-Actual Commitments", "213,231", "Totals-Actual Commitments", "213,231", "Totals-Actual Disbursements", "123,321"),
                    new ReportAreaForTests(new AreaOwner(13), "Project Title", "TAC_activity_2", "Mode of Payment", "", "Funding-2010-Actual Disbursements", "453,213", "Funding-2011-Actual Commitments", "999,888", "Totals-Actual Commitments", "999,888", "Totals-Actual Disbursements", "453,213"),
                    new ReportAreaForTests(new AreaOwner(26), "Project Title", "date-filters-activity", "Mode of Payment", "", "Funding-2009-Actual Commitments", "100,000", "Funding-2010-Actual Disbursements", "60,000", "Funding-2012-Actual Commitments", "25,000", "Funding-2012-Actual Disbursements", "12,000", "Totals-Actual Commitments", "125,000", "Totals-Actual Disbursements", "72,000"),
                    new ReportAreaForTests(new AreaOwner(33), "Project Title", "Activity with Zones", "Mode of Payment", "", "Funding-2013-Actual Commitments", "570,000", "Totals-Actual Commitments", "570,000"),
                    new ReportAreaForTests(new AreaOwner(36), "Project Title", "Activity With Zones and Percentages", "Mode of Payment", "", "Funding-2013-Actual Commitments", "890,000", "Totals-Actual Commitments", "890,000"),
                    new ReportAreaForTests(new AreaOwner(41), "Project Title", "Activity Linked With Pledge", "Mode of Payment", "", "Funding-2014-Actual Commitments", "50,000", "Totals-Actual Commitments", "50,000"),
                    new ReportAreaForTests(new AreaOwner(44), "Project Title", "activity with primary_program", "Mode of Payment", "", "Funding-2014-Actual Commitments", "32,000", "Totals-Actual Commitments", "32,000"),
                    new ReportAreaForTests(new AreaOwner(45), "Project Title", "activity with tertiary_program", "Mode of Payment", "", "Funding-2014-Actual Commitments", "15,000", "Totals-Actual Commitments", "15,000"),
                    new ReportAreaForTests(new AreaOwner(46), "Project Title", "pledged education activity 1", "Mode of Payment", "", "Funding-2013-Actual Commitments", "1,700,000", "Funding-2014-Actual Commitments", "3,300,000", "Totals-Actual Commitments", "5,000,000"),
                    new ReportAreaForTests(new AreaOwner(48), "Project Title", "pledged 2", "Mode of Payment", "", "Funding-2013-Actual Commitments", "2,670,000", "Funding-2014-Actual Commitments", "4,400,000", "Funding-2014-Actual Disbursements", "450,000", "Totals-Actual Commitments", "7,070,000", "Totals-Actual Disbursements", "450,000"),
                    new ReportAreaForTests(new AreaOwner(50), "Project Title", "activity with capital spending", "Mode of Payment", "", "Funding-2014-Actual Commitments", "65,760,63", "Funding-2014-Actual Disbursements", "80,000", "Totals-Actual Commitments", "65,760,63", "Totals-Actual Disbursements", "80,000"),
                    new ReportAreaForTests(new AreaOwner(52), "Project Title", "activity with contracting agency", "Mode of Payment", "", "Funding-2006-Actual Commitments", "96,840,58", "Funding-2014-Actual Disbursements", "50,000", "Totals-Actual Commitments", "96,840,58", "Totals-Actual Disbursements", "50,000"),
                    new ReportAreaForTests(new AreaOwner(53), "Project Title", "new activity with contracting", "Mode of Payment", "", "Funding-2014-Actual Commitments", "12,000", "Totals-Actual Commitments", "12,000"),
                    new ReportAreaForTests(new AreaOwner(61), "Project Title", "activity-with-unfunded-components", "Mode of Payment", "", "Funding-2014-Actual Commitments", "123,321", "Totals-Actual Commitments", "123,321"),
                    new ReportAreaForTests(new AreaOwner(63), "Project Title", "activity with funded components", "Mode of Payment", "", "Funding-2014-Actual Commitments", "100", "Totals-Actual Commitments", "100"),
                    new ReportAreaForTests(new AreaOwner(64), "Project Title", "Unvalidated activity", "Mode of Payment", "", "Funding-2015-Actual Commitments", "45,000", "Totals-Actual Commitments", "45,000"),
                    new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Mode of Payment", "", "Funding-2015-Actual Commitments", "456,789", "Funding-2015-Actual Disbursements", "321,765", "Totals-Actual Commitments", "456,789", "Totals-Actual Disbursements", "321,765"),
                    new ReportAreaForTests(new AreaOwner(66), "Project Title", "Activity 2 with multiple agreements", "Mode of Payment", "", "Funding-2015-Actual Commitments", "1,200", "Totals-Actual Commitments", "1,200"),
                    new ReportAreaForTests(new AreaOwner(67), "Project Title", "third activity with agreements", "Mode of Payment", "", "Funding-2015-Actual Commitments", "123,456", "Totals-Actual Commitments", "123,456"),
                    new ReportAreaForTests(new AreaOwner(68), "Project Title", "activity with incomplete agreement", "Mode of Payment", "", "Funding-2015-Actual Commitments", "123,000", "Totals-Actual Commitments", "123,000"),
                    new ReportAreaForTests(new AreaOwner(69), "Project Title", "Activity with planned disbursements", "Mode of Payment", "", "Funding-2014-Actual Disbursements", "200", "Funding-2015-Actual Disbursements", "570", "Totals-Actual Disbursements", "770"),
                    new ReportAreaForTests(new AreaOwner(77), "Project Title", "execution rate activity", "Mode of Payment", "", "Funding-2014-Actual Disbursements", "55,000", "Funding-2015-Actual Disbursements", "35,000", "Totals-Actual Disbursements", "90,000"),
                    new ReportAreaForTests(new AreaOwner(78), "Project Title", "activity with many MTEFs", "Mode of Payment", "", "Funding-2015-Actual Disbursements", "80,000", "Totals-Actual Disbursements", "80,000")      ));
        
        ReportSpecificationImpl spec = buildSpecForFiltering("testUndefinedFilterByPrimarySector", 
                Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.MODE_OF_PAYMENT), 
                null, 
                ColumnConstants.MODE_OF_PAYMENT, Arrays.asList(ColumnReportData.UNALLOCATED_ID), true);
        
        runNiTestCase(cor, spec, acts);
    }
}
