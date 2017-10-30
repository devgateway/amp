package org.dgfoundation.amp.ar.legacy;

import org.dgfoundation.amp.testutils.*;


import static org.dgfoundation.amp.testutils.ReportTestingUtils.NULL_PLACEHOLDER;

import org.dgfoundation.amp.nireports.testcases.ColumnReportDataModel;
import org.dgfoundation.amp.nireports.testcases.GroupColumnModel;
import org.dgfoundation.amp.nireports.testcases.GroupReportModel;
import org.dgfoundation.amp.nireports.testcases.SimpleColumnModel;
import org.junit.Test;

import static org.dgfoundation.amp.testutils.ReportTestingUtils.MUST_BE_EMPTY;

public class MiscColumnsTests extends ReportsTestCase {

    @Test
    public void testSscColumns()
    {
        GroupReportModel fssc_correct = GroupReportModel.withColumnReports("AMP-15844-ssc-columns", 
                ColumnReportDataModel.withColumns("AMP-15844-ssc-columns", 
                        SimpleColumnModel.withContents("Project Title", NULL_PLACEHOLDER),
                        SimpleColumnModel.withContents("Type of Cooperation", "SSC Project 1", "Official Development Aid (ODA)", "SSC Project 2", "Regional South South Cooperation"),
                        SimpleColumnModel.withContents("Type of Implementation", "SSC Project 1", "Program", "SSC Project 2", "Action"),
                        SimpleColumnModel.withContents("Modalities", "SSC Project 1", "Diplomats and courses", "SSC Project 2", "Virtual Platforms and blogs to consult, learn, and exchange ideas"),
                        SimpleColumnModel.withContents("Component Description", "SSC Project 1", "SSC Project 1 DescriptionOfComponent", "SSC Project 2", "SSC Project 2 Description of Component"),
                        SimpleColumnModel.withContents("Component Title", "SSC Project 1", "SSC Project 1 TitleOfComponent", "SSC Project 2", "SSC Project 2 Title of Component"),

                        
                        GroupColumnModel.withSubColumns("Funding", 
                                GroupColumnModel.withSubColumns("2013", 
                                    SimpleColumnModel.withContents("Actual Commitments", "ptc activity 1", "666 777", "SSC Project 1", "111 333", "SSC Project 2", "567 421"),
                                    SimpleColumnModel.withContents("Actual Disbursements", "SSC Project 1", "555 111", "SSC Project 2", "131 845"))),
                        GroupColumnModel.withSubColumns("Total Costs", 
                                SimpleColumnModel.withContents("Actual Commitments", "ptc activity 1", "666 777", "SSC Project 1", "111 333", "SSC Project 2", "567 421"),
                                SimpleColumnModel.withContents("Actual Disbursements", "SSC Project 1", "555 111", "SSC Project 2", "131 845")))
                        );
        
        boolean caughtException = false;
        try
        {
            runReportTest("flat SSC Columns", "AMP-15844-ssc-columns", new String[] {"ptc activity 1", "SSC Project 1", "SSC Project 2"}, fssc_correct);
        }
        catch(RuntimeException ex)
        {
            caughtException = ex.getMessage().startsWith("no report with the given name");
        }
        assertEquals("report AMP-15844-ssc-columns should have been deleted and non-existant", true, caughtException);
    }

    @Test
    public void testProjectedProjectCostEUR()
    {
        GroupReportModel prop_cost_eur_correct = GroupReportModel.withColumnReports("Proposed-cost-EUR",
                ColumnReportDataModel.withColumns("Proposed-cost-EUR", 
                    SimpleColumnModel.withContents("Project Title", "Proposed Project Cost 1 - USD", "Proposed Project Cost 1 - USD", "Proposed Project Cost 2 - EUR", "Proposed Project Cost 2 - EUR"),
                    SimpleColumnModel.withContents("Proposed Project Amount", "Proposed Project Cost 1 - USD", "770 600", "Proposed Project Cost 2 - EUR", "2 500 000"),
                    GroupColumnModel.withSubColumns("Total Costs",
                            SimpleColumnModel.withContents("Actual Commitments", NULL_PLACEHOLDER),
                            SimpleColumnModel.withContents("Actual Disbursements", NULL_PLACEHOLDER)
                            )
                ));
        
        runReportTest("Proposed-cost in EUR", "Proposed-cost-EUR", new String[] {"Proposed Project Cost 1 - USD", "Proposed Project Cost 2 - EUR"}, prop_cost_eur_correct);
    }

    @Test
    public void testProjectedProjectCostUSD()
    {
        GroupReportModel prop_cost_usd_correct = GroupReportModel.withColumnReports("Proposed-cost-USD",
                ColumnReportDataModel.withColumns("Proposed-cost-USD",
                        SimpleColumnModel.withContents("Project Title", "Proposed Project Cost 2 - EUR", "Proposed Project Cost 2 - EUR", "Proposed Project Cost 1 - USD", "Proposed Project Cost 1 - USD").setIsPledge(false), 
                        SimpleColumnModel.withContents("Proposed Project Amount", "Proposed Project Cost 2 - EUR", "3 399 510,47", "Proposed Project Cost 1 - USD", "1 000 000").setIsPledge(false), 
                        GroupColumnModel.withSubColumns("Total Costs",
                            SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(false), 
                            SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false)))
                    .withTrailCells(null, "4 399 510,47", "0", "0"))
                .withTrailCells(null, "4 399 510,47", "0", "0")
                .withPositionDigest(true,
                    "(line 0:RHLC Project Title: (startRow: 0, rowSpan: 2, totalRowSpan: 2, colStart: 0, colSpan: 1), RHLC Proposed Project Amount: (startRow: 0, rowSpan: 2, totalRowSpan: 2, colStart: 1, colSpan: 1), RHLC Total Costs: (startRow: 0, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 2))",
                    "(line 1:RHLC Actual Commitments: (startRow: 1, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1), RHLC Actual Disbursements: (startRow: 1, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1))");
        
        runReportTest("Proposed-cost in USD", "Proposed-cost-USD", new String[] {"Proposed Project Cost 1 - USD", "Proposed Project Cost 2 - EUR"}, prop_cost_usd_correct);
    }
}
