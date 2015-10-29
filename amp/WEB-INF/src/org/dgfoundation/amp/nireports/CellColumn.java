package org.dgfoundation.amp.nireports;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.algo.AmpCollections;

import com.google.common.base.Function;

/**
 * a leaf column
 * @author Dolghier Constantin
 *
 */
public class CellColumn extends Column {
	
	protected final Map<Long, List<Cell>> items;
	
	public CellColumn(String name, Map<Long, List<Cell>> items, GroupColumn parent) {
		super(name, parent);
		this.items = new HashMap<>(items);
	}
	
	public CellColumn(String name, List<Cell> items, GroupColumn parent) {
		this(name, AmpCollections.distribute(items, Cell.TO_ACTIVITY_ID), parent);
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
