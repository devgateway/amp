package org.dgfoundation.amp.nireports.schema;

import org.dgfoundation.amp.nireports.NiUtils;
import org.dgfoundation.amp.nireports.schema.NiDimension.Coordinate;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * a snapshot of a {@link NiDimension} at a given moment
 * 
 * @author Dolghier Constantin
 *
 */
public final class DimensionSnapshot {
    public final List<DimensionLevel> data;
    public final int depth;
    private final Map<Long, Integer> idToLevel;
    
    public DimensionSnapshot(List<DimensionLevel> data) {
        this.data = data;
        this.depth = data.size();
        this.idToLevel = indexNodesByLevel(data);
    }
    
    protected static Map<Long, Integer> indexNodesByLevel(List<DimensionLevel> data) {
        Map<Long, Integer> res = new HashMap<>();
        for(int level = 0; level < data.size(); level++)
            for(long id:data.get(level).getAllIds())
                res.put(id, level);
        return res;
    }
    
    /**
     * returns the acceptable descendants of level n-1
     * @param level
     * @param ids
     * @return
     */
    public Set<Long> getAcceptableAscendants(int level, Collection<Long> ids) {
        NiUtils.failIf(level <= 0 || level >= depth, () -> String.format("level should be between %d and %d", 1, depth - 1));
        Set<Long> res = ids.stream().map(id -> data.get(level).parents.get(id)).collect(Collectors.toSet());
        res.remove(null);
        return res;
    }
    
    /**
     * returns the acceptable descendants of level n+1
     * @param level
     * @param ids
     * @return
     */
    public Set<Long> getAcceptableDescendants(int level, Collection<Long> ids) {
        NiUtils.failIf(level < 0 || level >= depth - 1, () -> String.format("level should be between %d and %d", 0, depth - 2));
        Set<Long> res = ids.stream().flatMap(id -> data.get(level).children.getOrDefault(id, Collections.emptySet()).stream()).collect(Collectors.toSet());
        return res;
    }
        
    /**
     * walks up the tree, doing the necessary number of {@link #getAcceptableAscendants(int, Collection)} calls
     * @param level
     * @param ids
     * @param targetLevel
     * @return
     */
    public Set<Long> getAcceptableAscendants(int level, Collection<Long> ids, int targetLevel) {
        NiUtils.failIf(level <= 0 || level >= depth, () -> String.format("level should be between %d and %d", 1, depth - 1));
        NiUtils.failIf(targetLevel < 0 || targetLevel >= depth, () -> String.format("targetLevel should be between %d and %d", 0, depth - 1));
        NiUtils.failIf(level < targetLevel, () -> String.format("can only search for ascendants in lower levels, but you asked to go from %d to %d", level, targetLevel));
        Set<Long> res = new HashSet<>(ids);
        int curLevel = level;
        while (curLevel > targetLevel) {
            res = getAcceptableAscendants(curLevel, res);
            curLevel --;
        }
        return res;
    }
    
    /**
     * walks down the tree, doing the necessary number of {@link #getAcceptableDescendants(int, Collection)} calls
     * @param level
     * @param ids
     * @param targetLevel
     * @return
     */
    public Set<Long> getAcceptableDescendants(int level, Collection<Long> ids, int targetLevel) {
        NiUtils.failIf(level < 0 || level >= depth - 1, () -> String.format("level should be between %d and %d", 0, depth - 2));
        NiUtils.failIf(targetLevel < 0 || targetLevel >= depth, () -> String.format("targetLevel should be between %d and %d", 0, depth - 1));
        NiUtils.failIf(level > targetLevel, () -> String.format("can only search for descendants in higher levels, but you asked to go from %d to %d", level, targetLevel));
        Set<Long> res = new HashSet<>(ids);
        int curLevel = level;
        while (curLevel < targetLevel) {
            res = getAcceptableDescendants(curLevel, res);
            curLevel ++;
        }
        return res;
    }
    
    /**
     * computes the correct direction to search for peers
     * @param level
     * @param ids
     * @param targetLevel
     * @return
     */
    public Set<Long> getAcceptableNeighbours(int level, Collection<Long> ids, int targetLevel) {
        //System.out.format("called getAcceptableNeighbours on %d, %d, %s\n", level, targetLevel, ids);
        if (targetLevel == level) {
            return ids.stream().filter(z -> data.get(level).parents.containsKey(z)).collect(Collectors.toSet());
        }
        if (targetLevel < level)
            return getAcceptableAscendants(level, ids, targetLevel);
        
        return getAcceptableDescendants(level, ids, targetLevel);
    }
    
    private int getLevelOf(Coordinate coo) {
        int level = coo.level == NiDimension.LEVEL_ALL_IDS ?
                idToLevel.getOrDefault(coo.id, 0) : coo.level;
        return level;
    }
    
    /**
     * returns an {@link IdsAcceptor} which filters based on given coordinates in this Dimension Snapshot
     * @param splitterCell
     * @return
     */
    public IdsAcceptor getCachingIdsAcceptor(List<Coordinate> splitters) {
        Map<Integer, Set<Long>> splitterCells = new HashMap<>();
        for(Coordinate coo:splitters) {
            splitterCells.computeIfAbsent(getLevelOf(coo), lev -> new HashSet<>()).add(coo.id);
        }
        if (depth == 1)
            return new IdentityIdsAcceptor(splitterCells);
        return new CachingIdsAcceptor(splitterCells);
    }
    
    public static<K> K throw_up(String msg) {
        throw new RuntimeException(msg);
    }
    
    @Override
    public String toString() {
        return String.format("depth = %d, data = %s", depth, data);
    }
    
    class CachingIdsAcceptor implements IdsAcceptor {
        
        final Map<Integer, Set<Long>> splitterCells;
        final Map<Integer, Set<Long>> levelCaches = new ConcurrentHashMap<>();
        
        /**
         * 
         * @param splitters should all belong to the same NiDimension (but might be different levels)
         */
        CachingIdsAcceptor(Map<Integer, Set<Long>> splitterCells) {
            this.splitterCells = splitterCells;
        }
        
        @Override
        public boolean isAcceptable(Coordinate cellCoos) {
            Set<Long> acceptables = levelCaches.computeIfAbsent(getLevelOf(cellCoos), this::buildAcceptableNeighbours);
            return acceptables.contains(cellCoos.id);
        }
        
        protected Set<Long> buildAcceptableNeighbours(int targetLevel) {
            Set<Long> res = new HashSet<>();
            for(Map.Entry<Integer, Set<Long>> splitterLevel:splitterCells.entrySet())
                res.addAll(getAcceptableNeighbours(splitterLevel.getKey(), splitterLevel.getValue(), targetLevel));
            return res;
        }
        
        @Override
        public String toString() {
            return String.format("caching acceptor on %s: %s", splitterCells.toString(), levelCaches.toString());
        }
    }
}
