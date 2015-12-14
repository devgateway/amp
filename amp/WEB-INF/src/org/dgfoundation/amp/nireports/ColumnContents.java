package org.dgfoundation.amp.nireports;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.dgfoundation.amp.algo.AmpCollections;

/**
 * the contents of a column inside of a {@link CellColumn} <br />
 * it is mutable, because it is used during report generation
 * @author Dolghier Constantin
 *
 */
public class ColumnContents {
	public Map<Long, List<Cell>> data = new HashMap<>();
	
	public ColumnContents(Map<Long, List<Cell>> data) {
		Objects.requireNonNull(data);
		this.data = data;
	}
	
	public ColumnContents(List<Cell> items) {
		this(AmpCollections.distribute(items, Cell.TO_ACTIVITY_ID));
	}
	
	/**
	 * expensive!
	 * @return
	 */
	public List<Cell> getLinearData() {
		List<Cell> res = new ArrayList<>();
		for(List<Cell> list:data.values())
			res.addAll(list);
		return res;
	}
	
	@Override
	public String toString() {
		return data.toString();
	}
	
}
