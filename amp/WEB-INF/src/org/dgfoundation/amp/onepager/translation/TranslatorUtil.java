/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
*/
package org.dgfoundation.amp.onepager.translation;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebApplication;
import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;

/**
 * @author mpostelnicu@dgateway.org
 * @since Nov 18, 2010
 */
public final class TranslatorUtil {
	public static Logger logger = Logger.getLogger(TranslatorUtil.class);

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
									translateFromTree(genKey, site.getId().longValue(), session.getLocale().getLanguage(), 
											strTrn, TranslatorWorker.TRNTYPE_LOCAL, null);
		} catch (WorkerException e) {
			logger.error("Can't translate:", e);
			return strTrn;
		}
		return translatedValue;
	}
}
