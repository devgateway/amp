package org.digijava.module.aim.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.Util;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.helper.fiscalcalendar.BaseCalendar;
import org.digijava.module.aim.helper.fiscalcalendar.EthiopianCalendar;
import org.digijava.module.aim.helper.fiscalcalendar.ICalendarWorker;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.DecimalWraper;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.FiscalCalendarUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

public class QuarterlyInfoWorker {

	private static Logger logger = Logger.getLogger(QuarterlyInfoWorker.class);

	/**
	 * Returns a collection of QuarterlyInfo objects ready to be displayed in
	 * form
	 * 
	 * @param FilterParams
	 *            object with fields as the filter parameters ampFundingId
	 *            transactionType commitment=0,disbursement=1, expenditure=2
	 *            perspective orgRoleCode 'MA','DN','IA' currencyCode
	 *            'USD','ETB' fiscalCalId 1,2 ....
	 * @return Collection
	 */
	public static Collection<QuarterlyInfo> getQuarterlyInfo(FilterParams fp) {
		List<QuarterlyInfo> arrayList = getQuarterly(fp);
		List<QuarterlyInfo> ethArrList = null;
		List<QuarterlyInfo> filterArrList = null;
		
		AmpFiscalCalendar fiscalCal=FiscalCalendarUtil.getAmpFiscalCalendar(fp.getFiscalCalId());
		
		if (fiscalCal.getBaseCal().equalsIgnoreCase(BaseCalendar.BASE_ETHIOPIAN.getValue())) {
			if (fp.getFromYear() != 0 && fp.getToYear() != 0) {
				filterArrList = filterByYearRange(arrayList, fp.getFromYear(),fp.getToYear(), fp.getFiscalCalId());
				return filterArrList;
			}
		}

		if (fp.getFromYear() != 0 && fp.getToYear() != 0) {
			filterArrList = filterByYearRange(arrayList, fp.getFromYear(), fp.getToYear(), fp.getFiscalCalId());
			return filterArrList;
		}
		return arrayList;
	}

	
	public static ArrayList getQuarterlyForProjections(FilterParams fp, boolean showOnlyProjection) {
		
		Collection<AmpCategoryValue> mtefProjectionTypes	= CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.MTEF_PROJECTION_KEY);
		Iterator<AmpCategoryValue> iter						= mtefProjectionTypes.iterator();
		
		ArrayList amounts									= new ArrayList();
		
		while (iter.hasNext()) {
			AmpCategoryValue mtefProjectionType		= iter.next(); 
			Collection col		= DbUtil.getQuarterlyDataForProjections(fp.getAmpFundingId(), mtefProjectionType.getId().intValue() );
			Iterator colIter	= col.iterator();
			while( colIter.hasNext() ) {
				Object [] row				= (Object [])colIter.next();
				
				AmpCategoryValue type		= (AmpCategoryValue) row[3];
				
				if ( CategoryConstants.MTEF_PROJECTION_PROJECTION.equalsCategoryValue(type) ) {
				
					Double transactionAmount 	= (Double) row[0];
					
					Date transactionDate 		= (Date) row[1];
				
					/*Checking Date Filter*/
					
					// TODO this 01/01 string is hardcoded pattern part and is not good, its throws exception if pattern is dd/MMM/yyyy in global settinsg
					Date startDate=null;
					try {
						startDate = FormatHelper.parseDate("01/01/" + fp.getFromYear()).getTime();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}//DateConversion.getDate( "01/01/" + fp.getFromYear() );
					Date endDate=null;
					try {
						endDate = FormatHelper.parseDate("01/01/" + fp.getToYear()).getTime();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}//DateConversion.getDate( "01/01/" + fp.getToYear() );
					
					if ( startDate!=null&& endDate!=null&&!Util.checkYearFilter(transactionDate, startDate, endDate, fp.getFiscalCalId()) )
							continue;
					/*END - Checking Date Filter*/
					
					AmpCurrency currencyObject	= (AmpCurrency)row[2];
					double fromCurrencyRatio	= Util.getExchange(currencyObject.getCurrencyCode(), new java.sql.Date(transactionDate.getTime()) );
					double toCurrencyRatio	= Util.getExchange(fp.getCurrencyCode(), new java.sql.Date(transactionDate.getTime()) );
					
					Double convertedAmt = CurrencyWorker.convertToDouble(transactionAmount, fromCurrencyRatio,toCurrencyRatio);
					
					amounts.add(convertedAmt);
				}
			}
		}
		return amounts;
	}
	
	public static List<QuarterlyInfo> getQuarterly(FilterParams fp) {

	//	if (logger.isDebugEnabled())
			//logger.debug("GETQUARTERLY() WITH AMPFUNDINGID : "+ fp.getAmpFundingId());
		Collection c = null;
		Collection c1 = null;
		ArrayList<QuarterlyInfo> arrayList = new ArrayList<QuarterlyInfo>();
		ArrayList<QuarterlyInfo> arrayList1 = new ArrayList<QuarterlyInfo>();
		ArrayList arrayList2 = null;
		ArrayList ethArrList = null;
		Iterator iter = null;
		Iterator iter1 = null;
		Iterator iter2 = null;
		String strActualAmt;
		
		double fromCurrency;
		double targetCurrency;
		String selCurrency=fp.getCurrencyCode();
		
		//logger.debug("In get Quarterly frm curr = " + fromCurrency);
		//logger.debug("In get Quarterly target curr = " + targetCurrency);

		c = DbUtil.getQuarterlyData(fp.getAmpFundingId(), fp.getTransactionType(), Constants.PLANNED);
		if (c.size() > 0) {
			iter = c.iterator();
			while (iter.hasNext()) {
				Object[] row = (Object[]) iter.next();
				Double transactionAmount = (Double) row[0];
				Date transactionDate = (Date) row[1];
				// modified by priyajith
				AmpCurrency curr = (AmpCurrency) row[2];
				Double fixedRate=(row[3]!=null && ((Double)row[3]).doubleValue()!=1)? (Double)row[3]:null;
		//		fromCurrency = CurrencyUtil.getExchangeRate(curr.getCurrencyCode());
				//end
				fromCurrency = Util.getExchange(curr.getCurrencyCode(),new java.sql.Date(transactionDate.getTime()));
				targetCurrency = Util.getExchange(fp.getCurrencyCode(),new java.sql.Date(transactionDate.getTime()));
				QuarterlyInfo quarterlyInfo = new QuarterlyInfo();
				double tmpAmt = 0.0;
				if (transactionAmount != null)
					tmpAmt = transactionAmount.doubleValue();
				
				if (fixedRate!=null && fixedRate.doubleValue()!=1
						&& selCurrency !=null 
						&& selCurrency.trim().equals("USD")){
					fromCurrency=fixedRate.doubleValue();
				}
				
				//String strAmt = CurrencyWorker.convert(tmpAmt, fromCurrency, targetCurrency);
				DecimalWraper amountConverted = CurrencyWorker.convertWrapper(tmpAmt, fromCurrency, targetCurrency,
							new java.sql.Date(transactionDate.getTime()));
				quarterlyInfo.setPlannedAmount(FormatHelper.parseDouble(amountConverted.toString()));
				quarterlyInfo.setWrapedPlanned(amountConverted.getCalculations());
				String strDate = DateConversion.ConvertDateToString(transactionDate);
				quarterlyInfo.setDateDisbursed(strDate);
				//FiscalDO fdo = FiscalCalendarWorker.getFiscalYrQtr(transactionDate, fp.getFiscalCalId());
				AmpFiscalCalendar fiscalCalendar=FiscalCalendarUtil.getAmpFiscalCalendar( fp.getFiscalCalId());
				FiscalDO fdo=new FiscalDO();
				try {
				ICalendarWorker worker=fiscalCalendar.getworker();
				worker.setTime(transactionDate);
				fdo.setFiscalYear(worker.getYear());
				fdo.setFiscalQuarter(worker.getQuarter());
				} catch (Exception e) {
					logger.error("Can't get the Year and Quarter ",e);
				}
				
				quarterlyInfo.setFiscalYear(fdo.getFiscalYear());
				quarterlyInfo.setFiscalQuarter(fdo.getFiscalQuarter());
				quarterlyInfo.setAggregate(1);
				quarterlyInfo.setActualAmountSet(false);
				quarterlyInfo.setDisplay(false);
				arrayList.add(quarterlyInfo);
			}
		}

		c1 = DbUtil.getQuarterlyData(fp.getAmpFundingId(),
				fp.getTransactionType(), Constants.ACTUAL);
		
		if (arrayList.size() > 0 || c1.size() > 0) {
			//here it is !!!!!
			arrayList1 = merge(arrayList, c1,selCurrency,fp.getFiscalCalId());
		//AMP-2212
			AmpFiscalCalendar fiscalCal=FiscalCalendarUtil.getAmpFiscalCalendar(fp.getFiscalCalId());
			if (fiscalCal.getBaseCal().equalsIgnoreCase(BaseCalendar.BASE_ETHIOPIAN.getValue()) ) {
				//ethArrList = convertToEth(arrayList1);
				ethArrList = convertToEth(arrayList,fp.getFiscalCalId().longValue());
				arrayList2 = aggregate(ethArrList);
			} else {
				arrayList2 = aggregate(arrayList1);
			}
			iter = arrayList2.iterator();
			while (iter.hasNext()) {
				QuarterlyInfo qi = (QuarterlyInfo) iter.next();
				arrayList1.add(qi);
			}
			//			logger.debug("getQuarterlyInfo() returning array list of size:"
			// +arrayList1.size());
			if (arrayList1.size() != 0) {
				Collections.sort(arrayList1, new QuarterlyInfoComparator());
			}
		}

		return arrayList1;
	}


	public static ArrayList merge(ArrayList arrayList, Collection c1,
			 String selCurrency,Long fiscalId) {

		String baseCurr	= FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.BASE_CURRENCY);
    	if ( baseCurr == null )
    		baseCurr	= "USD";
	//	if (logger.isDebugEnabled())
		//	logger.debug("MERGE()<");
		double fromCurrency;
		Iterator iter1 = c1.iterator();
		boolean b = false;
		while (iter1.hasNext()) {
			b = false;
			Object[] row = (Object[]) iter1.next();
			Double transactionAmount = (Double) row[0];
			Date transactionDate = (Date) row[1];
			// modified by priyajith
			AmpCurrency curr = (AmpCurrency) row[2];
			Double fixedRate=(Double) row[3];
			fromCurrency = Util.getExchange(curr.getCurrencyCode(),new java.sql.Date(transactionDate.getTime()));
			double targetCurrency = Util.getExchange(selCurrency,new java.sql.Date(transactionDate.getTime()));	
			//end			
			double tmpAmt = 0.0;
			if (transactionAmount != null)
				tmpAmt = transactionAmount.doubleValue();
			
			if (fixedRate!=null && fixedRate.doubleValue()!=1 && selCurrency !=null){
				if (curr.getCurrencyCode().compareToIgnoreCase(selCurrency)==0){
					fromCurrency = 1;
					targetCurrency = 1;
				}else{
					fromCurrency=fixedRate.doubleValue();
				}
			}
			
			//String strAmt = CurrencyWorker.convert(tmpAmt, fromCurrency,targetCurrency);
			DecimalWraper amountConverted = CurrencyWorker.convertWrapper(tmpAmt, fromCurrency, targetCurrency,new java.sql.Date(transactionDate.getTime()));			
			
			String strDate = DateConversion
					.ConvertDateToString(transactionDate);
			
			AmpFiscalCalendar fiscalCalendar=FiscalCalendarUtil.getAmpFiscalCalendar(fiscalId);
			FiscalDO fdo=new FiscalDO();
			try {
			ICalendarWorker worker=fiscalCalendar.getworker();
			worker.setTime(transactionDate);
			fdo.setFiscalYear(worker.getYear());
			fdo.setFiscalQuarter(worker.getQuarter());
			} catch (Exception e) {
				logger.error("Can't get the Year and Quarter ",e);
			}
			//FiscalDO fdo = FiscalCalendarWorker.getFiscalYrQtr(transactionDate,fiscalId);
			
			for (int i = 0; i < arrayList.size(); i++) {
				QuarterlyInfo quarterlyInfo = (QuarterlyInfo) arrayList.get(i);
				if (fdo.getFiscalYear() == quarterlyInfo.getFiscalYear()
						&& fdo.getFiscalQuarter() == quarterlyInfo
								.getFiscalQuarter()
						&& !quarterlyInfo.isActualAmountSet()) {
					b = true;
					quarterlyInfo.setActualAmount(FormatHelper.parseDouble(amountConverted.toString()));
					quarterlyInfo.setWrapedActual(amountConverted.getCalculations());
					quarterlyInfo.setDateDisbursed(strDate);
					quarterlyInfo.setActualAmountSet(true);
					break;
				}
			}
			if (!b) {
				QuarterlyInfo qf = new QuarterlyInfo();
				qf.setFiscalYear(fdo.getFiscalYear());
				qf.setFiscalQuarter(fdo.getFiscalQuarter());
				qf.setPlannedAmount(0);
				qf.setActualAmount(FormatHelper.parseDouble(amountConverted.toString()));
				qf.setWrapedActual(amountConverted.getCalculations());
				qf.setDateDisbursed(strDate);
				qf.setAggregate(1);
				qf.setActualAmountSet(true);
				qf.setDisplay(false);
				arrayList.add(qf);
			}
		}
		//if (logger.isDebugEnabled())
			//logger.debug("MERGE() >");
		return arrayList;
	}

	/**
	 * Given a collection of objects with transaction date and transaction
	 * amount for a particular amp funding id ,perspective,transaction type and
	 * adjustment type it will return an aggregated collection of quarterly info
	 * objects.
	 * 
	 * @param c
	 * @return
	 */
	static ArrayList aggregate(ArrayList arrList) {
	//	if (logger.isDebugEnabled())
		//	logger.debug("aggregate() passed a collection of size : "+ arrList.size());
		if (arrList.size() != 0) {
			ArrayList al = new ArrayList();
			Iterator iter = arrList.iterator();
			double tmpActualAmt = 0.0;
			double tmpPlannedAmt = 0.0;
			Date transactionDate = null;
			FiscalDO fdo = null;
			int tmpYr = -1, tmpQtr = -1;
			double tmpAmt = 0.0;
			QuarterlyInfo qi = null;
			QuarterlyInfo qf = null;
			String tmpDate = null;
			String tmpActualWraped="";
			String tmpPlanedWraped="";
			double dblAmt = 0.0;

			if (arrList.size() == 1) {
				qi = (QuarterlyInfo) arrList.get(0);
				qf = new QuarterlyInfo();
				qf.setFiscalYear(qi.getFiscalYear());
				qf.setFiscalQuarter(qi.getFiscalQuarter());
				qf.setPlannedAmount(qi.getPlannedAmount());
				qf.setActualAmount(qi.getActualAmount());
				qf.setDateDisbursed(qi.getDateDisbursed());
				qf.setAggregate(0);
				qf.setPlus(true);
				qf.setDisplay(true);
				qf.setWrapedActual(qi.getWrapedActual());
				qf.setWrapedPlanned(qi.getWrapedPlanned());
				al.add(qf);
			} else {
				int index = 0;
				qi = (QuarterlyInfo) arrList.get(index);
				tmpYr = qi.getFiscalYear();
				tmpQtr = qi.getFiscalQuarter();
				tmpActualAmt = qi.getActualAmount();
				tmpActualWraped = qi.getWrapedActual();
				tmpPlanedWraped = qi.getWrapedPlanned();
				tmpPlannedAmt = qi.getPlannedAmount();
				tmpDate = qi.getDateDisbursed();

				while (index < arrList.size()) {
					index++;
					if (index < arrList.size()) {
						qi = (QuarterlyInfo) arrList.get(index);
					}
					while (index < arrList.size()
							&& (qi.getFiscalYear() == tmpYr && qi
									.getFiscalQuarter() == tmpQtr)) {
						tmpActualAmt += qi.getActualAmount();
						tmpActualWraped = tmpActualWraped + " + " + qi.getWrapedActual(); 
						tmpPlannedAmt += qi.getPlannedAmount();
						tmpPlanedWraped = tmpPlanedWraped +" + "+ qi.getWrapedPlanned(); 
						tmpDate = qi.getDateDisbursed();
						index++;
						if (index < arrList.size()) {
							qi = (QuarterlyInfo) arrList.get(index);
						}
					}
					qf = new QuarterlyInfo();
					qf.setFiscalYear(tmpYr);
					qf.setFiscalQuarter(tmpQtr);
					qf.setPlannedAmount(tmpPlannedAmt);
					qf.setWrapedPlanned(tmpPlanedWraped);
					qf.setActualAmount(tmpActualAmt);
					qf.setWrapedActual(tmpActualWraped);
					qf.setDateDisbursed(tmpDate);
					qf.setAggregate(0);
					qf.setPlus(true);
					qf.setDisplay(true);
					al.add(qf);

					tmpYr = qi.getFiscalYear();
					tmpQtr = qi.getFiscalQuarter();
					tmpActualAmt = qi.getActualAmount();
					tmpPlannedAmt = qi.getPlannedAmount();
					tmpActualWraped = qi.getWrapedActual();
					tmpPlanedWraped = qi.getWrapedPlanned();
					tmpDate = qi.getDateDisbursed();
				}
			}
		//	if (logger.isDebugEnabled())
		//		logger.debug("aggregate() returning a collection of size "	+ al.size());
			return al;
		} else
			return arrList;

	}

	/**
	 * This method given a collection of QuarterlyInfo objects will return a new
	 * collection with date disbursed(gregorian) converted to an Ethiopian date
	 * and fiscal year and qtr converted to Ethiopian fiscal year & quarter
	 * 
	 * @param al
	 * @return Collection
	 */
	public static ArrayList convertToEth(ArrayList arrList,long fiscalCalId) {

		//logger.debug("#inside convertToEth");
	//	if (logger.isDebugEnabled())
		//	logger.debug("CONVERTTOETH() PASSED AN ARRAYLIST OF SIZE : "+ arrList.size());

		EthiopianCalendar ec = new EthiopianCalendar();
		EthiopianCalendar ec1 = null;
		GregorianCalendar gc = new GregorianCalendar();
		for (int i = 0; i < arrList.size(); i++) {
			QuarterlyInfo qf = (QuarterlyInfo) arrList.get(i);
			String strTrsDate = qf.getDateDisbursed();
			//logger.debug("#$ strTrsDate dateDisbursed = " + strTrsDate);
			if (strTrsDate != null && strTrsDate.length() != 0) {

				//logger.debug("Gregorian Date  string " + strTrsDate);
				Date trsDate = DateConversion.getDate(strTrsDate);
				//	logger.debug("date parsed out "+trsDate);
				AmpFiscalCalendar fiscalCalendar=FiscalCalendarUtil.getAmpFiscalCalendar(fiscalCalId);
				FiscalDO fdo=new FiscalDO();
				try {
				ICalendarWorker worker=fiscalCalendar.getworker();
				worker.setTime(trsDate);
				fdo.setFiscalYear(worker.getYear());
				fdo.setFiscalQuarter(worker.getQuarter());
				} catch (Exception e) {
					logger.error("Can't get the Year and Quarter ",e);
				}
				
				qf.setFiscalYear(fdo.getFiscalYear());
				qf.setFiscalQuarter(fdo.getFiscalQuarter());
				gc.setTime(trsDate);
				
				ec1 = ec.getEthiopianDate(gc);
				String ethDate = ec1.ethDay + "/" + ec1.ethMonth + "/"+ ec1.ethYear;
				qf.setDateDisbursed(ethDate);
				
			}
		}
		//if (logger.isDebugEnabled())
			//logger.debug("CONVERTTOETH() RETURNING AN ARRAYLIST OF SIZE : "+ arrList.size());
		return arrList;
	}

	/**
	 * This method given a collection of QuarterlyInfo objects will will return
	 * a new collection of QuarterlyInfo objects where date disbursed lies in
	 * the given year range
	 * 
	 * @param arrList
	 * @param fromYear
	 * @param toYear
	 * @param fiscalCalId
	 *            if fiscalCalId == -1 filter by fiscal year field else filter
	 *            by calendar year(transactionDate)
	 * @return
	 */
	public static List filterByYearRange(List arrList, int fromYear,
			int toYear, Long fiscalCalId) {

		ArrayList a = new ArrayList();

		int yr = 0;
		GregorianCalendar gc = null;
		for (int i = 0; i < arrList.size(); i++) {

			QuarterlyInfo qf = (QuarterlyInfo) arrList.get(i);
			if (qf.getAggregate() == 1) {
			    
				AmpFiscalCalendar fiscalCal=FiscalCalendarUtil.getAmpFiscalCalendar(fiscalCalId);
					// Filter by calendar type greg or eth
					String ds = qf.getDateDisbursed();
					if (ds != null) {
						//if (fiscalCalId.longValue() == Constants.ETH_CAL.longValue()) 
//					    	if (fiscalCal.getBaseCal().equalsIgnoreCase(BaseCalendar.BASE_ETHIOPIAN.getValue())){
//							yr = DateConversion.getYear(ds);
//						} else {
//							yr = FiscalCalendarUtil.getYear(fiscalCalId,ds);
//						}
						 ICalendarWorker worker= fiscalCal.getworker();
                         try {
							worker.setTime(FormatHelper.parseDate(ds).getTime());
							yr =worker.getYear();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				
				if (yr >= fromYear && yr <= toYear) {
					a.add(qf);
				} else if (yr == 0) { // if transaction date is null
					a.add(qf);
				}
			}

		}

		ArrayList arr = aggregate(a);
		Iterator iter = arr.iterator();
		while (iter.hasNext()) {
			QuarterlyInfo qi = (QuarterlyInfo) iter.next();
			a.add(qi);
		}

		/*
		 * for ( int i = 0 ; i < arrList.size() ; i++ ) { QuarterlyInfo qf =
		 * (QuarterlyInfo)arrList.get(i); for ( int j = 0 ; j < a.size() ; j++ ) {
		 * QuarterlyInfo qi = (QuarterlyInfo)a.get(j); if ( qf.getAggregate() ==
		 * 0 ) { if ( qf.getFiscalYear()==qi.getFiscalYear() &&
		 * qf.getFiscalQuarter()==qi.getFiscalQuarter()) { arr.add(qf); break; } } } }
		 */
		if (a.size() != 0) {
			Collections.sort(a, new QuarterlyInfoComparator());
		}
		return a;
	}
	
	/**
	 * This method returns an TotalsQuarterly object containing totals required
	 * for the quarterly page on disbursements and expenditures
	 * 
	 * @param ampFundingId
	 * @param perspective
	 * @return TotalsQuarterly
	 * 
	 */
	public static TotalsQuarterly getTotalsQuarterly(Long ampFundingId,
		String currCode, boolean isDebug) {
		// if (logger.isDebugEnabled())
		// logger.debug("getTotalsQuarterly() with ampFundingId : "
		// + ampFundingId + ", perspective : " + perspective);
		TotalsQuarterly tq = new TotalsQuarterly();
		Integer adjType = new Integer(Constants.ACTUAL);
		// Total actual commitment
		
		DecimalWraper totCommitment = DbUtil.getTotalDonorFunding(ampFundingId,
				new Integer(Constants.COMMITMENT), adjType,currCode);
		String strTotCommitment = "";
		if (!isDebug) {
			strTotCommitment =totCommitment.toString();
		} 
		else {
			strTotCommitment = totCommitment.getCalculations();
			tq.setTotalCommitted(strTotCommitment);
		}
		String strTotDisbursement = "";
		DecimalWraper totDisbursement = DbUtil.getTotalDonorFunding(ampFundingId,
				new Integer(Constants.DISBURSEMENT), adjType,currCode);
		if (!isDebug) {
			strTotDisbursement = totDisbursement.toString();
		} else {
			strTotDisbursement = totDisbursement.getCalculations();
		}
		tq.setTotalDisbursed(strTotDisbursement);
		
		DecimalWraper totExpended = DbUtil.getTotalDonorFunding(ampFundingId,
				new Integer(Constants.EXPENDITURE), adjType,currCode);
		if (!isDebug) {
			tq.setTotalExpended(totExpended.toString());
		} else {
			tq.setTotalExpended(totExpended.getCalculations());
		}
		
		DecimalWraper totUnExpended = new DecimalWraper(); 
		totUnExpended.setValue(totDisbursement.getValue().subtract(totExpended.getValue()));
		totUnExpended.setCalculations(totDisbursement.getCalculations() + " - " + totExpended.getCalculations());
		String strTotUnexpended = "";
		if (!isDebug) {
			strTotUnexpended = totUnExpended.toString();
		} else {
			strTotUnexpended = totUnExpended.getCalculations();
		}
		tq.setTotalUnExpended(strTotUnexpended);
		DecimalWraper totRemaining = new DecimalWraper(); 
			totRemaining.setValue(totCommitment.getValue().subtract(totDisbursement.getValue()));
			totRemaining.setCalculations(totCommitment.getCalculations()+ " - " + totDisbursement.getCalculations());
		String strTotRemaining = "";
		if (!isDebug) {
			strTotRemaining =  totRemaining.toString();
		} else {
			strTotRemaining = totRemaining.getCalculations();
		}
		tq.setTotalRemaining(strTotRemaining);

		tq.setCurrencyCode(currCode);
		if (logger.isDebugEnabled())
			logger
					.debug("getTotalsQuarterly() returning a TotalsQuarterly object with "
							+ " Total commitment : " + tq.getTotalCommitted());
		return tq;
	}

}

