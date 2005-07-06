/*
 *   AdminManager.java
 *   @Author Mikheil Kapanadze mikheil@powerdot.org
 * 	 Created: Sep 12, 2004
 * 	 CVS-ID: $Id: AdminUIManager.java,v 1.1 2005-07-06 10:34:32 rahul Exp $
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
package org.digijava.module.admin.helper;

import java.security.Permission;
import java.util.Collection;
import java.util.Iterator;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.security.DigiSecurityManager;
import org.digijava.kernel.security.ModuleInstancePermission;
import org.digijava.kernel.security.SitePermission;
import org.digijava.kernel.security.principal.GroupPrincipal;
import org.digijava.kernel.user.Group;

public class AdminUIManager {

    public static ActionErrors checkGroupPermissions(Site currentSite, Group group, Collection newPermissions) {
        ActionErrors actionErrors = new ActionErrors();

        if (!group.isDefaultGroup() || !currentSite.getId().equals(group.getSite().getId())) {
            return null;
        }
        SitePermission requiredPerm = new SitePermission(group.getSite(), group.getRequiredActions());

        boolean found = false;
        Iterator iter = newPermissions.iterator();
        while (iter.hasNext()) {
            Permission item = (Permission)iter.next();
            if (item.equals(requiredPerm)) {
                found = true;
                break;
            }
        }
        if (!found) {
            Object[] params = { group.getRequiredActions() };
            actionErrors.add(null, new ActionError("error.admin.defaultGroupMustHavePermission", params));
        }

        return actionErrors.isEmpty() ? null : actionErrors;
    }

    public static void setGroupPermissions(Group group, Collection newPermissions) throws DgException {
        ActionErrors actionErrors = new ActionErrors();
        GroupPrincipal groupPrincipal = new GroupPrincipal(group.getId().
            longValue(), group.getSite().getName(), group.getName());

        DigiSecurityManager.setPrincipalPermissions(groupPrincipal, newPermissions,new Class[] {SitePermission.class, ModuleInstancePermission.class} );
    }

}
