package org.dgfoundation.amp.nireports.meta;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.stream.Collectors;

/**
 * holds the metadata of a cell, indexed by category for O(1) lookup <br />
 * This is an append-only datastructure by design <br />
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
    
    /**
     * returns 
     * @param category
     * @param value
     * @return
     */
    public boolean containsMeta(String category, Object value) {
        MetaInfo info = getMetaInfo(category);
        return info != null && info.v.equals(value);
    }
    
    public void add(MetaInfo info) {
        if (info == null)
            throw new RuntimeException("not allowed to add null metainfo!");
        if (frozen)
            throw new RuntimeException("trying to add metainfo to a frozen MetaInfoSet");
        this.metadata.put(info.getCategory(), info);
    }
    
    public MetaInfoSet newInstance(String category, Object value) {
        MetaInfoSet res = new MetaInfoSet(this);
        res.add(category, value);
        return res;
    }
    
    public void addAll(MetaInfoSet infoSet) {
        if (frozen)
            throw new RuntimeException("trying to add metainfo to a frozen MetaInfoSet");
        this.metadata.putAll(infoSet.metadata);
    }

    public void add(String category, Object value) {
        add(generator.getMetaInfo(category, value));
    }

    public boolean catEquals(String category, Object value) {
        MetaInfo mInfo = getMetaInfo(category);
        return mInfo != null && mInfo.getValue().equals(value);
    }
    
    public MetaInfoSet freeze() {
        frozen = true;
        return this;
    }
    
    /**
     * returns the number of cells in the map
     * @return
     */
    public int getSize() {
        return metadata.size();
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
        return String.format("MetaInfoSet: %s", this.metadata.entrySet().stream().map(entry -> String.format("%s: %s", entry.getValue().k, entry.getValue().v)).collect(Collectors.toList()));
    }
    
    private final static MetaInfoSet empty = new MetaInfoSet(new MetaInfoGenerator()).freeze();
    
    /**
     * returns a frozen (immutable) instance
     * @return
     */
    public static MetaInfoSet empty() {return empty;}
}
