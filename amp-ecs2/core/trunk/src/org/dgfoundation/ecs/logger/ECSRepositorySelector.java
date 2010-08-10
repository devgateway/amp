package org.dgfoundation.ecs.logger;

import org.apache.log4j.spi.RepositorySelector;
import org.apache.log4j.spi.LoggerRepository;
import org.apache.log4j.spi.RootCategory;
import org.apache.log4j.xml.DOMConfigurator;
import org.apache.log4j.xml.XMLLayout;
import org.apache.log4j.Hierarchy;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.PropertyConfigurator;
import org.dgfoundation.ecs.core.ECS;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ECSRepositorySelector implements RepositorySelector {
	private static boolean initialized = false;
	private static Object guard = LogManager.getRootLogger();
	private static Map<ClassLoader, LoggerRepository> repositories = new HashMap<ClassLoader, LoggerRepository>();
	private static Map<ClassLoader, LoggerRepository> repositories2 = new HashMap<ClassLoader, LoggerRepository>();
	private static LoggerRepository defaultRepository;
	private static LoggerRepository disabledRepository = null;
	private static LoggerRepository oldRepository;
	private static RegularLoggerRepository regularRepository;
	public static String serverName = "root";
	//public static String jbossPropertiesFile = "log4j.properties";
	public static String jbossXMLFile = "log4j.xml";
	public static boolean defaultDisable = false;
	
	public ECSRepositorySelector() {
	}

	public static synchronized void init() {
		if (!initialized) // set the global RepositorySelector
		{
			oldRepository = LogManager.getLoggerRepository();
			defaultRepository = new ECSLoggerRepository(new ECSLogger("root"));
			
			try {
				DOMConfigurator conf = new DOMConfigurator();
				conf.doConfigure(jbossXMLFile, defaultRepository);
				
				DOMConfigurator conf2 = new DOMConfigurator();
				conf2.doConfigure(jbossXMLFile, oldRepository);
				
				//PropertyConfigurator pconf = new PropertyConfigurator();
				//pconf.doConfigure(jbossPropertiesFile, defaultRepository);
			} catch (Exception e) {
				e.printStackTrace();
			}

			ECSLoggerRepository edefaultRepo = (ECSLoggerRepository) defaultRepository;
			edefaultRepo.setEcs(new ECS(serverName));
			edefaultRepo.getEcs().start();
			
			
			regularRepository = new RegularLoggerRepository(oldRepository
					.getLogger("root"));

			try {
				DOMConfigurator conf = new DOMConfigurator();
				conf.doConfigure(jbossXMLFile, regularRepository);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			RepositorySelector theSelector = new ECSRepositorySelector();
			
			LogManager.setRepositorySelector(theSelector, guard);
			initialized = true;
		}
	}
	
	public static synchronized void initWithECS(String serverName, String propertiesFile){
		init();

		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		ECSLoggerRepository ecsRepo = new ECSLoggerRepository(new ECSLogger("root"));
		LoggerRepository normalRepo = new Hierarchy(new RootCategory(Level.INFO)); 

		/*
		 * This section does init current war with the jboss log4j config file
		 * but reinits curent log files.
		 * 
		 * No need, log4j repo will default with the log4j config file
		 * 
		 */ 
		if (propertiesFile == null)
			propertiesFile = jbossXMLFile;
		
		if (propertiesFile != null){ //jbossXMLFile can be null
			File f = new File(propertiesFile);
			if (f.exists()){
				try {
					//PropertyConfigurator pconf = new PropertyConfigurator();
					//pconf.doConfigure(propertiesFile, normalRepo);
					
					DOMConfigurator conf = new DOMConfigurator();
					conf.doConfigure(propertiesFile, normalRepo);
				} catch (Exception e) {
					defaultRepository.getLogger(ECSRepositorySelector.class.getCanonicalName()).error("Error while applying properties file", e);
				}
			}
			else{
				defaultRepository.getLogger(ECSRepositorySelector.class.getCanonicalName()).info("Can't find properties file:"+ propertiesFile);
			}
		}

		ecsRepo.setEcs(new ECS(serverName));
		ecsRepo.getEcs().start();
		repositories.put(loader, ecsRepo);
		repositories2.put(loader, normalRepo);
	}
	
	public static synchronized void destroy(){
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		LoggerRepository repo = repositories.get(loader);
		if (repo instanceof ECSLoggerRepository){
			ECSLoggerRepository ecsRepo = (ECSLoggerRepository) repo;
			ecsRepo.getEcs().stop();
		}
		
		repositories.remove(loader);
		repositories2.remove(loader);
	}

	public static synchronized void initWithoutECS(String serverName, String propertiesFile){
		init();
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		repositories.put(loader, regularRepository);
	}

	private static synchronized void removeFromRepository() {
		repositories.remove(Thread.currentThread().getContextClassLoader());
		repositories2.remove(Thread.currentThread().getContextClassLoader());
	}

	public LoggerRepository getLoggerRepository() {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		LoggerRepository repository = (LoggerRepository) repositories
				.get(loader);
		if (repository == null) {
			if (defaultDisable){
				return regularRepository;
			}
			else
				return defaultRepository;
		} else {
			return repository;
		}
	}

	public static LoggerRepository getOldRepository() {
		return oldRepository;
	}
	
	public static LoggerRepository getNormalRepository(){
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		LoggerRepository repo = repositories2.get(loader);
		if (repo == null)
			repo = getOldRepository();
		return repo;
	}


}