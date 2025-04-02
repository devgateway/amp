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

package org.digijava.module.editor.form;

import java.util.Collection;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
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

    private String title;
    private String content;
    private String notice;

    private Editor editor;

    private Collection languages;

    private String contentEng;
    private String returnUrl;
    
    private String activityName;
    private String activityFieldName;
    
    private FormFile attachment;

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

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
            String editorId = request.getParameter("id");
            if (editorId != null && editorId.trim().length() != 0) {
                editorList = null;
                langCode = null;

                key = editorId;
                lang = null;
                returnUrl = request.getParameter("referrer");

                title = null;
                content = null;
                notice = null;

                languages = null;
            }
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

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setNavBarEditorList(List navBarEditorList) {
    this.navBarEditorList = navBarEditorList;
  }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getActivityFieldName() {
        return activityFieldName;
    }

    public void setActivityFieldName(String activityFieldName) {
        this.activityFieldName = activityFieldName;
    }

    public FormFile getAttachment() {
        return attachment;
    }

    public void setAttachment(FormFile attachment) {
        this.attachment = attachment;
    }
    
}
