package org.digijava.module.gis.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.gis.form.SectorByDonorTeaserForm;
import org.digijava.module.gis.util.ChartWidgetUtil;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;

public class ShowSectorByDonorChart extends Action {

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		SectorByDonorTeaserForm cForm = (SectorByDonorTeaserForm)form;
        response.setContentType("image/png");

        if (cForm.getImageHeight() == null || cForm.getImageHeight().intValue()<=0){
        	cForm.setImageHeight(new Integer(260));
        }
        if (cForm.getImageWidth() == null || cForm.getImageWidth().intValue()<=0){
        	cForm.setImageWidth(new Integer(420));
        }
        //generate chart
        JFreeChart chart = ChartWidgetUtil.getSectorByDonorChart(null, cForm.getSelectedYear());
        ChartRenderingInfo info = new ChartRenderingInfo();
        
        //write image in response
		ChartUtilities.writeChartAsPNG(response.getOutputStream(), 
				chart, 
				cForm.getImageWidth().intValue(),
				cForm.getImageHeight().intValue(), info);
		
		return null;
	}

}
