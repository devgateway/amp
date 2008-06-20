package org.digijava.module.gis.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.actions.TilesAction;

/**
 * Renders chart place teaser.
 * This renders just place teaser, image itself is displayed by different action.
 * @author Irakli Kobiashvili
 *
 */
public class ChartWidgetTeaser extends TilesAction {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		
		return super.execute(mapping, form, request, response);
	}

}
