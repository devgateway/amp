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
package org.digijava.module.editor.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.cache.AbstractCache;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.util.TrnLocale;
import org.digijava.kernel.translator.util.TrnUtil;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.DigiCacheManager;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.editor.dbentity.Editor;
import org.digijava.module.editor.form.EditorForm;
import org.digijava.module.editor.util.Constants;
import org.digijava.module.editor.util.DbUtil;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

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
            javax.servlet.http.HttpServletResponse response) throws java.lang.Exception {
        
        HttpSession session = request.getSession();

        String name=(String)session.getAttribute("activityName");
        String fieldName=(String)session.getAttribute("activityFieldName");
        //clear session from sdm item if language is changed
        if(session.getAttribute("document")!=null){
            session.removeAttribute("document");
        }
        EditorForm formBean = (EditorForm) form;
        formBean.setActivityName(name);
        formBean.setActivityFieldName(fieldName);
        List dbEditorList = null;
        Editor editor = null;

        if (formBean.getLangCode() != null && formBean.getLangCode().trim().length() != 0) {
            formBean.setLang(formBean.getLangCode());
        }

        User user = RequestUtils.getUser(request);
        ModuleInstance moduleInstance = RequestUtils.getRealModuleInstance(request);
        Site site;
        if (moduleInstance == null) {
            site = RequestUtils.getSite(request);
        } else {
            site = moduleInstance.getSite();
        }

        String key = formBean.getKey();

        if (user != null) {

            //get languages list
            Collection languages = TrnUtil.getLanguages(RequestUtils.getNavigationLanguage(request).getCode());
            //------
            String currentLang = RequestUtils.getNavigationLanguage(request).getCode();
            if (formBean.getLangCode() != null) {
                currentLang = formBean.getLangCode();
            }

            if ( (key != null) && (currentLang != null)) {
                //get editor
                editor = DbUtil.getEditor(site,
                        key, currentLang);

                //get editor list for other languages
                dbEditorList = DbUtil.getEditorList(moduleInstance.getSite(), key, currentLang);
/////////////////////////////////////
                if(dbEditorList.isEmpty() )
                    dbEditorList = null;
                
                if (editor == null) { //create editor
                    Editor editorKey = new Editor();
                    editorKey.setEditorKey(key);
                    editorKey.setSite(site);
                    editorKey.setLanguage(SiteUtils.getDefaultLanguages(site).getCode());
                    AbstractCache cache = DigiCacheManager.getInstance().getCache(Constants.TAG_BODY_CACHE);
                    editor = (Editor)cache.get(editorKey);
                    if (editor == null) {
                        editor = editorKey;
                    }

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
                            ti.setUserName(ur.getName());
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

            if (formBean.getLang() == null) {
                formBean.setLang(editor.getLanguage());
            }
            formBean.setContent(editor.getBody());
            formBean.setKey(editor.getEditorKey());
            formBean.setTitle(editor.getTitle());

            formBean.setEditor(editor);

        } else {
            formBean.setContent("default");
        }

        return mapping.findForward("forward");
    }
}
