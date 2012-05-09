package org.digijava.module.widget.oldTable;

import java.util.Map;

import org.digijava.module.widget.dbentity.AmpDaColumn;
import org.digijava.module.widget.dbentity.AmpDaValue;
import org.digijava.module.widget.web.HtmlGenerator;

/**
 * Html table cell.
 * @author Irakli Kobiashvili
 *
 */
public class DaCell implements HtmlGenerator {

	private Long id;
	private Long columnId;
	private Integer columnOrderNo;
	private Long rowPk;
	private String value;
	private String cssClass;
	private String htmlStyle;
	private String colorCodeText;
	private String colorCodeBackground;
	private boolean isHeader;
	private DaRow row;
	private DaColumn column;
	
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
	 * @param value
	 */
	public DaCell(AmpDaValue value){
		this.value = value.getValue();
		this.id = value.getId();
		this.columnId = value.getColumn().getId();
		this.columnOrderNo = value.getColumn().getOrderNo();
		this.rowPk = value.getPk();
		this.isHeader = false;
	}
	
	/**
	 * Converts helper cell back to db Value bean
	 * @return
	 */
	public AmpDaValue toValue(Map<Long,AmpDaColumn> columnsMap){
		AmpDaValue value=new AmpDaValue();
		value.setId(this.id);
		value.setValue(this.value);
		value.setColumn(columnsMap.get(this.columnId));
		value.setPk(this.rowPk);
		return value;
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

	public Long getRowPk() {
		return rowPk;
	}

	public void setRowPk(Long rowPk) {
		this.rowPk = rowPk;
	}

	public String getColorCodeText() {
		return colorCodeText;
	}

	public void setColorCodeText(String colorCodeText) {
		this.colorCodeText = colorCodeText;
	}

	public String getColorCodeBackground() {
		return colorCodeBackground;
	}

	public void setColorCodeBackground(String colorCodeBackground) {
		this.colorCodeBackground = colorCodeBackground;
	}

	public void setRow(DaRow row) {
		this.row = row;
	}

	public DaRow getRow() {
		return row;
	}

	public void setColumn(DaColumn column) {
		this.column = column;
	}

	public DaColumn getColumn() {
		return column;
	}

}
