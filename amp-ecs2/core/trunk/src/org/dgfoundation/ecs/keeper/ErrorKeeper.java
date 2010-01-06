package org.dgfoundation.ecs.keeper;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import org.dgfoundation.ecs.keeper.ErrorScene;
import org.dgfoundation.ecs.keeper.ErrorUser;

public interface ErrorKeeper {
	public void store(Throwable e, ErrorUser user, ErrorScene scene);
	public void reinsert(ErrorKeeperItem eki);
	public ConcurrentHashMap<String, ErrorKeeperItem> getErrors();
	public ConcurrentHashMap<String, Date> getLoggedErrors();
	
	
}
