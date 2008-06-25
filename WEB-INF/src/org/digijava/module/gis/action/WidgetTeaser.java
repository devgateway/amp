package org.digijava.module.gis.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.actions.TilesAction;
import org.digijava.module.gis.dbentity.AmpDaWidgetPlace;
import org.digijava.module.gis.dbentity.AmpWidget;
import org.digijava.module.gis.dbentity.AmpWidgetIndicatorChart;
import org.digijava.module.gis.form.WidgetTeaserForm;
import org.digijava.module.gis.util.WidgetUtil;
/**
 * Generic widget teaser.
 * Renders staff depending on widget type assigned to the teaser place.
 * @author Irakli Kobiashvili
 *
 */
public class WidgetTeaser extends TilesAction {

	@Override
	public ActionForward execute(ComponentContext context,
			ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		WidgetTeaserForm wform = (WidgetTeaserForm)form;
		AmpDaWidgetPlace place = WidgetUtil.saveOrUpdatePlace(context);
		if (place==null){
			//TODO do something!
			return null;
		}
		if (place!= null && place.getAssignedWidget()==null){
			wform.setRendertype(WidgetTeaserForm.EMPTY);
			return null;
		}
		AmpWidget widget = place.getAssignedWidget();
		// if widget is chart widget
		if((widget instanceof AmpWidgetIndicatorChart)){
			AmpWidgetIndicatorChart cWidget = (AmpWidgetIndicatorChart) widget;
			wform.setRendertype(WidgetTeaserForm.CHART_INDICATOR);
			wform.setId(cWidget.getId());
			return null;
		}
		//if widget is table widget
		//TODO implement same for table widgets.
		return null;
	}


}
