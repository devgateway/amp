package monetmonitor;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Class with blocking methods, running commands in a sequence
 * @author llenterak
 *
 */
public class SequentialRunner {
	String[] commands = null;
	public SequentialRunner(String[] commands) {
		this.commands = commands;
	}
	
	public void run() {
		for (String command : commands)
			runSingleCommand(command);
	}
	
	private void runSingleCommand(String command) {
		try {
			System.out.println(command);
//			proc = Runtime.getRuntime().exec(Constants.getMonetExecPath() + " " + Constants.getMonetFarmPath());
			Process proc = Runtime.getRuntime().exec(command);
			InputStreamReader streamReader = new InputStreamReader(proc.getInputStream());
		    BufferedReader input = new BufferedReader(streamReader);
		    String line;
		    while ((line = input.readLine()) != null) {
		    	Utils.toLog("MonetDB response to command " + command + ":>>> " + line);
		    }
		    input.close();
		} catch( Exception io) {
			
		} 

	}
}
