package org.digijava.module.widget.oldTable;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.digijava.kernel.exception.DgException;
import org.digijava.module.widget.dbentity.AmpDaTable;
import org.digijava.module.widget.dbentity.AmpDaValue;
import org.digijava.module.widget.util.TableWidgetUtil;
import org.digijava.module.widget.web.HtmlGenerator;

import edu.emory.mathcs.backport.java.util.Collections;

/**
 * Html table bean.
 * @author Irakli Kobiashvili
 *
 */
public class DaTable implements HtmlGenerator{

	private Long id;
	private String name;
	private String code;
	private String cssClass;
	private String htmlStyle;
	private String width;
	private boolean nameAsTitle;
	private Collection<DaRow> headerRows;
	private DaRow columnsRow;
	private Collection<DaRow> dataRows;
	private Map<Long, DaRow> rows;
	private Map<Long, DaColumn> columns;
	private DaCell[][] data;
	
	public DaTable(){
		
	}
	
	/**
	 * Sets up HTML table helper from db table entity.
	 * Note that session loaded entity should not be closed to let this constructor
	 * load rows and cells from db.
	 * @param entity
	 */
	public DaTable(AmpDaTable entity){
		setup(entity);
	}
	
	public void clear(){
		rows = new HashMap<Long, DaRow>();
		columns = new HashMap<Long, DaColumn>();
	}
	
	public void setup(AmpDaTable table){
		clear();
		
		
		if (table!=null){
			this.id=table.getId();
			this.name=table.getName();
			this.code=table.getCode();
			this.cssClass=table.getCssClass();
			this.htmlStyle=table.getHtmlStyle();
			this.width = table.getWidth();
			this.nameAsTitle = false;
			if (table.getNameAsTitle()!=null){
				this.nameAsTitle = table.getNameAsTitle().booleanValue();
			}
			if (null != table.getColumns() && table.getColumns().size() > 1){
				headerRows = new HashSet<DaRow>();
				headerRows.add(new DaRow(table.getColumns()));
			}
			try {
				List<AmpDaValue> values = TableWidgetUtil.getTableData(this.id);
				List<DaRow> rows = TableWidgetUtil.valuesToRows(values);
				Collections.sort(rows,new TableWidgetUtil.RowPkComparator());
				this.setDataRows(rows);
			} catch (DgException e) {
				e.printStackTrace();
			}
		}
		
		
		
		
//		Set<AmpDaColumn> dbColumns = table.getColumns();
//		if (dbColumns==null){
//			return;
//		}
//		columnsRow = new DaRow(dbColumns);
//		for (AmpDaColumn dbColumn : dbColumns) {
//			Set<AmpDaValue> dbValues = dbColumn.getValues();
//			if (dbValues==null){
//				return;
//			}
//			for (AmpDaValue dbValue : dbValues) {
//				DaRow row = getRowByPk(dbValue.getPk());
//				if (row==null){
//					row = new DaRow(columnsRow);
//					addRow(row);
//				}
//				DaColumn column = getColumn(dbValue.getColumn().getId());
//				if (column == null){
//					column = new DaColumn(dbColumn);
//					addColumn(column);
//				}
//				DaCell cell = new DaCell(dbValue);
//				cell.setRow(row);
//				cell.setColumn(column);
//				
//				row.addCell(cell);
//				column.addCell(cell);
//			}
//		}
	}
	
	public void addRow(DaRow row){
		rows.put(row.getPk(), row);
	}
	public DaRow getRowByPk(Long pk){
		return rows.get(pk);
	}
	public void addColumn(DaColumn column){
		columns.put(column.getId(), column);
	}
	public DaColumn getColumn(Long id){
		return columns.get(id);
	}
	
	public void addCellAt(int x,int y,DaCell cell){
		data[x][y]=cell;
	}
	
	/**
	 * Adds cell to the table.
	 * @param colId column where cell should be added 
	 * @param rowId 
	 * @param cell
	 */
	public void changeCell(Long colId,Long rowId,DaCell cell){
		
	}
	
	
	public String generateHtml() {
		String result = "<TABLE";
		if (this.cssClass!=null){
			result +=" class=\""+this.cssClass+"\"";
		}
		if (this.htmlStyle!=null){
			result +=" style=\""+this.htmlStyle+"\"";
		}
		if (this.width !=null){
			result += " width=\""+this.width+"\"";
		}
		result+=">";
		
		if (null != headerRows){
			for (DaRow row : headerRows) {
				result+=row.generateHtml();
			}
		}

		if (null != dataRows){
			for (DaRow row : dataRows) {
				result+=row.generateHtml();
			}
		}else{
			result+=(new DaRow()).generateHtml();
		}
		
		result+="</TABLE>";
		return result;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCssClass() {
		return cssClass;
	}
	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}
	public String getHtmlStyle() {
		return htmlStyle;
	}
	public void setHtmlStyle(String htmlStyle) {
		this.htmlStyle = htmlStyle;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}

	public Collection<DaRow> getHeaderRows() {
		return headerRows;
	}

	public void setHeaderRows(Collection<DaRow> headerRows) {
		this.headerRows = headerRows;
	}

	public Collection<DaRow> getDataRows() {
		return dataRows;
	}

	public void setDataRows(Collection<DaRow> dataRows) {
		this.dataRows = dataRows;
	}

	public boolean isNameAsTitle() {
		return nameAsTitle;
	}

	public void setNameAsTitle(boolean nameAsTitle) {
		this.nameAsTitle = nameAsTitle;
	}

	public void setColumnsRow(DaRow columnsRow) {
		this.columnsRow = columnsRow;
	}

	public DaRow getColumnsRow() {
		return columnsRow;
	}

	
}
