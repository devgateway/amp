package org.dgfoundation.ecs;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.dgfoundation.ecs.keeper.ErrorKeeperRAM;
import org.dgfoundation.ecs.logger.ECSRepositorySelector;

public class ContextLoaderListener implements ServletContextListener {
	private static Logger logger = Logger.getLogger(ErrorKeeperRAM.class);
	public static String DISABLE_ECS="ecsDisable";
	public static String SERVER_NAME="ecsServerName";
	public static String PROPERTIES_FILE="propertiesFile";
	
	public void contextInitialized(ServletContextEvent contextEvent) {
		try {
			
			ServletContext servletContext = contextEvent.getServletContext();
			
			String warPath = servletContext.getRealPath(".");
			
			
			String ecsDisable = servletContext.getInitParameter(DISABLE_ECS);
			String ecsServerName = servletContext.getInitParameter(SERVER_NAME);
			String propertiesFile = servletContext.getInitParameter(PROPERTIES_FILE);
			
			
			if ((propertiesFile != null) && (propertiesFile.trim().length() == 0))
				propertiesFile = null;
			
			if ("true".compareToIgnoreCase(ecsDisable) == 0 || ecsServerName == null){
				if (ecsServerName == null){
					logger.info("+++++++++++++++++++++++++++++++++++++++++");
					logger.info("+ You need to set ecsServerName context +");
					logger.info("+ parameter in order to get ECS running +");
					logger.info("+              ECS DISABLED             +");
					logger.info("+++++++++++++++++++++++++++++++++++++++++");
				}
				else{
					logger.info("* Disabling ECS for: " + warPath);
					logger.info("* Server name is: " + ecsServerName);
				}
				ECSRepositorySelector.init(true, ecsServerName, propertiesFile);
			}
			else{
				logger.info("* Enabling ECS for: " + warPath);
				logger.info("* Server name is: " + ecsServerName);
				logger.info("* Properties file: " + propertiesFile);
				ECSRepositorySelector.init(false, ecsServerName, propertiesFile);
			}
		} catch (Exception ex) {
			System.err.println(ex);
		}
	}
	public void contextDestroyed(ServletContextEvent contextEvent) {
		ECSRepositorySelector.destroy();
	}
}
