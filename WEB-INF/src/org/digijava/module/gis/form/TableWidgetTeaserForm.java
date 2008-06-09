package org.digijava.module.gis.form;

import org.apache.struts.action.ActionForm;
import org.digijava.module.gis.widget.table.DaTable;

/**
 * Teaser form of table widgets.
 * @author Irakli Kobiashvili
 *
 */
public class TableWidgetTeaserForm extends ActionForm {

	private static final long serialVersionUID = 1L;

	private DaTable table;
	private boolean isPreview=false;
	private String placeName;

	public String getPlaceName() {
		return placeName;
	}

	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}

	public boolean isPreview() {
		return isPreview;
	}

	public void setPreview(boolean isPreview) {
		this.isPreview = isPreview;
	}

	public DaTable getTable() {
		return table;
	}

	public void setTable(DaTable table) {
		this.table = table;
	} 
}
