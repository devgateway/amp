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

package org.digijava.kernel.translator.util;

/**
 * Wrapper object for cached locales, translated to particular language
 * @see TrnUtil
 */
public class TrnLocale
    implements Comparable {

    private String code;
    private String name;


    public TrnLocale(String code, String name, String trnName) {
         this.code = code;
         if( trnName != null && trnName.length() > 0 ) name = trnName;
         this.name = name;
    }

    public TrnLocale(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int hashCode() {
        return code.hashCode();
    }

    public boolean equals(Object another) {
        if (another instanceof TrnLocale) {
            return code.equals( ( (TrnLocale) another).code);
        }
        else {
            return false;
        }
    }

    public int compareTo(Object o) {
        if (o instanceof TrnLocale) {
            TrnLocale another = (TrnLocale) o;
            return code.compareTo(another.code);
        }
        else {
            return -1;
        }
    }
}
