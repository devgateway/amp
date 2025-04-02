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
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.SiteCache;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.translation.form.TranslatorForm;
import org.digijava.module.translation.security.TranslateSecurityManager;
import org.digijava.module.translation.taglib.TrnTag;

public class SaveTranslation
    extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {

        TranslatorForm formBean = (TranslatorForm) form;
        Site currentSite;
        
        String backUrl = null;
        try
        {
            backUrl = java.net.URLDecoder.decode(formBean.getBack_url(),"UTF-8");
        }catch (Exception ex){
            // need logger.
            backUrl = formBean.getBack_url();
        }
        
        backUrl = backUrl.replace("/translation/default", "/translation");
        
        if (formBean.getSiteId()!= null && formBean.getSiteId().trim().length() != 0) {
            currentSite = SiteCache.lookupByName(formBean.getSiteId());
        } else {
            currentSite= RequestUtils.getSite(request);;
        }
        boolean isLocalTranslator = false;
        boolean permitted = true;

        Long siteId = new Long(0);
        if (formBean.getType().equals(TrnTag.LOCAL_TRANSLATION)) {
            siteId = currentSite.getId();
            isLocalTranslator = DgUtil.isLocalTranslatorForSite(request);
        }
        else if (formBean.getType().equals(TrnTag.GROUP_TRANSLATION)) {
            siteId = DgUtil.getRootSite(currentSite).getId();
        }
        //
        String langCode = RequestUtils.getNavigationLanguage(request).getCode();
        if (!isLocalTranslator) {
            permitted = TranslateSecurityManager.isTranslationPermittedForSite(
                request, siteId,
                langCode);
        }
        formBean.setPermitted(permitted);

        if (permitted) {
            Message msg = TranslatorWorker.getInstance(formBean.getKey()).
                getByKey(formBean.getKey(), langCode, siteId);
            if (msg != null) {
                if (formBean.getDeleteTranslation() != null) {
                    TranslatorWorker.getInstance(formBean.getKey()).delete(msg);
                }
                else {
                    msg.setMessage(formBean.getTranslation());
                    TranslatorWorker.getInstance(formBean.getKey()).update(msg);
                }
            }
            else {
                Message newMsg = new Message();

                newMsg.setSiteId(siteId.toString());
                newMsg.setMessage(formBean.getTranslation());
                newMsg.setKey(formBean.getKey());
                newMsg.setLocale(langCode);

                TranslatorWorker.getInstance(formBean.getKey()).save(newMsg);
            }
            return new ActionForward(backUrl, true);
        }

        return new ActionForward(backUrl, true);

    }
}
