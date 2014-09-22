package org.dgfoundation.amp.algo;

/**
 * wraps a value in an object so that it could be referenced from within a Java7 callback
 * @author Constantin Dolghier
 *
 * @param <K>
 */
public class ValueWrapper<K> {
	
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
