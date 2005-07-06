/*
 *   OracleLocale.java
 *   @Author Mikheil Kapanadze mikheil@digijava.org
 *   Created: Feb 18, 2004
 *   CVS-ID: $Id: OracleLocale.java,v 1.1 2005-07-06 10:34:08 rahul Exp $
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
package org.digijava.kernel.entity;

/**
 * This class was created for WORKAROUND compatibility issue between DiGi and
 * the old, TCL-based system. It provides mapping between Oracle and DiGi
 * locales
 */
public class OracleLocale {

    private String oracleLocale;
    private Locale locale;

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getOracleLocale() {
        return oracleLocale;
    }

    public void setOracleLocale(String oracleLocale) {
        this.oracleLocale = oracleLocale;
    }
}