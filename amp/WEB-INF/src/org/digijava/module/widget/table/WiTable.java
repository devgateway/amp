package org.digijava.module.widget.table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.digijava.kernel.exception.DgException;
import org.digijava.module.widget.Widget;
import org.digijava.module.widget.dbentity.AmpDaColumn;
import org.digijava.module.widget.dbentity.AmpDaTable;
import org.digijava.module.widget.table.util.TableWidgetUtil;
import org.hibernate.Session;

import java.util.Collections;

/**
 * Table widget. To create new instance use Builder inner class like this:
 * <code>WiTable table = new WiTable.TableBuilder(dbTable.getId()).name("Some Name").showTitle(true).build();</code>
 * This is some kind of constructor but you decide to specify or not any parameter by chaining method on builder. 
 * Last method should be build(). This builder uses other utils to build widget table. To build from db record session which loaded
 * AmpDaTable entity should not be closed by utility methods used in this process, otherwise LazyInitializationExeption will be thrown. 
 * Beside building widget from db record, it also can generate HTML, XML (not finished yet) and convert its state back to DB Record.
 * This class is not thread safe.
 * TODO try to make thread safe, apply lock in all methods that can change state, also think about reference leak, because class gives away references of cells and columns.
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
	private Map<Long,WiColumn> columnsById = null;
	private Map<Long,WiRow> dataRowsByPk = null;
	private HttpServletRequest httpReqest;
	
	private WiTable(TableBuilder builder){
		builder.tableProxy.setTable(this);
		
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
		this.columnsById = builder.columnsById;
		this.dataRowsByPk = builder.dataRowsByPk;
		this.httpReqest = builder.httpReqest;
		
		Collections.sort(this.columns,new TableWidgetUtil.WiColumnOrderComparator());
	}
	
	/**
	 * Table widget builder.
	 * Initial idea was to have immutable WiTable, but currently this is not done, so such approach is not very useful.
	 * @author Irakli Kobiashvili
	 *
	 */
	public static class TableBuilder {
		private TableProxy tableProxy = null;
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
		private Map<Long, WiRow> dataRowsByPk = new HashMap<Long, WiRow>();
		private HttpServletRequest httpReqest = null;
		
		/**
		 * Use this constructor to build new empty table with default values set and empty but not null collections.
		 * @throws DgException
		 */
		public TableBuilder() throws DgException{
			this.tableProxy = new TableProxy();
		}

		/**
		 * Use this to build from db entity.
		 * @param table
		 */
		public TableBuilder(AmpDaTable table){
			this.tableProxy = new TableProxy();
			buildFromDbTable(table);
		}
		
		/**
		 * Use this one to build from db.
		 * @param tableId
		 * @throws DgException
		 */
		public TableBuilder(Long tableId) throws DgException{
			this.tableProxy = new TableProxy();
			this.tableId = tableId;
			buildFromDbTable(LoadFromDb(tableId));
		}

		/**
	     * Use this one to build from db.
         * @param tableId
         * @throws DgException
         */
        public TableBuilder(Long tableId, HttpServletRequest request) throws DgException{
                this.tableProxy = new TableProxy();
                this.tableId = tableId;
                this.httpReqest = request;
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
				WiColumn column = TableWidgetUtil.newColumn(dbColumn,this.tableProxy);
				this.columns.add(column);
				this.columnsById.put(column.getId(), column);
			}
			Collections.sort(columns,new TableWidgetUtil.WiColumnOrderComparator());

			//default header. later we may add complex headers, for example two rows in headers and with special cells.
			WiRowHeader headerRow = new WiRowHeader(0L,columnsById);
			//data rows
			for (WiColumn column : columns) {
				List<WiCell> cells = column.getAllCells();
				for (WiCell cell : cells) {
					WiRow row = dataRowsByPk.get(cell.getPk());
					if (row ==null){
						row = new WiRowStandard(cell.getPk(),columnsById);
						dataRowsByPk.put(row.getPk(), row);
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
		public TableBuilder httpRequest(HttpServletRequest request){
			this.httpReqest = request;
			return this;
		}
		
		public WiTable build() {
			WiTable table = new WiTable(this);
			return table;
		}

	}
	
	public static class TableProxy{
		private WiTable table = null;

		public synchronized void setTable(WiTable table) {
			this.table = table;
		}

		public synchronized WiTable getTable() {
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
		StringBuffer result = new StringBuffer("<TABLE id='tableWidget");
		result.append(this.getId());
		result.append("' border='1' ");
		if (this.htmlStyle!=null && !this.htmlStyle.trim().equals("")){
			result.append(" style='");
			result.append(this.htmlStyle);
			result.append("' ");
		}else if (this.cssClass!=null && !this.htmlStyle.trim().equals("")){
			result.append(" class='");
			result.append(this.cssClass);
			result.append("' ");
		}
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
	
	/**
	 * Adds row at specified position.
	 * Also rebuilds pk map of rows. All add methods of rows call this one. 
	 * @param rowIndex
	 * @param newRow
	 * @return
	 */
	public WiRow addDataRowAt(Long rowIndex,WiRow newRow){
		this.dataRows.add(rowIndex.intValue(), newRow);
		rebuildDataRowsMap();
		return newRow;
	}
	
	/**
	 * Adds row to the end of the table.
	 * @param row 
	 * @return
	 */
	public WiRow appendRow(WiRow row){
		row.setPk(getLastPk());
		return this.addDataRowAt(row.getPk(), row);
	}

	/**
	 * Adds new empty autogenerated row to the table at specified place.
	 * @param index this actually pk of the row.
	 * @return new row object that was added.
	 */
	public WiRow addNewRowAt(long index){
		WiRow newRow = createNewDataRow();
		newRow.setPk(new Long(index));
		return this.addDataRowAt(newRow.getPk(), newRow);
	}
	
	/**
	 * Appends new empty autogenerated row at the end of the table.
	 * @return the new row object that was added to the table 
	 */
	public WiRow appendNewRow(){
		WiRow newRow = createNewDataRow(); 
		newRow.setPk(getLastPk());
		return this.addDataRowAt(newRow.getPk(), newRow);
	}

	/**
	 * Removes row from table.
	 * Row is specified by PK and cells of the row will be also removed
	 * @param rowPk pk of the row to remove
	 * @return
	 */
	public WiRow deleteDataRow(long rowPk){
		WiRow deletedRow = dataRowsByPk.get(new Long(rowPk));
		if (deletedRow != null){
			dataRowsByPk.remove(new Long(rowPk));
			dataRows.remove(deletedRow);
			for (WiColumn col : this.columns) {
                            WiCell trashedCell=col.removeCellWithPk(deletedRow.getPk());
                            col.addTrashedCell(trashedCell);
			}
			rebuildDataRowsMap();
		}
		return deletedRow;
	}
	
	public void saveData(Session dbSession) throws DgException{
		for (WiColumn column : this.columns) {
			column.saveData(dbSession);
		}
	}

	private WiRow createNewDataRow(){
		return TableWidgetUtil.newDataRow(this.columnsById,this);
	}
	
	/**
	 * Returns last available pk for rows.
	 * This method valid only after {@link #rebuildDataRowsMap()} method when all pk's are renumbered.
	 * @return
	 */
	private Long getLastPk(){
		//remember this is not thread safe
		return new Long(this.dataRows.size());
	}
	
	private void rebuildDataRowsMap(){
		long pk=0;
		this.dataRowsByPk = new HashMap<Long, WiRow>(this.dataRows.size());
		for (WiRow row : this.dataRows) {
			row.setPk(new Long(pk++));
			this.dataRowsByPk.put(row.getPk(), row);
		}
		Collections.sort(dataRows,new TableWidgetUtil.WiRowPkComparator());
	}

	public WiRow getDataRowByPk(Long pk){
		return dataRowsByPk.get(pk);
	}

	public WiColumn getColumnById(Long colId){
		return this.columnsById.get(colId);
	}

	public void setHttpReqest(HttpServletRequest httpReqest) {
		this.httpReqest = httpReqest;
	}

	public HttpServletRequest getHttpReqest() {
		return httpReqest;
	}
}
