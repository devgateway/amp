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

package org.digijava.kernel.util;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.security.auth.Subject;

import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;

import org.apache.log4j.Logger;
import org.digijava.kernel.Constants;
import org.digijava.kernel.config.moduleconfig.Action;
import org.digijava.kernel.config.moduleconfig.ModuleConfig;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.security.DgSecurityManager;
import org.digijava.kernel.security.ResourcePermission;

/**
 * This class contains module-related utillity functions. Module must be
 * identified <b>only</b> using module name (String)
 */
public class ModuleUtils {
    private static Logger logger = Logger.getLogger(ModuleUtils.class);

    /**
     * Returns set of all identity types, defined for module's actions
     * @param moduleName String module name
     * @return Set of identity types. Set is empty, if there are no identities
     * defined
     */
    public Set getItemIdentityTypes(String moduleName) {
        HashSet itemIdentityTypes = new HashSet();
        ModuleConfig moduleConfig = DigiConfigManager.getModuleConfig(
            moduleName);
        if (moduleConfig != null && moduleConfig.getActions() != null) {
            Iterator iter = moduleConfig.getActions().iterator();
            while (iter.hasNext()) {
                Action item = (Action)iter.next();
                if (item.getIdentityType() != null) {
                    itemIdentityTypes.add(item.getIdentityType());
                }

            }
        }
        return itemIdentityTypes;
    }

    /**
     * Returns module default instance, defined in module-config.xml
     * <default-instance></default-instance>
     * @param moduleName name of the module
     * @return default instance name
     */
    public static String getModuleDefaultInstance(String moduleName ) {

        ModuleConfig moduleConfig = DigiConfigManager.getModuleConfig(moduleName);
        if( moduleConfig != null ) {
            return (moduleConfig.getDefaultInstance() == null) ?
                Constants.DEFAULT_INSTANCE : moduleConfig.getDefaultInstance();
        }

        return Constants.DEFAULT_INSTANCE;
    }

    /**
     * Get <code>ModuleInstance</code> object for given id
     * @param id module instance identity
     * @return <code>ModuleInstance</code>
     * @throws DgException of error occurs
     */
    public static ModuleInstance getModuleInstance(Long id) throws DgException {
        ModuleInstance moduleInstance = null;
        Session session = null;
        try {
            session = PersistenceManager.getSession();
            try {
                moduleInstance = (ModuleInstance) session.load(ModuleInstance.class, id);
            }
            catch (ObjectNotFoundException ex2) {
                logger.debug("ModuleInstance# " + id + " not found");
            }
        }
        catch (Exception ex) {
            logger.debug("Unable to get site from database ", ex);
            throw new DgException("Unable to get site from database ", ex);
        }
        return moduleInstance;
    }

    /**
     * Returns ModuleInstance object for given site, module and instance
     * identities
     * @param siteId String identity of site
     * @param module Name of the module
     * @param instance Name of the instance
     * @return ModuleInstance
     */
    public static ModuleInstance getModuleInstance(Long siteId, String module,
        String instance)  {
        SiteCache siteCache = SiteCache.getInstance();
        Site site = siteCache.getSite(siteId);
        if (site == null) {
            return null;
        }
        List instances = siteCache.getInstances(site);
        if (instances == null) {
            return null;
        }
        Iterator iter = instances.iterator();
        while (iter.hasNext()) {
            ModuleInstance item = (ModuleInstance) iter.next();
            if (item.getModuleName().equals(module) && item.getInstanceName().equals(instance)) {
                return item;
            }
        }
        return null;
    }

    /**
     * Returns Shared ModuleInstance object for module and instance identities.
     * The module instance is "shared" if it does not have site assigned. In
     * this case, this module instance exists on <b>all</b> sites. Examples are
     * admin:default, um:user, exception:default, etc
     * @param module Name of the module
     * @param instance Name of the instance
     * @return ModuleInstance
     */
    public static ModuleInstance getSharedModuleInstance(String module, String instance)  {
        SiteCache siteCache = SiteCache.getInstance();
        List instances = siteCache.getSharedInstances();

        Iterator iter = instances.iterator();
        while (iter.hasNext()) {
            ModuleInstance item = (ModuleInstance) iter.next();
            if (item.getModuleName().equals(module) && item.getInstanceName().equals(instance)) {
                return item;
            }
        }
        return null;
    }
    /**
     * Returns true, if logged user has content administrator rights on the
     * module instance or false if user does not have such rights.
     *
     * @param subject Authentication Subject
     * @param currentSite Site on which security check is performed
     * @param moduleInstance ModuleInstance
     * @return boolean
     */
    public static boolean isContentAdministrator(Subject subject,
                                                 Site currentSite,
                                                 ModuleInstance moduleInstance) {
        return DgSecurityManager.permitted(subject, currentSite, moduleInstance,
                                           ResourcePermission.INT_CONTENT_ADMIN);
    }

    /**
     * Returns true, if logged user has content administrator rights on the
     * module instance or false if user does not have such rights.
     *
     * @param subject Authentication Subject
     * @param currentSite Site on which security check is performed
     * @param moduleInstance ModuleInstance
     * @return boolean
     */
    public static boolean isAdministrator(Subject subject,
                                                 Site currentSite,
                                                 ModuleInstance moduleInstance) {
        return DgSecurityManager.permitted(subject, currentSite, moduleInstance,
                                           ResourcePermission.INT_ADMIN);
    }

}
