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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.internal.SessionImpl;
import org.apache.log4j.Logger;
import org.digijava.kernel.config.LogonSite;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.security.DigiPolicy;
import org.digijava.kernel.service.ServiceContext;
import org.digijava.kernel.service.ServiceManager;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.resource.ResourceStreamHandlerFactory;
import org.digijava.kernel.viewmanager.ViewConfigFactory;
import org.digijava.module.autopatcher.core.AutopatcherService;

public class DigiSchemaPopulate {

    private static Logger logger = Logger.getLogger(DigiSchemaPopulate.class);

    public static void main(String[] args) throws Exception {

        ResourceStreamHandlerFactory.installIfNeeded();

        ViewConfigFactory.initialize(new DummyServletContext("."));
        DigiConfigManager.initialize("./repository");
        PersistenceManager.initialize(false);
        ServiceContext serviceContext = new StandaloneServiceContext(".");
        ServiceManager.getInstance().init(serviceContext, 0);


        if (!DigiInitUtil.checkConfiguration()) {
            logger.error("Critical errors found in configuration. Aborting installation");
            return;
        }

        try {
            logger.debug(" ### [ Begining Data population ] ###");

            //using autopatcher to apply initSQL patches
            AutopatcherService aps=new AutopatcherService();
            aps.setPatchesDir("/init_patches");
            aps.processInitEvent(serviceContext);
            
//            File sqlDir = new File("./WEB-INF/classes/initSQL");
//            //-- logger.debug ( sqlDir.getAbsolutePath() );
//
//            DigiFileFilter sqlFilter = new DigiFileFilter(".sql");
//            String[] sqlFiles = sqlDir.list(sqlFilter);
//            String path = "";
//
//            for (int i = 0; i < sqlFiles.length; i++) {
//                path = sqlDir.getAbsolutePath() + sqlDir.separator + sqlFiles[i];
//                logger.info("Processing: " + path);
//
//                DigiInitUtil.process(path);
//            }
//
            DigiPolicy policy = new DigiPolicy();
            policy.install();

            // Create login site
           // DigiInitUtil.createLoginSite();

            // Create demo site
            //DigiInitUtil.createDemoSite();

            // Create um/user and admin/default instances
            //DigiInitUtil.createCommonInstances();

            // Greate system user
            //DigiInitUtil.createGlobalAdmin();
        }
        finally {
            PersistenceManager.cleanup();
        }
    }

}

class DigiFileFilter
    implements FilenameFilter {
    String key;

    /**
     * Default constructor
     * @param key key
     */
    DigiFileFilter(String key) {
        this.key = key;
    }

    public boolean accept(File dir, String name) {

        String f = new File(name).getName();
        return f.indexOf(this.key) != -1;
    }
}

class DigiInitUtil {

    private static Logger logger = Logger.getLogger(DigiInitUtil.class);

    private static String[] hostIpAddresses;
    private static final String[][] instanceNames;

    static {
        // IP Addresses
        ArrayList extIpAddresses = new ArrayList();
        try {
            Enumeration interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) interfaces.nextElement();
                Enumeration ipAddresses = ni.getInetAddresses();
                while (ipAddresses.hasMoreElements()) {
                    InetAddress addr = (InetAddress) ipAddresses.nextElement();
                    if (!addr.isAnyLocalAddress() && !addr.isLoopbackAddress()) {
                        extIpAddresses.add(addr.getHostAddress());
                    }

                }

            }
            hostIpAddresses = new String[extIpAddresses.size() + 1];
            int i = 0;
            for (; i < hostIpAddresses.length - 1; i++) {
                hostIpAddresses[i] = (String) extIpAddresses.get(i);

            }
            hostIpAddresses[i] = "localhost";
        }
        catch (SocketException ex) {
            hostIpAddresses = null;
            logger.error("Unable to resolve IP addresses for the host", ex);
        }

        // Common instance names
        instanceNames = new String[][] {
            new String[] {
            "admin", "default"}
            ,
            new String[] {
            "um", "user"}
            ,
            new String[] {
            "exception", "default"}
        };
    }

    static boolean process(String path) {
        StringBuffer szFInput=new StringBuffer();
        String s;
        String[] commands;

        try {
            BufferedReader in = new BufferedReader(new FileReader(path));
            while ( (s = in.readLine()) != null) {
                
                szFInput.append(s).append('\n');
            }
            in.close();
        }
        catch (java.io.IOException e) {
            logger.error("process() failed ",e);
            return false;
        }

        //-- Commans array has each of the SQL commands in each element.
        //-- It is safer than assuming that one command is one line in sql file
        //-- One command may be several lines, but we can be sure that one
        //-- command is always between the last ";" and the next ";" so this
        //-- approach is safer. Of course ";" may theoretically be part of the sql
        //-- and that can ruin our logic, but we know that our data won't have things like that.

        commands = szFInput.toString().split(";");

        //-- Just a test
        Session session = null;
        Statement st = null;
        try {
            session = PersistenceManager.getSession();
//beginTransaction();
            st = ((SessionImplementor)session).connection().createStatement();

            for (int i = 0; i < commands.length; i++) {

                if (commands[i].trim().length() != 0) {
                    logger.info("Processing SQL command: " + commands[i]);
                    st.execute(commands[i]);
                }

            }
            //tx.commit();
        }
        catch (Exception ex) {
            logger.error("Could not Process Command",ex);
        }
        return true;
    }

    static void createLoginSite() throws Exception {
        if (DigiConfigManager.getConfig().getLogonSite() != null &&
            DigiConfigManager.getConfig().getLogonSite().getHost() != null &&
            DigiConfigManager.getConfig().getLogonSite().getHost().trim().
            length() != 0) {

            SiteManager.createSite("DiGi login site",
                                   DigiConfigManager.getConfig().getLogonSite().
                                   getId(),
                                   DigiConfigManager.getConfig().getLogonSite().
                                   getId(), new String[] {
                                   DigiConfigManager.getConfig().getLogonSite().
                                   getHost()}
                                   , new String[] {
                                   DigiConfigManager.getConfig().getLogonSite().
                                   getPath()},"SITE", "default");

        }
    }

    static void createDemoSite() throws Exception {
        SiteManager.createSite("Demo Site", "demo", "demo", hostIpAddresses, null, "SITE", "demoTemplate");
    }

    static void createCommonInstances() throws Exception {
        Session session = null;
        try {
            session = PersistenceManager.getSession();
//beginTransaction();

            for (int i = 0; i < instanceNames.length; i++) {
                ModuleInstance admin = new ModuleInstance();
                admin.setModuleName(instanceNames[i][0]);
                admin.setInstanceName(instanceNames[i][1]);
                admin.setPermitted(true);
                admin.setRealInstance(null);
                admin.setSite(null);

                session.save(admin);
            }

            //tx.commit();
        }
        catch (Exception ex) {
            logger.debug("Unable to create common instances",ex);

            throw new DgException(
                "Unable to create common instances", ex);
        }
    }

    static void createGlobalAdmin() throws Exception {
        Locale english = new Locale();
        english.setCode("en");
        english.setName("English");

        User user = new User();
        user.setGlobalAdmin(true);
        user.setFirstNames("System");
        user.setLastName("System");
        user.setEmail("system@digijava.org");
        user.setPassword("changeme");
        user.setRegisterLanguage(english);
        user.setBanned(false);
        user.setOrganizationTypeOther(" ");
        org.digijava.module.um.util.DbUtil.registerUser(user);
    }

    static boolean checkConfiguration() {
        LogonSite loginSite = DigiConfigManager.getConfig().getLogonSite();
        if (loginSite != null &&
            loginSite.getHost() != null &&
            loginSite.getHost().trim().length() != 0) {

//            String loginSiteHost = loginSite.getHost().trim();
//            if (loginSite.getPath() == null || loginSite.getPath().trim().length() == 0) {
//                for (int i = 0; i < hostIpAddresses.length; i++) {
//                    if (hostIpAddresses[i].equals(loginSiteHost)) {
//                        logger.error("Login site must be different than " +
//                                     "\"localhost\" and any non-local IP " +
//                                     "addresses for the machine. The current " +
//                                     "setting (" + loginSiteHost +
//                                     ") violates this rule");
//                        return false;
//                    }
//                }
//            }
        }
        return true;
    }

}
