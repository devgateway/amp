package org.dgfoundation.amp.nireports.schema;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.dgfoundation.amp.nireports.NiUtils;

/**
 * a class describing a Level of a dimension. A Level has all its nodes represented as two maps with <strong>the exact set of keys</strong>:
 * 
 * @author Dolghier Constantin
 *
 * @param <K>
 */
public class DimensionLevel {
	public final int level;
	
	/**
	 * Map<Node_id in {@link #level}, Set<Node_id in levels n+1>(empty for last level)
	 */
	public final Map<Long, Set<Long>> children;
	
	/**
	 * Map<Node_id in {@link #level}, Node_id in {@link #level}-1
	 */
	public final Map<Long, Long> parents;
	
	public DimensionLevel(int level, Map<Long, Set<Long>> children, Map<Long, Long> parents) {
		NiUtils.failIf(children.size() != parents.size() || children.keySet().equals(parents.keySet()), "children and parents should have the exactly same keys!");
		this.children = children.entrySet().stream().collect(Collectors.toMap(e -> e.getKey(), e -> Collections.unmodifiableSet(e.getValue())));
		this.parents = Collections.unmodifiableMap(parents);
		this.level = level;
	}
}
