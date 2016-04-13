package org.dgfoundation.amp.nireports.runtime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.dgfoundation.amp.algo.AmpCollections;
import org.dgfoundation.amp.nireports.output.NiOutCell;
import org.dgfoundation.amp.nireports.schema.Behaviour;

import static org.dgfoundation.amp.algo.AmpCollections.remap;
import static org.dgfoundation.amp.algo.AmpCollections.relist;

/**
 * the contents of a column inside of a {@link CellColumn} <br />
 * it is mutable, because it is used during report generation
 * TODO: refactor given the fact that outputs have been separated
 * @author Dolghier Constantin
 *
 */
public class ColumnContents {
	public Map<Long, List<NiCell>> data = new HashMap<>();
	
	public ColumnContents(Map<Long, ? extends List<NiCell>> data) {
		Objects.requireNonNull(data);
		this.data.putAll(data);
	}
	
	/**
	 * creates an instance which only keeps the lines with the given ids
	 * @param data
	 * @param idsToKeep
	 */
	public ColumnContents(Map<Long, ? extends List<NiCell>> rawData, Set<Long> idsToKeep) {
		Objects.requireNonNull(rawData);
		for(Long id:idsToKeep) {
			List<NiCell> idCells = rawData.get(id);
			if(idCells != null)
				this.data.put(id, idCells);
		}
	}	
	
	public ColumnContents(List<NiCell> items) {
		this(items.stream().collect(Collectors.groupingBy(z -> z.getMainId())));
	}
	
	public int countCells() {
		int sum = 0;
		for(List<NiCell> l:data.values())
			sum += l.size();
		return sum;
	}
	
	/**
	 * expensive!
	 * @return
	 */
	public List<NiCell> getLinearData() {
		List<NiCell> res = new ArrayList<>();
		for(List<NiCell> list:data.values())
			res.addAll(list);
		return res;
	}
	
	public Map<Long, NiOutCell> flatten(Behaviour<?> behaviour) {
		Map<Long, NiOutCell> res = new HashMap<>();
		for(long id:data.keySet()) {
			List<NiCell> cells = data.get(id);
			NiOutCell z = behaviour.doHorizontalReduce(cells);
			res.put(id, z);
		}
		return res;
	}

	/**
	 * MUTABLY removes all ids from the set which are not in a given acceptable set of ids
	 * @param ids
	 */
	public void retainIds(Set<Long> ids) {
		Set<Long> idsToRemove = AmpCollections.difference(data.keySet(), ids);
		for(Long id:idsToRemove)
			data.remove(id);
	}
	
//	public ColumnContents keepEntries(Set<Long> ids) {
//		return new ColumnContents(AmpCollections.keepEntries(data, ids));
//	}
	
	@Override
	public String toString() {
		return data.toString();
	}

	public String debugString() {
		return remap(data, l -> relist(l, NiCell::getCell), null).toString();
	}
	
	public void add(ColumnContents v) {
		v.data.forEach(this::addCells); 
	}
	
	protected void addCells(long id, List<NiCell> cells) {
		this.data.computeIfAbsent(id, z -> new ArrayList<>()).addAll(cells);
	}
}
