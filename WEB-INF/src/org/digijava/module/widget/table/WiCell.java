package org.digijava.module.widget.table;

import org.digijava.module.widget.table.filteredColumn.WiCellFiltered;
import org.digijava.module.widget.web.HtmlGenerator;

/**
 * Table cell.
 * Use concrate subclasses: {@link WiCellStandard}, {@link WiCellHeader}, {@link WiCellFiltered}
 * @author Irakli Kobiashvili
 *
 */
public abstract class WiCell implements HtmlGenerator{
	
	private Long id;
	private Long pk;
	private WiColumn column;
	private boolean isHeaderCell = false;
	private boolean editMode = false;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPk() {
		return pk;
	}

	public void setPk(Long pk) {
		this.pk = pk;
	}

	public abstract String getValue();

	public abstract void setValue(String value);

	public String generateHtml() {
		StringBuffer result = new StringBuffer("\t\t<TD");
		result.append(">");
		if (isHeaderCell){
			result.append("<strong>");
		}
		result.append(tagContent()); //result.append(getValue());
		if (isHeaderCell){
			result.append("</strong>");
		}
		result.append("</TD>\n");
		return result.toString();
	}

	public abstract String tagContent();
	
	public void setColumn(WiColumn column) {
		this.column = column;
	}

	public WiColumn getColumn() {
		return column;
	}

	public void setHeaderCell(boolean isHeaderCell) {
		this.isHeaderCell = isHeaderCell;
	}

	public boolean isHeaderCell() {
		return isHeaderCell;
	}

	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}

	public boolean isEditMode() {
		return editMode;
	}

}
