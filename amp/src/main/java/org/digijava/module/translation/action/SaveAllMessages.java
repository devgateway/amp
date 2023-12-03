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
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.SiteCache;
import org.digijava.module.translation.form.AdvancedTranslationForm;
import org.digijava.module.translation.security.TranslateSecurityManager;
import org.digijava.module.translation.util.DbUtil;

import java.util.Iterator;
import java.util.List;

public class SaveAllMessages extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response)
            throws java.lang.Exception {

        AdvancedTranslationForm formBean = (AdvancedTranslationForm) form;

        boolean permitted = true;

        if (!DgUtil.isLocalTranslatorForSite(request)) {
            permitted = TranslateSecurityManager.isTranslationPermittedForSite(
                    request, formBean.getSiteId(), formBean
                            .getSelectedLangTarget());
        }

        if (permitted) {
            List messages = formBean.getMessages();
            Iterator iterator = messages.iterator();
            while (iterator.hasNext()) {
                AdvancedTranslationForm.MessageInfo currentMsg = (AdvancedTranslationForm.MessageInfo) iterator
                        .next();

                if (currentMsg != null && currentMsg.getKey() != null
                        && currentMsg.getKey().length() > 0) {

                    Message msg = DbUtil.getMessage(currentMsg.getKey(),
                            formBean.getSelectedLangTarget(), formBean.getSiteId());
                    boolean saveOrUpdate = true; //update
                    if (msg == null) {
                        saveOrUpdate = false; //save
                        msg = new Message();
                        msg.setSite(SiteCache.lookupById(formBean.getSiteId()));
                    }

                    msg.setKey(currentMsg.getKey());
                    msg.setMessage(currentMsg.getTargetValue());
                    msg.setLocale(formBean.getSelectedLangTarget());

                    if (saveOrUpdate) {
                        TranslatorWorker.getInstance(msg.getKey()).update(msg);
                        // DbUtil.updateMessage(msg);
                    } else {
                        TranslatorWorker.getInstance(msg.getKey()).save(msg);

                        // DbUtil.saveMessage(msg);
                    }
                }
            }
        }

        return new ActionForward(
                "/translation/showAdvancedTranslation.do?d-1338053-p=" +
                request.getParameter("d-1338053-p"),true);

    }

}
