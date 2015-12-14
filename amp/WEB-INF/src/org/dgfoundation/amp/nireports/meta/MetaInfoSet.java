package org.dgfoundation.amp.nireports.meta;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

/**
 * holds the metadata of a cell, indexed by category for O(1) lookup <br />
 * This class is in the critical path; no nice code here - all we care about is speed and client code expressivity <br />
 * Java does not have extension methods, so we live with what we can
 * @author Dolghier Constantin
 *
 */
public class MetaInfoSet implements Iterable<MetaInfo>
{
	/**
	 * Map<String category, MetaInfo-with-category>
	 */
	protected final HashMap<String, MetaInfo> metadata;
	protected final MetaInfoGenerator generator;
	
	protected boolean frozen = false;
		
	public MetaInfoSet(MetaInfoGenerator generator) {
		metadata = new HashMap<String, MetaInfo>();
		this.generator = generator;
	}
	
	public MetaInfoSet(MetaInfoSet source) {
		this.metadata = new HashMap<String, MetaInfo>(source.metadata);
		this.generator = source.generator; 
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

	public void add(String category, Comparable<?> value) {
		add(generator.getMetaInfo(category, value));
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
