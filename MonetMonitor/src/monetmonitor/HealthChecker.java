package monetmonitor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HealthChecker {
	/**
	 * Class for checking the health status of the database by running "monetdb status \<dbname\>"
	 * @return
	 */
	/*
	 * Structure of a line is:

name    	    state   health                     remarks
amp_monet_test  S 10m   17%  2h  mapi:monetdb://calcifer:50000/amp_monet_test

	 */
	
	
	private Integer extractHealthFromLine(String line) {
		Pattern p = Pattern.compile("\\d+%");
		Matcher m = p.matcher(line);
		if (m.find())
			return Integer.parseInt(m.group().replaceAll("%", ""));
		return null;
	}
	
	
	
	public BeholderObservationResult runHealthCheck() {
		BeholderObservationResult res = BeholderObservationResult.ERROR_UNKNOWN;
		try {
			
			Process proc = Runtime.getRuntime().exec(Constants.getCommandStatus());
			InputStreamReader streamReader = new InputStreamReader(proc.getInputStream());
		    BufferedReader input = new BufferedReader(streamReader);
		    String line;
		    while ((line = input.readLine()) != null) {
		    	System.out.println("Status response:" + line);
//		    	Utils.toLog("MonetDB response to command " +  + ":>>> " + line);
		    	if (line.contains("cannot find a control socket")){
		    		//monetdbd is down -> health checker doesn't have a proper response to this
		    		//, and the beholder should catch it too 
		    		res = BeholderObservationResult.ERROR_CANNOT_CONNECT;
		    	}
		    	
		    	if (line.contains(Constants.getDbName())) {
		    		Integer health = extractHealthFromLine(line);
		    		if (health != null) {
		    			if (health < Constants.getHealthThreshold())
		    				return BeholderObservationResult.ERROR_HEALTH_BELOW_THRESHOLD;
		    			else
		    				return BeholderObservationResult.SUCCESS;
		    		}
		    	}
		    	
		    }
		    input.close();
		} catch( Exception io) {
			Utils.broadcastStatus("error when parsing status message!");
		} 
		return res;
	}
}
