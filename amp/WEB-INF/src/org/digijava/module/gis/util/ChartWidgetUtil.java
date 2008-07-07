package org.digijava.module.gis.util;

import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
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
import org.digijava.module.gis.dbentity.AmpDaWidgetPlace;
import org.digijava.module.gis.dbentity.AmpWidget;
import org.digijava.module.gis.dbentity.AmpWidgetIndicatorChart;
import org.digijava.module.gis.helper.DonorSectorFundingHelper;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
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

    public static JFreeChart getIndicatorChart(IndicatorSector indicatorCon) throws DgException{
		JFreeChart result = null;
		TimeSeriesCollection ds = getIndicatorChartDataset(indicatorCon);
		String valuesAxisLabel = indicatorCon.getIndicator().getName();
		String categAxisLabel = "Date";
		String title = indicatorCon.getIndicator().getName();
		boolean legend = false;
		boolean tooltips = false;
		boolean urls = false;
		result = ChartFactory.createTimeSeriesChart(title, null, null, ds, legend, tooltips, urls);
		Font font = new Font(null,0,12);
		result.getTitle().setFont(font);
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
	
	public static JFreeChart getSectorByDonorChart(Long[] donors,Integer year)throws DgException{
		JFreeChart result = null;
		PieDataset ds = getSectorByDonorDataset(donors,year);
		String selYear = (year==null)?"All years":year.toString();
		String title = "Breackdown by sector ("+selYear+")";
		boolean legend = true;
		boolean tooltips = false;
		boolean urls = false;
		result = ChartFactory.createPieChart(title, ds, legend, tooltips, urls);
		Font font = new Font(null,0,12);
		result.getTitle().setFont(font);
		
		PiePlot plot = (PiePlot)result.getPlot();
		
		plot.setSectionOutlinesVisible(false);
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

		Collection<DonorSectorFundingHelper> fundings=getDonorSectorFunding(donors, fromDate, toDate);
		if (fundings!=null){
			for (DonorSectorFundingHelper funding : fundings) {
				ds.setValue(funding.getSector().getName(), funding.getFounding());
			}
		}
		return ds;
	}
	
	public static Date getStartOfYear(int year){
		GregorianCalendar cal = new GregorianCalendar();
		cal.set(year, 0, 1, 0, 0, 0);
		return cal.getTime();
	}
	
    public static Collection<DonorSectorFundingHelper> getDonorSectorFunding(Long donorIDs[],Date fromDate, Date toDate) throws DgException {
    	Collection<DonorSectorFundingHelper> fundings=null;
		String oql = "select f.ampDonorOrgId, sa.sectorId, sa.sectorPercentage, sa.activityId.ampActivityId, fd.ampCurrencyId, sum(fd.transactionAmount)";
		oql += " from ";
		oql += AmpFunding.class.getName() + " as f, ";
		oql += AmpFundingDetail.class.getName() + " as fd, ";
		// oql+=AmpActivity.class.getName()+" as a, ";
		// oql+=AmpSector.class.getName()+" as s, ";
		oql += AmpActivitySector.class.getName() + " as sa ";
		oql += " where fd.ampFundingId.ampActivityId.ampActivityId = sa.activityId.ampActivityId";
		oql += " and fd.transactionType = 0 and fd.adjustmentType = 1";
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
