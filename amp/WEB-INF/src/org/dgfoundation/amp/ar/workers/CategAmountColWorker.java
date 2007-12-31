/**
 * CategAmountColWorker.java
 * (c) 2005 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar.workers;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.ar.ARUtil;
import org.dgfoundation.amp.ar.AmountCellColumn;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.CellColumn;
import org.dgfoundation.amp.ar.FundingTypeSortedString;
import org.dgfoundation.amp.ar.MetaInfo;
import org.dgfoundation.amp.ar.ReportGenerator;
import org.dgfoundation.amp.ar.cell.CategAmountCell;
import org.dgfoundation.amp.ar.cell.Cell;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.helper.BaseCalendar;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.EthiopianCalendar;
import org.digijava.module.aim.util.DbUtil;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Jun 13, 2006
 * 
 */
public class CategAmountColWorker extends ColumnWorker {

	
    	
    
	
    	protected GregorianCalendar calendar;
    	protected EthiopianCalendar ethcalendar;
    	protected DateFormatSymbols dfs;
    	protected Map metaInfoCache;
    	protected MetaInfo getCachedMetaInfo(String category, Comparable value) {	    
	    Map valuesMap=(Map) metaInfoCache.get(category);
	    if(valuesMap==null) { 
		valuesMap=new HashMap();
		metaInfoCache.put(category, valuesMap);
	    }
	    MetaInfo mi=(MetaInfo) valuesMap.get(value);
	    if(mi!=null) return mi;
	    mi=new MetaInfo(category,value);
	    valuesMap.put(value, mi);
	    return mi;	    
	}
    	
	/**
	 * @param condition
	 * @param viewName
	 * @param columnName
	 */
	public CategAmountColWorker(String condition, String viewName,
			String columnName,ReportGenerator generator) {
		super(condition, viewName, columnName,generator);
		calendar=new GregorianCalendar();
		dfs=new DateFormatSymbols();
		ethcalendar=new EthiopianCalendar();
		this.metaInfoCache=new HashMap();
	}

	/**filter.getFromYear()!=null
	 * Decides if the CategAmountCell is showable or not, based on the measures selected
	 * in the report wizard.
	 * @param cac the given CategAmountCell
	 * @return true if showable
	 */
	public boolean isShowable(CategAmountCell cac) {
		boolean showable=true;
		AmpARFilter filter=(AmpARFilter) generator.getFilter();
		
		Set measures=generator.getReportMetadata().getMeasures();
		showable=ARUtil.containsMeasure(cac.getMetaValueString(ArConstants.FUNDING_TYPE),measures) || generator.getReportMetadata().getType().intValue()==4;
		if(!showable) return false;
		
		//we now check if the year filtering is used - we do not want items from other years to be shown
		if(filter.getFromYear()!=null || filter.getToYear()!=null) {
			Integer itemYear=(Integer) MetaInfo.getMetaInfo(cac.getMetaData(),ArConstants.YEAR).getValue();
			if(filter.getFromYear()!=null && filter.getFromYear().intValue()>itemYear.intValue()) showable=false;
			if(filter.getToYear()!=null && filter.getToYear().intValue()<itemYear.intValue()) showable=false;
		}
		return showable;
	}

	
	protected boolean isCummulativeShowable(CategAmountCell cac) {
		AmpARFilter filter=(AmpARFilter) generator.getFilter();
		if(filter.getToYear()==null) return true;
		int cellYear=Integer.parseInt(cac.getMetaValueString(ArConstants.YEAR));
		if(cellYear>filter.getToYear().intValue()) return false;
		return true;
	}

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dgfoundation.amp.ar.workers.ColumnWorker#getCellFromRow(java.sql.ResultSet,
	 *      java.lang.String)
	 */
	protected Cell getCellFromRow(ResultSet rs) throws SQLException {
		Long ownerId = rs.getLong(1);
		Long id = rs.getLong(3);
		CategAmountCell acc = new CategAmountCell(ownerId);

		acc.setId(id);

		AmpARFilter filter=(AmpARFilter) generator.getFilter();
		
		
		int tr_type = -1;
		int adj_type = -1;
		double tr_amount = rs.getDouble("transaction_amount");
		String tds = rs.getString("transaction_date");
		 
		java.sql.Date td=null;
		try {
		    td = new Date(sdf.parse(tds).getTime());
		} catch (Exception e1) {
		    logger.error(e1);
		    logger.info("Exception encountered parsing a transaction date!", e1);
		}
		//double exchangeRate=rs.getDouble("exchange_rate");
		String currencyCode=rs.getString("currency_code");
		String perspectiveCode=null; 
		double fixedExchangeRate=1;
		
		//the most important meta name, the source name (donor name, region name, component name)
		String headMetaName=rsmd.getColumnName(4);


		try {
			fixedExchangeRate=rs.getDouble("fixed_exchange_rate");
		
		} catch (SQLException e) {

		}
		

		try {
			perspectiveCode=rs.getString("perspective_code");
		
		} catch (SQLException e) {

		}

		
		try {
			adj_type = rs.getInt("adjustment_type");
			tr_type  = rs.getInt("transaction_type");
		
		} catch (SQLException e) {

		}
		
		try {
			String termsAssist = rs.getString("terms_assist_name");
			MetaInfo termsAssistMeta = this.getCachedMetaInfo(ArConstants.TERMS_OF_ASSISTANCE,
					termsAssist);
			acc.getMetaData().add(termsAssistMeta);
		} catch (SQLException e) {

		}
		
		try {
			String financingInstrument = rs.getString("financing_instrument_name");
			MetaInfo termsAssistMeta = this.getCachedMetaInfo(ArConstants.FINANCING_INSTRUMENT,
					financingInstrument);
			acc.getMetaData().add(termsAssistMeta);
		} catch (SQLException e) {

		}


		MetaInfo headMeta=null;
		
		if("region_name".equals(headMetaName)){
			String regionName = rs.getString("region_name");
			headMeta= this.getCachedMetaInfo(ArConstants.REGION, regionName);			
		} else
		
		if("component_name".equals(headMetaName)){
			String componentName = rs.getString("component_name");
			headMeta= this.getCachedMetaInfo(ArConstants.COMPONENT, componentName);			
		} else
	
		if("donor_name".equals(headMetaName)){
			String donorName = rs.getString("donor_name");
			headMeta= this.getCachedMetaInfo(ArConstants.DONOR, donorName);			
		}

		acc.setAmount(tr_amount);
		
		//use fixed exchange rate only if it has been entered. Else use 
		if(fixedExchangeRate!=1 && fixedExchangeRate!=0)
			acc.setFromExchangeRate(fixedExchangeRate); else
			
			//new and fast - cached
			//acc.setFromExchangeRate(1);
			acc.setFromExchangeRate(Util.getExchange(currencyCode,td));
			    
			//OLD AND SLOW - db based
			//acc.setFromExchangeRate(exchangeRate);
		
		acc.setCurrencyDate(td);
		acc.setCurrencyCode(currencyCode);
		//put toExchangeRate
		acc.setToExchangeRate(1);
		
		
		MetaInfo adjMs = this.getCachedMetaInfo(ArConstants.ADJUSTMENT_TYPE,
				adj_type == 0 ? ArConstants.PLANNED : ArConstants.ACTUAL);
		String trStr = null;

		switch (tr_type) {
		case 0:
			trStr = ArConstants.COMMITMENT;
			break;
		case 1:
			trStr = ArConstants.DISBURSEMENT;
			break;
		case 2:
			trStr = ArConstants.EXPENDITURE;
			break;
		}

		if(trStr!=null) {
		MetaInfo trMs = this.getCachedMetaInfo(ArConstants.TRANSACTION_TYPE, trStr);
		MetaInfo fundMs = this.getCachedMetaInfo(ArConstants.FUNDING_TYPE, new FundingTypeSortedString((String) adjMs
				.getValue()
				+ " " + (String) trMs.getValue()));
		acc.getMetaData().add(trMs);
		acc.getMetaData().add(fundMs);
		acc.getMetaData().add(adjMs);
		}	
		//Date handling..
		
		
		if (td!=null) calendar.setTime(td); else 
			logger.error("MISSING DATE FOR FUNDING id ="+id+ " of activity id ="+ ownerId);
		
		
		String quarter=null;
		String month=null;
		Integer year=null;
		
//		AMP-2212
		if(filter.getCalendarType()==null || filter.getCalendarType().getBaseCal().equalsIgnoreCase(BaseCalendar.BASE_GREGORIAN.getValue())) {
			int monthId=calendar.get(Calendar.MONTH);
			month=Integer.toString(monthId)+"-"+dfs.getMonths()[monthId];
			quarter= "Q"+ new Integer(calendar.get(Calendar.MONTH) / 4 + 1);
			year=new Integer(calendar.get(Calendar.YEAR));
		} else
		    //AMP-2212
		if(filter.getCalendarType().getBaseCal().equalsIgnoreCase(BaseCalendar.BASE_ETHIOPIAN.getValue()) || 
			filter.getCalendarType().getBaseCal().equalsIgnoreCase(BaseCalendar.BASE_ETHIOPIAN_FISCAl.getValue())) {		    	
			EthiopianCalendar ec = ethcalendar.getEthiopianDate(calendar);
			//AMP-2212
			if(filter.getCalendarType().getBaseCal().equalsIgnoreCase(BaseCalendar.BASE_ETHIOPIAN_FISCAl.getValue()))
			{
				year=new Integer(ec.ethFiscalYear);
				quarter=new String("Q"+ec.ethFiscalQrt);
				month=Integer.toString(ec.ethMonth)+"-"+ec.ethMonthName;
			}//AMP-2212
			if(filter.getCalendarType().getBaseCal().equalsIgnoreCase(BaseCalendar.BASE_ETHIOPIAN.getValue()))
			{
				year=new Integer(ec.ethYear);
				quarter=new String("Q"+ec.ethQtr);
				month=Integer.toString(ec.ethMonth)+"-"+ec.ethMonthName;
			}
		}

		
		
		
		if(perspectiveCode!=null) {
//			we eliminate the perspective items that do not match the filter one
			MetaInfo perspMs=this.getCachedMetaInfo(ArConstants.PERSPECTIVE,perspectiveCode);
			if(!filter.getPerspective().getCode().equals(perspMs.getValue())) return null;
			
		acc.getMetaData().add(perspMs);
		}

		
		MetaInfo qMs = this.getCachedMetaInfo(ArConstants.QUARTER,quarter);
		MetaInfo mMs = this.getCachedMetaInfo(ArConstants.MONTH,month);
		MetaInfo aMs = this.getCachedMetaInfo(ArConstants.YEAR, year);

		
		//add the newly created metainfo objects to the virtual funding object
	
		acc.getMetaData().add(aMs);
		acc.getMetaData().add(qMs);
		acc.getMetaData().add(mMs);		
		acc.getMetaData().add(headMeta);
		
		//set the showable flag, based on selected measures - THIS NEEDS TO BE MOVED OUT
		//TODO: move this to postProcess!!
		acc.setShow(isShowable(acc));
		acc.setCummulativeShow(isCummulativeShowable(acc));
		
		
		
		//UGLY get exchage rate if cross-rates are needed (if we need to convert from X to USD and then to Y)
		if(filter.getCurrency()!=null && !"USD".equals(filter.getCurrency().getCurrencyCode()))  
			acc.setToExchangeRate(Util.getExchange(filter.getCurrency().getCurrencyCode(),td));
						
		
		
		return acc;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dgfoundation.amp.ar.workers.ColumnWorker#getCellFromCell(org.dgfoundation.amp.ar.cell.Cell)
	 */
	protected Cell getCellFromCell(Cell src) {
		// TODO Auto-generated method stub
		return null;
	}

	public CellColumn newColumnInstance(int initialCapacity) {
		return new AmountCellColumn(columnName,initialCapacity);
	}

	public Cell newCellInstance() {
		return new CategAmountCell();
	}

}
