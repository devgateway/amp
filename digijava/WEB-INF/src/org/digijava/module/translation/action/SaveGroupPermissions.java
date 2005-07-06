/*
 *   SaveGroupPermissions.java
 *   @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: Oct 15, 2004
 * 	 CVS-ID: $Id: SaveGroupPermissions.java,v 1.1 2005-07-06 10:34:14 rahul Exp $
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
import java.util.HashMap;
import java.util.Iterator;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.user.Group;
import org.digijava.kernel.user.GroupPermission;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.translation.form.TranslationPermissionsForm;
import org.digijava.module.translation.security.TranslateObject;
import org.digijava.module.translation.security.TranslatePermission;
import org.digijava.kernel.security.principal.GroupPrincipal;
import org.digijava.kernel.security.DigiSecurityManager;

public class SaveGroupPermissions
      extends Action {

    public ActionForward execute(ActionMapping mapping,
				 ActionForm form,
				 javax.servlet.http.HttpServletRequest request,
				 javax.servlet.http.HttpServletResponse
				 response) throws java.lang.Exception {

	TranslationPermissionsForm formBean = (
	      TranslationPermissionsForm) form;
	Site currentSite = RequestUtils.getSite(request);

	Group group = null;
	if (formBean.getGroupId() != null) {
	    group = org.digijava.module.admin.util.DbUtil.getGroup(formBean.
		  getGroupId());
	}
	if (group != null) {

	    ArrayList permissions = new ArrayList();
	    Iterator iter = formBean.getPermissions().iterator();
	    while (iter.hasNext()) {
		TranslationPermissionsForm.PermissionInfo pi = (
		      TranslationPermissionsForm.
		      PermissionInfo) iter.next();

		TranslateObject translateObject = new TranslateObject(pi.
		      getSiteId(),
		      pi.getLocaleId());
		permissions.add(new TranslatePermission(translateObject,
		      TranslatePermission.INT_TRANSLATE));
	    }

	    GroupPrincipal groupPrincipal = new GroupPrincipal(group);
	    DigiSecurityManager.setPrincipalPermissions(groupPrincipal,
		  permissions,new Class[] {TranslatePermission.class} );
	}

	return mapping.findForward("forward");
    }
}