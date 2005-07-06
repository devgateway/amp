/*
 *   DigiSchemaExport.java
 *   @Author Lasha Dolidze lasha@digijava.org
 *   Created:
     *   CVS-ID: $Id: DigiSchemaExport.java,v 1.1 2005-07-06 12:00:13 rahul Exp $
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

import org.digijava.kernel.persistence.HibernateClassLoader;
import org.digijava.kernel.persistence.PersistenceManager;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.cfg.Configuration;
import net.sf.hibernate.tool.hbm2ddl.SchemaUpdate;
import org.apache.log4j.Logger;
import java.util.Map;
import java.util.HashMap;
import java.util.*;
import net.sf.hibernate.mapping.PersistentClass;

public class DigiSchemaExport {
    private static Logger logger = Logger.getLogger(DigiSchemaExport.class);

    public static void main(String[] args) throws Exception {
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

            if (commandLineParams.containsKey("-names")) {
                logger.info("Generating comma-separated table names");

                Iterator classIter = cfg.getClassMappings();
                boolean first = true;
                StringBuffer tables = new StringBuffer();
                while (classIter.hasNext()) {
                    PersistentClass item = (PersistentClass)classIter.next();
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
                logger.info("Updating database schema");
                try {
                    new SchemaUpdate(cfg).execute(true, true);
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