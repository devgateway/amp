package org.digijava.module.widget.form;

import java.util.Collection;

import org.apache.struts.action.ActionForm;
import org.digijava.module.widget.dbentity.AmpDaColumn;

/**
 * Form for working with widget columns.
 * @author Irakli Kobiashvili
 *
 */
public class WidgetColumnCreationForm extends ActionForm {

	private static final long serialVersionUID = 1L;
	
	private Long widgetId;
	private Long id;
	private String name;
	private String code;
	private String cssClass;
	private String htmlStyle;
	private String width;
	private String pattern;
	
	private Collection<AmpDaColumn> columns;
	
	public Collection<AmpDaColumn> getColumns() {
		return columns;
	}
	public void setColumns(Collection<AmpDaColumn> columns) {
		this.columns = columns;
	}
	public Long getWidgetId() {
		return widgetId;
	}
	public void setWidgetId(Long widgetId) {
		this.widgetId = widgetId;
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
	public String getPattern() {
		return pattern;
	}
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
}
