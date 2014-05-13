package org.digijava.module.widget.table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.digijava.module.widget.table.util.TableWidgetUtil;
import org.digijava.module.widget.web.HtmlGenerator;

import java.util.Collections;

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
	private WiTable table;

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
		//ask each known column to return cells for my pk
		for (WiColumn column : this.columns.values()) {
			WiCell cell = column.getCell(this.pk);
			//if column does not have cell for my pk
//			if (cell==null){
//				cell = TableWidgetUtil.newCell(column);
//				cell.setPk(this.pk);
//				cell.setColumn(column);
//				column.setCell(cell);
//			}
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
		//using getCells() guarantees that this method will work for subclasses too, for example for WiRowHeader
		List<WiCell> cells = getCells();
		for (WiCell cell : cells) {
			result.append(cell.generateHtml());
		}
		result.append("\t</TR>\n");
		return result.toString();
	}
	
	/**
	 * Update cell of this row and column referenced by cell 'column' property.
	 * @param cell new cell reference
	 */
	public void updateCell(WiCell cell){
		WiColumn col = columns.get(cell.getColumn().getId());
		col.setCell(cell);
		cell.setColumn(col);
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
	public WiCell getCell(int columnId){
		return columns.get(new Long(columnId)).getCell(this.pk);
	}

	public Long getPk() {
		return pk;
	}

	public void setPk(Long newPk) {
		Long oldPk = this.pk;
		this.pk =  newPk;
		for (WiColumn col : this.columns.values()) {
			col.replacePk(oldPk, newPk);
		}
	}
	public void setTable(WiTable table) {
		this.table = table;
	}
	public WiTable getTable() {
		return table;
	}
	
}
