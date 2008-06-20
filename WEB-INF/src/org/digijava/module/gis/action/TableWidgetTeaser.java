package org.digijava.module.gis.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.actions.TilesAction;
import org.digijava.module.gis.dbentity.AmpDaTable;
import org.digijava.module.gis.dbentity.AmpDaWidgetPlace;
import org.digijava.module.gis.form.TableWidgetTeaserForm;
import org.digijava.module.gis.util.TableWidgetUtil;
import org.digijava.module.gis.util.WidgetUtil;
import org.digijava.module.gis.widget.table.DaTable;

/**
 * Renders table widget teaser.
 * @author Irakli Kobiashvili
 *
 */
public class TableWidgetTeaser extends TilesAction {

    private static Logger logger = Logger.getLogger(TableWidgetTeaser.class);

	@Override
	public ActionForward execute(ComponentContext context,
			ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		TableWidgetTeaserForm tForm=(TableWidgetTeaserForm)form;

		DaTable table = null;
		
		AmpDaWidgetPlace place = WidgetUtil.saveOrUpdatePlace(context);
		
		AmpDaTable widget=place.getWidget();

		if (null != widget){
			table = TableWidgetUtil.widgetToHelper(widget);
		}

		//setting null if no widget assigned.
		tForm.setTable(table);
		tForm.setPlaceName(place.getName());
		
		logger.debug("table Widget '"+table.getName()+"' with for place '"+place.getName()+"'");
		return null;
	}
	
	
}
