package org.digijava.module.widget.form;

import org.apache.struts.action.ActionForm;
import org.digijava.module.widget.table.WiTable;

/**
 * Form of table widgets.
 * @author Irakli Kobiashvili
 *
 */
public class TableWidgetTeaserForm extends ActionForm {

	private static final long serialVersionUID = 1L;

	private String placeName;
	private WiTable table;
	private Long tableId;
	private Long columnId;
	private Long itemId;
	private Boolean preview;

	public String getPlaceName() {
		return placeName;
	}

	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}

	public void setTable(WiTable table) {
		this.table = table;
	}

	public WiTable getTable() {
		return table;
	}

	public void setTableId(Long tableId) {
		this.tableId = tableId;
	}

	public Long getTableId() {
		return tableId;
	}

	public void setColumnId(Long columnId) {
		this.columnId = columnId;
	}

	public Long getColumnId() {
		return columnId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setPreview(Boolean preview) {
		this.preview = preview;
	}

	public Boolean getPreview() {
		return preview;
	}

}
