package org.digijava.module.widget.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.digijava.module.widget.dbentity.AmpDaWidgetPlace;
import org.digijava.module.widget.dbentity.AmpWidget;
import org.digijava.module.widget.form.WidgetPlacesForm;
import org.digijava.module.widget.helper.WidgetPlaceHelper;
import org.digijava.module.widget.util.WidgetUtil;
/**
 * Widget places administration action.
 * @author Irakli Kobiashvili
 *
 */
public class AdminWidgetPlaces extends DispatchAction {

	@Override
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return list(mapping, form, request, response);
	}
	public ActionForward list(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		WidgetPlacesForm pform = (WidgetPlacesForm)form;
		List<AmpDaWidgetPlace> places = WidgetUtil.getAllPlaces();
		if (places !=null && places.size()>0){
			List<WidgetPlaceHelper> helpers = new ArrayList<WidgetPlaceHelper>(places.size());
			for (AmpDaWidgetPlace place : places) {
				helpers.add(new WidgetPlaceHelper(place));
			}
			pform.setPlaces(helpers);
		}else{
			pform.setPlaces(null);
		}
		return mapping.findForward("forward");
	}
	public ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		WidgetPlacesForm pform = (WidgetPlacesForm)form;
		Long id = pform.getPlaceId();
		WidgetUtil.deleteWidgetPlace(id);
		return mapping.findForward("showPlacesList");
	}
	
	public ActionForward assignWidgt(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		WidgetPlacesForm pform = (WidgetPlacesForm)form;
		Long placeId = pform.getPlaceId();
		AmpDaWidgetPlace place = WidgetUtil.getPlace(placeId);
		WidgetPlaceHelper placeHelper = new WidgetPlaceHelper(place);
		pform.setPlace(placeHelper);
		pform.setWidgetId(new Long(-1));
		if (place.getAssignedWidget()!=null){
			pform.setWidgetId(place.getAssignedWidget().getId());
		}
		pform.setWidgets(WidgetUtil.getAllWidgetsHelpers());
		return mapping.findForward("showAssignWidget");
	}
	public ActionForward save(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		WidgetPlacesForm pform = (WidgetPlacesForm)form;
		Long widgetId = pform.getWidgetId();
		Long placeId = pform.getPlaceId();
		AmpDaWidgetPlace place = WidgetUtil.getPlace(placeId);
		AmpWidget widget = null;
		if (pform.getWidgetId().longValue()==-1){
			widget=null;
		}else{
			widget=WidgetUtil.getWidget(widgetId);
		}
		place.setAssignedWidget(widget);
		WidgetUtil.savePlace(place);
		return mapping.findForward("showPlacesList");
	}

}
