package org.dgfoundation.amp.mondrian;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

public class ReadWriteLockHolder {
	public final ReentrantReadWriteLock lock;
	public final String name;
	
	public ReadWriteLockHolder(String name) {
		this.name = name;
		this.lock = new ReentrantReadWriteLock();
	}
	
	public void runUnderReadLock(ExceptionRunnable r) {
		runUnderLock(lock.readLock(), r);
	}
	
	public void runUnderWriteLock(ExceptionRunnable r) {
		runUnderLock(lock.writeLock(), r);
	}
	
	protected static void runUnderLock(Lock lock, ExceptionRunnable r) {
		try{
			lock.lock();
			r.run();
		}
		catch(Exception e) {
			if (e instanceof RuntimeException)
				throw (RuntimeException) e;
			throw new RuntimeException(e);
		}
		finally {
			lock.unlock();
		}
	}
}
