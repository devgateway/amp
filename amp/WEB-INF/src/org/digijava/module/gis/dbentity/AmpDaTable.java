package org.digijava.module.gis.dbentity;

import java.util.Set;

/**
 * Table widget entity.
 * @author Irakli Kobiashvili
 * @see AmpWidget
 *
 */
public class AmpDaTable extends AmpWidget{

	private static final long serialVersionUID = 1L;
	private String cssClass;
	private String htmlStyle;
	private String width;
	private Set<AmpDaColumn> columns;
	
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
	public Set<AmpDaColumn> getColumns() {
		return columns;
	}
	public void setColumns(Set<AmpDaColumn> columns) {
		this.columns = columns;
	}
	
}
