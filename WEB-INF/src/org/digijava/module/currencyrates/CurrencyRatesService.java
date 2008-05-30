package org.digijava.module.currencyrates;

import java.util.Collection;

import org.digijava.kernel.service.AbstractServiceImpl;
import org.digijava.kernel.service.ServiceContext;
import org.digijava.kernel.service.ServiceException;
import org.digijava.module.aim.dbentity.AmpGlobalSettings;
import org.digijava.module.aim.util.FeaturesUtil;

public class CurrencyRatesService extends AbstractServiceImpl {

	@SuppressWarnings("unchecked")
	@Override
	protected void processInitEvent(ServiceContext serviceContext)
			throws ServiceException {
		String serviceEnabled = FeaturesUtil.getGlobalSettingValue("Enabled Daily Currency Rates Update");
		String ampmHour = FeaturesUtil.getGlobalSettingValue("Daily Currency Rates Update Hour");
		if(serviceEnabled!=null && "On".compareToIgnoreCase(serviceEnabled)==0 && ampmHour!=null){
			CurrencyRatesService.startCurrencyRatesService(ampmHour);
		}
	}
	/**
	 * @param hourampm
	 */
	public static void startCurrencyRatesService(String hourampm) {
		String[] hourminampm = hourampm.split(" ");
		String[] hourmin = hourminampm[0].split(":");
		String hour = hourmin[0];
		String min = hourmin[1];
		String ampm = hourminampm[1];
		DailyCurrencyRateSingleton dcrf = DailyCurrencyRateSingleton.getInstance();
		dcrf.start(Integer.parseInt(hour), Integer.parseInt(min), ampm);
	}
	public static void stopCurrencyRatesService(){
		DailyCurrencyRateSingleton.getInstance().stop();
	}
	@Override
	public String toString() {
		return super.toString();
	}

}
