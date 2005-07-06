/*
 *   EditorAdminForm.java
 * 	 @Author George Kvizhinadze gio@digijava.org
 * 	 Created: April 8, 2004
 *   CVS-ID: $Id: EditorAdminForm.java,v 1.1 2005-07-06 10:34:23 rahul Exp $
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

import org.apache.struts.action.ActionForm;
import java.util.List;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import java.util.regex.Pattern;
import org.apache.struts.action.ActionError;

public class EditorAdminForm extends ActionForm {
    private List siteEditors;
    private String refUrl;
    private String key;
    private String value;
    private String title;
    private String lang;
    private String[] multiboxList;
    private String moveDir;

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        multiboxList = null;
    }

    public ActionErrors validate(ActionMapping mapping,
                                 HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        if (mapping.getType().
            equals("org.digijava.module.editor.action.admin.AddEditor")) {
            String matcherPatern = "[-a-zA-Z_0-9_{:_}]++";
            boolean isLegal = key.matches(matcherPatern);

            if (!isLegal) {
                errors.add("editor",
                           new ActionError(
                    "error.editor.ilegalKey"));
            }
        }

        return errors;
    }

    public List getSiteEditors() {
        return siteEditors;
    }
    public void setSiteEditors(List siteEditors) {
        this.siteEditors = siteEditors;
    }
    public String getRefUrl() {
        return refUrl;
    }
    public void setRefUrl(String refUrl) {
        this.refUrl = refUrl;
    }
    public String[] getMultiboxList() {
        return multiboxList;
    }
    public void setMultiboxList(String[] multiboxList) {
        this.multiboxList = multiboxList;
    }
    public String getMoveDir() {
        return moveDir;
    }
    public void setMoveDir(String moveDir) {
        this.moveDir = moveDir;
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
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
  }
}