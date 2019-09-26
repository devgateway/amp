package org.digijava.kernel.ampapi.endpoints.activity;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.struts.mock.MockHttpServletRequest;
import org.apache.struts.mock.MockHttpSession;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.request.TLSUtils;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * @author Octavian Ciubotaru
 */
public class AMPRequestRule implements TestRule {

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {

            @Override
            public void evaluate() throws Throwable {
                try {
                    populateRequest();
                    base.evaluate();
                } finally {
                    cleanupRequest();
                }
            }
        };
    }

    private void populateRequest() {
        MockHttpServletRequest mockRequest = new MockHttpServletRequest(new MockHttpSession());

        Site site = new Site("Test Site", "1");
        site.setDefaultLanguage(new Locale("en", "English"));

        SiteDomain siteDomain = new SiteDomain();
        siteDomain.setSite(site);
        siteDomain.setDefaultDomain(true);

        Set<String> trnCodes = Collections.singleton("en");
        mockRequest.setAttribute(EPConstants.TRANSLATIONS, new TranslationSettings("en", trnCodes, false));
    
        TLSUtils.populate(mockRequest, siteDomain);

        TranslationSettings defaultTranslationSettings = new TranslationSettings("en", trnCodes, false);
        TranslationSettings.setDefaultOverride(defaultTranslationSettings);
    }

    private void cleanupRequest() {
        TLSUtils.clean();

        TranslationSettings.setDefaultOverride(null);
    }

    public void enableMultilingual() {
        Set<String> trnCodes = new HashSet<>(Arrays.asList("en", "fr"));
        TLSUtils.getRequest().setAttribute(EPConstants.TRANSLATIONS, new TranslationSettings("en", trnCodes, true));

        TranslationSettings defaultTranslationSettings = new TranslationSettings("en", trnCodes, true);
        TranslationSettings.setDefaultOverride(defaultTranslationSettings);
    }
}
