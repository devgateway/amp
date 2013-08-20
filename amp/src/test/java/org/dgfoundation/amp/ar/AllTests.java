package org.dgfoundation.amp.ar;

//import org.dgfoundation.amp.testutils.LiberiaFiller;
import org.digijava.kernel.persistence.HibernateClassLoader;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.util.DigiConfigManager;
import org.digijava.kernel.util.resource.ResourceStreamHandlerFactory;
import org.hibernate.cfg.Configuration;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * entry point for AMP tests. Initializes standalone AMP as part of the discovery process.
 * standalone AMP configuration is taken off standAloneAmpHibernate.cfg.xml
 * @author Dolghier Constantin
 *
 */
public class AllTests {

	public static Test suite() {
		
		setUp();
//		LiberiaFiller.fillInDatabase();
		TestSuite suite = new TestSuite(AllTests.class.getName());
		//$JUnit-BEGIN$

		//suite.addTestSuite(DirectedDisbursementsTests.class);
		suite.addTest(DirectedDisbursementsTests.suite());
		suite.addTest(MtefTests.suite());
		suite.addTest(ComputedMeasuresTests.suite());
		suite.addTest(MiscColumnsTests.suite());
		//suite.addTest(new DirectedDisbursementsTests("testReports"));
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
    	
			ResourceStreamHandlerFactory.installIfNeeded();

			DigiConfigManager.initialize("./repository");
			PersistenceManager.initialize(false, null);
			Configuration cfg = HibernateClassLoader.getConfiguration();
			System.out.println("AMP started up!");
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
		
		// PersistenceManager.cleanup();
	}
	
}

