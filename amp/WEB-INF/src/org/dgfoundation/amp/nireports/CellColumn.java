package org.dgfoundation.amp.nireports;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.algo.AmpCollections;

import com.google.common.base.Function;

/**
 * 
 * @author Dolghier Constantin
 *
 */
public class CellColumn extends Column {
	
	protected final Map<Long, List<Cell>> items;
	
	public CellColumn(String name, Map<Long, List<Cell>> items) {
		super(name);
		this.items = new HashMap<>(items);
	}
	
	public CellColumn(String name, List<Cell> items) {
		this(name, AmpCollections.distribute(items, Cell.TO_ACTIVITY_ID));
	}
	
	public Map<Long, List<Cell>> getItems() {
		return Collections.unmodifiableMap(items);
	}
	
	/**
	 * expensive!
	 * @return
	 */
	public List<Cell> getLinearItems() {
		List<Cell> res = new ArrayList<>();
		for(List<Cell> list:items.values())
			res.addAll(list);
		return res;
	}
}
