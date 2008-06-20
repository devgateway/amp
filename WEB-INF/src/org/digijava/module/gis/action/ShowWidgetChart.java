	package org.digijava.module.gis.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.actions.TilesAction;
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.aim.util.IndicatorUtil;
import org.digijava.module.gis.dbentity.AmpDaWidgetPlace;
import org.digijava.module.gis.form.ShowWidgetChartForm;
import org.digijava.module.gis.util.ChartWidgetUtil;
import org.digijava.module.gis.util.WidgetUtil;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

/**
 * Renders jFreeChart for widget.
 * Initially used on gis dashboard
 * @author Irakli Kobiashvili
 *
 */
public class ShowWidgetChart extends TilesAction {

	
	
	@Override
	public ActionForward execute(ComponentContext context,
			ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ShowWidgetChartForm wForm = (ShowWidgetChartForm)form;
        response.setContentType("image/png");
        
        if (wForm.getImageHeight() == null || wForm.getImageHeight().intValue()<=0){
        	wForm.setImageHeight(new Integer(50));
        }
        if (wForm.getImageWidth() == null || wForm.getImageWidth().intValue()<=0){
        	wForm.setImageWidth(new Integer(80));
        }

        AmpDaWidgetPlace place = WidgetUtil.saveOrUpdatePlace(context);
        
        
        Long indicatorId = wForm.getObjectId();
        if (indicatorId != null){
        	//load indicator
            AmpIndicator indicator = IndicatorUtil.getIndicator(indicatorId);
            //generate chart
            JFreeChart chart = ChartWidgetUtil.getIndicatorChart(indicator);
            ChartRenderingInfo info = new ChartRenderingInfo();
            //write image in response
    		ChartUtilities.writeChartAsPNG(response.getOutputStream(), 
    				chart, 
    				wForm.getImageWidth().intValue(),
    				wForm.getImageHeight().intValue(), info);
        }
		return null;
	}


}
