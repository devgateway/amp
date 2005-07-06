/*
 *   TranslationForm.java
 * 	 @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: Jan 14, 2004
 * 	 CVS-ID: $Id: TranslationForm.java,v 1.1 2005-07-06 10:34:20 rahul Exp $
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

import java.util.List;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;

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

    public void setReferUrl(String referUrl) {
        this.referUrl = referUrl;
    }

}