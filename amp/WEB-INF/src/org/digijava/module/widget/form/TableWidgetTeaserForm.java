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

}
