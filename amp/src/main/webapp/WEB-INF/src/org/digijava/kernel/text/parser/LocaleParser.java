/**
 * This file is part of DiGi project (www.digijava.org).
 * DiGi is a multi-site portal system written in Java/J2EE.
 *
 * Copyright (C) 2002-2007 Development Gateway Foundation, Inc.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 */

package org.digijava.kernel.text.parser;

import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.Hashtable;
import java.util.Locale;

import org.apache.commons.digester.Digester;
import org.apache.log4j.Logger;

public class LocaleParser {

    private static Hashtable cachedLocaleData = new Hashtable(3);
    private static final String LOCALE_DEF =
        "org/digijava/kernel/text/data/LocaleData";
    private static Digester digester;
    private static Logger logger = Logger.getLogger(LocaleParser.class);

    private static String registrations[];

    static {
        registrations = new String[] {
            "-//Digijava Project//DTD Locale Configuration 1.0//EN",
            "/org/digijava/kernel/text/data/LocaleData_1_0.dtd",
        };
        digester = createDigester();

    }

    private static Digester createDigester() {
        Digester digester = new Digester();
        for (int i = 0; i < registrations.length; i += 2) {
            URL url = LocaleParser.class.getResource(registrations[i + 1]);
            if (url != null) {
                digester.register(registrations[i], url.toString());
            }
        }

        // Turn off validation against DTD
        digester.setValidating(true);
        // Workaround Tomcat's issue with ClassLoader
        digester.setUseContextClassLoader(true);

        // Configure digester
        digester.addObjectCreate("locale", LocaleData.class);
        digester.addCallMethod("locale/months/month", "addMonth", 0);
        digester.addCallMethod("locale/short-months/short-month", "addShortMonth", 0);
        digester.addCallMethod("locale/days/day", "addWeekDay", 0);
        digester.addCallMethod("locale/short-days/short-day", "addShortWeekDay", 0);
        digester.addCallMethod("locale/eras/era", "addEra", 0);
        digester.addCallMethod("locale/ampms/ampm", "addAmpm", 0);
        digester.addCallMethod("locale/date-patterns/date-pattern", "addDatePattern", 0);
        digester.addCallMethod("locale/time-patterns/time-pattern", "addTimePattern", 0);
        digester.addCallMethod("locale/date-time-pattern", "setDateTimePattern", 0);
        digester.addSetProperties("locale/date-patterns", "default", "defaultDateStyle");
        digester.addSetProperties("locale/time-patterns", "default", "defaultTimeStyle");

        return digester;
    }

    public static LocaleData getLocaleData(Locale locale) {
        SoftReference ref = (SoftReference)cachedLocaleData.get(locale);
        LocaleData cachedData;
        if (ref == null || (cachedData = (LocaleData)ref.get()) == null) {
            ClassLoader currentLoader = LocaleParser.class.getClassLoader();
            InputStream is = currentLoader.getResourceAsStream(LOCALE_DEF + "_" + locale.getLanguage() + "_" + locale.getCountry() + ".xml");
            if (is == null) {
                is = currentLoader.getResourceAsStream(LOCALE_DEF + "_" + locale.getLanguage() + ".xml");
                if (is == null) {
                    is = currentLoader.getResourceAsStream(LOCALE_DEF + ".xml");
                }
            }
            try {
                cachedData = (LocaleData) digester.parse(is);
            }
            catch (Exception ex) {
                logger.debug("Unable to parse locale definition for " + locale,ex);
                throw new RuntimeException("Unable to parse locale definition for " + locale, ex);
            }
            cachedData.prepareData();
            ref = new SoftReference(cachedData);
            cachedLocaleData.put(locale, ref);
        }
        return cachedData;

    }
}
