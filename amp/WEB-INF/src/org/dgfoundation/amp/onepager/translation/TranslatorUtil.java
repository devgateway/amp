/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
*/
package org.dgfoundation.amp.onepager.translation;

import org.apache.log4j.Logger;
import org.apache.wicket.Session;
import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import java.util.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author mpostelnicu@dgateway.org
 * @since Nov 18, 2010
 */
public final class TranslatorUtil {
	public static Logger logger = Logger.getLogger(TranslatorUtil.class);

    public static List<String> localeCache;

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

    public static List<String> getLocaleCache(){
        if (localeCache == null){
            try{
                AmpAuthWebSession session = (AmpAuthWebSession) Session.get();
                Site site = session.getSite();
                Set<Locale> list = site.getTranslationLanguages();
                localeCache = new ArrayList<String>();
                for (Locale loc: list){
                    localeCache.add(loc.getCode());
                }
                Collections.sort(localeCache);
            } finally {
            }
        }

        return localeCache;
    }

}
