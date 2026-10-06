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
