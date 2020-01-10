/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.currency;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.currency.ConstantCurrency;
import org.dgfoundation.amp.currency.CurrencyInflationUtil;
import org.dgfoundation.amp.currency.inflation.CCExchangeRate;
import org.dgfoundation.amp.currency.inflation.ds.FredDataSource;
import org.digijava.kernel.ampapi.endpoints.currency.dto.CurrencyPair;
import org.digijava.kernel.ampapi.endpoints.currency.dto.ExchangeRate;
import org.digijava.kernel.ampapi.endpoints.currency.dto.ExchangeRatesForPair;
import org.digijava.kernel.ampapi.endpoints.currency.dto.InflationDataSource;
import org.digijava.kernel.ampapi.endpoints.currency.dto.InflationDataSourceSettings;
import org.digijava.kernel.ampapi.endpoints.errors.ApiEMGroup;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.exception.AmpWebApplicationException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpInflationRate;
import org.digijava.module.aim.dbentity.AmpInflationSource;
import org.digijava.module.aim.helper.CurrencyRates;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.common.util.DateTimeUtil;
import org.hibernate.Session;


/**
 * Currency Endpoint utility methods
 * 
 * @author Nadejda Mandrescu
 */
public class CurrencyService {
    
    protected static final Logger logger = Logger.getLogger(CurrencyService.class);
    protected static final SimpleDateFormat DATE_FORMATTER = DateTimeUtil.getStrictSimpleDateFormat(
            CurrencyEPConstants.DATE_FORMAT);

    public static final CurrencyService INSTANCE = new CurrencyService();
    
    /**
     * @see Currencies#getCurrencyInflationDataSources()
     */
    public static List<InflationDataSource> getInflationDataSources() {
        List<InflationDataSource> dataSources = new ArrayList<>();
        List<AmpInflationSource> ampSources = CurrencyInflationUtil.getInflationDataSources();
        if (ampSources != null && ampSources.size() > 0) {
            for (AmpInflationSource ais : ampSources) {
                InflationDataSource ds = new InflationDataSource();
                ds.setId(ais.getId());
                ds.setName(TranslatorWorker.translateText(ais.getName()));
                ds.setDescription(TranslatorWorker.translateText(ais.getDescription()));
                ds.setSelected(ais.getSelected());
                
                //DEFLATOR: temporary just for information, until full settings support based on Settings V2 API can be used
                InflationDataSourceSettings settings = new InflationDataSourceSettings();
                settings.setCurrencyCode(ais.getCurrency().getCurrencyCode());
                settings.setFrequency(ais.getFrequency());
                settings.setApiToken(ais.getApiToken());
                ds.setSettings(settings);
                
                dataSources.add(ds);
            }
        }
        
        return dataSources;
    }
    
    /**
     * @see Currencies#getAmpInflationRates()
     */
    public static Map<String, Map<String, Double>> getAmpInflationRates() {
        Map<String, Map<String, Double>> inflationRates = new HashMap<>();
        
        List<AmpInflationRate> rates = CurrencyInflationUtil.getInflationRates();
        if (rates != null && rates.size() > 0) {
            for (AmpInflationRate rate : rates) {
                String currencyCode = rate.getCurrency().getCurrencyCode();
                inflationRates.putIfAbsent(currencyCode, new TreeMap<>());
                SortedMap<String, Double> currencyRates = (TreeMap<String, Double>) inflationRates.get(currencyCode);
                currencyRates.put(DATE_FORMATTER.format(rate.getPeriodStart()), rate.getInflationRate());
            }
        }
        
        return inflationRates;
    }
    
    /**
     * @see Currencies#saveInflationRates(Map<String, Map<String, Double>>)
     */
    public static void saveInflationRates(Map<String, Map<String, Double>> jsonRates) {
        ApiEMGroup errors = new ApiEMGroup();
        
        // prepare and validate data
        Map<AmpCurrency, Map<Date, Double>> ratesPerCurrency = new HashMap<>();
        for (Entry<String, Map<String, Double>> entry : jsonRates.entrySet()) {
            String currCode = entry.getKey();
            AmpCurrency currency = CurrencyUtil.getCurrencyByCode(currCode);
            Map<String, Double> series = entry.getValue();
            if (currency == null) {
                errors.addApiErrorMessage(CurrencyErrors.INVALID_CURRENCY_CODE, currCode);
            }
            String sInfo = currCode + " : ... %s ..."; 
            boolean invalidSeries = series == null;
            if (!invalidSeries) {
                Iterator<?> iter = series.keySet().iterator();
                if (iter.hasNext())
                    invalidSeries = !(iter.next() instanceof String);
            }
                
            if (invalidSeries) {
                errors.addApiErrorMessage(CurrencyErrors.INVALID_INFLATION_RATE_SERIES, String.format(sInfo,  
                        (series == null ? "null" : StringUtils.substring(series.toString(), 0, 20))));
            } else {
                // reset iterator and process the series
                for (Iterator<?> iter = ((Map) series).keySet().iterator(); iter.hasNext(); ) {
                    String dateStr = (String) iter.next();
                    Double value = series.get(dateStr);
                    Date date = null;
                    try {
                        date = DATE_FORMATTER.parse(dateStr);
                        if (date.before(AmpInflationRate.MIN_DATE) || date.after(AmpInflationRate.MAX_DATE)) {
                            errors.addApiErrorMessage(CurrencyErrors.INVALID_PERIOD, String.format(sInfo, dateStr));
                            date = null; // reset
                        }
                    } catch (ParseException e) {
                        logger.error("Date Format parse exception: " + dateStr, e);
                        errors.addApiErrorMessage(CurrencyErrors.INVALID_DATE_FORMAT, String.format(sInfo, dateStr));
                    }
                    
                    if (currency != null && date != null) {
                        // now if everything is valid, record it
                        Map<Date, Double> dateValues = ratesPerCurrency.get(currency);
                        if (dateValues == null) {
                            dateValues = new TreeMap<Date, Double>();
                            ratesPerCurrency.put(currency, dateValues);
                        }
                        dateValues.put(date, ((Number) value).doubleValue());
                    }
                }
            }
        }
        
        if (errors.size() == 0) {
            // if no errors, then cleanup existing rates and create new ones
            CurrencyInflationUtil.deleteAllInflationRates();
            Session session = PersistenceManager.getSession();
            for (Entry<AmpCurrency, Map<Date, Double>> entry : ratesPerCurrency.entrySet()) {
                for (Entry<Date, Double> vEntry : entry.getValue().entrySet()) {
                    AmpInflationRate air = new AmpInflationRate(entry.getKey(), vEntry.getKey(), vEntry.getValue());
                    session.save(air);
                }
            }
            // regenerate exchange rates based on new inflation rates
            CCExchangeRate.regenerateConstantCurrenciesExchangeRates(false);
        } else {
            throw new AmpWebApplicationException(Response.Status.BAD_REQUEST, ApiError.toError(errors));
        }
    }
    
    /**
     * @see Currencies#getInflationRatesFromSource(Long)
     */
    public static Map<String, Map<String, Double>> getInflationRatesFromSource(Long sourceId) {
        AmpInflationSource ds = CurrencyInflationUtil.getInflationDataSource(sourceId);
        if (ds == null) {
            throw new AmpWebApplicationException(Response.Status.BAD_REQUEST,
                    CurrencyErrors.INVALID_SOURCE_ID.withDetails(String.valueOf(sourceId)));
        }
        
        if (ds.getName().equals(CurrencyEPConstants.FRED_GNPDEF)) {
            Map<String, Map<String, Double>> inflRates = new HashMap<>();
            FredDataSource fredDS = new FredDataSource(ds.getApiToken(), ds.getFrequency());
            inflRates.put(ds.getCurrency().getCurrencyCode(), fredDS.getGNPDEFObservationsAsIs());
            
            return inflRates;
        }
        
        return null;
    }
    
    protected static Map<String, SortedMap<String, Double>> convert(Map<String, SortedMap<Date, Double>> inflRates) {
        Map<String, SortedMap<String, Double>> result = new HashMap<String, SortedMap<String, Double>>();
        for(Entry<String, SortedMap<Date, Double>> entry : inflRates.entrySet()) {
            SortedMap<String, Double> series = new TreeMap<String, Double>();
            for (Entry<Date, Double> sEntry : entry.getValue().entrySet()) {
                series.put(DATE_FORMATTER.format(sEntry.getKey()), sEntry.getValue());
            }
            result.put(entry.getKey(), series);
        }
        return result;
    }
    
    /**
     * @see Currencies#getConstantCurrencies()
     */
    public static Map<String, Map<String, String>> getConstantCurrencies() {
        
        Map<String, Map<String, String>> constantCurrencies = new HashMap<>();
        Map<AmpFiscalCalendar, List<ConstantCurrency>> cCurrencies =
                CurrencyInflationUtil.getConstantCurrenciesByCalendar();
        
        for (Entry<AmpFiscalCalendar, List<ConstantCurrency>> calEntry : cCurrencies.entrySet()) {
            // collect years per currency
            Map<String, SortedSet<Integer>> currencyYears = new HashMap<String, SortedSet<Integer>>();
            for (ConstantCurrency cc: calEntry.getValue()) {
                SortedSet<Integer> years = currencyYears.get(cc.standardCurrencyCode);
                if (years == null) {
                    years = new TreeSet<Integer>();
                    currencyYears.put(cc.standardCurrencyCode, years);
                }
                years.add(cc.year);
            }
            // build simplified years view
            SortedMap<String, String> calCurrYears = new TreeMap<String, String>();
            for (Entry<String, SortedSet<Integer>> cYears : currencyYears.entrySet()) {
                if (cYears.getValue().size() != 0) {
                    StringBuilder sb = new StringBuilder(); 
                    Iterator<Integer> iter = cYears.getValue().iterator(); 
                    Integer from = iter.next();
                    Integer to = from;
                    Integer next = null;
                    do {
                        next = iter.hasNext() ? iter.next() : null;
                        // if consecutive year, then shift "to"
                        if (next != null && next - to == 1) {
                            to = next;
                        }
                        // add new range
                        if (next == null || next - to > 1) {
                            sb.append(sb.length() > 0 ? ", " : "")
                                .append(from == to ? from.toString() : String.format("%d-%d", from, to));
                            from = next;
                            to = next;
                        }
                    } while(next != null);
                    calCurrYears.put(cYears.getKey(), sb.toString());
                }
            }
            constantCurrencies.put(calEntry.getKey().getAmpFiscalCalId().toString(), calCurrYears);
        }
        
        return constantCurrencies;
    }

    /**
     * @see Currencies#saveConstantCurrencies(Map<String, Map<String, String>>)
     */
    public static void saveConstantCurrencies(Map<String, Map<String, String>> input) {
        ApiEMGroup errors = new ApiEMGroup();
        Map<AmpFiscalCalendar, Map<AmpCurrency, SortedSet<Integer>>> constantsInput = getConstantsInput(input, errors);
        
        if (errors.size() == 0) {
            Set<AmpCurrency> newConstantCurrencies = new HashSet<>();
            Session session = PersistenceManager.getSession();
            for (Entry<AmpFiscalCalendar, Map<AmpCurrency, SortedSet<Integer>>> calEntry : constantsInput.entrySet()) {
                for (Entry<AmpCurrency, SortedSet<Integer>> currEntry : calEntry.getValue().entrySet()) {
                    for (Integer year : currEntry.getValue()) {
                        ConstantCurrency cc = CurrencyInflationUtil.createOrActivateConstantCurrency(
                                currEntry.getKey(), calEntry.getKey(), year);
                        session.saveOrUpdate(cc.currency);
                        newConstantCurrencies.add(cc.currency);
                    }
                }
            }
            // delete old constant currencies
            List<AmpCurrency> oldConstantCurrencies = CurrencyInflationUtil.getConstantAmpCurrencies();
            oldConstantCurrencies.removeAll(newConstantCurrencies);
            for (AmpCurrency oldConstCurrency : oldConstantCurrencies) {
                CurrencyInflationUtil.deleteConstantCurrencies(oldConstCurrency);
            }
            // generate exchange rates for the new constant currencies
            CCExchangeRate.regenerateConstantCurrenciesExchangeRates(false);
        } else {
            throw new AmpWebApplicationException(Response.Status.BAD_REQUEST, ApiError.toError(errors));
        }
    }
    
    public static Map<AmpFiscalCalendar, Map<AmpCurrency, SortedSet<Integer>>> getConstantsInput(
            Map<String, Map<String, String>> input, ApiEMGroup errors) {
        
        Map<AmpFiscalCalendar, Map<AmpCurrency, SortedSet<Integer>>> constInput = new HashMap<AmpFiscalCalendar, 
                Map<AmpCurrency, SortedSet<Integer>>>();
        for (Entry<String, Map<String, String>> calEntry : input.entrySet()) {
            // validate calendar
            AmpFiscalCalendar calendar = !NumberUtils.isDigits(calEntry.getKey()) ? null :
                DbUtil.getAmpFiscalCalendar(Long.valueOf(calEntry.getKey()));
            if (calendar == null) {
                errors.addApiErrorMessage(CurrencyErrors.INVALID_CALENDAR_ID, calEntry.getKey());
            } else {
                // check if data was already provided for this calendar
                if (constInput.get(calendar) != null) {
                    errors.addApiErrorMessage(CurrencyErrors.DUPLICATE_CALENDAR, calEntry.getKey());
                } else {
                    Map<AmpCurrency, SortedSet<Integer>> currYears = new HashMap<>();
                    constInput.put(calendar, currYears);
                    for (Entry<String, String> sEntry : calEntry.getValue().entrySet()) {
                        String currCode = sEntry.getKey();
                        AmpCurrency standardCurrency = CurrencyUtil.getCurrencyByCode(currCode);
                        // verify if valid and unique currency input
                        ApiErrorMessage err = standardCurrency == null ? CurrencyErrors.INVALID_CURRENCY_CODE :
                            (currYears.containsKey(standardCurrency) ? CurrencyErrors.DUPLICATE_CURRENCY : null);
                        if (err != null) {
                            errors.addApiErrorMessage(err, String.format("%s: {...%s...}", calEntry.getKey(), currCode));
                        } else {
                            SortedSet<Integer> years = parseYears(sEntry.getValue().split(","), errors);
                            if (years.size() > 0) {
                                currYears.put(standardCurrency, years);
                            }
                        }
                    }
                }
            }
        }
        
        return constInput;
    }
    
    private static SortedSet<Integer> parseYears(String[] ranges, ApiEMGroup errors) {
        // will validate if there are duplicate years
        SortedSet<Integer> years = new TreeSet<Integer>();
        for (String range : ranges) {
            String[] fromTo = range.split("-");
            if (fromTo.length == 1) { // one year from list
                Integer year = getYear(fromTo[0], errors);
                if (year != null) {
                    addYear(year, years, errors);
                }
            } else if (fromTo.length == 2) {
                Integer from = getYear(fromTo[0], errors);
                Integer to = getYear(fromTo[1], errors);
                if (from != null && to != null && from <= to) {
                    for (int year = from; year <= to; year ++) {
                        addYear(year, years, errors);
                    }
                } else {
                    errors.addApiErrorMessage(CurrencyErrors.INVALID_PERIOD, range);
                }
            } else {
                errors.addApiErrorMessage(CurrencyErrors.INVALID_PERIOD, range);
            }
        }
        return years;
    }
    
    private static void addYear(Integer year, SortedSet<Integer> years, ApiEMGroup errors) {
        if (years.contains(year)) {
            errors.addApiErrorMessage(CurrencyErrors.DUPLICATE_YEAR, String.valueOf(year));
        } else {
            years.add(year);
        }
    }
    
    private static Integer getYear(String value, ApiEMGroup errors) {
        Integer year = null;
        value = value.trim();
        if (NumberUtils.isDigits(value)) {
            year = Integer.valueOf(value);
            if (year < AmpInflationRate.MIN_DEFLATION_YEAR || year > AmpInflationRate.MAX_DEFLATION_YEAR) {
                year = null;
            }
        }
        if (year == null) {
            errors.addApiErrorMessage(CurrencyErrors.INVALID_PERIOD, value);
        }
        return year;
    }

    /**
     * Returns exchange rates for all currency pairs.
     * @return list of rates grouped by currency pairs
     */
    public List<ExchangeRatesForPair> getExchangeRatesForPairs() {
        Collection<CurrencyRates> rates = CurrencyUtil.getAllCurrencyRates();
        return groupRatesByCurrencyPairs(rates);
    }

    public List<ExchangeRatesForPair> getExchangeRatesForPairs(List<Date> days) {
        Collection<CurrencyRates> rates = CurrencyUtil.getCurrencyRates(days, false);
        return groupRatesByCurrencyPairs(rates);
    }

    private List<ExchangeRatesForPair> groupRatesByCurrencyPairs(Collection<CurrencyRates> rates) {
        Map<CurrencyPair, List<CurrencyRates>> groupedRates = rates.stream().collect(groupingBy(this::currencyPair));

        return groupedRates.entrySet().stream()
                .map(e -> getExchangeRatesForPair(e.getKey(), e.getValue()))
                .collect(toList());
    }

    private ExchangeRatesForPair getExchangeRatesForPair(CurrencyPair pair, List<CurrencyRates> rates) {
        return new ExchangeRatesForPair(pair, rates.stream().map(this::convertRate).collect(toList()));
    }

    private ExchangeRate convertRate(CurrencyRates r) {
        return new ExchangeRate(r.getExchangeRateDateAsDate(), r.getExchangeRate());
    }

    private CurrencyPair currencyPair(CurrencyRates r) {
        return new CurrencyPair(r.getFromCurrencyCode(), r.getCurrencyCode());
    }
}
