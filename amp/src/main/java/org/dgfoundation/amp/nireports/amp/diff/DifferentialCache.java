package org.dgfoundation.amp.nireports.amp.diff;

import org.dgfoundation.amp.nireports.Cell;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * a differential cache of {@link Cell} instances which can update in batches of ids. <br />
 * This class makes a difference between <i>having cells of a given id</i> and <i>having information about cells of a given id</i>. <br /> 
 * In particular, it is possible (and it does happen) to have information about a given id without having any cells with that id. The following states are distinct:
 * <ul>
 *  <li>the fact that {@link #cellsByOwner}[i] is an empty list means that <i>the backing store has no cells</i> with the given id</li>
 *  <li>the fact that {@link #cellsByOwner}[i] is missing (null) means that <i>the cache has no information</i> regarding the cells with the given id</i></li>
 * </ul>
 *  These 2 are semantically different and this is important when, for example, refetching data from the backing store.<br />
 *  This class is thread-safe
 * 
 * @author Dolghier Constantin
 *
 * @param <K>
 */
public class DifferentialCache<K extends Cell> {
    final Map<Long, List<K>> cellsByOwner = new ConcurrentHashMap<Long, List<K>>();
    protected long lastEventId = -1;
    
    public DifferentialCache(long lastEventId) {
        this.lastEventId = lastEventId;
    }
    
    /**
     * runs a minimal-disturbance update (not doing it atomically since it would be expensive in the average case) of the underlying data 
     * @param idsToReplace
     * @param cells
     */
    public synchronized void importCells(Set<Long> idsToReplace, List<K> cells, long eventId) {
        Map<Long, List<K>> imported = new HashMap<>();
        for(long id:idsToReplace)
            imported.put(id, new ArrayList<>());
        for(K cell:cells)
            imported.get(cell.activityId).add(cell);
        this.lastEventId = eventId;
        cellsByOwner.putAll(imported);
    }
    
    /**
     * returns all the stored cells which have their ownerId in a given set
     */
    public List<K> getCells(Set<Long> ownerIds) {
        List<K> res = new ArrayList<>();
        for(long ownerId:ownerIds) {
            List<K> cells = cellsByOwner.get(ownerId);
            if (cells != null && !cells.isEmpty())
                res.addAll(cells);
        }
        return res;
    }
    
    public long getLastEventId() {
        return lastEventId;
    }
    
    /**
     * returns all the ownerIds about which there is information (e.g. cells or information that no cells exist)
     */
    public Set<Long> getCachedEntityIds() {
        return this.cellsByOwner.keySet();
    }
}
