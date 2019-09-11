package org.digijava.kernel.persistence;

import java.util.List;

/**
 * Interface used to describe the methods of in memory objects manager.
 * <p>
 * Mainly intended for testing purposes.
 *
 * @author Viorel Chihai
 */
public interface InMemoryManager<T> {
    
    T get(Long id);
    
    List<T> getAllValues();
    
}
