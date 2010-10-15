package org.dgfoundation.ecs;

import org.apache.log4j.LogManager;

public class ECSLogManager extends LogManager{
	public ECSLogManager() {
		InternalLoggerPlugin elp = new InternalLoggerPlugin();
		elp.init("ChangeThis");
	}
}
