	package org.digijava.module.gis.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.IndicatorSector;
import org.digijava.module.gis.dbentity.AmpWidgetIndicatorChart;
import org.digijava.module.gis.form.ShowWidgetChartForm;
import org.digijava.module.gis.util.ChartWidgetUtil;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

/**
 * Renders jFreeChart for widget.
 * Initially used on gis dashboard
 * @author Irakli Kobiashvili
 *
 */
public class ShowWidgetChart extends Action {

	public ActionForward execute(
			ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ShowWidgetChartForm wForm = (ShowWidgetChartForm)form;
        response.setContentType("image/png");
        
        if (wForm.getImageHeight() == null || wForm.getImageHeight().intValue()<=0){
        	wForm.setImageHeight(new Integer(160));
        }
        if (wForm.getImageWidth() == null || wForm.getImageWidth().intValue()<=0){
        	wForm.setImageWidth(new Integer(220));
        }
        
        AmpWidgetIndicatorChart widget = null;
        if (wForm.getWidgetId()!=null){
        	widget = ChartWidgetUtil.getIndicatorChartWidget(wForm.getWidgetId());
        }else{
        	//System.out.println("No chart assigned to this teaser!");//TODO this should go to form ass error message.
        	return null;
        }
        IndicatorSector indicatorCon = widget.getIndicator();
        if (indicatorCon != null){
            //generate chart
            JFreeChart chart = ChartWidgetUtil.getIndicatorChart(indicatorCon);
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
