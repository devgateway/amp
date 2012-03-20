package org.digijava.module.widget.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.IndicatorSector;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.CurrencyWorker;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.logic.FundingCalculationsHelper;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DecimalWraper;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.FiscalCalendarUtil;
import org.digijava.module.aim.util.LocationUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.fundingpledges.dbentity.FundingPledges;
import org.digijava.module.fundingpledges.dbentity.FundingPledgesDetails;
import org.digijava.module.fundingpledges.dbentity.FundingPledgesLocation;
import org.digijava.module.fundingpledges.dbentity.PledgesEntityHelper;
import org.digijava.module.orgProfile.helper.FilterHelper;
import org.digijava.module.orgProfile.helper.PieChartCustomLabelGenerator;
import org.digijava.module.orgProfile.helper.PieChartCustomLabelGeneratorUnrestricted;
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
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieToolTipGenerator;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.RingPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.Range;
import org.jfree.data.UnknownKeyException;
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
import org.jfree.ui.TextAnchor;
import org.jfree.ui.VerticalAlignment;
import org.jfree.util.SortOrder;

/**
 * Chart widgets util.
 * @author Irakli Kobiashvili
 *
 */
public class ChartWidgetUtil {

	//Color Scheme
	static Color[] colorsColor = {
		new Color(0x49, 0x78, 0xb1),
		new Color(0xb3, 0x4a, 0x47),
		new Color(0x91, 0xaf, 0x53),
		new Color(0x77, 0x5d, 0x97),
		new Color(0x46, 0xa1, 0xb9),
		new Color(0xe7, 0x8c, 0x41),
		new Color(0x7e, 0x9b, 0xc8),
		new Color(0xca, 0x7e, 0x7d),
		new Color(0xae, 0xc6, 0x83),
		new Color(0x9b, 0x89, 0xb3),
		new Color(0x3c, 0x64, 0x94),
		new Color(0x96, 0x3d, 0x3b),
		new Color(0x79, 0x92, 0x44),
		new Color(0x63, 0x4d, 0x7e),
		new Color(0x39, 0x86, 0x9b),
		new Color(0xc2, 0x75, 0x35)
	};

	static Color[] colorsMonochrome = {
		new Color(0x55, 0x55, 0x55),
		new Color(0x9e, 0x9e, 0x9e),
		new Color(0x72, 0x72, 0x72),
		new Color(0x46, 0x46, 0x46),
		new Color(0x83, 0x83, 0x83),
		new Color(0xc1, 0xc1, 0xc1),
		new Color(0x9a, 0x9a, 0x9a),
		new Color(0xc8, 0xc8, 0xc8),
		new Color(0xaa, 0xaa, 0xaa)
//		new Color(0x00, 0x00, 0x00),
////		new Color(0x19, 0x19, 0x19),
//		new Color(0x32, 0x32, 0x32),
////		new Color(0x4b, 0x4b, 0x4b),
//		new Color(0x64, 0x64, 0x64),
////		new Color(0x7d, 0x7d, 0x7d),
//		new Color(0x96, 0x96, 0x96),
////		new Color(0xaf, 0xaf, 0xaf),
//		new Color(0xc8, 0xc8, 0xc8),
////		new Color(0xe1, 0xe1, 0xe1),
//		new Color(0xfa, 0xfa, 0xfa)
	};

	private static Logger logger = Logger.getLogger(ChartWidgetUtil.class);

    /**
     * Generates chart object from specified indicator and options.
     * This chart then can be rendered as image or pdf or file.
     * @param indicatorCon
     * @param opt
     * @return
     * @throws DgException
     */
    public static JFreeChart getIndicatorChart(IndicatorSector indicatorCon, ChartOption opt) throws DgException {
		JFreeChart chart = null;
		Font font8 = new Font(null, 0, 8);
		TimeSeriesCollection ds = getIndicatorChartDataset(indicatorCon);
		boolean tooltips = false;
		boolean urls = false;
		// create time series line chart
        chart = ChartFactory.createTimeSeriesChart(opt.getTitle(), null, null, ds, opt.isShowLegend(), tooltips, urls);
		chart.getTitle().setFont(font8);
		if (opt.isShowLegend()) {
			chart.getLegend().setItemFont(font8);
		}
		RectangleInsets padding = new RectangleInsets(0, 0, 0, 0);
		chart.setPadding(padding);

		XYPlot plot = (XYPlot) chart.getPlot();
		plot.setRangeZeroBaselineVisible(true);
		plot.getDomainAxis().setLabelFont(font8);
		plot.getDomainAxis().setTickLabelFont(font8);
		plot.getRangeAxis().setLabelFont(font8);
		plot.getRangeAxis().setTickLabelFont(font8);

		XYItemRenderer renderer = plot.getRenderer();
		// renderer.setItemLabelFont(font8);
		renderer.setItemLabelsVisible(opt.isShowLabels());
        XYItemLabelGenerator labelGenerator = new StandardXYItemLabelGenerator("{2}", new DecimalFormat("0.00"), new DecimalFormat("#.####"));
		renderer.setBaseItemLabelGenerator(labelGenerator);

        //For next two lines see order of TimeSeries added to TimeSeriesCollection in getIndicatorChartDataset() below
        renderer.setSeriesPaint(0, Color.blue);//0 = Actual line, because this was added first.
        renderer.setSeriesPaint(1, Color.red);//1 = Target line, because this was added second.
		if (renderer instanceof XYLineAndShapeRenderer) {
			XYLineAndShapeRenderer r = (XYLineAndShapeRenderer) renderer;
			r.setBaseShapesVisible(true);
			r.setBaseShapesFilled(true);
		}

        //This will expand ranges slightly so that points at the edge will move inside
		// and number will not be cut by borders.
		Range oldRange = plot.getRangeAxis().getRange();
		Range newRange = Range.expand(oldRange, 0.1, 0.1);
		plot.getRangeAxis().setRange(newRange);

		oldRange = plot.getDomainAxis().getRange();
		newRange = Range.expand(oldRange, 0.1, 0.1);
		plot.getDomainAxis().setRange(newRange);

		return chart;
	}

   

    /**
     * Generates category dataset using  filters .
     * This chart then can be rendered as image or pdf or file.
     * @param filter
     * @return CategoryDataset
     * @throws DgException
     */
    private static CategoryDataset getPledgesCommDisbExpDataset(FilterHelper filter, ChartOption opt) throws DgException, WorkerException {
		boolean nodata = true; // for displaying no data message
		DefaultCategoryDataset result = new DefaultCategoryDataset();
		Long year = filter.getYear();
		BigDecimal divideByDenominator;

        if (filter.getDivideThousands())
        	divideByDenominator=new BigDecimal(1000000000);
        else
        	divideByDenominator=new BigDecimal(1000000);
        

        if ("true".equals(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS))) {
            if (filter.getDivideThousands())
            	divideByDenominator=new BigDecimal(1000000);
            else
            	divideByDenominator=new BigDecimal(1000);
        }

        if (year == null || year == -1) {
            year = Long.parseLong(FeaturesUtil.getGlobalSettingValue("Current Fiscal Year"));
        }

		Long currId = filter.getCurrId();
		String currCode;
		if (currId == null) {
			currCode = "USD";
		} else {
			currCode = CurrencyUtil.getAmpcurrency(filter.getCurrId()).getCurrencyCode();
		}
		int yearsInRange = filter.getYearsInRange() - 1;
		Long fiscalCalendarId = filter.getFiscalCalendarId();
        String pledgesTranslatedTitle = TranslatorWorker.translateText("Pledges", opt.getLangCode(), opt.getSiteId());
        String actComTranslatedTitle = TranslatorWorker.translateText("Actual commitments", opt.getLangCode(), opt.getSiteId());
        String actDisbTranslatedTitle = TranslatorWorker.translateText("Actual disbursements", opt.getLangCode(), opt.getSiteId());
        String actExpTranslatedTitle = TranslatorWorker.translateText("Actual expenditures", opt.getLangCode(), opt.getSiteId());

        Double totalPledges;
        BigDecimal totalCommitments, totalDisbursements, totalExpenditures;

        totalPledges = 0.0;
        totalCommitments = totalDisbursements = totalExpenditures = new BigDecimal(0);

        for (int i = year.intValue() - yearsInRange; i <= year.intValue(); i++) {
            // apply calendar filter
            Date startDate = OrgProfileUtil.getStartDate(fiscalCalendarId, i);
            Date endDate = OrgProfileUtil.getEndDate(fiscalCalendarId, i);
            Double fundingPledge=0d;
            if (filter.isPledgeVisible()) {
                fundingPledge = getPledgesFunding(filter.getOrgIds(), filter.getOrgGroupId(), i, currCode,filter.getLocationIds());
                result.addValue(fundingPledge / divideByDenominator.doubleValue(), pledgesTranslatedTitle, new Long(i));
            }
            DecimalWraper fundingComm = getFunding(filter, startDate, endDate, null, null, Constants.COMMITMENT,Constants.ACTUAL);
            result.addValue(fundingComm.getValue().divide(divideByDenominator), actComTranslatedTitle, new Long(i));
            DecimalWraper fundingDisb = getFunding(filter, startDate, endDate, null, null, Constants.DISBURSEMENT,Constants.ACTUAL);
            result.addValue(fundingDisb.getValue().divide(divideByDenominator), actDisbTranslatedTitle, new Long(i));
            DecimalWraper fundingExp=new DecimalWraper();
            if (filter.isExpendituresVisible()) {
                fundingExp = getFunding(filter, startDate, endDate, null, null, Constants.EXPENDITURE, Constants.ACTUAL);
                result.addValue(fundingExp.getValue().divide(divideByDenominator), actExpTranslatedTitle, new Long(i));
            }
            if (fundingPledge.doubleValue() != 0 || fundingComm.doubleValue() != 0 || fundingDisb.doubleValue() != 0||fundingExp.doubleValue()!=0) {
				nodata = false;
			}
            totalPledges += fundingPledge;
            totalCommitments = totalCommitments.add(fundingComm.getValue());
            totalDisbursements = totalDisbursements.add(fundingDisb.getValue());
            totalExpenditures = totalExpenditures.add(fundingExp.getValue());
		}

        if (totalPledges == 0.0&&filter.isPledgeVisible())
        	result.removeRow(pledgesTranslatedTitle);
        if (totalCommitments.equals(new BigDecimal(0)))
        	result.removeRow(actComTranslatedTitle);
        if (totalDisbursements.equals(new BigDecimal(0)))
        	result.removeRow(actDisbTranslatedTitle);
        if (totalExpenditures.equals(new BigDecimal(0))&&filter.isExpendituresVisible())
        	result.removeRow(actExpTranslatedTitle);
        
        
		if (nodata) {
			result = new DefaultCategoryDataset();
		}

		return result;
	}

    /**
     * Generates category dataset using  filters .
     * This chart then can be rendered as image or pdf or file.
     * @param filter
     * @return CategoryDataset
     * @throws DgException
     */
    private static CategoryDataset getTypeOfAidOdaProfileDataset(FilterHelper filter, ChartOption opt, boolean typeOfAid) throws DgException, WorkerException {
        boolean nodata = true; // for displaying no data message
        DefaultCategoryDataset result = new DefaultCategoryDataset();
        double divideByDenominator;

        if (filter.getDivideThousands()) {
        	divideByDenominator=1000000000;
        }
        else
        {
        	divideByDenominator=1000000;
        }
        

        if ("true".equals(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS))) {
            if (filter.getDivideThousands()) {
            	divideByDenominator=1000000;
            }
            else
            {
            	divideByDenominator=1000;
            }
        }
        Long year = filter.getYear();
        if (year == null || year == -1) {
            year = Long.parseLong(FeaturesUtil.getGlobalSettingValue("Current Fiscal Year"));
        }
        Long fiscalCalendarId = filter.getFiscalCalendarId();
        Collection<AmpCategoryValue> categoryValues = null;
        if (typeOfAid) {
            categoryValues = CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.TYPE_OF_ASSISTENCE_KEY);
        } else {
            categoryValues = CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.FINANCING_INSTRUMENT_KEY);
        }
        int yearsInRange=filter.getYearsInRange()-1;
        for (AmpCategoryValue categoryValue : categoryValues) {
            String title = TranslatorWorker.translateText(categoryValue.getValue(), opt.getLangCode(), opt.getSiteId());
            Double totalCategory = 0.0;

            for (Long i = year - yearsInRange; i <= year; i++) {
                // apply calendar filter
                Date startDate = OrgProfileUtil.getStartDate(fiscalCalendarId, i.intValue());
                Date endDate = OrgProfileUtil.getEndDate(fiscalCalendarId, i.intValue());
                DecimalWraper funding = null;
                if (typeOfAid) {
                    funding = ChartWidgetUtil.getFunding(filter, startDate, endDate, categoryValue.getId(), null, filter.getTransactionType(),Constants.ACTUAL);
                } else {
                    funding = ChartWidgetUtil.getFunding(filter, startDate, endDate, null, categoryValue.getId(), filter.getTransactionType(),Constants.ACTUAL);
                }
                result.addValue(funding.doubleValue()/divideByDenominator, title, i);
                totalCategory += funding.doubleValue();
                if (funding.doubleValue() != 0) {
                    nodata = false;
                }
            }
            //Check if there are values. If empty, remove the row.
            if (totalCategory == 0.0){
            	result.removeRow(title);
            }
        }
        if (nodata) {
            result = new DefaultCategoryDataset();
        }

        return result;
    }

    public static JFreeChart getBarChart(ChartOption opt, FilterHelper filter,int widgetType) throws DgException, WorkerException {
		JFreeChart chart = null;
		Font titleFont = new Font("Arial", Font.BOLD, 12);
		Font plainFont = new Font("Arial", Font.PLAIN, 10);
        Font subTitleFont = new Font("Arial", Font.BOLD, 11);
        CategoryDataset dataset =null;
        TextTitle subTitle=null;
        String titleType="";
        if (filter.getTransactionType() == 0) {
            titleType = "Commitments";
        } else {
            titleType = "Disbursements";
        }
        switch (widgetType) {

            case WidgetUtil.ORG_PROFILE_PLEDGES_COMM_DISB:
                dataset = ChartWidgetUtil.getPledgesCommDisbExpDataset(filter, opt);   
                subTitle = new TextTitle(filter.getCurrCode(), subTitleFont);
                break;
            case WidgetUtil.ORG_PROFILE_ODA_PROFILE:
                dataset = ChartWidgetUtil.getTypeOfAidOdaProfileDataset(filter, opt, false);
                titleType = "Actual" + " "+ titleType;
                titleType = TranslatorWorker.translateText(titleType, opt.getLangCode(), opt.getSiteId());
                subTitle = new TextTitle(titleType+" ("+filter.getCurrCode()+")", subTitleFont);
                break;
            case WidgetUtil.ORG_PROFILE_TYPE_OF_AID:
                dataset = ChartWidgetUtil.getTypeOfAidOdaProfileDataset(filter, opt, true);
                titleType = "Actual" + " "+ titleType ;
                titleType = TranslatorWorker.translateText(titleType, opt.getLangCode(), opt.getSiteId());
                subTitle = new TextTitle( titleType+" ("+filter.getCurrCode()+")" ,subTitleFont);
                break;
            case WidgetUtil.ORG_PROFILE_AID_PREDICTIBLITY:
                titleType = "Actual vs Planned" +" "+titleType;
                dataset = ChartWidgetUtil.getAidPredictiblityDataset(filter,opt);
                titleType = TranslatorWorker.translateText(titleType, opt.getLangCode(), opt.getSiteId());
                subTitle = new TextTitle(titleType+" ("+filter.getCurrCode()+")" ,subTitleFont);
                break;

        }
		DecimalFormat format = FormatHelper.getDecimalFormat();
        format.setMinimumFractionDigits(0);
		format.setMaximumFractionDigits(filter.getDivideThousandsDecimalPlaces());
        format.setMinimumIntegerDigits(1);

        DecimalFormat formatAxis = FormatHelper.getDecimalFormat();
        formatAxis.setMinimumFractionDigits(0);
		formatAxis.setMaximumFractionDigits(3);
        formatAxis.setMinimumIntegerDigits(1);

		DecimalFormat toolTipformat = FormatHelper.getDecimalFormat();
		toolTipformat.setMaximumFractionDigits(5);
        toolTipformat.setMinimumIntegerDigits(1);
		String pattern = "{0} {2} ({1})";
		if (opt.getLabelPattern() != null) {
			pattern = opt.getLabelPattern();
		}
		String amount = "";
        if (filter.getDivideThousands()) {
        	amount = "Amounts in Thousands of Millions";
        }
        else
        {
        	amount = "Amounts in Millions";
        }
        String amountTranslatedTitle = TranslatorWorker.translateText(amount, opt.getLangCode(), opt.getSiteId());

		ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());
		BarRenderer.setDefaultShadowsVisible(false);
		BarRenderer.setDefaultBarPainter(new StandardBarPainter());

        chart = ChartFactory.createBarChart(opt.getTitle(), "", amountTranslatedTitle, dataset, PlotOrientation.VERTICAL, true, true, false);
		chart.setBackgroundPaint(new Color(255, 255, 255, 0));
		TextTitle title = chart.getTitle();
		if (title != null) {
			title.setFont(titleFont);
		}
        if (subTitle != null) {
            subTitle.setPadding(5, 5, 5, 5);
            chart.addSubtitle(0, subTitle);
        }

		chart.getLegend().setItemFont(plainFont);
		chart.getLegend().setBorder(0, 0, 0, 0);
		chart.getLegend().setBackgroundPaint(new Color(255, 255, 255, 0));
		chart.setPadding(new RectangleInsets(0,0,0,0));

		// get a reference to the plot for further customisation...
		CategoryPlot plot = chart.getCategoryPlot();
		plot.setBackgroundPaint(Color.WHITE);
		BarRenderer renderer = (BarRenderer) plot.getRenderer();
		plot.setAxisOffset(new RectangleInsets(0,2,2,0));

		NumberAxis numberAxis = (NumberAxis) plot.getRangeAxis();
		numberAxis.setNumberFormatOverride(formatAxis);
		numberAxis.setLabelFont(subTitleFont);
		Range oldRange = numberAxis.getRange();
		Range newRange = Range.expand(oldRange, 0, 0.1);
		numberAxis.setRange(newRange);
		numberAxis.setTickLabelFont(plainFont);

		CategoryAxis categoryAxis = plot.getDomainAxis();
		categoryAxis.setTickLabelFont(plainFont);
		categoryAxis.setCategoryMargin(0.1);
		categoryAxis.setMinorTickMarksVisible(false);

		renderer.setItemMargin(0);
		renderer.setMaximumBarWidth(.045);
		renderer.setDrawBarOutline(true);
		renderer.setMinimumBarLength(0.1);

		// renderer.

		CategoryItemLabelGenerator labelGenerator = new WidgetCategoryItemLabelGenerator("{2}", format);
		for (int i = 0; i < dataset.getRowCount(); i++) {
			renderer.setSeriesOutlinePaint(i, new Color(0,0,0));
			
//			renderer.setSeriesOutlineStroke(i, new BasicStroke(1f));
			renderer.setSeriesItemLabelsVisible(i, true);
			renderer.setSeriesItemLabelGenerator(i, labelGenerator);
			renderer.setSeriesItemLabelFont(i, plainFont);
            renderer.setSeriesToolTipGenerator(i, new StandardCategoryToolTipGenerator(pattern,toolTipformat));
            renderer.setSeriesPaint(i, opt.isMonochrome() ? colorsMonochrome[i] : colorsColor[i]);
			renderer.setSeriesNegativeItemLabelPosition(i, new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.TOP_CENTER));
		}
		return chart;
	}

   

     /**
     * Generates category dataset using  filters .
     * This chart then can be rendered as image or pdf or file.
     * @param filter
     * @return CategoryDataset
     * @throws DgException
     */
    private static CategoryDataset getAidPredictiblityDataset(FilterHelper filter, ChartOption opt) throws DgException, WorkerException {
		boolean nodata = true; // for displaying no data message
		DefaultCategoryDataset result = new DefaultCategoryDataset();
		Long year = filter.getYear();
		BigDecimal divideByDenominator;

        if (filter.getDivideThousands())
        	divideByDenominator=new BigDecimal(1000000000);
        else
        	divideByDenominator=new BigDecimal(1000000);
        

        if ("true".equals(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS))) {
            if (filter.getDivideThousands())
            	divideByDenominator=new BigDecimal(1000000);
            else
            	divideByDenominator=new BigDecimal(1000);
        }


        if (year == null || year == -1) {
            year = Long.parseLong(FeaturesUtil.getGlobalSettingValue("Current Fiscal Year"));
        }

		Long currId = filter.getCurrId();
		String currCode;
		if (currId == null) {
			currCode = "USD";
		} else {
			currCode = CurrencyUtil.getAmpcurrency(filter.getCurrId()).getCurrencyCode();
		}
		int yearsInRange = filter.getYearsInRange() - 1;
		Long fiscalCalendarId = filter.getFiscalCalendarId();
        String plannedTitle = TranslatorWorker.translateText("Planned", opt.getLangCode(), opt.getSiteId());
        String actualTitle = TranslatorWorker.translateText("Actual", opt.getLangCode(), opt.getSiteId());
        for (int i = year.intValue() - yearsInRange; i <= year.intValue(); i++) {
            // apply calendar filter
            Date startDate = OrgProfileUtil.getStartDate(fiscalCalendarId, i);
            Date endDate = OrgProfileUtil.getEndDate(fiscalCalendarId, i);
            DecimalWraper fundingPlanned = getFunding(filter, startDate, endDate,null,null,filter.getTransactionType(), Constants.PLANNED);
            result.addValue(fundingPlanned.getValue().divide(divideByDenominator), plannedTitle, new Long(i));
            DecimalWraper fundingActual = getFunding(filter, startDate, endDate,null,null,filter.getTransactionType(), Constants.ACTUAL);
            result.addValue(fundingActual.getValue().divide(divideByDenominator), actualTitle, new Long(i));
            if (fundingPlanned.doubleValue() != 0 || fundingActual.doubleValue() != 0) {
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
    public static TimeSeriesCollection getIndicatorChartDataset(IndicatorSector indicator) throws DgException {
        TimeSeriesCollection ds = new TimeSeriesCollection();
        if (indicator.getValues() != null && indicator.getValues().size() > 0) {
            TimeSeries tsActual = new TimeSeries("Actual");
            TimeSeries tsTarget = new TimeSeries("Target");
            for (AmpIndicatorValue value : indicator.getValues()) {
                if (value.getValueType() == AmpIndicatorValue.ACTUAL) {
                    tsActual.addOrUpdate(new Day(value.getValueDate()), value.getValue());
                }
                if (value.getValueType() == AmpIndicatorValue.BASE) {
                    tsTarget.addOrUpdate(new Day(value.getValueDate()), value.getValue());
                }
                if (value.getValueType() == AmpIndicatorValue.TARGET) {
                    tsTarget.addOrUpdate(new Day(value.getValueDate()), value.getValue());
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
    public static List<AmpWidgetIndicatorChart> getAllIndicatorChartWidgets() throws DgException {
        Session session = PersistenceManager.getRequestDBSession();
        String oql = "from " + AmpWidgetIndicatorChart.class.getName();
        List<AmpWidgetIndicatorChart> result = null;
        try {
            Query query = session.createQuery(oql);
            result = (List<AmpWidgetIndicatorChart>) query.list();
        } catch (Exception e) {
            logger.error(e);
            throw new DgException("Cannot load all chart widgets", e);
        }
        return result;
    }

    /**
     * Returns chart widget that is assigned to specified place.
     * @param place
     * @return
     * @throws DgException
     */
    public static AmpWidgetIndicatorChart getIndicatorChartWidget(AmpDaWidgetPlace place) throws DgException {
        AmpWidget widget = WidgetUtil.getWidgetOnPlace(place.getId());
        if (widget == null) {
            return null;
        }
        return (AmpWidgetIndicatorChart) widget;
    }

    /**
     * Loads indicator chart widget by its ID.
     * @param widgetId
     * @return
     * @throws DgException
     */
    public static AmpWidgetIndicatorChart getIndicatorChartWidget(Long widgetId) throws DgException {
        Session session = PersistenceManager.getRequestDBSession();
        AmpWidgetIndicatorChart result;
        try {
            result = (AmpWidgetIndicatorChart) session.load(AmpWidgetIndicatorChart.class, widgetId);
        } catch (HibernateException e) {
            logger.error(e);
            throw new DgException("Cannot load indicator chart widget", e);
        }
        return result;
    }

    /**
     * Saves or creates indicator chart widget in db.
     * @param widget
     * @return
     * @throws DgException
     */
    public static AmpWidgetIndicatorChart saveOrUpdate(AmpWidgetIndicatorChart widget) throws DgException {
        Session session = PersistenceManager.getRequestDBSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.saveOrUpdate(widget);
            tx.commit();
        } catch (Exception e) {
            //System.out.println(e);
            if (tx != null) {
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
    public static void delete(AmpWidgetIndicatorChart widget) throws DgException {
        Session session = PersistenceManager.getRequestDBSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(widget);
            tx.commit();
        } catch (Exception e) {
            logger.error(e);
            if (tx != null) {
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
    public static boolean isWidgetForIndicator(IndicatorSector sectorIndicator) throws DgException {
        boolean result = false;
        Session session = PersistenceManager.getRequestDBSession();
        String oql = "from " + AmpWidgetIndicatorChart.class.getName();
        oql += " as w where w.indicator.id = :indId";
        try {
            Query query = session.createQuery(oql);
            query.setLong("indId", sectorIndicator.getId());
            List widgets = query.list();
            return (widgets != null && widgets.size() > 0);
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
		Font titleFont = new Font("Arial", Font.BOLD, 12);
		Font plainFont = new Font("Arial", Font.PLAIN, 10);

        PieDataset ds = getSectorByDonorDataset(donors, fromYear, toYear, opt);
        String titleMsg = TranslatorWorker.translateText("Breakdown by Sector", opt.getLangCode(), opt.getSiteId());
        String title = (opt.isShowTitle()) ? titleMsg : null;
        boolean tooltips = true;
        boolean urls = true;
        result = ChartFactory.createRingChart(title, ds, opt.isShowLegend(), tooltips, urls);
		result.setBackgroundPaint(new Color(255,255,255,0));
		String donorString = "";
		if (donors != null) {
			donorString += "~donorId=" + getInStatment(donors);
		}
		String url = opt.getUrl() + "~startYear=" + fromYear + "~endYear=" + toYear + donorString;
		RingPlot plot = (RingPlot) result.getPlot();
		plot.setOuterSeparatorExtension(0);
		plot.setInnerSeparatorExtension(0);
		plot.setBackgroundPaint(new Color(255,255,255,0));
		plot.setSectionDepth(0.5);
		plot.setCircular(true);
		plot.setOutlineVisible(false);
		plot.setShadowXOffset(0);
		plot.setShadowYOffset(0);

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

		// plot.setSectionOutlinesVisible(false);
		LegendTitle lt = result.getLegend();
		if (lt != null) {
			plot.setLabelFont(plainFont);
			plot.setLegendItemShape(new Rectangle(10, 10));
			DecimalFormat format = FormatHelper.getDecimalFormatNotRounded();
			format.setMaximumFractionDigits(0);
			PieSectionLabelGenerator genLegend = new PieChartLegendGenerator(150);
			plot.setLegendLabelGenerator(genLegend);
			plot.setToolTipGenerator(new StandardPieToolTipGenerator(pattern, format, new DecimalFormat("0.00%")));
			plot.setLegendLabelToolTipGenerator(new StandardPieSectionLabelGenerator(pattern, format, new DecimalFormat("0.0%")));
			lt.setItemFont(plainFont);
			lt.setFrame(BlockBorder.NONE);
			lt.setMargin(10, 5, 5, 10);
			lt.setPosition(RectangleEdge.BOTTOM);
			lt.setVerticalAlignment(VerticalAlignment.CENTER);
			lt.setHorizontalAlignment(HorizontalAlignment.CENTER);
			lt.setBackgroundPaint(new Color(255,255,255,0));

		}
		DonorSectorPieChartURLGenerator urlGen = new DonorSectorPieChartURLGenerator(url);
		plot.setURLGenerator(urlGen);
		plot.setIgnoreNullValues(true);
		plot.setIgnoreZeroValues(true);
		List <Comparable> keys = ds.getKeys();
        int aInt;
        Color[] colors = opt.isMonochrome() ? colorsMonochrome : colorsColor;       
        for (int i = 0; i < keys.size(); i++)
        {
            aInt = i % colors.length;
            plot.setSectionPaint(keys.get(i), colors[aInt]);
        }

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
    public static JFreeChart getDonutChart(ChartOption opt, FilterHelper filter,int widgetType,Long sectorClassConfigId) throws DgException, WorkerException {
		JFreeChart chart = null;
		Font titleFont = new Font("Arial", Font.BOLD, 12);
		Font plainFont = new Font("Arial", Font.PLAIN, 10);
		Font subTitleFont = new Font("Arial", Font.BOLD, 10);
		DefaultPieDataset dataset = null;
        if (WidgetUtil.ORG_PROFILE_SECTOR_BREAKDOWN == widgetType) {
            String otherTitle = TranslatorWorker.translateText("Other", opt.getLangCode(), opt.getSiteId());
            dataset = getDonorSectorDataSet(filter, sectorClassConfigId, otherTitle);
        } else {
            dataset = getDonorRegionalDataSet(filter, opt);
        }
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
        dataset.sortByKeys(SortOrder.ASCENDING);
        chart = ChartFactory.createRingChart(opt.getTitle(), dataset, true, true, false);
		chart.setBackgroundPaint(new Color(255,255,255,0));
		TextTitle title = chart.getTitle();
		if (title != null) {
			title.setFont(titleFont);
		}
        TextTitle subTitle = new TextTitle(transTypeNameTrn+" ("+filter.getCurrCode()+")",subTitleFont);
        Long fiscalCalendarId=filter.getFiscalCalendarId();
        AmpFiscalCalendar calendar = FiscalCalendarUtil.getAmpFiscalCalendar(fiscalCalendarId);
        TextTitle subTitleDate=null;
        if(calendar.getBaseCal().equals("GREG-CAL")){
             subTitleDate = new TextTitle(FormatHelper.formatDate(filter.getStartDate())+"-" +FormatHelper.formatDate(filter.getEndDate()),subTitleFont);
        }
         else{
            int fiscalYear = filter.getYear().intValue();
            fi.joensuu.joyds1.calendar.Calendar startDatecalendar=OrgProfileUtil.getCalendar(calendar,true, fiscalYear);
            fi.joensuu.joyds1.calendar.Calendar endDatecalendar=OrgProfileUtil.getCalendar(calendar,false, fiscalYear);
            subTitleDate = new TextTitle(FormatHelper.formatDate(startDatecalendar)+"-" +FormatHelper.formatDate(endDatecalendar),subTitleFont);
         }
        
		subTitle.setPadding(5, 5, 5, 5);
		chart.addSubtitle(0, subTitle);
        chart.addSubtitle(1, subTitleDate);
		RingPlot plot = (RingPlot) chart.getPlot();
		plot.setOuterSeparatorExtension(0);
		plot.setInnerSeparatorExtension(0);
		plot.setBackgroundPaint(new Color(255,255,255,0));
		plot.setSectionDepth(0.5);
		plot.setCircular(true);
		plot.setLabelFont(plainFont);
		plot.setShadowXOffset(0);
		plot.setShadowYOffset(0);
		String amount = "";
        if (filter.getDivideThousands()) {
			amount = "Thousands of Millions";
		} else {
			amount = "Millions";
		}		
		String pattern = "{0} {1} " + amount + " ({2})";
		if (opt.getLabelPattern() != null) {
			pattern = opt.getLabelPattern();
		}

		DecimalFormat format = FormatHelper.getDecimalFormat();
		format.setMaximumFractionDigits(3);
        format.setMinimumIntegerDigits(1);
		if(filter.getShowComplexLabel()){
			PieSectionLabelGenerator gen = new PieChartCustomLabelGeneratorUnrestricted(new DecimalFormat("0.0%"));
			plot.setLabelGenerator(gen);
			plot.setSimpleLabels(false);
		}
		else
		{
			PieSectionLabelGenerator gen = new PieChartCustomLabelGenerator(new DecimalFormat("0.0%"));
			plot.setLabelGenerator(gen);
			plot.setSimpleLabels(true);
		}	
		plot.setLabelBackgroundPaint(new Color(0, 0, 0, 0));
		plot.setLabelGap(0);
		plot.setLabelLinkMargin(0.05);
		plot.setLabelShadowPaint(null);
		plot.setLabelOutlinePaint(new Color(0, 0, 0, 0));
		plot.setOutlineVisible(false);
		plot.setToolTipGenerator(new StandardPieToolTipGenerator(pattern, format, new DecimalFormat("0.0%")));
		LegendTitle legend = chart.getLegend();
		legend.setItemFont(plainFont);
		legend.setFrame(BlockBorder.NONE);
		legend.setMargin(10, 5, 5, 10);
		legend.setPosition(RectangleEdge.RIGHT);
		legend.setVerticalAlignment(VerticalAlignment.TOP);
		legend.setHorizontalAlignment(HorizontalAlignment.LEFT);
		legend.setBackgroundPaint(new Color(255,255,255,0));
        RectangleInsets legendItemPadding=new RectangleInsets(6,0,0,0);
        legend.setLegendItemGraphicPadding(legendItemPadding);
        legend.setItemLabelPadding(legendItemPadding);
		List <Comparable> keys = dataset.getKeys();
        int aInt;
        Color[] colors = opt.isMonochrome() ? colorsMonochrome : colorsColor;       
        for (int i = 0; i < keys.size(); i++)
        {
            aInt = i % colors.length;
            plot.setSectionPaint(keys.get(i), colors[aInt]);
        }

		plot.setLegendItemShape(new Rectangle(10, 10));
		PieSectionLabelGenerator genLegend = new PieChartLegendGenerator(150);
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
    public static PieDataset getSectorByDonorDataset(Long[] donors, Integer fromYear, Integer toYear, ChartOption opt) throws DgException {
        DefaultPieDataset ds = new DefaultPieDataset();
        Date fromDate = null;
        Date toDate = null;
        if (fromYear != null && toYear != null) {
            /**
             * we should get default calendar and from/to dates should be taken according to it.
             * So for example, if some calendar's startMonth is 1st of July, we should take an interval from
             * year's(parameter that is passed to function) 1st of July to the next year's 1st of July
             */
            Long defaultCalendarId = new Long(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_CALENDAR));
            AmpFiscalCalendar calendar = FiscalCalendarUtil.getAmpFiscalCalendar(defaultCalendarId);
            fromDate = getStartOfYear(fromYear.intValue(), calendar.getStartMonthNum() - 1, calendar.getStartDayNum());
            //we need data including the last day of toYear,this is till the first day of toYear+1
            int MILLISECONDS_IN_DAY = 1000 * 60 * 60 * 24;
            toDate = new Date(getStartOfYear(toYear.intValue() + 1, calendar.getStartMonthNum() - 1, calendar.getStartDayNum()).getTime() - MILLISECONDS_IN_DAY);
        }
        Double[] allFundingWrapper = {new Double(0)};// to hold whole funding value// to hold whole funding value
        Collection<DonorSectorFundingHelper> fundings = getDonorSectorFunding(donors, fromDate, toDate, allFundingWrapper);
        if (fundings != null) {
            Double otherFunding = new Double(0);
            List<Long> otherIds = new ArrayList<Long>();
            for (DonorSectorFundingHelper funding : fundings) {
                Double percent = funding.getFounding() / allFundingWrapper[0];
                // the sectors which percent is less then 5% should be group in "Other"
                AmpSector sector = funding.getSector();

                if (percent >= 0.05) {
                    SectorHelper secHelper = new SectorHelper();
                    secHelper.setName(sector.getName());
                    secHelper.setIds(new ArrayList<Long>());
                    secHelper.getIds().add(sector.getAmpSectorId());
                    ds.setValue(secHelper, Math.round(funding.getFounding()));
                } else {
                    otherFunding += funding.getFounding();
                    otherIds.add(sector.getAmpSectorId());
                }
            }
            if (otherFunding != 0) {
                String otherSectors = "Other Sectors";
                try {
                    otherSectors = TranslatorWorker.translateText("Other Sectors", opt.getLangCode(), opt.getSiteId());
                } catch (WorkerException e) {
                    e.printStackTrace();
                }
                SectorHelper secHelper = new SectorHelper();
                secHelper.setName(otherSectors);
                secHelper.setIds(otherIds);
                ds.setValue(secHelper, otherFunding);
            }
        }
        return ds;
    }

    public static Date getStartOfYear(int year, int month, int day) {
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
    public static Collection<DonorSectorFundingHelper> getDonorSectorFunding(Long donorIDs[], Date fromDate, Date toDate, Double[] wholeFunding) throws DgException {
        Collection<DonorSectorFundingHelper> fundings = null;
        String oql = "select actSec.sectorId, sum(fd.transactionAmountInBaseCurrency*actSec.sectorPercentage*0.01)";
        oql += " from ";
        oql += AmpFundingDetail.class.getName()
                + " as fd inner join fd.ampFundingId f ";
        oql += "   inner join f.ampActivityId act "
                + " inner join act.sectors actSec "
                + " inner join actSec.sectorId sec "
                + " inner join actSec.activityId act "
                + " inner join actSec.classificationConfig config ";
        oql += " where  fd.transactionType = 0 and fd.adjustmentType = 1 ";
        if (donorIDs != null && donorIDs.length > 0) {
            oql += " and (fd.ampFundingId.ampDonorOrgId in (" + getInStatment(donorIDs) + ") ) ";
        }
        if (fromDate != null && toDate != null) {
            oql += " and (fd.transactionDate between :fDate and  :eDate ) ";
        }
               
        oql += " and config.name='Primary' and act.team is not null ";
        oql += " group by actSec.sectorId ";

        Session session = PersistenceManager.getRequestDBSession();
        //search for grouped data
        @SuppressWarnings("unchecked")
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
            throw new DgException(
                    "Cannot load sector fundings by donors from db", e);
        }

        //Process grouped data
        if (result != null) {
            Map<Long, DonorSectorFundingHelper> donors = new HashMap<Long, DonorSectorFundingHelper>();
            for (Object row : result) {
                Object[] rowData = (Object[]) row;
                //AmpOrganisation donor = (AmpOrganisation) rowData[0];
                AmpSector sector = (AmpSector) rowData[0];
                Double amt = (Double) rowData[1];
                Double amount = FeaturesUtil.applyThousandsForVisibility(amt);
                sector = SectorUtil.getTopLevelParent(sector);
                DonorSectorFundingHelper sectorFundngObj = donors.get(sector.getAmpSectorId());
                //if not create and add to map
                if (sectorFundngObj == null) {
                    sectorFundngObj = new DonorSectorFundingHelper(sector);
                    donors.put(sector.getAmpSectorId(), sectorFundngObj);
                }
                //add amount to sector
                sectorFundngObj.addFunding(amount);
                //calculate whole funding information
                wholeFunding[0] = wholeFunding[0] + amount;
            }
            fundings = donors.values();
        }
        return fundings;
    }

    public static Double getDonorSectorFunding(Long donorIDs[], Date fromDate, Date toDate, Long sectorIds[], boolean showOnlyApprovedActivities) throws DgException {
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

        List result = getFunding(donorIDs, fromDate, toDate, sectIds, showOnlyApprovedActivities);
        //Process grouped data
        if (result != null) {
            amount = ((Double) result.get(0));
        }
        if (amount == null) {
            amount = 0d;
        }
        return amount;


    }

    public static List getFunding(Long[] donorIDs, Date fromDate, Date toDate, Long[] sectorIds, boolean showOnlyApprovedActivities) throws DgException {
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
        if (sectorIds != null) {
            oql += " and actSec.sectorId in (" + getInStatment(sectorIds) + ") ";
        }
        oql += " and config.name='Primary' and act.team is not null ";
        
         if (showOnlyApprovedActivities) {
            oql += getTeamQueryManagement();
        }
               
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
    public static List<AmpFundingDetail> getLocationFunding(FilterHelper filter,AmpCategoryValueLocations location) throws DgException {
        Long orgGroupId = filter.getOrgGroupId();
        Long[] orgIds = filter.getOrgIds();
        int transactionType = filter.getTransactionType();
        TeamMember teamMember = filter.getTeamMember();
        // apply calendar filter
        Date startDate = filter.getStartDate();
        Date endDate = filter.getEndDate();

                /* query that creates new  AmpFundingDetail objects
                which amounts are calculated by multiplication
                of the region percent and amount value*/
                String oql = "select new AmpFundingDetail(fd.transactionType,fd.adjustmentType,fd.transactionAmount,fd.transactionDate,fd.ampCurrencyId,actloc.locationPercentage,fd.fixedExchangeRate) ";
                oql += " from ";
                oql += AmpFundingDetail.class.getName()
                        + " as fd inner join fd.ampFundingId f ";
                oql += "   inner join f.ampActivityId act ";
                oql += " inner join act.locations actloc inner join actloc.location amploc inner join amploc.location loc ";

                oql += " where  fd.adjustmentType = 1 ";
                if (filter.getTransactionType() < 2) { // the option comm&disb is not selected
                oql += " and fd.transactionType =:transactionType  ";
                }
                else{
                     oql += " and (fd.transactionType =0 or  fd.transactionType =1) "; // the option comm&disb is selected
                }
                if (orgIds == null) {
                    if (orgGroupId != -1) {
                        oql += getOrganizationQuery(true, orgIds);
                    }
                } else {
                    oql += getOrganizationQuery(false, orgIds);
                }
                oql += " and  (fd.transactionDate>=:startDate and fd.transactionDate<=:endDate)  and  loc.id=  " + location.getId();
                if(filter.getFromPublicView() == true){
                    oql += getTeamQueryManagement();
                }
                else
                {
                    oql += getTeamQuery(teamMember);
                }
                
                
                Session session = PersistenceManager.getRequestDBSession();
                Query query = session.createQuery(oql);
                query.setDate("startDate", startDate);
                query.setDate("endDate", endDate);
                if (orgIds == null && orgGroupId != -1) {
                    query.setLong("orgGroupId", orgGroupId);
                }
                 if (filter.getTransactionType() < 2) { // the option comm&disb is not selected
                query.setLong("transactionType", transactionType);
                 }

                List<AmpFundingDetail> fundingDets = query.list();
                return fundingDets;
        
        
    }

     public static List<AmpCategoryValueLocations> getLocations(FilterHelper filter) throws DgException {
        Long orgGroupId = filter.getOrgGroupId();
        List<AmpCategoryValueLocations> locations=null;
        Long[] orgIds = filter.getOrgIds();
        int transactionType = filter.getTransactionType();
        TeamMember teamMember = filter.getTeamMember();
        // apply calendar filter
        Date startDate = filter.getStartDate();
        Date endDate = filter.getEndDate();
        /*
         * We are selecting regions which are funded
         * In selected year by the selected organization
         *
         */
        try {
            Long regionId = filter.getRegionId();
            String oql = "select distinct loc  from ";
            oql += AmpFundingDetail.class.getName()
                    + " as fd inner join fd.ampFundingId f ";
            oql += "   inner join f.ampActivityId act ";
            oql += " inner join act.locations actloc inner join actloc.location amploc inner join amploc.location loc ";
            oql += "  where fd.adjustmentType = 1";
            if (filter.getTransactionType() < 2) { // the option comm&disb is not selected
                oql += " and fd.transactionType =:transactionType  ";
            } else {
                oql += " and (fd.transactionType =0 or  fd.transactionType =1) "; // the option comm&disb is selected
            }
            if (orgIds == null) {
                if (orgGroupId != -1) {
                    oql += getOrganizationQuery(true, orgIds);
                }
            } else {
                oql += getOrganizationQuery(false, orgIds);
            }
            oql += " and  (fd.transactionDate>=:startDate and fd.transactionDate<=:endDate)  ";
            
            if(filter.getFromPublicView() == true){
                oql += getTeamQueryManagement();
            }
            else
            {
                oql += getTeamQuery(teamMember);
            }
            if (regionId != null && regionId != -1) {
                oql += " and loc.id in (:locations) ";
            }
            
            
            oql+=" order by loc.parentCategoryValue";
            Session session = PersistenceManager.getRequestDBSession();
            Query query = session.createQuery(oql);
            query.setDate("startDate", startDate);
            query.setDate("endDate", endDate);
            if (orgIds == null && orgGroupId != -1) {
                query.setLong("orgGroupId", orgGroupId);
            }
            if (filter.getTransactionType() < 2) { // the option comm&disb is not selected
                query.setLong("transactionType", transactionType);
            }

            Collection<Long> locationIds = filter.getLocationIds();
            if (regionId != null && regionId != -1) {
                query.setParameterList("locations", locationIds);
            }
            locations = query.list();
        }
        catch (Exception e) {
            logger.error(e);
            throw new DgException("Cannot load locations fundings by donors from db", e);
        }
        return locations;

     }

    /**
     * Generates Pie dataset using  filters .
     * This chart then can be rendered as image or pdf or file.
     * @param filter
     * @return CategoryDataset
     * @throws DgException
     */
    @SuppressWarnings("unchecked")
    public static DefaultPieDataset getDonorRegionalDataSet(FilterHelper filter,ChartOption opt) throws DgException, WorkerException {
        BigDecimal divideByMillionDenominator = new BigDecimal(1000000);
        String othersTitle = TranslatorWorker.translateText("Other", opt.getLangCode(), opt.getSiteId());
        String nationalTitle = TranslatorWorker.translateText("National", opt.getLangCode(), opt.getSiteId());
        String regionalTitle = TranslatorWorker.translateText("Regional", opt.getLangCode(), opt.getSiteId());
        String unallocatedTitle = TranslatorWorker.translateText("Unallocated", opt.getLangCode(), opt.getSiteId());
        if ("true".equals(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS))) {
            divideByMillionDenominator = new BigDecimal(1000);
        }
        BigDecimal regionalTotal = BigDecimal.ZERO;
        Long currId = filter.getCurrId();
        String currCode = CurrencyUtil.getAmpcurrency(filter.getCurrId()).getCurrencyCode();
        int transactionType = filter.getTransactionType();
        DefaultPieDataset ds = new DefaultPieDataset();
        /*
         * We are selecting regions which are funded
         * In selected year by the selected organization
         *
         */
        try {
            List<AmpCategoryValueLocations> locations = getLocations(filter);
            Iterator<AmpCategoryValueLocations> regionIter = locations.iterator();
            while (regionIter.hasNext()) {
                //calculating funding for each region
                AmpCategoryValueLocations location = regionIter.next();
                List<AmpFundingDetail> fundingDets = getLocationFunding(filter, location);
                /*Newly created objects and   selected currency
                are passed doCalculations  method*/
                FundingCalculationsHelper cal = new FundingCalculationsHelper();
                cal.doCalculations(fundingDets, currCode);
                DecimalWraper total = null;

                /*Depending on what is selected in the filter
                we should return either actual commitments
                or actual Disbursement */

                if (transactionType == Constants.COMMITMENT) {
                    total = cal.getTotActualComm();
                } else {
                    total = cal.getTotActualDisb();
                }

                BigDecimal amount = total.getValue().setScale(10, RoundingMode.HALF_UP).divide(divideByMillionDenominator);
                BigDecimal oldvalue = BigDecimal.ZERO;
                String keyName = "";
                String implLocation = CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY.getValueKey();
                if (location.getParentCategoryValue().getValue().equals(implLocation)) {
                    keyName = nationalTitle;
                } else {
                    Long zoneIds[] = filter.getZoneIds();
                    if (zoneIds != null && zoneIds.length > 0) {
                        implLocation = CategoryConstants.IMPLEMENTATION_LOCATION_REGION.getValueKey();
                        if (location.getParentCategoryValue().getValue().equals(implLocation)) {
                            keyName = regionalTitle;
                        } else {
                            AmpCategoryValueLocations parent = LocationUtil.getTopAncestor(location, implLocation);
                            keyName = parent.getName();
                        }
                    } else {
                        AmpCategoryValueLocations parent = LocationUtil.getTopAncestor(location, implLocation);
                        keyName = parent.getName();
                    }


                }
                try {
                    oldvalue = (BigDecimal) ds.getValue(keyName);
                } catch (UnknownKeyException ex) {
                    oldvalue = BigDecimal.ZERO;
                }
                if (oldvalue != null) {
                    BigDecimal newValue = oldvalue.add(amount);
                    ds.setValue(keyName, newValue);
                } else {
                    ds.setValue(keyName, amount);
                }
                regionalTotal = regionalTotal.add(amount);
            }
            Collection<Long> locationIds = filter.getLocationIds();
            boolean unallocatedCondition = locationIds == null || locationIds.isEmpty();
            if (unallocatedCondition) {
                List<AmpFundingDetail> unallocatedFundings = getUnallocatedFunding(filter);
                FundingCalculationsHelper cal = new FundingCalculationsHelper();
                cal.doCalculations(unallocatedFundings, currCode);
                DecimalWraper total = null;

                /*Depending on what is selected in the filter
                we should return either actual commitments
                or actual Disbursement */

                if (transactionType == Constants.COMMITMENT) {
                    total = cal.getTotActualComm();
                } else {
                    total = cal.getTotActualDisb();
                }
                if (total != null && total.doubleValue() != 0) {
                    double unallocatedAmount = total.doubleValue() / divideByMillionDenominator.doubleValue();
                    ds.setValue(unallocatedTitle, unallocatedAmount);
                    regionalTotal = regionalTotal.add(new BigDecimal(unallocatedAmount));
                }
            }
            List<String> keys = ds.getKeys();
            Iterator<String> keysIter = keys.iterator();
            BigDecimal othersValue = BigDecimal.ZERO;
            while (keysIter.hasNext()) {
                String key = keysIter.next();
                if (key.equals(nationalTitle)||key.equals(regionalTitle)||key.equals(unallocatedTitle)) {
                    continue;
                }
                BigDecimal value = (BigDecimal) ds.getValue(key);
                Double percent = (value.divide(regionalTotal, 3, RoundingMode.HALF_UP)).doubleValue();
                if (percent <= 0.05) {
                    othersValue = othersValue.add(value);
                    ds.remove(key);
                }
            }
            if (!othersValue.equals(BigDecimal.ZERO)) {
                ds.setValue(othersTitle, othersValue.setScale(10, RoundingMode.HALF_UP));
            }
           
        } catch (Exception e) {
            logger.error(e);
            throw new DgException("Cannot load sector fundings by donors from db", e);
        }
        return ds;

    }

     @SuppressWarnings("unchecked")
    public static List<AmpFundingDetail> getUnallocatedFunding(FilterHelper filter)
            throws DgException {
        Long[] orgIds = filter.getOrgIds();
        Long orgGroupId = filter.getOrgGroupId();
        int transactionType = filter.getTransactionType();
        TeamMember tm = filter.getTeamMember();
        Date startDate = filter.getStartDate();
        Date endDate = filter.getEndDate();
        Session session = PersistenceManager.getRequestDBSession();
        String oql = "select fd ";
        oql += " from ";
        oql += AmpFundingDetail.class.getName()
                + " as fd inner join fd.ampFundingId f ";
        oql += "   inner join f.ampActivityId act ";
        oql += " left join act.locations actloc  ";

        oql += "  where  "
                + "   fd.adjustmentType = 1 ";
        if (filter.getTransactionType() < 2) { // the option comm&disb is not selected
            oql += " and fd.transactionType =:transactionType  ";
        } else {
            oql += " and (fd.transactionType=1 or fd.transactionType=0) "; // the option comm&disb is selected
        }
        oql += " and  (fd.transactionDate>=:startDate and fd.transactionDate<=:endDate)   ";
        if (orgIds == null) {
            if (orgGroupId != -1) {
                oql += getOrganizationQuery(true, orgIds);
            }

        } else {
            oql += getOrganizationQuery(false, orgIds);
        }
     
        if(filter.getFromPublicView() == true){
            oql += getTeamQueryManagement();
        }
        else
        {
            oql += getTeamQuery(tm);
        }
        
        oql += " and actloc is NULL ";

        
        Query query = session.createQuery(oql);
        query.setDate("startDate", startDate);
        query.setDate("endDate", endDate);
        if (orgIds == null && orgGroupId != -1) {
            query.setLong("orgGroupId", orgGroupId);
        }
        if (filter.getTransactionType() < 2) { // the option comm&disb is not selected
            query.setLong("transactionType", transactionType);
        }
        List<AmpFundingDetail> fundingDets = query.list();
        return fundingDets;

    }

    public static List<AmpSector> getSectorList(FilterHelper filter,Long sectorClassConfigId) throws DgException {
        Long[] orgIds = filter.getOrgIds();
        Long orgGroupId = filter.getOrgGroupId();
        int transactionType = filter.getTransactionType();
        TeamMember tm = filter.getTeamMember();
        Collection<Long> locationIds = filter.getLocationIds();
        boolean locationCondition = locationIds != null && locationIds.size() > 0;
        Date startDate = filter.getStartDate();
        Date endDate = filter.getEndDate();

        /*
         * We are selecting sectors which are funded
         * In selected year by the selected organization
         * We are interesting only primary sectors
         * From the activities which belonging to team
         *
         */
        String oql = "select distinct sec  from ";
        oql += AmpFundingDetail.class.getName()
                + " as fd inner join fd.ampFundingId f ";

        oql += "   inner join f.ampActivityId act "
                + " inner join act.sectors actSec "
                + " inner join actSec.sectorId sec "
                + " inner join actSec.activityId act "
                + " inner join actSec.classificationConfig config ";
        if (locationCondition) {
            oql += " inner join act.locations actloc inner join actloc.location amploc inner join amploc.location loc ";
        }

        oql += "  where   fd.adjustmentType = 1";
        if (filter.getTransactionType() < 2) {
            oql += " and fd.transactionType =:transactionType  ";
        } else {
            oql += " and (fd.transactionType =0 or  fd.transactionType =1) "; // the option comm&disb is selected
        }
        if (orgIds == null) {
            if (orgGroupId != -1) {
                oql += getOrganizationQuery(true, orgIds);
            }
        } else {
            oql += getOrganizationQuery(false, orgIds);
        }
        oql += " and  (fd.transactionDate>=:startDate and fd.transactionDate<=:endDate)     and config.id=:configId ";
        
        
        if(filter.getFromPublicView() == true){
            oql += getTeamQueryManagement();
        }
        else
        {
            oql += getTeamQuery(tm);
        }
        if (locationCondition) {
            oql += " and loc.id in (:locations) ";
        }
        Session session = PersistenceManager.getRequestDBSession();
        List<AmpSector> sectors = null;
        try {
            Query query = session.createQuery(oql);
            query.setDate("startDate", startDate);
            query.setDate("endDate", endDate);
            query.setLong("configId", sectorClassConfigId);
            if (orgIds == null && orgGroupId != -1) {
                query.setLong("orgGroupId", orgGroupId);
            }
            if (filter.getTransactionType() < 2) {
                query.setLong("transactionType", transactionType);
            }
            if (locationCondition) {
                query.setParameterList("locations", locationIds);
            }
            sectors = query.list();
        } catch (Exception e) {
            logger.error(e);
            throw new DgException("Cannot load sector fundings by donors from db", e);
        }
        return sectors;

    }

    public static Collection<DonorSectorFundingHelper> getDonorSectorFundingHelperList(FilterHelper filter,Long sectorClassConfigId) throws DgException {
        int transactionType = filter.getTransactionType();
        String currCode = CurrencyUtil.getAmpcurrency(filter.getCurrId()).getCurrencyCode();
        List<AmpSector> sectors = getSectorList(filter,sectorClassConfigId);
        Iterator<AmpSector> sectorIter = sectors.iterator();
        Map<Long, DonorSectorFundingHelper> sectorsMap = new HashMap<Long, DonorSectorFundingHelper>();
        while (sectorIter.hasNext()) {
            AmpSector sector = sectorIter.next();
            Long sectorId = sector.getAmpSectorId();
            List<AmpFundingDetail> fundingDets = getSectorFunding(filter, sectorId,sectorClassConfigId);
            FundingCalculationsHelper cal = new FundingCalculationsHelper();
            cal.doCalculations(fundingDets, currCode);
            DecimalWraper amount = null;
            DecimalWraper disbAmount = null;

            /*Depending on what is selected in the filter
            we should return either actual commitments
            or actual Disbursement */
            switch (transactionType) {
                case Constants.COMMITMENT:
                    amount = cal.getTotActualComm();
                    break;
                case Constants.DISBURSEMENT:
                    disbAmount = cal.getTotActualDisb();
                    break;
                case 2: //both COMMITMENT & DISBURSEMENT
                    amount = cal.getTotActualComm();
                    disbAmount = cal.getTotActualDisb();
                    break;
            }


            AmpSector topLevelSector = SectorUtil.getTopLevelParent(sector);
            Long topSectorId = topLevelSector.getAmpSectorId();
            // getting object from map
            DonorSectorFundingHelper sectorFundngObj = sectorsMap.get(topSectorId);
            //if not create and add to map
            if (sectorFundngObj == null) {
                sectorFundngObj = new DonorSectorFundingHelper(topLevelSector);
                sectorsMap.put(topSectorId, sectorFundngObj);
            }
            if (amount != null) {
                Double amt = amount.doubleValue();
                //add amount disb to sector
                sectorFundngObj.addFunding(amt);

            }
            if (disbAmount != null) {
                Double amt = disbAmount.doubleValue();
                //add amount disb to sector
                sectorFundngObj.addDisbFunding(amt);

            }


        }
        Collection<DonorSectorFundingHelper> secFundCol = sectorsMap.values();
        return secFundCol;

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
    public static DefaultPieDataset getDonorSectorDataSet(FilterHelper filter,Long sectorClassConfigId,String othersTitle) throws DgException {
        Double divideByMillionDenominator=new Double(1000000);
        if ("true".equals(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS))) {
             divideByMillionDenominator=new Double(1000);
        }
        DefaultPieDataset ds = new DefaultPieDataset();
        List<AmpSector> sectors = getSectorList(filter,sectorClassConfigId);
        // to calculate funding for all sectors
        Double totAllSectors = 0d;
        Iterator<AmpSector> sectorIter = sectors.iterator();
        Double others = 0d;
        Map<Long, DonorSectorFundingHelper> sectorsMap = new HashMap<Long, DonorSectorFundingHelper>();
        while (sectorIter.hasNext()) {
            AmpSector sector = sectorIter.next();
            // calculate funding for each sector
            DecimalWraper amount = getSectorFundingAmount(filter, sector.getAmpSectorId(),sectorClassConfigId);
            AmpSector topLevelSector = SectorUtil.getTopLevelParent(sector);
            Long topSectorId = topLevelSector.getAmpSectorId();
            // getting object from map
            DonorSectorFundingHelper sectorFundngObj = sectorsMap.get(topSectorId);
            //if not create and add to map
            if (sectorFundngObj == null) {
                sectorFundngObj = new DonorSectorFundingHelper(topLevelSector);
                sectorsMap.put(topSectorId, sectorFundngObj);
            }
            if (amount != null) {
                Double amt = amount.doubleValue();
                //add amount to sector
                sectorFundngObj.addFunding(amt);
                // adding to total funding amount
                totAllSectors += amt;
            }
        }
        if (!sectorsMap.isEmpty()) {
            Collection<DonorSectorFundingHelper> secFundCol = sectorsMap.values();
            Iterator<DonorSectorFundingHelper> secFundColIter = secFundCol.iterator();
            while (secFundColIter.hasNext()) {
                DonorSectorFundingHelper sectorFunding = secFundColIter.next();
                Double percent = sectorFunding.getFounding() / totAllSectors;
                // if percent is less than 5, group in "others"
                if (percent >= 0.05) {
                    ds.setValue(sectorFunding.getSector().getName(), sectorFunding.getFounding()/divideByMillionDenominator);
                } else {
                    others += sectorFunding.getFounding();

                }
            }
        }
        if (others.doubleValue() > 0) {
            ds.setValue(othersTitle, others/divideByMillionDenominator);
        }

        return ds;

    }

    @SuppressWarnings("unchecked")
    public static List<AmpFundingDetail> getSectorFunding(FilterHelper filter, Long sectorId,Long sectorClassConfigId)
            throws DgException {
        Long[] orgIds = filter.getOrgIds();
        Long orgGroupId = filter.getOrgGroupId();
        int transactionType = filter.getTransactionType();
        TeamMember tm = filter.getTeamMember();
        Collection<Long> locationIds = filter.getLocationIds();
        boolean locationCondition = locationIds != null && locationIds.size() > 0;
        Date startDate = filter.getStartDate();
        Date endDate = filter.getEndDate();
        Session session = PersistenceManager.getRequestDBSession();
        String oql = "select new AmpFundingDetail(fd.transactionType,fd.adjustmentType,fd.transactionAmount,fd.transactionDate,fd.ampCurrencyId,";
        if (!locationCondition) {
            oql += "actSec.sectorPercentage,fd.fixedExchangeRate)";
        } else {
             oql += "0.01f*actloc.locationPercentage*actSec.sectorPercentage,fd.fixedExchangeRate)";
        }
        oql += " from ";
        oql += AmpFundingDetail.class.getName()
                + " as fd inner join fd.ampFundingId f ";
        oql += "   inner join f.ampActivityId act "
                + " inner join act.sectors actSec "
                + " inner join actSec.sectorId sec "
                + " inner join actSec.activityId act "
                + " inner join actSec.classificationConfig config ";
        if (locationCondition) {
            oql += " inner join act.locations actloc inner join actloc.location amploc inner join amploc.location loc ";
        }

        oql += "  where  "
                + "   fd.adjustmentType = 1 ";
        if (filter.getTransactionType() < 2) { // the option comm&disb is not selected
            oql += " and fd.transactionType =:transactionType  ";
        } else {
            oql += " and (fd.transactionType=1 or fd.transactionType=0) "; // the option comm&disb is selected
        }
        oql += " and  (fd.transactionDate>=:startDate and fd.transactionDate<=:endDate)    and config.id=:configId  ";
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
        if(filter.getFromPublicView()){
            oql += getTeamQueryManagement();
        }
        else
        {
        	oql += getTeamQuery(tm);
        }
        if (locationCondition) {
            oql += " and loc.id in (:locations) ";
        }
              
        Query query = session.createQuery(oql);
        query.setDate("startDate", startDate);
        query.setDate("endDate", endDate);
        query.setLong("configId", sectorClassConfigId);
        if (orgIds == null && orgGroupId != -1) {
            query.setLong("orgGroupId", orgGroupId);
        }
        if (filter.getTransactionType() < 2) { // the option comm&disb is not selected
            query.setLong("transactionType", transactionType);
        }
        if (sectorId != null) {
            query.setLong("sectorId", sectorId);
        }
        if (locationCondition) {
            query.setParameterList("locations", locationIds);
        }
        List<AmpFundingDetail> fundingDets = query.list();
        return fundingDets;

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
    public static DecimalWraper getSectorFundingAmount(FilterHelper filter, Long sectorId,Long sectorClassConfigId)
            throws DgException {
        List<AmpFundingDetail> fundingDets = getSectorFunding(filter,sectorId,sectorClassConfigId);
        FundingCalculationsHelper cal = new FundingCalculationsHelper();
        String currCode = CurrencyUtil.getAmpcurrency(filter.getCurrId()).getCurrencyCode();
        cal.doCalculations(fundingDets, currCode);
        DecimalWraper amount = null;
        /*Depending on what is selected in the filter
        we should return either actual commitments
        or actual Disbursement */
        if (filter.getTransactionType() == Constants.COMMITMENT) {
           amount = cal.getTotActualComm();
        } else {
            amount = cal.getTotActualDisb();
        }
        return amount;

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
    public static DecimalWraper getFunding(FilterHelper filter, Date startDate,
            Date endDate, Long assistanceTypeId,
            Long financingInstrumentId,
            int transactionType,int adjustmentType) throws DgException {
        DecimalWraper total = null;
        String oql = "";
        String currCode = CurrencyUtil.getAmpcurrency(filter.getCurrId()).getCurrencyCode();
        Long[] orgIds = filter.getOrgIds();
        Long orgGroupId = filter.getOrgGroupId();
        TeamMember tm = filter.getTeamMember();
        Collection<Long> locationIds = filter.getLocationIds();
        boolean locationCondition = locationIds != null && locationIds.size() > 0;

        if (locationCondition) {
            oql = " select new AmpFundingDetail(fd.transactionType,fd.adjustmentType,fd.transactionAmount,fd.transactionDate,fd.ampCurrencyId,actloc.locationPercentage,fd.fixedExchangeRate) ";
        } else {
            oql = "select fd ";
        }
        oql += " from ";
        oql += AmpFundingDetail.class.getName()
                + " as fd inner join fd.ampFundingId f ";
        oql += "   inner join f.ampActivityId act ";
        if (locationCondition) {
            oql += " inner join act.locations actloc inner join actloc.location amploc inner join amploc.location loc ";
        }

        oql += " where  fd.transactionType =:transactionType  and  fd.adjustmentType =:adjustmentType ";
        if (orgIds == null) {
            if (orgGroupId != -1) {
                oql += getOrganizationQuery(true, orgIds);
            }
        } else {
            oql += getOrganizationQuery(false, orgIds);
        }
        if (locationCondition) {
            oql += " and loc.id in (:locations) ";
        }

        oql += " and  (fd.transactionDate>=:startDate and fd.transactionDate<=:endDate)  ";
        if (assistanceTypeId != null) {
            oql += "  and f.typeOfAssistance=:assistanceTypeId ";
        }
        if (financingInstrumentId != null) {
            oql += "   and f.financingInstrument=:financingInstrumentId  ";
        }

        if(filter.getFromPublicView()){
            oql += getTeamQueryManagement();
        }
        else
        {
            oql += getTeamQuery(tm);
        }

        Session session = PersistenceManager.getRequestDBSession();
        List<AmpFundingDetail> fundingDets = null;
        try {
            Query query = session.createQuery(oql);
            query.setDate("startDate", startDate);
            query.setDate("endDate", endDate);
            if (orgIds == null && orgGroupId != -1) {
                query.setLong("orgGroupId", orgGroupId);
            }
            if (assistanceTypeId != null) {
                query.setLong("assistanceTypeId", assistanceTypeId);
            }
            if (financingInstrumentId != null) {
                query.setLong("financingInstrumentId", financingInstrumentId);
            }
            query.setLong("transactionType", transactionType);
            query.setLong("adjustmentType",adjustmentType);
            if (locationCondition) {
                query.setParameterList("locations", locationIds);
            }
            fundingDets = query.list();
            /*the objects retuned by query  and   selected currency
            are passed doCalculations  method*/
            FundingCalculationsHelper cal = new FundingCalculationsHelper();
            cal.doCalculations(fundingDets, currCode);
            /*Depending on what is selected in the filter
            we should return either actual commitments
            or actual Disbursement or  */
            switch (transactionType) {
                case Constants.EXPENDITURE:
                    if (Constants.PLANNED == adjustmentType) {
                        total = cal.getTotPlannedExp();
                    } else {
                        total = cal.getTotActualExp();
                    }
                    break;
                case Constants.DISBURSEMENT:
                    if (Constants.ACTUAL == adjustmentType) {
                        total = cal.getTotActualDisb();
                    } else {
                        total = cal.getTotPlanDisb();
                    }
                    break;
                default:
                    if (Constants.ACTUAL == adjustmentType) {
                        total = cal.getTotActualComm();
                    } else {
                        total = cal.getTotPlannedComm();
                    }
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
    public static double getPledgesFunding(Long[] orgIds, Long orgGroupId, int year,
            String currCode, Collection<Long> locationIds) throws DgException {
        double totalPlannedPldges = 0;
        // Since there's no date for FundingPledgeDetail we use the year of the startDate from calendar
        String currentYear = String.valueOf(year);
        List<Long> orgs = new ArrayList<Long>();
        if (orgIds != null) {
            orgs = Arrays.asList(orgIds);
        }
        try {
            boolean locationCondition = locationIds != null && locationIds.size() > 0;
            Session session = PersistenceManager.getRequestDBSession();
            Query query;
            StringBuilder oql = new StringBuilder("select fd from ");
            oql.append(FundingPledgesDetails.class.getName());
            oql.append(" fd inner join fd.pledgeid plg ");
            oql.append(" inner join  plg.organization org  ");
            oql.append(" where fd.fundingYear = :currentYear ");
        if (orgIds == null) {
            if (orgGroupId != -1) {
                    oql.append(" and  org.orgGrpId.ampOrgGrpId=:orgGroupId ");
            }
        } else {
                oql.append(" and org.ampOrgId in (:organizations) ");
        }
        List<FundingPledgesDetails> fundingDets = null;
            query = session.createQuery(oql.toString());
            query.setString("currentYear", currentYear);

            if (orgIds == null && orgGroupId != -1) {
                query.setLong("orgGroupId", orgGroupId);
            } else {
                if (orgIds != null) {
                    query.setParameterList("organizations", orgs);
            }
            }
            fundingDets = query.list();
            Iterator<FundingPledgesDetails> fundDetIter = fundingDets.iterator();
            while (fundDetIter.hasNext()) {
                FundingPledgesDetails pledgeDet = fundDetIter.next();
                Double amount = pledgeDet.getAmount();

                if (locationCondition) { // The locationlist is commented in FundingPledgesLocation
                    FundingPledges pledge = pledgeDet.getPledgeid();
                    Collection<FundingPledgesLocation> pledesLocations = PledgesEntityHelper.getPledgesLocations(pledge.getId());
                    double percent = 0;
                    if (pledesLocations != null) {
                        for (FundingPledgesLocation pledgesLocation : pledesLocations) {
                            if (locationIds.contains(pledgesLocation.getLocation().getId())) {
                                percent += pledgesLocation.getLocationpercentage();
                            }

                        }
                        if (amount != null) {
                            amount *= percent / 100;
                        }
                    }

                }

                //converting amounts
                java.sql.Date dt = new java.sql.Date(pledgeDet.getFunding_date().getTime());
                double frmExRt = Util.getExchange(pledgeDet.getCurrency().getCurrencyCode(), dt);
                double toExRt = Util.getExchange(currCode, dt);
                DecimalWraper amt = CurrencyWorker.convertWrapper(amount, frmExRt, toExRt, dt);
                totalPlannedPldges += amt.doubleValue();
            }



        } catch (Exception e) {
            logger.error(e);
            throw new DgException(
                    "Cannot load pledge fundings by donors from db", e);
        }


        return totalPlannedPldges;
    }

    public static String getTeamQuery(TeamMember teamMember) {
        String qr = "";
        if (teamMember != null) {
            AmpTeam team = TeamUtil.getAmpTeam(teamMember.getTeamId());
            List<AmpTeam> teams = new ArrayList<AmpTeam>();
            getTeams(team, teams);
            String relatedOrgs = "";
            String teamIds = "";
            if (teamMember.getTeamAccessType().equals("Management")) {
                qr += " and act.draft=false and (act.approvalStatus ='approved' or act.approvalStatus ='startedapproved') ";
            }
            qr += " and (";
            for (AmpTeam tm : teams) {
                if (tm.getComputation() != null && tm.getComputation()) {
                    relatedOrgs += getComputationOrgsQry(tm);
                }
                teamIds += tm.getAmpTeamId() + ",";

            }
            if (teamIds.length() > 1) {
                teamIds = teamIds.substring(0, teamIds.length() - 1);
                qr += " act.team.ampTeamId in ( " + teamIds + ")";

            }
            if (relatedOrgs.length() > 1) {
                relatedOrgs = relatedOrgs.substring(0, relatedOrgs.length() - 1);
                qr += " or f.ampDonorOrgId in(" + relatedOrgs + ")";
            }
            qr += ")";

        } else {
            qr += "  and act.team is not null ";
        }
        return qr;
    }
    
    public static String getTeamQueryManagement() {
        String qr = "";
        qr += " and act.draft=false and (act.approvalStatus ='approved' or act.approvalStatus ='startedapproved') ";
        qr += " and act.team is not null and act.team in (select at.ampTeamId from " 
		+ AmpTeam.class.getName() + " at where parentTeamId is not null)";
        
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

    public static void getTeams(AmpTeam team, List<AmpTeam> teams) {
        teams.add(team);
        Collection<AmpTeam> childrenTeams =  TeamUtil.getAllChildrenWorkspaces(team.getAmpTeamId());
        if (childrenTeams != null) {
            for (AmpTeam tm : childrenTeams) {
                getTeams(tm, teams);
            }
        }
    }

    public static String getOrganizationQuery(boolean orgGroupView, Long[] orgIds) {
        String qry = "";
        if (orgGroupView) {
            qry = " and  f.ampDonorOrgId.orgGrpId.ampOrgGrpId=:orgGroupId ";
        } else {
            qry = " and f.ampDonorOrgId in (" + getInStatment(orgIds) + ") ";
        }
        return qry;
    }

    /**
     * Returns  amount in selected currency
     * for selected organization and year
     * @param orgID
     * @param year
     * @param currCode
     * @param isComm
     * @return
     * @throws org.digijava.kernel.exception.DgException
     */
    

    public static Double convert(Double amount, AmpCurrency originalCurrency) {
        try {
            CurrencyWorker.convertToUSD(amount, originalCurrency.getCurrencyCode());
        } catch (AimException e) {
            //e.printStackTrace();
            return amount;
        }
        return amount;
    }

    public static BigDecimal calculatePercentage(BigDecimal amount, Float percentage) {
        BigDecimal result = amount.multiply(new BigDecimal(percentage)).divide(new BigDecimal(100));
        return result;
    }

    public static String getInStatment(Long ids[]) {
        String oql = "";
        for (int i = 0; i < ids.length; i++) {
            oql += "" + ids[i];
            if (i < ids.length - 1) {
                oql += ",";
            }
        }
        return oql;
    }

    /**
     * Key worker for donors
     * @author Irakli Kobiashvili
     *
     */
    public static class DonorIdWorker implements KeyWorker<Long, AmpOrganisation> {

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

    public static List<LabelValueBean> getYears(boolean from) {
        //get default calendar
        Long defaultCalendarId = new Long(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_CALENDAR));
        AmpFiscalCalendar defaultCalendar = FiscalCalendarUtil.getAmpFiscalCalendar(defaultCalendarId);
        //get current year
        Calendar cal = Calendar.getInstance();
        Integer toYear = new Integer(cal.get(java.util.Calendar.YEAR));
        // get startYear from Global Settings
        Integer fromYear = new Integer(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.YEAR_RANGE_START));
        if (fromYear > toYear) {
            fromYear = toYear;
        }
        if (!from) {
            toYear++;
        }
        return getYears(fromYear.intValue(), toYear.intValue(), defaultCalendar.getIsFiscal());
    }

    public static List<LabelValueBean> getYears(int fromYear, int toYear, boolean isCalendarFiscal) {
        List<LabelValueBean> years = new ArrayList<LabelValueBean>();
        for (int year = fromYear; year <= toYear; year++) {
            String label = null;
            if (isCalendarFiscal) {
                String nextYear = new Integer(year + 1).toString();
                label = "FY";
                label += " " + year + "/" + nextYear.substring(2);
            } else {
                label = "" + year;
            }
            LabelValueBean lvb = new LabelValueBean(label, new Integer(year).toString());
            years.add(lvb);
        }
        return years;
    }
}