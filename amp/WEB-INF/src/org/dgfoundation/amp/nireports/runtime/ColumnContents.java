package org.dgfoundation.amp.nireports.runtime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.dgfoundation.amp.nireports.Cell;


/**
 * the contents of a column inside of a {@link CellColumn} <br />
 * it is mutable, because it is used during report generation
 * @author Dolghier Constantin
 *
 */
public class ColumnContents<K extends Cell> {
	public Map<Long, List<? extends K>> data = new HashMap<>();
	
	public ColumnContents(Map<Long, ? extends List<? extends K>> data) {
		Objects.requireNonNull(data);
		this.data.putAll(data);
	}
	
	public ColumnContents(List<? extends K> items) {
		this(items.stream().collect(Collectors.groupingBy(z -> z.activityId)));
	}
	
	public int countCells() {
		int sum = 0;
		for(List<? extends K> l:data.values())
			sum += l.size();
		return sum;
	}
	
	/**
	 * expensive!
	 * @return
	 */
	public List<K> getLinearData() {
		List<K> res = new ArrayList<>();
		for(List<? extends K> list:data.values())
			res.addAll(list);
		return res;
	}
	
	@Override
	public String toString() {
		return data.toString();
	}
	
}
