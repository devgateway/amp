package monetmonitor;


import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
	  
	  MonetStarter starter;
	  
	  boolean prevStatus = false;
	  
	  void startTimer(MonetBeholder beh,  MonetStarter st){
		  this.beholder = beh;
//		  this.shower = sh;
		  this.starter = st;
		    Runnable checkServerTask = new Runnable () {
				@Override
				public void run() {
			    	boolean serverStatus = beholder.checkMonetServerRunning();
			    	String statusMessage = serverStatus ? "Server running" : "Server not running";
			    	try{
			    		if (prevStatus != serverStatus)
			    			Utils.broadcastStatus(statusMessage);
			    		prevStatus = serverStatus;
//			    	shower.showStatus(statusMessage);
			    	} catch (Exception exc) {
			    		System.err.println("Error in status shower:" + exc.getMessage());
			    	}
			    	if (!serverStatus) {
			    		try {
			    			Utils.broadcastStatus("Starting server...");
						} catch (Exception e) {
				    		System.err.println("Error in status shower:" + e.getMessage());
						}
			    		starter.startServer();
			    	}
				}
		    };
		    scheduler.scheduleWithFixedDelay(checkServerTask, initialDelay, delay, TimeUnit.SECONDS);
		  }
}
