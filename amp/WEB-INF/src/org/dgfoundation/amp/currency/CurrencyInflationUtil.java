/**
 * 
 */
package org.dgfoundation.amp.currency;

import java.util.List;

import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpInflationRate;
import org.digijava.module.aim.dbentity.AmpInflationSource;

/**
 * Common currency inflation rates utility methods
 * 
 * @author Nadejda Mandrescu
 */
public class CurrencyInflationUtil {
	/*
	public static ConstantCurrency getConstantCurrency(String currencyName) {
		// TODO:
		return null;
	}
	
	public static int getConstantCurrencyYear(String currencyName) {
		
	}*/
	
	public static List<AmpInflationSource> getInflationDataSources() {
		return PersistenceManager.getRequestDBSession().createQuery(
				"select o from " +  AmpInflationSource.class.getName() + " o").list();
	}
	
	public static List<AmpInflationRate> getInflationRates() {
		return PersistenceManager.getRequestDBSession().createQuery(
				"select o from " +  AmpInflationRate.class.getName() + " o").list();
	}
}
