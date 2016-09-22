package org.digijava.module.translation.exotic;

import java.util.Locale;

import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.common.util.DateTimeUtil;

public class AmpDateFormatterFactory {
	
	private static boolean isLangCodeSupported(String langCode) {
		Locale[] supportedLocales = Locale.getAvailableLocales();
		for (Locale supLoc : supportedLocales) {
			if (langCode.equals(supLoc.getLanguage())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Gets the default formatter if no locale is specified. 
	 * The default is English to avoid weird behaviour in the case of 
	 * non-English systems.
	 * @return
	 */
	public static AmpDateFormatter getDefaultFormatter() {
		return new AmpSimpleDateFormatter(DateTimeUtil.getGlobalPattern(), Locale.ENGLISH);
	}
	
	/**
	 * Gets a localized formatter with the default pattern.
	 * @return
	 */
	public static AmpDateFormatter getLocalizedFormatter() {
		return getLocalizedFormatter(DateTimeUtil.getGlobalPattern());
	}

	/**
	 * Gets a localized formatter with a specified pattern.
	 * @param format
	 * @return
	 */
	public static AmpDateFormatter getLocalizedFormatter(String format) {
		String langCode = TLSUtils.getEffectiveLangCode();
		if (isLangCodeSupported(langCode))
			return new AmpSimpleDateFormatter(format, langCode);
		else 
			return new ExoticDateFormatter(format, langCode);
	}
	

}
