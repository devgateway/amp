package org.dgfoundation.amp.mondrian;

/**
 * a class which returns (through always-lazy evaluation) an object of class K
 * @author Dolghier Constantin
 *
 * @param <K>
 */
public interface ObjectSource<K> {
	public K getObject();
}
