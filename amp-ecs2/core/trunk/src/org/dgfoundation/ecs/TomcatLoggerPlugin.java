package org.dgfoundation.ecs;

import org.jboss.logging.LoggerPlugin;

public class TomcatLoggerPlugin extends InternalLoggerPlugin
{
	public TomcatLoggerPlugin() {
		setTomcatFriendly(true);
	}
}
