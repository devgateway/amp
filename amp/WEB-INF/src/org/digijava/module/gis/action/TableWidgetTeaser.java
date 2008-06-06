package org.digijava.module.gis.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.actions.TilesAction;
import org.digijava.kernel.exception.DgException;
import org.digijava.module.gis.dbentity.AmpDaTable;
import org.digijava.module.gis.dbentity.AmpDaWidgetPlace;
import org.digijava.module.gis.form.TableWidgetTeaserForm;
import org.digijava.module.gis.util.TableWidgetUtil;
import org.digijava.module.gis.widget.table.DaTable;

public class TableWidgetTeaser extends TilesAction {

    private static Logger logger = Logger.getLogger(TableWidgetTeaser.class);

	public static final String WIDGET_TEASER_PARAM = "widget-teaser-param";
	
	@Override
	public ActionForward execute(ComponentContext context,
			ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		TableWidgetTeaserForm tForm=(TableWidgetTeaserForm)form;

		DaTable table = null;
		
		AmpDaWidgetPlace place = saveOrUpdatePlace(context);
		
		AmpDaTable widget=place.getWidget();

		if (null != widget){
			table = TableWidgetUtil.widgetToHelper(widget);
		}

		//setting null if no widget assigned.
		tForm.setTable(table);
		
		return null;
	}
	
	private AmpDaWidgetPlace saveOrUpdatePlace(ComponentContext context) throws DgException{
		Object paramObj = context.getAttribute(WIDGET_TEASER_PARAM);
		if (paramObj == null || !(paramObj instanceof String)) {
			logger.error("No place param specified for teaser in layout!");
			return null;
		}
		String code=(String) paramObj;
		AmpDaWidgetPlace place = TableWidgetUtil.getPlace(code);
		if (place == null){
			place = new AmpDaWidgetPlace();
			place.setName(code);
		}
		place.setModule((String)context.getAttribute("org.digijava.kernel.module_name"));
		place.setModuleInstance((String)context.getAttribute("org.digijava.kernel.module_instance"));
		place.setCode(code);
		TableWidgetUtil.savePlace(place);
		return place;
	}
	
}
