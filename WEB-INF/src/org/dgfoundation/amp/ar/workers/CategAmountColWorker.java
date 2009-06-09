/**
 * CategAmountColWorker.java
 * (c) 2005 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar.workers;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.ar.AmountCellColumn;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.CellColumn;
import org.dgfoundation.amp.ar.FundingTypeSortedString;
import org.dgfoundation.amp.ar.MetaInfo;
import org.dgfoundation.amp.ar.ReportGenerator;
import org.dgfoundation.amp.ar.cell.CategAmountCell;
import org.dgfoundation.amp.ar.cell.Cell;
import org.digijava.module.aim.dbentity.AmpReportHierarchy;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.fiscalcalendar.ICalendarWorker;
import org.digijava.module.aim.util.FeaturesUtil;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Jun 13, 2006
 * 
 */
public class CategAmountColWorker extends ColumnWorker {

	
	protected Map metaInfoCache;

	protected MetaInfo getCachedMetaInfo(String category, Comparable value) {
		Map valuesMap = (Map) metaInfoCache.get(category);
		if (valuesMap == null) {
			valuesMap = new HashMap();
			metaInfoCache.put(category, valuesMap);
		}
		MetaInfo mi = (MetaInfo) valuesMap.get(value);
		if (mi != null)
			return mi;
		mi = new MetaInfo(category, value);
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
		
		//proposed cost is by default not showable and should not appear in any funding totals. it is used to ease the use of destination post processed columns
		if(this.getViewName().equals(ArConstants.VIEW_PROPOSED_COST))
		    return false;
		AmpARFilter filter=(AmpARFilter) generator.getFilter();
		
		//we now check if the year filtering is used - we do not want items from other years to be shown
		//now this is null due we have one field 
		try {
			if(filter.getFromDate()!=null || filter.getToDate()!=null) {
				java.util.Date tDate=(java.util.Date) MetaInfo.getMetaInfo(cac.getMetaData(),ArConstants.TRANSACTION_DATE).getValue();
				
				
				if (filter.getFromDate()!=null  && !("".equalsIgnoreCase(filter.getFromDate()))){
					java.util.Date sDate=FormatHelper.parseDate2(filter.getFromDate());
					if (tDate.before(sDate)) showable=false;
				}
				
				if (filter.getToDate()!=null && !("".equalsIgnoreCase(filter.getToDate()))){
					java.util.Date toDate=FormatHelper.parseDate2(filter.getToDate());
					if (tDate.after(toDate)) showable=false;
				}
		}
		/* if(filter.getYearFrom()!=null || filter.getYearTo()!=null) {
			Integer itemYear=(Integer) MetaInfo.getMetaInfo(cac.getMetaData(),ArConstants.YEAR).getValue();
			if(filter.getYearFrom()!=null && filter.getYearFrom().intValue()>itemYear.intValue()) showable=false;
			if(filter.getYearTo()!=null && filter.getYearTo().intValue()<itemYear.intValue()) showable=false;
		}*/
		} catch (Exception e) {
			logger.error("Can't define if cell is Showable possible parse error detected",e );
		}

		return showable;
	}

	
	public boolean isRenderizable(CategAmountCell cac) {
		boolean renderizable=true;
		
	
		AmpARFilter filter=(AmpARFilter) generator.getFilter();
		
		
		//we now check if the year filtering is used - we do not want items from other years to be shown
		if((filter.getRenderStartYear()!=null && filter.getRenderStartYear()> 0) || (filter.getRenderEndYear()!=null && filter.getRenderEndYear() > 0  )) {
			Integer itemYear=(Integer) MetaInfo.getMetaInfo(cac.getMetaData(),ArConstants.YEAR).getValue();
			
			if(filter.getRenderStartYear()!=null &&  filter.getRenderStartYear() > 0 &&
				itemYear.intValue() < filter.getRenderStartYear().intValue()) renderizable=false;
			
			if(filter.getRenderEndYear()!=null &&  filter.getRenderEndYear()>0 &&
				itemYear.intValue() > filter.getRenderEndYear().intValue()) renderizable=false;
		}
		return renderizable;
	}
	
	protected boolean isCummulativeShowable(CategAmountCell cac) {
		AmpARFilter filter=(AmpARFilter) generator.getFilter();
		if(filter.getYearTo()==null) return true;
		int cellYear=Integer.parseInt(cac.getMetaValueString(ArConstants.YEAR));
		if(cellYear>filter.getYearTo().intValue()) return false;
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
		BigDecimal tr_amount = rs.getBigDecimal("transaction_amount");
		java.sql.Date td= rs.getDate("transaction_date");
		
		String currencyCode="";
		
		if (columnsMetaData.contains("currency_code")){
		    currencyCode=rs.getString("currency_code");
		}
		
		String donorGroupName=null;
		String donorTypeName=null;
		Double fixedExchangeRate = null;;
		
		//the most important meta name, the source name (donor name, region name, component name)
		String headMetaName=rsmd.getColumnName(4).toLowerCase();


		if (columnsMetaData.contains("fixed_exchange_rate")){
		    fixedExchangeRate=rs.getDouble("fixed_exchange_rate");
		}

		if (columnsMetaData.contains("adjustment_type")){
		    	adj_type = rs.getInt("adjustment_type");
		}
		
		if(columnsMetaData.contains("donor_type_name"))
			donorTypeName=rs.getString("donor_type_name");
					
		
		if (columnsMetaData.contains("transaction_type")){
			tr_type  = rs.getInt("transaction_type");
		}
		
		if (columnsMetaData.contains("org_grp_name")) {
			donorGroupName	= rs.getString("org_grp_name");
		}
		
		
		//tr_type == DISBURSEMENT_ORDERS
		if( tr_type == Constants.DISBURSEMENT_ORDER && filter.getDisbursementOrderRejected()!=null){
			if (columnsMetaData.contains("disb_ord_rej")) {
				Boolean disbursementOrderRejected = rs.getBoolean("disb_ord_rej");
				if(!filter.getDisbursementOrderRejected().equals(disbursementOrderRejected))
					return null;
			}			
		}
			
		
		String value=FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SPLIT_BY_TYPE_OF_ASSISTANCE);
		boolean skpyCategorize=("false".equalsIgnoreCase(value));
        //if (generator.getReportMetadata().getHierarchies().contains()
		//check if the column is a into Hierarchies //Ned to talk with mihai to find a better solution
		
		Set<AmpReportHierarchy> hierarchies =generator.getReportMetadata().getHierarchies(); 
		for (AmpReportHierarchy ampReportHierarchy : hierarchies) {
			if (ArConstants.TERMS_OF_ASSISTANCE.equalsIgnoreCase(ampReportHierarchy.getColumn().getColumnName())){
				skpyCategorize=false;
				break ;
			}
		}
		
		if (!skpyCategorize){
			if (columnsMetaData.contains("terms_assist_name")){
				String termsAssist = rs.getString("terms_assist_name");
				MetaInfo termsAssistMeta = this.getCachedMetaInfo(ArConstants.TERMS_OF_ASSISTANCE,
						termsAssist);
				acc.getMetaData().add(termsAssistMeta);
			}
        }
			
		if (columnsMetaData.contains("financing_instrument_name")){			
		    	String financingInstrument = rs.getString("financing_instrument_name");
			MetaInfo termsAssistMeta = this.getCachedMetaInfo(ArConstants.FINANCING_INSTRUMENT,
					financingInstrument);
			acc.getMetaData().add(termsAssistMeta);
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

		
		acc.setAmount(FeaturesUtil.applyThousandsForVisibility(tr_amount));
		
		//use fixed exchange rate only if it has been entered. Else use Agency
		if (fixedExchangeRate != null && fixedExchangeRate != 0) {
			acc.setFromExchangeRate(fixedExchangeRate);
		} else {
			acc.setFromExchangeRate(Util.getExchange(currencyCode, td));
		}
		
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
		case 4:
			trStr = ArConstants.DISBURSEMENT_ORDERS;
			break;
			
		}

		if(trStr!=null) {
			MetaInfo trMs = this.getCachedMetaInfo(ArConstants.TRANSACTION_TYPE, trStr);
			String fundMes = (String) adjMs.getValue()+ " " + (String) trMs.getValue();
			Integer order = this.generator.getReportMetadata().getMeasureOrder(fundMes);
			MetaInfo fundMs = this.getCachedMetaInfo(ArConstants.FUNDING_TYPE, new FundingTypeSortedString(fundMes, order));
			acc.getMetaData().add(trMs);
			acc.getMetaData().add(fundMs);
			acc.getMetaData().add(adjMs);			
		}	
		//Date handling..
		
		
		if (td==null) 
			logger.error("MISSING DATE FOR FUNDING id ="+id+ " of activity id ="+ ownerId);
		
		
		String quarter=null;
		Comparable month=null;		
		Integer year=null;
		
		if (filter.getCalendarType() != null) {
			try {
				ICalendarWorker worker = filter.getCalendarType().getworker();
				worker.setTime(td);
				month = worker.getMonth();
				quarter = "Q" + worker.getQuarter();
				year = worker.getYear();
				//The complete will be used to see if the cell is showable
				MetaInfo dateInfo = this.getCachedMetaInfo(ArConstants.TRANSACTION_DATE, worker.getDate());
				acc.getMetaData().add(dateInfo);
				
			} catch (Exception e) {
				logger.error("Error gettin fiscal year of activity id =" + id);
			}
		}

		
		MetaInfo qMs = this.getCachedMetaInfo(ArConstants.QUARTER,quarter);
		MetaInfo mMs = this.getCachedMetaInfo(ArConstants.MONTH,month);
		MetaInfo aMs = this.getCachedMetaInfo(ArConstants.YEAR, year);

		
		
		
		//add the newly created metainfo objects to the virtual funding object
	
		acc.getMetaData().add(aMs);
		acc.getMetaData().add(qMs);
		acc.getMetaData().add(mMs);		
		acc.getMetaData().add(headMeta);
		
		if(this.getViewName().equals("v_proposed_cost")) {
		    //used as a flag, no value needed
		    MetaInfo costMs = this.getCachedMetaInfo(ArConstants.PROPOSED_COST, null);
		    acc.getMetaData().add(costMs);
		}
		
		if(donorGroupName!=null) {
			MetaInfo donorGroupMs = this.getCachedMetaInfo(ArConstants.DONOR_GROUP, donorGroupName);
			acc.getMetaData().add(donorGroupMs);
		}
		
		if(donorTypeName!=null) {
			MetaInfo donorTypeMs = this.getCachedMetaInfo(ArConstants.DONOR_TYPE_COL, donorTypeName);
			acc.getMetaData().add(donorTypeMs);
		}
		
		//set the showable flag, based on selected measures - THIS NEEDS TO BE MOVED OUT
		//TODO: move this to postProcess!!
		acc.setShow(isShowable(acc));
		acc.setRenderizable(isRenderizable(acc));
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
