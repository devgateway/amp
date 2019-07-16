package org.dgfoundation.amp.ar.amp212;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.mondrian.ReportAreaForTests;
import org.dgfoundation.amp.mondrian.ReportingTestCase;
import org.dgfoundation.amp.newreports.AreaOwner;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportElement;
import org.dgfoundation.amp.newreports.ReportFilters;
import org.dgfoundation.amp.newreports.ReportFiltersImpl;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.newreports.ReportElement.ElementType;
import org.dgfoundation.amp.nireports.testcases.NiReportModel;
import org.junit.Test;

/**
 * filtering testcases common between both the offdb schema and the AmpReportsSchema-using one.
 * These are not supposed to be exhaustive tests; instead they are concerned about "no stupid or weird things happening"
 * @author Dolghier Constantin
 *
 */
public abstract class FilteringSanityChecks extends ReportingTestCase {
            
    final List<String> acts = Arrays.asList(
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

    public static ReportSpecificationImpl buildSpecForFiltering(String title, List<String> cols, List<String> hiers, ReportElement elem, FilterRule rule) {
        ReportFiltersImpl filters = new ReportFiltersImpl();
        filters.addFilterRule(elem, rule);

        return buildSpecForFiltering(title, cols, hiers, filters);
    }

    public static ReportSpecificationImpl buildSpecForFiltering(String title, List<String> cols, List<String> hiers, ReportFilters reportFilters) {
        ReportSpecificationImpl spec = ReportSpecificationImpl.buildFor(title,
                cols,
                Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS),
                hiers,
                GroupingCriteria.GROUPING_YEARLY);

        spec.setFilters(reportFilters);

        return spec;
    }

    public static ReportSpecificationImpl buildSpecForFiltering(String title, List<String> cols, List<String> hiers, String filterColumnName, List<Long> ids, boolean positive) {
        FilterRule rule = ids.size() == 1 ? new FilterRule(ids.get(0).toString(), positive) : new FilterRule(ids.stream().map(z -> z.toString()).collect(Collectors.toList()), positive);
        ReportElement elem = new ReportElement(new ReportColumn(filterColumnName));
        return buildSpecForFiltering(title, cols, hiers, elem, rule);
    }
    
    @Test
    public void testFilteringByModeOfPaymentCash() {
        NiReportModel cor = new NiReportModel("flat mop [cash]")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 10))",
                        "(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Region: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 6));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 8, colSpan: 2))",
                        "(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 2));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 2));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 6, colSpan: 2))",
                        "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1))"))
                    .withWarnings(Arrays.asList())
                    .withBody(      new ReportAreaForTests(null)
                      .withContents("Project Title", "", "Region", "", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "143,777", "Funding-2013-Actual Commitments", "111,111", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "50,000", "Funding-2014-Actual Disbursements", "0", "Totals-Actual Commitments", "161,111", "Totals-Actual Disbursements", "143,777")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(18), "Project Title", "Test MTEF directed", "Region", "Anenii Noi County", "Funding-2010-Actual Disbursements", "143,777", "Totals-Actual Disbursements", "143,777"),
                        new ReportAreaForTests(new AreaOwner(32), "Project Title", "crazy funding 1", "Region", "Balti County", "Funding-2013-Actual Commitments", "111,111", "Totals-Actual Commitments", "111,111"),
                        new ReportAreaForTests(new AreaOwner(43), "Project Title", "Activity with primary_tertiary_program", "Region", "", "Funding-2014-Actual Commitments", "50,000", "Totals-Actual Commitments", "50,000")      ));
        
        ReportSpecificationImpl spec = buildSpecForFiltering("flat mop [cash]", 
                Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.REGION), null, ColumnConstants.MODE_OF_PAYMENT, Arrays.asList(2093l), true);
        runNiTestCase(spec, "en", acts, cor);
    }

    @Test
    public void testFilteringByModeOfPaymentCash_DirectPayment() {
        NiReportModel cor = new NiReportModel("flat mop [cash, Direct Payment]")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 12))",
                        "(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Region: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 8));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 10, colSpan: 2))",
                        "(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 2));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 2));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 6, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 8, colSpan: 2))",
                        "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1))"))
                    .withWarnings(Arrays.asList())
                    .withBody(      new ReportAreaForTests(null)
                      .withContents("Project Title", "", "Region", "", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "143,777", "Funding-2013-Actual Commitments", "1,012,087", "Funding-2013-Actual Disbursements", "721,956", "Funding-2014-Actual Commitments", "161,632,14", "Funding-2014-Actual Disbursements", "75,000", "Funding-2015-Actual Commitments", "1,222,386,84", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "2,396,105,98", "Totals-Actual Disbursements", "940,733")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(18), "Project Title", "Test MTEF directed", "Region", "Anenii Noi County", "Funding-2010-Actual Disbursements", "143,777", "Totals-Actual Disbursements", "143,777"),
                        new ReportAreaForTests(new AreaOwner(30), "Project Title", "SSC Project 1", "Region", "Anenii Noi County", "Funding-2013-Actual Commitments", "111,333", "Funding-2013-Actual Disbursements", "555,111", "Totals-Actual Commitments", "111,333", "Totals-Actual Disbursements", "555,111"),
                        new ReportAreaForTests(new AreaOwner(31), "Project Title", "SSC Project 2", "Region", "Edinet County", "Funding-2013-Actual Commitments", "567,421", "Funding-2013-Actual Disbursements", "131,845", "Totals-Actual Commitments", "567,421", "Totals-Actual Disbursements", "131,845"),
                        new ReportAreaForTests(new AreaOwner(32), "Project Title", "crazy funding 1", "Region", "Balti County", "Funding-2013-Actual Commitments", "333,333", "Totals-Actual Commitments", "333,333"),
                        new ReportAreaForTests(new AreaOwner(40), "Project Title", "SubNational no percentages", "Region", "Anenii Noi County, Balti County", "Funding-2014-Actual Commitments", "75,000", "Totals-Actual Commitments", "75,000"),
                        new ReportAreaForTests(new AreaOwner(43), "Project Title", "Activity with primary_tertiary_program", "Region", "", "Funding-2014-Actual Commitments", "50,000", "Totals-Actual Commitments", "50,000"),
                        new ReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms", "Region", "Balti County, Drochia County", "Funding-2015-Actual Commitments", "888,000", "Totals-Actual Commitments", "888,000"),
                        new ReportAreaForTests(new AreaOwner(71), "Project Title", "activity_with_disaster_response", "Region", "", "Funding-2014-Actual Commitments", "33,000", "Funding-2015-Actual Commitments", "117,000", "Totals-Actual Commitments", "150,000"),
                        new ReportAreaForTests(new AreaOwner(73), "Project Title", "activity with directed MTEFs", "Region", "Chisinau City", "Funding-2015-Actual Commitments", "123,456", "Totals-Actual Commitments", "123,456"),
                        new ReportAreaForTests(new AreaOwner(76), "Project Title", "activity with pipeline MTEFs and act. disb", "Region", "Chisinau County", "Funding-2013-Actual Disbursements", "35,000", "Funding-2014-Actual Disbursements", "75,000", "Totals-Actual Disbursements", "110,000"),
                        new ReportAreaForTests(new AreaOwner(79), "Project Title", "with weird currencies", "Region", "", "Funding-2014-Actual Commitments", "3,632,14", "Funding-2015-Actual Commitments", "93,930,84", "Totals-Actual Commitments", "97,562,98")));
        
        ReportSpecificationImpl spec = buildSpecForFiltering("flat mop [cash, Direct Payment]", 
                Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.REGION), null, ColumnConstants.MODE_OF_PAYMENT, Arrays.asList(2093l, 2094l), true);
        runNiTestCase(spec, "en", acts, cor);
    }

    
//  TODO: disabled for now - known bug  
//  @Test
//  public void testFilteringByModeOfPaymentCashNegative() {
//      NiReportModel cor = null;
//      
//      ReportSpecificationImpl spec = buildSpecForFiltering("flat mop [NOT cash]", 
//              Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.REGION), null, ColumnConstants.MODE_OF_PAYMENT, Arrays.asList(2093l), false);
//      runNiTestCase(spec, "en", acts, cor);
//  }
    
    @Test
    public void testFilteringTypeOfAssistanceSecond() {
        NiReportModel cor = new NiReportModel("flat toa [second]")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 8))",
                        "(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Region: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 4));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 6, colSpan: 2))",
                        "(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 2));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 2))",
                        "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1))"))
                    .withWarnings(Arrays.asList())
                    .withBody(      new ReportAreaForTests(null)
                      .withContents("Project Title", "", "Region", "", "Funding-2013-Actual Commitments", "2,892,222", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "4,589,081,63", "Funding-2014-Actual Disbursements", "530,000", "Totals-Actual Commitments", "7,481,303,63", "Totals-Actual Disbursements", "530,000")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(32), "Project Title", "crazy funding 1", "Region", "Balti County", "Funding-2013-Actual Commitments", "222,222", "Totals-Actual Commitments", "222,222"),
                        new ReportAreaForTests(new AreaOwner(48), "Project Title", "pledged 2", "Region", "Cahul County", "Funding-2013-Actual Commitments", "2,670,000", "Funding-2014-Actual Commitments", "4,400,000", "Funding-2014-Actual Disbursements", "450,000", "Totals-Actual Commitments", "7,070,000", "Totals-Actual Disbursements", "450,000"),
                        new ReportAreaForTests(new AreaOwner(50), "Project Title", "activity with capital spending", "Region", "Chisinau County", "Funding-2014-Actual Commitments", "65,760,63", "Funding-2014-Actual Disbursements", "80,000", "Totals-Actual Commitments", "65,760,63", "Totals-Actual Disbursements", "80,000"),
                        new ReportAreaForTests(new AreaOwner(61), "Project Title", "activity-with-unfunded-components", "Region", "Transnistrian Region", "Funding-2014-Actual Commitments", "123,321", "Totals-Actual Commitments", "123,321")
                        ));
        
        ReportSpecificationImpl spec = buildSpecForFiltering("flat toa [second]", 
                Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.REGION), null, ColumnConstants.TYPE_OF_ASSISTANCE, Arrays.asList(2124l), true);
        
        runNiTestCase(spec, "en", acts, cor);
    }
    
    @Test
    public void testFilteringTypeOfAssistanceNotSecond() {
        NiReportModel cor = new NiReportModel("flat toa [not second]")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 20))",
                        "(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Region: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 16));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 18, colSpan: 2))",
                        "(2006: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 2));(2009: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 2));(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 6, colSpan: 2));(2011: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 8, colSpan: 2));(2012: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 10, colSpan: 2));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 12, colSpan: 2));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 14, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 16, colSpan: 2))",
                        "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 14, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 15, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 16, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 17, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 18, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 19, colSpan: 1))"))
                    .withWarnings(Arrays.asList())
                    .withBody(      new ReportAreaForTests(null)
                      .withContents("Project Title", "", "Region", "", "Funding-2006-Actual Commitments", "96,840,58", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "100,000", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "780,311", "Funding-2011-Actual Commitments", "1,213,119", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "25,000", "Funding-2012-Actual Disbursements", "12,000", "Funding-2013-Actual Commitments", "4,949,864", "Funding-2013-Actual Disbursements", "1,266,956", "Funding-2014-Actual Commitments", "3,570,732,14", "Funding-2014-Actual Disbursements", "180,200", "Funding-2015-Actual Commitments", "1,971,831,84", "Funding-2015-Actual Disbursements", "437,335", "Totals-Actual Commitments", "11,927,387,56", "Totals-Actual Disbursements", "2,676,802")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(12), "Project Title", "TAC_activity_1", "Region", "Dubasari County", "Funding-2010-Actual Disbursements", "123,321", "Funding-2011-Actual Commitments", "213,231", "Totals-Actual Commitments", "213,231", "Totals-Actual Disbursements", "123,321"),
                        new ReportAreaForTests(new AreaOwner(13), "Project Title", "TAC_activity_2", "Region", "Falesti County", "Funding-2010-Actual Disbursements", "453,213", "Funding-2011-Actual Commitments", "999,888", "Totals-Actual Commitments", "999,888", "Totals-Actual Disbursements", "453,213"),
                        new ReportAreaForTests(new AreaOwner(18), "Project Title", "Test MTEF directed", "Region", "Anenii Noi County", "Funding-2010-Actual Disbursements", "143,777", "Totals-Actual Disbursements", "143,777"),
                        new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Region", "Anenii Noi County", "Funding-2013-Actual Disbursements", "545,000", "Totals-Actual Disbursements", "545,000"),
                        new ReportAreaForTests(new AreaOwner(26), "Project Title", "date-filters-activity", "Region", "", "Funding-2009-Actual Commitments", "100,000", "Funding-2010-Actual Disbursements", "60,000", "Funding-2012-Actual Commitments", "25,000", "Funding-2012-Actual Disbursements", "12,000", "Totals-Actual Commitments", "125,000", "Totals-Actual Disbursements", "72,000"),
                        new ReportAreaForTests(new AreaOwner(28), "Project Title", "ptc activity 1", "Region", "Anenii Noi County", "Funding-2013-Actual Commitments", "666,777", "Totals-Actual Commitments", "666,777"),
                        new ReportAreaForTests(new AreaOwner(29), "Project Title", "ptc activity 2", "Region", "Anenii Noi County", "Funding-2013-Actual Commitments", "333,222", "Totals-Actual Commitments", "333,222"),
                        new ReportAreaForTests(new AreaOwner(30), "Project Title", "SSC Project 1", "Region", "Anenii Noi County", "Funding-2013-Actual Commitments", "111,333", "Funding-2013-Actual Disbursements", "555,111", "Totals-Actual Commitments", "111,333", "Totals-Actual Disbursements", "555,111"),
                        new ReportAreaForTests(new AreaOwner(31), "Project Title", "SSC Project 2", "Region", "Edinet County", "Funding-2013-Actual Commitments", "567,421", "Funding-2013-Actual Disbursements", "131,845", "Totals-Actual Commitments", "567,421", "Totals-Actual Disbursements", "131,845"),
                        new ReportAreaForTests(new AreaOwner(32), "Project Title", "crazy funding 1", "Region", "Balti County", "Funding-2013-Actual Commitments", "111,111", "Totals-Actual Commitments", "111,111"),
                        new ReportAreaForTests(new AreaOwner(33), "Project Title", "Activity with Zones", "Region", "Anenii Noi County, Balti County", "Funding-2013-Actual Commitments", "570,000", "Totals-Actual Commitments", "570,000"),
                        new ReportAreaForTests(new AreaOwner(36), "Project Title", "Activity With Zones and Percentages", "Region", "Anenii Noi County, Balti County", "Funding-2013-Actual Commitments", "890,000", "Totals-Actual Commitments", "890,000"),
                        new ReportAreaForTests(new AreaOwner(40), "Project Title", "SubNational no percentages", "Region", "Anenii Noi County, Balti County", "Funding-2014-Actual Commitments", "75,000", "Totals-Actual Commitments", "75,000"),
                        new ReportAreaForTests(new AreaOwner(41), "Project Title", "Activity Linked With Pledge", "Region", "Chisinau City", "Funding-2014-Actual Commitments", "50,000", "Totals-Actual Commitments", "50,000"),
                        new ReportAreaForTests(new AreaOwner(43), "Project Title", "Activity with primary_tertiary_program", "Region", "", "Funding-2014-Actual Commitments", "50,000", "Totals-Actual Commitments", "50,000"),
                        new ReportAreaForTests(new AreaOwner(44), "Project Title", "activity with primary_program", "Region", "", "Funding-2014-Actual Commitments", "32,000", "Totals-Actual Commitments", "32,000"),
                        new ReportAreaForTests(new AreaOwner(45), "Project Title", "activity with tertiary_program", "Region", "", "Funding-2014-Actual Commitments", "15,000", "Totals-Actual Commitments", "15,000"),
                        new ReportAreaForTests(new AreaOwner(46), "Project Title", "pledged education activity 1", "Region", "Chisinau County", "Funding-2013-Actual Commitments", "1,700,000", "Funding-2014-Actual Commitments", "3,300,000", "Totals-Actual Commitments", "5,000,000"),
                        new ReportAreaForTests(new AreaOwner(52), "Project Title", "activity with contracting agency", "Region", "Balti County, Transnistrian Region", "Funding-2006-Actual Commitments", "96,840,58", "Funding-2014-Actual Disbursements", "50,000", "Totals-Actual Commitments", "96,840,58", "Totals-Actual Disbursements", "50,000"),
                        new ReportAreaForTests(new AreaOwner(53), "Project Title", "new activity with contracting", "Funding-2014-Actual Commitments", "12,000", "Totals-Actual Commitments", "12,000"),
                        new ReportAreaForTests(new AreaOwner(63), "Project Title", "activity with funded components", "Region", "", "Funding-2014-Actual Commitments", "100", "Totals-Actual Commitments", "100"),
                        new ReportAreaForTests(new AreaOwner(64), "Project Title", "Unvalidated activity", "Region", "", "Funding-2015-Actual Commitments", "45,000", "Totals-Actual Commitments", "45,000"),
                        new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Region", "Balti County", "Funding-2015-Actual Commitments", "456,789", "Funding-2015-Actual Disbursements", "321,765", "Totals-Actual Commitments", "456,789", "Totals-Actual Disbursements", "321,765"),
                        new ReportAreaForTests(new AreaOwner(66), "Project Title", "Activity 2 with multiple agreements", "Region", "Chisinau County", "Funding-2015-Actual Commitments", "1,200", "Totals-Actual Commitments", "1,200"),
                        new ReportAreaForTests(new AreaOwner(67), "Project Title", "third activity with agreements", "Region", "Chisinau City", "Funding-2015-Actual Commitments", "123,456", "Totals-Actual Commitments", "123,456"),
                        new ReportAreaForTests(new AreaOwner(68), "Project Title", "activity with incomplete agreement", "Region", "", "Funding-2015-Actual Commitments", "123,000", "Totals-Actual Commitments", "123,000"),
                        new ReportAreaForTests(new AreaOwner(69), "Project Title", "Activity with planned disbursements", "Region", "", "Funding-2014-Actual Disbursements", "200", "Funding-2015-Actual Disbursements", "570", "Totals-Actual Disbursements", "770"),
                        new ReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms", "Region", "Balti County, Drochia County", "Funding-2015-Actual Commitments", "888,000", "Totals-Actual Commitments", "888,000"),
                        new ReportAreaForTests(new AreaOwner(71), "Project Title", "activity_with_disaster_response", "Region", "", "Funding-2014-Actual Commitments", "33,000", "Funding-2015-Actual Commitments", "117,000", "Totals-Actual Commitments", "150,000"),
                        new ReportAreaForTests(new AreaOwner(73), "Project Title", "activity with directed MTEFs", "Region", "Chisinau City", "Funding-2015-Actual Commitments", "123,456", "Totals-Actual Commitments", "123,456"),
                        new ReportAreaForTests(new AreaOwner(76), "Project Title", "activity with pipeline MTEFs and act. disb", "Region", "Chisinau County", "Funding-2013-Actual Disbursements", "35,000", "Funding-2014-Actual Disbursements", "75,000", "Totals-Actual Disbursements", "110,000"),
                        new ReportAreaForTests(new AreaOwner(77), "Project Title", "execution rate activity", "Region", "Chisinau City, Dubasari County", "Funding-2014-Actual Disbursements", "55,000", "Funding-2015-Actual Disbursements", "35,000", "Totals-Actual Disbursements", "90,000"),
                        new ReportAreaForTests(new AreaOwner(78), "Project Title", "activity with many MTEFs", "Region", "Drochia County", "Funding-2015-Actual Disbursements", "80,000", "Totals-Actual Disbursements", "80,000"),
                        new ReportAreaForTests(new AreaOwner(79), "Project Title", "with weird currencies", "Region", "", "Funding-2014-Actual Commitments", "3,632,14", "Funding-2015-Actual Commitments", "93,930,84", "Totals-Actual Commitments", "97,562,98")      ));
;
        
        ReportSpecificationImpl spec = buildSpecForFiltering("flat toa [not second]", 
                Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.REGION), null, ColumnConstants.TYPE_OF_ASSISTANCE, Arrays.asList(2124l), false);
        
        runNiTestCase(spec, "en", acts, cor);
    }

    @Test
    public void testFilterByImplementationLevel() {
        NiReportModel cor = new NiReportModel("flat impl. level [national]")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 14))",
                        "(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Region: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 10));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 12, colSpan: 2))",
                        "(2009: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 2));(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 2));(2012: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 6, colSpan: 2));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 8, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 10, colSpan: 2))",
                        "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1))"))
                    .withWarnings(Arrays.asList())
                    .withBody(      new ReportAreaForTests(null)
                      .withContents("Project Title", "", "Region", "", "Funding-2009-Actual Commitments", "100,000", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "60,000", "Funding-2012-Actual Commitments", "25,000", "Funding-2012-Actual Disbursements", "12,000", "Funding-2014-Actual Commitments", "133,732,14", "Funding-2014-Actual Disbursements", "200", "Funding-2015-Actual Commitments", "378,930,84", "Funding-2015-Actual Disbursements", "570", "Totals-Actual Commitments", "637,662,98", "Totals-Actual Disbursements", "72,770")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(25), "Project Title", "mtef activity 1", "Region", ""),
                        new ReportAreaForTests(new AreaOwner(26), "Project Title", "date-filters-activity", "Region", "", "Funding-2009-Actual Commitments", "100,000", "Funding-2010-Actual Disbursements", "60,000", "Funding-2012-Actual Commitments", "25,000", "Funding-2012-Actual Disbursements", "12,000", "Totals-Actual Commitments", "125,000", "Totals-Actual Disbursements", "72,000"),
                        new ReportAreaForTests(new AreaOwner(43), "Project Title", "Activity with primary_tertiary_program", "Region", "", "Funding-2014-Actual Commitments", "50,000", "Totals-Actual Commitments", "50,000"),
                        new ReportAreaForTests(new AreaOwner(44), "Project Title", "activity with primary_program", "Region", "", "Funding-2014-Actual Commitments", "32,000", "Totals-Actual Commitments", "32,000"),
                        new ReportAreaForTests(new AreaOwner(45), "Project Title", "activity with tertiary_program", "Region", "", "Funding-2014-Actual Commitments", "15,000", "Totals-Actual Commitments", "15,000"),
                        new ReportAreaForTests(new AreaOwner(63), "Project Title", "activity with funded components", "Region", "", "Funding-2014-Actual Commitments", "100", "Totals-Actual Commitments", "100"),
                        new ReportAreaForTests(new AreaOwner(64), "Project Title", "Unvalidated activity", "Region", "", "Funding-2015-Actual Commitments", "45,000", "Totals-Actual Commitments", "45,000"),
                        new ReportAreaForTests(new AreaOwner(68), "Project Title", "activity with incomplete agreement", "Region", "", "Funding-2015-Actual Commitments", "123,000", "Totals-Actual Commitments", "123,000"),
                        new ReportAreaForTests(new AreaOwner(69), "Project Title", "Activity with planned disbursements", "Region", "", "Funding-2014-Actual Disbursements", "200", "Funding-2015-Actual Disbursements", "570", "Totals-Actual Disbursements", "770"),
                        new ReportAreaForTests(new AreaOwner(71), "Project Title", "activity_with_disaster_response", "Region", "", "Funding-2014-Actual Commitments", "33,000", "Funding-2015-Actual Commitments", "117,000", "Totals-Actual Commitments", "150,000"),
                        new ReportAreaForTests(new AreaOwner(79), "Project Title", "with weird currencies", "Region", "", "Funding-2014-Actual Commitments", "3,632,14", "Funding-2015-Actual Commitments", "93,930,84", "Totals-Actual Commitments", "97,562,98")));

        ReportSpecificationImpl spec = buildSpecForFiltering("flat impl. level [national]", 
                Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.REGION), null, ColumnConstants.IMPLEMENTATION_LEVEL, Arrays.asList(70l), true);
        
        runNiTestCase(spec, "en", acts, cor);
    }
    
    @Test
    public void testFilterByImplementationLevelNot() {
        NiReportModel cor = new NiReportModel("flat impl. level [not national]")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 16))",
                        "(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Region: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 12));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 14, colSpan: 2))",
                        "(2006: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 2));(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 2));(2011: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 6, colSpan: 2));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 8, colSpan: 2));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 10, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 12, colSpan: 2))",
                        "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 14, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 15, colSpan: 1))"))
                    .withWarnings(Arrays.asList())
                    .withBody(      new ReportAreaForTests(null)
                      .withContents("Project Title", "", "Region", "", "Funding-2006-Actual Commitments", "96,840,58", "Funding-2006-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "720,311", "Funding-2011-Actual Commitments", "1,213,119", "Funding-2011-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "7,842,086", "Funding-2013-Actual Disbursements", "1,266,956", "Funding-2014-Actual Commitments", "8,014,081,63", "Funding-2014-Actual Disbursements", "710,000", "Funding-2015-Actual Commitments", "1,592,901", "Funding-2015-Actual Disbursements", "436,765", "Totals-Actual Commitments", "18,759,028,21", "Totals-Actual Disbursements", "3,134,032")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(12), "Project Title", "TAC_activity_1", "Region", "Dubasari County", "Funding-2010-Actual Disbursements", "123,321", "Funding-2011-Actual Commitments", "213,231", "Totals-Actual Commitments", "213,231", "Totals-Actual Disbursements", "123,321"),
                        new ReportAreaForTests(new AreaOwner(13), "Project Title", "TAC_activity_2", "Region", "Falesti County", "Funding-2010-Actual Disbursements", "453,213", "Funding-2011-Actual Commitments", "999,888", "Totals-Actual Commitments", "999,888", "Totals-Actual Disbursements", "453,213"),
                        new ReportAreaForTests(new AreaOwner(15), "Project Title", "Proposed Project Cost 1 - USD", "Region", "Drochia County"),
                        new ReportAreaForTests(new AreaOwner(17), "Project Title", "Proposed Project Cost 2 - EUR", "Region", "Anenii Noi County"),
                        new ReportAreaForTests(new AreaOwner(18), "Project Title", "Test MTEF directed", "Region", "Anenii Noi County", "Funding-2010-Actual Disbursements", "143,777", "Totals-Actual Disbursements", "143,777"),
                        new ReportAreaForTests(new AreaOwner(19), "Project Title", "Pure MTEF Project", "Region", "Cahul County"),
                        new ReportAreaForTests(new AreaOwner(21), "Project Title", "activity with components", "Region", "Anenii Noi County"),
                        new ReportAreaForTests(new AreaOwner(23), "Project Title", "Project with documents", "Region", "Balti County"),
                        new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Region", "Anenii Noi County", "Funding-2013-Actual Disbursements", "545,000", "Totals-Actual Disbursements", "545,000"),
                        new ReportAreaForTests(new AreaOwner(27), "Project Title", "mtef activity 2", "Region", "Anenii Noi County"),
                        new ReportAreaForTests(new AreaOwner(28), "Project Title", "ptc activity 1", "Region", "Anenii Noi County", "Funding-2013-Actual Commitments", "666,777", "Totals-Actual Commitments", "666,777"),
                        new ReportAreaForTests(new AreaOwner(29), "Project Title", "ptc activity 2", "Region", "Anenii Noi County", "Funding-2013-Actual Commitments", "333,222", "Totals-Actual Commitments", "333,222"),
                        new ReportAreaForTests(new AreaOwner(30), "Project Title", "SSC Project 1", "Region", "Anenii Noi County", "Funding-2013-Actual Commitments", "111,333", "Funding-2013-Actual Disbursements", "555,111", "Totals-Actual Commitments", "111,333", "Totals-Actual Disbursements", "555,111"),
                        new ReportAreaForTests(new AreaOwner(31), "Project Title", "SSC Project 2", "Region", "Edinet County", "Funding-2013-Actual Commitments", "567,421", "Funding-2013-Actual Disbursements", "131,845", "Totals-Actual Commitments", "567,421", "Totals-Actual Disbursements", "131,845"),
                        new ReportAreaForTests(new AreaOwner(32), "Project Title", "crazy funding 1", "Region", "Balti County", "Funding-2013-Actual Commitments", "333,333", "Totals-Actual Commitments", "333,333"),
                        new ReportAreaForTests(new AreaOwner(33), "Project Title", "Activity with Zones", "Region", "Anenii Noi County, Balti County", "Funding-2013-Actual Commitments", "570,000", "Totals-Actual Commitments", "570,000"),
                        new ReportAreaForTests(new AreaOwner(36), "Project Title", "Activity With Zones and Percentages", "Region", "Anenii Noi County, Balti County", "Funding-2013-Actual Commitments", "890,000", "Totals-Actual Commitments", "890,000"),
                        new ReportAreaForTests(new AreaOwner(40), "Project Title", "SubNational no percentages", "Region", "Anenii Noi County, Balti County", "Funding-2014-Actual Commitments", "75,000", "Totals-Actual Commitments", "75,000"),
                        new ReportAreaForTests(new AreaOwner(41), "Project Title", "Activity Linked With Pledge", "Region", "Chisinau City", "Funding-2014-Actual Commitments", "50,000", "Totals-Actual Commitments", "50,000"),
                        new ReportAreaForTests(new AreaOwner(46), "Project Title", "pledged education activity 1", "Region", "Chisinau County", "Funding-2013-Actual Commitments", "1,700,000", "Funding-2014-Actual Commitments", "3,300,000", "Totals-Actual Commitments", "5,000,000"),
                        new ReportAreaForTests(new AreaOwner(48), "Project Title", "pledged 2", "Region", "Cahul County", "Funding-2013-Actual Commitments", "2,670,000", "Funding-2014-Actual Commitments", "4,400,000", "Funding-2014-Actual Disbursements", "450,000", "Totals-Actual Commitments", "7,070,000", "Totals-Actual Disbursements", "450,000"),
                        new ReportAreaForTests(new AreaOwner(50), "Project Title", "activity with capital spending", "Region", "Chisinau County", "Funding-2014-Actual Commitments", "65,760,63", "Funding-2014-Actual Disbursements", "80,000", "Totals-Actual Commitments", "65,760,63", "Totals-Actual Disbursements", "80,000"),
                        new ReportAreaForTests(new AreaOwner(52), "Project Title", "activity with contracting agency", "Region", "Balti County, Transnistrian Region", "Funding-2006-Actual Commitments", "96,840,58", "Funding-2014-Actual Disbursements", "50,000", "Totals-Actual Commitments", "96,840,58", "Totals-Actual Disbursements", "50,000"),
                        new ReportAreaForTests(new AreaOwner(61), "Project Title", "activity-with-unfunded-components", "Region", "Transnistrian Region", "Funding-2014-Actual Commitments", "123,321", "Totals-Actual Commitments", "123,321"),
                        new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Region", "Balti County", "Funding-2015-Actual Commitments", "456,789", "Funding-2015-Actual Disbursements", "321,765", "Totals-Actual Commitments", "456,789", "Totals-Actual Disbursements", "321,765"),
                        new ReportAreaForTests(new AreaOwner(66), "Project Title", "Activity 2 with multiple agreements", "Region", "Chisinau County", "Funding-2015-Actual Commitments", "1,200", "Totals-Actual Commitments", "1,200"),
                        new ReportAreaForTests(new AreaOwner(67), "Project Title", "third activity with agreements", "Region", "Chisinau City", "Funding-2015-Actual Commitments", "123,456", "Totals-Actual Commitments", "123,456"),
                        new ReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms", "Region", "Balti County, Drochia County", "Funding-2015-Actual Commitments", "888,000", "Totals-Actual Commitments", "888,000"),
                        new ReportAreaForTests(new AreaOwner(73), "Project Title", "activity with directed MTEFs", "Region", "Chisinau City", "Funding-2015-Actual Commitments", "123,456", "Totals-Actual Commitments", "123,456"),
                        new ReportAreaForTests(new AreaOwner(76), "Project Title", "activity with pipeline MTEFs and act. disb", "Region", "Chisinau County", "Funding-2013-Actual Disbursements", "35,000", "Funding-2014-Actual Disbursements", "75,000", "Totals-Actual Disbursements", "110,000"),
                        new ReportAreaForTests(new AreaOwner(77), "Project Title", "execution rate activity", "Region", "Chisinau City, Dubasari County", "Funding-2014-Actual Disbursements", "55,000", "Funding-2015-Actual Disbursements", "35,000", "Totals-Actual Disbursements", "90,000"),
                        new ReportAreaForTests(new AreaOwner(78), "Project Title", "activity with many MTEFs", "Region", "Drochia County", "Funding-2015-Actual Disbursements", "80,000", "Totals-Actual Disbursements", "80,000")));

        ReportSpecificationImpl spec = buildSpecForFiltering("flat impl. level [not national]", 
                Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.REGION), null, ColumnConstants.IMPLEMENTATION_LEVEL, Arrays.asList(70l), false);
        
        runNiTestCase(spec, "en", acts, cor);
    }
    
    @Test
    public void testFilterByActivityId() {
        NiReportModel cor = new NiReportModel("flat filter by amp_activity_id")
            .withHeaders(Arrays.asList(
                "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 8))",
                "(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Region: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 4));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 6, colSpan: 2))",
                "(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 2))",
                "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1))"))
            .withWarnings(Arrays.asList())
            .withBody(      new ReportAreaForTests(null)
              .withContents("Project Title", "", "Region", "", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "55,000", "Funding-2015-Actual Commitments", "45,000", "Funding-2015-Actual Disbursements", "35,000", "Totals-Actual Commitments", "45,000", "Totals-Actual Disbursements", "90,000")
              .withChildren(
                new ReportAreaForTests(new AreaOwner(64), "Project Title", "Unvalidated activity", "Region", "", "Funding-2015-Actual Commitments", "45,000", "Totals-Actual Commitments", "45,000"),
                new ReportAreaForTests(new AreaOwner(77), "Project Title", "execution rate activity", "Region", "Chisinau City, Dubasari County", "Funding-2014-Actual Disbursements", "55,000", "Funding-2015-Actual Disbursements", "35,000", "Totals-Actual Disbursements", "90,000")));
        
        ReportSpecificationImpl spec = buildSpecForFiltering("flat filter by amp_activity_id", 
            Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.REGION), null, ColumnConstants.ACTIVITY_ID, Arrays.asList(77l, 64l), true);
        
        runNiTestCase(spec, "en", acts, cor);
    }
    
    @Test
    public void testFilterByNotActivityId() {
        NiReportModel cor2 = new NiReportModel("flat filter by [not amp_activity_id]")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 8))",
                        "(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Region: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 4));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 6, colSpan: 2))",
                        "(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 2));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 2))",
                        "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1))"))
                    .withWarnings(Arrays.asList())
                    .withBody(      new ReportAreaForTests(null)
                      .withContents("Project Title", "", "Region", "", "Funding-2013-Actual Commitments", "2,670,000", "Funding-2013-Actual Disbursements", "545,000", "Funding-2014-Actual Commitments", "4,400,000", "Funding-2014-Actual Disbursements", "450,000", "Totals-Actual Commitments", "7,070,000", "Totals-Actual Disbursements", "995,000")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Region", "Anenii Noi County", "Funding-2013-Actual Disbursements", "545,000", "Totals-Actual Disbursements", "545,000"),
                        new ReportAreaForTests(new AreaOwner(48), "Project Title", "pledged 2", "Region", "Cahul County", "Funding-2013-Actual Commitments", "2,670,000", "Funding-2014-Actual Commitments", "4,400,000", "Funding-2014-Actual Disbursements", "450,000", "Totals-Actual Commitments", "7,070,000", "Totals-Actual Disbursements", "450,000")));
        
        ReportSpecificationImpl spec = buildSpecForFiltering("flat filter by [not amp_activity_id]", 
                Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.REGION), null, ColumnConstants.ACTIVITY_ID, Arrays.asList(77l, 64l), false);
        
        runNiTestCase(spec, "en", Arrays.asList("Unvalidated activity", "execution rate activity", "Eth Water", "pledged 2"), cor2);
    }
    
    @Test
    public void testFilterByDonorAgency() {
        NiReportModel cor2 = new NiReportModel("flat filter by donor agency")
            .withHeaders(Arrays.asList(
                "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 8))",
                "(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Region: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 4));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 6, colSpan: 2))",
                "(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 2));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 2))",
                "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1))"))
            .withWarnings(Arrays.asList())
            .withBody(      new ReportAreaForTests(null)
              .withContents("Project Title", "", "Region", "", "Funding-2013-Actual Commitments", "2,670,000", "Funding-2013-Actual Disbursements", "415,000", "Funding-2014-Actual Commitments", "4,400,000", "Funding-2014-Actual Disbursements", "450,000", "Totals-Actual Commitments", "7,070,000", "Totals-Actual Disbursements", "865,000")
              .withChildren(
                new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Region", "Anenii Noi County", "Funding-2013-Actual Disbursements", "415,000", "Totals-Actual Disbursements", "415,000"),
                new ReportAreaForTests(new AreaOwner(48), "Project Title", "pledged 2", "Region", "Cahul County", "Funding-2013-Actual Commitments", "2,670,000", "Funding-2014-Actual Commitments", "4,400,000", "Funding-2014-Actual Disbursements", "450,000", "Totals-Actual Commitments", "7,070,000", "Totals-Actual Disbursements", "450,000")));

        ReportSpecificationImpl spec = buildSpecForFiltering("flat filter by donor agency", 
            Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.REGION), null, ColumnConstants.DONOR_AGENCY, Arrays.asList(21696l, 21702l, 21701l), true);
        
        runNiTestCase(spec, "en", Arrays.asList("Unvalidated activity", "execution rate activity", "Eth Water", "pledged 2"), cor2);
    }
    
    @Test
    public void testFilterByDonorGroup() {
        NiReportModel cor2 = new NiReportModel("flat filter by NOT donor group")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 19))",
                        "(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Primary Sector: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Donor Group: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 3, colSpan: 14));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 17, colSpan: 2))",
                        "(2009: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 2));(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 2));(2011: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 7, colSpan: 2));(2012: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 9, colSpan: 2));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 11, colSpan: 2));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 13, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 15, colSpan: 2))",
                        "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 14, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 15, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 16, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 17, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 18, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                        .withContents("Project Title", "", "Primary Sector", "", "Donor Group", "", "Funding-2009-Actual Commitments", "100,000", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "780,311", "Funding-2011-Actual Commitments", "1,213,119", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "25,000", "Funding-2012-Actual Disbursements", "12,000", "Funding-2013-Actual Commitments", "6,160,643", "Funding-2013-Actual Disbursements", "691,845", "Funding-2014-Actual Commitments", "7,875,732,14", "Funding-2014-Actual Disbursements", "580,200", "Funding-2015-Actual Commitments", "387,042,84", "Funding-2015-Actual Disbursements", "115,570", "Totals-Actual Commitments", "15,761,536,98", "Totals-Actual Disbursements", "2,179,926")
                        .withChildren(
                                new ReportAreaForTests(new AreaOwner(12), "Project Title", "TAC_activity_1", "Primary Sector", "112 - BASIC EDUCATION", "Donor Group", "International", "Funding-2010-Actual Disbursements", "123,321", "Funding-2011-Actual Commitments", "213,231", "Totals-Actual Commitments", "213,231", "Totals-Actual Disbursements", "123,321"),
                                new ReportAreaForTests(new AreaOwner(13), "Project Title", "TAC_activity_2", "Primary Sector", "130 - POPULATION POLICIES/PROGRAMMES AND REPRODUCTIVE HEALTH", "Donor Group", "American", "Funding-2010-Actual Disbursements", "453,213", "Funding-2011-Actual Commitments", "999,888", "Totals-Actual Commitments", "999,888", "Totals-Actual Disbursements", "453,213"),
                                new ReportAreaForTests(new AreaOwner(18), "Project Title", "Test MTEF directed", "Primary Sector", "110 - EDUCATION", "Donor Group", "American, National", "Funding-2010-Actual Disbursements", "143,777", "Totals-Actual Disbursements", "143,777"),
                                new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Primary Sector", "110 - EDUCATION", "Donor Group", "American, European", "Funding-2013-Actual Disbursements", "525,000", "Totals-Actual Disbursements", "525,000"),
                                new ReportAreaForTests(new AreaOwner(26), "Project Title", "date-filters-activity", "Primary Sector", "110 - EDUCATION", "Donor Group", "National", "Funding-2009-Actual Commitments", "100,000", "Funding-2010-Actual Disbursements", "60,000", "Funding-2012-Actual Commitments", "25,000", "Funding-2012-Actual Disbursements", "12,000", "Totals-Actual Commitments", "125,000", "Totals-Actual Disbursements", "72,000"),
                                new ReportAreaForTests(new AreaOwner(29), "Project Title", "ptc activity 2", "Primary Sector", "110 - EDUCATION", "Donor Group", "American", "Funding-2013-Actual Commitments", "333,222", "Totals-Actual Commitments", "333,222"),
                                new ReportAreaForTests(new AreaOwner(31), "Project Title", "SSC Project 2", "Primary Sector", "112 - BASIC EDUCATION", "Donor Group", "American", "Funding-2013-Actual Commitments", "567,421", "Funding-2013-Actual Disbursements", "131,845", "Totals-Actual Commitments", "567,421", "Totals-Actual Disbursements", "131,845"),
                                new ReportAreaForTests(new AreaOwner(36), "Project Title", "Activity With Zones and Percentages", "Primary Sector", "110 - EDUCATION, 120 - HEALTH", "Donor Group", "European", "Funding-2013-Actual Commitments", "890,000", "Totals-Actual Commitments", "890,000"),
                                new ReportAreaForTests(new AreaOwner(40), "Project Title", "SubNational no percentages", "Primary Sector", "110 - EDUCATION", "Donor Group", "National", "Funding-2014-Actual Commitments", "75,000", "Totals-Actual Commitments", "75,000"),
                                new ReportAreaForTests(new AreaOwner(43), "Project Title", "Activity with primary_tertiary_program", "Primary Sector", "110 - EDUCATION", "Donor Group", "International", "Funding-2014-Actual Commitments", "50,000", "Totals-Actual Commitments", "50,000"),
                                new ReportAreaForTests(new AreaOwner(44), "Project Title", "activity with primary_program", "Primary Sector", "110 - EDUCATION", "Donor Group", "International", "Funding-2014-Actual Commitments", "32,000", "Totals-Actual Commitments", "32,000"),
                                new ReportAreaForTests(new AreaOwner(45), "Project Title", "activity with tertiary_program", "Primary Sector", "110 - EDUCATION", "Donor Group", "National", "Funding-2014-Actual Commitments", "15,000", "Totals-Actual Commitments", "15,000"),
                                new ReportAreaForTests(new AreaOwner(46), "Project Title", "pledged education activity 1", "Primary Sector", "110 - EDUCATION", "Donor Group", "American", "Funding-2013-Actual Commitments", "1,700,000", "Funding-2014-Actual Commitments", "3,300,000", "Totals-Actual Commitments", "5,000,000"),
                                new ReportAreaForTests(new AreaOwner(48), "Project Title", "pledged 2", "Primary Sector", "113 - SECONDARY EDUCATION", "Donor Group", "American", "Funding-2013-Actual Commitments", "2,670,000", "Funding-2014-Actual Commitments", "4,400,000", "Funding-2014-Actual Disbursements", "450,000", "Totals-Actual Commitments", "7,070,000", "Totals-Actual Disbursements", "450,000"),
                                new ReportAreaForTests(new AreaOwner(63), "Project Title", "activity with funded components", "Primary Sector", "110 - EDUCATION", "Donor Group", "American, International", "Funding-2014-Actual Commitments", "100", "Totals-Actual Commitments", "100"),
                                new ReportAreaForTests(new AreaOwner(64), "Project Title", "Unvalidated activity", "Primary Sector", "110 - EDUCATION", "Donor Group", "International", "Funding-2015-Actual Commitments", "45,000", "Totals-Actual Commitments", "45,000"),
                                new ReportAreaForTests(new AreaOwner(66), "Project Title", "Activity 2 with multiple agreements", "Primary Sector", "110 - EDUCATION", "Donor Group", "American, International, National", "Funding-2015-Actual Commitments", "1,200", "Totals-Actual Commitments", "1,200"),
                                new ReportAreaForTests(new AreaOwner(67), "Project Title", "third activity with agreements", "Primary Sector", "110 - EDUCATION", "Donor Group", "National", "Funding-2015-Actual Commitments", "123,456", "Totals-Actual Commitments", "123,456"),
                                new ReportAreaForTests(new AreaOwner(69), "Project Title", "Activity with planned disbursements", "Primary Sector", "112 - BASIC EDUCATION", "Donor Group", "American, European", "Funding-2014-Actual Disbursements", "200", "Funding-2015-Actual Disbursements", "570", "Totals-Actual Disbursements", "770"),
                                new ReportAreaForTests(new AreaOwner(73), "Project Title", "activity with directed MTEFs", "Primary Sector", "110 - EDUCATION", "Donor Group", "National", "Funding-2015-Actual Commitments", "123,456", "Totals-Actual Commitments", "123,456"),
                                new ReportAreaForTests(new AreaOwner(76), "Project Title", "activity with pipeline MTEFs and act. disb", "Primary Sector", "110 - EDUCATION", "Donor Group", "European, International", "Funding-2013-Actual Disbursements", "35,000", "Funding-2014-Actual Disbursements", "75,000", "Totals-Actual Disbursements", "110,000"),
                                new ReportAreaForTests(new AreaOwner(77), "Project Title", "execution rate activity", "Primary Sector", "110 - EDUCATION", "Donor Group", "International, National", "Funding-2014-Actual Disbursements", "55,000", "Funding-2015-Actual Disbursements", "35,000", "Totals-Actual Disbursements", "90,000"),
                                new ReportAreaForTests(new AreaOwner(78), "Project Title", "activity with many MTEFs", "Primary Sector", "110 - EDUCATION", "Donor Group", "American, European", "Funding-2015-Actual Disbursements", "80,000", "Totals-Actual Disbursements", "80,000"),
                                new ReportAreaForTests(new AreaOwner(79), "Project Title", "with weird currencies", "Primary Sector", "110 - EDUCATION, 112 - BASIC EDUCATION", "Donor Group", "European, National", "Funding-2014-Actual Commitments", "3,632,14", "Funding-2015-Actual Commitments", "93,930,84", "Totals-Actual Commitments", "97,562,98")      ));
        
        ReportSpecificationImpl spec = buildSpecForFiltering("flat filter by NOT donor group", 
                Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.PRIMARY_SECTOR, ColumnConstants.DONOR_GROUP), null, ColumnConstants.DONOR_GROUP, Arrays.asList(17l), false);
        
        runNiTestCase(spec, "en", acts, cor2);
    }
    
    @Test
    public void testFilteringByTransactionDate() { // fails in offline because of AMP-22605
        NiReportModel cor = new NiReportModel("flat filter by transaction date range")
            .withHeaders(Arrays.asList(
                "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 10))",
                "(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Primary Sector: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 6));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 8, colSpan: 2))",
                "(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 2));(2011: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 2));(2012: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 6, colSpan: 2))",
                "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1))"))
            .withWarnings(Arrays.asList())
            .withBody(      new ReportAreaForTests(null)
              .withContents("Project Title", "", "Primary Sector", "", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "780,311", "Funding-2011-Actual Commitments", "1,213,119", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "25,000", "Funding-2012-Actual Disbursements", "12,000", "Totals-Actual Commitments", "1,238,119", "Totals-Actual Disbursements", "792,311")
              .withChildren(
                new ReportAreaForTests(new AreaOwner(12), "Project Title", "TAC_activity_1", "Primary Sector", "112 - BASIC EDUCATION", "Funding-2010-Actual Disbursements", "123,321", "Funding-2011-Actual Commitments", "213,231", "Totals-Actual Commitments", "213,231", "Totals-Actual Disbursements", "123,321"),
                new ReportAreaForTests(new AreaOwner(13), "Project Title", "TAC_activity_2", "Primary Sector", "130 - POPULATION POLICIES/PROGRAMMES AND REPRODUCTIVE HEALTH", "Funding-2010-Actual Disbursements", "453,213", "Funding-2011-Actual Commitments", "999,888", "Totals-Actual Commitments", "999,888", "Totals-Actual Disbursements", "453,213"),
                new ReportAreaForTests(new AreaOwner(18), "Project Title", "Test MTEF directed", "Primary Sector", "110 - EDUCATION", "Funding-2010-Actual Disbursements", "143,777", "Totals-Actual Disbursements", "143,777"),
                new ReportAreaForTests(new AreaOwner(26), "Project Title", "date-filters-activity", "Primary Sector", "110 - EDUCATION", "Funding-2010-Actual Disbursements", "60,000", "Funding-2012-Actual Commitments", "25,000", "Funding-2012-Actual Disbursements", "12,000", "Totals-Actual Commitments", "25,000", "Totals-Actual Disbursements", "72,000")));

        ReportSpecificationImpl spec = buildSpecForFiltering("flat filter by transaction date range", 
                Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.PRIMARY_SECTOR), null, new ReportElement(ElementType.DATE), new FilterRule("2455197", "2456292", true, true, true));
        
        runNiTestCase(spec, "en", acts, cor);
    }
    
    
    @Test
    public void testFilteringByNegativeTransactionDate() { // fails in offline because of AMP-22605
        NiReportModel cor = new NiReportModel("flat filter by negative transaction date range")
            .withHeaders(Arrays.asList(
                    "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 14))",
                    "(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Primary Sector: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 10));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 12, colSpan: 2))",
                    "(2006: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 2));(2009: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 2));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 6, colSpan: 2));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 8, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 10, colSpan: 2))",
                    "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                  .withContents("Project Title", "", "Primary Sector", "", "Funding-2006-Actual Commitments", "96,840,58", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "100,000", "Funding-2009-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "7,842,086", "Funding-2013-Actual Disbursements", "1,266,956", "Funding-2014-Actual Commitments", "8,159,813,77", "Funding-2014-Actual Disbursements", "710,200", "Funding-2015-Actual Commitments", "1,971,831,84", "Funding-2015-Actual Disbursements", "437,335", "Totals-Actual Commitments", "18,170,572,19", "Totals-Actual Disbursements", "2,414,491")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Primary Sector", "110 - EDUCATION", "Funding-2013-Actual Disbursements", "545,000", "Totals-Actual Disbursements", "545,000"),
                    new ReportAreaForTests(new AreaOwner(26), "Project Title", "date-filters-activity", "Primary Sector", "110 - EDUCATION", "Funding-2009-Actual Commitments", "100,000", "Totals-Actual Commitments", "100,000"),
                    new ReportAreaForTests(new AreaOwner(28), "Project Title", "ptc activity 1", "Primary Sector", "110 - EDUCATION", "Funding-2013-Actual Commitments", "666,777", "Totals-Actual Commitments", "666,777"),
                    new ReportAreaForTests(new AreaOwner(29), "Project Title", "ptc activity 2", "Primary Sector", "110 - EDUCATION", "Funding-2013-Actual Commitments", "333,222", "Totals-Actual Commitments", "333,222"),
                    new ReportAreaForTests(new AreaOwner(30), "Project Title", "SSC Project 1", "Primary Sector", "110 - EDUCATION", "Funding-2013-Actual Commitments", "111,333", "Funding-2013-Actual Disbursements", "555,111", "Totals-Actual Commitments", "111,333", "Totals-Actual Disbursements", "555,111"),
                    new ReportAreaForTests(new AreaOwner(31), "Project Title", "SSC Project 2", "Primary Sector", "112 - BASIC EDUCATION", "Funding-2013-Actual Commitments", "567,421", "Funding-2013-Actual Disbursements", "131,845", "Totals-Actual Commitments", "567,421", "Totals-Actual Disbursements", "131,845"),
                    new ReportAreaForTests(new AreaOwner(32), "Project Title", "crazy funding 1", "Primary Sector", "110 - EDUCATION", "Funding-2013-Actual Commitments", "333,333", "Totals-Actual Commitments", "333,333"),
                    new ReportAreaForTests(new AreaOwner(33), "Project Title", "Activity with Zones", "Primary Sector", "110 - EDUCATION", "Funding-2013-Actual Commitments", "570,000", "Totals-Actual Commitments", "570,000"),
                    new ReportAreaForTests(new AreaOwner(36), "Project Title", "Activity With Zones and Percentages", "Primary Sector", "110 - EDUCATION, 120 - HEALTH", "Funding-2013-Actual Commitments", "890,000", "Totals-Actual Commitments", "890,000"),
                    new ReportAreaForTests(new AreaOwner(40), "Project Title", "SubNational no percentages", "Primary Sector", "110 - EDUCATION", "Funding-2014-Actual Commitments", "75,000", "Totals-Actual Commitments", "75,000"),
                    new ReportAreaForTests(new AreaOwner(41), "Project Title", "Activity Linked With Pledge", "Primary Sector", "110 - EDUCATION", "Funding-2014-Actual Commitments", "50,000", "Totals-Actual Commitments", "50,000"),
                    new ReportAreaForTests(new AreaOwner(43), "Project Title", "Activity with primary_tertiary_program", "Primary Sector", "110 - EDUCATION", "Funding-2014-Actual Commitments", "50,000", "Totals-Actual Commitments", "50,000"),
                    new ReportAreaForTests(new AreaOwner(44), "Project Title", "activity with primary_program", "Primary Sector", "110 - EDUCATION", "Funding-2014-Actual Commitments", "32,000", "Totals-Actual Commitments", "32,000"),
                    new ReportAreaForTests(new AreaOwner(45), "Project Title", "activity with tertiary_program", "Primary Sector", "110 - EDUCATION", "Funding-2014-Actual Commitments", "15,000", "Totals-Actual Commitments", "15,000"),
                    new ReportAreaForTests(new AreaOwner(46), "Project Title", "pledged education activity 1", "Primary Sector", "110 - EDUCATION", "Funding-2013-Actual Commitments", "1,700,000", "Funding-2014-Actual Commitments", "3,300,000", "Totals-Actual Commitments", "5,000,000"),
                    new ReportAreaForTests(new AreaOwner(48), "Project Title", "pledged 2", "Primary Sector", "113 - SECONDARY EDUCATION", "Funding-2013-Actual Commitments", "2,670,000", "Funding-2014-Actual Commitments", "4,400,000", "Funding-2014-Actual Disbursements", "450,000", "Totals-Actual Commitments", "7,070,000", "Totals-Actual Disbursements", "450,000"),
                    new ReportAreaForTests(new AreaOwner(50), "Project Title", "activity with capital spending", "Primary Sector", "110 - EDUCATION", "Funding-2014-Actual Commitments", "65,760,63", "Funding-2014-Actual Disbursements", "80,000", "Totals-Actual Commitments", "65,760,63", "Totals-Actual Disbursements", "80,000"),
                    new ReportAreaForTests(new AreaOwner(52), "Project Title", "activity with contracting agency", "Primary Sector", "110 - EDUCATION, 112 - BASIC EDUCATION, 120 - HEALTH", "Funding-2006-Actual Commitments", "96,840,58", "Funding-2014-Actual Disbursements", "50,000", "Totals-Actual Commitments", "96,840,58", "Totals-Actual Disbursements", "50,000"),
                    new ReportAreaForTests(new AreaOwner(53), "Project Title", "new activity with contracting", "Funding-2014-Actual Commitments", "12,000", "Totals-Actual Commitments", "12,000"),
                    new ReportAreaForTests(new AreaOwner(61), "Project Title", "activity-with-unfunded-components", "Primary Sector", "110 - EDUCATION", "Funding-2014-Actual Commitments", "123,321", "Totals-Actual Commitments", "123,321"),
                    new ReportAreaForTests(new AreaOwner(63), "Project Title", "activity with funded components", "Primary Sector", "110 - EDUCATION", "Funding-2014-Actual Commitments", "100", "Totals-Actual Commitments", "100"),
                    new ReportAreaForTests(new AreaOwner(64), "Project Title", "Unvalidated activity", "Primary Sector", "110 - EDUCATION", "Funding-2015-Actual Commitments", "45,000", "Totals-Actual Commitments", "45,000"),
                    new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Primary Sector", "110 - EDUCATION, 112 - BASIC EDUCATION", "Funding-2015-Actual Commitments", "456,789", "Funding-2015-Actual Disbursements", "321,765", "Totals-Actual Commitments", "456,789", "Totals-Actual Disbursements", "321,765"),
                    new ReportAreaForTests(new AreaOwner(66), "Project Title", "Activity 2 with multiple agreements", "Primary Sector", "110 - EDUCATION", "Funding-2015-Actual Commitments", "1,200", "Totals-Actual Commitments", "1,200"),
                    new ReportAreaForTests(new AreaOwner(67), "Project Title", "third activity with agreements", "Primary Sector", "110 - EDUCATION", "Funding-2015-Actual Commitments", "123,456", "Totals-Actual Commitments", "123,456"),
                    new ReportAreaForTests(new AreaOwner(68), "Project Title", "activity with incomplete agreement", "Primary Sector", "110 - EDUCATION", "Funding-2015-Actual Commitments", "123,000", "Totals-Actual Commitments", "123,000"),
                    new ReportAreaForTests(new AreaOwner(69), "Project Title", "Activity with planned disbursements", "Primary Sector", "112 - BASIC EDUCATION", "Funding-2014-Actual Disbursements", "200", "Funding-2015-Actual Disbursements", "570", "Totals-Actual Disbursements", "770"),
                    new ReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms", "Primary Sector", "110 - EDUCATION, 112 - BASIC EDUCATION", "Funding-2015-Actual Commitments", "888,000", "Totals-Actual Commitments", "888,000"),
                    new ReportAreaForTests(new AreaOwner(71), "Project Title", "activity_with_disaster_response", "Primary Sector", "110 - EDUCATION, 113 - SECONDARY EDUCATION", "Funding-2014-Actual Commitments", "33,000", "Funding-2015-Actual Commitments", "117,000", "Totals-Actual Commitments", "150,000"),
                    new ReportAreaForTests(new AreaOwner(73), "Project Title", "activity with directed MTEFs", "Primary Sector", "110 - EDUCATION", "Funding-2015-Actual Commitments", "123,456", "Totals-Actual Commitments", "123,456"),
                    new ReportAreaForTests(new AreaOwner(76), "Project Title", "activity with pipeline MTEFs and act. disb", "Primary Sector", "110 - EDUCATION", "Funding-2013-Actual Disbursements", "35,000", "Funding-2014-Actual Disbursements", "75,000", "Totals-Actual Disbursements", "110,000"),
                    new ReportAreaForTests(new AreaOwner(77), "Project Title", "execution rate activity", "Primary Sector", "110 - EDUCATION", "Funding-2014-Actual Disbursements", "55,000", "Funding-2015-Actual Disbursements", "35,000", "Totals-Actual Disbursements", "90,000"),
                    new ReportAreaForTests(new AreaOwner(78), "Project Title", "activity with many MTEFs", "Primary Sector", "110 - EDUCATION", "Funding-2015-Actual Disbursements", "80,000", "Totals-Actual Disbursements", "80,000"),
                    new ReportAreaForTests(new AreaOwner(79), "Project Title", "with weird currencies", "Primary Sector", "110 - EDUCATION, 112 - BASIC EDUCATION", "Funding-2014-Actual Commitments", "3,632,14", "Funding-2015-Actual Commitments", "93,930,84", "Totals-Actual Commitments", "97,562,98")));

        ReportSpecificationImpl spec = buildSpecForFiltering("flat filter by negative transaction date range", 
                Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.PRIMARY_SECTOR), null, new ReportElement(ElementType.DATE), new FilterRule("2455197", "2456292", true, true, false));
        
        runNiTestCase(spec, "en", acts, cor);
    }
    
    @Test
    public void testFilterByActivityCreatedOnRange() {
        NiReportModel cor = new NiReportModel("flat filter by created_on range")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 8))",
                        "(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Activity Created On: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 4));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 6, colSpan: 2))",
                        "(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 2));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 2))",
                        "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1))"))
                    .withWarnings(Arrays.asList())
                    .withBody(      new ReportAreaForTests(null)
                      .withContents("Project Title", "", "Activity Created On", "", "Funding-2013-Actual Commitments", "4,370,000", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "7,922,000", "Funding-2014-Actual Disbursements", "450,000", "Totals-Actual Commitments", "12,292,000", "Totals-Actual Disbursements", "450,000")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(40), "Project Title", "SubNational no percentages", "Activity Created On", "21/02/2014", "Funding-2014-Actual Commitments", "75,000", "Totals-Actual Commitments", "75,000"),
                        new ReportAreaForTests(new AreaOwner(41), "Project Title", "Activity Linked With Pledge", "Activity Created On", "27/03/2014", "Funding-2014-Actual Commitments", "50,000", "Totals-Actual Commitments", "50,000"),
                        new ReportAreaForTests(new AreaOwner(43), "Project Title", "Activity with primary_tertiary_program", "Activity Created On", "28/03/2014", "Funding-2014-Actual Commitments", "50,000", "Totals-Actual Commitments", "50,000"),
                        new ReportAreaForTests(new AreaOwner(44), "Project Title", "activity with primary_program", "Activity Created On", "28/03/2014", "Funding-2014-Actual Commitments", "32,000", "Totals-Actual Commitments", "32,000"),
                        new ReportAreaForTests(new AreaOwner(45), "Project Title", "activity with tertiary_program", "Activity Created On", "28/03/2014", "Funding-2014-Actual Commitments", "15,000", "Totals-Actual Commitments", "15,000"),
                        new ReportAreaForTests(new AreaOwner(46), "Project Title", "pledged education activity 1", "Activity Created On", "29/04/2014", "Funding-2013-Actual Commitments", "1,700,000", "Funding-2014-Actual Commitments", "3,300,000", "Totals-Actual Commitments", "5,000,000"),
                        new ReportAreaForTests(new AreaOwner(48), "Project Title", "pledged 2", "Activity Created On", "29/04/2014", "Funding-2013-Actual Commitments", "2,670,000", "Funding-2014-Actual Commitments", "4,400,000", "Funding-2014-Actual Disbursements", "450,000", "Totals-Actual Commitments", "7,070,000", "Totals-Actual Disbursements", "450,000")));
        
        ReportSpecificationImpl spec = buildSpecForFiltering("flat filter by created_on range", 
            Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.ACTIVITY_CREATED_ON), null, 
            new ReportElement(new ReportColumn(ColumnConstants.ACTIVITY_CREATED_ON)), new FilterRule("2456658", "2456809", true, true, true)); // 2014/jan/1 - 2014/jun/1
        
        runNiTestCase(spec, "en", acts, cor);
    }
    
    @Test
    public void testFilterByActivityCreatedOnMaximum() {
        NiReportModel cor = new NiReportModel("flat filter by created_on max")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 16))",
                        "(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Activity Created On: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 12));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 14, colSpan: 2))",
                        "(2009: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 2));(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 2));(2011: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 6, colSpan: 2));(2012: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 8, colSpan: 2));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 10, colSpan: 2));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 12, colSpan: 2))",
                        "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 14, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 15, colSpan: 1))"))
                    .withWarnings(Arrays.asList())
                    .withBody(      new ReportAreaForTests(null)
                      .withContents("Project Title", "", "Activity Created On", "", "Funding-2009-Actual Commitments", "100,000", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "780,311", "Funding-2011-Actual Commitments", "1,213,119", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "25,000", "Funding-2012-Actual Disbursements", "12,000", "Funding-2013-Actual Commitments", "7,842,086", "Funding-2013-Actual Disbursements", "1,231,956", "Funding-2014-Actual Commitments", "7,922,000", "Funding-2014-Actual Disbursements", "450,000", "Totals-Actual Commitments", "17,102,205", "Totals-Actual Disbursements", "2,474,267")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(12), "Project Title", "TAC_activity_1", "Activity Created On", "23/08/2013", "Funding-2010-Actual Disbursements", "123,321", "Funding-2011-Actual Commitments", "213,231", "Totals-Actual Commitments", "213,231", "Totals-Actual Disbursements", "123,321"),
                        new ReportAreaForTests(new AreaOwner(13), "Project Title", "TAC_activity_2", "Activity Created On", "23/08/2013", "Funding-2010-Actual Disbursements", "453,213", "Funding-2011-Actual Commitments", "999,888", "Totals-Actual Commitments", "999,888", "Totals-Actual Disbursements", "453,213"),
                        new ReportAreaForTests(new AreaOwner(15), "Project Title", "Proposed Project Cost 1 - USD", "Activity Created On", "01/10/2013"),
                        new ReportAreaForTests(new AreaOwner(17), "Project Title", "Proposed Project Cost 2 - EUR", "Activity Created On", "01/10/2013"),
                        new ReportAreaForTests(new AreaOwner(18), "Project Title", "Test MTEF directed", "Activity Created On", "10/10/2013", "Funding-2010-Actual Disbursements", "143,777", "Totals-Actual Disbursements", "143,777"),
                        new ReportAreaForTests(new AreaOwner(19), "Project Title", "Pure MTEF Project", "Activity Created On", "11/10/2013"),
                        new ReportAreaForTests(new AreaOwner(21), "Project Title", "activity with components", "Activity Created On", "15/11/2013"),
                        new ReportAreaForTests(new AreaOwner(23), "Project Title", "Project with documents", "Activity Created On", "18/11/2013"),
                        new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Activity Created On", "01/08/2013", "Funding-2013-Actual Disbursements", "545,000", "Totals-Actual Disbursements", "545,000"),
                        new ReportAreaForTests(new AreaOwner(25), "Project Title", "mtef activity 1", "Activity Created On", "05/08/2013"),
                        new ReportAreaForTests(new AreaOwner(26), "Project Title", "date-filters-activity", "Activity Created On", "21/09/2013", "Funding-2009-Actual Commitments", "100,000", "Funding-2010-Actual Disbursements", "60,000", "Funding-2012-Actual Commitments", "25,000", "Funding-2012-Actual Disbursements", "12,000", "Totals-Actual Commitments", "125,000", "Totals-Actual Disbursements", "72,000"),
                        new ReportAreaForTests(new AreaOwner(27), "Project Title", "mtef activity 2", "Activity Created On", "05/08/2013"),
                        new ReportAreaForTests(new AreaOwner(28), "Project Title", "ptc activity 1", "Activity Created On", "19/08/2013", "Funding-2013-Actual Commitments", "666,777", "Totals-Actual Commitments", "666,777"),
                        new ReportAreaForTests(new AreaOwner(29), "Project Title", "ptc activity 2", "Activity Created On", "19/08/2013", "Funding-2013-Actual Commitments", "333,222", "Totals-Actual Commitments", "333,222"),
                        new ReportAreaForTests(new AreaOwner(30), "Project Title", "SSC Project 1", "Activity Created On", "20/08/2013", "Funding-2013-Actual Commitments", "111,333", "Funding-2013-Actual Disbursements", "555,111", "Totals-Actual Commitments", "111,333", "Totals-Actual Disbursements", "555,111"),
                        new ReportAreaForTests(new AreaOwner(31), "Project Title", "SSC Project 2", "Activity Created On", "20/08/2013", "Funding-2013-Actual Commitments", "567,421", "Funding-2013-Actual Disbursements", "131,845", "Totals-Actual Commitments", "567,421", "Totals-Actual Disbursements", "131,845"),
                        new ReportAreaForTests(new AreaOwner(32), "Project Title", "crazy funding 1", "Activity Created On", "20/12/2013", "Funding-2013-Actual Commitments", "333,333", "Totals-Actual Commitments", "333,333"),
                        new ReportAreaForTests(new AreaOwner(33), "Project Title", "Activity with Zones", "Activity Created On", "23/12/2013", "Funding-2013-Actual Commitments", "570,000", "Totals-Actual Commitments", "570,000"),
                        new ReportAreaForTests(new AreaOwner(36), "Project Title", "Activity With Zones and Percentages", "Activity Created On", "23/12/2013", "Funding-2013-Actual Commitments", "890,000", "Totals-Actual Commitments", "890,000"),
                        new ReportAreaForTests(new AreaOwner(40), "Project Title", "SubNational no percentages", "Activity Created On", "21/02/2014", "Funding-2014-Actual Commitments", "75,000", "Totals-Actual Commitments", "75,000"),
                        new ReportAreaForTests(new AreaOwner(41), "Project Title", "Activity Linked With Pledge", "Activity Created On", "27/03/2014", "Funding-2014-Actual Commitments", "50,000", "Totals-Actual Commitments", "50,000"),
                        new ReportAreaForTests(new AreaOwner(43), "Project Title", "Activity with primary_tertiary_program", "Activity Created On", "28/03/2014", "Funding-2014-Actual Commitments", "50,000", "Totals-Actual Commitments", "50,000"),
                        new ReportAreaForTests(new AreaOwner(44), "Project Title", "activity with primary_program", "Activity Created On", "28/03/2014", "Funding-2014-Actual Commitments", "32,000", "Totals-Actual Commitments", "32,000"),
                        new ReportAreaForTests(new AreaOwner(45), "Project Title", "activity with tertiary_program", "Activity Created On", "28/03/2014", "Funding-2014-Actual Commitments", "15,000", "Totals-Actual Commitments", "15,000"),
                        new ReportAreaForTests(new AreaOwner(46), "Project Title", "pledged education activity 1", "Activity Created On", "29/04/2014", "Funding-2013-Actual Commitments", "1,700,000", "Funding-2014-Actual Commitments", "3,300,000", "Totals-Actual Commitments", "5,000,000"),
                        new ReportAreaForTests(new AreaOwner(48), "Project Title", "pledged 2", "Activity Created On", "29/04/2014", "Funding-2013-Actual Commitments", "2,670,000", "Funding-2014-Actual Commitments", "4,400,000", "Funding-2014-Actual Disbursements", "450,000", "Totals-Actual Commitments", "7,070,000", "Totals-Actual Disbursements", "450,000")));
        
        ReportSpecificationImpl spec = buildSpecForFiltering("flat filter by created_on max", 
            Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.ACTIVITY_CREATED_ON), null, 
            new ReportElement(new ReportColumn(ColumnConstants.ACTIVITY_CREATED_ON)), new FilterRule(null, "2456809", true, true, true)); // 2014/jan/1 - future
        
        runNiTestCase(spec, "en", acts, cor);
    }
    
    @Test
    public void testFilterByActivityCreatedOnMinimum() {
        NiReportModel cor = new NiReportModel("flat filter by created_on min")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 12))",
                        "(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Activity Created On: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 8));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 10, colSpan: 2))",
                        "(2006: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 2));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 2));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 6, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 8, colSpan: 2))",
                        "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1))"))
                    .withWarnings(Arrays.asList())
                    .withBody(      new ReportAreaForTests(null)
                      .withContents("Project Title", "", "Activity Created On", "", "Funding-2006-Actual Commitments", "96,840,58", "Funding-2006-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "4,370,000", "Funding-2013-Actual Disbursements", "35,000", "Funding-2014-Actual Commitments", "8,159,813,77", "Funding-2014-Actual Disbursements", "710,200", "Funding-2015-Actual Commitments", "1,971,831,84", "Funding-2015-Actual Disbursements", "437,335", "Totals-Actual Commitments", "14,598,486,19", "Totals-Actual Disbursements", "1,182,535")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(40), "Project Title", "SubNational no percentages", "Activity Created On", "21/02/2014", "Funding-2014-Actual Commitments", "75,000", "Totals-Actual Commitments", "75,000"),
                        new ReportAreaForTests(new AreaOwner(41), "Project Title", "Activity Linked With Pledge", "Activity Created On", "27/03/2014", "Funding-2014-Actual Commitments", "50,000", "Totals-Actual Commitments", "50,000"),
                        new ReportAreaForTests(new AreaOwner(43), "Project Title", "Activity with primary_tertiary_program", "Activity Created On", "28/03/2014", "Funding-2014-Actual Commitments", "50,000", "Totals-Actual Commitments", "50,000"),
                        new ReportAreaForTests(new AreaOwner(44), "Project Title", "activity with primary_program", "Activity Created On", "28/03/2014", "Funding-2014-Actual Commitments", "32,000", "Totals-Actual Commitments", "32,000"),
                        new ReportAreaForTests(new AreaOwner(45), "Project Title", "activity with tertiary_program", "Activity Created On", "28/03/2014", "Funding-2014-Actual Commitments", "15,000", "Totals-Actual Commitments", "15,000"),
                        new ReportAreaForTests(new AreaOwner(46), "Project Title", "pledged education activity 1", "Activity Created On", "29/04/2014", "Funding-2013-Actual Commitments", "1,700,000", "Funding-2014-Actual Commitments", "3,300,000", "Totals-Actual Commitments", "5,000,000"),
                        new ReportAreaForTests(new AreaOwner(48), "Project Title", "pledged 2", "Activity Created On", "29/04/2014", "Funding-2013-Actual Commitments", "2,670,000", "Funding-2014-Actual Commitments", "4,400,000", "Funding-2014-Actual Disbursements", "450,000", "Totals-Actual Commitments", "7,070,000", "Totals-Actual Disbursements", "450,000"),
                        new ReportAreaForTests(new AreaOwner(50), "Project Title", "activity with capital spending", "Activity Created On", "21/11/2014", "Funding-2014-Actual Commitments", "65,760,63", "Funding-2014-Actual Disbursements", "80,000", "Totals-Actual Commitments", "65,760,63", "Totals-Actual Disbursements", "80,000"),
                        new ReportAreaForTests(new AreaOwner(52), "Project Title", "activity with contracting agency", "Activity Created On", "26/11/2014", "Funding-2006-Actual Commitments", "96,840,58", "Funding-2014-Actual Disbursements", "50,000", "Totals-Actual Commitments", "96,840,58", "Totals-Actual Disbursements", "50,000"),
                        new ReportAreaForTests(new AreaOwner(53), "Project Title", "new activity with contracting", "Activity Created On", "26/11/2014", "Funding-2014-Actual Commitments", "12,000", "Totals-Actual Commitments", "12,000"),
                        new ReportAreaForTests(new AreaOwner(61), "Project Title", "activity-with-unfunded-components", "Activity Created On", "15/12/2014", "Funding-2014-Actual Commitments", "123,321", "Totals-Actual Commitments", "123,321"),
                        new ReportAreaForTests(new AreaOwner(63), "Project Title", "activity with funded components", "Activity Created On", "16/12/2014", "Funding-2014-Actual Commitments", "100", "Totals-Actual Commitments", "100"),
                        new ReportAreaForTests(new AreaOwner(64), "Project Title", "Unvalidated activity", "Activity Created On", "25/01/2015", "Funding-2015-Actual Commitments", "45,000", "Totals-Actual Commitments", "45,000"),
                        new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Activity Created On", "22/03/2015", "Funding-2015-Actual Commitments", "456,789", "Funding-2015-Actual Disbursements", "321,765", "Totals-Actual Commitments", "456,789", "Totals-Actual Disbursements", "321,765"),
                        new ReportAreaForTests(new AreaOwner(66), "Project Title", "Activity 2 with multiple agreements", "Activity Created On", "22/03/2015", "Funding-2015-Actual Commitments", "1,200", "Totals-Actual Commitments", "1,200"),
                        new ReportAreaForTests(new AreaOwner(67), "Project Title", "third activity with agreements", "Activity Created On", "22/03/2015", "Funding-2015-Actual Commitments", "123,456", "Totals-Actual Commitments", "123,456"),
                        new ReportAreaForTests(new AreaOwner(68), "Project Title", "activity with incomplete agreement", "Activity Created On", "22/03/2015", "Funding-2015-Actual Commitments", "123,000", "Totals-Actual Commitments", "123,000"),
                        new ReportAreaForTests(new AreaOwner(69), "Project Title", "Activity with planned disbursements", "Activity Created On", "10/04/2015", "Funding-2014-Actual Disbursements", "200", "Funding-2015-Actual Disbursements", "570", "Totals-Actual Disbursements", "770"),
                        new ReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms", "Activity Created On", "06/08/2015", "Funding-2015-Actual Commitments", "888,000", "Totals-Actual Commitments", "888,000"),
                        new ReportAreaForTests(new AreaOwner(71), "Project Title", "activity_with_disaster_response", "Activity Created On", "24/08/2015", "Funding-2014-Actual Commitments", "33,000", "Funding-2015-Actual Commitments", "117,000", "Totals-Actual Commitments", "150,000"),
                        new ReportAreaForTests(new AreaOwner(73), "Project Title", "activity with directed MTEFs", "Activity Created On", "29/09/2015", "Funding-2015-Actual Commitments", "123,456", "Totals-Actual Commitments", "123,456"),
                        new ReportAreaForTests(new AreaOwner(76), "Project Title", "activity with pipeline MTEFs and act. disb", "Activity Created On", "19/10/2015", "Funding-2013-Actual Disbursements", "35,000", "Funding-2014-Actual Disbursements", "75,000", "Totals-Actual Disbursements", "110,000"),
                        new ReportAreaForTests(new AreaOwner(77), "Project Title", "execution rate activity", "Activity Created On", "19/10/2015", "Funding-2014-Actual Disbursements", "55,000", "Funding-2015-Actual Disbursements", "35,000", "Totals-Actual Disbursements", "90,000"),
                        new ReportAreaForTests(new AreaOwner(78), "Project Title", "activity with many MTEFs", "Activity Created On", "05/11/2015", "Funding-2015-Actual Disbursements", "80,000", "Totals-Actual Disbursements", "80,000"),
                        new ReportAreaForTests(new AreaOwner(79), "Project Title", "with weird currencies", "Activity Created On", "15/12/2015", "Funding-2014-Actual Commitments", "3,632,14", "Funding-2015-Actual Commitments", "93,930,84", "Totals-Actual Commitments", "97,562,98")      ));
;
        
        ReportSpecificationImpl spec = buildSpecForFiltering("flat filter by created_on min", 
            Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.ACTIVITY_CREATED_ON), null, 
            new ReportElement(new ReportColumn(ColumnConstants.ACTIVITY_CREATED_ON)), new FilterRule("2456658", null, true, true, true)); // past - 2014/jun/1
        
        runNiTestCase(spec, "en", acts, cor);
    }
    
    @Test
    public void testFilterByActivityCreatedOnRangeNegated() {
        NiReportModel cor = new NiReportModel("flat filter by created_on range negated")
            .withHeaders(Arrays.asList(
                    "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 20))",
                    "(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Activity Created On: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 16));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 18, colSpan: 2))",
                    "(2006: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 2));(2009: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 2));(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 6, colSpan: 2));(2011: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 8, colSpan: 2));(2012: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 10, colSpan: 2));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 12, colSpan: 2));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 14, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 16, colSpan: 2))",
                    "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 14, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 15, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 16, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 17, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 18, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 19, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                  .withContents("Project Title", "", "Activity Created On", "", "Funding-2006-Actual Commitments", "96,840,58", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "100,000", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "780,311", "Funding-2011-Actual Commitments", "1,213,119", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "25,000", "Funding-2012-Actual Disbursements", "12,000", "Funding-2013-Actual Commitments", "3,472,086", "Funding-2013-Actual Disbursements", "1,266,956", "Funding-2014-Actual Commitments", "237,813,77", "Funding-2014-Actual Disbursements", "260,200", "Funding-2015-Actual Commitments", "1,971,831,84", "Funding-2015-Actual Disbursements", "437,335", "Totals-Actual Commitments", "7,116,691,19", "Totals-Actual Disbursements", "2,756,802")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(12), "Project Title", "TAC_activity_1", "Activity Created On", "23/08/2013", "Funding-2010-Actual Disbursements", "123,321", "Funding-2011-Actual Commitments", "213,231", "Totals-Actual Commitments", "213,231", "Totals-Actual Disbursements", "123,321"),
                    new ReportAreaForTests(new AreaOwner(13), "Project Title", "TAC_activity_2", "Activity Created On", "23/08/2013", "Funding-2010-Actual Disbursements", "453,213", "Funding-2011-Actual Commitments", "999,888", "Totals-Actual Commitments", "999,888", "Totals-Actual Disbursements", "453,213"),
                    new ReportAreaForTests(new AreaOwner(15), "Project Title", "Proposed Project Cost 1 - USD", "Activity Created On", "01/10/2013"),
                    new ReportAreaForTests(new AreaOwner(17), "Project Title", "Proposed Project Cost 2 - EUR", "Activity Created On", "01/10/2013"),
                    new ReportAreaForTests(new AreaOwner(18), "Project Title", "Test MTEF directed", "Activity Created On", "10/10/2013", "Funding-2010-Actual Disbursements", "143,777", "Totals-Actual Disbursements", "143,777"),
                    new ReportAreaForTests(new AreaOwner(19), "Project Title", "Pure MTEF Project", "Activity Created On", "11/10/2013"),
                    new ReportAreaForTests(new AreaOwner(21), "Project Title", "activity with components", "Activity Created On", "15/11/2013"),
                    new ReportAreaForTests(new AreaOwner(23), "Project Title", "Project with documents", "Activity Created On", "18/11/2013"),
                    new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Activity Created On", "01/08/2013", "Funding-2013-Actual Disbursements", "545,000", "Totals-Actual Disbursements", "545,000"),
                    new ReportAreaForTests(new AreaOwner(25), "Project Title", "mtef activity 1", "Activity Created On", "05/08/2013"),
                    new ReportAreaForTests(new AreaOwner(26), "Project Title", "date-filters-activity", "Activity Created On", "21/09/2013", "Funding-2009-Actual Commitments", "100,000", "Funding-2010-Actual Disbursements", "60,000", "Funding-2012-Actual Commitments", "25,000", "Funding-2012-Actual Disbursements", "12,000", "Totals-Actual Commitments", "125,000", "Totals-Actual Disbursements", "72,000"),
                    new ReportAreaForTests(new AreaOwner(27), "Project Title", "mtef activity 2", "Activity Created On", "05/08/2013"),
                    new ReportAreaForTests(new AreaOwner(28), "Project Title", "ptc activity 1", "Activity Created On", "19/08/2013", "Funding-2013-Actual Commitments", "666,777", "Totals-Actual Commitments", "666,777"),
                    new ReportAreaForTests(new AreaOwner(29), "Project Title", "ptc activity 2", "Activity Created On", "19/08/2013", "Funding-2013-Actual Commitments", "333,222", "Totals-Actual Commitments", "333,222"),
                    new ReportAreaForTests(new AreaOwner(30), "Project Title", "SSC Project 1", "Activity Created On", "20/08/2013", "Funding-2013-Actual Commitments", "111,333", "Funding-2013-Actual Disbursements", "555,111", "Totals-Actual Commitments", "111,333", "Totals-Actual Disbursements", "555,111"),
                    new ReportAreaForTests(new AreaOwner(31), "Project Title", "SSC Project 2", "Activity Created On", "20/08/2013", "Funding-2013-Actual Commitments", "567,421", "Funding-2013-Actual Disbursements", "131,845", "Totals-Actual Commitments", "567,421", "Totals-Actual Disbursements", "131,845"),
                    new ReportAreaForTests(new AreaOwner(32), "Project Title", "crazy funding 1", "Activity Created On", "20/12/2013", "Funding-2013-Actual Commitments", "333,333", "Totals-Actual Commitments", "333,333"),
                    new ReportAreaForTests(new AreaOwner(33), "Project Title", "Activity with Zones", "Activity Created On", "23/12/2013", "Funding-2013-Actual Commitments", "570,000", "Totals-Actual Commitments", "570,000"),
                    new ReportAreaForTests(new AreaOwner(36), "Project Title", "Activity With Zones and Percentages", "Activity Created On", "23/12/2013", "Funding-2013-Actual Commitments", "890,000", "Totals-Actual Commitments", "890,000"),
                    new ReportAreaForTests(new AreaOwner(50), "Project Title", "activity with capital spending", "Activity Created On", "21/11/2014", "Funding-2014-Actual Commitments", "65,760,63", "Funding-2014-Actual Disbursements", "80,000", "Totals-Actual Commitments", "65,760,63", "Totals-Actual Disbursements", "80,000"),
                    new ReportAreaForTests(new AreaOwner(52), "Project Title", "activity with contracting agency", "Activity Created On", "26/11/2014", "Funding-2006-Actual Commitments", "96,840,58", "Funding-2014-Actual Disbursements", "50,000", "Totals-Actual Commitments", "96,840,58", "Totals-Actual Disbursements", "50,000"),
                    new ReportAreaForTests(new AreaOwner(53), "Project Title", "new activity with contracting", "Activity Created On", "26/11/2014", "Funding-2014-Actual Commitments", "12,000", "Totals-Actual Commitments", "12,000"),
                    new ReportAreaForTests(new AreaOwner(61), "Project Title", "activity-with-unfunded-components", "Activity Created On", "15/12/2014", "Funding-2014-Actual Commitments", "123,321", "Totals-Actual Commitments", "123,321"),
                    new ReportAreaForTests(new AreaOwner(63), "Project Title", "activity with funded components", "Activity Created On", "16/12/2014", "Funding-2014-Actual Commitments", "100", "Totals-Actual Commitments", "100"),
                    new ReportAreaForTests(new AreaOwner(64), "Project Title", "Unvalidated activity", "Activity Created On", "25/01/2015", "Funding-2015-Actual Commitments", "45,000", "Totals-Actual Commitments", "45,000"),
                    new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Activity Created On", "22/03/2015", "Funding-2015-Actual Commitments", "456,789", "Funding-2015-Actual Disbursements", "321,765", "Totals-Actual Commitments", "456,789", "Totals-Actual Disbursements", "321,765"),
                    new ReportAreaForTests(new AreaOwner(66), "Project Title", "Activity 2 with multiple agreements", "Activity Created On", "22/03/2015", "Funding-2015-Actual Commitments", "1,200", "Totals-Actual Commitments", "1,200"),
                    new ReportAreaForTests(new AreaOwner(67), "Project Title", "third activity with agreements", "Activity Created On", "22/03/2015", "Funding-2015-Actual Commitments", "123,456", "Totals-Actual Commitments", "123,456"),
                    new ReportAreaForTests(new AreaOwner(68), "Project Title", "activity with incomplete agreement", "Activity Created On", "22/03/2015", "Funding-2015-Actual Commitments", "123,000", "Totals-Actual Commitments", "123,000"),
                    new ReportAreaForTests(new AreaOwner(69), "Project Title", "Activity with planned disbursements", "Activity Created On", "10/04/2015", "Funding-2014-Actual Disbursements", "200", "Funding-2015-Actual Disbursements", "570", "Totals-Actual Disbursements", "770"),
                    new ReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms", "Activity Created On", "06/08/2015", "Funding-2015-Actual Commitments", "888,000", "Totals-Actual Commitments", "888,000"),
                    new ReportAreaForTests(new AreaOwner(71), "Project Title", "activity_with_disaster_response", "Activity Created On", "24/08/2015", "Funding-2014-Actual Commitments", "33,000", "Funding-2015-Actual Commitments", "117,000", "Totals-Actual Commitments", "150,000"),
                    new ReportAreaForTests(new AreaOwner(73), "Project Title", "activity with directed MTEFs", "Activity Created On", "29/09/2015", "Funding-2015-Actual Commitments", "123,456", "Totals-Actual Commitments", "123,456"),
                    new ReportAreaForTests(new AreaOwner(76), "Project Title", "activity with pipeline MTEFs and act. disb", "Activity Created On", "19/10/2015", "Funding-2013-Actual Disbursements", "35,000", "Funding-2014-Actual Disbursements", "75,000", "Totals-Actual Disbursements", "110,000"),
                    new ReportAreaForTests(new AreaOwner(77), "Project Title", "execution rate activity", "Activity Created On", "19/10/2015", "Funding-2014-Actual Disbursements", "55,000", "Funding-2015-Actual Disbursements", "35,000", "Totals-Actual Disbursements", "90,000"),
                    new ReportAreaForTests(new AreaOwner(78), "Project Title", "activity with many MTEFs", "Activity Created On", "05/11/2015", "Funding-2015-Actual Disbursements", "80,000", "Totals-Actual Disbursements", "80,000"),
                    new ReportAreaForTests(new AreaOwner(79), "Project Title", "with weird currencies", "Activity Created On", "15/12/2015", "Funding-2014-Actual Commitments", "3,632,14", "Funding-2015-Actual Commitments", "93,930,84", "Totals-Actual Commitments", "97,562,98")));
            
        ReportSpecificationImpl spec = buildSpecForFiltering("flat filter by created_on range negated", 
            Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.ACTIVITY_CREATED_ON), null, 
            new ReportElement(new ReportColumn(ColumnConstants.ACTIVITY_CREATED_ON)), new FilterRule("2456658", "2456809", true, true, false)); // 2014/jan/1 - 2014/jun/1
        
        runNiTestCase(spec, "en", acts, cor);
    }
    
    @Test
    public void testFilterFlatByZone() {
        NiReportModel cor = new NiReportModel("flat filter by zone")
            .withHeaders(Arrays.asList(
                    "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 9))",
                    "(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 1, colSpan: 6));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 7, colSpan: 2))",
                    "(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 1, colSpan: 2));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 2))",
                    "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                  .withContents("Project Title", "", "Funding-2013-Actual Commitments", "1,282,000", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "37,500", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "456,789", "Funding-2015-Actual Disbursements", "321,765", "Totals-Actual Commitments", "1,776,289", "Totals-Actual Disbursements", "321,765")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(33), "Project Title", "Activity with Zones", "Funding-2013-Actual Commitments", "570,000", "Totals-Actual Commitments", "570,000"),
                    new ReportAreaForTests(new AreaOwner(36), "Project Title", "Activity With Zones and Percentages", "Funding-2013-Actual Commitments", "712,000", "Totals-Actual Commitments", "712,000"),
                    new ReportAreaForTests(new AreaOwner(40), "Project Title", "SubNational no percentages", "Funding-2014-Actual Commitments", "37,500", "Totals-Actual Commitments", "37,500"),
                    new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Funding-2015-Actual Commitments", "456,789", "Funding-2015-Actual Disbursements", "321,765", "Totals-Actual Commitments", "456,789", "Totals-Actual Disbursements", "321,765")));
        
        ReportSpecificationImpl spec = buildSpecForFiltering("flat filter by zone", 
            Arrays.asList(ColumnConstants.PROJECT_TITLE), null, ColumnConstants.ZONE, Arrays.asList(9108l, 9111l), true); // Bulboaca, Glodeni
            
        runNiTestCase(spec, "en", acts, cor);
    }
    
    @Test
    public void testFilterFlatByZoneNegative() {
        NiReportModel cor = new NiReportModel("flat filter by not zone")
            .withHeaders(Arrays.asList(
                    "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 20))",
                    "(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Zone: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 16));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 18, colSpan: 2))",
                    "(2006: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 2));(2009: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 2));(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 6, colSpan: 2));(2011: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 8, colSpan: 2));(2012: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 10, colSpan: 2));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 12, colSpan: 2));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 14, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 16, colSpan: 2))",
                    "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 14, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 15, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 16, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 17, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 18, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 19, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                  .withContents("Project Title", "", "Zone", "", "Funding-2006-Actual Commitments", "96,840,58", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "100,000", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "780,311", "Funding-2011-Actual Commitments", "1,213,119", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "25,000", "Funding-2012-Actual Disbursements", "12,000", "Funding-2013-Actual Commitments", "6,845,086", "Funding-2013-Actual Disbursements", "1,266,956", "Funding-2014-Actual Commitments", "8,110,313,77", "Funding-2014-Actual Disbursements", "710,200", "Funding-2015-Actual Commitments", "1,515,042,84", "Funding-2015-Actual Disbursements", "115,570", "Totals-Actual Commitments", "17,905,402,19", "Totals-Actual Disbursements", "2,885,037")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(12), "Project Title", "TAC_activity_1", "Zone", "", "Funding-2010-Actual Disbursements", "123,321", "Funding-2011-Actual Commitments", "213,231", "Totals-Actual Commitments", "213,231", "Totals-Actual Disbursements", "123,321"),
                    new ReportAreaForTests(new AreaOwner(13), "Project Title", "TAC_activity_2", "Zone", "", "Funding-2010-Actual Disbursements", "453,213", "Funding-2011-Actual Commitments", "999,888", "Totals-Actual Commitments", "999,888", "Totals-Actual Disbursements", "453,213"),
                    new ReportAreaForTests(new AreaOwner(18), "Project Title", "Test MTEF directed", "Zone", "", "Funding-2010-Actual Disbursements", "143,777", "Totals-Actual Disbursements", "143,777"),
                    new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Zone", "", "Funding-2013-Actual Disbursements", "545,000", "Totals-Actual Disbursements", "545,000"),
                    new ReportAreaForTests(new AreaOwner(26), "Project Title", "date-filters-activity", "Zone", "", "Funding-2009-Actual Commitments", "100,000", "Funding-2010-Actual Disbursements", "60,000", "Funding-2012-Actual Commitments", "25,000", "Funding-2012-Actual Disbursements", "12,000", "Totals-Actual Commitments", "125,000", "Totals-Actual Disbursements", "72,000"),
                    new ReportAreaForTests(new AreaOwner(28), "Project Title", "ptc activity 1", "Zone", "", "Funding-2013-Actual Commitments", "666,777", "Totals-Actual Commitments", "666,777"),
                    new ReportAreaForTests(new AreaOwner(29), "Project Title", "ptc activity 2", "Zone", "", "Funding-2013-Actual Commitments", "333,222", "Totals-Actual Commitments", "333,222"),
                    new ReportAreaForTests(new AreaOwner(30), "Project Title", "SSC Project 1", "Zone", "", "Funding-2013-Actual Commitments", "111,333", "Funding-2013-Actual Disbursements", "555,111", "Totals-Actual Commitments", "111,333", "Totals-Actual Disbursements", "555,111"),
                    new ReportAreaForTests(new AreaOwner(31), "Project Title", "SSC Project 2", "Zone", "", "Funding-2013-Actual Commitments", "567,421", "Funding-2013-Actual Disbursements", "131,845", "Totals-Actual Commitments", "567,421", "Totals-Actual Disbursements", "131,845"),
                    new ReportAreaForTests(new AreaOwner(32), "Project Title", "crazy funding 1", "Zone", "", "Funding-2013-Actual Commitments", "333,333", "Totals-Actual Commitments", "333,333"),
                    new ReportAreaForTests(new AreaOwner(33), "Project Title", "Activity with Zones", "Zone", "Bulboaca", "Funding-2013-Actual Commitments", "285,000", "Totals-Actual Commitments", "285,000"),
                    new ReportAreaForTests(new AreaOwner(36), "Project Title", "Activity With Zones and Percentages", "Zone", "Dolboaca", "Funding-2013-Actual Commitments", "178,000", "Totals-Actual Commitments", "178,000"),
                    new ReportAreaForTests(new AreaOwner(40), "Project Title", "SubNational no percentages", "Zone", "", "Funding-2014-Actual Commitments", "37,500", "Totals-Actual Commitments", "37,500"),
                    new ReportAreaForTests(new AreaOwner(41), "Project Title", "Activity Linked With Pledge", "Zone", "", "Funding-2014-Actual Commitments", "50,000", "Totals-Actual Commitments", "50,000"),
                    new ReportAreaForTests(new AreaOwner(43), "Project Title", "Activity with primary_tertiary_program", "Zone", "", "Funding-2014-Actual Commitments", "50,000", "Totals-Actual Commitments", "50,000"),
                    new ReportAreaForTests(new AreaOwner(44), "Project Title", "activity with primary_program", "Zone", "", "Funding-2014-Actual Commitments", "32,000", "Totals-Actual Commitments", "32,000"),
                    new ReportAreaForTests(new AreaOwner(45), "Project Title", "activity with tertiary_program", "Zone", "", "Funding-2014-Actual Commitments", "15,000", "Totals-Actual Commitments", "15,000"),
                    new ReportAreaForTests(new AreaOwner(46), "Project Title", "pledged education activity 1", "Zone", "", "Funding-2013-Actual Commitments", "1,700,000", "Funding-2014-Actual Commitments", "3,300,000", "Totals-Actual Commitments", "5,000,000"),
                    new ReportAreaForTests(new AreaOwner(48), "Project Title", "pledged 2", "Zone", "", "Funding-2013-Actual Commitments", "2,670,000", "Funding-2014-Actual Commitments", "4,400,000", "Funding-2014-Actual Disbursements", "450,000", "Totals-Actual Commitments", "7,070,000", "Totals-Actual Disbursements", "450,000"),
                    new ReportAreaForTests(new AreaOwner(50), "Project Title", "activity with capital spending", "Zone", "", "Funding-2014-Actual Commitments", "65,760,63", "Funding-2014-Actual Disbursements", "80,000", "Totals-Actual Commitments", "65,760,63", "Totals-Actual Disbursements", "80,000"),
                    new ReportAreaForTests(new AreaOwner(52), "Project Title", "activity with contracting agency", "Zone", "Apareni, Slobozia", "Funding-2006-Actual Commitments", "96,840,58", "Funding-2014-Actual Disbursements", "50,000", "Totals-Actual Commitments", "96,840,58", "Totals-Actual Disbursements", "50,000"),
                    new ReportAreaForTests(new AreaOwner(61), "Project Title", "activity-with-unfunded-components", "Zone", "Tiraspol", "Funding-2014-Actual Commitments", "123,321", "Totals-Actual Commitments", "123,321"),
                    new ReportAreaForTests(new AreaOwner(63), "Project Title", "activity with funded components", "Zone", "", "Funding-2014-Actual Commitments", "100", "Totals-Actual Commitments", "100"),
                    new ReportAreaForTests(new AreaOwner(64), "Project Title", "Unvalidated activity", "Zone", "", "Funding-2015-Actual Commitments", "45,000", "Totals-Actual Commitments", "45,000"),
                    new ReportAreaForTests(new AreaOwner(66), "Project Title", "Activity 2 with multiple agreements", "Zone", "", "Funding-2015-Actual Commitments", "1,200", "Totals-Actual Commitments", "1,200"),
                    new ReportAreaForTests(new AreaOwner(67), "Project Title", "third activity with agreements", "Zone", "", "Funding-2015-Actual Commitments", "123,456", "Totals-Actual Commitments", "123,456"),
                    new ReportAreaForTests(new AreaOwner(68), "Project Title", "activity with incomplete agreement", "Zone", "", "Funding-2015-Actual Commitments", "123,000", "Totals-Actual Commitments", "123,000"),
                    new ReportAreaForTests(new AreaOwner(69), "Project Title", "Activity with planned disbursements", "Zone", "", "Funding-2014-Actual Disbursements", "200", "Funding-2015-Actual Disbursements", "570", "Totals-Actual Disbursements", "770"),
                    new ReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms", "Zone", "", "Funding-2015-Actual Commitments", "888,000", "Totals-Actual Commitments", "888,000"),
                    new ReportAreaForTests(new AreaOwner(71), "Project Title", "activity_with_disaster_response", "Zone", "", "Funding-2014-Actual Commitments", "33,000", "Funding-2015-Actual Commitments", "117,000", "Totals-Actual Commitments", "150,000"),
                    new ReportAreaForTests(new AreaOwner(73), "Project Title", "activity with directed MTEFs", "Zone", "", "Funding-2015-Actual Commitments", "123,456", "Totals-Actual Commitments", "123,456"),
                    new ReportAreaForTests(new AreaOwner(76), "Project Title", "activity with pipeline MTEFs and act. disb", "Zone", "", "Funding-2013-Actual Disbursements", "35,000", "Funding-2014-Actual Disbursements", "75,000", "Totals-Actual Disbursements", "110,000"),
                    new ReportAreaForTests(new AreaOwner(77), "Project Title", "execution rate activity", "Zone", "", "Funding-2014-Actual Disbursements", "55,000", "Funding-2015-Actual Disbursements", "35,000", "Totals-Actual Disbursements", "90,000"),
                    new ReportAreaForTests(new AreaOwner(78), "Project Title", "activity with many MTEFs", "Zone", "", "Funding-2015-Actual Disbursements", "80,000", "Totals-Actual Disbursements", "80,000"),
                    new ReportAreaForTests(new AreaOwner(79), "Project Title", "with weird currencies", "Zone", "", "Funding-2014-Actual Commitments", "3,632,14", "Funding-2015-Actual Commitments", "93,930,84", "Totals-Actual Commitments", "97,562,98")));

        
        ReportSpecificationImpl spec = buildSpecForFiltering("flat filter by not zone", 
            Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.ZONE), null, ColumnConstants.ZONE, Arrays.asList(9111l), false); // NOT Glodeni
            
        runNiTestCase(spec, "en", acts, cor);
    }

    @Test
    public void testFilterHierByZoneByZone() {
        NiReportModel cor = new NiReportModel("by zone filter by zone")
        .withHeaders(Arrays.asList(
                "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 10))",
                "(Zone: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 6));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 8, colSpan: 2))",
                "(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 2));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 6, colSpan: 2))",
                "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1))"))
            .withWarnings(Arrays.asList())
            .withBody(      new ReportAreaForTests(null)
              .withContents("Zone", "", "Project Title", "", "Funding-2013-Actual Commitments", "1,282,000", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "37,500", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "456,789", "Funding-2015-Actual Disbursements", "321,765", "Totals-Actual Commitments", "1,776,289", "Totals-Actual Disbursements", "321,765")
              .withChildren(
                new ReportAreaForTests(new AreaOwner("Zone", "Bulboaca")).withContents("Project Title", "", "Funding-2013-Actual Commitments", "285,000", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "285,000", "Totals-Actual Disbursements", "0", "Zone", "Bulboaca")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner(33), "Project Title", "Activity with Zones", "Funding-2013-Actual Commitments", "285,000", "Totals-Actual Commitments", "285,000")        ),
                new ReportAreaForTests(new AreaOwner("Zone", "Glodeni"))
                .withContents("Project Title", "", "Funding-2013-Actual Commitments", "997,000", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "37,500", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "456,789", "Funding-2015-Actual Disbursements", "321,765", "Totals-Actual Commitments", "1,491,289", "Totals-Actual Disbursements", "321,765", "Zone", "Glodeni")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner(33), "Project Title", "Activity with Zones", "Funding-2013-Actual Commitments", "285,000", "Totals-Actual Commitments", "285,000"),
                  new ReportAreaForTests(new AreaOwner(36), "Project Title", "Activity With Zones and Percentages", "Funding-2013-Actual Commitments", "712,000", "Totals-Actual Commitments", "712,000"),
                  new ReportAreaForTests(new AreaOwner(40), "Project Title", "SubNational no percentages", "Funding-2014-Actual Commitments", "37,500", "Totals-Actual Commitments", "37,500"),
                  new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Funding-2015-Actual Commitments", "456,789", "Funding-2015-Actual Disbursements", "321,765", "Totals-Actual Commitments", "456,789", "Totals-Actual Disbursements", "321,765")        )      ));
        
        ReportSpecificationImpl spec = buildSpecForFiltering("by zone filter by zone", 
            Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.ZONE), 
            Arrays.asList(ColumnConstants.ZONE), 
            ColumnConstants.ZONE, Arrays.asList(9108l, 9111l), true); // Bulboaca, Glodeni
            
        runNiTestCase(spec, "en", acts, cor);
    }

    @Test
    public void testFilterHierByRegionByZone() {
        NiReportModel cor = new NiReportModel("by region filter by zone")
            .withHeaders(Arrays.asList(
                    "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 10))",
                    "(Region: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 6));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 8, colSpan: 2))",
                    "(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 2));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 6, colSpan: 2))",
                    "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                  .withContents("Region", "", "Project Title", "", "Funding-2013-Actual Commitments", "1,282,000", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "37,500", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "456,789", "Funding-2015-Actual Disbursements", "321,765", "Totals-Actual Commitments", "1,776,289", "Totals-Actual Disbursements", "321,765")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner("Region", "Anenii Noi County")).withContents("Project Title", "", "Funding-2013-Actual Commitments", "285,000", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "285,000", "Totals-Actual Disbursements", "0", "Region", "Anenii Noi County")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner(33), "Project Title", "Activity with Zones", "Funding-2013-Actual Commitments", "285,000", "Totals-Actual Commitments", "285,000")        ),
                    new ReportAreaForTests(new AreaOwner("Region", "Balti County"))
                    .withContents("Project Title", "", "Funding-2013-Actual Commitments", "997,000", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "37,500", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "456,789", "Funding-2015-Actual Disbursements", "321,765", "Totals-Actual Commitments", "1,491,289", "Totals-Actual Disbursements", "321,765", "Region", "Balti County")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner(33), "Project Title", "Activity with Zones", "Funding-2013-Actual Commitments", "285,000", "Totals-Actual Commitments", "285,000"),
                      new ReportAreaForTests(new AreaOwner(36), "Project Title", "Activity With Zones and Percentages", "Funding-2013-Actual Commitments", "712,000", "Totals-Actual Commitments", "712,000"),
                      new ReportAreaForTests(new AreaOwner(40), "Project Title", "SubNational no percentages", "Funding-2014-Actual Commitments", "37,500", "Totals-Actual Commitments", "37,500"),
                      new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Funding-2015-Actual Commitments", "456,789", "Funding-2015-Actual Disbursements", "321,765", "Totals-Actual Commitments", "456,789", "Totals-Actual Disbursements", "321,765")        )      ));

        
        ReportSpecificationImpl spec = buildSpecForFiltering("by region filter by zone", 
            Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.REGION), 
            Arrays.asList(ColumnConstants.REGION), 
            ColumnConstants.ZONE, Arrays.asList(9108l, 9111l), true); // Bulboaca, Glodeni
            
        runNiTestCase(spec, "en", acts, cor);
    }
    
    @Test
    public void testFilterHierBySectorByZone() {
        NiReportModel cor = new NiReportModel("by primary sector filter by zone")
        .withHeaders(Arrays.asList(
                "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 10))",
                "(Primary Sector: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 6));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 8, colSpan: 2))",
                "(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 2));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 6, colSpan: 2))",
                "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1))"))
            .withWarnings(Arrays.asList())
            .withBody(      new ReportAreaForTests(null)
              .withContents("Primary Sector", "", "Project Title", "", "Funding-2013-Actual Commitments", "1,282,000", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "37,500", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "456,789", "Funding-2015-Actual Disbursements", "321,765", "Totals-Actual Commitments", "1,776,289", "Totals-Actual Disbursements", "321,765")
              .withChildren(
                new ReportAreaForTests(new AreaOwner("Primary Sector", "110 - EDUCATION"))
                .withContents("Project Title", "", "Funding-2013-Actual Commitments", "783,600", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "37,500", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "228,394,5", "Funding-2015-Actual Disbursements", "160,882,5", "Totals-Actual Commitments", "1,049,494,5", "Totals-Actual Disbursements", "160,882,5", "Primary Sector", "110 - EDUCATION")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner(33), "Project Title", "Activity with Zones", "Funding-2013-Actual Commitments", "570,000", "Totals-Actual Commitments", "570,000"),
                  new ReportAreaForTests(new AreaOwner(36), "Project Title", "Activity With Zones and Percentages", "Funding-2013-Actual Commitments", "213,600", "Totals-Actual Commitments", "213,600"),
                  new ReportAreaForTests(new AreaOwner(40), "Project Title", "SubNational no percentages", "Funding-2014-Actual Commitments", "37,500", "Totals-Actual Commitments", "37,500"),
                  new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Funding-2015-Actual Commitments", "228,394,5", "Funding-2015-Actual Disbursements", "160,882,5", "Totals-Actual Commitments", "228,394,5", "Totals-Actual Disbursements", "160,882,5")        ),
                new ReportAreaForTests(new AreaOwner("Primary Sector", "112 - BASIC EDUCATION")).withContents("Project Title", "", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "228,394,5", "Funding-2015-Actual Disbursements", "160,882,5", "Totals-Actual Commitments", "228,394,5", "Totals-Actual Disbursements", "160,882,5", "Primary Sector", "112 - BASIC EDUCATION")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Funding-2015-Actual Commitments", "228,394,5", "Funding-2015-Actual Disbursements", "160,882,5", "Totals-Actual Commitments", "228,394,5", "Totals-Actual Disbursements", "160,882,5")        ),
                new ReportAreaForTests(new AreaOwner("Primary Sector", "120 - HEALTH")).withContents("Project Title", "", "Funding-2013-Actual Commitments", "498,400", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "498,400", "Totals-Actual Disbursements", "0", "Primary Sector", "120 - HEALTH")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner(36), "Project Title", "Activity With Zones and Percentages", "Funding-2013-Actual Commitments", "498,400", "Totals-Actual Commitments", "498,400"))));

        
        ReportSpecificationImpl spec = buildSpecForFiltering("by primary sector filter by zone", 
            Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.PRIMARY_SECTOR), 
            Arrays.asList(ColumnConstants.PRIMARY_SECTOR), 
            ColumnConstants.ZONE, Arrays.asList(9108l, 9111l), true); // Bulboaca, Glodeni
            
        runNiTestCase(spec, "en", acts, cor);
    }
    
    @Test
    public void testFilterHierBySectorByRegionBySectorByRegion() {
        NiReportModel cor = new NiReportModel("by primary sector by region filter by zone by primary sector")
            .withHeaders(Arrays.asList(
                    "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 13))",
                    "(Primary Sector: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Region: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 3, colSpan: 8));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 11, colSpan: 2))",
                    "(2006: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 2));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 2));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 7, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 9, colSpan: 2))",
                    "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null).withContents("Primary Sector", "", "Region", "", "Project Title", "", "Funding-2006-Actual Commitments", "31,957,39", "Funding-2006-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "831,933", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "37,500", "Funding-2014-Actual Disbursements", "16,500", "Funding-2015-Actual Commitments", "388,234,5", "Funding-2015-Actual Disbursements", "160,882,5", "Totals-Actual Commitments", "1,289,624,89", "Totals-Actual Disbursements", "177,382,5")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner("Primary Sector", "110 - EDUCATION")).withContents("Region", "", "Project Title", "", "Funding-2006-Actual Commitments", "31,957,39", "Funding-2006-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "831,933", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "37,500", "Funding-2014-Actual Disbursements", "16,500", "Funding-2015-Actual Commitments", "388,234,5", "Funding-2015-Actual Disbursements", "160,882,5", "Totals-Actual Commitments", "1,289,624,89", "Totals-Actual Disbursements", "177,382,5", "Primary Sector", "110 - EDUCATION")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner("Region", "Balti County"))
                      .withContents("Project Title", "", "Funding-2006-Actual Commitments", "31,957,39", "Funding-2006-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "831,933", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "37,500", "Funding-2014-Actual Disbursements", "16,500", "Funding-2015-Actual Commitments", "388,234,5", "Funding-2015-Actual Disbursements", "160,882,5", "Totals-Actual Commitments", "1,289,624,89", "Totals-Actual Disbursements", "177,382,5", "Region", "Balti County")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(32), "Project Title", "crazy funding 1", "Funding-2013-Actual Commitments", "333,333", "Totals-Actual Commitments", "333,333"),
                        new ReportAreaForTests(new AreaOwner(33), "Project Title", "Activity with Zones", "Funding-2013-Actual Commitments", "285,000", "Totals-Actual Commitments", "285,000"),
                        new ReportAreaForTests(new AreaOwner(36), "Project Title", "Activity With Zones and Percentages", "Funding-2013-Actual Commitments", "213,600", "Totals-Actual Commitments", "213,600"),
                        new ReportAreaForTests(new AreaOwner(40), "Project Title", "SubNational no percentages", "Funding-2014-Actual Commitments", "37,500", "Totals-Actual Commitments", "37,500"),
                        new ReportAreaForTests(new AreaOwner(52), "Project Title", "activity with contracting agency", "Funding-2006-Actual Commitments", "31,957,39", "Funding-2014-Actual Disbursements", "16,500", "Totals-Actual Commitments", "31,957,39", "Totals-Actual Disbursements", "16,500"),
                        new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Funding-2015-Actual Commitments", "228,394,5", "Funding-2015-Actual Disbursements", "160,882,5", "Totals-Actual Commitments", "228,394,5", "Totals-Actual Disbursements", "160,882,5"),
                        new ReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms", "Funding-2015-Actual Commitments", "159,840", "Totals-Actual Commitments", "159,840")          )        )      ));

        
        ReportSpecificationImpl spec = buildSpecForFiltering("by primary sector by region filter by zone by primary sector", 
            Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.PRIMARY_SECTOR, ColumnConstants.REGION), 
            Arrays.asList(ColumnConstants.PRIMARY_SECTOR, ColumnConstants.REGION), 
            ColumnConstants.REGION, Arrays.asList(9086l), true); // Balti
        ((ReportFiltersImpl) spec.getFilters()).addFilterRule(new ReportElement(new ReportColumn(ColumnConstants.PRIMARY_SECTOR)), new FilterRule("6236", true)); // 110 - Education

        runNiTestCase(spec, "en", acts, cor);
    }
    
    @Test
    public void testFilterFlatBySectorByRegion() {
        NiReportModel cor = new NiReportModel("flat filter by zone by primary sector")
            .withHeaders(Arrays.asList(
                    "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 11))",
                    "(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 1, colSpan: 8));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 9, colSpan: 2))",
                    "(2006: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 1, colSpan: 2));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 2));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 7, colSpan: 2))",
                    "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                  .withContents("Project Title", "", "Funding-2006-Actual Commitments", "31,957,39", "Funding-2006-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "831,933", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "37,500", "Funding-2014-Actual Disbursements", "16,500", "Funding-2015-Actual Commitments", "388,234,5", "Funding-2015-Actual Disbursements", "160,882,5", "Totals-Actual Commitments", "1,289,624,89", "Totals-Actual Disbursements", "177,382,5")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(32), "Project Title", "crazy funding 1", "Funding-2013-Actual Commitments", "333,333", "Totals-Actual Commitments", "333,333"),
                    new ReportAreaForTests(new AreaOwner(33), "Project Title", "Activity with Zones", "Funding-2013-Actual Commitments", "285,000", "Totals-Actual Commitments", "285,000"),
                    new ReportAreaForTests(new AreaOwner(36), "Project Title", "Activity With Zones and Percentages", "Funding-2013-Actual Commitments", "213,600", "Totals-Actual Commitments", "213,600"),
                    new ReportAreaForTests(new AreaOwner(40), "Project Title", "SubNational no percentages", "Funding-2014-Actual Commitments", "37,500", "Totals-Actual Commitments", "37,500"),
                    new ReportAreaForTests(new AreaOwner(52), "Project Title", "activity with contracting agency", "Funding-2006-Actual Commitments", "31,957,39", "Funding-2014-Actual Disbursements", "16,500", "Totals-Actual Commitments", "31,957,39", "Totals-Actual Disbursements", "16,500"),
                    new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Funding-2015-Actual Commitments", "228,394,5", "Funding-2015-Actual Disbursements", "160,882,5", "Totals-Actual Commitments", "228,394,5", "Totals-Actual Disbursements", "160,882,5"),
                    new ReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms", "Funding-2015-Actual Commitments", "159,840", "Totals-Actual Commitments", "159,840")      ));

        ReportSpecificationImpl spec = buildSpecForFiltering("flat filter by zone by primary sector", 
            Arrays.asList(ColumnConstants.PROJECT_TITLE), 
            null, 
            ColumnConstants.REGION, Arrays.asList(9086l), true); // Balti
        ((ReportFiltersImpl) spec.getFilters()).addFilterRule(new ReportElement(new ReportColumn(ColumnConstants.PRIMARY_SECTOR)), new FilterRule("6236", true)); // 110 - Education

        runNiTestCase(spec, "en", acts, cor);
    }

    @Test
    public void testFilterFlatBySectorsByRegion() {
        NiReportModel cor = new NiReportModel("flat filter by zone by primary sectors")
            .withHeaders(Arrays.asList(
                    "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 11))",
                    "(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 1, colSpan: 8));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 9, colSpan: 2))",
                    "(2006: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 1, colSpan: 2));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 2));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 7, colSpan: 2))",
                    "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                  .withContents("Project Title", "", "Funding-2006-Actual Commitments", "37,283,62", "Funding-2006-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "831,933", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "37,500", "Funding-2014-Actual Disbursements", "19,250", "Funding-2015-Actual Commitments", "723,189", "Funding-2015-Actual Disbursements", "321,765", "Totals-Actual Commitments", "1,629,905,62", "Totals-Actual Disbursements", "341,015")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(32), "Project Title", "crazy funding 1", "Funding-2013-Actual Commitments", "333,333", "Totals-Actual Commitments", "333,333"),
                    new ReportAreaForTests(new AreaOwner(33), "Project Title", "Activity with Zones", "Funding-2013-Actual Commitments", "285,000", "Totals-Actual Commitments", "285,000"),
                    new ReportAreaForTests(new AreaOwner(36), "Project Title", "Activity With Zones and Percentages", "Funding-2013-Actual Commitments", "213,600", "Totals-Actual Commitments", "213,600"),
                    new ReportAreaForTests(new AreaOwner(40), "Project Title", "SubNational no percentages", "Funding-2014-Actual Commitments", "37,500", "Totals-Actual Commitments", "37,500"),
                    new ReportAreaForTests(new AreaOwner(52), "Project Title", "activity with contracting agency", "Funding-2006-Actual Commitments", "37,283,62", "Funding-2014-Actual Disbursements", "19,250", "Totals-Actual Commitments", "37,283,62", "Totals-Actual Disbursements", "19,250"),
                    new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Funding-2015-Actual Commitments", "456,789", "Funding-2015-Actual Disbursements", "321,765", "Totals-Actual Commitments", "456,789", "Totals-Actual Disbursements", "321,765"),
                    new ReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms", "Funding-2015-Actual Commitments", "266,400", "Totals-Actual Commitments", "266,400")      ));

        
        ReportSpecificationImpl spec = buildSpecForFiltering("flat filter by zone by primary sectors", 
            Arrays.asList(ColumnConstants.PROJECT_TITLE), 
            null, 
            ColumnConstants.REGION, Arrays.asList(9086l), true); // Balti
        ((ReportFiltersImpl) spec.getFilters()).addFilterRule(new ReportElement(new ReportColumn(ColumnConstants.PRIMARY_SECTOR)), new FilterRule(Arrays.asList("6236", "6242"), true)); // 110 - Education, 112 - Basic Education

        runNiTestCase(spec, "en", acts, cor);
    }
    
    @Test
    public void testFilterFlatByDonor() {
        NiReportModel cor = new NiReportModel("flat filter by donor")
            .withHeaders(Arrays.asList(
                    "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 13))",
                    "(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 1, colSpan: 10));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 11, colSpan: 2))",
                    "(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 1, colSpan: 2));(2011: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 2));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 2));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 7, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 9, colSpan: 2))",
                    "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                  .withContents("Project Title", "", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "453,213", "Funding-2011-Actual Commitments", "999,888", "Funding-2011-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "5,270,643", "Funding-2013-Actual Disbursements", "546,845", "Funding-2014-Actual Commitments", "7,700,000", "Funding-2014-Actual Disbursements", "450,000", "Funding-2015-Actual Commitments", "500", "Funding-2015-Actual Disbursements", "80,070", "Totals-Actual Commitments", "13,971,031", "Totals-Actual Disbursements", "1,530,128")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(13), "Project Title", "TAC_activity_2", "Funding-2010-Actual Disbursements", "453,213", "Funding-2011-Actual Commitments", "999,888", "Totals-Actual Commitments", "999,888", "Totals-Actual Disbursements", "453,213"),
                    new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Funding-2013-Actual Disbursements", "415,000", "Totals-Actual Disbursements", "415,000"),
                    new ReportAreaForTests(new AreaOwner(29), "Project Title", "ptc activity 2", "Funding-2013-Actual Commitments", "333,222", "Totals-Actual Commitments", "333,222"),
                    new ReportAreaForTests(new AreaOwner(31), "Project Title", "SSC Project 2", "Funding-2013-Actual Commitments", "567,421", "Funding-2013-Actual Disbursements", "131,845", "Totals-Actual Commitments", "567,421", "Totals-Actual Disbursements", "131,845"),
                    new ReportAreaForTests(new AreaOwner(46), "Project Title", "pledged education activity 1", "Funding-2013-Actual Commitments", "1,700,000", "Funding-2014-Actual Commitments", "3,300,000", "Totals-Actual Commitments", "5,000,000"),
                    new ReportAreaForTests(new AreaOwner(48), "Project Title", "pledged 2", "Funding-2013-Actual Commitments", "2,670,000", "Funding-2014-Actual Commitments", "4,400,000", "Funding-2014-Actual Disbursements", "450,000", "Totals-Actual Commitments", "7,070,000", "Totals-Actual Disbursements", "450,000"),
                    new ReportAreaForTests(new AreaOwner(66), "Project Title", "Activity 2 with multiple agreements", "Funding-2015-Actual Commitments", "500", "Totals-Actual Commitments", "500"),
                    new ReportAreaForTests(new AreaOwner(69), "Project Title", "Activity with planned disbursements", "Funding-2015-Actual Disbursements", "70", "Totals-Actual Disbursements", "70"),
                    new ReportAreaForTests(new AreaOwner(78), "Project Title", "activity with many MTEFs", "Funding-2015-Actual Disbursements", "80,000", "Totals-Actual Disbursements", "80,000")      ));

        ReportSpecificationImpl spec = buildSpecForFiltering("flat filter by donor", 
            Arrays.asList(ColumnConstants.PROJECT_TITLE), 
            null, 
            ColumnConstants.DONOR_AGENCY, Arrays.asList(21696l, 21702l, 21701l), true); //"USAID", "Water Foundation", "Water Org"

        runNiTestCase(spec, "en", acts, cor);
    }
    
    @Test
    public void testFilterFlatByDonorGroup() {
        NiReportModel cor = new NiReportModel("flat filter by donor group")
            .withHeaders(Arrays.asList(
                    "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 13))",
                    "(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 1, colSpan: 10));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 11, colSpan: 2))",
                    "(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 1, colSpan: 2));(2011: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 2));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 2));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 7, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 9, colSpan: 2))",
                    "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                  .withContents("Project Title", "", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "453,213", "Funding-2011-Actual Commitments", "999,888", "Funding-2011-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "5,270,643", "Funding-2013-Actual Disbursements", "546,845", "Funding-2014-Actual Commitments", "7,700,000", "Funding-2014-Actual Disbursements", "450,000", "Funding-2015-Actual Commitments", "500", "Funding-2015-Actual Disbursements", "80,070", "Totals-Actual Commitments", "13,971,031", "Totals-Actual Disbursements", "1,530,128")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(13), "Project Title", "TAC_activity_2", "Funding-2010-Actual Disbursements", "453,213", "Funding-2011-Actual Commitments", "999,888", "Totals-Actual Commitments", "999,888", "Totals-Actual Disbursements", "453,213"),
                    new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Funding-2013-Actual Disbursements", "415,000", "Totals-Actual Disbursements", "415,000"),
                    new ReportAreaForTests(new AreaOwner(29), "Project Title", "ptc activity 2", "Funding-2013-Actual Commitments", "333,222", "Totals-Actual Commitments", "333,222"),
                    new ReportAreaForTests(new AreaOwner(31), "Project Title", "SSC Project 2", "Funding-2013-Actual Commitments", "567,421", "Funding-2013-Actual Disbursements", "131,845", "Totals-Actual Commitments", "567,421", "Totals-Actual Disbursements", "131,845"),
                    new ReportAreaForTests(new AreaOwner(46), "Project Title", "pledged education activity 1", "Funding-2013-Actual Commitments", "1,700,000", "Funding-2014-Actual Commitments", "3,300,000", "Totals-Actual Commitments", "5,000,000"),
                    new ReportAreaForTests(new AreaOwner(48), "Project Title", "pledged 2", "Funding-2013-Actual Commitments", "2,670,000", "Funding-2014-Actual Commitments", "4,400,000", "Funding-2014-Actual Disbursements", "450,000", "Totals-Actual Commitments", "7,070,000", "Totals-Actual Disbursements", "450,000"),
                    new ReportAreaForTests(new AreaOwner(66), "Project Title", "Activity 2 with multiple agreements", "Funding-2015-Actual Commitments", "500", "Totals-Actual Commitments", "500"),
                    new ReportAreaForTests(new AreaOwner(69), "Project Title", "Activity with planned disbursements", "Funding-2015-Actual Disbursements", "70", "Totals-Actual Disbursements", "70"),
                    new ReportAreaForTests(new AreaOwner(78), "Project Title", "activity with many MTEFs", "Funding-2015-Actual Disbursements", "80,000", "Totals-Actual Disbursements", "80,000")      ));

        ReportSpecificationImpl spec = buildSpecForFiltering("flat filter by donor group", 
            Arrays.asList(ColumnConstants.PROJECT_TITLE), 
            null, 
            ColumnConstants.DONOR_GROUP, Arrays.asList(19l), true); // "American"

        runNiTestCase(spec, "en", acts, cor);
    }
    
    @Test
    public void testFilterFlatByNotDonorGroup() {
        NiReportModel cor = new NiReportModel("flat filter by not donor group")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 20))",
                        "(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Donor Group: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 16));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 18, colSpan: 2))",
                        "(2006: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 2));(2009: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 2));(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 6, colSpan: 2));(2011: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 8, colSpan: 2));(2012: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 10, colSpan: 2));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 12, colSpan: 2));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 14, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 16, colSpan: 2))",
                        "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 14, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 15, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 16, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 17, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 18, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 19, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                        .withContents("Project Title", "", "Donor Group", "", "Funding-2006-Actual Commitments", "96,840,58", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "100,000", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "327,098", "Funding-2011-Actual Commitments", "213,231", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "25,000", "Funding-2012-Actual Disbursements", "12,000", "Funding-2013-Actual Commitments", "2,571,443", "Funding-2013-Actual Disbursements", "720,111", "Funding-2014-Actual Commitments", "459,813,77", "Funding-2014-Actual Disbursements", "260,200", "Funding-2015-Actual Commitments", "1,971,331,84", "Funding-2015-Actual Disbursements", "357,265", "Totals-Actual Commitments", "5,437,660,19", "Totals-Actual Disbursements", "1,676,674")
                        .withChildren(
                                new ReportAreaForTests(new AreaOwner(12), "Project Title", "TAC_activity_1", "Donor Group", "International", "Funding-2010-Actual Disbursements", "123,321", "Funding-2011-Actual Commitments", "213,231", "Totals-Actual Commitments", "213,231", "Totals-Actual Disbursements", "123,321"),
                                new ReportAreaForTests(new AreaOwner(18), "Project Title", "Test MTEF directed", "Donor Group", "National", "Funding-2010-Actual Disbursements", "143,777", "Totals-Actual Disbursements", "143,777"),
                                new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Donor Group", "Default Group, European", "Funding-2013-Actual Disbursements", "130,000", "Totals-Actual Disbursements", "130,000"),
                                new ReportAreaForTests(new AreaOwner(26), "Project Title", "date-filters-activity", "Donor Group", "National", "Funding-2009-Actual Commitments", "100,000", "Funding-2010-Actual Disbursements", "60,000", "Funding-2012-Actual Commitments", "25,000", "Funding-2012-Actual Disbursements", "12,000", "Totals-Actual Commitments", "125,000", "Totals-Actual Disbursements", "72,000"),
                                new ReportAreaForTests(new AreaOwner(28), "Project Title", "ptc activity 1", "Donor Group", "Default Group", "Funding-2013-Actual Commitments", "666,777", "Totals-Actual Commitments", "666,777"),
                                new ReportAreaForTests(new AreaOwner(30), "Project Title", "SSC Project 1", "Donor Group", "Default Group", "Funding-2013-Actual Commitments", "111,333", "Funding-2013-Actual Disbursements", "555,111", "Totals-Actual Commitments", "111,333", "Totals-Actual Disbursements", "555,111"),
                                new ReportAreaForTests(new AreaOwner(32), "Project Title", "crazy funding 1", "Donor Group", "Default Group", "Funding-2013-Actual Commitments", "333,333", "Totals-Actual Commitments", "333,333"),
                                new ReportAreaForTests(new AreaOwner(33), "Project Title", "Activity with Zones", "Donor Group", "Default Group", "Funding-2013-Actual Commitments", "570,000", "Totals-Actual Commitments", "570,000"),
                                new ReportAreaForTests(new AreaOwner(36), "Project Title", "Activity With Zones and Percentages", "Donor Group", "European", "Funding-2013-Actual Commitments", "890,000", "Totals-Actual Commitments", "890,000"),
                                new ReportAreaForTests(new AreaOwner(40), "Project Title", "SubNational no percentages", "Donor Group", "National", "Funding-2014-Actual Commitments", "75,000", "Totals-Actual Commitments", "75,000"),
                                new ReportAreaForTests(new AreaOwner(41), "Project Title", "Activity Linked With Pledge", "Donor Group", "Default Group", "Funding-2014-Actual Commitments", "50,000", "Totals-Actual Commitments", "50,000"),
                                new ReportAreaForTests(new AreaOwner(43), "Project Title", "Activity with primary_tertiary_program", "Donor Group", "International", "Funding-2014-Actual Commitments", "50,000", "Totals-Actual Commitments", "50,000"),
                                new ReportAreaForTests(new AreaOwner(44), "Project Title", "activity with primary_program", "Donor Group", "International", "Funding-2014-Actual Commitments", "32,000", "Totals-Actual Commitments", "32,000"),
                                new ReportAreaForTests(new AreaOwner(45), "Project Title", "activity with tertiary_program", "Donor Group", "National", "Funding-2014-Actual Commitments", "15,000", "Totals-Actual Commitments", "15,000"),
                                new ReportAreaForTests(new AreaOwner(50), "Project Title", "activity with capital spending", "Donor Group", "Default Group", "Funding-2014-Actual Commitments", "65,760,63", "Funding-2014-Actual Disbursements", "80,000", "Totals-Actual Commitments", "65,760,63", "Totals-Actual Disbursements", "80,000"),
                                new ReportAreaForTests(new AreaOwner(52), "Project Title", "activity with contracting agency", "Donor Group", "Default Group, National", "Funding-2006-Actual Commitments", "96,840,58", "Funding-2014-Actual Disbursements", "50,000", "Totals-Actual Commitments", "96,840,58", "Totals-Actual Disbursements", "50,000"),
                                new ReportAreaForTests(new AreaOwner(53), "Project Title", "new activity with contracting", "Donor Group", "Default Group", "Funding-2014-Actual Commitments", "12,000", "Totals-Actual Commitments", "12,000"),
                                new ReportAreaForTests(new AreaOwner(61), "Project Title", "activity-with-unfunded-components", "Donor Group", "Default Group, National", "Funding-2014-Actual Commitments", "123,321", "Totals-Actual Commitments", "123,321"),
                                new ReportAreaForTests(new AreaOwner(63), "Project Title", "activity with funded components", "Donor Group", "International", "Funding-2014-Actual Commitments", "100", "Totals-Actual Commitments", "100"),
                                new ReportAreaForTests(new AreaOwner(64), "Project Title", "Unvalidated activity", "Donor Group", "International", "Funding-2015-Actual Commitments", "45,000", "Totals-Actual Commitments", "45,000"),
                                new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Donor Group", "Default Group", "Funding-2015-Actual Commitments", "456,789", "Funding-2015-Actual Disbursements", "321,765", "Totals-Actual Commitments", "456,789", "Totals-Actual Disbursements", "321,765"),
                                new ReportAreaForTests(new AreaOwner(66), "Project Title", "Activity 2 with multiple agreements", "Donor Group", "International, National", "Funding-2015-Actual Commitments", "700", "Totals-Actual Commitments", "700"),
                                new ReportAreaForTests(new AreaOwner(67), "Project Title", "third activity with agreements", "Donor Group", "Default Group, National", "Funding-2015-Actual Commitments", "123,456", "Totals-Actual Commitments", "123,456"),
                                new ReportAreaForTests(new AreaOwner(68), "Project Title", "activity with incomplete agreement", "Donor Group", "Default Group", "Funding-2015-Actual Commitments", "123,000", "Totals-Actual Commitments", "123,000"),
                                new ReportAreaForTests(new AreaOwner(69), "Project Title", "Activity with planned disbursements", "Donor Group", "European", "Funding-2014-Actual Disbursements", "200", "Funding-2015-Actual Disbursements", "500", "Totals-Actual Disbursements", "700"),
                                new ReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms", "Donor Group", "Default Group, National", "Funding-2015-Actual Commitments", "888,000", "Totals-Actual Commitments", "888,000"),
                                new ReportAreaForTests(new AreaOwner(71), "Project Title", "activity_with_disaster_response", "Donor Group", "Default Group, National", "Funding-2014-Actual Commitments", "33,000", "Funding-2015-Actual Commitments", "117,000", "Totals-Actual Commitments", "150,000"),
                                new ReportAreaForTests(new AreaOwner(73), "Project Title", "activity with directed MTEFs", "Donor Group", "National", "Funding-2015-Actual Commitments", "123,456", "Totals-Actual Commitments", "123,456"),
                                new ReportAreaForTests(new AreaOwner(76), "Project Title", "activity with pipeline MTEFs and act. disb", "Donor Group", "European, International", "Funding-2013-Actual Disbursements", "35,000", "Funding-2014-Actual Disbursements", "75,000", "Totals-Actual Disbursements", "110,000"),
                                new ReportAreaForTests(new AreaOwner(77), "Project Title", "execution rate activity", "Donor Group", "International, National", "Funding-2014-Actual Disbursements", "55,000", "Funding-2015-Actual Disbursements", "35,000", "Totals-Actual Disbursements", "90,000"),
                                new ReportAreaForTests(new AreaOwner(79), "Project Title", "with weird currencies", "Donor Group", "Default Group, European, National", "Funding-2014-Actual Commitments", "3,632,14", "Funding-2015-Actual Commitments", "93,930,84", "Totals-Actual Commitments", "97,562,98")      ));

        ReportSpecificationImpl spec = buildSpecForFiltering("flat filter by not donor group",
            Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.DONOR_GROUP),
            null, 
            ColumnConstants.DONOR_GROUP, Arrays.asList(19l), false); // "American"

        runNiTestCase(spec, "en", acts, cor);
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
                  .withContents("Primary Sector", "", "Primary Sector Sub-Sector", "", "Project Title", "", "Funding-2015-Actual Commitments", "1,344,789", "Totals-Actual Commitments", "1,344,789")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner("Primary Sector", "110 - EDUCATION")).withContents("Primary Sector Sub-Sector", "", "Project Title", "", "Funding-2015-Actual Commitments", "761,194,5", "Totals-Actual Commitments", "761,194,5", "Primary Sector", "110 - EDUCATION")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner("Primary Sector Sub-Sector", "111 - Education, level unspecified"))
                      .withContents("Project Title", "", "Funding-2015-Actual Commitments", "761,194,5", "Totals-Actual Commitments", "761,194,5", "Primary Sector Sub-Sector", "111 - Education, level unspecified")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Funding-2015-Actual Commitments", "228,394,5", "Totals-Actual Commitments", "228,394,5"),
                        new ReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms", "Funding-2015-Actual Commitments", "532,800", "Totals-Actual Commitments", "532,800")          )        ),
                    new ReportAreaForTests(new AreaOwner("Primary Sector", "112 - BASIC EDUCATION"))
                    .withContents("Primary Sector Sub-Sector", "", "Project Title", "", "Funding-2015-Actual Commitments", "583,594,5", "Totals-Actual Commitments", "583,594,5", "Primary Sector", "112 - BASIC EDUCATION")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner("Primary Sector Sub-Sector", "11230 - Basic life skills for youth and adults")).withContents("Project Title", "", "Funding-2015-Actual Commitments", "105,061,47", "Totals-Actual Commitments", "105,061,47", "Primary Sector Sub-Sector", "11230 - Basic life skills for youth and adults")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Funding-2015-Actual Commitments", "105,061,47", "Totals-Actual Commitments", "105,061,47")          ),
                      new ReportAreaForTests(new AreaOwner("Primary Sector Sub-Sector", "11240 - Early childhood education"))
                      .withContents("Project Title", "", "Funding-2015-Actual Commitments", "478,533,03", "Totals-Actual Commitments", "478,533,03", "Primary Sector Sub-Sector", "11240 - Early childhood education")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Funding-2015-Actual Commitments", "123,333,03", "Totals-Actual Commitments", "123,333,03"),
                        new ReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms", "Funding-2015-Actual Commitments", "355,200", "Totals-Actual Commitments", "355,200")          )        )      ));

        //spec("double-hierarchy-by-sector-by-subsector")
        ReportSpecificationImpl spec = buildSpecification("double-hierarchy-by-sector-by-subsector", 
                Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.PRIMARY_SECTOR, ColumnConstants.PRIMARY_SECTOR_SUB_SECTOR), 
                Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS), 
                Arrays.asList(ColumnConstants.PRIMARY_SECTOR, ColumnConstants.PRIMARY_SECTOR_SUB_SECTOR),
                GroupingCriteria.GROUPING_YEARLY);
        runNiTestCase(cor, spec, Arrays.asList("activity 1 with agreement", "Activity with both MTEFs and Act.Comms"));
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
                .withBody(      new ReportAreaForTests(null).withContents("Primary Sector", "", "Primary Sector Sub-Sector", "", "Project Title", "", "Funding-2015-Actual Commitments", "583,594,5", "Totals-Actual Commitments", "583,594,5")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner("Primary Sector", "112 - BASIC EDUCATION"))
                    .withContents("Primary Sector Sub-Sector", "", "Project Title", "", "Funding-2015-Actual Commitments", "583,594,5", "Totals-Actual Commitments", "583,594,5", "Primary Sector", "112 - BASIC EDUCATION")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner("Primary Sector Sub-Sector", "11230 - Basic life skills for youth and adults")).withContents("Project Title", "", "Funding-2015-Actual Commitments", "105,061,47", "Totals-Actual Commitments", "105,061,47", "Primary Sector Sub-Sector", "11230 - Basic life skills for youth and adults")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Funding-2015-Actual Commitments", "105,061,47", "Totals-Actual Commitments", "105,061,47")          ),
                      new ReportAreaForTests(new AreaOwner("Primary Sector Sub-Sector", "11240 - Early childhood education"))
                      .withContents("Project Title", "", "Funding-2015-Actual Commitments", "478,533,03", "Totals-Actual Commitments", "478,533,03", "Primary Sector Sub-Sector", "11240 - Early childhood education")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Funding-2015-Actual Commitments", "123,333,03", "Totals-Actual Commitments", "123,333,03"),
                        new ReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms", "Funding-2015-Actual Commitments", "355,200", "Totals-Actual Commitments", "355,200")          )        )      ));

        //spec("double-hierarchy-by-sector-by-subsector")
        ReportSpecificationImpl spec = buildSpecification("double-hierarchy-by-sector-by-subsector-filtered-by-sector", 
                Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.PRIMARY_SECTOR, ColumnConstants.PRIMARY_SECTOR_SUB_SECTOR), 
                Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS), 
                Arrays.asList(ColumnConstants.PRIMARY_SECTOR, ColumnConstants.PRIMARY_SECTOR_SUB_SECTOR),
                GroupingCriteria.GROUPING_YEARLY);
        
        ReportFiltersImpl filters = new ReportFiltersImpl();
        filters.addFilterRule(new ReportElement(new ReportColumn(ColumnConstants.PRIMARY_SECTOR)), new FilterRule(Arrays.asList("6242", "6243", "6244", "6245"), true));
        spec.setFilters(filters);

        runNiTestCase(cor, spec, Arrays.asList("activity 1 with agreement", "Activity with both MTEFs and Act.Comms"));
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
                .withBody(      new ReportAreaForTests(null).withContents("Primary Sector", "", "Primary Sector Sub-Sector", "", "Project Title", "", "Funding-2015-Actual Commitments", "478,533,03", "Totals-Actual Commitments", "478,533,03")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner("Primary Sector", "112 - BASIC EDUCATION")).withContents("Primary Sector Sub-Sector", "", "Project Title", "", "Funding-2015-Actual Commitments", "478,533,03", "Totals-Actual Commitments", "478,533,03", "Primary Sector", "112 - BASIC EDUCATION")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner("Primary Sector Sub-Sector", "11240 - Early childhood education"))
                      .withContents("Project Title", "", "Funding-2015-Actual Commitments", "478,533,03", "Totals-Actual Commitments", "478,533,03", "Primary Sector Sub-Sector", "11240 - Early childhood education")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Funding-2015-Actual Commitments", "123,333,03", "Totals-Actual Commitments", "123,333,03"),
                        new ReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms", "Funding-2015-Actual Commitments", "355,200", "Totals-Actual Commitments", "355,200")          )        )      ));

        //spec("double-hierarchy-by-sector-by-subsector")
        ReportSpecificationImpl spec = buildSpecification("double-hierarchy-by-sector-by-subsector-filtered-by-subsector", 
                Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.PRIMARY_SECTOR, ColumnConstants.PRIMARY_SECTOR_SUB_SECTOR), 
                Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS), 
                Arrays.asList(ColumnConstants.PRIMARY_SECTOR, ColumnConstants.PRIMARY_SECTOR_SUB_SECTOR),
                GroupingCriteria.GROUPING_YEARLY);
        
        ReportFiltersImpl filters = new ReportFiltersImpl();
        filters.addFilterRule(new ReportElement(new ReportColumn(ColumnConstants.PRIMARY_SECTOR_SUB_SECTOR)), new FilterRule("6245", true));
        spec.setFilters(filters);

        runNiTestCase(cor, spec, Arrays.asList("activity 1 with agreement", "Activity with both MTEFs and Act.Comms"));
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
                  .withContents("Primary Sector", "", "Primary Sector Sub-Sector", "", "Project Title", "", "Funding-2015-Actual Commitments", "1,344,789", "Totals-Actual Commitments", "1,344,789")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner("Primary Sector", "110 - EDUCATION")).withContents("Primary Sector Sub-Sector", "", "Project Title", "", "Funding-2015-Actual Commitments", "761,194,5", "Totals-Actual Commitments", "761,194,5", "Primary Sector", "110 - EDUCATION")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner("Primary Sector Sub-Sector", "111 - Education, level unspecified"))
                      .withContents("Project Title", "", "Funding-2015-Actual Commitments", "761,194,5", "Totals-Actual Commitments", "761,194,5", "Primary Sector Sub-Sector", "111 - Education, level unspecified")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Funding-2015-Actual Commitments", "228,394,5", "Totals-Actual Commitments", "228,394,5"),
                        new ReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms", "Funding-2015-Actual Commitments", "532,800", "Totals-Actual Commitments", "532,800")          )        ),
                    new ReportAreaForTests(new AreaOwner("Primary Sector", "112 - BASIC EDUCATION"))
                    .withContents("Primary Sector Sub-Sector", "", "Project Title", "", "Funding-2015-Actual Commitments", "583,594,5", "Totals-Actual Commitments", "583,594,5", "Primary Sector", "112 - BASIC EDUCATION")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner("Primary Sector Sub-Sector", "11230 - Basic life skills for youth and adults")).withContents("Project Title", "", "Funding-2015-Actual Commitments", "105,061,47", "Totals-Actual Commitments", "105,061,47", "Primary Sector Sub-Sector", "11230 - Basic life skills for youth and adults")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Funding-2015-Actual Commitments", "105,061,47", "Totals-Actual Commitments", "105,061,47")          ),
                      new ReportAreaForTests(new AreaOwner("Primary Sector Sub-Sector", "11240 - Early childhood education"))
                      .withContents("Project Title", "", "Funding-2015-Actual Commitments", "478,533,03", "Totals-Actual Commitments", "478,533,03", "Primary Sector Sub-Sector", "11240 - Early childhood education")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Funding-2015-Actual Commitments", "123,333,03", "Totals-Actual Commitments", "123,333,03"),
                        new ReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms", "Funding-2015-Actual Commitments", "355,200", "Totals-Actual Commitments", "355,200")          )        )      ));

        //spec("double-hierarchy-by-sector-by-subsector")
        ReportSpecificationImpl spec = buildSpecification("double-hierarchy-by-sector-by-subsector-filtered-fictively-by-subsector", 
                Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.PRIMARY_SECTOR, ColumnConstants.PRIMARY_SECTOR_SUB_SECTOR), 
                Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS), 
                Arrays.asList(ColumnConstants.PRIMARY_SECTOR, ColumnConstants.PRIMARY_SECTOR_SUB_SECTOR),
                GroupingCriteria.GROUPING_YEARLY);
        
        ReportFiltersImpl filters = new ReportFiltersImpl();
        filters.addFilterRule(new ReportElement(new ReportColumn(ColumnConstants.PRIMARY_SECTOR)), new FilterRule(Arrays.asList("6236", "6237", "6238", "6239", "6240", "6241", "6242", "6243", "6244", "6245", "6246", "6247", "6248", "6249", "6250", "6251"), true));
        spec.setFilters(filters);

        runNiTestCase(cor, spec, Arrays.asList("activity 1 with agreement", "Activity with both MTEFs and Act.Comms"));
    }

    @Test
    public void testFilteringByOneDonorAgency() throws Exception {
        NiReportModel cor = new NiReportModel("testFilteringByOneDonorAgency")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 9))",
                        "(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 1, colSpan: 6));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 7, colSpan: 2))",
                        "(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 1, colSpan: 2));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 2))",
                        "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                        .withContents("Project Title", "", "Funding-2013-Actual Commitments", "4,703,222", "Funding-2013-Actual Disbursements", "415,000", "Funding-2014-Actual Commitments", "7,700,000", "Funding-2014-Actual Disbursements", "450,000", "Funding-2015-Actual Commitments", "500", "Funding-2015-Actual Disbursements", "80,070", "Totals-Actual Commitments", "12,403,722", "Totals-Actual Disbursements", "945,070")
                        .withChildren(
                                new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Funding-2013-Actual Disbursements", "415,000", "Totals-Actual Disbursements", "415,000"),
                                new ReportAreaForTests(new AreaOwner(29), "Project Title", "ptc activity 2", "Funding-2013-Actual Commitments", "333,222", "Totals-Actual Commitments", "333,222"),
                                new ReportAreaForTests(new AreaOwner(46), "Project Title", "pledged education activity 1", "Funding-2013-Actual Commitments", "1,700,000", "Funding-2014-Actual Commitments", "3,300,000", "Totals-Actual Commitments", "5,000,000"),
                                new ReportAreaForTests(new AreaOwner(48), "Project Title", "pledged 2", "Funding-2013-Actual Commitments", "2,670,000", "Funding-2014-Actual Commitments", "4,400,000", "Funding-2014-Actual Disbursements", "450,000", "Totals-Actual Commitments", "7,070,000", "Totals-Actual Disbursements", "450,000"),
                                new ReportAreaForTests(new AreaOwner(66), "Project Title", "Activity 2 with multiple agreements", "Funding-2015-Actual Commitments", "500", "Totals-Actual Commitments", "500"),
                                new ReportAreaForTests(new AreaOwner(69), "Project Title", "Activity with planned disbursements", "Funding-2015-Actual Disbursements", "70", "Totals-Actual Disbursements", "70"),
                                new ReportAreaForTests(new AreaOwner(78), "Project Title", "activity with many MTEFs", "Funding-2015-Actual Disbursements", "80,000", "Totals-Actual Disbursements", "80,000")      ));

        ReportElement elem = new ReportElement(new ReportColumn(ColumnConstants.DONOR_AGENCY));
        FilterRule filterRule = new FilterRule(Arrays.asList("21696"), true);

        ReportSpecificationImpl spec = buildSpecForFiltering("testFilteringByOneDonorAgency",
                Arrays.asList(ColumnConstants.PROJECT_TITLE), null, elem, filterRule);

        runNiTestCase(cor, spec, acts);
    }

    @Test
    public void testFilteringByOneDonorGroup() throws Exception {
        NiReportModel cor = new NiReportModel("testFilteringByOneDonorGroup")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 13))",
                        "(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 1, colSpan: 10));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 11, colSpan: 2))",
                        "(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 1, colSpan: 2));(2011: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 2));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 2));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 7, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 9, colSpan: 2))",
                        "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                        .withContents("Project Title", "", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "123,321", "Funding-2011-Actual Commitments", "213,231", "Funding-2011-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "35,000", "Funding-2014-Actual Commitments", "82,100", "Funding-2014-Actual Disbursements", "75,000", "Funding-2015-Actual Commitments", "45,700", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "341,031", "Totals-Actual Disbursements", "233,321")
                        .withChildren(
                                new ReportAreaForTests(new AreaOwner(12), "Project Title", "TAC_activity_1", "Funding-2010-Actual Disbursements", "123,321", "Funding-2011-Actual Commitments", "213,231", "Totals-Actual Commitments", "213,231", "Totals-Actual Disbursements", "123,321"),
                                new ReportAreaForTests(new AreaOwner(43), "Project Title", "Activity with primary_tertiary_program", "Funding-2014-Actual Commitments", "50,000", "Totals-Actual Commitments", "50,000"),
                                new ReportAreaForTests(new AreaOwner(44), "Project Title", "activity with primary_program", "Funding-2014-Actual Commitments", "32,000", "Totals-Actual Commitments", "32,000"),
                                new ReportAreaForTests(new AreaOwner(63), "Project Title", "activity with funded components", "Funding-2014-Actual Commitments", "100", "Totals-Actual Commitments", "100"),
                                new ReportAreaForTests(new AreaOwner(64), "Project Title", "Unvalidated activity", "Funding-2015-Actual Commitments", "45,000", "Totals-Actual Commitments", "45,000"),
                                new ReportAreaForTests(new AreaOwner(66), "Project Title", "Activity 2 with multiple agreements", "Funding-2015-Actual Commitments", "700", "Totals-Actual Commitments", "700"),
                                new ReportAreaForTests(new AreaOwner(76), "Project Title", "activity with pipeline MTEFs and act. disb", "Funding-2013-Actual Disbursements", "35,000", "Funding-2014-Actual Disbursements", "75,000", "Totals-Actual Disbursements", "110,000")      ));

        ReportElement elem = new ReportElement(new ReportColumn(ColumnConstants.DONOR_GROUP));
        FilterRule filterRule = new FilterRule(Arrays.asList("20"), true);

        ReportSpecificationImpl spec = buildSpecForFiltering("testFilteringByOneDonorGroup",
                Arrays.asList(ColumnConstants.PROJECT_TITLE), null, elem, filterRule);

        runNiTestCase(cor, spec, acts);
    }

    @Test
    public void testFilteringByDonorAgencyOrDonorGroup() throws Exception {
        NiReportModel cor = new NiReportModel("testFilteringByDonorAgencyOrDonorGroup")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 13))",
                        "(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 1, colSpan: 10));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 11, colSpan: 2))",
                        "(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 1, colSpan: 2));(2011: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 2));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 2));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 7, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 9, colSpan: 2))",
                        "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                        .withContents("Project Title", "", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "123,321", "Funding-2011-Actual Commitments", "213,231", "Funding-2011-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "4,703,222", "Funding-2013-Actual Disbursements", "450,000", "Funding-2014-Actual Commitments", "7,782,100", "Funding-2014-Actual Disbursements", "525,000", "Funding-2015-Actual Commitments", "46,200", "Funding-2015-Actual Disbursements", "80,070", "Totals-Actual Commitments", "12,744,753", "Totals-Actual Disbursements", "1,178,391")
                        .withChildren(
                                new ReportAreaForTests(new AreaOwner(12), "Project Title", "TAC_activity_1", "Funding-2010-Actual Disbursements", "123,321", "Funding-2011-Actual Commitments", "213,231", "Totals-Actual Commitments", "213,231", "Totals-Actual Disbursements", "123,321"),
                                new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Funding-2013-Actual Disbursements", "415,000", "Totals-Actual Disbursements", "415,000"),
                                new ReportAreaForTests(new AreaOwner(29), "Project Title", "ptc activity 2", "Funding-2013-Actual Commitments", "333,222", "Totals-Actual Commitments", "333,222"),
                                new ReportAreaForTests(new AreaOwner(43), "Project Title", "Activity with primary_tertiary_program", "Funding-2014-Actual Commitments", "50,000", "Totals-Actual Commitments", "50,000"),
                                new ReportAreaForTests(new AreaOwner(44), "Project Title", "activity with primary_program", "Funding-2014-Actual Commitments", "32,000", "Totals-Actual Commitments", "32,000"),
                                new ReportAreaForTests(new AreaOwner(46), "Project Title", "pledged education activity 1", "Funding-2013-Actual Commitments", "1,700,000", "Funding-2014-Actual Commitments", "3,300,000", "Totals-Actual Commitments", "5,000,000"),
                                new ReportAreaForTests(new AreaOwner(48), "Project Title", "pledged 2", "Funding-2013-Actual Commitments", "2,670,000", "Funding-2014-Actual Commitments", "4,400,000", "Funding-2014-Actual Disbursements", "450,000", "Totals-Actual Commitments", "7,070,000", "Totals-Actual Disbursements", "450,000"),
                                new ReportAreaForTests(new AreaOwner(63), "Project Title", "activity with funded components", "Funding-2014-Actual Commitments", "100", "Totals-Actual Commitments", "100"),
                                new ReportAreaForTests(new AreaOwner(64), "Project Title", "Unvalidated activity", "Funding-2015-Actual Commitments", "45,000", "Totals-Actual Commitments", "45,000"),
                                new ReportAreaForTests(new AreaOwner(66), "Project Title", "Activity 2 with multiple agreements", "Funding-2015-Actual Commitments", "1,200", "Totals-Actual Commitments", "1,200"),
                                new ReportAreaForTests(new AreaOwner(69), "Project Title", "Activity with planned disbursements", "Funding-2015-Actual Disbursements", "70", "Totals-Actual Disbursements", "70"),
                                new ReportAreaForTests(new AreaOwner(76), "Project Title", "activity with pipeline MTEFs and act. disb", "Funding-2013-Actual Disbursements", "35,000", "Funding-2014-Actual Disbursements", "75,000", "Totals-Actual Disbursements", "110,000"),
                                new ReportAreaForTests(new AreaOwner(78), "Project Title", "activity with many MTEFs", "Funding-2015-Actual Disbursements", "80,000", "Totals-Actual Disbursements", "80,000")      ));

        ReportFiltersImpl reportFilters = new ReportFiltersImpl();

        reportFilters.addFilterRule(
                new ReportElement(new ReportColumn(ColumnConstants.DONOR_AGENCY)),
                new FilterRule(Arrays.asList("21696"), true));

        reportFilters.addFilterRule(
                new ReportElement(new ReportColumn(ColumnConstants.DONOR_GROUP)),
                new FilterRule(Arrays.asList("20"), true));

        ReportSpecificationImpl spec = buildSpecForFiltering("testFilteringByDonorAgencyOrDonorGroup",
                Arrays.asList(ColumnConstants.PROJECT_TITLE), null, reportFilters);

        runNiTestCase(cor, spec, acts);
    }

    @Test
    public void testFilteringByImplementingAgency() throws Exception {
        NiReportModel cor = new NiReportModel("testFilteringByImplementingAgency")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 7))",
                        "(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 1, colSpan: 4));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 5, colSpan: 2))",
                        "(2006: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 1, colSpan: 2));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 2))",
                        "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null).withContents("Project Title", "", "Funding-2006-Actual Commitments", "96,840,58", "Funding-2006-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "50,000", "Totals-Actual Commitments", "96,840,58", "Totals-Actual Disbursements", "50,000")
                        .withChildren(
                                new ReportAreaForTests(new AreaOwner(52), "Project Title", "activity with contracting agency", "Funding-2006-Actual Commitments", "96,840,58", "Funding-2014-Actual Disbursements", "50,000", "Totals-Actual Commitments", "96,840,58", "Totals-Actual Disbursements", "50,000")      ));

        ReportElement elem = new ReportElement(new ReportColumn(ColumnConstants.IMPLEMENTING_AGENCY));
        FilterRule filterRule = new FilterRule(Arrays.asList("21378"), true);

        ReportSpecificationImpl spec = buildSpecForFiltering("testFilteringByImplementingAgency",
                Arrays.asList(ColumnConstants.PROJECT_TITLE), null, elem, filterRule);

        runNiTestCase(cor, spec, acts);
    }

    @Test
    public void testFilteringByDonorAgencyAndExecutingAgency() throws Exception {
        NiReportModel cor = new NiReportModel("testFilteringByDonorAgencyAndExecutingAgency")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 7))",
                        "(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 1, colSpan: 4));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 5, colSpan: 2))",
                        "(2006: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 1, colSpan: 2));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 2))",
                        "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null).withContents("Project Title", "", "Funding-2006-Actual Commitments", "96,840,58", "Funding-2006-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "50,000", "Totals-Actual Commitments", "96,840,58", "Totals-Actual Disbursements", "50,000")
                        .withChildren(
                                new ReportAreaForTests(new AreaOwner(52), "Project Title", "activity with contracting agency", "Funding-2006-Actual Commitments", "96,840,58", "Funding-2014-Actual Disbursements", "50,000", "Totals-Actual Commitments", "96,840,58", "Totals-Actual Disbursements", "50,000")      ));

        ReportFiltersImpl reportFilters = new ReportFiltersImpl();

        reportFilters.addFilterRule(
                new ReportElement(new ReportColumn(ColumnConstants.DONOR_AGENCY)),
                new FilterRule(Arrays.asList("21698"), true));

        reportFilters.addFilterRule(
                new ReportElement(new ReportColumn(ColumnConstants.IMPLEMENTING_AGENCY)),
                new FilterRule(Arrays.asList("21378"), true));

        ReportSpecificationImpl spec = buildSpecForFiltering("testFilteringByDonorAgencyAndExecutingAgency",
                Arrays.asList(ColumnConstants.PROJECT_TITLE), null, reportFilters);

        runNiTestCase(cor, spec, acts);
    }
}
