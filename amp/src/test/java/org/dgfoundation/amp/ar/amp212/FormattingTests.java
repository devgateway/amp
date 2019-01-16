package org.dgfoundation.amp.ar.amp212;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.List;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.newreports.ReportAreaForTests;
import org.dgfoundation.amp.newreports.ReportingTestCase;
import org.dgfoundation.amp.newreports.AmountsUnits;
import org.dgfoundation.amp.newreports.AreaOwner;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.nireports.testcases.NiReportModel;
import org.junit.Test;

/**
 * 
 * testcases for the fetching states of AMP + the AMP schema
 * 
 * @author Constantin Dolghier
 *
 */
public class FormattingTests extends ReportingTestCase {

    final List<String> acts = Arrays.asList(
            "Activity with Zones", "Activity With Zones and Percentages",
            "crazy funding 1", "date-filters-activity", "Eth Water",
            "TAC_activity_1", "TAC_activity_2");

    public FormattingTests() {
        inTransactionRule = null;
    }

    @Test
    public void testAmountUnits() {
        NiReportModel cor = new NiReportModel("amountUnits")
            .withHeaders(Arrays.asList(
                    "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 13))",
                    "(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 1, colSpan: 10));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 11, colSpan: 2))",
                    "(2009: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 1, colSpan: 2));(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 2));(2011: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 2));(2012: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 7, colSpan: 2));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 9, colSpan: 2))",
                    "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                  .withContents("Project Title", "", "Funding-2009-Actual Commitments", "100,000", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "636,534", "Funding-2011-Actual Commitments", "1,213,119", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "25,000", "Funding-2012-Actual Disbursements", "12,000", "Funding-2013-Actual Commitments", "1,793,333", "Funding-2013-Actual Disbursements", "545,000", "Totals-Actual Commitments", "3,131,452", "Totals-Actual Disbursements", "1,193,534")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(12), "Project Title", "TAC_activity_1", "Funding-2010-Actual Disbursements", "123,321", "Funding-2011-Actual Commitments", "213,231", "Totals-Actual Commitments", "213,231", "Totals-Actual Disbursements", "123,321"),
                    new ReportAreaForTests(new AreaOwner(13), "Project Title", "TAC_activity_2", "Funding-2010-Actual Disbursements", "453,213", "Funding-2011-Actual Commitments", "999,888", "Totals-Actual Commitments", "999,888", "Totals-Actual Disbursements", "453,213"),
                    new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Funding-2013-Actual Disbursements", "545,000", "Totals-Actual Disbursements", "545,000"),
                    new ReportAreaForTests(new AreaOwner(26), "Project Title", "date-filters-activity", "Funding-2009-Actual Commitments", "100,000", "Funding-2010-Actual Disbursements", "60,000", "Funding-2012-Actual Commitments", "25,000", "Funding-2012-Actual Disbursements", "12,000", "Totals-Actual Commitments", "125,000", "Totals-Actual Disbursements", "72,000"),
                    new ReportAreaForTests(new AreaOwner(32), "Project Title", "crazy funding 1", "Funding-2013-Actual Commitments", "333,333", "Totals-Actual Commitments", "333,333"),
                    new ReportAreaForTests(new AreaOwner(33), "Project Title", "Activity with Zones", "Funding-2013-Actual Commitments", "570,000", "Totals-Actual Commitments", "570,000"),
                    new ReportAreaForTests(new AreaOwner(36), "Project Title", "Activity With Zones and Percentages", "Funding-2013-Actual Commitments", "890,000", "Totals-Actual Commitments", "890,000")      ));
        
        ReportSpecificationImpl spec = buildSpecification("amountUnits", Arrays.asList(ColumnConstants.PROJECT_TITLE), Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS), null, GroupingCriteria.GROUPING_YEARLY);
        spec.getOrCreateSettings().setUnitsOption(AmountsUnits.AMOUNTS_OPTION_UNITS);

        runNiTestCase(cor, spec, acts);
    }
    
    @Test
    public void testAmountThousands() {
        NiReportModel cor = new NiReportModel("amountThousands")
            .withHeaders(Arrays.asList(
                    "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 13))",
                    "(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 1, colSpan: 10));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 11, colSpan: 2))",
                    "(2009: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 1, colSpan: 2));(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 2));(2011: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 2));(2012: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 7, colSpan: 2));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 9, colSpan: 2))",
                    "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                  .withContents("Project Title", "", "Funding-2009-Actual Commitments", "100", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "636,53", "Funding-2011-Actual Commitments", "1,213,12", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "25", "Funding-2012-Actual Disbursements", "12", "Funding-2013-Actual Commitments", "1,793,33", "Funding-2013-Actual Disbursements", "545", "Totals-Actual Commitments", "3,131,45", "Totals-Actual Disbursements", "1,193,53")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(12), "Project Title", "TAC_activity_1", "Funding-2010-Actual Disbursements", "123,32", "Funding-2011-Actual Commitments", "213,23", "Totals-Actual Commitments", "213,23", "Totals-Actual Disbursements", "123,32"),
                    new ReportAreaForTests(new AreaOwner(13), "Project Title", "TAC_activity_2", "Funding-2010-Actual Disbursements", "453,21", "Funding-2011-Actual Commitments", "999,89", "Totals-Actual Commitments", "999,89", "Totals-Actual Disbursements", "453,21"),
                    new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Funding-2013-Actual Disbursements", "545", "Totals-Actual Disbursements", "545"),
                    new ReportAreaForTests(new AreaOwner(26), "Project Title", "date-filters-activity", "Funding-2009-Actual Commitments", "100", "Funding-2010-Actual Disbursements", "60", "Funding-2012-Actual Commitments", "25", "Funding-2012-Actual Disbursements", "12", "Totals-Actual Commitments", "125", "Totals-Actual Disbursements", "72"),
                    new ReportAreaForTests(new AreaOwner(32), "Project Title", "crazy funding 1", "Funding-2013-Actual Commitments", "333,33", "Totals-Actual Commitments", "333,33"),
                    new ReportAreaForTests(new AreaOwner(33), "Project Title", "Activity with Zones", "Funding-2013-Actual Commitments", "570", "Totals-Actual Commitments", "570"),
                    new ReportAreaForTests(new AreaOwner(36), "Project Title", "Activity With Zones and Percentages", "Funding-2013-Actual Commitments", "890", "Totals-Actual Commitments", "890")      ));

        
        ReportSpecificationImpl spec = buildSpecification("amountThousands", Arrays.asList(ColumnConstants.PROJECT_TITLE), Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS), null, GroupingCriteria.GROUPING_YEARLY);
        spec.getOrCreateSettings().setUnitsOption(AmountsUnits.AMOUNTS_OPTION_THOUSANDS);

        runNiTestCase(cor, spec, acts);
    }
    
    @Test
    public void testAmountMillions() {
        NiReportModel cor = new NiReportModel("amountMillions")
            .withHeaders(Arrays.asList(
                    "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 13))",
                    "(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 1, colSpan: 10));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 11, colSpan: 2))",
                    "(2009: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 1, colSpan: 2));(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 2));(2011: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 2));(2012: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 7, colSpan: 2));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 9, colSpan: 2))",
                    "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                  .withContents("Project Title", "", "Funding-2009-Actual Commitments", "0,1", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0,64", "Funding-2011-Actual Commitments", "1,21", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0,02", "Funding-2012-Actual Disbursements", "0,01", "Funding-2013-Actual Commitments", "1,79", "Funding-2013-Actual Disbursements", "0,54", "Totals-Actual Commitments", "3,13", "Totals-Actual Disbursements", "1,19")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(12), "Project Title", "TAC_activity_1", "Funding-2010-Actual Disbursements", "0,12", "Funding-2011-Actual Commitments", "0,21", "Totals-Actual Commitments", "0,21", "Totals-Actual Disbursements", "0,12"),
                    new ReportAreaForTests(new AreaOwner(13), "Project Title", "TAC_activity_2", "Funding-2010-Actual Disbursements", "0,45", "Funding-2011-Actual Commitments", "1", "Totals-Actual Commitments", "1", "Totals-Actual Disbursements", "0,45"),
                    new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Funding-2013-Actual Disbursements", "0,54", "Totals-Actual Disbursements", "0,54"),
                    new ReportAreaForTests(new AreaOwner(26), "Project Title", "date-filters-activity", "Funding-2009-Actual Commitments", "0,1", "Funding-2010-Actual Disbursements", "0,06", "Funding-2012-Actual Commitments", "0,02", "Funding-2012-Actual Disbursements", "0,01", "Totals-Actual Commitments", "0,12", "Totals-Actual Disbursements", "0,07"),
                    new ReportAreaForTests(new AreaOwner(32), "Project Title", "crazy funding 1", "Funding-2013-Actual Commitments", "0,33", "Totals-Actual Commitments", "0,33"),
                    new ReportAreaForTests(new AreaOwner(33), "Project Title", "Activity with Zones", "Funding-2013-Actual Commitments", "0,57", "Totals-Actual Commitments", "0,57"),
                    new ReportAreaForTests(new AreaOwner(36), "Project Title", "Activity With Zones and Percentages", "Funding-2013-Actual Commitments", "0,89", "Totals-Actual Commitments", "0,89")      ));

        
        ReportSpecificationImpl spec = buildSpecification("amountMillions", Arrays.asList(ColumnConstants.PROJECT_TITLE), Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS), null, GroupingCriteria.GROUPING_YEARLY);
        spec.getOrCreateSettings().setUnitsOption(AmountsUnits.AMOUNTS_OPTION_MILLIONS);

        runNiTestCase(cor, spec, acts);
    }
    
    @Test
    public void testAmountBillions() {
        NiReportModel cor = new NiReportModel("amountBillions")
            .withHeaders(Arrays.asList(
                    "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 13))",
                    "(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 1, colSpan: 10));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 11, colSpan: 2))",
                    "(2009: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 1, colSpan: 2));(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 2));(2011: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 2));(2012: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 7, colSpan: 2));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 9, colSpan: 2))",
                    "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                  .withContents("Project Title", "", "Funding-2009-Actual Commitments", "0,0001", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0,00064", "Funding-2011-Actual Commitments", "0,00121", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0,00002", "Funding-2012-Actual Disbursements", "0,00001", "Funding-2013-Actual Commitments", "0,00179", "Funding-2013-Actual Disbursements", "0,00054", "Totals-Actual Commitments", "0,00313", "Totals-Actual Disbursements", "0,00119")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(12), "Project Title", "TAC_activity_1", "Funding-2010-Actual Disbursements", "0,00012", "Funding-2011-Actual Commitments", "0,00021", "Totals-Actual Commitments", "0,00021", "Totals-Actual Disbursements", "0,00012"),
                    new ReportAreaForTests(new AreaOwner(13), "Project Title", "TAC_activity_2", "Funding-2010-Actual Disbursements", "0,00045", "Funding-2011-Actual Commitments", "0,001", "Totals-Actual Commitments", "0,001", "Totals-Actual Disbursements", "0,00045"),
                    new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Funding-2013-Actual Disbursements", "0,00054", "Totals-Actual Disbursements", "0,00054"),
                    new ReportAreaForTests(new AreaOwner(26), "Project Title", "date-filters-activity", "Funding-2009-Actual Commitments", "0,0001", "Funding-2010-Actual Disbursements", "0,00006", "Funding-2012-Actual Commitments", "0,00002", "Funding-2012-Actual Disbursements", "0,00001", "Totals-Actual Commitments", "0,00012", "Totals-Actual Disbursements", "0,00007"),
                    new ReportAreaForTests(new AreaOwner(32), "Project Title", "crazy funding 1", "Funding-2013-Actual Commitments", "0,00033", "Totals-Actual Commitments", "0,00033"),
                    new ReportAreaForTests(new AreaOwner(33), "Project Title", "Activity with Zones", "Funding-2013-Actual Commitments", "0,00057", "Totals-Actual Commitments", "0,00057"),
                    new ReportAreaForTests(new AreaOwner(36), "Project Title", "Activity With Zones and Percentages", "Funding-2013-Actual Commitments", "0,00089", "Totals-Actual Commitments", "0,00089")      ));

        
        ReportSpecificationImpl spec = buildSpecification("amountBillions", Arrays.asList(ColumnConstants.PROJECT_TITLE), Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS), null, GroupingCriteria.GROUPING_YEARLY);
        spec.getOrCreateSettings().setUnitsOption(AmountsUnits.AMOUNTS_OPTION_BILLIONS);
        
        DecimalFormat currencyFormat = (DecimalFormat) DecimalFormat.getNumberInstance();
        currencyFormat.setMaximumFractionDigits(5);
        currencyFormat.setMinimumFractionDigits(0);
        
        DecimalFormatSymbols custom = new DecimalFormatSymbols();
        custom.setDecimalSeparator(',');
        currencyFormat.setDecimalFormatSymbols(custom);
        
        spec.getOrCreateSettings().setCurrencyFormat(currencyFormat);

        runNiTestCase(cor, spec, acts);
    }
}
