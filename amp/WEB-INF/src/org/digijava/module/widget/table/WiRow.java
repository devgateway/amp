package org.digijava.module.widget.table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.digijava.module.widget.table.util.TableWidgetUtil;
import org.digijava.module.widget.web.HtmlGenerator;

import edu.emory.mathcs.backport.java.util.Collections;

/**
 * Table widget row.
 * Each row has only map of columns by their db ID.
 * When row is asked for cells, it just delegates request to specific or all columns.
 * @author Irakli Kobiashvili
 *
 */
public abstract class WiRow implements HtmlGenerator{

	private Long pk;
	private Map<Long, WiColumn> columns = new HashMap<Long, WiColumn>();

	public WiRow(Long pk){
		this.pk = pk;
	}
	/**
	 * Constructs row from PK of the row and column map.
	 * @param pk
	 * @param colsByIds
	 */
	public WiRow(Long pk,Map<Long, WiColumn> colsByIds){
		this.pk = pk;
		this.columns = colsByIds;
	}
	
	/**
	 * Return all cells in row, ordered by column order numbers.
	 * @return list of WeCell beans.
	 */
	public List<WiCell> getCells() {
		List<WiCell> result = new ArrayList<WiCell>(columns.size());
		for (WiColumn column : this.columns.values()) {
			WiCell cell = column.getCell(this.pk);
			if (cell==null){
				cell = new WiCellStandard();
			}
			result.add(cell);
		}
		Collections.sort(result,new TableWidgetUtil.WiCellColumnOrderComparator());
		return result;
	}

	/**
	 * Generates TR tag for this row.
	 */
	public String generateHtml() {
		StringBuffer result = new StringBuffer("\t<TR");
		result.append(">\n");
		List<WiCell> cells = getCells();
		for (WiCell cell : cells) {
			result.append(cell.generateHtml());
		}
		result.append("\t</TR>\n");
		return result.toString();
	}
	
	/**
	 * Update cell in specified column.
	 * @param cell new cell reference
	 * @param columnId column id where cell should be  changed with new one
	 */
	public void updateCell(WiCell cell,Long columnId){
		WiColumn col = columns.get(columnId);
		col.setCell(cell);
		cell.setPk(this.pk);
	}
	
	/**
	 * Returns cell from specified column in this row.
	 * @param columnId db id of the column.
	 * @return WiCell bean.
	 */
	public WiCell getCell(Long columnId){
		return columns.get(columnId).getCell(this.pk);
	}

	public Long getPk() {
		return pk;
	}
	
}
