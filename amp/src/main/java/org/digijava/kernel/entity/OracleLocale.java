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

package org.digijava.kernel.entity;

/**
 * This class was created for WORKAROUND compatibility issue between DiGi and
 * the old, TCL-based system. It provides mapping between Oracle and DiGi
 * locales
 */
public class OracleLocale {

    private String oracleLocale;
    private Locale locale;
    private String languageName;

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getOracleLocale() {
        return oracleLocale;
    }

    public String getLanguageName() {

        return languageName;
    }

    public void setOracleLocale(String oracleLocale) {
        this.oracleLocale = oracleLocale;
    }

    public void setLanguageName(String languageName) {

        this.languageName = languageName;
    }
}
