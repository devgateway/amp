package org.dgfoundation.amp.nireports.runtime;

import org.dgfoundation.amp.nireports.schema.Behaviour;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * an umbrella for multiple-main-ids-trackers
 * @author Dolghier Constantin
 *
 */
public class MultiHierarchiesTracker {
    public final static MultiHierarchiesTracker buildEmpty(CacheHitsCounter counter) {return new MultiHierarchiesTracker(Collections.emptyMap(), counter);}
    
    private final Map<Long, HierarchiesTracker> perActivityTrackers;
    private final CacheHitsCounter counter;
    private MultiHierarchiesTracker(Map<Long, HierarchiesTracker> perActivityTrackers, CacheHitsCounter counter) {
        this.perActivityTrackers = perActivityTrackers;
        this.counter = counter;
    }
    
    public MultiHierarchiesTracker advanceHierarchy(Map<Long, NiCell> percentages) {
        Map<Long, HierarchiesTracker> res = new HashMap<>(perActivityTrackers);
        for(long actId:percentages.keySet()) {
            HierarchiesTracker oldValue = res.getOrDefault(actId, HierarchiesTracker.buildEmpty(counter)); // class not used now, so counters are wasted
            HierarchiesTracker newValue = oldValue.advanceHierarchy(percentages.get(actId).getCell());
            res.put(actId, newValue);
        }
        return new MultiHierarchiesTracker(res, counter);
    }
    
    public BigDecimal calculatePercentage(long actId, Behaviour<?> behaviour) {
        if (!perActivityTrackers.containsKey(actId))
            return BigDecimal.ONE; // no info -> no percentages
        return perActivityTrackers.get(actId).calculatePercentage(behaviour.getHierarchiesListener());
    }
}
