package org.dgfoundation.amp.error.keeper;

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ecs.ECSClient;
import org.dgfoundation.amp.ecs.common.ErrorScene;
import org.dgfoundation.amp.ecs.common.ErrorUser;
import org.dgfoundation.amp.error.AMPException;
import org.digijava.module.aim.util.ActivityUtil;

/**
 * 
 * @author Arty
 *
 */
public class ErrorKeeperRAM implements ErrorKeeper {
    private static Logger logger = Logger.getLogger(ErrorKeeperRAM.class);

	
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

		ErrorKeeperItem eki = new ErrorKeeperItem(stackTrace, user, scene);

		//atomic
		ErrorKeeperItem existing = errors.putIfAbsent(stackTrace, eki);
		
		if (existing != null){
			logger.info("Old exception, incrementing counters!");
			synchronized (existing) {
				existing.setCount(existing.getCount() + 1); //increment

				boolean found = false;
				for (int i = 0; i < existing.getUsers().size(); i++){
					ErrorUser errorUser = existing.getUsers().get(i);
					ErrorScene[] errorScenes = existing.getUserScenes().get(i);
					
					if (user.getLogin().compareTo(errorUser.getLogin()) == 0){
						found = true;
						LinkedList<ErrorScene> tmp = new LinkedList<ErrorScene>(Arrays.asList(errorScenes));
						tmp.add(scene);
						existing.getUserScenes().set(i, (ErrorScene[])tmp.toArray());
						break;
					}
				}
				
				if (!found){
					ErrorScene[] sList = new ErrorScene[1];
					sList[0] = scene;
					existing.getUsers().add(user);
					existing.getUserScenes().add(sList);
				}
			}
		}
		else{
			Date logged = loggedErrors.putIfAbsent(stackTrace, new Date());
			logger.info("New exception ?");
			if (logged == null){//exception is new => must be sent right away
				logger.info("    YES - Sending to server ...");
				manageSendException(eki); //we sent the error and logged it
				errors.remove(stackTrace); // => no need to be in errors, only future recurrences will
			}
			else
				logger.info("    NO");
		}
		
	}
	
	public static ErrorKeeperRetryThread getRetryThread() {
		return retryThread;
	}

	public void reinsert(ErrorKeeperItem eki){
		String stackTrace = eki.getStackTrace();
		logger.info("Reinserting EKI");

		//atomic
		ErrorKeeperItem existing = errors.putIfAbsent(stackTrace, eki);
		
		if (existing != null){
			synchronized (existing) {
				existing.setCount(existing.getCount() + eki.getCount());
				
				for (int i = 0; i < eki.getUsers().size(); i++){
					ErrorUser errorUser = eki.getUsers().get(i);
					ErrorScene[] errorScenes = eki.getUserScenes().get(i);
					
					boolean found = false;
					for (int j = 0; j < existing.getUsers().size(); j++){
						ErrorUser existingUser = existing.getUsers().get(j);
						if (existingUser.getLogin().compareTo(errorUser.getLogin())==0){
							found = true;
							ErrorScene[] existingScenes = existing.getUserScenes().get(j);
							LinkedList<ErrorScene> tmpSc = new LinkedList<ErrorScene>(Arrays.asList(existingScenes));
							tmpSc.addAll(new LinkedList(Arrays.asList(errorScenes)));
							existing.getUserScenes().set(j, (ErrorScene[]) tmpSc.toArray());
							break;
						}
					}
					if (!found){
						existing.getUsers().add(errorUser);
						existing.getUserScenes().add(errorScenes);
					}
				}
			}
		}
	}
	
	private void manageSendException(ErrorKeeperItem eki){
		try {
			logger.info("Sending EKI to Server ...");
			ECSClient ecs = new ECSClient();
			ecs.sendError(eki);
			logger.info("    SUCCESS");
		} catch (AMPException e) {
			//schedule for resend
			logger.info("    FAIL");
			logger.error(e);
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
