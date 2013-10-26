/*
 * CurrencyUtil.java
 * Created: 01-May-2005
 */
package org.digijava.module.aim.util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.dgfoundation.amp.onepager.util.SessionUtil;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.util.DigiCacheManager;
import org.digijava.module.aim.dbentity.AmpColumns;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpCurrencyRate;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.Currency;
import org.digijava.module.aim.helper.CurrencyRates;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.caching.AmpCaching;
import org.hibernate.Hibernate;
import org.hibernate.JDBCException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class CurrencyUtil {

	private static Logger logger = Logger.getLogger(CurrencyUtil.class);
	public static DecimalFormat df = new DecimalFormat("###,###,###,###,###") ;

	public static final int RATE_FROM_FILE					= 0;
	public static final int RATE_FROM_WEB_SERVICE			= 1;
	public static final int RATE_BY_HAND					= 2;

    public static final int ORDER_BY_CURRENCY_CODE			=-1;
    public static final int ORDER_BY_CURRENCY_NAME			= 2;
    public static final int ORDER_BY_CURRENCY_COUNTRY_NAME	= 3;
    public static final int ALL_ACTIVE						= 1;
    public static final String BASE_CODE                    = "USD";


	public static Collection getAllActiveRates() {
		Collection col = new ArrayList();
		Session session = null;
		String qryStr = null;
		Query qry = null;

		try {
			session = PersistenceManager.getSession();
			qryStr = "select currency from " + AmpCurrency.class.getName() + "" +
					" currency where currency.activeFlag='1'";
			qry = session.createQuery(qryStr);
			Collection res = qry.list();
			if (res.size() > 0) {
				logger.debug("Active currencies found");
				Iterator<AmpCurrency> itr = res.iterator();
				HashMap<String, String> currencies = new HashMap<String, String>( res.size() );
				while (itr.hasNext()) {
					AmpCurrency curr		= itr.next();
					currencies.put( curr.getCurrencyCode(), curr.getCurrencyName() ) ;
				}
				qryStr = "select cRate from " + AmpCurrencyRate.class.getName() + " cRate " +
				"where cRate.toCurrencyCode in (";
				Set<String> currencyCodes	= currencies.keySet();
				if ( currencyCodes != null && currencyCodes.size() > 0 ) {
					for ( String currencyCode: currencyCodes) {
						qryStr += "'" + currencyCode + "'" + ",";
					}
					qryStr	= qryStr.substring(0, qryStr.length()-1) ;
				}
				
				qryStr += ") order by cRate.exchangeRateDate desc,cRate.toCurrencyCode";
				qry = session.createQuery(qryStr);
				Iterator<AmpCurrencyRate> itr2 = qry.list().iterator();
				AmpCurrencyRate cRate = null;

				CurrencyRates currencyRates = null;
				while (itr2.hasNext()) {
					currencyRates = new CurrencyRates();
					cRate = (AmpCurrencyRate) itr2.next();

					currencyRates.setCurrencyCode(cRate.getToCurrencyCode());
					currencyRates.setFromCurrencyCode(cRate.getFromCurrencyCode());
					currencyRates.setCurrencyName( currencies.get(cRate.getToCurrencyCode()) );
					currencyRates.setFromCurrencyName( currencies.get(cRate.getFromCurrencyCode()) );
					currencyRates.setExchangeRate(cRate.getExchangeRate());
					currencyRates.setExchangeRateDate(DateConversion.
							ConvertDateToString(cRate.getExchangeRateDate()));
					currencyRates.setId(cRate.getAmpCurrencyRateId());
					col.add(currencyRates);
				}

			}

		} catch (Exception e) {
			logger.error("Exception from getAllActiveRates");
			e.printStackTrace(System.out);
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed");
				}
			}
		}

		logger.info("returning a collection of size get all active rates function" + col.size());
		return col;
	}

	public static Collection getActiveRates(Date fromDate,Date toDate) {
		Collection col = new ArrayList();
		Session session = null;
		String qryStr = null;
		Query qry = null;

		try {
			session = PersistenceManager.getSession();
			qryStr = "select currency from " + AmpCurrency.class.getName() + "" +
					" currency where currency.activeFlag='1'";
			qry = session.createQuery(qryStr);
			Collection res = qry.list();
			if (res.size() > 0) {
				logger.debug("Active currencies found");
				Iterator<AmpCurrency> itr = res.iterator();
				HashMap<String, String> currencies = new HashMap<String, String>( res.size() );
				while (itr.hasNext()) {
					AmpCurrency curr		= itr.next();
					currencies.put( curr.getCurrencyCode(), curr.getCurrencyName() ) ;
				}
				qryStr = "select cRate from " + AmpCurrencyRate.class.getName() + " cRate " +
				"where cRate.toCurrencyCode in (";
				Set<String> currencyCodes	= currencies.keySet();
				if ( currencyCodes != null && currencyCodes.size() > 0 ) {
					for ( String currencyCode: currencyCodes) {
						qryStr += "'" + currencyCode + "'" + ",";
					}
					qryStr	= qryStr.substring(0, qryStr.length()-1) ;
				}
				qryStr += ") and cRate.exchangeRateDate between :fromDate and :toDate order by " +
						"cRate.exchangeRateDate desc,cRate.toCurrencyCode";
				qry = session.createQuery(qryStr);
				qry.setParameter("fromDate",fromDate,Hibernate.DATE);
				qry.setParameter("toDate",toDate,Hibernate.DATE);

				Iterator <AmpCurrencyRate> itr2 = qry.list().iterator();
				AmpCurrencyRate cRate = null;

				CurrencyRates currencyRates = null;
				while (itr2.hasNext()) {
					currencyRates = new CurrencyRates();
					cRate = (AmpCurrencyRate) itr2.next();

					currencyRates.setId(cRate.getAmpCurrencyRateId());
					currencyRates.setCurrencyCode(cRate.getToCurrencyCode());
					currencyRates.setFromCurrencyCode(cRate.getFromCurrencyCode());
					currencyRates.setCurrencyName( currencies.get(cRate.getToCurrencyCode()) );
					currencyRates.setFromCurrencyName( currencies.get(cRate.getFromCurrencyCode()) );
					currencyRates.setExchangeRate(cRate.getExchangeRate());
					currencyRates.setExchangeRateDate(DateConversion.
							ConvertDateToString(cRate.getExchangeRateDate()));
					currencyRates.setId(cRate.getAmpCurrencyRateId());
					col.add(currencyRates);
				}

			}

		} catch (Exception e) {
			logger.error("Exception from getActiveRates");
			e.printStackTrace(System.out);
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed");
				}
			}
		}

		logger.info("returning a collection of size...get active rates... " + col.size());
		return col;
	}
//	/**
//	 * AMP-2805
//	 * @deprecated use Util.getExchange(currency, currencyDate)
//	 * @param currCode
//	 * @param date
//	 * @return
//	 */
//	public static Double getExchangeRate(String currCode,Date date) {
//		Double exchRate = null;
//		Session session = null;
//		Query qry = null;
//		String qryStr = null;
//
//		try {
//			session = PersistenceManager.getSession();
//			qryStr = "select cr.exchangeRate from " + AmpCurrencyRate.class.getName() + "" +
//					" cr where (cr.toCurrencyCode=:code) and " +
//					"(cr.exchangeRateDate=:date)";
//			qry = session.createQuery(qryStr);
//			qry.setParameter("code",currCode,Hibernate.STRING);
//			qry.setParameter("date",date,Hibernate.DATE);
//
//			Iterator itr = qry.list().iterator();
//			if (itr.hasNext()) {
//				exchRate = (Double) itr.next();
//			}
//		} catch (Exception e) {
//			logger.error("Exception from getExchangeRate()");
//			e.printStackTrace(System.out);
//		} finally {
//			if (session != null) {
//				try {
//					PersistenceManager.releaseSession(session);
//				} catch (Exception rsf) {
//					logger.error("Release session failed");
//				}
//			}
//		}
//
//		return exchRate;
//	}

	/**
	 * Saves an AmpCurrencyRate object to the database
	 * @param cRate The AmpCurrencyRate object
	 */
	public static void saveCurrencyRate(AmpCurrencyRate cRate, boolean calledFromQuartzJob) {
		Session session = null;
		Transaction tx = null;
		Query qry = null;
		String qryStr = null;
		if (!calledFromQuartzJob)
		{
			AmpCaching.getInstance().currencyCache.reset();
		}

		try {
			//session = PersistenceManager.getSession();
            session = PersistenceManager.getCurrentSession();
            tx = session.beginTransaction();

			qryStr = "select cRate from " + AmpCurrencyRate.class.getName() + " cRate " +
					"where (cRate.toCurrencyCode=:code) and (cRate.fromCurrencyCode=:fromCode) and" +
					"(cRate.exchangeRateDate=:date)";
			qry = session.createQuery(qryStr);
			qry.setParameter("code",cRate.getToCurrencyCode(),Hibernate.STRING);
			qry.setParameter("fromCode",cRate.getFromCurrencyCode(),Hibernate.STRING);
			qry.setParameter("date",cRate.getExchangeRateDate(),Hibernate.DATE);

			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				// if the currency rate already exist update the rate
				AmpCurrencyRate actRate = (AmpCurrencyRate) itr.next();
				actRate.setExchangeRate(cRate.getExchangeRate());
				//System.out.println("updating......................");
				session.update(actRate);
			} else {
				// add the currency rate object if it does not exist
				//System.out.println("saving......................");
				session.save(cRate);
			}

			tx.commit();
		} catch (Exception e) {
			logger.error("Exception from saveCurrencyRate");
			e.printStackTrace(System.out);
			if (tx != null) {
				try {
					tx.rollback();
				} catch (Exception rbf) {
					logger.error("Rollback failed");
				}
			}
		} finally {
			if (session != null) {
				try {
					//PersistenceManager.releaseSession(session);
                    if(session.isOpen()) session.close();
				} catch (Exception rsf) {
					logger.error("Release session failed");
				}
			}
		}
	}

    public static Collection<AmpCurrency> getAllCurrencies(int active){
        return getAllCurrencies(active, "");
    }

	public static Collection<AmpCurrency> getAllCurrencies(int active,String sortOrder) {
		Collection<AmpCurrency> col = new ArrayList<AmpCurrency>();
		Session session = null;
		Query qry = null;
		String qryStr = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			if (active == CurrencyUtil.ORDER_BY_CURRENCY_CODE) {
				qryStr = "select curr from " + AmpCurrency.class.getName() + " as curr left join fetch  curr.countryLocation dg order by curr.currencyCode "+sortOrder;
				qry = session.createQuery(qryStr);
			}else if(active == CurrencyUtil.ORDER_BY_CURRENCY_NAME){
				qryStr = "select curr from " + AmpCurrency.class.getName() + " as curr left join fetch  curr.countryLocation dg order by curr.currencyName "+sortOrder;
			qry = session.createQuery(qryStr);
			}else if(active == CurrencyUtil.ORDER_BY_CURRENCY_COUNTRY_NAME){
				qryStr = "select curr from " + AmpCurrency.class.getName() + " as curr left outer join curr.countryLocation dg order by dg.name "+sortOrder;
			qry = session.createQuery(qryStr);
			}else {
				qryStr = "select curr from " + AmpCurrency.class.getName() + " curr " +
					"where (curr.activeFlag=:flag) order by curr.currencyCode "+sortOrder;
				qry = session.createQuery(qryStr);
				qry.setParameter("flag",new Integer(active),Hibernate.INTEGER);
			}
			col = qry.list();
		} catch (Exception e) {
			logger.error("Exception from getAllCurrencies()");
			e.printStackTrace(System.out);
		} 

		return col;
	}

	public static void updateCurrencyStatus(String code,int status) {
		Session session = null;
		Query qry = null;
		String qryStr = null;
		Transaction tx = null;
		AmpCaching.getInstance().currencyCache.reset();

		try {
			session = PersistenceManager.getSession();
			qryStr = "select curr from " + AmpCurrency.class.getName() + " curr " +
					"where (curr.currencyCode=:code)";
			qry = session.createQuery(qryStr);
			qry.setParameter("code",code,Hibernate.STRING);
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
			if (tx != null) {
				try {
					tx.rollback();
				} catch (Exception rbf) {
					logger.error("Rollback failed");
				}
			}
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed");
				}
			}
		}
	}

	public static boolean currencyCodeExist(String currCode,Long id) {
		Session session = null;
		Query qry = null;
		String qryStr = null;
		boolean exist = false;

		try {
			session = PersistenceManager.getSession();
			qryStr = "select count(*) from " + AmpCurrency.class.getName() + " c where " +
					"(c.currencyCode=:code)";
			if (id != null) {
				qryStr += " and (c.ampCurrencyId!=:id)";

			}
			qry = session.createQuery(qryStr);
			qry.setParameter("code",currCode,Hibernate.STRING);
			if (id != null) {
				qry.setParameter("id",id,Hibernate.LONG);
			}
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				Integer count = (Integer) itr.next();
				if (count.intValue() > 0) {
					exist = true;
				}
			}

		} catch (Exception e) {
			logger.error("Exception from currencyCodeExist()");
			e.printStackTrace(System.out);
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed");
				}
			}
		}
		return exist;
	}

	public static void saveCurrency(AmpCurrency curr) {
		Session session = null;
		Transaction tx = null;
		AmpCaching.getInstance().currencyCache.reset();

		try {
			session = PersistenceManager.getSession();
//beginTransaction();
			session.save(curr);
			//tx.commit();

		} catch (Exception e) {
			logger.error("Exception from saveCurrency");
			e.printStackTrace(System.out);
			if (tx != null) {
				try {
					tx.rollback();
				} catch (Exception rbf) {
					logger.error("Rollback failed");
				}
			}
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed");
				}
			}
		}
	}
	public static void updateCurrency(AmpCurrency curr) {
		Session session = null;
		Transaction tx = null;

		try {
			AmpCaching.getInstance().currencyCache.reset();
			session = PersistenceManager.getSession();
//beginTransaction();
			session.update(curr);
			//tx.commit();

		} catch (Exception e) {
			logger.error("Exception from saveCurrency");
			e.printStackTrace(System.out);
			if (tx != null) {
				try {
					tx.rollback();
				} catch (Exception rbf) {
					logger.error("Rollback failed");
				}
			}
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed");
				}
			}
		}
	}


	/**
	 * Save an AmpCurrency object
	 * @param currency The AmpCurrency Object to be saved
	 * @param cRate The initial currency rates in an AmpCurrencyRate object
	 */
	public static void saveCurrency(AmpCurrency currency,AmpCurrencyRate cRate) {
		AmpCaching.getInstance().currencyCache.reset();
		Session session = null;
		Query qry = null;
		String qryStr = null;
		Transaction tx = null;

		try {
			session = PersistenceManager.getSession();
			qryStr = "select curr from " + AmpCurrency.class.getName() + " curr " +
					"where (curr.ampCurrencyId=:id)";
			qry = session.createQuery(qryStr);
			logger.debug("Checking with the id " + currency.getAmpCurrencyId());
			qry.setParameter("id",currency.getAmpCurrencyId(),Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				// currency object already exist, update the object
				logger.debug("Updating the existing currency id ...");
				AmpCurrency curr = (AmpCurrency) itr.next();
				curr.setCountryName(currency.getCountryName());
				curr.setCurrencyCode(currency.getCurrencyCode());
				curr.setCurrencyName(currency.getCurrencyName());
                curr.setCountryLocation(currency.getCountryLocation());
//beginTransaction();
				session.update(curr);
				//tx.commit();
			} else {
				logger.debug("Creating new currency id ...");
//beginTransaction();
				session.save(currency);
				session.save(cRate);
				//tx.commit();
			}
		} catch (Exception e) {
			logger.error("Exception from saveCurrency");
			e.printStackTrace(System.out);
			if (tx != null) {
				try {
					tx.rollback();
				} catch (Exception rbf) {
					logger.error("Rollback failed");
				}
			}
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed");
				}
			}
		}
	}

	public static void deleteCurrencyRates(Long cRates[]) {
		AmpCaching.getInstance().currencyCache.reset();
		Session session = null;
		Transaction tx = null;

		try {
			session = PersistenceManager.getSession();
//beginTransaction();
			for (int i = 0;i < cRates.length; i++) {
				if (cRates[i] != null) {
					AmpCurrencyRate cRate = (AmpCurrencyRate) session.load(AmpCurrencyRate.class,cRates[i]);
					session.delete(cRate);
				}
			}
			//tx.commit();
		} catch (Exception e) {
			logger.error("Exception from deleteCurrencyRates");
			e.printStackTrace(System.out);
			if (tx != null) {
				try {
					tx.rollback();
				} catch (Exception rbf) {
					logger.error("Rollback failed");
				}
			}
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed");
				}
			}
		}
	}

	/**
	 * Saves currency rates to the database
	 * @param currRates Collection of CurrencyRates object which need
	 * to be saved
	 * @throws Exception 
	 */
	public static void saveCurrencyRates(Collection currRates, String baseCurrencyCode) throws Exception {
		Session session = null;
		Query qry = null;
		String qryStr = null;
		Transaction tx = null;
		AmpCaching.getInstance().currencyCache.reset();
		
		try {
			session = PersistenceManager.getSession();
//beginTransaction();

			Iterator itr = currRates.iterator();
			logger.debug("currency rates size :" + currRates.size());
			while (itr.hasNext()) {
				CurrencyRates cr = (CurrencyRates) itr.next();
				qryStr = "select crate from " + AmpCurrencyRate.class.getName() +
					" crate where (crate.toCurrencyCode=:code) and (crate.fromCurrencyCode=:fromCurrencyCode) and " +
					"(crate.exchangeRateDate=:date)";
				qry = session.createQuery(qryStr);
				qry.setParameter("code",cr.getCurrencyCode(),Hibernate.STRING);
				qry.setParameter("fromCurrencyCode", baseCurrencyCode, Hibernate.STRING);
				Date exRtDate = DateConversion.getDate(cr.getExchangeRateDate());
				qry.setParameter("date",exRtDate,Hibernate.DATE);
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
					currencyRate.setFromCurrencyCode( baseCurrencyCode );
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
			
			if (tx != null) {
				try {
					tx.rollback();
					
				} catch (Exception rbf) {
					logger.error("Rollback failed");
				}
			}
			throw e;
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
					
				} catch (Exception rsf) {
					logger.error("Release session failed");
				}
			}
		}
	}

	public static void addCurrency(AmpCurrency ampCurr,
			AmpCurrencyRate ampCurrRate) throws AimException {
		DbUtil.add(ampCurr);
		DbUtil.add(ampCurrRate);
		AmpCaching.getInstance().currencyCache.reset();
	}

	public static void deleteCurrency(AmpCurrency ampCurr,
			AmpCurrencyRate ampCurrRate) {
		try {
			AmpCaching.getInstance().currencyCache.reset();
			DbUtil.delete(ampCurr);
			DbUtil.delete(ampCurrRate);
		} catch (JDBCException e) {
			// TODO Auto-generated catch block
			logger.error(e);
		}
	}

	public static void updateCurrency(AmpCurrency ampCurr,
			AmpCurrencyRate ampCurrRate) {
		AmpCaching.getInstance().currencyCache.reset();
		DbUtil.update(ampCurr);
		DbUtil.update(ampCurrRate);
	}

	public static AmpCurrency getCurrencyByCode(String currCode) {
		Session session = null;
		Query qry = null;
		AmpCurrency ampCurrency = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = "select c from " + AmpCurrency.class.getName()
					+ " c where (c.currencyCode=:currCode)";
			qry = session.createQuery(queryString);
			qry.setParameter("currCode", currCode, Hibernate.STRING);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				ampCurrency = (AmpCurrency) itr.next();
			}
		} catch (Exception e) {
			logger.error("Unable to get currency");
			logger.debug("Exceptiion " + e);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		return ampCurrency;
	}
	
	public static Currency getCurrency(Long id) {
		logger.debug("in getCurrency" + id);
		Session sess = null;
		Query qry1 = null;
		Query qry2 = null;
		Currency curr = null;
		AmpCurrency ampCurrency = null;

			try {
				sess = PersistenceManager.getRequestDBSession();
			} catch (DgException e) {
				logger.error(e);
				e.printStackTrace();
			}
			String queryString = "select c from " + AmpCurrency.class.getName()	+ " c where (c.ampCurrencyId=:id)";
			qry1 = sess.createQuery(queryString);
			qry1.setParameter("id", id, Hibernate.LONG);
			ampCurrency = (AmpCurrency)qry1.uniqueResult();
			
			curr = new Currency();
			curr.setCurrencyId(ampCurrency.getAmpCurrencyId());
			curr.setCurrencyCode(ampCurrency.getCurrencyCode());
			curr.setCountryName(ampCurrency.getCountryName());
			
			String qryStr = "select cr from "+ AmpCurrencyRate.class.getName()+ " cr where (cr.toCurrencyCode=:currCode)";
			qry2 = sess.createQuery(qryStr);
			qry2.setParameter("currCode", ampCurrency.getCurrencyCode());
			Iterator itr1 = qry2.list().iterator();
			if (itr1.hasNext()) {
				AmpCurrencyRate ampCurrRate = (AmpCurrencyRate) itr1.next();
				curr.setCurrencyRateId(ampCurrRate.getAmpCurrencyRateId());
				curr.setExchangeRate(ampCurrRate.getExchangeRate());
			}else{
				curr.setExchangeRate(new Double(1));
			}
	
		
		return curr;
	}

	public static AmpCurrency getAmpcurrency(Long id) {
		AmpCurrency ampCurrency = null;
		Session session = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			// modified by Priyajith
			// desc:used select query instead of session.load
			// start
			String queryString = "select c from " + AmpCurrency.class.getName()
					+ " c " + "where (c.ampCurrencyId=:id)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("id", id, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				ampCurrency = (AmpCurrency) itr.next();
			}
			// end
		} catch (Exception ex) {
			logger.error("Unable to get currency " + ex);
		} 
		return ampCurrency;
	}

	public static AmpCurrency getAmpcurrency(String currCode) {
		AmpCurrency ampCurrency = null;
		Session session = null;

		try {
			session = PersistenceManager.getSession();
			// modified by Priyajith
			// desc:used select query instead of session.load
			// start
			String queryString = "select c from " + AmpCurrency.class.getName()
					+ " c " + "where (c.currencyCode=:id)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("id", currCode, Hibernate.STRING);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				ampCurrency = (AmpCurrency) itr.next();
			}
			// end
		} catch (Exception ex) {
			logger.error("Unable to get currency " + ex);
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger.debug("releaseSession() failed", ex2);
			}
		}
		return ampCurrency;
	}

	
	public static ArrayList<AmpCurrency> getActiveAmpCurrencyByName() {
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
//		finally {
//			try {
//				PersistenceManager.releaseSession(session);
//			} catch (Exception ex2) {
//				logger.debug("releaseSession() failed", ex2);
//			}
//		}
		AmpCaching.getInstance().currencyCache.activeCurrencies = new ArrayList<AmpCurrency>(currency);
		return currency;
	}
	
	public static ArrayList<AmpCurrency> getActiveAmpCurrencyByCode() {
		AmpCurrency ampCurrency = null;
		Session session = null;
		Query q = null;
		Iterator<AmpCurrency> iter = null;
		String queryString = null;
		ArrayList<AmpCurrency> currency = new ArrayList<AmpCurrency>();
		try {
			session = PersistenceManager.getRequestDBSession();
			queryString = " select c from " + AmpCurrency.class.getName()
					+ " c where c.activeFlag='1' order by c.currencyCode";
			q = session.createQuery(queryString);
			iter = q.list().iterator();

			while (iter.hasNext()) {
				ampCurrency = iter.next();
				currency.add(ampCurrency);
			}
		} catch (Exception ex) {
			logger.error("Unable to get currency " + ex);
		}
		return currency;
	}

	public static double getExchangeRate(String currencyCode,
			int adjustmentType, Date exchangeRateDate) {
		//if (logger.isDebugEnabled())
			//logger.debug("getExchangeRate with currencyCode" + currencyCode);

		Session session = null;
		Query q = null;
		Collection c = null;
		Iterator iter = null;
		Double exchangeRate = null;

		try {
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(exchangeRateDate);
			String currencyDate=calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH)+1) + "-01";
		/*	logger.debug("Date: " + exchangeRateDate);
			logger.debug("Exchange Rate Month: " + calendar.get(Calendar.MONTH));
			logger.debug("Exchange Rate Year: " + calendar.get(Calendar.YEAR));
			logger.debug("Currency Code: " + currencyCode);*/
			session = PersistenceManager.getSession();
			String queryString = new String();
			queryString = "select f.exchangeRate from "
					+ AmpCurrencyRate.class.getName()
					+ " f where (f.toCurrencyCode='" + currencyCode + "') and (f.exchangeRateDate='" + currencyDate + "')";
//			logger.debug("queryString:" + queryString);
			q = session.createQuery(queryString);
//			q.setParameter("currencyCode", currencyCode, Hibernate.STRING);

//			q.setParameter("exchangeRateDate", exchangeRateDate,Hibernate.DATE);
			boolean searchOther = false;
			if (q.list().size() > 0){
				exchangeRate = (Double) q.list().get(0);
				if (exchangeRate == null)
					searchOther = true;
			}
			else
				searchOther = true;

			if (searchOther){
				queryString = "select f.exchangeRate from "
						+ AmpCurrencyRate.class.getName()
						+ " f where (f.toCurrencyCode=:currencyCode) and (f.exchangeRateDate<:exchangeRateDate) order by f.exchangeRateDate desc";
				q = session.createQuery(queryString);
					q.setParameter("currencyCode", currencyCode,
							Hibernate.STRING);
					q.setParameter("exchangeRateDate", exchangeRateDate,
							Hibernate.DATE);
					if (q.list().size() > 0){
						exchangeRate = (Double) q.list().get(0);
						Iterator itr = q.list().iterator();
						while ((exchangeRate == null)&&(itr.hasNext())) //fix for null currency
							exchangeRate = (Double) itr.next();
					}
					else {
						queryString = "select f.exchangeRate from "
								+ AmpCurrencyRate.class.getName()
								+ " f where (f.toCurrencyCode=:currencyCode) and (f.exchangeRateDate>:exchangeRateDate) order by f.exchangeRateDate";
						q = session.createQuery(queryString);
						q.setParameter("currencyCode", currencyCode,
								Hibernate.STRING);
						q.setParameter("exchangeRateDate", exchangeRateDate,
								Hibernate.DATE);
						if (q.list().size() > 0){
							exchangeRate = (Double) q.list().get(0);
							Iterator itr = q.list().iterator();
							while ((exchangeRate == null)&&(itr.hasNext())) //fix for null currency
								exchangeRate = (Double) itr.next();
						}
					}
				}

		} catch (Exception ex) {
			logger.debug("Unable to get exchange rate from database " + ex.getMessage());
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
/*		if (logger.isDebugEnabled())
			logger.debug("getExchangeRate() for currency code :" + currencyCode
					+ "returns " + exchangeRate); */
		if(exchangeRate!=null)
			return exchangeRate.doubleValue();
		return 0;
	}

	public static Collection getAmpCurrencyRate() {

		Session session = null;
		Query q = null;
		Collection c = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = new String();
			queryString = "select currency from "
					+ AmpCurrencyRate.class.getName() + " currency";
			q = session.createQuery(queryString);
			c = q.list();
		} catch (Exception ex) {
			logger.debug("Unable to get exchange rate from database", ex);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		return c;
	}

	public static double getExchangeRate(Long ampFundingId, String orgRoleCode) {

		//if (logger.isDebugEnabled())
			//logger.debug("getExchangeRate with ampFundingId=" + ampFundingId
				//	+ " orgRoleCode=" + orgRoleCode);

		Session session = null;
		Query q = null;
		Collection c = null;
		Iterator iter = null;
		AmpCurrency ampCurrency = null;
		String currencyCode = "";
		double exchangeRate = 1.0;

		try {
			session = PersistenceManager.getSession();
			String queryString = new String();
			queryString = "select f.ampCurrencyId from "
					+ AmpFundingDetail.class.getName()
					+ " f where (f.ampFundingId=:ampFundingId) and (f.orgRoleCode=:orgRoleCode)"
					+ " group by f.ampFundingId";
			q = session.createQuery(queryString);
			q.setParameter("ampFundingId", ampFundingId, Hibernate.LONG);
			q.setParameter("orgRoleCode", orgRoleCode, Hibernate.STRING);
			c = q.list();
			if (c.size() != 0) {
				iter = c.iterator();
				if (iter.hasNext()) {
					ampCurrency = (AmpCurrency) iter.next();
				}
			} else {
				if (logger.isDebugEnabled())
					logger
							.debug("Unable to get ampCurrencyId from table AmpFunding");
			}

			if (ampCurrency != null) {
				currencyCode = ampCurrency.getCurrencyCode();
				exchangeRate = getExchangeRate(currencyCode);
			}
		} catch (Exception ex) {
			logger.debug("Unable to get exchange rate from database", ex);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		logger.debug("getExchangeRate(id,orgRoleCode " + exchangeRate);
		return exchangeRate;
	}

	public static double getExchangeRate(String currencyCode) {
		
		try {
			return CurrencyUtil.getLatestExchangeRate(currencyCode);
		} catch (AimException e) {
			e.printStackTrace();
			return 1.0;
		}
		
		//if (logger.isDebugEnabled())
			//logger.debug("getExchangeRate with currencyCode" + currencyCode);

//		Session session = null;
//		Query q = null;
//		Collection c = null;
//		Iterator iter = null;
//		Double exchangeRate = null;
//
//		try {
//			session = PersistenceManager.getSession();
//			String queryString = new String();
//			queryString = "select f.exchangeRate from "
//					+ AmpCurrencyRate.class.getName()
//					+ " f where (f.toCurrencyCode=:currencyCode) order by f.exchangeRateDate desc";
//			q = session.createQuery(queryString);
//			q.setParameter("currencyCode", currencyCode, Hibernate.STRING);
//			c = q.list();
//			if (c.size() != 0) {
//				iter = c.iterator();
//				if (iter.hasNext()) {
//					exchangeRate = (Double) iter.next();
//				}
//			} else {
//				if (logger.isDebugEnabled())
//					logger
//							.debug("Unable to get exchange rate for currency code :"
//									+ currencyCode);
//				exchangeRate = new Double(1.0);
//			}
//		} catch (Exception ex) {
//			logger.debug("Unable to get exchange rate from database", ex);
//		} finally {
//			try {
//				if (session != null) {
//					PersistenceManager.releaseSession(session);
//				}
//			} catch (Exception ex) {
//				logger.error("releaseSession() failed");
//			}
//		}
///*		if (logger.isDebugEnabled())
//			logger.debug("getExchangeRate() for currency code :" + currencyCode
//					+ "returns " + exchangeRate); */
//		return exchangeRate.doubleValue();
	}

	/**
	 * Returns true if currency have at leat one value added.
	 * Used in addfunding.
	 * @param currencyCode currency Code
	 * @return boolean
	 * @author Irakli Kobiashvili
	 */
	public static Boolean isRate(String currencyCode) {
		if (AmpCaching.getInstance().currencyCache.currencyHasRate.containsKey(currencyCode))
			return AmpCaching.getInstance().currencyCache.currencyHasRate.get(currencyCode);
		
		Session session = null;
		Query q = null;

		try {
			String baseCurrencyCode = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.BASE_CURRENCY);
			if (currencyCode.equalsIgnoreCase(baseCurrencyCode)){
				return true;
			}

			session = PersistenceManager.getRequestDBSession();
//			AMP-4299			
//			String queryString = "select f.exchangeRate from "
//					+ AmpCurrencyRate.class.getName()
//					+ " f where (f.toCurrencyCode=:currencyCode) and f.exchangeRateDate between :fromDate and :toDate order by f.exchangeRateDate desc";
			
			//commented by dare for  AMP-10504 
//			String queryString = "select f.exchangeRate from " + AmpCurrencyRate.class.getName() +
//					" f where f.toCurrencyCode=:currencyCode " +
//					" order by f.exchangeRateDate desc";
			
			String queryString = "select f.exchangeRate from " + AmpCurrencyRate.class.getName() +
			" f where f.toCurrencyCode=:currencyCode " +" and f.fromCurrencyCode=:baseCurrencyCode"+
			" order by f.exchangeRateDate desc";
			
			q = session.createQuery(queryString);
			q.setString("currencyCode", currencyCode);
			q.setString("baseCurrencyCode", baseCurrencyCode);
//			AMP-4299			
//			q.setParameter("fromDate",fromdate,Hibernate.DATE);
//			q.setParameter("toDate",todate,Hibernate.DATE);
			List rates = q.list();
			boolean result = false;
			for (Iterator iter = rates.iterator(); iter.hasNext();) {
				Double element = (Double) iter.next();
				if (element != null){
					result = true;
					break;
				}
			}
			AmpCaching.getInstance().currencyCache.currencyHasRate.put(currencyCode, result);
			return result;
		} catch (Exception ex) {
			logger.debug("Unable to get exchange rate from database", ex);
			throw new RuntimeException("Error retriving currency exchange rate for "+ currencyCode,ex);
		}
	}




	/**
	 * Returns Latest Exchange rate for currency specified in parameter by code.
	 * Used in NPD, may be temporarry
	 * @param currencyCode currency Code
	 * @return exchange rate, latest value
	 * @author Irakli Kobiashvili
	 */
        public static double getLatestExchangeRate(String currencyCode) throws AimException {
	        	String baseCurrCode		= FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.BASE_CURRENCY);
	    		if ( baseCurrCode == null ) 
	    			baseCurrCode	= "USD";
	    		
                Session session = null;
                Query q = null;
                try {
                        logger.debug("retrivieving latest exchange rate for currency:"+currencyCode);
                        session = PersistenceManager.getRequestDBSession();
                        String queryString = "select f.exchangeRate from "
                                        + AmpCurrencyRate.class.getName()
                                        + " f where (f.toCurrencyCode=:currencyCode and f.fromCurrencyCode=:baseCurrencyCode) order by f.exchangeRateDate desc limit 1";
                        q = session.createQuery(queryString);
                        q.setString("currencyCode", currencyCode);
                        q.setString("baseCurrencyCode", baseCurrCode);
                        List rates = q.list();
                        Double result = null;
                        if (rates == null || rates.isEmpty()){
                                logger.debug("No exchange rate value found for currency: "+currencyCode);
                                result = new Double(1.0);
                        }else{
                                result = (Double)rates.iterator().next();
                        }
                        return result.doubleValue();
                } catch (Exception ex) {
                        logger.debug("Unable to get exchange rate from database", ex);
                        throw new AimException("Error retriving currency exchange rate for "+ currencyCode,ex);
                }
        }


	public static String getCurrencyName(Long currencyId) {
		Session session = null;
		String queryString = null;
		Query q = null;

		try {
			session = PersistenceManager.getSession();
			queryString = "select a.currencyCode from "+AmpCurrency.class.getName()+" a where a.ampCurrencyId=:currencyId";
			q = session.createQuery(queryString);
			q.setLong("currencyId", currencyId);
			logger.info("Got currency name : "+q.list().get(0).toString());
			return q.list().get(0).toString();
		}
		catch (Exception ex) {
			logger.error("Unable to get currency name", ex);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
			}
		}
		return null;
	}

	public static List getAmpCurrency(Long ampActivityId) {
		List currency = null;
		Query q = null;
		Session session = null;
		String queryString = null;

		try {
			session = PersistenceManager.getSession();
			//queryString = " select Progress from " +
			// AmpPhysicalPerformance.class.getName() + " Progress where
			// (Progress.ampActivityId=:ampActivityId )";
			queryString = "select distinct ac.currencyCode from "
					+ AmpCurrency.class.getName()
					+ " ac, "
					+ AmpFundingDetail.class.getName()
					+ " afd, "
					+ AmpFunding.class.getName()
					+ " af where (afd.ampFundingId=af.ampFundingId) and (afd.ampCurrencyId=ac.ampCurrencyId) and (af.ampActivityId=:ampActivityId)";
			q = session.createQuery(queryString);
			q.setParameter("ampActivityId", ampActivityId, Hibernate.LONG);
			currency = q.list();
			Iterator iter = currency.iterator();
			while (iter.hasNext()) {
				iter.next();
				logger.debug("Size :" + currency.size());
				logger.debug("Currency 1:" + (String) currency.get(0));
				logger.debug("Currency 2:" + (String) currency.get(1));

			}
		} catch (Exception ex) {
			logger.error("Unable to get Amp PhysicalPerformance", ex);
			//////System.out.println(ex.toString()) ;
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
			}
		}
		logger
				.debug("Getting funding Executed successfully "
						+ currency.size());
		return currency;

	}

	/*
	 * searching for a currency Rate using the code
	 * added by Govind
	 */
	public static Collection getCurrencyRate(String code) {
		Collection col = new ArrayList();
		Session session = null;
		Query qry = null;
		String qryStr = null;
		try {
			session = PersistenceManager.getSession();

				qryStr = "select curr from " + AmpCurrencyRate.class.getName() + " curr where curr.toCurrencyCode=:code ";
				qry = session.createQuery(qryStr);
				qry.setParameter("code",code,Hibernate.STRING);
			col = qry.list();
		} catch (Exception e) {
			logger.error("Exception from getAllCurrencies()");
			e.printStackTrace(System.out);
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed");
				}
			}
		}

		return col;
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
				qry.setParameter("id",id,Hibernate.INTEGER);
			col = qry.list();
		} catch (Exception e) {
			logger.error("Exception from getAllCurrencies()");
			e.printStackTrace(System.out);
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed");
				}
			}
		}

		return col;
	}
	public static Collection getCurrencyRateValues(Long id) {
		Collection col = new ArrayList();
		Session session = null;
		Query qry = null;
		String qryStr = null;
		try {
			session = PersistenceManager.getSession();

				qryStr = "select curr from " + AmpCurrencyRate.class.getName() + " curr where curr.ampCurrencyRateId=:id ";
				qry = session.createQuery(qryStr);
				qry.setParameter("id",id,Hibernate.LONG);
			col = qry.list();
		} catch (Exception e) {
			logger.error("Exception from getAllCurrencies()");
			e.printStackTrace(System.out);
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed");
				}
			}
		}

		return col;
	}
	/*
	 * For Deleting a Currency...
	 */
	public static void deleteCurrency(String Code) throws Exception {
		/*try
		{
		*/	
			AmpCaching.getInstance().currencyCache.reset();
			AmpCurrency ampC = new AmpCurrency();
			ampC = CurrencyUtil.getCurrencyByCode(Code);
			if (ampC != null)
				DbUtil.delete(ampC);
		/*}
		catch (Exception e) {
			logger.error("Exception from getAllCurrencies()");
			e.printStackTrace(System.out);
		}*/
	}

	/**
	 * returns default currency if no team member logged in
	 * @param tm
	 * @return
	 */
	public static AmpCurrency getWorkspaceCurrency(TeamMember tm) {
		if ((tm != null) && (tm.getAppSettings() != null))
			return getAmpcurrency(tm.getAppSettings().getCurrencyId());
		return getDefaultCurrency();		
	}

	/**
	 * returns default currency if no team member logged in
	 * @param tm
	 * @return
	 */
	public static AmpCurrency getWicketWorkspaceCurrency() {
		TeamMember tm =  AmpAuthWebSession.getYourAppSession().getCurrentMember();
		return getWorkspaceCurrency(tm);		
	}
	
	/**
	 * not initialized on static constructor, because Hibernate & stuff are not initialized at that point
	 */
	private static AmpCurrency defaultCurrency;
	public static AmpCurrency getDefaultCurrency()
	{
		if (defaultCurrency == null)
			defaultCurrency = AmpARFilter.getDefaultCurrency();
		return defaultCurrency;
	}
	
//	public static AmpCurrency getEffectiveCurrency(TeamMember tm)
//	{
//		if ((tm != null) && (tm.getAppSettings() != null))
//			return getAmpcurrency(tm.getAppSettings().getCurrencyId());
//		return getDefaultCurrency();
//	}
}
