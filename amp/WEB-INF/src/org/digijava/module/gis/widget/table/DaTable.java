package org.digijava.module.gis.widget.table;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.digijava.kernel.exception.DgException;
import org.digijava.module.gis.dbentity.AmpDaTable;
import org.digijava.module.gis.dbentity.AmpDaValue;
import org.digijava.module.gis.util.TableWidgetUtil;

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
	private Collection<DaRow> headerRows;
	private Collection<DaRow> dataRows;
	
	public DaTable(){
		
	}
	
	/**
	 * Sets up HTML table helper from db table entity.
	 * Note that session loaded entity should not be closed to let this constructor
	 * load rows and cells from db.
	 * @param entity
	 */
	public DaTable(AmpDaTable entity){
		if (entity!=null){
			this.id=entity.getId();
			this.name=entity.getName();
			this.code=entity.getCode();
			this.cssClass=entity.getCssClass();
			this.htmlStyle=entity.getHtmlStyle();
			this.width = entity.getWidth();
			if (null != entity.getColumns() && entity.getColumns().size() > 1){
				headerRows = new HashSet<DaRow>();
				headerRows.add(new DaRow(entity.getColumns()));
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

	
}
