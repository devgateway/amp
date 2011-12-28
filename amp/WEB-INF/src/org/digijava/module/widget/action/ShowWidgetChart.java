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
        HttpSession session = request.getSession();
        FilterHelper filter = null;
        String chartId = "chartMap" + wForm.getChartType();
        if (wForm.getTransactionType() != null) {
            filter = new FilterHelper((FilterHelper) session.getAttribute("orgProfileFilter"));
            filter.setTransactionType(wForm.getTransactionType());
            chartId += "_" + wForm.getTransactionType();
        } else {
            filter = (FilterHelper) session.getAttribute("orgProfileFilter");
        }
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
            } else {
                ChartOption opt = createChartOptions(wForm, widget);
                String siteId = RequestUtils.getSiteDomain(request).getSite().getId().toString();
                opt.setSiteId(siteId);
                String langCode = RequestUtils.getNavigationLanguage(request).getCode();
                opt.setLangCode(langCode);
                String title = null;
                JFreeChart chart = null;
                ChartRenderingInfo info = new ChartRenderingInfo();
                Long sectorClassConfigId=wForm.getSectorClassConfigId();
                switch (wForm.getChartType().intValue()) {
                    case WidgetUtil.ORG_PROFILE_TYPE_OF_AID:
                        title = TranslatorWorker.translateText("Type Of Aid", opt.getLangCode(), opt.getSiteId());
                        opt.setTitle(title);
                        chart = ChartWidgetUtil.getBarChart(opt, filter, wForm.getChartType().intValue());
                        break;

                    case WidgetUtil.ORG_PROFILE_PLEDGES_COMM_DISB:
                        String charttitle="";
                        if(filter.isPledgeVisible()){
                             charttitle="Pledges|";
                        }
                        charttitle+="Commitments|Disbursements";
                        if(filter.isExpendituresVisible()){
                             charttitle+="|Expenditures";
                        }
                        title = TranslatorWorker.translateText(charttitle, opt.getLangCode(), opt.getSiteId());
                        opt.setTitle(title);
                        chart = ChartWidgetUtil.getBarChart(opt, filter, wForm.getChartType().intValue());
                        break;

                    case WidgetUtil.ORG_PROFILE_ODA_PROFILE:
                        title = TranslatorWorker.translateText("ODA Profile", opt.getLangCode(), opt.getSiteId());
                        opt.setTitle(title);
                       chart = ChartWidgetUtil.getBarChart(opt, filter, wForm.getChartType().intValue());;
                        break;
                    case WidgetUtil.ORG_PROFILE_SECTOR_BREAKDOWN:
                        if( sectorClassConfigId==null|| sectorClassConfigId==0){
                            sectorClassConfigId=SectorUtil.getPrimaryConfigClassification().getId();
                        }
                        title = SectorUtil.getClassificationConfigById(sectorClassConfigId).getClassification().getSecSchemeName();
                        opt.setTitle(title);
                        
                        chart = ChartWidgetUtil.getDonutChart(opt, filter,wForm.getChartType().intValue(),sectorClassConfigId);
                        break;
                    case WidgetUtil.ORG_PROFILE_REGIONAL_BREAKDOWN:
                        title = TranslatorWorker.translateText("Regional", opt.getLangCode(), opt.getSiteId());
                        opt.setTitle(title);
                        chart = ChartWidgetUtil.getDonutChart(opt, filter,wForm.getChartType().intValue(),sectorClassConfigId);
                        break;
                    case WidgetUtil.ORG_PROFILE_AID_PREDICTIBLITY:
                        title = TranslatorWorker.translateText("Aid Predictability", opt.getLangCode(), opt.getSiteId());
                        opt.setTitle(title);
                        chart = ChartWidgetUtil.getBarChart(opt, filter, wForm.getChartType().intValue());
                        break;
                }
                Plot plot = chart.getPlot();
                String noDataString = TranslatorWorker.translateText("No Data Available", langCode, siteId);
                plot.setNoDataMessage(noDataString);
                Font font = new Font(null, 0, 24);
                plot.setNoDataMessageFont(font);


                // write image in response
                ChartUtilities.writeChartAsPNG(
                        response.getOutputStream(),
                        chart,
                        opt.getWidth().intValue(),
                        opt.getHeight().intValue(),
                        info,
                        true,
                        0);

                String map = ChartUtilities.getImageMap(chartId, info);
                session.setAttribute(chartId, map);




            }
        } else {
            // System.out.println("No chart assigned to this teaser!");//TODO this should go to form as error message.
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
