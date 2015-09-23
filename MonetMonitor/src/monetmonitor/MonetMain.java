package monetmonitor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import monetmonitor.runners.MonetBeholderSanity;
import monetmonitor.runners.MonetServerStarter;
import monetmonitor.runners.Semaphore;


public class MonetMain {
    

/**
 * @author acartaleanu
 * parses settings from settings.conf
 * settings are stored in a <name> <value> structure 
 * if parameter is not found, will write an error to stdout, but will still continue with defaults
 * 
 * */
	private static void parseSettings() throws Exception{
		File file = new File("settings.conf");
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;
		Map<String, String> parsedValues = new HashMap<String, String>();
		while ((line = br.readLine()) != null) {
			//comments are lines that start with a #
			if (line.replace(" ", "").indexOf("#") == 0)
				continue;
			
			String name, value;
			int index = line.indexOf(" ");
			if (index == -1) {
				value = "";
				index = line.length();
			}
			else {
				value = line.substring(index + 1);	
			}
			name = line.substring(0, index);
			parsedValues.put(name, value);
		}
		br.close();
		for (Map.Entry<String, String> entry : parsedValues.entrySet()) {
			Constants.parametersMap.put(entry.getKey(), entry.getValue());
			System.out.println("Adding " + entry.getKey() + " with value " + entry.getValue());
		}
	}
	
	private static void initStatusShowers(String[] args) {
		boolean nogui = Constants.getNoGui();
		Utils.statusShowers.add(nogui ? new StatusShowerCLI() : new StatusShowerGUI());
		Utils.logfile = new StatusShowerLog();
		Utils.statusShowers.add(Utils.logfile);
		for (StatusShower sh : Utils.statusShowers )
			sh.start();
	}
	
	public static void main(String[] args) throws Exception {
		parseSettings();
		initStatusShowers(args);
    	int timer_delay = Constants.getTimerDelay();
    	Utils.broadcastStatus("Starting beholder with timer delay = "+ timer_delay);
    	MonitorTimer timer = new MonitorTimer(1, timer_delay);
    	timer.startTimer(new MonetBeholderSanity(), new MonetServerStarter());
    }
}