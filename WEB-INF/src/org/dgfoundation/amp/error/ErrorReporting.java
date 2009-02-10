package org.dgfoundation.amp.error;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;

public class ErrorReporting {
	
	/** 
	 * I think this is not a good idea
	 * the logger should be the one instantiated 
	 * in the class where the error occurred
	 *  
	 * @param e
	 * @deprecated
	 */
	private static void handle(Exception e){
	}

	public static void handle(Exception e, Logger logger){
		handle(e, logger, null);
	}

	public static void handle(Exception e, Logger logger, HttpServletRequest request){
		logger.error(e.getMessage(), e);
		User user = new User("unknown@amp.org", "Unknown", "Unknown");
		if (request != null){
			try{
				User us = RequestUtils.getUser(request);
				if (us != null)
					user = us;
			} catch (Exception shouldBeIgnored) {
				logger.error("Can't get user", shouldBeIgnored);
			}
		}
		
		sendToKeeper(e, user);
	}

	private static void sendToKeeper(Exception e, User user){
		ErrorKeeper ek = new ErrorKeeperRAM();
		ek.store(e, user);
	}
	
}

