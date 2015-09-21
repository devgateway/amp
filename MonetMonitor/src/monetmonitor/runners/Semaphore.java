package monetmonitor.runners;

//import java.util.HashMap;
//import java.util.Map;
@Deprecated
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
