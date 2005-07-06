/*
 *   TrnLocale.java
 *   @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Nov 5, 2003
 * 	 CVS-ID: $Id: TrnLocale.java,v 1.1 2005-07-06 10:34:20 rahul Exp $
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
