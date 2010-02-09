package org.dgfoundation.amp.error.keeper;

import java.io.File;
import java.lang.reflect.Method;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.digester.Digester;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerRepository;
import org.digijava.kernel.config.DigiConfig;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.util.DigiConfigManager;

public class ContextLoaderListener implements ServletContextListener {
	private static Logger logger = Logger.getLogger(ContextLoaderListener.class);
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
			
			
			if ((propertiesFile != null) && (propertiesFile.trim().length() == 0))
				propertiesFile = null;
			boolean ecsDisableBool;
			if (ecsDisable != null && ecsDisable.compareTo("false") == 0)
				ecsDisableBool = false;
			else
				ecsDisableBool = true;
			
			init(ecsDisableBool, ecsServerName, propertiesFile, warPath);
			
		} catch (Exception ex) {
			System.err.println(ex);
		}
	}
	public void contextDestroyed(ServletContextEvent contextEvent) {
		destroy();
	}
	
	public static synchronized void init(Boolean ecsDisable, String serverName, String propertiesFile, String warPath){
		LoggerRepository current = LogManager.getLoggerRepository();
		if (current.getClass().getCanonicalName().startsWith("org.dgfoundation.ecs.logger")){//already changed
			if (ecsDisable || serverName == null){
				if (serverName == null){
					logger.info("+++++++++++++++++++++++++++++++++++++++++");
					logger.info("+ You need to set ecsServerName context +");
					logger.info("+ parameter in order to get ECS running +");
					logger.info("+              ECS DISABLED             +");
					logger.info("+++++++++++++++++++++++++++++++++++++++++");
				}
				else{
					logger.info("* Disabling ECS for: " + warPath);
					logger.info("* Server name is: " + serverName);
				}
			}
			else{
				logger.info("* Enabling ECS for: " + warPath);
				logger.info("* Server name is: " + serverName);
				logger.info("* Properties file: " + propertiesFile);
			}
			
			ClassLoader bsLoader = current.getClass().getClassLoader();
			try {
				Class bsRepo = bsLoader.loadClass("org.dgfoundation.ecs.logger.ECSRepositorySelector");
				Object repoInstance = bsRepo.newInstance();

				String methName;
				if (ecsDisable)
					methName = "initWithoutECS";
				else
					methName = "initWithECS";
				
				Method method = repoInstance.getClass().getMethod(methName, String.class, String.class);
				method.invoke(method, serverName, propertiesFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static synchronized void destroy() {
		//removeFromRepository();

		LoggerRepository current = LogManager.getLoggerRepository();
		if (current.getClass().getCanonicalName().startsWith("org.dgfoundation.ecs.logger")){//already changed
			ClassLoader bsLoader = current.getClass().getClassLoader();
			try {
				Class bsRepo = bsLoader.loadClass("org.dgfoundation.ecs.logger.ECSRepositorySelector");
				Object repoInstance = bsRepo.newInstance();

				String methName;
				methName = "destroy";
				
				Method method = repoInstance.getClass().getMethod(methName);
				method.invoke(method);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
}
