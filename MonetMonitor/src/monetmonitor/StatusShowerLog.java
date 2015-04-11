package monetmonitor;



import java.io.FileWriter;
import java.text.DateFormat;
import java.util.Date;


/**
 * 
 * @author llenterak
 * StatusShower outputting to log file
 * path to logfile in settings.conf 
 */
public class StatusShowerLog implements StatusShower {
	
	
	
	public StatusShowerLog() {
	}
	
	public void showStatus(String status) throws Exception {
		FileWriter writer = new FileWriter(Constants.getLogfilePath(), true);
		Date now = new Date();
		writer.append(DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(now));
		writer.append(status);
		writer.append("\r\n");
		
		writer.close();		
	}

	@Override
	public void start() {
	}
}
 