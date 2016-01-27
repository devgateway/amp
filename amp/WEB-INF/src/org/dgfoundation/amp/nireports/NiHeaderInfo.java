package org.dgfoundation.amp.nireports;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.dgfoundation.amp.nireports.runtime.CellColumn;
import org.dgfoundation.amp.nireports.runtime.Column;
import org.dgfoundation.amp.nireports.runtime.GroupColumn;

/**
 * the header layout of a NiReport
 * @author Dolghier Constantin
 *
 */
public class NiHeaderInfo {
	public final GroupColumn rootColumn; // the root headers
	public final List<CellColumn> leafColumns;
	public final int nrHierarchies;
	
	/**
	 * [i] = columns which start on row i of the header, SortedMap<startingColumn, Column> 
	 * 
	 */
	public final List<SortedMap<Integer, Column>> rasterizedHeaders;
	
	public NiHeaderInfo(GroupColumn rootColumn, int nrHierarchies) {
		this.rootColumn = rootColumn;
		this.leafColumns = rootColumn.getLeafColumns();
		this.nrHierarchies = nrHierarchies;
		rootColumn.calculateHeaders();
		this.rasterizedHeaders = Collections.unmodifiableList(buildRasterizedHeaders(rootColumn));
	}
	
	public List<SortedMap<Integer, Column>> buildRasterizedHeaders(GroupColumn rootColumn) {
		List<SortedMap<Integer, Column>> rh = new ArrayList<>();
		for(int i = 0; i < rootColumn.getReportHeaderCell().totalRowSpan; i++) {
			List<Column> cells = rootColumn.getChildrenStartingAtDepth(i);
			SortedMap<Integer, Column> maps = new TreeMap<>();
			for(Column col:cells)
				maps.put(col.getReportHeaderCell().startColumn, col);
			rh.add(maps);
		}
		return rh;
	}
	
	public List<CellColumn> getLeafColumns() {
		return leafColumns;
	}
}
