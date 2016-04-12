package org.dgfoundation.amp.ar;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.dgfoundation.amp.ar.amp212.AmpSchemaFilteringTests;
import org.dgfoundation.amp.ar.amp212.AmpSchemaSanityTests;
import org.dgfoundation.amp.ar.amp212.AmpSchemaSortingTests;
import org.dgfoundation.amp.ar.amp212.CurrencyConvertorTests;
import org.dgfoundation.amp.ar.amp212.DimensionsFetchingTests;
import org.dgfoundation.amp.ar.amp212.FundingFlowsTests;
import org.dgfoundation.amp.ar.amp212.NiReportsFetchingTests;
import org.dgfoundation.amp.ar.amp212.OfflineTests;
import org.dgfoundation.amp.ar.viewfetcher.InternationalizedViewsRepository;
import org.dgfoundation.amp.mondrian.monet.MonetConnection;
import org.digijava.kernel.persistence.HibernateClassLoader;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.util.DigiConfigManager;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.kernel.util.resource.ResourceStreamHandlerFactory;

/**
 * entry point for AMP 2.12 tests. Initializes standalone AMP as part of the discovery process. <br />
 * standalone AMP configuration is taken off standAloneAmpHibernate.cfg.xml <br />
 * @author Dolghier Constantin
 *
 */
public class AllTests_amp212
{

	public static Test suite() {
		
		setUp();
		
		TestSuite suite = new TestSuite(AllTests_amp212.class.getName());
		
		suite.addTest(OfflineTests.suite());
		suite.addTest(new JUnit4TestAdapter(NiReportsFetchingTests.class));
		suite.addTest(new JUnit4TestAdapter(AmpSchemaSanityTests.class));
		suite.addTest(new JUnit4TestAdapter(AmpSchemaSortingTests.class));
		suite.addTest(new JUnit4TestAdapter(AmpSchemaFilteringTests.class));
		suite.addTest(new JUnit4TestAdapter(FundingFlowsTests.class));

		suite.addTest(new JUnit4TestAdapter(CurrencyConvertorTests.class));
		suite.addTest(new JUnit4TestAdapter(DimensionsFetchingTests.class));

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
			HibernateClassLoader.HIBERNATE_CFG_OVERRIDE_DATABASE = "jdbc:postgresql://localhost:5433/amp_tests_212";
			MonetConnection.MONET_CFG_OVERRIDE_URL = "jdbc:monetdb://localhost/amp_tests_212";
			
			org.digijava.kernel.ampapi.mondrian.util.Connection.IS_TESTING = true;
			//HibernateClassLoader.HIBERNATE_CFG_OVERRIDE_DATABASE = "jdbc:postgresql://localhost/amp_moldova_27";
			//HibernateClassLoader.HIBERNATE_CFG_OVERRIDE_DATABASE = "jdbc:postgresql://localhost:15434/amp_moldova";
    	
			ResourceStreamHandlerFactory.installIfNeeded();

			DigiConfigManager.initialize("./repository");
			PersistenceManager.initialize(false, null);
//			Configuration cfg = HibernateClassLoader.getConfiguration();
			//System.out.println("AMP started up!");
			TLSUtils.getThreadLocalInstance().setForcedLangCode(SiteUtils.getDefaultSite().getDefaultLanguage().getCode());
			InternationalizedViewsRepository.i18Models.size(); // force init outside of testcases
			org.apache.struts.mock.MockHttpServletRequest mockRequest = new org.apache.struts.mock.MockHttpServletRequest(new org.apache.struts.mock.MockHttpSession());
			TLSUtils.populate(mockRequest);
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
}
