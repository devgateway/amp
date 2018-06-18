package org.dgfoundation.amp.nireports.schema;

import static org.dgfoundation.amp.algo.AmpCollections.safeGet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dgfoundation.amp.nireports.NiUtils;
import org.dgfoundation.amp.nireports.runtime.ColumnReportData;

/**
 * a NiDimension which uses a tabular bidiarray as a source of data.
 * Use it by defining {@link #getTabularData()}
 * @author Dolghier Constantin
 *
 */
public abstract class TabularSourcedNiDimension extends NiDimension {
    
    /**
     * @param name
     * @param sourceViewName
     * @param idColumnsNames must be enumerated from top to bottom
     */
    public TabularSourcedNiDimension(String name, int depth) {
        super(name, depth);
    }
    
    @Override
    protected List<DimensionLevel> fetchDimension() {
        
        // build the list of ids and the parents of each
        List<Map<Long, Long>> parentsPerLevel = new ArrayList<>(); // each level contains Map<element in level K, parent in level K - 1. For level 0 the parents are zero
        List<Map<Long, Set<Long>>> childrenPerLevel = new ArrayList<>();            

        for(int i = 0; i < depth; i++) {
            parentsPerLevel.add(new HashMap<>());
            childrenPerLevel.add(new HashMap<>());
        }
        // build parent and child relationships
        List<List<Long>> tabularData = getTabularData();
        for (List<Long> row : tabularData) {
            NiUtils.failIf(row.size() != depth, 
                    () -> String.format("NiDimension %s: row has length %d instead of %d: %s", 
                    this.name, row.size(), depth, row.toString()));
            for (int level = 0; level < depth; level++) {
                final int llevel = level;
                NiUtils.failIf(row.get(level) == null, () -> String.format("NiDimension %s: row contains null: <%s>", this.name, row.toString()));
                Map<Long, Long> levelParents = parentsPerLevel.get(level);
                Map<Long, Set<Long>> levelChildren = childrenPerLevel.get(level);
                
                Long elemId = row.get(level);
                if (elemId == null) {
                    for(int i = level + 1; i < depth; i++) {
                        final int li = i;
                        NiUtils.failIf(row.get(i) != null, () -> String.format("NiDimension %s: level %d is null, but sublevel %d is non null", this.name, llevel, li));
                    }
                    break; // all the subsequent columns are ignored
                }
                
                long parentId = safeGet(row, level - 1);
                NiUtils.failIf(level > 0 && parentId == 0, () -> String.format("while fetching dimension %s, level %d, element %d had a null parent, which is not allowed", this, llevel, elemId));
                    
                long childId = safeGet(row, level + 1);
                boolean haveChild = childId != 0;
                    
                NiUtils.failIf(levelParents.containsKey(elemId) && levelParents.get(elemId) != parentId, () -> String.format("error while fetching dimension %s, level %d: element %d has two different parents defined: %d and %d", this.toString(), llevel, elemId, parentId, levelParents.get(elemId)));
                levelParents.putIfAbsent(elemId, parentId);

                if (haveChild) {
                    // not everyone has a l+1 child
                    levelChildren.computeIfAbsent(elemId, key -> new HashSet<>()).add(childId);
                }
            }
        };
        
        // AMP-24342 - Add 'Undefined' items
        if (!tabularData.isEmpty()) {
            for (int level = 0; level < depth; level++) {
                Map<Long, Long> levelParents = parentsPerLevel.get(level);
                Map<Long, Set<Long>> levelChildren = childrenPerLevel.get(level);
                
                long elemId = ColumnReportData.UNALLOCATED_ID;
                long parentId = level == 0 ? 0L : ColumnReportData.UNALLOCATED_ID;
                long childId = ColumnReportData.UNALLOCATED_ID;
                
                levelParents.putIfAbsent(elemId, parentId);
    
                if (level < depth - 1) {
                    levelChildren.computeIfAbsent(elemId, key -> new HashSet<>()).add(childId);
                }
            }
        }
        
        List<DimensionLevel> res = new ArrayList<>();
        for(int level = 0; level < depth; level++) {
            res.add(new DimensionLevel(level, childrenPerLevel.get(level), parentsPerLevel.get(level), level == depth - 1));
        }
        return res;
    }

    protected abstract List<List<Long>> getTabularData();               
    
    protected void addIfOk(Map<Long, String> map, Long key, String value) {
        if (key != null && key > 0 && value != null)
            map.put(key, value);
    }
}
