package org.dgfoundation.amp.ar.legacy;

import static org.dgfoundation.amp.testutils.ReportTestingUtils.NULL_PLACEHOLDER;

import org.dgfoundation.amp.testutils.ReportsTestCase;
import org.dgfoundation.amp.testmodels.*;

import junit.framework.Test;
import junit.framework.TestSuite;

public class FiltersTests extends ReportsTestCase{
	
	public FiltersTests(String name)
	{
		super(name);
	}
	
	public static Test suite()
	{
		TestSuite suite = new TestSuite(FiltersTests.class.getName());
		suite.addTest(new FiltersTests("testDateFiltersTotals"));
		return suite;
	}
	
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

