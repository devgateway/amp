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
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.user.Group;
import org.digijava.kernel.user.GroupPermission;
import org.digijava.kernel.util.DigiCacheManager;
import org.digijava.kernel.util.I18NHelper;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.CodeSource;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.Policy;
import java.security.Principal;
import java.security.ProtectionDomain;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DBPolicy
    extends Policy
    implements Serializable {

    private static Logger logger = I18NHelper.getKernelLogger(DBPolicy.class);

    //private volatile HashMap principalPermissionsMap;
    private Policy currentDefaultPolicy;
    private AbstractCache appScopeCache;
    private static final String appScopeKey = DBPolicy.class.getName();

    /**
     * Get permission information from application scope. If it does not exist
     * there, or putOver is set, populate from database and put back
     * @param putOver if true, permission information will be re-populated and
     * put to cache
     * @return permission information
     * @throws DgException if process was terminated unsuccessfully
     */
    private HashMap getPrincipalPermissions(boolean putOver) throws DgException {
        HashMap principalPermissionsMap = null;
        logger.debug("getPrincipalPermissionsMap() called");
        synchronized (appScopeKey) {

            principalPermissionsMap = (HashMap) appScopeCache.get(
                appScopeKey);
            if (principalPermissionsMap == null || putOver) {
                principalPermissionsMap = loadPrivileges();
                appScopeCache.put(appScopeKey, principalPermissionsMap);
            }
        }

        return principalPermissionsMap;
    }

    private HashMap loadPrivileges() throws DgException {
        Session session = null;
        HashMap newPrincipalPermissionsMap = new HashMap();
        try {
            session = PersistenceManager.getSession();
            Iterator iter = session.createQuery("from " + GroupPermission.class.getName()).iterate();
            while (iter.hasNext()) {
                GroupPermission groupPermission = (GroupPermission) iter.
                    next();
                switch (groupPermission.getPermissionType()) {
                    case GroupPermission.SITE_PERMISSION:

                        // Find target site
                        Site targetSite = groupPermission.getGroup().getSite();
                        // Pupulate permissions
                        addPermissionToGroup(session,
                                             newPrincipalPermissionsMap,
                                             groupPermission.getGroup(),
                                             new
                                             SitePermission(targetSite,
                            groupPermission.getActions()));
                        break;
                    case GroupPermission.MODULE_INSTANCE_PERMISSION:
                        Long moduleInstanceId = new Long(groupPermission.
                            getTargetName());
                        ModuleInstance moduleInstance = (ModuleInstance)
                            session.load(ModuleInstance.class, moduleInstanceId);
                        addPermissionToGroup(session,
                                             newPrincipalPermissionsMap,
                                             groupPermission.getGroup(),
                                             new
                                             ModuleInstancePermission(
                            moduleInstance,
                            groupPermission.getActions()));

                        break;
                    default:
                        throw new DgException(
                            "Unsupported permission type for permission(" +
                            groupPermission.getGroupPermissionId() + "): " +
                            groupPermission.getPermissionType());
                }
            }
            //principalPermissionsMap = newPrincipalPermissionsMap;
        }
        catch (Throwable cause) {
            throw new DgException("Error while loading privileges", cause);
        }

        return newPrincipalPermissionsMap;
    }

    public DBPolicy() {
        appScopeCache = DigiCacheManager.getInstance().getCache(Constants.APP_SCOPE_REGION);
        refresh();
    }

    public PermissionCollection getPermissions(CodeSource cs) {
        logger.debug("DBPolicy.getPermissionsCS called for " + cs);
        if (currentDefaultPolicy != null) {
            return currentDefaultPolicy.getPermissions(cs);
        }
        else {
            return new Permissions();
        }

    }

    /**
      public PermissionCollection getPermissions(ProtectionDomain pd) {
     //we should either overwrite this method or the implies method
     logger.debug("DBPolicy:getPermissions called");
     //we get array of principals accosiated with the PD
     Principal[] principals = pd.getPrincipals();
     //we loop through all principals to populate the final PermissionCollection
     //and we start with the static permissions
     PermissionCollection all_permissions = pd.getPermissions();
     for (int i=0; i < principals.length; i++) {
         logger.debug("DBPolicy.getPermissions called for principal: " + principals[i]);
      //we get permissions for a principal
      Enumeration enum = ((Permissions) principalPermissionsMap.get(principals[i])).elements();
      //we get all permissions for a principal and we populate the all_permissions
      while (enum.hasMoreElements()) {
       all_permissions.add((Permission) enum.nextElement());
      }
     }
     return all_permissions;
      }
     **/

    public boolean implies(ProtectionDomain domain, Permission permission) {
        boolean retVal = false;
        logger.debug("implies() called");
        HashMap principalPermissionsMap = null;
        try {
            principalPermissionsMap = getPrincipalPermissions(false);
        }
        catch (DgException ex) {
            throw new RuntimeException("DBPolicy.implies() caused an error", ex);
        }
        // DBPolicy verifies only DiGi permissions. All others will be passed
        // to system default policy
        if (permission instanceof ResourcePermission) {
            if (logger.isDebugEnabled()) {
                logger.debug(MessageFormat.format("Checking permission {0} with DBPolicy", permission));
            }
            Principal[] principals = domain.getPrincipals();

            for (int i = 0; i < principals.length; i++) {
                if (principals[i] instanceof Group) {
                    Group dgGroup = (Group) principals[i];
                    Long groupId = dgGroup.getId();
                    Permissions ps = (Permissions) principalPermissionsMap.get(
                        groupId);
                    if (permission != null && ps != null) {
                        retVal = ps.implies(permission);
                        if (retVal) {
                            break;
                        }
                    }

                }
                else {
                        /** @todo do we need to raise any exception here? I suggest, no. Mikheil */
                }
            }

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

    public void refresh() {
        logger.debug("refresh() called");
        try {
            getPrincipalPermissions(true);
        }
        catch (DgException ex) {
            throw new RuntimeException("Error reloading DBPolicy", ex);
        }

        if (logger.isDebugEnabled()) {
            logger.debug(MessageFormat.format("Current DBPolicy object is:\\n------\\n{0} \\n------\\n",
                    this.toString()));
        }
    }

    public void install() {
        logger.debug("install() called");
        Policy defaultPolicy = Policy.getPolicy();
        if ( (defaultPolicy != null) && ! (defaultPolicy instanceof DBPolicy)) {
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

    private void addPermissionToGroup(Session session,
                                      HashMap principalPermissions, Group group,
                                      ResourcePermission permission) throws
        DgException {

        Long groupId = group.getId();
        Permissions ps = (Permissions) principalPermissions.get(groupId);
        if (ps == null) {
            ps = new Permissions();
            principalPermissions.put(groupId, ps);
        }
        // Add permission, which is not implied yet to save space and search
        // time
        if (!ps.implies(permission)) {
            ps.add(permission);
        }

        if (group.getSite().isInheritSecurity()) {
            Long parentGroupId = group.getParentId();
            if (parentGroupId != null) {
                try {
                    Group parentGroup = (Group) session.load(Group.class,
                        parentGroupId);
                    addPermissionToGroup(session, principalPermissions,
                                         parentGroup, permission);
                }
                catch (HibernateException ex) {
                    throw new DgException("Unable to process parent group", ex);
                }
            }
        }

    }

    public String toXML() {
        logger.debug("toString() called");
        final String newLine = "\n";
        HashMap principalPermissionsMap = null;
        StringBuffer buffer = new StringBuffer();
        try {
            principalPermissionsMap = getPrincipalPermissions(false);

            buffer.append("<dbpolicy>").append(newLine);
            Iterator iter = principalPermissionsMap.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry item = (Map.Entry) iter.next();
                Long groupId = (Long) item.getKey();
                buffer.append(" ").append("<group id=\"" + groupId + "\">").
                    append(newLine);
                Permissions ps = (Permissions) item.getValue();

                for (Enumeration permissions = ps.elements();
                     permissions.hasMoreElements(); ) {
                    ResourcePermission permission = (ResourcePermission)
                        permissions.nextElement();
                    buffer.append("    ").append(permission).append(newLine);
                }

                buffer.append(" ").append("</group>").append(newLine);
            }

            buffer.append("</dbpolicy>");
        }
        catch (DgException ex) {
            logger.error("Unable to print DBPolicy", ex);
        }

        return buffer.toString();
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
    }

    private void readObject(ObjectInputStream ois) throws
        ClassNotFoundException, IOException {
        ois.defaultReadObject();
    }

}
