package org.digijava.module.aim.helper;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.Util;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author 
 */
public class MonthlyInfoWorker {

    private static Logger logger = Logger.getLogger(MonthlyInfoWorker.class);

    public static List getMonthYears(FilterParams fp, boolean useType) throws DgException {
        Session session = null;
        Query q = null;
        int trsType = -1;
        Long ampFundingId = fp.getAmpFundingId();
        int fromYear = fp.getFromYear();
        int toYear = fp.getToYear();
        List monthYears = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select month(f.transactionDate), year(f.transactionDate) from " + AmpFundingDetail.class.getName() +
                    " f where (f.ampFundingId=:ampFundingId) " + "  and " +
                    " year(f.transactionDate)>=:fromYear and year(f.transactionDate)<=:toYear ";
            if (useType) {
                trsType = fp.getTransactionType();
                queryString += " and (f.transactionType=:trsType) ";
            }
            queryString += " group by month(f.transactionDate), year(f.transactionDate)" + " order by  month(f.transactionDate) ";

            q = session.createQuery(queryString);
            if (useType) {
                q.setInteger("trsType", trsType);
            }
            q.setLong("ampFundingId", ampFundingId);
            q.setInteger("fromYear", fromYear);
            q.setInteger("toYear", toYear);
            monthYears = q.list();
        } catch (Exception ex) {
            logger.error("Unable to get monthly data from database", ex);
            throw new DgException(ex);


        }
        return monthYears;
    }

    /**
     * Returns a collection of records
     * commitment, disbursement, disbursement orders, expenditure group by month and year
     * based on below  
     * @param fp
     * @return List
     */
    public static List<MonthlyComparison> getMonthlyComparisons(FilterParams fp) throws DgException {


        List<MonthlyComparison> monthlyData = new ArrayList<MonthlyComparison>();
        Long ampFundingId = fp.getAmpFundingId();
        String currCode = fp.getCurrencyCode();


        /* if (transactionType == Constants.MTEFPROJECTION ) {
        return getQuarterlyDataForProjections(ampFundingId, adjustmentType);
        }
         */


        try {
            List months = getMonthYears(fp, false);
            if (months != null && months.size() > 0) {
                Iterator iter = months.iterator();
                while (iter.hasNext()) {
                    Object[] dates = (Object[]) iter.next();
                    Integer month = (Integer) dates[0];
                    Integer year = (Integer) dates[1];

                    MonthlyComparison comparison = new MonthlyComparison();
                    DateFormatSymbols dfs = new DateFormatSymbols();
                    comparison.setFiscalYear(year);
                    comparison.setMonth(dfs.getMonths()[month - 1]);
                    comparison.setMonthNumber(month);

                    List monthPlannedComm = getMonthlyData(ampFundingId, Constants.COMMITMENT, Constants.PLANNED, month, year);
                    List monthActualComm = getMonthlyData(ampFundingId, Constants.COMMITMENT, Constants.ACTUAL, month, year);
                    comparison.setActualCommitment(getSumAmounts(monthActualComm, currCode));
                    comparison.setPlannedCommitment(getSumAmounts(monthPlannedComm, currCode));


                    List monthPlannedDisb = getMonthlyData(ampFundingId, Constants.DISBURSEMENT, Constants.PLANNED, month, year);
                    List monthActualDisb = getMonthlyData(ampFundingId, Constants.DISBURSEMENT, Constants.ACTUAL, month, year);
                    comparison.setActualDisbursement(getSumAmounts(monthActualDisb, currCode));
                    comparison.setPlannedDisbursement(getSumAmounts(monthPlannedDisb, currCode));



                    List monthActualDisbOrder = getMonthlyData(ampFundingId, Constants.DISBURSEMENT_ORDER, Constants.ACTUAL, month, year);
                    comparison.setDisbOrders(getSumAmounts(monthActualDisbOrder, currCode));



                    List monthPlannedExp = getMonthlyData(ampFundingId, Constants.EXPENDITURE, Constants.PLANNED, month, year);
                    List monthActualExp = getMonthlyData(ampFundingId, Constants.EXPENDITURE, Constants.ACTUAL, month, year);
                    comparison.setActualExpenditure(getSumAmounts(monthActualExp, currCode));
                    comparison.setPlannedExpenditure(getSumAmounts(monthPlannedExp, currCode));

                    monthlyData.add(comparison);
                }
            }
        } catch (Exception ex) {
            logger.error("Unable to get monthly data from database", ex);
            throw new DgException(ex);


        }
        return monthlyData;

    }

    public static double getSumAmounts(List fundingDetails, String currCode) throws DgException {
        double sum = 0;
        if (fundingDetails != null && fundingDetails.size() > 0) {
            Iterator<AmpFundingDetail> monthPlannedIter = fundingDetails.iterator();

            while (monthPlannedIter.hasNext()) {
                AmpFundingDetail det = monthPlannedIter.next();
                double rate;
                double defCurrRate;
                java.sql.Date date = new java.sql.Date(det.getTransactionDate().getTime());
                defCurrRate = Util.getExchange(currCode, date);
                if (det.getFixedExchangeRate() == null) {



                    rate = Util.getExchange(det.getAmpCurrencyId().getCurrencyCode(), date);
                } else {
                    rate = det.getFixedExchangeRate();
                }


                sum += det.getTransactionAmount() / rate * defCurrRate;

            }

        }
        return sum;

    }


    public static List getMonthlyData(FilterParams fp) throws DgException {
        Long ampFundingId = fp.getAmpFundingId();
        String currCode = fp.getCurrencyCode();
        int transactionType = fp.getTransactionType();
        List monthlyData = new ArrayList();
        Double actualSum = new Double(0);
        Double plannedSum = new Double(0);


        /* if (transactionType == Constants.MTEFPROJECTION ) {
        return getQuarterlyDataForProjections(ampFundingId, adjustmentType);
        }
         */


        try {
            List months = getMonthYears(fp, true);
            if (months != null && months.size() > 0) {
                Iterator iter = months.iterator();
                while (iter.hasNext()) {
                    Object[] dates = (Object[]) iter.next();
                    Integer month = (Integer) dates[0];
                    Integer year = (Integer) dates[1];
                    List monthPlanned = getMonthlyData(ampFundingId, transactionType, Constants.PLANNED, month, year);
                    plannedSum = getSumAmounts(monthPlanned, currCode);
                    List monthActual = getMonthlyData(ampFundingId, transactionType, Constants.ACTUAL, month, year);
                    actualSum = getSumAmounts(monthActual, currCode);
                    MonthlyInfo info = new MonthlyInfo();
                    info.setActualAmount(actualSum);
                    info.setPlannedAmount(plannedSum);
                    DateFormatSymbols dfs = new DateFormatSymbols();
                    info.setYear(year);
                    info.setMonth(dfs.getMonths()[month - 1]);

                    monthlyData.add(info);


                }

            }







        } catch (Exception ex) {
            logger.error("Unable to get monthly data from database", ex);
            new DgException(ex);


        }
        return monthlyData;
    }

    /**
     * Returns a collection of records from amp_funding_detail
     * based on below  
     * @param ampFundingId
     * @param transactionType
     *                 commitment=0,disbursement=1,expenditure=2
     * @param adjustmentType
     *                 planned=0,actual=1
     * @param month
     * @param year           
     * @return List
     */
    public static List getMonthlyData(Long ampFundingId, int transactionType, int adjustmentType, int month, int year) throws DgException {

        Session session = null;
        Query q = null;
        List c = null;


        /* if (transactionType == Constants.MTEFPROJECTION ) {
        return getQuarterlyDataForProjections(ampFundingId, adjustmentType);
        }
         */


        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select f from " + AmpFundingDetail.class.getName() + " f where (f.ampFundingId=:ampFundingId) " + " and (f.transactionType=:trsType) " + " and (f.adjustmentType=:adjType) and month(f.transactionDate)=:month and year(f.transactionDate)=:year ";

            q = session.createQuery(queryString);
            q.setLong("ampFundingId", ampFundingId);
            q.setInteger("trsType", transactionType);
            q.setInteger("adjType", adjustmentType);
            q.setInteger("month", month);
            q.setInteger("year", year);

           
            /*queryString = "select  month(f.transactionDate), f.adjustmentType,"
            +  "sum(f.transactionAmount/"+
            "coalesce(f.fixedExchangeRate,1)" 
            +"(select coalesce(currRate.exchangeRate,1) from "
            + AmpCurrencyRate.class.getName()+" currRate ,"+AmpCurrency.class.getName()
            +" curr " +
            " where currRate.exchangeRateDate<=f.transactionDate "+ 
            " and currRate.toCurrencyCode=curr.currencyCode"+
            " and curr.ampCurrencyId=f.ampCurrencyId " +
            "  order by currRate.exchangeRateDate desc limit 1)))"*/
            /*+ " from "
            + AmpFundingDetail.class.getName()
            + " f where (f.ampFundingId=:ampFundingId) "
            + " and (f.orgRoleCode=:perspective) "
            + " and (f.transactionType=:trsType) "
            +" group by f.adjustmentType, month(f.transactionDate) ";*/


            c = q.list();
        } catch (Exception ex) {
            logger.error("Unable to get monthly data from database", ex);
            new DgException(ex);

        }
        return c;
    }
       

}
