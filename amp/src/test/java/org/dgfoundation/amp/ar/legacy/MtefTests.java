package org.dgfoundation.amp.ar.legacy;

import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.nireports.testcases.ColumnReportDataModel;
import org.dgfoundation.amp.nireports.testcases.GroupColumnModel;
import org.dgfoundation.amp.nireports.testcases.GroupReportModel;
import org.dgfoundation.amp.nireports.testcases.SimpleColumnModel;
import org.dgfoundation.amp.testutils.*;
import org.digijava.module.aim.dbentity.AmpReports;
import org.junit.Test;

import static org.dgfoundation.amp.testutils.ReportTestingUtils.NULL_PLACEHOLDER;
import static org.dgfoundation.amp.testutils.ReportTestingUtils.MUST_BE_EMPTY;

/**
 * testcase for Directed Disbursements (AMP-15337)
 * this can only be run IFF amp has been started standalone. For this, AllTests.initialize() should have been run previously (typically called by AllTests.suite() as part of the JUnit discovery process)
 * @author Dolghier Constantin
 *
 */
public class MtefTests extends ReportsTestCase {

    /**
     * a flat report containing RealDisbursements of a single activity
     */
    @Test
    public void testAllMtef()
    {
        // ========================= one more report ===============================
        GroupReportModel fddr_correct = GroupReportModel.withColumnReports("AMP-15794", 
                ColumnReportDataModel.withColumns("AMP-15794", 
                        SimpleColumnModel.withContents("Project Title", NULL_PLACEHOLDER),
                        SimpleColumnModel.withContents("MTEF 2011", "mtef activity 1", "789 123"),
                        SimpleColumnModel.withContents("MTEF 2013", "mtef activity 2", "123 654"),
                        GroupColumnModel.withSubColumns("Funding", 
                                GroupColumnModel.withSubColumns("2013", 
                                        SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY),
                                        SimpleColumnModel.withContents("Actual Disbursements", "Eth Water", "545 000")
                                                )),
                        GroupColumnModel.withSubColumns("Total Costs", 
                                SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY/*"Eth Water", "0", "mtef activity 1", "0", "mtef activity 2", "0"*/),
                                SimpleColumnModel.withContents("Actual Disbursements", "Eth Water", "545 000")
                        )));
        
        runReportTest("all Mtef report", "AMP-15794", new String[] {"Eth Water", "mtef activity 1", "mtef activity 2"}, fddr_correct);
    }

    @Test
    public void testPlainMtef()
    {
        GroupReportModel fddr_correct = GroupReportModel.withColumnReports("AMP-16100-flat-mtefs", 
                ColumnReportDataModel.withColumns("AMP-16100-flat-mtefs", 
                        SimpleColumnModel.withContents("Project Title", NULL_PLACEHOLDER),
                        SimpleColumnModel.withContents("MTEF 2011", "Test MTEF directed", "150 000"),
                        SimpleColumnModel.withContents("MTEF 2012", "Test MTEF directed", "65 000"),
                        SimpleColumnModel.withContents("MTEF 2013", MUST_BE_EMPTY),
                        GroupColumnModel.withSubColumns("Funding", 
                                GroupColumnModel.withSubColumns("2010", 
                                        SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY),
                                        SimpleColumnModel.withContents("Actual Disbursements", "Test MTEF directed", "143 777")
                                                )),
                        GroupColumnModel.withSubColumns("Total Costs", 
                                SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY),
                                SimpleColumnModel.withContents("Actual Disbursements", "Test MTEF directed", "143 777")
                        ))).withTrailCells(null, "150 000", "65 000", "0", "0", "143 777", "0", "143 777");
        
        runReportTest("all Mtef, implicit filter by Donor", "AMP-16100-flat-mtefs", new String[] {"Test MTEF directed"}, fddr_correct);
    }

    @Test
    public void testMtefByDonorAgency()
    {
        GroupReportModel fddr_correct =
                GroupReportModel.withGroupReports("AMP-16100-mtef-by-donor-agency", 
                GroupReportModel.withColumnReports("AMP-16100-mtef-by-donor-agency", 
                ColumnReportDataModel.withColumns("Donor Agency: Ministry of Economy", 
                        SimpleColumnModel.withContents("Project Title", NULL_PLACEHOLDER),
                        SimpleColumnModel.withContents("MTEF 2011", "Test MTEF directed", "150 000"),
                        SimpleColumnModel.withContents("MTEF 2012", "Test MTEF directed", "65 000"),
                        SimpleColumnModel.withContents("MTEF 2013", MUST_BE_EMPTY),
                        GroupColumnModel.withSubColumns("Funding", 
                                GroupColumnModel.withSubColumns("2010", 
                                        SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY),
                                        SimpleColumnModel.withContents("Actual Disbursements", "Test MTEF directed", "143 777")
                                                )),
                        GroupColumnModel.withSubColumns("Total Costs", 
                                SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY),
                                SimpleColumnModel.withContents("Actual Disbursements", "Test MTEF directed", "143 777")
                        ))).withTrailCells(null, "150 000", "65 000", "0", "0", "143 777", "0", "143 777")
                    ).withTrailCells(null, "150 000", "65 000", "0", "0", "143 777", "0", "143 777");
        
        runReportTest("all Mtef, by Donor", "AMP-16100-mtef-by-donor-agency", new String[] {"Test MTEF directed"}, fddr_correct);
    }

    @Test
    public void testMtefByImplementingAgency()
    {
        GroupReportModel fddr_correct =
                GroupReportModel.withGroupReports("AMP-16100-mtef-projection-by-impl", 
                GroupReportModel.withColumnReports("AMP-16100-mtef-projection-by-impl", 
                ColumnReportDataModel.withColumns("Implementing Agency: USAID", 
                        SimpleColumnModel.withContents("Project Title", NULL_PLACEHOLDER),
                        SimpleColumnModel.withContents("MTEF 2011", "Test MTEF directed", "25 400"),
                        SimpleColumnModel.withContents("MTEF 2012", "Test MTEF directed", "62 500"),
                        SimpleColumnModel.withContents("MTEF 2013", MUST_BE_EMPTY),
                        GroupColumnModel.withSubColumns("Funding", 
                                GroupColumnModel.withSubColumns("2010", 
                                        SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY),
                                        SimpleColumnModel.withContents("Actual Disbursements", "Test MTEF directed", "143 777")
                                                )),
                        GroupColumnModel.withSubColumns("Total Costs", 
                                SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY),
                                SimpleColumnModel.withContents("Actual Disbursements", "Test MTEF directed", "143 777")
                        ))).withTrailCells(null, "25 400", "62 500", "0", "0", "143 777", "0", "143 777")
                    ).withTrailCells(null, "25 400", "62 500", "0", "0", "143 777", "0", "143 777");
        
        runReportTest("all Mtef, by Implementing Agency", "AMP-16100-mtef-projection-by-impl", new String[] {"Test MTEF directed"}, fddr_correct);
    }

    @Test
    public void testMtefByExecutingAgency()
    {
        GroupReportModel fddr_correct =
                GroupReportModel.withGroupReports("AMP-16100-mtef-projections-by-exec", 
                GroupReportModel.withColumnReports("AMP-16100-mtef-projections-by-exec", 
                ColumnReportDataModel.withColumns("Executing Agency: Water Foundation", 
                        SimpleColumnModel.withContents("Project Title", NULL_PLACEHOLDER),
                        SimpleColumnModel.withContents("MTEF 2011", MUST_BE_EMPTY),
                        SimpleColumnModel.withContents("MTEF 2012", MUST_BE_EMPTY),
                        SimpleColumnModel.withContents("MTEF 2013", MUST_BE_EMPTY),
                        GroupColumnModel.withSubColumns("Funding", 
                                GroupColumnModel.withSubColumns("2010", 
                                        SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY),
                                        SimpleColumnModel.withContents("Actual Disbursements", "Test MTEF directed", "143 777")
                                                )),
                        GroupColumnModel.withSubColumns("Total Costs", 
                                SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY),
                                SimpleColumnModel.withContents("Actual Disbursements", "Test MTEF directed", "143 777")
                        ))).withTrailCells(null, "0", "0", "0", "0", "143 777", "0", "143 777")
                    ).withTrailCells(null, "0", "0", "0", "0", "143 777", "0", "143 777");
        
        runReportTest("all Mtef, by Executing Agency", "AMP-16100-mtef-projections-by-exec", new String[] {"Test MTEF directed"}, fddr_correct);
    }

    @Test
    public void testPurePlainMtef()
    {
        GroupReportModel fddr_correct = GroupReportModel.withColumnReports("AMP-16100-flat-mtefs", 
                ColumnReportDataModel.withColumns("AMP-16100-flat-mtefs", 
                        SimpleColumnModel.withContents("Project Title", NULL_PLACEHOLDER),
                        SimpleColumnModel.withContents("MTEF 2011", "Pure MTEF Project", "33 888"),
                        SimpleColumnModel.withContents("MTEF 2012", MUST_BE_EMPTY),
                        SimpleColumnModel.withContents("MTEF 2013", MUST_BE_EMPTY),
                        /*GroupColumnModel.withSubColumns("Funding", 
                                GroupColumnModel.withSubColumns("2010", 
                                        SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY),
                                        SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY)
                                                )),*/
                        GroupColumnModel.withSubColumns("Total Costs", 
                                SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY),
                                SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY)
                        ))).withTrailCells(null, "33 888", "0", "0", "0", "0");
        
        runReportTest("pure Mtef, implicit filter by Donor", "AMP-16100-flat-mtefs", new String[] {"Pure MTEF Project"}, fddr_correct);
    }

    @Test
    public void testPureMtefByDonorAgency()
    {
        GroupReportModel fddr_correct =
                GroupReportModel.withGroupReports("AMP-16100-mtef-by-donor-agency", 
                GroupReportModel.withColumnReports("AMP-16100-mtef-by-donor-agency", 
                ColumnReportDataModel.withColumns("Donor Agency: Ministry of Finance", 
                        SimpleColumnModel.withContents("Project Title", NULL_PLACEHOLDER),
                        SimpleColumnModel.withContents("MTEF 2011", "Pure MTEF Project", "33 888"),
                        SimpleColumnModel.withContents("MTEF 2012", MUST_BE_EMPTY),
                        SimpleColumnModel.withContents("MTEF 2013", MUST_BE_EMPTY),
                        /*GroupColumnModel.withSubColumns("Funding", 
                                GroupColumnModel.withSubColumns("2010", 
                                        SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY),
                                        SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY)
                                                )),*/
                        GroupColumnModel.withSubColumns("Total Costs", 
                                SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY),
                                SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY)
                        ))).withTrailCells(null, "33 888", "0", "0", "0", "0")
                    ).withTrailCells(null, "33 888", "0", "0", "0", "0");
        
        runReportTest("pure Mtef, by Donor", "AMP-16100-mtef-by-donor-agency", new String[] {"Pure MTEF Project"}, fddr_correct);
    }

    @Test
    public void testPureMtefByImplementingAgency()
    {
        GroupReportModel fddr_correct = GroupReportModel.empty("AMP-16100-mtef-projection-by-impl");
        
        runReportTest("pure Mtef, by Implementing Agency", "AMP-16100-mtef-projection-by-impl", new String[] {"Pure MTEF Project"}, fddr_correct);
    }

    @Test
    public void testPureMtefByExecutingAgency()
    {
        GroupReportModel fddr_correct =
                GroupReportModel.withGroupReports("AMP-16100-mtef-projections-by-exec", 
                GroupReportModel.withColumnReports("AMP-16100-mtef-projections-by-exec", 
                ColumnReportDataModel.withColumns("Executing Agency: Ministry of Economy", 
                        SimpleColumnModel.withContents("Project Title", NULL_PLACEHOLDER),
                        SimpleColumnModel.withContents("MTEF 2011", MUST_BE_EMPTY),
                        SimpleColumnModel.withContents("MTEF 2012", "Pure MTEF Project", "55 333"),
                        SimpleColumnModel.withContents("MTEF 2013", MUST_BE_EMPTY),
                        /*GroupColumnModel.withSubColumns("Funding", 
                                GroupColumnModel.withSubColumns("2010", 
                                        SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY),
                                        SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY)
                                                )),*/
                        GroupColumnModel.withSubColumns("Total Costs", 
                                SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY),
                                SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY)
                        ))).withTrailCells(null, "0", "55 333", "0", "0", "0")
                    ).withTrailCells(null, "0", "55 333", "0", "0", "0");
        
        runReportTest("pure Mtef, by Executing Agency", "AMP-16100-mtef-projections-by-exec", new String[] {"Pure MTEF Project"}, fddr_correct);
    }

    @Test
    public void testPurePlainMtefEUR()
    {
        GroupReportModel fddr_correct = GroupReportModel.withColumnReports("AMP-16100-flat-mtefs-eur", 
                ColumnReportDataModel.withColumns("AMP-16100-flat-mtefs-eur", 
                        SimpleColumnModel.withContents("Project Title", NULL_PLACEHOLDER),
                        SimpleColumnModel.withContents("MTEF 2011", "Pure MTEF Project", "25 311"),
                        SimpleColumnModel.withContents("MTEF 2012", MUST_BE_EMPTY),
                        SimpleColumnModel.withContents("MTEF 2013", MUST_BE_EMPTY),
                        /*GroupColumnModel.withSubColumns("Funding", 
                                GroupColumnModel.withSubColumns("2010", 
                                        SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY),
                                        SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY)
                                                )),*/
                        GroupColumnModel.withSubColumns("Total Costs", 
                                SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY),
                                SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY)
                        ))).withTrailCells(null, "25 311", "0", "0", "0", "0");
        
        runReportTest("pure Mtef, implicit filter by Donor, EUR", "AMP-16100-flat-mtefs-eur", new String[] {"Pure MTEF Project"}, fddr_correct);
    }

    @Test
    public void testPurePlainMtefEURInThousands()
    {
        GroupReportModel fddr_correct = GroupReportModel.withColumnReports("AMP-16100-flat-mtefs-eur", 
                ColumnReportDataModel.withColumns("AMP-16100-flat-mtefs-eur", 
                        SimpleColumnModel.withContents("Project Title", NULL_PLACEHOLDER),
                        SimpleColumnModel.withContents("MTEF 2011", "Pure MTEF Project", "25"),
                        SimpleColumnModel.withContents("MTEF 2012", MUST_BE_EMPTY),
                        SimpleColumnModel.withContents("MTEF 2013", MUST_BE_EMPTY),
                        /*GroupColumnModel.withSubColumns("Funding", 
                                GroupColumnModel.withSubColumns("2010", 
                                        SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY),
                                        SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY)
                                                )),*/
                        GroupColumnModel.withSubColumns("Total Costs", 
                                SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY),
                                SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY)
                        ))).withTrailCells(null, "25", "0", "0", "0", "0");
        
        AmpReportModifier modifier = new AmpReportModifier() {          
            @Override
            public void modifyAmpReportSettings(AmpReports report, AmpARFilter filter) {                
                filter.setAmountinthousand(AmpARFilter.AMOUNT_OPTION_IN_THOUSANDS);
            }
        };
        runReportTest("pure Mtef, implicit filter by Donor, EUR, THOUSANDS", "AMP-16100-flat-mtefs-eur", new String[] {"Pure MTEF Project"}, fddr_correct, modifier, null);
    }

    @Test
    public void testPurePlainMtefEURInThousandsMoreActivities()
    {
        GroupReportModel fddr_correct = GroupReportModel.withColumnReports("AMP-16100-flat-mtefs-eur",
                ColumnReportDataModel.withColumns("AMP-16100-flat-mtefs-eur",
                        SimpleColumnModel.withContents("Project Title", "Test MTEF directed", "Test MTEF directed", "Pure MTEF Project", "Pure MTEF Project", "TAC_activity_2", "TAC_activity_2").setIsPledge(false), 
                        SimpleColumnModel.withContents("MTEF 2011", "Test MTEF directed", "112", "Pure MTEF Project", "25").setIsPledge(false), 
                        SimpleColumnModel.withContents("MTEF 2012", "Test MTEF directed", "49").setIsPledge(false), 
                        SimpleColumnModel.withContents("MTEF 2013", MUST_BE_EMPTY).setIsPledge(false), 
                        GroupColumnModel.withSubColumns("Funding",
                            GroupColumnModel.withSubColumns("2010",
                                SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(false), 
                                SimpleColumnModel.withContents("Actual Disbursements", "Test MTEF directed", "113", "TAC_activity_2", "355").setIsPledge(false)), 
                            GroupColumnModel.withSubColumns("2011",
                                SimpleColumnModel.withContents("Actual Commitments", "TAC_activity_2", "747").setIsPledge(false), 
                                SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false))), 
                        GroupColumnModel.withSubColumns("Total Costs",
                            SimpleColumnModel.withContents("Actual Commitments", "TAC_activity_2", "747").setIsPledge(false), 
                            SimpleColumnModel.withContents("Actual Disbursements", "Test MTEF directed", "113", "TAC_activity_2", "355").setIsPledge(false)))
                    .withTrailCells(null, "137", "49", "0", "0", "467", "747", "0", "747", "467"))
                .withTrailCells(null, "137", "49", "0", "0", "467", "747", "0", "747", "467")
                .withPositionDigest(true,
                    "(line 0:RHLC Project Title: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1), RHLC MTEF 2011: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1), RHLC MTEF 2012: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1), RHLC MTEF 2013: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 3, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 4, colSpan: 4), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 3, colStart: 8, colSpan: 2))",
                    "(line 1:RHLC 2010: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 2), RHLC 2011: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 6, colSpan: 2))",
                    "(line 2:RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1))");
        
        AmpReportModifier modifier = new AmpReportModifier() {          
            @Override
            public void modifyAmpReportSettings(AmpReports report, AmpARFilter filter) {                
                filter.setAmountinthousand(AmpARFilter.AMOUNT_OPTION_IN_THOUSANDS);
            }
        };
        runReportTest("pure Mtef, implicit filter by Donor, EUR, THOUSANDS", "AMP-16100-flat-mtefs-eur", new String[] {"Pure MTEF Project", "Test MTEF directed", "TAC_activity_2"}, fddr_correct, modifier, null);
    }   
    
}

