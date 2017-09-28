package org.dgfoundation.amp.nireports.schema;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.dgfoundation.amp.nireports.NiUtils;

/**
 * a class describing a Level of a dimension. A Level has all its nodes represented as two maps <br />
 * {@link #parents} always contains the full set, {@link #children} contains either the same keys as {@link #parents} or is empty
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
    
    /**
     * if this one is true, then {@link #children} is empty
     */
    public final boolean isLastLevel;
    
    public DimensionLevel(int level, Map<Long, Set<Long>> children, Map<Long, Long> parents, boolean isLastLevel) {
        if (isLastLevel) {
            NiUtils.failIf(!children.isEmpty(), () -> String.format("for last level %d, children should be empty", level));
        } else {
            for(long childKey:children.keySet()) {
                NiUtils.failIf(!parents.containsKey(childKey), () -> String.format("element %d on level %d has children %s specified but no parent", childKey, level, children.get(childKey)));
            }
        }
        this.children = parents.keySet().stream().collect(Collectors.toMap(e -> e, e -> Collections.unmodifiableSet(children.getOrDefault(e, Collections.emptySet()))));
        this.parents = Collections.unmodifiableMap(parents);
        this.level = level;
        this.isLastLevel = isLastLevel;
    }
    
    public Set<Long> getAllIds() {
        Set<Long> res = new HashSet<>(parents.keySet());
        res.addAll(children.keySet());
        return res;
    }
    
    @Override
    public String toString() {
        return String.format("level %d, info: %s", level, 
            new TreeSet<>(parents.keySet()).stream().map(
                id -> String.format("(id: %d, parent: %d, children: %s)", id, parents.get(id), new TreeSet<>(children.get(id))))
                .collect(Collectors.toList()));
    }
}
