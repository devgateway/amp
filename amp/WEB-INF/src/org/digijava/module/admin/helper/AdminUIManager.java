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
