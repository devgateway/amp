/*
 *   DeleteGroup.java
 *   @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Sep 20, 2003
 * 	 CVS-ID: $Id: DeleteGroup.java,v 1.1 2005-07-06 10:34:09 rahul Exp $
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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.security.DigiSecurityManager;
import org.digijava.kernel.security.principal.GroupPrincipal;
import org.digijava.kernel.user.Group;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.admin.form.GroupsForm;
import org.digijava.module.admin.util.DbUtil;

public class DeleteGroup  extends Action {

    public ActionForward execute(ActionMapping mapping,
                                ActionForm form,
                                javax.servlet.http.HttpServletRequest request,
                                javax.servlet.http.HttpServletResponse
                                response) throws java.lang.Exception {
       Site currentSite = RequestUtils.getSite(request);
       Site realSite = DbUtil.getSite(currentSite.getId());

       GroupsForm groupForm = (GroupsForm)form;
       Group targetGroup = null;
       if (groupForm.getGroupId() != null) {
           Iterator iter = realSite.getGroups().iterator();
           while (iter.hasNext()) {
               Group group = (Group)iter.next();
               if (group.getId().equals(groupForm.getGroupId())) {
                   targetGroup = group;
                   break;
               }
           }
       }

       ActionErrors errors = new ActionErrors();
       if (targetGroup == null) {
           errors.add(null,
                      new ActionError("error.admin.unknownGroup"));
       } else {
           if (targetGroup.isDefaultGroup()) {
               errors.add(null,
                          new ActionError("error.admin.cannotDelDefGroup"));
           } else {
               GroupPrincipal gp = new GroupPrincipal(targetGroup.getId().
                   longValue());
               Set principals = new HashSet();
               principals.add(gp);
               targetGroup.setSite(null);

               DbUtil.editSite(realSite);
               DigiSecurityManager.removePrincipals(principals);
           }
       }

       if (errors.isEmpty()) {
           return mapping.findForward("forward");
       } else {
           saveErrors(request, errors);
           return mapping.findForward("error");
       }

   }

}