package monetmonitor;


import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 
 * @author acartaleanu
 * Class for keeping parameters extracted from settings.conf.
 * Methods are self-explanatory.
 *
 */
public class  Constants {
	
	
	private static Properties props = new Properties();
	public static Map<String, String> parametersMap = new HashMap<String, String>();

	
	public static Properties getPropertiesInstance() {
		return props;
	}
	
	public static String getMonetFarmPath() {
		return props.getProperty("MONET_FARM_PATH");
	}

	
	public static String getMonetExecPath() {
		return props.getProperty("MONET_EXEC_PATH");
	}

	public static String getLogfilePath() {
		return props.getProperty("LOG_FILE_PATH");
	}
	
	public static int getTimerDelay() {
		//remove eventual trailing spaces
		return Integer.parseInt(props.getProperty("TIMER_DELAY").replaceAll(" ", ""));
	}
	public static String getDbName() {
		return props.getProperty("DB_NAME").replace(" ", "");
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
		return props.getProperty("STOP_DB_COMMAND");
	}
	public static String getCommandCreate() {
		return props.getProperty("CREATE_DB_COMMAND");
	}
	public static String getCommandRelease() {
		return props.getProperty("RELEASE_DB_COMMAND");
	}
	public static String getCommandDestroy() {
		return props.getProperty("DESTROY_DB_COMMAND");
	}

	public static String getCommandStatus() {
		return props.getProperty("STATUS_DB_COMMAND");
	}
	
	public static String getPostgresUser() {
		return props.getProperty("PG_USERNAME");
	}
	
	/* it's called "swearword" for obfuscation
	 * obviously a bad idea to keep the pg pass in plaintext in a config file
	 * and keeping it hashed or somehow otherwise of a lock is too much fuss 
	*/
	public static String getPostgresPassword() {
		return props.getProperty("PG_SWEARWORD");
	}
	
	public static String getPostgresPort() {
		return props.getProperty("PG_PORT").replaceAll(" ", "");
	}

	//default value == 100
	public static int getHealthThreshold() {
		return Integer.parseInt(props.getProperty("HEALTH_THRESHOLD", "100"));
	}

	public static int getBroadcastFrequency() {
		return Integer.parseInt(props.getProperty("BROADCAST_FREQUENCY", "1200"));
	}

	public static int getMemCheckFrequency() {
		return Integer.parseInt(props.getProperty("MEMCHECK_FREQUENCY", "75"));
	}

	
	public static boolean debugLogMode() {
		return props.getProperty("LOG_LEVEL").contains("debug");
	}
	
}
