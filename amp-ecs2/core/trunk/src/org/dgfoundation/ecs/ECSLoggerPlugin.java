package org.dgfoundation.ecs;


import java.io.File;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.dgfoundation.ecs.core.ECSClientManager;
import org.dgfoundation.ecs.exceptions.ECSException;
import org.dgfoundation.ecs.logger.ECSLogger;
import org.dgfoundation.ecs.logger.ECSRepositorySelector;
import org.jboss.logging.LoggerPlugin;

public class ECSLoggerPlugin implements LoggerPlugin
{
	private Logger log;
	private static Boolean init = true; 

	public void init(String name)
	{
		synchronized (init) {
			if (init){
				String fileName = "log4j.properties";
				File f = new File(fileName);
				String serverName=System.getProperty("amp.ecs.serverName");
				if ((serverName == null) || (serverName.compareTo("") == 0)) 
					serverName = "root";
				
				ECSRepositorySelector.serverName = serverName;
				
				System.out.println("............................................................");
				System.out.println(".......................Ꜿ.ECS.System.........................");
				System.out.println("............................................................");
				System.out.println(".....starting up . . .                                 .....");
				String outp = ".....server name: " + serverName;
				for (int i = 0; i < 37 - serverName.length() ; i++){
					outp += " ";
				}
				outp += ".....";
				System.out.println(outp);
				if (f.exists()){
					System.out.println(".....config file found                                 .....");
					System.out.println(".....configuring log4j                                 .....");
					PropertyConfigurator.configureAndWatch(fileName);
					ECSRepositorySelector.jbossPropertiesFile = f.getAbsolutePath();
				}
				else{
					System.out.println(".....config file not found                             .....");
					System.out.println(".....place config file in JBOSS_HOME/bin               .....");
					ECSRepositorySelector.jbossPropertiesFile = null;
				}
				System.out.println(".....initializing ecs                                  .....");
				
				try {
					ECSClientManager.getClient();
				} catch (ECSException e) {
					e.printStackTrace();
				}
				Logger unusedButUsefull = Logger.getLogger("ecs.root.unused.logger.but.usefull");
				ECSRepositorySelector.init();
				System.out.println(".....startup successful                                .....");
				System.out.println("............................................................");
				init = false;
			}
		}

		log = ECSLogger.getLogger(name);
	}

	public boolean isTraceEnabled()
	{
		return log.isEnabledFor(Level.TRACE);
	}

	public void trace(Object message)
	{
		log.trace(message.toString());
	}

	public void trace(Object message, Throwable t)
	{
		log.log(Level.TRACE, message.toString(), t);
	}

	public boolean isDebugEnabled()
	{
		return log.isEnabledFor(Level.DEBUG);
	}

	public void debug(Object message)
	{
		log.debug(message.toString());
	}

	public void debug(Object message, Throwable t)
	{
		log.log(Level.DEBUG, message.toString(), t);
	}

	public boolean isInfoEnabled()
	{
		return log.isEnabledFor(Level.INFO);
	}

	public void info(Object message)
	{
		log.info(message.toString());
	}

	public void info(Object message, Throwable t)
	{
		log.log(Level.INFO, message.toString(), t);
	}

	public void warn(Object message)
	{
		log.warn(message.toString());
	}

	public void warn(Object message, Throwable t)
	{
		log.log(Level.WARN, message.toString(), t);
	}

	public void error(Object message)
	{
		log.error(message.toString());
	}

	public void error(Object message, Throwable t)
	{
		log.log(Level.ERROR, message.toString(), t);
	}

	public void fatal(Object message)
	{
		log.fatal(message.toString());
	}

	public void fatal(Object message, Throwable t)
	{
		log.log(Level.FATAL, message.toString(), t);
	}
}
