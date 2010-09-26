package org.digijava.module.widget.form;

import java.util.List;
import java.util.Date;
import org.apache.struts.action.ActionForm;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.widget.dbentity.AmpWidget;
import org.digijava.module.widget.oldTable.DaTable;
import org.digijava.module.widget.table.WiTable;

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
	private WiTable tableWidget;
	/**
	 * multi-purpose ID.
	 * Depending on {@link #rendertype} this is used for table or indicator or chart widget id in teaser JSP.
	 */
	private Long id;
	private boolean showPlaceInfo;
    private List<AmpClassificationConfiguration> sectorClassificationConfigs;

    // used for Org Profile to determine which chart/text render
    private Long type;
        
    public Long getType() {
        return type;
    }

    public List<AmpClassificationConfiguration> getSectorClassificationConfigs() {
        return sectorClassificationConfigs;
    }

    public void setSectorClassificationConfigs(List<AmpClassificationConfiguration> sectorClassificationConfigs) {
        this.sectorClassificationConfigs = sectorClassificationConfigs;
    }

    public void setType(Long type) {
        this.type = type;
    }
	
	public boolean isShowPlaceInfo() {
		return showPlaceInfo;
	}
	public void setShowPlaceInfo(boolean showPlaceInfo) {
		this.showPlaceInfo = showPlaceInfo;
	}
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
	public void setTableWidget(WiTable tableWidget) {
		this.tableWidget = tableWidget;
	}
	public WiTable getTableWidget() {
		return tableWidget;
	}
    public long getTime(){
        Date date=new Date();
        long time=date.getTime();
        return time;

   }
}
