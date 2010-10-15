package org.dgfoundation.ecs;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerRepository;
import org.dgfoundation.ecs.keeper.ErrorKeeperRAM;

public class ContextLoaderListener implements ServletContextListener {
	private static Logger logger = Logger.getLogger(ErrorKeeperRAM.class);
	public static String DISABLE_ECS="ecsDisable";
	public static String SERVER_NAME="ecsServerName";
	public static String PROPERTIES_FILE="propertiesFile";
	
	public void contextInitialized(ServletContextEvent contextEvent) {
		try {
			
			ServletContext servletContext = contextEvent.getServletContext();
			
			String warPath = servletContext.getRealPath(".");
			
			
			String ecsDisable = (String) servletContext.getAttribute(DISABLE_ECS);
			String ecsServerName = (String) servletContext.getAttribute(SERVER_NAME);
			String propertiesFile = (String) servletContext.getAttribute(PROPERTIES_FILE);
			servletContext.removeAttribute(DISABLE_ECS);
			servletContext.removeAttribute(SERVER_NAME);
			servletContext.removeAttribute(PROPERTIES_FILE);
			System.setProperty("amp.ecs.serverName", ecsServerName);
			System.setProperty("amp.ecs.defaultDisabled", ecsDisable);
			
			
			if ((propertiesFile != null) && (propertiesFile.trim().length() == 0))
				propertiesFile = null;
			boolean ecsDisableBool;
			if (ecsDisable != null && ecsDisable.compareTo("false") == 0)
				ecsDisableBool = false;
			else
				ecsDisableBool = true;
			
			TomcatLoggerPlugin ilp = new TomcatLoggerPlugin();
			ilp.init("changethis", warPath);
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
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		
		logger.info("Stopping threads");
		
		LoggerRepository current = LogManager.getLoggerRepository();
		if (current.getClass().getCanonicalName().startsWith("org.dgfoundation.ecs.logger")){
			ClassLoader bsLoader = current.getClass().getClassLoader();
			try {
				Class bsRepo = bsLoader.loadClass("org.dgfoundation.ecs.logger.ECSRepositorySelector");
				Object repoInstance = bsRepo.newInstance();

				String methName = "destroy";
				Method method = repoInstance.getClass().getMethod(methName);
				method.invoke(method);
			} catch (Exception e){
				e.printStackTrace();
			}
		}
	}
}
