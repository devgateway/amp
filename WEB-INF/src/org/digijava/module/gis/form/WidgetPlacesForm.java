package org.digijava.module.gis.form;

import java.util.List;

import org.apache.struts.action.ActionForm;
import org.digijava.module.gis.dbentity.AmpDaWidgetPlace;

/**
 * Widget place form.
 * @author Irakli Kobiashvili
 *
 */
public class WidgetPlacesForm extends ActionForm {

	private static final long serialVersionUID = 1L;
	private List<AmpDaWidgetPlace> places;
	
	public List<AmpDaWidgetPlace> getPlaces() {
		return places;
	}
	public void setPlaces(List<AmpDaWidgetPlace> places) {
		this.places = places;
	}
}
