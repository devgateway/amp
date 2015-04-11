package monetmonitor;


import java.util.HashMap;

import java.util.Map;


public class  Constants {
	
	public static Map<String, String> parametersMap = new HashMap<String, String>();

	
	public static String getMonetFarmPath() {
		return parametersMap.get("MONET_FARM_PATH");
	}
	
	public static boolean getNoGui() {
		return parametersMap.get("NO_GUI").contains("true");
		
	}
	
	public static String getMonetExecPath() {
		return parametersMap.get("MONET_EXEC_PATH");
	}

	public static String getLogfilePath() {
		return parametersMap.get("LOG_FILE_PATH");
	}
	public static int getTimerDelay() {
		//remove eventual trailing spaces
		return Integer.parseInt(parametersMap.get("TIMER_DELAY").replaceAll(" ", ""));
	}
	public static String getDbName() {
		return parametersMap.get("DB_NAME").replace(" ", "");
	}
}
