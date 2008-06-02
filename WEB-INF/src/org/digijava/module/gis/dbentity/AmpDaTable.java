package org.digijava.module.gis.dbentity;

import java.io.Serializable;
import java.util.Set;

/**
 * table widget entity.
 * @author Irakli Kobiashvili
 *
 */
public class AmpDaTable implements Serializable{

	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	private String code;
	private String cssClass;
	private String htmlStyle;
	private String width;
	private Set<AmpDaColumn> columns;
	private Set<AmpDaWidgetPlace> places;
	
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
	public Set<AmpDaColumn> getColumns() {
		return columns;
	}
	public void setColumns(Set<AmpDaColumn> columns) {
		this.columns = columns;
	}
	public Set<AmpDaWidgetPlace> getPlaces() {
		return places;
	}
	public void setPlaces(Set<AmpDaWidgetPlace> places) {
		this.places = places;
	}
	
}
