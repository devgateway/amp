package org.digijava.module.currencyrates;


import org.apache.log4j.Logger;
import org.digijava.module.currencyrates.NET.webserviceX.www.Currency;
import org.digijava.module.currencyrates.NET.webserviceX.www.CurrencyConvertorLocator;
import org.digijava.module.currencyrates.NET.webserviceX.www.CurrencyConvertorSoap;

import javax.xml.rpc.ServiceException;
import java.rmi.RemoteException;
import java.util.HashMap;

/**
 * 
 * @author Marcelo Sotero
 * 
 */
public class WSCurrencyClientImp implements WSCurrencyClient {
    private static Logger logger = Logger
            .getLogger(WSCurrencyClientImp.class);
    private CurrencyConvertorSoap currencyConvertor;

    public WSCurrencyClientImp() {
        this.currencyConvertor = null;
        try {
            this.currencyConvertor = new CurrencyConvertorLocator()
                    .getCurrencyConvertorSoap();
        } catch (ServiceException e) {
            logger.error(e.getMessage(), e);
        }
    }
    public WSCurrencyClientImp(Integer minutes) {
        this.currencyConvertor = null;
        try {
            this.currencyConvertor = new MyCurrencyConvertorLocator()
                    .getCurrencyConvertorSoap(minutes);
        } catch (ServiceException e) {
            logger.error("Can't instantiate webservice stub",e);
            throw new RuntimeException(e);
        }
    }
    public HashMap<String, Double> getCRatesBasedUSD(String[] currencyCode) {
        HashMap<String, Double> values=null;
        try {
            values = getCurrencyRates(currencyCode, "USD");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return values;

    }
    public HashMap<String, Double> getCurrencyRates(String[] currencyCode, String baseCurrency) throws Exception{
        HashMap<String, Double> values = new HashMap<String, Double>();
        double rate;
        Currency base;
        try {               
            base = Currency.fromString(baseCurrency);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Base Currency to compare not supported by WS");
        }
        
        for (int i = 0; i < currencyCode.length; i++) {
            rate = 0.0;
            try {               
                Currency c = Currency.fromString(currencyCode[i]);
                if(c==base){//Due to the WS return 0 in this case.
                    rate = 1.0;
                }else{
                    rate = this.currencyConvertor.conversionRate(base, c);
                    logger.debug(this.currencyConvertor.conversionRate(c,base ));
                }
            } catch (IllegalArgumentException e) {
                rate = WSCurrencyClient.INVALID_CURRENCY_CODE;
                logger.error(e.getMessage(), e);
        
            } catch ( org.apache.axis.AxisFault af) {
                logger.error( "fault reason: " + af.getFaultReason() ,af);
                rate = WSCurrencyClient.CONNECTION_ERROR;
            } catch (RemoteException e) {
                rate = WSCurrencyClient.CONNECTION_ERROR;
                logger.error(e.getMessage(), e);
            }
            
            values.put(currencyCode[i], rate);
        }
        return values;
    }

    public Double getCurrencyRateBasedUSD(String codeCurrency) {
        double rate;
        try {
            Currency c = Currency.fromString(codeCurrency);
            rate = this.currencyConvertor.conversionRate(Currency.USD, c);
        } catch (IllegalArgumentException e) {
            rate = WSCurrencyClient.INVALID_CURRENCY_CODE;
            logger.error(e.getMessage(), e);
        } catch (RemoteException e) {
            rate = WSCurrencyClient.CONNECTION_ERROR;
            logger.error(e.getMessage(), e);
        }
        return rate;
    }
    @Override
    public void setUsername(String pUsername) {
        // TODO Auto-generated method stub
        
    }
    @Override
    public void setPassword(String pPassword) {
        // TODO Auto-generated method stub
        
    }
}
