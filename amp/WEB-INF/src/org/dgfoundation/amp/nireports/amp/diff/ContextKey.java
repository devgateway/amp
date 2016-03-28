package org.dgfoundation.amp.nireports.amp.diff;

import org.dgfoundation.amp.nireports.NiReportsEngine;

public class ContextKey<K> {
	public final NiReportsEngine context;
	public final K key;
	
	public ContextKey(NiReportsEngine context, K key) {
		this.context = context;
		this.key = key;
	}
	
	@Override
	public int hashCode() {
		return key.hashCode();
	}
	
	@Override
	public boolean equals(Object oth) {
		if (!(oth instanceof ContextKey))
			return false;
		return key.equals(((ContextKey) oth).key);
	}
	
	@Override
	public String toString() {
		return String.format("ContextKey of %s", key);
	}
}
