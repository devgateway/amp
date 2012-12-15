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

import java.util.Iterator;
import java.util.List;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.SiteCache;
import org.digijava.module.translation.form.AdvancedTranslationForm;
import org.digijava.module.translation.security.TranslateSecurityManager;
import org.digijava.module.translation.util.DbUtil;
import org.digijava.module.translation.util.TranslationManager;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class UpdateMessage
    extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {

        AdvancedTranslationForm formBean = (AdvancedTranslationForm) form;

        boolean permitted = true;
        if (!DgUtil.isLocalTranslatorForSite(request)) {
            permitted = TranslateSecurityManager.isTranslationPermittedForSite(
                request, formBean.getSiteId(),
                formBean.getSelectedLangTarget());
        }

        if (permitted) {

            String[] selectedKeys = formBean.getSelectedKeys();

            if (selectedKeys != null) {
                for (int i = 0; i < selectedKeys.length; i++) {
                    String decodedKey = selectedKeys[i];
                    if (formBean.getExpireSelected() != null) {
                        TranslationManager.expireTranslation(decodedKey);
                    }
                    else if (formBean.getUnExpireSelected() != null) {
                        TranslationManager.unExpireTranslation(decodedKey);
                    }
                }
            }
            else if (formBean.getKey() != null) {
                String decodedKey = formBean.getKey();
                Message msg = DbUtil.getMessage(decodedKey,
                                                formBean.getSelectedLangTarget().
                                                trim(),
                                                formBean.getSiteId());

                boolean saveOrUpdate = true; //update
                if (msg == null) {
                    saveOrUpdate = false; // save
                    msg = new Message();

                    msg.setKey(decodedKey);
                    msg.setSite(SiteCache.lookupById(formBean.getSiteId()));
                    msg.setLocale(formBean.getSelectedLangTarget().trim());
                }

                List messages = formBean.getMessages();

                Iterator iterator = messages.iterator();
                while (iterator.hasNext()) {
                    AdvancedTranslationForm.MessageInfo currentMsg = (
                        AdvancedTranslationForm.MessageInfo) iterator.next();

                    if (currentMsg != null && currentMsg.getKey() != null &&
                        currentMsg.getKey().trim().equals(decodedKey)) {

                        msg.setLocale(formBean.getSelectedLangTarget().trim());
                        msg.setMessage(currentMsg.getTargetValue());

                        if (formBean.getExpireKey() != null) {
                            TranslationManager.expireTranslation(decodedKey);
                        }
                        else if (formBean.getUnexpireKey() != null) {
                            TranslationManager.unExpireTranslation(decodedKey);
                        }
                        else {
                            if (saveOrUpdate) {
                                DbUtil.updateMessage(msg);
                            }
                            else {
                                DbUtil.saveMessage(msg);
                            }
                        }
                        break;
                    }

                }
            }
        }

        return new ActionForward(
            "/translation/showAdvancedTranslation.do?d-1338053-p=" +
            request.getParameter("d-1338053-p"));

    }

}
