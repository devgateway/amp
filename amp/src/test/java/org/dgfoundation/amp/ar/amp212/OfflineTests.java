package org.dgfoundation.amp.ar.amp212;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.dgfoundation.amp.ar.amp212.BigDecimalPrecisionTests;
import org.dgfoundation.amp.ar.amp212.DimensionSnapshotTests;
import org.dgfoundation.amp.ar.amp212.FundingFlowsInnerTests;
import org.dgfoundation.amp.ar.amp212.GraphAlgorithmsTests;
import org.dgfoundation.amp.ar.amp212.HierarchyTrackingTestcases;
import org.dgfoundation.amp.ar.amp212.InclusiveRunnerTests;
import org.dgfoundation.amp.ar.amp212.MetaInfoTests;
import org.dgfoundation.amp.ar.amp212.OffDbNiReportEngineTests;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.util.DigiConfigManager;
import org.digijava.kernel.util.resource.ResourceStreamHandlerFactory;

/**
 * entry point for offline NiReports tests
 * @author Dolghier Constantin
 *
 */
public class OfflineTests
{

	public static Test suite() {
		
		setUp();
		
		TestSuite suite = new TestSuite(OfflineTests.class.getName());
		
		suite.addTest(new JUnit4TestAdapter(GraphAlgorithmsTests.class));
		suite.addTest(new JUnit4TestAdapter(InclusiveRunnerTests.class));
		suite.addTest(new JUnit4TestAdapter(MetaInfoTests.class)); 
		suite.addTest(new JUnit4TestAdapter(FundingFlowsInnerTests.class));
		suite.addTest(new JUnit4TestAdapter(DimensionSnapshotTests.class));
		suite.addTest(new JUnit4TestAdapter(BigDecimalPrecisionTests.class));
		suite.addTest(new JUnit4TestAdapter(HierarchyTrackingTestcases.class));
		suite.addTest(new JUnit4TestAdapter(OffDbNiReportEngineTests.class));
		suite.addTest(new JUnit4TestAdapter(OffDbNiReportSortingTests.class));
		suite.addTest(new JUnit4TestAdapter(PercentagesCorrectorTests.class));
		suite.addTest(new JUnit4TestAdapter(SummaryReportsTests.class));
		suite.addTest(new JUnit4TestAdapter(PaginationTests.class));
		suite.addTest(new JUnit4TestAdapter(FormattingTests.class));
		suite.addTest(new JUnit4TestAdapter(OffDbNiReportFilteringTests.class));
		suite.addTest(new JUnit4TestAdapter(UtilsTests.class));
		suite.addTest(new JUnit4TestAdapter(UnitsSettingsUtilityTests.class));
		suite.addTest(new JUnit4TestAdapter(ETLTests.class));
		suite.addTest(new JUnit4TestAdapter(FilterRuleTests.class));
		
		return suite;
	}
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	
	public static void configureLog4j() {
		BasicConfigurator.configure();
		LogManager.getRootLogger().setLevel(Level.ERROR);
	}
	
	
	public static void setUp() {
		try {
			configureLog4j();
			org.digijava.kernel.ampapi.mondrian.util.Connection.IS_TESTING = true;
			ResourceStreamHandlerFactory.installIfNeeded();
			DigiConfigManager.initialize("./repository");
			org.apache.struts.mock.MockHttpServletRequest mockRequest = new org.apache.struts.mock.MockHttpServletRequest(new org.apache.struts.mock.MockHttpSession());
			TLSUtils.populate(mockRequest);
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
}
