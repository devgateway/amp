package org.dgfoundation.amp.mondrian;

import java.util.concurrent.Semaphore;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.algo.ValueWrapper;

/**
 * shamelessly copy-pasted from http://syndeticlogic.net/?p=89 and then perfectly suited for AMP
 * @author simple
 *
 */
public class ReaderWriterLock {

	protected static Logger logger = Logger.getLogger(ReaderWriterLock.class);
	
	public ReaderWriterLock() {
		readers = 0;
		readLock = new Semaphore(1);
		readWriteLock = new Semaphore(1);
	}

	public void writeLock() {
		acquire(readWriteLock);
	}

	public void writeUnlock() {
		readWriteLock.release();
	}

	public void readLock() {
		acquire(readLock);
		if (readers == 0) {
			acquire(readWriteLock);
		}
		readers++;
		readLock.release();
	}

	public void readUnlock() {
		assert readers > 0;
		acquire(readLock);    
		readers--;
		if (readers == 0) {
			readWriteLock.release();
		}
		readLock.release();
	}

	public long readLockWithTimeout(final int timeout, final ValueWrapper<Boolean> forced) {
		long start = System.currentTimeMillis();
		readLock();
		long delta = System.currentTimeMillis() - start;
		new Thread(new Runnable() {
			@Override public void run() {
				try{Thread.sleep(timeout);}catch(InterruptedException e){}
				boolean unlocked = unlockIfStillUsed(forced);
				if (unlocked)
					logger.error("mdx report has been holding the ETL FULL_ETL lock for too long, releasing prematurely");
			}
		}).start();
		return delta;
	}
	
	public boolean unlockIfStillUsed(final ValueWrapper<Boolean> forced) {
		synchronized(forced) {
			if (!forced.value) {
				forced.value = true; // race condition here, but too lazy to work on it more. AMP has much bigger issues :D
				readUnlock();
				return true;
			}
			return false;
		}
	}
	
	private void acquire(Semaphore s) {
		try {s.acquire();}
		catch(InterruptedException e){};
	}
	
	private Semaphore readLock;
	private Semaphore readWriteLock;
	private int readers;
}
