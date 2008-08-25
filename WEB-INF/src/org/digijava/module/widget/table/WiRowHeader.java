package org.digijava.module.widget.table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.digijava.module.widget.table.util.TableWidgetUtil;

import edu.emory.mathcs.backport.java.util.Collections;

/**
 * Header row of the table widget.
 * overrides some methods from parent to handle header row logic. 
 * This type of row has its own map of cell where keys are column id's
 * and does not delegate cell methods to columns.
 * @author Irakli Kobiashvili
 *
 */
public class WiRowHeader extends WiRow {
	
	private Map<Long, WiCell> cellsByColumnId = new HashMap<Long, WiCell>();
	
	public WiRowHeader(Long pk, Map<Long, WiColumn> colsByIds) {
		super(pk,colsByIds);
		for (WiColumn col : colsByIds.values()) {
			WiCell hCell = TableWidgetUtil.newHeaderCell(col);
			this.cellsByColumnId.put(hCell.getColumn().getId(), hCell);
			//not useful but may be, if we change PK generation for header rows. 
			hCell.setPk(this.getPk());
		}
	}
	
	@Override
	public WiCell getCell(Long columnId) {
		return cellsByColumnId.get(columnId);
	}

	@Override
	public List<WiCell> getCells() {
		List<WiCell> allCells = new ArrayList<WiCell>(cellsByColumnId.values());
		Collections.sort(allCells,new TableWidgetUtil.WiCellColumnOrderComparator());
		return allCells;
	}

	@Override
	public void updateCell(WiCell cell) {
		cellsByColumnId.put(cell.getColumn().getId(),cell);
	}

}
