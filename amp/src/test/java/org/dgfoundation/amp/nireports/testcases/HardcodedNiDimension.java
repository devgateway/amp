package org.dgfoundation.amp.nireports.testcases;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dgfoundation.amp.nireports.schema.DimensionLevel;
import org.dgfoundation.amp.nireports.schema.NiDimension;


/**
 * a hardcoded NiDimension which is using a tree as a source of its data. Because NiReports need a full tree, missing nodes are simulated by using -X nodes 
 * @author simple
 *
 */
public abstract class HardcodedNiDimension extends NiDimension {

    protected final List<HNDNode> rootElements;
    private final Map<String, Long> entityIds = new HashMap<String, Long>();
    private final List<DimensionLevel> computedLevels;
    private List<Map<Long, Long>> parentsPerLevel = new ArrayList<>(); 
    private List<Map<Long, Set<Long>>> childrenPerLevel = new ArrayList<>();            
    
    public HardcodedNiDimension(String name, int depth) {
        super(name, depth);
        rootElements = buildHardcodedElements();
        this.computedLevels = computeLevels();
    }
    
    private List<DimensionLevel> computeLevels() {
        List<DimensionLevel> computedLevels = new ArrayList<DimensionLevel>();
        for(int i = 0; i < depth; i++) {
            parentsPerLevel.add(new HashMap<>());
            childrenPerLevel.add(new HashMap<>());
        }
        
        for(HNDNode rootNode:rootElements)
            populateMaps(rootNode, 0l, 0);
        
        for(int level = 0; level < depth; level++) {
            computedLevels.add(new DimensionLevel(level, childrenPerLevel.get(level), parentsPerLevel.get(level), level == depth - 1));
        }
        return computedLevels;
    }
    
    protected void populateMaps(HNDNode node, long parentId, int level) {
        parentsPerLevel.get(level).put(node.id, parentId);
        entityIds.put(node.name, node.id);
            
        for (HNDNode child:node.children) {
            childrenPerLevel.get(level).computeIfAbsent(node.id, z -> new HashSet<>()).add(child.id);
            populateMaps(child, node.id, level + 1);
        }
        populateNegatives(node.id, node.id, level + 1);
    }
    
    /**
     * goes all the way till the bottom of the tree and completes it with negatives of the last positive seen
     * @param positiveId
     * @param parentId
     * @param level
     */
    protected void populateNegatives(long positiveId, long parentId, int level) {
        if (level >= depth)
            return;
        entityIds.put("Undefined", - positiveId);
        childrenPerLevel.get(level - 1).computeIfAbsent(parentId, z -> new HashSet<>()).add( - positiveId);
        parentsPerLevel.get(level).put( - positiveId, parentId);
        populateNegatives(positiveId, - positiveId, level + 1);
    }
    
    @Override
    protected List<DimensionLevel> fetchDimension() {
        return computedLevels;
    }
    
    public Map<String, Long> getEntityIds() {
        return entityIds;
    }

    /**
     * Will contain element() calls, representing trees describing said dimension
     * @return
     */
    protected abstract List<HNDNode> buildHardcodedElements();

}
