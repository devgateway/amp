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

package org.digijava.module.admin.action;

import org.apache.struts.action.*;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.security.DigiSecurityManager;
import org.digijava.kernel.security.principal.GroupPrincipal;
import org.digijava.kernel.user.Group;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.admin.form.GroupsForm;
import org.digijava.module.admin.util.DbUtil;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

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

       ActionMessages errors = new ActionMessages();
       if (targetGroup == null) {
           errors.add(null,
                      new ActionMessage("error.admin.unknownGroup"));
       } else {
           if (targetGroup.isDefaultGroup()) {
               errors.add(null,
                          new ActionMessage("error.admin.cannotDelDefGroup"));
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
