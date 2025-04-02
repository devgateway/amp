package org.dgfoundation.amp.algo;

import java.io.Serializable;

/**
 * wraps a value in an object so that it could be referenced from within a Java7 callback
 * @author Constantin Dolghier
 *
 * @param <K>
 */
public class ValueWrapper<K> implements Serializable{
    
    /**
     * 
     */
    private static final long serialVersionUID = 4014305659318790545L;
    public K value;

    public ValueWrapper(K value) {
        this.value = value;
    }
        
    public void set(K value) {
        this.value = value;
    }
    
    @Override
    public String toString() {
        return this.value == null ? "null" : this.value.toString();
    }
}
