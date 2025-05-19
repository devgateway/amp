package org.digijava.module.translation.exotic;

import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.common.util.DateTimeUtil;

import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

public class AmpDateFormatterFactory {
    
    /*
     * List of supported locales with custom month names
     * tm - Tetum  
     */
    private static final Set<String> EXOTIC_LOCALE_NAMES = Collections.singleton("tm");
    
    private static boolean isLangCodeSupported(String langCode) {
        Locale[] supportedLocales = Locale.getAvailableLocales();
        for (Locale supLoc : supportedLocales) {
            if (langCode.equals(supLoc.getLanguage()) && !EXOTIC_LOCALE_NAMES.contains(supLoc.getLanguage())) {
                return true;
            }
        }
        return false;
    }
    
    private static Map<Locale, Map<String,AmpDateFormatter>> cachedFormatters = new ConcurrentHashMap<>();
    
    private static AmpDateFormatter getFormatter(Locale locale, String pattern, BiFunction<Locale, String, AmpDateFormatter> instanceGenerator) {
        return cachedFormatters.computeIfAbsent(locale, c -> new ConcurrentHashMap<>())
                .computeIfAbsent(pattern, z -> instanceGenerator.apply(locale, pattern));
    }
    
    /**
     * Gets the default formatter if no locale is specified. 
     * The default is English to avoid weird behaviour in the case of 
     * non-English systems.
     * @return
     */
    public static AmpDateFormatter getDefaultFormatter() {
        return getFormatter(Locale.ENGLISH, DateTimeUtil.getGlobalPattern(), AmpSimpleDateFormatter::new);
    }
    
    /**
     * Gets the default locale, but specified pattern
     * @return
     */
    public static AmpDateFormatter getDefaultFormatter(String pattern) {
        return getFormatter(Locale.ENGLISH, pattern, AmpSimpleDateFormatter::new);
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
     * Locale picked from current request.
     * @param format
     * @return
     */
    public static AmpDateFormatter getLocalizedFormatter(String pattern) {
        String langCode = TLSUtils.getEffectiveLangCode();
        return getLocalizedFormatter(pattern, Locale.forLanguageTag(langCode));
    }
    
    /**
     * Gets a localized formatter with a specified pattern.
     * @param format
     * @return
     */
    public static AmpDateFormatter getLocalizedFormatter(String pattern, Locale locale) {
        if (isLangCodeSupported(locale.getLanguage()))
            return getFormatter(locale, pattern, AmpSimpleDateFormatter::new);
        else
            return getFormatter(locale, pattern, ExoticDateFormatter::new);
    }
}
