package org.dgfoundation.amp.nireports.schema;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * a class holding info regarding a node of a NiDimension level
 * @author Dolghier Constantin
 *
 * @param <K>
 */
public class DimensionCell<K> implements Comparable<DimensionCell<K>> {
	public final long id;
	public final K payload;
	public final Set<Long> children;
	
	public DimensionCell(long id, K payload, Set<Long> children) {
		this.id = id;
		this.payload = payload;
		this.children = Collections.unmodifiableSet(new HashSet<>(children));
	}
	
	@Override
	public int compareTo(DimensionCell<K> other) {
		return Long.compare(id, other.id);
	}
	
	@Override
	public int hashCode() {
		return Long.hashCode(id);
	}
	
	@Override
	public boolean equals(Object oth) {
		if (!(oth instanceof DimensionCell))
			return false;
		return id == ((DimensionCell<K>) oth).id;
	}
}
