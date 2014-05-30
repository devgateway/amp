package org.digijava.module.widget.action;

import java.awt.Font;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.IndicatorSector;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.orgProfile.helper.FilterHelper;
import org.digijava.module.widget.dbentity.AmpWidget;
import org.digijava.module.widget.dbentity.AmpWidgetIndicatorChart;
import org.digijava.module.widget.form.ShowWidgetChartForm;
import org.digijava.module.widget.helper.ChartOption;
import org.digijava.module.widget.util.ChartWidgetUtil;
import org.digijava.module.widget.util.WidgetUtil;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.Plot;

/**
 * Renders jFreeChart for widget. Initially used on gis dashboard
 * 
 * @author Irakli Kobiashvili
 * 
 */
public class ShowWidgetChart extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ShowWidgetChartForm wForm = (ShowWidgetChartForm) form;
        response.setContentType("image/png");
        AmpWidgetIndicatorChart widget = null;
        if (wForm.getWidgetId() != null) {
            if (wForm.getChartType() == null) {
                widget = ChartWidgetUtil.getIndicatorChartWidget(wForm.getWidgetId());
                ChartOption opt = createChartOptions(wForm, widget);
                IndicatorSector indicatorCon = widget.getIndicator();
                if (indicatorCon != null) {
                    // generate chart
                    JFreeChart chart = ChartWidgetUtil.getIndicatorChart(indicatorCon, opt);
                    ChartRenderingInfo info = new ChartRenderingInfo();
                    // write image in response
                    ChartUtilities.writeChartAsPNG(
                            response.getOutputStream(),
                            chart,
                            opt.getWidth().intValue(),
                            opt.getHeight().intValue(),
                            info);
                }
            } 
        } else {
            // //System.out.println("No chart assigned to this teaser!");//TODO this should go to form as error message.
            return null;
        }


        return null;
    }

    /**
     * Builds chart options bean from action form.
     * Uses default values if parameters are not specified in from or form has no field yet for such parameters.
     * This gives us ability to specify chart objects directly from URL, something like cewolf tag library.
     * @param form
     * @param widget
     * @return
     */
    private ChartOption createChartOptions(ShowWidgetChartForm form, AmpWidget widget) {
        ChartOption opt = new ChartOption();

        opt.setShowTitle(true);
        if (widget != null) {
            opt.setTitle(widget.getName());
        }
        opt.setShowLegend(false);
		if (form.getShowLegend()!=null){
            opt.setShowLegend(form.getShowLegend().booleanValue());
        }
        opt.setShowLabels(true);
		if (form.getShowLabels()!=null){
            opt.setShowLabels(form.getShowLabels().booleanValue());
        }
        if (form.getImageHeight() == null) {
            opt.setHeight(new Integer(160));
        } else {
            opt.setHeight(form.getImageHeight());
        }
        if (form.getImageWidth() == null) {
            opt.setWidth(new Integer(220));
        } else {
            opt.setWidth(form.getImageWidth());
        }

        return opt;
    }
}
