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

import org.apache.struts.action.ActionForm;
import java.util.List;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import java.util.regex.Pattern;
import org.apache.struts.action.ActionMessage;

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
                           new ActionMessage(
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
