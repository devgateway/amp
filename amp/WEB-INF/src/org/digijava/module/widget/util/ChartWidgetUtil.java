package org.digijava.module.widget.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.utils.AmpCollectionUtils.KeyWorker;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.translator.CachedTranslatorWorker;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpIndicatorValue;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.IndicatorSector;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.aim.helper.CurrencyWorker;
import org.digijava.module.translation.util.DbUtil;
import org.digijava.module.widget.dbentity.AmpDaWidgetPlace;
import org.digijava.module.widget.dbentity.AmpWidget;
import org.digijava.module.widget.dbentity.AmpWidgetIndicatorChart;
import org.digijava.module.widget.helper.ChartOption;
import org.digijava.module.widget.helper.DonorSectorFundingHelper;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;


/**
 * Chart widgets util.
 * @author Irakli Kobiashvili
 *
 */
public class ChartWidgetUtil {

    private static Logger logger = Logger.getLogger(ChartWidgetUtil.class);

    public static JFreeChart getIndicatorChart(IndicatorSector indicatorCon,ChartOption opt) throws DgException{
		JFreeChart result = null;
		TimeSeriesCollection ds = getIndicatorChartDataset(indicatorCon);
		boolean tooltips = false;
		boolean urls = false;
		opt.setShowLegend(true);//TODO add one JPG on page as legend and turn this off, this is temporary.
		Font font = new Font(null,0,8);
		//create time series line chart
		result = ChartFactory.createTimeSeriesChart(opt.getTitle(), null, null, ds, opt.isShowLegend(), tooltips, urls);
		result.getLegend().setItemFont(font);		
		result.getTitle().setFont(font);
		XYPlot plot =(XYPlot) result.getPlot();
		XYItemRenderer renderer = plot.getRenderer();
		renderer.setItemLabelFont(font);
		plot.setRangeZeroBaselineVisible(true);
		plot.getDomainAxis().setLabelFont(font);
		//For next two lines see order of TimeSeries added to TimeSeriesCollection in getIndicatorChartDataset() below
		renderer.setSeriesPaint(0, Color.blue);//0 = Actual line, because this was added first.
		renderer.setSeriesPaint(1, Color.red);//1 = Target line, because this was added second.
		if (renderer instanceof XYLineAndShapeRenderer){
			XYLineAndShapeRenderer r = (XYLineAndShapeRenderer)renderer;
			r.setBaseShapesVisible(false);
			r.setBaseShapesFilled(true);
//			r.setItemLabelsVisible(true);
		}
		return result;
	}
    
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

	public static AmpWidgetIndicatorChart getIndicatorChartWidget(AmpDaWidgetPlace place) throws DgException{
		AmpWidget widget = WidgetUtil.getWidgetOnPlace(place.getId());
		if (widget==null) return null;
		return (AmpWidgetIndicatorChart)widget;
	}
	
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
	
	public static JFreeChart getSectorByDonorChart(Long[] donors,Integer year,ChartOption opt)throws DgException,WorkerException{
		JFreeChart result = null;
		PieDataset ds = getSectorByDonorDataset(donors,year);
		String selYear = (year==null)?"All years":year.toString();
                //String titleMsg= TranslatorWorker.translate("widget:piechart:breakdownbysector", opt.getLangCode(), opt.getSiteId());
                Message msg= DbUtil.getMessage("widget:piechart:breakdownbysector", opt.getLangCode(), opt.getSiteId());
                String titleMsg="";
                if(msg!=null){
                   titleMsg=msg.getMessage(); 
                }
		String title = (opt.isShowTitle())? titleMsg+" ("+selYear+")":null;
		boolean tooltips = false;
		boolean urls = false;
		result = ChartFactory.createPieChart(title, ds, opt.isShowLegend(), tooltips, urls);
		PiePlot plot = (PiePlot)result.getPlot();

		if (opt.isShowTitle()){
			Font font = new Font(null,0,12);
			result.getTitle().setFont(font);
		}
		
		if (opt.isShowLabels()){
			String pattern = "{0} = {1} ({2})";
			if (opt.getLabelPattern()!=null){
				pattern=opt.getLabelPattern();
			}
			PieSectionLabelGenerator gen = new StandardPieSectionLabelGenerator(pattern,new DecimalFormat("0"),new DecimalFormat("0.0%"));
			plot.setLabelGenerator(gen);
		}else{
			plot.setLabelGenerator(null);
		}
		
		//plot.setSectionOutlinesVisible(false);
		plot.setIgnoreNullValues(true);
		plot.setIgnoreZeroValues(true);
		return result;
	}
	
	public static PieDataset getSectorByDonorDataset(Long[] donors, Integer year) throws DgException{
		DefaultPieDataset ds = new DefaultPieDataset();
		Date fromDate = null;
		Date toDate = null;
		if (year!=null){
			fromDate=getStartOfYear(year.intValue());
			toDate = getStartOfYear(year.intValue()+1);
		}
                Double[] allFundingWrapper={new Double(0)};// to hold whole funding value
		Collection<DonorSectorFundingHelper> fundings=getDonorSectorFunding(donors, fromDate, toDate,allFundingWrapper);
		if (fundings!=null){
                         double otherFunfing=0;
			for (DonorSectorFundingHelper funding : fundings) {
                             Double percent = funding.getFounding() / allFundingWrapper[0];
                            // the sectors which percent is less then 10% should be group in "Other"
                            if (percent > 0.1) {
                                ds.setValue(funding.getSector().getName(), funding.getFounding());
                            } else {
                                otherFunfing += funding.getFounding();
                            }
			}
                        if(otherFunfing!=0){
                            ds.setValue("Other", otherFunfing);
                        }		
		}
		return ds;
	}
	
	public static Date getStartOfYear(int year){
		GregorianCalendar cal = new GregorianCalendar();
		cal.set(year, 0, 1, 0, 0, 0);
		return cal.getTime();
	}
	
    public static Collection<DonorSectorFundingHelper> getDonorSectorFunding(Long donorIDs[],Date fromDate, Date toDate,Double[] wholeFunding) throws DgException {
    	Collection<DonorSectorFundingHelper> fundings=null;  
		String oql = "select f.ampDonorOrgId, sa.sectorId, sa.sectorPercentage, sa.activityId.ampActivityId, fd.ampCurrencyId, sum(fd.transactionAmount)";
		oql += " from ";
		oql += AmpFunding.class.getName() + " as f, ";
		oql += AmpFundingDetail.class.getName() + " as fd, ";
		// oql+=AmpActivity.class.getName()+" as a, ";
		// oql+=AmpSector.class.getName()+" as s, ";
		oql += AmpActivitySector.class.getName() + " as sa, ";
                oql+=AmpSector.class.getName() + " as s ";
		oql += " where fd.ampFundingId.ampActivityId.ampActivityId = sa.activityId.ampActivityId";
		oql += " and s.parentSectorId is null and s.ampSectorId=sa.sectorId.ampSectorId and fd.transactionType = 0 and fd.adjustmentType = 1 ";
		if (donorIDs != null && donorIDs.length > 0) {
			oql += " and (fd.ampFundingId.ampDonorOrgId in ("+ getInStatment(donorIDs) + ") ) ";
		}
		if (fromDate != null && toDate != null) {
			oql += " and (fd.transactionDate between :fDate and  :eDate ) ";
		}
		oql += " group by fd.ampFundingId.ampDonorOrgId, sa.sectorId, sa.activityId.ampActivityId, fd.ampCurrencyId";
		oql += " order by fd.ampFundingId.ampDonorOrgId, sa.sectorId";

		Session session = PersistenceManager.getRequestDBSession();

		//search for grouped data
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
		
		//Process groupped data
		if (result != null) {
			Map<Long, DonorSectorFundingHelper> donors = new HashMap<Long, DonorSectorFundingHelper>();
			for (Object row : result) {
				Object[] rowData = (Object[]) row;
				//AmpOrganisation donor = (AmpOrganisation) rowData[0];
				AmpSector sector = (AmpSector) rowData[1];
				Float sectorPrcentage = (Float) rowData[2];
				//AmpActivity activity = (AmpActivity) rowData[3];
				AmpCurrency currency = (AmpCurrency) rowData[4];
				Double amount = (Double) rowData[5];
				//calculate percentage
				Double calculated = (sectorPrcentage.floatValue() == 100)?amount:calculatePercentage(amount,sectorPrcentage);
				//convert to
				Double converted = convert(calculated, currency);
				//search if we already have such sector data
				DonorSectorFundingHelper sectorFundngObj = donors.get(sector.getAmpSectorId());
				//if not create and add to map
				if (sectorFundngObj == null){
					sectorFundngObj = new DonorSectorFundingHelper(sector);
					donors.put(sector.getAmpSectorId(), sectorFundngObj);
				}
				//add amount to sector
				sectorFundngObj.addFunding(converted.doubleValue());
                                //calculate whole funding information
                                wholeFunding[0]+=converted.doubleValue();
			}
			fundings = donors.values(); 
		}
		return fundings;
	}
    
    public static Double convert(Double amount,AmpCurrency originalCurrency){
    	try {
			CurrencyWorker.convertToUSD(amount, originalCurrency.getCurrencyCode());
		} catch (AimException e) {
			//e.printStackTrace();
			return amount;
		}
    	return amount;
    }
    
    public static Double calculatePercentage(Double amount,Float percentage){
    	double result=amount.doubleValue()*percentage.floatValue()/100;
    	return new Double(result);
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
    
    public static class DonorIdWorker implements KeyWorker<Long, AmpOrganisation>{

		public void markKeyForRemoval(AmpOrganisation element) {
			//empty, no need yet
		}

		public void updateKey(AmpOrganisation element, Long newKey) {
			element.setAmpOrgId(newKey);
		}

		public Long resolveKey(AmpOrganisation element) {
			return element.getAmpOrgId();
		}
    	
    }

	
}
