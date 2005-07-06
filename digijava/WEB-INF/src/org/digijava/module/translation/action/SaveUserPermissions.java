/*
 *   SaveUserPermissions.java
 *   @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: Oct 15, 2004
 * 	 CVS-ID: $Id: SaveUserPermissions.java,v 1.1 2005-07-06 10:34:14 rahul Exp $
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

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.security.DigiSecurityManager;
import org.digijava.kernel.security.principal.UserPrincipal;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.UserUtils;
import org.digijava.module.translation.form.TranslationPermissionsForm;
import org.digijava.module.translation.security.TranslateObject;
import org.digijava.module.translation.security.TranslatePermission;

public class SaveUserPermissions
      extends Action {

    public ActionForward execute(ActionMapping mapping,
				 ActionForm form,
				 javax.servlet.http.HttpServletRequest request,
				 javax.servlet.http.HttpServletResponse
				 response) throws java.lang.Exception {

	TranslationPermissionsForm formBean = (
	      TranslationPermissionsForm) form;

	User user = null;
	if (formBean.getUserId() != null) {
	    user = UserUtils.getUser(formBean.getUserId());
	}

	if (user != null) {
	    ArrayList permissions = new ArrayList();
	    Iterator iter = formBean.getPermissions().iterator();
	    while (iter.hasNext()) {
		TranslationPermissionsForm.PermissionInfo pi = (
		      TranslationPermissionsForm.
		      PermissionInfo) iter.next();

		TranslateObject translateObject = new TranslateObject(
		      pi.getSiteId(),
		      pi.getLocaleId());
		permissions.add(new TranslatePermission(translateObject,
		      TranslatePermission.INT_TRANSLATE));
	    }

	    UserPrincipal userPrincipal = new UserPrincipal(user);
	    DigiSecurityManager.setPrincipalPermissions(userPrincipal,
		  permissions,new Class[] {TranslatePermission.class} );
	}

	return mapping.findForward("forward");

    }

}