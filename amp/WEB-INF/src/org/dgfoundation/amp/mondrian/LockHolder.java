package org.dgfoundation.amp.mondrian;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import org.dgfoundation.amp.algo.ExceptionRunnable;

/**
 * Lock utils
 * @author Constantin Dolghier
 *
 */
public class LockHolder {
	public final ReentrantLock lock;
	public final String name;
	
	public LockHolder(String name) {
		this.name = name;
		this.lock = new ReentrantLock();
	}
	
	public void runUnderLock(ExceptionRunnable<?> r) {
		runUnderLock(lock, r);
	}
		
	public static void runUnderLock(Lock lock, ExceptionRunnable<?> r) {
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
	
	@Override public String toString() {
		return "lockholder for " + name;
	}
	
	@Override public int hashCode() {
		return toString().hashCode();
	}
	
	@Override public boolean equals(Object oth) {
		LockHolder other = (LockHolder) oth;
		return this.toString().equals(other.toString()) && this.lock.equals(other.lock);
	}
}
