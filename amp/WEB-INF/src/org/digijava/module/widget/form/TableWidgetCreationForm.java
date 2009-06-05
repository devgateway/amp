package org.digijava.module.widget.form;

import java.util.Collection;
import java.util.List;

import org.apache.struts.action.ActionForm;
import org.apache.struts.util.LabelValueBean;
import org.digijava.module.widget.dbentity.AmpDaColumn;
import org.digijava.module.widget.dbentity.AmpDaTable;

/**
 * Form to create edit etc table widgets.
 * @author Irakli Kobiashvili
 *
 */
public class TableWidgetCreationForm extends ActionForm {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String name;
	private String code;
	private String cssClass;
	private String htmlStyle;
	private String width;
	private Boolean nameAsTitle;
	private boolean shouldClose;
	
	private Collection<AmpDaTable> tables;
	private List<AmpDaColumn> columns;
	private List<LabelValueBean> places;
	private String selectedPlaceCode;
	private Long[] selPlaces;
	
	private AmpDaTable dbEntity;
	
	//======= Column properties===========
	private Long colId;
	private String colName;
	private String colCode;
	private String colCssClass;
	private String colHtmlStyle;
	private String colPattern;
	private String colWidth;
	private Boolean colColumnEdit;
	
	private Collection<LabelValueBean> columnTypes;
	private Long colSelectedType;

	public Collection<LabelValueBean> getColumnTypes() {
		return columnTypes;
	}

	public void setColumnTypes(Collection<LabelValueBean> columnTypes) {
		this.columnTypes = columnTypes;
	}

	public Long getColId() {
		return colId;
	}

	public void setColId(Long colId) {
		this.colId = colId;
	}

	public String getColName() {
		return colName;
	}

	public void setColName(String colName) {
		this.colName = colName;
	}

	public String getColCode() {
		return colCode;
	}

	public void setColCode(String colCode) {
		this.colCode = colCode;
	}

	public String getColCssClass() {
		return colCssClass;
	}

	public void setColCssClass(String colCssClass) {
		this.colCssClass = colCssClass;
	}

	public String getColHtmlStyle() {
		return colHtmlStyle;
	}

	public void setColHtmlStyle(String colHtmlStyle) {
		this.colHtmlStyle = colHtmlStyle;
	}

	public String getColPattern() {
		return colPattern;
	}

	public void setColPattern(String colPattern) {
		this.colPattern = colPattern;
	}

	public String getColWidth() {
		return colWidth;
	}

	public void setColWidth(String colWidth) {
		this.colWidth = colWidth;
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

	public AmpDaTable getDbEntity() {
		return dbEntity;
	}

	public void setDbEntity(AmpDaTable dbEntity) {
		this.dbEntity = dbEntity;
	}

	public Collection<AmpDaTable> getTables() {
		return tables;
	}

	public void setTables(Collection<AmpDaTable> tables) {
		this.tables = tables;
	}

	public List<AmpDaColumn> getColumns() {
		return columns;
	}

	public void setColumns(List<AmpDaColumn> columns) {
		this.columns = columns;
	}

	public List<LabelValueBean> getPlaces() {
		return places;
	}

	public void setPlaces(List<LabelValueBean> places) {
		this.places = places;
	}

	public String getSelectedPlaceCode() {
		return selectedPlaceCode;
	}

	public void setSelectedPlaceCode(String selectedPlaceCode) {
		this.selectedPlaceCode = selectedPlaceCode;
	}

	public Long[] getSelPlaces() {
		return selPlaces;
	}

	public void setSelPlaces(Long[] selPlaces) {
		this.selPlaces = selPlaces;
	}

	public Boolean getNameAsTitle() {
		return nameAsTitle;
	}

	public void setNameAsTitle(Boolean nameAsTitle) {
		this.nameAsTitle = nameAsTitle;
	}

	public boolean isShouldClose() {
		return shouldClose;
	}

	public void setShouldClose(boolean shouldClose) {
		this.shouldClose = shouldClose;
	}

	public Boolean getColColumnEdit() {
		return colColumnEdit;
	}

	public void setColColumnEdit(Boolean colColumnEdit) {
		this.colColumnEdit = colColumnEdit;
	}

	public void setColSelectedType(Long colSelectedType) {
		this.colSelectedType = colSelectedType;
	}

	public Long getColSelectedType() {
		return colSelectedType;
	}

}
