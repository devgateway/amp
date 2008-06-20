package org.digijava.module.gis.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.digijava.module.gis.dbentity.AmpDaWidgetPlace;
import org.digijava.module.gis.form.WidgetPlacesForm;
import org.digijava.module.gis.util.WidgetUtil;

/**
 * Lists all widgets
 * @author Irakli Kobiashvili
 *
 */
public class ListWidgetPlaces extends DispatchAction {

	public ActionForward list(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		WidgetPlacesForm wForm = (WidgetPlacesForm)form;
		List<AmpDaWidgetPlace> places = WidgetUtil.getAllPlaces();
		wForm.setPlaces(places);
		return mapping.findForward("forward");
	}

	public ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		WidgetPlacesForm wForm = (WidgetPlacesForm)form;
		List<AmpDaWidgetPlace> places = WidgetUtil.getAllPlaces();
		wForm.setPlaces(places);
		return mapping.findForward("forward");
	}

	@Override
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return list(mapping, form, request, response);
	}

}
