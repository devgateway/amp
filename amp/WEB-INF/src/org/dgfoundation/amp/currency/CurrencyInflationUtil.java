/**
 * 
 */
package org.dgfoundation.amp.currency;

import java.util.List;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpInflationRate;
import org.digijava.module.aim.dbentity.AmpInflationSource;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;

/**
 * Common currency inflation rates utility methods
 * 
 * @author Nadejda Mandrescu
 */
public class CurrencyInflationUtil {
	protected static final Logger logger = Logger.getLogger(CurrencyInflationUtil.class);
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
	
	public static AmpInflationSource getInflationDataSource(Long sourceId) {
		try {
			return (AmpInflationSource) PersistenceManager.getRequestDBSession().load(AmpInflationSource.class, sourceId);
		} catch (ObjectNotFoundException e) {
			logger.error(e.getMessage());
			return null;
		}
	}
	
	public static List<AmpInflationRate> getInflationRates() {
		return PersistenceManager.getRequestDBSession().createQuery(
				"select o from " +  AmpInflationRate.class.getName() + " o").list();
	}
	
	public static void deleteAllInflationRates() {
		Session session = PersistenceManager.getRequestDBSession();
		for (AmpInflationRate air : getInflationRates()) {
			session.delete(air);
		}
		session.flush();
	}
}
