package org.digijava.module.widget.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.actions.TilesAction;
import org.digijava.module.widget.dbentity.AmpDaTable;
import org.digijava.module.widget.dbentity.AmpDaWidgetPlace;
import org.digijava.module.widget.dbentity.AmpWidget;
import org.digijava.module.widget.dbentity.AmpWidgetIndicatorChart;
import org.digijava.module.widget.form.WidgetTeaserForm;
import org.digijava.module.widget.oldTable.DaTable;
import org.digijava.module.widget.table.WiTable;
import org.digijava.module.widget.util.TableWidgetUtil;
import org.digijava.module.widget.util.WidgetUtil;
/**
 * Generic widget teaser.
 * Renders staff depending on widget type assigned to the teaser place.
 * @author Irakli Kobiashvili
 *
 */
public class WidgetTeaser extends TilesAction {

	private static Logger logger = Logger.getLogger(WidgetTeaser.class);

	@Override
	public ActionForward execute(ComponentContext context,
			ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		WidgetTeaserForm wform = (WidgetTeaserForm)form;
		AmpDaWidgetPlace place = WidgetUtil.saveOrUpdatePlace(context);
		
		//we have null if there is no place parameter in context - layout should define it when inserting teaser 
		if (place==null){
			logger.debug("No place param specified!");
			wform.setRendertype(WidgetUtil.NO_PLACE_PARAM);
			return null;
		}
		//if we have place parameter then assign place name to form
		wform.setPlaceName(place.getName());
		// check if there is something assigned to place
		if (place!= null && place.getAssignedWidget()==null){
			logger.debug("Rendering empty widget for "+wform.getPlaceName());
			wform.setRendertype(WidgetUtil.EMPTY);
			return null;
		}
		//we have widget assigned to teaser place.
		AmpWidget widget = place.getAssignedWidget();
		// if widget is chart widget
		if((widget instanceof AmpWidgetIndicatorChart)){
			logger.debug("Rendering indicator chart widget for "+wform.getPlaceName());
			AmpWidgetIndicatorChart cWidget = (AmpWidgetIndicatorChart) widget;
			wform.setRendertype(WidgetUtil.CHART_INDICATOR);
			wform.setId(cWidget.getId());
			return null;
		}
		//if widget is table widget
		if (widget instanceof AmpDaTable){
			logger.debug("Rendering table widget for "+wform.getPlaceName());
			AmpDaTable dbTable = (AmpDaTable)widget;
			wform.setRendertype(WidgetUtil.TABLE);
			DaTable tableHelper = TableWidgetUtil.widgetToHelper(dbTable);
			wform.setTable(tableHelper);
			//below this is temporary test for new redesigned tables.
			WiTable table1 = null;
			try {
				table1 = new WiTable.TableBuilder(dbTable.getId()).build();
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println(table1.getName());
			System.out.println(table1.generateHtml());
			return null;
		}
		logger.debug("Widget teaser: we should not get here!");
		return null;
	}


}
