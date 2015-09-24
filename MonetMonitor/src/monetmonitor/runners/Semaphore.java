package monetmonitor.runners;

//import java.util.HashMap;
//import java.util.Map;
@Deprecated
/**
 * sacked the idea, but feel bad about deleting code that hasn't ever been used, 
 * maybe will find its use in the future
 * @author acartaleanu
 *
 */
public class Semaphore {
//	private Map<String, Boolean> signals = new HashMap<String, Boolean>();
	private boolean signal = false;
	
	@Deprecated
	public synchronized void take() throws InterruptedException {
		while (this.signal) wait();
		this.signal = true;
		this.notify();
	}
	@Deprecated
	public synchronized void release() throws InterruptedException{
		while(!this.signal) 
			wait();
		this.signal = false;
		this.notify();
	}
}
