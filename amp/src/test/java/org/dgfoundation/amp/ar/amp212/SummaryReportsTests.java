package org.dgfoundation.amp.ar.amp212;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.newreports.ReportAreaForTests;
import org.dgfoundation.amp.newreports.ReportingTestCase;
import org.dgfoundation.amp.newreports.AreaOwner;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.nireports.testcases.NiReportModel;
import org.dgfoundation.amp.nireports.testcases.generic.HardcodedReportsTestSchema;
import org.junit.Test;

/**
 * 
 * sanity checks for NiReports running offdb
 * 
 * @author Alexandru Cartaleanu
 *
 */
public class SummaryReportsTests extends ReportingTestCase {

    HardcodedReportsTestSchema schema = new HardcodedReportsTestSchema();
    final static List<String> ACTS = Arrays.asList("TAC_activity_1", "Eth Water", "Unvalidated activity");

    public SummaryReportsTests() {
        inTransactionRule = null;
    }

    /**
     * builds a summary report spec with given hiers, AC / AD as measures
     * @param hiers
     * @return
     */
    protected ReportSpecificationImpl summarySpec(String reportName, List<String> hiers, GroupingCriteria groupingCrit) {
        ReportSpecificationImpl ret = buildSpecification(reportName, hiers,
            Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS), 
            hiers, 
            groupingCrit);
        ret.setSummaryReport(true);
        return ret;
    }
    
    @Test
    public void testSummary_report_flat() {
        NiReportModel cor = new NiReportModel("flat-summary")
        .withHeaders(Arrays.asList(
                "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 10))",
                "(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 0, colSpan: 8));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 8, colSpan: 2))",
                "(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 0, colSpan: 2));(2011: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 2));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 6, colSpan: 2))",
                "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 0, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1))"))
            .withWarnings(Arrays.asList())
            .withBody(      new ReportAreaForTests(null, "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "123,321", "Funding-2011-Actual Commitments", "213,231", "Funding-2011-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "545,000", "Funding-2015-Actual Commitments", "45,000", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "258,231", "Totals-Actual Disbursements", "668,321"));
    
        runNiTestCase(cor, summarySpec("flat-summary", Collections.emptyList(), GroupingCriteria.GROUPING_YEARLY), ACTS);
        
        NiReportModel corTotalsOnly = new NiReportModel("flat-summary")
        .withHeaders(Arrays.asList(
                "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 0, colSpan: 2))",
                "(Totals: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 0, colSpan: 2))",
                "(Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 0, colSpan: 1));(Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1))"))
            .withWarnings(Arrays.asList())
            .withBody(      new ReportAreaForTests(null, "Totals-Actual Commitments", "258,231", "Totals-Actual Disbursements", "668,321"));
        
        runNiTestCase(corTotalsOnly, summarySpec("flat-summary-totals-only", Collections.emptyList(), GroupingCriteria.GROUPING_TOTALS_ONLY), ACTS);
    }
    
    @Test
    public void testSummaryReport_single_hier() {
        NiReportModel cor = new NiReportModel("by-region-summary")
        .withHeaders(Arrays.asList(
                "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 11))",
                "(Administrative Level 1: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 1, colSpan: 8));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 9, colSpan: 2))",
                "(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 1, colSpan: 2));(2011: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 2));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 7, colSpan: 2))",
                "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1))"))
            .withWarnings(Arrays.asList())
            .withBody(      new ReportAreaForTests(null)
              .withContents("Administrative Level 1", "", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "123,321", "Funding-2011-Actual Commitments", "213,231", "Funding-2011-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "545,000", "Funding-2015-Actual Commitments", "45,000", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "258,231", "Totals-Actual Disbursements", "668,321")
              .withChildren(
                new ReportAreaForTests(new AreaOwner("Administrative Level 1", "Anenii Noi County"), "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "545,000", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "0", "Totals-Actual Disbursements", "545,000", "Administrative Level 1", "Anenii Noi County"),
                new ReportAreaForTests(new AreaOwner("Administrative Level 1", "Dubasari County"), "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "123,321", "Funding-2011-Actual Commitments", "213,231", "Funding-2011-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "213,231", "Totals-Actual Disbursements", "123,321", "Administrative Level 1", "Dubasari County"),
                new ReportAreaForTests(new AreaOwner("Administrative Level 1", "Administrative Level 1: Undefined"), "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "45,000", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "45,000", "Totals-Actual Disbursements", "0", "Administrative Level 1", "Administrative Level 1: Undefined")      ));
        
        runNiTestCase(cor, summarySpec("by-region-summary", Arrays.asList(ColumnConstants.LOCATION_ADM_LEVEL_1), GroupingCriteria.GROUPING_YEARLY), ACTS);
        
        NiReportModel corTotalsOnly = new NiReportModel("by-region-summary-totals-only")
        .withHeaders(Arrays.asList(
                "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 0, colSpan: 3))",
                "(Administrative Level 1: (startRow: 1, rowSpan: 2, totalRowSpan: 2, colStart: 0, colSpan: 1));(Totals: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 1, colSpan: 2))",
                "(Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1));(Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1))"))
            .withWarnings(Arrays.asList())
            .withBody(      new ReportAreaForTests(null)
              .withContents("Administrative Level 1", "", "Totals-Actual Commitments", "258,231", "Totals-Actual Disbursements", "668,321")
              .withChildren(
                new ReportAreaForTests(new AreaOwner("Administrative Level 1", "Anenii Noi County"), "Totals-Actual Commitments", "0", "Totals-Actual Disbursements", "545,000", "Administrative Level 1", "Anenii Noi County"),
                new ReportAreaForTests(new AreaOwner("Administrative Level 1", "Dubasari County"), "Totals-Actual Commitments", "213,231", "Totals-Actual Disbursements", "123,321", "Administrative Level 1", "Dubasari County"),
                new ReportAreaForTests(new AreaOwner("Administrative Level 1", "Administrative Level 1: Undefined"), "Totals-Actual Commitments", "45,000", "Totals-Actual Disbursements", "0", "Administrative Level 1", "Administrative Level 1: Undefined")      ));

        runNiTestCase(corTotalsOnly, summarySpec("by-region-summary-totals-only", Arrays.asList(ColumnConstants.LOCATION_ADM_LEVEL_1), GroupingCriteria.GROUPING_TOTALS_ONLY), ACTS);
    }
    
    @Test
    public void testSummaryReport_double_hier() {
        NiReportModel cor =  new NiReportModel("by-region-sector")
        .withHeaders(Arrays.asList(
                "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 12))",
                "(Administrative Level 1: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Primary Sector: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 8));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 10, colSpan: 2))",
                "(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 2));(2011: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 2));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 6, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 8, colSpan: 2))",
                "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1))"))
            .withWarnings(Arrays.asList())
            .withBody(      new ReportAreaForTests(null)
              .withContents("Administrative Level 1", "", "Primary Sector", "", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "123,321", "Funding-2011-Actual Commitments", "213,231", "Funding-2011-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "545,000", "Funding-2015-Actual Commitments", "45,000", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "258,231", "Totals-Actual Disbursements", "668,321")
              .withChildren(
                new ReportAreaForTests(new AreaOwner("Administrative Level 1", "Anenii Noi County")).withContents("Primary Sector", "", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "545,000", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "0", "Totals-Actual Disbursements", "545,000", "Administrative Level 1", "Anenii Noi County")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner("Primary Sector", "110 - EDUCATION"), "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "545,000", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "0", "Totals-Actual Disbursements", "545,000", "Primary Sector", "110 - EDUCATION")        ),
                new ReportAreaForTests(new AreaOwner("Administrative Level 1", "Dubasari County")).withContents("Primary Sector", "", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "123,321", "Funding-2011-Actual Commitments", "213,231", "Funding-2011-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "213,231", "Totals-Actual Disbursements", "123,321", "Administrative Level 1", "Dubasari County")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner("Primary Sector", "112 - BASIC EDUCATION"), "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "123,321", "Funding-2011-Actual Commitments", "213,231", "Funding-2011-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "213,231", "Totals-Actual Disbursements", "123,321", "Primary Sector", "112 - BASIC EDUCATION")        ),
                new ReportAreaForTests(new AreaOwner("Administrative Level 1", "Administrative Level 1: Undefined")).withContents("Primary Sector", "", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "45,000", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "45,000", "Totals-Actual Disbursements", "0", "Administrative Level 1", "Administrative Level 1: Undefined")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner("Primary Sector", "110 - EDUCATION"), "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "45,000", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "45,000", "Totals-Actual Disbursements", "0", "Primary Sector", "110 - EDUCATION")        )      ));

        runNiTestCase(cor, summarySpec("by-region-sector", Arrays.asList(ColumnConstants.LOCATION_ADM_LEVEL_1, ColumnConstants.PRIMARY_SECTOR), GroupingCriteria.GROUPING_YEARLY), ACTS);
        
        NiReportModel corTotalsOnly = new NiReportModel("by-region-sector-totals-only")
        .withHeaders(Arrays.asList(
                "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 0, colSpan: 4))",
                "(Administrative Level 1: (startRow: 1, rowSpan: 2, totalRowSpan: 2, colStart: 0, colSpan: 1));(Primary Sector: (startRow: 1, rowSpan: 2, totalRowSpan: 2, colStart: 1, colSpan: 1));(Totals: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 2))",
                "(Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1))"))
            .withWarnings(Arrays.asList())
            .withBody(      new ReportAreaForTests(null)
              .withContents("Administrative Level 1", "", "Primary Sector", "", "Totals-Actual Commitments", "258,231", "Totals-Actual Disbursements", "668,321")
              .withChildren(
                new ReportAreaForTests(new AreaOwner("Administrative Level 1", "Anenii Noi County")).withContents("Primary Sector", "", "Totals-Actual Commitments", "0", "Totals-Actual Disbursements", "545,000", "Administrative Level 1", "Anenii Noi County")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner("Primary Sector", "110 - EDUCATION"), "Totals-Actual Commitments", "0", "Totals-Actual Disbursements", "545,000", "Primary Sector", "110 - EDUCATION")        ),
                new ReportAreaForTests(new AreaOwner("Administrative Level 1", "Dubasari County")).withContents("Primary Sector", "", "Totals-Actual Commitments", "213,231", "Totals-Actual Disbursements", "123,321", "Administrative Level 1", "Dubasari County")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner("Primary Sector", "112 - BASIC EDUCATION"), "Totals-Actual Commitments", "213,231", "Totals-Actual Disbursements", "123,321", "Primary Sector", "112 - BASIC EDUCATION")        ),
                new ReportAreaForTests(new AreaOwner("Administrative Level 1", "Administrative Level 1: Undefined")).withContents("Primary Sector", "", "Totals-Actual Commitments", "45,000", "Totals-Actual Disbursements", "0", "Administrative Level 1", "Administrative Level 1: Undefined")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner("Primary Sector", "110 - EDUCATION"), "Totals-Actual Commitments", "45,000", "Totals-Actual Disbursements", "0", "Primary Sector", "110 - EDUCATION"))));

        runNiTestCase(corTotalsOnly, summarySpec("by-region-sector-totals-only", Arrays.asList(ColumnConstants.LOCATION_ADM_LEVEL_1, ColumnConstants.PRIMARY_SECTOR), GroupingCriteria.GROUPING_TOTALS_ONLY), ACTS);
    }
}
