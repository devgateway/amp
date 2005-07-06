/*
 *   TranslationInfo.java
 * 	 @Author Lasha Dolidze lasha@digijava.org
 * 	 Created: Jan 14, 2004
 * 	 CVS-ID: $Id: TranslationInfo.java,v 1.1 2005-07-06 10:34:20 rahul Exp $
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
package org.digijava.module.translation.form;

import org.digijava.kernel.util.SiteUtils;

public class TranslationInfo
      implements Comparable {
    private String langName;
    private String langCode;
    private String key;
    private String message;
    private String siteDomain;
    private String sitePath;


    public TranslationInfo() {
    }

    public TranslationInfo(String code, String name, String trnName,
			   String siteDbDomain, String sitePath) {
	this.langCode = code;
	if (trnName != null && trnName.length() > 0) {
	    this.langName = trnName;
	}
	else {
	    this.langName = name;
	}
	this.siteDomain = siteDbDomain == null ? null :
	      SiteUtils.prefixDomainName(siteDbDomain);
	this.sitePath = sitePath;
    }

    public TranslationInfo(String code, String key, String message) {

	this.langCode = code;
	this.key = key;
	this.message = message;
    }


    public String getLangCode() {
	return langCode;
    }

    public void setLangCode(String langCode) {
	this.langCode = langCode;
    }

    public String getLangName() {
	return langName;
    }

    public void setLangName(String langName) {
	this.langName = langName;
    }

    public String getKey() {
	return key;
    }

    public void setKey(String key) {
	this.key = key;
    }

    public int compareTo(Object o) {
	return this.langCode.compareTo( ( (TranslationInfo) o).langCode);
    }

    public String getSiteDomain() {
	return siteDomain;
    }

    public void setSiteDomain(String siteDomain) {
	this.siteDomain = siteDomain;
    }

    public String getSitePath() {
	return sitePath;
    }

    public void setSitePath(String sitePath) {
	this.sitePath = sitePath;
    }

    public String getMessage() {
	return message;
    }

    public void setMessage(String message) {
	this.message = message;
    }
}