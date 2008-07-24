package org.digijava.module.widget.table;

import java.util.ArrayList;
import java.util.List;

import org.digijava.module.widget.Builder;
import org.digijava.module.widget.Widget;
import org.digijava.module.widget.dbentity.AmpDaTable;

public class WiTable extends Widget{

	private boolean showTitle;
	private boolean showHeaderRows;
	private boolean showFooterRows;
	private boolean showPagination;
	private Integer currentPage;
	private List<WiRow> headerRows;
	private List<WiRow> dataRows;
	private List<WiRow> footerRows;
	private List<WiColumn> columns;
	
	private WiTable(TableBuilder builder){
		this.setId(builder.tableId);
		this.setName(builder.name);
		headerRows = builder.headerRows;
		dataRows = builder.dataRows;
		footerRows = builder.footerRows;
	}
	
	public static class TableBuilder implements Builder<WiTable>{
		private Long tableId = 0L;
		private String name = "no name";
		private List<WiRow> dataRows = new ArrayList<WiRow>();
		private List<WiRow> headerRows = new ArrayList<WiRow>();
		private List<WiRow> footerRows = new ArrayList<WiRow>();
		
		public TableBuilder(Long tableId){
			this.setTableId(tableId);
			LoadFromDb(tableId);
		}
		
		private void LoadFromDb(Long tableId){
			//TODO load table from db.
		}
		
		private void buildFromDb(AmpDaTable dbTable){
			
		}
		
		public TableBuilder name(String name){
			this.name = name;
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

		public void setTableId(Long tableId) {
			this.tableId = tableId;
		}

		public Long getTableId() {
			return tableId;
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

	public void setDataRows(List<WiRow> dataRows) {
		this.dataRows = dataRows;
	}

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

	public Integer getRowsToShow() {
		return rowsToShow;
	}

	public void setRowsToShow(Integer rowsToShow) {
		this.rowsToShow = rowsToShow;
	}

	private Integer rowsToShow;
	
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

}
