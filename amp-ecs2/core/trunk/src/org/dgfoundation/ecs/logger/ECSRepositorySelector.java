package org.dgfoundation.ecs.logger;

import org.apache.log4j.spi.RepositorySelector;
import org.apache.log4j.spi.LoggerRepository;
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
	private static LoggerRepository oldRepository;
	private static RegularLoggerRepository regularRepository;
	public static String serverName = "root";
	public static String jbossPropertiesFile = "log4j.properties";

	public ECSRepositorySelector() {
	}

	public static synchronized void init() {
		if (!initialized) // set the global RepositorySelector
		{
			oldRepository = LogManager.getLoggerRepository();
			defaultRepository = new ECSLoggerRepository(new ECSLogger("root"));
			
			try {
				PropertyConfigurator pconf = new PropertyConfigurator();
				pconf.doConfigure(jbossPropertiesFile, defaultRepository);
			} catch (Exception e) {
				e.printStackTrace();
			}

			ECSLoggerRepository edefaultRepo = (ECSLoggerRepository) defaultRepository;
			edefaultRepo.setEcs(new ECS(serverName));
			edefaultRepo.getEcs().start();
			
			
			regularRepository = new RegularLoggerRepository(oldRepository
					.getLogger("root"));

			RepositorySelector theSelector = new ECSRepositorySelector();
			LogManager.setRepositorySelector(theSelector, guard);
			initialized = true;
		}
	}
	
	public static synchronized void initWithECS(String serverName, String propertiesFile){
		init();

		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		ECSLoggerRepository ecsRepo = new ECSLoggerRepository(new ECSLogger("root"));
		LoggerRepository normalRepo = new Hierarchy(new RootCategory(Level.DEBUG)); 
		if (propertiesFile == null)
			propertiesFile = jbossPropertiesFile;
		
		if (propertiesFile != null){ //jbossPropertiesFile can be null
			File f = new File(propertiesFile);
			if (f.exists()){
				try {
					PropertyConfigurator pconf = new PropertyConfigurator();
					pconf.doConfigure(propertiesFile, normalRepo);
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

	public static synchronized void initWithoutECS(String serverName, String propertiesFile){
		init();
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		repositories.put(loader, regularRepository);
	}

	public static synchronized void init(Boolean ecsDisable, String serverName, String propertiesFile){
		LoggerRepository current = LogManager.getLoggerRepository();
		if ("org.dgfoundation.ecs.logger.ECSLoggerRepository".compareTo(current.getClass().getCanonicalName())==0){//already changed
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

	private static synchronized void removeFromRepository() {
		repositories.remove(Thread.currentThread().getContextClassLoader());
	}

	public static synchronized void destroy() {
		removeFromRepository();
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		// TODO: disable threads etc
		// WARN: implement destroy as init(boolean);
		
	}

	public LoggerRepository getLoggerRepository() {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		LoggerRepository repository = (LoggerRepository) repositories
				.get(loader);
		if (repository == null) {
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