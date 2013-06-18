package org.digijava.module.widget.action;

import java.awt.Font;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.ChartUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.widget.form.SectorByDonorTeaserForm;
import org.digijava.module.widget.helper.ChartOption;
import org.digijava.module.widget.util.ChartWidgetUtil;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.Plot;

public class ShowSectorByDonorChart extends Action {

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		SectorByDonorTeaserForm cForm = (SectorByDonorTeaserForm)form;
        response.setContentType("image/png");
        Integer fromYear = null;
        Integer toYear = null;
        Long[] donorIDs = null;
    	Site site = RequestUtils.getSite(request);
		Locale navigationLanguage = RequestUtils.getNavigationLanguage(request);
		String siteId = site.getId()+"";
		String locale = navigationLanguage.getCode();
        ChartOption opt=createChartOption(cForm,request);
        //from year
        if (cForm.getSelectedFromYear()!=null && !cForm.getSelectedFromYear().equals("-1")){
        	fromYear = new Integer(cForm.getSelectedFromYear());
        }else{
        	GregorianCalendar cal = new GregorianCalendar();
        	fromYear = new Integer(cal.get(Calendar.YEAR)-1);
        }
        //to year
        if (cForm.getSelectedToYear()!=null && !cForm.getSelectedToYear().equals("-1")){
        	toYear = new Integer(cForm.getSelectedToYear());
        }else{
        	GregorianCalendar cal = new GregorianCalendar();
        	toYear = new Integer(cal.get(Calendar.YEAR));
        }
        //donors
        if(cForm.getSelectedDonor()!=null && cForm.getSelectedDonor().longValue()!=-1){
        	donorIDs = new Long[1];
        	donorIDs[0] = cForm.getSelectedDonor();
        }

        //generate chart
        JFreeChart chart = ChartWidgetUtil.getSectorByDonorChart(donorIDs, fromYear,toYear, opt, request.getSession(),cForm.getSelectedCurrency());
        //if no data is available,user should get a message about it.
        Plot plot = chart.getPlot();
        String noDatamessage = TranslatorWorker.translateText("There is no data available for the selected filters. Please adjust the date and/or donor filters",locale,siteId);
        plot.setNoDataMessage(noDatamessage);
        Font font= new Font(null,0,15);
        plot.setNoDataMessageFont(font);

        ChartRenderingInfo info = new ChartRenderingInfo();

        //write image in response
		ChartUtilities.writeChartAsPNG(response.getOutputStream(),chart,opt.getWidth().intValue(),opt.getHeight().intValue(), info, true, 0);

		String fiscalYear = TranslatorWorker.translateText("FY", locale, site.getId().toString());
		//fill from years' drop-down
		cForm.setYearsFrom(ChartWidgetUtil.getYears(true, fiscalYear));
		//fill to years' drop-down
		cForm.setYearsTo(ChartWidgetUtil.getYears(false, fiscalYear));
        //generate map for this graph
        String map = ChartUtilities.getImageMap("sectorByDonorChartImageMap", info);
        
        //save map with timestamp from request for later use
        //timestemp is generated with javascript before sending ajax request.
        ChartUtil.saveMap(map, cForm.getTimestamp(), request.getSession());

		boolean amountsInThousands=FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS).equals("true");
		cForm.setAmountsInThousands(amountsInThousands);
		return null;
	}

	   private ChartOption createChartOption(SectorByDonorTeaserForm form, HttpServletRequest request) {
        ChartOption opt = new ChartOption();

        // URL
        String url = RequestUtils.getFullModuleUrl(request);
        url += "showSectorDonorReport.do";
        opt.setUrl(url);


        //TITLE
        if (form.getShowTitle() == null) {
            opt.setShowTitle(true);
        } else {
            opt.setShowTitle(form.getShowTitle());
        }
        //LEGEND
        if (form.getShowLegend() == null) {
            opt.setShowLegend(true);
        } else {
            opt.setShowLegend(form.getShowLegend());
        }
        //LABEL
        if (form.getShowLabel() == null) {
            opt.setShowLabels(true);
        } else {
            opt.setShowLabels(form.getShowLabel());
        }
        //HEIGHT
        if (form.getImageHeight() == null) {
            opt.setHeight(new Integer(480));
        } else {
            opt.setHeight(form.getImageHeight());
        }
        //WIDTH
        if (form.getImageWidth() == null) {
            opt.setWidth(new Integer(420));
        } else {
            opt.setWidth(form.getImageWidth());
        }
        String siteId = RequestUtils.getSiteDomain(request).getSite().getId().toString();
        opt.setSiteId(siteId);
        String langCode = RequestUtils.getNavigationLanguage(request).getCode();
        opt.setLangCode(langCode);
        return opt;
    }

}
