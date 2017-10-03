package org.dgfoundation.amp.nireports.meta;

import java.util.HashMap;
import java.util.Map;

import org.dgfoundation.amp.algo.ValueWrapper;
import org.dgfoundation.amp.nireports.ImmutablePair;

/**
 * a caching generator of MetaInfo, used for saving memory and GC runtime <br />
 * <b>NOT THREAD SAFE</b> <br />
 * 
 * TODO: in case this approach turns out to be too memory-hungry, we could move instances of this class from column fetchers to NiReportsEngine and make the cache concurrent
 * This class does not use any lambdas or other fancy stuff, because it is on the critical performance path <br />
 * 
 * @author Dolghier Constantin
 *
 */
public class MetaInfoGenerator {
    final Map<String, Map<Object, MetaInfo>> cache = new HashMap<>();
    final ValueWrapper<Long> callsCount = new ValueWrapper<Long>(0l);
    final ValueWrapper<Long> uncachedCount = new ValueWrapper<Long>(0l);
    
    public MetaInfo getMetaInfo(String category, Object value) {
        callsCount.value ++;
        return cache.computeIfAbsent(category, cat -> new HashMap<Object, MetaInfo>()).computeIfAbsent(value, val -> {
            uncachedCount.value ++;
            return new MetaInfo(category, value);
        });
    }
    
    public ImmutablePair<Long, Long> getStats() {
        return new ImmutablePair<Long, Long>(callsCount.value, uncachedCount.value);
    }
}
