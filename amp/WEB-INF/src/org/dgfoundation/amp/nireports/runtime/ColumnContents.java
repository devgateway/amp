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
public class ColumnContents {
	public Map<Long, List<NiCell>> data = new HashMap<>();
	
	public ColumnContents(Map<Long, ? extends List<NiCell>> data) {
		Objects.requireNonNull(data);
		this.data.putAll(data);
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
	
	@Override
	public String toString() {
		return data.toString();
	}
	
}
