/*
 *   TranslateObject.java
 *   @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: Oct 10, 2004
 * 	 CVS-ID: $Id: TranslateObject.java,v 1.1 2005-07-06 10:34:25 rahul Exp $
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

package org.digijava.module.translation.security;

import org.apache.commons.lang.builder.EqualsBuilder;

public class TranslateObject {

    public static final String LOCALE_CODE_ALL = "*";
    public static final String SITE_NAME_GLOBAL = "global";

    private Long siteId;
    private String localeId;

    public TranslateObject() {
    }

    public TranslateObject(Long siteId, String localeId) {
	this.siteId = siteId;
	this.localeId = localeId;
    }

    public String getLocaleId() {
	return localeId;
    }

    public void setLocaleId(String localeId) {
	this.localeId = localeId;
    }

    public Long getSiteId() {
	return siteId;
    }

    public void setSiteId(Long siteId) {
	this.siteId = siteId;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object other) {
	if (! (other instanceof TranslateObject))
	    return false;
	TranslateObject castOther = (TranslateObject) other;
	return new EqualsBuilder()
	      .append(this.getSiteId(), castOther.getSiteId())
	      .append(this.getLocaleId(), castOther.getLocaleId())
	      .isEquals();
    }

}