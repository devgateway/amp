package org.dgfoundation.amp.testmodels.dimensions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dgfoundation.amp.nireports.schema.DimensionLevel;
import org.dgfoundation.amp.nireports.schema.NiDimension;



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
	
	private void populateMaps(HNDNode node, long parentId, int level) {
		parentsPerLevel.get(level).put(node.id, parentId);
		entityIds.put(node.name, node.id);
			
		for (HNDNode child : node.children) {
			childrenPerLevel.get(level).computeIfAbsent(node.id, z -> new HashSet<>()).add(child.id);
			populateMaps(child, node.id, level + 1);
		}
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
