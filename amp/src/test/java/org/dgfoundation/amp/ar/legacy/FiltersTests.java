package org.dgfoundation.amp.ar.legacy;

import org.dgfoundation.amp.nireports.testcases.ColumnReportDataModel;
import org.dgfoundation.amp.nireports.testcases.GroupColumnModel;
import org.dgfoundation.amp.nireports.testcases.GroupReportModel;
import org.dgfoundation.amp.nireports.testcases.SimpleColumnModel;
import org.dgfoundation.amp.testutils.ReportsTestCase;
import org.junit.Test;

public class FiltersTests extends ReportsTestCase{
    
    @Test
    public void testDateFiltersTotals()
    {
        GroupReportModel fssc_correct = GroupReportModel.withColumnReports("AMP-15988-test-date-filters", 
                ColumnReportDataModel.withColumns("AMP-15988-test-date-filters", 
                        SimpleColumnModel.withContents("Project Title", "date-filters-activity", "date-filters-activity"),
                        
                        GroupColumnModel.withSubColumns("Funding", 
                                GroupColumnModel.withSubColumns("2012", 
                                    SimpleColumnModel.withContents("Actual Commitments", "date-filters-activity", "25 000"),
                                    SimpleColumnModel.withContents("Actual Disbursements", "date-filters-activity", "12 000"))),
                                    
                        GroupColumnModel.withSubColumns("Total Costs", 
                                SimpleColumnModel.withContents("Actual Commitments", "date-filters-activity", "25 000"),
                                SimpleColumnModel.withContents("Actual Disbursements", "date-filters-activity", "12 000")))
                        );
        runReportTest("date filters on actual comm/disb", "AMP-15988-test-date-filters", new String[] {"date-filters-activity"}, fssc_correct);

    }
}

