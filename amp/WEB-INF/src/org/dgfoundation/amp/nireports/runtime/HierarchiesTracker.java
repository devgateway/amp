package org.dgfoundation.amp.nireports.runtime;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.dgfoundation.amp.nireports.schema.Behaviour;

/**
 * an umbrella for multiple-main-ids-trackers
 * @author Dolghier Constantin
 *
 */
public class HierarchiesTracker {
	public final static HierarchiesTracker EMPTY = new HierarchiesTracker(Collections.emptyMap());
	
	private final Map<Long, PerItemHierarchiesTracker> perActivityTrackers;
	private HierarchiesTracker(Map<Long, PerItemHierarchiesTracker> perActivityTrackers) {
		this.perActivityTrackers = perActivityTrackers;
	}
	
	public HierarchiesTracker advanceHierarchy(Map<Long, NiCell> percentages) {
		Map<Long, PerItemHierarchiesTracker> res = new HashMap<>(perActivityTrackers);
		for(long actId:percentages.keySet()) {
			PerItemHierarchiesTracker oldValue = res.getOrDefault(actId, PerItemHierarchiesTracker.EMPTY);
			PerItemHierarchiesTracker newValue = oldValue.advanceHierarchy(percentages.get(actId).getCell());
			res.put(actId, newValue);
		}
		return new HierarchiesTracker(res);
	}
	
	public BigDecimal calculatePercentage(long actId, Behaviour<?> behaviour) {
		if (!perActivityTrackers.containsKey(actId))
			return BigDecimal.ONE; // no info -> no percentages
		return perActivityTrackers.get(actId).calculatePercentage(behaviour.getHierarchiesListener());
	}
}
