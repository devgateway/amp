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

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.SiteCache;
import org.digijava.module.translation.form.TranslatorForm;
import org.digijava.module.translation.taglib.TrnTag;

public class ShowTranslate
    extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {

        TranslatorForm formBean = (TranslatorForm) form;

        formBean.setPermitted(true);
        //site name translation
        Site currentSite = RequestUtils.getSite(request);
        Site trnSite;
        if (formBean.getSiteId()!= null && formBean.getSiteId().trim().length() != 0) {
            trnSite = SiteCache.lookupByName(formBean.getSiteId());
        } else {
            trnSite= currentSite;
        }

        RequestUtils.setTranslationAttribute(request, "siteName", currentSite.getName());
        formBean.setSiteName(currentSite.getName());

        //set target language key
        String langCode = RequestUtils.getNavigationLanguage(request).getCode();
        formBean.setLangCode(langCode);
        formBean.setLangKey("ln:" + langCode);

        String key = formBean.getKey();
        if (key != null && formBean.getType() != null) {

            //Site site = RequestUtils.getSite(request);
            Long siteId = trnSite.getId();
             formBean.setGroupTranslation(null);
             formBean.setGlobalTranslation(null);

            if (formBean.getType().equalsIgnoreCase(TrnTag.LOCAL_TRANSLATION)) {
                Long rootSiteId = DgUtil.getRootSite(trnSite).getId();

                String msgGroup = TranslatorWorker.translate(key,
                    formBean.getLangCode(), rootSiteId);
                if (msgGroup != null && msgGroup.trim().length() != 0) {
                    formBean.setGroupTranslation(msgGroup.trim());
                }
            }
            if (formBean.getType().equalsIgnoreCase(TrnTag.GROUP_TRANSLATION)) {
                siteId = DgUtil.getRootSite(trnSite).getId();
            }

            if (!formBean.getType().equalsIgnoreCase(TrnTag.GLOBAL_TRANSLATION)) {
                String msgGlobal = TranslatorWorker.translate(key,
                    formBean.getLangCode(), null);
                if (msgGlobal != null && msgGlobal.trim().length() != 0) {
                    formBean.setGlobalTranslation(msgGlobal.trim());
                }
            }

            //get message in english
            String msg = TranslatorWorker.translate(key, "en", siteId);
            formBean.setMessage(msg);
            //get message in target language
            String msgTarget = TranslatorWorker.translate(key,
                formBean.getLangCode(), siteId);
            if (msgTarget != null && msgTarget.length() != 0) {
                formBean.setTranslation(msgTarget);
            }
            else {
                formBean.setTranslation(key);
            }
        }
        return mapping.findForward("forward");
    }
}
