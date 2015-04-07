package monetmonitor;

import java.util.ArrayList;
import java.util.List;

public class Utils {
	
	public static List<StatusShower> statusShowers = new ArrayList<StatusShower>();
	public static StatusShower logfile;

	
	public static void broadcastStatus(String statusMessage) throws Exception {
		for (StatusShower st : statusShowers) {
			st.showStatus(statusMessage);
		}
	}
	
	public static void toLog(String status) throws Exception{
		logfile.showStatus(status);
	}
}
