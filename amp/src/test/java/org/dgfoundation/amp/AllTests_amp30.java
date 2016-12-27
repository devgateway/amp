/**
 * 
 */
package org.dgfoundation.amp;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.digijava.kernel.ampapi.endpoints.security.UsersTest;
import org.dgfoundation.amp.ar.viewfetcher.InternationalizedViewsRepository;
import org.dgfoundation.amp.mondrian.monet.MonetConnection;
import org.digijava.kernel.persistence.HibernateClassLoader;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.util.DigiConfigManager;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.kernel.util.resource.ResourceStreamHandlerFactory;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * The main entry point for AMP 3.0 tests.Initializes standalone AMP as part of the discovery process. <br />
 * Standalone AMP configuration is taken off standAloneAmpHibernate.cfg.xml <br />
 * 
 * @author Nadejda Mandrescu
 */
public class AllTests_amp30 {
    
    public static Test suite() {
        setUp();
        
        TestSuite suite = new TestSuite(AllTests_amp30.class.getName());
        suite.addTest(new JUnit4TestAdapter(UsersTest.class));
        
        return suite;
    }
    
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    
    public static void configureLog4j() {
        BasicConfigurator.configure();
        LogManager.getRootLogger().setLevel(Level.ERROR);
    }
    
    /**
     * set to true once hibernate has been initialized
     */
    private static boolean SETUP = false;
    
    public static void setUp() {
        try {
            if (SETUP)
                return;
            configureLog4j();
            HibernateClassLoader.HIBERNATE_CFG_XML = "/standAloneAmpHibernate.cfg.xml";
            HibernateClassLoader.HIBERNATE_CFG_OVERRIDE_DATABASE = "jdbc:postgresql://localhost:5433/amp_tests_30";
            MonetConnection.MONET_CFG_OVERRIDE_URL = "jdbc:monetdb://localhost/amp_tests_30";
            
            org.digijava.kernel.ampapi.mondrian.util.Connection.IS_TESTING = true;
            ResourceStreamHandlerFactory.installIfNeeded();

            DigiConfigManager.initialize("./repository");
            PersistenceManager.initialize(false, null);
            TLSUtils.getThreadLocalInstance().setForcedLangCode(SiteUtils.getDefaultSite().getDefaultLanguage().getCode());
            InternationalizedViewsRepository.i18Models.size(); // force init outside of testcases
            org.apache.struts.mock.MockHttpServletRequest mockRequest = new org.apache.struts.mock.MockHttpServletRequest(new org.apache.struts.mock.MockHttpSession());
            TLSUtils.populate(mockRequest, SiteUtils.getDefaultSiteDomain(SiteUtils.getDefaultSite()));
            SETUP = true;
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
