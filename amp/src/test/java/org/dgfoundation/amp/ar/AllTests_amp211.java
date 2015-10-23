package org.dgfoundation.amp.ar;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.dgfoundation.amp.ar.amp211.BooleanFilterTests;
import org.dgfoundation.amp.ar.amp211.FundingFlowsMondrianReportTests;
import org.dgfoundation.amp.ar.amp211.InflationRatesTests;
import org.dgfoundation.amp.ar.amp211.NewFeaturesMondrianReportTests;
import org.dgfoundation.amp.ar.amp211.OldReportsNewFeaturesTests;
import org.dgfoundation.amp.ar.amp211.PlainMTEFMondrianReportTests;
import org.dgfoundation.amp.ar.amp211.PledgeReportsTests211;
import org.dgfoundation.amp.mondrian.monet.MonetConnection;
import org.digijava.kernel.persistence.HibernateClassLoader;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.util.DigiConfigManager;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.kernel.util.resource.ResourceStreamHandlerFactory;

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
		suite.addTest(new JUnit4TestAdapter(PlainMTEFMondrianReportTests.class));
		suite.addTest(new JUnit4TestAdapter(InflationRatesTests.class));
		suite.addTest(new JUnit4TestAdapter(BooleanFilterTests.class));
		suite.addTest(new JUnit4TestAdapter(FundingFlowsMondrianReportTests.class));
		suite.addTest(new JUnit4TestAdapter(OldReportsNewFeaturesTests.class));
		suite.addTest(new JUnit4TestAdapter(PledgeReportsTests211.class));
		suite.addTest(new JUnit4TestAdapter(NewFeaturesMondrianReportTests.class));
		
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
			HibernateClassLoader.HIBERNATE_CFG_XML = "/standAloneAmpHibernate.cfg.xml";
			HibernateClassLoader.HIBERNATE_CFG_OVERRIDE_DATABASE = "jdbc:postgresql://localhost/amp_tests_211";
			MonetConnection.MONET_CFG_OVERRIDE_URL = "jdbc:monetdb://localhost/amp_tests_211";
			
			org.digijava.kernel.ampapi.mondrian.util.Connection.IS_TESTING = true;
			//HibernateClassLoader.HIBERNATE_CFG_OVERRIDE_DATABASE = "jdbc:postgresql://localhost/amp_moldova_27";
			//HibernateClassLoader.HIBERNATE_CFG_OVERRIDE_DATABASE = "jdbc:postgresql://localhost:15434/amp_moldova";
    	
			ResourceStreamHandlerFactory.installIfNeeded();

			DigiConfigManager.initialize("./repository");
			PersistenceManager.initialize(false, null);
//			Configuration cfg = HibernateClassLoader.getConfiguration();
			//System.out.println("AMP started up!");
			TLSUtils.getThreadLocalInstance().setForcedLangCode(SiteUtils.getDefaultSite().getDefaultLanguage().getCode());
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
}
