package org.dgfoundation.amp.mondrian;

public interface ExceptionRunnable<K extends Exception>{
	public void run() throws K;
}
