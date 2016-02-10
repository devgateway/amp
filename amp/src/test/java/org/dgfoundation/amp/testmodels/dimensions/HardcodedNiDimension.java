package org.dgfoundation.amp.testmodels.dimensions;

import java.util.ArrayList;
import java.util.HashMap;
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
		populateMaps(rootElements, 0);
		for(int level = 0; level < depth; level++) {
			computedLevels.add(new DimensionLevel(level, childrenPerLevel.get(level), parentsPerLevel.get(level), level == depth - 1));
		}
		return computedLevels;
	}
	
	private void populateMaps(List<HNDNode> roots, int level){
		for (HNDNode rootNode : roots) {
			long parentId = rootNode.id;
			entityIds.put(rootNode.name, rootNode.id);
			for (HNDNode child : rootNode.children) {
				entityIds.put(child.name, child.id);
				parentsPerLevel.get(level).put(child.id, parentId);
				childrenPerLevel.get(level).put(child.id, child.getChildrenIds());
				populateMaps(child.children, level + 1);
			}
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
	 * Must override in subclass.
	 * Will contain element() calls, representing heirarchical trees describing said dimension.
	 * @return
	 */
	protected abstract List<HNDNode> buildHardcodedElements();

}
