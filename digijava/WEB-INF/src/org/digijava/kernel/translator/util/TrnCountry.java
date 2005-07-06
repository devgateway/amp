/*
 *   TrnCountry.java
 *   @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Nov 12, 2003
 * 	 CVS-ID: $Id: TrnCountry.java,v 1.1 2005-07-06 10:34:20 rahul Exp $
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
 * Wrapper object for cached countries, translated to particular language
 * @see TrnUtil
 */
public class TrnCountry
    implements Comparable {

    private String iso;
    private String name;

    public TrnCountry(String iso, String name) {
        this.iso = iso;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getIso() {
        return iso;
    }

    public boolean equals(Object another) {
        if (another instanceof TrnCountry) {
            return iso.equals( ( (TrnCountry) another).iso);
        }
        else {
            return false;
        }
    }

    public int hashCode() {
        return iso.hashCode();
    }

    public int compareTo(Object o) {
        if (o instanceof TrnCountry) {
            TrnCountry another = (TrnCountry) o;
            return iso.compareTo(another.iso);
        }
        else {
            return -1;
        }
    }
}
