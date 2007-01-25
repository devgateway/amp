package org.digijava.module.aim.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import org.apache.log4j.Logger;
import org.digijava.kernel.dbentity.Country;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpFeature;
import org.digijava.module.aim.dbentity.AmpGlobalSettings;
import org.digijava.module.aim.dbentity.AmpSiteFlag;
import org.digijava.module.aim.helper.Flag;

public class FeaturesUtil {
	
	private static Logger logger = Logger.getLogger(FeaturesUtil.class);
	
	public static Collection getAMPFeatures() {
		Session session = null;
		Collection col = new ArrayList();
		String qryStr = null;
		Query qry = null;
		
		try {
			session = PersistenceManager.getSession();
			qryStr = "select f from " + AmpFeature.class.getName() + " f";
			qry = session.createQuery(qryStr);
			col = qry.list();
				
		} catch (Exception ex) {
			logger.error("Exception : " + ex.getMessage());
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
		}
		return col;
	}	
	
	/**
	 * Used to get the features which are currently active for AMP 
	 * @return The collection of org.digijava.module.aim.dbentity.AmpFeature objects
	 */
	public static Collection getActiveFeatures() {
		Session session = null;
		Collection col = new ArrayList();
		String qryStr = null;
		Query qry = null;
		
		try {
			session = PersistenceManager.getSession();
			qryStr = "select f from " + AmpFeature.class.getName() + " f" +
					" where f.active = true";
			qry = session.createQuery(qryStr);
			col = qry.list();
				
		} catch (Exception ex) {
			logger.error("Exception : " + ex.getMessage());
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
		}
		return col;
	}
	
	public static AmpFeature toggleFeature(Integer featureId) {
		Session session = null;
		Transaction tx = null;
		AmpFeature feature = null;
		
		try {
			session = PersistenceManager.getSession();
			tx = session.beginTransaction();
			feature = (AmpFeature) session.load(AmpFeature.class,
					featureId);
			feature.setActive(!feature.isActive());
			session.update(feature);
			tx.commit();
		} catch (Exception ex) {
			logger.error("Exception : " + ex.getMessage());
			if (tx != null) {
				try {
					tx.rollback();
				} catch (Exception rbf) {
					logger.error("Transaction rollback failed :" + rbf.getMessage());
				}
			}
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
		}
		return feature;
	}
	
	public static Collection getAllCountries() {
		Session session = null;
		Collection col = new ArrayList();
		String qryStr = null;
		Query qry = null;
		
		try {
			session = PersistenceManager.getSession();
			qryStr = "select c.countryId,c.countryName from " + Country.class.getName() + " c order by c.countryName";
			qry = session.createQuery(qryStr);
			Iterator itr = qry.list().iterator();
			while (itr.hasNext()) {
				Object obj[] = (Object[]) itr.next();
				Long cId = (Long) obj[0];
				String cName = (String) obj[1];
				org.digijava.module.aim.helper.Country 
					ctry = new org.digijava.module.aim.helper.Country();
				ctry.setId(cId);
				ctry.setName(cName);
				col.add(ctry);
			}
			
		} catch (Exception ex) {
			logger.error("Exception : " + ex.getMessage());
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
		}
		
		return col; 
	}
	
	public static Collection getAllCountryFlags() {
		Session session = null;
		Collection col = new ArrayList();
		String qryStr = null;
		Query qry = null;
		String params = "";
		
		try {
			session = PersistenceManager.getSession();
			qryStr = "select f.countryId,f.defaultFlag from " + AmpSiteFlag.class.getName() + " f";
			qry = session.createQuery(qryStr);
			Iterator itr = qry.list().iterator();
			while (itr.hasNext()) {
				Object obj[] = (Object[]) itr.next();
				Long cId = (Long) obj[0];
				Boolean defFlag = (Boolean) obj[1];
				Flag f = new Flag();
				if (params != null && params.trim().length() > 0) {
					params += ",";
				}
				params += cId.longValue();
				
				f.setCntryId(cId);
				f.setDefaultFlag(defFlag.booleanValue());
				col.add(f);
			}
			
			
			if (params != null && params.trim().length() > 0) {
				qryStr = "select c.countryId,c.countryName from " + Country.class.getName() + " c" +
					" where c.countryId in (" + params + ")";
			
				qry = session.createQuery(qryStr);
				itr = qry.list().iterator();
				while (itr.hasNext()) {
					Object obj[] = (Object[]) itr.next();
					Long cId = (Long) obj[0];
					String cName = (String) obj[1];
					long temp = cId.longValue();
					
					Iterator itr1 = col.iterator();
					while (itr1.hasNext()) {
						Flag f = (Flag) itr1.next();
						if (f.getCntryId().longValue() == temp) {
							f.setCntryName(cName);
						}
					}
					
				}				
			}
		} catch (Exception ex) {
			logger.error("Exception : " + ex.getMessage());
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
		}
		
		return col; 		
	}
	
	public static AmpSiteFlag getAmpSiteFlag(Long id) {
		Session session = null;
		AmpSiteFlag flag = null;
		
		try {
			session = PersistenceManager.getSession();
			flag = (AmpSiteFlag) session.get(AmpSiteFlag.class,id);
		} catch (Exception ex) {
			logger.error("Exception : " + ex.getMessage());
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
		}
		
		return flag; 		
	}
	
	public static byte[] getFlag(Long id) {
		Session session = null;
		byte flag[] = null;
		
		try {
			session = PersistenceManager.getSession();
			AmpSiteFlag tmp = (AmpSiteFlag) session.get(AmpSiteFlag.class,id);
			if (tmp != null) {
				flag = tmp.getFlag();
			}
			
		} catch (Exception ex) {
			logger.error("Exception : " + ex.getMessage());
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
		}
		
		return flag; 				
	}
	
	public static byte[] getDefaultFlag() {
		Session session = null;
		byte flag[] = null;
		String qryStr = null;
		Query qry = null;
		
		try {
			qryStr = "select f from " + AmpSiteFlag.class.getName() + " f " +
					"where f.defaultFlag=true";
			session = PersistenceManager.getSession();
			qry = session.createQuery(qryStr);
			AmpSiteFlag sf = null;
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				sf = (AmpSiteFlag) itr.next();
			}
			if (sf != null) {
				flag = sf.getFlag();
			}
			
		} catch (Exception ex) {
			logger.error("Exception : " + ex.getMessage());
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
		}
		
		return flag; 				
	}	
	
	public static boolean defaultFlagExist() {
		Session session = null;
		boolean exist = false;
		String qryStr = null;
		Query qry = null;
		
		try {
			qryStr = "select count(*) from " + AmpSiteFlag.class.getName() + " f " +
					"where f.defaultFlag=true";
			session = PersistenceManager.getSession();
			qry = session.createQuery(qryStr);
			Iterator itr = qry.list().iterator();
			Integer num = null;
			if (itr.hasNext()) {
				num = (Integer) itr.next(); 
			}
			if (num.intValue() > 0) {
				exist = true;
			}
			
		} catch (Exception ex) {
			logger.error("Exception : " + ex.getMessage());
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
		}
		
		return exist;		
	}
	
	public static void setDefaultFlag(Long id) {
		Session session = null;
		Transaction tx = null;
		String qryStr = null;
		Query qry = null;
		try {
			session = PersistenceManager.getSession();
			qryStr = "select s from " + AmpSiteFlag.class.getName() + " s " +
					"where s.defaultFlag=true";			
			qry = session.createQuery(qryStr);
			Iterator itr = qry.list().iterator();
			AmpSiteFlag defFlag = null;
			if (itr.hasNext()) {
				defFlag = (AmpSiteFlag) itr.next();
			}
			AmpSiteFlag newDefFlag = (AmpSiteFlag) session.load(AmpSiteFlag.class,id);
			tx = session.beginTransaction();
			newDefFlag.setDefaultFlag(true);
			session.update(newDefFlag);
			if (defFlag != null) {
				defFlag.setDefaultFlag(false);
				session.update(defFlag);				
			}
			tx.commit();
		} catch (Exception ex) {
			logger.error("Exception : " + ex.getMessage());
			ex.printStackTrace(System.out);
			if (tx != null) {
				try {
					tx.rollback();
				} catch (Exception rbf) {
					logger.error("Rollback failed !");
				}
			}
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
		}
	}
	/*
	 * edited by Govind G Dalwani
	 */
	/*
	 * to get all the Global settings
	 */
	public static Collection getGlobalSettings()
	{
		Collection coll = null;
		Session session = null;
		Transaction tx = null;
		String qryStr = null;
		Query qry = null;
		try{
				session = PersistenceManager.getSession();
				qryStr = "select gs from " + AmpGlobalSettings.class.getName() + " gs " ;
				qry = session.createQuery(qryStr);
				coll=qry.list();
				
		}
		catch (Exception ex) {
			logger.error("Exception : " + ex.getMessage());
			ex.printStackTrace(System.out);
			if (tx != null) {
				try {
					tx.rollback();
				} catch (Exception rbf) {
					logger.error("Rollback failed !");
				}
			}
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
		}
		return coll;
	}
	/*
	 * to get the country names
	 */
	public static Collection getCountryNames()
	{
		Collection col = null;
		Session session = null;
		Query qry = null;
		String qryStr = null;
		Transaction tx = null;
		try{
			session = PersistenceManager.getSession();
			qryStr = "select cn from " + Country.class.getName() + " cn order by cn.countryName" ;
			qry = session.createQuery(qryStr);
			col=qry.list();
			
			}
			catch (Exception ex) {
				logger.error("Exception : " + ex.getMessage());
				ex.printStackTrace(System.out);
				if (tx != null) {
					try {
						tx.rollback();
					} catch (Exception rbf) {
						logger.error("Rollback failed !");
					}
				}
			} finally {
				if (session != null) {
					try {
						PersistenceManager.releaseSession(session);
					} catch (Exception rsf) {
						logger.error("Release session failed :" + rsf.getMessage());
					}
				}
			}
		return col;
	}
	/* 
	 * to get the country ISO that is set as a default value...
	 */
	public static Collection getDefaultCountryISO()
	{
		Collection col = null;
		Session session = null;
		Query qry = null;
		String qryStr = null;
		Transaction tx = null;
		try{
			session = PersistenceManager.getSession();
			qryStr = "select gs from " + AmpGlobalSettings.class.getName() + " gs where gs.globalSettingsName = 'Default Country' " ;
			qry = session.createQuery(qryStr);
			col=qry.list();
			
			}
			catch (Exception ex) {
				logger.error("Exception : " + ex.getMessage());
				ex.printStackTrace(System.out);
				if (tx != null) {
					try {
						tx.rollback();
					} catch (Exception rbf) {
						logger.error("Rollback failed !");
					}
				}
			} finally {
				if (session != null) {
					try {
						PersistenceManager.releaseSession(session);
					} catch (Exception rsf) {
						logger.error("Release session failed :" + rsf.getMessage());
					}
				}
			}
		return col;
	}
	/*
	 * to get the country name from the Iso got
	 */
	public static Collection getDefaultCountry(String ISO)
	{
		Collection col = null;
		Session session = null;
		Query qry = null;
		String qryStr = null;
		Transaction tx = null;
		String a ="in the get country...";
		logger.info(a);
		try{
			session = PersistenceManager.getSession();
			qryStr = "select cn from " + Country.class.getName() + " cn where cn.iso = '"+ ISO +"'" ;
			qry = session.createQuery(qryStr);
			col=qry.list();
			
			}
			catch (Exception ex) {
				logger.error("Exception : " + ex.getMessage());
				ex.printStackTrace(System.out);
				if (tx != null) {
					try {
						tx.rollback();
					} catch (Exception rbf) {
						logger.error("Rollback failed !");
					}
				}
			} finally {
				if (session != null) {
					try {
						PersistenceManager.releaseSession(session);
					} catch (Exception rsf) {
						logger.error("Release session failed :" + rsf.getMessage());
					}
				}
			}
		return col;
	}
	public static void deleteFlag(Long cntryId)
	{
		logger.info(" deleting the flag");
		Session session = null;
		Transaction tx = null;
		try
		{
			session = PersistenceManager.getSession();
			AmpSiteFlag flag = (AmpSiteFlag) session.load(
					AmpSiteFlag.class,cntryId);
			tx = session.beginTransaction();
			session.delete(flag);
			tx.commit();
		}
		catch (Exception e)
		{
			logger.error("Exception from deleteFlag() :" + e.getMessage());
			e.printStackTrace(System.out);
			if (tx != null)
			{
				try
				{
					tx.rollback();
				}
				catch (Exception trbf)
				{
					logger.error("Transaction roll back failed ");
					e.printStackTrace(System.out);
				}
			}
		}
		finally
		{
			if (session != null)
			{
				try
				{
					PersistenceManager.releaseSession(session);
				}
				catch (Exception rsf)
				{
					logger.error("Failed to release session :" + rsf.getMessage());
				}
			}
		}
	}

}