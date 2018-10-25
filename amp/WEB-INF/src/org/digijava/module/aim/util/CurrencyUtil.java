/*
 * CurrencyUtil.java
 * Created: 01-May-2005
 */
package org.digijava.module.aim.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.diffcaching.DatabaseChangedDetector;
import org.dgfoundation.amp.diffcaching.ExpiringCacher;
import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.util.DigiCacheManager;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpCurrencyRate;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.aim.helper.Currency;
import org.digijava.module.aim.helper.CurrencyRates;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.caching.AmpCaching;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.type.DateType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.StringType;

public class CurrencyUtil {

    private static Logger logger = Logger.getLogger(CurrencyUtil.class);

    public static final int RATE_FROM_FILE = 0;
    public static final int RATE_FROM_WEB_SERVICE = 1;
    public static final int RATE_BY_HAND = 2;

    public static final int ORDER_BY_CURRENCY_CODE = -1;
    public static final int ORDER_BY_CURRENCY_NAME = 2;
    public static final int ORDER_BY_CURRENCY_COUNTRY_NAME = 3;
    public static final int ALL_ACTIVE = 1;
    public static final String BASE_CODE = "USD";


    public static Collection<CurrencyRates> getAllCurrencyRates() {
        return getCurrencyRates(null, null, Collections.emptyList(), false);
    }

    public static Collection<CurrencyRates> getActiveCurrencyRates(Date fromDate, Date toDate) {
        return getCurrencyRates(fromDate, toDate, Collections.emptyList(), true);
    }

    public static Collection<CurrencyRates> getCurrencyRates(List<Date> days, boolean onlyActive) {
        return getCurrencyRates(null, null, days, onlyActive);
    }

    private static Collection<CurrencyRates> getCurrencyRates(Date fromDate, Date toDate, List<Date> days,
                                                              boolean onlyActive) {
        if ((fromDate == null && toDate != null) || (fromDate != null && toDate == null)) {
            throw new IllegalArgumentException("fromDate and toDate must be both either null or non null");
        }
        Collection<CurrencyRates> col = new ArrayList<>();
        Session session = null;
        String qryStr = null;
        Query qry = null;

        try {
            session = PersistenceManager.getSession();
            qryStr = "select currency from " + AmpCurrency.class.getName() + " currency";
            if (onlyActive) {
                qryStr += " where (currency.activeFlag = '1') ";
            }
            qry = session.createQuery(qryStr);
            Collection res = qry.list();
            if (res.size() > 0) {
                logger.debug("Active currencies found");
                Iterator<AmpCurrency> itr = res.iterator();
                HashMap<String, String> currencies = new HashMap<String, String>(res.size());
                while (itr.hasNext()) {
                    AmpCurrency curr = itr.next();
                    currencies.put(curr.getCurrencyCode(), curr.getCurrencyName());
                }
                qryStr = "select cRate from " + AmpCurrencyRate.class.getName() + " cRate " +
                        "where cRate.toCurrencyCode in (";
                Set<String> currencyCodes = currencies.keySet();
                if (currencyCodes != null && currencyCodes.size() > 0) {
                    for (String currencyCode : currencyCodes) {
                        qryStr += "'" + currencyCode + "'" + ",";
                    }
                    qryStr = qryStr.substring(0, qryStr.length() - 1);
                }

                qryStr += ") ";
                if (fromDate != null && toDate != null) {
                    qryStr += "and cRate.exchangeRateDate between :fromDate and :toDate ";
                }
                if (!days.isEmpty()) {
                    qryStr += "and cRate.exchangeRateDate in :days ";
                }
                qryStr += "order by cRate.exchangeRateDate desc,cRate.toCurrencyCode";
                qry = session.createQuery(qryStr);
                if (fromDate != null && toDate != null) {
                    qry.setParameter("fromDate", fromDate, DateType.INSTANCE);
                    qry.setParameter("toDate", toDate, DateType.INSTANCE);
                }
                if (!days.isEmpty()) {
                    qry.setParameterList("days", days, DateType.INSTANCE);
                }
                Iterator<AmpCurrencyRate> itr2 = qry.list().iterator();
                AmpCurrencyRate cRate = null;

                CurrencyRates currencyRates = null;
                while (itr2.hasNext()) {
                    cRate = itr2.next();
                    boolean bothCurrenciesAcceptable = cRate.getfromCurrencyCode() != null && currencyCodes.contains(cRate.getfromCurrencyCode()) &&
                            cRate.getToCurrencyCode() != null && currencyCodes.contains(cRate.getToCurrencyCode());
                    if (!bothCurrenciesAcceptable)
                        continue;
                    currencyRates = new CurrencyRates();
                    currencyRates.setCurrencyCode(cRate.getToCurrencyCode());
                    currencyRates.setFromCurrencyCode(cRate.getFromCurrencyCode());
                    currencyRates.setCurrencyName(currencies.get(cRate.getToCurrencyCode()));
                    currencyRates.setFromCurrencyName(currencies.get(cRate.getFromCurrencyCode()));
                    currencyRates.setExchangeRate(cRate.getExchangeRate());
                    currencyRates.setExchangeRateDateAsDate(cRate.getExchangeRateDate());
                    currencyRates.setExchangeRateDate(DateConversion.convertDateToString(cRate.getExchangeRateDate()));
                    currencyRates.setId(cRate.getAmpCurrencyRateId());
                    col.add(currencyRates);
                }

            }

        } catch (Exception e) {
            logger.error("Exception from getAllCurrencyRates");
            e.printStackTrace(System.out);
        }

        logger.info("returning a collection of size get all active rates function" + col.size());
        return col;
    }

    /**
     * Saves an AmpCurrencyRate object to the database
     *
     * @param cRate The AmpCurrencyRate object
     */
    public static void saveCurrencyRate(AmpCurrencyRate cRate, boolean calledFromQuartzJob) {
        String qryStr = null;
        if (!calledFromQuartzJob) {
            AmpCaching.getInstance().currencyCache.reset();
        }

        try {
            //session = PersistenceManager.getCurrentSession();
            //tx = session.beginTransaction();

            qryStr = "select cRate from " + AmpCurrencyRate.class.getName() + " cRate " +
                    "where (cRate.toCurrencyCode=:code) and (cRate.fromCurrencyCode=:fromCode) and" +
                    "(cRate.exchangeRateDate=:date)";
            Query qry = PersistenceManager.getSession().createQuery(qryStr);
            qry.setString("code", cRate.getToCurrencyCode());
            qry.setString("fromCode", cRate.getFromCurrencyCode());
            qry.setDate("date", cRate.getExchangeRateDate());

            Iterator<AmpCurrencyRate> itr = qry.list().iterator();
            if (itr.hasNext()) {
                // if the currency rate already exist update the rate
                AmpCurrencyRate actRate = itr.next();
                actRate.setExchangeRate(cRate.getExchangeRate());
                PersistenceManager.getSession().update(actRate);
            } else {
                // add the currency rate object if it does not exist
                PersistenceManager.getSession().save(cRate);
            }

            //tx.commit();
        } catch (Exception e) {
            logger.error("Couldn't save Exchange Rates ", e);
            throw new RuntimeException(e);
        }
    }

    public static Collection<AmpCurrency> getAllCurrencies(int active) {
        return getAllCurrencies(active, "");
    }

    public static Collection<AmpCurrency> getAllCurrencies(int active, String sortOrder) {
        Collection<AmpCurrency> col = new ArrayList<AmpCurrency>();
        Session session = null;
        Query qry = null;
        String qryStr = null;
        try {
            session = PersistenceManager.getRequestDBSession();

            if (active == CurrencyUtil.ORDER_BY_CURRENCY_CODE) {
                qryStr = "select curr from " + AmpCurrency.class.getName() + " as curr left join fetch  "
                        + "curr.countryLocation dg where curr.virtual is false order by curr.currencyCode " + sortOrder;
                qry = session.createQuery(qryStr);
            } else if (active == CurrencyUtil.ORDER_BY_CURRENCY_NAME) {
                qryStr = "select curr from " + AmpCurrency.class.getName() + " as curr left join fetch  "
                        + "curr.countryLocation dg where curr.virtual is false order by curr.currencyName " + sortOrder;
                qry = session.createQuery(qryStr);
            } else if (active == CurrencyUtil.ORDER_BY_CURRENCY_COUNTRY_NAME) {
                qryStr = "select curr from " + AmpCurrency.class.getName()
                        + " as curr left outer join curr.countryLocation dg where curr.virtual "
                        + "is false order by dg.name " + sortOrder;
                qry = session.createQuery(qryStr);
            } else {
                qryStr = "select curr from " + AmpCurrency.class.getName() + " curr "
                        + "where (curr.activeFlag=:flag) and curr.virtual is false order by curr.currencyCode "
                        + sortOrder;
                qry = session.createQuery(qryStr);
                qry.setParameter("flag", new Integer(active), IntegerType.INSTANCE);
            }
            col = qry.list();
        } catch (Exception e) {
            logger.error("Exception from getAllCurrencies()", e);
        }
        return col;
    }

    /**
     * @param in usually is the result of CurrencyUtil.getActiveAmpCurrencyByName()
     * @return
     */
    public static List<AmpCurrency> removeVirtualCurrencies(Collection<AmpCurrency> in) {
        List<AmpCurrency> res = new ArrayList<>(CurrencyUtil.getActiveAmpCurrencyByName());
        // O(n^2) algorithm ok because there are at most 150 currencies worldwode
        Iterator<AmpCurrency> it = res.iterator();
        while (it.hasNext()) {
            if (it.next().isVirtual())
                it.remove();
        }
        return res;
    }

    public static boolean isCurrencyRateInDatabase(AmpCurrencyRate cRate) {
        Session session = null;
        Query qry = null;
        String qryStr = null;
        boolean exists = false;
        try {
            session = PersistenceManager.getSession();
            qryStr = "select cRate from " + AmpCurrencyRate.class.getName() + " cRate " +
                    "where (cRate.toCurrencyCode=:code) and (cRate.fromCurrencyCode=:fromCode) and" +
                    "(cRate.exchangeRateDate=:date)";
            qry = session.createQuery(qryStr);
            qry.setString("code", cRate.getToCurrencyCode());
            qry.setString("fromCode", cRate.getFromCurrencyCode());
            qry.setDate("date", cRate.getExchangeRateDate());

            exists = !qry.list().isEmpty();

        } catch (Exception e) {
            logger.error("Exception while trying to check if a currency rate exists in database", e);
        }
        return exists;
    }


    public static void updateCurrencyStatus(String code, int status) {
        Session session = null;
        Query qry = null;
        String qryStr = null;
        AmpCaching.getInstance().currencyCache.reset();

        try {
            session = PersistenceManager.getSession();
            qryStr = "select curr from " + AmpCurrency.class.getName() + " curr " +
                    "where (curr.currencyCode=:code)";
            qry = session.createQuery(qryStr);
            qry.setParameter("code", code, StringType.INSTANCE);
            Iterator itr = qry.list().iterator();
            if (itr.hasNext()) {
                AmpCurrency curr = (AmpCurrency) itr.next();
                curr.setActiveFlag(new Integer(status));
//beginTransaction();
                session.update(curr);
                //tx.commit();
            }
        } catch (Exception e) {
            logger.error("Exception from updateCurrencyStatus");
            e.printStackTrace(System.out);
        }
    }

    public static void saveCurrency(AmpCurrency curr) {
        Session session = null;
        AmpCaching.getInstance().currencyCache.reset();

        try {
            session = PersistenceManager.getSession();
//beginTransaction();
            session.save(curr);
            //tx.commit();

        } catch (Exception e) {
            logger.error("Exception from saveCurrency");
            e.printStackTrace(System.out);
        }
    }

    /**
     * Save an AmpCurrency object
     *
     * @param currency The AmpCurrency Object to be saved
     * @param cRate    The initial currency rates in an AmpCurrencyRate object
     */
    public static void saveCurrency(AmpCurrency currency, AmpCurrencyRate cRate) {
        AmpCaching.getInstance().currencyCache.reset();
        Session session = null;
        Query qry = null;
        String qryStr = null;

        try {
            session = PersistenceManager.getSession();
            qryStr = "select curr from " + AmpCurrency.class.getName() + " curr "
                    + "where (curr.ampCurrencyId=:id)";
            qry = session.createQuery(qryStr);
            logger.debug("Checking with the id " + currency.getAmpCurrencyId());
            qry.setParameter("id", currency.getAmpCurrencyId(), StandardBasicTypes.LONG);
            if (!qry.list().isEmpty()) {
                // currency object already exist, update the object
                logger.debug("Updating the existing currency id ...");
                AmpCurrency curr = (AmpCurrency) qry.uniqueResult();
                curr.setCountryName(currency.getCountryName());
                curr.setCurrencyCode(currency.getCurrencyCode());
                curr.setCurrencyName(currency.getCurrencyName());
                curr.setCountryLocation(currency.getCountryLocation());
                session.update(curr);
            } else {
                logger.debug("Creating new currency id ...");
                session.save(currency);
                if (cRate != null) {
                    session.save(cRate);
                }
            }
        } catch (Exception e) {
            logger.error("Exception from saveCurrency", e);
        }
    }

    public static void deleteCurrencyRates(Long[] cRates) {
        AmpCaching.getInstance().currencyCache.reset();
        Session session = null;
        try {
            session = PersistenceManager.getSession();
//beginTransaction();
            for (int i = 0; i < cRates.length; i++) {
                if (cRates[i] != null) {
                    AmpCurrencyRate cRate = (AmpCurrencyRate) session.load(AmpCurrencyRate.class, cRates[i]);
                    session.delete(cRate);
                }
            }
            //tx.commit();
        } catch (Exception e) {
            logger.error("Exception from deleteCurrencyRates");
            e.printStackTrace(System.out);
        }
    }

    /**
     * Saves currency rates to the database
     *
     * @param currRates Collection of CurrencyRates object which need
     *                  to be saved
     * @throws Exception
     */
    public static void saveCurrencyRates(Collection currRates, String baseCurrencyCode) throws Exception {
        Session session = null;
        Query qry = null;
        String qryStr = null;
        AmpCaching.getInstance().currencyCache.reset();

        try {
            session = PersistenceManager.getSession();
//beginTransaction();

            Iterator itr = currRates.iterator();
            logger.debug("currency rates size :" + currRates.size());
            while (itr.hasNext()) {
                CurrencyRates cr = (CurrencyRates) itr.next();
                qryStr = "select crate from " + AmpCurrencyRate.class.getName()
                        + " crate where (crate.toCurrencyCode=:code) and (crate.fromCurrencyCode=:fromCurrencyCode) "
                        + "and (crate.exchangeRateDate=:date)";
                qry = session.createQuery(qryStr);
                qry.setParameter("code", cr.getCurrencyCode(), StringType.INSTANCE);
                qry.setParameter("fromCurrencyCode", baseCurrencyCode, StringType.INSTANCE);
                Date exRtDate = DateConversion.getDate(cr.getExchangeRateDate());
                qry.setParameter("date", exRtDate, DateType.INSTANCE);
                Iterator tmpItr = qry.list().iterator();
                if (tmpItr.hasNext()) {
                    AmpCurrencyRate currencyRate = (AmpCurrencyRate) tmpItr.next();
                    currencyRate.setExchangeRate(cr.getExchangeRate());
                    session.update(currencyRate);
                    logger.debug("Updating " + currencyRate.getToCurrencyCode());
                } else {
                    AmpCurrencyRate currencyRate = new AmpCurrencyRate();
                    currencyRate.setExchangeRate(cr.getExchangeRate());
                    currencyRate.setExchangeRateDate(exRtDate);
                    currencyRate.setToCurrencyCode(cr.getCurrencyCode());
                    currencyRate.setFromCurrencyCode(baseCurrencyCode);
                    currencyRate.setDataSource(CurrencyUtil.RATE_FROM_FILE);
                    logger.debug("Saving " + currencyRate.getToCurrencyCode());
                    session.save(currencyRate);
                }
            }
            //tx.commit();
            DigiCacheManager.getInstance().getCache(ArConstants.EXCHANGE_RATES_CACHE).clear();
        } catch (Exception e) {
            logger.error("Exception from saveCurrencyRates");
            e.printStackTrace(System.out);
            throw e;
        }
    }

    public static void updateCurrency(AmpCurrency ampCurr,
                                      AmpCurrencyRate ampCurrRate) {
        AmpCaching.getInstance().currencyCache.reset();
        DbUtil.update(ampCurr);
        DbUtil.update(ampCurrRate);
    }

    public static AmpCurrency getCurrencyByCode(String currCode) {
        try {
            String queryString = "select c from " + AmpCurrency.class.getName()
                    + " c where (c.currencyCode=:currCode)";
            return (AmpCurrency) PersistenceManager.getSession().createQuery(queryString).setParameter("currCode", currCode, StringType.INSTANCE).uniqueResult();
        } catch (Exception e) {
            logger.error("Unable to get currency with code " + currCode, e);
            return null;
        }
    }

    public static Currency getCurrency(Long id) {
        logger.debug("in getCurrency" + id);
        Session sess = PersistenceManager.getSession();
        Query qry1 = null;
        Query qry2 = null;
        Currency curr = null;
        AmpCurrency ampCurrency = null;

        String queryString = "select c from " + AmpCurrency.class.getName() + " c where (c.ampCurrencyId=:id)";
        qry1 = sess.createQuery(queryString);
        qry1.setParameter("id", id, LongType.INSTANCE);
        ampCurrency = (AmpCurrency) qry1.uniqueResult();

        if (ampCurrency == null) {
            ampCurrency = getDefaultCurrency();
        }

        curr = new Currency();
        curr.setCurrencyId(ampCurrency.getAmpCurrencyId());
        curr.setCurrencyCode(ampCurrency.getCurrencyCode());
        curr.setCountryName(ampCurrency.getCountryName());

        String qryStr = "select cr from " + AmpCurrencyRate.class.getName() + " cr where (cr.toCurrencyCode=:currCode)";
        qry2 = sess.createQuery(qryStr);
        qry2.setParameter("currCode", ampCurrency.getCurrencyCode());
        Iterator itr1 = qry2.list().iterator();
        if (itr1.hasNext()) {
            AmpCurrencyRate ampCurrRate = (AmpCurrencyRate) itr1.next();
            curr.setCurrencyRateId(ampCurrRate.getAmpCurrencyRateId());
            curr.setExchangeRate(ampCurrRate.getExchangeRate());
        } else {
            curr.setExchangeRate(1.0);
        }


        return curr;
    }

    public static AmpCurrency getAmpcurrency(Long id) {
        return (AmpCurrency) PersistenceManager.getSession().get(AmpCurrency.class, id);
    }

    private static ExpiringCacher<String, Boolean, AmpCurrency> currencies = new ExpiringCacher<>("currencyByCode", (code, ignored) -> doFetchCurrency(code), new DatabaseChangedDetector(), 30 * 60 * 1000);

    public static AmpCurrency getAmpcurrency(String currCode) {
        return currencies.buildOrGetValue(currCode, true);
    }

    private static AmpCurrency doFetchCurrency(String currCode) {
        try {
            String queryString = "select c from " + AmpCurrency.class.getName()
                    + " c " + "where (c.currencyCode=:id)";
            Query qry = PersistenceManager.getSession().createQuery(queryString);
            qry.setCacheable(true);
            qry.setParameter("id", currCode, StringType.INSTANCE);
            return (AmpCurrency) qry.uniqueResult();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static List<AmpCurrency> getActiveAmpCurrencyByName() {
        // don't include by default, each module should explicitly request if it needs them
        return getActiveAmpCurrencyByName(false);
    }

    public static List<AmpCurrency> getActiveAmpCurrencyByName(boolean includeVirtual) {
        List<AmpCurrency> currencies = getActiveAlsoVirtualAmpCurrencyByName();
        if (!includeVirtual) {
            for (Iterator<AmpCurrency> iter = currencies.iterator(); iter.hasNext();) {
                if (iter.next().isVirtual()) {
                    iter.remove();
                }
            }
        }
        return currencies;
    }

    private static List<AmpCurrency> getActiveAlsoVirtualAmpCurrencyByName() {
        if (AmpCaching.getInstance().currencyCache.activeCurrencies != null)
            return new ArrayList<AmpCurrency>(AmpCaching.getInstance().currencyCache.activeCurrencies);
        AmpCurrency ampCurrency = null;
        Session session = null;
        Query q = null;
        Iterator<AmpCurrency> iter = null;
        String queryString = null;
        ArrayList<AmpCurrency> currency = new ArrayList<AmpCurrency>();
        try {
            session = PersistenceManager.getRequestDBSession();
            queryString = " select c from " + AmpCurrency.class.getName()
                    + " c where c.activeFlag='1' order by c.currencyName";
            q = session.createQuery(queryString);
            iter = q.list().iterator();

            while (iter.hasNext()) {
                ampCurrency = iter.next();
                currency.add(ampCurrency);
            }
        } catch (Exception ex) {
            logger.error("Unable to get currency " + ex);
        }
//      finally {
//          try {
//              PersistenceManager.releaseSession(session);
//          } catch (Exception ex2) {
//              logger.debug("releaseSession() failed", ex2);
//          }
//      }
        AmpCaching.getInstance().currencyCache.activeCurrencies = new ArrayList<AmpCurrency>(currency);
        return currency;
    }

    public static List<AmpCurrency> getActiveAmpCurrencyByCode() {
        String queryString = null;
        ArrayList<AmpCurrency> currency = new ArrayList<AmpCurrency>();
        try {
            queryString = " select c from " + AmpCurrency.class.getName()
                    + " c where c.activeFlag='1' order by c.currencyCode";
            List<AmpCurrency> temp = PersistenceManager.getSession().createQuery(queryString).setCacheable(true).list();
            List<AmpCurrency> res = new ArrayList<>();
            for (AmpCurrency c : temp) {
                if (!c.isVirtual()) {
                    res.add(c);
                }
            }
            return res;
        } catch (Exception ex) {
            logger.error("Unable to get currency " + ex);
        }
        return currency;
    }

    public static List<AmpCurrency> getAllAmpCurrencies() {
        String queryString = null;
        ArrayList<AmpCurrency> currency = new ArrayList<AmpCurrency>();
        try {
            queryString = " select c from " + AmpCurrency.class.getName()
                    + " c order by c.currencyCode";
            List<AmpCurrency> temp = PersistenceManager.getSession().createQuery(queryString).setCacheable(true).list();
            List<AmpCurrency> res = new ArrayList<>();
            for (AmpCurrency c : temp) {
                if (!c.isVirtual()) {
                    res.add(c);
                }
            }
            return res;
        } catch (Exception ex) {
            logger.error("Unable to get currency " + ex);
        }
        return currency;
    }

    public static double getExchangeRate(String currencyCode) {

        try {
            return CurrencyUtil.getLatestExchangeRate(currencyCode);
        } catch (AimException e) {
            e.printStackTrace();
            return 1.0;
        }
    }

    /**
     * returns list of all "usable" currencies in AMP: all configured currencies which have an exchange rate + base currency
     *
     * @return
     */
    public static List<AmpCurrency> getUsableCurrencies() {
        // keeping old references as not using virtual currencies, since not requested
        return getUsableCurrencies(false);
    }

    public static List<AmpCurrency> getUsableCurrencies(boolean includeVirtual) {
        //Only currencies having exchanges rates AMP-2620
        List<AmpCurrency> usableCurrencies = new ArrayList<AmpCurrency>();

        for (AmpCurrency currency : getActiveAmpCurrencyByName(includeVirtual)) {
            if (currency.isRate()) {
                usableCurrencies.add(currency);
            }
        }
        return usableCurrencies;
    }

    public static List<AmpCurrency> getUsableNonVirtualCurrencies() {
        //Only currencies having exchanges rates AMP-2620
        List<AmpCurrency> usableCurrencies = new ArrayList<AmpCurrency>();

        for (AmpCurrency currency : getActiveAmpCurrencyByName()) {
            if (currency.isRate() && !currency.isVirtual()) {
                usableCurrencies.add(currency);
            }
        }
        return usableCurrencies;
    }

    /**
     * Returns Latest Exchange rate for currency specified in parameter by code.
     * Used in NPD, may be temporarry
     *
     * @param currencyCode currency Code
     * @return exchange rate, latest value
     * @author Irakli Kobiashvili
     */
    public static double getLatestExchangeRate(String currencyCode) throws AimException {
        String baseCurrCode = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.BASE_CURRENCY);
        if (baseCurrCode == null) {
            baseCurrCode = "USD";
        }

        Session session = null;
        Query q = null;
        try {
            logger.debug("retrivieving latest exchange rate for currency:" + currencyCode);
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select f.exchangeRate from "
                    + AmpCurrencyRate.class.getName()
                    + " f where (f.toCurrencyCode=:currencyCode and f.fromCurrencyCode=:baseCurrencyCode) "
                    + "order by f.exchangeRateDate desc limit 1";
            q = session.createQuery(queryString);
            q.setString("currencyCode", currencyCode);
            q.setString("baseCurrencyCode", baseCurrCode);
            List rates = q.list();
            Double result = null;
            if (rates == null || rates.isEmpty()) {
                logger.debug("No exchange rate value found for currency: " + currencyCode);
                result = new Double(1.0);
            } else {
                result = (Double) rates.iterator().next();
            }
            return result.doubleValue();
        } catch (Exception ex) {
            logger.debug("Unable to get exchange rate from database", ex);
            throw new AimException("Error retriving currency exchange rate for " + currencyCode, ex);
        }
    }

    public static Collection getCurrencyRateByDataSource(Integer id) {
        Collection col = new ArrayList();
        Session session = null;
        Query qry = null;
        String qryStr = null;
        try {
            session = PersistenceManager.getSession();

            qryStr = "select curr from " + AmpCurrencyRate.class.getName() + " curr where curr.dataSource=:id ";
            qry = session.createQuery(qryStr);
            qry.setParameter("id", id, IntegerType.INSTANCE);
            col = qry.list();
        } catch (Exception e) {
            logger.error("Exception from getAllCurrencies()");
            e.printStackTrace(System.out);
        }
        return col;
    }

    public static AmpCurrency getBaseCurrency() {
        return CurrencyUtil.getAmpcurrency(getBaseCurrencyCode());
    }

    /*
     * For Deleting a Currency...
     */
    public static void deleteCurrency(String code) throws Exception {
        /*try
        {
        */
        AmpCaching.getInstance().currencyCache.reset();
        AmpCurrency ampC = new AmpCurrency();
        ampC = CurrencyUtil.getCurrencyByCode(code);
        if (ampC != null) {
            DbUtil.delete(ampC);
        }
        /*}
        catch (Exception e) {
            logger.error("Exception from getAllCurrencies()");
            e.printStackTrace(System.out);
        }*/
    }

    /**
     * Returns workspace settings currency or
     * returns default currency if no team member logged in or the currency is not set in the workspace settings
     *
     * @param tm
     * @return
     */
    public static AmpCurrency getWorkspaceCurrency(TeamMember tm) {
        AmpCurrency currency = null;
        if (tm != null && tm.getAppSettings() != null) {
            currency = getAmpcurrency(tm.getAppSettings().getCurrencyId());
            if (currency != null) {
                return currency;
            }

            return getDefaultCurrency();
        }
        return getDefaultCurrency();
    }

    /**
     * returns default currency if no team member logged in
     *
     * @param tm
     * @return
     */
    public static AmpCurrency getWicketWorkspaceCurrency() {
        TeamMember tm = AmpAuthWebSession.getYourAppSession().getCurrentMember();
        return getWorkspaceCurrency(tm);
    }

    /**
     * not initialized on static constructor, because Hibernate & stuff are not initialized at that point
     */
    private static AmpCurrency defaultCurrency;

    public static AmpCurrency getDefaultCurrency() {
        if (defaultCurrency == null) {
            defaultCurrency = AmpARFilter.getDefaultCurrency();
        }
        return defaultCurrency;
    }

    public static String getBaseCurrencyCode() {
        String currCode = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.BASE_CURRENCY);
        if (currCode == null)
            currCode = BASE_CODE;
        return currCode;
    }

    /**
     * checks AMP_CURRENCY_RATE table for invalid entries
     *
     * @throws AimException
     */
    public static void checkDatabaseSanity(Session session) throws AimException {
        String errMsg = null;
        logger.debug("Database sanity check - in progress...");
        Query query = session.createQuery("select r from AmpCurrencyRate r "
                + "where r.fromCurrencyCode is null or r.toCurrencyCode is null or r.exchangeRate is null "
                + "or r.exchangeRate <=0 or r.fromCurrencyCode=r.toCurrencyCode").setMaxResults(1);
        List<?> res = query.list();
        if (!res.isEmpty()) {
            errMsg = "AMP_CURRENCY_RATE contains invalid entries. The following constraints must be met: " + System.lineSeparator()
                    + "1) NOT NULL: to_currency_code, from_currency_code, exchange_rate, exchange_rate_date; " + System.lineSeparator()
                    + "2) currency_rate > 0; " + System.lineSeparator()
                    + "3) from_currency_code <> to_currency_code; " + System.lineSeparator()
                    + "4) unique tuples (from, to, date).";
        }
        if (errMsg != null) {
            logger.error("AMP_CURRENCY_RATE table consistency check - FAIL:" + errMsg);
            throw new AimException(errMsg);
        } else {
            logger.debug("AMP_CURRENCY_RATE table consistency check - PASS");
        }
    }

    public static void deleteCurrencyRates(String currencyCode) {
        PersistenceManager.getSession().createQuery(
                String.format("delete from %s o where o.fromCurrencyCode = '%s' or o.toCurrencyCode = '%s'",
                        AmpCurrencyRate.class.getName(), currencyCode, currencyCode)).executeUpdate();
    }

    public static void deleteCurrencyRates(List<String> currencyCode) {
        if (currencyCode.size() == 0) {
            return;
        }
        String codes = Util.toCSString(currencyCode);
        PersistenceManager.getSession().createSQLQuery(String.format("DELETE FROM amp_currency_rate r "
                + " WHERE r.from_currency_code in (%s) OR r.to_currency_code in (%s)", codes, codes)).executeUpdate();
    }

    public static AmpCurrency getEffectiveCurrency() {
        return CurrencyUtil.getWorkspaceCurrency(TeamUtil.getCurrentMember());
    }
}
