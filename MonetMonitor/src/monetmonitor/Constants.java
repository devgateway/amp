package monetmonitor;


import java.util.HashMap;

import java.util.Map;

/**
 * 
 * @author acartaleanu
 * Class for keeping parameters extracted from settings.conf.
 * Methods are self-explanatory.
 *
 */
public class  Constants {
	
	public static Map<String, String> parametersMap = new HashMap<String, String>();

	
	public static String getMonetFarmPath() {
		return parametersMap.get("MONET_FARM_PATH");
	}
	
//	public static String getDatabaseName() {
//		return parametersMap.get("DB_NAME");
//	}
	
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
	/*#create db command
CREATE_DB_COMMAND monetdb create 
#release db command
RELEASE_DB_COMMAND monetdb release 
#stop db command
STOP_DB_COMMMAND monetdb stop 
#destroy db command
DESTROY_COMMAND monetdb destroy 
	 * */
	public static String getCommandStop() {
		return parametersMap.get("STOP_DB_COMMAND");
	}
	public static String getCommandCreate() {
		return parametersMap.get("CREATE_DB_COMMAND");
	}
	public static String getCommandRelease() {
		return parametersMap.get("RELEASE_DB_COMMAND");
	}
	public static String getCommandDestroy() {
		return parametersMap.get("DESTROY_DB_COMMAND");
	}
}
