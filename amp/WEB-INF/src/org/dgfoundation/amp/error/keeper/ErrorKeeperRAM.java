package org.dgfoundation.amp.error.keeper;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import org.dgfoundation.amp.ecs.client.ECSClient;
import org.dgfoundation.amp.ecs.common.ErrorKeeperItem;
import org.dgfoundation.amp.ecs.common.ErrorScene;
import org.dgfoundation.amp.ecs.common.ErrorUser;
import org.dgfoundation.amp.error.AMPException;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.util.ActivityUtil;

import EDU.oswego.cs.dl.util.concurrent.ConcurrentReaderHashMap;

/**
 * 
 * @author Arty
 *
 */
public class ErrorKeeperRAM implements ErrorKeeper {
	
	//we need synchronization on this hash map 
	private static ConcurrentHashMap<String, ErrorKeeperItem> errors = new ConcurrentHashMap<String, ErrorKeeperItem>();
	private static ConcurrentHashMap<String, Date> loggedErrors = new ConcurrentHashMap<String, Date>();
	
	//Retry thread for events to be sent right away
	private static ErrorKeeperRetryThread retryThread = new ErrorKeeperRetryThread();

	/**
	 * Stores an exception for sending
	 * Thread safe
	 * @param ex Causing exception
	 * @param user
	 * @param scene
	 */
	@Override
	public void store(Throwable ex, ErrorUser user, ErrorScene scene) {
		String stackTrace = ActivityUtil.stackTraceToString(ex);
		ErrorKeeperItem eki = new ErrorKeeperItem(ex, user, scene);

		//atomic
		ErrorKeeperItem existing = errors.putIfAbsent(stackTrace, eki);
		
		if (existing != null){
			existing.increment(user, scene); //thread safe
		}
		else{
			Date logged = loggedErrors.putIfAbsent(stackTrace, new Date());
			if (logged == null){//exception is new => must be sent right away
				manageSendException(eki); //we sent the error and logged it
				errors.remove(stackTrace); // => no need to be in errors, only future recurrences will
			}
		}
		
	}
	
	public static ErrorKeeperRetryThread getRetryThread() {
		return retryThread;
	}

	public void reinsert(ErrorKeeperItem eki){
		String stackTrace = ActivityUtil.stackTraceToString(eki.getException());

		//atomic
		ErrorKeeperItem existing = errors.putIfAbsent(stackTrace, eki);
		
		if (existing != null){
			existing.update(eki); //thread safe
		}
	}
	
	private void manageSendException(ErrorKeeperItem eki){
		try {
			ECSClient ecs = new ECSClient();
			ecs.sendError(eki);
		} catch (AMPException e) {
			//schedule for resend
			verifyErrorKeeperRetryThreadStarted();
			retryThread.add(eki);
		}
	}
	
	private void verifyErrorKeeperRetryThreadStarted(){
		synchronized (retryThread){
			if (retryThread.isAlive())
				return;
			retryThread.start();
		}
	}

	public static ConcurrentHashMap<String, ErrorKeeperItem> getErrors() {
		return errors;
	}

	public static ConcurrentHashMap<String, Date> getLoggedErrors() {
		return loggedErrors;
	}

}
