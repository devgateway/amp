package org.dgfoundation.amp.nireports.runtime;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * a thin {@link Map} wrapper which counts access stats
 * @author Dolghier Constantin
 *
 * @param <K>
 * @param <V>
 */
public class CountedCacher<K, V> {
    public final CacheHitsCounter stats;
    public final Map<K, V> cache = new HashMap<>();
    
    public CountedCacher(CacheHitsCounter stats) {
        this.stats = stats;
    }
    
    public V retrieveOrCompute(K k, int estimatedCost, Function<K, V> func) {
        stats.registerCall();
        return cache.computeIfAbsent(k, key -> {
            stats.registerMiss(estimatedCost);
            return func.apply(key);
        });
    }
        
}
