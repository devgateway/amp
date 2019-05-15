package org.digijava.module.aim.helper;

import java.util.Locale;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

public final class EasternArabicUtils {
    
    public static final String CODE_ARABIC_LANGUAGE = "ar";
    public static final String NUMERIC_ARABIC_EXTENSION = "nu-arab";
    
    public static final Map<Character, Character> ARABIC_NUMERALS = new ImmutableMap.Builder<Character, Character>()
            .put('0', '\u0660')
            .put('1', '\u0661')
            .put('2', '\u0662')
            .put('3', '\u0663')
            .put('4', '\u0664')
            .put('5', '\u0665')
            .put('6', '\u0666')
            .put('7', '\u0667')
            .put('8', '\u0668')
            .put('9', '\u0669')
            .build();
    
    private EasternArabicUtils() {
    
    }
    
    public static String convertWesternArabicToEasternArabic(String text) {
        for (Map.Entry<Character, Character> entry : ARABIC_NUMERALS.entrySet()) {
            text = text.replace(entry.getKey(), entry.getValue());
        }

        return text;
    }
    
    public static String convertEasternArabicToWasternArabic(String text) {
        for (Map.Entry<Character, Character> entry : ARABIC_NUMERALS.entrySet()) {
            text = text.replace(entry.getValue(), entry.getKey());
        }
    
        return text;
    }
    
    public static boolean isLocaleEasternArabic(Locale locale) {
        return locale.getLanguage().equals(CODE_ARABIC_LANGUAGE)
                && locale.getExtension(Locale.UNICODE_LOCALE_EXTENSION).equals(NUMERIC_ARABIC_EXTENSION);
    }
    
}
