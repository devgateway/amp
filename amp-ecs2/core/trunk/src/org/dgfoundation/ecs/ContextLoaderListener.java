package org.dgfoundation.ecs;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.dgfoundation.ecs.logger.ECSRepositorySelector;

public class ContextLoaderListener implements ServletContextListener {
	public static String DISABLE_ECS="ecsDisable";
	public static String SERVER_NAME="ecsServerName";
	
	public void contextInitialized(ServletContextEvent contextEvent) {
		try {
			
			ServletContext servletContext = contextEvent.getServletContext();
			String ecsDisable = servletContext.getInitParameter(DISABLE_ECS);
			String ecsServerName = servletContext.getInitParameter(SERVER_NAME);
			
			if ("true".compareToIgnoreCase(ecsDisable) == 0 || ecsServerName == null){
				if (ecsServerName == null){
					System.out.println("+++++++++++++++++++++++++++++++++++++++++");
					System.out.println("+ You need to set ecsServerName context +");
					System.out.println("+ parameter in order to get ECS running +");
					System.out.println("+              ECS DISABLED             +");
					System.out.println("+++++++++++++++++++++++++++++++++++++++++");
				}
				else
					System.out.println("EcsDisable = true");
				ECSRepositorySelector.init(true, ecsServerName);
			}
			else{
				System.out.println("EcsDisable = false");
				ECSRepositorySelector.init(false, ecsServerName);
			}
		} catch (Exception ex) {
			System.err.println(ex);
		}
	}
	public void contextDestroyed(ServletContextEvent contextEvent) {
		ECSRepositorySelector.destroy();
	}
}
