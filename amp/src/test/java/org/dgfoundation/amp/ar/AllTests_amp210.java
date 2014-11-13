package org.dgfoundation.amp.ar;

//import org.dgfoundation.amp.testutils.LiberiaFiller;
import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestSuite;

import org.dgfoundation.amp.ar.amp210.BasicMondrianReportTests;
import org.dgfoundation.amp.ar.amp210.DateColumnsMondrianReportTests;
import org.dgfoundation.amp.ar.amp210.ETLTests;
import org.dgfoundation.amp.ar.amp210.FiltersMondrianReportTests;
import org.dgfoundation.amp.ar.amp210.MondrianSummaryReportTests;
import org.dgfoundation.amp.ar.amp210.SQLUtilsTests;
import org.dgfoundation.amp.mondrian.monet.MonetConnection;
import org.digijava.kernel.persistence.HibernateClassLoader;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.util.DigiConfigManager;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.kernel.util.resource.ResourceStreamHandlerFactory;

/**
 * entry point for AMP 2.8 tests. Initializes standalone AMP as part of the discovery process.
 * standalone AMP configuration is taken off standAloneAmpHibernate.cfg.xml
 * @author Dolghier Constantin
 *
 */
public class AllTests_amp210
{

	public static Test suite() {
		
		setUp();
		//new MoldovaTranslationsSplit().doMoldovaTranslations();
		
//		LiberiaFiller.fillInDatabase();
		TestSuite suite = new TestSuite(AllTests_amp210.class.getName());
		suite.addTest(new JUnit4TestAdapter(FiltersMondrianReportTests.class));
		suite.addTest(new JUnit4TestAdapter(ETLTests.class));
		suite.addTest(SQLUtilsTests.suite());
		suite.addTest(new JUnit4TestAdapter(BasicMondrianReportTests.class));
		suite.addTest(new JUnit4TestAdapter(DateColumnsMondrianReportTests.class));
		suite.addTest(new JUnit4TestAdapter(MondrianSummaryReportTests.class));
		//$JUnit-BEGIN$

		//$JUnit-END$
		return suite;
	}
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static void setUp() {
		try {
			HibernateClassLoader.HIBERNATE_CFG_XML = "/standAloneAmpHibernate.cfg.xml";
			HibernateClassLoader.HIBERNATE_CFG_OVERRIDE_DATABASE = "jdbc:postgresql://localhost/amp_tests_210";
			MonetConnection.MONET_CFG_OVERRIDE_URL = "jdbc:monetdb://localhost/amp_tests_210";
			
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
