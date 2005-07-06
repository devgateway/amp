/*
 *   EditorForm.java
 *   @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: Dec 17, 2003
 * 	 CVS-ID: $Id: EditorForm.java,v 1.1 2005-07-06 10:34:23 rahul Exp $
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

package org.digijava.module.editor.form;

import java.util.Collection;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.editor.dbentity.Editor;

/**
 *
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class EditorForm
    extends ActionForm {

    public static class TextInfo {

        private String content;
        private String langName;
        private String langCode;
        private String userName;
        private String lastModDate;
        private String url;

        public void setContent(String content) {
            this.content = content;
        }

        public String getContent() {
            return content;
        }

        public void setLangName(String langName) {
            this.langName = langName;
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

        public void setLasModDate(String lastModDate) {
            this.lastModDate = lastModDate;
        }

        public String getLastModDate() {
            return lastModDate;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserName() {
            return userName;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }

    }

    private List editorList;
    private List navBarEditorList;
    private String langCode;

    private String key;
    private String lang;
    private String returnUrl;

    private String title;
    private String content;
    private String notice;

    private Editor editor;

    private Collection languages;

    private String contentEng;

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLangCode() {
        return langCode;
    }

    public void setLangCode(String langCode) {
        this.langCode = langCode;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public Collection getLanguages() {
        return languages;
    }

    public void setLanguages(Collection languages) {
        this.languages = languages;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        if (!mapping.getType().equals("org.digijava.module.editor.action.ShowNavigationBar")) {
            editorList = null;
            langCode = null;

            key = null;
            lang = null;
            returnUrl = null;

            title = null;
            content = null;
            notice = null;

            languages = null;
        }
    }

    public String getContentEng() {
        return contentEng;
    }

    public void setContentEng(String contentEng) {
        this.contentEng = contentEng;
    }

    public List getEditorList() {
        return editorList;
    }

    public void setEditorList(List editorList) {
        this.editorList = editorList;
    }

    public Editor getEditor() {
        return editor;
    }

    public void setEditor(Editor editor) {
        this.editor = editor;
    }
  public List getNavBarEditorList() {
    return navBarEditorList;
  }
  public void setNavBarEditorList(List navBarEditorList) {
    this.navBarEditorList = navBarEditorList;
  }
}