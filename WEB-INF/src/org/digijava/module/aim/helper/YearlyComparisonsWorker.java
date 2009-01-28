package org.digijava.module.aim.helper ;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;

public class YearlyComparisonsWorker	{
	private static Logger logger = Logger.getLogger(YearlyComparisonsWorker.class);

	public static Collection getYearlyComparisons(FilterParams fp)	{
		if ( logger.isDebugEnabled() )
			logger.debug("GETYEARLYCOMPARISONS() WITH AMPFUNDINGID : " + fp.getAmpFundingId() );
		ArrayList arrList = new ArrayList();


		fp.setTransactionType(Constants.COMMITMENT);
 		Collection c = YearlyInfoWorker.getYearlyInfo(fp);

		Iterator iter = c.iterator();
		while( iter.hasNext() )	{
			YearlyInfo yearlyInfo = (YearlyInfo)iter.next();
			YearlyComparison yearlyComparison = new YearlyComparison();
			yearlyComparison.setFiscalYear(yearlyInfo.getFiscalYear());
			yearlyComparison.setPlannedCommitment(yearlyInfo.getPlannedAmount());
			yearlyComparison.setActualCommitment(yearlyInfo.getActualAmount());

			arrList.add(yearlyComparison);
		}
              fp.setTransactionType(Constants.DISBURSEMENT_ORDER);
              Collection disbOrderList = YearlyInfoWorker.getYearlyInfo(fp);

              Iterator iterDisbOrder = disbOrderList.iterator();
              while( iterDisbOrder.hasNext() )	{
                      boolean flag=false;
                      YearlyInfo yearlyInfo = (YearlyInfo)iterDisbOrder.next();
                      for ( int i = 0 ; i < arrList.size(); i++)	{
                                YearlyComparison yearlyComparison = (YearlyComparison)arrList.get(i);
                                if ( yearlyInfo.getFiscalYear() == yearlyComparison.getFiscalYear() )	{
                                       flag = true;

                                        yearlyComparison.setDisbOrders(yearlyInfo.getActualAmount());
                                        break;
                                }
                        }
                        if ( !flag  )	{
                                YearlyComparison yearlyComparison = new YearlyComparison();
                                yearlyComparison.setFiscalYear(yearlyInfo.getFiscalYear());
                                yearlyComparison.setDisbOrders(yearlyInfo.getActualAmount());
                                arrList.add(yearlyComparison);
                        }
              }


		fp.setTransactionType(Constants.DISBURSEMENT);
		c = YearlyInfoWorker.getYearlyInfo(fp);
		iter = c.iterator();

		while ( iter.hasNext() )	{
			boolean b = false;
			YearlyInfo yearlyInfo = (YearlyInfo)iter.next();
			for ( int i = 0 ; i < arrList.size(); i++)	{
				YearlyComparison yearlyComparison = (YearlyComparison)arrList.get(i);
				if ( yearlyInfo.getFiscalYear() == yearlyComparison.getFiscalYear() )	{
					b = true;
					yearlyComparison.setPlannedDisbursement(yearlyInfo.getPlannedAmount());
					yearlyComparison.setActualDisbursement(yearlyInfo.getActualAmount());
					break;
				}
			}
			if ( !b )	{
				YearlyComparison yearlyComparison = new YearlyComparison();
				yearlyComparison.setFiscalYear(yearlyInfo.getFiscalYear());
				yearlyComparison.setPlannedDisbursement(yearlyInfo.getPlannedAmount());
				yearlyComparison.setActualDisbursement(yearlyInfo.getActualAmount());
				arrList.add(yearlyComparison);
			}
		}

		fp.setTransactionType(Constants.EXPENDITURE);
		c = YearlyInfoWorker.getYearlyInfo(fp);
		iter = c.iterator();
		while ( iter.hasNext() )	{
			boolean b = false;
			YearlyInfo yearlyInfo = (YearlyInfo)iter.next();
			for ( int i = 0 ; i < arrList.size() ; i++)	{
				YearlyComparison yearlyComparison = (YearlyComparison)arrList.get(i);
				if ( yearlyInfo.getFiscalYear() == yearlyComparison.getFiscalYear() )	{
					b = true;
					yearlyComparison.setPlannedExpenditure(yearlyInfo.getPlannedAmount());
					yearlyComparison.setActualExpenditure(yearlyInfo.getActualAmount());
					break;
				}
			}
			if ( !b )	{
				YearlyComparison yearlyComparison = new YearlyComparison();
				yearlyComparison.setFiscalYear(yearlyInfo.getFiscalYear());
				yearlyComparison.setPlannedExpenditure(yearlyInfo.getPlannedAmount());
				yearlyComparison.setActualExpenditure(yearlyInfo.getActualAmount());
				arrList.add(yearlyComparison);
			}
		}
		if ( logger.isDebugEnabled() )
			logger.debug("getYearlyComparisons() returning a collection of size"
							+ arrList.size()) ;
		return arrList ;
	}

	public static AllTotals getAllTotals(Collection c)	{
		if ( logger.isDebugEnabled() )
			logger.debug("GETALLTOTALS()<");
		Iterator iter = c.iterator();
		AllTotals allTotals = new AllTotals();
		double totalActualCommitment = 0.0;
		double totalPlannedDisbursement = 0.0;
	 	double totalActualDisbursement = 0.0;
 		double totalActualExpenditure = 0.0;
                double totalDisbOrder=0.0;

		while ( iter.hasNext() )	{
			YearlyComparison yearlyComparison = (YearlyComparison)iter.next();
			totalActualCommitment += yearlyComparison.getActualCommitment();
			totalPlannedDisbursement +=yearlyComparison.getPlannedDisbursement();
			totalActualDisbursement +=yearlyComparison.getActualDisbursement();
			totalActualExpenditure += yearlyComparison.getActualExpenditure();
                        totalDisbOrder+=yearlyComparison.getDisbOrders();

		}
		allTotals.setTotalActualCommitment(totalActualCommitment);
		allTotals.setTotalPlannedDisbursement(totalPlannedDisbursement);
		allTotals.setTotalActualDisbursement(totalActualDisbursement);
		allTotals.setTotalActualExpenditure(totalActualExpenditure);
                allTotals.setTotalDisbOrder(totalDisbOrder);
		if ( logger.isDebugEnabled() )
					logger.debug("GETALLTOTALS()>");
		return allTotals;
	}
}

