package org.dgfoundation.amp.nireports.amp.diff;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.dgfoundation.amp.nireports.Cell;

/**
 * a differential cache of cells which can update in batches of ids
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
	
	public Set<Long> getCachedEntityIds() {
		return this.cellsByOwner.keySet();
	}
}
