package org.dgfoundation.ecs.logger;

import org.apache.log4j.spi.RepositorySelector;
import org.apache.log4j.spi.LoggerRepository;
import org.apache.log4j.LogManager;
import org.dgfoundation.ecs.core.ECS;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ECSRepositorySelector implements RepositorySelector {
	private static boolean initialized = false;
	private static Object guard = LogManager.getRootLogger();
	private static Map<ClassLoader, LoggerRepository> repositories = new HashMap<ClassLoader, LoggerRepository>();
	private static LoggerRepository defaultRepository;
	private static LoggerRepository oldRepository;
	private static RegularLoggerRepository regularRepository;

	public ECSRepositorySelector() {
	}

	public static synchronized void init() {
		if (!initialized) // set the global RepositorySelector
		{
			oldRepository = LogManager.getLoggerRepository();
			defaultRepository = new ECSLoggerRepository(new ECSLogger("root"));
			ECSLoggerRepository edefaultRepo = (ECSLoggerRepository) defaultRepository;
			edefaultRepo.setEcs(new ECS("root"));
			edefaultRepo.getEcs().start();
			
			
			regularRepository = new RegularLoggerRepository(oldRepository
					.getLogger("root"));

			RepositorySelector theSelector = new ECSRepositorySelector();
			LogManager.setRepositorySelector(theSelector, guard);
			initialized = true;
		}
	}
	
	public static synchronized void initWithECS(String serverName){
		init();

		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		ECSLoggerRepository ecsRepo = new ECSLoggerRepository(new ECSLogger("root"));
		ecsRepo.setEcs(new ECS(serverName));
		ecsRepo.getEcs().start();
		repositories.put(loader, ecsRepo);
		
	}

	public static synchronized void initWithoutECS(String serverName){
		init();
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		repositories.put(loader, regularRepository);
	}

	public static synchronized void init(Boolean ecsDisable, String serverName){
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
				
				Method method = repoInstance.getClass().getMethod(methName, String.class);
				method.invoke(method, serverName);
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

	/*
	 * // load log4j.xml from WEB-INF private static void
	 * loadLog4JConfig(ServletContext servletContext, Hierarchy hierarchy)
	 * throws ServletException { try { String log4jFile = "/WEB-INF/log4j.xml";
	 * InputStream log4JConfig = servletContext.getResourceAsStream(log4jFile);
	 * if (log4JConfig != null){ Document doc =
	 * DocumentBuilderFactory.newInstance
	 * ().newDocumentBuilder().parse(log4JConfig); DOMConfigurator conf = new
	 * DOMConfigurator(); conf.doConfigure(doc.getDocumentElement(), hierarchy);
	 * } } catch (Exception e) { throw new ServletException(e); } }
	 */

}