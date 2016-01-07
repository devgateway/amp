package org.dgfoundation.amp.ar;

//import org.dgfoundation.amp.testutils.LiberiaFiller;
import org.digijava.kernel.persistence.HibernateClassLoader;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.util.DigiConfigManager;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.kernel.util.resource.ResourceStreamHandlerFactory;
import org.hibernate.cfg.Configuration;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * entry point for AMP 2.7 tests. Initializes standalone AMP as part of the discovery process.
 * standalone AMP configuration is taken off standAloneAmpHibernate.cfg.xml
 * @author Dolghier Constantin
 *
 */
public class AllTests_amp27
{

	public static Test suite() {
		
		setUp();
		//new MoldovaTranslationsSplit().doMoldovaTranslations();
		
//		LiberiaFiller.fillInDatabase();
		TestSuite suite = new TestSuite(AllTests_amp27.class.getName());
		//$JUnit-BEGIN$

		suite.addTest(HierarchyTests27.suite());
		suite.addTest(DirectedDisbursementsTests_amp27.suite());
		suite.addTest(MultilingualTests.suite());
		suite.addTest(MultilingualThroughTrnTests.suite());
		suite.addTest(SscTests27.suite());
		suite.addTest(CategoryManagerTests.suite());
		suite.addTest(MiscColumnsTests27.suite());
		//$JUnit-END$
		return suite;
	}
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static void setUp()
	{
		try
		{ 
			AllTests_amp212.configureLog4j();
			HibernateClassLoader.HIBERNATE_CFG_XML = "/standAloneAmpHibernate.cfg.xml";
			HibernateClassLoader.HIBERNATE_CFG_OVERRIDE_DATABASE = "jdbc:postgresql://localhost/amp_tests_27_amp212";
			//HibernateClassLoader.HIBERNATE_CFG_OVERRIDE_DATABASE = "jdbc:postgresql://localhost/amp_moldova_27";
			//HibernateClassLoader.HIBERNATE_CFG_OVERRIDE_DATABASE = "jdbc:postgresql://localhost:15434/amp_moldova";
    	
			ResourceStreamHandlerFactory.installIfNeeded();

			DigiConfigManager.initialize("./repository");
			PersistenceManager.initialize(false, null);
			Configuration cfg = HibernateClassLoader.getConfiguration();
			//System.out.println("AMP started up!");
			
			org.apache.struts.mock.MockHttpServletRequest mockRequest = new org.apache.struts.mock.MockHttpServletRequest(new org.apache.struts.mock.MockHttpSession());
			TLSUtils.populate(mockRequest);

			TLSUtils.getThreadLocalInstance().setForcedLangCode(SiteUtils.getDefaultSite().getDefaultLanguage().getCode());
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
		
		// PersistenceManager.cleanup();
	}
	
}

