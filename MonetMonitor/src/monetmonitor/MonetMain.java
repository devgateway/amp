package monetmonitor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;


public class MonetMain {
    

/*
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
				value = line.substring(index);	
			}
			name = line.substring(0, index);
			parsedValues.put(name, value);
		}
		br.close();
		for (Map.Entry<String, String> entry : parsedValues.entrySet()) {
			Constants.parametersMap.put(entry.getKey(), entry.getValue());
//			Constants.class.getField(entry.getKey()).set(null, entry.getValue());
		}
	}
	
	private static void parseArgs(String[] args) {
		boolean nogui = Constants.getNoGui();
//		for (String arg: args) {
//			if (arg.equals("--nogui"))
//				nogui = true;
//		}
//		nogui = true;
		
		
		Utils.statusShowers.add(nogui ? new StatusShowerCLI() : new StatusShowerGUI());
		Utils.logfile = new StatusShowerLog();
		Utils.statusShowers.add(Utils.logfile);
		for (StatusShower sh : Utils.statusShowers )
			sh.start();
	}
	
	public static void main(String[] args) throws Exception {
		parseSettings();
		parseArgs(args);
    	int timer_delay = Constants.getTimerDelay();
        System.out.println("Starting beholder with timer delay=" + timer_delay);
    	MonitorTimer timer = new MonitorTimer(1, timer_delay);
    	timer.startTimer(new MonetBeholder(), new MonetStarter());
    }
}