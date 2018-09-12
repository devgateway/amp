package org.dgfoundation.amp;

import java.util.HashSet;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.struts.mock.MockHttpServletRequest;
import org.apache.struts.mock.MockHttpSession;
import org.dgfoundation.amp.ar.viewfetcher.InternationalizedViewsRepository;
import org.dgfoundation.amp.mondrian.monet.MonetConnection;
import org.digijava.kernel.ampapi.endpoints.activity.TranslationSettings;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.kernel.content.ContentRepositoryManager;
import org.digijava.kernel.persistence.HibernateClassLoader;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.util.DigiConfigManager;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.kernel.util.resource.ResourceStreamHandlerFactory;

/**
 * Initializes standalone AMP for testing purposes.<br />
 * Standalone AMP configuration is taken off standAloneAmpHibernate.cfg.xml <br />
 * 
 * @author Nadejda Mandrescu
 */
public class StandaloneAMPInitializer {
    
    /**
     * set to true once hibernate has been initialized
     */
    private static boolean SETUP = false;
    
    public static synchronized void initialize() {
        try {
            if (SETUP) {
                return;
            }

            configureLog4j();
            HibernateClassLoader.HIBERNATE_CFG_XML = "/standAloneAmpHibernate.cfg.xml";
            MonetConnection.MONET_CFG_OVERRIDE_URL = "jdbc:monetdb://localhost/amp_tests_212";
            
            org.digijava.kernel.ampapi.mondrian.util.Connection.IS_TESTING = true;
            ResourceStreamHandlerFactory.installIfNeeded();

            DigiConfigManager.initialize("./repository");
            PersistenceManager.initialize(false, null);
            ContentRepositoryManager.initialize();
            
            TLSUtils.getThreadLocalInstance().setForcedLangCode(SiteUtils.getDefaultSite().getDefaultLanguage().getCode());
            InternationalizedViewsRepository.i18Models.size(); // force init outside of testcases

            populateMockRequest();
            configureMockTranslationRequest();

            SETUP = true;
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static MockHttpServletRequest populateMockRequest() {
        MockHttpServletRequest mockRequest = new MockHttpServletRequest(new MockHttpSession());
        TLSUtils.populate(mockRequest, SiteUtils.getDefaultSiteDomain(SiteUtils.getDefaultSite()));
        return mockRequest;
    }
    
    public static void configureMockTranslationRequest() {
        TranslationSettings trnSettings = new TranslationSettings(TLSUtils.getEffectiveLangCode(), 
                new HashSet<>(SiteUtils.getUserLanguagesCodes(TLSUtils.getSite())));
        TLSUtils.getRequest().setAttribute(EPConstants.TRANSLATIONS, trnSettings);
    }

    private static void configureLog4j() {
        BasicConfigurator.configure();
        LogManager.getRootLogger().setLevel(Level.ERROR);
    }
}
