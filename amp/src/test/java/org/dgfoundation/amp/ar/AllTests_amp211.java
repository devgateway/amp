package org.dgfoundation.amp.ar;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.dgfoundation.amp.ar.amp211.BooleanFilterTests;
import org.dgfoundation.amp.ar.amp211.InflationRatesTests;
import org.dgfoundation.amp.ar.amp211.MondrianComputedMeasuresReportTests211;
import org.dgfoundation.amp.ar.amp211.OldReportsNewFeaturesTests;
import org.dgfoundation.amp.ar.amp211.PledgeReportsTests211;
import org.dgfoundation.amp.ar.amp211.ReportCalendarTests;


/**
 * entry point for AMP 2.11 tests. Initializes standalone AMP as part of the discovery process. <br />
 * standalone AMP configuration is taken off standAloneAmpHibernate.cfg.xml <br />
 * <strong>Please notice that it uses the same amp_tests_210 database, just that the testcases are run separately</strong>
 * @author Dolghier Constantin
 *
 */
public class AllTests_amp211
{

	public static Test suite() {
		
		setUp();
		
		TestSuite suite = new TestSuite(AllTests_amp211.class.getName());
		suite.addTest(new JUnit4TestAdapter(InflationRatesTests.class));
		suite.addTest(new JUnit4TestAdapter(BooleanFilterTests.class));
		//suite.addTest(new JUnit4TestAdapter(FundingFlowsMondrianReportTests.class));
		suite.addTest(new JUnit4TestAdapter(OldReportsNewFeaturesTests.class));
		suite.addTest(new JUnit4TestAdapter(PledgeReportsTests211.class));
		//suite.addTest(new JUnit4TestAdapter(NewFeaturesMondrianReportTests.class));
		suite.addTest(new JUnit4TestAdapter(MondrianComputedMeasuresReportTests211.class));
		suite.addTest(new JUnit4TestAdapter(ReportCalendarTests.class));
		
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
		AllTests_amp212.setUp();
	}
}
