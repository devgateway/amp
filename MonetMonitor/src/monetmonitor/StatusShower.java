package monetmonitor;


/*interface for abstractization of output method*/
public interface StatusShower {
	public void showStatus(String status) throws Exception;
	public void start();
}
 