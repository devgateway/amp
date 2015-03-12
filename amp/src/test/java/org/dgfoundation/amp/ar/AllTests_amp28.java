package org.dgfoundation.amp.ar;

//import org.dgfoundation.amp.testutils.LiberiaFiller;
import org.dgfoundation.amp.ar.amp28.CombinedPledgeAndActivitiesReportsTests;
import org.dgfoundation.amp.ar.amp28.DashboardsTests;
import org.dgfoundation.amp.ar.amp28.MiscReportsTest28;
import org.dgfoundation.amp.ar.amp28.MiscTests28;
import org.dgfoundation.amp.ar.amp28.MultilingualTests28;
import org.dgfoundation.amp.ar.amp28.PledgeReportsTests;
import org.dgfoundation.amp.ar.amp28.PledgesFormTests;
import org.dgfoundation.amp.ar.amp28.ProgramsTests;
import org.dgfoundation.amp.ar.amp28.RealCommitmentsAndMTEFs;
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
 * entry point for AMP 2.8 tests. Initializes standalone AMP as part of the discovery process.
 * standalone AMP configuration is taken off standAloneAmpHibernate.cfg.xml
 * @author Dolghier Constantin
 *
 */
public class AllTests_amp28
{

	public static Test suite() {
		
		setUp();
		//new MoldovaTranslationsSplit().doMoldovaTranslations();
		
//		LiberiaFiller.fillInDatabase();
		TestSuite suite = new TestSuite(AllTests_amp28.class.getName());
		//$JUnit-BEGIN$

		suite.addTest(MultilingualTests28.suite());
		suite.addTest(PledgesFormTests.suite());
		suite.addTest(MiscTests28.suite());
		suite.addTest(ProgramsTests.suite());
		suite.addTest(PledgeReportsTests.suite());
		suite.addTest(CombinedPledgeAndActivitiesReportsTests.suite());
		suite.addTest(MiscReportsTest28.suite());
		suite.addTest(DashboardsTests.suite());
		suite.addTest(RealCommitmentsAndMTEFs.suite());
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
			HibernateClassLoader.HIBERNATE_CFG_XML = "/standAloneAmpHibernate.cfg.xml";
			HibernateClassLoader.HIBERNATE_CFG_OVERRIDE_DATABASE = "jdbc:postgresql://localhost/amp_tests_28_amp211";
			//HibernateClassLoader.HIBERNATE_CFG_OVERRIDE_DATABASE = "jdbc:postgresql://localhost/amp_moldova_27";
			//HibernateClassLoader.HIBERNATE_CFG_OVERRIDE_DATABASE = "jdbc:postgresql://localhost:15434/amp_moldova";
    	
			ResourceStreamHandlerFactory.installIfNeeded();

			DigiConfigManager.initialize("./repository");
			PersistenceManager.initialize(false, null);
//			Configuration cfg = HibernateClassLoader.getConfiguration();
			//System.out.println("AMP started up!");
			TLSUtils.getThreadLocalInstance().setForcedLangCode(SiteUtils.getDefaultSite().getDefaultLanguage().getCode());
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
		
		// PersistenceManager.cleanup();
	}
	
}

