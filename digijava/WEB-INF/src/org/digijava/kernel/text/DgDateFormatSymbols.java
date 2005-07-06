/*
 *   DgDateFormatSymbols.java
 * 	 @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Nov 25, 2003
 * 	 CVS-ID: $Id: DgDateFormatSymbols.java,v 1.1 2005-07-06 10:34:08 rahul Exp $
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

import java.util.Locale;
import org.digijava.kernel.text.parser.LocaleParser;
import org.digijava.kernel.text.parser.LocaleData;

public class DgDateFormatSymbols
    extends java.text.DateFormatSymbols {

    public DgDateFormatSymbols() {
        super();
        initializeFields(Locale.getDefault());
    }

    public DgDateFormatSymbols(Locale locale) {
        super(locale);
        initializeFields(locale);
    }
    private void initializeFields(Locale locale) {
        LocaleData localeData = LocaleParser.getLocaleData(locale);

        this.setEras(localeData.getEras());
        this.setAmPmStrings(localeData.getAmpms());
        this.setMonths(localeData.getMonths());
        this.setShortMonths(localeData.getShortMonths());
        this.setShortWeekdays(localeData.getShortWeekdays());
        this.setWeekdays(localeData.getWeekdays());
        //this.setZoneStrings(systemSymbols.getZoneStrings());
        //this.setLocalPatternChars(systemSymbols.getLocalPatternChars());
    }
}