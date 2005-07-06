/*
 *   ModuleUtils.java
 * 	 @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Jan 29, 2004
 * 	 CVS-ID: $Id: ModuleUtils.java,v 1.1 2005-07-06 12:00:13 rahul Exp $
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
package org.digijava.kernel.util;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.digijava.kernel.config.moduleconfig.Action;
import org.digijava.kernel.config.moduleconfig.ModuleConfig;
import org.digijava.kernel.Constants;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import net.sf.hibernate.Session;
import org.apache.log4j.Logger;
import net.sf.hibernate.*;

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
     *
     * @param moduleName
     * @return
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
     * Get <code>ModuleInstance</code> object for the site id
     * @param id
     * @return
     * @throws DgException
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
        finally {
            try {
                if (session != null) {
                    PersistenceManager.releaseSession(session);
                }
            }
            catch (Exception ex1) {
                logger.warn("releaseSession() failed ", ex1);
            }
        }
        return moduleInstance;
    }

}
