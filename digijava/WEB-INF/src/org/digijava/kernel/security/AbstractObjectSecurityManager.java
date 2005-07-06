package org.digijava.kernel.security;

import org.digijava.kernel.security.permission.ObjectPermission;
import java.security.Principal;
import org.digijava.kernel.exception.DgException;
import javax.security.auth.Subject;
import java.util.Set;
import java.util.HashSet;
import java.util.*;
import java.security.PermissionCollection;
import java.security.Permission;
import org.digijava.kernel.user.User;
import org.digijava.kernel.security.principal.UserPrincipal;
import org.digijava.kernel.user.Group;
import org.digijava.kernel.security.principal.GroupPrincipal;

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
     * instance. Please note that it returns only user with <b>explicit</b>
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
        return targets;
    }

}