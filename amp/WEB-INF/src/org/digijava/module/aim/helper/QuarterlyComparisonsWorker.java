package org.digijava.module.aim.helper ;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import org.apache.log4j.Logger;

public class QuarterlyComparisonsWorker	{

	private static Logger logger = Logger.getLogger(QuarterlyComparisonsWorker.class);
	private static ArrayList arrList;
	private static Iterator iter;

	public static Collection getQuarterlyComparisons(FilterParams fp)	{
		if ( logger.isDebugEnabled() )
			logger.debug("getQuarterlyComparisons() passed filter parameters " +
						" ampFundingId=" +	fp.getAmpFundingId());

		fp.setTransactionType(Constants.COMMITMENT);
		Collection commitments = QuarterlyInfoWorker.getQuarterlyInfo(fp);
                fp.setTransactionType(Constants.DISBURSEMENT_ORDER);
                Collection disbursementOrders = QuarterlyInfoWorker.getQuarterlyInfo(fp);
                fp.setTransactionType(Constants.DISBURSEMENT);
		Collection disbursements = QuarterlyInfoWorker.getQuarterlyInfo(fp);
		fp.setTransactionType(Constants.EXPENDITURE);
		Collection expenditure = QuarterlyInfoWorker.getQuarterlyInfo(fp);
		iter = commitments.iterator();
		arrList = new ArrayList();
		while ( iter.hasNext() )	{
			QuarterlyComparison qc = new QuarterlyComparison();
			QuarterlyInfo qi = (QuarterlyInfo) iter.next();
			fillAdd(qc,qi,Constants.COMMITMENT);
		}
                iter=disbursementOrders.iterator();
                merge(Constants.DISBURSEMENT_ORDER);
		iter = disbursements.iterator();
		merge(Constants.DISBURSEMENT);
		iter = expenditure.iterator();
		merge(Constants.EXPENDITURE);
		Collections.sort(arrList,new QuarterlyInfoComparator());

		if (logger.isDebugEnabled() )
			logger.debug("getQuarterlyComparisons() returning a collection of size " +
						(arrList!=null?arrList.size():0));
		return arrList;
	}

	public static void merge(int transactionType)	{
		if (logger.isDebugEnabled() )
			logger.debug("merge() < transactionType="+transactionType);
		boolean b = false ;

		while ( iter.hasNext() )	{
			b = false;
			QuarterlyInfo qi = (QuarterlyInfo) iter.next();
			if ( qi.getAggregate()==0)	{
				int fy = qi.getFiscalYear();
				int fq = qi.getFiscalQuarter();

				for ( int i = 0 ; i < arrList.size() ; i++ )	{
					QuarterlyComparison qc = (QuarterlyComparison) arrList.get(i);
					if ( fy == qc.getFiscalYear() && fq == qc.getFiscalQuarter() )	{
						double pm = qi.getPlannedAmount();
						double am = qi.getActualAmount();

                        if ( transactionType == Constants.DISBURSEMENT_ORDER)	{
                                if ( qc.getActualDisbOrder() == 0.0) {
                                        qc.setActualDisbOrder(am);
                                        b = true;
                                }
                        }



						if ( transactionType == Constants.DISBURSEMENT)	{
							if ( qc.getPlannedDisbursement() == 0.0
								&& qc.getActualDisbursement()== 0.0) {
								qc.setPlannedDisbursement(pm);
								qc.setActualDisbursement(am);
								b = true;
							}
						}

						if ( transactionType == Constants.EXPENDITURE)	{
							if ( qc.getPlannedExpenditure() == 0.0
								&& qc.getActualExpenditure() == 0.0) {
								qc.setPlannedExpenditure(pm);
								qc.setActualExpenditure(am);
								b = true;
							}

						}
						if ( b )
							break;
					}
				}
				if ( !b )	{
					QuarterlyComparison qc = new QuarterlyComparison();
					fillAdd(qc,qi,transactionType);
				}
			}

		}
		if (logger.isDebugEnabled() )
			logger.debug("merge() > transactionType="+transactionType);
	}

	public static void fillAdd(QuarterlyComparison qc,
							   QuarterlyInfo qi,
							   int transactionType)	{
		if ( qi.getAggregate()==0)	{
			qc.setFiscalYear(qi.getFiscalYear());
			qc.setFiscalQuarter(qi.getFiscalQuarter());
			if ( transactionType == Constants.COMMITMENT ) {
				qc.setPlannedCommitment(qi.getPlannedAmount());
				qc.setActualCommitment(qi.getActualAmount());
			}
			if ( transactionType == Constants.DISBURSEMENT)	{
				qc.setPlannedDisbursement(qi.getPlannedAmount());
				qc.setActualDisbursement(qi.getActualAmount());
			}
                        if ( transactionType == Constants.DISBURSEMENT_ORDER)	{
				qc.setActualDisbOrder(qi.getActualAmount());
			}
			if ( transactionType == Constants.EXPENDITURE)	{
				qc.setPlannedExpenditure(qi.getPlannedAmount());
				qc.setActualExpenditure(qi.getActualAmount());
			}
			arrList.add(qc);
		}
	}
}
