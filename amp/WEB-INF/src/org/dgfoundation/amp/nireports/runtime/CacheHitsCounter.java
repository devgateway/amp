package org.dgfoundation.amp.nireports.runtime;

import java.util.HashMap;
import java.util.Map;

/**
 * utility container for counting cache misses
 * @author Dolghier Constantin
 *
 */
public class CacheHitsCounter {
    private long calls;
    private long misses;
    private long estimatedCost;
    
    public void registerCall() {
        this.calls ++;
    }
    
    public void registerMiss(int cost) {
        this.misses ++;
        this.estimatedCost += cost;
    }
    
    public double getHitPercentage() {
        return calls == 0 ? 0 : (calls - misses) * 100.0 / calls;
    }
    
    public double getCostPerMiss() {
        return misses == 0 ? 0 : (estimatedCost * 1.0) / misses;
    }
    
    public void reset() {
        this.calls = this.misses = 0;
    }
    
    public Map<String, Object> getStats() {
        Map<String, Object> res = new HashMap<>();
        res.put("calls", calls);
        res.put("misses", misses);
        res.put("hits", calls - misses);
        res.put("hitPercentage", getHitPercentage());
        res.put("costPerMiss", getCostPerMiss());
        return res;
    }
}
