/*
 *   AddPermission.java
 *   @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: Oct 15, 2004
 * 	 CVS-ID: $Id: AddPermission.java,v 1.1 2005-07-06 10:34:14 rahul Exp $
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
import org.digijava.kernel.request.Site;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.translation.form.TranslationPermissionsForm;

public class AddPermission
      extends Action {

    public ActionForward execute(ActionMapping mapping,
				 ActionForm form,
				 javax.servlet.http.HttpServletRequest request,
				 javax.servlet.http.HttpServletResponse
				 response) throws java.lang.Exception {

	TranslationPermissionsForm formBean = (TranslationPermissionsForm) form;
	Site currentSite = RequestUtils.getSite(request);
	TranslationPermissionsForm.PermissionInfo pi = new TranslationPermissionsForm.
	      PermissionInfo();

	formBean.getPermissions().add(pi);

	if (formBean.isUserMode()) {
	    return mapping.findForward("forwardUser");
	} else {
	    return mapping.findForward("forwardGroup");
	}

    }

}