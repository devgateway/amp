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

package org.digijava.kernel.security.util;

import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.security.DigiPolicy;
import org.digijava.kernel.security.ModuleInstancePermission;
import org.digijava.kernel.security.SitePermission;
import org.digijava.kernel.security.principal.GroupPrincipal;
import org.digijava.kernel.user.GroupPermission;
import org.digijava.kernel.util.DigiConfigManager;
import org.digijava.kernel.util.DummyServletContext;
import org.digijava.kernel.viewmanager.ViewConfigFactory;
import org.hibernate.Session;

import java.security.Permission;
import java.util.Iterator;

public class PermissionConverter {

    public static void main(String[] args) throws Exception {
        ViewConfigFactory.initialize(new DummyServletContext("."));
        DigiConfigManager.initialize("./repository");
        PersistenceManager.initialize(false);
        DigiPolicy digiPolicy = new DigiPolicy();
        try {
            Session session = PersistenceManager.getSession();
            Iterator iter = session.createQuery("from " + GroupPermission.class.getName()).iterate();
            while (iter.hasNext()) {
                GroupPermission item = (GroupPermission)iter.next();
                Permission permission = createPermission(session, item);

                GroupPrincipal gp = new GroupPrincipal(item.getGroup().getId().longValue());

                digiPolicy.grant(gp, permission);
            }
        }
        finally {
            PersistenceManager.cleanup();
        }
    }

    private static Permission createPermission(Session session,
                                        GroupPermission groupPermission) throws
        Exception {
        Permission permission = null;
        switch (groupPermission.getPermissionType()) {
            case GroupPermission.SITE_PERMISSION:

                // Find target site
                Site targetSite = groupPermission.getGroup().getSite();
                permission = new SitePermission(targetSite,
                                                groupPermission.getActions());
                break;
            case GroupPermission.MODULE_INSTANCE_PERMISSION:
                Long moduleInstanceId = new Long(groupPermission.
                                                 getTargetName());
                ModuleInstance moduleInstance = (ModuleInstance)
                    session.load(ModuleInstance.class, moduleInstanceId);
                permission = new ModuleInstancePermission(moduleInstance,
                    groupPermission.getActions());

                break;
        }

        return permission;
    }

}
