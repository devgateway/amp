/*
 *   LocalizationUtil.java
 *   @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Nov 27, 2003
 * 	 CVS-ID: $Id: LocalizationUtil.java,v 1.1 2005-07-06 10:34:08 rahul Exp $
 *
 *   This file is part of DiGi project (www.digijava.org).
 *   DiGi is a multi-site portal system written in Java/J2EE.
 *
 *   Confidential and Proprietary, Subject to the Non-Disclosure
 *   Agreement, Version 1.0, between the Development Gateway
 *   Foundation, Inc and the Recipient -- Copyright 2001-2004 Development
 *   Gateway Foundation, Inc.
 *
 *   Unauthorized Disclosure Prohibited.
 *
 *************************************************************************/
package org.digijava.kernel.text;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.digijava.kernel.text.parser.LocaleData;
import org.digijava.kernel.text.parser.LocaleParser;

public class LocalizationUtil {

    private static final Set systemLocales;

    static {
        Locale[] installedLocales = Locale.getAvailableLocales();
        systemLocales = new HashSet();
        for (int i = 0; i < installedLocales.length; i++) {
            systemLocales.add(installedLocales[i]);
        }
    }

    /**
     * Gets the date formatter with the default formatting style for the default locale.
     * @return
     */
    public static DateFormat getDateInstance() {
        Locale aLocale = Locale.getDefault();
        if (systemLocales.contains(aLocale)) {
            return DateFormat.getDateInstance();
        }
        else {
            LocaleData localeData = LocaleParser.getLocaleData(aLocale);
            return getCustomDateTimeInstance(localeData.getDefaultDateStyle(),
                                             -1, aLocale);
        }
    }

    public static DateFormat getDateInstance(Locale aLocale) {
        if (systemLocales.contains(aLocale)) {
            /** @todo this is not the 100% correct way */
            return DateFormat.getDateInstance(DateFormat.DEFAULT, aLocale);
        }
        else {
            LocaleData localeData = LocaleParser.getLocaleData(aLocale);
            return getCustomDateTimeInstance(localeData.getDefaultDateStyle(),
                                             -1, aLocale);
        }
    }

    /**
     * Gets the date formatter with the given formatting style for the default locale.
     * @param style
     */
    public static DateFormat getDateInstance(int style) {
        Locale aLocale = Locale.getDefault();
        if (systemLocales.contains(aLocale)) {
            return DateFormat.getDateInstance(style, aLocale);
        }
        else {
            return getCustomDateTimeInstance(style, -1, aLocale);
        }
    }

    /**
         * Gets the date formatter with the given formatting style for the given locale.
     * @param style
     * @param aLocale
     */
    public static DateFormat getDateInstance(int style, Locale aLocale) {
        if (systemLocales.contains(aLocale)) {
            return DateFormat.getDateInstance(style, aLocale);
        }
        else {
            return getCustomDateTimeInstance(style, -1, aLocale);
        }
    }

    /**
     * Gets the date/time formatter with the default formatting style for the
     * default locale.
     */
    public static DateFormat getDateTimeInstance() {
        Locale aLocale = Locale.getDefault();
        if (systemLocales.contains(aLocale)) {
            return DateFormat.getDateTimeInstance();
        }
        else {
            LocaleData localeData = LocaleParser.getLocaleData(aLocale);
            return getCustomDateTimeInstance(localeData.getDefaultDateStyle(),
                                             localeData.getDefaultTimeStyle(),
                                             aLocale);
        }
    }

    public static DateFormat getDateTimeInstance(Locale aLocale) {
        if (systemLocales.contains(aLocale)) {
            return DateFormat.getDateTimeInstance();
        }
        else {
            LocaleData localeData = LocaleParser.getLocaleData(aLocale);
            return getCustomDateTimeInstance(localeData.getDefaultDateStyle(),
                                             localeData.getDefaultTimeStyle(),
                                             aLocale);
        }
    }

    /**
     * Gets the date/time formatter with the given date and time formatting
     * styles for the default locale.
     * @param dateStyle
     * @param timeStyle
     */
   public static DateFormat getDateTimeInstance(int dateStyle, int timeStyle) {
        if (systemLocales.contains(Locale.getDefault())) {
            return DateFormat.getDateTimeInstance(dateStyle, timeStyle);
        }
        else {
            return getCustomDateTimeInstance(dateStyle, timeStyle,
                                             Locale.getDefault());
        }
    }

    /**
     * Gets the date/time formatter with the given formatting styles for the
     * given locale.
     * @param dateStyle
     * @param timeStyle
     * @param aLocale
     */
    public static DateFormat getDateTimeInstance(int dateStyle, int timeStyle,
                                          Locale aLocale) {
        if (systemLocales.contains(aLocale)) {
            return DateFormat.getDateTimeInstance(dateStyle, timeStyle,
                                                  aLocale);
        }
        else {
            return getCustomDateTimeInstance(dateStyle, timeStyle, aLocale);
        }
    }

    /**
     * Get a default date/time formatter that uses the SHORT style for both the
     * date and the time.
     */
    public static DateFormat getShortDateTimeInstance() {
        if (systemLocales.contains(Locale.getDefault())) {
            return DateFormat.getDateTimeInstance();
        }
        else {
            return getCustomDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT,
                                             Locale.getDefault());
        }
    }

    /**
     *
     * @param dateStyle
     * @param timeStyle
     * @param aLocale
     * @return
     */
    private static DateFormat getCustomDateTimeInstance(int dateStyle,
        int timeStyle,
        Locale aLocale) {
        LocaleData localeData = LocaleParser.getLocaleData(aLocale);
        String[] datePatterns = localeData.getDatePatterns();
        String[] timePatterns = localeData.getTimePatterns();
        String pattern;
        if ( (timeStyle >= 0) && (dateStyle >= 0)) {
            Object[] dateTimeArgs = {
                timePatterns[timeStyle],
                datePatterns[dateStyle]};
            pattern = MessageFormat.format(localeData.getDateTimePattern(),
                                           dateTimeArgs);
        }
        else if (timeStyle >= 0) {
            pattern = timePatterns[timeStyle];
        }
        else if (dateStyle >= 0) {
            pattern = datePatterns[dateStyle];
        }
        else {
            throw new IllegalArgumentException(
                "No date or time style specified");
        }

        /** @todo should be cached */
        DateFormatSymbols formatSymbols = new DgDateFormatSymbols(aLocale);
        SimpleDateFormat df = new SimpleDateFormat(pattern, formatSymbols);
        return df;
    }

}