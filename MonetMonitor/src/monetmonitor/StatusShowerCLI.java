package monetmonitor;


/**
 * 
 * @author acartaleanu
 * Command line interface output 
 */
public class StatusShowerCLI implements StatusShower {

	/**
	 * @param status String to be shown in the console
	 */
	public void showStatus(String status) {
		System.out.println(status);
	}
	/**
	 * NOP for command line interface 
	 */
	@Override
	public void start() {
	}
}
 