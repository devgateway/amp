package org.digijava.module.gis.form;

import org.apache.struts.action.ActionForm;
import org.digijava.module.gis.dbentity.AmpWidget;
import org.digijava.module.gis.widget.table.DaTable;

/**
 * Generic widget form.
 * @author Irakli Kobiashvili
 *
 */
public class WidgetTeaserForm extends ActionForm {

	private static final long serialVersionUID = 1L;
	private String placeName;
	/**
	 * Defines what should be rendered on teaser.
	 * For this one use constants defined in this class.
	 */
	private int rendertype=0;
	
	/**
	 * Embedded HTML directly in the form.
	 */
	private String embeddedHtml;
	private AmpWidget widget;
	private DaTable table;
	/**
	 * multi-purpose ID.
	 * Depending on {@link #rendertype} this is used for table or indicator or chart widget id in teaser JSP.
	 */
	private Long id;
	
	public AmpWidget getWidget() {
		return widget;
	}
	public void setWidget(AmpWidget widget) {
		this.widget = widget;
	}
	public int getRendertype() {
		return rendertype;
	}
	public void setRendertype(int rendertype) {
		this.rendertype = rendertype;
	}
	public String getEmbeddedHtml() {
		return embeddedHtml;
	}
	public void setEmbeddedHtml(String embeddedHtml) {
		this.embeddedHtml = embeddedHtml;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public DaTable getTable() {
		return table;
	}
	public void setTable(DaTable table) {
		this.table = table;
	}
	public String getPlaceName() {
		return placeName;
	}
	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}
}
