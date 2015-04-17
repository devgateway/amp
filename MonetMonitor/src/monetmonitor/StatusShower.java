package monetmonitor;

/**
 * 
 * @author acartaleanu
 * Interface for showing messages (status, errors etc.)
 *
 */

public interface StatusShower {
	public void showStatus(String status) throws Exception;
	public void start();
}
 