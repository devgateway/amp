package org.digijava.module.widget.dbentity;

import java.io.Serializable;
import java.util.Set;

/**
 * Table widget column entity class.
 * @author Irakli Kobiashvili
 *
 */
public class AmpDaColumn implements Serializable, Comparable<AmpDaColumn>{

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String name;
	private String code;
	private String cssClass;
	private String htmlStyle;
	private String width;
	private String pattern;
	private Integer orderNo;
	private AmpDaTable widget;
	private Integer columnType;
	private Set<AmpDaValue> values;
	
	public Set<AmpDaValue> getValues() {
		return values;
	}
	public void setValues(Set<AmpDaValue> values) {
		this.values = values;
	}
	public AmpDaTable getWidget() {
		return widget;
	}
	public void setWidget(AmpDaTable widget) {
		this.widget = widget;
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
	public Integer getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}
	public int compareTo(AmpDaColumn c) {
		return this.orderNo.compareTo(c.getOrderNo());
	}
	public Integer getColumnType() {
		return columnType;
	}
	public void setColumnType(Integer columnType) {
		this.columnType = columnType;
	}
	
}
