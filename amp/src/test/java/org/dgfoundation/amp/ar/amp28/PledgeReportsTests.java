package org.dgfoundation.amp.ar.amp28;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.dgfoundation.amp.testmodels.ColumnReportDataModel;
import org.dgfoundation.amp.testmodels.GroupColumnModel;
import org.dgfoundation.amp.testmodels.GroupReportModel;
import org.dgfoundation.amp.testmodels.SimpleColumnModel;
import org.dgfoundation.amp.testutils.AmpTestCase;
import org.dgfoundation.amp.testutils.ReportsTestCase;
import org.digijava.kernel.request.TLSUtils;

import static org.dgfoundation.amp.testutils.ReportTestingUtils.NULL_PLACEHOLDER;
import static org.dgfoundation.amp.testutils.ReportTestingUtils.MUST_BE_EMPTY;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * pledge-reports tests
 * @author Dolghier Constantin
 *
 */
public class PledgeReportsTests extends ReportsTestCase
{
	
	private PledgeReportsTests(String name)
	{
		super(name);		
	}
	
	public static Test suite()
	{
		TestSuite suite = new TestSuite(PledgeReportsTests.class.getName());
		suite.addTest(new PledgeReportsTests("testPledgeDateRange"));
		return suite;
	}
	
	public void testPledgeDateRange()
	{
		GroupReportModel fddr_correct = GroupReportModel.withColumnReports("AMP-16007-date-range-columns",
				ColumnReportDataModel.withColumns("AMP-16007-date-range-columns",
						SimpleColumnModel.withContents("Pledges Detail Date Range", "Test pledge 1", "[2012-06-06 - 2014-04-04, 2014-04-01 - 2014-04-16, 2014-04-18 - 2014-04-24]", "free text name 2", "2012-03-02 - 2015-03-03").setIsPledge(true), 
						SimpleColumnModel.withContents("Pledges Detail End Date", "Test pledge 1", "[04/04/2014, 16/04/2014, 24/04/2014]", "free text name 2", "03/03/2015").setIsPledge(true), 
						SimpleColumnModel.withContents("Pledges Detail Start Date", "Test pledge 1", "[06/06/2012, 01/04/2014, 18/04/2014]", "free text name 2", "02/03/2012").setIsPledge(true), 
						SimpleColumnModel.withContents("Pledges Titles", "Test pledge 1", "Test pledge 1", "ACVL Pledge Name 2", "ACVL Pledge Name 2", "free text name 2", "free text name 2").setIsPledge(true), 
						GroupColumnModel.withSubColumns("Funding",
							GroupColumnModel.withSubColumns("2005",
								SimpleColumnModel.withContents("Actual Pledge", "Test pledge 1", "1,34").setIsPledge(true)), 
							GroupColumnModel.withSubColumns("2006",
								SimpleColumnModel.withContents("Actual Pledge", "Test pledge 1", "767 676").setIsPledge(true)), 
							GroupColumnModel.withSubColumns("2013",
								SimpleColumnModel.withContents("Actual Pledge", "free text name 2", "1 035 994,16").setIsPledge(true))), 
						GroupColumnModel.withSubColumns("Total Costs",
							SimpleColumnModel.withContents("Actual Pledge", "Test pledge 1", "1 033 246,32", "ACVL Pledge Name 2", "938 069,75", "free text name 2", "1 035 994,16").setIsPledge(true)))
					.withTrailCells(null, null, null, null, "1,34", "767 676", "1 035 994,16", "3 007 310,23"))
				.withTrailCells(null, null, null, null, "1,34", "767 676", "1 035 994,16", "3 007 310,23")
				.withPositionDigest(true,
					"(line 0:RHLC Pledges Detail Date Range: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1), RHLC Pledges Detail End Date: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1), RHLC Pledges Detail Start Date: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1), RHLC Pledges Titles: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 3, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 4, colSpan: 3), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 3, colStart: 7, colSpan: 1))",
					"(line 1:RHLC 2005: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 1), RHLC 2006: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 1), RHLC 2013: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 6, colSpan: 1))",
					"(line 2:RHLC Actual Pledge: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1), RHLC Actual Pledge: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1), RHLC Actual Pledge: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1), RHLC Actual Pledge: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1))");
		runReportTest("Basic pledge report with date range", "AMP-16007-date-range-columns", new String[] {"irrelevant since this is a pledge report"}, fddr_correct);					
	}
	
	
	@Override
    protected void setUp() throws Exception
    {
		TLSUtils.getThreadLocalInstance().setForcedLangCode("en");
		super.setUp();
        // do nothing now                
    }
}