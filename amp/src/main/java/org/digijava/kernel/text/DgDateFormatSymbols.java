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

package org.digijava.kernel.text;

import java.util.Locale;

import org.digijava.kernel.text.parser.LocaleData;
import org.digijava.kernel.text.parser.LocaleParser;

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
