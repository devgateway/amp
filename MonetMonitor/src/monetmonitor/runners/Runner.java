package monetmonitor.runners;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import monetmonitor.Utils;

//import monetmonitor.MonetServerStarter.MonetThread;

/**
 * 
 * @author acartaleanu
 *
 * Class for running commands (start server, stop server, recreate database)
 */
public abstract class Runner {
	Process proc = null;
	MonetThread thread = null;	
//	Semaphore sem = null;
	
//	public Runner(Semaphore sem) {
//		this.sem = sem;
//	}
	
//	protected String command;
	
	protected abstract String getCommand();
	protected abstract String getCustomMessage();
	
	class MonetThread extends Thread {
		private volatile boolean running = true;

		public void terminate() {
			running = false;
		}
		public void run() {
			try {
				System.out.println(getCommand());
//				proc = Runtime.getRuntime().exec(Constants.getMonetExecPath() + " " + Constants.getMonetFarmPath());
				proc = Runtime.getRuntime().exec(getCommand());
				InputStreamReader streamReader = new InputStreamReader(proc.getInputStream());
			    BufferedReader input = new BufferedReader(streamReader);
			    String line;
			    while ((line = input.readLine()) != null && running) {
			    	Utils.toLog("MonetDB response to command " + getCommand() + ":>>> " + line);
			    }
			    input.close();
			} catch( Exception io) {
				
			} 
		}
	}
	
	public abstract boolean allowedToRun();
	
	/**
	 * Attempts to start the MonetDB server in a child process
	 * @return true on success, false on error
	 */
	public  boolean run() {
		
//		if (new MonetBeholderConnectivity().check().equals(BeholderObservationResult.SUCCESS))
//			return false;
		if (!allowedToRun()) 
			return false;
		Utils.broadcastStatus(getCustomMessage());

		if (thread == null){
			thread = new MonetThread();
			thread.start();			
		}
		else {
			if (proc != null) {
				proc.destroy();
				proc = null;
				thread.terminate();
			}
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			thread = new MonetThread();
			thread.start();
		}
		return true;
	}

}
