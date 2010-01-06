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
				String fileName = "log4j.xml";
				File f = new File(fileName);
				System.out.println("............................................................");
				System.out.println(".........................ecs.system.........................");
				System.out.println("............................................................");
				System.out.println(".....starting up . . .                                 .....");
				System.out.println(".....searching for config file:                        .....");
				if (f.exists()){
					System.out.println(".....                          found                   .....");
					System.out.println(".....configuring log4j                                 .....");
					PropertyConfigurator.configureAndWatch(fileName);
				}
				else{
					System.out.println(".....                          not found               .....");
					System.out.println(".....place config file in JBOSS_HOME/bin               .....");
				}
				System.out.println(".....initializing ecs                                  .....");
				/*
				try {
					Thread.sleep(4000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				*/
				
				try {
					ECSClientManager.getClient();
				} catch (ECSException e) {
					e.printStackTrace();
				}
				Logger unusedButUsefull = Logger.getLogger("ecs.root.unused.logger.but.usefull");
				ECSRepositorySelector.init();
				System.out.println(".....startup successful in                             .....");
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
