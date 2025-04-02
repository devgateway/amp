package org.dgfoundation.amp.algo.timing;

/**
 * a task which is also given the {@link InclusiveTimer}'s node of this running code 
 * @author Dolghier Constantin
 *
 * @param <K> the time of exception thrown by the client code
 */
public interface TimedTask<K extends Exception> {
    public void run(RunNode selfTask) throws K;
}
