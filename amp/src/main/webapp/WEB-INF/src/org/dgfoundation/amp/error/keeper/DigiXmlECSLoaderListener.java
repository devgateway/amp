package org.dgfoundation.amp.error.keeper;

import org.apache.commons.digester.Digester;
import org.apache.log4j.Logger;
import org.digijava.kernel.config.DigiConfig;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.util.DigiConfigManager;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;

public class DigiXmlECSLoaderListener implements ServletContextListener {
    private static Logger logger = Logger.getLogger(DigiXmlECSLoaderListener.class);
    public static String DISABLE_ECS="ecsDisable";
    public static String SERVER_NAME="ecsServerName";
    public static String PROPERTIES_FILE="propertiesFile";
    
    public void contextInitialized(ServletContextEvent contextEvent) {
        try {
            
            ServletContext servletContext = contextEvent.getServletContext();
            
            String warPath = servletContext.getRealPath("repository");
    
            String configDirectory = warPath;
            
            File configDirFile = new File(configDirectory);
            if (!configDirFile.exists() || !configDirFile.isDirectory()) {
                throw new DgException("Configuration directory " + configDirectory + " does not exist or is not directory");
            } else {
                logger.debug("Parsing configuration from " + configDirectory);
            }
            // Create Digester object
            Digester digester = DigiConfigManager.createConfigDigester();
            File configFile = new File(configDirFile.getAbsolutePath() + File.separator + DigiConfigManager.CONFIG_FILE);
            DigiConfig digiConfig = null;
            try {
                logger.debug("Parsing " + configFile.getName());
                digiConfig = (DigiConfig) digester.parse(configFile);
                logger.info("File " + configFile.getName() + " was parsed successfully");
            }
            catch (Exception ex) {
                logger.debug("Error while parsing DiGi configuration file: " + configFile.getName(), ex);
                throw new DgException(
                    "Error while parsing DiGi configuration file: " + configFile.getName(), ex);
            }
            
            String ecsDisable = digiConfig.getEcsDisable();//servletContext.getInitParameter(DISABLE_ECS);
            String ecsServerName = digiConfig.getEcsServerName();//servletContext.getInitParameter(SERVER_NAME);
            String propertiesFile = digiConfig.getPropertiesFile();//servletContext.getInitParameter(PROPERTIES_FILE);
            
            servletContext.setAttribute(SERVER_NAME, ecsServerName);
            servletContext.setAttribute(DISABLE_ECS, ecsDisable);
            servletContext.setAttribute(PROPERTIES_FILE, propertiesFile);
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }
    public void contextDestroyed(ServletContextEvent contextEvent) {
    
    }
    
    public static synchronized void destroy() {
    }
}
