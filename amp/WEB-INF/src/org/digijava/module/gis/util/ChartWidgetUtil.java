package org.digijava.module.gis.util;

import java.awt.Font;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import org.apache.log4j.Logger;
import org.apache.struts.util.LabelValueBean;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpIndicatorValue;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.IndicatorSector;
import org.digijava.module.aim.logic.DonorFundingCalculator;
import org.digijava.module.gis.dbentity.AmpDaWidgetPlace;
import org.digijava.module.gis.dbentity.AmpWidget;
import org.digijava.module.gis.dbentity.AmpWidgetIndicatorChart;
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
	
	public static JFreeChart getSectorByDonorChart(Collection<AmpOrganisation> donors,String year)throws DgException{
		JFreeChart result = null;
		PieDataset ds = getSectorByDonorDataset(donors,year);
		String valuesAxisLabel = "Babuawvera";
		String categAxisLabel = "Date";
		String selYear = (year==null)?"2008":year;
		String title = "Breackdown by sector ("+selYear+")";
		boolean legend = true;
		boolean tooltips = false;
		boolean urls = false;
		result = ChartFactory.createPieChart(title, ds, legend, tooltips, urls);
		Font font = new Font(null,0,12);
		result.getTitle().setFont(font);
		
		PiePlot plot = (PiePlot)result.getPlot();
		//plot.setLegendLabelGenerator(new GisPieLabelGenerator());
		
		plot.setSectionOutlinesVisible(false);
		plot.setIgnoreNullValues(true);
		plot.setIgnoreZeroValues(true);
		return result;
	}
	public static PieDataset getSectorByDonorDataset(Collection<AmpOrganisation> donors, String year) throws DgException{
		DefaultPieDataset ds = new DefaultPieDataset();
		ds.setValue("Test data 1", 200);
		ds.setValue("Test data 2", 400);
		ds.setValue("Test data 3", 380);
		ds.setValue("Test data 4", 200);
		ds.setValue("Test data 5", 0.0);
		ds.setValue("Test data 6", null);
		ds.setValue("Test data 7", 230);
		DonorFundingCalculator bla=null;
		
		return ds;
	}
    public static List<LabelValueBean> getSectorFunding(Long donorIDs[],Date startDate, Date endDate)throws DgException{
    	String oql = "select f.ampDonorOrgId, sa.sectorId, fd.ampCurrencyId sum(fd.)";
    	oql+=" from ";
    	oql+=AmpFunding.class.getName()+" as f, ";
    	oql+=AmpFundingDetail.class.getName()+" as fd, ";
    	oql+=AmpActivity.class.getName()+" as a, ";
    	oql+=AmpActivitySector.class.getName()+" as sa, ";
    	oql+=AmpSector.class.getName()+" as sa ";
    	oql+=" where fd.ampFundingId.ampActivityId.ampActivityId = sa.activityId.ampActivityId";
    	if (donorIDs!=null && donorIDs.length>0){
    		oql+=" and (fd.ampFundingId.ampDonorOrgId in ("+getInStatment(donorIDs)+") ) ";
    	}
    	oql+=" group by fd.ampFundingId.ampDonorOrgId, sa.sectorId, fd.ampCurrencyId";
    	oql+=" order by fd.ampFundingId.ampDonorOrgId, sa.sectorId";
    	
    	Session session=PersistenceManager.getRequestDBSession();
    	
    	List result = null;
    	try {
			Query query = session.createQuery(oql);
			result = query.list();
		} catch (Exception e) {
			throw new DgException("Cannot load sector fundings by donors from db",e);
		}
		
    	if(result!=null){
    		
    	}
		
   	 return null;
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

	
}
