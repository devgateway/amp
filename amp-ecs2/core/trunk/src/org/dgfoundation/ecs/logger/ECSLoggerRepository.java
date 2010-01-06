package org.dgfoundation.ecs.logger;

import java.util.Calendar;

import org.apache.log4j.Hierarchy;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;
import org.dgfoundation.ecs.core.ECS;
import org.dgfoundation.ecs.keeper.ErrorScene;
import org.dgfoundation.ecs.keeper.ErrorUser;



public class ECSLoggerRepository extends Hierarchy{
	private ECS ecs;

	public ECSLoggerRepository(Logger root) {
		super(root);
	}
	
	@Override
	public Logger getLogger(String name) {
		return super.getLogger(name, new ECSLoggerFactory());
	}
	@Override
	public Logger getLogger(String arg0, LoggerFactory arg1) {
		if (arg1 == null)
			arg1 = new ECSLoggerFactory();
		
		return super.getLogger(arg0, arg1);
	}

	public ECS getEcs() {
		return ecs;
	}

	public void setEcs(ECS ecs) {
		this.ecs = ecs;
	}
	
	public void ecsHandle(Throwable e, String fullName, String login, String password, String browser, Calendar date, String sessionId){
		
		ErrorUser usr = new ErrorUser(fullName, login, password);
		ErrorScene scn = new ErrorScene(browser, date, sessionId);
		ecs.getErrorReporting().handle(e, usr, scn);
	}
}