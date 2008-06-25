package org.digijava.module.gis.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.actions.TilesAction;
import org.digijava.module.gis.dbentity.AmpDaWidgetPlace;
import org.digijava.module.gis.util.WidgetUtil;

/**
 * Renders chart place teaser.
 * This renders just place teaser, image itself is displayed by different action.
 * @author Irakli Kobiashvili
 *
 */
public class ChartWidgetTeaser extends TilesAction {

	@Override
	public ActionForward execute(ComponentContext context,
			ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		AmpDaWidgetPlace place = WidgetUtil.saveOrUpdatePlace(context);
		return super.execute(context, mapping, form, request, response);
	}


}
