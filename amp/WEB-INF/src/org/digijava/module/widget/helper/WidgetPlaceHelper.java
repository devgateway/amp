package org.digijava.module.widget.helper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import org.digijava.module.widget.dbentity.AmpDaTable;
import org.digijava.module.widget.dbentity.AmpDaWidgetPlace;
import org.digijava.module.widget.dbentity.AmpWidget;
import org.digijava.module.widget.dbentity.AmpWidgetIndicatorChart;
import org.digijava.module.widget.dbentity.AmpWidgetOrgProfile;
import org.digijava.module.widget.util.WidgetUtil;

/**
 * Widget place and Widget helper class.
 * @author Irakli Kobiashvili
 *
 */
public class WidgetPlaceHelper implements Serializable{

	private static final long serialVersionUID = 1L;
	private Long placeId;
	private String placeName;
	private String placeCode;
	private Date placeLastRenderTime;
	private Long widgetId;
	private String widgetName;
	private String widgetCode;
	private String widgetTypeName;
	private Integer widgetTypeCode;
	private String widgetClassName;
	private Long objectId;
	private String objectName;
	private String widgetCombinedName;
	
	/**
	 * Default constructor
	 */
	public WidgetPlaceHelper(){
		
	}
	
	/**
	 * Constructor from widget place entity bean.
	 * @param place
	 */
	public WidgetPlaceHelper(AmpDaWidgetPlace place){
		this.placeId = place.getId();
		this.placeName = place.getName();
		this.placeCode = place.getCode();
		this.placeLastRenderTime = place.getLastRendered();
		if (place.getAssignedWidget()!=null){
			fromWidget(place.getAssignedWidget());
		}
	}
	
	/**
	 * Constructor from widget entity bean.
	 * @param widget
	 */
	public WidgetPlaceHelper(AmpWidget widget){
		fromWidget(widget);
	}
	
	   private void fromWidget(AmpWidget widget) {
        this.widgetClassName = widget.getClass().getName();
        this.widgetId = widget.getId();
        this.widgetName = widget.getName();
        this.widgetCode = widget.getCode();
        final ArrayList<Integer> rendertype = new ArrayList<Integer>();
        final ArrayList<String> rendertypeName = new ArrayList<String>();
        WidgetVisitor adapter = new WidgetVisitorAdapter() {

            @Override
            public void visit(AmpWidgetIndicatorChart chart) {
                rendertype.add(WidgetUtil.CHART_INDICATOR);
                rendertypeName.add("Indicator Chart");
            }

            @Override
            public void visit(AmpWidgetOrgProfile orgProfile) {
                rendertype.add(WidgetUtil.ORG_PROFILE);
                rendertypeName.add("Organization Profile");

            }

            @Override
            public void visit(AmpDaTable table) {
                rendertype.add(WidgetUtil.TABLE);
                rendertypeName.add("Table");
            }
        };
        widget.accept(adapter);
        if (rendertype.size() > 0) {
            this.widgetTypeCode = rendertype.get(0);
            this.widgetTypeName = rendertypeName.get(0);
        } else {
            this.widgetTypeName = "Unknown";
            this.widgetTypeCode = new Integer(0);
        }
        this.widgetCombinedName = this.widgetName + "  (" + this.widgetTypeName + ")";
    }
	
	public Long getPlaceId() {
		return placeId;
	}
	public void setPlaceId(Long placeId) {
		this.placeId = placeId;
	}
	public String getPlaceName() {
		return placeName;
	}
	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}
	public String getPlaceCode() {
		return placeCode;
	}
	public void setPlaceCode(String placeCode) {
		this.placeCode = placeCode;
	}
	public Date getPlaceLastRenderTime() {
		return placeLastRenderTime;
	}
	public void setPlaceLastRenderTime(Date placeLastRenderTime) {
		this.placeLastRenderTime = placeLastRenderTime;
	}
	public Long getWidgetId() {
		return widgetId;
	}
	public void setWidgetId(Long widgetId) {
		this.widgetId = widgetId;
	}
	public String getWidgetName() {
		return widgetName;
	}
	public void setWidgetName(String widgetName) {
		this.widgetName = widgetName;
	}
	public String getWidgetCode() {
		return widgetCode;
	}
	public void setWidgetCode(String widgetCode) {
		this.widgetCode = widgetCode;
	}
	public String getWidgetTypeName() {
		return widgetTypeName;
	}
	public void setWidgetTypeName(String widgetTypeName) {
		this.widgetTypeName = widgetTypeName;
	}
	public String getWidgetClassName() {
		return widgetClassName;
	}
	public void setWidgetClassName(String widgetClassName) {
		this.widgetClassName = widgetClassName;
	}
	public Long getObjectId() {
		return objectId;
	}
	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}
	public String getObjectName() {
		return objectName;
	}
	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public Integer getWidgetTypeCode() {
		return widgetTypeCode;
	}

	public void setWidgetTypeCode(Integer widgetTypeCode) {
		this.widgetTypeCode = widgetTypeCode;
	}

	public String getWidgetCombinedName() {
		return widgetCombinedName;
	}

	public void setWidgetCombinedName(String widgetCombinedName) {
		this.widgetCombinedName = widgetCombinedName;
	}

}
