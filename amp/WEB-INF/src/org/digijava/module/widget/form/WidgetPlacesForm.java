package org.digijava.module.widget.form;

import java.util.List;

import org.apache.struts.action.ActionForm;
import org.digijava.module.widget.helper.WidgetPlaceHelper;

/**
 * Widget place form.
 * @author Irakli Kobiashvili
 *
 */
public class WidgetPlacesForm extends ActionForm {
	private static final long serialVersionUID = 1L;
	private Long placeId;
	private List<WidgetPlaceHelper> places;
	private List<WidgetPlaceHelper> widgets;
	private Long widgetId;
	private WidgetPlaceHelper place;
	
	public WidgetPlaceHelper getPlace() {
		return place;
	}
	public void setPlace(WidgetPlaceHelper place) {
		this.place = place;
	}
	public Long getWidgetId() {
		return widgetId;
	}
	public void setWidgetId(Long widgetId) {
		this.widgetId = widgetId;
	}
	public List<WidgetPlaceHelper> getWidgets() {
		return widgets;
	}
	public void setWidgets(List<WidgetPlaceHelper> widgets) {
		this.widgets = widgets;
	}
	public List<WidgetPlaceHelper> getPlaces() {
		return places;
	}
	public void setPlaces(List<WidgetPlaceHelper> places) {
		this.places = places;
	}
	public Long getPlaceId() {
		return placeId;
	}
	public void setPlaceId(Long placeId) {
		this.placeId = placeId;
	}
}
