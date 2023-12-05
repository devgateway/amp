/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
*/
package org.dgfoundation.amp.onepager.translation;

import org.apache.log4j.Logger;
import org.apache.wicket.Session;
import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.SiteUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author mpostelnicu@dgateway.org
 * @since Nov 18, 2010
 */
public final class TranslatorUtil {
    public static Logger logger = Logger.getLogger(TranslatorUtil.class);

    public static List<String> localeCache;
    public static String defaultLocaleCache;

    public static boolean isTranslatorMode(Session session) {
        AmpAuthWebSession ampSession=(AmpAuthWebSession) session;
        return ampSession.isTranslatorMode();
    }
    
    public static String getTranslatedText(String string){
        return getTranslation(string);
    }
    
    public static String getTranslation(String strTrn){
        String genKey = TranslatorWorker.generateTrnKey(strTrn);
        String translatedValue=strTrn;
        AmpAuthWebSession session = (AmpAuthWebSession) Session.get();
        Site site = session.getSite();
        try {
            translatedValue = TranslatorWorker.getInstance(genKey).
                                    translateFromTree(genKey, site, session.getLocale().getLanguage(), 
                                            strTrn, TranslatorWorker.TRNTYPE_LOCAL, null);
        } catch (WorkerException e) {
            logger.error("Can't translate:", e);
            return strTrn;
        }
        return translatedValue;
    }

    /**
     * only works if called from within Wicket
     * @return
     */
    public static List<String> getLocaleCache() {
        AmpAuthWebSession session = (AmpAuthWebSession) Session.get();
        return getLocaleCache(session.getSite());
    }

    /**
     * fast, caches the result of the first call
     * @param site
     * @return
     */
    public synchronized static List<String> getLocaleCache(Site site) {
        if (localeCache == null) {
            Set<Locale> list = site.getTranslationLanguages();
            localeCache = new ArrayList<String>();
            for (Locale loc: list) {
                localeCache.add(loc.getCode());
            }
            Collections.sort(localeCache);
        }
        return localeCache;
    }
    
    public static List<String> getLanguages() {
        return getLocaleCache(SiteUtils.getDefaultSite());
    }

    public static String getDefaultLocaleCache(){
        if(defaultLocaleCache==null){
            AmpAuthWebSession session = (AmpAuthWebSession) Session.get();
            Site site = session.getSite();
            defaultLocaleCache=site.getDefaultLanguage().getCode();
        }
        return defaultLocaleCache;
    }
    
    public static void insertAvailableLanguages(javax.servlet.http.HttpServletRequest request) {
        request.setAttribute("all_locales", getLocaleCache(RequestUtils.getSite(request)));
    }

}
