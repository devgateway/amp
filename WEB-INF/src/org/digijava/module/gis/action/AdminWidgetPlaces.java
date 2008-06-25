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
		pform.setPlaces(places);
		return mapping.findForward("forward");
	}

}
