package monetmonitor;

/**
 * 
 * @author acartaleanu
 * Interface for showing messages (status, errors etc.)
 *
 */

public interface StatusShower {
	/**
	 * Send a message to the statusShower destination
	 * @param 	status Sting status (any message, actually) to be sent 
	 * 			to the statusShower destination
	 * @throws Exception
	 */
	public void showStatus(String status);
	/**
	 * Perform any class init code (may be NOP)
	 */
	public void start();
}
 