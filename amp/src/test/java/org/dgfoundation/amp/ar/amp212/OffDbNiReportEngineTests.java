package org.dgfoundation.amp.ar.amp212;

import java.util.Arrays;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.newreports.ReportAreaForTests;
import org.dgfoundation.amp.newreports.AreaOwner;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.nireports.testcases.NiReportModel;
import org.junit.Test;

/**
 * 
 * sanity checks for NiReports running offdb
 * 
 * @author Alexandru Cartaleanu
 *
 */
public class OffDbNiReportEngineTests extends BasicSanityChecks {

    public OffDbNiReportEngineTests() {
        inTransactionRule = null;
        nrRunReports = 0;
    }

    @Test
    public void testVarianceAndAverageFlat() {
        NiReportModel cor = new NiReportModel("testVarianceAndAverageMeasuresFlat")
        .withHeaders(Arrays.asList(
                "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 18))",
                "(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Primary Sector: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 12));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 14, colSpan: 4))",
                "(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 4));(2011: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 6, colSpan: 4));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 10, colSpan: 4))",
                "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Average Size of Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Variance Of Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Variance Of Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Average Size of Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Variance Of Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Variance Of Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Average Size of Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1));(Variance Of Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1));(Variance Of Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 14, colSpan: 1));(Average Size of Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 15, colSpan: 1));(Variance Of Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 16, colSpan: 1));(Variance Of Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 17, colSpan: 1))"))
            .withWarnings(Arrays.asList())
            .withBody(      new ReportAreaForTests(null)
              .withContents("Project Title", "", "Primary Sector", "", "Funding-2010-Actual Commitments", "0", "Funding-2010-Average Size of Disbursements", "", "Funding-2010-Variance Of Commitments", "", "Funding-2010-Variance Of Disbursements", "", "Funding-2011-Actual Commitments", "213,231", "Funding-2011-Average Size of Disbursements", "", "Funding-2011-Variance Of Commitments", "", "Funding-2011-Variance Of Disbursements", "", "Funding-2015-Actual Commitments", "137,000", "Funding-2015-Average Size of Disbursements", "", "Funding-2015-Variance Of Commitments", "", "Funding-2015-Variance Of Disbursements", "", "Totals-Actual Commitments", "350,231", "Totals-Average Size of Disbursements", "", "Totals-Variance Of Commitments", "", "Totals-Variance Of Disbursements", "")
              .withChildren(
                new ReportAreaForTests(new AreaOwner(12), "Project Title", "TAC_activity_1", "Primary Sector", "112 - BASIC EDUCATION", "Funding-2010-Average Size of Disbursements", "123,321", "Funding-2010-Variance Of Disbursements", "0", "Funding-2011-Actual Commitments", "213,231", "Funding-2011-Variance Of Commitments", "0", "Totals-Actual Commitments", "213,231", "Totals-Average Size of Disbursements", "123,321", "Totals-Variance Of Commitments", "0", "Totals-Variance Of Disbursements", "0"),
                new ReportAreaForTests(new AreaOwner(700), "Project Title", "custom_1", "Primary Sector", "110 - EDUCATION, 112 - BASIC EDUCATION", "Funding-2015-Actual Commitments", "137,000", "Funding-2015-Average Size of Disbursements", "23,000", "Funding-2015-Variance Of Commitments", "18,000", "Funding-2015-Variance Of Disbursements", "6,000", "Totals-Actual Commitments", "137,000", "Totals-Average Size of Disbursements", "23,000", "Totals-Variance Of Commitments", "18,000", "Totals-Variance Of Disbursements", "6,000")      ));
    
        ReportSpecificationImpl spec = buildSpecification("testVarianceAndAverageMeasuresFlat",
            Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.PRIMARY_SECTOR),
            Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.AVERAGE_SIZE_DISBURSEMENTS, MeasureConstants.VARIANCE_OF_COMMITMENTS, MeasureConstants.VARIANCE_OF_DISBURSEMENTS),
            null,
            GroupingCriteria.GROUPING_YEARLY);
        
        runNiTestCase(spec, "en", Arrays.asList("custom_1", "TAC_activity_1"), cor);
    }
    
    @Test
    public void testVarianceAndAverageByPrimarySector() {
        NiReportModel cor = 
            new NiReportModel("testVarianceAndAverageMeasuresByPrimarySector")
            .withHeaders(Arrays.asList(
                "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 18))",
                "(Primary Sector: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 12));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 14, colSpan: 4))",
                "(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 4));(2011: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 6, colSpan: 4));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 10, colSpan: 4))",
                "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Average Size of Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Variance Of Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Variance Of Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Average Size of Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Variance Of Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Variance Of Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Average Size of Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1));(Variance Of Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1));(Variance Of Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 14, colSpan: 1));(Average Size of Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 15, colSpan: 1));(Variance Of Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 16, colSpan: 1));(Variance Of Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 17, colSpan: 1))"))
            .withWarnings(Arrays.asList())
            .withBody(      new ReportAreaForTests(null)
              .withContents("Primary Sector", "", "Project Title", "", "Funding-2010-Actual Commitments", "0", "Funding-2010-Average Size of Disbursements", "", "Funding-2010-Variance Of Commitments", "", "Funding-2010-Variance Of Disbursements", "", "Funding-2011-Actual Commitments", "213,231", "Funding-2011-Average Size of Disbursements", "", "Funding-2011-Variance Of Commitments", "", "Funding-2011-Variance Of Disbursements", "", "Funding-2015-Actual Commitments", "137,000", "Funding-2015-Average Size of Disbursements", "", "Funding-2015-Variance Of Commitments", "", "Funding-2015-Variance Of Disbursements", "", "Totals-Actual Commitments", "350,231", "Totals-Average Size of Disbursements", "", "Totals-Variance Of Commitments", "", "Totals-Variance Of Disbursements", "")
              .withChildren(
                new ReportAreaForTests(new AreaOwner("Primary Sector", "110 - EDUCATION", 6236)).withContents("Project Title", "", "Funding-2010-Actual Commitments", "0", "Funding-2010-Average Size of Disbursements", "", "Funding-2010-Variance Of Commitments", "", "Funding-2010-Variance Of Disbursements", "", "Funding-2011-Actual Commitments", "0", "Funding-2011-Average Size of Disbursements", "", "Funding-2011-Variance Of Commitments", "", "Funding-2011-Variance Of Disbursements", "", "Funding-2015-Actual Commitments", "82,200", "Funding-2015-Average Size of Disbursements", "", "Funding-2015-Variance Of Commitments", "", "Funding-2015-Variance Of Disbursements", "", "Totals-Actual Commitments", "82,200", "Totals-Average Size of Disbursements", "", "Totals-Variance Of Commitments", "", "Totals-Variance Of Disbursements", "", "Primary Sector", "110 - EDUCATION")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner(700), "Project Title", "custom_1", "Funding-2015-Actual Commitments", "82,200", "Funding-2015-Average Size of Disbursements", "13,800", "Funding-2015-Variance Of Commitments", "10,800", "Funding-2015-Variance Of Disbursements", "3,600", "Totals-Actual Commitments", "82,200", "Totals-Average Size of Disbursements", "13,800", "Totals-Variance Of Commitments", "10,800", "Totals-Variance Of Disbursements", "3,600")        ),
                new ReportAreaForTests(new AreaOwner("Primary Sector", "112 - BASIC EDUCATION", 6242))
                .withContents("Project Title", "", "Funding-2010-Actual Commitments", "0", "Funding-2010-Average Size of Disbursements", "", "Funding-2010-Variance Of Commitments", "", "Funding-2010-Variance Of Disbursements", "", "Funding-2011-Actual Commitments", "213,231", "Funding-2011-Average Size of Disbursements", "", "Funding-2011-Variance Of Commitments", "", "Funding-2011-Variance Of Disbursements", "", "Funding-2015-Actual Commitments", "54,800", "Funding-2015-Average Size of Disbursements", "", "Funding-2015-Variance Of Commitments", "", "Funding-2015-Variance Of Disbursements", "", "Totals-Actual Commitments", "268,031", "Totals-Average Size of Disbursements", "", "Totals-Variance Of Commitments", "", "Totals-Variance Of Disbursements", "", "Primary Sector", "112 - BASIC EDUCATION")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner(12), "Project Title", "TAC_activity_1", "Funding-2010-Average Size of Disbursements", "123,321", "Funding-2010-Variance Of Disbursements", "0", "Funding-2011-Actual Commitments", "213,231", "Funding-2011-Variance Of Commitments", "0", "Totals-Actual Commitments", "213,231", "Totals-Average Size of Disbursements", "123,321", "Totals-Variance Of Commitments", "0", "Totals-Variance Of Disbursements", "0"),
                  new ReportAreaForTests(new AreaOwner(700), "Project Title", "custom_1", "Funding-2015-Actual Commitments", "54,800", "Funding-2015-Average Size of Disbursements", "9,200", "Funding-2015-Variance Of Commitments", "7,200", "Funding-2015-Variance Of Disbursements", "2,400", "Totals-Actual Commitments", "54,800", "Totals-Average Size of Disbursements", "9,200", "Totals-Variance Of Commitments", "7,200", "Totals-Variance Of Disbursements", "2,400"))));
                        
        ReportSpecificationImpl spec = buildSpecification("testVarianceAndAverageMeasuresByPrimarySector",
            Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.PRIMARY_SECTOR),
            Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.AVERAGE_SIZE_DISBURSEMENTS, MeasureConstants.VARIANCE_OF_COMMITMENTS, MeasureConstants.VARIANCE_OF_DISBURSEMENTS),
            Arrays.asList(ColumnConstants.PRIMARY_SECTOR),
            GroupingCriteria.GROUPING_YEARLY);
        
        runNiTestCase(spec, "en", Arrays.asList("custom_1", "TAC_activity_1"), cor);
    }
}
