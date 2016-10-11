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

package org.digijava.kernel.security;

import java.security.Principal;

import javax.security.auth.Subject;

import org.digijava.kernel.security.permission.ObjectPermission;

public class ModuleInstanceSecurityManager
    extends AbstractObjectSecurityManager {
    /**
     *
     * @param clazz Target object's class
     * @param key Target object's key
     * @param action action code
     * @return org.digijava.kernel.security.permission.ObjectPermission
     */
    public ObjectPermission createPermission(Class clazz, Object key,
                                             int action) {
        return new ModuleInstancePermission( (Long) key, new Integer(action));
    }

    public boolean checkPermission(Principal principal, Class targetClass,
                                   Object key, int action) {
        ModuleInstancePermission perm = (ModuleInstancePermission)
            createPermission(targetClass,
                             key, action);
        if (DigiSecurityManager.checkPermission(principal, perm)) {
            return true;
        }
        else {
            if (perm.getSiteId() == null) {
                return false;
            } else {
                return checkPermission(principal, new SitePermission(perm.getSiteId(), perm.getActionMask()));
            }
        }
    }

    public boolean checkPermission(Subject subject, Class targetClass,
                                   Object key,
                                   int action) {
        ModuleInstancePermission perm = (ModuleInstancePermission)
            createPermission(targetClass,
                             key, action);
        if (DigiSecurityManager.checkPermission(subject, perm)) {
            return true;
        }
        else {
            if (perm.getSiteId() == null) {
                return false;
            } else {
                return checkPermission(subject, new SitePermission(perm.getSiteId(), perm.getActionMask()));
            }
        }
    }

}
