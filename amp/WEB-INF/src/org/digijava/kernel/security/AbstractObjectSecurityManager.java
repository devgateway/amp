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

import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Principal;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.security.auth.Subject;

import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.security.permission.AdditivePermission;
import org.digijava.kernel.security.permission.ObjectPermission;
import org.digijava.kernel.security.principal.GroupPrincipal;
import org.digijava.kernel.security.principal.UserPrincipal;
import org.digijava.kernel.user.Group;
import org.digijava.kernel.user.User;

public abstract class AbstractObjectSecurityManager {

    /**
     * Subclasses need to override this method only - other ones from this class
     * use it to construct permission object according given parameters
     * @param clazz Target object's class
     * @param key Target object's key
     * @param action action code
     * @return
     */
    public abstract ObjectPermission createPermission(Class clazz, Object key,
        int action);

    public boolean checkPermission(Principal principal, Class targetClass,
                                   Object key, int action) {
        return DigiSecurityManager.checkPermission(principal,
            createPermission(targetClass,
                             key, action));
    }

    public boolean checkPermission(Subject subject, Class targetClass,
                                   Object key,
                                   int action) {
        return DigiSecurityManager.checkPermission(subject,
            createPermission(targetClass,
                             key, action));
    }

    public void grantPermission(Principal principal, Class targetClass,
                                Object key,
                                int action) throws DgException {
        DigiSecurityManager.grantPermission(principal,
                                            createPermission(targetClass,
            key, action));
    }

    public void grantPermission(User user, Class targetClass,
                                Object key,
                                int action) throws DgException {
        UserPrincipal principal = new UserPrincipal(user);
        grantPermission(principal, targetClass, key, action);
    }

    public void grantPermission(Group group, Class targetClass,
                                Object key,
                                int action) throws DgException {
        GroupPrincipal principal = new GroupPrincipal(group);
        grantPermission(principal, targetClass, key, action);
    }

    public void revokePermission(Principal principal, Class targetClass,
                                 Object key,
                                 int action) throws DgException {
        DigiSecurityManager.revokePermission(principal,
                                             createPermission(targetClass,
            key, action));
    }

    public void revokePermission(User user, Class targetClass,
                                 Object key,
                                 int action) throws DgException {
        UserPrincipal principal = new UserPrincipal(user);
        revokePermission(principal, targetClass, key, action);
    }

    public void revokePermission(Group group, Class targetClass,
                                 Object key,
                                 int action) throws DgException {
        GroupPrincipal principal = new GroupPrincipal(group);
        revokePermission(principal, targetClass, key, action);
    }

    /**
     * Returns users which have given action granted on target class's given
     * instance. Please note that it returns only users with <b>explicit</b>
     * permissions (permission is granted to user or group and user belongs to
     * that group). There is no implication check.
     * @param targetClass
     * @param key
     * @param action
     * @return
     * @throws DgException
     */
    public Set getUsers (Class targetClass, Object key, int action) throws DgException {
        return DigiSecurityManager.getUsers(createPermission(targetClass, key, action));
    }

    /**
     * Returns users which have given action granted on target class's given
     * instance. Please note that it returns users with <b>implicit</b>
     * permissions too (as opposite of getUsers())
     * @param targetClass
     * @param key
     * @param action
     * @return
     * @throws DgException
     */
    public Set getAllUsers (Class targetClass, Object key, int action) throws DgException {
        return DigiSecurityManager.getUsers(createPermission(targetClass, key, action));
    }

    /**
     * Returns users which have given action or its overlapping one granted. For
     * example, if user has ADMIN action granted on particular object and ADMIN
     * is passed as action, this user will be returned by the method
     * @param targetClass
     * @param key
     * @param action
     * @return
     * @throws DgException
     * @throws IllegalArgumentException if createPermission() method of this
     * class constructs Permission class which does not extend
     * AdditivePermission interface
     */
    public Set getUsersByActionImplication(Class targetClass, Object key,
                                           int action) throws DgException {
        Permission permission = createPermission(targetClass, key, action);
        if (permission instanceof AdditivePermission) {
            return DigiSecurityManager.getUsersByAdditivePermission( (
                AdditivePermission) permission);
        }
        else {
            throw new IllegalArgumentException("getUsersByActionImplication() is only available for AdditivePermission interface implementations");
        }
    }


    /**
     * Get identifiers of objects of the given class on which the given subject
     * has the given permission action. Method recognizes only <b>explicit</b>
     * grants. There is no implication check.
     * @param subject
     * @param targetClass
     * @param action
     * @return
     */
    public Set getTargetIdentities(Subject subject, Class targetClass, int action) {
        HashSet targets = new HashSet();
        String className = targetClass.getName();
        Iterator iter = subject.getPrincipals().iterator();
        while (iter.hasNext()) {
            Principal principal = (Principal) iter.next();
            PermissionCollection permissions = DigiSecurityManager.
                getPermissions(principal);

            if (permissions != null) {
                Enumeration enumElements = permissions.elements();
                while (enumElements.hasMoreElements()) {
                    Permission permission = (Permission) enumElements.nextElement();
                    if (permission instanceof ObjectPermission) {
                        ObjectPermission op = (ObjectPermission) permission;
                        if (op.getClassName().equals(className) &&
                            action == op.getActionMask()) {
                            targets.add(op.getInstanceId());
                        }
                    }
                }
            }
        }
        return targets;
    }

    public final static boolean checkPermission(Principal principal,
                                         ObjectPermission permission) {
        AbstractObjectSecurityManager mgr = permission.getSecurityManager();
        if (mgr == null) {
            return DigiSecurityManager.checkPermission(principal, permission);
        }
        Class permissionClass = null;
        try {
            permissionClass = Class.forName(permission.getClassName());
        }
        catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
        return mgr.checkPermission(principal, permissionClass,
                                   permission.getInstanceId(),
                                   permission.getActionMask());
    }

    public final static boolean checkPermission(Subject subject,
                                         ObjectPermission permission) {
        AbstractObjectSecurityManager mgr = permission.getSecurityManager();
        if (mgr == null) {
            return DigiSecurityManager.checkPermission(subject, permission);
        }
        Class permissionClass = null;
        try {
            permissionClass = Class.forName(permission.getClassName());
        }
        catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
        return mgr.checkPermission(subject, permissionClass,
                                   permission.getInstanceId(),
                                   permission.getActionMask());
    }

}
