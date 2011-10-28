package org.digijava.module.widget.oldTable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.utils.AmpCollectionUtils;
import org.digijava.module.widget.dbentity.AmpDaColumn;
import org.digijava.module.widget.dbentity.AmpDaValue;
import org.digijava.module.widget.util.TableWidgetUtil;
import org.digijava.module.widget.web.HtmlGenerator;

/**
 * Table row.
 * This is helper class to make easy rendering HTML, 
 * but in db row is represented by PrimaryKey (pk) field.
 * @author Irakli Kobiashvili
 *
 */
public class DaRow implements HtmlGenerator{

	private Long id;
	private Long pk;
	private String cssClass;
	private List<DaCell> cells;
	private boolean isHeader;
	private DaTable parentTable;
	private Map<Long, DaCell> cellsByColumns;
	
	public DaRow(){
		this.isHeader=false;
	}

	/**
	 * Creates row from columns.
	 * This will result in header row bean.
	 * @param columns
	 */
	public DaRow(Collection<AmpDaColumn> columns){
		this.isHeader = true;
		cells = new ArrayList<DaCell>(); 
		for (AmpDaColumn col : columns) {
			DaCell cell = new DaCell(col);
			cells.add(cell);
		}
	}

	/**
	 * Constructor from header or any other row.
	 * uses only column ID's of the row passed and create same number or cells for itself.
	 * @param row reference row, usually header row create from {@link AmpDaColumn}
	 */
	public DaRow(DaRow row){
		Collection<DaCell> columnCells=row.getCells();
		for (DaCell colCell : columnCells) {
			if (cellsByColumns==null){
				cellsByColumns = new HashMap<Long, DaCell>();
			}
			DaCell emptyCell = new DaCell();
			emptyCell.setColumnId(colCell.getColumnId());
			emptyCell.setRowPk(this.pk);
			cellsByColumns.put(colCell.getColumnId(), emptyCell);
		}
	}
	
	public void addCell(DaCell cell){
		cellsByColumns.put(cell.getColumnId(), cell);
	}
	
	/**
	 * Creates and returns list of value db beans from cells of this row.
	 * Map of column db beans, where keys are ID's of the beans, is required for this conversion.
	 * @param columnMap
	 * @return
	 * @see AmpCollectionUtils#createMap(Collection, org.dgfoundation.amp.utils.AmpCollectionUtils.KeyResolver)
	 * @see TableWidgetUtil.TableWidgetColumnKeyResolver
	 */
	public List<AmpDaValue> getValues(Map<Long,AmpDaColumn> columnMap){
		if (cells == null) return null;
		List<AmpDaValue> values = new ArrayList<AmpDaValue>(cells.size());
		for (DaCell cell : cells) {
			AmpDaValue value=cell.toValue(columnMap);
			value.setPk(this.pk);
			values.add(value);
		}
		return values;
	}
	
	public String generateHtml() {
		String result="<TR>";
		
		if (cells!=null){
			for (DaCell cell : cells) {
				result+=cell.generateHtml();
			}
		}else{
			result+="<TD/>";
		}
		
		result+="</TR>";
		return result;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPk() {
		return pk;
	}

	public void setPk(Long pk) {
		this.pk = pk;
	}

	public String getCssClass() {
		return cssClass;
	}

	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}

	public List<DaCell> getCells() {
		return cells;
	}

	public void setCells(List<DaCell> cells) {
		this.cells = cells;
	}

	public boolean isHeader() {
		return isHeader;
	}

	public void setHeader(boolean isHeader) {
		this.isHeader = isHeader;
	}
	
	public DaCell getCells(int index){
		return (cells==null)?null:cells.get(index);
	}

	public DaTable getParentTable() {
		return parentTable;
	}

	public void setParentTable(DaTable parentTable) {
		this.parentTable = parentTable;
	}
	
	//=====These methods are for old struts to submit directly in these beans, note differences in names from normal methods.
	public List getCell() {
		return cells;
	}
	public void setCell(List cells) {
		this.cells = cells;
	}
	public DaCell getCell(int index){
		return (cells==null)?null:cells.get(index);
	}

}
