package org.digijava.module.translation.exotic;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
	
	private static Map<PatternLocalePair, AmpDateFormatter> cachedFormatters = new ConcurrentHashMap<>();
	
	/**
	 * Gets the default formatter if no locale is specified. 
	 * The default is English to avoid weird behaviour in the case of 
	 * non-English systems.
	 * @return
	 */
	public static AmpDateFormatter getDefaultFormatter() {
		PatternLocalePair p = new PatternLocalePair(DateTimeUtil.getGlobalPattern(), Locale.ENGLISH);
		return cachedFormatters.computeIfAbsent(p, s -> new AmpSimpleDateFormatter(p));
	}
	
	/**
	 * Gets the default locale, but specified pattern
	 * @return
	 */
	public static AmpDateFormatter getDefaultFormatter(String pattern) {
		PatternLocalePair p = new PatternLocalePair(pattern, Locale.ENGLISH);
		return cachedFormatters.computeIfAbsent(p, s -> new AmpSimpleDateFormatter(p));
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
		Locale loc = Locale.forLanguageTag(langCode);
		PatternLocalePair p = new PatternLocalePair(format, loc);
		if (isLangCodeSupported(langCode))
			return cachedFormatters.computeIfAbsent(p, s -> new AmpSimpleDateFormatter(p));
		else 
			return cachedFormatters.computeIfAbsent(p, s -> new ExoticDateFormatter(p));
	}
}
