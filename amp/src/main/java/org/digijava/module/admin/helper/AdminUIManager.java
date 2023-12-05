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

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.security.DigiSecurityManager;
import org.digijava.kernel.security.ModuleInstancePermission;
import org.digijava.kernel.security.SitePermission;
import org.digijava.kernel.security.principal.GroupPrincipal;
import org.digijava.kernel.user.Group;

import java.security.Permission;
import java.util.Collection;
import java.util.Iterator;

public class AdminUIManager {

    public static ActionMessages checkGroupPermissions(Site currentSite, Group group, Collection newPermissions) {
        ActionMessages ActionMessages = new ActionMessages();

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
            ActionMessages.add(null, new ActionMessage("error.admin.defaultGroupMustHavePermission", params));
        }

        return ActionMessages.isEmpty() ? null : ActionMessages;
    }

    public static void setGroupPermissions(Group group, Collection newPermissions) throws DgException {
        ActionMessages ActionMessages = new ActionMessages();
        GroupPrincipal groupPrincipal = new GroupPrincipal(group.getId().
            longValue(), group.getSite().getName(), group.getName());

        DigiSecurityManager.setPrincipalPermissions(groupPrincipal, newPermissions,new Class[] {SitePermission.class, ModuleInstancePermission.class} );
    }

}
