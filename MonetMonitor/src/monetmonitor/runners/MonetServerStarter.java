package monetmonitor.runners;



import monetmonitor.BeholderObservationResult;
import monetmonitor.Constants;

/**
 * @author acartaleanu
 * starts the server, based on Constants, eventually imported from settings.conf
 * runs the server in a separate thread for listening to output
 * doesn't start it if server already running 
 * */

public class MonetServerStarter extends Runner {



	@Override
	/**
	 * 
	 */
	protected String getCommand() {
		return Constants.getMonetExecPath() + " " + Constants.getMonetFarmPath();
	}

	@Override
	/**
	 * Checks whether BeholderConnectivity could connect to the server or not. 
	 * If he couldn't, server is OK to be started.
	 * If he could, no point in starting the server, it's already started. 
	 */
	public boolean allowedToRun() {
		return !(new MonetBeholderSanity()).check().equals(BeholderObservationResult.SUCCESS);

	}

	@Override
	protected String getCustomMessage() {
		return "Starting server...";
	}
}
