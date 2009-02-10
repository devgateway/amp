package org.dgfoundation.amp.error;

import org.apache.log4j.Logger;

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
		logger.error(e.getMessage(), e);
	}
	
}

