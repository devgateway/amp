/**
 * 
 */
package org.digijava.kernel.ampapi.mondrian;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.dgfoundation.amp.mondrian.MondrianETL;
import org.digijava.kernel.ampapi.mondrian.util.Connection;
import org.digijava.kernel.ampapi.mondrian.util.MoConstants;
import org.digijava.kernel.ampapi.mondrian.util.MondrianUtils;
import org.digijava.kernel.persistence.HibernateClassLoader;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.util.DigiConfigManager;
import org.digijava.kernel.util.resource.ResourceStreamHandlerFactory;
import org.hibernate.cfg.Configuration;

/**
 * Entry point for Mondrian queries Test suite. Initializes standalone AMP as part of the discovery process.
 * standalone AMP configuration is taken off standAloneAmpHibernate.cfg.xml
 * @author Nadejda Mandrescu
 */
public class AllQueryTests_amp210 {
	private static final String CONNECTION_PATH = ""
			+ "jdbc:mondrian:Jdbc=jdbc:monetdb:" + "//localhost/amp_moldova_210" + ";"
			+ "JdbcUser=monetdb;"
			+ "JdbcPassword=monetdb;"
			+ "JdbcDrivers=nl.cwi.monetdb.jdbc.MonetDriver";
	
	private static String PRINT_PATH = null;

	public static Test suite() {
		
		setUp();
		TestSuite suite = new TestSuite(AllQueryTests_amp210.class.getName());
		//$JUnit-BEGIN$
		suite.addTest(MDXTests.suite());
		suite.addTest(MondrianReportsTests.suite());
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
			//hack for ampDS initialization
			MondrianETL.CONNECTION_DS = CONNECTION_PATH;
			Connection.IS_TESTING = true;
			MondrianUtils.PRINT_PATH = PRINT_PATH;
	   		
			HibernateClassLoader.HIBERNATE_CFG_XML = "/standAloneAmpHibernate.cfg.xml";
			HibernateClassLoader.HIBERNATE_CFG_OVERRIDE_DATABASE = "jdbc:postgresql://localhost/amp_moldova_210";
    	
			ResourceStreamHandlerFactory.installIfNeeded();

			DigiConfigManager.initialize("./repository");
			PersistenceManager.initialize(false, null);
			Configuration cfg = HibernateClassLoader.getConfiguration();
			//System.out.println("AMP started up!");
			//TLSUtils.getThreadLocalInstance().setForcedLangCode(SiteUtils.getDefaultSite().getDefaultLanguage().getCode());
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
	}
}
