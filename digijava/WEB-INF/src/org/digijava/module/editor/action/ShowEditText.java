/*
 *   ShowEditText.java
 *   @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: Dec 17, 2003
 * 	 CVS-ID: $Id: ShowEditText.java,v 1.1 2005-07-06 10:34:12 rahul Exp $
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

package org.digijava.module.editor.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.translator.util.TrnLocale;
import org.digijava.kernel.translator.util.TrnUtil;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.DgUtil;
import org.digijava.module.editor.dbentity.Editor;
import org.digijava.module.editor.form.EditorForm;
import org.digijava.module.editor.util.DbUtil;

import java.net.URLDecoder;
import org.digijava.kernel.util.RequestUtils;

/**
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ShowEditText
    extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {

        EditorForm formBean = (EditorForm) form;

        List dbEditorList = null;
        Editor editor = null;

        String key = request.getParameter("id");
//        key = URLDecoder.decode(key, "UTF-8");
        String url = null;
        String body = null;

        User user = RequestUtils.getUser(request);

        if (user != null) {

            //get languages list
            Collection languages = TrnUtil.getLanguages(RequestUtils.getNavigationLanguage(request).getCode());
            //------
            String currentLang = RequestUtils.getNavigationLanguage(request).getCode();

            if (key == null) { //
                if (formBean.getEditor() != null) {
                    key = formBean.getEditor().getEditorKey();
                }
                if (formBean.getLangCode() != null) {
                    currentLang = formBean.getLangCode();
                }
            }

            if ( (key != null) && (currentLang != null)) {
                ModuleInstance moduleInstance = DgUtil.getRealModuleInstance(
                    request);
                //get editor
                editor = DbUtil.getEditor(moduleInstance.getSite().getSiteId(),
                                          key, currentLang);

                //get editor list for other languages
                dbEditorList = DbUtil.getEditorList(moduleInstance.getSite().
                    getSiteId(), key, currentLang);

                url = request.getParameter("referrer");

                if (editor == null) { //create editor
                    body = request.getParameter("body");
                    if (url == null && formBean.getEditor() != null) {
                        url = formBean.getEditor().getUrl();
                    }
                    if (body == null && formBean.getEditor() != null) {
                        body = formBean.getEditor().getBody();
                    }
                    if (url != null && body != null) {
                        editor = DbUtil.createEditor(user,
                            currentLang,
                            url, key, null, body, null,
                            request);
                        DbUtil.saveEditor(editor);
                    }
                } else {//update editor
                    editor.setUrl(url);
                    DbUtil.updateEditor(editor);
                }

                //set other editors list
                if (dbEditorList != null) {

                    List editorList = new ArrayList();

                    Iterator iterator = dbEditorList.iterator();
                    while (iterator.hasNext()) {
                        Editor editorItem = (Editor) iterator.next();
                        EditorForm.TextInfo ti = new EditorForm.TextInfo();

                        //set language name
                        Iterator iter = languages.iterator();
                        while (iter.hasNext()) {
                            TrnLocale item = (TrnLocale) iter.next();
                            if (item.getCode().equals(editorItem.getLanguage())) {
                                ti.setLangName(item.getName());
                                break;
                            }
                        }
                        //set language code
                        ti.setLangCode(editorItem.getLanguage());
                        //set content
                        ti.setContent(editorItem.getBody());
                        //set last modification date
                        if (editorItem.getLastModDate() != null) {
                            ti.setLasModDate(DgUtil.formatDate(editorItem.
                                getLastModDate()));
                        }
                        else {
                            ti.setLasModDate(null);
                        }
                        //set user name
                        if (editorItem.getUser() != null) {
                            User ur = editorItem.getUser();
                            ti.setUserName(ur.getFirstNames() + " " +
                                           ur.getLastName());
                        }
                        else {
                            ti.setUserName(null);
                        }
                        //set url
                        ti.setUrl(editorItem.getUrl());
                        editorList.add(ti);
                    }
                    formBean.setEditorList(editorList);
                }
                else {
                    formBean.setEditorList(null);
                }
            }

            //set formBean
            formBean.setLanguages(languages);
            if (editor != null) {
                formBean.setLang(editor.getLanguage());
                if (body == null) {
                    body = editor.getBody();
                }
                formBean.setContent(body);
                formBean.setKey(editor.getEditorKey());
                formBean.setTitle(editor.getTitle());

                formBean.setEditor(editor);
            }
            else {
                formBean.setEditor(null);
            }

        } else {
            formBean.setContent("default");
        }

        return mapping.findForward("forward");
    }
}