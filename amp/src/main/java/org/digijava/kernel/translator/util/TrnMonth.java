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

import java.util.GregorianCalendar;
import java.util.HashMap;

/**
 * Wrapper object for cached months, translated to particular language
 * @see TrnUtil
 */
public class TrnMonth {

    private static final HashMap monthCodes;

    static {
        monthCodes = new HashMap();
        monthCodes.put("jan", new Integer(GregorianCalendar.JANUARY));
        monthCodes.put("feb", new Integer(GregorianCalendar.FEBRUARY));
        monthCodes.put("mar", new Integer(GregorianCalendar.MARCH));
        monthCodes.put("apr", new Integer(GregorianCalendar.APRIL));
        monthCodes.put("may", new Integer(GregorianCalendar.MAY));
        monthCodes.put("jun", new Integer(GregorianCalendar.JUNE));
        monthCodes.put("jul", new Integer(GregorianCalendar.JULY));
        monthCodes.put("aug", new Integer(GregorianCalendar.AUGUST));
        monthCodes.put("sep", new Integer(GregorianCalendar.SEPTEMBER));
        monthCodes.put("oct", new Integer(GregorianCalendar.OCTOBER));
        monthCodes.put("nov", new Integer(GregorianCalendar.NOVEMBER));
        monthCodes.put("dec", new Integer(GregorianCalendar.DECEMBER));
    }

    private String iso;
    private String name;

    /**
     * Construct TrnMonth object
     * @param iso short name of the month("jan", "feb", etc)
     * @param name name of the month. Can be translated on the other languages
     */
    public TrnMonth(String iso, String name) {
        this.iso = iso;
        this.name = name;
        getCode();
    }

    /**
     * Returns numeric presentation of the month. Compiliant with
     * GregorianCalendar class
     * @see {@link java.util.GregorianCalendar}
     * @return
     */
    public int getCode() {
        Integer code = (Integer) monthCodes.get(iso);
        if (code == null) {
            throw new java.lang.IllegalArgumentException(
                "Integer code for month \"" + iso + "\" not found");
        }
        return code.intValue();
    }

    /**
     * Returns short name of the month
     * @return short name of the month
     */
    public String getIso() {
        return iso;
    }

    /**
     * Returns translated name of the month
     * @return translated name of the month
     */
    public String getName() {
        return name;
    }

    public boolean equals(Object another) {
        if (another instanceof TrnMonth) {
            return iso.equals( ( (TrnMonth) another).iso);
        }
        else {
            return false;
        }
    }

    public int hashCode() {
        return iso.hashCode();
    }

}
