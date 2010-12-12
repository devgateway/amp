/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
*/
package org.dgfoundation.amp.onepager.translation;

import org.apache.wicket.Session;
import org.dgfoundation.amp.onepager.AmpWebSession;

/**
 * @author mpostelnicu@dgateway.org
 * @since Nov 18, 2010
 */
public final class TranslatorUtil {

	public static boolean isTranslatorMode(Session session) {
		AmpWebSession ampSession=(AmpWebSession) session;
		return ampSession.isTranslatorMode();
	}
	
	public static String getTranslatedText(String string){
		return string;
	}
	
}
