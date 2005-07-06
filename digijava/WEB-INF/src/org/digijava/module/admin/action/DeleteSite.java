/*
 *   DeleteSite.java
 *   @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Sep 5, 2003
 * 	 CVS-ID: $Id: DeleteSite.java,v 1.1 2005-07-06 10:34:09 rahul Exp $
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
package org.digijava.module.admin.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.Constants;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.security.DgSecurityManager;
import org.digijava.kernel.security.ResourcePermission;
import org.digijava.kernel.user.User;
import org.digijava.module.admin.form.DeleteSiteForm;
import org.digijava.module.admin.util.DbUtil;
import java.util.*;
import org.digijava.kernel.user.Group;
import org.digijava.kernel.security.principal.GroupPrincipal;
import org.digijava.kernel.security.DigiSecurityManager;

/*
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class DeleteSite
    extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {

        DeleteSiteForm siteForm = (DeleteSiteForm) form;
        User user = (User) request.getSession(true).getAttribute(Constants.USER);
        Site site = DbUtil.getSite(siteForm.getSelectedSiteId());

        siteForm.setSiteName(site.getName());

        if (DgSecurityManager.permitted(user.getSubject(), site,
                                        ResourcePermission.INT_ADMIN)) {
            Set principals = new HashSet();
            Iterator iter = site.getGroups().iterator();
            while (iter.hasNext()) {
                Group item = (Group)iter.next();
                GroupPrincipal gp = new GroupPrincipal(item.getId().longValue());
                principals.add(gp);

            }
            DigiSecurityManager.removePrincipals(principals);
            DbUtil.deleteSite(siteForm.getSelectedSiteId());
        }
        else {
            ActionErrors errors = new ActionErrors();
            errors.add(ActionErrors.GLOBAL_ERROR,
                       new ActionError("error.admin.noPermissionToDeleteSite"));
            saveErrors(request, errors);

        }

        return mapping.findForward("forward");
    }
}