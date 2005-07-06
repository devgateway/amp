/*
 *   AddKey.java
 *   @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: Sept 28, 2004
 * 	 CVS-ID: $Id: AddKey.java,v 1.1 2005-07-06 10:34:14 rahul Exp $
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
import org.digijava.kernel.security.DgSecurityManager;
import org.digijava.module.translation.form.AdvancedTranslationForm;
import org.digijava.module.translation.security.TranslateObject;
import org.digijava.module.translation.security.TranslatePermission;
import org.digijava.module.translation.security.TranslateSecurityManager;
import org.digijava.module.translation.util.DbUtil;
import org.digijava.kernel.util.DgUtil;


public class AddKey
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
				      formBean.getLocale()),
		  TranslatePermission.INT_TRANSLATE);
	}

	if (permitted) {
	    Message msg = new Message();

	    msg.setKey(formBean.getKey());
	    msg.setMessage(formBean.getMessageText());
	    msg.setSiteId(formBean.getSiteId().toString());
	    msg.setLocale(formBean.getLocale());

	    DbUtil.saveMessage(msg);
	}
	formBean.setAddKey(false);
	return mapping.findForward("forward");

    }

}