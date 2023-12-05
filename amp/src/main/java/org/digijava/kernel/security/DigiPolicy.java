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

import org.apache.log4j.Logger;
import org.digijava.kernel.Constants;
import org.digijava.kernel.cache.AbstractCache;
import org.digijava.kernel.entity.PrincipalPermission;
import org.digijava.kernel.entity.PrincipalPermissionParameter;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.security.permission.AdditivePermission;
import org.digijava.kernel.security.principal.GroupPrincipal;
import org.digijava.kernel.security.principal.UserPrincipal;
import org.digijava.kernel.util.DigiCacheManager;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.security.*;
import java.sql.SQLException;
import java.util.*;

public class DigiPolicy
    extends Policy{
    private static Logger logger = Logger.getLogger(DigiPolicy.class);
    private static final String appScopeKey = DigiPolicy.class.getName();
    private static DigiPolicy _instance = null;

    private Policy currentDefaultPolicy;
    private PermissionStorage permissionStorage;
    private AbstractCache appScopeCache;

    Map getPrivileges() {
            return getPermissionStorage().getPrivileges();
    }

    Set getPrincipals(Permission permission) {
        logger.debug("Searching principals for permission " + permission);
        if (permission instanceof ResourcePermission) {
            return getPermissionStorage().getPrincipals((ResourcePermission)permission);
        } else {
            return null;
        }
    }

    Set getPrincipals(AdditivePermission permission) {
        logger.debug("Searching principals for permission " + permission + " as AdditivePermission");

        if (permission instanceof ResourcePermission) {
            Set result = getPrincipals((Permission)permission);
            if (result == null) {
                result = new HashSet();
            }
            Map privileges = getPermissionStorage().getPrivileges();
            Iterator iter = privileges.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry item = (Map.Entry)iter.next();
                Principal principal = (Principal)item.getKey();
                Permissions permissions = (Permissions)item.getValue();

                if (!result.contains(principal)) {
                    Enumeration permissionEnum = permissions.elements();
                    while (permissionEnum.hasMoreElements()) {
                        Permission permissionItem = (Permission) permissionEnum.
                            nextElement();

                        if (permissionItem instanceof AdditivePermission &&
                            ( (AdditivePermission) permissionItem).minus( (
                            ResourcePermission) permission) != null) {
                            result.add(principal);
                            break;
                        }
                    }
                }
            }

            if (result.isEmpty()) {
                return null;
            } else {
                return result;
            }
        } else {
            return null;
        }
    }

    /**
     * Returns ALL principals, who have granted this permission. Including ones
     * with implicit grants
     * @param permission
     * @return
     */
    Set getAllPrincipals(Permission permission) {
        logger.debug("Searching principals for permission " + permission + " including implicit ones");

        if (permission instanceof ResourcePermission) {
            Set result = getPrincipals(permission);
            if (result == null) {
                result = new HashSet();
            }
            Map privileges = getPermissionStorage().getPrivileges();
            Iterator iter = privileges.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry item = (Map.Entry)iter.next();
                Principal principal = (Principal)item.getKey();
                Permissions permissions = (Permissions)item.getValue();

                if (!result.contains(principal) & permissions.implies(permission)) {
                    logger.debug("Adding principal: " + principal);
                    result.add(principal);
                } else {
                    logger.debug("Discarding principal: " + principal);
                }
            }

            if (result.isEmpty()) {
                return null;
            } else {
                return result;
            }
        } else {
            return null;
        }

    }

    static DigiPolicy getInstance() {
        if (_instance == null) {
            throw new IllegalStateException(
                "DigiPolicy instance is not initialized yet");
        }
        return _instance;
    }

    public DigiPolicy() {
        if (_instance == null) {
            _instance = this;
        }
        else {
            throw new IllegalStateException(
                "DigiPolicy instance can be created only once");
        }
        appScopeCache = DigiCacheManager.getInstance().getCache(Constants.
            APP_SCOPE_REGION);
        refresh();
    }

    public void grant(Principal principal, Permission permission) throws DgException {
        if (permission instanceof ResourcePermission) {
            PermissionStorage ps = getPermissionStorage();
            try {
                if (permission instanceof AdditivePermission) {
                    ps.grantRevokeAdditivePermission(principal, permission, true);
                } else {
                    ps.grant(principal, (ResourcePermission) permission);
                }
                updatePermissionStorage(ps);
            }
            catch (Exception ex) {
                throw new DgException("Unable to grant permission", ex);
            }
        }
    }

    public void revoke(Principal principal, Permission permission) throws DgException {
        if (permission instanceof ResourcePermission) {
            PermissionStorage ps = getPermissionStorage();
            try {
                if (permission instanceof AdditivePermission) {
                    ps.grantRevokeAdditivePermission(principal, permission, false);
                } else {
                    ps.revoke(principal, (ResourcePermission) permission);
                }
                updatePermissionStorage(ps);
            }
            catch (Exception ex) {
                throw new DgException("Unable to revoke permission", ex);
            }
        }
    }

    public void removePrincipals(Set principals) throws DgException {
        PermissionStorage ps = getPermissionStorage();
        try {
            ps.removePrincipals(principals);
            updatePermissionStorage(ps);
        }
        catch (Exception ex) {
            throw new DgException("Unable to revoke permission", ex);
        }
    }

    /**
     * Refresh privileges property from the persistent storage (e.g. database).
     */
    public void refresh() {
        try {
            permissionStorage = new PermissionStorage(true);
        }
        catch (Exception ex) {
            throw new RuntimeException("refresh() failed", ex);
        }
        logger.debug("Current policy is:\n" + toXML());
    }

    /*
        public PermissionCollection getPermissions(ProtectionDomain pd) {
            //  //System.out.println( "ProtectionDomain version called" );
            Principal[] principals = pd.getPrincipals();
            if (principals == null) {
                //System.out.println("Policy check returned no permissions");
                return new Permissions();
            }
            PermissionCollection pc = new Permissions();
            Map currentPrivileges = getPrivileges();
            for (int i = 0; i < principals.length; i++) {
                Principal principal = (Principal) principals[i];
                Permissions ppc =
                    (Permissions)currentPrivileges.get(principal);
                if (ppc != null) {
                    Enumeration e = ppc.elements();
                    while (e.hasMoreElements()) {
                        Permission curP = (Permission) e.nextElement();
                        pc.add(curP);
                    }
                }
            }
//    //System.out.println( "%%% Printing out permissions " );
//    DigiSecurityManager.printPermissions ( pc );
            return pc;
        }
     */

    public boolean implies(ProtectionDomain domain, Permission permission) {
        boolean retVal = false;
        logger.debug("implies() called");

        // DigiPolicy verifies only DiGi permissions. All others will be passed
        // to system default policy
        if (permission instanceof ResourcePermission) {
            Principal[] principals = domain.getPrincipals();
            retVal = getPermissionStorage().implies(principals,
                                               (ResourcePermission) permission);
        }
        else {
            if (currentDefaultPolicy == null) {
                retVal = false;
            }
            else {
                retVal = currentDefaultPolicy.implies(domain, permission);
            }
        }

        return retVal;
    }

    public PermissionCollection getPermissions(CodeSource cs) {
        if (currentDefaultPolicy != null) {
            return currentDefaultPolicy.getPermissions(cs);
        }
        else {
            return new Permissions();
        }

    }

    public void install() {
        logger.debug("install() called");
        Policy defaultPolicy = Policy.getPolicy();
        if ( (defaultPolicy != null) && ! (defaultPolicy instanceof DigiPolicy)) {
            currentDefaultPolicy = defaultPolicy;
        }
        else {
            currentDefaultPolicy = null;
        }
        Policy.setPolicy(this);
    }

    public void uninstall() {
        logger.debug("uninstall() called");
        if (currentDefaultPolicy != null) {
            Policy.setPolicy(currentDefaultPolicy);
        }
        logger.debug("uninstall() complete");
    }

    private PermissionStorage getPermissionStorage() {
        return getPermissionStorage(false);
    }

    private PermissionStorage getPermissionStorage(boolean forceReload) throws
        RuntimeException {
        PermissionStorage storage = null;
        logger.debug("getPermissionStorage( " + forceReload + ") called");
        synchronized (appScopeKey) {

            storage = (PermissionStorage) appScopeCache.get(
                appScopeKey);
            if (storage == null || forceReload) {
                logger.debug("Reloading permissions");
                try {
                    storage = new PermissionStorage(true);
                }
                catch (Exception ex) {
                    throw new RuntimeException("getPermissionStorage() failed", ex);
                }
                updatePermissionStorage(storage);
            } else {
                logger.debug("Reload is not required");
            }
        }

        return storage;
    }

    private void updatePermissionStorage(PermissionStorage permissionStorage) {
        logger.debug("Putting permissions into cache");
        synchronized (appScopeKey) {
            appScopeCache.put(appScopeKey, permissionStorage);
        }
    }

    public String toXML() {
        logger.debug("toXml() called");
        final String newLine = "\n";
        Map principalPermissionsMap = null;
        StringBuffer buffer = new StringBuffer();

        principalPermissionsMap = getPrivileges();

        buffer.append("<digiPolicy>").append(newLine);
        Iterator iter = principalPermissionsMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry item = (Map.Entry) iter.next();
            Principal principal = (Principal) item.getKey();
            buffer.append(" ").append("<entry>").
                append(newLine);
            buffer.append("    ").append(principal);
            Permissions ps = (Permissions) item.getValue();

            for (Enumeration permissions = ps.elements();
                 permissions.hasMoreElements(); ) {
                ResourcePermission permission = (ResourcePermission)
                    permissions.nextElement();
                buffer.append("    ").append(permission).append(newLine);
            }

            buffer.append(" ").append("</entry>").append(newLine);
        }

        buffer.append("</digiPolicy>");

        return buffer.toString();
    }

}

final class PermissionStorage implements Serializable {
    private static Logger logger = Logger.getLogger(PermissionStorage.class);

    private static final long serialVersionUID = 1;

    private transient Map principalPermissions;
    private Map permissionToPrincipalPermission;

    public PermissionStorage() {
        principalPermissions = new HashMap();
        permissionToPrincipalPermission = new HashMap();
    }

    public Map getPrivileges() {
        synchronized (principalPermissions) {
            return new HashMap(principalPermissions);
        }
    }

    public Set getPrincipals(ResourcePermission permission) {
        synchronized(permissionToPrincipalPermission) {
            Map permissions = (Map)permissionToPrincipalPermission.get(permission);
            if (permissions == null) {
                return null;
            } else {
                return new HashSet(permissions.values());
            }
        }
    }

    public PermissionStorage(boolean load) throws SQLException, HibernateException,
        IOException, ClassNotFoundException  {
        this();
        if (load) {
            load();
        }
    }

    private void readObject(ObjectInputStream ois) throws
        ClassNotFoundException, IOException {
        logger.debug("deserializing PermissionStorage");
        ois.defaultReadObject();

        logger.debug("permissionToPrincipalPermission size is " + permissionToPrincipalPermission.size());
        this.principalPermissions = new HashMap();

        Iterator iter = permissionToPrincipalPermission.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry item = (Map.Entry)iter.next();

            Permission perm = (Permission)item.getKey();
            Map principals = (Map)item.getValue();

            Iterator principalIter = principals.values().iterator();
            while (principalIter.hasNext()) {
                Principal principal = (Principal)principalIter.next();
                Permissions ps = (Permissions)principalPermissions.get(principal);
                if (ps == null) {
                    ps = new Permissions();
                    principalPermissions.put(principal, ps);
                }
                ps.add(perm);
            }
        }
        logger.debug("principalPermissions size is " + principalPermissions.size());
    }

    public void grant(Principal principal, ResourcePermission permission) throws
        Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("Granting permission " + permission + " to " +
                         principal);
        }
        synchronized (permissionToPrincipalPermission) {
            // Only modification operations are locked
            PrincipalPermission pp = grantIntoDB(principal, permission);
            addToMapping(pp, principal, permission);

            synchronized (principalPermissions) {
                // All operations are locked
                Permissions ps = (Permissions) principalPermissions.get(
                    principal);

                if (ps == null) {
                    ps = new Permissions();
                }
                ps.add(permission);
                principalPermissions.put(principal, ps);
            }
        }
    }

    public void revoke(Principal principal, ResourcePermission permission) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("Revoking permission " + permission + " from " +
                         principal);
        }
        synchronized (permissionToPrincipalPermission) {
            ArrayList itemIds = new ArrayList();
            Map permissions = (Map)permissionToPrincipalPermission.get(permission);
            if (permissions != null) {
                // At first, collect information
                Iterator iter = permissions.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry item = (Map.Entry)iter.next();
                    Long id = (Long)item.getKey();
                    Principal p = (Principal)item.getValue();

                    if (p.equals(principal)) {
                        itemIds.add(id);
                    }
                }
                // Remove permissions from database
                revokeFromDb(itemIds);
                // Now remove from the mapping
                iter = itemIds.iterator();
                while (iter.hasNext()) {
                    Long itemId = (Long)iter.next();
                    permissions.remove(itemId);
                }
                // Prune (if needed)
                if (permissions.size() == 0) {
                    permissionToPrincipalPermission.remove(permission);
                }
            }
            // Remove from the main mapping
            synchronized (principalPermissions) {
                Permissions ps = (Permissions) principalPermissions.get(principal);

                if (ps == null) {
                    return;
                }
                Enumeration e = ps.elements();
                Permissions newPs = new Permissions();

                boolean isEmpty = true;
                while (e.hasMoreElements()) {
                    Permission p = (Permission) e.nextElement();
                    if (!p.equals(permission)) {
                        newPs.add(p);
                        if (isEmpty) {
                            isEmpty = false;
                        }
                    }
                }
                if (isEmpty) {
                    principalPermissions.remove(principal);
                } else {
                    principalPermissions.put(principal, newPs);
                }

            }

        }
    }

    public void removePrincipals(Set principals) throws Exception {
        removePrincipalsFromDb(principals);
        synchronized (permissionToPrincipalPermission) {
            Permissions permissions = null;

            // Remove from active area quickly and unlock
            synchronized (principalPermissions) {
                Iterator iter = principals.iterator();
                while (iter.hasNext()) {
                    Principal item = (Principal) iter.next();

                    permissions = (Permissions)
                        principalPermissions.get(item);
                    if (permissions != null) {
                        principalPermissions.remove(permissions);
                    }
                }
            }
            if (permissions != null) {
                Enumeration enumElements = permissions.elements();
                while (enumElements.hasMoreElements()) {
                    Permission permission = (Permission) enumElements.nextElement();
                    Map pPermissions = (Map) permissionToPrincipalPermission.
                        get(permission);
                    if (pPermissions != null) {
                        Iterator iter = pPermissions.entrySet().iterator();
                        while (iter.hasNext()) {
                            Map.Entry item = (Map.Entry) iter.next();
                            Principal p = (Principal) item.getValue();
                            if (principals.contains(p)) {
                                iter.remove();
                            }
                        }
                    }
                }
            }

        }
    }

    public boolean implies(Principal[] principals, ResourcePermission permission) {
        boolean result = false;
        for (int i = 0; i < principals.length; i++) {
            Principal principal = principals[i];
            if (principal instanceof UserPrincipal) {
                UserPrincipal userPrincipal = (UserPrincipal)principal;
                if (permission.getActionMask() != ResourcePermission.INT_SUPER_ADMIN && userPrincipal.isGlobalAdmin()) {
                    return true;
                }
            }
            Permissions ps = (Permissions) principalPermissions.get(principal);

            if (ps != null && ps.implies(permission)) {
                result = true;
                break;
            }
        }
        return result;
    }

    private PrincipalPermission grantIntoDB(Principal principal, ResourcePermission permission) throws
        Exception {
        Session session = null;
        PrincipalPermission pp = createPrincipalPermission(principal,
            permission);
        try {
            session = PersistenceManager.getSession();
//beginTransaction();
            session.save(pp);
            Iterator iter = pp.getParameters().iterator();
            while (iter.hasNext()) {
                Object item = (Object)iter.next();
                session.save(item);
            }
            //tx.commit();
        }
        catch (Exception ex) {
            throw ex;
        }

        return pp;
    }

    private void revokeFromDb(List principalPermissionIds) throws Exception {
        Session session = null;
        try {
            session = PersistenceManager.getSession();
//beginTransaction();
            PrincipalPermission pp = null;
            Iterator iter = principalPermissionIds.iterator();
            while (iter.hasNext()) {
                Long itemId = (Long) iter.next();
                try {
                    pp = (PrincipalPermission) session.load(
                        PrincipalPermission.class, itemId);
                }
                catch (ObjectNotFoundException ex1) {
                    logger.warn("Unable to load principal permission #" + itemId, ex1);
                }
                if (pp != null) {
                    logger.debug("Removing permission #" + itemId);
                    session.delete(pp);
                }
            }
            //tx.commit();
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    private void removePrincipalsFromDb(Set principals) throws Exception {
        logger.debug("removePrincipalsFromDb(" + principals + ") called");
        Session session = null;
        try {
            session = PersistenceManager.getSession();
//beginTransaction();

            Iterator iter = principals.iterator();
            while (iter.hasNext()) {
                Principal item = (Principal)iter.next();
                int principalType = 0;
                Long targetId = null;

                if (item instanceof UserPrincipal) {
                    principalType = PrincipalPermission.USER_PRINCIPAL;
                    targetId = new Long(((UserPrincipal)item).getUserId());
                } else
                if (item instanceof GroupPrincipal) {
                    principalType = PrincipalPermission.GROUP_PRINCIPAL;
                    targetId = new Long(((GroupPrincipal)item).getGroupId());
                }

                if (targetId == null) {
                    logger.warn("Unable to remove unknown principal: "+ item);
                    continue;
                }
                Query q = session.createQuery("from " + PrincipalPermission.class.getName() +
                    " pp where pp.principalType=:prType and pp.targetId=:targetId");
                q.setInteger("prType", principalType);
                q.setLong("targetId", targetId.longValue());

                List permissions = q.list();
                Iterator ppIter = permissions.iterator();
                while (ppIter.hasNext()) {
                    PrincipalPermission ppItem = (PrincipalPermission)ppIter.next();
                    session.delete(ppItem);
                }
                logger.debug("Removed " + permissions.size() + " permission(s)");
            }
            //tx.commit();
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    private void load() throws SQLException, HibernateException,
        IOException, ClassNotFoundException {
        Session session = null;

        synchronized (permissionToPrincipalPermission) {
            synchronized (principalPermissions) {
                principalPermissions.clear();
                permissionToPrincipalPermission.clear();

                    session = PersistenceManager.getSession();
                    Iterator iter = session.createQuery("from " +
                        PrincipalPermission.class.
                        getName() +
                        " as pp").iterate();
                    while (iter.hasNext()) {
                        PrincipalPermission principalPermission = (
                            PrincipalPermission) iter.
                            next();
                        Principal principal = createPrincipal(
                            principalPermission);
                        ResourcePermission permission = null;
                        try {
                            permission = createPermission(principalPermission);
                        }
                        catch (ClassNotFoundException ex) {
                            /*
                             For 2 digi installations on the same application
                             server this error may be most common if both use
                             ths same DB. That's why we make logger.debug() here
                             */
                            if (logger.isDebugEnabled()) {
                                logger.debug(
                                    "Unable to find permission class process PrincipalPermission #" +
                                    principalPermission.
                                    getPrincipalPermissionId(),
                                    ex);
                            }
                            continue;
                        }
                        catch (Exception ex) {
                            // Minimize output for invalid permissions
                            if (logger.isDebugEnabled()) {
                                logger.warn(
                                    "Unable to process PrincipalPermission #" +
                                    principalPermission.
                                    getPrincipalPermissionId(),
                                    ex);
                            }
                            else {
                                logger.warn(
                                    "Unable to process PrincipalPermission #" +
                                    principalPermission.
                                    getPrincipalPermissionId() + ": " +
                                    ex.getMessage());
                            }
                            continue;
                        }

                        Permissions permissions = (Permissions)
                            principalPermissions.get(principal);
                        if (permissions == null) {
                            permissions = new Permissions();
                            principalPermissions.put(principal, permissions);
                        }

                        permissions.add(permission);

                        addToMapping(principalPermission, principal, permission);
                    }
                }
            logger.debug("permissionToPrincipalPermission = " + permissionToPrincipalPermission);
        }
    }

    private void addToMapping(PrincipalPermission principalPermission,
                              Principal principal,
                              ResourcePermission permission) {
        Map permissionPrincipals = (Map)
            permissionToPrincipalPermission.get(permission);
        if (permissionPrincipals == null) {
            permissionPrincipals = new HashMap();
            permissionToPrincipalPermission.put(permission,
                permissionPrincipals);
        }
        permissionPrincipals.put(principalPermission.
            getPrincipalPermissionId(), principal);
    }

    private Principal createPrincipal(PrincipalPermission principalPermission) {
        Principal result = null;
        switch (principalPermission.getPrincipalType()) {
            case PrincipalPermission.GROUP_PRINCIPAL:
                result = new GroupPrincipal(principalPermission.getTargetId());
                break;
            case PrincipalPermission.USER_PRINCIPAL:
                result = new UserPrincipal(principalPermission.getTargetId());
                break;
            default:
                throw new IllegalArgumentException("Illegal principal type " +
                    principalPermission.getPrincipalType());
        }

        return result;
    }


    private Set createPermissionParameters(PrincipalPermission
                                           principalPermission,
                                           ResourcePermission permission) throws
        UnsupportedEncodingException {
        HashSet result = new HashSet();

        ResourcePermission.TypedParameter[] parameters = permission.getTypedParameters();
        for (int i = 0; i < parameters.length; i++) {
            ResourcePermission.TypedParameter parameter = parameters[i];
            PrincipalPermissionParameter dbParam = new
                PrincipalPermissionParameter();
            dbParam.setIndex(i);
            dbParam.setPrincipalPermission(principalPermission);
            dbParam.setParameterClass(PeramSerializer.getTypeName(parameter.getParameterClass()));
            dbParam.setParameterValue(PeramSerializer.getSerializationValue(parameter.getParameterClass(), parameter.getValue()));

            result.add(dbParam);
        }

        return result;
    }

    private PrincipalPermission createPrincipalPermission(Principal principal,
        ResourcePermission permission) throws IOException {
        PrincipalPermission pp = new PrincipalPermission();
        if (principal instanceof UserPrincipal) {
            pp.setPrincipalType(PrincipalPermission.USER_PRINCIPAL);
            pp.setTargetId( ( (UserPrincipal) principal).getUserId());
        }
        else
        if (principal instanceof GroupPrincipal) {
            pp.setPrincipalType(PrincipalPermission.GROUP_PRINCIPAL);
            pp.setTargetId( ( (GroupPrincipal) principal).getGroupId());
        }
        else {
            throw new IllegalArgumentException("Illegal principal type " +
                                               principal.getClass().getName());
        }
        pp.setPermissionClass(permission.getClass().getName());
        pp.setParameters(createPermissionParameters(pp, permission));
        return pp;
    }

    private ResourcePermission createPermission(PrincipalPermission
                                                principalPermission) throws
        ClassNotFoundException, NoSuchMethodException, InstantiationException,
        IllegalAccessException, InvocationTargetException {
        Class permissionClass = Class.forName(principalPermission.
                                              getPermissionClass());
        Class[] paramTypes = new Class[principalPermission.getParameters().size()];
        Object[] paramValues = new Object[paramTypes.length];
        int i = 0;
        Iterator iter = principalPermission.getParameters().iterator();
        while (iter.hasNext()) {
            PrincipalPermissionParameter item = (PrincipalPermissionParameter)
                iter.next();
            ResourcePermission.TypedParameter param = PeramSerializer.
                restoreParameter(item.getParameterClass(),
                                 item.getParameterValue());
            paramValues[i] = param.getValue();
            paramTypes[i] = param.getParameterClass();
            i++;
        }

        Constructor constructor = permissionClass.getConstructor(paramTypes);
        return (ResourcePermission) constructor.newInstance(paramValues);
    }

    public void grantRevokeAdditivePermission(Principal principal, Permission permission, boolean grant) throws Exception {
        logger.debug("grantRevokeAdditivePermission(" + principal + ", " +
                     permission + "," + grant + ")");
        AdditivePermission resultPermission = null;
        if (! (permission instanceof AdditivePermission)) return;
        AdditivePermission additivePermission = (AdditivePermission)permission;
        synchronized (permissionToPrincipalPermission) {
            synchronized (principalPermissions) {
                // All operations are locked
                Permissions ps = (Permissions) principalPermissions.get(
                    principal);

                if (ps != null) {
                    Enumeration permEnum = ps.elements();
                    while (permEnum.hasMoreElements()) {
                        Permission item = (Permission) permEnum.nextElement();
                        if ((item instanceof AdditivePermission) && additivePermission.isRelativePermission(item)) {
                            if (resultPermission == null) {
                                resultPermission = (AdditivePermission) item;
                            }
                            else {
                                resultPermission = resultPermission.plus(item);
                            }
                            revoke(principal, (ResourcePermission)item);
                        }
                    }
                }
                /**
                 * @todo maybe we need to try exception here?
                 */
                logger.debug("resultPermission=" + resultPermission + " additivePermission=" + additivePermission);
                if (resultPermission == null) {
                    if (grant) {
                        resultPermission = additivePermission;
                    }
                }
                else {
                    if (grant) {
                        resultPermission = resultPermission.plus(permission);
                    }
                    else {
                        resultPermission = resultPermission.minus(permission);
                    }
                }
                if (resultPermission != null) {
                    grant(principal, (ResourcePermission)resultPermission);
                }
            }
        }
    }
}

final class PeramSerializer {

    private static final HashMap wrappersToPrimitiveTypes;

    static {
        wrappersToPrimitiveTypes = new HashMap();
        wrappersToPrimitiveTypes.put(Boolean.class.getName(), "boolean");
        wrappersToPrimitiveTypes.put(Byte.class.getName(), "byte");
        wrappersToPrimitiveTypes.put(Character.class.getName(), "char");
        wrappersToPrimitiveTypes.put(Short.class.getName(), "short");
        wrappersToPrimitiveTypes.put(Integer.class.getName(), "int");
        wrappersToPrimitiveTypes.put(Long.class.getName(), "long");
        wrappersToPrimitiveTypes.put(Float.class.getName(), "float");
        wrappersToPrimitiveTypes.put(Double.class.getName(), "double");
    }

    static String getTypeName(Class clazz) {
        String result = (String) wrappersToPrimitiveTypes.get(clazz.getName());
        if (result == null) {
            result = clazz.getName();
        }

        return result;
    }

    static String getSerializationValue(Class clazz, Object object) throws UnsupportedEncodingException {
        if (object == null) {
            return null;
        }
        if (clazz.equals(String.class)) {
            return (String)object;
        } else
        if (wrappersToPrimitiveTypes.containsKey(clazz.getName())) {
            return object.toString();
        }
        else {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            XMLEncoder encoder = new XMLEncoder(stream);
            encoder.writeObject(object);
            encoder.close();

            return stream.toString("UTF-8");

        }
    }

    static ResourcePermission.TypedParameter restoreParameter(String typeName, String value) throws
        ClassNotFoundException, NoSuchMethodException, InstantiationException,
        IllegalAccessException, InvocationTargetException {

        Class resultType = null;
        Object result;

        if (String.class.getName().equals(typeName)) {
            resultType = String.class;
            result = value;
        } else {
            Iterator iter = wrappersToPrimitiveTypes.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry item = (Map.Entry) iter.next();
                if (typeName.equals(item.getValue())) {
                    resultType = Class.forName( (String) item.getKey());
                }
            }

            if (resultType == null) {
                if (value == null) {
                    resultType = Class.forName(typeName);
                    result = null;
                } else {
                    StringBufferInputStream sb = new StringBufferInputStream(
                        value);
                    XMLDecoder decoder = new XMLDecoder(sb);
                    result = decoder.readObject();
                    decoder.close();
                    resultType = result.getClass();
                }
            }
            else {
                if (value == null) {
                    result = null;
                } else {
                    Constructor constructor = resultType.getConstructor(new
                        Class[] {
                        String.class});
                    result = constructor.newInstance(new Object[] {value});
                }
            }

        }
        return new ResourcePermission.TypedParameter(resultType, result);
    }
}
