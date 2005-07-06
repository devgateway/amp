/*
 * CurrencyUtil.java
 * Created: 01-May-2005
 */
package org.digijava.module.aim.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpCurrencyRate;
import org.digijava.module.aim.helper.CurrencyRates;
import org.digijava.module.aim.helper.DateConversion;

public class CurrencyUtil {
	
	private static Logger logger = Logger.getLogger(CurrencyUtil.class);
	
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
				Iterator itr = res.iterator();
				AmpCurrency currencies[] = new AmpCurrency[res.size()];
				int index = 0;
				while (itr.hasNext()) {
					currencies[index++] = (AmpCurrency) itr.next();
					logger.debug(currencies[index-1].getCurrencyName());
				}
				qryStr = "select cRate from " + AmpCurrencyRate.class.getName() + " cRate " +
				"where cRate.toCurrencyCode in ('";

				for (int i = 0;i < index;i ++) {
					qryStr += currencies[i].getCurrencyCode() + "'";
					if ((i+1) < index) {
						qryStr += ",'";
					}
				}
				qryStr += ") order by cRate.exchangeRateDate desc,cRate.toCurrencyCode";
				qry = session.createQuery(qryStr);
				itr = qry.list().iterator();
				AmpCurrencyRate cRate = null;
		
				CurrencyRates currencyRates = null;
				while (itr.hasNext()) {
					currencyRates = new CurrencyRates();
					cRate = (AmpCurrencyRate) itr.next();
					
					currencyRates.setCurrencyCode(cRate.getToCurrencyCode());
					for (int i = 0;i < currencies.length;i ++) {
						if (currencies[i].getCurrencyCode().equals(
								cRate.getToCurrencyCode())) {
							currencyRates.setCurrencyName(currencies[i].
									getCurrencyName());
							break;
						}
					}
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
		
		logger.info("returning a collection of size " + col.size());
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
				Iterator itr = res.iterator();
				AmpCurrency currencies[] = new AmpCurrency[res.size()];
				int index = 0;
				while (itr.hasNext()) {
					currencies[index++] = (AmpCurrency) itr.next();
					logger.debug(currencies[index-1].getCurrencyName());
				}
				qryStr = "select cRate from " + AmpCurrencyRate.class.getName() + " cRate " +
				"where cRate.toCurrencyCode in ('";

				for (int i = 0;i < index;i ++) {
					qryStr += currencies[i].getCurrencyCode() + "'";
					if ((i+1) < index) {
						qryStr += ",'";
					}
				}
				qryStr += ") and cRate.exchangeRateDate between :fromDate and :toDate order by " +
						"cRate.exchangeRateDate desc,cRate.toCurrencyCode";
				qry = session.createQuery(qryStr);
				qry.setParameter("fromDate",fromDate,Hibernate.DATE);
				qry.setParameter("toDate",toDate,Hibernate.DATE);

				itr = qry.list().iterator();
				AmpCurrencyRate cRate = null;
		
				CurrencyRates currencyRates = null;
				while (itr.hasNext()) {
					currencyRates = new CurrencyRates();
					cRate = (AmpCurrencyRate) itr.next();
					
					currencyRates.setId(cRate.getAmpCurrencyRateId());
					currencyRates.setCurrencyCode(cRate.getToCurrencyCode());
					for (int i = 0;i < currencies.length;i ++) {
						if (currencies[i].getCurrencyCode().equals(
								cRate.getToCurrencyCode())) {
							currencyRates.setCurrencyName(currencies[i].
									getCurrencyName());
							break;
						}
					}
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
		
		logger.info("returning a collection of size " + col.size());
		return col;
	}	
	
	public static Double getExchangeRate(String currCode,Date date) {
		Double exchRate = null;
		Session session = null;
		Query qry = null;
		String qryStr = null;
		
		try {
			session = PersistenceManager.getSession();
			qryStr = "select cr.exchangeRate from " + AmpCurrencyRate.class.getName() + "" +
					" cr where (cr.toCurrencyCode=:code) and " +
					"(cr.exchangeRateDate=:date)";
			qry = session.createQuery(qryStr);
			qry.setParameter("code",currCode,Hibernate.STRING);
			qry.setParameter("date",date,Hibernate.DATE);
			
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				exchRate = (Double) itr.next();
			}
		} catch (Exception e) {
			logger.error("Exception from getExchangeRate()");
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
		
		return exchRate;
	}
	
	/**
	 * Saves an AmpCurrencyRate object to the database
	 * @param cRate The AmpCurrencyRate object
	 */
	public static void saveCurrencyRate(AmpCurrencyRate cRate) {
		Session session = null;
		Transaction tx = null;
		Query qry = null;
		String qryStr = null;
		
		try {
			session = PersistenceManager.getSession();
			tx = session.beginTransaction();
			
			qryStr = "select cRate from " + AmpCurrencyRate.class.getName() + " cRate " +
					"where (cRate.toCurrencyCode=:code) and " +
					"(cRate.exchangeRateDate=:date)";
			qry = session.createQuery(qryStr);
			qry.setParameter("code",cRate.getToCurrencyCode(),Hibernate.STRING);
			qry.setParameter("date",cRate.getExchangeRateDate(),Hibernate.DATE);
			
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) { 
				// if the currency rate already exist update the rate 
				AmpCurrencyRate actRate = (AmpCurrencyRate) itr.next();
				actRate.setExchangeRate(cRate.getExchangeRate());
				session.update(actRate);
			} else {
				// add the currency rate object if it does not exist
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
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed");
				}
			}
		}
	}
	
	public static Collection getAllCurrencies(int active) {
		Collection col = new ArrayList();
		Session session = null;
		Query qry = null;
		String qryStr = null;
		
		try {
			session = PersistenceManager.getSession();
			if (active == -1) {
				qryStr = "select curr from " + AmpCurrency.class.getName() + " curr " +
					"order by curr.currencyCode";
				qry = session.createQuery(qryStr);				
			} else {
				qryStr = "select curr from " + AmpCurrency.class.getName() + " curr " +
					"where (curr.activeFlag=:flag) order by curr.currencyCode";
				qry = session.createQuery(qryStr);
				qry.setParameter("flag",new Integer(active),Hibernate.INTEGER);
			}
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
	
	public static void updateCurrencyStatus(String code,int status) {
		Session session = null;
		Query qry = null;
		String qryStr = null;
		Transaction tx = null;
		
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
				tx = session.beginTransaction();
				session.update(curr);
				tx.commit();
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
	
	/**
	 * Save an AmpCurrency object
	 * @param currency The AmpCurrency Object to be saved
	 * @param cRate The initial currency rates in an AmpCurrencyRate object
	 */
	public static void saveCurrency(AmpCurrency currency,AmpCurrencyRate cRate) {
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
				tx = session.beginTransaction();
				session.update(curr);
				tx.commit();
			} else {
				logger.debug("Creating new currency id ...");
				tx = session.beginTransaction();
				session.save(currency);
				session.save(cRate);
				tx.commit();				
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
		Session session = null;
		Transaction tx = null;
		
		try {
			session = PersistenceManager.getSession();
			tx = session.beginTransaction();
			for (int i = 0;i < cRates.length; i++) {
				if (cRates[i] != null) {
					AmpCurrencyRate cRate = (AmpCurrencyRate) session.load(AmpCurrencyRate.class,cRates[i]);
					session.delete(cRate);
				}
			}
			tx.commit();				
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
	 */
	public static void saveCurrencyRates(Collection currRates) {
		Session session = null;
		Query qry = null;
		String qryStr = null;
		Transaction tx = null;
		
		try {
			session = PersistenceManager.getSession();
			tx = session.beginTransaction();
			
			Iterator itr = currRates.iterator();
			logger.debug("currency rates size :" + currRates.size());
			while (itr.hasNext()) {
				CurrencyRates cr = (CurrencyRates) itr.next();
				qryStr = "select crate from " + AmpCurrencyRate.class.getName() + 
					" crate where (crate.toCurrencyCode=:code) and " +
					"(crate.exchangeRateDate=:date)";
				qry = session.createQuery(qryStr);
				qry.setParameter("code",cr.getCurrencyCode(),Hibernate.STRING);
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
					logger.debug("Saving " + currencyRate.getToCurrencyCode());
					session.save(currencyRate);
				}
			}
			tx.commit();
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
}