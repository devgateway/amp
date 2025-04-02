package org.dgfoundation.amp.ar;

import java.util.*;
import java.util.Map.Entry;

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
    
    static long calls = 0;
    static long iterations = 0;
    
    public MetaInfoSet()
    {
        metadata = new HashMap<String, MetaInfo>();
    }
    
    public MetaInfoSet(MetaInfoSet source)
    {
        this.metadata = new HashMap<String, MetaInfo>(source.metadata);
    }
    
    public MetaInfo getMetaInfo(String category)
    {
        calls ++;
        iterations ++;
        return metadata.get(category);
    }
    
    public boolean hasMetaInfo(String category)
    {
        return getMetaInfo(category) != null;
    }
    
    public void add(MetaInfo info)
    {
        if (info == null)
            throw new RuntimeException("not allowed to add null metainfo!");
        if (info.getCategory() == null)
            throw new RuntimeException("not allowed to add null-category metainfo!");
        if (info.getValue() == null)
            throw new RuntimeException("not allowed to add null-valued metainfo!");

        this.metadata.put(info.getCategory(), info);
    }
    
    public void addAll(MetaInfoSet infoSet)
    {
        this.metadata.putAll(infoSet.metadata);
    }
    
    public int size()
    {
        return metadata.size();
    }
    
    public boolean isEmpty()
    {
        return metadata.isEmpty();
    }
    
    public void clear()
    {
        metadata.clear();
    }
    
    
    /**
     * do not ever modify the collection via the iterator!
     * might introduces caches / precomputed stuff here in the future. In this case, will return an iterator which invalidates caches on remove()
     */
    @Override
    public Iterator<MetaInfo> iterator()
    {
        return this.metadata.values().iterator();
    }
    
    public void removeItemsByCategory(Collection<String> categories)
    {
        for(String cat:categories)
        {
            this.metadata.remove(cat);
        }
    }
    
    /**
     * equivalent to {@link #add(MetaInfo)}, but throws an exception in case a preexisting cell with the same value does not exist (sanity check)
     * @param newMetaInfo
     */
    public void replace(MetaInfo newMetaInfo)
    {
        if (!hasMetaInfo(newMetaInfo.getCategory()))
            throw new RuntimeException("replace() should only be used for replacing cells");
        add(newMetaInfo);
    }
    
    @Override
    public String toString()
    {
        return String.format("MetaInfoSet: " + this.metadata.toString());
    }
}
