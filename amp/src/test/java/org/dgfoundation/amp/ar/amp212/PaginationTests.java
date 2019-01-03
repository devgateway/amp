package org.dgfoundation.amp.ar.amp212;

import java.util.Arrays;
import java.util.List;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.newreports.PaginatedReportAreaForTests;
import org.dgfoundation.amp.newreports.ReportingTestCase;
import org.dgfoundation.amp.newreports.AreaOwner;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.newreports.pagination.PaginatedReport;
import org.dgfoundation.amp.nireports.testcases.ReportModelGenerator;
import org.junit.Test;

/**
 * 
 * testcases for the fetching states of AMP + the AMP schema
 * 
 * @author Constantin Dolghier
 *
 */
public class PaginationTests extends ReportingTestCase {

    final List<String> acts = Arrays.asList("activity 1 with agreement", "Activity 2 with multiple agreements",
            "Activity Linked With Pledge", "Activity with both MTEFs and Act.Comms", "activity with capital spending",
            "activity with components", "activity with contracting agency", "activity with directed MTEFs",
            "activity_with_disaster_response", "activity with funded components", "activity with incomplete agreement",
            "activity with many MTEFs", "activity with pipeline MTEFs and act. disb",
            "Activity with planned disbursements", "activity with primary_program",
            "Activity with primary_tertiary_program", "activity with tertiary_program",
            "activity-with-unfunded-components", "Activity with Zones", "Activity With Zones and Percentages",
            "crazy funding 1", "date-filters-activity", "Eth Water", "execution rate activity", "mtef activity 1",
            "mtef activity 2", "new activity with contracting", "pledged 2", "pledged education activity 1",
            "Project with documents", "Proposed Project Cost 1 - USD", "Proposed Project Cost 2 - EUR",
            "ptc activity 1", "ptc activity 2", "Pure MTEF Project", "SSC Project 1", "SSC Project 2",
            "SubNational no percentages", "TAC_activity_1", "TAC_activity_2", "Test MTEF directed",
            "third activity with agreements", "Unvalidated activity", "with weird currencies");

    public PaginationTests() {
        inTransactionRule = null;
    }

    final ReportSpecification theFlatSpec = ReportSpecificationImpl.buildFor("initReport",
            Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.REGION),
            Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS), null,
            GroupingCriteria.GROUPING_YEARLY);
    final ReportSpecification theSingleHierSpec = ReportSpecificationImpl.buildFor("initReport",
            Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.REGION),
            Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS),
            Arrays.asList(ColumnConstants.REGION), GroupingCriteria.GROUPING_YEARLY);
    final ReportSpecification theDoubleHierSpec = ReportSpecificationImpl.buildFor("initReport",
            Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.REGION, ColumnConstants.PRIMARY_SECTOR),
            Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS),
            Arrays.asList(ColumnConstants.REGION, ColumnConstants.PRIMARY_SECTOR), GroupingCriteria.GROUPING_YEARLY);

    final PaginatedReport initFlatReport = new PaginatedReport(
            getNiExecutor(acts).executeReport(theFlatSpec, new ReportModelGenerator()).body);
    final PaginatedReport initHierReport = new PaginatedReport(
            getNiExecutor(acts).executeReport(theSingleHierSpec, new ReportModelGenerator()).body);
    final PaginatedReport initDoubleHierReport = new PaginatedReport(
            getNiExecutor(acts).executeReport(theDoubleHierSpec, new ReportModelGenerator()).body);

    @Test
    public void testFullFlat() {
        PaginatedReportAreaForTests cor =  new PaginatedReportAreaForTests(null).withCounts(44, 44)
                .withContents("Project Title", "", "Region", "", "Funding-2006-Actual Commitments", "96,840,58", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "100,000", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "780,311", "Funding-2011-Actual Commitments", "1,213,119", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "25,000", "Funding-2012-Actual Disbursements", "12,000", "Funding-2013-Actual Commitments", "7,842,086", "Funding-2013-Actual Disbursements", "1,266,956", "Funding-2014-Actual Commitments", "8,159,813,77", "Funding-2014-Actual Disbursements", "710,200", "Funding-2015-Actual Commitments", "1,971,831,84", "Funding-2015-Actual Disbursements", "437,335", "Totals-Actual Commitments", "19,408,691,19", "Totals-Actual Disbursements", "3,206,802")
                .withChildren(
                  new PaginatedReportAreaForTests(new AreaOwner(12), "Project Title", "TAC_activity_1", "Region", "Dubasari County", "Funding-2010-Actual Disbursements", "123,321", "Funding-2011-Actual Commitments", "213,231", "Totals-Actual Commitments", "213,231", "Totals-Actual Disbursements", "123,321").withCounts(1, -1),
                  new PaginatedReportAreaForTests(new AreaOwner(13), "Project Title", "TAC_activity_2", "Region", "Falesti County", "Funding-2010-Actual Disbursements", "453,213", "Funding-2011-Actual Commitments", "999,888", "Totals-Actual Commitments", "999,888", "Totals-Actual Disbursements", "453,213").withCounts(1, -1),
                  new PaginatedReportAreaForTests(new AreaOwner(15), "Project Title", "Proposed Project Cost 1 - USD", "Region", "Drochia County").withCounts(1, -1),
                  new PaginatedReportAreaForTests(new AreaOwner(17), "Project Title", "Proposed Project Cost 2 - EUR", "Region", "Anenii Noi County").withCounts(1, -1),
                  new PaginatedReportAreaForTests(new AreaOwner(18), "Project Title", "Test MTEF directed", "Region", "Anenii Noi County", "Funding-2010-Actual Disbursements", "143,777", "Totals-Actual Disbursements", "143,777").withCounts(1, -1),
                  new PaginatedReportAreaForTests(new AreaOwner(19), "Project Title", "Pure MTEF Project", "Region", "Cahul County").withCounts(1, -1),
                  new PaginatedReportAreaForTests(new AreaOwner(21), "Project Title", "activity with components", "Region", "Anenii Noi County").withCounts(1, -1),
                  new PaginatedReportAreaForTests(new AreaOwner(23), "Project Title", "Project with documents", "Region", "Balti County").withCounts(1, -1),
                  new PaginatedReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Region", "Anenii Noi County", "Funding-2013-Actual Disbursements", "545,000", "Totals-Actual Disbursements", "545,000").withCounts(1, -1),
                  new PaginatedReportAreaForTests(new AreaOwner(25), "Project Title", "mtef activity 1", "Region", "").withCounts(1, -1),
                  new PaginatedReportAreaForTests(new AreaOwner(26), "Project Title", "date-filters-activity", "Region", "", "Funding-2009-Actual Commitments", "100,000", "Funding-2010-Actual Disbursements", "60,000", "Funding-2012-Actual Commitments", "25,000", "Funding-2012-Actual Disbursements", "12,000", "Totals-Actual Commitments", "125,000", "Totals-Actual Disbursements", "72,000").withCounts(1, -1),
                  new PaginatedReportAreaForTests(new AreaOwner(27), "Project Title", "mtef activity 2", "Region", "Anenii Noi County").withCounts(1, -1),
                  new PaginatedReportAreaForTests(new AreaOwner(28), "Project Title", "ptc activity 1", "Region", "Anenii Noi County", "Funding-2013-Actual Commitments", "666,777", "Totals-Actual Commitments", "666,777").withCounts(1, -1),
                  new PaginatedReportAreaForTests(new AreaOwner(29), "Project Title", "ptc activity 2", "Region", "Anenii Noi County", "Funding-2013-Actual Commitments", "333,222", "Totals-Actual Commitments", "333,222").withCounts(1, -1),
                  new PaginatedReportAreaForTests(new AreaOwner(30), "Project Title", "SSC Project 1", "Region", "Anenii Noi County", "Funding-2013-Actual Commitments", "111,333", "Funding-2013-Actual Disbursements", "555,111", "Totals-Actual Commitments", "111,333", "Totals-Actual Disbursements", "555,111").withCounts(1, -1),
                  new PaginatedReportAreaForTests(new AreaOwner(31), "Project Title", "SSC Project 2", "Region", "Edinet County", "Funding-2013-Actual Commitments", "567,421", "Funding-2013-Actual Disbursements", "131,845", "Totals-Actual Commitments", "567,421", "Totals-Actual Disbursements", "131,845").withCounts(1, -1),
                  new PaginatedReportAreaForTests(new AreaOwner(32), "Project Title", "crazy funding 1", "Region", "Balti County", "Funding-2013-Actual Commitments", "333,333", "Totals-Actual Commitments", "333,333").withCounts(1, -1),
                  new PaginatedReportAreaForTests(new AreaOwner(33), "Project Title", "Activity with Zones", "Region", "Anenii Noi County, Balti County", "Funding-2013-Actual Commitments", "570,000", "Totals-Actual Commitments", "570,000").withCounts(1, -1),
                  new PaginatedReportAreaForTests(new AreaOwner(36), "Project Title", "Activity With Zones and Percentages", "Region", "Anenii Noi County, Balti County", "Funding-2013-Actual Commitments", "890,000", "Totals-Actual Commitments", "890,000").withCounts(1, -1),
                  new PaginatedReportAreaForTests(new AreaOwner(40), "Project Title", "SubNational no percentages", "Region", "Anenii Noi County, Balti County", "Funding-2014-Actual Commitments", "75,000", "Totals-Actual Commitments", "75,000").withCounts(1, -1),
                  new PaginatedReportAreaForTests(new AreaOwner(41), "Project Title", "Activity Linked With Pledge", "Region", "Chisinau City", "Funding-2014-Actual Commitments", "50,000", "Totals-Actual Commitments", "50,000").withCounts(1, -1),
                  new PaginatedReportAreaForTests(new AreaOwner(43), "Project Title", "Activity with primary_tertiary_program", "Region", "", "Funding-2014-Actual Commitments", "50,000", "Totals-Actual Commitments", "50,000").withCounts(1, -1),
                  new PaginatedReportAreaForTests(new AreaOwner(44), "Project Title", "activity with primary_program", "Region", "", "Funding-2014-Actual Commitments", "32,000", "Totals-Actual Commitments", "32,000").withCounts(1, -1),
                  new PaginatedReportAreaForTests(new AreaOwner(45), "Project Title", "activity with tertiary_program", "Region", "", "Funding-2014-Actual Commitments", "15,000", "Totals-Actual Commitments", "15,000").withCounts(1, -1),
                  new PaginatedReportAreaForTests(new AreaOwner(46), "Project Title", "pledged education activity 1", "Region", "Chisinau County", "Funding-2013-Actual Commitments", "1,700,000", "Funding-2014-Actual Commitments", "3,300,000", "Totals-Actual Commitments", "5,000,000").withCounts(1, -1),
                  new PaginatedReportAreaForTests(new AreaOwner(48), "Project Title", "pledged 2", "Region", "Cahul County", "Funding-2013-Actual Commitments", "2,670,000", "Funding-2014-Actual Commitments", "4,400,000", "Funding-2014-Actual Disbursements", "450,000", "Totals-Actual Commitments", "7,070,000", "Totals-Actual Disbursements", "450,000").withCounts(1, -1),
                  new PaginatedReportAreaForTests(new AreaOwner(50), "Project Title", "activity with capital spending", "Region", "Chisinau County", "Funding-2014-Actual Commitments", "65,760,63", "Funding-2014-Actual Disbursements", "80,000", "Totals-Actual Commitments", "65,760,63", "Totals-Actual Disbursements", "80,000").withCounts(1, -1),
                  new PaginatedReportAreaForTests(new AreaOwner(52), "Project Title", "activity with contracting agency", "Region", "Balti County, Transnistrian Region", "Funding-2006-Actual Commitments", "96,840,58", "Funding-2014-Actual Disbursements", "50,000", "Totals-Actual Commitments", "96,840,58", "Totals-Actual Disbursements", "50,000").withCounts(1, -1),
                  new PaginatedReportAreaForTests(new AreaOwner(53), "Project Title", "new activity with contracting", "Funding-2014-Actual Commitments", "12,000", "Totals-Actual Commitments", "12,000").withCounts(1, -1),
                  new PaginatedReportAreaForTests(new AreaOwner(61), "Project Title", "activity-with-unfunded-components", "Region", "Transnistrian Region", "Funding-2014-Actual Commitments", "123,321", "Totals-Actual Commitments", "123,321").withCounts(1, -1),
                  new PaginatedReportAreaForTests(new AreaOwner(63), "Project Title", "activity with funded components", "Region", "", "Funding-2014-Actual Commitments", "100", "Totals-Actual Commitments", "100").withCounts(1, -1),
                  new PaginatedReportAreaForTests(new AreaOwner(64), "Project Title", "Unvalidated activity", "Region", "", "Funding-2015-Actual Commitments", "45,000", "Totals-Actual Commitments", "45,000").withCounts(1, -1),
                  new PaginatedReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Region", "Balti County", "Funding-2015-Actual Commitments", "456,789", "Funding-2015-Actual Disbursements", "321,765", "Totals-Actual Commitments", "456,789", "Totals-Actual Disbursements", "321,765").withCounts(1, -1),
                  new PaginatedReportAreaForTests(new AreaOwner(66), "Project Title", "Activity 2 with multiple agreements", "Region", "Chisinau County", "Funding-2015-Actual Commitments", "1,200", "Totals-Actual Commitments", "1,200").withCounts(1, -1),
                  new PaginatedReportAreaForTests(new AreaOwner(67), "Project Title", "third activity with agreements", "Region", "Chisinau City", "Funding-2015-Actual Commitments", "123,456", "Totals-Actual Commitments", "123,456").withCounts(1, -1),
                  new PaginatedReportAreaForTests(new AreaOwner(68), "Project Title", "activity with incomplete agreement", "Region", "", "Funding-2015-Actual Commitments", "123,000", "Totals-Actual Commitments", "123,000").withCounts(1, -1),
                  new PaginatedReportAreaForTests(new AreaOwner(69), "Project Title", "Activity with planned disbursements", "Region", "", "Funding-2014-Actual Disbursements", "200", "Funding-2015-Actual Disbursements", "570", "Totals-Actual Disbursements", "770").withCounts(1, -1),
                  new PaginatedReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms", "Region", "Balti County, Drochia County", "Funding-2015-Actual Commitments", "888,000", "Totals-Actual Commitments", "888,000").withCounts(1, -1),
                  new PaginatedReportAreaForTests(new AreaOwner(71), "Project Title", "activity_with_disaster_response", "Region", "", "Funding-2014-Actual Commitments", "33,000", "Funding-2015-Actual Commitments", "117,000", "Totals-Actual Commitments", "150,000").withCounts(1, -1),
                  new PaginatedReportAreaForTests(new AreaOwner(73), "Project Title", "activity with directed MTEFs", "Region", "Chisinau City", "Funding-2015-Actual Commitments", "123,456", "Totals-Actual Commitments", "123,456").withCounts(1, -1),
                  new PaginatedReportAreaForTests(new AreaOwner(76), "Project Title", "activity with pipeline MTEFs and act. disb", "Region", "Chisinau County", "Funding-2013-Actual Disbursements", "35,000", "Funding-2014-Actual Disbursements", "75,000", "Totals-Actual Disbursements", "110,000").withCounts(1, -1),
                  new PaginatedReportAreaForTests(new AreaOwner(77), "Project Title", "execution rate activity", "Region", "Chisinau City, Dubasari County", "Funding-2014-Actual Disbursements", "55,000", "Funding-2015-Actual Disbursements", "35,000", "Totals-Actual Disbursements", "90,000").withCounts(1, -1),
                  new PaginatedReportAreaForTests(new AreaOwner(78), "Project Title", "activity with many MTEFs", "Region", "Drochia County", "Funding-2015-Actual Disbursements", "80,000", "Totals-Actual Disbursements", "80,000").withCounts(1, -1),
                  new PaginatedReportAreaForTests(new AreaOwner(79), "Project Title", "with weird currencies", "Region", "", "Funding-2014-Actual Commitments", "3,632,14", "Funding-2015-Actual Commitments", "93,930,84", "Totals-Actual Commitments", "97,562,98").withCounts(1, -1));

        compareBodies("full flat", cor, initFlatReport.getPage(0, 150));
    }

    @Test
    public void testBeginFlat() {
        PaginatedReportAreaForTests cor = new PaginatedReportAreaForTests(null).withCounts(10, 44)
                .withContents("Project Title", "", "Region", "", "Funding-2006-Actual Commitments", "96,840,58",
                        "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "100,000",
                        "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0",
                        "Funding-2010-Actual Disbursements", "780,311", "Funding-2011-Actual Commitments", "1,213,119",
                        "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "25,000",
                        "Funding-2012-Actual Disbursements", "12,000", "Funding-2013-Actual Commitments", "7,842,086",
                        "Funding-2013-Actual Disbursements", "1,266,956", "Funding-2014-Actual Commitments",
                        "8,159,813,77", "Funding-2014-Actual Disbursements", "710,200",
                        "Funding-2015-Actual Commitments", "1,971,831,84", "Funding-2015-Actual Disbursements",
                        "437,335", "Totals-Actual Commitments", "19,408,691,19", "Totals-Actual Disbursements",
                        "3,206,802")
                .withChildren(
                        new PaginatedReportAreaForTests(new AreaOwner(12), "Project Title", "TAC_activity_1", "Region",
                                "Dubasari County", "Funding-2010-Actual Disbursements", "123,321",
                                "Funding-2011-Actual Commitments", "213,231", "Totals-Actual Commitments", "213,231",
                                "Totals-Actual Disbursements", "123,321").withCounts(1, -1),
                        new PaginatedReportAreaForTests(new AreaOwner(13), "Project Title", "TAC_activity_2", "Region",
                                "Falesti County", "Funding-2010-Actual Disbursements", "453,213",
                                "Funding-2011-Actual Commitments", "999,888", "Totals-Actual Commitments", "999,888",
                                "Totals-Actual Disbursements", "453,213").withCounts(1, -1),
                        new PaginatedReportAreaForTests(new AreaOwner(15), "Project Title",
                                "Proposed Project Cost 1 - USD", "Region", "Drochia County").withCounts(1, -1),
                        new PaginatedReportAreaForTests(new AreaOwner(17), "Project Title",
                                "Proposed Project Cost 2 - EUR", "Region", "Anenii Noi County").withCounts(1, -1),
                        new PaginatedReportAreaForTests(new AreaOwner(18), "Project Title", "Test MTEF directed",
                                "Region", "Anenii Noi County", "Funding-2010-Actual Disbursements", "143,777",
                                "Totals-Actual Disbursements", "143,777").withCounts(1, -1),
                        new PaginatedReportAreaForTests(new AreaOwner(19), "Project Title", "Pure MTEF Project",
                                "Region", "Cahul County").withCounts(1, -1),
                        new PaginatedReportAreaForTests(new AreaOwner(21), "Project Title", "activity with components",
                                "Region", "Anenii Noi County").withCounts(1, -1),
                        new PaginatedReportAreaForTests(new AreaOwner(23), "Project Title", "Project with documents",
                                "Region", "Balti County").withCounts(1, -1),
                        new PaginatedReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Region",
                                "Anenii Noi County", "Funding-2013-Actual Disbursements", "545,000",
                                "Totals-Actual Disbursements", "545,000").withCounts(1, -1),
                        new PaginatedReportAreaForTests(new AreaOwner(25), "Project Title", "mtef activity 1", "Region",
                                "").withCounts(1, -1));

        compareBodies("begin flat", cor, initFlatReport.getPage(0, 10));
    }

    @Test
    public void testMidFlat() {
        PaginatedReportAreaForTests cor = new PaginatedReportAreaForTests(null).withCounts(12, 44)
                .withContents("Project Title", "", "Region", "", "Funding-2006-Actual Commitments", "96,840,58", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "100,000", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "780,311", "Funding-2011-Actual Commitments", "1,213,119", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "25,000", "Funding-2012-Actual Disbursements", "12,000", "Funding-2013-Actual Commitments", "7,842,086", "Funding-2013-Actual Disbursements", "1,266,956", "Funding-2014-Actual Commitments", "8,159,813,77", "Funding-2014-Actual Disbursements", "710,200", "Funding-2015-Actual Commitments", "1,971,831,84", "Funding-2015-Actual Disbursements", "437,335", "Totals-Actual Commitments", "19,408,691,19", "Totals-Actual Disbursements", "3,206,802")
                .withChildren(
                  new PaginatedReportAreaForTests(new AreaOwner(31), "Project Title", "SSC Project 2", "Region", "Edinet County", "Funding-2013-Actual Commitments", "567,421", "Funding-2013-Actual Disbursements", "131,845", "Totals-Actual Commitments", "567,421", "Totals-Actual Disbursements", "131,845").withCounts(1, -1),
                  new PaginatedReportAreaForTests(new AreaOwner(32), "Project Title", "crazy funding 1", "Region", "Balti County", "Funding-2013-Actual Commitments", "333,333", "Totals-Actual Commitments", "333,333").withCounts(1, -1),
                  new PaginatedReportAreaForTests(new AreaOwner(33), "Project Title", "Activity with Zones", "Region", "Anenii Noi County, Balti County", "Funding-2013-Actual Commitments", "570,000", "Totals-Actual Commitments", "570,000").withCounts(1, -1),
                  new PaginatedReportAreaForTests(new AreaOwner(36), "Project Title", "Activity With Zones and Percentages", "Region", "Anenii Noi County, Balti County", "Funding-2013-Actual Commitments", "890,000", "Totals-Actual Commitments", "890,000").withCounts(1, -1),
                  new PaginatedReportAreaForTests(new AreaOwner(40), "Project Title", "SubNational no percentages", "Region", "Anenii Noi County, Balti County", "Funding-2014-Actual Commitments", "75,000", "Totals-Actual Commitments", "75,000").withCounts(1, -1),
                  new PaginatedReportAreaForTests(new AreaOwner(41), "Project Title", "Activity Linked With Pledge", "Region", "Chisinau City", "Funding-2014-Actual Commitments", "50,000", "Totals-Actual Commitments", "50,000").withCounts(1, -1),
                  new PaginatedReportAreaForTests(new AreaOwner(43), "Project Title", "Activity with primary_tertiary_program", "Region", "", "Funding-2014-Actual Commitments", "50,000", "Totals-Actual Commitments", "50,000").withCounts(1, -1),
                  new PaginatedReportAreaForTests(new AreaOwner(44), "Project Title", "activity with primary_program", "Region", "", "Funding-2014-Actual Commitments", "32,000", "Totals-Actual Commitments", "32,000").withCounts(1, -1),
                  new PaginatedReportAreaForTests(new AreaOwner(45), "Project Title", "activity with tertiary_program", "Region", "", "Funding-2014-Actual Commitments", "15,000", "Totals-Actual Commitments", "15,000").withCounts(1, -1),
                  new PaginatedReportAreaForTests(new AreaOwner(46), "Project Title", "pledged education activity 1", "Region", "Chisinau County", "Funding-2013-Actual Commitments", "1,700,000", "Funding-2014-Actual Commitments", "3,300,000", "Totals-Actual Commitments", "5,000,000").withCounts(1, -1),
                  new PaginatedReportAreaForTests(new AreaOwner(48), "Project Title", "pledged 2", "Region", "Cahul County", "Funding-2013-Actual Commitments", "2,670,000", "Funding-2014-Actual Commitments", "4,400,000", "Funding-2014-Actual Disbursements", "450,000", "Totals-Actual Commitments", "7,070,000", "Totals-Actual Disbursements", "450,000").withCounts(1, -1),
                  new PaginatedReportAreaForTests(new AreaOwner(50), "Project Title", "activity with capital spending", "Region", "Chisinau County", "Funding-2014-Actual Commitments", "65,760,63", "Funding-2014-Actual Disbursements", "80,000", "Totals-Actual Commitments", "65,760,63", "Totals-Actual Disbursements", "80,000").withCounts(1, -1)    );
        
        compareBodies("mid flat", cor, initFlatReport.getPage(15, 12));
    }

    @Test
    public void testEndFlat() {
        PaginatedReportAreaForTests cor = new PaginatedReportAreaForTests(null).withCounts(10, 44)
                .withContents("Project Title", "", "Region", "", "Funding-2006-Actual Commitments", "96,840,58",
                        "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "100,000",
                        "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0",
                        "Funding-2010-Actual Disbursements", "780,311", "Funding-2011-Actual Commitments", "1,213,119",
                        "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "25,000",
                        "Funding-2012-Actual Disbursements", "12,000", "Funding-2013-Actual Commitments", "7,842,086",
                        "Funding-2013-Actual Disbursements", "1,266,956", "Funding-2014-Actual Commitments",
                        "8,159,813,77", "Funding-2014-Actual Disbursements", "710,200",
                        "Funding-2015-Actual Commitments", "1,971,831,84", "Funding-2015-Actual Disbursements",
                        "437,335", "Totals-Actual Commitments", "19,408,691,19", "Totals-Actual Disbursements",
                        "3,206,802")
                .withChildren(
                        new PaginatedReportAreaForTests(new AreaOwner(67), "Project Title",
                                "third activity with agreements", "Region", "Chisinau City",
                                "Funding-2015-Actual Commitments", "123,456", "Totals-Actual Commitments", "123,456")
                                        .withCounts(1, -1),
                        new PaginatedReportAreaForTests(new AreaOwner(68), "Project Title",
                                "activity with incomplete agreement", "Region", "", "Funding-2015-Actual Commitments",
                                "123,000", "Totals-Actual Commitments", "123,000").withCounts(1, -1),
                        new PaginatedReportAreaForTests(new AreaOwner(69), "Project Title",
                                "Activity with planned disbursements", "Region", "",
                                "Funding-2014-Actual Disbursements", "200", "Funding-2015-Actual Disbursements", "570",
                                "Totals-Actual Disbursements", "770").withCounts(1, -1),
                        new PaginatedReportAreaForTests(new AreaOwner(70), "Project Title",
                                "Activity with both MTEFs and Act.Comms", "Region", "Balti County, Drochia County",
                                "Funding-2015-Actual Commitments", "888,000", "Totals-Actual Commitments", "888,000")
                                        .withCounts(1, -1),
                        new PaginatedReportAreaForTests(new AreaOwner(71), "Project Title",
                                "activity_with_disaster_response", "Region", "", "Funding-2014-Actual Commitments",
                                "33,000", "Funding-2015-Actual Commitments", "117,000", "Totals-Actual Commitments",
                                "150,000").withCounts(1, -1),
                        new PaginatedReportAreaForTests(new AreaOwner(73), "Project Title",
                                "activity with directed MTEFs", "Region", "Chisinau City",
                                "Funding-2015-Actual Commitments", "123,456", "Totals-Actual Commitments", "123,456")
                                        .withCounts(1, -1),
                        new PaginatedReportAreaForTests(new AreaOwner(76), "Project Title",
                                "activity with pipeline MTEFs and act. disb", "Region", "Chisinau County",
                                "Funding-2013-Actual Disbursements", "35,000", "Funding-2014-Actual Disbursements",
                                "75,000", "Totals-Actual Disbursements", "110,000").withCounts(1, -1),
                        new PaginatedReportAreaForTests(new AreaOwner(77), "Project Title", "execution rate activity",
                                "Region", "Chisinau City, Dubasari County", "Funding-2014-Actual Disbursements",
                                "55,000", "Funding-2015-Actual Disbursements", "35,000", "Totals-Actual Disbursements",
                                "90,000").withCounts(1, -1),
                        new PaginatedReportAreaForTests(new AreaOwner(78), "Project Title", "activity with many MTEFs",
                                "Region", "Drochia County", "Funding-2015-Actual Disbursements", "80,000",
                                "Totals-Actual Disbursements", "80,000").withCounts(1, -1),
                        new PaginatedReportAreaForTests(new AreaOwner(79), "Project Title", "with weird currencies",
                                "Region", "", "Funding-2014-Actual Commitments", "3,632,14",
                                "Funding-2015-Actual Commitments", "93,930,84", "Totals-Actual Commitments",
                                "97,562,98").withCounts(1, -1));

        compareBodies("end flat", cor, initFlatReport.getPage(34, 10));
    }

    @Test
    public void testCutEndFlat() {
        PaginatedReportAreaForTests cor = new PaginatedReportAreaForTests(null).withCounts(4, 44)
                .withContents("Project Title", "", "Region", "", "Funding-2006-Actual Commitments", "96,840,58",
                        "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "100,000",
                        "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0",
                        "Funding-2010-Actual Disbursements", "780,311", "Funding-2011-Actual Commitments", "1,213,119",
                        "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "25,000",
                        "Funding-2012-Actual Disbursements", "12,000", "Funding-2013-Actual Commitments", "7,842,086",
                        "Funding-2013-Actual Disbursements", "1,266,956", "Funding-2014-Actual Commitments",
                        "8,159,813,77", "Funding-2014-Actual Disbursements", "710,200",
                        "Funding-2015-Actual Commitments", "1,971,831,84", "Funding-2015-Actual Disbursements",
                        "437,335", "Totals-Actual Commitments", "19,408,691,19", "Totals-Actual Disbursements",
                        "3,206,802")
                .withChildren(
                        new PaginatedReportAreaForTests(new AreaOwner(76), "Project Title",
                                "activity with pipeline MTEFs and act. disb", "Region", "Chisinau County",
                                "Funding-2013-Actual Disbursements", "35,000", "Funding-2014-Actual Disbursements",
                                "75,000", "Totals-Actual Disbursements", "110,000").withCounts(1, -1),
                        new PaginatedReportAreaForTests(new AreaOwner(77), "Project Title", "execution rate activity",
                                "Region", "Chisinau City, Dubasari County", "Funding-2014-Actual Disbursements",
                                "55,000", "Funding-2015-Actual Disbursements", "35,000", "Totals-Actual Disbursements",
                                "90,000").withCounts(1, -1),
                        new PaginatedReportAreaForTests(new AreaOwner(78), "Project Title", "activity with many MTEFs",
                                "Region", "Drochia County", "Funding-2015-Actual Disbursements", "80,000",
                                "Totals-Actual Disbursements", "80,000").withCounts(1, -1),
                        new PaginatedReportAreaForTests(new AreaOwner(79), "Project Title", "with weird currencies",
                                "Region", "", "Funding-2014-Actual Commitments", "3,632,14",
                                "Funding-2015-Actual Commitments", "93,930,84", "Totals-Actual Commitments",
                                "97,562,98").withCounts(1, -1));

        compareBodies("cut end flat", cor, initFlatReport.getPage(40, 10));
    }

    @Test
    public void testFullHier() {
        PaginatedReportAreaForTests cor = new PaginatedReportAreaForTests(null).withCounts(37, 37)
                .withContents("Region", "", "Project Title", "", "Funding-2006-Actual Commitments", "96,840,58", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "100,000", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "780,311", "Funding-2011-Actual Commitments", "1,213,119", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "25,000", "Funding-2012-Actual Disbursements", "12,000", "Funding-2013-Actual Commitments", "7,842,086", "Funding-2013-Actual Disbursements", "1,266,956", "Funding-2014-Actual Commitments", "8,159,813,77", "Funding-2014-Actual Disbursements", "710,200", "Funding-2015-Actual Commitments", "1,971,831,84", "Funding-2015-Actual Disbursements", "437,335", "Totals-Actual Commitments", "19,408,691,19", "Totals-Actual Disbursements", "3,206,802")
                .withChildren(
                  new PaginatedReportAreaForTests(new AreaOwner("Region", "Anenii Noi County")).withCounts(8, 8)
                  .withContents("Project Title", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "143,777", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "1,574,332", "Funding-2013-Actual Disbursements", "1,100,111", "Funding-2014-Actual Commitments", "37,500", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "1,611,832", "Totals-Actual Disbursements", "1,243,888", "Region", "Anenii Noi County")
                  .withChildren(
                    new PaginatedReportAreaForTests(new AreaOwner(18), "Project Title", "Test MTEF directed", "Funding-2010-Actual Disbursements", "143,777", "Totals-Actual Disbursements", "143,777").withCounts(1, -1),
                    new PaginatedReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Funding-2013-Actual Disbursements", "545,000", "Totals-Actual Disbursements", "545,000").withCounts(1, -1),
                    new PaginatedReportAreaForTests(new AreaOwner(28), "Project Title", "ptc activity 1", "Funding-2013-Actual Commitments", "666,777", "Totals-Actual Commitments", "666,777").withCounts(1, -1),
                    new PaginatedReportAreaForTests(new AreaOwner(29), "Project Title", "ptc activity 2", "Funding-2013-Actual Commitments", "333,222", "Totals-Actual Commitments", "333,222").withCounts(1, -1),
                    new PaginatedReportAreaForTests(new AreaOwner(30), "Project Title", "SSC Project 1", "Funding-2013-Actual Commitments", "111,333", "Funding-2013-Actual Disbursements", "555,111", "Totals-Actual Commitments", "111,333", "Totals-Actual Disbursements", "555,111").withCounts(1, -1),
                    new PaginatedReportAreaForTests(new AreaOwner(33), "Project Title", "Activity with Zones", "Funding-2013-Actual Commitments", "285,000", "Totals-Actual Commitments", "285,000").withCounts(1, -1),
                    new PaginatedReportAreaForTests(new AreaOwner(36), "Project Title", "Activity With Zones and Percentages", "Funding-2013-Actual Commitments", "178,000", "Totals-Actual Commitments", "178,000").withCounts(1, -1),
                    new PaginatedReportAreaForTests(new AreaOwner(40), "Project Title", "SubNational no percentages", "Funding-2014-Actual Commitments", "37,500", "Totals-Actual Commitments", "37,500").withCounts(1, -1)      ),
                  new PaginatedReportAreaForTests(new AreaOwner("Region", "Balti County")).withCounts(7, 7)
                  .withContents("Project Title", "", "Funding-2006-Actual Commitments", "53,262,32", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "1,330,333", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "37,500", "Funding-2014-Actual Disbursements", "27,500", "Funding-2015-Actual Commitments", "723,189", "Funding-2015-Actual Disbursements", "321,765", "Totals-Actual Commitments", "2,144,284,32", "Totals-Actual Disbursements", "349,265", "Region", "Balti County")
                  .withChildren(
                    new PaginatedReportAreaForTests(new AreaOwner(32), "Project Title", "crazy funding 1", "Funding-2013-Actual Commitments", "333,333", "Totals-Actual Commitments", "333,333").withCounts(1, -1),
                    new PaginatedReportAreaForTests(new AreaOwner(33), "Project Title", "Activity with Zones", "Funding-2013-Actual Commitments", "285,000", "Totals-Actual Commitments", "285,000").withCounts(1, -1),
                    new PaginatedReportAreaForTests(new AreaOwner(36), "Project Title", "Activity With Zones and Percentages", "Funding-2013-Actual Commitments", "712,000", "Totals-Actual Commitments", "712,000").withCounts(1, -1),
                    new PaginatedReportAreaForTests(new AreaOwner(40), "Project Title", "SubNational no percentages", "Funding-2014-Actual Commitments", "37,500", "Totals-Actual Commitments", "37,500").withCounts(1, -1),
                    new PaginatedReportAreaForTests(new AreaOwner(52), "Project Title", "activity with contracting agency", "Funding-2006-Actual Commitments", "53,262,32", "Funding-2014-Actual Disbursements", "27,500", "Totals-Actual Commitments", "53,262,32", "Totals-Actual Disbursements", "27,500").withCounts(1, -1),
                    new PaginatedReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Funding-2015-Actual Commitments", "456,789", "Funding-2015-Actual Disbursements", "321,765", "Totals-Actual Commitments", "456,789", "Totals-Actual Disbursements", "321,765").withCounts(1, -1),
                    new PaginatedReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms", "Funding-2015-Actual Commitments", "266,400", "Totals-Actual Commitments", "266,400").withCounts(1, -1)      ),
                  new PaginatedReportAreaForTests(new AreaOwner("Region", "Cahul County")).withCounts(1, 1).withContents("Project Title", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "2,670,000", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "4,400,000", "Funding-2014-Actual Disbursements", "450,000", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "7,070,000", "Totals-Actual Disbursements", "450,000", "Region", "Cahul County")
                  .withChildren(
                    new PaginatedReportAreaForTests(new AreaOwner(48), "Project Title", "pledged 2", "Funding-2013-Actual Commitments", "2,670,000", "Funding-2014-Actual Commitments", "4,400,000", "Funding-2014-Actual Disbursements", "450,000", "Totals-Actual Commitments", "7,070,000", "Totals-Actual Disbursements", "450,000").withCounts(1, -1)      ),
                  new PaginatedReportAreaForTests(new AreaOwner("Region", "Chisinau City")).withCounts(4, 4)
                  .withContents("Project Title", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "50,000", "Funding-2014-Actual Disbursements", "27,500", "Funding-2015-Actual Commitments", "246,912", "Funding-2015-Actual Disbursements", "17,500", "Totals-Actual Commitments", "296,912", "Totals-Actual Disbursements", "45,000", "Region", "Chisinau City")
                  .withChildren(
                    new PaginatedReportAreaForTests(new AreaOwner(41), "Project Title", "Activity Linked With Pledge", "Funding-2014-Actual Commitments", "50,000", "Totals-Actual Commitments", "50,000").withCounts(1, -1),
                    new PaginatedReportAreaForTests(new AreaOwner(67), "Project Title", "third activity with agreements", "Funding-2015-Actual Commitments", "123,456", "Totals-Actual Commitments", "123,456").withCounts(1, -1),
                    new PaginatedReportAreaForTests(new AreaOwner(73), "Project Title", "activity with directed MTEFs", "Funding-2015-Actual Commitments", "123,456", "Totals-Actual Commitments", "123,456").withCounts(1, -1),
                    new PaginatedReportAreaForTests(new AreaOwner(77), "Project Title", "execution rate activity", "Funding-2014-Actual Disbursements", "27,500", "Funding-2015-Actual Disbursements", "17,500", "Totals-Actual Disbursements", "45,000").withCounts(1, -1)      ),
                  new PaginatedReportAreaForTests(new AreaOwner("Region", "Chisinau County")).withCounts(4, 4)
                  .withContents("Project Title", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "1,700,000", "Funding-2013-Actual Disbursements", "35,000", "Funding-2014-Actual Commitments", "3,365,760,63", "Funding-2014-Actual Disbursements", "155,000", "Funding-2015-Actual Commitments", "1,200", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "5,066,960,63", "Totals-Actual Disbursements", "190,000", "Region", "Chisinau County")
                  .withChildren(
                    new PaginatedReportAreaForTests(new AreaOwner(46), "Project Title", "pledged education activity 1", "Funding-2013-Actual Commitments", "1,700,000", "Funding-2014-Actual Commitments", "3,300,000", "Totals-Actual Commitments", "5,000,000").withCounts(1, -1),
                    new PaginatedReportAreaForTests(new AreaOwner(50), "Project Title", "activity with capital spending", "Funding-2014-Actual Commitments", "65,760,63", "Funding-2014-Actual Disbursements", "80,000", "Totals-Actual Commitments", "65,760,63", "Totals-Actual Disbursements", "80,000").withCounts(1, -1),
                    new PaginatedReportAreaForTests(new AreaOwner(66), "Project Title", "Activity 2 with multiple agreements", "Funding-2015-Actual Commitments", "1,200", "Totals-Actual Commitments", "1,200").withCounts(1, -1),
                    new PaginatedReportAreaForTests(new AreaOwner(76), "Project Title", "activity with pipeline MTEFs and act. disb", "Funding-2013-Actual Disbursements", "35,000", "Funding-2014-Actual Disbursements", "75,000", "Totals-Actual Disbursements", "110,000").withCounts(1, -1)      ),
                  new PaginatedReportAreaForTests(new AreaOwner("Region", "Drochia County")).withCounts(2, 2)
                  .withContents("Project Title", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "621,600", "Funding-2015-Actual Disbursements", "80,000", "Totals-Actual Commitments", "621,600", "Totals-Actual Disbursements", "80,000", "Region", "Drochia County")
                  .withChildren(
                    new PaginatedReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms", "Funding-2015-Actual Commitments", "621,600", "Totals-Actual Commitments", "621,600").withCounts(1, -1),
                    new PaginatedReportAreaForTests(new AreaOwner(78), "Project Title", "activity with many MTEFs", "Funding-2015-Actual Disbursements", "80,000", "Totals-Actual Disbursements", "80,000").withCounts(1, -1)      ),
                  new PaginatedReportAreaForTests(new AreaOwner("Region", "Dubasari County")).withCounts(2, 2)
                  .withContents("Project Title", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "123,321", "Funding-2011-Actual Commitments", "213,231", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "27,500", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "17,500", "Totals-Actual Commitments", "213,231", "Totals-Actual Disbursements", "168,321", "Region", "Dubasari County")
                  .withChildren(
                    new PaginatedReportAreaForTests(new AreaOwner(12), "Project Title", "TAC_activity_1", "Funding-2010-Actual Disbursements", "123,321", "Funding-2011-Actual Commitments", "213,231", "Totals-Actual Commitments", "213,231", "Totals-Actual Disbursements", "123,321").withCounts(1, -1),
                    new PaginatedReportAreaForTests(new AreaOwner(77), "Project Title", "execution rate activity", "Funding-2014-Actual Disbursements", "27,500", "Funding-2015-Actual Disbursements", "17,500", "Totals-Actual Disbursements", "45,000").withCounts(1, -1)      ),
                  new PaginatedReportAreaForTests(new AreaOwner("Region", "Edinet County")).withCounts(1, 1).withContents("Project Title", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "567,421", "Funding-2013-Actual Disbursements", "131,845", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "567,421", "Totals-Actual Disbursements", "131,845", "Region", "Edinet County")
                  .withChildren(
                    new PaginatedReportAreaForTests(new AreaOwner(31), "Project Title", "SSC Project 2", "Funding-2013-Actual Commitments", "567,421", "Funding-2013-Actual Disbursements", "131,845", "Totals-Actual Commitments", "567,421", "Totals-Actual Disbursements", "131,845").withCounts(1, -1)      ),
                  new PaginatedReportAreaForTests(new AreaOwner("Region", "Falesti County")).withCounts(1, 1).withContents("Project Title", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "453,213", "Funding-2011-Actual Commitments", "999,888", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "999,888", "Totals-Actual Disbursements", "453,213", "Region", "Falesti County")
                  .withChildren(
                    new PaginatedReportAreaForTests(new AreaOwner(13), "Project Title", "TAC_activity_2", "Funding-2010-Actual Disbursements", "453,213", "Funding-2011-Actual Commitments", "999,888", "Totals-Actual Commitments", "999,888", "Totals-Actual Disbursements", "453,213").withCounts(1, -1)      ),
                  new PaginatedReportAreaForTests(new AreaOwner("Region", "Transnistrian Region")).withCounts(2, 2)
                  .withContents("Project Title", "", "Funding-2006-Actual Commitments", "43,578,26", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "123,321", "Funding-2014-Actual Disbursements", "22,500", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "166,899,26", "Totals-Actual Disbursements", "22,500", "Region", "Transnistrian Region")
                  .withChildren(
                    new PaginatedReportAreaForTests(new AreaOwner(52), "Project Title", "activity with contracting agency", "Funding-2006-Actual Commitments", "43,578,26", "Funding-2014-Actual Disbursements", "22,500", "Totals-Actual Commitments", "43,578,26", "Totals-Actual Disbursements", "22,500").withCounts(1, -1),
                    new PaginatedReportAreaForTests(new AreaOwner(61), "Project Title", "activity-with-unfunded-components", "Funding-2014-Actual Commitments", "123,321", "Totals-Actual Commitments", "123,321").withCounts(1, -1)      ),
                  new PaginatedReportAreaForTests(new AreaOwner("Region", "Region: Undefined")).withCounts(11, 11)
                  .withContents("Project Title", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "100,000", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "60,000", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "25,000", "Funding-2012-Actual Disbursements", "12,000", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "145,732,14", "Funding-2014-Actual Disbursements", "200", "Funding-2015-Actual Commitments", "378,930,84", "Funding-2015-Actual Disbursements", "570", "Totals-Actual Commitments", "649,662,98", "Totals-Actual Disbursements", "72,770", "Region", "Region: Undefined")
                  .withChildren(
                    new PaginatedReportAreaForTests(new AreaOwner(26), "Project Title", "date-filters-activity", "Funding-2009-Actual Commitments", "100,000", "Funding-2010-Actual Disbursements", "60,000", "Funding-2012-Actual Commitments", "25,000", "Funding-2012-Actual Disbursements", "12,000", "Totals-Actual Commitments", "125,000", "Totals-Actual Disbursements", "72,000").withCounts(1, -1),
                    new PaginatedReportAreaForTests(new AreaOwner(43), "Project Title", "Activity with primary_tertiary_program", "Funding-2014-Actual Commitments", "50,000", "Totals-Actual Commitments", "50,000").withCounts(1, -1),
                    new PaginatedReportAreaForTests(new AreaOwner(44), "Project Title", "activity with primary_program", "Funding-2014-Actual Commitments", "32,000", "Totals-Actual Commitments", "32,000").withCounts(1, -1),
                    new PaginatedReportAreaForTests(new AreaOwner(45), "Project Title", "activity with tertiary_program", "Funding-2014-Actual Commitments", "15,000", "Totals-Actual Commitments", "15,000").withCounts(1, -1),
                    new PaginatedReportAreaForTests(new AreaOwner(53), "Project Title", "new activity with contracting", "Funding-2014-Actual Commitments", "12,000", "Totals-Actual Commitments", "12,000").withCounts(1, -1),
                    new PaginatedReportAreaForTests(new AreaOwner(63), "Project Title", "activity with funded components", "Funding-2014-Actual Commitments", "100", "Totals-Actual Commitments", "100").withCounts(1, -1),
                    new PaginatedReportAreaForTests(new AreaOwner(64), "Project Title", "Unvalidated activity", "Funding-2015-Actual Commitments", "45,000", "Totals-Actual Commitments", "45,000").withCounts(1, -1),
                    new PaginatedReportAreaForTests(new AreaOwner(68), "Project Title", "activity with incomplete agreement", "Funding-2015-Actual Commitments", "123,000", "Totals-Actual Commitments", "123,000").withCounts(1, -1),
                    new PaginatedReportAreaForTests(new AreaOwner(69), "Project Title", "Activity with planned disbursements", "Funding-2014-Actual Disbursements", "200", "Funding-2015-Actual Disbursements", "570", "Totals-Actual Disbursements", "770").withCounts(1, -1),
                    new PaginatedReportAreaForTests(new AreaOwner(71), "Project Title", "activity_with_disaster_response", "Funding-2014-Actual Commitments", "33,000", "Funding-2015-Actual Commitments", "117,000", "Totals-Actual Commitments", "150,000").withCounts(1, -1),
                    new PaginatedReportAreaForTests(new AreaOwner(79), "Project Title", "with weird currencies", "Funding-2014-Actual Commitments", "3,632,14", "Funding-2015-Actual Commitments", "93,930,84", "Totals-Actual Commitments", "97,562,98").withCounts(1, -1)));


        compareBodies("full hier", cor, initHierReport.getPage(0, 150));
    }

    @Test
    public void testBeginHier() {
        PaginatedReportAreaForTests cor = new PaginatedReportAreaForTests(null).withCounts(9, 37)
                .withContents("Region", "", "Project Title", "", "Funding-2006-Actual Commitments", "96,840,58",
                        "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "100,000",
                        "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0",
                        "Funding-2010-Actual Disbursements", "780,311", "Funding-2011-Actual Commitments", "1,213,119",
                        "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "25,000",
                        "Funding-2012-Actual Disbursements", "12,000", "Funding-2013-Actual Commitments", "7,842,086",
                        "Funding-2013-Actual Disbursements", "1,266,956", "Funding-2014-Actual Commitments",
                        "8,159,813,77", "Funding-2014-Actual Disbursements", "710,200",
                        "Funding-2015-Actual Commitments", "1,971,831,84", "Funding-2015-Actual Disbursements",
                        "437,335", "Totals-Actual Commitments", "19,408,691,19", "Totals-Actual Disbursements",
                        "3,206,802")
                .withChildren(new PaginatedReportAreaForTests(new AreaOwner("Region", "Anenii Noi County"))
                        .withCounts(8, 8)
                        .withContents("Project Title", "", "Funding-2006-Actual Commitments", "0",
                                "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0",
                                "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0",
                                "Funding-2010-Actual Disbursements", "143,777", "Funding-2011-Actual Commitments", "0",
                                "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0",
                                "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments",
                                "1,574,332", "Funding-2013-Actual Disbursements", "1,100,111",
                                "Funding-2014-Actual Commitments", "37,500", "Funding-2014-Actual Disbursements", "0",
                                "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0",
                                "Totals-Actual Commitments", "1,611,832", "Totals-Actual Disbursements", "1,243,888",
                                "Region", "Anenii Noi County")
                        .withChildren(
                                new PaginatedReportAreaForTests(new AreaOwner(18), "Project Title",
                                        "Test MTEF directed", "Funding-2010-Actual Disbursements", "143,777",
                                        "Totals-Actual Disbursements", "143,777").withCounts(1, -1),
                                new PaginatedReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water",
                                        "Funding-2013-Actual Disbursements", "545,000", "Totals-Actual Disbursements",
                                        "545,000").withCounts(1, -1),
                                new PaginatedReportAreaForTests(new AreaOwner(28), "Project Title", "ptc activity 1",
                                        "Funding-2013-Actual Commitments", "666,777", "Totals-Actual Commitments",
                                        "666,777").withCounts(1, -1),
                                new PaginatedReportAreaForTests(new AreaOwner(29), "Project Title", "ptc activity 2",
                                        "Funding-2013-Actual Commitments", "333,222", "Totals-Actual Commitments",
                                        "333,222").withCounts(1, -1),
                                new PaginatedReportAreaForTests(new AreaOwner(30), "Project Title", "SSC Project 1",
                                        "Funding-2013-Actual Commitments", "111,333",
                                        "Funding-2013-Actual Disbursements", "555,111", "Totals-Actual Commitments",
                                        "111,333", "Totals-Actual Disbursements", "555,111").withCounts(1, -1),
                                new PaginatedReportAreaForTests(new AreaOwner(33), "Project Title",
                                        "Activity with Zones", "Funding-2013-Actual Commitments", "285,000",
                                        "Totals-Actual Commitments", "285,000").withCounts(1, -1),
                                new PaginatedReportAreaForTests(new AreaOwner(36), "Project Title",
                                        "Activity With Zones and Percentages", "Funding-2013-Actual Commitments",
                                        "178,000", "Totals-Actual Commitments", "178,000").withCounts(1, -1),
                                new PaginatedReportAreaForTests(new AreaOwner(40), "Project Title",
                                        "SubNational no percentages", "Funding-2014-Actual Commitments", "37,500",
                                        "Totals-Actual Commitments", "37,500").withCounts(1, -1)),
                        new PaginatedReportAreaForTests(new AreaOwner("Region", "Balti County")).withCounts(2, 7)
                                .withContents("Project Title", "", "Funding-2006-Actual Commitments", "53,262,32",
                                        "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments",
                                        "0", "Funding-2009-Actual Disbursements", "0",
                                        "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements",
                                        "0", "Funding-2011-Actual Commitments", "0",
                                        "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments",
                                        "0", "Funding-2012-Actual Disbursements", "0",
                                        "Funding-2013-Actual Commitments", "1,330,333",
                                        "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments",
                                        "37,500", "Funding-2014-Actual Disbursements", "27,500",
                                        "Funding-2015-Actual Commitments", "723,189",
                                        "Funding-2015-Actual Disbursements", "321,765", "Totals-Actual Commitments",
                                        "2,144,284,32", "Totals-Actual Disbursements", "349,265", "Region",
                                        "Balti County")
                                .withChildren(
                                        new PaginatedReportAreaForTests(new AreaOwner(32), "Project Title",
                                                "crazy funding 1", "Funding-2013-Actual Commitments", "333,333",
                                                "Totals-Actual Commitments", "333,333").withCounts(1, -1),
                                        new PaginatedReportAreaForTests(new AreaOwner(33), "Project Title",
                                                "Activity with Zones", "Funding-2013-Actual Commitments", "285,000",
                                                "Totals-Actual Commitments", "285,000").withCounts(1, -1)));

        compareBodies("begin hier", cor, initHierReport.getPage(0, 10));
    }

    @Test
    public void testMidHier() {
        PaginatedReportAreaForTests cor = new PaginatedReportAreaForTests(null).withCounts(12, 37)
                .withContents("Region", "", "Project Title", "", "Funding-2006-Actual Commitments", "96,840,58", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "100,000", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "780,311", "Funding-2011-Actual Commitments", "1,213,119", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "25,000", "Funding-2012-Actual Disbursements", "12,000", "Funding-2013-Actual Commitments", "7,842,086", "Funding-2013-Actual Disbursements", "1,266,956", "Funding-2014-Actual Commitments", "8,159,813,77", "Funding-2014-Actual Disbursements", "710,200", "Funding-2015-Actual Commitments", "1,971,831,84", "Funding-2015-Actual Disbursements", "437,335", "Totals-Actual Commitments", "19,408,691,19", "Totals-Actual Disbursements", "3,206,802")
                .withChildren(
                  new PaginatedReportAreaForTests(new AreaOwner("Region", "Cahul County")).withCounts(1, 1).withContents("Project Title", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "2,670,000", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "4,400,000", "Funding-2014-Actual Disbursements", "450,000", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "7,070,000", "Totals-Actual Disbursements", "450,000", "Region", "Cahul County")
                  .withChildren(
                    new PaginatedReportAreaForTests(new AreaOwner(48), "Project Title", "pledged 2", "Funding-2013-Actual Commitments", "2,670,000", "Funding-2014-Actual Commitments", "4,400,000", "Funding-2014-Actual Disbursements", "450,000", "Totals-Actual Commitments", "7,070,000", "Totals-Actual Disbursements", "450,000").withCounts(1, -1)      ),
                  new PaginatedReportAreaForTests(new AreaOwner("Region", "Chisinau City")).withCounts(4, 4)
                  .withContents("Project Title", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "50,000", "Funding-2014-Actual Disbursements", "27,500", "Funding-2015-Actual Commitments", "246,912", "Funding-2015-Actual Disbursements", "17,500", "Totals-Actual Commitments", "296,912", "Totals-Actual Disbursements", "45,000", "Region", "Chisinau City")
                  .withChildren(
                    new PaginatedReportAreaForTests(new AreaOwner(41), "Project Title", "Activity Linked With Pledge", "Funding-2014-Actual Commitments", "50,000", "Totals-Actual Commitments", "50,000").withCounts(1, -1),
                    new PaginatedReportAreaForTests(new AreaOwner(67), "Project Title", "third activity with agreements", "Funding-2015-Actual Commitments", "123,456", "Totals-Actual Commitments", "123,456").withCounts(1, -1),
                    new PaginatedReportAreaForTests(new AreaOwner(73), "Project Title", "activity with directed MTEFs", "Funding-2015-Actual Commitments", "123,456", "Totals-Actual Commitments", "123,456").withCounts(1, -1),
                    new PaginatedReportAreaForTests(new AreaOwner(77), "Project Title", "execution rate activity", "Funding-2014-Actual Disbursements", "27,500", "Funding-2015-Actual Disbursements", "17,500", "Totals-Actual Disbursements", "45,000").withCounts(1, -1)      ),
                  new PaginatedReportAreaForTests(new AreaOwner("Region", "Chisinau County")).withCounts(4, 4)
                  .withContents("Project Title", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "1,700,000", "Funding-2013-Actual Disbursements", "35,000", "Funding-2014-Actual Commitments", "3,365,760,63", "Funding-2014-Actual Disbursements", "155,000", "Funding-2015-Actual Commitments", "1,200", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "5,066,960,63", "Totals-Actual Disbursements", "190,000", "Region", "Chisinau County")
                  .withChildren(
                    new PaginatedReportAreaForTests(new AreaOwner(46), "Project Title", "pledged education activity 1", "Funding-2013-Actual Commitments", "1,700,000", "Funding-2014-Actual Commitments", "3,300,000", "Totals-Actual Commitments", "5,000,000").withCounts(1, -1),
                    new PaginatedReportAreaForTests(new AreaOwner(50), "Project Title", "activity with capital spending", "Funding-2014-Actual Commitments", "65,760,63", "Funding-2014-Actual Disbursements", "80,000", "Totals-Actual Commitments", "65,760,63", "Totals-Actual Disbursements", "80,000").withCounts(1, -1),
                    new PaginatedReportAreaForTests(new AreaOwner(66), "Project Title", "Activity 2 with multiple agreements", "Funding-2015-Actual Commitments", "1,200", "Totals-Actual Commitments", "1,200").withCounts(1, -1),
                    new PaginatedReportAreaForTests(new AreaOwner(76), "Project Title", "activity with pipeline MTEFs and act. disb", "Funding-2013-Actual Disbursements", "35,000", "Funding-2014-Actual Disbursements", "75,000", "Totals-Actual Disbursements", "110,000").withCounts(1, -1)      ),
                  new PaginatedReportAreaForTests(new AreaOwner("Region", "Drochia County")).withCounts(2, 2)
                  .withContents("Project Title", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "621,600", "Funding-2015-Actual Disbursements", "80,000", "Totals-Actual Commitments", "621,600", "Totals-Actual Disbursements", "80,000", "Region", "Drochia County")
                  .withChildren(
                    new PaginatedReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms", "Funding-2015-Actual Commitments", "621,600", "Totals-Actual Commitments", "621,600").withCounts(1, -1),
                    new PaginatedReportAreaForTests(new AreaOwner(78), "Project Title", "activity with many MTEFs", "Funding-2015-Actual Disbursements", "80,000", "Totals-Actual Disbursements", "80,000").withCounts(1, -1)      ),
                  new PaginatedReportAreaForTests(new AreaOwner("Region", "Dubasari County")).withCounts(1, 2).withContents("Project Title", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "123,321", "Funding-2011-Actual Commitments", "213,231", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "27,500", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "17,500", "Totals-Actual Commitments", "213,231", "Totals-Actual Disbursements", "168,321", "Region", "Dubasari County")
                  .withChildren(
                    new PaginatedReportAreaForTests(new AreaOwner(12), "Project Title", "TAC_activity_1", "Funding-2010-Actual Disbursements", "123,321", "Funding-2011-Actual Commitments", "213,231", "Totals-Actual Commitments", "213,231", "Totals-Actual Disbursements", "123,321").withCounts(1, -1)      )    );

        compareBodies("mid hier", cor, initHierReport.getPage(15, 12));
    }

    @Test
    public void testEndHier() {
        PaginatedReportAreaForTests cor = new PaginatedReportAreaForTests(null).withCounts(9, 37)
                .withContents("Region", "", "Project Title", "", "Funding-2006-Actual Commitments", "96,840,58",
                        "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "100,000",
                        "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0",
                        "Funding-2010-Actual Disbursements", "780,311", "Funding-2011-Actual Commitments", "1,213,119",
                        "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "25,000",
                        "Funding-2012-Actual Disbursements", "12,000", "Funding-2013-Actual Commitments", "7,842,086",
                        "Funding-2013-Actual Disbursements", "1,266,956", "Funding-2014-Actual Commitments",
                        "8,159,813,77", "Funding-2014-Actual Disbursements", "710,200",
                        "Funding-2015-Actual Commitments", "1,971,831,84", "Funding-2015-Actual Disbursements",
                        "437,335", "Totals-Actual Commitments", "19,408,691,19", "Totals-Actual Disbursements",
                        "3,206,802")
                .withChildren(new PaginatedReportAreaForTests(new AreaOwner("Region", "Region: Undefined"))
                        .withCounts(9, 11)
                        .withContents("Project Title", "", "Funding-2006-Actual Commitments", "0",
                                "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "100,000",
                                "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0",
                                "Funding-2010-Actual Disbursements", "60,000", "Funding-2011-Actual Commitments", "0",
                                "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "25,000",
                                "Funding-2012-Actual Disbursements", "12,000", "Funding-2013-Actual Commitments", "0",
                                "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments",
                                "145,732,14", "Funding-2014-Actual Disbursements", "200",
                                "Funding-2015-Actual Commitments", "378,930,84", "Funding-2015-Actual Disbursements",
                                "570", "Totals-Actual Commitments", "649,662,98", "Totals-Actual Disbursements",
                                "72,770", "Region", "Region: Undefined")
                        .withChildren(
                                new PaginatedReportAreaForTests(new AreaOwner(44), "Project Title",
                                        "activity with primary_program", "Funding-2014-Actual Commitments", "32,000",
                                        "Totals-Actual Commitments", "32,000").withCounts(1, -1),
                                new PaginatedReportAreaForTests(new AreaOwner(45), "Project Title",
                                        "activity with tertiary_program", "Funding-2014-Actual Commitments", "15,000",
                                        "Totals-Actual Commitments", "15,000").withCounts(1, -1),
                                new PaginatedReportAreaForTests(new AreaOwner(53), "Project Title",
                                        "new activity with contracting", "Funding-2014-Actual Commitments", "12,000",
                                        "Totals-Actual Commitments", "12,000").withCounts(1, -1),
                                new PaginatedReportAreaForTests(new AreaOwner(63), "Project Title",
                                        "activity with funded components", "Funding-2014-Actual Commitments", "100",
                                        "Totals-Actual Commitments", "100").withCounts(1, -1),
                                new PaginatedReportAreaForTests(new AreaOwner(64), "Project Title",
                                        "Unvalidated activity", "Funding-2015-Actual Commitments", "45,000",
                                        "Totals-Actual Commitments", "45,000").withCounts(1, -1),
                                new PaginatedReportAreaForTests(new AreaOwner(68), "Project Title",
                                        "activity with incomplete agreement", "Funding-2015-Actual Commitments",
                                        "123,000", "Totals-Actual Commitments", "123,000").withCounts(1, -1),
                                new PaginatedReportAreaForTests(new AreaOwner(69), "Project Title",
                                        "Activity with planned disbursements", "Funding-2014-Actual Disbursements",
                                        "200", "Funding-2015-Actual Disbursements", "570",
                                        "Totals-Actual Disbursements", "770").withCounts(1, -1),
                                new PaginatedReportAreaForTests(new AreaOwner(71), "Project Title",
                                        "activity_with_disaster_response", "Funding-2014-Actual Commitments", "33,000",
                                        "Funding-2015-Actual Commitments", "117,000", "Totals-Actual Commitments",
                                        "150,000").withCounts(1, -1),
                                new PaginatedReportAreaForTests(new AreaOwner(79), "Project Title",
                                        "with weird currencies", "Funding-2014-Actual Commitments", "3,632,14",
                                        "Funding-2015-Actual Commitments", "93,930,84", "Totals-Actual Commitments",
                                        "97,562,98").withCounts(1, -1)));

        compareBodies("end hier", cor, initHierReport.getPage(34, 10));
    }

    @Test
    public void testCutEndHier() {
        PaginatedReportAreaForTests cor = new PaginatedReportAreaForTests(null).withCounts(3, 37).withContents("Region", "", "Project Title", "", "Funding-2006-Actual Commitments", "96,840,58", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "100,000", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "780,311", "Funding-2011-Actual Commitments", "1,213,119", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "25,000", "Funding-2012-Actual Disbursements", "12,000", "Funding-2013-Actual Commitments", "7,842,086", "Funding-2013-Actual Disbursements", "1,266,956", "Funding-2014-Actual Commitments", "8,159,813,77", "Funding-2014-Actual Disbursements", "710,200", "Funding-2015-Actual Commitments", "1,971,831,84", "Funding-2015-Actual Disbursements", "437,335", "Totals-Actual Commitments", "19,408,691,19", "Totals-Actual Disbursements", "3,206,802")
            .withChildren(
                      new PaginatedReportAreaForTests(new AreaOwner("Region", "Region: Undefined")).withCounts(3, 11)
                      .withContents("Project Title", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "100,000", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "60,000", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "25,000", "Funding-2012-Actual Disbursements", "12,000", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "145,732,14", "Funding-2014-Actual Disbursements", "200", "Funding-2015-Actual Commitments", "378,930,84", "Funding-2015-Actual Disbursements", "570", "Totals-Actual Commitments", "649,662,98", "Totals-Actual Disbursements", "72,770", "Region", "Region: Undefined")
                      .withChildren(
                        new PaginatedReportAreaForTests(new AreaOwner(69), "Project Title", "Activity with planned disbursements", "Funding-2014-Actual Disbursements", "200", "Funding-2015-Actual Disbursements", "570", "Totals-Actual Disbursements", "770").withCounts(1, -1),
                        new PaginatedReportAreaForTests(new AreaOwner(71), "Project Title", "activity_with_disaster_response", "Funding-2014-Actual Commitments", "33,000", "Funding-2015-Actual Commitments", "117,000", "Totals-Actual Commitments", "150,000").withCounts(1, -1),
                        new PaginatedReportAreaForTests(new AreaOwner(79), "Project Title", "with weird currencies", "Funding-2014-Actual Commitments", "3,632,14", "Funding-2015-Actual Commitments", "93,930,84", "Totals-Actual Commitments", "97,562,98").withCounts(1, -1)));

        compareBodies("cut end hier", cor, initHierReport.getPage(40, 10));
    }

    @Test
    public void testFullDoubleHier() {
        PaginatedReportAreaForTests cor = new PaginatedReportAreaForTests(null).withCounts(37, 37)
                .withContents("Region", "", "Primary Sector", "", "Project Title", "", "Funding-2006-Actual Commitments", "96,840,58", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "100,000", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "780,311", "Funding-2011-Actual Commitments", "1,213,119", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "25,000", "Funding-2012-Actual Disbursements", "12,000", "Funding-2013-Actual Commitments", "7,842,086", "Funding-2013-Actual Disbursements", "1,266,956", "Funding-2014-Actual Commitments", "8,159,813,77", "Funding-2014-Actual Disbursements", "710,200", "Funding-2015-Actual Commitments", "1,971,831,84", "Funding-2015-Actual Disbursements", "437,335", "Totals-Actual Commitments", "19,408,691,19", "Totals-Actual Disbursements", "3,206,802")
                .withChildren(
                  new PaginatedReportAreaForTests(new AreaOwner("Region", "Anenii Noi County")).withCounts(8, 8)
                  .withContents("Primary Sector", "", "Project Title", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "143,777", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "1,574,332", "Funding-2013-Actual Disbursements", "1,100,111", "Funding-2014-Actual Commitments", "37,500", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "1,611,832", "Totals-Actual Disbursements", "1,243,888", "Region", "Anenii Noi County")
                  .withChildren(
                    new PaginatedReportAreaForTests(new AreaOwner("Primary Sector", "110 - EDUCATION")).withCounts(8, 8)
                    .withContents("Project Title", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "143,777", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "1,449,732", "Funding-2013-Actual Disbursements", "1,100,111", "Funding-2014-Actual Commitments", "37,500", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "1,487,232", "Totals-Actual Disbursements", "1,243,888", "Primary Sector", "110 - EDUCATION")
                    .withChildren(
                      new PaginatedReportAreaForTests(new AreaOwner(18), "Project Title", "Test MTEF directed", "Funding-2010-Actual Disbursements", "143,777", "Totals-Actual Disbursements", "143,777").withCounts(1, -1),
                      new PaginatedReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Funding-2013-Actual Disbursements", "545,000", "Totals-Actual Disbursements", "545,000").withCounts(1, -1),
                      new PaginatedReportAreaForTests(new AreaOwner(28), "Project Title", "ptc activity 1", "Funding-2013-Actual Commitments", "666,777", "Totals-Actual Commitments", "666,777").withCounts(1, -1),
                      new PaginatedReportAreaForTests(new AreaOwner(29), "Project Title", "ptc activity 2", "Funding-2013-Actual Commitments", "333,222", "Totals-Actual Commitments", "333,222").withCounts(1, -1),
                      new PaginatedReportAreaForTests(new AreaOwner(30), "Project Title", "SSC Project 1", "Funding-2013-Actual Commitments", "111,333", "Funding-2013-Actual Disbursements", "555,111", "Totals-Actual Commitments", "111,333", "Totals-Actual Disbursements", "555,111").withCounts(1, -1),
                      new PaginatedReportAreaForTests(new AreaOwner(33), "Project Title", "Activity with Zones", "Funding-2013-Actual Commitments", "285,000", "Totals-Actual Commitments", "285,000").withCounts(1, -1),
                      new PaginatedReportAreaForTests(new AreaOwner(36), "Project Title", "Activity With Zones and Percentages", "Funding-2013-Actual Commitments", "53,400", "Totals-Actual Commitments", "53,400").withCounts(1, -1),
                      new PaginatedReportAreaForTests(new AreaOwner(40), "Project Title", "SubNational no percentages", "Funding-2014-Actual Commitments", "37,500", "Totals-Actual Commitments", "37,500").withCounts(1, -1)        ),
                    new PaginatedReportAreaForTests(new AreaOwner("Primary Sector", "120 - HEALTH")).withCounts(1, 1).withContents("Project Title", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "124,600", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "124,600", "Totals-Actual Disbursements", "0", "Primary Sector", "120 - HEALTH")
                    .withChildren(
                      new PaginatedReportAreaForTests(new AreaOwner(36), "Project Title", "Activity With Zones and Percentages", "Funding-2013-Actual Commitments", "124,600", "Totals-Actual Commitments", "124,600").withCounts(1, -1)        )      ),
                  new PaginatedReportAreaForTests(new AreaOwner("Region", "Balti County")).withCounts(7, 7)
                  .withContents("Primary Sector", "", "Project Title", "", "Funding-2006-Actual Commitments", "53,262,32", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "1,330,333", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "37,500", "Funding-2014-Actual Disbursements", "27,500", "Funding-2015-Actual Commitments", "723,189", "Funding-2015-Actual Disbursements", "321,765", "Totals-Actual Commitments", "2,144,284,32", "Totals-Actual Disbursements", "349,265", "Region", "Balti County")
                  .withChildren(
                    new PaginatedReportAreaForTests(new AreaOwner("Primary Sector", "110 - EDUCATION")).withCounts(7, 7)
                    .withContents("Project Title", "", "Funding-2006-Actual Commitments", "31,957,39", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "831,933", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "37,500", "Funding-2014-Actual Disbursements", "16,500", "Funding-2015-Actual Commitments", "388,234,5", "Funding-2015-Actual Disbursements", "160,882,5", "Totals-Actual Commitments", "1,289,624,89", "Totals-Actual Disbursements", "177,382,5", "Primary Sector", "110 - EDUCATION")
                    .withChildren(
                      new PaginatedReportAreaForTests(new AreaOwner(32), "Project Title", "crazy funding 1", "Funding-2013-Actual Commitments", "333,333", "Totals-Actual Commitments", "333,333").withCounts(1, -1),
                      new PaginatedReportAreaForTests(new AreaOwner(33), "Project Title", "Activity with Zones", "Funding-2013-Actual Commitments", "285,000", "Totals-Actual Commitments", "285,000").withCounts(1, -1),
                      new PaginatedReportAreaForTests(new AreaOwner(36), "Project Title", "Activity With Zones and Percentages", "Funding-2013-Actual Commitments", "213,600", "Totals-Actual Commitments", "213,600").withCounts(1, -1),
                      new PaginatedReportAreaForTests(new AreaOwner(40), "Project Title", "SubNational no percentages", "Funding-2014-Actual Commitments", "37,500", "Totals-Actual Commitments", "37,500").withCounts(1, -1),
                      new PaginatedReportAreaForTests(new AreaOwner(52), "Project Title", "activity with contracting agency", "Funding-2006-Actual Commitments", "31,957,39", "Funding-2014-Actual Disbursements", "16,500", "Totals-Actual Commitments", "31,957,39", "Totals-Actual Disbursements", "16,500").withCounts(1, -1),
                      new PaginatedReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Funding-2015-Actual Commitments", "228,394,5", "Funding-2015-Actual Disbursements", "160,882,5", "Totals-Actual Commitments", "228,394,5", "Totals-Actual Disbursements", "160,882,5").withCounts(1, -1),
                      new PaginatedReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms", "Funding-2015-Actual Commitments", "159,840", "Totals-Actual Commitments", "159,840").withCounts(1, -1)        ),
                    new PaginatedReportAreaForTests(new AreaOwner("Primary Sector", "112 - BASIC EDUCATION")).withCounts(3, 3)
                    .withContents("Project Title", "", "Funding-2006-Actual Commitments", "5,326,23", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "2,750", "Funding-2015-Actual Commitments", "334,954,5", "Funding-2015-Actual Disbursements", "160,882,5", "Totals-Actual Commitments", "340,280,73", "Totals-Actual Disbursements", "163,632,5", "Primary Sector", "112 - BASIC EDUCATION")
                    .withChildren(
                      new PaginatedReportAreaForTests(new AreaOwner(52), "Project Title", "activity with contracting agency", "Funding-2006-Actual Commitments", "5,326,23", "Funding-2014-Actual Disbursements", "2,750", "Totals-Actual Commitments", "5,326,23", "Totals-Actual Disbursements", "2,750").withCounts(1, -1),
                      new PaginatedReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Funding-2015-Actual Commitments", "228,394,5", "Funding-2015-Actual Disbursements", "160,882,5", "Totals-Actual Commitments", "228,394,5", "Totals-Actual Disbursements", "160,882,5").withCounts(1, -1),
                      new PaginatedReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms", "Funding-2015-Actual Commitments", "106,560", "Totals-Actual Commitments", "106,560").withCounts(1, -1)        ),
                    new PaginatedReportAreaForTests(new AreaOwner("Primary Sector", "120 - HEALTH")).withCounts(2, 2)
                    .withContents("Project Title", "", "Funding-2006-Actual Commitments", "15,978,7", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "498,400", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "8,250", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "514,378,7", "Totals-Actual Disbursements", "8,250", "Primary Sector", "120 - HEALTH")
                    .withChildren(
                      new PaginatedReportAreaForTests(new AreaOwner(36), "Project Title", "Activity With Zones and Percentages", "Funding-2013-Actual Commitments", "498,400", "Totals-Actual Commitments", "498,400").withCounts(1, -1),
                      new PaginatedReportAreaForTests(new AreaOwner(52), "Project Title", "activity with contracting agency", "Funding-2006-Actual Commitments", "15,978,7", "Funding-2014-Actual Disbursements", "8,250", "Totals-Actual Commitments", "15,978,7", "Totals-Actual Disbursements", "8,250").withCounts(1, -1)        )      ),
                  new PaginatedReportAreaForTests(new AreaOwner("Region", "Cahul County")).withCounts(1, 1).withContents("Primary Sector", "", "Project Title", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "2,670,000", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "4,400,000", "Funding-2014-Actual Disbursements", "450,000", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "7,070,000", "Totals-Actual Disbursements", "450,000", "Region", "Cahul County")
                  .withChildren(
                    new PaginatedReportAreaForTests(new AreaOwner("Primary Sector", "113 - SECONDARY EDUCATION")).withCounts(1, 1).withContents("Project Title", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "2,670,000", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "4,400,000", "Funding-2014-Actual Disbursements", "450,000", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "7,070,000", "Totals-Actual Disbursements", "450,000", "Primary Sector", "113 - SECONDARY EDUCATION")
                    .withChildren(
                      new PaginatedReportAreaForTests(new AreaOwner(48), "Project Title", "pledged 2", "Funding-2013-Actual Commitments", "2,670,000", "Funding-2014-Actual Commitments", "4,400,000", "Funding-2014-Actual Disbursements", "450,000", "Totals-Actual Commitments", "7,070,000", "Totals-Actual Disbursements", "450,000").withCounts(1, -1)        )      ),
                  new PaginatedReportAreaForTests(new AreaOwner("Region", "Chisinau City")).withCounts(4, 4).withContents("Primary Sector", "", "Project Title", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "50,000", "Funding-2014-Actual Disbursements", "27,500", "Funding-2015-Actual Commitments", "246,912", "Funding-2015-Actual Disbursements", "17,500", "Totals-Actual Commitments", "296,912", "Totals-Actual Disbursements", "45,000", "Region", "Chisinau City")
                  .withChildren(
                    new PaginatedReportAreaForTests(new AreaOwner("Primary Sector", "110 - EDUCATION")).withCounts(4, 4)
                    .withContents("Project Title", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "50,000", "Funding-2014-Actual Disbursements", "27,500", "Funding-2015-Actual Commitments", "246,912", "Funding-2015-Actual Disbursements", "17,500", "Totals-Actual Commitments", "296,912", "Totals-Actual Disbursements", "45,000", "Primary Sector", "110 - EDUCATION")
                    .withChildren(
                      new PaginatedReportAreaForTests(new AreaOwner(41), "Project Title", "Activity Linked With Pledge", "Funding-2014-Actual Commitments", "50,000", "Totals-Actual Commitments", "50,000").withCounts(1, -1),
                      new PaginatedReportAreaForTests(new AreaOwner(67), "Project Title", "third activity with agreements", "Funding-2015-Actual Commitments", "123,456", "Totals-Actual Commitments", "123,456").withCounts(1, -1),
                      new PaginatedReportAreaForTests(new AreaOwner(73), "Project Title", "activity with directed MTEFs", "Funding-2015-Actual Commitments", "123,456", "Totals-Actual Commitments", "123,456").withCounts(1, -1),
                      new PaginatedReportAreaForTests(new AreaOwner(77), "Project Title", "execution rate activity", "Funding-2014-Actual Disbursements", "27,500", "Funding-2015-Actual Disbursements", "17,500", "Totals-Actual Disbursements", "45,000").withCounts(1, -1)        )      ),
                  new PaginatedReportAreaForTests(new AreaOwner("Region", "Chisinau County")).withCounts(4, 4).withContents("Primary Sector", "", "Project Title", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "1,700,000", "Funding-2013-Actual Disbursements", "35,000", "Funding-2014-Actual Commitments", "3,365,760,63", "Funding-2014-Actual Disbursements", "155,000", "Funding-2015-Actual Commitments", "1,200", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "5,066,960,63", "Totals-Actual Disbursements", "190,000", "Region", "Chisinau County")
                  .withChildren(
                    new PaginatedReportAreaForTests(new AreaOwner("Primary Sector", "110 - EDUCATION")).withCounts(4, 4)
                    .withContents("Project Title", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "1,700,000", "Funding-2013-Actual Disbursements", "35,000", "Funding-2014-Actual Commitments", "3,365,760,63", "Funding-2014-Actual Disbursements", "155,000", "Funding-2015-Actual Commitments", "1,200", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "5,066,960,63", "Totals-Actual Disbursements", "190,000", "Primary Sector", "110 - EDUCATION")
                    .withChildren(
                      new PaginatedReportAreaForTests(new AreaOwner(46), "Project Title", "pledged education activity 1", "Funding-2013-Actual Commitments", "1,700,000", "Funding-2014-Actual Commitments", "3,300,000", "Totals-Actual Commitments", "5,000,000").withCounts(1, -1),
                      new PaginatedReportAreaForTests(new AreaOwner(50), "Project Title", "activity with capital spending", "Funding-2014-Actual Commitments", "65,760,63", "Funding-2014-Actual Disbursements", "80,000", "Totals-Actual Commitments", "65,760,63", "Totals-Actual Disbursements", "80,000").withCounts(1, -1),
                      new PaginatedReportAreaForTests(new AreaOwner(66), "Project Title", "Activity 2 with multiple agreements", "Funding-2015-Actual Commitments", "1,200", "Totals-Actual Commitments", "1,200").withCounts(1, -1),
                      new PaginatedReportAreaForTests(new AreaOwner(76), "Project Title", "activity with pipeline MTEFs and act. disb", "Funding-2013-Actual Disbursements", "35,000", "Funding-2014-Actual Disbursements", "75,000", "Totals-Actual Disbursements", "110,000").withCounts(1, -1)        )      ),
                  new PaginatedReportAreaForTests(new AreaOwner("Region", "Drochia County")).withCounts(2, 2)
                  .withContents("Primary Sector", "", "Project Title", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "621,600", "Funding-2015-Actual Disbursements", "80,000", "Totals-Actual Commitments", "621,600", "Totals-Actual Disbursements", "80,000", "Region", "Drochia County")
                  .withChildren(
                    new PaginatedReportAreaForTests(new AreaOwner("Primary Sector", "110 - EDUCATION")).withCounts(2, 2)
                    .withContents("Project Title", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "372,960", "Funding-2015-Actual Disbursements", "80,000", "Totals-Actual Commitments", "372,960", "Totals-Actual Disbursements", "80,000", "Primary Sector", "110 - EDUCATION")
                    .withChildren(
                      new PaginatedReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms", "Funding-2015-Actual Commitments", "372,960", "Totals-Actual Commitments", "372,960").withCounts(1, -1),
                      new PaginatedReportAreaForTests(new AreaOwner(78), "Project Title", "activity with many MTEFs", "Funding-2015-Actual Disbursements", "80,000", "Totals-Actual Disbursements", "80,000").withCounts(1, -1)        ),
                    new PaginatedReportAreaForTests(new AreaOwner("Primary Sector", "112 - BASIC EDUCATION")).withCounts(1, 1).withContents("Project Title", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "248,640", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "248,640", "Totals-Actual Disbursements", "0", "Primary Sector", "112 - BASIC EDUCATION")
                    .withChildren(
                      new PaginatedReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms", "Funding-2015-Actual Commitments", "248,640", "Totals-Actual Commitments", "248,640").withCounts(1, -1)        )      ),
                  new PaginatedReportAreaForTests(new AreaOwner("Region", "Dubasari County")).withCounts(2, 2)
                  .withContents("Primary Sector", "", "Project Title", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "123,321", "Funding-2011-Actual Commitments", "213,231", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "27,500", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "17,500", "Totals-Actual Commitments", "213,231", "Totals-Actual Disbursements", "168,321", "Region", "Dubasari County")
                  .withChildren(
                    new PaginatedReportAreaForTests(new AreaOwner("Primary Sector", "110 - EDUCATION")).withCounts(1, 1).withContents("Project Title", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "27,500", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "17,500", "Totals-Actual Commitments", "0", "Totals-Actual Disbursements", "45,000", "Primary Sector", "110 - EDUCATION")
                    .withChildren(
                      new PaginatedReportAreaForTests(new AreaOwner(77), "Project Title", "execution rate activity", "Funding-2014-Actual Disbursements", "27,500", "Funding-2015-Actual Disbursements", "17,500", "Totals-Actual Disbursements", "45,000").withCounts(1, -1)        ),
                    new PaginatedReportAreaForTests(new AreaOwner("Primary Sector", "112 - BASIC EDUCATION")).withCounts(1, 1).withContents("Project Title", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "123,321", "Funding-2011-Actual Commitments", "213,231", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "213,231", "Totals-Actual Disbursements", "123,321", "Primary Sector", "112 - BASIC EDUCATION")
                    .withChildren(
                      new PaginatedReportAreaForTests(new AreaOwner(12), "Project Title", "TAC_activity_1", "Funding-2010-Actual Disbursements", "123,321", "Funding-2011-Actual Commitments", "213,231", "Totals-Actual Commitments", "213,231", "Totals-Actual Disbursements", "123,321").withCounts(1, -1)        )      ),
                  new PaginatedReportAreaForTests(new AreaOwner("Region", "Edinet County")).withCounts(1, 1).withContents("Primary Sector", "", "Project Title", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "567,421", "Funding-2013-Actual Disbursements", "131,845", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "567,421", "Totals-Actual Disbursements", "131,845", "Region", "Edinet County")
                  .withChildren(
                    new PaginatedReportAreaForTests(new AreaOwner("Primary Sector", "112 - BASIC EDUCATION")).withCounts(1, 1).withContents("Project Title", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "567,421", "Funding-2013-Actual Disbursements", "131,845", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "567,421", "Totals-Actual Disbursements", "131,845", "Primary Sector", "112 - BASIC EDUCATION")
                    .withChildren(
                      new PaginatedReportAreaForTests(new AreaOwner(31), "Project Title", "SSC Project 2", "Funding-2013-Actual Commitments", "567,421", "Funding-2013-Actual Disbursements", "131,845", "Totals-Actual Commitments", "567,421", "Totals-Actual Disbursements", "131,845").withCounts(1, -1)        )      ),
                  new PaginatedReportAreaForTests(new AreaOwner("Region", "Falesti County")).withCounts(1, 1).withContents("Primary Sector", "", "Project Title", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "453,213", "Funding-2011-Actual Commitments", "999,888", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "999,888", "Totals-Actual Disbursements", "453,213", "Region", "Falesti County")
                  .withChildren(
                    new PaginatedReportAreaForTests(new AreaOwner("Primary Sector", "130 - POPULATION POLICIES/PROGRAMMES AND REPRODUCTIVE HEALTH")).withCounts(1, 1).withContents("Project Title", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "453,213", "Funding-2011-Actual Commitments", "999,888", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "999,888", "Totals-Actual Disbursements", "453,213", "Primary Sector", "130 - POPULATION POLICIES/PROGRAMMES AND REPRODUCTIVE HEALTH")
                    .withChildren(
                      new PaginatedReportAreaForTests(new AreaOwner(13), "Project Title", "TAC_activity_2", "Funding-2010-Actual Disbursements", "453,213", "Funding-2011-Actual Commitments", "999,888", "Totals-Actual Commitments", "999,888", "Totals-Actual Disbursements", "453,213").withCounts(1, -1)        )      ),
                  new PaginatedReportAreaForTests(new AreaOwner("Region", "Transnistrian Region")).withCounts(2, 2)
                  .withContents("Primary Sector", "", "Project Title", "", "Funding-2006-Actual Commitments", "43,578,26", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "123,321", "Funding-2014-Actual Disbursements", "22,500", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "166,899,26", "Totals-Actual Disbursements", "22,500", "Region", "Transnistrian Region")
                  .withChildren(
                    new PaginatedReportAreaForTests(new AreaOwner("Primary Sector", "110 - EDUCATION")).withCounts(2, 2)
                    .withContents("Project Title", "", "Funding-2006-Actual Commitments", "26,146,96", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "123,321", "Funding-2014-Actual Disbursements", "13,500", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "149,467,96", "Totals-Actual Disbursements", "13,500", "Primary Sector", "110 - EDUCATION")
                    .withChildren(
                      new PaginatedReportAreaForTests(new AreaOwner(52), "Project Title", "activity with contracting agency", "Funding-2006-Actual Commitments", "26,146,96", "Funding-2014-Actual Disbursements", "13,500", "Totals-Actual Commitments", "26,146,96", "Totals-Actual Disbursements", "13,500").withCounts(1, -1),
                      new PaginatedReportAreaForTests(new AreaOwner(61), "Project Title", "activity-with-unfunded-components", "Funding-2014-Actual Commitments", "123,321", "Totals-Actual Commitments", "123,321").withCounts(1, -1)        ),
                    new PaginatedReportAreaForTests(new AreaOwner("Primary Sector", "112 - BASIC EDUCATION")).withCounts(1, 1).withContents("Project Title", "", "Funding-2006-Actual Commitments", "4,357,83", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "2,250", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "4,357,83", "Totals-Actual Disbursements", "2,250", "Primary Sector", "112 - BASIC EDUCATION")
                    .withChildren(
                      new PaginatedReportAreaForTests(new AreaOwner(52), "Project Title", "activity with contracting agency", "Funding-2006-Actual Commitments", "4,357,83", "Funding-2014-Actual Disbursements", "2,250", "Totals-Actual Commitments", "4,357,83", "Totals-Actual Disbursements", "2,250").withCounts(1, -1)        ),
                    new PaginatedReportAreaForTests(new AreaOwner("Primary Sector", "120 - HEALTH")).withCounts(1, 1).withContents("Project Title", "", "Funding-2006-Actual Commitments", "13,073,48", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "6,750", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "13,073,48", "Totals-Actual Disbursements", "6,750", "Primary Sector", "120 - HEALTH")
                    .withChildren(
                      new PaginatedReportAreaForTests(new AreaOwner(52), "Project Title", "activity with contracting agency", "Funding-2006-Actual Commitments", "13,073,48", "Funding-2014-Actual Disbursements", "6,750", "Totals-Actual Commitments", "13,073,48", "Totals-Actual Disbursements", "6,750").withCounts(1, -1)        )      ),
                  new PaginatedReportAreaForTests(new AreaOwner("Region", "Region: Undefined")).withCounts(11, 11)
                  .withContents("Primary Sector", "", "Project Title", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "100,000", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "60,000", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "25,000", "Funding-2012-Actual Disbursements", "12,000", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "145,732,14", "Funding-2014-Actual Disbursements", "200", "Funding-2015-Actual Commitments", "378,930,84", "Funding-2015-Actual Disbursements", "570", "Totals-Actual Commitments", "649,662,98", "Totals-Actual Disbursements", "72,770", "Region", "Region: Undefined")
                  .withChildren(
                    new PaginatedReportAreaForTests(new AreaOwner("Primary Sector", "110 - EDUCATION")).withCounts(9, 9)
                    .withContents("Project Title", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "100,000", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "60,000", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "25,000", "Funding-2012-Actual Disbursements", "12,000", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "120,168,92", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "322,737,76", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "567,906,68", "Totals-Actual Disbursements", "72,000", "Primary Sector", "110 - EDUCATION")
                    .withChildren(
                      new PaginatedReportAreaForTests(new AreaOwner(26), "Project Title", "date-filters-activity", "Funding-2009-Actual Commitments", "100,000", "Funding-2010-Actual Disbursements", "60,000", "Funding-2012-Actual Commitments", "25,000", "Funding-2012-Actual Disbursements", "12,000", "Totals-Actual Commitments", "125,000", "Totals-Actual Disbursements", "72,000").withCounts(1, -1),
                      new PaginatedReportAreaForTests(new AreaOwner(43), "Project Title", "Activity with primary_tertiary_program", "Funding-2014-Actual Commitments", "50,000", "Totals-Actual Commitments", "50,000").withCounts(1, -1),
                      new PaginatedReportAreaForTests(new AreaOwner(44), "Project Title", "activity with primary_program", "Funding-2014-Actual Commitments", "32,000", "Totals-Actual Commitments", "32,000").withCounts(1, -1),
                      new PaginatedReportAreaForTests(new AreaOwner(45), "Project Title", "activity with tertiary_program", "Funding-2014-Actual Commitments", "15,000", "Totals-Actual Commitments", "15,000").withCounts(1, -1),
                      new PaginatedReportAreaForTests(new AreaOwner(63), "Project Title", "activity with funded components", "Funding-2014-Actual Commitments", "100", "Totals-Actual Commitments", "100").withCounts(1, -1),
                      new PaginatedReportAreaForTests(new AreaOwner(64), "Project Title", "Unvalidated activity", "Funding-2015-Actual Commitments", "45,000", "Totals-Actual Commitments", "45,000").withCounts(1, -1),
                      new PaginatedReportAreaForTests(new AreaOwner(68), "Project Title", "activity with incomplete agreement", "Funding-2015-Actual Commitments", "123,000", "Totals-Actual Commitments", "123,000").withCounts(1, -1),
                      new PaginatedReportAreaForTests(new AreaOwner(71), "Project Title", "activity_with_disaster_response", "Funding-2014-Actual Commitments", "19,800", "Funding-2015-Actual Commitments", "70,200", "Totals-Actual Commitments", "90,000").withCounts(1, -1),
                      new PaginatedReportAreaForTests(new AreaOwner(79), "Project Title", "with weird currencies", "Funding-2014-Actual Commitments", "3,268,92", "Funding-2015-Actual Commitments", "84,537,76", "Totals-Actual Commitments", "87,806,68").withCounts(1, -1)        ),
                    new PaginatedReportAreaForTests(new AreaOwner("Primary Sector", "112 - BASIC EDUCATION")).withCounts(2, 2)
                    .withContents("Project Title", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "363,21", "Funding-2014-Actual Disbursements", "200", "Funding-2015-Actual Commitments", "9,393,08", "Funding-2015-Actual Disbursements", "570", "Totals-Actual Commitments", "9,756,3", "Totals-Actual Disbursements", "770", "Primary Sector", "112 - BASIC EDUCATION")
                    .withChildren(
                      new PaginatedReportAreaForTests(new AreaOwner(69), "Project Title", "Activity with planned disbursements", "Funding-2014-Actual Disbursements", "200", "Funding-2015-Actual Disbursements", "570", "Totals-Actual Disbursements", "770").withCounts(1, -1),
                      new PaginatedReportAreaForTests(new AreaOwner(79), "Project Title", "with weird currencies", "Funding-2014-Actual Commitments", "363,21", "Funding-2015-Actual Commitments", "9,393,08", "Totals-Actual Commitments", "9,756,3").withCounts(1, -1)        ),
                    new PaginatedReportAreaForTests(new AreaOwner("Primary Sector", "113 - SECONDARY EDUCATION")).withCounts(1, 1).withContents("Project Title", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "13,200", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "46,800", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "60,000", "Totals-Actual Disbursements", "0", "Primary Sector", "113 - SECONDARY EDUCATION")
                    .withChildren(
                      new PaginatedReportAreaForTests(new AreaOwner(71), "Project Title", "activity_with_disaster_response", "Funding-2014-Actual Commitments", "13,200", "Funding-2015-Actual Commitments", "46,800", "Totals-Actual Commitments", "60,000").withCounts(1, -1)        ),
                    new PaginatedReportAreaForTests(new AreaOwner("Primary Sector", "Primary Sector: Undefined")).withCounts(1, 1).withContents("Project Title", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "12,000", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "12,000", "Totals-Actual Disbursements", "0", "Primary Sector", "Primary Sector: Undefined")
                    .withChildren(
                      new PaginatedReportAreaForTests(new AreaOwner(53), "Project Title", "new activity with contracting", "Funding-2014-Actual Commitments", "12,000", "Totals-Actual Commitments", "12,000").withCounts(1, -1))));
        
        compareBodies("full double hier", cor, initDoubleHierReport.getPage(0, 150));
    }

    @Test
    public void testMidDoubleHier() {
        PaginatedReportAreaForTests cor = new PaginatedReportAreaForTests(null).withCounts(7, 37)
                .withContents("Region", "", "Primary Sector", "", "Project Title", "",
                        "Funding-2006-Actual Commitments", "96,840,58", "Funding-2006-Actual Disbursements", "0",
                        "Funding-2009-Actual Commitments", "100,000", "Funding-2009-Actual Disbursements", "0",
                        "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "780,311",
                        "Funding-2011-Actual Commitments", "1,213,119", "Funding-2011-Actual Disbursements", "0",
                        "Funding-2012-Actual Commitments", "25,000", "Funding-2012-Actual Disbursements", "12,000",
                        "Funding-2013-Actual Commitments", "7,842,086", "Funding-2013-Actual Disbursements",
                        "1,266,956", "Funding-2014-Actual Commitments", "8,159,813,77",
                        "Funding-2014-Actual Disbursements", "710,200", "Funding-2015-Actual Commitments",
                        "1,971,831,84", "Funding-2015-Actual Disbursements", "437,335", "Totals-Actual Commitments",
                        "19,408,691,19", "Totals-Actual Disbursements", "3,206,802")
                .withChildren(new PaginatedReportAreaForTests(new AreaOwner("Region", "Anenii Noi County"))
                        .withCounts(2, 8)
                        .withContents("Primary Sector", "", "Project Title", "", "Funding-2006-Actual Commitments",
                                "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0",
                                "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0",
                                "Funding-2010-Actual Disbursements", "143,777", "Funding-2011-Actual Commitments", "0",
                                "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0",
                                "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments",
                                "1,574,332", "Funding-2013-Actual Disbursements", "1,100,111",
                                "Funding-2014-Actual Commitments", "37,500", "Funding-2014-Actual Disbursements", "0",
                                "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0",
                                "Totals-Actual Commitments", "1,611,832", "Totals-Actual Disbursements", "1,243,888",
                                "Region", "Anenii Noi County")
                        .withChildren(
                                new PaginatedReportAreaForTests(new AreaOwner("Primary Sector", "110 - EDUCATION"))
                                        .withCounts(2, 8)
                                        .withContents("Project Title", "", "Funding-2006-Actual Commitments", "0",
                                                "Funding-2006-Actual Disbursements", "0",
                                                "Funding-2009-Actual Commitments", "0",
                                                "Funding-2009-Actual Disbursements", "0",
                                                "Funding-2010-Actual Commitments", "0",
                                                "Funding-2010-Actual Disbursements", "143,777",
                                                "Funding-2011-Actual Commitments", "0",
                                                "Funding-2011-Actual Disbursements", "0",
                                                "Funding-2012-Actual Commitments", "0",
                                                "Funding-2012-Actual Disbursements", "0",
                                                "Funding-2013-Actual Commitments", "1,449,732",
                                                "Funding-2013-Actual Disbursements", "1,100,111",
                                                "Funding-2014-Actual Commitments", "37,500",
                                                "Funding-2014-Actual Disbursements", "0",
                                                "Funding-2015-Actual Commitments", "0",
                                                "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments",
                                                "1,487,232", "Totals-Actual Disbursements", "1,243,888",
                                                "Primary Sector", "110 - EDUCATION")
                                        .withChildren(
                                                new PaginatedReportAreaForTests(new AreaOwner(36), "Project Title",
                                                        "Activity With Zones and Percentages",
                                                        "Funding-2013-Actual Commitments", "53,400",
                                                        "Totals-Actual Commitments", "53,400").withCounts(1, -1),
                                                new PaginatedReportAreaForTests(new AreaOwner(40), "Project Title",
                                                        "SubNational no percentages", "Funding-2014-Actual Commitments",
                                                        "37,500", "Totals-Actual Commitments", "37,500").withCounts(1,
                                                                -1)),
                                new PaginatedReportAreaForTests(new AreaOwner("Primary Sector", "120 - HEALTH"))
                                        .withCounts(1, 1)
                                        .withContents("Project Title", "", "Funding-2006-Actual Commitments", "0",
                                                "Funding-2006-Actual Disbursements", "0",
                                                "Funding-2009-Actual Commitments", "0",
                                                "Funding-2009-Actual Disbursements", "0",
                                                "Funding-2010-Actual Commitments", "0",
                                                "Funding-2010-Actual Disbursements", "0",
                                                "Funding-2011-Actual Commitments", "0",
                                                "Funding-2011-Actual Disbursements", "0",
                                                "Funding-2012-Actual Commitments", "0",
                                                "Funding-2012-Actual Disbursements", "0",
                                                "Funding-2013-Actual Commitments", "124,600",
                                                "Funding-2013-Actual Disbursements", "0",
                                                "Funding-2014-Actual Commitments", "0",
                                                "Funding-2014-Actual Disbursements", "0",
                                                "Funding-2015-Actual Commitments", "0",
                                                "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments",
                                                "124,600", "Totals-Actual Disbursements", "0", "Primary Sector",
                                                "120 - HEALTH")
                                        .withChildren(new PaginatedReportAreaForTests(new AreaOwner(36),
                                                "Project Title", "Activity With Zones and Percentages",
                                                "Funding-2013-Actual Commitments", "124,600",
                                                "Totals-Actual Commitments", "124,600").withCounts(1, -1))),
                        new PaginatedReportAreaForTests(new AreaOwner("Region", "Balti County")).withCounts(7, 7)
                                .withContents("Primary Sector", "", "Project Title",
                                        "", "Funding-2006-Actual Commitments", "53,262,32",
                                        "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments",
                                        "0", "Funding-2009-Actual Disbursements", "0",
                                        "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements",
                                        "0", "Funding-2011-Actual Commitments", "0",
                                        "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments",
                                        "0", "Funding-2012-Actual Disbursements", "0",
                                        "Funding-2013-Actual Commitments", "1,330,333",
                                        "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments",
                                        "37,500", "Funding-2014-Actual Disbursements", "27,500",
                                        "Funding-2015-Actual Commitments", "723,189",
                                        "Funding-2015-Actual Disbursements", "321,765", "Totals-Actual Commitments",
                                        "2,144,284,32", "Totals-Actual Disbursements", "349,265", "Region",
                                        "Balti County")
                                .withChildren(new PaginatedReportAreaForTests(
                                        new AreaOwner("Primary Sector", "110 - EDUCATION"))
                                                .withCounts(7, 7)
                                                .withContents("Project Title", "", "Funding-2006-Actual Commitments",
                                                        "31,957,39", "Funding-2006-Actual Disbursements", "0",
                                                        "Funding-2009-Actual Commitments", "0",
                                                        "Funding-2009-Actual Disbursements", "0",
                                                        "Funding-2010-Actual Commitments", "0",
                                                        "Funding-2010-Actual Disbursements", "0",
                                                        "Funding-2011-Actual Commitments", "0",
                                                        "Funding-2011-Actual Disbursements", "0",
                                                        "Funding-2012-Actual Commitments", "0",
                                                        "Funding-2012-Actual Disbursements", "0",
                                                        "Funding-2013-Actual Commitments", "831,933",
                                                        "Funding-2013-Actual Disbursements", "0",
                                                        "Funding-2014-Actual Commitments", "37,500",
                                                        "Funding-2014-Actual Disbursements", "16,500",
                                                        "Funding-2015-Actual Commitments", "388,234,5",
                                                        "Funding-2015-Actual Disbursements", "160,882,5",
                                                        "Totals-Actual Commitments", "1,289,624,89",
                                                        "Totals-Actual Disbursements", "177,382,5", "Primary Sector",
                                                        "110 - EDUCATION")
                                                .withChildren(
                                                        new PaginatedReportAreaForTests(new AreaOwner(32),
                                                                "Project Title", "crazy funding 1",
                                                                "Funding-2013-Actual Commitments", "333,333",
                                                                "Totals-Actual Commitments", "333,333").withCounts(1,
                                                                        -1),
                                                        new PaginatedReportAreaForTests(new AreaOwner(33),
                                                                "Project Title", "Activity with Zones",
                                                                "Funding-2013-Actual Commitments", "285,000",
                                                                "Totals-Actual Commitments", "285,000").withCounts(1,
                                                                        -1),
                                                        new PaginatedReportAreaForTests(new AreaOwner(36),
                                                                "Project Title", "Activity With Zones and Percentages",
                                                                "Funding-2013-Actual Commitments", "213,600",
                                                                "Totals-Actual Commitments", "213,600").withCounts(1,
                                                                        -1),
                                                        new PaginatedReportAreaForTests(new AreaOwner(40),
                                                                "Project Title", "SubNational no percentages",
                                                                "Funding-2014-Actual Commitments", "37,500",
                                                                "Totals-Actual Commitments", "37,500").withCounts(1,
                                                                        -1),
                                                        new PaginatedReportAreaForTests(new AreaOwner(52),
                                                                "Project Title", "activity with contracting agency",
                                                                "Funding-2006-Actual Commitments", "31,957,39",
                                                                "Funding-2014-Actual Disbursements", "16,500",
                                                                "Totals-Actual Commitments", "31,957,39",
                                                                "Totals-Actual Disbursements", "16,500").withCounts(1,
                                                                        -1),
                                                        new PaginatedReportAreaForTests(new AreaOwner(65),
                                                                "Project Title", "activity 1 with agreement",
                                                                "Funding-2015-Actual Commitments", "228,394,5",
                                                                "Funding-2015-Actual Disbursements", "160,882,5",
                                                                "Totals-Actual Commitments", "228,394,5",
                                                                "Totals-Actual Disbursements", "160,882,5")
                                                                        .withCounts(1, -1),
                                                        new PaginatedReportAreaForTests(new AreaOwner(70),
                                                                "Project Title",
                                                                "Activity with both MTEFs and Act.Comms",
                                                                "Funding-2015-Actual Commitments", "159,840",
                                                                "Totals-Actual Commitments", "159,840").withCounts(1,
                                                                        -1)),
                                        new PaginatedReportAreaForTests(
                                                new AreaOwner("Primary Sector", "112 - BASIC EDUCATION"))
                                                        .withCounts(3, 3)
                                                        .withContents("Project Title", "",
                                                                "Funding-2006-Actual Commitments", "5,326,23",
                                                                "Funding-2006-Actual Disbursements", "0",
                                                                "Funding-2009-Actual Commitments", "0",
                                                                "Funding-2009-Actual Disbursements", "0",
                                                                "Funding-2010-Actual Commitments", "0",
                                                                "Funding-2010-Actual Disbursements", "0",
                                                                "Funding-2011-Actual Commitments", "0",
                                                                "Funding-2011-Actual Disbursements", "0",
                                                                "Funding-2012-Actual Commitments", "0",
                                                                "Funding-2012-Actual Disbursements", "0",
                                                                "Funding-2013-Actual Commitments", "0",
                                                                "Funding-2013-Actual Disbursements", "0",
                                                                "Funding-2014-Actual Commitments", "0",
                                                                "Funding-2014-Actual Disbursements", "2,750",
                                                                "Funding-2015-Actual Commitments", "334,954,5",
                                                                "Funding-2015-Actual Disbursements", "160,882,5",
                                                                "Totals-Actual Commitments", "340,280,73",
                                                                "Totals-Actual Disbursements", "163,632,5",
                                                                "Primary Sector", "112 - BASIC EDUCATION")
                                                        .withChildren(
                                                                new PaginatedReportAreaForTests(new AreaOwner(52),
                                                                        "Project Title",
                                                                        "activity with contracting agency",
                                                                        "Funding-2006-Actual Commitments", "5,326,23",
                                                                        "Funding-2014-Actual Disbursements", "2,750",
                                                                        "Totals-Actual Commitments", "5,326,23",
                                                                        "Totals-Actual Disbursements", "2,750")
                                                                                .withCounts(1, -1),
                                                                new PaginatedReportAreaForTests(new AreaOwner(65),
                                                                        "Project Title", "activity 1 with agreement",
                                                                        "Funding-2015-Actual Commitments", "228,394,5",
                                                                        "Funding-2015-Actual Disbursements",
                                                                        "160,882,5", "Totals-Actual Commitments",
                                                                        "228,394,5", "Totals-Actual Disbursements",
                                                                        "160,882,5").withCounts(1, -1),
                                                                new PaginatedReportAreaForTests(new AreaOwner(70),
                                                                        "Project Title",
                                                                        "Activity with both MTEFs and Act.Comms",
                                                                        "Funding-2015-Actual Commitments", "106,560",
                                                                        "Totals-Actual Commitments", "106,560")
                                                                                .withCounts(1, -1)),
                                        new PaginatedReportAreaForTests(new AreaOwner("Primary Sector", "120 - HEALTH"))
                                                .withCounts(2, 2)
                                                .withContents("Project Title", "", "Funding-2006-Actual Commitments",
                                                        "15,978,7", "Funding-2006-Actual Disbursements", "0",
                                                        "Funding-2009-Actual Commitments", "0",
                                                        "Funding-2009-Actual Disbursements", "0",
                                                        "Funding-2010-Actual Commitments", "0",
                                                        "Funding-2010-Actual Disbursements", "0",
                                                        "Funding-2011-Actual Commitments", "0",
                                                        "Funding-2011-Actual Disbursements", "0",
                                                        "Funding-2012-Actual Commitments", "0",
                                                        "Funding-2012-Actual Disbursements", "0",
                                                        "Funding-2013-Actual Commitments", "498,400",
                                                        "Funding-2013-Actual Disbursements", "0",
                                                        "Funding-2014-Actual Commitments", "0",
                                                        "Funding-2014-Actual Disbursements", "8,250",
                                                        "Funding-2015-Actual Commitments", "0",
                                                        "Funding-2015-Actual Disbursements", "0",
                                                        "Totals-Actual Commitments", "514,378,7",
                                                        "Totals-Actual Disbursements", "8,250", "Primary Sector",
                                                        "120 - HEALTH")
                                                .withChildren(new PaginatedReportAreaForTests(new AreaOwner(36),
                                                        "Project Title", "Activity With Zones and Percentages",
                                                        "Funding-2013-Actual Commitments", "498,400",
                                                        "Totals-Actual Commitments", "498,400").withCounts(1, -1),
                                                        new PaginatedReportAreaForTests(new AreaOwner(52),
                                                                "Project Title", "activity with contracting agency",
                                                                "Funding-2006-Actual Commitments", "15,978,7",
                                                                "Funding-2014-Actual Disbursements", "8,250",
                                                                "Totals-Actual Commitments", "15,978,7",
                                                                "Totals-Actual Disbursements", "8,250").withCounts(1,
                                                                        -1))));

        compareBodies("mid double hier", cor, initDoubleHierReport.getPage(6, 15));
    }

    @Test
    public void testCutEndDoubleHier() {
        PaginatedReportAreaForTests cor = new PaginatedReportAreaForTests(null).withCounts(2, 37)
                .withContents("Region", "", "Primary Sector", "", "Project Title", "",
                        "Funding-2006-Actual Commitments", "96,840,58", "Funding-2006-Actual Disbursements", "0",
                        "Funding-2009-Actual Commitments", "100,000", "Funding-2009-Actual Disbursements", "0",
                        "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "780,311",
                        "Funding-2011-Actual Commitments", "1,213,119", "Funding-2011-Actual Disbursements", "0",
                        "Funding-2012-Actual Commitments", "25,000", "Funding-2012-Actual Disbursements", "12,000",
                        "Funding-2013-Actual Commitments", "7,842,086", "Funding-2013-Actual Disbursements",
                        "1,266,956", "Funding-2014-Actual Commitments", "8,159,813,77",
                        "Funding-2014-Actual Disbursements", "710,200", "Funding-2015-Actual Commitments",
                        "1,971,831,84", "Funding-2015-Actual Disbursements", "437,335", "Totals-Actual Commitments",
                        "19,408,691,19", "Totals-Actual Disbursements", "3,206,802")
                .withChildren(new PaginatedReportAreaForTests(new AreaOwner("Region", "Region: Undefined"))
                        .withCounts(2, 11)
                        .withContents("Primary Sector", "", "Project Title", "",
                                "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0",
                                "Funding-2009-Actual Commitments", "100,000", "Funding-2009-Actual Disbursements", "0",
                                "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "60,000",
                                "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0",
                                "Funding-2012-Actual Commitments", "25,000", "Funding-2012-Actual Disbursements",
                                "12,000", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements",
                                "0", "Funding-2014-Actual Commitments", "145,732,14",
                                "Funding-2014-Actual Disbursements", "200", "Funding-2015-Actual Commitments",
                                "378,930,84", "Funding-2015-Actual Disbursements", "570", "Totals-Actual Commitments",
                                "649,662,98", "Totals-Actual Disbursements", "72,770", "Region", "Region: Undefined")
                        .withChildren(
                                new PaginatedReportAreaForTests(
                                        new AreaOwner("Primary Sector", "113 - SECONDARY EDUCATION"))
                                                .withCounts(1, 1)
                                                .withContents("Project Title", "", "Funding-2006-Actual Commitments",
                                                        "0", "Funding-2006-Actual Disbursements", "0",
                                                        "Funding-2009-Actual Commitments", "0",
                                                        "Funding-2009-Actual Disbursements", "0",
                                                        "Funding-2010-Actual Commitments", "0",
                                                        "Funding-2010-Actual Disbursements", "0",
                                                        "Funding-2011-Actual Commitments", "0",
                                                        "Funding-2011-Actual Disbursements", "0",
                                                        "Funding-2012-Actual Commitments", "0",
                                                        "Funding-2012-Actual Disbursements", "0",
                                                        "Funding-2013-Actual Commitments", "0",
                                                        "Funding-2013-Actual Disbursements", "0",
                                                        "Funding-2014-Actual Commitments", "13,200",
                                                        "Funding-2014-Actual Disbursements", "0",
                                                        "Funding-2015-Actual Commitments", "46,800",
                                                        "Funding-2015-Actual Disbursements", "0",
                                                        "Totals-Actual Commitments", "60,000",
                                                        "Totals-Actual Disbursements", "0", "Primary Sector",
                                                        "113 - SECONDARY EDUCATION")
                                                .withChildren(new PaginatedReportAreaForTests(new AreaOwner(71),
                                                        "Project Title", "activity_with_disaster_response",
                                                        "Funding-2014-Actual Commitments", "13,200",
                                                        "Funding-2015-Actual Commitments", "46,800",
                                                        "Totals-Actual Commitments", "60,000").withCounts(1, -1)),
                                new PaginatedReportAreaForTests(
                                        new AreaOwner("Primary Sector", "Primary Sector: Undefined"))
                                                .withCounts(1, 1)
                                                .withContents("Project Title", "", "Funding-2006-Actual Commitments",
                                                        "0", "Funding-2006-Actual Disbursements", "0",
                                                        "Funding-2009-Actual Commitments", "0",
                                                        "Funding-2009-Actual Disbursements", "0",
                                                        "Funding-2010-Actual Commitments", "0",
                                                        "Funding-2010-Actual Disbursements", "0",
                                                        "Funding-2011-Actual Commitments", "0",
                                                        "Funding-2011-Actual Disbursements", "0",
                                                        "Funding-2012-Actual Commitments", "0",
                                                        "Funding-2012-Actual Disbursements", "0",
                                                        "Funding-2013-Actual Commitments", "0",
                                                        "Funding-2013-Actual Disbursements", "0",
                                                        "Funding-2014-Actual Commitments", "12,000",
                                                        "Funding-2014-Actual Disbursements", "0",
                                                        "Funding-2015-Actual Commitments", "0",
                                                        "Funding-2015-Actual Disbursements", "0",
                                                        "Totals-Actual Commitments", "12,000",
                                                        "Totals-Actual Disbursements", "0", "Primary Sector",
                                                        "Primary Sector: Undefined")
                                                .withChildren(new PaginatedReportAreaForTests(new AreaOwner(53),
                                                        "Project Title", "new activity with contracting",
                                                        "Funding-2014-Actual Commitments", "12,000",
                                                        "Totals-Actual Commitments", "12,000").withCounts(1, -1))));

        compareBodies("mid double hier", cor, initDoubleHierReport.getPage(52, 5));
    }
}
