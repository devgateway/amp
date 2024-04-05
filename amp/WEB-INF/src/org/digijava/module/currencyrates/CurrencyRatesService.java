package org.digijava.module.currencyrates;

import org.digijava.kernel.service.AbstractServiceImpl;
import org.digijava.kernel.service.ServiceContext;
import org.digijava.kernel.service.ServiceException;
import org.digijava.module.aim.dbentity.AmpCurrencyRate;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.common.util.DateTimeUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

public class CurrencyRatesService extends AbstractServiceImpl {

    protected void processInitEvent(ServiceContext serviceContext)
            throws ServiceException {
        String serviceEnabled = FeaturesUtil
                .getGlobalSettingValue(GlobalSettingsConstants.DAILY_CURRENCY_RATES_UPDATE_ENALBLED);
        String ampmHour = FeaturesUtil
                .getGlobalSettingValue(GlobalSettingsConstants.DAILY_CURRENCY_RATES_UPDATE_HOUR);
        String timeout = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DAILY_CURRENCY_RATES_UPDATE_TIMEOUT);
        if (serviceEnabled != null
                && "On".compareToIgnoreCase(serviceEnabled) == 0
                && ampmHour != null) {
            CurrencyRatesService.startCurrencyRatesService(ampmHour, timeout);
        }
    }

    /**
     * @param hourampm
     */
    public static void startCurrencyRatesService(String hourampm, String timeout) {
        String[] hourminampm = hourampm.split(" ");
        String[] hourmin = hourminampm[0].split(":");
        String hour = hourmin[0];
        String min = hourmin[1];
        String ampm = hourminampm[1];
        DailyCurrencyRateSingleton dcrf = DailyCurrencyRateSingleton
                .getInstance();
        
        dcrf.setTimeout(Integer.parseInt(timeout));
        dcrf.start(Integer.parseInt(hour), Integer.parseInt(min), ampm);
    }

    public static void stopCurrencyRatesService() {
        DailyCurrencyRateSingleton.getInstance().stop();
    }

    @SuppressWarnings("unchecked")
    public static String getStringLastTimeUpdate(int mode) {
        DateFormat formatter = new SimpleDateFormat("hh:mm:ss a");
        String date = null;
        if (mode == CurrencyUtil.RATE_FROM_WEB_SERVICE) {
            Date wsdate = DailyCurrencyRateSingleton.getInstance()
                    .getLastExcecution();
            if (wsdate == null) {
                Collection<AmpCurrencyRate> col = CurrencyUtil
                        .getCurrencyRateByDataSource(CurrencyUtil.RATE_FROM_WEB_SERVICE);
                if (col != null && col.size()>0) {
                    AmpCurrencyRate mylast = null;
                    Date dbdate = null;
                    for (AmpCurrencyRate last : col) {
                        mylast = last;
                    }
                    dbdate = mylast.getExchangeRateDate();

                    date = DateTimeUtil.formatDate(dbdate);
                } else {
                    return null;
                }
            } else {
                date = DateTimeUtil.formatDate(wsdate) + " "
                        + formatter.format(wsdate);
            }
        }
        return date;
    }

    @Override
    public String toString() {
        return super.toString();
    }

}
