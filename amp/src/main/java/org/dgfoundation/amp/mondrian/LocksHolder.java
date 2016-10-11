package org.dgfoundation.amp.mondrian;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

/**
 * this class is NOT thread-safe
 * 
 * P.S. 2: THIS CLASS IS NOT USED :-)
 * @author Constantin Dolghier
 *
 */
public class LocksHolder {
	private final Set<Semaphore> acquiredLocks = new HashSet<>();
	protected static Logger logger = Logger.getLogger(LocksHolder.class);
	
	public synchronized void lock(Semaphore lock) {
		if (this.acquiredLocks.contains(lock))
			return;
		try{lock.acquire();}catch(Exception e){}
		this.acquiredLocks.add(lock);
	}
	
	public synchronized void lockWithTimeout(final Semaphore lock, final int timeout) {
		lock(lock);
		new Thread(new Runnable() {
			@Override public void run() {
				try {Thread.sleep(timeout);}
				catch(Exception e){}
				if (acquiredLocks.contains(lock)) {
					logger.error("lock timed out the deadline of " + timeout + " ms, forcibly releasing (is Mondrian stupid again?)");
				}
				release(lock);
			}
		}).start();
	}
	
	public synchronized void unlockAll() {
		for(Semaphore lock:this.acquiredLocks)
			lock.release();
		acquiredLocks.clear();
	}
	
	public synchronized void release(Semaphore lock) {
		if (acquiredLocks.contains(lock)) {
			lock.release();
			acquiredLocks.remove(lock);
		}
	}
}
