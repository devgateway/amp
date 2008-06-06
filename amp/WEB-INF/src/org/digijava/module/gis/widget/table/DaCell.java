package org.digijava.module.gis.widget.table;

import org.digijava.module.gis.dbentity.AmpDaColumn;
import org.digijava.module.gis.dbentity.AmpDaValue;

/**
 * Html table cell.
 * @author Irakli Kobiashvili
 *
 */
public class DaCell implements HtmlGenerator {

	private Long id;
	private Long columnId;
	private Integer columnOrderNo;
	private String value;
	private String cssClass;
	private String htmlStyle;
	private boolean isHeader;
	
	/**
	 * Default constructor
	 */
	public DaCell(){
		this.isHeader=false;
	}
	
	/**
	 * Constructs header cell.
	 * Because column beans go in header.
	 * @param col
	 */
	public DaCell(AmpDaColumn col){
		this.value = col.getName();
		this.cssClass = col.getCssClass();
		this.htmlStyle = col.getHtmlStyle();
		this.columnId = col.getId();
		this.columnOrderNo = col.getOrderNo();
		this.isHeader = true;
	}

	/**
	 * Constructs data cell.
	 * @param cell
	 */
	public DaCell(AmpDaValue cell){
		this.value = cell.getValue();
		this.id = cell.getId();
		this.columnId = cell.getColumn().getId();
		this.columnOrderNo = cell.getColumn().getOrderNo();
		this.isHeader = false;
	}
	
	public String generateHtml() {
		String result="<TD>";
		
		if (value!=null){
			result+=value;
		}else{
			result+="&nbsp;";
		}
		
		result+="</TD>";
		return result;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getValue() {
		return value;
	}


	public void setValue(String value) {
		this.value = value;
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

	public boolean isHeader() {
		return isHeader;
	}

	public void setHeader(boolean isHeader) {
		this.isHeader = isHeader;
	}

	public Long getColumnId() {
		return columnId;
	}

	public void setColumnId(Long columnId) {
		this.columnId = columnId;
	}

	public Integer getColumnOrderNo() {
		return columnOrderNo;
	}

	public void setColumnOrderNo(Integer columnOrderNo) {
		this.columnOrderNo = columnOrderNo;
	}

}
