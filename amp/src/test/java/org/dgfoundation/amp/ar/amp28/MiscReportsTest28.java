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

	public MiscReportsTest28(String name) {
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
		return suite;
	}
	
	public void testNoProjectsReport() {
		GroupReportModel noProjects = GroupReportModel.withColumnReports("AMP-17400-no-projects", (ColumnReportDataModel[])null)
				.withTrailCells().withPositionDigest(true, (String[])null);
		
		runReportTest("AMP-17400 no projects", "AMP-17400-no-projects", new String[]{""}, noProjects);
	}
}
