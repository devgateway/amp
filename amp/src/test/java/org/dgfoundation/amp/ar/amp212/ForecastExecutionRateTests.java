package org.dgfoundation.amp.ar.amp212;

import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.newreports.AmpReportingTestCase;
import org.dgfoundation.amp.newreports.AreaOwner;
import org.dgfoundation.amp.newreports.ReportAreaForTests;
import org.dgfoundation.amp.nireports.NiPrecisionSetting;
import org.dgfoundation.amp.nireports.amp.ForecastExecutionRateBehaviour;
import org.dgfoundation.amp.nireports.output.nicells.NiFormulaicAmountCell;
import org.dgfoundation.amp.nireports.testcases.NiReportModel;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * testcases for "Forecast Execution Rate"
 * @author Dolghier Constantin
 *
 */
public class ForecastExecutionRateTests extends AmpReportingTestCase {
        
    ForecastExecutionRateBehaviour ferbeh = ForecastExecutionRateBehaviour.instance;
    
    final List<String> acts = Arrays.asList(
            "Activity with both MTEFs and Act.Comms",
            "activity with directed MTEFs",
            "activity with many MTEFs",
            "activity with pipeline MTEFs and act. disb",
            "Activity with planned disbursements",
            "Activity with Zones",
            "Activity With Zones and Percentages",
            "expenditure class",
            "date-filters-activity",
            "crazy funding 1",
            "date-filters-activity",
            "Eth Water",
            "pledged 2",
            "execution rate activity",
            "mtef activity 1",
            "mtef activity 2",
            "new activity with contracting",
            "pledged 2",
            "pledged education activity 1",
            "Proposed Project Cost 1 - USD",
            "Proposed Project Cost 2 - EUR",
            "Pure MTEF Project",
            "TAC_activity_1",
            "Test MTEF directed"
        );
    
    
    @Test
    public void testPlain() {
        NiReportModel cor = new NiReportModel("AMP-21240-forecast-execution-rate")
            .withHeaders(Arrays.asList(
                    "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 10))",
                    "(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 1, colSpan: 6));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 7, colSpan: 3))",
                    "(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 1, colSpan: 1));(2012: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 1));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 1));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 1));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 1));(2016: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 6, colSpan: 1))",
                    "(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Execution Rate: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Forecast Execution Rate: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                  .withContents("Project Title", "", "Funding-2010-Actual Disbursements", "327 098", "Funding-2012-Actual Disbursements", "12 000", "Funding-2013-Actual Disbursements", "580 000", "Funding-2014-Actual Disbursements", "580 200", "Funding-2015-Actual Disbursements", "115 570", "Funding-2016-Actual Disbursements", "253 700", "Totals-Actual Disbursements", "1 868 568", "Totals-Execution Rate", "2 013,54", "Totals-Forecast Execution Rate", "152,47")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(12), "Project Title", "TAC_activity_1", "Funding-2010-Actual Disbursements", "123 321", "Totals-Actual Disbursements", "123 321"),
                    new ReportAreaForTests(new AreaOwner(15), "Project Title", "Proposed Project Cost 1 - USD"),
                    new ReportAreaForTests(new AreaOwner(17), "Project Title", "Proposed Project Cost 2 - EUR"),
                    new ReportAreaForTests(new AreaOwner(18), "Project Title", "Test MTEF directed", "Funding-2010-Actual Disbursements", "143 777", "Totals-Actual Disbursements", "143 777", "Totals-Forecast Execution Rate", "66,87"),
                    new ReportAreaForTests(new AreaOwner(19), "Project Title", "Pure MTEF Project", "Totals-Forecast Execution Rate", "0"),
                    new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Funding-2013-Actual Disbursements", "545 000", "Totals-Actual Disbursements", "545 000"),
                    new ReportAreaForTests(new AreaOwner(25), "Project Title", "mtef activity 1", "Totals-Forecast Execution Rate", "0"),
                    new ReportAreaForTests(new AreaOwner(26), "Project Title", "date-filters-activity", "Funding-2010-Actual Disbursements", "60 000", "Funding-2012-Actual Disbursements", "12 000", "Totals-Actual Disbursements", "72 000"),
                    new ReportAreaForTests(new AreaOwner(27), "Project Title", "mtef activity 2", "Totals-Forecast Execution Rate", "0"),
                    new ReportAreaForTests(new AreaOwner(32), "Project Title", "crazy funding 1"),
                    new ReportAreaForTests(new AreaOwner(33), "Project Title", "Activity with Zones"),
                    new ReportAreaForTests(new AreaOwner(36), "Project Title", "Activity With Zones and Percentages"),
                    new ReportAreaForTests(new AreaOwner(46), "Project Title", "pledged education activity 1"),
                    new ReportAreaForTests(new AreaOwner(48), "Project Title", "pledged 2", "Funding-2014-Actual Disbursements", "450 000", "Totals-Actual Disbursements", "450 000"),
                    new ReportAreaForTests(new AreaOwner(53), "Project Title", "new activity with contracting"),
                    new ReportAreaForTests(new AreaOwner(69), "Project Title", "Activity with planned disbursements", "Funding-2014-Actual Disbursements", "200", "Funding-2015-Actual Disbursements", "570", "Totals-Actual Disbursements", "770", "Totals-Execution Rate", "96,25"),
                    new ReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms", "Totals-Forecast Execution Rate", "0"),
                    new ReportAreaForTests(new AreaOwner(73), "Project Title", "activity with directed MTEFs"),
                    new ReportAreaForTests(new AreaOwner(76), "Project Title", "activity with pipeline MTEFs and act. disb", "Funding-2013-Actual Disbursements", "35 000", "Funding-2014-Actual Disbursements", "75 000", "Totals-Actual Disbursements", "110 000", "Totals-Forecast Execution Rate", "129,41"),
                    new ReportAreaForTests(new AreaOwner(77), "Project Title", "execution rate activity", "Funding-2014-Actual Disbursements", "55 000", "Funding-2015-Actual Disbursements", "35 000", "Totals-Actual Disbursements", "90 000", "Totals-Execution Rate", "97,83"),
                    new ReportAreaForTests(new AreaOwner(78), "Project Title", "activity with many MTEFs", "Funding-2015-Actual Disbursements", "80 000", "Totals-Actual Disbursements", "80 000", "Totals-Forecast Execution Rate", "117,65"),
                    new ReportAreaForTests(new AreaOwner(87), "Project Title", "expenditure class", "Funding-2016-Actual Disbursements", "253 700", "Totals-Actual Disbursements", "253 700")      ));
        
        runNiTestCase(spec("AMP-21240-forecast-execution-rate"), "en", acts, cor);
    }

    @Test
    public void testByDonor() {
        NiReportModel cor = new NiReportModel("AMP-21240-forecast-execution-rate-by-donor")
            .withHeaders(Arrays.asList(
                    "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 10))",
                    "(Donor Agency: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 6));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 8, colSpan: 2))",
                    "(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 1));(2012: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 1));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 1));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 1));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 6, colSpan: 1));(2016: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 7, colSpan: 1))",
                    "(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Forecast Execution Rate: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                  .withContents("Donor Agency", "", "Project Title", "", "Funding-2010-Actual Disbursements", "327 098", "Funding-2012-Actual Disbursements", "12 000", "Funding-2013-Actual Disbursements", "580 000", "Funding-2014-Actual Disbursements", "580 200", "Funding-2015-Actual Disbursements", "115 570", "Funding-2016-Actual Disbursements", "253 700", "Totals-Actual Disbursements", "1 868 568", "Totals-Forecast Execution Rate", "152,47")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner("Donor Agency", "Finland", 21698))
                    .withContents("Project Title", "", "Funding-2010-Actual Disbursements", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Disbursements", "20 000", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Disbursements", "0", "Funding-2016-Actual Disbursements", "0", "Totals-Actual Disbursements", "20 000", "Totals-Forecast Execution Rate", "2,24", "Donor Agency", "Finland")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Funding-2013-Actual Disbursements", "20 000", "Totals-Actual Disbursements", "20 000"),
                      new ReportAreaForTests(new AreaOwner(27), "Project Title", "mtef activity 2", "Totals-Forecast Execution Rate", "0"),
                      new ReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms", "Totals-Forecast Execution Rate", "0"),
                      new ReportAreaForTests(new AreaOwner(78), "Project Title", "activity with many MTEFs", "Totals-Forecast Execution Rate", "0")        ),
                    new ReportAreaForTests(new AreaOwner("Donor Agency", "Ministry of Economy", 21700))
                    .withContents("Project Title", "", "Funding-2010-Actual Disbursements", "143 777", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Disbursements", "0", "Funding-2016-Actual Disbursements", "253 700", "Totals-Actual Disbursements", "397 477", "Totals-Forecast Execution Rate", "184,87", "Donor Agency", "Ministry of Economy")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner(18), "Project Title", "Test MTEF directed", "Funding-2010-Actual Disbursements", "143 777", "Totals-Actual Disbursements", "143 777", "Totals-Forecast Execution Rate", "66,87"),
                      new ReportAreaForTests(new AreaOwner(87), "Project Title", "expenditure class", "Funding-2016-Actual Disbursements", "253 700", "Totals-Actual Disbursements", "253 700")        ),
                    new ReportAreaForTests(new AreaOwner("Donor Agency", "Ministry of Finance", 21699))
                    .withContents("Project Title", "", "Funding-2010-Actual Disbursements", "60 000", "Funding-2012-Actual Disbursements", "12 000", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Disbursements", "55 000", "Funding-2015-Actual Disbursements", "35 000", "Funding-2016-Actual Disbursements", "0", "Totals-Actual Disbursements", "162 000", "Totals-Forecast Execution Rate", "478,05", "Donor Agency", "Ministry of Finance")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner(19), "Project Title", "Pure MTEF Project", "Totals-Forecast Execution Rate", "0"),
                      new ReportAreaForTests(new AreaOwner(26), "Project Title", "date-filters-activity", "Funding-2010-Actual Disbursements", "60 000", "Funding-2012-Actual Disbursements", "12 000", "Totals-Actual Disbursements", "72 000"),
                      new ReportAreaForTests(new AreaOwner(77), "Project Title", "execution rate activity", "Funding-2014-Actual Disbursements", "55 000", "Funding-2015-Actual Disbursements", "35 000", "Totals-Actual Disbursements", "90 000")        ),
                    new ReportAreaForTests(new AreaOwner("Donor Agency", "Norway", 21694))
                    .withContents("Project Title", "", "Funding-2010-Actual Disbursements", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Disbursements", "110 000", "Funding-2014-Actual Disbursements", "200", "Funding-2015-Actual Disbursements", "500", "Funding-2016-Actual Disbursements", "0", "Totals-Actual Disbursements", "110 700", "Donor Agency", "Norway")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Funding-2013-Actual Disbursements", "110 000", "Totals-Actual Disbursements", "110 000"),
                      new ReportAreaForTests(new AreaOwner(69), "Project Title", "Activity with planned disbursements", "Funding-2014-Actual Disbursements", "200", "Funding-2015-Actual Disbursements", "500", "Totals-Actual Disbursements", "700")        ),
                    new ReportAreaForTests(new AreaOwner("Donor Agency", "UNDP", 21695))
                    .withContents("Project Title", "", "Funding-2010-Actual Disbursements", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Disbursements", "35 000", "Funding-2014-Actual Disbursements", "75 000", "Funding-2015-Actual Disbursements", "0", "Funding-2016-Actual Disbursements", "0", "Totals-Actual Disbursements", "110 000", "Totals-Forecast Execution Rate", "12,58", "Donor Agency", "UNDP")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner(25), "Project Title", "mtef activity 1", "Totals-Forecast Execution Rate", "0"),
                      new ReportAreaForTests(new AreaOwner(76), "Project Title", "activity with pipeline MTEFs and act. disb", "Funding-2013-Actual Disbursements", "35 000", "Funding-2014-Actual Disbursements", "75 000", "Totals-Actual Disbursements", "110 000", "Totals-Forecast Execution Rate", "129,41")        ),
                    new ReportAreaForTests(new AreaOwner("Donor Agency", "USAID", 21696))
                    .withContents("Project Title", "", "Funding-2010-Actual Disbursements", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Disbursements", "415 000", "Funding-2014-Actual Disbursements", "450 000", "Funding-2015-Actual Disbursements", "80 070", "Funding-2016-Actual Disbursements", "0", "Totals-Actual Disbursements", "945 070", "Totals-Forecast Execution Rate", "7 269,77", "Donor Agency", "USAID")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Funding-2013-Actual Disbursements", "415 000", "Totals-Actual Disbursements", "415 000"),
                      new ReportAreaForTests(new AreaOwner(48), "Project Title", "pledged 2", "Funding-2014-Actual Disbursements", "450 000", "Totals-Actual Disbursements", "450 000"),
                      new ReportAreaForTests(new AreaOwner(69), "Project Title", "Activity with planned disbursements", "Funding-2015-Actual Disbursements", "70", "Totals-Actual Disbursements", "70"),
                      new ReportAreaForTests(new AreaOwner(78), "Project Title", "activity with many MTEFs", "Funding-2015-Actual Disbursements", "80 000", "Totals-Actual Disbursements", "80 000", "Totals-Forecast Execution Rate", "615,38")        ),
                    new ReportAreaForTests(new AreaOwner("Donor Agency", "World Bank", 21697)).withContents("Project Title", "", "Funding-2010-Actual Disbursements", "123 321", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Disbursements", "0", "Funding-2016-Actual Disbursements", "0", "Totals-Actual Disbursements", "123 321", "Donor Agency", "World Bank")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner(12), "Project Title", "TAC_activity_1", "Funding-2010-Actual Disbursements", "123 321", "Totals-Actual Disbursements", "123 321")        )      ));
        
        runNiTestCase(spec("AMP-21240-forecast-execution-rate-by-donor"), "en", acts, cor);
    }

    @Test
    public void testFlatTotalsOnly() {
        NiReportModel cor = new NiReportModel("AMP-21240-forecast-execution-rate-totals-only")
            .withHeaders(Arrays.asList(
                    "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 0, colSpan: 4))",
                    "(Project Title: (startRow: 1, rowSpan: 2, totalRowSpan: 2, colStart: 0, colSpan: 1));(Totals: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 1, colSpan: 3))",
                    "(Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1));(Execution Rate: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Forecast Execution Rate: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                  .withContents("Project Title", "", "Totals-Actual Disbursements", "1 868 568", "Totals-Execution Rate", "2 013,54", "Totals-Forecast Execution Rate", "152,47")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(12), "Project Title", "TAC_activity_1", "Totals-Actual Disbursements", "123 321"),
                    new ReportAreaForTests(new AreaOwner(15), "Project Title", "Proposed Project Cost 1 - USD"),
                    new ReportAreaForTests(new AreaOwner(17), "Project Title", "Proposed Project Cost 2 - EUR"),
                    new ReportAreaForTests(new AreaOwner(18), "Project Title", "Test MTEF directed", "Totals-Actual Disbursements", "143 777", "Totals-Forecast Execution Rate", "66,87"),
                    new ReportAreaForTests(new AreaOwner(19), "Project Title", "Pure MTEF Project", "Totals-Forecast Execution Rate", "0"),
                    new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Totals-Actual Disbursements", "545 000"),
                    new ReportAreaForTests(new AreaOwner(25), "Project Title", "mtef activity 1", "Totals-Forecast Execution Rate", "0"),
                    new ReportAreaForTests(new AreaOwner(26), "Project Title", "date-filters-activity", "Totals-Actual Disbursements", "72 000"),
                    new ReportAreaForTests(new AreaOwner(27), "Project Title", "mtef activity 2", "Totals-Forecast Execution Rate", "0"),
                    new ReportAreaForTests(new AreaOwner(32), "Project Title", "crazy funding 1"),
                    new ReportAreaForTests(new AreaOwner(33), "Project Title", "Activity with Zones"),
                    new ReportAreaForTests(new AreaOwner(36), "Project Title", "Activity With Zones and Percentages"),
                    new ReportAreaForTests(new AreaOwner(46), "Project Title", "pledged education activity 1"),
                    new ReportAreaForTests(new AreaOwner(48), "Project Title", "pledged 2", "Totals-Actual Disbursements", "450 000"),
                    new ReportAreaForTests(new AreaOwner(53), "Project Title", "new activity with contracting"),
                    new ReportAreaForTests(new AreaOwner(69), "Project Title", "Activity with planned disbursements", "Totals-Actual Disbursements", "770", "Totals-Execution Rate", "96,25"),
                    new ReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms", "Totals-Forecast Execution Rate", "0"),
                    new ReportAreaForTests(new AreaOwner(73), "Project Title", "activity with directed MTEFs"),
                    new ReportAreaForTests(new AreaOwner(76), "Project Title", "activity with pipeline MTEFs and act. disb", "Totals-Actual Disbursements", "110 000", "Totals-Forecast Execution Rate", "129,41"),
                    new ReportAreaForTests(new AreaOwner(77), "Project Title", "execution rate activity", "Totals-Actual Disbursements", "90 000", "Totals-Execution Rate", "97,83"),
                    new ReportAreaForTests(new AreaOwner(78), "Project Title", "activity with many MTEFs", "Totals-Actual Disbursements", "80 000", "Totals-Forecast Execution Rate", "117,65"),
                    new ReportAreaForTests(new AreaOwner(87), "Project Title", "expenditure class", "Totals-Actual Disbursements", "253 700")      ));
        
        runNiTestCase(spec("AMP-21240-forecast-execution-rate-totals-only"), "en", acts, cor);
    }

    @Test
    public void testFlatQuarterly() {
        NiReportModel cor = new NiReportModel("AMP-21240-forecast-execution-rate-quarterly")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 5, colStart: 0, colSpan: 22))",
                        "(Project Title: (startRow: 1, rowSpan: 4, totalRowSpan: 4, colStart: 0, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 4, colStart: 1, colSpan: 18));(Totals: (startRow: 1, rowSpan: 3, totalRowSpan: 4, colStart: 19, colSpan: 3))",
                        "(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 1, colSpan: 3));(2012: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 4, colSpan: 2));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 6, colSpan: 3));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 9, colSpan: 4));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 13, colSpan: 3));(2016: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 16, colSpan: 3))",
                        "(Q1: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 1, colSpan: 1));(Q2: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 1));(Total: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 1));(Q4: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 1));(Total: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 1));(Q3: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 6, colSpan: 1));(Q4: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 7, colSpan: 1));(Total: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 8, colSpan: 1));(Q1: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 9, colSpan: 1));(Q2: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 10, colSpan: 1));(Q4: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 11, colSpan: 1));(Total: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 12, colSpan: 1));(Q2: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 13, colSpan: 1));(Q4: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 14, colSpan: 1));(Total: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 15, colSpan: 1));(Q1: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 16, colSpan: 1));(Q2: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 17, colSpan: 1));(Total: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 18, colSpan: 1))",
                        "(Actual Disbursements: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1));(Actual Disbursements: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Disbursements: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Disbursements: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Disbursements: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Disbursements: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Disbursements: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Disbursements: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Disbursements: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Disbursements: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Actual Disbursements: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1));(Actual Disbursements: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1));(Actual Disbursements: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1));(Actual Disbursements: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 14, colSpan: 1));(Actual Disbursements: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 15, colSpan: 1));(Actual Disbursements: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 16, colSpan: 1));(Actual Disbursements: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 17, colSpan: 1));(Actual Disbursements: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 18, colSpan: 1));(Actual Disbursements: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 19, colSpan: 1));(Execution Rate: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 20, colSpan: 1));(Forecast Execution Rate: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 21, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                        .withContents("Project Title", "", "Funding-2010-Q1-Actual Disbursements", "143 777", "Funding-2010-Q2-Actual Disbursements", "183 321", "Funding-2010-Total-Actual Disbursements", "327 098", "Funding-2012-Q4-Actual Disbursements", "12 000", "Funding-2012-Total-Actual Disbursements", "12 000", "Funding-2013-Q3-Actual Disbursements", "545 000", "Funding-2013-Q4-Actual Disbursements", "35 000", "Funding-2013-Total-Actual Disbursements", "580 000", "Funding-2014-Q1-Actual Disbursements", "200", "Funding-2014-Q2-Actual Disbursements", "495 000", "Funding-2014-Q4-Actual Disbursements", "85 000", "Funding-2014-Total-Actual Disbursements", "580 200", "Funding-2015-Q2-Actual Disbursements", "570", "Funding-2015-Q4-Actual Disbursements", "115 000", "Funding-2015-Total-Actual Disbursements", "115 570", "Funding-2016-Q1-Actual Disbursements", "87 500", "Funding-2016-Q2-Actual Disbursements", "166 200", "Funding-2016-Total-Actual Disbursements", "253 700", "Totals-Actual Disbursements", "1 868 568", "Totals-Execution Rate", "2 013,54", "Totals-Forecast Execution Rate", "152,47")
                        .withChildren(
                                new ReportAreaForTests(new AreaOwner(12), "Project Title", "TAC_activity_1", "Funding-2010-Q2-Actual Disbursements", "123 321", "Funding-2010-Total-Actual Disbursements", "123 321", "Totals-Actual Disbursements", "123 321"),
                                new ReportAreaForTests(new AreaOwner(15), "Project Title", "Proposed Project Cost 1 - USD"),
                                new ReportAreaForTests(new AreaOwner(17), "Project Title", "Proposed Project Cost 2 - EUR"),
                                new ReportAreaForTests(new AreaOwner(18), "Project Title", "Test MTEF directed", "Funding-2010-Q1-Actual Disbursements", "143 777", "Funding-2010-Total-Actual Disbursements", "143 777", "Totals-Actual Disbursements", "143 777", "Totals-Forecast Execution Rate", "66,87"),
                                new ReportAreaForTests(new AreaOwner(19), "Project Title", "Pure MTEF Project", "Totals-Forecast Execution Rate", "0"),
                                new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Funding-2013-Q3-Actual Disbursements", "545 000", "Funding-2013-Total-Actual Disbursements", "545 000", "Totals-Actual Disbursements", "545 000"),
                                new ReportAreaForTests(new AreaOwner(25), "Project Title", "mtef activity 1", "Totals-Forecast Execution Rate", "0"),
                                new ReportAreaForTests(new AreaOwner(26), "Project Title", "date-filters-activity", "Funding-2010-Q2-Actual Disbursements", "60 000", "Funding-2010-Total-Actual Disbursements", "60 000", "Funding-2012-Q4-Actual Disbursements", "12 000", "Funding-2012-Total-Actual Disbursements", "12 000", "Totals-Actual Disbursements", "72 000"),
                                new ReportAreaForTests(new AreaOwner(27), "Project Title", "mtef activity 2", "Totals-Forecast Execution Rate", "0"),
                                new ReportAreaForTests(new AreaOwner(32), "Project Title", "crazy funding 1"),
                                new ReportAreaForTests(new AreaOwner(33), "Project Title", "Activity with Zones"),
                                new ReportAreaForTests(new AreaOwner(36), "Project Title", "Activity With Zones and Percentages"),
                                new ReportAreaForTests(new AreaOwner(46), "Project Title", "pledged education activity 1"),
                                new ReportAreaForTests(new AreaOwner(48), "Project Title", "pledged 2", "Funding-2014-Q2-Actual Disbursements", "450 000", "Funding-2014-Total-Actual Disbursements", "450 000", "Totals-Actual Disbursements", "450 000"),
                                new ReportAreaForTests(new AreaOwner(53), "Project Title", "new activity with contracting"),
                                new ReportAreaForTests(new AreaOwner(69), "Project Title", "Activity with planned disbursements", "Funding-2014-Q1-Actual Disbursements", "200", "Funding-2014-Total-Actual Disbursements", "200", "Funding-2015-Q2-Actual Disbursements", "570", "Funding-2015-Total-Actual Disbursements", "570", "Totals-Actual Disbursements", "770", "Totals-Execution Rate", "96,25"),
                                new ReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms", "Totals-Forecast Execution Rate", "0"),
                                new ReportAreaForTests(new AreaOwner(73), "Project Title", "activity with directed MTEFs"),
                                new ReportAreaForTests(new AreaOwner(76), "Project Title", "activity with pipeline MTEFs and act. disb", "Funding-2013-Q4-Actual Disbursements", "35 000", "Funding-2013-Total-Actual Disbursements", "35 000", "Funding-2014-Q2-Actual Disbursements", "45 000", "Funding-2014-Q4-Actual Disbursements", "30 000", "Funding-2014-Total-Actual Disbursements", "75 000", "Totals-Actual Disbursements", "110 000", "Totals-Forecast Execution Rate", "129,41"),
                                new ReportAreaForTests(new AreaOwner(77), "Project Title", "execution rate activity", "Funding-2014-Q4-Actual Disbursements", "55 000", "Funding-2014-Total-Actual Disbursements", "55 000", "Funding-2015-Q4-Actual Disbursements", "35 000", "Funding-2015-Total-Actual Disbursements", "35 000", "Totals-Actual Disbursements", "90 000", "Totals-Execution Rate", "97,83"),
                                new ReportAreaForTests(new AreaOwner(78), "Project Title", "activity with many MTEFs", "Funding-2015-Q4-Actual Disbursements", "80 000", "Funding-2015-Total-Actual Disbursements", "80 000", "Totals-Actual Disbursements", "80 000", "Totals-Forecast Execution Rate", "117,65"),
                                new ReportAreaForTests(new AreaOwner(87), "Project Title", "expenditure class", "Funding-2016-Q1-Actual Disbursements", "87 500", "Funding-2016-Q2-Actual Disbursements", "166 200", "Funding-2016-Total-Actual Disbursements", "253 700", "Totals-Actual Disbursements", "253 700")      ));
        
        runNiTestCase(spec("AMP-21240-forecast-execution-rate-quarterly"), "en", acts, cor);
    }

    @Test
    public void testByMoP() {
        NiReportModel cor = new NiReportModel("AMP-21240-forecast-execution-rate-by-mop")
        .withHeaders(Arrays.asList(
                "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 10))",
                "(Mode of Payment: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 6));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 8, colSpan: 2))",
                "(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 1));(2012: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 1));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 1));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 1));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 6, colSpan: 1));(2016: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 7, colSpan: 1))",
                "(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Forecast Execution Rate: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1))"))
            .withWarnings(Arrays.asList())
            .withBody(      new ReportAreaForTests(null)
              .withContents("Mode of Payment", "", "Project Title", "", "Funding-2010-Actual Disbursements", "327 098", "Funding-2012-Actual Disbursements", "12 000", "Funding-2013-Actual Disbursements", "580 000", "Funding-2014-Actual Disbursements", "580 200", "Funding-2015-Actual Disbursements", "115 570", "Funding-2016-Actual Disbursements", "253 700", "Totals-Actual Disbursements", "1 868 568", "Totals-Forecast Execution Rate", "152,47")
              .withChildren(
                new ReportAreaForTests(new AreaOwner("Mode of Payment", "Cash", 2093)).withContents("Project Title", "", "Funding-2010-Actual Disbursements", "143 777", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Disbursements", "0", "Funding-2016-Actual Disbursements", "0", "Totals-Actual Disbursements", "143 777", "Totals-Forecast Execution Rate", "66,87", "Mode of Payment", "Cash")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner(18), "Project Title", "Test MTEF directed", "Funding-2010-Actual Disbursements", "143 777", "Totals-Actual Disbursements", "143 777", "Totals-Forecast Execution Rate", "66,87")        ),
                new ReportAreaForTests(new AreaOwner("Mode of Payment", "Direct payment", 2094))
                .withContents("Project Title", "", "Funding-2010-Actual Disbursements", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Disbursements", "35 000", "Funding-2014-Actual Disbursements", "75 000", "Funding-2015-Actual Disbursements", "0", "Funding-2016-Actual Disbursements", "0", "Totals-Actual Disbursements", "110 000", "Totals-Forecast Execution Rate", "10,39", "Mode of Payment", "Direct payment")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner(27), "Project Title", "mtef activity 2", "Totals-Forecast Execution Rate", "0"),
                  new ReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms", "Totals-Forecast Execution Rate", "0"),
                  new ReportAreaForTests(new AreaOwner(76), "Project Title", "activity with pipeline MTEFs and act. disb", "Funding-2013-Actual Disbursements", "35 000", "Funding-2014-Actual Disbursements", "75 000", "Totals-Actual Disbursements", "110 000", "Totals-Forecast Execution Rate", "129,41")        ),
                new ReportAreaForTests(new AreaOwner("Mode of Payment", "Mode of Payment: Undefined", -999999999))
                .withContents("Project Title", "", "Funding-2010-Actual Disbursements", "183 321", "Funding-2012-Actual Disbursements", "12 000", "Funding-2013-Actual Disbursements", "545 000", "Funding-2014-Actual Disbursements", "505 200", "Funding-2015-Actual Disbursements", "115 570", "Funding-2016-Actual Disbursements", "253 700", "Totals-Actual Disbursements", "1 614 791", "Totals-Forecast Execution Rate", "1 584,87", "Mode of Payment", "Mode of Payment: Undefined")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner(12), "Project Title", "TAC_activity_1", "Funding-2010-Actual Disbursements", "123 321", "Totals-Actual Disbursements", "123 321"),
                  new ReportAreaForTests(new AreaOwner(19), "Project Title", "Pure MTEF Project", "Totals-Forecast Execution Rate", "0"),
                  new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Funding-2013-Actual Disbursements", "545 000", "Totals-Actual Disbursements", "545 000"),
                  new ReportAreaForTests(new AreaOwner(25), "Project Title", "mtef activity 1", "Totals-Forecast Execution Rate", "0"),
                  new ReportAreaForTests(new AreaOwner(26), "Project Title", "date-filters-activity", "Funding-2010-Actual Disbursements", "60 000", "Funding-2012-Actual Disbursements", "12 000", "Totals-Actual Disbursements", "72 000"),
                  new ReportAreaForTests(new AreaOwner(48), "Project Title", "pledged 2", "Funding-2014-Actual Disbursements", "450 000", "Totals-Actual Disbursements", "450 000"),
                  new ReportAreaForTests(new AreaOwner(69), "Project Title", "Activity with planned disbursements", "Funding-2014-Actual Disbursements", "200", "Funding-2015-Actual Disbursements", "570", "Totals-Actual Disbursements", "770"),
                  new ReportAreaForTests(new AreaOwner(77), "Project Title", "execution rate activity", "Funding-2014-Actual Disbursements", "55 000", "Funding-2015-Actual Disbursements", "35 000", "Totals-Actual Disbursements", "90 000"),
                  new ReportAreaForTests(new AreaOwner(78), "Project Title", "activity with many MTEFs", "Funding-2015-Actual Disbursements", "80 000", "Totals-Actual Disbursements", "80 000", "Totals-Forecast Execution Rate", "117,65"),
                  new ReportAreaForTests(new AreaOwner(87), "Project Title", "expenditure class", "Funding-2016-Actual Disbursements", "253 700", "Totals-Actual Disbursements", "253 700")        )      ));

        
        runNiTestCase(spec("AMP-21240-forecast-execution-rate-by-mop"), "en", acts, cor);
    }

    protected void checkCalculatedValue(Double expectedValue, Object...z) {
        Map<String, BigDecimal> vals = new HashMap<>();
        for(int i = 0; i < z.length / 2; i ++) {
            String name = z[2 * i].toString();
            BigDecimal value = BigDecimal.valueOf((int) z[2 * i + 1]);
            vals.put(name, value);
        }
        NiFormulaicAmountCell cell = ferbeh.buildCell(vals, NiPrecisionSetting.IDENTITY_PRECISION_SETTING);
        assertBigDecimalEquals(expectedValue == null ? null : BigDecimal.valueOf(expectedValue), cell.amount);
    }
    
    @Test
    public void testBehaviourNoMtefs() {
        checkCalculatedValue(null, MeasureConstants.ACTUAL_DISBURSEMENTS, 2);
    }

    @Test
    public void testBehaviourNoDisbursements() {
        checkCalculatedValue(0.0, "pipe2010", 2);
    }

    @Test
    public void testBehaviourOnlyPipe() {
        checkCalculatedValue(200.0, "pipe2010", 2, "pipe2011", 5, MeasureConstants.ACTUAL_DISBURSEMENTS, 14);
    }

    @Test
    public void testBehaviourPipeMasksProjection() {
        checkCalculatedValue(200.0, "pipe2010", 2, "pipe2011", 5, "proj2010", 300, MeasureConstants.ACTUAL_DISBURSEMENTS, 14);
    }

    @Test
    public void testBehaviourPipeAndProjection() {
        checkCalculatedValue(140.0, "pipe2010", 2, "pipe2011", 5, "proj2012", 3, MeasureConstants.ACTUAL_DISBURSEMENTS, 14);
        
        checkCalculatedValue(140.0, "pipe2010", 2, "pipe2011", 5, "proj2012", 3, "proj2011", 500, MeasureConstants.ACTUAL_DISBURSEMENTS, 14);
    }
}
