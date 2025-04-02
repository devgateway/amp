package org.dgfoundation.amp.algo;

/**
 * a piece of code which might throw an Exception
 * @author Dolghier Constantin
 *
 * @param <K>
 */
public interface ExceptionRunnable<K extends Exception>{
    public void run() throws K;
}
