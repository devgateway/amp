package org.digijava.kernel.ampapi.authentication;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.visibility.AmpTreeVisibility;
import org.digijava.kernel.ampapi.endpoints.activity.TranslationSettings;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.util.SiteCache;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.glassfish.jersey.server.ContainerRequest;


/***
 * Configures common parameters and validates some requests
 * 
 * @author Diego Dimunzio
 */
public class AuthRequestFilter implements ContainerRequestFilter {

    // Inject request into the filter
    @Context
    private HttpServletRequest httpRequest;
    
    private static final Logger logger = Logger.getLogger(AuthRequestFilter.class);


    
    private void addLanguage(SiteDomain siteDomain) {
        if (httpRequest.getParameter(EPConstants.LANGUAGE) != null) {
            String lang = httpRequest.getParameter(EPConstants.LANGUAGE).toLowerCase();
            if (SiteUtils.getUserLanguagesCodes(siteDomain.getSite()).contains(lang)) {
                Locale locale = new Locale();
                locale.setCode(lang);
                httpRequest.setAttribute(org.digijava.kernel.Constants.NAVIGATION_LANGUAGE, locale);
            }
        }
    }
    
    private void addTranslations(SiteDomain siteDomain) {
        Locale defaultLocale = SiteUtils.getDefaultLanguages(SiteUtils.getDefaultSite());
        String defaultLocaleCode = defaultLocale != null ? defaultLocale.getCode() : null;
        Locale currentLocale = (Locale) httpRequest.getAttribute(org.digijava.kernel.Constants.NAVIGATION_LANGUAGE);
        String currentLocaleCode = currentLocale != null ? currentLocale.getCode() : defaultLocaleCode;
        
        Set<String> translations = new HashSet<String>();
        translations.add(defaultLocaleCode);
        translations.add(currentLocaleCode);
        
        if (httpRequest.getParameter(EPConstants.TRANSLATIONS) != null) {
            String translationsParam = httpRequest.getParameter(EPConstants.TRANSLATIONS).toLowerCase();
            
            if (StringUtils.isNotEmpty(translationsParam)) {
                List<String> requestedTranslations = Arrays.asList(translationsParam.split("\\|")); 
            
                for (String translation : requestedTranslations) {
                    if (SiteUtils.getUserLanguagesCodes(siteDomain.getSite()).contains(translation)) {
                        translations.add(translation);
                    } else {
                        logger.warn("Translation " + translation + " does not exist in amp system");
                    }
                }
            }
        }
        
        TranslationSettings translationBean = new TranslationSettings(currentLocaleCode, translations);
        
        httpRequest.setAttribute(EPConstants.TRANSLATIONS, translationBean);
    }
    
    private void addDefaultTreeVisibility() {
        if (httpRequest.getAttribute(Constants.TEAM_ID) == null
                && httpRequest.getSession().getAttribute("ampTreeVisibility") == null) {
            //old visibility tree will be refreshed later on, thus no need to always recrate it 
            AmpTreeVisibility ampTreeVisibility = new AmpTreeVisibility();
            ampTreeVisibility.buildAmpTreeVisibility(FeaturesUtil.getCurrentTemplate());
            FeaturesUtil.setAmpTreeVisibility(httpRequest.getServletContext(), httpRequest.getSession(), ampTreeVisibility);
        }
    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        SiteDomain siteDomain = null;
        //yet to strip the mainPath dynamically, committed hardcoded for testing purposes
        String mainPath="/rest";
        siteDomain = SiteCache.getInstance().getSiteDomain(httpRequest.getServerName(), mainPath);

        // configure requested language
        addLanguage(siteDomain);

        // configure translastions if exist
        addTranslations(siteDomain);

        addDefaultTreeVisibility();

//        return containerReq;
    }
}
