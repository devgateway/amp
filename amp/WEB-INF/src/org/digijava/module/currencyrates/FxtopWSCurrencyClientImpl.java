package org.digijava.module.currencyrates;

import org.apache.log4j.Logger;
import org.digijava.module.aim.helper.DecimalToText;
import org.digijava.module.currencyrates.fxtop.ConvertResult;
import org.digijava.module.currencyrates.fxtop.FxtopServicesLocator;
import org.digijava.module.currencyrates.fxtop.FxtopServicesPortType;

import javax.xml.rpc.ServiceException;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class FxtopWSCurrencyClientImpl implements WSCurrencyClient {
    private static Logger logger = Logger
            .getLogger(FxtopWSCurrencyClientImpl.class);
    private Set<String> supportedCurrencies;
    private String username;
    private String password;
    private FxtopServicesPortType currencyConvertor;

    public FxtopWSCurrencyClientImpl() {
        this(null);
    }

    public FxtopWSCurrencyClientImpl(Integer minutes) {
        this.currencyConvertor = null;
        try {
            currencyConvertor = new FxtopServicesLocator()
                    .getFxtopServicesPort(minutes);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public HashMap<String, Double> getCRatesBasedUSD(String[] currencyCode) {
        throw new RuntimeException("Method not implemented");
    }

    public Set<String> getSupportedCurrencies() {
        if (supportedCurrencies == null || supportedCurrencies.size() == 0) {
            // we initate the list
            try {
                // we will get a list of supported currencies and store their
                // ISO country codes to a string array
                supportedCurrencies = new HashSet<String>();
                String supported = currencyConvertor.listCurrencies(username,
                        password);
                if (supported != null) {
                    supportedCurrencies = new HashSet<String>(
                            Arrays.asList(supported.split("/")));
                } else {
                    supportedCurrencies = new HashSet<String>();
                }
            } catch (RemoteException e) {
                logger.error("Cannot access supported currencies", e);
                throw new RuntimeException(e);
            }
        }
        return supportedCurrencies;
    }

    @Override
    public HashMap<String, Double> getCurrencyRates(String[] currencyCode,
            String baseCurrency) throws Exception {
        HashMap<String, Double> values = new HashMap<String, Double>();
        double rate;
        // check wheter we can check if the currency is supported by ws
        if (!getSupportedCurrencies().contains(baseCurrency)) {
            throw new IllegalArgumentException(
                    "Base Currency to compare not supported by WS");
        }

        for (int i = 0; i < currencyCode.length; i++) {
            rate = 0.0;
            try {

                if (currencyCode[i].endsWith(baseCurrency)) {
                    rate = 1.0;
                } else {
                    ConvertResult result = currencyConvertor.convert("1",
                            baseCurrency, currencyCode[i], null, username,
                            password);
                    rate = DecimalToText.getDouble(result.getExchangeRate());
                }
            } catch (IllegalArgumentException e) {
                rate = WSCurrencyClient.INVALID_CURRENCY_CODE;
                logger.error(e.getMessage(), e);
            } catch (org.apache.axis.AxisFault af) {
                logger.error("AxisFault error to access currency "
                        + currencyCode[i], af);
                rate = WSCurrencyClient.CONNECTION_ERROR;
            } catch (RemoteException e) {
                logger.error("Connection error to access currency "
                        + currencyCode[i], e);
                rate = WSCurrencyClient.CONNECTION_ERROR;
            }
            values.put(currencyCode[i], rate);
        }
        return values;
    }

    @Override
    public Double getCurrencyRateBasedUSD(String codeCurrency) {
        throw new RuntimeException("Method not implemented");
    }

}
