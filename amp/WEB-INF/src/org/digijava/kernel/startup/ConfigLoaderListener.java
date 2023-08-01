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

package org.digijava.kernel.startup;

import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.endpoints.security.Security;
import org.digijava.kernel.config.moduleconfig.ModuleConfig;
import org.digijava.kernel.exception.IncompatibleEnvironmentException;
import org.digijava.kernel.mail.scheduler.MailSpoolManager;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.security.DigiPolicy;
import org.digijava.kernel.service.ServiceContext;
import org.digijava.kernel.service.ServiceManager;
import org.digijava.kernel.service.WebappServiceContext;
import org.digijava.kernel.translator.util.TrnAccesTimeSaver;
import org.digijava.kernel.util.AccessLogger;
import org.digijava.kernel.util.DigiCacheManager;
import org.digijava.kernel.util.DigiConfigManager;
import org.digijava.kernel.util.SiteCache;
import org.digijava.kernel.viewmanager.ViewConfigFactory;
import org.digijava.module.xmlpatcher.core.SimpleSQLPatcher;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServlet;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.security.Policy;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Parses digi.xml configuration file,
 * this class initialize when web context load
 * @author Lasha Dolidze
 * @version 1.0
 */

public class ConfigLoaderListener

    extends HttpServlet implements ServletContextListener {

    private static final long  MB = 1024L * 1024L;

    private static Logger logger = Logger.getLogger(ConfigLoaderListener.class);

    private static final String  ORACLE_DB = "ORACLE";

    private static final String  DISABLE_MEM_PARAM=". To disable checking for development purposes, add -Damp.disableMemCheck=true to the VM arguments.";
    
    
    private static String STARTUP_BEGIN_MESSAGE = "Attempting to start up AMP";
    private static String STARTUP_COMPLETE_MESSAGE = "AMP startup apparently successful";
    private static String STARTUP_FAILED_MESSAGE = "AMP startup failed; exception message: ";
    private static String STARTUP_LOGGER = "AMP startup";
    
    private static String MODULE_LISTENERS = ConfigLoaderListener.class.
        getName() + ".moduleContextListeners";
    private static ExecutorService exec ;
    private static TrnAccesTimeSaver tats;
    
    public static int parseBugFixingVersion(String completeProductVersion, String versionPrefix) {
            int indexOf = completeProductVersion.indexOf(versionPrefix);
            String bugFixingVersionString="";
            for(int i=indexOf+versionPrefix.length()+1;i<completeProductVersion.length();i++) {
                char c=completeProductVersion.charAt(i);
                if(c<'0' || c>'9') break;
                bugFixingVersionString+=c;
            }
        if(bugFixingVersionString.length()==0) return 0;
        return Integer.parseInt(bugFixingVersionString);
    }

    public void contextInitialized(ServletContextEvent sce) {
        //ResourceStreamHandlerFactory.installIfNeeded();
        String path = sce.getServletContext().getRealPath("/") + Security.getSiteConfigPath();
        try {
            String jaasConfPath = sce.getServletContext().getRealPath("/WEB-INF/jaas.config");
            if (jaasConfPath != null) {
                File file = new File(jaasConfPath);
                if (file.exists()) {
                    System.setProperty("java.security.auth.login.config", jaasConfPath);
                }
            }
            // Custom cache manager must be initialized first
            DigiConfigManager.initialize(sce.getServletContext().getRealPath("/repository"));
            // Initialize services
            ServiceContext serviceContext = new WebappServiceContext(sce.getServletContext());
            ServiceManager.getInstance().init(serviceContext, 0);

            DigiCacheManager.getInstance();
            new SimpleSQLPatcher().doWork();
            PersistenceManager.initialize(true);

            checkDatabaseCompatibility( sce.getServletContext().getRealPath("/compat.properties"));
            BuildVersionVerifier bvv = BuildVersionVerifier.getInstance(path);
            bvv.writeVersionToStartupLog(STARTUP_LOGGER, STARTUP_BEGIN_MESSAGE, path);
            bvv.checkAmpVersionCompatibility();

            checkMemoryAllocation( sce.getServletContext().getRealPath("/compat.properties"));
            
            checkOtherVMParameters();

            PersistenceManager.inTransaction(() -> continueInitialization(sce, serviceContext));
        } catch (Exception ex) {
            logger.debug("Unable to initialize", ex);
            try {
                BuildVersionVerifier.getInstance(path).writeVersionToStartupLog(STARTUP_LOGGER, STARTUP_FAILED_MESSAGE + ex.getMessage(), path);
            } catch (Exception e) {
                logger.error("Failed to write error message to startup log: " + e.getMessage());
            }
            throw new RuntimeException("Unable to initialize", ex);
        }

        try {
            BuildVersionVerifier.getInstance(path).writeVersionToStartupLog(STARTUP_LOGGER, STARTUP_COMPLETE_MESSAGE, path);
        } catch (Exception e) {
            logger.error("Failed to write error message to startup log: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void continueInitialization(ServletContextEvent sce, ServiceContext serviceContext) {
        try {
            SiteCache.getInstance();
            DigiPolicy policy = new DigiPolicy();
            policy.install();

            if (DigiConfigManager.getConfig().getSmtp().isEnable()) {
                MailSpoolManager.initialize();
            }

            // Initialize modules
            Map listeners = getModuleContextInitializers();
            Iterator iter = listeners.values().iterator();
            while (iter.hasNext()) {
                ServletContextListener item = (ServletContextListener) iter.next();
                item.contextInitialized(sce);
            }
            sce.getServletContext().setAttribute(MODULE_LISTENERS, listeners);


            // Initialize services
            ServiceManager.getInstance().init(serviceContext, 1);

            ViewConfigFactory.initialize(sce.getServletContext());

            // patches translations to hash code keys if this is not already done.
            tats = new TrnAccesTimeSaver();
            exec = Executors.newSingleThreadExecutor();
            exec.execute(tats);
        } catch (Exception e) {
            throw new RuntimeException("Init failed", e);
        }
    }

    /**
     * Uses Memory pool MXBeans to check if AMP has the right amount of memory allocated
     * @param propertiesFileName path to the compat.properties
     * @throws FileNotFoundException
     * @throws IOException
     * @throws NumberFormatException
     * @throws IncompatibleEnvironmentException
     */
    private void checkMemoryAllocation(String propertiesFileName) throws FileNotFoundException, IOException, NumberFormatException, IncompatibleEnvironmentException {
        Properties compat=new Properties();
        File compatFile=new File(propertiesFileName);
        compat.load(new FileInputStream(compatFile));
        boolean verify=true;
        String disableMemCheck=System.getProperty("amp.disableMemCheck");
        if(disableMemCheck!=null && "true".equalsIgnoreCase(disableMemCheck)) {
            verify=false;
            logger.warn("Memory Allocation Check Disabled! THIS SHOULD ONLY BE USED IN DEVELOPMENT ENVIRONMENTS !!");
        }
             
        Iterator iter = ManagementFactory.getMemoryPoolMXBeans().iterator();  
        while(iter.hasNext()){  
           MemoryPoolMXBean item = (MemoryPoolMXBean) iter.next();  
               MemoryUsage mu = item.getUsage();  
               long used      = mu.getUsed()/MB;  
               long committed = mu.getCommitted()/MB;  
               long max       = mu.getMax()/MB;  
               logger.info("MEMORY Type "+item.getName()+": Used="+used+"m; Committed="+committed+"m; Max="+max+"m");
               String setting=compat.getProperty("jvm."+item.getName().replaceAll(" ","").toLowerCase());
               if(verify && setting!=null && Long.parseLong(setting)>max) throw new IncompatibleEnvironmentException("The JVM does not have enough memory allocated. Memory Type "+item.getName()+"; Max available="+max+"m; Max required="+Long.parseLong(setting)+"m"+DISABLE_MEM_PARAM); 
        }
        long maxMemAvaiable=Runtime.getRuntime().maxMemory()/MB;
        logger.info("MEMORY Total Max: "+maxMemAvaiable+"m");
        long maxMemRequired=Long.parseLong(compat.getProperty("jvm.maxmem"));
        if(verify &&  maxMemRequired>maxMemAvaiable) throw new IncompatibleEnvironmentException("The JVM does not have enough TOTAL memory allocated; Max available="+maxMemAvaiable+"m; Max required="+maxMemRequired+"m"+DISABLE_MEM_PARAM); 
        logger.info("Memory Allocation Check OK.");
    }
    
    /**
     * Checks if the database server to which AMP connects as well as the JDBC driver used are compatible with the testing environment that AMP is using.
     * @param propertiesFileName path to the compat.properties file
     * @throws FileNotFoundException
     * @throws IOException
     * @throws SQLException
     * @throws IncompatibleEnvironmentException
     */
    private void checkDatabaseCompatibility(String propertiesFileName) throws FileNotFoundException, IOException, SQLException, IncompatibleEnvironmentException {
        Connection connection = PersistenceManager.getJdbcConnection();
        DatabaseMetaData metaData = connection.getMetaData();
        Properties compat=new Properties();
        File compatFile=new File(propertiesFileName);
        compat.load(new FileInputStream(compatFile));
        
        String prefix=null;
        if(metaData.getDatabaseProductName().equals("PostgreSQL")) prefix="postgresql";else
        prefix =(metaData.getDatabaseProductVersion().toUpperCase().indexOf(ORACLE_DB)>-1)?"oracle":"mysql";
        
        logger.info("AMP Running on "+metaData.getDatabaseProductName()+" "+metaData.getDatabaseProductVersion());
        
        int dbMajorVersion=Integer.parseInt((String)compat.get(prefix+".version.major"));
        int dbMinorVersion=Integer.parseInt((String)compat.get(prefix+".version.minor"));
        int dbBugfixingVersion=Integer.parseInt((String)compat.get(prefix+".version.bugfixing"));
        
        int jdbcMajorVersion=Integer.parseInt((String)compat.get(prefix+".jdbc.version.major"));
        int jdbcMinorVersion=Integer.parseInt((String)compat.get(prefix+".jdbc.version.minor"));
        int jdbcBugfixingVersion=Integer.parseInt((String)compat.get(prefix+".jdbc.version.bugfixing"));
        
        if(metaData.getDatabaseMajorVersion()!=dbMajorVersion || 
                //metaData.getDatabaseMinorVersion()!=dbMinorVersion || 
                dbBugfixingVersion>parseBugFixingVersion(metaData.getDatabaseProductVersion(), metaData.getDatabaseMajorVersion()+"."+metaData.getDatabaseMinorVersion())) 
            throw new IncompatibleEnvironmentException("Database version ("+metaData.getDatabaseProductVersion()+") is incompatible. Database version needs to be "+dbMajorVersion+"."+dbMinorVersion+" and bugfixing version at least "+dbBugfixingVersion);
    
        if(metaData.getDriverMajorVersion()!=jdbcMajorVersion || 
                // metaData.getDriverMinorVersion()!=jdbcMinorVersion || 
                jdbcBugfixingVersion>parseBugFixingVersion(metaData.getDriverVersion(), metaData.getDriverMajorVersion()+"."+metaData.getDriverMinorVersion())) 
            throw new IncompatibleEnvironmentException("JDBC driver version ("+metaData.getDriverVersion()+") is incompatible. JDBC version needs to be "+jdbcMajorVersion+"."+jdbcMinorVersion+" and bugfixing version at least "+jdbcBugfixingVersion);
        
        logger.info("Database compatibility OK.");
        
        PersistenceManager.closeQuietly(connection);
    }
    
    /**
     * Checks other properties that have to be set in order for AMP to work properly
     * @throws IncompatibleEnvironmentException
     */
    private void checkOtherVMParameters() throws IncompatibleEnvironmentException {
        String awtHeadless=System.getProperty("java.awt.headless");
        if(awtHeadless==null || !"true".equalsIgnoreCase(awtHeadless)) throw new IncompatibleEnvironmentException("Please add -Djava.awt.headless=true to the VM parameters. This is required in order to get the charts work properly");
    }
    
    private Map getModuleContextInitializers() throws ClassNotFoundException,
        InstantiationException, IllegalAccessException, InstantiationException,
        ClassCastException {
        // Initialize modules
        Map contextListeners = new HashMap();
        Iterator moduleConfigIter = DigiConfigManager.getModulesConfig().entrySet().iterator();
        while (moduleConfigIter.hasNext()) {
            Map.Entry configRecord = (Map.Entry) moduleConfigIter.next();
            ModuleConfig moduleConfig = (ModuleConfig) configRecord.getValue();
            Iterator moduleLsnrIter = moduleConfig.getWebappContextListeners().iterator();
            while (moduleLsnrIter.hasNext()) {
                String listenerClassName = (String) moduleLsnrIter.next();
                if (!contextListeners.containsKey(listenerClassName)) {
                    logger.debug("Loading context listener class " +
                                 listenerClassName + " for module " +
                                 ( (String) configRecord.getKey()));
                    Class listenerClass = Class.forName(listenerClassName);
                    ServletContextListener listenerObj = (ServletContextListener) listenerClass.newInstance();
                    contextListeners.put(listenerClassName, listenerObj);
                }
            }
        }
        return contextListeners;
    }

    /**
     * contextDestroyed method call when ServletContextListener object destroy
     * see ServletContextListener form more details.
     */
    public void contextDestroyed(ServletContextEvent sce) {     
        
        //shut down translation thread
        if (tats != null) {
            tats.shutdown();
        }
        
        ServiceManager.getInstance().shutdown(1);
        try {
            Map listeners = (Map) sce.getServletContext().getAttribute(
                MODULE_LISTENERS);
            if (listeners != null) {
                Iterator iter = listeners.values().iterator();
                while (iter.hasNext()) {
                    ServletContextListener item = (ServletContextListener) iter.
                        next();
                    item.contextDestroyed(sce);
                }
            }
        }
        catch (Exception ex) {
            logger.warn("Module destruction failed", ex);
        }

        try {
            if (Policy.getPolicy() instanceof DigiPolicy) {
                DigiPolicy policy = (DigiPolicy) Policy.getPolicy();
                policy.uninstall();
            }
        }
        finally {
            AccessLogger.shutdown();
            PersistenceManager.cleanup();
            // Custom cache manager must be shut after all other cleanup stuff
            DigiCacheManager.shutdown();
            if (exec != null) {
                exec.shutdownNow();
            }
            ServiceManager.getInstance().shutdown(0);
        }
    }
}
