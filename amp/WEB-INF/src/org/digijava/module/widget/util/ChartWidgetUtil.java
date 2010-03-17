
package org.digijava.module.widget.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.struts.util.LabelValueBean;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.utils.AmpCollectionUtils.KeyWorker;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpIndicatorValue;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpPledge;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.IndicatorSector;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.aim.helper.CurrencyWorker;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.logic.FundingCalculationsHelper;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DecimalWraper;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.FiscalCalendarUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.orgProfile.helper.FilterHelper;
import org.digijava.module.orgProfile.helper.PieChartCustomLabelGenerator;
import org.digijava.module.orgProfile.helper.PieChartLegendGenerator;
import org.digijava.module.orgProfile.util.OrgProfileUtil;
import org.digijava.module.widget.dbentity.AmpDaWidgetPlace;
import org.digijava.module.widget.dbentity.AmpWidget;
import org.digijava.module.widget.dbentity.AmpWidgetIndicatorChart;
import org.digijava.module.widget.helper.ChartOption;
import org.digijava.module.widget.helper.DonorSectorFundingHelper;
import org.digijava.module.widget.helper.DonorSectorPieChartURLGenerator;
import org.digijava.module.widget.helper.SectorHelper;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.Range;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.VerticalAlignment;

/**
 * Chart widgets util.
 * @author Irakli Kobiashvili
 *
 */
public class ChartWidgetUtil {
    private static Logger logger = Logger.getLogger(ChartWidgetUtil.class);


    /**
     * Generates chart object from specified indicator and options.
     * This chart then can be rendered as image or pdf or file.
     * @param indicatorCon
     * @param opt
     * @return
     * @throws DgException
     */
    public static JFreeChart getIndicatorChart(IndicatorSector indicatorCon,ChartOption opt) throws DgException{
		JFreeChart chart = null;
		Font font8 = new Font(null,0,8);
		TimeSeriesCollection ds = getIndicatorChartDataset(indicatorCon);
		boolean tooltips = false;
		boolean urls = false;
		//create time series line chart
		chart = ChartFactory.createTimeSeriesChart(opt.getTitle(), null, null, ds, opt.isShowLegend(), tooltips, urls);
		chart.getTitle().setFont(font8);
		if (opt.isShowLegend()){
			chart.getLegend().setItemFont(font8);
		}
		RectangleInsets padding = new RectangleInsets(0,0,0,0);
		chart.setPadding(padding);

		XYPlot plot =(XYPlot) chart.getPlot();
		plot.setRangeZeroBaselineVisible(true);
		plot.getDomainAxis().setLabelFont(font8);
		plot.getDomainAxis().setTickLabelFont(font8);
		plot.getRangeAxis().setLabelFont(font8);
		plot.getRangeAxis().setTickLabelFont(font8);

		XYItemRenderer renderer = plot.getRenderer();
//		renderer.setItemLabelFont(font8);
		renderer.setItemLabelsVisible(opt.isShowLabels());
		XYItemLabelGenerator labelGenerator = new StandardXYItemLabelGenerator("{2}",new DecimalFormat("0.00"),new DecimalFormat("#.####"));
		renderer.setBaseItemLabelGenerator(labelGenerator);

		//For next two lines see order of TimeSeries added to TimeSeriesCollection in getIndicatorChartDataset() below
		renderer.setSeriesPaint(0, Color.blue);//0 = Actual line, because this was added first.
		renderer.setSeriesPaint(1, Color.red);//1 = Target line, because this was added second.
		if (renderer instanceof XYLineAndShapeRenderer){
			XYLineAndShapeRenderer r = (XYLineAndShapeRenderer)renderer;
			r.setBaseShapesVisible(true);
			r.setBaseShapesFilled(true);
		}

		//This will expand ranges slightly so that points at the edge will move inside
		//and number will not be cut by borders.
		Range oldRange = plot.getRangeAxis().getRange();
		Range newRange = Range.expand(oldRange, 0.1, 0.1);
		plot.getRangeAxis().setRange(newRange);

		oldRange = plot.getDomainAxis().getRange();
		newRange = Range.expand(oldRange, 0.1, 0.1);
		plot.getDomainAxis().setRange(newRange);

		return chart;
	}



     /**
     * Generates chart object from specified filters and options.
     * This chart then can be rendered as image or pdf or file.
     * @param opt
     * @param filter
     * @return chart
     * @throws DgException
     */
    public static JFreeChart getPledgesCommDisbChart(ChartOption opt, FilterHelper filter) throws DgException, WorkerException {
        JFreeChart chart = null;
        Font titleFont = new Font("Arial", Font.BOLD, 12);
        Font plainFont = new Font("Arial", Font.PLAIN, 10);
        CategoryDataset dataset = getPledgesCommDisbDataset(filter,opt);
        DecimalFormat format=FormatHelper.getDecimalFormat();
        String amount="";
        if ("true".equals(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS))) {
            amount = "Amounts in thousands";
        } else {
            amount = "Amounts";
        }
        String amountTranslatedTitle=TranslatorWorker.translateText(amount, opt.getLangCode(), opt.getSiteId());
        String titleMsg= TranslatorWorker.translateText("Pledges|Commitments|Disbursements", opt.getLangCode(), opt.getSiteId())+"("+filter.getCurrName()+")";
        chart = ChartFactory.createBarChart3D(titleMsg, "", amountTranslatedTitle, dataset, PlotOrientation.VERTICAL, true, true, false);
        chart.getTitle().setFont(titleFont);
        chart.getLegend().setItemFont(plainFont);
        // get a reference to the plot for further customisation...
        CategoryPlot plot = chart.getCategoryPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);
        NumberAxis numberAxis = (NumberAxis) plot.getRangeAxis();
        numberAxis.setNumberFormatOverride(format);
        numberAxis.setLabelFont(plainFont);
        numberAxis.setTickLabelFont(plainFont);
        CategoryAxis categoryAxis=plot.getDomainAxis();
        categoryAxis.setTickLabelFont(plainFont);
        renderer.setSeriesPaint(0,new Color(0,0,255));
	renderer.setSeriesPaint(1,new Color(0,204,255));
	renderer.setSeriesPaint(2,new Color(204,255,255));
        renderer.setItemMargin(0);
        return chart;
    }




     /**
     * Generates category dataset using  filters .
     * This chart then can be rendered as image or pdf or file.
     * @param filter
     * @return CategoryDataset
     * @throws DgException
     */


    private static CategoryDataset getPledgesCommDisbDataset(FilterHelper filter,ChartOption opt) throws DgException, WorkerException {
        boolean nodata = true; // for displaying no data message
        DefaultCategoryDataset result = new DefaultCategoryDataset();
        Long year = filter.getYear();
        if (year == null || year == -1) {
            year = Long.parseLong(FeaturesUtil.getGlobalSettingValue("Current Fiscal Year"));
        }

        Long currId = filter.getCurrId();
        String currCode;
        if (currId == null) {
            currCode = "USD";
        } else {
            currCode = CurrencyUtil.getCurrency(currId).getCurrencyCode();
        }
        Long fiscalCalendarId = filter.getFiscalCalendarId();
        String pledgesTranslatedTitle=TranslatorWorker.translateText("Pledges", opt.getLangCode(), opt.getSiteId());
        String actComTranslatedTitle=TranslatorWorker.translateText("Actual commitments", opt.getLangCode(), opt.getSiteId());
        String actDisbTranslatedTitle=TranslatorWorker.translateText("Actual disbursements", opt.getLangCode(), opt.getSiteId());
        for (int i = year.intValue() - 2; i <= year.intValue(); i++) {
            // apply calendar filter
            Date startDate = OrgProfileUtil.getStartDate(fiscalCalendarId, i);
            Date endDate =OrgProfileUtil.getEndDate(fiscalCalendarId, i);
            Double fundingPledge = getPledgesFunding(filter.getOrgIds(), filter.getOrgGroupId(), startDate, endDate, currCode);
            result.addValue(fundingPledge.doubleValue(), pledgesTranslatedTitle, new Long(i));
            DecimalWraper fundingComm = getFunding(filter.getOrgIds(), filter.getOrgGroupId(), startDate, endDate, currCode, true, filter.getTeamMember());
            result.addValue(fundingComm.doubleValue(), actComTranslatedTitle, new Long(i));
            DecimalWraper fundingDisb = getFunding(filter.getOrgIds(), filter.getOrgGroupId(), startDate, endDate, currCode, false, filter.getTeamMember());
            result.addValue(fundingDisb.doubleValue(), actDisbTranslatedTitle, new Long(i));
            if (fundingPledge.doubleValue() != 0 || fundingComm.doubleValue() != 0 || fundingDisb.doubleValue() != 0) {
                nodata = false;
            }

        }
        if (nodata) {
            result = new DefaultCategoryDataset();
        }

        return result;
    }


    /**
     * Generates chart dataset from AMP data.
     * This dataset is required to generate chart object.
     * Because this chart show changes of some values in time we use time series objects.
     * It creates two lines (TimeSeries): one line is desired change of values, which connects base value to target value,
     * and another line is actual change of indicator value, it connects all actual value points.
     * So we go through values of the indicator and put them in the correct TimeSeries objects.
     * Correctness of the resulting chart depends on correctness of data.
     * @param indicator
     * @return
     * @throws DgException
     */
	public static TimeSeriesCollection getIndicatorChartDataset(IndicatorSector indicator) throws DgException{
		TimeSeriesCollection ds = new TimeSeriesCollection();
		if (indicator.getValues()!=null && indicator.getValues().size()>0){
			TimeSeries tsActual = new TimeSeries("Actual");
			TimeSeries tsTarget = new TimeSeries("Target");
			for (AmpIndicatorValue value : indicator.getValues()) {
				if (value.getValueType()==AmpIndicatorValue.ACTUAL){
					tsActual.addOrUpdate(new Day(value.getValueDate()),value.getValue());
				}
				if (value.getValueType()==AmpIndicatorValue.BASE){
					tsTarget.addOrUpdate(new Day(value.getValueDate()),value.getValue());
				}
				if (value.getValueType()==AmpIndicatorValue.TARGET){
					tsTarget.addOrUpdate(new Day(value.getValueDate()),value.getValue());
				}
			}
			ds.addSeries(tsActual);
			ds.addSeries(tsTarget);
		}
		return ds;
	}

	/**
	 * Returns all chart widgets.
	 * @return
	 * @throws DgException
	 */
	@SuppressWarnings("unchecked")
	public static List<AmpWidgetIndicatorChart> getAllIndicatorChartWidgets() throws DgException{
		Session session = PersistenceManager.getRequestDBSession();
		String oql="from "+AmpWidgetIndicatorChart.class.getName();
		List<AmpWidgetIndicatorChart> result = null;
		try {
			Query query = session.createQuery(oql);
			result = (List<AmpWidgetIndicatorChart>)query.list();
		} catch (Exception e) {
			logger.error(e);
			throw new DgException("Cannot load all chart widgets",e);
		}
		return result;
	}

	/**
	 * Returns chart widget that is assigned to specified place.
	 * @param place
	 * @return
	 * @throws DgException
	 */
	public static AmpWidgetIndicatorChart getIndicatorChartWidget(AmpDaWidgetPlace place) throws DgException{
		AmpWidget widget = WidgetUtil.getWidgetOnPlace(place.getId());
		if (widget==null) return null;
		return (AmpWidgetIndicatorChart)widget;
	}

	/**
	 * Loads indicator chart widget by its ID.
	 * @param widgetId
	 * @return
	 * @throws DgException
	 */
	public static AmpWidgetIndicatorChart getIndicatorChartWidget(Long widgetId) throws DgException{
		Session session = PersistenceManager.getRequestDBSession();
		AmpWidgetIndicatorChart result;
		try {
			result = (AmpWidgetIndicatorChart)session.load(AmpWidgetIndicatorChart.class, widgetId);
		} catch (HibernateException e) {
			logger.error(e);
			throw new DgException("Cannot load indicator chart widget",e);
		}
		return result;
	}

	/**
	 * Saves or creates indicator chart widget in db.
	 * @param widget
	 * @return
	 * @throws DgException
	 */
	public static AmpWidgetIndicatorChart saveOrUpdate(AmpWidgetIndicatorChart widget) throws DgException{
		Session session = PersistenceManager.getRequestDBSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.saveOrUpdate(widget);
			tx.commit();
		} catch (Exception e) {
			//System.out.println(e);
			if (tx !=null){
				try {
					tx.rollback();
				} catch (Exception e1) {
					//System.out.println(e1);
					throw new DgException("Cannot rallback chart widget save");
				}
			}
			throw new DgException("Cannot save chart widget");
		}
		return widget;
	}

	/**
	 * Removes indicator chart widget from db.
	 * @param widget
	 * @throws DgException
	 */
	public static void delete(AmpWidgetIndicatorChart widget) throws DgException{
		Session session = PersistenceManager.getRequestDBSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.delete(widget);
			tx.commit();
		} catch (Exception e) {
			logger.error(e);
			if (tx !=null){
				try {
					tx.rollback();
				} catch (Exception e1) {
					logger.error(e1);
					throw new DgException("Cannot rallback chart widget delete");
				}
			}
			throw new DgException("Cannot delete chart widget");
		}
	}

	/**
	 * Checks if there is an widget for specified indicator.
	 * This helps to prevent deletion of indicator while it is assigned to widgets.
	 * @param sectorIndicator
	 * @return true if widgets for the indicator is found in db. othervise false is returned.
	 * @throws DgException
	 */
	@SuppressWarnings("unchecked")
	public static boolean isWidgetForIndicator(IndicatorSector sectorIndicator) throws DgException{
		boolean result=false;
		Session session = PersistenceManager.getRequestDBSession();
		String oql = "from " + AmpWidgetIndicatorChart.class.getName();
		oql += " as w where w.indicator.id = :indId";
		try {
			Query query = session.createQuery(oql);
			query.setLong("indId", sectorIndicator.getId());
			List widgets = query.list();
			return (widgets!=null && widgets.size()>0);
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * Generates chart of sector-donor funding.
	 * @param donors ID's of donors to use in calculation
	 * @param fromYear
	 * @param opt chart options
	 * @return
	 * @throws DgException
	 * @throws WorkerException
	 */
	   public static JFreeChart getSectorByDonorChart(Long[] donors, Integer fromYear, Integer toYear, ChartOption opt) throws DgException, WorkerException {
        JFreeChart result = null;
        PieDataset ds = getSectorByDonorDataset(donors, fromYear, toYear, opt);
        String titleMsg = TranslatorWorker.translateText("Breakdown by Sector", opt.getLangCode(), opt.getSiteId());
        String title = (opt.isShowTitle()) ? titleMsg : null;
        boolean tooltips = true;
        boolean urls = true;
        result = ChartFactory.createPieChart(title, ds, opt.isShowLegend(), tooltips, urls);
        String donorString = "";
        if (donors != null) {
            donorString += "~donorId=" + getInStatment(donors);
        }
        String url = opt.getUrl() + "~startYear=" + fromYear + "~endYear=" + toYear + donorString;
        PiePlot plot = (PiePlot) result.getPlot();

        if (opt.isShowTitle()) {
            Font font = new Font(null, 0, 12);
            result.getTitle().setFont(font);
        }
       String pattern = "{0} {1} ({2})";
       if (opt.getLabelPattern() != null) {
               pattern = opt.getLabelPattern();
        }

        if (opt.isShowLabels()) {
        PieSectionLabelGenerator gen = new PieChartCustomLabelGenerator();
        plot.setLabelGenerator(gen);
        plot.setSimpleLabels(true);
        plot.setLabelBackgroundPaint(new Color(0, 0, 0, 0));
        plot.setLabelGap(0);
        plot.setLabelLinkMargin(0.05);
        plot.setLabelShadowPaint(null);
        plot.setLabelOutlinePaint(new Color(0, 0, 0, 0));
        } else {
            plot.setLabelGenerator(null);
        }

           //plot.setSectionOutlinesVisible(false);
           LegendTitle lt = result.getLegend();
           if (lt != null) {
               Font labelFont = new Font(null, Font.PLAIN, 9);
               lt.setItemFont(labelFont);
               plot.setLabelFont(labelFont);
               lt.setPosition(RectangleEdge.RIGHT);
               lt.setVerticalAlignment(VerticalAlignment.TOP);
               lt.setHorizontalAlignment(HorizontalAlignment.RIGHT);
               plot.setLegendItemShape(new Rectangle(10, 10));
               DecimalFormat format = FormatHelper.getDecimalFormat();
               format.setMaximumFractionDigits(0);
               PieSectionLabelGenerator genLegend = new PieChartLegendGenerator();
               plot.setLegendLabelGenerator(genLegend);
               plot.setLegendLabelToolTipGenerator(new StandardPieSectionLabelGenerator(pattern, format, new DecimalFormat("0.0%")));

           }
        DonorSectorPieChartURLGenerator urlGen = new DonorSectorPieChartURLGenerator(url);
        plot.setURLGenerator(urlGen);
        plot.setIgnoreNullValues(true);
        plot.setIgnoreZeroValues(true);
        return result;
    }

      /**
     * Generates chart object from specified filters and options.
     * This chart then can be rendered as image or pdf or file.
     * Each Pie slice contains funding information for the sector.
     * @param opt
     * @param filter
     * @return chart
     * @throws DgException
     */


    public static JFreeChart getSectorByDonorChart(ChartOption opt, FilterHelper filter) throws DgException, WorkerException {
        JFreeChart chart = null;
        Font titleFont = new Font("Arial", Font.BOLD, 12);
        Font plainFont = new Font("Arial", Font.PLAIN, 10);
        DefaultPieDataset dataset = getDonorSectorDataSet(filter);
        String transTypeName="";
        switch(filter.getTransactionType()){
            case org.digijava.module.aim.helper.Constants.COMMITMENT: transTypeName="Commitment";break;
            case org.digijava.module.aim.helper.Constants.DISBURSEMENT: transTypeName="Disbursement";break;
        }
        String transTypeNameTrn=TranslatorWorker.translateText(transTypeName,opt.getLangCode(),opt.getSiteId());

        chart = ChartFactory.createPieChart(TranslatorWorker.translateText("Primary Sector(s) Breakdown ",opt.getLangCode(),opt.getSiteId())+" ("+transTypeNameTrn+","+(filter.getYear()-1)+" | "+filter.getCurrName()+")", dataset, true, true, false);
        chart.getTitle().setFont(titleFont);
        LegendTitle legend = chart.getLegend();
        legend.setItemFont(plainFont);
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelFont(plainFont);
        String pattern = "{0} {1} ({2})";
        if (opt.getLabelPattern() != null) {
            pattern = opt.getLabelPattern();
        }

        DecimalFormat format = FormatHelper.getDecimalFormat();
        format.setMaximumFractionDigits(0);
        PieSectionLabelGenerator gen = new PieChartCustomLabelGenerator();
        plot.setLabelGenerator(gen);
        plot.setSimpleLabels(true);
        plot.setLabelBackgroundPaint(new Color(0, 0, 0, 0));
        plot.setLabelGap(0);
        plot.setLabelLinkMargin(0.05);
        plot.setLabelShadowPaint(null);
        plot.setLabelOutlinePaint(new Color(0, 0, 0, 0));
        legend.setPosition(RectangleEdge.LEFT);
        legend.setVerticalAlignment(VerticalAlignment.TOP);
        legend.setHorizontalAlignment(HorizontalAlignment.LEFT);
        plot.setLegendItemShape(new Rectangle(10,10));
        PieSectionLabelGenerator genLegend = new PieChartLegendGenerator();
        plot.setLegendLabelGenerator(genLegend);
        plot.setLegendLabelToolTipGenerator(new StandardPieSectionLabelGenerator(pattern, format, new DecimalFormat("0.0%")));
        return chart;
    }
      /**
     * Generates chart object from specified filters and options.
     * This chart then can be rendered as image or pdf or file.
     * Each Pie slice contains funding information for the region.
     * @param opt
     * @param filter
     * @return chart
     * @throws DgException
     */

       public static JFreeChart getRegionByDonorChart(ChartOption opt,FilterHelper filter) throws DgException, WorkerException {
     	JFreeChart chart = null;
        Font titleFont = new Font("Arial", Font.BOLD, 12);
        Font plainFont = new Font("Arial", Font.PLAIN, 10);

        DefaultPieDataset dataset=getDonorRegionalDataSet(filter);
        String transTypeName = "";
        switch (filter.getTransactionType()) {
            case org.digijava.module.aim.helper.Constants.COMMITMENT:
                transTypeName = "Commitment";
                break;
            case org.digijava.module.aim.helper.Constants.DISBURSEMENT:
                transTypeName = "Disbursement";
                break;
        }
        String transTypeNameTrn = TranslatorWorker.translateText(transTypeName, opt.getLangCode(), opt.getSiteId());
        chart = ChartFactory.createPieChart(TranslatorWorker.translateText("Regional Breakdown", opt.getLangCode(), opt.getSiteId()) + " (" + transTypeNameTrn + "," + (filter.getYear() - 1)  +" | "+filter.getCurrName()+")", dataset, true, true, false);
        chart.getTitle().setFont(titleFont);
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelFont(titleFont);
        String pattern = "{0} {1} ({2})";
        if (opt.getLabelPattern() != null) {
            pattern = opt.getLabelPattern();
        }
        DecimalFormat format = FormatHelper.getDecimalFormat();
        format.setMaximumFractionDigits(0);
        PieSectionLabelGenerator gen = new PieChartCustomLabelGenerator();
        plot.setLabelGenerator(gen);
        plot.setSimpleLabels(true);
        plot.setLabelBackgroundPaint(new Color(0, 0, 0, 0));
        plot.setLabelGap(0);
        plot.setLabelLinkMargin(0.05);
        plot.setLabelShadowPaint(null);
        plot.setLabelOutlinePaint(new Color(0, 0, 0, 0));
        plot.setLabelFont(plainFont);
        LegendTitle legend = chart.getLegend();
        legend.setPosition(RectangleEdge.LEFT);
        legend.setVerticalAlignment(VerticalAlignment.TOP);
        legend.setItemFont(plainFont);    
        plot.setLegendItemShape(new Rectangle(10,10));
        PieSectionLabelGenerator genLegend = new PieChartLegendGenerator();
        plot.setLegendLabelGenerator(genLegend);
        plot.setLegendLabelToolTipGenerator(new StandardPieSectionLabelGenerator(pattern, format, new DecimalFormat("0.0%")));
        return chart;
    }

	/**
	 * Generates dataset for sector-donor pie chart.
	 * @param donors
	 * @param fromYear fundings only for this year are used.
	 * @return
	 * @throws DgException
	 */
	public static PieDataset getSectorByDonorDataset(Long[] donors, Integer fromYear, Integer toYear,ChartOption opt) throws DgException{
		DefaultPieDataset ds = new DefaultPieDataset();
		Date fromDate = null;
		Date toDate = null;
		if (fromYear!=null && toYear!=null){
			/**
			 * we should get default calendar and from/to dates should be taken according to it.
			 * So for example, if some calendar's startMonth is 1st of July, we should take an interval from
			 * year's(parameter that is passed to function) 1st of July to the next year's 1st of July
			 */
			Long defaultCalendarId=new Long(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_CALENDAR));
			AmpFiscalCalendar calendar=FiscalCalendarUtil.getAmpFiscalCalendar(defaultCalendarId);
			fromDate=getStartOfYear(fromYear.intValue(),calendar.getStartMonthNum()-1,calendar.getStartDayNum());
			//we need data including the last day of toYear,this is till the first day of toYear+1
			int MILLISECONDS_IN_DAY = 1000 * 60 * 60 * 24;
			toDate =new Date(getStartOfYear(toYear.intValue()+1,calendar.getStartMonthNum()-1,calendar.getStartDayNum()).getTime()-MILLISECONDS_IN_DAY);
		}
        BigDecimal[] allFundingWrapper={BigDecimal.ZERO};// to hold whole funding value
		Collection<DonorSectorFundingHelper> fundings=getDonorSectorFunding(donors, fromDate, toDate,allFundingWrapper);
		if (fundings!=null){
            double otherFunfing=0;
            String otherIds="";
			for (DonorSectorFundingHelper funding : fundings) {
				Double percent = (funding.getFounding().divide(allFundingWrapper[0],RoundingMode.HALF_UP)).doubleValue();
                // the sectors which percent is less then 5% should be group in "Other"
                AmpSector sector = funding.getSector();

                if (percent >= 0.05) {
                    SectorHelper secHelper=new SectorHelper();
                    secHelper.setName(sector.getName());
                    secHelper.setIds(sector.getAmpSectorId().toString());
                    ds.setValue(secHelper, Math.round(funding.getFounding().doubleValue()));
                } else {
                	otherFunfing += funding.getFounding().doubleValue();
                        otherIds+=sector.getAmpSectorId()+",";
                }
			}
                    if (otherFunfing != 0) {
                        String otherSectors = "Other Sectors";
                        try {
                            otherSectors = TranslatorWorker.translateText("Other Sectors", opt.getLangCode(), opt.getSiteId());
                        } catch (WorkerException e) {
                            e.printStackTrace();
                        }
                        SectorHelper secHelper = new SectorHelper();
                        secHelper.setName(otherSectors);
                        secHelper.setIds(otherIds.substring(0,otherIds.length()-1));
                        ds.setValue(secHelper, Math.round(otherFunfing));
                    }
		}
		return ds;
	}

	public static Date getStartOfYear(int year, int month,int day){
		GregorianCalendar cal = new GregorianCalendar();
		cal.set(year, month, day, 0, 0, 0);
		return cal.getTime();
	}

	/**
	 * Returns collection of DonorSectorFundingHelper beans.
	 * Each of them represents funding of one particular sector.
	 * @param donorIDs
	 * @param fromDate
	 * @param toDate
	 * @param wholeFunding
	 * @return
	 * @throws DgException
	 */
	 public static Collection<DonorSectorFundingHelper> getDonorSectorFunding(Long donorIDs[],Date fromDate, Date toDate,BigDecimal[] wholeFunding) throws DgException {
    	Collection<DonorSectorFundingHelper> fundings=null;
		String oql ="select actSec.sectorId, sum(fd.transactionAmountInBaseCurrency*actSec.sectorPercentage*0.01)";
		oql += " from ";
		oql += AmpFundingDetail.class.getName() +
                        " as fd inner join fd.ampFundingId f ";
                oql +=  "   inner join f.ampActivityId act "+
                        " inner join act.sectors actSec "+
                        " inner join actSec.sectorId sec "+
                        " inner join actSec.activityId act "+
                        " inner join actSec.classificationConfig config ";
		oql += " where  fd.transactionType = 0 and fd.adjustmentType = 1 ";
		if (donorIDs != null && donorIDs.length > 0) {
			oql += " and (fd.ampFundingId.ampDonorOrgId in ("+ getInStatment(donorIDs) + ") ) ";
		}
		if (fromDate != null && toDate != null) {
			oql += " and (fd.transactionDate between :fDate and  :eDate ) ";
		}
        oql +=" and config.name='Primary' and act.team is not null ";
		oql += " group by actSec.sectorId ";

		Session session = PersistenceManager.getRequestDBSession();

		//search for grouped data
	    @SuppressWarnings("unchecked")
		List result = null;
		try {
			Query query = session.createQuery(oql);
			if (fromDate!=null && toDate!=null){
				query.setDate("fDate", fromDate);
				query.setDate("eDate", toDate);
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MMMM-dd hh:mm:ss");
				logger.debug("Filtering from "+df.format(fromDate)+" to "+df.format(toDate));
			}
			result = query.list();
		} catch (Exception e) {
			throw new DgException(
					"Cannot load sector fundings by donors from db", e);
		}

		//Process grouped data
		if (result != null) {
			Map<Long, DonorSectorFundingHelper> donors = new HashMap<Long, DonorSectorFundingHelper>();
			for (Object row : result) {
				Object[] rowData = (Object[]) row;
				//AmpOrganisation donor = (AmpOrganisation) rowData[0];
				AmpSector sector =(AmpSector) rowData[0];
				BigDecimal amt = new BigDecimal((Double)rowData[1]);
				BigDecimal amount =FeaturesUtil.applyThousandsForVisibility(amt);
                sector = SectorUtil.getTopLevelParent(sector);
				DonorSectorFundingHelper sectorFundngObj = donors.get(sector.getAmpSectorId());
				//if not create and add to map
				if (sectorFundngObj == null){
					sectorFundngObj = new DonorSectorFundingHelper(sector);
					donors.put(sector.getAmpSectorId(), sectorFundngObj);
				}
				//add amount to sector
				sectorFundngObj.addFunding(amount);
               //calculate whole funding information
               wholeFunding[0]=wholeFunding[0].add(amount);
			}
			fundings = donors.values();
		}
		return fundings;
	}

    public static Double getDonorSectorFunding(Long donorIDs[], Date fromDate, Date toDate, Long sectorIds[]) throws DgException {
        Double amount = 0d;
        Long[] sectIds = null;
        if (sectorIds != null) {
            for (Long sectId : sectorIds) {
                List<Long> ids = new ArrayList<Long>();
                ids.add(sectId);
                List<AmpSector> sectors = SectorUtil.getAllDescendants(sectId);
                for (AmpSector sector : sectors) {
                    ids.add(sector.getAmpSectorId());
                }
                sectIds = new Long[ids.size()];
                ids.toArray(sectIds);

            }
        }

        List result = getFunding(donorIDs, fromDate, toDate, sectIds);
        //Process grouped data
        if (result != null) {
            amount = ((Double) result.get(0));
        }
        if (amount == null) {
            amount = 0d;
        }
        return amount;


    }

       public static List getFunding(Long[] donorIDs, Date fromDate, Date toDate, Long[] sectorIds) throws DgException {
        String oql = "select   sum(fd.transactionAmountInBaseCurrency*actSec.sectorPercentage*0.01)";
        oql += " from ";
        oql += AmpFundingDetail.class.getName() + " as fd inner join fd.ampFundingId f ";
        oql += "   inner join f.ampActivityId act " + " inner join act.sectors actSec " + " inner join actSec.sectorId sec " + " inner join actSec.activityId act " + " inner join actSec.classificationConfig config ";
        oql += " where   fd.transactionType = 0 and fd.adjustmentType = 1 ";
        if (donorIDs != null && donorIDs.length > 0) {
            oql += " and (fd.ampFundingId.ampDonorOrgId in (" + getInStatment(donorIDs) + ") ) ";
        }
        if (fromDate != null && toDate != null) {
            oql += " and (fd.transactionDate between :fDate and  :eDate ) ";
        }
        if(sectorIds!=null){
              oql += " and actSec.sectorId in (" + getInStatment(sectorIds) + ") ";
        }
        oql += " and config.name='Primary' and act.team is not null ";
        Session session = PersistenceManager.getRequestDBSession();
        //search for grouped data
        @SuppressWarnings(value = "unchecked")
        List result = null;
        try {
            Query query = session.createQuery(oql);
            if (fromDate != null && toDate != null) {
                query.setDate("fDate", fromDate);
                query.setDate("eDate", toDate);
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MMMM-dd hh:mm:ss");
                logger.debug("Filtering from " + df.format(fromDate) + " to " + df.format(toDate));
            }
            result = query.list();
        } catch (Exception e) {
            throw new DgException("Cannot load sector fundings by donors from db", e);
        }
        return result;
    }


     /**
     * Generates Pie dataset using  filters .
     * This chart then can be rendered as image or pdf or file.
     * @param filter
     * @return CategoryDataset
     * @throws DgException
     */

    @SuppressWarnings("unchecked")
    public static DefaultPieDataset getDonorRegionalDataSet(FilterHelper filter) throws DgException {
        Long year = filter.getYear();
        Long orgGroupId = filter.getOrgGroupId();
        double regionalTotal = 0;
        if (year == null || year == -1) {
            year = Long.parseLong(FeaturesUtil.getGlobalSettingValue("Current Fiscal Year"));
        }
        year -= 1; // previous fiscal year
        Long currId = filter.getCurrId();
        String currCode;
        if (currId == null) {
            currCode = "USD";
        } else {
            currCode = CurrencyUtil.getCurrency(currId).getCurrencyCode();
        }
        Long[] orgIds = filter.getOrgIds();
        int transactionType = filter.getTransactionType();
        TeamMember teamMember = filter.getTeamMember();
        DefaultPieDataset ds = new DefaultPieDataset();


        // apply calendar filter
        Long fiscalCalendarId = filter.getFiscalCalendarId();
        Date startDate = OrgProfileUtil.getStartDate(fiscalCalendarId, year.intValue());
        Date endDate = OrgProfileUtil.getEndDate(fiscalCalendarId, year.intValue());
        /*
         * We are selecting regions which are funded
         * In selected year by the selected organization
         *
         */
        String oql = "select distinct reg  from ";
        oql += AmpFundingDetail.class.getName() +
                " as fd inner join fd.ampFundingId f ";
        oql += "   inner join f.ampActivityId act ";

        oql += " inner join act.locations loc inner join loc.location.regionLocation reg where " +
                " reg is not null  and fd.transactionType =:transactionType and  fd.adjustmentType = 1";
        if (orgIds == null) {
            if(orgGroupId!=-1){
                oql += getOrganizationQuery(true,orgIds);
            }
        } else {
            oql += getOrganizationQuery(false,orgIds);
        }
        oql += " and  (fd.transactionDate>=:startDate and fd.transactionDate<=:endDate)  ";
        oql += getTeamQuery(teamMember);
        Session session = PersistenceManager.getRequestDBSession();
        List<AmpCategoryValueLocations> regions = null;
        try {
            Query query = session.createQuery(oql);
            query.setDate("startDate", startDate);
            query.setDate("endDate", endDate);
            if (orgIds == null&&orgGroupId!=-1) {
                    query.setLong("orgGroupId", orgGroupId);
            }
            query.setLong("transactionType", transactionType);
            if (teamMember != null) {
                query.setLong("teamId", teamMember.getTeamId());

            }
            regions = query.list();
            Iterator<AmpCategoryValueLocations> regionIter = regions.iterator();
            while (regionIter.hasNext()) {
                //calculating funding for each region
                AmpCategoryValueLocations region = regionIter.next();

                /* query that creates new  AmpFundingDetail objects
                which amounts are calculated by multiplication
                of the region percent and amount value*/

                oql = "select new AmpFundingDetail(fd.transactionType,fd.adjustmentType,fd.transactionAmount,fd.transactionDate,fd.ampCurrencyId,loc.locationPercentage,fd.fixedExchangeRate) ";
                oql += " from ";
                oql += AmpFundingDetail.class.getName() +
                        " as fd inner join fd.ampFundingId f ";
                oql += "   inner join f.ampActivityId act inner join act.locations loc inner join " +
                        " loc.location.regionLocation reg ";

                oql += " where  fd.transactionType =:transactionType and  fd.adjustmentType = 1 ";
                if (orgIds == null) {
                    if(orgGroupId!=-1){
                       oql += getOrganizationQuery(true, orgIds);
                    }
                } else {
                    oql += getOrganizationQuery(false, orgIds);
                }
                oql += " and  (fd.transactionDate>=:startDate and fd.transactionDate<=:endDate)  and reg is not null and reg.id=  " + region.getId();
                oql += getTeamQuery(teamMember);
                query = session.createQuery(oql);
                query.setDate("startDate", startDate);
                query.setDate("endDate", endDate);
                if (orgIds == null&&orgGroupId!=-1) {
                    query.setLong("orgGroupId", orgGroupId);
                }
                query.setLong("transactionType", transactionType);
                if (teamMember != null) {
                    query.setLong("teamId", teamMember.getTeamId());

                }
                List<AmpFundingDetail> fundingDets = query.list();

                /*Newly created objects and   selected currency
                are passed doCalculations  method*/

                FundingCalculationsHelper cal = new FundingCalculationsHelper();
                cal.doCalculations(fundingDets, currCode);
                DecimalWraper total = null;

                /*Depending on what is selected in the filter
                we should return either actual commitments
                or actual Disbursement */

                if (transactionType == 0) {
                    total = cal.getTotActualComm();
                } else {
                    total = cal.getTotActualDisb();
                }
                ds.setValue(region.getName(), total.doubleValue());
                regionalTotal += total.doubleValue();
            }
            List<String> keys = ds.getKeys();
            Iterator<String> keysIter = keys.iterator();
            double othersValue = 0;
            while (keysIter.hasNext()) {
                String key = keysIter.next();
                Double value = (Double) ds.getValue(key);
                if (value / regionalTotal <= 0.05) {
                    othersValue += value;
                    ds.remove(key);
                }
            }
            if (othersValue > 0) {
                ds.setValue("Others", othersValue);
            }
        } catch (Exception e) {
            logger.error(e);
            throw new DgException("Cannot load sector fundings by donors from db", e);
        }
        return ds;

    }

    /**
     * Generates Pie dataset using  filters .
     * This chart then can be rendered as image or pdf or file.
     * The sectors which have funding less than 5% from total funding
     * are group to "Others"
     * @param filter
     * @return CategoryDataset
     * @throws DgException
     */

	@SuppressWarnings("unchecked")
	public static DefaultPieDataset getDonorSectorDataSet(FilterHelper filter) throws DgException {


        /* if user doesn't select year, currency we are
        taking this information from global settings value*/

        Long year = filter.getYear();
        if (year == null || year == -1) {
            year = Long.parseLong(FeaturesUtil.getGlobalSettingValue("Current Fiscal Year"));
        }
        year -= 1; // previous fiscal year
        Long currId = filter.getCurrId();
        String currCode;
        if (currId == null) {
            currCode = "USD";
        } else {
            currCode = CurrencyUtil.getCurrency(currId).getCurrencyCode();
        }
        Long[] orgIds = filter.getOrgIds();
        Long orgGroupId=filter.getOrgGroupId();
        int transactionType=filter.getTransactionType();
        TeamMember tm=filter.getTeamMember();

         // apply calendar filter
        Long fiscalCalendarId = filter.getFiscalCalendarId();
        Date startDate = OrgProfileUtil.getStartDate(fiscalCalendarId, year.intValue());
        Date endDate =OrgProfileUtil.getEndDate(fiscalCalendarId, year.intValue());
        DefaultPieDataset ds = new DefaultPieDataset();
        /*
         * We are selecting sectors which are funded
         * In selected year by the selected organization
         * We are interesting only primary sectors
         * From the activities which belonging to team
         *
         */

        String oql = "select distinct sec  from ";
        oql += AmpFundingDetail.class.getName() +
                " as fd inner join fd.ampFundingId f ";

        oql += "   inner join f.ampActivityId act " +
                " inner join act.sectors actSec " +
                " inner join actSec.sectorId sec " +
                " inner join actSec.activityId act " +
                " inner join actSec.classificationConfig config ";

        oql += "  where  " +
                " fd.adjustmentType = 1 and fd.transactionType =:transactionType  ";
        if (orgIds == null) {
            if(orgGroupId!=-1){
                oql += getOrganizationQuery(true,orgIds);
            }
        } else {
            oql += getOrganizationQuery(false,orgIds);
        }
        oql += " and  (fd.transactionDate>=:startDate and fd.transactionDate<:endDate)     and config.name='Primary' ";
        oql+=getTeamQuery(tm);
        Session session = PersistenceManager.getRequestDBSession();
        List<AmpSector> sectors = null;
        try {
            Query query = session.createQuery(oql);
             query.setDate("startDate", startDate);
             query.setDate("endDate", endDate);
            if (orgIds == null&&orgGroupId!=-1) {
                query.setLong("orgGroupId", orgGroupId);         
            }
            query.setLong("transactionType", transactionType);
            if(tm!=null){
                query.setLong("teamId", tm.getTeamId());

            }
            sectors = query.list();
             // to calculate funding for all sectors
            BigDecimal totAllSectors = BigDecimal.ZERO;
            Iterator<AmpSector> sectorIter = sectors.iterator();
            BigDecimal others = BigDecimal.ZERO;
            Map<Long, DonorSectorFundingHelper> sectorsMap = new HashMap<Long, DonorSectorFundingHelper>();
            while (sectorIter.hasNext()) {
                AmpSector sector = sectorIter.next();
                // calculate funding for each sector
                DecimalWraper amount = getSectorFunding(startDate, endDate, orgIds, orgGroupId, transactionType, sector.getAmpSectorId(), currCode, tm);
                AmpSector topLevelSector = SectorUtil.getTopLevelParent(sector);
                Long topSectorId = topLevelSector.getAmpSectorId();
                // getting object from map
                DonorSectorFundingHelper sectorFundngObj = sectorsMap.get(topSectorId);
                //if not create and add to map
                if (sectorFundngObj == null) {
                    sectorFundngObj = new DonorSectorFundingHelper(topLevelSector);
                    sectorsMap.put(topSectorId, sectorFundngObj);
                }
                if(amount!=null){
                    BigDecimal amt=new BigDecimal(amount.doubleValue());
                    //add amount to sector
                    sectorFundngObj.addFunding(amt);
                     // adding to total funding amount
                    totAllSectors=totAllSectors.add(amt);
                }


            }
            if (!sectorsMap.isEmpty()) {
                Collection<DonorSectorFundingHelper> secFundCol = sectorsMap.values();
                Iterator<DonorSectorFundingHelper> secFundColIter = secFundCol.iterator();
                while (secFundColIter.hasNext()) {
                    DonorSectorFundingHelper sectorFunding = secFundColIter.next();
                    Double percent = sectorFunding.getFounding().divide(totAllSectors,3,RoundingMode.HALF_UP).doubleValue();
                    // if percent is less than 5, group in "others"
                    if (percent>=0.05) {
                        ds.setValue(sectorFunding.getSector().getName(), sectorFunding.getFounding());
                    } else {
                        others=others.add(sectorFunding.getFounding());

                    }
                    }
                }
            if (others.doubleValue()>0) {
                ds.setValue("Others", others);
            }
        } catch (Exception e) {
            logger.error(e);
            throw new DgException("Cannot load sector fundings by donors from db", e);
        }
        return ds;

    }
    /**
     * Returns funding amount of the selected organization.
     * The method is creating new {@link AmpFundingDetail} objects
     * which amounts are calculated by multiplication
     * of the sector percent and amount value.
     * Newly created objects are passed  to {@link  FundingCalculationsHelper#doCalculations(java.util.Collection, java.lang.String) }
     * method, which is calculating total using selected currency
     *
     * @param year
     * @param orgId
     * @param transactionType
     * @param sectorId
     * @param currCode
     * @return Funding amount
     * @throws org.digijava.kernel.exception.DgException
     * @see AmpFundingDetail
     * @see FundingCalculationsHelper
     */

    @SuppressWarnings("unchecked")
	public static DecimalWraper getSectorFunding(Date startDate, Date endDate, Long[] orgIds, Long orgGroupId, int transactionType, Long sectorId, String currCode, TeamMember tm) throws DgException {
        Session session = PersistenceManager.getRequestDBSession();
        String oql = "select new AmpFundingDetail(fd.transactionType,fd.adjustmentType,fd.transactionAmount,fd.transactionDate,fd.ampCurrencyId,actSec.sectorPercentage,fd.fixedExchangeRate) ";
        oql += " from ";
        oql += AmpFundingDetail.class.getName() +
                " as fd inner join fd.ampFundingId f ";
        oql += "   inner join f.ampActivityId act " +
                " inner join act.sectors actSec " +
                " inner join actSec.sectorId sec " +
                " inner join actSec.activityId act " +
                " inner join actSec.classificationConfig config ";

        oql += "  where  " +
                "   fd.transactionType =:transactionType and  fd.adjustmentType = 1 ";
        oql += " and  (fd.transactionDate>=:startDate and fd.transactionDate<:endDate)    and config.name='Primary'  ";
        if (orgIds == null) {
            if (orgGroupId != -1) {
                oql += getOrganizationQuery(true, orgIds);
            }

        } else {
            oql += getOrganizationQuery(false, orgIds);
        }
        if (sectorId != null) {
            oql += " and  sec.ampSectorId=:sectorId  ";
        }
        oql += getTeamQuery(tm);
        Query query = session.createQuery(oql);
        query.setDate("startDate", startDate);
        query.setDate("endDate", endDate);
        if (orgIds == null&&orgGroupId!=-1) {
            query.setLong("orgGroupId", orgGroupId);
        }
        query.setLong("transactionType", transactionType);
        if (sectorId != null) {
            query.setLong("sectorId", sectorId);
        }
        if (tm != null) {
            query.setLong("teamId", tm.getTeamId());

        }
        List<AmpFundingDetail> fundingDets = query.list();
        FundingCalculationsHelper cal = new FundingCalculationsHelper();
        cal.doCalculations(fundingDets, currCode);
        DecimalWraper total = null;

        /*Depending on what is selected in the filter
        we should return either actual commitments
        or actual Disbursement */

        if (transactionType == 0) {
            total = cal.getTotActualComm();
        } else {
            total = cal.getTotActualDisb();
        }
        return total;

    }
    /**
     * Returns funding amount
     * @param orgID
     * @param year
     * @param financingInstrumentId
     * @param currCode
     * @param transactionType
     * @return
     * @throws org.digijava.kernel.exception.DgException
     */



	   @SuppressWarnings("unchecked")
	public static DecimalWraper getFundingByFinancingInstrument(Long[] orgIds, Long orgGroupId, Date startDate,Date endDate, Long financingInstrumentId, String currCode, int transactionType, TeamMember tm) throws DgException {
        DecimalWraper total = null;
        String oql = "select fd ";
        oql += " from ";
        oql += AmpFundingDetail.class.getName() +
                " as fd inner join fd.ampFundingId f ";
        oql += "   inner join f.ampActivityId act ";

        oql += " where  fd.transactionType =:transactionType and  fd.adjustmentType = 1  ";

        if (orgIds == null) {
            if(orgGroupId!=-1){
               oql += getOrganizationQuery(true,orgIds);
            }
        } else {
            oql += getOrganizationQuery(false,orgIds);
        }

        oql += " and  (fd.transactionDate>=:startDate and fd.transactionDate<=:endDate)  ";

        oql += "   and f.financingInstrument=:financingInstrumentId ";
        oql += getTeamQuery(tm);


        Session session = PersistenceManager.getRequestDBSession();
        List<AmpFundingDetail> fundingDets = null;
        try {
            Query query = session.createQuery(oql);
            query.setDate("startDate", startDate);
            query.setDate("endDate", endDate);
            if (orgIds == null&&orgGroupId!=-1) {
                 query.setLong("orgGroupId", orgGroupId);
            }
            query.setLong("transactionType", transactionType);
            query.setLong("financingInstrumentId", financingInstrumentId);
            if (tm != null) {
                query.setLong("teamId", tm.getTeamId());

            }
            fundingDets = query.list();

            /*the objects retuned by query  and   selected currency
            are passed doCalculations  method*/
            FundingCalculationsHelper cal = new FundingCalculationsHelper();
            cal.doCalculations(fundingDets, currCode);

            /*Depending on what is selected in the filter
            we should return either actual commitments
            or actual Disbursement */

            if (transactionType == 0) {
                total = cal.getTotActualComm();
            } else {
                total = cal.getTotActualDisb();
            }


        } catch (Exception e) {
            logger.error(e);
            throw new DgException(
                    "Cannot load sector fundings by donors from db", e);
        }


        return total;
    }
    /**
     * Returns funding amount
     * @param orgID
     * @param year
     * @param assistanceTypeId
     * @param currCode
     * @param transactionType
     * @return
     * @throws org.digijava.kernel.exception.DgException
     */



	   @SuppressWarnings("unchecked")
	public static DecimalWraper getFunding(Long[] orgIds, Long orgGroupId, Date startDate,Date endDate, Long assistanceTypeId, String currCode, int transactionType, TeamMember tm) throws DgException {
        DecimalWraper total = null;
        String oql = "select fd ";
        oql += " from ";
        oql += AmpFundingDetail.class.getName() +
                " as fd inner join fd.ampFundingId f ";
        oql += "   inner join f.ampActivityId act ";

        oql += " where  fd.transactionType =:transactionType and  fd.adjustmentType = 1 ";
        if (orgIds == null ) {
            if (orgGroupId != -1) {
                oql += getOrganizationQuery(true,orgIds);
            } 
        } else {
                oql += getOrganizationQuery(false,orgIds);
        }

        oql += " and  (fd.transactionDate>=:startDate and fd.transactionDate<=:endDate)  ";

        oql += "  and f.typeOfAssistance=:assistanceTypeId ";

        oql += getTeamQuery(tm);

        Session session = PersistenceManager.getRequestDBSession();
        List<AmpFundingDetail> fundingDets = null;
        try {
            Query query = session.createQuery(oql);
            query.setDate("startDate", startDate);
            query.setDate("endDate", endDate);
            if (orgIds == null&&orgGroupId!=-1) {
                  query.setLong("orgGroupId", orgGroupId);
            }
            query.setLong("assistanceTypeId", assistanceTypeId);
            query.setLong("transactionType", transactionType);
            if (tm != null) {
                query.setLong("teamId", tm.getTeamId());

            }
            fundingDets = query.list();
            /*the objects retuned by query  and   selected currency
            are passed doCalculations  method*/
            FundingCalculationsHelper cal = new FundingCalculationsHelper();
            cal.doCalculations(fundingDets, currCode);
            /*Depending on what is selected in the filter
            we should return either actual commitments
            or actual Disbursement */
            if (transactionType == 0) {
                total = cal.getTotActualComm();
            } else {
                total = cal.getTotActualDisb();
            }

        } catch (Exception e) {
            logger.error(e);
            throw new DgException(
                    "Cannot load sector fundings by donors from db", e);
        }


        return total;
    }
    /**
     * Returns pledge amount in selected currency
     * for selected organization and year
     * @param orgID
     * @param year
     * @param currCode
     * @return
     * @throws org.digijava.kernel.exception.DgException
     */



    @SuppressWarnings("unchecked")
	public static double getPledgesFunding(Long[] orgIds, Long orgGroupId,  Date startDate,Date endDate, String currCode) throws DgException {
        double totalPlannedPldges = 0;
        String oql = "select fd ";
        oql += " from ";
        oql += AmpOrganisation.class.getName() +
                " org inner join org.fundingDetails fd ";
        oql += " where    fd.adjustmentType = 0 ";
        if (orgIds == null) {
           if(orgGroupId!=-1){
               oql += " and  org.orgGrpId.ampOrgGrpId=:orgGroupId ";
           }
        } else {
            oql += " and org.ampOrgId in ("+getInStatment(orgIds)+") ";
        }
        oql += " and fd.date>=:startDate and fd.date<=:endDate ";
        Session session = PersistenceManager.getRequestDBSession();
        List<AmpPledge> fundingDets = null;
        try {
            Query query = session.createQuery(oql);
            query.setDate("startDate", startDate);
            query.setDate("endDate", endDate);
            if (orgIds == null&&orgGroupId!=-1) {
                query.setLong("orgGroupId", orgGroupId);
            }
            fundingDets = query.list();
            Iterator<AmpPledge> fundDetIter = fundingDets.iterator();
            while (fundDetIter.hasNext()) {
                AmpPledge pledge = fundDetIter.next();
                //converting amounts
                java.sql.Date dt = new java.sql.Date(pledge.getDate().getTime());
                double frmExRt = Util.getExchange(pledge.getCurrency().getCurrencyCode(), dt);
                double toExRt = Util.getExchange(currCode, dt);
                DecimalWraper amt = CurrencyWorker.convertWrapper(pledge.getAmount(), frmExRt, toExRt, dt);
                totalPlannedPldges += amt.doubleValue();
            }



        } catch (Exception e) {
            logger.error(e);
            throw new DgException(
                    "Cannot load sector fundings by donors from db", e);
        }


        return totalPlannedPldges;
    }

    public static String getTeamQuery(TeamMember teamMember) {
        String qr = "";
        if (teamMember != null) {
            AmpTeam team = TeamUtil.getAmpTeam(teamMember.getTeamId());
            if (team.getComputation() != null && team.getComputation()) {
                String ids =getComputationOrgsQry(team);
                if (ids.length() > 1) {
                    ids = ids.substring(0, ids.length() - 1);
                    qr += "  and ( act.team.ampTeamId =:teamId or  f.ampDonorOrgId in(" + ids + ")) ";
                }
            } else {
                if (!teamMember.getTeamAccessType().equals("Management")) {
                    qr += " and ( act.team.ampTeamId =:teamId ) ";
                } else {
                    qr = " and ( act.team.ampTeamId =:teamId or ";
                    Collection<AmpTeam> childrenTeams = TeamUtil.getAllChildrenWorkspaces(team.getAmpTeamId());
                    Iterator<AmpTeam> iter = childrenTeams.iterator();
                    String teamIds = "";
                    String orgIds = "";
                    while (iter.hasNext()) {
                        AmpTeam childTeam = iter.next();
                        orgIds+=getComputationOrgsQry(childTeam);
                        teamIds+=childTeam.getAmpTeamId()+",";
                    }

                    if (teamIds.length() > 1) {
                        teamIds = teamIds.substring(0, teamIds.length() - 1);
                        qr += "  act.team.ampTeamId in ( " + teamIds + ")";
                    }
                    if (orgIds.length() > 1) {
                        orgIds = orgIds.substring(0, orgIds.length() - 1);
                        qr+=" or f.ampDonorOrgId in(" +  orgIds + ")";
                    }


                    qr += " ) and act.draft=false and act.approvalStatus in ('approved','edited') ";
                }

            }

        } else {
            qr += "  and act.team is not null ";
        }
        return qr;
    }

    public static String getComputationOrgsQry(AmpTeam team) {
        String orgIds = "";
        if (team.getComputation() != null && team.getComputation()) {
            Set<AmpOrganisation> orgs = team.getOrganizations();
            Iterator<AmpOrganisation> orgIter = orgs.iterator();
            while (orgIter.hasNext()) {
                AmpOrganisation org = orgIter.next();
                orgIds += org.getAmpOrgId() + ",";
            }

        }
        return orgIds;
    }

	public static String getOrganizationQuery(boolean orgGroupView,Long[] orgIds) {
		String qry = "";
		if (orgGroupView) {
			qry = " and  f.ampDonorOrgId.orgGrpId.ampOrgGrpId=:orgGroupId ";
		} else {
			qry = " and f.ampDonorOrgId in ("+getInStatment(orgIds)+") ";
		}
		return qry;
	}

      /**
       * Returns actual or planned amount in selected currency
       * for selected organization and year
       * @param orgID
       * @param year
       * @param currCode
       * @param isComm
       * @return
       * @throws org.digijava.kernel.exception.DgException
       */
    @SuppressWarnings("unchecked")
	public static DecimalWraper getFunding(Long[] orgIds, Long orgGroupId, Date startDate,Date endDate, String currCode, boolean isComm, TeamMember tm) throws DgException {
        String oql = "select fd ";
        oql += " from ";
        oql += AmpFundingDetail.class.getName() +
                " as fd inner join fd.ampFundingId f ";
        oql += "   inner join f.ampActivityId act ";

        oql += " where  fd.adjustmentType = 1 ";
        if (orgIds == null) {
            if(orgGroupId!=-1){
               oql += getOrganizationQuery(true,orgIds);
            }
        } else {
            oql += getOrganizationQuery(false,orgIds);
        }
        oql += " and  (fd.transactionDate>=:startDate and fd.transactionDate<=:endDate)  ";
        oql += getTeamQuery(tm);


        Session session = PersistenceManager.getRequestDBSession();
        List<AmpFundingDetail> fundingDets = null;
        try {
            Query query = session.createQuery(oql);
            query.setDate("startDate", startDate);
            query.setDate("endDate", endDate);
             if (orgIds == null&&orgGroupId!=-1) {
                  query.setLong("orgGroupId", orgGroupId);
             }
            if (tm != null) {
                query.setLong("teamId", tm.getTeamId());

            }
            fundingDets = query.list();
            /*the objects retuned by query  and   selected currency
            are passed doCalculations  method*/
            FundingCalculationsHelper cal = new FundingCalculationsHelper();
            cal.doCalculations(fundingDets, currCode);
            if (isComm) {
                return cal.getTotActualComm();
            } else {
                return cal.getTotActualDisb();
            }

        } catch (Exception e) {
            logger.error(e);
            throw new DgException(
                    "Cannot load sector fundings by donors from db", e);
        }
    }

    public static BigDecimal convert(BigDecimal amount,AmpCurrency originalCurrency){
    	try {
			CurrencyWorker.convertToUSD(amount, originalCurrency.getCurrencyCode());
		} catch (AimException e) {
			//e.printStackTrace();
			return amount;
		}
    	return amount;
    }

    public static BigDecimal calculatePercentage(BigDecimal amount,Float percentage){
    	BigDecimal result=amount.multiply(new BigDecimal(percentage)).divide(new BigDecimal(100));
    	return result;
    }

    public static String getInStatment(Long ids[]){
    	String oql="";
		for (int i = 0; i < ids.length; i++) {
			oql+=""+ids[i];
			if (i<ids.length-1){
				oql+=",";
			}
		}
    	return oql;
    }

    /**
     * Key worker for donors
     * @author Irakli Kobiashvili
     *
     */
    public static class DonorIdWorker implements KeyWorker<Long, AmpOrganisation>{

		public void markKeyForRemoval(AmpOrganisation element) {
			//empty, no need yet
		}

		@SuppressWarnings("deprecation")
		public void updateKey(AmpOrganisation element, Long newKey) {
			element.setAmpOrgId(newKey);
		}

		public Long resolveKey(AmpOrganisation element) {
			return element.getAmpOrgId();
		}

    }
    //from field indicates whether we are taking years for "From Year" dropdown
    public static List<LabelValueBean> getYears(boolean from){
    	//get default calendar
    	Long defaultCalendarId=new Long(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_CALENDAR));
		AmpFiscalCalendar defaultCalendar=FiscalCalendarUtil.getAmpFiscalCalendar(defaultCalendarId);
		//get current year
		Calendar cal=Calendar.getInstance();
		Integer toYear=new Integer(cal.get(java.util.Calendar.YEAR));
		// get startYear from Global Settings
		Integer fromYear=new Integer(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.YEAR_RANGE_START)) ;
		if(fromYear>toYear){
			fromYear=toYear;
		}
		if(!from){
			toYear++;
		}
    	return getYears(fromYear.intValue(),toYear.intValue(),defaultCalendar.getIsFiscal());
    }

    public static List<LabelValueBean> getYears(int fromYear,int toYear,boolean isCalendarFiscal){
    	List<LabelValueBean> years=new ArrayList<LabelValueBean>();
    	for (int year=fromYear;year<=toYear;year++) {
    		String label=null;
    		if(isCalendarFiscal){
    			String nextYear=new Integer(year+1).toString();
    			label="FY";
    			label+=" "+year+"/"+nextYear.substring(2);
    		}else{
    			label=""+year;
    		}
    		LabelValueBean lvb=new LabelValueBean(label,new Integer(year).toString());
    		years.add(lvb);
		}
    	return years;
    }

}
