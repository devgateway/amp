package org.dgfoundation.amp.mondrian;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import org.dgfoundation.amp.algo.ExceptionRunnable;

/**
 * Lock utils
 * @author Constantin Dolghier
 *
 */
public class ReadWriteLockHolder {
	public final ReentrantReadWriteLock lock;
	public final String name;
	
	public ReadWriteLockHolder(String name) {
		this.name = name;
		this.lock = new ReentrantReadWriteLock();
	}
	
	public void runUnderReadLock(ExceptionRunnable<?> r) {
		LockHolder.runUnderLock(lock.readLock(), r);
	}
	
	public void runUnderWriteLock(ExceptionRunnable<?> r) {
		LockHolder.runUnderLock(lock.writeLock(), r);
	}
}
