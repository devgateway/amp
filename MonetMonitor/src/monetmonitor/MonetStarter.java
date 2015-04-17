package monetmonitor;

import java.io.BufferedReader;

import java.io.InputStreamReader;

/**
 * @author acartaleanu
 * starts the server, based on Constants, eventually imported from settings.conf
 * runs the server in a separate thread for listening to output
 * doesn't start it if server already running 
 * */

public class MonetStarter {


	Process proc = null;
	MonetThread thread = null;	
	
	class MonetThread extends Thread {
		private volatile boolean running = true;

		public void terminate() {
			running = false;
		}
		public void run() {
			try {
				System.out.println(Constants.getMonetExecPath() + " " + Constants.getMonetFarmPath());
				proc = Runtime.getRuntime().exec(Constants.getMonetExecPath() + " " + Constants.getMonetFarmPath());
				InputStreamReader streamReader = new InputStreamReader(proc.getInputStream());
			    BufferedReader input = new BufferedReader(streamReader);
			    String line;
			    while ((line = input.readLine()) != null && running) {
			    	Utils.toLog(line);
			    }
			    input.close();
			} catch( Exception io) {
				
			} 
		}
	}
	
	
	
	public  boolean startServer() {
		
		if (new MonetBeholder().checkMonetServerRunning())
			return false;
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
