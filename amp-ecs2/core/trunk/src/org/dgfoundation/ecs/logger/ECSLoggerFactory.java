package org.dgfoundation.ecs.logger;

import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;

public class ECSLoggerFactory implements LoggerFactory {

	@Override
	public Logger makeNewLoggerInstance(String name) {
		Logger l = new ECSLogger(name);
	    return l;
	}

}
