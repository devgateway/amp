
package org.digijava.module.currencyrates;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.currency.inflation.CCExchangeRate;
import org.digijava.kernel.mail.DgEmailManager;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpCurrencyRate;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.FilteredCurrencyRateUtil;
import org.digijava.module.calendar.util.AmpDbUtil;
import org.digijava.module.common.util.DateTimeUtil;
import org.digijava.module.message.jobs.ConnectionCleaningJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.*;

/**
 * 
 * @author Marcelo Sotero
 * 
 */
public class CurrencyRatesQuartzJob extends ConnectionCleaningJob {
    private static Logger logger = Logger.getLogger(CurrencyRatesQuartzJob.class);
    private WSCurrencyClient myWSCurrencyClient;
    private String baseCurrency;
    private Date lastExcecution;
    private static final int tries = 3;

    public CurrencyRatesQuartzJob() {
        //check wheter we should reinstantiate the webservice client(if it has changed since las instantiation)
        String className=
        FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.CURRENCY_WS_CLASS);
        if(DailyCurrencyRateSingleton.getInstance()
                .getMyWSCurrencyClient()==null || 
                !DailyCurrencyRateSingleton.getInstance().getMyWSCurrencyClient().getClass().getCanonicalName().equals(className)){
            //if the instance is null or if it has changed(via globalsettings) we instantiate again
            DailyCurrencyRateSingleton.getInstance().createWebserviceImplementationInstance();
        }
        myWSCurrencyClient = DailyCurrencyRateSingleton.getInstance()
                .getMyWSCurrencyClient();
        this.baseCurrency = DailyCurrencyRateSingleton.getInstance()
                .getBaseCurrency();
    }

    public void executeTest(JobExecutionContext context)
            throws JobExecutionException {
        logger
                .info("START Getting Currencies Rates from WS.............................");

    }
    
    @Override
    public void executeInternal(JobExecutionContext context) throws JobExecutionException {
        //we check for username and password stored on GlobalSettigs 
        this.myWSCurrencyClient.setUsername(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.CURRENCY_WS_USERNAME));
        this.myWSCurrencyClient.setPassword(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.CURRENCY_WS_PASSWORD));
        logger.info("START Getting Currencies Rates from WS.............................");

        Collection<AmpCurrency> currencies = CurrencyUtil.getAllCurrencies(CurrencyUtil.ORDER_BY_CURRENCY_CODE);

        String[] ampCurrencies = this.getCurrencies(currencies);
        HashMap<String, Double> wsCurrencyValues = null;
        
        /* Remove currencies that need to skipped in automatic update */
        if ( ampCurrencies != null ) {
                FilteredCurrencyRateUtil filteredCurrencyUtil       = new FilteredCurrencyRateUtil();
                List<String> remainingCurrencies                                = new ArrayList<String>();
                for (int i=0; i < ampCurrencies.length; i ++) {
                    if (filteredCurrencyUtil.checkPairExistanceUsingCache(ampCurrencies[i], this.baseCurrency) || 
                            filteredCurrencyUtil.checkPairExistanceUsingCache(this.baseCurrency, ampCurrencies[i]) ) {
                        logger.info("Skipping update for currency " + ampCurrencies[i] + ". Base currency beeing: " + this.baseCurrency );
                        continue;
                    }
                    else
                        remainingCurrencies.add( ampCurrencies[i] );
                }
                ampCurrencies               = remainingCurrencies.toArray( new String[0] );
        }
        /* END - Remove currencies that need to skipped in automatic update */

        try {
            
            int mytries = 1;            
            while (mytries <= tries && ampCurrencies!=null) {
                logger.info("Attempt.........................." + mytries);
                wsCurrencyValues = this.myWSCurrencyClient.getCurrencyRates(ampCurrencies, baseCurrency);
                
                showValues(ampCurrencies, wsCurrencyValues);
                save(ampCurrencies, wsCurrencyValues);
                ampCurrencies = this.getWrongCurrencies(ampCurrencies,
                        wsCurrencyValues);
                mytries ++;
            }
            this.lastExcecution = new Date();
            DailyCurrencyRateSingleton.getInstance().setLastExcecution(this.lastExcecution);
            /* 
             * fully regenerate Constant Currencies exchange rate, because our exchange rates are not continuous
             * and thus we could have used worse exchange rates previously available to calculate an ER for a CC 
             */
            CCExchangeRate.regenerateConstantCurrenciesExchangeRates(true);

        } catch (Exception e) {
            logger.error("Could not get exchange rate, caused by: " + e.getMessage());
        }
        if(ampCurrencies!=null){
            sendEmailToAdmin();
        }
        logger.info("END Getting Currencies Rates from WS.............................");
    }

    private String[] getWrongCurrencies(String[] currencies,
            HashMap<String, Double> wsCurrencyValues) {
        String[] wrongArray = null;
        ArrayList<AmpCurrencyRate> wrongAList = new ArrayList<AmpCurrencyRate>();
        for (int i=0; i<currencies.length; i++) {
            AmpCurrencyRate currRate = new AmpCurrencyRate();
            currRate.setToCurrencyCode(currencies[i]);
            double value = wsCurrencyValues.get(currencies[i]);
            if (value == WSCurrencyClient.CONNECTION_ERROR) {
                wrongAList.add(currRate);
            }
        }
        if (wrongAList.size() != 0) {
            wrongArray = new String[wrongAList.size()];
            int i = 0;
            for (AmpCurrencyRate ampCurrency : wrongAList) {
                wrongArray[i++] = ampCurrency.getToCurrencyCode();
            }
        }
        return wrongArray;
    }
    private void save(String[] currencies,
            HashMap<String, Double> wsCurrencyValues) {
        for (int i=0; i < currencies.length; i++) {
            if (baseCurrency != null && currencies[i] != null && !currencies[i].equals(baseCurrency)) {
                AmpCurrencyRate currRate = new AmpCurrencyRate();
                //currRate.setAmpCurrencyRateId(ampCurrency.getAmpCurrencyId());
                Double value = wsCurrencyValues.get(currencies[i]);
                if (value!=null && value .equals(WSCurrencyClient.INVALID_CURRENCY_CODE) && value >0D) {
                    logger.info(currencies[i]+ " Not Supported...");
                    continue;
                } else if (value == WSCurrencyClient.CONNECTION_ERROR) {
                    logger.info("Connection Error trying to get "+ currencies[i]);
                    continue;
                }else{
                    if (value == null || value <= 0D){
                        logger.info("0 rate for  "+ currencies[i]);
                        continue;
                    }
                }               
                
                currRate.setExchangeRate(value);
                Date aDate = new Date();
                try {
                    String sDate = DateTimeUtil.formatDate(aDate);
                    aDate = DateTimeUtil.parseDate(sDate);
                } catch (Exception e) {
                    //this should never get here
                    logger.error(e.getMessage(), e);
                }
                
                currRate.setExchangeRateDate(aDate);
                currRate.setToCurrencyCode(currencies[i]);
                currRate.setFromCurrencyCode(baseCurrency);
                currRate.setDataSource(CurrencyUtil.RATE_FROM_WEB_SERVICE);
                CurrencyUtil.saveCurrencyRate(currRate, true);
            }
        }
    }

    private String[] getCurrencies(Collection<AmpCurrency> currencies) {
        String[] curr = new String[currencies.size()];
        int i = 0;
        for (AmpCurrency ampCurrency : currencies) {
            curr[i] = ampCurrency.getCurrencyCode().trim();
            i++;
        }
        return curr;
    }

    private void showValues(String[] curr,
            HashMap<String, Double> currencyValues) {
        for (int i = 0; i < curr.length; i++) {
            logger.info(curr[i].trim() + ": "
                    + currencyValues.get(curr[i].trim()));
        }
    }

    void sendEmailToAdmin(){
        try {
            Collection<User> users = (List <User>)AmpDbUtil.getUsers();
            for(User user:users){
                if(user.isGlobalAdmin()){
                    logger.info("An email has been sent to " + user.getFirstNames() + " " + user.getLastName() + "-"
                            + user.getEmailUsedForNotification());
                    DgEmailManager.sendMail(user.getEmailUsedForNotification(), 
                            "Daily Currency Rates Update ERROR",
                            "Please, check your internet connection and Timeout at Admin Tools > Global Settings "
                            + ">Timeout Daily Currency Update.");
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        logger.info("There were connection error trying to update currencies");
    }
}
