package monetmonitor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



/*
 * 
free -m
             total       used       free     shared    buffers     cached
Mem:         15732      13329       2402       1144        685       4078
-/+ buffers/cache:       8566       7166
Swap:        15532         26      15506

 * 
 * 
 */

public class MemoryChecker {
	private Integer extractMemoryFromLine(String line) {
		Pattern p = Pattern.compile("\\d+");
		Matcher m = p.matcher(line);
		//see in comment above -- we need the third number to see free memory in Mb
		int i = 0; 
		while (m.find()) {
			i++;
			if (i == 3)
				return Integer.parseInt(m.group());
		}
		return null;
	}
	


	
	public Integer getFreeMemory() {
		Integer res = null;
		try {
			
			Process proc = Runtime.getRuntime().exec("free -m");
			InputStreamReader streamReader = new InputStreamReader(proc.getInputStream());
		    BufferedReader input = new BufferedReader(streamReader);
		    String line;
		    while ((line = input.readLine()) != null) {
//		    	System.out.println("Status response:" + line);
		    	
//		    	if (line.contains("cannot find a control socket")){
//		    		res = BeholderObservationResult.ERROR_CANNOT_CONNECT;
//		    	}
		    	if (line.contains("Mem")) {
		    		Integer freemem = extractMemoryFromLine(line);
		    		Utils.broadcastStatus("Memory: " + freemem);
		    	}
		    	
		    }
		    input.close();
		} catch( Exception io) {
			Utils.broadcastStatus("error when parsing status message!");
		} 
		return res;
	}

}
