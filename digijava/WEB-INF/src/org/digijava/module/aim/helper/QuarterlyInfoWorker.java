package org.digijava.module.aim.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FiscalCalendarUtil;

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
	public static Collection getQuarterlyInfo(FilterParams fp) {
		ArrayList arrayList = getQuarterly(fp);
		ArrayList ethArrList = null;
		ArrayList filterArrList = null;
		
		if (fp.getFiscalCalId().longValue() == Constants.ETH_CAL.longValue()
				|| fp.getFiscalCalId().longValue() == Constants.ETH_FY.longValue()) {
			if (fp.getFromYear() != 0 && fp.getToYear() != 0) {
				filterArrList = filterByYearRange(arrayList, fp.getFromYear(),
						fp.getToYear(), fp.getFiscalCalId());
				return filterArrList;
			}
		}

		if (fp.getFromYear() != 0 && fp.getToYear() != 0) {
			filterArrList = filterByYearRange(arrayList, fp.getFromYear(), fp
					.getToYear(), fp.getFiscalCalId());
			return filterArrList;
		}
		return arrayList;
	}

	public static ArrayList getQuarterly(FilterParams fp) {

	//	if (logger.isDebugEnabled())
			//logger.debug("GETQUARTERLY() WITH AMPFUNDINGID : "+ fp.getAmpFundingId());
		Collection c = null;
		Collection c1 = null;
		ArrayList arrayList = new ArrayList();
		ArrayList arrayList1 = new ArrayList();
		ArrayList arrayList2 = null;
		ArrayList ethArrList = null;
		Iterator iter = null;
		Iterator iter1 = null;
		Iterator iter2 = null;
		String strActualAmt;

		double fromCurrency = CurrencyUtil.getExchangeRate(fp.getAmpFundingId(), fp
				.getPerspective());
		double targetCurrency = 1.0;
		String selCurrency=fp.getCurrencyCode();
		
		//logger.debug("In get Quarterly frm curr = " + fromCurrency);
		//logger.debug("In get Quarterly target curr = " + targetCurrency);

		c = DbUtil.getQuarterlyData(fp.getAmpFundingId(), fp.getPerspective(),
				fp.getTransactionType(), Constants.PLANNED);
		if (c.size() > 0) {
			iter = c.iterator();
			while (iter.hasNext()) {
				Object[] row = (Object[]) iter.next();
				Double transactionAmount = (Double) row[0];
				Date transactionDate = (Date) row[1];
				// modified by priyajith
				AmpCurrency curr = (AmpCurrency) row[2];
		//		fromCurrency = CurrencyUtil.getExchangeRate(curr.getCurrencyCode());
				//end
				fromCurrency = CurrencyUtil.getExchangeRate(curr.getCurrencyCode(),Constants.PLANNED,transactionDate);
				targetCurrency = CurrencyUtil.getExchangeRate(fp.getCurrencyCode(),Constants.PLANNED,transactionDate);
				QuarterlyInfo quarterlyInfo = new QuarterlyInfo();
				double tmpAmt = 0.0;
				if (transactionAmount != null)
					tmpAmt = transactionAmount.doubleValue();
				String strAmt = CurrencyWorker.convert(tmpAmt, fromCurrency,
						targetCurrency);
				quarterlyInfo.setPlannedAmount(strAmt);
				String strDate = DateConversion.ConvertDateToString(transactionDate);
				quarterlyInfo.setDateDisbursed(strDate);
				FiscalDO fdo = FiscalCalendarWorker.getFiscalYrQtr(
						transactionDate, fp.getFiscalCalId());
				quarterlyInfo.setFiscalYear(fdo.getFiscalYear());
				quarterlyInfo.setFiscalQuarter(fdo.getFiscalQuarter());
				quarterlyInfo.setAggregate(1);
				quarterlyInfo.setActualAmountSet(false);
				quarterlyInfo.setDisplay(false);
				arrayList.add(quarterlyInfo);
			}
		}

		c1 = DbUtil.getQuarterlyData(fp.getAmpFundingId(), fp.getPerspective(),
				fp.getTransactionType(), Constants.ACTUAL);
		
		if (arrayList.size() > 0 || c1.size() > 0) {
			arrayList1 = merge(arrayList, c1, fromCurrency, selCurrency,fp.getFiscalCalId());
			if (fp.getFiscalCalId().longValue() == Constants.ETH_CAL.longValue()
					|| fp.getFiscalCalId().longValue() == Constants.ETH_FY.longValue()) {
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
			double fromCurrency, String selCurrency,Long fiscalId) {

	//	if (logger.isDebugEnabled())
		//	logger.debug("MERGE()<");

		Iterator iter1 = c1.iterator();
		boolean b = false;
		while (iter1.hasNext()) {
			b = false;
			Object[] row = (Object[]) iter1.next();
			Double transactionAmount = (Double) row[0];
			Date transactionDate = (Date) row[1];
			// modified by priyajith
			AmpCurrency curr = (AmpCurrency) row[2];
			fromCurrency = CurrencyUtil.getExchangeRate(curr.getCurrencyCode(),Constants.ACTUAL,transactionDate);
			double targetCurrency = CurrencyUtil.getExchangeRate(selCurrency,Constants.ACTUAL,transactionDate);	
			//end			
			double tmpAmt = 0.0;
			if (transactionAmount != null)
				tmpAmt = transactionAmount.doubleValue();
			String strAmt = CurrencyWorker.convert(tmpAmt, fromCurrency,
					targetCurrency);
			String strDate = DateConversion
					.ConvertDateToString(transactionDate);
			FiscalDO fdo = FiscalCalendarWorker.getFiscalYrQtr(transactionDate,fiscalId);

			for (int i = 0; i < arrayList.size(); i++) {
				QuarterlyInfo quarterlyInfo = (QuarterlyInfo) arrayList.get(i);
				if (fdo.getFiscalYear() == quarterlyInfo.getFiscalYear()
						&& fdo.getFiscalQuarter() == quarterlyInfo
								.getFiscalQuarter()
						&& !quarterlyInfo.isActualAmountSet()) {
					b = true;
					quarterlyInfo.setActualAmount(strAmt);
					quarterlyInfo.setDateDisbursed(strDate);
					quarterlyInfo.setActualAmountSet(true);
					break;
				}
			}
			if (!b) {
				QuarterlyInfo qf = new QuarterlyInfo();
				qf.setFiscalYear(fdo.getFiscalYear());
				qf.setFiscalQuarter(fdo.getFiscalQuarter());
				qf.setPlannedAmount("0");
				qf.setActualAmount(strAmt);
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
			String strActualAmt = null;
			String strPlannedAmt = null;
			String tmpDate = null;
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
				al.add(qf);
			} else {
				int index = 0;
				qi = (QuarterlyInfo) arrList.get(index);
				tmpYr = qi.getFiscalYear();
				tmpQtr = qi.getFiscalQuarter();
				tmpActualAmt = DecimalToText.getDouble(qi.getActualAmount());
				tmpPlannedAmt = DecimalToText.getDouble(qi.getPlannedAmount());
				tmpDate = qi.getDateDisbursed();

				while (index < arrList.size()) {
					index++;
					if (index < arrList.size()) {
						qi = (QuarterlyInfo) arrList.get(index);
					}
					while (index < arrList.size()
							&& (qi.getFiscalYear() == tmpYr && qi
									.getFiscalQuarter() == tmpQtr)) {
						tmpActualAmt += DecimalToText.getDouble(qi
								.getActualAmount());
						tmpPlannedAmt += DecimalToText.getDouble(qi
								.getPlannedAmount());
						tmpDate = qi.getDateDisbursed();
						index++;
						if (index < arrList.size()) {
							qi = (QuarterlyInfo) arrList.get(index);
						}
					}
					strActualAmt = DecimalToText
							.ConvertDecimalToText(tmpActualAmt);
					strPlannedAmt = DecimalToText
							.ConvertDecimalToText(tmpPlannedAmt);
					qf = new QuarterlyInfo();
					qf.setFiscalYear(tmpYr);
					qf.setFiscalQuarter(tmpQtr);
					qf.setPlannedAmount(strPlannedAmt);
					qf.setActualAmount(strActualAmt);
					qf.setDateDisbursed(tmpDate);
					qf.setAggregate(0);
					qf.setPlus(true);
					qf.setDisplay(true);
					al.add(qf);

					tmpYr = qi.getFiscalYear();
					tmpQtr = qi.getFiscalQuarter();
					tmpActualAmt = DecimalToText
							.getDouble(qi.getActualAmount());
					tmpPlannedAmt = DecimalToText.getDouble(qi
							.getPlannedAmount());
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
				FiscalDO fdo = null;
				if (fiscalCalId == Constants.ETH_CAL.longValue()) {
					fdo = FiscalCalendarWorker.getFiscalYrQtr(trsDate,
							Constants.ETH_CAL);					
				} else {
					fdo = FiscalCalendarWorker.getFiscalYrQtr(trsDate,
							Constants.ETH_FY);
				}
				qf.setFiscalYear(fdo.getFiscalYear());
				qf.setFiscalQuarter(fdo.getFiscalQuarter());
				//		logger.debug("Ethiopian fiscal year"+fdo.getFiscalYear()+
				//					 "Ethiopian fiscal quarter "+fdo.fiscalQuarter) ;
				gc.setTime(trsDate);
				//		logger.debug("Greg date passed to getEthiopianDate"+gc) ;

				ec1 = ec.getEthiopianDate(gc);

				//				logger.debug("Ethiopian date returned day "+ec1.ethDay
				//							+ " month " + ec1.ethMonth
				//							+ " year " + ec1.ethYear );
				String ethDate = ec1.ethDay + "/" + ec1.ethMonth + "/"
						+ ec1.ethYear;
				//		logger.debug("Ethiopian date set " + ethDate);
				qf.setDateDisbursed(ethDate);
				//logger.debug("Ethiopian Date :" + ethDate);
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
	public static ArrayList filterByYearRange(ArrayList arrList, int fromYear,
			int toYear, Long fiscalCalId) {

		ArrayList a = new ArrayList();

		int yr = 0;
		GregorianCalendar gc = null;
		for (int i = 0; i < arrList.size(); i++) {

			QuarterlyInfo qf = (QuarterlyInfo) arrList.get(i);
			if (qf.getAggregate() == 1) {
				if (fiscalCalId.longValue() == Constants.ETH_FY.longValue()) {
					yr = qf.getFiscalYear();
				} else { // Filter by calendar type greg or eth
					String ds = qf.getDateDisbursed();
					if (ds != null) {
						if (fiscalCalId.longValue() == Constants.ETH_CAL.longValue()) {
							yr = DateConversion.getYear(ds);
						} else {
							yr = FiscalCalendarUtil.getYear(fiscalCalId,ds);
						}
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
	 */
	public static TotalsQuarterly getTotalsQuarterly(Long ampFundingId,
			String perspective,String currCode) {
	//	if (logger.isDebugEnabled())
		//	logger.debug("getTotalsQuarterly() with ampFundingId : "
		//			+ ampFundingId + ", perspective : " + perspective);
		TotalsQuarterly tq = new TotalsQuarterly();
		Integer adjType = new Integer(Constants.ACTUAL);
		//Total actual commitment
		double totCommitment = DbUtil.getTotalDonorFund(ampFundingId,
				new Integer(Constants.COMMITMENT), adjType, perspective);
		double fromCurrency = CurrencyUtil.getExchangeRate(ampFundingId, perspective);
		double targetCurrency = CurrencyUtil
				.getExchangeRate(currCode);
		String strTotCommitment = CurrencyWorker.convert(totCommitment,
				fromCurrency, targetCurrency);
		tq.setTotalCommitted(strTotCommitment);
		double totDisbursement = DbUtil.getTotalDonorFund(ampFundingId,
				new Integer(Constants.DISBURSEMENT), adjType, perspective);
		String strTotDisbursement = CurrencyWorker.convert(totDisbursement,
				fromCurrency, targetCurrency);
		tq.setTotalDisbursed(strTotDisbursement);
		double totExpended = DbUtil.getTotalDonorFund(ampFundingId,
				new Integer(Constants.EXPENDITURE), adjType, perspective);
		double totUnExpended = totDisbursement - totExpended;
		String strTotUnexpended = CurrencyWorker.convert(totUnExpended,
				fromCurrency, targetCurrency);
		tq.setTotalUnExpended(strTotUnexpended);
		double totRemaining = totCommitment - totDisbursement;
		String strTotRemaining = CurrencyWorker.convert(totRemaining,
				fromCurrency, targetCurrency);
		tq.setTotalRemaining(strTotRemaining);
		tq.setCurrencyCode(currCode);
		if (logger.isDebugEnabled())
			logger
					.debug("getTotalsQuarterly() returning a TotalsQuarterly object with "
							+ " Total commitment : " + tq.getTotalCommitted());
		return tq;
	}

}

