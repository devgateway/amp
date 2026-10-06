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

package org.digijava.module.translation.action;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.config.KeyValuePair;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.util.TrnUtil;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.DigiConfigManager;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.common.action.PaginationAction;
import org.digijava.module.translation.config.TranslationConfig;
import org.digijava.module.translation.form.AdvancedTranslationForm;
import org.digijava.module.translation.security.TranslateSecurityManager;
import org.digijava.module.translation.util.DbUtil;
import org.digijava.module.translation.util.TranslationManager;

import javax.security.auth.Subject;
import java.util.*;

public class ShowAdvancedTranslation
    extends PaginationAction {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {
        AdvancedTranslationForm formBean = (AdvancedTranslationForm) form;

        Site currentSite = RequestUtils.getSite(request);
        Site rootSite = DgUtil.getRootSite(currentSite);


        if (!rootSite.equals(currentSite)) {
            formBean.setRootSiteId(rootSite.getSiteId());
        }

        Subject subject = RequestUtils.getSubject(request);

        Collection allLanguages = TrnUtil.getLanguages();

        formBean.setLocalTranslationGranted(TranslationManager.
                                            isTranslationPermitted(subject,
            currentSite.getId().longValue(), allLanguages));
        formBean.setGlobalTranslationGranted(TranslationManager.
                                             isTranslationPermitted(subject, 0,
            allLanguages));
        if (formBean.getRootSiteId() != null) {
            formBean.setGroupTranslationGranted(TranslationManager.
                                                isTranslationPermitted(subject,
                rootSite.getId().longValue(), allLanguages));
        } else {
            formBean.setGroupTranslationGranted(false);
        }

        if (! (formBean.isGlobalTranslationGranted() || formBean.isGroupTranslationGranted() || formBean.isLocalTranslationGranted())) {
            throw new SecurityException("Translation is not granted to subject " + subject + " on site " + currentSite.getName());
        }
        //set site identity
        if (formBean.getSwitchToGlobal() != null) {
            formBean.setSelectedPrefix(null);
            formBean.setSearchKey(null);
            formBean.setSearchSource(null);
            formBean.setSearchTarget(null);

            if (formBean.getSwitchToGlobal().equals("true")) {
                formBean.setGlobalTranslation(true);
                formBean.setSiteId(new Long(0));
            }
            else {
                formBean.setGlobalTranslation(false);
                formBean.setSiteId(currentSite.getId());
            }
        }
        if (formBean.getSiteId() == null) {
            formBean.setSiteId(currentSite.getId());
        }

        Map parameters = new HashMap();
        addToParamMap(parameters, "selectedPrefix", formBean.getSelectedPrefix());
        addToParamMap(parameters, "selectedLangSource", formBean.getSelectedLangSource());
        addToParamMap(parameters, "selectedLangTarget", formBean.getSelectedLangTarget());
        addToParamMap(parameters, "showExpired", String.valueOf(formBean.isShowExpired()));
        addToParamMap(parameters, "searchKey", formBean.getSearchKey());
        addToParamMap(parameters, "searchSource", formBean.getSearchSource());
        addToParamMap(parameters, "searchTarget", formBean.getSearchTarget());

        formBean.setParameters(createPrams(parameters));

        String srcLanguage = formBean.getSelectedLangSource() == null ?
            "en" : formBean.getSelectedLangSource();

        String dstLanguage = formBean.getSelectedLangTarget() == null ?
            RequestUtils.getNavigationLanguage(request).getCode() :
            formBean.getSelectedLangTarget();

        formBean.setSelectedLangSource(srcLanguage);
        formBean.setSelectedLangTarget(dstLanguage);

        //get key prefixes
        ArrayList keyPrefixesList = new ArrayList();

        List keyList = null;
        TranslationConfig cfg = (TranslationConfig) DigiConfigManager.
            getConfigBean(
                "translationConfig");

        if (cfg != null) {
            keyList = cfg.getKeys(currentSite.getSiteId());
            if (keyList == null || keyList.size() == 0) {
                keyList = cfg.getKeys(rootSite.getSiteId());
            }
        }

        if (keyList != null && keyList.size() >0) {
            Iterator iter = keyList.iterator();
            while (iter.hasNext()) {
                AdvancedTranslationForm.PrefixInfo pi = new
                    AdvancedTranslationForm.PrefixInfo();
                KeyValuePair item = (KeyValuePair) iter.next();
                pi.setKey(item.getKey());
                pi.setValue(item.getValue());
                keyPrefixesList.add(pi);
            }
            if (formBean.getSelectedPrefix() == null) {
                formBean.setSelectedPrefix( ( (KeyValuePair) keyList.get(0)).
                                           getKey());
            }
        }
        else {
            List dbKeyPrefixesList = DbUtil.getKeyPrefixesList(formBean.
                getSiteId(), formBean.getSelectedLangSource());
            if (dbKeyPrefixesList != null) {
                Iterator iter = (new TreeSet(dbKeyPrefixesList)).iterator();
                while (iter.hasNext()) {
                    AdvancedTranslationForm.PrefixInfo pi = new
                        AdvancedTranslationForm.PrefixInfo();
                    String item = (String) iter.next();
                    pi.setKey(item);
                    pi.setValue(item);
                    keyPrefixesList.add(pi);
                }
            }
            keyPrefixesList.add(0, new AdvancedTranslationForm.PrefixInfo("all"));
        }
        formBean.setKeyPrefixes(keyPrefixesList);
        if (formBean.getSelectedPrefix() != null &&
            formBean.getSelectedPrefix().equals("all")) {
            formBean.setSelectedPrefix(null);
        }

        formBean.setTranslations(TranslationManager.getAdvancedTranslations(
            formBean.getSiteId().longValue(), srcLanguage, dstLanguage,
            formBean.getSelectedPrefix(), formBean.isShowExpired(),
            formBean.getSearchKey(), formBean.getSearchSource(),
            formBean.getSearchTarget()));



        //site name translation
        RequestUtils.setTranslationAttribute(request, "siteName",
                                             currentSite.getName());
        formBean.setSiteName(currentSite.getName());

        formBean.setPermitted(TranslateSecurityManager.checkPermission(subject,
            formBean.getSiteId().longValue(), formBean.getSelectedLangTarget()));

        List sortedLanguages = TranslationManager.getTreeLanguagesToDisplay(
            currentSite, null, false);
        formBean.setLanguages(sortedLanguages);

        sortedLanguages = TranslationManager.getLanguagesToDisplay(
            currentSite, null, false);

        formBean.setDstLanguages(sortedLanguages);

        return mapping.findForward("forward");

    }

    private void addToParamMap(Map params, String name, String value) {
        if (value != null) {
            params.put(name, value);
        }
    }

    private String createPrams(Map params) {
        String retVal = "";
        boolean first = true;
        Iterator iter = params.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry item = (Map.Entry) iter.next();
            if (first) {
                first = false;
            } else {
                retVal += '&';
            }
            retVal += ((String)item.getKey()) + "=" +(String)item.getValue();
        }

        if (retVal.trim().length() != 0) {
            retVal = '?' + retVal;
        }
        ////System.out.println(retVal);
        return retVal;
    }
}
