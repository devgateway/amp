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

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class TranslatorForm
    extends ActionForm {

    private String key;
    private String message;
    private String translation;
    private String groupTranslation;
    private String globalTranslation;
    private String deleteTranslation;
    private String siteId;

    private String type;

    /**
     * target language's Code and Key
     */
    private String langCode;
    private String langKey;

    private String back_url;
    private String siteName;

    private boolean permitted;

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getTranslation() {
        return translation;
    }

    public String getLangCode() {
        return langCode;
    }

    public void setLangCode(String langCode) {
        this.langCode = langCode;
    }

    public String getLangKey() {
        return langKey;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isPermitted() {
        return permitted;
    }

    public void setPermitted(boolean permitted) {
        this.permitted = permitted;
    }

    public String getBack_url() {
        return back_url;
    }

    public void setBack_url(String back_url) {
        this.back_url = back_url;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        permitted = true;
        deleteTranslation = null;
        siteId = null;
    }

    public String getGlobalTranslation() {
        return globalTranslation;
    }

    public void setGlobalTranslation(String globalTranslation) {
        this.globalTranslation = globalTranslation;
    }

    public String getGroupTranslation() {
        return groupTranslation;
    }

    public void setGroupTranslation(String groupTranslation) {
        this.groupTranslation = groupTranslation;
    }

    public String getDeleteTranslation() {
        return deleteTranslation;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setDeleteTranslation(String deleteTranslation) {
        this.deleteTranslation = deleteTranslation;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }
}
