package org.dgfoundation.ecs.logger;

import org.apache.log4j.Hierarchy;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;


public class RegularLoggerRepository extends Hierarchy{

	public RegularLoggerRepository(Logger root) {
		super(root);
	}
	
	@Override
	public Logger getLogger(String name) {
		return ECSRepositorySelector.getOldRepository().getLogger(name);
	}
	@Override
	public Logger getLogger(String arg0, LoggerFactory arg1) {
		return ECSRepositorySelector.getOldRepository().getLogger(arg0, arg1);
	}
}