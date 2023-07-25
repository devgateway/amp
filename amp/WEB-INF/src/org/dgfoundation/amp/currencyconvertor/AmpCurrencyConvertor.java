package org.dgfoundation.amp.currencyconvertor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.newreports.AmountsUnits;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.common.util.DateTimeUtil;

/**
 * the AMP schema currency convertor. Thread-safe
 * @author Dolghier Constantin
 *
 */
public class AmpCurrencyConvertor implements CurrencyConvertor {
    protected static Logger logger = Logger.getLogger(AmpCurrencyConvertor.class);

    protected ConcurrentHashMap<String, OneCurrencyCalculator> currencyCalculators = new ConcurrentHashMap<>();

    /**
     * the biggest event_id which has been factored in the state of the currencies calculator
     */
    protected long lastCurrencyChangeFactoredIn = -1;
    protected long lastTimeTokenChecked = -1;

    private static AmpCurrencyConvertor instance = new AmpCurrencyConvertor();
    protected String baseCurrencyCode = CurrencyUtil.getBaseCurrency().getCurrencyCode();

    public static AmpCurrencyConvertor getInstance() {
        return instance;
    }

    protected void checkCache() {
        if (System.currentTimeMillis() - lastTimeTokenChecked > 5 * 1000 * 60) { // check ETL for changes no more frequently than once in 5 minutes
            Long lastCurrencyChange = PersistenceManager.getLong(
                PersistenceManager.getSession().createNativeQuery("select max(event_id) from amp_etl_changelog where entity_name = 'exchange_rate'").uniqueResult());

            lastTimeTokenChecked = System.currentTimeMillis();

            if (lastCurrencyChange == null)
                lastCurrencyChange = 1l;

            if (lastCurrencyChange > this.lastCurrencyChangeFactoredIn) {
                currencyCalculators.clear();
                logger.warn("currencies cache invalidated, cleaning cache");
            }
            this.lastCurrencyChangeFactoredIn = lastCurrencyChange;
        }
    }

    /**
     * returns a currency calculator; also adds it to the map if missing
     * @param currency
     * @return
     */
    protected OneCurrencyCalculator getCalculator(String currencyCode) {
        OneCurrencyCalculator res = currencyCalculators.get(currencyCode);
        if (res != null)
            return res;

        logger.warn(String.format("calculating currency %s exchange rates", currencyCode));
        OneCurrencyCalculator newRes = PersistenceManager.getSession().doReturningWork(conn -> new OneCurrencyCalculator(CurrencyUtil.getAmpcurrency(currencyCode), conn));
        currencyCalculators.put(newRes.currency.getCurrencyCode(), newRes);
        return newRes;
    }

    @Override
    public double getExchangeRate(String fromCurrencyCode, String toCurrencyCode, Double fixedExchangeRate, LocalDate date) {
        if (fromCurrencyCode.equals(toCurrencyCode))
            return 1d;

        checkCache();

        if (fixedExchangeRate != null && fixedExchangeRate > 0) {
            return getExchangeRate(baseCurrencyCode, toCurrencyCode, null, date) / fixedExchangeRate;
        }

        long julianCode = DateTimeUtil.toJulianDayNumber(date);
        double res = getCalculator(fromCurrencyCode).getRate(julianCode) / getCalculator(toCurrencyCode).getRate(julianCode);
        return res;
    }

    public Double convertAmount(Double originalAmount, String fromCurrencyCode, String toCurrencyCode,
            LocalDate transactionDate) {

        return convertAmount(originalAmount, fromCurrencyCode, toCurrencyCode, null, transactionDate);
    }

    public Double convertAmount(Double originalAmount, String fromCurrencyCode, String toCurrencyCode,
            Double fixedExchangeRate, LocalDate transactionDate) {

        Double exchangeRate = getExchangeRate(fromCurrencyCode, toCurrencyCode, fixedExchangeRate, transactionDate);
        Double convertedAmount = BigDecimal.valueOf(originalAmount)
                .multiply(BigDecimal.valueOf(exchangeRate))
                .divide(new BigDecimal(AmountsUnits.getDefaultValue().divider)).doubleValue();

        return convertedAmount;
    }
}
