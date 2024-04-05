package org.dgfoundation.amp.ar.legacy;

import org.dgfoundation.amp.nireports.testcases.ColumnReportDataModel;
import org.dgfoundation.amp.nireports.testcases.GroupColumnModel;
import org.dgfoundation.amp.nireports.testcases.GroupReportModel;
import org.dgfoundation.amp.nireports.testcases.SimpleColumnModel;
import org.dgfoundation.amp.testutils.ReportsTestCase;
import org.junit.Ignore;
import org.junit.Test;

import static org.dgfoundation.amp.testutils.ReportTestingUtils.MUST_BE_EMPTY;

/**
 * testcases for new features added into the old reports engine in AMP 2.11
 * @author Constantin Dolghier
 */
public class OldReportsNewFeaturesTests extends ReportsTestCase {
    
    public static String[] activities = new String[] {"Eth Water", "pledged 2", "activity with directed MTEFs", "Activity with both MTEFs and Act.Comms"};
    public static String[] mtefActivities = new String[] {"TAC_activity_1", "Test MTEF directed", "Pure MTEF Project", "mtef activity 1", "Activity with both MTEFs and Act.Comms"};
    
    @Test
    public void testRealCommsDisbsByRegion() {
        GroupReportModel cor = GroupReportModel.withColumnReports("AMP-21355-try-real-mtef-column-1",
            ColumnReportDataModel.withColumns("AMP-21355-try-real-mtef-column-1",
                    SimpleColumnModel.withContents("Project Title", "Activity with both MTEFs and Act.Comms", "Activity with both MTEFs and Act.Comms", "pledged 2", "pledged 2", "Eth Water", "Eth Water", "activity with directed MTEFs", "activity with directed MTEFs").setIsPledge(false), 
                    GroupColumnModel.withSubColumns("Funding",
                        GroupColumnModel.withSubColumns("2011",
                            SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(false), 
                            SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false), 
                            GroupColumnModel.withSubColumns("Real MTEFs",
                                SimpleColumnModel.withContents("EXEC-BENF", "activity with directed MTEFs", "50 000").setIsPledge(false), 
                                SimpleColumnModel.withContents("IMPL-EXEC", "activity with directed MTEFs", "110 500").setIsPledge(false))), 
                        GroupColumnModel.withSubColumns("2013",
                            SimpleColumnModel.withContents("Actual Commitments", "pledged 2", "2 670 000").setIsPledge(false), 
                            SimpleColumnModel.withContents("Actual Disbursements", "Eth Water", "545 000").setIsPledge(false), 
                            SimpleColumnModel.withContents("Real MTEFs", MUST_BE_EMPTY).setIsPledge(false)), 
                        GroupColumnModel.withSubColumns("2014",
                            SimpleColumnModel.withContents("Actual Commitments", "pledged 2", "4 400 000").setIsPledge(false), 
                            SimpleColumnModel.withContents("Actual Disbursements", "pledged 2", "450 000").setIsPledge(false), 
                            SimpleColumnModel.withContents("Real MTEFs", MUST_BE_EMPTY).setIsPledge(false)), 
                        GroupColumnModel.withSubColumns("2015",
                            SimpleColumnModel.withContents("Actual Commitments", "Activity with both MTEFs and Act.Comms", "888 000", "activity with directed MTEFs", "123 456").setIsPledge(false), 
                            SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false), 
                            SimpleColumnModel.withContents("Real MTEFs", MUST_BE_EMPTY).setIsPledge(false))), 
                    GroupColumnModel.withSubColumns("Total Costs",
                        SimpleColumnModel.withContents("Actual Commitments", "Activity with both MTEFs and Act.Comms", "888 000", "pledged 2", "7 070 000", "activity with directed MTEFs", "123 456").setIsPledge(false), 
                        SimpleColumnModel.withContents("Actual Disbursements", "pledged 2", "450 000", "Eth Water", "545 000").setIsPledge(false), 
                        GroupColumnModel.withSubColumns("Real MTEFs",
                            SimpleColumnModel.withContents("EXEC-BENF", "activity with directed MTEFs", "50 000").setIsPledge(false), 
                            SimpleColumnModel.withContents("IMPL-EXEC", "activity with directed MTEFs", "110 500").setIsPledge(false))))
                .withTrailCells(null, "0", "0", "50 000", "110 500", "2 670 000", "545 000", "0", "4 400 000", "450 000", "0", "1 011 456", "0", "0", "8 081 456", "995 000", "50 000", "110 500"))
            .withTrailCells(null, "0", "0", "50 000", "110 500", "2 670 000", "545 000", "0", "4 400 000", "450 000", "0", "1 011 456", "0", "0", "8 081 456", "995 000", "50 000", "110 500")
            .withPositionDigest(true,
                "(line 0:RHLC Project Title: (startRow: 0, rowSpan: 4, totalRowSpan: 4, colStart: 0, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 1, colSpan: 13), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 4, colStart: 14, colSpan: 4))",
                "(line 1:RHLC 2011: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 1, colSpan: 4), RHLC 2013: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 5, colSpan: 3), RHLC 2014: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 8, colSpan: 3), RHLC 2015: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 11, colSpan: 3))",
                "(line 2:RHLC Actual Commitments: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colStart: 1, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colStart: 2, colSpan: 1), RHLC Real MTEFs: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 2), RHLC Actual Commitments: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colStart: 5, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colStart: 6, colSpan: 1), RHLC Real MTEFs: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colStart: 7, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colStart: 8, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colStart: 9, colSpan: 1), RHLC Real MTEFs: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colStart: 10, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colStart: 11, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colStart: 12, colSpan: 1), RHLC Real MTEFs: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colStart: 13, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colStart: 14, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colStart: 15, colSpan: 1), RHLC Real MTEFs: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 16, colSpan: 2))",
                "(line 3:RHLC EXEC-BENF: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1), RHLC IMPL-EXEC: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1), RHLC EXEC-BENF: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 16, colSpan: 1), RHLC IMPL-EXEC: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 17, colSpan: 1))");

        runReportTest("Real MTEF alongside Actual Commitments and Actual Disbursements", "AMP-21355-try-real-mtef-column-1", activities, 
            cor, null, "en");
    }

    @Test
    public void testRealMtefAndMtefColumns() {
        GroupReportModel cor = GroupReportModel.withColumnReports("AMP-21355-real-mtef-and-mtef-columns",
            ColumnReportDataModel.withColumns("AMP-21355-real-mtef-and-mtef-columns",
                    SimpleColumnModel.withContents("Project Title", "Activity with both MTEFs and Act.Comms", "Activity with both MTEFs and Act.Comms", "pledged 2", "pledged 2", "Eth Water", "Eth Water", "activity with directed MTEFs", "activity with directed MTEFs").setIsPledge(false), 
                    SimpleColumnModel.withContents("MTEF 2012", "Activity with both MTEFs and Act.Comms", "150 000").setIsPledge(false), 
                    GroupColumnModel.withSubColumns("Funding",
                        GroupColumnModel.withSubColumns("2011",
                            SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(false), 
                            GroupColumnModel.withSubColumns("Real MTEFs",
                                SimpleColumnModel.withContents("EXEC-BENF", "activity with directed MTEFs", "50 000").setIsPledge(false), 
                                SimpleColumnModel.withContents("IMPL-EXEC", "activity with directed MTEFs", "110 500").setIsPledge(false))), 
                        GroupColumnModel.withSubColumns("2013",
                            SimpleColumnModel.withContents("Actual Commitments", "pledged 2", "2 670 000").setIsPledge(false), 
                            SimpleColumnModel.withContents("Real MTEFs", MUST_BE_EMPTY).setIsPledge(false)), 
                        GroupColumnModel.withSubColumns("2014",
                            SimpleColumnModel.withContents("Actual Commitments", "pledged 2", "4 400 000").setIsPledge(false), 
                            SimpleColumnModel.withContents("Real MTEFs", MUST_BE_EMPTY).setIsPledge(false)), 
                        GroupColumnModel.withSubColumns("2015",
                            SimpleColumnModel.withContents("Actual Commitments", "Activity with both MTEFs and Act.Comms", "888 000", "activity with directed MTEFs", "123 456").setIsPledge(false), 
                            SimpleColumnModel.withContents("Real MTEFs", MUST_BE_EMPTY).setIsPledge(false))), 
                    GroupColumnModel.withSubColumns("Total Costs",
                        SimpleColumnModel.withContents("Actual Commitments", "Activity with both MTEFs and Act.Comms", "888 000", "pledged 2", "7 070 000", "activity with directed MTEFs", "123 456").setIsPledge(false), 
                        GroupColumnModel.withSubColumns("Real MTEFs",
                            SimpleColumnModel.withContents("EXEC-BENF", "activity with directed MTEFs", "50 000").setIsPledge(false), 
                            SimpleColumnModel.withContents("IMPL-EXEC", "activity with directed MTEFs", "110 500").setIsPledge(false))))
                .withTrailCells(null, "150 000", "0", "50 000", "110 500", "2 670 000", "0", "4 400 000", "0", "1 011 456", "0", "8 081 456", "50 000", "110 500"))
            .withTrailCells(null, "150 000", "0", "50 000", "110 500", "2 670 000", "0", "4 400 000", "0", "1 011 456", "0", "8 081 456", "50 000", "110 500")
            .withPositionDigest(true,
                "(line 0:RHLC Project Title: (startRow: 0, rowSpan: 4, totalRowSpan: 4, colStart: 0, colSpan: 1), RHLC MTEF 2012: (startRow: 0, rowSpan: 4, totalRowSpan: 4, colStart: 1, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 2, colSpan: 9), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 4, colStart: 11, colSpan: 3))",
                "(line 1:RHLC 2011: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 3), RHLC 2013: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 5, colSpan: 2), RHLC 2014: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 7, colSpan: 2), RHLC 2015: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 9, colSpan: 2))",
                "(line 2:RHLC Actual Commitments: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colStart: 2, colSpan: 1), RHLC Real MTEFs: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 2), RHLC Actual Commitments: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colStart: 5, colSpan: 1), RHLC Real MTEFs: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colStart: 6, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colStart: 7, colSpan: 1), RHLC Real MTEFs: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colStart: 8, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colStart: 9, colSpan: 1), RHLC Real MTEFs: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colStart: 10, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colStart: 11, colSpan: 1), RHLC Real MTEFs: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 12, colSpan: 2))",
                "(line 3:RHLC EXEC-BENF: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1), RHLC IMPL-EXEC: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1), RHLC EXEC-BENF: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1), RHLC IMPL-EXEC: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1))");
        runReportTest("MTEF columns alongside Real MTEF columns for different years", "AMP-21355-real-mtef-and-mtef-columns", activities, 
            cor, null, "en");
    }
    
    @Test
    public void testRealMtefAndMtefColumnsAndRealMtefColumns() {
        GroupReportModel cor = GroupReportModel.withColumnReports("AMP-21355-real-mtef-and-mtef-columns-and-real-mtef-measure",
            ColumnReportDataModel.withColumns("AMP-21355-real-mtef-and-mtef-columns-and-real-mtef-measure",
                    SimpleColumnModel.withContents("Project Title", "Activity with both MTEFs and Act.Comms", "Activity with both MTEFs and Act.Comms", "pledged 2", "pledged 2", "Eth Water", "Eth Water", "activity with directed MTEFs", "activity with directed MTEFs").setIsPledge(false), 
                    SimpleColumnModel.withContents("MTEF 2012", "Activity with both MTEFs and Act.Comms", "150 000").setIsPledge(false), 
                    GroupColumnModel.withSubColumns("Funding",
                        GroupColumnModel.withSubColumns("2011",
                            SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(false), 
                            GroupColumnModel.withSubColumns("Real MTEFs",
                                SimpleColumnModel.withContents("EXEC-BENF", "activity with directed MTEFs", "50 000").setIsPledge(false), 
                                SimpleColumnModel.withContents("IMPL-EXEC", "activity with directed MTEFs", "110 500").setIsPledge(false))), 
                        GroupColumnModel.withSubColumns("2013",
                            SimpleColumnModel.withContents("Actual Commitments", "pledged 2", "2 670 000").setIsPledge(false), 
                            SimpleColumnModel.withContents("Real MTEFs", MUST_BE_EMPTY).setIsPledge(false)), 
                        GroupColumnModel.withSubColumns("2014",
                            SimpleColumnModel.withContents("Actual Commitments", "pledged 2", "4 400 000").setIsPledge(false), 
                            SimpleColumnModel.withContents("Real MTEFs", MUST_BE_EMPTY).setIsPledge(false)), 
                        GroupColumnModel.withSubColumns("2015",
                            SimpleColumnModel.withContents("Actual Commitments", "Activity with both MTEFs and Act.Comms", "888 000", "activity with directed MTEFs", "123 456").setIsPledge(false), 
                            SimpleColumnModel.withContents("Real MTEFs", MUST_BE_EMPTY).setIsPledge(false))), 
                    GroupColumnModel.withSubColumns("Total Costs",
                        SimpleColumnModel.withContents("Actual Commitments", "Activity with both MTEFs and Act.Comms", "888 000", "pledged 2", "7 070 000", "activity with directed MTEFs", "123 456").setIsPledge(false), 
                        GroupColumnModel.withSubColumns("Real MTEFs",
                            SimpleColumnModel.withContents("EXEC-BENF", "activity with directed MTEFs", "50 000").setIsPledge(false), 
                            SimpleColumnModel.withContents("IMPL-EXEC", "activity with directed MTEFs", "110 500").setIsPledge(false))))
                .withTrailCells(null, "150 000", "0", "50 000", "110 500", "2 670 000", "0", "4 400 000", "0", "1 011 456", "0", "8 081 456", "50 000", "110 500"))
            .withTrailCells(null, "150 000", "0", "50 000", "110 500", "2 670 000", "0", "4 400 000", "0", "1 011 456", "0", "8 081 456", "50 000", "110 500")
            .withPositionDigest(true,
                "(line 0:RHLC Project Title: (startRow: 0, rowSpan: 4, totalRowSpan: 4, colStart: 0, colSpan: 1), RHLC MTEF 2012: (startRow: 0, rowSpan: 4, totalRowSpan: 4, colStart: 1, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 2, colSpan: 9), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 4, colStart: 11, colSpan: 3))",
                "(line 1:RHLC 2011: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 3), RHLC 2013: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 5, colSpan: 2), RHLC 2014: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 7, colSpan: 2), RHLC 2015: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 9, colSpan: 2))",
                "(line 2:RHLC Actual Commitments: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colStart: 2, colSpan: 1), RHLC Real MTEFs: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 2), RHLC Actual Commitments: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colStart: 5, colSpan: 1), RHLC Real MTEFs: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colStart: 6, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colStart: 7, colSpan: 1), RHLC Real MTEFs: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colStart: 8, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colStart: 9, colSpan: 1), RHLC Real MTEFs: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colStart: 10, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colStart: 11, colSpan: 1), RHLC Real MTEFs: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 12, colSpan: 2))",
                "(line 3:RHLC EXEC-BENF: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1), RHLC IMPL-EXEC: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1), RHLC EXEC-BENF: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1), RHLC IMPL-EXEC: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1))");
        
        runReportTest("Real MTEF columns, MTEF columns, Real Mtefs measure", "AMP-21355-real-mtef-and-mtef-columns-and-real-mtef-measure", activities, 
            cor, null, "en");
    }
    
    @Test
    public void testRealMtefColumnsAndMeasure() {
        GroupReportModel cor = GroupReportModel.withColumnReports("AMP-21355-real-mtef-columns-and-measure",
            ColumnReportDataModel.withColumns("AMP-21355-real-mtef-columns-and-measure",
                    SimpleColumnModel.withContents("Project Title", "Activity with both MTEFs and Act.Comms", "Activity with both MTEFs and Act.Comms", "pledged 2", "pledged 2", "Eth Water", "Eth Water", "activity with directed MTEFs", "activity with directed MTEFs").setIsPledge(false), 
                    GroupColumnModel.withSubColumns("Funding",
                        GroupColumnModel.withSubColumns("2011",
                            GroupColumnModel.withSubColumns("Real MTEFs",
                                SimpleColumnModel.withContents("EXEC-BENF", "activity with directed MTEFs", "50 000").setIsPledge(false), 
                                SimpleColumnModel.withContents("IMPL-EXEC", "activity with directed MTEFs", "110 500").setIsPledge(false)))), 
                    GroupColumnModel.withSubColumns("Total Costs",
                        GroupColumnModel.withSubColumns("Real MTEFs",
                            SimpleColumnModel.withContents("EXEC-BENF", "activity with directed MTEFs", "50 000").setIsPledge(false), 
                            SimpleColumnModel.withContents("IMPL-EXEC", "activity with directed MTEFs", "110 500").setIsPledge(false))))
                .withTrailCells(null, "50 000", "110 500", "50 000", "110 500"))
            .withTrailCells(null, "50 000", "110 500", "50 000", "110 500")
            .withPositionDigest(true,
                "(line 0:RHLC Project Title: (startRow: 0, rowSpan: 4, totalRowSpan: 4, colStart: 0, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 1, colSpan: 2), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 4, colStart: 3, colSpan: 2))",
                "(line 1:RHLC 2011: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 1, colSpan: 2))",
                "(line 2:RHLC Real MTEFs: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 1, colSpan: 2), RHLC Real MTEFs: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 2))",
                "(line 3:RHLC EXEC-BENF: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1), RHLC IMPL-EXEC: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1), RHLC EXEC-BENF: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1), RHLC IMPL-EXEC: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1))");
        
        runReportTest("Real MTEF columns, Real Mtefs measure", "AMP-21355-real-mtef-columns-and-measure", activities, 
            cor, null, "en");
    }
    
    @Test
    @Ignore
    public void testMtefProjectionsAsColumns() {
        GroupReportModel cor = GroupReportModel.withColumnReports("AMP-21275-split-mtef-projections-as-columns",
                ColumnReportDataModel.withColumns("AMP-21275-split-mtef-projections-as-columns",
                        SimpleColumnModel.withContents("Project Title", "Activity with both MTEFs and Act.Comms", "Activity with both MTEFs and Act.Comms", "Pure MTEF Project", "Pure MTEF Project", "Test MTEF directed", "Test MTEF directed", "mtef activity 1", "mtef activity 1", "TAC_activity_1", "TAC_activity_1").setIsPledge(false), 
                        SimpleColumnModel.withContents("MTEF 2011", "Pure MTEF Project", "33 888", "Activity with both MTEFs and Act.Comms", "700 000", "Test MTEF directed", "150 000", "mtef activity 1", "789 123").setIsPledge(false), 
                        SimpleColumnModel.withContents("Pipeline MTEF Projections 2011", "Activity with both MTEFs and Act.Comms", "700 000", "Pure MTEF Project", "33 888", "Test MTEF directed", "150 000").setIsPledge(false), 
                        SimpleColumnModel.withContents("Pipeline MTEF Projections 2012", "Test MTEF directed", "65 000").setIsPledge(false), 
                        SimpleColumnModel.withContents("Pipeline MTEF Projections 2013", MUST_BE_EMPTY).setIsPledge(false), 
                        SimpleColumnModel.withContents("Projection MTEF Projections 2011", "mtef activity 1", "789 123").setIsPledge(false), 
                        SimpleColumnModel.withContents("Projection MTEF Projections 2012", "Activity with both MTEFs and Act.Comms", "150 000").setIsPledge(false), 
                        SimpleColumnModel.withContents("Projection MTEF Projections 2013", MUST_BE_EMPTY).setIsPledge(false), 
                        GroupColumnModel.withSubColumns("Funding",
                            GroupColumnModel.withSubColumns("2011",
                                SimpleColumnModel.withContents("Actual Commitments", "TAC_activity_1", "213 231").setIsPledge(false)), 
                            GroupColumnModel.withSubColumns("2015",
                                SimpleColumnModel.withContents("Actual Commitments", "Activity with both MTEFs and Act.Comms", "888 000").setIsPledge(false))), 
                        GroupColumnModel.withSubColumns("Total Costs",
                            SimpleColumnModel.withContents("Actual Commitments", "Activity with both MTEFs and Act.Comms", "888 000", "TAC_activity_1", "213 231").setIsPledge(false)))
                    .withTrailCells(null, "1 673 011", "883 888", "65 000", "0", "789 123", "150 000", "0", "213 231", "888 000", "1 101 231"))
                .withTrailCells(null, "1 673 011", "883 888", "65 000", "0", "789 123", "150 000", "0", "213 231", "888 000", "1 101 231")
                .withPositionDigest(true,
                    "(line 0:RHLC Project Title: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1), RHLC MTEF 2011: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1), RHLC Pipeline MTEF Projections 2011: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1), RHLC Pipeline MTEF Projections 2012: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 3, colSpan: 1), RHLC Pipeline MTEF Projections 2013: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 4, colSpan: 1), RHLC Projection MTEF Projections 2011: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 5, colSpan: 1), RHLC Projection MTEF Projections 2012: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 6, colSpan: 1), RHLC Projection MTEF Projections 2013: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 7, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 8, colSpan: 2), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 3, colStart: 10, colSpan: 1))",
                    "(line 1:RHLC 2011: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 8, colSpan: 1), RHLC 2015: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 9, colSpan: 1))",
                    "(line 2:RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1))");
        
        runReportTest("AMP-21275-split-mtef-projections-as-columns", "AMP-21275-split-mtef-projections-as-columns", mtefActivities, cor, null, "en");
    }
    
    @Test
    @Ignore
    public void testMtefProjectionsAsColumnsNoMtefs() {
        GroupReportModel cor = GroupReportModel.withColumnReports("AMP-21275-split-mtef-projections-as-columns-small",
                ColumnReportDataModel.withColumns("AMP-21275-split-mtef-projections-as-columns-small",
                        SimpleColumnModel.withContents("Project Title", "Activity with both MTEFs and Act.Comms", "Activity with both MTEFs and Act.Comms", "Pure MTEF Project", "Pure MTEF Project", "Test MTEF directed", "Test MTEF directed", "mtef activity 1", "mtef activity 1", "TAC_activity_1", "TAC_activity_1").setIsPledge(false), 
                        SimpleColumnModel.withContents("Pipeline MTEF Projections 2012", "Test MTEF directed", "65 000").setIsPledge(false), 
                        SimpleColumnModel.withContents("Projection MTEF Projections 2012", "Activity with both MTEFs and Act.Comms", "150 000").setIsPledge(false), 
                        GroupColumnModel.withSubColumns("Funding",
                            GroupColumnModel.withSubColumns("2011",
                                SimpleColumnModel.withContents("Actual Commitments", "TAC_activity_1", "213 231").setIsPledge(false)), 
                            GroupColumnModel.withSubColumns("2015",
                                SimpleColumnModel.withContents("Actual Commitments", "Activity with both MTEFs and Act.Comms", "888 000").setIsPledge(false))), 
                        GroupColumnModel.withSubColumns("Total Costs",
                            SimpleColumnModel.withContents("Actual Commitments", "Activity with both MTEFs and Act.Comms", "888 000", "TAC_activity_1", "213 231").setIsPledge(false)))
                    .withTrailCells(null, "65 000", "150 000", "213 231", "888 000", "1 101 231"))
                .withTrailCells(null, "65 000", "150 000", "213 231", "888 000", "1 101 231")
                .withPositionDigest(true,
                    "(line 0:RHLC Project Title: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1), RHLC Pipeline MTEF Projections 2012: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1), RHLC Projection MTEF Projections 2012: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 3, colSpan: 2), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 3, colStart: 5, colSpan: 1))",
                    "(line 1:RHLC 2011: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 1), RHLC 2015: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 1))",
                    "(line 2:RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1))");
        
        runReportTest("AMP-21275-split-mtef-projections-as-columns-small", "AMP-21275-split-mtef-projections-as-columns-small", mtefActivities, cor, null, "en");
    }
    
    @Test
    @Ignore
    public void testMtefProjectionsAllAsColumns() {
        GroupReportModel cor = GroupReportModel.withColumnReports("AMP-21275-all-plain-mtefs",
                ColumnReportDataModel.withColumns("AMP-21275-all-plain-mtefs",
                        SimpleColumnModel.withContents("Project Title", "Activity with both MTEFs and Act.Comms", "Activity with both MTEFs and Act.Comms", "Pure MTEF Project", "Pure MTEF Project", "Test MTEF directed", "Test MTEF directed", "mtef activity 1", "mtef activity 1", "TAC_activity_1", "TAC_activity_1").setIsPledge(false), 
                        SimpleColumnModel.withContents("MTEF 2011", "Pure MTEF Project", "33 888", "Activity with both MTEFs and Act.Comms", "700 000", "Test MTEF directed", "150 000", "mtef activity 1", "789 123").setIsPledge(false), 
                        SimpleColumnModel.withContents("Pipeline MTEF Projections 2011", "Activity with both MTEFs and Act.Comms", "700 000", "Pure MTEF Project", "33 888", "Test MTEF directed", "150 000").setIsPledge(false), 
                        SimpleColumnModel.withContents("Projection MTEF Projections 2011", "mtef activity 1", "789 123").setIsPledge(false), 
                        SimpleColumnModel.withContents("MTEF 2012", "Activity with both MTEFs and Act.Comms", "150 000", "Test MTEF directed", "65 000").setIsPledge(false), 
                        SimpleColumnModel.withContents("Pipeline MTEF Projections 2012", "Test MTEF directed", "65 000").setIsPledge(false), 
                        SimpleColumnModel.withContents("Projection MTEF Projections 2012", "Activity with both MTEFs and Act.Comms", "150 000").setIsPledge(false), 
                        SimpleColumnModel.withContents("MTEF 2013", MUST_BE_EMPTY).setIsPledge(false), 
                        SimpleColumnModel.withContents("Pipeline MTEF Projections 2013", MUST_BE_EMPTY).setIsPledge(false), 
                        SimpleColumnModel.withContents("Projection MTEF Projections 2013", MUST_BE_EMPTY).setIsPledge(false), 
                        GroupColumnModel.withSubColumns("Funding",
                            GroupColumnModel.withSubColumns("2011",
                                SimpleColumnModel.withContents("Actual Commitments", "TAC_activity_1", "213 231").setIsPledge(false)), 
                            GroupColumnModel.withSubColumns("2015",
                                SimpleColumnModel.withContents("Actual Commitments", "Activity with both MTEFs and Act.Comms", "888 000").setIsPledge(false))), 
                        GroupColumnModel.withSubColumns("Total Costs",
                            SimpleColumnModel.withContents("Actual Commitments", "Activity with both MTEFs and Act.Comms", "888 000", "TAC_activity_1", "213 231").setIsPledge(false)))
                    .withTrailCells(null, "1 673 011", "883 888", "789 123", "215 000", "65 000", "150 000", "0", "0", "0", "213 231", "888 000", "1 101 231"))
                .withTrailCells(null, "1 673 011", "883 888", "789 123", "215 000", "65 000", "150 000", "0", "0", "0", "213 231", "888 000", "1 101 231")
                .withPositionDigest(true,
                    "(line 0:RHLC Project Title: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1), RHLC MTEF 2011: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1), RHLC Pipeline MTEF Projections 2011: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1), RHLC Projection MTEF Projections 2011: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 3, colSpan: 1), RHLC MTEF 2012: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 4, colSpan: 1), RHLC Pipeline MTEF Projections 2012: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 5, colSpan: 1), RHLC Projection MTEF Projections 2012: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 6, colSpan: 1), RHLC MTEF 2013: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 7, colSpan: 1), RHLC Pipeline MTEF Projections 2013: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 8, colSpan: 1), RHLC Projection MTEF Projections 2013: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 9, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 10, colSpan: 2), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 3, colStart: 12, colSpan: 1))",
                    "(line 1:RHLC 2011: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 10, colSpan: 1), RHLC 2015: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 11, colSpan: 1))",
                    "(line 2:RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1))");
        
        runReportTest("AMP-21275-all-plain-mtefs", "AMP-21275-all-plain-mtefs", mtefActivities, cor, null, "en");
    }
    
    @Test
    @Ignore
    public void testMtefProjectionsAllAsColumnsRare() {
        GroupReportModel cor = GroupReportModel.withColumnReports("AMP-21275-all-plain-mtefs-rare",
                ColumnReportDataModel.withColumns("AMP-21275-all-plain-mtefs-rare",
                        SimpleColumnModel.withContents("Project Title", "Activity with both MTEFs and Act.Comms", "Activity with both MTEFs and Act.Comms", "Pure MTEF Project", "Pure MTEF Project", "Test MTEF directed", "Test MTEF directed", "mtef activity 1", "mtef activity 1", "TAC_activity_1", "TAC_activity_1").setIsPledge(false), 
                        SimpleColumnModel.withContents("MTEF 2011", "Pure MTEF Project", "33 888", "Activity with both MTEFs and Act.Comms", "700 000", "Test MTEF directed", "150 000", "mtef activity 1", "789 123").setIsPledge(false), 
                        SimpleColumnModel.withContents("Pipeline MTEF Projections 2011", "Activity with both MTEFs and Act.Comms", "700 000", "Pure MTEF Project", "33 888", "Test MTEF directed", "150 000").setIsPledge(false), 
                        SimpleColumnModel.withContents("MTEF 2012", "Activity with both MTEFs and Act.Comms", "150 000", "Test MTEF directed", "65 000").setIsPledge(false), 
                        SimpleColumnModel.withContents("Projection MTEF Projections 2012", "Activity with both MTEFs and Act.Comms", "150 000").setIsPledge(false), 
                        SimpleColumnModel.withContents("MTEF 2013", MUST_BE_EMPTY).setIsPledge(false), 
                        GroupColumnModel.withSubColumns("Funding",
                            GroupColumnModel.withSubColumns("2011",
                                SimpleColumnModel.withContents("Actual Commitments", "TAC_activity_1", "213 231").setIsPledge(false)), 
                            GroupColumnModel.withSubColumns("2015",
                                SimpleColumnModel.withContents("Actual Commitments", "Activity with both MTEFs and Act.Comms", "888 000").setIsPledge(false))), 
                        GroupColumnModel.withSubColumns("Total Costs",
                            SimpleColumnModel.withContents("Actual Commitments", "Activity with both MTEFs and Act.Comms", "888 000", "TAC_activity_1", "213 231").setIsPledge(false)))
                    .withTrailCells(null, "1 673 011", "883 888", "215 000", "150 000", "0", "213 231", "888 000", "1 101 231"))
                .withTrailCells(null, "1 673 011", "883 888", "215 000", "150 000", "0", "213 231", "888 000", "1 101 231")
                .withPositionDigest(true,
                    "(line 0:RHLC Project Title: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1), RHLC MTEF 2011: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1), RHLC Pipeline MTEF Projections 2011: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1), RHLC MTEF 2012: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 3, colSpan: 1), RHLC Projection MTEF Projections 2012: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 4, colSpan: 1), RHLC MTEF 2013: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 5, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 6, colSpan: 2), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 3, colStart: 8, colSpan: 1))",
                    "(line 1:RHLC 2011: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 6, colSpan: 1), RHLC 2015: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 7, colSpan: 1))",
                    "(line 2:RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1))");
        
        runReportTest("AMP-21275-all-plain-mtefs-rare", "AMP-21275-all-plain-mtefs-rare", mtefActivities, cor, null, "en");
    }
    
    @Test
    @Ignore
    public void testMtefProjectionsAllAsColumnsVeryRare() {
        GroupReportModel cor = GroupReportModel.withColumnReports("AMP-21275-all-plain-mtefs-very-rare",
                ColumnReportDataModel.withColumns("AMP-21275-all-plain-mtefs-very-rare",
                        SimpleColumnModel.withContents("Project Title", "Activity with both MTEFs and Act.Comms", "Activity with both MTEFs and Act.Comms", "Pure MTEF Project", "Pure MTEF Project", "Test MTEF directed", "Test MTEF directed", "mtef activity 1", "mtef activity 1", "TAC_activity_1", "TAC_activity_1").setIsPledge(false), 
                        SimpleColumnModel.withContents("Pipeline MTEF Projections 2011", "Activity with both MTEFs and Act.Comms", "700 000", "Pure MTEF Project", "33 888", "Test MTEF directed", "150 000").setIsPledge(false), 
                        SimpleColumnModel.withContents("MTEF 2012", "Activity with both MTEFs and Act.Comms", "150 000", "Test MTEF directed", "65 000").setIsPledge(false), 
                        SimpleColumnModel.withContents("Projection MTEF Projections 2012", "Activity with both MTEFs and Act.Comms", "150 000").setIsPledge(false), 
                        GroupColumnModel.withSubColumns("Funding",
                            GroupColumnModel.withSubColumns("2011",
                                SimpleColumnModel.withContents("Actual Commitments", "TAC_activity_1", "213 231").setIsPledge(false)), 
                            GroupColumnModel.withSubColumns("2015",
                                SimpleColumnModel.withContents("Actual Commitments", "Activity with both MTEFs and Act.Comms", "888 000").setIsPledge(false))), 
                        GroupColumnModel.withSubColumns("Total Costs",
                            SimpleColumnModel.withContents("Actual Commitments", "Activity with both MTEFs and Act.Comms", "888 000", "TAC_activity_1", "213 231").setIsPledge(false)))
                    .withTrailCells(null, "883 888", "215 000", "150 000", "213 231", "888 000", "1 101 231"))
                .withTrailCells(null, "883 888", "215 000", "150 000", "213 231", "888 000", "1 101 231")
                .withPositionDigest(true,
                    "(line 0:RHLC Project Title: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1), RHLC Pipeline MTEF Projections 2011: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1), RHLC MTEF 2012: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1), RHLC Projection MTEF Projections 2012: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 3, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 4, colSpan: 2), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 3, colStart: 6, colSpan: 1))",
                    "(line 1:RHLC 2011: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 1), RHLC 2015: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 1))",
                    "(line 2:RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1))");
        
        runReportTest("AMP-21275-all-plain-mtefs-very-rare", "AMP-21275-all-plain-mtefs-very-rare", mtefActivities, cor, null, "en");
    }
    
    public static String[] expenditureClassActivities = new String[] {"execution rate activity", "ptc activity 1", "activity_with_expenditure_class_1", "another_activity_with_expenditure_class"};
}
