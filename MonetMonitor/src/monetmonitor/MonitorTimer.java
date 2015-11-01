package monetmonitor;


import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


import monetmonitor.runners.MonetServerStarter;


/**
 * @author acartaleanu
 * scheduler class for repeatedly invoking MonetBeholder to check server's status
 * if server is down, calls MonetStarter*/
public class MonitorTimer {
	private final ScheduledExecutorService scheduler;
	private final long initialDelay;
	private final long delay;
	
	private static final int NUM_THREADS = 1;
		
	
	  MonitorTimer(long initialDelay, long delay){
		  this.initialDelay = initialDelay;
		  this.delay = delay;
		  scheduler = Executors.newScheduledThreadPool(NUM_THREADS);    
	  }

	  MonetBeholder beholder;
	  
	  MonetServerStarter starter;
	  
	  
	 
	  
	  
	  BeholderObservationResult prevStatus = BeholderObservationResult.ERROR_UNKNOWN;
	  /**
	   * 
	   * @param beh reference to the MonetBeholder class (it checks if MonetMonitor is responding)
	   * @param st reference to the MonetStarter class (starts if MonetBeholder doesn't get an answer from the server)
	   */
	  void startTimer(MonetBeholder beh,  MonetServerStarter st){
		  this.beholder = beh;
		  this.starter = st;
		    Runnable checkServerTask = new Runnable () {
				@Override
				public void run() {
			    	BeholderObservationResult serverStatus = beholder.check();
			    	String statusMessage = null;
			    	switch(serverStatus) {
			    	case SUCCESS: 						statusMessage = "Server running"; break;
			    	case ERROR_CANNOT_CONNECT: 			statusMessage = "Server not running"; break;
			    	case ERROR_INTERNAL_MONETDB:		statusMessage = "Database corrupt"; break;
			    	case ERROR_UNKNOWN: 				statusMessage = "Unknown error"; break;
			    	case ERROR_DATABASE_MAINTENANCE: 	statusMessage = "Database under maintenance"; break;
			    	case ERROR_NO_DATABASE: 			statusMessage = "Database missing"; break;
			    	}

			    	/*
			    	 * [1]Part broadcasting server status message <-------------------------
			    	 */
			    	try{
			    		if (prevStatus != serverStatus)
			    			Utils.broadcastStatus(statusMessage);
			    		prevStatus = serverStatus;
			    	} catch (Exception exc) {
			    		System.err.println("Error in status shower:" + exc.getMessage());
			    	}
			    	/*
			    	 * ------------------------>[1] End of part broadcasting server status 
			    	 */
			    	
			    	/*
			    	 * [2]Part attempting to correct server's status <-------------------------
			    	 */
			    	switch(serverStatus) {
			    	case SUCCESS: //do nothing, server is online, it's fine
			    		break;
			    	case ERROR_CANNOT_CONNECT: 
			    		//attempt to start the server
//			    		try {
//		    			Utils.broadcastStatus("Starting server...");
//					} catch (Exception e) {
//			    		System.err.println("Error in status shower:" + e.getMessage());
//					}

			    		starter.run();
			    		break; 
			    	case ERROR_NO_DATABASE:
			    		new SequentialRunner(CommandGenerator.generateCreateDatabaseCommands()).run();
//			    		Semaphore sem = new Semaphore();
//			    		
//			    		new MonetDatabaseCreator(sem).run();
//			    		new MonetDatabaseReleaser(sem).run();
			    		break;
			    	case ERROR_INTERNAL_MONETDB:
			    		
			    		new SequentialRunner(CommandGenerator.generateRecreateDatabaseCommands()).run();
			    		//attempt to recreate the db 
//			    		try {
//		    			Utils.broadcastStatus("Recreating database...");
//					} catch (Exception e) {
//			    		System.err.println("Error in status shower:" + e.getMessage());
//					}
//			    		Semaphore sem2 = new Semaphore();
//			    		new MonetDatabaseStopper(sem2).run();
//			    		new MonetDatabaseDestroyer(sem2).run();
//			    		new MonetDatabaseCreator(sem2).run();
//			    		new MonetDatabaseReleaser(sem2).run();
//			    		
			    		starter.run();
			    		break;
			    	case ERROR_DATABASE_MAINTENANCE:
//			    		new MonetDatabaseReleaser().run();
			    		new SequentialRunner(CommandGenerator.generateReleaseDatabaseCommands()).run();;
			    		starter.run();
			    		break;
			    	case ERROR_UNKNOWN: //this shouldn't be happening
			    						//broadcast unknown error and break the cycle
			    		new SequentialRunner(CommandGenerator.generateRecreateDatabaseCommands()).run();
			    		starter.run();
			    		break;
			    	}
//			    	if (!serverStatus.equals(BeholderObservationResult.SUCCESS)) {
//			    		try {
//			    			Utils.broadcastStatus("Starting server...");
//						} catch (Exception e) {
//				    		System.err.println("Error in status shower:" + e.getMessage());
//						}
//			    		starter.run();
//			    	}
			    	/*
			    	 * ----------------> [2]end of part attempting to correct server's status
			    	 */
				}
		    };
		    scheduler.scheduleWithFixedDelay(checkServerTask, initialDelay, delay, TimeUnit.SECONDS);
		  }
}
