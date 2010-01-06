package org.dgfoundation.ecs.logger;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.apache.log4j.spi.LoggerRepository;
import org.dgfoundation.ecs.core.ECS;
import org.dgfoundation.ecs.exceptions.ECSIgnoreException;

public class ECSLogger extends Logger {
	private String FQCN = ECSLogger.class.getName()+".";
	private static ECSLoggerFactory factory = new ECSLoggerFactory();
	private Logger log;
	private ECS ecs = null;
	public ECSLogger(String name) {
	    super(name);
	    super.additive=false;
	    super.level = Level.INFO;
	    log = ECSRepositorySelector.getOldRepository().getLogger(name);
	}
	
	public static Logger getLogger(String name) {
	    return Logger.getLogger(name, factory);
	}
	
	public void handle(Throwable t){
		String clname = null;
		
		if (t != null )
			clname = t.getClass().getCanonicalName();
		
		if ((t != null) && ((clname == null) || (!clname.toUpperCase().contains("ECSIGNORE")))){
			if (ecs == null){
				LoggerRepository logRepo = LogManager.getLoggerRepository();
				if (logRepo instanceof ECSLoggerRepository){
					ECSLoggerRepository ecsLogRepo = (ECSLoggerRepository) logRepo;
					ecs = ecsLogRepo.getEcs();
				}
			}	
			ecs.getErrorReporting().handle(t);
		}
	}
	
	@Override
	public void info(Object message) {
		log.info("ECS>>ii>>" + message);
	}
	
	@Override
	public void info(Object message, Throwable t) {
		//handle(t);
		log.info("ECS>>ii>>" + message, t);
	}
	
	@Override
	public void error(Object message, Throwable t) {
		handle(t);
		log.error("ECS>>ee>>" + message, t);
		//log.error("ignore", new ECSIgnoreException(new Exception("this should be IGNORED!")));
	}
	
	@Override
	public void error(Object message) {
		log.error("ECS>>ee>>" + message);
	}
	
	@Override
	public void fatal(Object message) {
		log.fatal("ECS>>ff>>" + message);
	}
	
	@Override
	public void fatal(Object message, Throwable t) {
		handle(t);
		log.fatal("ECS>>ff>>" + message, t);
	}
	
	@Override
	public void warn(Object message) {
		log.warn("ECS>>ww>>" + message);
	}
	
	@Override
	public void warn(Object message, Throwable t) {
		//handle(t);
		log.warn("ECS>>ww>>" + message, t);
	}
	
	@Override
	public void log(Priority priority, Object message) {
		log.log(priority, "ECS>>LL>>" +  message);
	}
	
	@Override
	public void log(Priority priority, Object message, Throwable t) {
		if ((priority.toInt() == Priority.ERROR_INT)||(priority.toInt() == Priority.FATAL_INT))
			handle(t);
		log.log(priority, "ECS>>LL>>" +  message, t);
	}
	
	@Override
	public void log(String callerFQCN, Priority level, Object message,
			Throwable t) {
		if ((level.toInt() == Priority.ERROR_INT)||(level.toInt() == Priority.FATAL_INT))
			handle(t);
		log.log(callerFQCN, level, "ECS>>LL>>" +  message, t);
	}
	
}
