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

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 *
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class TranslationForm
    extends ActionForm {

    public static class TranslationInfo implements Comparable {
        private String langName;
        private String langCode;
        private String referUrl;
        private String key;

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

        public String getReferUrl() {
            return referUrl;
        }

        public void setReferUrl(String referUrl) {
            this.referUrl = referUrl;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }
        public int compareTo(Object o) {
            return this.langName.compareTo(((TranslationInfo) o).getLangName());
        }

    }

    private List languages;
    private String referUrl;
    private String referrerParameter;

    public List getLanguages() {
        return languages;
    }

    public void setLanguages(List languages) {
        this.languages = languages;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        languages = null;
    }

    public String getReferUrl() {
        return referUrl;
    }

    public String getReferrerParameter() {
        return referrerParameter;
    }

    public void setReferUrl(String referUrl) {
        this.referUrl = referUrl;
    }

    public void setReferrerParameter(String referrerParameter) {
        this.referrerParameter = referrerParameter;
    }

}
