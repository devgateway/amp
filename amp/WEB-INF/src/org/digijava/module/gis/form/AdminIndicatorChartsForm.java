package org.digijava.module.gis.form;

import java.util.List;

import org.apache.struts.action.ActionForm;
import org.digijava.module.aim.dbentity.IndicatorSector;
import org.digijava.module.gis.dbentity.AmpDaWidgetPlace;
import org.digijava.module.gis.dbentity.AmpWidgetIndicatorChart;

/**
 * Form for indicator chart widget mnipulation.
 * @author Irakli Kobiashvili
 *
 */
public class AdminIndicatorChartsForm extends ActionForm {

	private static final long serialVersionUID = 1L;
	private List<AmpWidgetIndicatorChart> widgets;
	private List<IndicatorSector> indicators;
	private List<AmpDaWidgetPlace> places;
	private String widgetName;
	private Long selIndicators;
	private Long[] selPlaces;
	private Long widgetId;
	
	public Long getWidgetId() {
		return widgetId;
	}
	public void setWidgetId(Long widgetId) {
		this.widgetId = widgetId;
	}
	public Long getSelIndicators() {
		return selIndicators;
	}
	public void setSelIndicators(Long selIndicators) {
		this.selIndicators = selIndicators;
	}
	public List<IndicatorSector> getIndicators() {
		return indicators;
	}
	public void setIndicators(List<IndicatorSector> indicators) {
		this.indicators = indicators;
	}
	public List<AmpDaWidgetPlace> getPlaces() {
		return places;
	}
	public void setPlaces(List<AmpDaWidgetPlace> places) {
		this.places = places;
	}
	public String getWidgetName() {
		return widgetName;
	}
	public void setWidgetName(String widgetName) {
		this.widgetName = widgetName;
	}
	public List<AmpWidgetIndicatorChart> getWidgets() {
		return widgets;
	}
	public void setWidgets(List<AmpWidgetIndicatorChart> widgets) {
		this.widgets = widgets;
	}
	public Long[] getSelPlaces() {
		return selPlaces;
	}
	public void setSelPlaces(Long[] selPlaces) {
		this.selPlaces = selPlaces;
	}
}
