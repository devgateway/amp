/**
 * 
 */
package org.dgfoundation.amp.ar.amp28;

import static org.dgfoundation.amp.testutils.ReportTestingUtils.MUST_BE_EMPTY;
import junit.framework.Test;
import junit.framework.TestSuite;

import org.dgfoundation.amp.testmodels.ColumnReportDataModel;
import org.dgfoundation.amp.testmodels.GroupColumnModel;
import org.dgfoundation.amp.testmodels.GroupReportModel;
import org.dgfoundation.amp.testmodels.SimpleColumnModel;
import org.dgfoundation.amp.testutils.ReportsTestCase;
import org.digijava.kernel.request.TLSUtils;



/**
 * Reports tests for 2.8
 * @author Nadia Mandrescu
 */
public class MiscReportsTest28 extends ReportsTestCase {

	private MiscReportsTest28(String name) {
		super(name);
	}
	
	@Override
    protected void setUp() throws Exception
    {
		TLSUtils.getThreadLocalInstance().setForcedLangCode("en");
		super.setUp();
        // do nothing now                
    }

	public static Test suite()
	{
		TestSuite suite = new TestSuite(PledgeReportsTests.class.getName());
		suite.addTest(new MiscReportsTest28("testNoProjectsReport"));
		suite.addTest(new MiscReportsTest28("testTotalsOnlySummaryReport"));
		return suite;
	}
	
	public void testTotalsOnlySummaryReport() {
		GroupReportModel summaryTotalsOnly = GroupReportModel.withColumnReports("AMP-17646",
				ColumnReportDataModel.withColumns("AMP-17646",
						SimpleColumnModel.withContents("", MUST_BE_EMPTY).setIsPledge(false), 
						GroupColumnModel.withSubColumns("Total Costs",
							SimpleColumnModel.withContents("Actual Commitments", "crazy funding 1", "333 333", "ptc activity 2", "333 222", "ptc activity 1", "666 777").setIsPledge(false), 
							SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false)))
					.withTrailCells(null, "1 333 332", "0"))
				.withTrailCells(null, "1 333 332", "0")
				.withPositionDigest(true,
					"(line 0:RHLC : (startRow: 0, rowSpan: 2, totalRowSpan: 2, colStart: 0, colSpan: 1), RHLC Total Costs: (startRow: 0, rowSpan: 1, totalRowSpan: 2, colStart: 1, colSpan: 2))",
					"(line 1:RHLC Actual Commitments: (startRow: 1, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1), RHLC Actual Disbursements: (startRow: 1, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1))");
		
		runReportTest("Summary report with totals only", "AMP-17646", new String[] {"ptc activity 1", "ptc activity 2", "crazy funding 1"}, 
				summaryTotalsOnly, null, "en");
	}
	
	public void testNoProjectsReport() {
		GroupReportModel noProjects = GroupReportModel.withColumnReports("AMP-17400-no-projects", (ColumnReportDataModel[])null)
				.withTrailCells().withPositionDigest(true, (String[])null);
		
		runReportTest("AMP-17400 no projects", "AMP-17400-no-projects", new String[]{""}, noProjects);
	}
}
