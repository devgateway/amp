package monetmonitor;



import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;


/**
 * 
 * @author acartaleanu
 * StatusShower outputting to log file
 * path to logfile should be specified in settings.conf 
 */
public class StatusShowerLog implements StatusShower {
	/**
	 * @param status String to be sent over to the logfile. Adds timestamp at the beginning of a log entry.
	 */
	public void showStatus(String status) {
		FileWriter writer;
		try {
			writer = new FileWriter(Constants.getLogfilePath(), true);
			Date now = new Date();
			writer.append(DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(now));
			writer.append(" ");
			writer.append(status);
			writer.append("\r\n");
			writer.close();		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * NOP for log status shower
	 */
	@Override
	public void start() {
	}
}
 