package org.digijava.module.widget.table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.digijava.kernel.exception.DgException;
import org.digijava.module.widget.Widget;
import org.digijava.module.widget.dbentity.AmpDaColumn;
import org.digijava.module.widget.dbentity.AmpDaTable;
import org.digijava.module.widget.table.util.TableWidgetUtil;

import edu.emory.mathcs.backport.java.util.Collections;

/**
 * Table widget. To create new instance use Builder inner class like this:
 * <code>WiTable table = new WiTable.TableBuilder(dbTable.getId()).name("Some Name").showTitle(true).build();</code>
 * This is some kind of constructor but you decide to specify or not any parameter by chaining method on builder. 
 * Last method should be build(). This builder uses other utils to build widget table. To build from db record session which loaded
 * AmpDaTable entity should not be closed by utility methods used in this process, otherwise LazyInitializationExeption will be thrown. 
 * Beside building widget from db record, it also can generate HTML, XML (not finished yet) and convert its state back to DB Record.
 * This class is not thread safe.
 * @author Irakli Kobiashvili
 *
 */
public class WiTable extends Widget{

	private String code;
	private String cssClass;
	private String htmlStyle;
	private String width;
	private boolean showTitle;
	private boolean showHeaderRows;
	private boolean showFooterRows;
	private boolean showPagination;
	private boolean nameAsTitle;
	private boolean dataEntryMode = false;
	private Integer currentPage;
	private List<WiRow> headerRows;
	private List<WiRow> dataRows;
	private List<WiRow> footerRows;
	private List<WiColumn> columns;
	
	private WiTable(TableBuilder builder){
		this.setId(builder.tableId);
		this.setName(builder.name);
		this.code = builder.code;
		this.cssClass = builder.cssClass;
		this.htmlStyle = builder.htmlStyle;
		this.width = builder.width;
		this.headerRows = builder.headerRows;
		this.dataRows = builder.dataRows;
		this.footerRows = builder.footerRows;
		this.columns = builder.columns;
		this.showFooterRows = builder.showFooterRows;
		this.showHeaderRows = builder.showHeaderRows;
		this.showPagination = builder.showPagination;
		this.showTitle = builder.showTitle;
		this.nameAsTitle = builder.nameAsTitle;
		this.dataEntryMode = builder.dataEntryMode;
	}
	
	/**
	 * Table widget builder.
	 * Initial idea was to have immutable WiTable, but currently this is not done, so such approach is not very useful.
	 * @author Irakli Kobiashvili
	 *
	 */
	public static class TableBuilder {
		private Long tableId = 0L;
		private String name = "no name";
		private String code = null;
		private String cssClass = null;
		private String htmlStyle = null;
		private String width = null;
		private boolean showTitle = true;
		private boolean showHeaderRows = true;
		private boolean showFooterRows = true;
		private boolean showPagination = false;
		private boolean nameAsTitle = true;
		private boolean dataEntryMode = false;
		private List<WiRow> dataRows = new ArrayList<WiRow>();
		private List<WiRow> headerRows = new ArrayList<WiRow>();
		private List<WiRow> footerRows = new ArrayList<WiRow>();
		private List<WiColumn> columns = new ArrayList<WiColumn>();
		private Map<Long,WiColumn> columnsById=new HashMap<Long,WiColumn>();
		
		/**
		 * Use this constructor to build new empty table with default values set and empty but not null collections.
		 * @throws DgException
		 */
		public TableBuilder() throws DgException{
		}

		/**
		 * Use this to build from db entity.
		 * @param table
		 */
		public TableBuilder(AmpDaTable table){
			buildFromDbTable(table);
		}
		
		/**
		 * Use this one to build from db.
		 * @param tableId
		 * @throws DgException
		 */
		public TableBuilder(Long tableId) throws DgException{
			this.tableId = tableId;
			buildFromDbTable(LoadFromDb(tableId));
		}

		/**
		 * Loads table from db. Change this to change method of retrieving table from db. 
		 * @param tableId
		 * @return
		 * @throws DgException
		 */
		private AmpDaTable LoadFromDb(Long tableId) throws DgException{
			return TableWidgetUtil.getDbTable(tableId);
		}
		
		/**
		 * Builds table from db entity.
		 * @param dbTable
		 */
		private void buildFromDbTable(AmpDaTable dbTable){
			this.tableId = dbTable.getId();
			this.name = dbTable.getName();
			this.code = dbTable.getCode();
			this.cssClass = dbTable.getCssClass();
			this.htmlStyle = dbTable.getHtmlStyle();
			this.nameAsTitle = (dbTable.getNameAsTitle()!=null)?dbTable.getNameAsTitle().booleanValue():true;
			this.width = dbTable.getWidth();
			Set<AmpDaColumn> dbColumns = dbTable.getColumns();
			for (AmpDaColumn dbColumn : dbColumns) {
				WiColumn column = TableWidgetUtil.newColumn(dbColumn);
				this.columns.add(column);
				this.columnsById.put(column.getId(), column);
			}
			Collections.sort(columns,new TableWidgetUtil.WiColumnOrderComparator());

			//default header. later we may add complex headers, for example two rows in headers and with special cells.
			WiRowHeader headerRow = new WiRowHeader(0L,columnsById);
			//data rows
			Map<Long, WiRow> rowsByPk = new HashMap<Long, WiRow>();
			for (WiColumn column : columns) {
				List<WiCell> cells = column.getAllCells();
				for (WiCell cell : cells) {
					WiRow row = rowsByPk.get(cell.getPk());
					if (row ==null){
						row = new WiRowStandard(cell.getPk(),columnsById);
						rowsByPk.put(row.getPk(), row);
						this.dataRows.add(row);
					}
					row.updateCell(cell);
				}
				//set up header
//				WiCell hCell = TableWidgetUtil.newHeaderCell(column);
//				headerRow.updateCell(hCell);
			}
			Collections.sort(dataRows,new TableWidgetUtil.WiRowPkComparator());
			this.headerRows.add(headerRow);
			
		}

		public TableBuilder name(String name){
			this.name = name;
			return this;
		}
		public TableBuilder code(String code){
			this.code = code;
			return this;
		}
		public TableBuilder cssClass(String className){
			this.cssClass = className;
			return this;
		}
		public TableBuilder htmlStyle(String style){
			this.htmlStyle = style;
			return this;
		}
		public TableBuilder width(String width){
			this.width = width;
			return this;
		}
		public TableBuilder showTitle(boolean show){
			this.showTitle = show;
			return this;
		}
		public TableBuilder showHeaderRows(boolean show){
			this.showHeaderRows = show;
			return this;
		}
		public TableBuilder showFooterRows(boolean show){
			this.showFooterRows = show;
			return this;
		}
		public TableBuilder nameAsTitle(boolean asTitle){
			this.nameAsTitle = asTitle;
			return this;
		}
		public TableBuilder dataEntryMode(boolean dataEntry){
			this.dataEntryMode = dataEntry;
			return this;
		}
		public TableBuilder dataRows(List<WiRow> rows){
			this.dataRows = rows;
			return this;
		}
		public TableBuilder headerRows(List<WiRow> rows){
			this.headerRows = rows;
			return this;
		}
		public TableBuilder footerRows(List<WiRow> rows){
			this.footerRows = rows;
			return this;
		}
		
		public WiTable build() {
			WiTable table = new WiTable(this);
			return table;
		}

	}
	
	public List<WiRow> getHeaderRows() {
		return headerRows;
	}

	public void setHeaderRows(List<WiRow> headerRows) {
		this.headerRows = headerRows;
	}

	public List<WiRow> getDataRows() {
		return dataRows;
	}

//	public void setDataRows(List<WiRow> dataRows) {
//		this.dataRows = dataRows;
//	}
//
	public List<WiRow> getFooterRows() {
		return footerRows;
	}

	public void setFooterRows(List<WiRow> footerRows) {
		this.footerRows = footerRows;
	}

	public boolean isShowTitle() {
		return showTitle;
	}

	public void setShowTitle(boolean showTitle) {
		this.showTitle = showTitle;
	}

	public boolean isShowHeaderRows() {
		return showHeaderRows;
	}

	public void setShowHeaderRows(boolean showHeaderRow) {
		this.showHeaderRows = showHeaderRow;
	}

	public boolean isShowPagination() {
		return showPagination;
	}

	public void setShowPagination(boolean showPagination) {
		this.showPagination = showPagination;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public String generateHtml() {
		StringBuffer result = new StringBuffer("<TABLE");
		//TODO add parameters
		result.append(">\n");
		if (showHeaderRows){
			for (WiRow row : headerRows) {
				result.append(row.generateHtml());
			}
		}
		for (WiRow row : dataRows) {
			result.append(row.generateHtml());
		}
		if (showFooterRows){
			for (WiRow row : footerRows) {
				result.append(row.generateHtml());
			}
		}
		result.append("</TABLE>\n");
		return result.toString();
	}

	public boolean isShowFooterRows() {
		return showFooterRows;
	}

	public void setShowFooterRows(boolean showFooterRows) {
		this.showFooterRows = showFooterRows;
	}

	public void setColumns(List<WiColumn> columns) {
		this.columns = columns;
	}

	public List<WiColumn> getColumns() {
		return columns;
	}

	public String getCssClass() {
		return cssClass;
	}

	public String getHtmlStyle() {
		return htmlStyle;
	}

	public boolean isNameAsTitle() {
		return nameAsTitle;
	}
	public String getCode() {
		return code;
	}
	public String getWidth() {
		return width;
	}

	public boolean isDataEntryMode() {
		return dataEntryMode;
	}


}
