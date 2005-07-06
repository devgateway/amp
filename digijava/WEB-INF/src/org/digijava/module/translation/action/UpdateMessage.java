/*
 *   UpdateMessage.java
 *   @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: Sept 29, 2004
 * 	 CVS-ID: $Id: UpdateMessage.java,v 1.1 2005-07-06 10:34:14 rahul Exp $
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

package org.digijava.module.translation.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.Message;
import org.digijava.module.translation.form.AdvancedTranslationForm;
import org.digijava.module.translation.util.DbUtil;
import java.util.List;
import java.util.Iterator;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.translation.security.TranslateSecurityManager;
import org.digijava.kernel.security.DgSecurityManager;
import org.digijava.module.translation.security.TranslateObject;
import org.digijava.module.translation.security.TranslatePermission;

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
	    permitted = TranslateSecurityManager.getInstance().
		  checkPermission(
		  DgSecurityManager.getSubject(request), TranslateObject.class,
		  new TranslateObject(formBean.getSiteId(),
				      formBean.getSelectedLangTarget()),
		  TranslatePermission.INT_TRANSLATE);
	}

	if (permitted) {
	    if (formBean.getKey() != null) {
		String decodedKey = DgUtil.decodeString(formBean.getKey());
		Message msg = DbUtil.getMessage(decodedKey,
						formBean.getSelectedLangTarget(),
						formBean.getSiteId());

		boolean saveOrUpdate = true; //update
		if (msg == null) {
		    saveOrUpdate = false; // save
		    msg = new Message();

		    msg.setKey(decodedKey);
		    msg.setSiteId(formBean.getSiteId().toString());
		}

		List messages = formBean.getMessages();

		Iterator iterator = messages.iterator();
		while (iterator.hasNext()) {
		    AdvancedTranslationForm.MessageInfo currentMsg = (
			  AdvancedTranslationForm.MessageInfo) iterator.next();

		    if (currentMsg != null && currentMsg.getKey() != null &&
			currentMsg.getKey().equals(decodedKey)) {

			msg.setLocale(formBean.getSelectedLangTarget());
			msg.setMessage(currentMsg.getTargetValue());

			if (saveOrUpdate) {
			    DbUtil.updateMessage(msg);
			}
			else {
			    DbUtil.saveMessage(msg);
			}
		    }
		}
	    }
	}

	return mapping.findForward("forward");
    }

}