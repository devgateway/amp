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

import java.io.File;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.StandaloneJndiAMPInitializer;
import org.digijava.kernel.persistence.HibernateClassLoader;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.util.resource.ResourceStreamHandlerFactory;
import org.hibernate.HibernateException;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;

public class DigiSchemaExport {
    private static Logger logger = Logger.getLogger(DigiSchemaExport.class);

    public static void main(String[] args) throws Exception {
        //StandaloneJndiAMPInitializer.initAMPUnifiedJndiAlias();
        
        
        /**
         * Change hibernate configuration file
         */
        HibernateClassLoader.HIBERNATE_CFG_XML = "/standAloneAmpHibernate.cfg.xml";
        
        ResourceStreamHandlerFactory.installIfNeeded();

        Map commandLineParams = getCommandLineParameters(args);
        logger.info("Command-line parameters: " + commandLineParams);
        String moduleName = (String) commandLineParams.get("-m");

        if (moduleName != null) {
            logger.info("Working for the module " + moduleName);
        }
        else {
            logger.info("Working for the whole database");
        }

        DigiConfigManager.initialize("./repository");
        PersistenceManager.initialize(false, moduleName);
        try {
            Configuration cfg = null;
            try {
                cfg = HibernateClassLoader.getConfiguration();
            }
            catch (Exception ex1) {
                logger.error("Error creating hibernate configuration", ex1);
                throw ex1;
            }
            URL configFileURL = new File("/standAloneAmpHibernate.cfg.xml").toURI().toURL(); //some method to get hold of the location of your hibernate.cfg.xml
            StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder().configure(configFileURL).build();
            Metadata metaData = new MetadataSources(standardRegistry).getMetadataBuilder().build();
            Collection<PersistentClass> entityBindings = metaData.getEntityBindings();
            Iterator<PersistentClass> classIter = entityBindings.iterator();

            if (commandLineParams.containsKey("-names")) {
                logger.info("Generating comma-separated table names");

//                Iterator classIter = cfg.getClassMappings();
                boolean first = true;
                StringBuffer tables = new StringBuffer();
                while (classIter.hasNext()) {
                    PersistentClass item = classIter.next();
                    String tableName = item.getTable().getName();
                    if (first) {
                        first = false;
                    } else {
                        tables.append(",");
                    }
                    tables.append(tableName);
                }
                logger.info("Table names: " + tables.toString());
            }
            else {
                boolean doUpdate = !commandLineParams.containsKey("-show");
                if (doUpdate) {
                    logger.info("Updating database schema");
                } else {
                    logger.info("Skipping database schema update");
                }
                try {
                    new SchemaUpdate(cfg).execute(true, doUpdate);
                }
                catch (HibernateException ex) {
                    logger.error("Error updating schema ", ex);
                    throw ex;
                }
            }
        }
        finally {
            PersistenceManager.cleanup();
        }
    }

    private static Map getCommandLineParameters(String[] parameters) {
        HashMap result = new HashMap();
        String key = null;
        for (int i = 0; i < parameters.length; i++) {
            logger.info(parameters[i]);
            if (parameters[i].startsWith("-")) {
                if (key != null) {
                    result.put(key, "########");
                }
                key = parameters[i];
            }
            else {
                if (key == null) {
                    logger.error("Unknown switch: " + parameters[i]);
                }
                else {
                    result.put(key, parameters[i]);
                    key = null;
                }
            }
        }
        if (key != null) {
            result.put(key, "########");
        }
        return result;
    }
}
