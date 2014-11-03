package org.digijava.kernel.ampapi.saiku.util;

public class ComparableValue<K extends Comparable<K>> implements Comparable<ComparableValue<K>> {
	public String v;
	public K k;
	
	public ComparableValue(String v, K k) {
		this.v = v;
		this.k = k;
	}
	
	public int compareTo(ComparableValue<K> oth) {
		return - this.k.compareTo(oth.k);
	}
	
	public String toString() {
		return String.format("(<%s>: %s)", v, k);
	}
}
