package org.dgfoundation.ecs.keeper;

import java.util.Calendar;

import org.apache.log4j.Logger;
import org.dgfoundation.ecs.keeper.ErrorScene;
import org.dgfoundation.ecs.keeper.ErrorUser;

public class ErrorReporting {
    private static Logger logger = Logger.getLogger(ErrorReporting.class);
	private ErrorKeeper ek;

	public ErrorReporting(ErrorKeeper keeper) {
		this.ek = keeper;
	}
	
	public void handle(Throwable e){
		
		ErrorUser user = new ErrorUser();
		user.setLogin("unknown");
		user.setFullName("Unknown");

		ErrorScene eScene = new ErrorScene(); // error "surroundings"
		eScene.setDate(Calendar.getInstance());
		
		handle(e, user, eScene);
	}

	public void handle(Throwable e, ErrorUser user, ErrorScene scene){
		ek.store(e, user, scene);
	}
	
}

