package org.dgfoundation.amp.nireports;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;


/**
 * holds the metadata of a cell, indexed by category for O(1) lookup
 * @author Dolghier Constantin
 *
 */
public class MetaInfoSet implements Iterable<MetaInfo>
{
	/**
	 * Map<String category, MetaInfo-with-category>
	 */
	protected final HashMap<String, MetaInfo> metadata;
	protected boolean frozen = false;
		
	public MetaInfoSet() {
		metadata = new HashMap<String, MetaInfo>();
	}
	
	public MetaInfoSet(MetaInfoSet source) {
		this.metadata = new HashMap<String, MetaInfo>(source.metadata);
	}
	
	public MetaInfo getMetaInfo(String category) {
		return metadata.get(category);
	}
	
	public boolean hasMetaInfo(String category) {
		return getMetaInfo(category) != null;
	}
	
	public void add(MetaInfo info) {
		if (info == null)
			throw new RuntimeException("not allowed to add null metainfo!");
		if (frozen)
			throw new RuntimeException("trying to add metainfo to a frozen MetaInfoSet");
		this.metadata.put(info.getCategory(), info);
	}
	
	public void addAll(MetaInfoSet infoSet) {
		if (frozen)
			throw new RuntimeException("trying to add metainfo to a frozen MetaInfoSet");
		this.metadata.putAll(infoSet.metadata);
	}
	
	public boolean catEquals(String category, Comparable<?> value) {
		MetaInfo mInfo = getMetaInfo(category);
		return mInfo != null && mInfo.getValue().equals(value);
	}
	
	/**
	 * read-only iterator
	 */
	@Override
	public Iterator<MetaInfo> iterator() {
		return Collections.unmodifiableCollection(this.metadata.values()).iterator();
	}
	
	@Override
	public String toString() {
		return String.format("MetaInfoSet: " + this.metadata.toString());
	}
}
