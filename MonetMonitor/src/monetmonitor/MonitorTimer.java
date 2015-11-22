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
	  
	  int iteration;
	  
	  
	 
	  
	  
	  BeholderObservationResult prevStatus = BeholderObservationResult.ERROR_UNKNOWN;
	private HealthChecker healthChecker;
	  /**
	   * 
	   * @param beh reference to the MonetBeholder class (it checks if MonetMonitor is responding)
	   * @param st reference to the MonetStarter class (starts if MonetBeholder doesn't get an answer from the server)
	   */
	  void startTimer(MonetBeholder beh,  MonetServerStarter st, HealthChecker hc){
		  this.beholder = beh;
		  this.starter = st;
		  this.healthChecker = hc;
		    Runnable checkServerTask = new Runnable () {
				@Override
				public void run() {
					iteration++;
			    	BeholderObservationResult serverStatus = beholder.check();
//			    	BeholderObservationResult healthStatus = healthChecker.runHealthCheck();
			    	String statusMessage = null;
			    	switch(serverStatus) {
			    	case SUCCESS: 						statusMessage = "Server running"; break;
			    	case ERROR_CANNOT_CONNECT: 			statusMessage = "Server not running"; break;
			    	case ERROR_INTERNAL_MONETDB:		statusMessage = "Database corrupt"; break;
			    	case ERROR_UNKNOWN: 				statusMessage = "Unknown error"; break;
			    	case ERROR_DATABASE_MAINTENANCE: 	statusMessage = "Database under maintenance"; break;
			    	case ERROR_NO_DATABASE: 			statusMessage = "Database missing"; break;
			    	case ERROR_HEALTH_BELOW_THRESHOLD:  statusMessage = "Database health below threshold"; break;
			    	}

			    	/*
			    	 * [1]Part broadcasting server status message <-------------------------
			    	 */
//			    	if (iteration == Constants.getBroadcastFrequency())
//			    		iteration = 0;
			    	if (iteration % Constants.getMemCheckFrequency() == 0) {
			    		new MemoryChecker().getFreeMemory();
			    	}
		    		if (prevStatus != serverStatus || (iteration % Constants.getBroadcastFrequency() == 0) || Constants.debugLogMode())
		    			Utils.broadcastStatus(statusMessage);
		    		prevStatus = serverStatus;
			    	/*
			    	 * ------------------------>[1] End of part broadcasting server status 
			    	 */
			    	
			    	/*
			    	 * [2]Part attempting to correct server's status <-------------------------
			    	 */
		    		
		    		//recreate the database before we take a chance at starting it
		    		
//			    	if (healthStatus.equals(BeholderObservationResult.ERROR_HEALTH_BELOW_THRESHOLD)){
//			    		Utils.broadcastStatus("Recreating database...");
//			    		new SequentialRunner(CommandGenerator.generateRecreateDatabaseCommands()).run();
//			    		starter.run();
//			    	} else {
				    	switch(serverStatus) {
				    	case SUCCESS: //do nothing, server is online, it's fine
				    		break;
				    	case ERROR_CANNOT_CONNECT: 
				    		//attempt to start the server
			    			Utils.broadcastStatus("Starting server...");
				    		starter.run();
				    		break; 
				    	case ERROR_NO_DATABASE:
				    		Utils.broadcastStatus("Creating database...");
				    		new SequentialRunner(CommandGenerator.generateCreateDatabaseCommands()).run();
				    		starter.run();
				    		break;
				    	case ERROR_INTERNAL_MONETDB: 
				    		Utils.broadcastStatus("Recreating database...");
				    		new SequentialRunner(CommandGenerator.generateRecreateDatabaseCommands()).run();
				    		starter.run();
				    		break;
				    	case ERROR_DATABASE_MAINTENANCE:
				    		Utils.broadcastStatus("Releasing database...");
				    		new SequentialRunner(CommandGenerator.generateReleaseDatabaseCommands()).run();;
				    		starter.run();
				    		break;
				    	case ERROR_UNKNOWN: //this shouldn't be happening
				    						//broadcast unknown error and break the cycle
				    		Utils.broadcastStatus("Recreating database...");
				    		new SequentialRunner(CommandGenerator.generateRecreateDatabaseCommands()).run();
				    		starter.run();
				    		break;
						case ERROR_HEALTH_BELOW_THRESHOLD:
							//it's not possible to get this from the beholder
							break;
						default:
							//whatever
							break;
				    	}
//			    	}
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
