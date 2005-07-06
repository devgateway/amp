/*
 *   TrnMonth.java
 *   @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Nov 12, 2003
 * 	 CVS-ID: $Id: TrnMonth.java,v 1.1 2005-07-06 10:34:20 rahul Exp $
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
package org.digijava.kernel.translator.util;

import java.util.HashMap;
import java.util.GregorianCalendar;

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
